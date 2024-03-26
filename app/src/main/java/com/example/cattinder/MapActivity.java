package com.example.cattinder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.util.Locale;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Set the default zoom and location
        MapController mapController = (MapController) map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Button calculateDistanceButton = findViewById(R.id.calculate_location_button);
        calculateDistanceButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            } else {
                calculateDistance();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                calculateDistance();
            } else {
                finish();
            }
        }
    }

    private void calculateDistance() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            try {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double userLatitude = location.getLatitude();
                    double userLongitude = location.getLongitude();

                    double catLatitude = getIntent().getDoubleExtra("latitude", 0);
                    double catLongitude = getIntent().getDoubleExtra("longitude", 0);

                    float[] results = new float[1];
                    Location.distanceBetween(userLatitude, userLongitude, catLatitude, catLongitude, results);

                    TextView distanceTextView = findViewById(R.id.distance_text_view);
                    distanceTextView.setText(String.format(Locale.getDefault(), "Distance: %.2f km", results[0] / 1000));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}