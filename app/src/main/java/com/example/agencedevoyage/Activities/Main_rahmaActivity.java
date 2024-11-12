package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.ComplaintAdapter;
import com.example.agencedevoyage.Database.AppDatabase_rahma;
import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;

import java.util.ArrayList;
import java.util.List;

public class Main_rahmaActivity extends AppCompatActivity {
    private final List<Complaint> complaintList = new ArrayList<>();
    private ComplaintAdapter complaintAdapter;
    private AppDatabase_rahma database;

    // ActivityResultLauncher to handle complaints submission
    ActivityResultLauncher<Intent> submitComplaintLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String title = data.getStringExtra("complaint_title");
                    String description = data.getStringExtra("complaint_description");
                    if (title != null && description != null) {
                        Complaint newComplaint = new Complaint(title, description, "Unknown");
                        complaintList.add(newComplaint);
                        database.complaintDAO().insertComplaint(newComplaint);
                        complaintAdapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rahmamain);

        database = AppDatabase_rahma.getInstance(this);

        RecyclerView recyclerViewComplaints = findViewById(R.id.recyclerViewComplaints);
        recyclerViewComplaints.setLayoutManager(new LinearLayoutManager(this));

        complaintAdapter = new ComplaintAdapter(complaintList, new ComplaintAdapter.OnComplaintClickListener() {
            @Override
            public void onEditComplaint(Complaint complaint) {
                Intent intent = new Intent(Main_rahmaActivity.this, EditComplaintActivity.class);
                intent.putExtra("complaint_id", complaint.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteComplaint(Complaint complaint) {
                database.complaintDAO().deleteComplaint(complaint);
                complaintList.remove(complaint);
                complaintAdapter.notifyDataSetChanged();
                Toast.makeText(Main_rahmaActivity.this, "Complaint deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplaintStatusUpdated(Complaint complaint) {
                // Show rating dialog when status is updated to Resolved
                if ("Resolved".equals(complaint.getStatus())) {
                    showRatingDialog(complaint);
                }
            }
        });

        recyclerViewComplaints.setAdapter(complaintAdapter);

        ImageView addComplaintButton = findViewById(R.id.addComplaintButton);
        addComplaintButton.setOnClickListener(v -> {
            Intent intent = new Intent(Main_rahmaActivity.this, SubmitComplaintActivity.class);
            submitComplaintLauncher.launch(intent);
        });

        loadComplaintsFromDatabase();
    }

    private void loadComplaintsFromDatabase() {
        complaintList.clear();
        List<Complaint> complaintsFromDB = database.complaintDAO().getAllComplaints();
        complaintList.addAll(complaintsFromDB);
        complaintAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadComplaintsFromDatabase();
    }

    // Method to show the rating dialog when the status is "Resolved"
    private void showRatingDialog(Complaint complaint) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText feedbackInput = dialogView.findViewById(R.id.feedbackInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate Your Experience")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    int rating = (int) ratingBar.getRating(); // Get rating
                    String feedback = feedbackInput.getText().toString(); // Get feedback

                    // Update the complaint with rating and feedback
                    complaint.setRating(rating);
                    complaint.setFeedback(feedback);

                    // Update in the database
                    database.complaintDAO().updateComplaintFeedback(complaint.getId(), rating, feedback);

                    Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    complaintAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
