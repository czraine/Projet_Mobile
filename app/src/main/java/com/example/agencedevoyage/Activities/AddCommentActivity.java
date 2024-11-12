package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.Dao.DatabaseHelper;
import com.example.agencedevoyage.Domains.Post;
import com.example.agencedevoyage.R;

import java.util.List;

public class AddCommentActivity extends AppCompatActivity {

    private EditText commentEditText;
    private String postId;
    private DatabaseHelper databaseHelper;
    private LinearLayout commentsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        commentEditText = findViewById(R.id.editTextComment);
        commentsContainer = findViewById(R.id.commentsContainer);
        databaseHelper = new DatabaseHelper(this);

        postId = getIntent().getStringExtra("POST_ID");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> returnToPostDetails());

        findViewById(R.id.buttonSubmitComment).setOnClickListener(v -> submitComment());

        loadComments();
    }

    private void loadComments() {
        Post post = databaseHelper.getPostById(Integer.parseInt(postId));
        if (post != null) {
            List<String> comments = post.getComments();
            if (comments != null && !comments.isEmpty()) {
                for (String comment : comments) {
                    Log.d("AddCommentActivity", "Comment: " + comment); // Add debugging
                    View commentView = LayoutInflater.from(this).inflate(R.layout.comment_item, commentsContainer, false);
                    TextView commentText = commentView.findViewById(R.id.comment_text);
                    commentText.setText(comment);
                    commentsContainer.addView(commentView);
                }
            } else {
                Toast.makeText(this, "No comments available.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Post not found.", Toast.LENGTH_SHORT).show();
        }
    }


    private void returnToPostDetails() {
        Intent intent = new Intent(AddCommentActivity.this, PostDetailsActivity.class);
        intent.putExtra("POST_ID", postId);
        startActivity(intent);
        finish();
    }

    private void submitComment() {
        String comment = commentEditText.getText().toString().trim();

        if (comment.isEmpty()) {
            Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        Post post = databaseHelper.getPostById(Integer.parseInt(postId));
        if (post != null) {
            post.addComment(comment);
            databaseHelper.updatePost(post); // Save the post with the new comment

            Toast.makeText(this, "Comment submitted!", Toast.LENGTH_SHORT).show();
            loadComments(); // Reload the comments after submitting the new one
            commentEditText.setText(""); // Clear the input field
        } else {
            Toast.makeText(this, "Post not found.", Toast.LENGTH_SHORT).show();
        }
    }
}
