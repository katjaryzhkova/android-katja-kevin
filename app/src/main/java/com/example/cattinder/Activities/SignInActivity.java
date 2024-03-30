package com.example.cattinder.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Controllers.LoginController;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.databinding.LoginBinding;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        LoginBinding binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new LoginController(
            this,
            AuthViewModel.getInstance(),
            binding.loginButton,
            binding.navigateToRegisterButton,
            binding.loginEmailInputField,
            binding.loginPasswordInputField
        );
    }
}
