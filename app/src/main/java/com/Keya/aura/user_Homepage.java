package com.Keya.aura;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Keya.aura.R;
import com.Keya.aura.User;
import com.Keya.aura.UserAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser ;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_Homepage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private DatabaseReference usersReference;
    private List<String> followingList;
    Button newpost;
    private DatabaseReference postsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);

        recyclerView = findViewById(R.id.posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        followingList = new ArrayList<>();



        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        postsReference = FirebaseDatabase.getInstance().getReference().child("posts");

        boolean newPostUploaded = getIntent().getBooleanExtra("newPostUploaded", false);
        if (newPostUploaded) {
            fetchFollowingUsers();
        }
        loadFollowingList();
        newpost=findViewById(R.id.button_new_post);
        newpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newpost=new Intent(user_Homepage.this,CreateNewPost.class);
                startActivity(newpost);

            }
        });







    }



    private void loadFollowingList() {

        FirebaseUser  currentUser  = FirebaseAuth.getInstance().getCurrentUser ();
        if (currentUser  != null) {
            String currentUserId = currentUser .getUid();

            DatabaseReference followingReference = usersReference.child(currentUserId).child("Following");
            followingReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    followingList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String followedUserId = dataSnapshot.getKey();
                        followingList.add(followedUserId);
                    }
                    fetchFollowingUsers();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("user_Homepage", "Error loading following list", error.toException());
                }
            });
        } else {
            Log.e("user_Homepage", "No authenticated user found.");
        }
    }

    private void fetchFollowingUsers() {
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(usersReference.orderByKey().startAt(followingList.get(0)).endAt(followingList.get(followingList.size() - 1)), User.class)
                .build();

        userAdapter = new UserAdapter(options, this);
        recyclerView.setAdapter(userAdapter);
        userAdapter.startListening();
        
        
        
        
        
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (userAdapter != null) {
            userAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userAdapter != null) {
            userAdapter.stopListening();
        }
    }
}