package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FaqActivity extends AppCompatActivity {

    LinearLayout backBTN, faq1, faq2, faq3, faq4, faq5, faq6, faq7, faq8, actionBar;
    TextView hubungiIT;
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
        faq6 = findViewById(R.id.faq_6);
        faq7 = findViewById(R.id.faq_7);
        faq8 = findViewById(R.id.faq_8);
        actionBar = findViewById(R.id.action_bar);
        hubungiIT = findViewById(R.id.hubungi_it);

        statusKaryawan = getIntent().getExtras().getString("status_karyawan");
        tanggalBergabung = getIntent().getExtras().getString("tanggal_bergabung");
        statusFitur = getIntent().getExtras().getString("status_fitur");
        statusFinger = getIntent().getExtras().getString("status_finger");

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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

        faq6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "6");
                startActivity(intent);
            }
        });

        faq7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "7");
                startActivity(intent);
            }
        });

        faq8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, DetailFaqActivity.class);
                intent.putExtra("no_faq", "8");
                startActivity(intent);
            }
        });

        hubungiIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactIT();
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

    private void getContactIT() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://hrisgelora.co.id/api/get_contact_it";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String bagian = response.getString("bagian");
                            String nama = response.getString("nama");
                            String whatsapp = response.getString("whatsapp");

                            Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                            try {
                                startActivity(webIntent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                                new KAlertDialog(FaqActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Tidak dapat terhubung ke Whatsapp, anda bisa hubungi secara langsung ke 0"+whatsapp.substring(2, whatsapp.length())+" atas nama Bapak "+nama+" bagian IT/EDP")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void connectionFailed(){
        CookieBar.build(FaqActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}