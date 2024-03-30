package com.example.cattinder.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cattinder.Controllers.ProfileController;
import com.example.cattinder.Tasks.LoadDrawerTask;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.databinding.ProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    public final static int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        ProfileBinding binding = ProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new ProfileController(
            binding.signOutLinearLayout,
            binding.deleteAccountLinearLayout,
            binding.profileName,
            AuthViewModel.getInstance(),
            binding.profilePicture,
            binding.profileOpenDrawerButton,
            binding.drawerLayout,
            this
        );

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        new LoadDrawerTask(this, fragmentTransaction, AuthViewModel.getInstance()).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            AuthViewModel.getInstance().updatePhoto(this, intent.getData());
        }
    }
}
