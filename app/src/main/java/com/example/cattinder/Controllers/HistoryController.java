package com.example.cattinder.Controllers;

import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattinder.Adapters.LikedCatAdapter;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HistoryController {
    public HistoryController(
        FloatingActionButton openDrawerButton,
        DrawerLayout drawerLayout,
        AuthViewModel authViewModel,
        RecyclerView recycler,
        AppCompatActivity activity
    ) {
        openDrawerButton.setOnClickListener(v -> drawerLayout.open());

        authViewModel.retrieveLikeHistory(activity).thenAccept(history -> {
            RecyclerView.LayoutManager manager;

            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                manager = new GridLayoutManager(activity, 5);
            } else {
                manager = new GridLayoutManager(activity, 3);
            }

            recycler.setLayoutManager(manager);
            recycler.setAdapter(new LikedCatAdapter(history, activity));
        });
    }
}
