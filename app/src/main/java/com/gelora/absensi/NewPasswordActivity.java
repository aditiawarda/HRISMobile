package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    EditText passwordED, repasswordED;
    TextView showPassword, matchPassword, indicatorMatchPass;
    LinearLayout registerBTN, backBTN;
    String statusPass = "hide", nik;
    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        backBTN = findViewById(R.id.back_btn);
        passwordED = findViewById(R.id.passwordED);
        repasswordED = findViewById(R.id.repasswordED);
        showPassword = findViewById(R.id.show_password_register);
        matchPassword = findViewById(R.id.match_password);
        indicatorMatchPass = findViewById(R.id.indicator_match_pass);
        registerBTN = findViewById(R.id.register_btn);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        nik = getIntent().getExtras().getString("nik");

        loadingProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#A6441F"),android.graphics.PorterDuff.Mode.MULTIPLY);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        passwordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void afterTextChanged(Editable s) {
                if (!passwordED.getText().toString().equals("")||!repasswordED.getText().toString().equals("")) {
                    matchPassword.setVisibility(View.VISIBLE);
                    indicatorMatchPass.setVisibility(View.VISIBLE);
                    if (passwordED.getText().toString().equals(repasswordED.getText().toString())) {
                        matchPassword.setText("Macthing");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_green));
                    } else {
                        matchPassword.setText("No Matching");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_red));
                    }
                } else {
                    indicatorMatchPass.setVisibility(View.GONE);
                    matchPassword.setVisibility(View.GONE);
                }
            }

        });

        repasswordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void afterTextChanged(Editable s) {
                if (!passwordED.getText().toString().equals("")||!repasswordED.getText().toString().equals("")){
                    matchPassword.setVisibility(View.VISIBLE);
                    indicatorMatchPass.setVisibility(View.VISIBLE);
                    if (passwordED.getText().toString().equals(repasswordED.getText().toString())) {
                        matchPassword.setText("Macthing");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_green));
                    } else {
                        matchPassword.setText("No Matching");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_red));
                    }
                } else {
                    indicatorMatchPass.setVisibility(View.GONE);
                    matchPassword.setVisibility(View.GONE);
                }
            }

        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View view) {
                Drawable show = getResources().getDrawable(R.drawable.ic_baseline_visibility_on);
                Drawable hide = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                if(statusPass.equals("hide")){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    repasswordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setCompoundDrawablesWithIntrinsicBounds(hide, null, null, null);
                    showPassword.setText("Hide");
                    statusPass = "show";
                } else if(statusPass.equals("show")) {
                    //Jika tidak, maka password akan di sembuyikan
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    repasswordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setCompoundDrawablesWithIntrinsicBounds(show, null, null, null);
                    showPassword.setText("Show");
                    statusPass = "hide";
                }
            }
        });

        repasswordED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordED.clearFocus();
                repasswordED.clearFocus();

                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                passwordED.setBackground(ContextCompat.getDrawable(NewPasswordActivity.this, R.drawable.shape_feel_login));
                repasswordED.setBackground(ContextCompat.getDrawable(NewPasswordActivity.this, R.drawable.shape_feel_login));

                passwordED.setError(null);
                repasswordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String password = passwordED.getText().toString();
                String repassword = repasswordED.getText().toString();

                if (TextUtils.isEmpty(password) || password.equals("")) {
                    if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                        new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Lengkapi Password dan Re-Passwword!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = passwordED;
                        cancel = true;
                    } else {
                        new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Lengkapi Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = passwordED;
                        cancel = true;
                    }
                } else {
                    if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                        new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Lengkapi Re-Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = repasswordED;
                        cancel = true;
                    } else {
                        if (!password.equals(repassword)) {
                            new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Password tidak cocok!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        }
                    }
                }

                if (cancel){
                    fokus.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    newPasswordFunction(nik, password);
                }

                return false;
            }

        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordED.clearFocus();
                repasswordED.clearFocus();

                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                passwordED.setBackground(ContextCompat.getDrawable(NewPasswordActivity.this, R.drawable.shape_feel_login));
                repasswordED.setBackground(ContextCompat.getDrawable(NewPasswordActivity.this, R.drawable.shape_feel_login));

                passwordED.setError(null);
                repasswordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String password = passwordED.getText().toString();
                String repassword = repasswordED.getText().toString();

                if (TextUtils.isEmpty(password) || password.equals("")) {
                    if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                        new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Lengkapi Password dan Re-Passwword!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = passwordED;
                        cancel = true;
                    } else {
                        new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Lengkapi Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = passwordED;
                        cancel = true;
                    }
                } else {
                    if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                        new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Lengkapi Re-Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = repasswordED;
                        cancel = true;
                    } else {
                        if (!password.equals(repassword)) {
                            new KAlertDialog(NewPasswordActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Password tidak cocok!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        }
                    }
                }

                if (cancel){
                    fokus.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    newPasswordFunction(nik,password);
                }

            }
        });

    }

    private void newPasswordFunction(String nik,String new_password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/update_password";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String nama = data.getString("nama");
                                Intent intent = new Intent(NewPasswordActivity.this, SuccessNewPasswordActivity.class);
                                intent.putExtra("nama_karyawan", nama);
                                intent.putExtra("nik_karyawan", nik);
                                startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingProgressBar.setVisibility(View.GONE);
                                    }
                                }, 500);
                                finish();
                            } else {
                                loadingProgressBar.setVisibility(View.GONE);
                                new KAlertDialog(NewPasswordActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan")
                                        .setConfirmText("    OK    ")
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingProgressBar.setVisibility(View.GONE);
                            new KAlertDialog(NewPasswordActivity.this, KAlertDialog.ERROR_TYPE)
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
                        loadingProgressBar.setVisibility(View.GONE);
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
                params.put("new_password", new_password);
                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

    }

    private void connectionFailed(){
        CookieBar.build(NewPasswordActivity.this)
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