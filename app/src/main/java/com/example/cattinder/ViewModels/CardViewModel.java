package com.example.cattinder.ViewModels;

import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
import com.example.cattinder.R;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class CardViewModel {
    private final Context context;
    private LatLng catLocation;

    public CardViewModel(Context context) {
        this.context = context;
    }

    public void newCat() {
        try {
            ProgressBar progressBar = ((Activity) context).findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            loadCatImage(new URL("https://api.thecatapi.com/v1/images/search"), progressBar);
            loadUserData(new URL("https://randomuser.me/api/"), progressBar);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadCatImage(URL url, ProgressBar progressBar) {
        ImageView imageView = ((Activity) context).findViewById(R.id.card_image);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject(0);
                String imageUrl = jsonObject.getString("url");

                Glide.with(context).load(imageUrl).listener(new RequestListener<Drawable>() {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void loadUserData(URL url, ProgressBar progressBar) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, response -> {
            try {
                JSONObject results = response.getJSONArray("results").getJSONObject(0); // Only returns one user, so we can just get the first one
                JSONObject name = results.getJSONObject("name");
                JSONObject location = results.getJSONObject("location");

                String firstName = name.getString("first");
                String lastName = name.getString("last");
                String city = location.getString("city");

                double latitude = location.getJSONObject("coordinates").getDouble("latitude");
                double longitude = location.getJSONObject("coordinates").getDouble("longitude");

                catLocation = new LatLng(latitude, longitude);

                TextView textView = ((Activity) context).findViewById(R.id.cat_info);
                textView.setText(firstName + " " + lastName + " from " + city);

                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public LatLng getCatLocation() {
        return catLocation;
    }
}