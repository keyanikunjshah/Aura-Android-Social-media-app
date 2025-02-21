package com.Keya.aura;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowAdapter extends FirebaseRecyclerAdapter<FollowClass,FollowAdapter.ViewHolder> {


    private String currentUserId;
    public FollowAdapter(@NonNull FirebaseRecyclerOptions<FollowClass> options,String currentUserId){

        super(options);
        this.currentUserId = currentUserId;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.followlistlayout,parent,false);
         ViewHolder viewHolder =new ViewHolder(view);
        return  viewHolder;

    }



    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FollowClass model) {
        holder.user_name.setText(model.getUsername());
        String imageUrl = model.getProfileImageUrl();
        Log.d("FollowAdapter", "Profile Image URL: " + imageUrl);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.image.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.ic_launcher_background)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GlideError", "Image load failed", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }


                    })
                    .into(holder.image);

        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }
        DatabaseReference followRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(currentUserId)
                .child("Following")
                .child(model.getId());

        followRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // User is already following
                holder.followbtn.setText("Following");
                holder.followbtn.setEnabled(false);
            } else {

                holder.followbtn.setText("Follow");
                holder.followbtn.setEnabled(true);

                // Follow button click listener
                holder.followbtn.setOnClickListener(v -> {
                    holder.followbtn.setEnabled(false);
                    holder.followbtn.setText("Following");


                    followUser(model.getId(), holder.followbtn);
                });
            }
        });

        holder.followbtn.setOnClickListener(v -> {
            // Toggle button state immediately
            holder.followbtn.setText("Following");
            holder.followbtn.setEnabled(false);




            followUser(model.getId(), holder.followbtn);
        });














    }

    private void followUser(String targetUserId, Button followbtn) {
        if (currentUserId != null) {

            DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
            DatabaseReference targetUserRef = FirebaseDatabase.getInstance().getReference("Users").child(targetUserId);

            currentUserRef.child("Following").child(targetUserId).setValue(true);

            targetUserRef.child("Followers").child(currentUserId).setValue(true);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView user_name;
        Button followbtn;
        public ViewHolder(View itemView){
            super(itemView);
            image=itemView.findViewById(R.id.pfpfollowlist);
            user_name=itemView.findViewById(R.id.username_followlist);
            followbtn=itemView.findViewById(R.id.followbtn);



        }

    }


}
