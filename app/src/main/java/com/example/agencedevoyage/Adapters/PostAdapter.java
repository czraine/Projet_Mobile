package com.example.agencedevoyage.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Activities.PostDetailsActivity;
import com.example.agencedevoyage.Domains.Post;
import com.example.agencedevoyage.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<Post> posts;
    private Context context;

    public PostAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_blog_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.userName.setText(post.getUserName());
        holder.description.setText(post.getDescription());
        holder.date.setText(post.getDate());

        // Set click listener for the post image
        holder.postImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("postUserName", post.getUserName());
            intent.putExtra("postDescription", post.getDescription());
            intent.putExtra("postDate", post.getDate());
            // Start PostDetailsActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userName, description, date;
        ImageView postImage; // Reference to the ImageView

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.post_user_name);
            description = itemView.findViewById(R.id.post_description);
            date = itemView.findViewById(R.id.post_date_time);
            postImage = itemView.findViewById(R.id.post_image); // Initialize the post image
        }
    }
}
