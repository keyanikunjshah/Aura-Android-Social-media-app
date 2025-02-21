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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateNewPost extends AppCompatActivity {


    private boolean isFollowingView = false;

    ImageView postImage;
    Button cameraBtn;
    Button galleryBtn;
    Button doneBtn;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    FirebaseRecyclerOptions<User> options;
    String imageUrl;
    String username;
    String ProfileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "auraapp");
        config.put("api_key", "166358472174135");
        config.put("api_secret", "EDntHZhQYu7SB_YXMLQiinbS3fU");
        MediaManager.init(this, config);

        setContentView(R.layout.activity_create_new_post);

        postImage = findViewById(R.id.post_img);
        cameraBtn = findViewById(R.id.post_cameraBtn);
        galleryBtn = findViewById(R.id.post_gallery);
        doneBtn = findViewById(R.id.post_done);














        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bitmap img = (Bitmap) (data.getExtras().get("data"));
                        postImage.setImageBitmap(img);
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
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            postImage.setImageURI(selectedImage);
                            uploadImageToCloudinary(selectedImage);
                        }
                    }
                }
        );


        cameraBtn.setOnClickListener(view -> {
            Intent cameraOpen = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraOpen);
        });


        galleryBtn.setOnClickListener(view -> {
            Intent galleryOpen = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(galleryOpen);
        });

        doneBtn.setOnClickListener(view -> {

            Intent intent = new Intent(CreateNewPost.this, user_Homepage.class);
            intent.putExtra("newPostUploaded", true);
            startActivity(intent);
            finish();
        });
    }

    private Uri getImageUriFromBitmap(Context context, Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "PostImage_" + System.currentTimeMillis() + ".jpg");
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

        MediaManager.get().upload(imageUri).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                int progress = (int) (bytes * 100 / totalBytes);
                progressDialog.setProgress(progress);
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                progressDialog.dismiss();
                 imageUrl = (String) resultData.get("secure_url");
                String userId = FirebaseAuth.getInstance().getCurrentUser ().getUid();
                DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("username");
                DatabaseReference ProfilepicRef=FirebaseDatabase.getInstance().getReference("Users").child(userId).child("ProfileImageUrl");

                usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                             username = dataSnapshot.getValue(String.class);

                        } else {
                            Log.d("Username", "Username not found.");
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e("Username", "Failed to retrieve username", databaseError.toException());
                    }
                });

                ProfilepicRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ProfileImageUrl=snapshot.getValue(String.class);
                        }else{
                            Log.d("ProfileImageUrl", "Profilepic not found.");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("ProfileImageUrl", "Failed to retrieve profilpic", error.toException());

                    }
                });


                User newPost = new User(userId, username,  ProfileImageUrl,imageUrl );

                // Save the post to Firebase
                savePostToFirebase(newPost);













            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                progressDialog.dismiss();
                Toast.makeText(CreateNewPost.this, "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }
    private void savePostToFirebase(User newPost) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("posts");
        String postId = postsRef.push().getKey();

        if (postId != null) {

            postsRef.child(postId).setValue(newPost)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateNewPost.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CreateNewPost.this, "Failed to upload post", Toast.LENGTH_SHORT).show();
                        }
                    });







        }
    }



}