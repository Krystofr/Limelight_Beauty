package com.krystofrapp.limelightbeauty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class PayerInfo extends AppCompatActivity {


    public static final String PRICE_MESSAGE = "Price Value";
    public static final String TIME_MESSAGE = "Time Slot";
    private CardView confirm;
    private TextInputEditText firstName, lastName, nicNo, phone;
    private TextView userReference;
    TextView priceTv, timeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer_info);

        String price_message = getIntent().getStringExtra(BookAppointment.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(price_message);

        String time_message = getIntent().getStringExtra(BookAppointment.TIME_MESSAGE);
        timeTv = findViewById(R.id.time_tv);
        timeTv.setText(time_message);


        confirm = findViewById(R.id.confirm_btn);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        nicNo = findViewById(R.id.nic);
        phone = findViewById(R.id.phone);

        userReference = findViewById(R.id.user_reference);
        userReference.setText("");
        userReference.setTextIsSelectable(true);
        if (userReference != null) {
            userReference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("copied text", userReference.getText());
                    clipboardManager.setPrimaryClip(clipData);
                    Snackbar.make(view, "Reference copied to clipboard!", Snackbar.LENGTH_LONG)
                            .setAction("GOT IT", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(PayerInfo.this, Homepage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.color_white))
                            .setDuration(Snackbar.LENGTH_INDEFINITE)
                            .setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark))
                            .show();
                }
            });
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPay();
            }
        });
    }

    private void confirmPay() {

        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String nic_no = nicNo.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();


        if (first_name.isEmpty()) {
            Toast.makeText(this, "Please enter your 1st Name", Toast.LENGTH_SHORT).show();
        } else if (last_name.isEmpty()) {
            Toast.makeText(this, "Please enter your Last Name", Toast.LENGTH_SHORT).show();
        } else if (nic_no.isEmpty()) {
            Toast.makeText(this, "Please enter your National ID Card number", Toast.LENGTH_LONG).show();
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your Mobile Money account phone number.", Toast.LENGTH_LONG).show();
        } else if (phoneNumber.length() < 9) {
            Toast.makeText(this, "Number too short. Should be 9 digits exact.", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.length() > 9) {
            Toast.makeText(this, "Number too Long. Should be 9 digits exact.", Toast.LENGTH_SHORT).show();
        } else {
           /*
            successDialogBuilder();
            String fn = first_name;
            String nic = nic_no;
            String infoConcat = fn + "" + nic;
            userReference.setText(infoConcat);
            */

            Intent intent = new Intent(PayerInfo.this, PaymentGateway.class);

            String priceText = priceTv.getText().toString().trim();
            intent.putExtra(PRICE_MESSAGE, priceText);

            String timeText = timeTv.getText().toString().trim();
            intent.putExtra(TIME_MESSAGE, timeText);

            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        }
    }

    private void successDialogBuilder() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Confirmation!");
        alertDialogBuilder.setMessage("We have provided you with a 'Unique Reference Code' below. " +
                " " +
                "Use it as 'Reference' when making your MoMo PayerInfo to " +
                "-680-735-174-. " +
                "Click Now to copy the Reference and paste when making the PayerInfo.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        alertDialogBuilder.show();
    }

}
