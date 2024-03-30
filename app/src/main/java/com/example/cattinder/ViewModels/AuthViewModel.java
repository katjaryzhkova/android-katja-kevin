package com.example.cattinder.ViewModels;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.Activities.MainActivity;
import com.example.cattinder.Activities.ProfileActivity;
import com.example.cattinder.Activities.SignInActivity;
import com.example.cattinder.Models.LikedCat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
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

    private void snackbar(AppCompatActivity activity, String message) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public void tryLogIn(
        AppCompatActivity activity,
        String email,
        String password
    ) {
        if (auth.getCurrentUser() != null) {
            snackbar(activity, "You are already signed in.");
            return;
        }

        if (email == null || email.isEmpty()) {
            snackbar(activity, "An email is required to sign in.");
            return;
        }

        if (password == null || password.isEmpty()) {
            snackbar(activity, "A password is required to sign in.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    } else {
                        snackbar(activity, task.getException().getMessage());
                    }
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
            snackbar(activity, "You are already signed in.");
            return;
        }
        if (fullName == null || fullName.isEmpty()) {
            snackbar(activity, "A name is required.");
            return;
        }

        if (email == null || email.isEmpty()) {
            snackbar(activity, "An email is required.");
            return;
        }

        if (password == null || password.isEmpty()) {
            snackbar(activity, "A password is required.");
            return;
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            snackbar(activity, "Please confirm your password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            snackbar(activity, "The passwords do not match.");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        auth.signOut();
                                        tryLogIn(activity, email, password);
                                    }
                                });
                    } else {
                        snackbar(activity, task.getException().getMessage());
                    }
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
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {
                            auth.getCurrentUser()
                                .updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // main.navigateToProfile();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        snackbar(activity, "Failed uploading profile picture: " + e.getMessage());
                                    }
                                });
                        }
                    });
                }
            });
    }

    public void updateProfile(
        AppCompatActivity activity,
        String name,
        String oldPassword,
        String newPassword
    ) {
        if (name == null || name.isEmpty()) {
            snackbar(activity, "A name is required.");
            return;
        }

        if (oldPassword == null || oldPassword.isEmpty()) {
            snackbar(activity, "Your current account password is required.");
            return;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            snackbar(activity, "A new password is required.");
            return;
        }

        final FirebaseUser user = auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                activity.startActivity(new Intent(activity, ProfileActivity.class));
                                activity.finish();
                            }
                        });
                    }
                });
            }
        });
    }

    public Uri getPhoto() {
        return auth.getCurrentUser().getPhotoUrl();
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

        try {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
            user
                .reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                logOut(activity);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar(activity, e.getMessage());
                    }
                });
        } catch (Exception e) {
            snackbar(activity, e.getMessage());
        }
    }

    public String getName(
        AppCompatActivity activity
    ) {
        if (auth.getCurrentUser() == null) {
            snackbar(activity, "You are not signed in.");
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
            snackbar(activity, "You are not signed in.");
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
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    snackbar(activity, e.getMessage());
                }
            });
    }

    public CompletableFuture<ArrayList<LikedCat>> retrieveLikeHistory(
        AppCompatActivity activity
    ) {
        if (auth.getCurrentUser() == null) {
            snackbar(activity, "You are not signed in.");
            return null;
        }

        CompletableFuture<ArrayList<LikedCat>> futureHistory = new CompletableFuture<>();

        FirebaseFirestore
            .getInstance()
            .collection("history")
            .document(auth.getCurrentUser().getUid())
            .collection("liked-cats")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<LikedCat> history = new ArrayList<>();

                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();

                        history.addAll(snapshot.toObjects(LikedCat.class));
                    } else {
                        snackbar(activity, "An error occurred loading your history.");
                    }

                    futureHistory.complete(history);
                }
            });

        return futureHistory;
    }
}
