package com.example.agencedevoyage.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.agencedevoyage.Database.AppDatabase_rahma;
import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class SubmitComplaintActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE_AUDIO_PERMISSION = 2;

    private AppDatabase_rahma database;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);

        // Initialize form elements
        EditText issueTitle = findViewById(R.id.issueTitle);
        description = findViewById(R.id.description); // Initialize description EditText
        TextInputLayout descriptionLayout = findViewById(R.id.descriptionLayout); // TextInputLayout containing the EditText
        Button submitButton = findViewById(R.id.submitButton);

        // Initialize database
        database = AppDatabase_rahma.getInstance(this);

        // Set up microphone icon click listener on TextInputLayout
        descriptionLayout.setEndIconOnClickListener(v -> {
            // Check for audio permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO_PERMISSION);
            } else {
                startSpeechToText();
            }
        });

        // Handle "Submit" button click
        submitButton.setOnClickListener(v -> {
            // Get text field values
            String title = issueTitle.getText().toString();
            String desc = description.getText().toString();

            // Check if fields are filled
            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show();
            } else {
                // Create new Complaint object
                Complaint complaint = new Complaint(title, desc, "Unknown");

                // Insert complaint into the database
                database.complaintDAO().insertComplaint(complaint);

                // Show confirmation message
                Toast.makeText(this, getString(R.string.complaint_submitted), Toast.LENGTH_SHORT).show();

                // Close the activity after submission
                finish();
            }
        });
    }

    // Start speech-to-text functionality
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH); // Change to specific language if needed
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your complaint description");
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000); // Wait 2 seconds after user stops speaking
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000); // Minimum speaking duration

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the speech-to-text input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                description.setText(result.get(0)); // Set the recognized text in the EditText
                Log.d("SpeechRecognizer", "Recognized Text: " + result.get(0)); // Log recognized text
            }
        } else {
            Log.d("SpeechRecognizer", "Speech recognition failed or was canceled.");
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSpeechToText();
            } else {
                Toast.makeText(this, "Audio permission is required for voice input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Go back method for the back arrow click
    public void goBack(android.view.View view) {
        finish(); // Close the current activity
    }
}
