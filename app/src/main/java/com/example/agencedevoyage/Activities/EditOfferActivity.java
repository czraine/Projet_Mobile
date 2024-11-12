package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.Entity.Offer;
import com.example.agencedevoyage.R;

public class EditOfferActivity extends AppCompatActivity {

    private EditText editOfferTitle, editOfferDescription, editOfferDestination, editOfferPrice;
    private Button selectNewImageButton, saveOfferButton;
    private ImageView selectedNewImageView;
    private Uri imageUri;
    private Offer offer;

    private static final int IMAGE_PICK_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_offer_activity);

        // Initialize views
        editOfferTitle = findViewById(R.id.editOfferTitle);
        editOfferDescription = findViewById(R.id.editOfferDescription);
        editOfferDestination = findViewById(R.id.editOfferDestination);
        editOfferPrice = findViewById(R.id.editOfferPrice);
        selectNewImageButton = findViewById(R.id.selectNewImageButton);
        selectedNewImageView = findViewById(R.id.selectedNewImageView);
        saveOfferButton = findViewById(R.id.saveOfferButton);

        // Retrieve offer from intent
        if (getIntent() != null && getIntent().hasExtra("offer")) {
            offer = (Offer) getIntent().getSerializableExtra("offer");
        }

        // Populate fields with offer data
        if (offer != null) {
            editOfferTitle.setText(offer.getTitle());
            editOfferDescription.setText(offer.getDescription());
            editOfferDestination.setText(offer.getDestination());
            editOfferPrice.setText(String.valueOf(offer.getPrice()));

            if (offer.getImageUri() != null && !offer.getImageUri().isEmpty()) {
                selectedNewImageView.setImageURI(Uri.parse(offer.getImageUri()));
            }
        }

        // Select a new image
        selectNewImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        // Save updated offer details
        saveOfferButton.setOnClickListener(v -> {
            if (isInputValid()) {
                // Update fields in the offer object
                offer.setTitle(editOfferTitle.getText().toString());
                offer.setDescription(editOfferDescription.getText().toString());
                offer.setDestination(editOfferDestination.getText().toString());

                // Parse price safely
                try {
                    offer.setPrice(Double.parseDouble(editOfferPrice.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update image URI if a new one is selected
                if (imageUri != null) {
                    offer.setImageUri(imageUri.toString());
                }

                // Send updated offer back to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedOffer", offer);  // Pass the updated offer
                setResult(RESULT_OK, resultIntent);
                finish(); // Close the activity and return to the previous screen
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ensure all fields are filled
    private boolean isInputValid() {
        return !editOfferTitle.getText().toString().isEmpty() &&
                !editOfferDescription.getText().toString().isEmpty() &&
                !editOfferDestination.getText().toString().isEmpty() &&
                !editOfferPrice.getText().toString().isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            imageUri = data.getData();
            selectedNewImageView.setImageURI(imageUri);  // Update image preview
        }
    }
}
