package com.example.cattinder.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattinder.Models.LikedCat;
import com.example.cattinder.Tasks.ImageLoadTask;
import com.example.cattinder.databinding.LikedCatBinding;

import java.util.ArrayList;

public class LikedCatAdapter extends RecyclerView.Adapter<LikedCatAdapter.ViewHolder> {
    private final ArrayList<LikedCat> history;
    private final AppCompatActivity activity;

    public LikedCatAdapter(ArrayList<LikedCat> history, AppCompatActivity activity) {
        super();
        this.history = history;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LikedCatBinding binding = LikedCatBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LikedCat cat = history.get(position);

        new ImageLoadTask(activity, cat.imageUrl, holder.image).execute();
        holder.name.setText(cat.name);
    }

    @Override
    public int getItemCount() {
        if (history == null) {
            return 0;
        }

        return history.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull LikedCatBinding binding) {
            super(binding.getRoot());
            image = binding.likedCatImage;
            name = binding.likedCatName;
        }
    }
}
