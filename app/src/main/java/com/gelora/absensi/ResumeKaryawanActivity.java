package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ResumeKaryawanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_karyawan);

        String NIK = getIntent().getExtras().getString("NIK");

        Toast.makeText(this, NIK, Toast.LENGTH_SHORT).show();

    }
}