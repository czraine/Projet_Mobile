package com.example.agencedevoyage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Activities.UpdateReservation;
import com.example.agencedevoyage.Dao.ReservationDAO;
import com.example.agencedevoyage.Entity.Reservation;
import com.example.agencedevoyage.R;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservations;
    private Context context;
    private ReservationDAO reservationDAO;

    public ReservationAdapter(List<Reservation> reservations, Context context, ReservationDAO reservationDAO) {
        this.reservations = reservations;
        this.context = context;
        this.reservationDAO = reservationDAO;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.reservationInfo.setText("Name: " + reservation.getFirstName() + " " + reservation.getLastName() + "\n"
                + "Phone: " + reservation.getPhone());

        holder.updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateReservation.class);
            intent.putExtra("reservationId", reservation.getId());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            reservationDAO.deleteReservation(reservation.getId());
            reservations.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Reservation deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView reservationInfo;
        Button updateButton, deleteButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            reservationInfo = itemView.findViewById(R.id.reservation_info_text);
            updateButton = itemView.findViewById(R.id.update_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
