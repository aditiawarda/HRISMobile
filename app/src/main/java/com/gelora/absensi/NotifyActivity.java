package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class NotifyActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    TextView descRegister;
    String namaKarywan, nikKaryawan;
    LinearLayout toLoginBTN;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        //NotifyActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);

        descRegister = findViewById(R.id.desc);
        toLoginBTN = findViewById(R.id.to_login_btn);
        namaKarywan = getIntent().getExtras().getString("nama_karyawan");
        nikKaryawan = getIntent().getExtras().getString("nik_karyawan");

        descRegister.setText("Selamat "+namaKarywan+" registrasi akun anda berhasil, silakan Login dengan NIK "+nikKaryawan+" dan password anda.");

        toLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifyActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}