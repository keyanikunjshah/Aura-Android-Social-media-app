package com.Keya.aura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    private Context context;
   private List<User>userList;

    public UserAdapter(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
        this.userList=new ArrayList<>();
    }



    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

        Glide.with(context)
                .load(model.getProfileImageUrl())
                .into(holder.profileImageView);


        Glide.with(context)
                .load(model.getProfileImageUrl())
                .into(holder.postImage);


        holder.caption.setText(model.getUsername());
        holder.username.setText(model.getUsername());
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_format, parent, false);
        return new UserViewHolder(view);
    }
    public  void addPost(User user) {
        userList.add(user); // Add the new user to the list
        notifyItemInserted(userList.size() - 1);
        // Notify the adapter of the new item
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        ImageView postImage;
        TextView username;
        TextView caption;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.pfp);
            postImage=itemView.findViewById(R.id.post);
            username=itemView.findViewById(R.id.post_username);
            caption=itemView.findViewById(R.id.caption);


        }
    }
}