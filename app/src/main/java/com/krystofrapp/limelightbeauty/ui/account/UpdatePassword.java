package com.krystofrapp.limelightbeauty.ui.account;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.krystofrapp.limelightbeauty.R;

public class UpdatePassword extends AppCompatActivity {

    TextInputEditText update_email;
    private AppCompatButton update_password;
    private FirebaseAuth auth;
    private AppCompatImageButton btnToAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        auth = FirebaseAuth.getInstance();
        update_password = findViewById(R.id.update_password);
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserPassword();
            }
        });

        update_email = findViewById(R.id.update_email);

        btnToAccount = findViewById(R.id.img_btn_bk);
        btnToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdatePassword.this, AccountFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void updateUserPassword() {
        final String email = update_email.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(UpdatePassword.this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (email.isEmpty()) {
                            Toast.makeText(UpdatePassword.this, "Please enter your registered email address.", Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            resetPasswordBuilder();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "This email isn't valid! Please enter a registered email address.", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void resetPasswordBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Password Reset.");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Reset password instructions has been sent to your email")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(UpdatePassword.this, AccountFragment.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        alertDialogBuilder.show();
    }

}
