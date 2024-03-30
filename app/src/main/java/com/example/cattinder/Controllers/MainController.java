package com.example.cattinder.Controllers;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cattinder.Activities.MapActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.ViewModels.CardViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// MainController.java
public class MainController implements View.OnClickListener {

    private final CardViewModel cardViewModel;
    private final GestureDetector gestureDetector;
    private final AppCompatActivity activity;
    private final AuthViewModel auth;

    public MainController(
        CardViewModel cardViewModel,
        ImageButton dislikeButton,
        ImageButton likeButton,
        ImageButton mapButton,
        View cardView,
        FloatingActionButton openDrawerButton,
        DrawerLayout drawerLayout,
        AuthViewModel auth,
        AppCompatActivity activity
    ) {
        this.cardViewModel = cardViewModel;
        this.auth = auth;
        this.activity = activity;

        dislikeButton.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

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
            if (button.getId() == R.id.like_button) {
                auth.addToLikeHistory(activity, cardViewModel.getImageUrl(), cardViewModel.getName());
            }

            cardViewModel.newCat();
        } else if (button.getId() == R.id.map_button) {
            LatLng catLocation = cardViewModel.getCatLocation();
            Intent intent = new Intent(button.getContext(), MapActivity.class);
            intent.putExtra("latitude", catLocation.latitude);
            intent.putExtra("longitude", catLocation.longitude);
            button.getContext().startActivity(intent);
        }
    }
}