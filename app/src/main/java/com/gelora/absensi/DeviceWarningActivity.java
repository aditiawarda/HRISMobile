package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DeviceWarningActivity extends AppCompatActivity {

    TextView loginBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_warning);

        loginBTN = findViewById(R.id.to_login_btn);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceWarningActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}