package com.example.agencedevoyage.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.agencedevoyage.Domains.CategoryDomain;
import com.example.agencedevoyage.Domains.PopularDomain;
import com.example.agencedevoyage.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<PopularDomain> items ;
DecimalFormat formater ;
    public PopularAdapter(ArrayList<PopularDomain> items) {
        this.items = items;
    formater = new DecimalFormat("###,###,###,###");
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular,parent, false) ;

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        holder.titletxt.setText(items.get(position).getTitle());
        holder.locationtxt.setText(items.get(position).getLocation());
        holder.socretxt.setText(""+items.get(position).getScore());
        int drawableResId = holder.itemView.getResources().getIdentifier(items.get(position).getPic() , "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).load(drawableResId).transform( new CenterCrop() , new GranularRoundedCorners(40,40,40,40)).into(holder.pic) ;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titletxt , locationtxt , socretxt ;
        ImageView pic ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.titletxt) ;
            locationtxt = itemView.findViewById(R.id.locationtxt) ;
            socretxt = itemView.findViewById(R.id.scoretxt) ;
            pic = itemView.findViewById(R.id.picimg) ;


        }
    }
}
