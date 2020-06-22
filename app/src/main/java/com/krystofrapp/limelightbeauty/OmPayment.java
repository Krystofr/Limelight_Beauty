package com.krystofrapp.limelightbeauty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

public class OmPayment extends AppCompatActivity {

    TextView priceTv;
    private CardView btnOm;
    private TextInputEditText transactionId, omPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_om_payment);

        String message = getIntent().getStringExtra(BookAppointment.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(message);

        btnOm = findViewById(R.id.btn_om);
        btnOm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                omPhoneNumber = findViewById(R.id.om_phone_number);
                transactionId = findViewById(R.id.transaction_id);
                final String om_phone = omPhoneNumber.getText().toString().trim();
                final String transId = transactionId.getText().toString().trim();

                if (om_phone.isEmpty()) {
                    omNumberErrorBuilder();
                } else if (transId.isEmpty()) {
                    transIdErrorBuilder();
                } else {
                    paymentBuilder();
                }
            }
        });
    }

    private void paymentBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("PAYMENT ALERT!")
                .setMessage("Are you sure you've entered the correct •Transaction ID• of your Booked Appointment?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new AlertDialog.Builder(OmPayment.this)
                                .setTitle("CONFIRMATION!")
                                .setCancelable(false)
                                .setMessage("Please note that, if you entered a wrong ''Transaction ID'', your •Booked Appointment• will not be valid and may be subject to cancellation. DO YOU WISH TO PROCEED TO SUBMISSION.?")
                                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(OmPayment.this, "PAYMENT RECORDED! You will receive a confirmation Email shortly.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(OmPayment.this, Homepage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(OmPayment.this, "Please verify before submission.", Toast.LENGTH_LONG).show();
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();

                    }

                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(OmPayment.this, "Please verify before submission.", Toast.LENGTH_LONG).show();

                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    private void omNumberErrorBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Please enter your •Orange Money• account number for this Payment.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void transIdErrorBuilder() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("Please, get the ''Transaction ID'' of the payment sms you received from •Orange Money• after making this payment and enter it below.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}
