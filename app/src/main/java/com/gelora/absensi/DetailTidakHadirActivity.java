package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class DetailTidakHadirActivity extends AppCompatActivity {

    LinearLayout backBTN, homeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tidak_hadir);

        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailTidakHadirActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}