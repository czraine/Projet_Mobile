package com.example.agencedevoyage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agencedevoyage.Activities.EditOfferActivity;
import com.example.agencedevoyage.Activities.OfferDetailsActivity;
import com.example.agencedevoyage.Entity.Offer;
import com.example.agencedevoyage.R;

import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private List<Offer> offersList;
    private Context context;
    private static final int EDIT_OFFER_REQUEST_CODE = 2000;


    public OfferAdapter(Context context, ArrayList<Offer> offerList) {
        this.offersList = offerList;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offersList.get(position);
        holder.titleTextView.setText(offer.getTitle());
        holder.descriptionTextView.setText(offer.getDescription());
        holder.destinationTextView.setText(offer.getDestination());
        holder.priceTextView.setText(String.valueOf(offer.getPrice()));

        // Set the image URI or placeholder
        if (offer.getImageUri() != null && !offer.getImageUri().isEmpty()) {
            holder.offerImageView.setImageURI(Uri.parse(offer.getImageUri()));
        } else {
            holder.offerImageView.setImageResource(R.drawable.default_image);  // Placeholder image
        }

        holder.offerDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, OfferDetailsActivity.class);
            intent.putExtra("selectedOffer", offer);  // Pass the selected offer object to the details activity
            context.startActivity(intent);
        });

        holder.editDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditOfferActivity.class);
            intent.putExtra("offer", offersList.get(position));
            ((AppCompatActivity) context).startActivityForResult(intent, OfferAdapter.EDIT_OFFER_REQUEST_CODE);
        });

        holder.deleteDetailsButton.setOnClickListener(v -> {
            offersList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, offersList.size());
            Toast.makeText(context, "Deleted: " + offer.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public List<Offer> getOffersList() {
        return offersList;
    }
    public void updateOffersList(List<Offer> newOffersList) {
        offersList.clear();
        offersList.addAll(newOffersList);
        notifyDataSetChanged();
    }


    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, destinationTextView, priceTextView;
        ImageView offerImageView;
        Button offerDetailsButton, editDetailsButton, deleteDetailsButton;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.offerTitleText);
            descriptionTextView = itemView.findViewById(R.id.offerDescriptionText);
            destinationTextView = itemView.findViewById(R.id.offerDestinationText);
            priceTextView = itemView.findViewById(R.id.offerPriceText);
            offerImageView = itemView.findViewById(R.id.offerImageView);
            offerDetailsButton = itemView.findViewById(R.id.offerDetailsButton);
            editDetailsButton = itemView.findViewById(R.id.editDetailsButton);
            deleteDetailsButton = itemView.findViewById(R.id.deleteDetailsButton);
        }
    }
}
