package com.example.cattinder.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Controllers.RegisterController;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.databinding.RegisterBinding;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        RegisterBinding binding = RegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new RegisterController(
            this,
            AuthViewModel.getInstance(),
            binding.registerButton,
            binding.alreadyHaveAccountButton,
            binding.registerNameInputField,
            binding.registerEmailInputField,
            binding.registerPasswordInputField,
            binding.confirmPasswordInputField
        );
    }
}
