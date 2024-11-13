package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordVerificationActivity extends AppCompatActivity {

    LinearLayout backBTN, resendOTP;
    PinView otpForm;
    private TextView countdownTV;
    private CountDownTimer countDownTimer;
    ProgressBar loadingResending;
    String nik, email, otp, expired;
    Boolean timeOut = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_verification);

        backBTN = findViewById(R.id.back_btn);
        countdownTV = findViewById(R.id.countdown_tv);
        otpForm = findViewById(R.id.otp_form);
        loadingResending = findViewById(R.id.loading_resending);
        resendOTP = findViewById(R.id.resend_otp);

        nik = getIntent().getExtras().getString("nik");
        email = getIntent().getExtras().getString("email");
        otp = getIntent().getExtras().getString("otp");
        expired = getIntent().getExtras().getString("expired");

        // Format tanggal dan waktu
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date targetDateTime = sdf.parse(expired);
            Date currentTime = new Date();
            long diffMilliseconds = targetDateTime.getTime() - currentTime.getTime();
            startCountdown(diffMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        otpForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == otpForm.getItemCount()) {
                    String pin = s.toString();
                    if(pin.equals(otp)){
                        if (!timeOut){
                            checkOTP(nik, otp);
                        } else {
                            new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("OTP yang anda masukkan kadaluarsa, silakan kirim ulang OTP")
                                    .setConfirmText("    OK    ")
                                    .show();
                        }
                    } else {
                        new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("OTP yang anda masukkan salah")
                                .setConfirmText("    OK    ")
                                .show();
                    }
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingResending.setVisibility(View.VISIBLE);
                resendOTP.setVisibility(View.GONE);
                sendOTPtoEmail(nik, email);
            }
        });

    }

    private void startCountdown(long milliseconds) {
        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Konversi sisa waktu ke jam, menit, dan detik
                long seconds = millisUntilFinished / 1000 % 60;
                long minutes = millisUntilFinished / (1000 * 60) % 60;
                long hours = millisUntilFinished / (1000 * 60 * 60) % 24;

                // Update TextView dengan sisa waktu
                @SuppressLint("DefaultLocale")
                String countdownFormatted = String.format("%02d:%02d", minutes, seconds);
                float textSizeInDp = 18;
                float scale = getResources().getDisplayMetrics().density;
                int textSizeInPixels = (int) (textSizeInDp * scale + 0.5f);

                countdownTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPixels);
                countdownTV.setText(countdownFormatted);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                // Countdown selesai, lakukan tindakan sesuai kebutuhan
                countdownTV.setText("WAKTU HABIS");
                countdownTV.setTextSize(20);

                float textSizeInDp = 16;
                float scale = getResources().getDisplayMetrics().density;
                int textSizeInPixels = (int) (textSizeInDp * scale + 0.5f);
                countdownTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPixels);
                timeOut = true;
            }
        }.start();
    }

    private void sendOTPtoEmail(String nik, String email) {
        int MY_SOCKET_TIMEOUT_MS = 180000;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/reset_password";
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
                                JSONObject data_otp = data.getJSONObject("data");
                                String otp = data_otp.getString("otp");
                                String expired_at = data_otp.getString("expired_at");
                                Intent intent = new Intent(ForgotPasswordVerificationActivity.this, ForgotPasswordVerificationActivity.class);
                                intent.putExtra("nik", nik);
                                intent.putExtra("otp", otp);
                                intent.putExtra("expired", expired_at);
                                startActivity(intent);
                                handler.postDelayed(new Runnable() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void run() {
                                        loadingResending.setVisibility(View.GONE);
                                        resendOTP.setVisibility(View.VISIBLE);
                                    }
                                }, 500);
                                finish();
                            } else {
                                loadingResending.setVisibility(View.GONE);
                                resendOTP.setVisibility(View.VISIBLE);
                                new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Email gagal terkirim")
                                        .setConfirmText("    OK    ")
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingResending.setVisibility(View.GONE);
                            resendOTP.setVisibility(View.VISIBLE);
                            new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
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
                        loadingResending.setVisibility(View.GONE);
                        resendOTP.setVisibility(View.VISIBLE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("email", email);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

    }

    private void checkOTP(String nik, String otp) {
        int MY_SOCKET_TIMEOUT_MS = 180000;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/check_otp_reset_password";
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
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                                Intent intent = new Intent(ForgotPasswordVerificationActivity.this, NewPasswordActivity.class);
                                intent.putExtra("nik", nik);
                                startActivity(intent);
                                finish();
                            } else if(status.equals("Failed 1")) {
                                new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("OTP yang anda masukkan salah")
                                        .setConfirmText("    OK    ")
                                        .show();
                            } else if(status.equals("Failed 2")) {
                                new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("OTP sudah digunakan")
                                        .setConfirmText("    OK    ")
                                        .show();
                            } else if(status.equals("Failed 3")) {
                                new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan")
                                        .setConfirmText("    OK    ")
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new KAlertDialog(ForgotPasswordVerificationActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Terjadi kesalahan")
                                    .setConfirmText("    OK    ")
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("otp", otp);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void connectionFailed(){
        CookieBar.build(ForgotPasswordVerificationActivity.this)
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