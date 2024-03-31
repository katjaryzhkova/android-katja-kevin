package com.example.cattinder.ViewModels;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Activities.MainActivity;
import com.example.cattinder.Activities.ProfileActivity;
import com.example.cattinder.Activities.SignInActivity;
import com.example.cattinder.Models.LikedCat;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AuthViewModel {
    private static AuthViewModel instance;
    public static AuthViewModel getInstance() {
        if (instance == null) {
            instance = new AuthViewModel();
        }

        return instance;
    }

    private final FirebaseAuth auth;

    public AuthViewModel() {
        this.auth = FirebaseAuth.getInstance();
    }

    private void snackBar(AppCompatActivity activity, String message) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public void tryLogIn(
        AppCompatActivity activity,
        String email,
        String password
    ) {
        if (auth.getCurrentUser() != null) {
            snackBar(activity, "You are already signed in.");
            return;
        }

        if (email == null || email.isEmpty()) {
            snackBar(activity, "An email is required to sign in.");
            return;
        }

        if (password == null || password.isEmpty()) {
            snackBar(activity, "A password is required to sign in.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                } else {
                    snackBar(activity, Objects.requireNonNull(task.getException()).getMessage());
                }
            });
    }

    public void tryRegister(
        AppCompatActivity activity,
        String fullName,
        String email,
        String password,
        String confirmPassword
    ) {
        if (auth.getCurrentUser() != null) {
            snackBar(activity, "You are already signed in.");
            return;
        }
        if (fullName == null || fullName.isEmpty()) {
            snackBar(activity, "A name is required.");
            return;
        }

        if (email == null || email.isEmpty()) {
            snackBar(activity, "An email is required.");
            return;
        }

        if (password == null || password.isEmpty()) {
            snackBar(activity, "A password is required.");
            return;
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            snackBar(activity, "Please confirm your password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            snackBar(activity, "The passwords do not match.");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    auth.getCurrentUser()
                            .updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build())
                            .addOnCompleteListener(task1 -> {
                                auth.signOut();
                                tryLogIn(activity, email, password);
                            });
                } else {
                    snackBar(activity, Objects.requireNonNull(task.getException()).getMessage());
                }
            });
    }

    public void updatePhoto(
        AppCompatActivity activity,
        Uri path
    ) {
        StorageReference ref = FirebaseStorage
            .getInstance()
            .getReference()
            .child("images/" + UUID.randomUUID().toString());

        ref
            .putFile(path)
            .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> Objects.requireNonNull(auth.getCurrentUser())
                .updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build())
                .addOnSuccessListener(unused -> {
                    activity.startActivity(new Intent(activity, ProfileActivity.class));
                    activity.finish();
                })
                .addOnFailureListener(e -> snackBar(activity, "Failed uploading profile picture: " + e.getMessage()))));
    }

    public void updateProfile(
        AppCompatActivity activity,
        String name,
        String oldPassword,
        String newPassword
    ) {
        if (name == null || name.isEmpty()) {
            snackBar(activity, "A name is required.");
            return;
        }

        if (oldPassword == null || oldPassword.isEmpty()) {
            snackBar(activity, "Your current account password is required.");
            return;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            snackBar(activity, "A new password is required.");
            return;
        }

        final FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(task -> user.updatePassword(newPassword).addOnCompleteListener(task1 -> user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build()).addOnCompleteListener(task11 -> {
            activity.startActivity(new Intent(activity, ProfileActivity.class));
            activity.finish();
        })));
    }

    public Uri getPhoto() {
        return Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl();
    }

    public void logOut(
        AppCompatActivity activity
    ) {
        auth.signOut();
        activity.startActivity(new Intent(activity, SignInActivity.class));
        activity.finish();
    }

    public void deleteAccount(
        AppCompatActivity activity,
        String password
    ) {
        final FirebaseUser user = auth.getCurrentUser();

        if (password == null || password.isEmpty()) {
            snackBar(activity, "Your current account password is required.");
            return;
        }

        try {
            assert user != null;
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), password);
            user
                .reauthenticate(credential)
                .addOnSuccessListener(unused -> user.delete().addOnSuccessListener(unused1 -> logOut(activity)))
                .addOnFailureListener(e -> snackBar(activity, e.getMessage()));
        } catch (Exception e) {
            snackBar(activity, e.getMessage());
        }
    }

    public String getName(
        AppCompatActivity activity
    ) {
        if (MainActivity.isNetworkUnavailable(activity)) {
            Snackbar.make(activity.findViewById(android.R.id.content), "An internet connection is required to use CatTinder", 999999999).show();
            return "Unknown user";
        }

        if (auth.getCurrentUser() == null) {
            snackBar(activity, "You are not signed in.");
            return "";
        }

        return auth.getCurrentUser().getDisplayName();
    }

    public void addToLikeHistory(
        AppCompatActivity activity,
        String imageUrl,
        String name
    ) {
        if (auth.getCurrentUser() == null) {
            snackBar(activity, "You are not signed in.");
            return;
        }

        if (imageUrl.isEmpty() || name.isEmpty()) {
            return;
        }

        LikedCat cat = new LikedCat(imageUrl, name);

        FirebaseFirestore
            .getInstance()
            .collection("history")
            .document(auth.getCurrentUser().getUid())
            .collection("liked-cats")
            .add(cat)
            .addOnFailureListener(e -> snackBar(activity, e.getMessage()));
    }

    public CompletableFuture<ArrayList<LikedCat>> retrieveLikeHistory(
        AppCompatActivity activity
    ) {
        CompletableFuture<ArrayList<LikedCat>> futureHistory = new CompletableFuture<>();

        if (MainActivity.isNetworkUnavailable(activity)) {
            futureHistory.complete(null);
            return futureHistory;
        }

        if (auth.getCurrentUser() == null) {
            snackBar(activity, "You are not signed in.");

            futureHistory.complete(null);
            return futureHistory;
        }

        FirebaseFirestore
            .getInstance()
            .collection("history")
            .document(auth.getCurrentUser().getUid())
            .collection("liked-cats")
            .get()
            .addOnCompleteListener(task -> {
                ArrayList<LikedCat> history = new ArrayList<>();

                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();

                    history.addAll(snapshot.toObjects(LikedCat.class));
                } else {
                    snackBar(activity, "An error occurred loading your history.");
                }

                futureHistory.complete(history);
            });

        return futureHistory;
    }
}
