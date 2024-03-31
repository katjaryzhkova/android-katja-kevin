package com.example.cattinder.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cattinder.Fragments.DrawerFragment;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadDrawerTask extends AsyncTask<Void, Void, Bitmap> {
    private final AppCompatActivity activity;
    private final String url;
    private final FragmentTransaction transaction;
    private final AuthViewModel authViewModel;

    public LoadDrawerTask(AppCompatActivity activity, FragmentTransaction transaction, AuthViewModel authViewModel) {
        Uri uri = authViewModel.getPhoto();
        if (uri != null) {
            this.url = uri.toString();
        } else {
            this.url = "";
        }
        this.activity = activity;
        this.transaction = transaction;
        this.authViewModel = authViewModel;
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
            return ImageLoadTask.resize(BitmapFactory.decodeStream(input), 128, 128);
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        try {
            if (activity.findViewById(R.id.drawer_fragment) != null) {
                transaction.replace(R.id.drawer_fragment, DrawerFragment.newInstance(activity, authViewModel, result));
                transaction.commit();
            }
        } catch (Exception ignored) { }
    }

}