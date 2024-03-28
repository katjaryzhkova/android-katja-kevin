package com.example.cattinder.ViewModels;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;

public class AuthViewModel extends AppCompatActivity {
    private FirebaseAuth auth;
    private MainActivity main;

    public AuthViewModel(MainActivity main) {
        this.auth = FirebaseAuth.getInstance();
        this.main = main;
    }

    public void tryLogIn(
            String email,
            String password
    ) {
        if (auth.getCurrentUser() != null) {
            main.snackbar("You are already signed in.");
            return;
        }

        if (email == null || email.equals("")) {
            main.snackbar("An email is required to sign in.");
            return;
        }

        if (password == null || password.equals("")) {
            main.snackbar("A password is required to sign in.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            main.navigateHome();
                        } else {
                            main.snackbar(task.getException().getMessage());
                        }
                    }
                });
    }

    public void tryRegister(
            String fullname,
            String email,
            String password,
            String confirmPassword
    ) {
        if (auth.getCurrentUser() != null) {
            main.snackbar("You are already signed in.");
            return;
        }
        if (fullname == null || fullname.equals("")) {
            main.snackbar("A name is required.");
            return;
        }

        if (email == null || email.equals("")) {
            main.snackbar("An email is required.");
            return;
        }

        if (password == null || password.equals("")) {
            main.snackbar("A password is required.");
            return;
        }

        if (confirmPassword == null || confirmPassword.equals("")) {
            main.snackbar("Please confirm your password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            main.snackbar("The passwords do not match.");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser()
                                    .updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullname).build())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            auth.signOut();
                                            tryLogIn(email, password);
                                        }
                                    });
                        } else {
                            main.snackbar(task.getException().getMessage());
                        }
                    }
                });
    }

    public void updatePhoto(
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
                                                main.navigateToProfile();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                main.snackbar("Failed uploading profile picture: " + e.getMessage());
                                            }
                                        });
                            }
                        });
                    }
                });
    }

    public void updateProfile(
            String name,
            String email,
            String oldPassword,
            String newPassword
    ) {
        if (name == null || name.equals("")) {
            main.snackbar("A name is required.");
            return;
        }

        if (email == null || email.equals("")) {
            main.snackbar("An email is required.");
            return;
        }

        if (oldPassword == null || oldPassword.equals("")) {
            main.snackbar("Your current account password is required.");
            return;
        }

        if (newPassword == null || newPassword.equals("")) {
            main.snackbar("A new password is required.");
            return;
        }

        final FirebaseUser user = auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        main.navigateToProfile();
                                    }
                                });
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

    public void logOut() {
        auth.signOut();
        main.navigateSignIn();
    }

    public void deleteAccount(
            String password
    ) {
        final FirebaseUser user = auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        logOut();
                    }
                });
            }
        });
    }

    public String getName() {
        if (auth.getCurrentUser() == null) {
            main.snackbar("You are not signed in.");
            return "";
        }

        return auth.getCurrentUser().getDisplayName();
    }
}
