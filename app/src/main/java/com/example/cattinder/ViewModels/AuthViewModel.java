package com.example.cattinder.ViewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cattinder.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
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
    ) throws Exception {
        if (auth.getCurrentUser() != null) {
            throw new Exception("You are already signed in.");
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            main.navigateHome();
                        } else {
                            System.out.print(task.getException().toString());
                        }
                    }
                });
    }

    public void tryRegister(
            String name,
            String email,
            String password,
            String confirmPassword
    ) throws Exception {
        if (auth.getCurrentUser() != null) {
            throw new Exception("You are already signed in.");
        }

        if (!Objects.equals(password, confirmPassword)) {
            throw new Exception("The passwords do not match.");
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            main.navigateHome();
                        } else {
                            System.out.print(task.getException().toString());
                        }
                    }
                });
    }
}