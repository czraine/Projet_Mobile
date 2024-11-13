package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.ReservationAdapter;
import com.example.agencedevoyage.Dao.ReservationDAO;
import com.example.agencedevoyage.Entity.Reservation;
import com.example.agencedevoyage.R;

import java.util.List;

public class ReservationListActivity extends AppCompatActivity {

    private RecyclerView reservationsRecyclerView;
    private ReservationDAO reservationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        reservationsRecyclerView = findViewById(R.id.reservations_recycler_view);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reservationDAO = new ReservationDAO(this);
        reservationDAO.open();

        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationDAO.getAllReservations();
        if (reservations.isEmpty()) {
            Toast.makeText(this, "No reservations found", Toast.LENGTH_SHORT).show();
        } else {
            ReservationAdapter adapter = new ReservationAdapter(reservations, this, reservationDAO);
            reservationsRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reservationDAO.close();
    }
}
