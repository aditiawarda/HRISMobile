package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class EmailShowActivity extends AppCompatActivity {

    LinearLayout backBTN;
    String userNIK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_show);

        backBTN = findViewById(R.id.back_btn);
        userNIK = getIntent().getExtras().getString("nik");

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}