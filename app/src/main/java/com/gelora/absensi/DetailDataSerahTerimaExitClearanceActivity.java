package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class DetailDataSerahTerimaExitClearanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_serah_terima_exit_clearance);

        String role = getIntent().getExtras().getString("role");
        Toast.makeText(this, role, Toast.LENGTH_SHORT).show();
    }
}