package com.krystofrapp.limelightbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemsPreviewActivity extends AppCompatActivity {

    public static final String PRICE_MESSAGE = "Price Value";
    private TextView book1, book2, book3, book4, book5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_preview);

        book1 = findViewById(R.id.book1);
        book2 = findViewById(R.id.book2);
        book3 = findViewById(R.id.book3);
        book4 = findViewById(R.id.book4);
        book5 = findViewById(R.id.book5);

        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsPreviewActivity.this, BookAppointment.class);
                String message = book1.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, message);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });
        book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsPreviewActivity.this, BookAppointment.class);
                String message = book2.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, message);
                startActivity(intent);
            }
        });
        book3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsPreviewActivity.this, BookAppointment.class);
                String message = book3.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, message);
                startActivity(intent);
            }
        });
        book4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsPreviewActivity.this, BookAppointment.class);
                String message = book4.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, message);
                startActivity(intent);
            }
        });
        book5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsPreviewActivity.this, BookAppointment.class);
                String message = book5.getText().toString().trim();
                intent.putExtra(PRICE_MESSAGE, message);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ItemsPreviewActivity.this, Homepage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
