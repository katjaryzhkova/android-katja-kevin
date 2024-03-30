package com.example.cattinder.Controllers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.view.View;
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
        EditText fullnameInput,
        EditText passwordInput,
        EditText newPasswordInput
    ) {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSystemService(activity, InputMethodManager.class)
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);

                authViewModel.updateProfile(
                    activity,
                    fullnameInput.getText().toString(),
                    passwordInput.getText().toString(),
                    newPasswordInput.getText().toString()
                );
            }
        });

        navigateBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
