package com.example.cattinder.Controllers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.ViewModels.AuthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateProfileController {
    public UpdateProfileController(
        AppCompatActivity activity,
        AuthViewModel authViewModel,
        Button updateButton,
        FloatingActionButton navigateBackButton,
        EditText fullNameInput,
        EditText passwordInput,
        EditText newPasswordInput
    ) {
        updateButton.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = getSystemService(activity, InputMethodManager.class);
            assert inputMethodManager != null;

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

            authViewModel.updateProfile(
                activity,
                fullNameInput.getText().toString(),
                passwordInput.getText().toString(),
                newPasswordInput.getText().toString()
            );
        });

        navigateBackButton.setOnClickListener(v -> activity.finish());
    }
}
