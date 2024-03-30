package com.example.cattinder.Controllers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Activities.SignUpActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;

public class LoginController implements View.OnClickListener {
    private final AppCompatActivity activity;
    private final AuthViewModel authViewModel;

    private final EditText emailInput;
    private final EditText passwordInput;

    public LoginController(
        AppCompatActivity activity,
        AuthViewModel authViewModel,
        Button loginButton,
        Button navigateToRegisterViewButton,
        EditText emailInput,
        EditText passwordInput
    ) {
        loginButton.setOnClickListener(this);
        navigateToRegisterViewButton.setOnClickListener(this);

        this.activity = activity;
        this.authViewModel = authViewModel;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
    }

    @Override
    public void onClick(View button) {
        getSystemService(activity, InputMethodManager.class)
            .hideSoftInputFromWindow(button.getWindowToken(), 0);

        if (button.getId() == R.id.login_button) {
            authViewModel.tryLogIn(
                activity,
                emailInput.getText().toString(),
                passwordInput.getText().toString()
            );
        } else if (button.getId() == R.id.navigate_to_register_button) {
            activity.startActivity(new Intent(activity, SignUpActivity.class));
            activity.finish();
        }
    }
}