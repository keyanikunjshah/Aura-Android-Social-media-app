package com.Keya.aura;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;


public class CreateProfile extends AppCompatActivity {

    ImageView Profilepic;
    Button camera;
    Button gallery;
    Button done;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private UserAdapter userAdapter;
    FirebaseRecyclerOptions<User> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Users"), User.class)
                .build();

        userAdapter = new UserAdapter(options, this);

        setContentView(R.layout.activity_create_profile);
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "auraapp");
        MediaManager.init(this, config);


        Profilepic = findViewById(R.id.pfpimg);
        camera = findViewById(R.id.cameraBtn);
        gallery = findViewById(R.id.gallery);
        done=findViewById(R.id.done);




        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap img = (Bitmap) (data.getExtras().get("data"));
                        Profilepic.setImageBitmap(img);

                        Uri imageUri = getImageUriFromBitmap(this, img);
                        if (imageUri != null) {
                            uploadImageToCloudinary(imageUri);
                        }
                    }
                }
        );


        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            Profilepic.setImageURI(selectedImage);
                            uploadImageToCloudinary(selectedImage);
                        }
                    }
                }
        );


        camera.setOnClickListener(view -> {
            Intent cameraOpen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraOpen);
        });


        gallery.setOnClickListener(view -> {
            Intent galleryOpen = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(galleryOpen);
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent Followlist=new Intent(CreateProfile.this, Follow_List.class);
                 startActivity(Followlist);

            }
        });


    }

    private Uri getImageUriFromBitmap(Context context, Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "ProfilePic_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AuraApp");

        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try (OutputStream out = context.getContentResolver().openOutputStream(uri)) {
            if (out != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
            }
        } catch (IOException e) {
            Log.e("ImageUri", "Error saving image", e);
            return null;
        }

        return uri;
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please wait while the image is being uploaded...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.show();

        MediaManager.get().upload(imageUri)
                .option("resource_type", "image")
                .unsigned("iddv5qmi")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("CloudinaryUpload", "Upload started: " + requestId);
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        int progress = (int) ((bytes * 100) / totalBytes);
                        progressDialog.setProgress(progress);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        progressDialog.dismiss();
                        String imageUrl = (String) resultData.get("secure_url");

                        saveImageUrlToFirebase(imageUrl);
                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        progressDialog.dismiss();
                        Log.e("CloudinaryUpload", "Upload error: " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        Log.d("CloudinaryUpload", "Upload rescheduled: " + error.getDescription());
                    }
                })
                .dispatch();
    }

    private void saveImageUrlToFirebase(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)
                .child("ProfileImageUrl")
                .setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateProfile.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateProfile.this, "Failed to save profile picture", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
