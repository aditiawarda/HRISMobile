package com.gelora.absensi;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CompanyActivity extends AppCompatActivity {

    LinearLayout backBTN, homeBTN;
    TextView p1;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        sharedPrefManager = new SharedPrefManager(this);
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
                Intent intent = new Intent(CompanyActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")){
            homeBTN.setVisibility(View.GONE);
        } else {
            homeBTN.setVisibility(View.VISIBLE);
        }

    }
}