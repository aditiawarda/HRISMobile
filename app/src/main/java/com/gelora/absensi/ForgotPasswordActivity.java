package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    LinearLayout backBTN, submitBTN;
    EditText nikED;
    String deviceID;
    ProgressBar loadingProgress;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        backBTN = findViewById(R.id.back_btn);
        submitBTN = findViewById(R.id.submit_btn);
        nikED = findViewById(R.id.nikED);
        loadingProgress = findViewById(R.id.loading_progress);

        deviceID = String.valueOf(Settings.Secure.getString(ForgotPasswordActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID)).toUpperCase();

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgress.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(nikED.getText().toString().equals("")){
                            loadingProgress.setVisibility(View.GONE);
                            new KAlertDialog(ForgotPasswordActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap masukkan nik anda")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            checkDevice(nikED.getText().toString(),deviceID);
                        }
                    }
                }, 800);
            }
        });

    }

    private void checkDevice(String nik, String device_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/check_device_id";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                Intent intent = new Intent(ForgotPasswordActivity.this, EmailShowActivity.class);
                                intent.putExtra("nik", nik);
                                startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingProgress.setVisibility(View.GONE);
                                    }
                                }, 500);
                                finish();
                            } else if (status.equals("Warning")){
                                loadingProgress.setVisibility(View.GONE);
                                new KAlertDialog(ForgotPasswordActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar!")
                                        .setConfirmText("    OK    ")
                                        .show();
                            } else {
                                String atas_nama = data.getString("atas_nama");
                                String nik = data.getString("NIK");
                                Intent intent = new Intent(ForgotPasswordActivity.this, DeviceWarningForgotPasswordActivity.class);
                                intent.putExtra("nik", nik);
                                intent.putExtra("atas_nama", atas_nama);
                                startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingProgress.setVisibility(View.GONE);
                                    }
                                }, 500);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingProgress.setVisibility(View.GONE);
                            new KAlertDialog(ForgotPasswordActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Terjadi kesalahan")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        loadingProgress.setVisibility(View.GONE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", nik);
                params.put("device_id_user", device_id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(ForgotPasswordActivity.this)
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