package com.Keya.aura;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button login;
        TextView Signup;
        EditText L_email;
        EditText L_password;
        auth = FirebaseAuth.getInstance();

        Signup = findViewById(R.id.signup);
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        login = findViewById(R.id.login);
        L_email = findViewById(R.id.login_email);
        L_password = findViewById(R.id.login_password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = L_email.getText().toString();
                String password = L_password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    L_email.setError("Enter your email");
                } else if (TextUtils.isEmpty(password)) {
                    L_password.setError("Enter password");


                } else {
                    loginUser(email, password);
                }


            }


        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(register);

            }
        });


    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                userRef.child("isFirstLogin").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Boolean isFirstLogin = task.getResult().getValue(Boolean.class);
                        if (Boolean.TRUE.equals(isFirstLogin)) {
                            Intent followListIntent = new Intent(LoginActivity.this, Follow_List.class);
                            startActivity(followListIntent);
                        } else {
                            startActivity(new Intent(LoginActivity.this, user_Homepage.class));
                        }
                        finish();
                    }
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

