package com.example.cattinder.Controllers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.cattinder.MainActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;

public class RegisterController implements View.OnClickListener {
    private final MainActivity main;
    private final AuthViewModel authViewModel;

    private final EditText fullnameInput;
    private final EditText emailInput;
    private final EditText passwordInput;
    private final EditText confirmPasswordInput;

    public RegisterController(MainActivity main, AuthViewModel authViewModel, Button registerButton, Button navigateToSignInViewButton, EditText fullnameInput, EditText emailInput, EditText passwordInput, EditText confirmPasswordInput) {
        registerButton.setOnClickListener(this);
        navigateToSignInViewButton.setOnClickListener(this);

        this.main = main;
        this.authViewModel = authViewModel;
        this.fullnameInput = fullnameInput;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.confirmPasswordInput = confirmPasswordInput;
    }

    @Override
    public void onClick(View button) {
        getSystemService(main, InputMethodManager.class)
            .hideSoftInputFromWindow(button.getWindowToken(), 0);

        if (button.getId() == R.id.register_button) {
            authViewModel.tryRegister(
                fullnameInput.getText().toString(),
                emailInput.getText().toString(),
                passwordInput.getText().toString(),
                confirmPasswordInput.getText().toString()
            );
        } else if (button.getId() == R.id.already_have_account_button) {
            main.navigateSignIn();
        }
    }
}