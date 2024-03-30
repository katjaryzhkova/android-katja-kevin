package com.example.cattinder.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.cattinder.Activities.HistoryActivity;
import com.example.cattinder.Activities.MainActivity;
import com.example.cattinder.Activities.PreferencesActivity;
import com.example.cattinder.Activities.ProfileActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.databinding.FragmentDrawerBinding;

public class DrawerFragment extends Fragment {
    private static final String FULL_NAME_ARG = "fullName";
    private static final String PROFILE_BITMAP_ARG = "profilePicture";
    private AppCompatActivity activity;
    private String fullName;
    private Bitmap profilePicture;

    public DrawerFragment() {
        // Required empty public constructor
        super(R.layout.fragment_drawer);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DrawerFragment.
     */
    public static DrawerFragment newInstance(AppCompatActivity activity, AuthViewModel auth, Bitmap profilePicture) {
        DrawerFragment fragment = new DrawerFragment();
        Bundle args = new Bundle();
        args.putString(FULL_NAME_ARG, auth.getName(activity));
        args.putParcelable(PROFILE_BITMAP_ARG, profilePicture);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = requireArguments();
        fullName = args.getString(FULL_NAME_ARG);
        profilePicture = args.getParcelable(PROFILE_BITMAP_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDrawerBinding binding = FragmentDrawerBinding.inflate(inflater, container, false);

        binding.drawerProfileName.setText(fullName);

        binding.drawerUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ProfileActivity.class));
                activity.finish();
            }
        });

        binding.drawerNavigateHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        });

        binding.drawerNavigateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ProfileActivity.class));
                activity.finish();
            }
        });

        binding.drawerNavigateHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, HistoryActivity.class));
                activity.finish();
            }
        });

        binding.drawerNavigatePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, PreferencesActivity.class));
                activity.finish();
            }
        });

        if (this.profilePicture != null) {
            binding.drawerProfilePicture.setImageBitmap(this.profilePicture);
        }

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.scrollView.setBackgroundColor(Color.BLACK);
            binding.drawerNavigateHome.setCardBackgroundColor(Color.DKGRAY);
            binding.drawerNavigateProfile.setCardBackgroundColor(Color.DKGRAY);
            binding.drawerNavigateHistory.setCardBackgroundColor(Color.DKGRAY);
            binding.drawerNavigatePreferences.setCardBackgroundColor(Color.DKGRAY);
        } else {
            binding.scrollView.setBackgroundColor(Color.WHITE);
            binding.drawerNavigateHome.setCardBackgroundColor(Color.LTGRAY);
            binding.drawerNavigateProfile.setCardBackgroundColor(Color.LTGRAY);
            binding.drawerNavigateHistory.setCardBackgroundColor(Color.LTGRAY);
            binding.drawerNavigatePreferences.setCardBackgroundColor(Color.LTGRAY);
        }

        return binding.getRoot();
    }
}