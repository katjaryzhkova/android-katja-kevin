package com.example.cattinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.cattinder.Controllers.TinderButtonClickListener;
import com.example.cattinder.ViewModels.CardViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        ImageButton dislikeButton = findViewById(R.id.dislike_button);
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton mapButton = findViewById(R.id.map_button);

        TinderButtonClickListener buttonClickListener = new TinderButtonClickListener();
        dislikeButton.setOnClickListener(buttonClickListener);
        likeButton.setOnClickListener(buttonClickListener);
        mapButton.setOnClickListener(buttonClickListener);

        CardViewModel cardViewModel = new CardViewModel(this);
        cardViewModel.newCat();
    }
}