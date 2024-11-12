package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.PostAdapter;
import com.example.agencedevoyage.Domains.Post;
import com.example.agencedevoyage.R;
import com.example.agencedevoyage.Dao.postDao;

import java.io.File;
import java.util.ArrayList;

public class DisplayPostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_posts);

        // Setup settings button click listener
        ImageView settingsButton = findViewById(R.id.nv_settings);
        settingsButton.setOnClickListener(view -> {
            Toast.makeText(DisplayPostsActivity.this, "Navigating to Display Item Post...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DisplayPostsActivity.this, DisplayPostsActivity.class);
            startActivity(intent);
        });

        // Configure Window Insets for immersive display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load posts from the database
        loadPostsFromDatabase();

        // Set the adapter with loaded posts
        postAdapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(postAdapter);

        // Set up navigation button to add a new post
        ImageView navPostsButton = findViewById(R.id.nav_posts2);
        navPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayPostsActivity.this, PostAddActivity.class);
            startActivity(intent);
        });
    }

    private void loadPostsFromDatabase() {
        postDao postDao = new postDao(this);
        postDao.open(); // Open the database connection

        // Fetch all posts from the database
        posts = postDao.getAllPosts();

        postDao.close(); // Close the database connection
    }
}
