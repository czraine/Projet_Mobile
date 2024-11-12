package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

public class AdminActivity extends AppCompatActivity {

    private final List<Complaint> complaintList = new ArrayList<>();
    private ComplaintAdapter complaintAdapter;
    private Complaint selectedComplaint;

    // Statuts possibles
    private final String[] statuses = {"In Progress", "Resolved", "Unknown"};

    // Instance de la base de données
    private AppDatabase_rahma database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialiser la base de données
        database = AppDatabase_rahma.getInstance(this);

        // Configurer RecyclerView pour les plaintes admin
        RecyclerView recyclerViewAdminComplaints = findViewById(R.id.recyclerViewAdminComplaints);
        recyclerViewAdminComplaints.setLayoutManager(new LinearLayoutManager(this));

        // Configurer l'adaptateur avec les actions d'édition et de suppression
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

        // Charger les plaintes depuis la base de données
        loadAdminComplaintsFromDatabase();

        // Bouton de mise à jour du statut
        Button updateStatusButton = findViewById(R.id.updateStatusButton);
        updateStatusButton.setOnClickListener(v -> {
            if (selectedComplaint != null) {
                // Afficher le dialogue pour changer le statut
                showStatusUpdateDialog();
            } else {
                // Message si aucune plainte sélectionnée
                Toast.makeText(AdminActivity.this, "Please select a complaint to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Charger les plaintes depuis la base de données
    private void loadAdminComplaintsFromDatabase() {
        // Vider la liste actuelle
        complaintList.clear();

        // Récupérer toutes les plaintes depuis la base de données
        List<Complaint> complaintsFromDB = database.complaintDAO().getAllComplaints();
        complaintList.addAll(complaintsFromDB);

        // Notifier l'adaptateur des changements
        complaintAdapter.notifyDataSetChanged();
    }

    // Appelé lorsqu'une plainte est sélectionnée
    private void onComplaintSelected(Complaint complaint) {
        selectedComplaint = complaint;
    }

    // Appelé lorsqu'une plainte est supprimée
    private void onComplaintDeleted(Complaint complaint) {
        // Supprimer la plainte de la base de données et de la liste
        database.complaintDAO().deleteComplaint(complaint);
        complaintList.remove(complaint);
        complaintAdapter.notifyDataSetChanged();
    }

    // Afficher un dialogue pour mettre à jour le statut
    private void showStatusUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status");

        // Afficher les statuts disponibles
        builder.setItems(statuses, (dialog, which) -> {
            selectedComplaint.setStatus(statuses[which]);

            // Mettre à jour le statut dans la base de données
            database.complaintDAO().updateComplaintStatus(selectedComplaint.getTitle(), statuses[which]);

            // Notifier l'adaptateur que les données ont changé
            complaintAdapter.notifyItemChanged(complaintList.indexOf(selectedComplaint));
        });

        builder.show();
    }
}
