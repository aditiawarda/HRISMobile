package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class PasswordRequestActivity extends AppCompatActivity {

    LinearLayout backBTN, nextBTN, showPasswordBTN;
    EditText passwordED;
    TextView showPassword;
    ProgressBar loadingProgressBar;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    RequestQueue requestQueue;
    String visibilityPassword = "hide";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_request);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        backBTN = findViewById(R.id.back_btn);
        passwordED = findViewById(R.id.passwordED);
        nextBTN = findViewById(R.id.next_btn);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        showPasswordBTN = findViewById(R.id.show_password_btn);
        showPassword = findViewById(R.id.show_password);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View view) {
                Drawable show = getResources().getDrawable(R.drawable.ic_baseline_visibility_on);
                Drawable show_aktif = getResources().getDrawable(R.drawable.ic_baseline_visibility_on);
                Drawable hide = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                Drawable hide_aktif = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                if(visibilityPassword.equals("hide")){
                    passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    if(passwordED.hasFocus()){
                        showPassword.setBackground(hide_aktif);
                    } else {
                        showPassword.setBackground(hide);
                    }
                    visibilityPassword = "show";
                }else if(visibilityPassword.equals("show")) {
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    if(passwordED.hasFocus()){
                        showPassword.setBackground(show_aktif);
                    } else {
                        showPassword.setBackground(show);
                    }
                    visibilityPassword = "hide";
                }
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordED.getText().toString().equals("")){
                    loadingProgressBar.setVisibility(View.GONE);
                    new KAlertDialog(PasswordRequestActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap masukkan password")
                            .setConfirmText("    OK    ")
                            .show();
                } else {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    getTokenAccess();
                }
            }
        });

    }

    private void getTokenAccess() {
        String URL = "https://timeline.geloraaksara.co.id/auth/login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("nik", sharedPrefManager.getSpNik());
            jsonBody.put("password", passwordED.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        JSONObject data = null;
                        try {
                            data = response.getJSONObject("data");
                            String status = data.getString("status");
                            if(status.equals("Success")){
                                String token = data.getString("token");
                                Intent intent = new Intent(PasswordRequestActivity.this, ProjectViewActivity.class);
                                startActivity(intent);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN_TIMELINE, token);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_PASSWORD, passwordED.getText().toString());
                                finish();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingProgressBar.setVisibility(View.GONE);
                                    }
                                }, 3000);
                            } else if(status.equals("NIK tidak terdaftar")) {
                                new KAlertDialog(PasswordRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Tidak dapat mengakses menu, harap hubungi IT untuk kendala ini")
                                        .setConfirmText("    OK    ")
                                        .show();
                                loadingProgressBar.setVisibility(View.GONE);
                            } else if(status.equals("Password salah")) {
                                new KAlertDialog(PasswordRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Password yang anda masukkan salah")
                                        .setConfirmText("    OK    ")
                                        .show();
                                loadingProgressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }

    private void connectionFailed(){
        CookieBar.build(PasswordRequestActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}