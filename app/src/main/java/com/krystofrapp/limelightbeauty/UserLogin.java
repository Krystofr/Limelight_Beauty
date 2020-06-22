package com.krystofrapp.limelightbeauty;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class UserLogin extends AppCompatActivity {

    ContentLoadingProgressBar progressBar;
    private TextView tv_to_signUp, pwd_reset;
    private CardView login;
    private TextInputEditText email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.cl_progressbar);
        progressBar.setVisibility(View.GONE);

        tv_to_signUp = findViewById(R.id.to_sign_up);
        pwd_reset = findViewById(R.id.reset_pwd);
        login = findViewById(R.id.customer_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == login) {
                    loginCustomer();
                }
            }
        });

        tv_to_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignUp();
            }
        });

        pwd_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        final String inputEmail = email.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(UserLogin.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();


        auth.sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (inputEmail.isEmpty()) {
                            Toast.makeText(UserLogin.this, "Please enter your registered email address.", Toast.LENGTH_SHORT).show();
                        } else if (!task.isSuccessful()) {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Toast.makeText(UserLogin.this, "User doesn't exist.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            resetPasswordBuilder();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void toSignUp() {
        Intent intent = new Intent(UserLogin.this, SignUpPage.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    private void loginCustomer() {
        String cust_email = email.getText().toString().trim();
        String cust_pwd = password.getText().toString().trim();

        //Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error);
        //customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());

        if (cust_email.isEmpty()) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        } else if (cust_pwd.isEmpty()) {
            Toast.makeText(this, "Password required to login", Toast.LENGTH_SHORT).show();
        } else if (cust_email.isEmpty() && cust_pwd.isEmpty()) {
            Toast.makeText(this, "Enter your email and password to login.", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(cust_email, cust_pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(UserLogin.this, Homepage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(UserLogin.this, "Sign-in Successful", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                login.setEnabled(false);
                                email.setEnabled(false);
                                password.setEnabled(false);
                            } else if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Toast.makeText(UserLogin.this, "Wrong or invalid email.", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    Toast.makeText(UserLogin.this, "Invalid or wrong password.", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(UserLogin.this, "Unable to Sign In. Try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    private void resetPasswordBuilder() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Password Reset.");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Reset password instructions has been sent to your email")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alertDialogBuilder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        login.setEnabled(true);
        email.setEnabled(true);
        password.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
