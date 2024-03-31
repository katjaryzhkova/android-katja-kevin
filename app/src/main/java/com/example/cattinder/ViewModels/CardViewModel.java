package com.example.cattinder.ViewModels;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cattinder.Activities.MainActivity;

import com.example.cattinder.databinding.HomescreenBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.net.URL;

public class CardViewModel {
    private final AppCompatActivity activity;
    private LatLng catLocation;
    private String imageUrl = "";
    private String name = "";
    private Snackbar internetOfflineSnackbar;
    private final HomescreenBinding binding;

    public CardViewModel(
        AppCompatActivity activity,
        HomescreenBinding binding
    ) {
        this.activity = activity;
        this.binding = binding;
    }

    private boolean checkInternet() {
        if (MainActivity.isNetworkUnavailable(activity)) {
            if (internetOfflineSnackbar == null) {
                internetOfflineSnackbar = Snackbar.make(activity.findViewById(android.R.id.content), "An internet connection is required to use CatTinder", 999999999);
                internetOfflineSnackbar.show();
            }

            new Handler(Looper.getMainLooper()).postDelayed(this::checkInternet, 1000);
            return false;
        }

        if (internetOfflineSnackbar != null) {
            internetOfflineSnackbar.dismiss();
            internetOfflineSnackbar = null;
            newCat();
        }

        return true;
    }

    public void newCat() {
        try {
            if (!checkInternet()) {
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);

            loadCatImage(new URL("https://api.thecatapi.com/v1/images/search"), binding.progressBar);
            loadUserData(new URL("https://randomuser.me/api/"));
        } catch (Exception ignored) { }
    }

    private void loadCatImage(URL url, ProgressBar progressBar) {
        if (activity.isDestroyed()) {
            return;
        }

        ImageView imageView = binding.cardImage;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject(0);
                imageUrl = jsonObject.getString("url");

                Glide.with(activity).load(imageUrl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                }).into(imageView);
            } catch (Exception ignored) { }
        }, error -> { });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(jsonArrayRequest);
    }

    private void loadUserData(URL url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, response -> {
            try {
                JSONObject results = response.getJSONArray("results").getJSONObject(0); // Only returns one user, so we can just get the first one
                JSONObject name = results.getJSONObject("name");
                JSONObject location = results.getJSONObject("location");

                String firstName = name.getString("first");
                String lastName = name.getString("last");

                this.name = firstName + " " + lastName;

                double latitude = location.getJSONObject("coordinates").getDouble("latitude");
                double longitude = location.getJSONObject("coordinates").getDouble("longitude");

                catLocation = new LatLng(latitude, longitude);

                TextView textView = binding.catInfo;
                textView.setText(this.name);
            } catch (Exception ignored) { }
        }, error -> { });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(jsonObjectRequest);
    }

    public LatLng getCatLocation() {
        return catLocation;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getName() {
        return name;
    }
}