package com.example.agencedevoyage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        holder.userName.setText(post.getUSERNAME());  // Set the username
        holder.description.setText(post.getDescription());  // Set the description
        holder.date.setText(post.getDate());  // Set the post date

        // Load the image into the ImageView using Glide
        String imageUrl = post.getImageUrl();  // Ensure your Post class has this method
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)  // Load the image URL or path
                    .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                    .error(R.drawable.error)  // Optional: image if load fails
                    .into(holder.postImage);  // Set the image into the ImageView
        } else {
            holder.postImage.setImageResource(R.drawable.error);  // Fallback if image URL is empty
        }

        // Set click listener for the entire item to open PostDetailsActivity with POST_ID
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("POST_ID", post.getId());  // Pass POST_ID to PostDetailsActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();  // Return the total number of posts
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView userName, description, date;
        ImageView postImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.post_user_name);
            description = itemView.findViewById(R.id.post_description);
            date = itemView.findViewById(R.id.post_date_time);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}
