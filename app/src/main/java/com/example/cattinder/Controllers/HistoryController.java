package com.example.cattinder.Controllers;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cattinder.Adapters.LikedCatAdapter;
import com.example.cattinder.MainActivity;
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
        MainActivity main
    ) {
        openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        RecyclerView.LayoutManager manager = new GridLayoutManager(main, 3);
        recycler.setLayoutManager(manager);
        authViewModel.retrieveLikeHistory().thenAccept(new Consumer<ArrayList<LikedCat>>() {
            @Override
            public void accept(ArrayList<LikedCat> history) {
                recycler.setAdapter(new LikedCatAdapter(history, main));
            }
        });
    }
}
