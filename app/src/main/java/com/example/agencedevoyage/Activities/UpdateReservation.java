package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.Dao.ReservationDAO;
import com.example.agencedevoyage.Entity.Reservation;
import com.example.agencedevoyage.R;

public class UpdateReservation extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneEditText, passportEditText, requirementsEditText;
    private Button saveButton;
    private ReservationDAO reservationDAO;
    private int reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reservation);

        // Initialize views
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        phoneEditText = findViewById(R.id.edit_phone);
        passportEditText = findViewById(R.id.edit_passport);
        requirementsEditText = findViewById(R.id.edit_requirements);
        saveButton = findViewById(R.id.save_button);

        // Initialize DAO
        reservationDAO = new ReservationDAO(this);
        reservationDAO.open();

        // Get reservation ID from intent extras
        reservationId = getIntent().getIntExtra("reservationId", -1);

        // Load reservation details if valid ID is provided
        if (reservationId != -1) {
            loadReservationDetails();
        } else {
            Toast.makeText(this, "Invalid reservation ID", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if ID is invalid
        }

        // Set up save button click listener
        saveButton.setOnClickListener(v -> saveUpdatedReservation());
    }

    private void loadReservationDetails() {
        // Fetch the reservation by ID
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation != null) {
            // Populate fields with current reservation data
            firstNameEditText.setText(reservation.getFirstName());
            lastNameEditText.setText(reservation.getLastName());
            phoneEditText.setText(reservation.getPhone());
            passportEditText.setText(reservation.getPassport());
            requirementsEditText.setText(reservation.getRequirements());
        } else {
            Toast.makeText(this, "Reservation not found", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if reservation not found
        }
    }

    private void saveUpdatedReservation() {
        // Get updated values from the fields
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String passport = passportEditText.getText().toString().trim();
        String requirements = requirementsEditText.getText().toString().trim();

        // Create updated reservation object
        Reservation updatedReservation = new Reservation(reservationId, firstName, lastName, phone, passport, requirements);

        // Update in the database
        int rowsAffected = reservationDAO.updateReservation(updatedReservation);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after successful update
        } else {
            Toast.makeText(this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reservationDAO.close(); // Close database connection
    }
}
