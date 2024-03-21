package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    EditText nikED, passwordED, repasswordED;
    TextView namaTV, showPassword, matchPassword, indicatorMatchPass;
    String regisStatus = "", statusPass = "hide";
    LinearLayout toLoginBTN, registerBTN, contactServiceBTN, connectBTN, closeBTN;
    BottomSheetLayout bottomSheetCS;
    SwipeRefreshLayout refreshLayout;
    ProgressBar loadingProgressBar;
    View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);

        rootview = findViewById(android.R.id.content);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nikED = findViewById(R.id.nikED);
        namaTV = findViewById(R.id.namaTV);
        passwordED = findViewById(R.id.passwordED);
        repasswordED = findViewById(R.id.repasswordED);
        showPassword = findViewById(R.id.show_password_register);
        matchPassword = findViewById(R.id.match_password);
        indicatorMatchPass = findViewById(R.id.indicator_match_pass);
        registerBTN = findViewById(R.id.register_btn);
        contactServiceBTN = findViewById(R.id.contact_service_btn);
        bottomSheetCS = findViewById(R.id.bottom_sheet_cs);
        toLoginBTN = findViewById(R.id.to_login_btn);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        loadingProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#A6441F"),android.graphics.PorterDuff.Mode.MULTIPLY);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        nikED.setText("");
                        namaTV.setText("Nama Karyawan");
                        passwordED.setText("");
                        repasswordED.setText("");

                        nikED.clearFocus();
                        passwordED.clearFocus();
                        repasswordED.clearFocus();

                        nikED.setTextColor(Color.parseColor("#FFFFFF"));
                        passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                        repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                        nikED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                        passwordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                        repasswordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));

                    }
                }, 1000);
            }
        });

        toLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        contactServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactService();
            }
        });

        nikED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                nikED.setTextColor(Color.parseColor("#FFDFB8"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                nikED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login_aktif));
                passwordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                repasswordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
            }
        });

        passwordED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFDFB8"));
                repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                nikED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                passwordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login_aktif));
                repasswordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
            }
        });

        repasswordED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                repasswordED.setTextColor(Color.parseColor("#FFDFB8"));

                nikED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                passwordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                repasswordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login_aktif));
            }
        });

        nikED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nik = nikED.getText().toString();
                if(!nik.equals("")){
                    getNamaKaryawan(nik);
                }
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
                nikED.clearFocus();
                passwordED.clearFocus();
                repasswordED.clearFocus();

                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                nikED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                passwordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                repasswordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));

                nikED.setError(null);
                passwordED.setError(null);
                repasswordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String nik = nikED.getText().toString();
                String nama = namaTV.getText().toString();
                String password = passwordED.getText().toString();
                String repassword = repasswordED.getText().toString();

                if (regisStatus.equals("registered") || regisStatus.equals("notfound") || regisStatus.equals("")){
                    if (TextUtils.isEmpty(nik) || nik.equals("")){
                        if (TextUtils.isEmpty(password) || password.equals("")){
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")){
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan data!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Re-Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(password) || password.equals("")){
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan Password kosong!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lengkapi Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lengkapi Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                if (!password.equals(repassword)){
                                    new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("NIK tidak terdaftar dan Password tidak cocok!")
                                            .setConfirmText("    OK    ")
                                            .show();
                                    fokus = nikED;
                                    cancel = true;
                                } else {
                                    if (regisStatus.equals("registered")){
                                        new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Akun sudah teregistrasi, silakan Login!")
                                                .setConfirmText("    OK    ")
                                                .show();
                                    } else {
                                        new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("NIK tidak terdaftar!")
                                                .setConfirmText("    OK    ")
                                                .show();
                                    }
                                    fokus = namaTV;
                                    cancel = true;
                                }
                            }
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(password) || password.equals("")) {
                        if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password dan Re-Passwword!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = passwordED;
                            cancel = true;
                        } else {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = passwordED;
                            cancel = true;
                        }
                    } else {
                        if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Re-Password!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        } else {
                            if (!password.equals(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Password tidak cocok!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
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
                    registerFunction(nik,password);
                }

                return false;
            }

        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nikED.clearFocus();
                passwordED.clearFocus();
                repasswordED.clearFocus();

                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                repasswordED.setTextColor(Color.parseColor("#FFFFFF"));

                nikED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                passwordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));
                repasswordED.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.shape_feel_login));

                nikED.setError(null);
                passwordED.setError(null);
                repasswordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String nik = nikED.getText().toString();
                String nama = namaTV.getText().toString();
                String password = passwordED.getText().toString();
                String repassword = repasswordED.getText().toString();

                if (regisStatus.equals("registered") || regisStatus.equals("notfound") || regisStatus.equals("")){
                    if (TextUtils.isEmpty(nik) || nik.equals("")){
                        if (TextUtils.isEmpty(password) || password.equals("")){
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")){
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan data!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Re-Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(password) || password.equals("")){
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan Password kosong!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lenkapi Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lengkapi Password!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            } else {
                                if (!password.equals(repassword)){
                                    new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("NIK tidak terdaftar dan Password tidak cocok!")
                                            .setConfirmText("    OK    ")
                                            .show();
                                    fokus = nikED;
                                    cancel = true;
                                } else {
                                    if (regisStatus.equals("registered")){
                                        new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Akun sudah teregistrasi, silakan Login!")
                                                .setConfirmText("    OK    ")
                                                .show();
                                    } else {
                                        new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("NIK tidak terdaftar!")
                                                .setConfirmText("    OK    ")
                                                .show();
                                    }
                                    fokus = namaTV;
                                    cancel = true;
                                }
                            }
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(password) || password.equals("")) {
                        if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password dan Re-Passwword!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = passwordED;
                            cancel = true;
                        } else {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = passwordED;
                            cancel = true;
                        }
                    } else {
                        if (TextUtils.isEmpty(repassword) || repassword.equals("")) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Re-Password!")
                                    .setConfirmText("    OK    ")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        } else {
                            if (!password.equals(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Password tidak cocok!")
                                        .setConfirmText("    OK    ")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
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
                    registerFunction(nik,password);
                }
            }
        });

    }

    private void getNamaKaryawan(String nik){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/nama_karyawan";
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
                            String nama_karyawan = data.getString("nama_karyawan");

                            if(status.equals("Success")){
                                regisStatus = "ready";
                                namaTV.setText(nama_karyawan);
                            } else if (status.equals("Warning")){
                                regisStatus = "registered";
                                namaTV.setText(nama_karyawan);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Akun sudah teregistrasi!")
                                        .setConfirmText("    OK    ")
                                        .show();
                            } else {
                                regisStatus = "notfound";
                                namaTV.setText("Data tidak ditemukan");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("NIK", nik);
                return params;
            }
        };

        //requestQueue.add(postRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

    }

    private void registerFunction(String nik,String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/register_akun";
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
                                String namaKaryawan = data.getString("nama_karyawan");
                                String nikKaryawan = data.getString("nik_karyawan");

                                Intent intent = new Intent(RegisterActivity.this, NotifyActivity.class);
                                intent.putExtra("nama_karyawan", namaKaryawan);
                                intent.putExtra("nik_karyawan", nikKaryawan);
                                startActivity(intent);
                                finish();
                            } else {
                                loadingProgressBar.setVisibility(View.GONE);
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Opps")
                                        .setContentText("Not Found!")
                                        .setConfirmText("    OK    ")
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("NIK", nik);
                params.put("password", password);
                return params;
            }
        };

        //requestQueue.add(postRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

    }

    private void contactService(){
        bottomSheetCS.showWithSheetView(LayoutInflater.from(RegisterActivity.this).inflate(R.layout.layout_contact_service_it, bottomSheetCS, false));
        closeBTN = findViewById(R.id.close_btn);
        connectBTN = findViewById(R.id.connect_btn);
        getContactIT();
    }

    private void connectionFailed(){
        // Banner.make(rootview, RegisterActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(RegisterActivity.this)
                .setCustomView(R.layout.layout_custom_cookie)
                .setEnableAutoDismiss(true)
                .setSwipeToDismiss(false)
                .setCookiePosition(Gravity.TOP)
                .show();

    }

    @Override
    public void onBackPressed() {
        if (bottomSheetCS.isSheetShowing()){
            bottomSheetCS.dismissSheet();
        } else {
            super.onBackPressed();
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

                            closeBTN = findViewById(R.id.close_btn);
                            connectBTN = findViewById(R.id.connect_btn);

                            try {
                                closeBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomSheetCS.dismissSheet();
                                    }
                                });
                                connectBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                                        try {
                                            startActivity(webIntent);
                                        } catch (SecurityException e) {
                                            e.printStackTrace();
                                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Tidak dapat terhubung ke Whatsapp, anda bisa hubungi secara langsung ke 0"+whatsapp.substring(2, whatsapp.length())+" atas nama Bapak "+nama+" bagian HRD")
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
                                });
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

}