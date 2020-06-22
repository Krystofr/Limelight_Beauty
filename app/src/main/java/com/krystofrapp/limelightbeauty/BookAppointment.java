package com.krystofrapp.limelightbeauty;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class BookAppointment extends AppCompatActivity {

    public static final String PRICE_MESSAGE = "Price Value";
    public static final String TIME_MESSAGE = "Time Slot";
    private CardView confirmAppointment;
    private CalendarView calendarView;
    private TextView dateHolder;
    private TextInputEditText edtTime;
    private TimePickerDialog pickerDialog;
    TextView priceTv;
    ProgressBar progressBar;
    private TextView timeSlot1, timeSlot2, timeSlot3, timeSlot4, timeSlot5, timeSlot6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        String message = getIntent().getStringExtra(ItemsPreviewActivity.PRICE_MESSAGE);
        priceTv = findViewById(R.id.price_tv);
        priceTv.setText(message);

        timeSlot1 = findViewById(R.id.time_slot1);
        timeSlot2 = findViewById(R.id.time_slot2);
        timeSlot3 = findViewById(R.id.time_slot3);
        timeSlot4 = findViewById(R.id.time_slot4);
        timeSlot5 = findViewById(R.id.time_slot5);
        timeSlot6 = findViewById(R.id.time_slot6);

        edtTime = findViewById(R.id.edt_timePlace);
        edtTime.setInputType(InputType.TYPE_NULL);

        timeSlot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot1 = timeSlot1.getText().toString().trim();
                edtTime.setText(slot1);
            }
        });
        timeSlot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot2 = timeSlot2.getText().toString().trim();
                edtTime.setText(slot2);

            }
        });
        timeSlot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot3 = timeSlot3.getText().toString().trim();
                edtTime.setText(slot3);

            }
        });
        timeSlot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot4 = timeSlot4.getText().toString().trim();
                edtTime.setText(slot4);
            }
        });
        timeSlot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot5 = timeSlot5.getText().toString().trim();
                edtTime.setText(slot5);
            }
        });
        timeSlot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot6 = timeSlot6.getText().toString().trim();
                edtTime.setText(slot6);
            }
        });

        calendarView = findViewById(R.id.calendar);
        dateHolder = findViewById(R.id.date_holder);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                dateHolder.setText("");
                int d = dayOfMonth;
                int m = month;
                int y = year;
                String dateConcat = d + "-" + m + "-" + y;
                dateHolder.append(dateConcat);
            }
        });

        confirmAppointment = findViewById(R.id.cAppoint);

        confirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt_time = edtTime.getText().toString().trim();
                if (edt_time.equals("")) {
                    Snackbar.make(view, "Please select a Time Slot.", Snackbar.LENGTH_LONG)
                            .setAction("GOT IT", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.color_white))
                            .setDuration(Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.brown))
                            .show();
                } else {
                    bookingBuilder();
                }
            }
        });


     /*   edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                pickerDialog = new TimePickerDialog(BookAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int sHour, int sMinute) {
                        String time_concat = sHour + ":" + sMinute;
                        if (time_concat.equals("12:00") || time_concat.equals("13:00") || time_concat.equals("14:00")
                            || time_concat.equals("15:00") || time_concat.equals("16:30") || time_concat.equals("17:45")) {
                            edtTime.setText(time_concat);
                        }
                        else {
                            Toast.makeText(BookAppointment.this, "Selected Time Slot is unavailable", Toast.LENGTH_LONG).show();
                        }
                    }
                }, hour, minutes, true);
                pickerDialog.show();
            }
        });

        */
    }


    private void bookingBuilder() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Are you sure about your selected appointment Date & Time?")
                .setPositiveButton("YES, PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(BookAppointment.this, PaymentGateway.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        String priceText = priceTv.getText().toString().trim();
                        intent.putExtra(PRICE_MESSAGE, priceText);
                        String timeText = edtTime.getText().toString().trim();
                        intent.putExtra(TIME_MESSAGE, timeText);
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(intent);
                        Toast.makeText(BookAppointment.this, "Booking Saved.", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        progressBar.setVisibility(View.GONE);
    }
}
