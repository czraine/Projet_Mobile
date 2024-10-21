package com.example.agencedevoyage.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.agencedevoyage.R;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.button.MaterialButton;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PostAddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Code pour identifier l'intention de sélection d'image
    private ImageView imageViewBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_add);
        // Setup settings button click listener to redirect to DisplayItemPostActivity
        ImageView settingsButton = findViewById(R.id.nv_settings);
        settingsButton.setOnClickListener(view -> {
            Toast.makeText(PostAddActivity.this, "Navigating to Display Item Post...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PostAddActivity.this, DisplayPostsActivity.class); // Ensure DisplayPostsActivity is the correct class
            startActivity(intent);
        });
        // Applique des marges pour les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Référencer le bouton de retour
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Termine l'activité actuelle et revient à l'activité précédente
            }
        });

        // Récupération de l'ImageView pour la sélection d'image
        imageViewBlog = findViewById(R.id.imageViewBlog);

        // Ajout du listener pour ouvrir la galerie d'images
        imageViewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir la galerie d'images
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    // Gère le résultat de la sélection d'image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();  // Récupère l'URI de l'image sélectionnée
            imageViewBlog.setImageURI(imageUri);  // Met à jour l'ImageView avec l'image sélectionnée
        }
    }
}
