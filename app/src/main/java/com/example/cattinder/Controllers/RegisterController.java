package com.example.cattinder.Controllers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Activities.SignInActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;

public class RegisterController implements View.OnClickListener {
    private final AppCompatActivity activity;
    private final AuthViewModel authViewModel;

    private final EditText fullNameInput;
    private final EditText emailInput;
    private final EditText passwordInput;
    private final EditText confirmPasswordInput;

    public RegisterController(
        AppCompatActivity activity,
        AuthViewModel authViewModel,
        Button registerButton,
        Button navigateToSignInViewButton,
        EditText fullNameInput,
        EditText emailInput,
        EditText passwordInput,
        EditText confirmPasswordInput
    ) {
        registerButton.setOnClickListener(this);
        navigateToSignInViewButton.setOnClickListener(this);

        this.activity = activity;
        this.authViewModel = authViewModel;
        this.fullNameInput = fullNameInput;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.confirmPasswordInput = confirmPasswordInput;
    }

    @Override
    public void onClick(View button) {
        InputMethodManager inputMethodManager = getSystemService(activity, InputMethodManager.class);
        assert inputMethodManager != null;

        inputMethodManager.hideSoftInputFromWindow(button.getWindowToken(), 0);

        if (button.getId() == R.id.register_button) {
            authViewModel.tryRegister(
                activity,
                fullNameInput.getText().toString(),
                emailInput.getText().toString(),
                passwordInput.getText().toString(),
                confirmPasswordInput.getText().toString()
            );
        } else if (button.getId() == R.id.already_have_account_button) {
            activity.startActivity(new Intent(activity, SignInActivity.class));
            activity.finish();
        }
    }
}