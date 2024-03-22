package com.example.cattinder.Controllers;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.cattinder.R;
import com.example.cattinder.ViewModels.CardViewModel;

import kotlin.NotImplementedError;

// MainController.java
public class MainController implements View.OnClickListener {

    private final CardViewModel cardViewModel;
    private final GestureDetector gestureDetector;

    public MainController(CardViewModel cardViewModel, ImageButton dislikeButton, ImageButton likeButton, ImageButton mapButton, View cardView) {
        this.cardViewModel = cardViewModel;

        dislikeButton.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        gestureDetector = new GestureDetector(cardView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > 50) {
                    // Swipe left
                    onClick(dislikeButton);
                    return true;
                } else if (e2.getX() - e1.getX() > 50) {
                    // Swipe right
                    onClick(likeButton);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        cardView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    @Override
    public void onClick(View button) {
        if (button.getId() == R.id.dislike_button || button.getId() == R.id.like_button) {
            cardViewModel.newCat();
        } else if (button.getId() == R.id.map_button) {
            throw new NotImplementedError("Map feature not implemented yet");
        }
    }
}