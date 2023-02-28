package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class ComingSoonActivity extends AppCompatActivity {

    LinearLayout backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        backBTN = findViewById(R.id.back_btn);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    onBackPressed();
                    finish();
                } catch (NullPointerException e){
                    Log.d("Error", e.toString());
                }
            }
        }, 3000);

    }
}