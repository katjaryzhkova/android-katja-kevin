package com.example.cattinder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattinder.MainActivity;
import com.example.cattinder.Models.LikedCat;
import com.example.cattinder.R;
import com.example.cattinder.Tasks.ImageLoadTask;

import java.util.ArrayList;

public class LikedCatAdapter extends RecyclerView.Adapter<LikedCatAdapter.ViewHolder> {
    private final ArrayList<LikedCat> history;
    private final MainActivity main;

    public LikedCatAdapter(ArrayList<LikedCat> history, MainActivity main) {
        super();
        this.history = history;
        this.main = main;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.liked_cat, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LikedCat cat = history.get(position);

        new ImageLoadTask(main, cat.imageUrl, holder.image).execute();
        holder.name.setText(cat.name);
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.liked_cat_image);
            name = itemView.findViewById(R.id.liked_cat_name);
        }
    }
}
