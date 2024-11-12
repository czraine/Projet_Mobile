package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.agencedevoyage.Dao.postDao;
import com.example.agencedevoyage.Domains.Post;
import com.example.agencedevoyage.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PostAddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Code for identifying the image selection intent
    private ImageView imageViewBlog;
    private TextInputLayout textInputLayoutContent;
    private TextInputEditText editTextContent;
    private MaterialButton addPostButton;
    private Uri imageUri;  // Variable to store the selected image URI

    private postDao postDaoInstance; // Instance for handling post database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_add);

        // Initialize UI components and database DAO
        initializeUIComponents();
        initializeDatabase();

        // Setup click listeners
        setupListeners();
    }

    private void initializeUIComponents() {
        imageViewBlog = findViewById(R.id.imageViewBlog);
        textInputLayoutContent = findViewById(R.id.textInputLayoutContent);
        editTextContent = findViewById(R.id.add_content);
        addPostButton = findViewById(R.id.AddPost);

        // Apply margins for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeDatabase() {
        postDaoInstance = new postDao(this);
        postDaoInstance.open();
    }

    private void setupListeners() {
        // Settings button to redirect to DisplayPostsActivity
        ImageView settingsButton = findViewById(R.id.nv_settings);
        settingsButton.setOnClickListener(view -> {
            Toast.makeText(PostAddActivity.this, "Navigating to Display Item Post...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PostAddActivity.this, DisplayPostsActivity.class);
            startActivity(intent);
        });

        // Back button to finish the activity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Image view click listener to open the gallery
        imageViewBlog.setOnClickListener(v -> openImageGallery());

        // Add post button click listener
        addPostButton.setOnClickListener(v -> validateInput());
    }

    private void openImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void validateInput() {
        String content = editTextContent.getText().toString().trim();

        // Reset errors
        textInputLayoutContent.setError(null);

        if (imageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;  // Stop execution if no image is uploaded
        }

        if (content.isEmpty()) {
            textInputLayoutContent.setError("Content cannot be empty");
            editTextContent.requestFocus();
        } else {
            // Save the post data to the database
            savePost(content, imageUri.toString());
        }
    }

    private void savePost(String content, String imageUri) {
        // Create a Post object
        Post post = new Post();
        post.setDescription(content);
        post.setImageUrl(imageUri);

        // Insert post into the database
        long result = postDaoInstance.addPost(post);

        if (result != -1) {
            Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
            // Optionally: Clear fields or navigate to another activity
            clearFields();

            // Redirect to the DisplayPostsActivity
            Intent intent = new Intent(PostAddActivity.this, DisplayPostsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to submit post. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextContent.setText("");  // Clear the content field
        imageViewBlog.setImageResource(R.drawable.jk_placeholder_image);  // Reset the image to placeholder
        imageUri = null;  // Reset the image URI
    }

    // Handle the result of the image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();  // Store the URI of the selected image
            imageViewBlog.setImageURI(imageUri);  // Update the ImageView with the selected image
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postDaoInstance.close(); // Close database connection when activity is destroyed
    }
}
