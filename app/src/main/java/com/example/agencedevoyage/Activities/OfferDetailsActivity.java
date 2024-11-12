package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.agencedevoyage.Database.AppDatabase_firas;
import com.example.agencedevoyage.Entity.Offer;
import com.example.agencedevoyage.R;
import android.net.Uri;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;

public class OfferDetailsActivity extends AppCompatActivity {

    private ImageView offerImageView;
    private TextView titleTextView, descriptionTextView, destinationTextView, priceTextView, ratingTextView, reviewsTextView;
    private Offer selectedOffer;
    private AppDatabase_firas database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        // Initialize views
        offerImageView = findViewById(R.id.offerImageView);
        titleTextView = findViewById(R.id.offerTitleTextView);
        descriptionTextView = findViewById(R.id.offerDescriptionTextView);
        destinationTextView = findViewById(R.id.offerDestinationTextView);
        priceTextView = findViewById(R.id.offerPriceTextView);
        ratingTextView = findViewById(R.id.offerRatingTextView);
        reviewsTextView = findViewById(R.id.reviewsTextView);
        Button addReviewButton = findViewById(R.id.addReviewButton);

        // Get the Offer data from the intent
        selectedOffer = (Offer) getIntent().getSerializableExtra("selectedOffer");
        database = AppDatabase_firas.getInstance(this);

        // Set the offer details in the views
        if (selectedOffer != null) {
            titleTextView.setText(selectedOffer.getTitle());
            descriptionTextView.setText(selectedOffer.getDescription());
            destinationTextView.setText(selectedOffer.getDestination());
            priceTextView.setText(String.format("$%.2f", selectedOffer.getPrice()));

            // Load image from URI or use default
            if (selectedOffer.getImageUri() != null && !selectedOffer.getImageUri().isEmpty()) {
                Glide.with(this)
                        .load(Uri.parse(selectedOffer.getImageUri()))
                        .placeholder(R.drawable.sample_image)  // Default placeholder
                        .error(R.drawable.sample_image)       // Error placeholder
                        .into(offerImageView);
            } else {
                offerImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sample_image));
            }

            updateRatingAndReviews();
        }

        // Listener for adding a review
        addReviewButton.setOnClickListener(v -> showAddReviewDialog());
    }

    private void updateRatingAndReviews() {
        ratingTextView.setText(String.format("Rating: %.1f (%d reviews)", selectedOffer.getAverageRating(), selectedOffer.getReviewCount()));
        reviewsTextView.setText(selectedOffer.getReviews() == null || selectedOffer.getReviews().isEmpty() ? "No reviews yet" : selectedOffer.getReviews());
    }

    private void showAddReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Review");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_review, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            TextView ratingInput = customLayout.findViewById(R.id.ratingInput);
            TextView commentInput = customLayout.findViewById(R.id.commentInput);

            try {
                float rating = Float.parseFloat(ratingInput.getText().toString());
                String comment = commentInput.getText().toString();

                selectedOffer.addRating(rating);
                selectedOffer.addReview(comment);

                // Save the updated offer in the database asynchronously
                AppDatabase_firas.databaseExecutor.execute(() -> {
                    database.offerDao().updateOffer(selectedOffer);
                    runOnUiThread(() -> {
                        updateRatingAndReviews();
                        Toast.makeText(OfferDetailsActivity.this, "Review added!", Toast.LENGTH_SHORT).show();
                    });
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid rating input", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
