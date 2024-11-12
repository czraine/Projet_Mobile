package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.Database.AppDatabase_rahma;
import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;


public class EditComplaintActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button saveButton;
    private AppDatabase_rahma database;
    private Complaint complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_complaint);

        // Initialisation des composants de l'interface utilisateur
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        saveButton = findViewById(R.id.saveButton);

        // Récupérer la base de données
        database = AppDatabase_rahma.getInstance(this);

        // Récupérer l'ID de la plainte à modifier
        int complaintId = getIntent().getIntExtra("complaint_id", -1);
        if (complaintId != -1) {
            complaint = database.complaintDAO().getComplaintById(complaintId);

            if (complaint != null) {
                // Pré-remplir les champs avec les données actuelles de la plainte
                editTextTitle.setText(complaint.getTitle());
                editTextDescription.setText(complaint.getDescription());
            }
        }

        // Sauvegarder les modifications lorsqu'on clique sur le bouton "Save"
        saveButton.setOnClickListener(v -> {
            // Mettre à jour les données de la plainte
            String newTitle = editTextTitle.getText().toString();
            String newDescription = editTextDescription.getText().toString();

            // Mettre à jour la plainte dans la base de données
            database.complaintDAO().updateComplaint(complaint.getId(), newTitle, newDescription);

            // Fermer l'activité après la sauvegarde
            finish();
        });
    }
}
