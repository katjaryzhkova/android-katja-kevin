package com.example.cattinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cattinder.ViewModels.CardViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        CardViewModel cardViewModel = new CardViewModel(this);
        cardViewModel.newCat();
    }
}