package com.krystofrapp.limelightbeauty.ui.account;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krystofrapp.limelightbeauty.R;

public class UpgradeAccount extends AppCompatActivity {

    private AppCompatImageButton backBtn;
    private TextInputEditText busName, phoneNumber, bsAddress, nkName;
    private SwitchMaterial facial, nails, hair, massage;
    private AppCompatButton register;
    private FirebaseFirestore upgradeAccountDetails = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_account);

        backBtn = findViewById(R.id.img_btn);
        //Fields
        busName = findViewById(R.id.business_name);
        phoneNumber = findViewById(R.id.phone);
        bsAddress = findViewById(R.id.address);
        nkName = findViewById(R.id.nk_name);
        //Services
        facial = findViewById(R.id.facials);
        nails = findViewById(R.id.nails);
        hair = findViewById(R.id.hair);
        massage = findViewById(R.id.massage);

        register = findViewById(R.id.register);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpgradeAccount.this, AccountFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 123);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerBusiness();
            }
        });
    }

    private void registerBusiness() {
        String bus_name = busName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String address = bsAddress.getText().toString().trim();
        String nk_name = nkName.getText().toString().trim();

       /* String face = facial.getText().toString().trim();
        String nail = nails.getText().toString().trim();
        String hair_dress = hair.getText().toString().trim();
        String massage_sess = massage.getText().toString().trim();  */


        if (bus_name.isEmpty()) {
            Toast.makeText(this, "Set your business name", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            Toast.makeText(this, "Enter your business phone number", Toast.LENGTH_SHORT).show();
        } else if (phone.length() > 9) {
            Toast.makeText(this, "Phone number too long. Should be 9 digits exact!", Toast.LENGTH_LONG).show();
        } else if (phone.length() < 9) {
            Toast.makeText(this, "Phone number too short. Should be 9 digits exact!.", Toast.LENGTH_LONG).show();
        } else if (address.isEmpty()) {
            Toast.makeText(this, "Set your business address", Toast.LENGTH_SHORT).show();
        } else if (nk_name.isEmpty()) {
            Toast.makeText(this, "Enter a nickname", Toast.LENGTH_SHORT).show();
        } else if (!facial.isChecked() && !nails.isChecked() && !hair.isChecked() && !massage.isChecked()) {
            Toast.makeText(this, "You must enable at least one service you offer.", Toast.LENGTH_SHORT).show();
        } else if (bus_name.isEmpty() || phone.isEmpty() || address.isEmpty() || nk_name.isEmpty()) {
            Toast.makeText(this, "All details are required to create a Business Account", Toast.LENGTH_SHORT).show();
        } else {

            final ProfileDetails businessDetails = new ProfileDetails(bus_name, phone, address, nk_name);
            upgradeAccountDetails.collection("Business Accounts").document("Account Details").set(businessDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            successDialogBuilder();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showFailureDialog();
                        }
                    });
        }
    }

    private void successDialogBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage("Upgrade successful.")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(UpgradeAccount.this, AccountFragment.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        alertDialogBuilder.show();
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(UpgradeAccount.this)
                .setMessage("Failed To Process Upgrade! Try Again.")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }
}
