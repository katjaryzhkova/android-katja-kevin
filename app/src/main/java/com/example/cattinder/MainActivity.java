package com.example.cattinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.cattinder.Controllers.MainController;
import com.example.cattinder.Controllers.TinderButtonClickListener;
import com.example.cattinder.ViewModels.CardViewModel;

// MainActivity.java
public class MainActivity extends AppCompatActivity {

    private MainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        ImageButton dislikeButton = findViewById(R.id.dislike_button);
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton mapButton = findViewById(R.id.map_button);

        View cardView = findViewById(R.id.card_stack);

        CardViewModel cardViewModel = new CardViewModel(this);
        controller = new MainController(cardViewModel, dislikeButton, likeButton, mapButton, cardView);

        cardViewModel.newCat(); // Initial cat
    }
}