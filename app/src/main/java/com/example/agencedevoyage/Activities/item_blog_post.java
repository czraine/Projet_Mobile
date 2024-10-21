package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.agencedevoyage.R;

public class item_blog_post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_blog_post);

        // Configure Window Insets for immersive display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED; // Consume the insets
        });

        // Handle click to navigate to CommentsActivity
        ImageButton commentButton = findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CommentsActivity
                Intent intent = new Intent(item_blog_post.this, AddCommentActivity.class);
                startActivity(intent);
            }
        });

        // Handle click on the image to show post details
        ImageView postImage = findViewById(R.id.post_image);
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start PostDetailsActivity
                Intent intent = new Intent(item_blog_post.this, PostDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}