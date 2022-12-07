package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FaqActivity extends AppCompatActivity {

    LinearLayout backBTN, faq1, faq2, faq3, faq4;
    String statusKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        backBTN = findViewById(R.id.back_btn);
        faq1 = findViewById(R.id.faq_1);
        faq2 = findViewById(R.id.faq_2);
        faq3 = findViewById(R.id.faq_3);
        faq4 = findViewById(R.id.faq_4);

        statusKaryawan = getIntent().getExtras().getString("status_karyawan");

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        faq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "1");
                startActivity(intent);
            }
        });

        faq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "2");
                startActivity(intent);
            }
        });

        faq3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "3");
                startActivity(intent);
            }
        });

        faq4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "4");
                startActivity(intent);
            }
        });

        if(statusKaryawan.equals("Tetap")||statusKaryawan.equals("Kontrak")){
            faq3.setVisibility(View.VISIBLE);
            faq4.setVisibility(View.VISIBLE);
        } else {
            faq3.setVisibility(View.GONE);
            faq4.setVisibility(View.GONE);
        }

    }
}