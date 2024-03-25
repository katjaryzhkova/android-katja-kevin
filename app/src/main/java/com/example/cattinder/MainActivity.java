package com.example.cattinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cattinder.Controllers.LoginController;
import com.example.cattinder.Controllers.MainController;
import com.example.cattinder.Controllers.RegisterController;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.ViewModels.CardViewModel;
import com.google.firebase.auth.FirebaseAuth;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private CardViewModel cardViewModel;
    private AuthViewModel authViewModel;

    public void navigateHome() {
        setContentView(R.layout.homescreen);

        ImageButton dislikeButton = findViewById(R.id.dislike_button);
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton mapButton = findViewById(R.id.map_button);

        View cardView = findViewById(R.id.card_stack);

        if (cardViewModel == null) {
            cardViewModel = new CardViewModel(this);
        }

        MainController mainController = new MainController(
                cardViewModel,
                dislikeButton,
                likeButton,
                mapButton,
                cardView
        );

        cardViewModel.newCat(); // Initial cat
    }

    public void navigateSignIn() {
        setContentView(R.layout.login);

        Button loginButton = findViewById(R.id.login_button);
        Button navigateToRegisterViewButton = findViewById(R.id.navigate_to_register_button);
        EditText emailInput = findViewById(R.id.login_email_input_field);
        EditText passwordInput = findViewById(R.id.login_password_input_field);

        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        LoginController loginController = new LoginController(
                this,
                authViewModel,
                loginButton,
                navigateToRegisterViewButton,
                emailInput,
                passwordInput
        );
    }

    public void navigateSignUp() {
        setContentView(R.layout.register);

        Button registerButton = findViewById(R.id.register_button);
        Button navigateToSignInViewButton = findViewById(R.id.already_have_account_button);
        EditText nameInput = findViewById(R.id.register_name_input_field);
        EditText emailInput = findViewById(R.id.register_email_input_field);
        EditText passwordInput = findViewById(R.id.register_password_input_field);
        EditText confirmPasswordInput = findViewById(R.id.register_confirm_password_input_field);

        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        RegisterController registerController = new RegisterController(
                this,
                authViewModel,
                registerButton,
                navigateToSignInViewButton,
                nameInput,
                emailInput,
                passwordInput,
                confirmPasswordInput
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            navigateHome();
        } else {
            navigateSignIn();
        }
    }
}
