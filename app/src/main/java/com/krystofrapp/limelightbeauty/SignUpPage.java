package com.krystofrapp.limelightbeauty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;


public class SignUpPage extends AppCompatActivity {
    private static final String TAG = "SignUpPage";
    private FirebaseAuth mAuth;
    private CardView signUpBtn;
    private TextInputEditText name, email, password, confirmPassword;
    private TextView toLogin;
    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signUpBtn = findViewById(R.id.signup_btn);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.confirm_password);
        toLogin = findViewById(R.id.go_to_login);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this, UserLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        toLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                intentBuilder();
                return true;
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        final String username = name.getText().toString().trim();
        final String user_email = email.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        final String cpwd = confirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (user_email.isEmpty()) {
            Toast.makeText(this, "Your email is required", Toast.LENGTH_SHORT).show();
        } else if (pwd.isEmpty()) {
            Toast.makeText(this, "Your password is required", Toast.LENGTH_SHORT).show();
        } else if (pwd.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long in length", Toast.LENGTH_SHORT).show();
        } else if (cpwd.isEmpty()) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
        } else if (!cpwd.equals(pwd)) {
            Toast.makeText(this, "Passwords do not match! Please check again", Toast.LENGTH_SHORT).show();
        } else if (username.isEmpty() && user_email.isEmpty() && pwd.isEmpty() && cpwd.isEmpty()) {
            Toast.makeText(this, "All fields are required to sign up for an account!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(user_email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                callAlertDialogBuilder();
                                progressBar.setVisibility(View.GONE);
                                signUpBtn.setEnabled(false);
                                name.setEnabled(false);
                                email.setEnabled(false);
                                password.setEnabled(false);
                                confirmPassword.setEnabled(false);
                                toLogin.setEnabled(false);
                            } else if (!task.isSuccessful()) {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    Log.d(TAG, "onComplete: Malformed email");
                                    errorBuilder();
                                    progressBar.setVisibility(View.GONE);
                                } catch (FirebaseAuthUserCollisionException emailExists) {
                                    Log.d(TAG, "onComplete: emails exists");
                                    Toast.makeText(SignUpPage.this, "Email already exists. Login", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(SignUpPage.this, UserLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    private void callAlertDialogBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Sign Up successful.")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(SignUpPage.this, Homepage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        alertDialogBuilder.show();
    }

    private void errorBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Wrongly Formatted Email ID!");
        alertDialogBuilder.setMessage("Correct format: your_name@gmail.com, " + "OR" + " your_name@yahoo.com")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alertDialogBuilder.show();
    }

    private void intentBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Go to Login Page?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SignUpPage.this, UserLogin.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialogBuilder.show();
    }

    /*
        @Override
        public void onStart() {
            super.onStart();
            //if the user is already signed in
            //Update UI accordingly.
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(SignUpPage.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        */
    @Override
    protected void onResume() {
        super.onResume();
        signUpBtn.setEnabled(true);
        name.setEnabled(true);
        email.setEnabled(true);
        password.setEnabled(true);
        confirmPassword.setEnabled(true);
        toLogin.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

/*
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //Update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(SignUpPage.this, Homepage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           // startActivity(intent);
        }
        else {
            Intent intent = new Intent(SignUpPage.this, UserLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           // startActivity(intent);
        }

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (googleSignInAccount != null) {
                callAlertDialogBuilder();
            } else {
                Toast.makeText(this, "User doesn't exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }
            *///