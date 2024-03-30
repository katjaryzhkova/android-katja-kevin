package com.example.cattinder.Tasks;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Widgets.CatTinderWidget;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private AppCompatActivity activity;
    private Context context;
    private String url;
    private ImageView imageView;
    private RemoteViews remoteViews;
    private int imageViewId;

    public ImageLoadTask(AppCompatActivity activity, String url, ImageView imageView) {
        this.activity = activity;
        this.url = url;
        this.imageView = imageView;
    }

    // This constructor is used for loading images into widgets
    public ImageLoadTask(Context context, String url, RemoteViews remoteViews, int imageViewId) {
        this.context = context;
        this.url = url;
        this.remoteViews = remoteViews;
        this.imageViewId = imageViewId;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            if (activity != null) {
                Snackbar.make(activity.findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null) {
            if (imageView != null) {
                imageView.setImageBitmap(result);
            }

            if (remoteViews != null && context != null) {
                remoteViews.setImageViewBitmap(imageViewId, result);
                AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, CatTinderWidget.class), remoteViews);
            }
        }
    }

}