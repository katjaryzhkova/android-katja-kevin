package com.example.cattinder.Controllers;

import android.view.View;

import com.example.cattinder.R;
import com.example.cattinder.ViewModels.CardViewModel;

import kotlin.NotImplementedError;


public class TinderButtonClickListener implements View.OnClickListener {
    @Override
    public void onClick(View button) {
        CardViewModel cardViewModel = new CardViewModel(button.getContext());
        cardViewModel.newCat();
        if (button.getId() == R.id.dislike_button || button.getId() == R.id.like_button) {
            cardViewModel.newCat();
        } else if (button.getId() == R.id.map_button) {
            throw new NotImplementedError("Map feature not implemented yet");
        }
    }
}