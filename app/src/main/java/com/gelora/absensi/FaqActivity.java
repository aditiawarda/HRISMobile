package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class FaqActivity extends AppCompatActivity {

    LinearLayout backBTN, faq1, faq2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        backBTN = findViewById(R.id.back_btn);
        faq1 = findViewById(R.id.faq_1);
        faq2 = findViewById(R.id.faq_2);

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

    }
}