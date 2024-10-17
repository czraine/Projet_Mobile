package com.example.agencedevoyage.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.example.agencedevoyage.Domains.CategoryDomain;
import com.example.agencedevoyage.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<CategoryDomain> items ;

    public CategoryAdapter(ArrayList<CategoryDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent, false) ;
            return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
holder.titletxt.setText(items.get(position).getTitle());
int drawableRessourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPicpath() , "drawable", holder.itemView.getContext().getPackageName());


Glide.with(holder.itemView.getContext()).load(drawableRessourceId).into(holder.picimg) ;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titletxt ;
        ImageView picimg ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.categ_title) ;
            picimg = itemView.findViewById(R.id.categ_img) ;
        }
    }
}
