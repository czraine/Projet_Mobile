package com.example.agencedevoyage.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.agencedevoyage.Database.AppDatabase_firas;
import com.example.agencedevoyage.Entity.Offer;
import com.example.agencedevoyage.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Add_Offres extends AppCompatActivity {

    private EditText offerTitle, offerDescription, offerDestination, offerPrice, offerType, offerStartDate, offerEndDate;
    private Button addOfferButton, selectImageButton;
    private ImageView backButton, selectedImageView;
    private Uri imageUri;  // To store the selected image URI
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public static ArrayList<Offer> offersList = new ArrayList<>();
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;  // Permission request code
    private AppDatabase_firas offerDatabase;  // Room database instance
    private ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);  // Thread pool for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offres);

        // Initialize views
        offerTitle = findViewById(R.id.offerTitle);
        offerDescription = findViewById(R.id.offerDescription);
        offerDestination = findViewById(R.id.offerDestination);
        offerPrice = findViewById(R.id.offerPrice);
        offerType = findViewById(R.id.offerType);
        offerStartDate = findViewById(R.id.offerStartDate);
        offerEndDate = findViewById(R.id.offerEndDate);
        addOfferButton = findViewById(R.id.addOfferButton);
        backButton = findViewById(R.id.backButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectedImageView = findViewById(R.id.selectedImageView);

        offerDatabase = AppDatabase_firas.getInstance(this);  // Initialize Room database

        // Back button listener to navigate back to Dashboard
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Add_Offres.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Date picker dialogs for selecting start and end dates
        offerStartDate.setOnClickListener(v -> showDatePickerDialog(offerStartDate));
        offerEndDate.setOnClickListener(v -> showDatePickerDialog(offerEndDate));

        // Button listener to select an image
        selectImageButton.setOnClickListener(v -> checkPermissions());

        // Button listener to add the offer to the database
        addOfferButton.setOnClickListener(v -> {
            String title = offerTitle.getText().toString();
            String description = offerDescription.getText().toString();
            String destination = offerDestination.getText().toString();
            String priceString = offerPrice.getText().toString();
            String type = offerType.getText().toString();
            String startDateString = offerStartDate.getText().toString();
            String endDateString = offerEndDate.getText().toString();

            if (title.isEmpty() || description.isEmpty() || destination.isEmpty() || priceString.isEmpty() || type.isEmpty() || startDateString.isEmpty() || endDateString.isEmpty()) {
                Toast.makeText(Add_Offres.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double price = Double.parseDouble(priceString);
                    long availabilityStartDate = dateFormat.parse(startDateString).getTime();
                    long availabilityEndDate = dateFormat.parse(endDateString).getTime();
                    String imageUriString = imageUri != null ? imageUri.toString() : "";

                    Offer newOffer = new Offer(title, description, destination, price, imageUriString, type, availabilityStartDate, availabilityEndDate);

                    // Insert the offer into the Room database
                    databaseExecutor.execute(() -> {
                        offerDatabase.offerDao().insertOffer(newOffer);
                        runOnUiThread(() -> {
                            Toast.makeText(Add_Offres.this, "Offer added to database", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Add_Offres.this, DashboardActivity.class);
                            startActivity(intent);
                        });
                    });
                } catch (Exception e) {
                    Toast.makeText(Add_Offres.this, "Invalid price or date format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to show a DatePickerDialog and set the selected date to the EditText
    private void showDatePickerDialog(EditText dateField) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Add_Offres.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    dateField.setText(dateFormat.format(calendar.getTime()));  // Set formatted date
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Check for permissions before selecting an image
    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 (API 33) and above
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            // For Android 12 (API 32) and below
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Launch the gallery to pick an image
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);  // Display the selected image
        }
    }
}
