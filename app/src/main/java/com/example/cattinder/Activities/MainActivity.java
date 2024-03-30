package com.example.cattinder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.example.cattinder.Activities.SignInActivity;
import com.example.cattinder.Controllers.MainController;
import com.example.cattinder.Services.CatTinderService;
import com.example.cattinder.Tasks.LoadDrawerTask;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.ViewModels.CardViewModel;
import com.example.cattinder.databinding.HomescreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static String CHANNEL_ID = "CAT_TINDER";
    private CardViewModel cardViewModel;

    public static boolean isNetworkAvailable(AppCompatActivity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkMode = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        if (auth.getCurrentUser() != null) {
            overridePendingTransition(0, 0);

            HomescreenBinding binding = HomescreenBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            if (cardViewModel == null) {
                cardViewModel = new CardViewModel(this);
            }

            cardViewModel.newCat();

            new MainController(
                cardViewModel,
                binding.dislikeButton,
                binding.likeButton,
                binding.mapButton,
                binding.cardStack,
                binding.homeOpenDrawerButton,
                binding.drawerLayout,
                AuthViewModel.getInstance(),
                this
            );

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            new LoadDrawerTask(this, fragmentTransaction, AuthViewModel.getInstance()).execute();
        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CatTinder", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, CatTinderService.class);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}