package com.example.cattinder.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cattinder.databinding.MapBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;

import java.util.Locale;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private MapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        binding = MapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        binding.map.setTileSource(TileSourceFactory.MAPNIK);

        // Set the default zoom and location
        MapController mapController = (MapController) binding.map.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);

        binding.backButton.setOnClickListener(v -> finish());

        binding.calculateLocationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                calculateDistance();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }
        });
    }

    private void calculateDistance() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            performLocationOperation();
        }
    }

    private void performLocationOperation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.distanceTextView.setVisibility(View.GONE);
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        binding.progressBar.setVisibility(View.GONE);
                        double latitude = getIntent().getDoubleExtra("latitude", 0);
                        double longitude = getIntent().getDoubleExtra("longitude", 0);

                        Location catLocation = new Location(LocationManager.GPS_PROVIDER);
                        catLocation.setLatitude(latitude);
                        catLocation.setLongitude(longitude);

                        Location userLocation = new Location(LocationManager.GPS_PROVIDER);
                        userLocation.setLatitude(location.getLatitude());
                        userLocation.setLongitude(location.getLongitude());

                        float distance = userLocation.distanceTo(catLocation) / 1000; // Convert to kilometers
                        binding.distanceTextView.setVisibility(View.VISIBLE);
                        binding.distanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", distance));
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                performLocationOperation();
            } else {
                finish();
            }
        }
    }
}