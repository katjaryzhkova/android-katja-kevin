package com.example.cattinder.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cattinder.Controllers.HistoryController;
import com.example.cattinder.Tasks.LoadDrawerTask;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.databinding.HistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);
        HistoryBinding binding = HistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new HistoryController(
            binding.profileOpenDrawerButton,
            binding.drawerLayout,
            AuthViewModel.getInstance(),
            binding.historyView,
            this
        );

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        new LoadDrawerTask(this, fragmentTransaction, AuthViewModel.getInstance()).execute();
    }
}
