package com.example.agencedevoyage.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {

    private final List<Complaint> complaintList;
    private final OnComplaintClickListener listener;

    // Constructor
    public ComplaintAdapter(List<Complaint> complaintList, OnComplaintClickListener listener) {
        this.complaintList = complaintList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_complaint, parent, false);
        return new ComplaintViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        Complaint complaint = complaintList.get(position);
        holder.bind(complaint, listener);

        // Set background color based on complaint status
        switch (complaint.getStatus()) {
            case "In Progress":
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.in_progress_color));
                break;
            case "Resolved":
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.resolved_color));
                break;
            case "Unknown":
            default:
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.unknown_color));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    // Define the interface for click events
    public interface OnComplaintClickListener {
        void onEditComplaint(Complaint complaint);
        void onDeleteComplaint(Complaint complaint);
        void onComplaintStatusUpdated(Complaint complaint);
    }

    static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView complaintTitle;
        TextView complaintDescription;
        TextView complaintStatus;
        ImageView editIcon;
        ImageView deleteIcon;

        public ComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintTitle = itemView.findViewById(R.id.complaintTitle);
            complaintDescription = itemView.findViewById(R.id.complaintDescription);
            complaintStatus = itemView.findViewById(R.id.complaintStatus);
            editIcon = itemView.findViewById(R.id.editButton);
            deleteIcon = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Complaint complaint, OnComplaintClickListener listener) {
            complaintTitle.setText(complaint.getTitle());
            complaintDescription.setText(complaint.getDescription());
            complaintStatus.setText(complaint.getStatus());

            // Set up click listeners for edit and delete buttons
            editIcon.setOnClickListener(v -> listener.onEditComplaint(complaint));
            deleteIcon.setOnClickListener(v -> listener.onDeleteComplaint(complaint));

            // Handle click event for updating the complaint's status
            itemView.setOnClickListener(v -> {
                if ("Resolved".equals(complaint.getStatus())) {
                    listener.onComplaintStatusUpdated(complaint);
                }
            });
        }
    }
}
