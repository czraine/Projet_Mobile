package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.agencedevoyage.Dao.DatabaseHelper;
import com.example.agencedevoyage.Domains.Post;
import com.example.agencedevoyage.GlideApp;
import com.example.agencedevoyage.R;

public class item_blog_post extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_blog_post);

        databaseHelper = new DatabaseHelper(this);

        // Retrieve the Post ID passed from the previous activity
        int postId = getIntent().getIntExtra("POST_ID", -1);

        // Fetch post details from database
        post = databaseHelper.getPostById(postId);

        // Check if post is found
        if (post == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up UI components
        loadPostDetails();
        loadImage();
        setupCommentButton();
    }

    private void loadPostDetails() {
        // Accessing the static USERNAME directly from Post class
        TextView userNameTextView = findViewById(R.id.post_user_name);
        TextView descriptionTextView = findViewById(R.id.post_description);
        TextView dateTextView = findViewById(R.id.post_date_time);

        userNameTextView.setText(post.getUSERNAME()); // Display the static username
        descriptionTextView.setText(post.getDescription());
        dateTextView.setText(post.getDate());
    }

    private void loadImage() {
        ImageView postImageView = findViewById(R.id.post_image);

        String imageUrl = post.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            GlideApp.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder) // Displayed while the image loads
                    .error(R.drawable.error) // Displayed if the image fails to load
                    .into(postImageView);
        } else {
            postImageView.setImageResource(R.drawable.error); // Fallback if URL is null or empty
        }
    }


    private void setupCommentButton() {
        ImageButton commentButton = findViewById(R.id.comment_button);
        commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(item_blog_post.this, AddCommentActivity.class);
            startActivity(intent);
        });
    }
}
