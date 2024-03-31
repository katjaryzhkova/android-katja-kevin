package com.example.cattinder.Controllers;

import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cattinder.Activities.EditProfileActivity;
import com.example.cattinder.Activities.ProfileActivity;
import com.example.cattinder.Tasks.ImageLoadTask;
import com.example.cattinder.ViewModels.AuthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileController {
    public ProfileController(
        LinearLayout logoutButton,
        LinearLayout deleteAccountButton,
        TextView name,
        AuthViewModel authViewModel,
        ImageView profilePicture,
        FloatingActionButton openDrawerButton,
        DrawerLayout drawerLayout,
        AppCompatActivity activity
    ) {
        logoutButton.setOnClickListener(v -> authViewModel.logOut(activity));

        deleteAccountButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Confirm your password to delete your account");

            final EditText input = new EditText(activity);

            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> authViewModel.deleteAccount(activity, input.getText().toString()));

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        openDrawerButton.setOnClickListener(v -> drawerLayout.open());

        name.setText(authViewModel.getName(activity));
        Uri src = authViewModel.getPhoto();
        if (src != null) {
            new ImageLoadTask(activity, src.toString(), profilePicture).execute();
        }

        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), ProfileActivity.PICK_IMAGE_REQUEST);
        });

        name.setOnClickListener(v -> activity.startActivity(new Intent(activity, EditProfileActivity.class)));
    }
}