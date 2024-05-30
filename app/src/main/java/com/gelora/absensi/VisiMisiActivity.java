package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class VisiMisiActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, mainContent, loadingContent;
    TextView visiTV, misiTV;
    RequestQueue requestQueue;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visi_misi);

        requestQueue = Volley.newRequestQueue(this);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        visiTV = findViewById(R.id.visi_tv);
        misiTV = findViewById(R.id.misi_tv);
        mainContent = findViewById(R.id.main_content);
        loadingContent = findViewById(R.id.loading_part);

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

        getVisiMisi();

    }

    private void getVisiMisi() {
        final String url = "https://hrisgelora.co.id/api/get_visi_misi";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                String visi = response.getString("visi");
                                String misi = response.getString("misi");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainContent.setVisibility(View.VISIBLE);
                                        loadingContent.setVisibility(View.GONE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            visiTV.setText(Html.fromHtml(visi, Html.FROM_HTML_MODE_COMPACT));
                                            misiTV.setText(Html.fromHtml(misi, Html.FROM_HTML_MODE_COMPACT));
                                        } else {
                                            visiTV.setText(Html.fromHtml(visi));
                                            misiTV.setText(Html.fromHtml(misi));
                                        }
                                    }
                                }, 1000);
                            } else {
                                mainContent.setVisibility(View.GONE);
                                loadingContent.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                try {
                    new KAlertDialog(VisiMisiActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Gagal terhubung, harap periksa koneksi internet atau jaringan anda")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } catch (WindowManager.BadTokenException e){
                    e.printStackTrace();
                }
            }
        });

        requestQueue.add(request);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}