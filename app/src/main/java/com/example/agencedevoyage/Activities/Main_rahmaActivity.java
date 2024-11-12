package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
}
