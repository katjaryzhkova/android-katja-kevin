package com.example.cattinder.Controllers;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cattinder.MainActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;

public class RegisterController implements View.OnClickListener {
    private MainActivity main;
    private AuthViewModel authViewModel;

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    public RegisterController(MainActivity main, AuthViewModel authViewModel, Button registerButton, Button navigateToSignInViewButton, EditText nameInput, EditText emailInput, EditText passwordInput, EditText confirmPasswordInput) {
        registerButton.setOnClickListener(this);
        navigateToSignInViewButton.setOnClickListener(this);

        this.main = main;
        this.authViewModel = authViewModel;
        this.nameInput = nameInput;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.confirmPasswordInput = confirmPasswordInput;
    }

    @Override
    public void onClick(View button) {
        if (button.getId() == R.id.register_button) {
            try {
                authViewModel.tryRegister(
                        nameInput.getText().toString(),
                        emailInput.getText().toString(),
                        passwordInput.getText().toString(),
                        confirmPasswordInput.getText().toString()
                );
            } catch(Exception e) {
                System.out.print(e.toString());
                // ...
            }
        } else if (button.getId() == R.id.already_have_account_button) {
            main.navigateSignIn();
        }
    }
}