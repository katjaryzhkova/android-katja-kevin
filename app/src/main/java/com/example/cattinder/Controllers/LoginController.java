package com.example.cattinder.Controllers;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cattinder.MainActivity;
import com.example.cattinder.R;
import com.example.cattinder.ViewModels.AuthViewModel;

public class LoginController implements View.OnClickListener {
    private MainActivity main;
    private AuthViewModel authViewModel;

    private EditText emailInput;
    private EditText passwordInput;

    public LoginController(MainActivity main, AuthViewModel authViewModel, Button loginButton, Button navigateToRegisterViewButton, EditText emailInput, EditText passwordInput) {
        loginButton.setOnClickListener(this);
        navigateToRegisterViewButton.setOnClickListener(this);

        this.main = main;
        this.authViewModel = authViewModel;
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
    }

    @Override
    public void onClick(View button) {
        if (button.getId() == R.id.login_button) {
            try {
                authViewModel.tryLogIn(
                        emailInput.getText().toString(),
                        passwordInput.getText().toString()
                );
            } catch(Exception e) {
                System.out.print(e.toString());
                // ...
            }
        } else if (button.getId() == R.id.navigate_to_register_button) {
            main.navigateSignUp();
        }
    }
}