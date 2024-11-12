package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.Database.AppDatabase_rahma;
import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;


public class SubmitComplaintActivity extends AppCompatActivity {

    private AppDatabase_rahma database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaint);

        // Initialiser les éléments de formulaire
        EditText issueTitle = findViewById(R.id.issueTitle);
        EditText description = findViewById(R.id.description);
        Button submitButton = findViewById(R.id.submitButton);

        // Initialiser la base de données
        database = AppDatabase_rahma.getInstance(this);

        // Gestion du clic sur le bouton "Submit"
        submitButton.setOnClickListener(v -> {
            // Récupérer les valeurs des champs de texte
            String title = issueTitle.getText().toString();
            String desc = description.getText().toString();

            // Vérifier si les champs sont remplis
            if (title.isEmpty() || desc.isEmpty()) {
                // Afficher un message si l'un des champs est vide
                Toast.makeText(SubmitComplaintActivity.this, getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show();
            } else {
                // Créer un nouvel objet Complaint avec la description et un statut "Unknown"
                Complaint complaint = new Complaint(title, desc, "Unknown");

                // Insérer la plainte dans la base de données
                database.complaintDAO().insertComplaint(complaint);

                // Afficher un message de confirmation
                Toast.makeText(SubmitComplaintActivity.this, getString(R.string.complaint_submitted), Toast.LENGTH_SHORT).show();

                // Fermer l'activité après la soumission
                finish();
            }
        });
    }

    // Méthode appelée lors du clic sur la flèche de retour
    public void goBack(android.view.View view) {
        finish(); // Ferme l'activité actuelle
    }
}
