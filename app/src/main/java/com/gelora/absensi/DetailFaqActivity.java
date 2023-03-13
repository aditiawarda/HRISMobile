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

public class DetailFaqActivity extends AppCompatActivity {

    String noFAQ;
    TextView titleTV, hubungiIT;
    LinearLayout backBTN, faq1Detail, faq2Detail, faq3Detail, faq4Detail, faq5Detail, actionBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_faq);

        titleTV = findViewById(R.id.title_tv);
        hubungiIT = findViewById(R.id.hubungi_it);
        backBTN = findViewById(R.id.back_btn);
        faq1Detail = findViewById(R.id.faq_1_detail);
        faq2Detail = findViewById(R.id.faq_2_detail);
        faq3Detail = findViewById(R.id.faq_3_detail);
        faq4Detail = findViewById(R.id.faq_4_detail);
        faq5Detail = findViewById(R.id.faq_5_detail);
        actionBar = findViewById(R.id.action_bar);

        noFAQ = getIntent().getExtras().getString("no_faq");

        if(noFAQ.equals("1")){
            titleTV.setText("TITIK PADA MAPS TIDAK AKURAT ATAU TIDAK MUNCUL");
            faq1Detail.setVisibility(View.VISIBLE);
        } else if(noFAQ.equals("2")){
            titleTV.setText("STATUS ABSENSI ATAU SHIFT TIDAK MUNCUL");
            faq2Detail.setVisibility(View.VISIBLE);
        } else if(noFAQ.equals("3")){
            titleTV.setText("CARA PENGISIAN FORM IZIN/SAKIT");
            faq3Detail.setVisibility(View.VISIBLE);
        } else if(noFAQ.equals("4")){
            titleTV.setText("CARA PENGISIAN FORM PERMOHONAN CUTI");
            faq4Detail.setVisibility(View.VISIBLE);
        } else if(noFAQ.equals("5")){
            titleTV.setText("CARA PENGISIAN FORM KETERANGAN TIDAK ABSEN");
            faq5Detail.setVisibility(View.VISIBLE);
        }

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

        hubungiIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactIT();
            }
        });

    }

    private void getContactIT() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/get_contact_it";
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
                                new KAlertDialog(DetailFaqActivity.this, KAlertDialog.WARNING_TYPE)
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
        CookieBar.build(DetailFaqActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

}