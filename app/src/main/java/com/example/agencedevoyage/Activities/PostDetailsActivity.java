package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.agencedevoyage.R;


public class PostDetailsActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_details);
        // Setup settings button click listener to redirect to DisplayItemPostActivity
        ImageView settingsButton = findViewById(R.id.nv_settings);
        settingsButton.setOnClickListener(view -> {
            Toast.makeText(PostDetailsActivity.this, "Navigating to Display Item Post...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PostDetailsActivity.this, DisplayPostsActivity.class); // Ensure DisplayPostsActivity is the correct class
            startActivity(intent);
        });
        // Configure Window Insets for immersive display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;  // Consume the insets
        });

        // Reference the UI elements
        userNameTextView = findViewById(R.id.user_name_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        dateTextView = findViewById(R.id.date_text_view);

        // Get data from Intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra("postUserName");
        String description = intent.getStringExtra("postDescription");
        String date = intent.getStringExtra("postDate");

        // Set the data to TextViews
        userNameTextView.setText(userName);
        descriptionTextView.setText(description);
        dateTextView.setText(date);

        // Reference the back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish()); // Close the current activity

        // Reference the comment image view
        ImageView commentImageView = findViewById(R.id.comment_button);
        commentImageView.setOnClickListener(v -> {
            // Launch AddCommentActivity
            Intent commentIntent = new Intent(PostDetailsActivity.this, AddCommentActivity.class);
            commentIntent.putExtra("postUserName", userName);
            commentIntent.putExtra("postDescription", description);
            startActivity(commentIntent); // Start AddCommentActivity
        });
    }
}