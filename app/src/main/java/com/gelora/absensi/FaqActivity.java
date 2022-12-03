package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class FaqActivity extends AppCompatActivity {

    LinearLayout faq1, faq2, faq3, faq4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faq1 = findViewById(R.id.faq_1);
        faq2 = findViewById(R.id.faq_2);
        faq3 = findViewById(R.id.faq_3);
        faq4 = findViewById(R.id.faq_4);

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

    }
}