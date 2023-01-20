package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FaqActivity extends AppCompatActivity {

    LinearLayout backBTN, faq1, faq2, faq3, faq4, faq5;
    String statusKaryawan, tanggalBergabung, statusFitur, statusFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        backBTN = findViewById(R.id.back_btn);
        faq1 = findViewById(R.id.faq_1);
        faq2 = findViewById(R.id.faq_2);
        faq3 = findViewById(R.id.faq_3);
        faq4 = findViewById(R.id.faq_4);
        faq5 = findViewById(R.id.faq_5);

        statusKaryawan = getIntent().getExtras().getString("status_karyawan");
        tanggalBergabung = getIntent().getExtras().getString("tanggal_bergabung");
        statusFitur = getIntent().getExtras().getString("status_fitur");
        statusFinger = getIntent().getExtras().getString("status_finger");

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

        faq5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "5");
                startActivity(intent);
            }
        });

        if(statusKaryawan.equals("Tetap")||statusKaryawan.equals("Kontrak")){
            if(statusFitur.equals("1")){
                if(statusKaryawan.equals("Tetap")){
                    faq3.setVisibility(View.VISIBLE);
                    faq4.setVisibility(View.VISIBLE);
                } else if(statusKaryawan.equals("Kontrak")){
                    String tanggalMulaiBekerja = tanggalBergabung;
                    String tanggalSekarang = getDate();

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = format.parse(tanggalSekarang);
                        date2 = format.parse(tanggalMulaiBekerja);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long waktu1 = date1.getTime();
                    long waktu2 = date2.getTime();
                    long selisih_waktu = waktu1 - waktu2;
                    long diffDays = selisih_waktu / (24 * 60 * 60 * 1000);
                    long diffMonths = (selisih_waktu / (24 * 60 * 60 * 1000)) / 30;
                    long diffYears =  ((selisih_waktu / (24 * 60 * 60 * 1000)) / 30) / 12;

                    if(diffMonths >= 12){
                        faq3.setVisibility(View.VISIBLE);
                        faq4.setVisibility(View.VISIBLE);
                    } else {
                        faq3.setVisibility(View.VISIBLE);
                        faq4.setVisibility(View.GONE);
                    }
                }
            } else {
                faq3.setVisibility(View.GONE);
                faq4.setVisibility(View.GONE);
            }
        } else {
            if(statusFitur.equals("1")){
                faq3.setVisibility(View.VISIBLE);
                faq4.setVisibility(View.GONE);
            } else {
                faq3.setVisibility(View.GONE);
                faq4.setVisibility(View.GONE);
            }
        }

        if(statusFinger.equals("1")){
            faq5.setVisibility(View.VISIBLE);
        } else {
            faq5.setVisibility(View.GONE);
        }

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}