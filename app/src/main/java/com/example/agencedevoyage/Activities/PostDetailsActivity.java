package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.documentfile.provider.DocumentFile;

import com.bumptech.glide.Glide;
import com.example.agencedevoyage.GlideApp;
import com.example.agencedevoyage.R;
import com.example.agencedevoyage.Dao.DatabaseHelper;
import com.example.agencedevoyage.Domains.Post;

import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private TextView userNameTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private ImageView postImageView;
    private LinearLayout commentsLayout;
    private DatabaseHelper databaseHelper;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // Configure les insets pour un affichage immersif
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Référence aux composants UI
        userNameTextView = findViewById(R.id.user_name_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        dateTextView = findViewById(R.id.date_text_view);
        postImageView = findViewById(R.id.blog_image);
        commentsLayout = findViewById(R.id.comments_layout); // Layout pour afficher les commentaires

        // Charger les détails du post
        loadPostDetails();

        // Gérer le bouton d'ajout d'image
        postImageView.setOnClickListener(v -> openImagePicker());

        // Gérer le bouton pour ajouter un commentaire
        findViewById(R.id.comment_button).setOnClickListener(v -> {
            Intent intent = new Intent(PostDetailsActivity.this, AddCommentActivity.class);
            intent.putExtra("POST_ID", post.getId());  // Pass post ID as Integer
            startActivity(intent);
        });

        // Gérer les autres boutons si nécessaire (exemple: bouton de paramètres)
        ImageView settingsButton = findViewById(R.id.nv_settings);
        settingsButton.setOnClickListener(view -> {
            Toast.makeText(PostDetailsActivity.this, "Navigating to Display Item Post...", Toast.LENGTH_SHORT).show();
            Intent intentSettings = new Intent(PostDetailsActivity.this, DisplayPostsActivity.class);
            startActivity(intentSettings);
        });

        // Bouton retour
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    // Méthode pour ouvrir le sélecteur d'image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Prendre la permission persistante pour cet URI
                    getContentResolver().takePersistableUriPermission(
                            selectedImageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                    Log.d("PostDetailsActivity", "Permission persistante ajoutée pour URI: " + selectedImageUri);
                    loadImage(selectedImageUri); // Charger l'image
                } catch (SecurityException e) {
                    Log.e("PostDetailsActivity", "Impossible de prendre la permission persistante", e);
                    Toast.makeText(this, "Impossible de charger l'image en raison de permissions.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Méthode pour charger les détails du post
    private void loadPostDetails() {
        // Récupération et configuration des informations du post
        int postId = getIntent().getIntExtra("POST_ID", -1);  // Ensure you pass an Integer
        post = databaseHelper.getPostById(postId);

        if (post != null) {
            userNameTextView.setText(post.getUSERNAME());
            descriptionTextView.setText(post.getDescription());
            dateTextView.setText(post.getDate());
            if (post.getImageUrl() != null) {
                loadImage(Uri.parse(post.getImageUrl()));
            }

            // Afficher les commentaires associés au post
            displayComments(post.getComments());
        } else {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Méthode pour charger une image dans l'ImageView avec Glide
    private void loadImage(Uri imageUri) {
        if (imageUri != null) {
            DocumentFile documentFile = DocumentFile.fromSingleUri(this, imageUri);
            if (documentFile != null && documentFile.canRead()) {
                GlideApp.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.placeholder) // Affiche une image de chargement
                        .error(R.drawable.error) // Affiche une image d'erreur si le chargement échoue
                        .into(postImageView);
            } else {
                Toast.makeText(this, "Impossible de lire l'image. Vérifiez les permissions.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Méthode pour afficher les commentaires dans le layout
    private void displayComments(List<String> comments) {
        commentsLayout.removeAllViews(); // Nettoyer les commentaires précédents
        if (comments != null && !comments.isEmpty()) {
            for (String comment : comments) {
                TextView commentTextView = new TextView(this);
                commentTextView.setText(comment);
                commentTextView.setTextSize(16);
                commentTextView.setPadding(16, 16, 16, 16);
                commentsLayout.addView(commentTextView); // Ajouter chaque commentaire au layout
            }
        } else {
            TextView noCommentsTextView = new TextView(this);
            noCommentsTextView.setText("No comments yet.");
            noCommentsTextView.setTextSize(14);
            noCommentsTextView.setPadding(16, 16, 16, 16);
            commentsLayout.addView(noCommentsTextView);
        }
    }
}
