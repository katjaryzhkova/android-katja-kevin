package com.example.cattinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cattinder.Controllers.LoginController;
import com.example.cattinder.Controllers.MainController;
import com.example.cattinder.Controllers.ProfileController;
import com.example.cattinder.Controllers.RegisterController;
import com.example.cattinder.Controllers.UpdateProfileController;
import com.example.cattinder.Tasks.ImageLoadTask;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.example.cattinder.ViewModels.CardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private CardViewModel cardViewModel;
    private AuthViewModel authViewModel;
    private final int PICK_IMAGE_REQUEST = 71;

    public void snackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void setUpDrawer(
            TextView nameText,
            LinearLayout profile,
            CardView homeLink,
            CardView profileLink,
            CardView settingsLink,
            ImageView profilePicture
    ) {
        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        nameText.setText(authViewModel.getName());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        homeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateHome();
            }
        });

        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        settingsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });

        setImageFromUri(profilePicture, authViewModel.getPhoto());
    }

    public void navigateHome() {
        setContentView(R.layout.homescreen);

        ImageButton dislikeButton = findViewById(R.id.dislike_button);
        ImageButton likeButton = findViewById(R.id.like_button);
        ImageButton mapButton = findViewById(R.id.map_button);
        FloatingActionButton openDrawerButton = findViewById(R.id.home_open_drawer_button);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        View cardView = findViewById(R.id.card_stack);

        if (cardViewModel == null) {
            cardViewModel = new CardViewModel(this);

//            cardViewModel.newCat(); // Initial cat
        }

        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        TextView nameText = findViewById(R.id.name_text);
        LinearLayout profile = findViewById(R.id.user_profile);
        CardView homeLink = findViewById(R.id.home_navigate_home);
        CardView profileLink = findViewById(R.id.home_navigate_profile);
        CardView settingsLink = findViewById(R.id.home_navigate_preferences);
        ImageView profilePicture = findViewById(R.id.home_profile_picture);


        MainController mainController = new MainController(
                cardViewModel,
                dislikeButton,
                likeButton,
                mapButton,
                cardView,
                openDrawerButton,
                drawerLayout
        );

        setUpDrawer(
                nameText,
                profile,
                homeLink,
                profileLink,
                settingsLink,
                profilePicture
        );
    }

    public void navigateToProfile() {
        setContentView(R.layout.profile);

        LinearLayout logoutButton = findViewById(R.id.sign_out_linear_layout);
        LinearLayout deleteAccountButton = findViewById(R.id.delete_account_linear_layout);
        TextView name = findViewById(R.id.profile_name);
        FloatingActionButton openDrawerButton = findViewById(R.id.profile_open_drawer_button);
        ImageView profilePicture = findViewById(R.id.profile_picture);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        TextView nameText = findViewById(R.id.name_text_2);
        LinearLayout profile = findViewById(R.id.profile_user_profile);
        CardView homeLink = findViewById(R.id.profile_navigate_home);
        CardView profileLink = findViewById(R.id.profile_navigate_profile);
        CardView settingsLink = findViewById(R.id.profile_navigate_preferences);
        ImageView navProfilePicture = findViewById(R.id.profile_profile_picture);

        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        new ProfileController(
                logoutButton,
                deleteAccountButton,
                name,
                authViewModel,
                profilePicture,
                openDrawerButton,
                drawerLayout,
                this
        );

        setUpDrawer(
                nameText,
                profile,
                homeLink,
                profileLink,
                settingsLink,
                navProfilePicture
        );
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
        EditText fullnameInput = findViewById(R.id.register_name_input_field);
        EditText emailInput = findViewById(R.id.register_email_input_field);
        EditText passwordInput = findViewById(R.id.register_password_input_field);
        EditText confirmPasswordInput = findViewById(R.id.confirm_password_input_field);

        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        RegisterController registerController = new RegisterController(
                this,
                authViewModel,
                registerButton,
                navigateToSignInViewButton,
                fullnameInput,
                emailInput,
                passwordInput,
                confirmPasswordInput
        );
    }

    public void navigateToUpdateProfile() {
        setContentView(R.layout.edit_profile);

        Button updateButton = findViewById(R.id.update_button);
        FloatingActionButton navigateBackButton = findViewById(R.id.update_navigate_back_button);
        EditText fullnameInput = findViewById(R.id.update_name_input_field);
        EditText emailInput = findViewById(R.id.update_email_input_field);
        EditText passwordInput = findViewById(R.id.update_current_password_input_field);
        EditText newPasswordInput = findViewById(R.id.update_new_password_input_field);

        if (authViewModel == null) {
            authViewModel = new AuthViewModel(this);
        }

        UpdateProfileController updateProfileController = new UpdateProfileController(
                this,
                authViewModel,
                updateButton,
                navigateBackButton,
                fullnameInput,
                emailInput,
                passwordInput,
                newPasswordInput
        );
    }

    public void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            authViewModel.updatePhoto(data.getData());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkMode = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        if (auth.getCurrentUser() != null) {
            navigateHome();
        } else {
            navigateSignIn();
        }
    }

    public void setImageFromUri(ImageView imageView, Uri src) {
        if (src != null) {
            new ImageLoadTask(this, src.toString(), imageView).execute();
        }
    }
}