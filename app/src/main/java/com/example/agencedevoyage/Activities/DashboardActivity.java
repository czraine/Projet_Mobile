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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize the TextView to display the average rating
        TextView averageRatingTextView = findViewById(R.id.averageRatingTextView);

        // Initialize BarChart
        BarChart ratingBarChart = findViewById(R.id.ratingBarChart);

        // Retrieve data from the database
        AppDatabase_rahma db = AppDatabase_rahma.getInstance(this);
        List<Complaint> complaintList = db.complaintDAO().getAllComplaints();

        // Calculate average rating
        double averageRating = calculateAverageRating(complaintList);
        averageRatingTextView.setText("Average Rating: " + String.format("%.2f", averageRating) + " stars");

        // Set up the bar chart with rating distribution data
        setupBarChart(ratingBarChart, complaintList);

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

    // Method to set up the bar chart for rating distribution
    private void setupBarChart(BarChart barChart, List<Complaint> complaints) {
        int[] starCounts = new int[5]; // Array to hold counts for each star rating (1-5)

        for (Complaint complaint : complaints) {
            int rating = complaint.getRating();
            if (rating >= 1 && rating <= 5) {
                starCounts[rating - 1]++; // Increment count for the respective rating
            }
        }

        // Create entries for each star rating
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < starCounts.length; i++) {
            entries.add(new BarEntry(i + 1, starCounts[i]));
        }

        // Create a BarDataSet and style it
        BarDataSet dataSet = new BarDataSet(entries, "Ratings Distribution");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        // Create BarData and set it to the BarChart
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false); // Disable description text
        barChart.getAxisLeft().setGranularity(1f);   // Force Y-axis labels to be integers
        barChart.getAxisRight().setEnabled(false);   // Disable the right Y-axis
        barChart.getXAxis().setGranularity(1f);      // Force X-axis labels to be integers
        barChart.getXAxis().setLabelCount(5, true);  // Show labels for 1 to 5 stars
        barChart.animateY(1000);                     // Add animation
        barChart.invalidate();                       // Refresh chart

    }
}
