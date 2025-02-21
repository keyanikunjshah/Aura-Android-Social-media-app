package com.Keya.aura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Follow_List extends AppCompatActivity {
FollowAdapter adapter;
    RecyclerView followlist;
    Button Proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Proceed=findViewById(R.id.Proceed);
        followlist=findViewById(R.id.Followlist);
        followlist.setLayoutManager(new LinearLayoutManager(this));


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser != null ? currentUser.getUid() : null;



        FirebaseRecyclerOptions<FollowClass> options =
                new FirebaseRecyclerOptions.Builder<FollowClass>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Users")
                                .orderByKey(), FollowClass.class)
                        .build();

        adapter=new FollowAdapter(options, currentUserId);
        followlist.setAdapter(adapter);

        Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userHomepage=new Intent(Follow_List.this, user_Homepage.class);
                startActivity(userHomepage);


            }
        });








    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}