package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.PostAdapter;
import com.example.agencedevoyage.Domains.Post;
import com.example.agencedevoyage.R;



import java.util.ArrayList;

public class DisplayPostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_posts);
        // Setup settings button click listener to redirect to DisplayItemPostActivity
        ImageView settingsButton = findViewById(R.id.nv_settings);
        settingsButton.setOnClickListener(view -> {
            Toast.makeText(DisplayPostsActivity.this, "Navigating to Display Item Post...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DisplayPostsActivity.this, DisplayPostsActivity.class); // Ensure DisplayPostsActivity is the correct class
            startActivity(intent);
        });
        // Configure Window Insets for immersive display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;  // Consume the insets
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_posts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your list of posts
        posts = new ArrayList<>();
        // Example: Add some dummy data
        posts.add(new Post("User 1", "This is the first post.", "2024-10-17"));
        posts.add(new Post("User 2", "This is the second post.", "2024-10-18"));
        posts.add(new Post("User 3", "This is the 99 post.", "2024-10-17"));
        posts.add(new Post("User 4", "lately.", "2024-10-18"));

        // Set the adapter, passing the context
        postAdapter = new PostAdapter(posts, this); // Pass the context to the adapter
        recyclerView.setAdapter(postAdapter);

        // Add Post ImageView setup for navigation to add a new post
        ImageView navPostsButton = findViewById(R.id.nav_posts2);  // Ensure this matches your XML ID
        navPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayPostsActivity.this, PostAddActivity.class);
                startActivity(intent);
            }
        });
    }
}