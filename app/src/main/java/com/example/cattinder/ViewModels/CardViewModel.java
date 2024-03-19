package com.example.cattinder.ViewModels;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cattinder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class CardViewModel {
    private final Context context;

    public CardViewModel(Context context) {
        this.context = context;
    }

    public void newCat() {
        try {
            loadCatImage(new URL("https://api.thecatapi.com/v1/images/search"));
            loadUserData(new URL("https://randomuser.me/api/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadCatImage(URL url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url.toString(), null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        String imageUrl = jsonObject.getString("url");

                        ImageView imageView = ((Activity) context).findViewById(R.id.card_image);
                        Glide.with(context).load(imageUrl).into(imageView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void loadUserData(URL url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                response -> {
                    try {
                        JSONObject results = response.getJSONArray("results").getJSONObject(0); // Only returns one user, so we can just get the first one
                        JSONObject name = results.getJSONObject("name");
                        JSONObject location = results.getJSONObject("location");

                        String firstName = name.getString("first");
                        String lastName = name.getString("last");
                        String city = location.getString("city");

                        TextView textView = ((Activity) context).findViewById(R.id.cat_info);
                        textView.setText(firstName + " " + lastName + " from " + city);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}