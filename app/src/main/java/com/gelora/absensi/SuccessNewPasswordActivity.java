package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelora.absensi.support.StatusBarColorManager;

public class SuccessNewPasswordActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    TextView descRegister;
    String namaKarywan, nikKaryawan;
    LinearLayout toLoginBTN;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_new_password);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);

        descRegister = findViewById(R.id.desc);
        toLoginBTN = findViewById(R.id.to_login_btn);
        namaKarywan = getIntent().getExtras().getString("nama_karyawan");
        nikKaryawan = getIntent().getExtras().getString("nik_karyawan");

        descRegister.setText("Halo "+namaKarywan+", password baru anda telah berhasil disimpan silakan login menggunakan NIK/id karyawan "+nikKaryawan+" beserta password terbaru.");

        toLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessNewPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}