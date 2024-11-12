package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.FeedbackAdabter;
import com.example.agencedevoyage.Database.AppDatabase_rahma;
import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize the TextView to display the average rating
        TextView averageRatingTextView = findViewById(R.id.averageRatingTextView);

        // Retrieve data from the database
        AppDatabase_rahma db = AppDatabase_rahma.getInstance(this);
        List<Complaint> complaintList = db.complaintDAO().getAllComplaints();

        // Calculate average rating
        double averageRating = calculateAverageRating(complaintList);
        averageRatingTextView.setText("Average Rating: " + String.format("%.2f", averageRating) + " stars");

        // Filter complaints to only those with feedback and ratings
        List<Complaint> feedbackList = new ArrayList<>();
        for (Complaint complaint : complaintList) {
            if (complaint.getRating() > 0 && complaint.getFeedback() != null) {
                feedbackList.add(complaint);
            }
        }

        // Set up the RecyclerView to display feedback and ratings
        RecyclerView feedbackRecyclerView = findViewById(R.id.commentsRecyclerView);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FeedbackAdabter adapter = new FeedbackAdabter(feedbackList);
        feedbackRecyclerView.setAdapter(adapter);
    }

    // Method to calculate the average rating
    private double calculateAverageRating(List<Complaint> complaintList) {
        double sum = 0;
        int count = 0;
        for (Complaint complaint : complaintList) {
            if (complaint.getRating() > 0) {
                sum += complaint.getRating();
                count++;
            }
        }
        return count == 0 ? 0 : sum / count;
    }
}
