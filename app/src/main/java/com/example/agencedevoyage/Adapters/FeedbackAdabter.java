package com.example.agencedevoyage.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Domains.Complaint;
import com.example.agencedevoyage.R;

import java.util.List;

public class FeedbackAdabter extends RecyclerView.Adapter<FeedbackAdabter.FeedbackViewHolder> {

    private final List<Complaint> feedbackList;

    // Constructor
    public FeedbackAdabter(List<Complaint> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Complaint complaint = feedbackList.get(position);
        holder.titleText.setText(complaint.getTitle());
        holder.descriptionText.setText(complaint.getDescription());
        holder.feedbackText.setText(complaint.getFeedback() != null ? complaint.getFeedback() : "No feedback");
        holder.ratingBar.setRating(complaint.getRating());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    // ViewHolder for Feedback
    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;
        TextView feedbackText;
        RatingBar ratingBar;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.complaintTitle);
            descriptionText = itemView.findViewById(R.id.complaintDescription);
            feedbackText = itemView.findViewById(R.id.feedbackText);
            ratingBar = itemView.findViewById(R.id.ratingBarFeedback);
        }
    }
}
