package com.Keya.aura;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    EditText R_email;
    EditText R_password;
    EditText R_username;
    Button R_googleB;
    Button R_facebookB;
    Button R_signup;
   FirebaseAuth auth;
   FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();


        R_email=findViewById(R.id.Register_email);
        R_password=findViewById(R.id.Register_password);
        R_username=findViewById(R.id.Register_username);
        R_signup=findViewById(R.id.sign);


         String email=R_email.getText().toString();
         String pass=R_password.getText().toString();
         String username=R_username.getText().toString();

         R_signup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String email=R_email.getText().toString();
                 String pass=R_password.getText().toString();
                 String username=R_username.getText().toString();

                 if(TextUtils.isEmpty(email)){
                     R_email.setError("Enter your email-id");
                     return;


                 }
                 else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                     R_email.setError("Invalid email address");
                     return;

                 }
                 else{
                     R_email.setError(null);
                 }

                 if(TextUtils.isEmpty(pass)){
                     R_password.setError("Enter password");
                     return;
                 } else if (pass.length()<6) {
                     R_password.setError("Password too short");
                     return;

                 }
                 else {
                     R_password.setError(null);

                 }
                 registerUser(email,pass,username);


             }
         });


















   }

    private void registerUser(String email,String password,String username ) {

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map =new HashMap<>();
                map.put("username",username);
                map.put("email",email);
                map.put("password",password);
                map.put("id",auth.getCurrentUser().getUid());
                map.put("Followers","");
                map.put("Following","");
                map.put("ProfileImageUrl","");

                database=FirebaseDatabase.getInstance();
                database.getReference().child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            createUserProfile(auth.getCurrentUser().getUid());
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent createProfile = new Intent(RegisterActivity.this, com.Keya.aura.CreateProfile.class);
                            createProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(createProfile);
                            finish();



                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });







            }
        });


    }
    private void createUserProfile(String username) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(username)
                .child("isFirstLogin")
                .setValue(true);
    }


}