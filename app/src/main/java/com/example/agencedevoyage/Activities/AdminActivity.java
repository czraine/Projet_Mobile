package com.example.agencedevoyage.Activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.ComplaintAdapter;
import com.example.agencedevoyage.Database.AppDatabase_rahma;
import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private final List<Complaint> complaintList = new ArrayList<>();
    private ComplaintAdapter complaintAdapter;
    private Complaint selectedComplaint;
    private Switch themeSwitch;

    // Possible statuses
    private final String[] statuses = {"In Progress", "Resolved", "Unknown"};

    // Database instance
    private AppDatabase_rahma database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize UI components
        themeSwitch = findViewById(R.id.themeSwitch);
        RecyclerView recyclerViewAdminComplaints = findViewById(R.id.recyclerViewAdminComplaints);
        Button updateStatusButton = findViewById(R.id.updateStatusButton);

        // Initialize SharedPreferences
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkModeOn = preferences.getBoolean("dark_mode", false);

        // Set initial theme and status bar color
        setStatusBarColor(isDarkModeOn);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            themeSwitch.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            themeSwitch.setChecked(false);
        }

        // Listen for theme switch changes
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            // Apply dark mode based on the switch's state
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            setStatusBarColor(isChecked);
        });

        // Initialize database and RecyclerView
        database = AppDatabase_rahma.getInstance(this);
        recyclerViewAdminComplaints.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView adapter
        complaintAdapter = new ComplaintAdapter(complaintList, new ComplaintAdapter.OnComplaintClickListener() {
            @Override
            public void onEditComplaint(Complaint complaint) {
                onComplaintSelected(complaint);
            }

            @Override
            public void onDeleteComplaint(Complaint complaint) {
                onComplaintDeleted(complaint);
            }
        });
        recyclerViewAdminComplaints.setAdapter(complaintAdapter);

        // Load complaints from database
        loadAdminComplaintsFromDatabase();

        // Set up Update Status button
        updateStatusButton.setOnClickListener(v -> {
            if (selectedComplaint != null) {
                showStatusUpdateDialog();
            } else {
                Toast.makeText(AdminActivity.this, "Please select a complaint to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set the status bar color based on the theme
    private void setStatusBarColor(boolean isDarkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            if (isDarkMode) {
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_dark)); // Dark mode color
                window.getDecorView().setSystemUiVisibility(0); // White text on dark background
            } else {
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_light)); // Light mode color
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Dark text on light background
            }
        }
    }

    // Load complaints from the database into the RecyclerView
    private void loadAdminComplaintsFromDatabase() {
        complaintList.clear();
        List<Complaint> complaintsFromDB = database.complaintDAO().getAllComplaints();
        complaintList.addAll(complaintsFromDB);
        complaintAdapter.notifyDataSetChanged();
    }

    // Handle complaint selection for editing
    private void onComplaintSelected(Complaint complaint) {
        selectedComplaint = complaint;
    }

    // Handle complaint deletion
    private void onComplaintDeleted(Complaint complaint) {
        database.complaintDAO().deleteComplaint(complaint);
        complaintList.remove(complaint);
        complaintAdapter.notifyDataSetChanged();
    }

    // Show dialog to update complaint status
    private void showStatusUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status");
        builder.setItems(statuses, (dialog, which) -> {
            selectedComplaint.setStatus(statuses[which]);
            database.complaintDAO().updateComplaintStatus(selectedComplaint.getTitle(), statuses[which]);
            complaintAdapter.notifyItemChanged(complaintList.indexOf(selectedComplaint));
        });
        builder.show();
    }
}
