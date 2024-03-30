package com.example.cattinder.Controllers;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattinder.Adapters.LikedCatAdapter;
import com.example.cattinder.Models.LikedCat;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HistoryController {
    public HistoryController(
        FloatingActionButton openDrawerButton,
        DrawerLayout drawerLayout,
        AuthViewModel authViewModel,
        RecyclerView recycler,
        AppCompatActivity activity
    ) {
        openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        authViewModel.retrieveLikeHistory(activity).thenAccept(new Consumer<ArrayList<LikedCat>>() {
            @Override
            public void accept(ArrayList<LikedCat> history) {
                RecyclerView.LayoutManager manager = new GridLayoutManager(activity, 3);
                recycler.setLayoutManager(manager);
                recycler.setAdapter(new LikedCatAdapter(history, activity));
            }
        });
    }
}
