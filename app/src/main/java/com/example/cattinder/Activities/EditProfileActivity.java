package com.example.cattinder.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Controllers.UpdateProfileController;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.databinding.EditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);
        EditProfileBinding binding = EditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new UpdateProfileController(
            this,
            AuthViewModel.getInstance(),
            binding.updateButton,
            binding.updateNavigateBackButton,
            binding.updateNameInputField,
            binding.updateCurrentPasswordInputField,
            binding.updateNewPasswordInputField
        );
    }
}
