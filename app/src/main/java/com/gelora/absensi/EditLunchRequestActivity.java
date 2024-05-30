package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditLunchRequestActivity extends AppCompatActivity {

    LinearLayout removeBTN1, addBTN1;
    EditText jumlahTV1;

    LinearLayout removeBTN2, addBTN2;
    EditText jumlahTV2;

    LinearLayout removeBTN3, addBTN3;
    EditText jumlahTV3;

    LinearLayout removeBTN4, addBTN4;
    EditText jumlahTV4;

    LinearLayout removeBTN5, addBTN5;
    EditText jumlahTV5;

    LinearLayout removeBTN6, addBTN6;
    EditText jumlahTV6;

    LinearLayout removeBTN7, addBTN7;
    EditText jumlahTV7;

    TextView bagianTV, tanggalTV, submitLabelTV, warningTV;
    LinearLayout rebackBTN, formPart, successPart, closePart, inputPart, soreMalamPart1, soreMalamPart2, siangPart1, siangPart2, backBTN, submitBTN, actionBar;
    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    ImageView successGif;

    String idPermohonan = "", timeOutRequest = "12:00:00";
    int pusat_siang1 = 0, pusat_siang2 = 0, pusat_sore = 0, pusat_malam = 0;
    int gapprint_siang = 0, gapprint_sore = 0, gapprint_malam = 0;
    KAlertDialog pDialog;
    private int i = -1;
    private Handler handler = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lunch_request);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        siangPart1 = findViewById(R.id.siang_part_1);
        siangPart2 = findViewById(R.id.siang_part_2);
        soreMalamPart1 = findViewById(R.id.sore_malam_part_1);
        soreMalamPart2 = findViewById(R.id.sore_malam_part_2);
        inputPart = findViewById(R.id.input_part);
        closePart = findViewById(R.id.close_part);

        removeBTN1 = findViewById(R.id.remove_1_btn);
        addBTN1 = findViewById(R.id.add_1_btn);
        jumlahTV1 = findViewById(R.id.jumlah_1_tv);

        removeBTN2 = findViewById(R.id.remove_2_btn);
        addBTN2 = findViewById(R.id.add_2_btn);
        jumlahTV2 = findViewById(R.id.jumlah_2_tv);

        removeBTN3 = findViewById(R.id.remove_3_btn);
        addBTN3 = findViewById(R.id.add_3_btn);
        jumlahTV3 = findViewById(R.id.jumlah_3_tv);

        removeBTN4 = findViewById(R.id.remove_4_btn);
        addBTN4 = findViewById(R.id.add_4_btn);
        jumlahTV4 = findViewById(R.id.jumlah_4_tv);

        removeBTN5 = findViewById(R.id.remove_5_btn);
        addBTN5 = findViewById(R.id.add_5_btn);
        jumlahTV5 = findViewById(R.id.jumlah_5_tv);

        removeBTN6 = findViewById(R.id.remove_6_btn);
        addBTN6 = findViewById(R.id.add_6_btn);
        jumlahTV6 = findViewById(R.id.jumlah_6_tv);

        removeBTN7 = findViewById(R.id.remove_7_btn);
        addBTN7 = findViewById(R.id.add_7_btn);
        jumlahTV7 = findViewById(R.id.jumlah_7_tv);

        submitBTN = findViewById(R.id.submit_btn);
        submitLabelTV = findViewById(R.id.submit_label);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        rebackBTN = findViewById(R.id.reback_btn);
        bagianTV = findViewById(R.id.bagian_tv);
        tanggalTV = findViewById(R.id.date_tv);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);
        warningTV = findViewById(R.id.warning_tv);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        idPermohonan = getIntent().getExtras().getString("id");

        getTimeOut();

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimeOut();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

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


        rebackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        jumlahTV1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    pusat_siang1 = 0;
                } else {
                    pusat_siang1 = Integer.parseInt(jumlahTV1.getText().toString());
                }
            }
        });

        jumlahTV2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    pusat_siang2 = 0;
                } else {
                    pusat_siang2 = Integer.parseInt(jumlahTV2.getText().toString());
                }
            }
        });

        jumlahTV3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    pusat_sore = 0;
                } else {
                    pusat_sore = Integer.parseInt(jumlahTV3.getText().toString());
                }
            }
        });

        jumlahTV4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    pusat_malam = 0;
                } else {
                    pusat_malam = Integer.parseInt(jumlahTV4.getText().toString());
                }
            }
        });

        jumlahTV5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    gapprint_siang = 0;
                } else {
                    gapprint_siang = Integer.parseInt(jumlahTV5.getText().toString());
                }
            }
        });

        jumlahTV6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    gapprint_sore = 0;
                } else {
                    gapprint_sore = Integer.parseInt(jumlahTV6.getText().toString());
                }
            }
        });

        jumlahTV7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    gapprint_malam = 0;
                } else {
                    gapprint_malam = Integer.parseInt(jumlahTV7.getText().toString());
                }
            }
        });

        jumlahTV7.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();

                return false;
            }
        });

        removeBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV1.getText().toString().isEmpty()){
                    jumlahTV1.setText(String.valueOf(0));
                    pusat_siang1 = 0;
                } else if(Integer.parseInt(jumlahTV1.getText().toString()) == 0){
                    jumlahTV1.setText(String.valueOf(0));
                    pusat_siang1 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV1.getText().toString()) - 1;
                    jumlahTV1.setText(String.valueOf(newValue));
                    pusat_siang1 = newValue;
                }
            }
        });

        addBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV1.getText().toString().isEmpty()){
                    jumlahTV1.setText(String.valueOf(0));
                    pusat_siang1 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV1.getText().toString()) + 1;
                    jumlahTV1.setText(String.valueOf(newValue));
                    pusat_siang1 = newValue;
                }
            }
        });

        removeBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV2.getText().toString().isEmpty()){
                    jumlahTV2.setText(String.valueOf(0));
                    pusat_siang2 = 0;
                } else if(Integer.parseInt(jumlahTV2.getText().toString()) == 0){
                    jumlahTV2.setText(String.valueOf(0));
                    pusat_siang2 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV2.getText().toString()) - 1;
                    jumlahTV2.setText(String.valueOf(newValue));
                    pusat_siang2 = newValue;
                }
            }
        });

        addBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV2.getText().toString().isEmpty()){
                    jumlahTV2.setText(String.valueOf(0));
                    pusat_siang2 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV2.getText().toString()) + 1;
                    jumlahTV2.setText(String.valueOf(newValue));
                    pusat_siang2 = newValue;
                }
            }
        });

        removeBTN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV3.getText().toString().isEmpty()){
                    jumlahTV3.setText(String.valueOf(0));
                    pusat_sore = 0;
                } else if(Integer.parseInt(jumlahTV3.getText().toString()) == 0){
                    jumlahTV3.setText(String.valueOf(0));
                    pusat_sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV3.getText().toString()) - 1;
                    jumlahTV3.setText(String.valueOf(newValue));
                    pusat_sore = newValue;
                }
            }
        });

        addBTN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV3.getText().toString().isEmpty()){
                    jumlahTV3.setText(String.valueOf(0));
                    pusat_sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV3.getText().toString()) + 1;
                    jumlahTV3.setText(String.valueOf(newValue));
                    pusat_sore = newValue;
                }
            }
        });

        removeBTN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV4.getText().toString().isEmpty()){
                    jumlahTV4.setText(String.valueOf(0));
                    pusat_malam = 0;
                } else if(Integer.parseInt(jumlahTV4.getText().toString()) == 0){
                    jumlahTV4.setText(String.valueOf(0));
                    pusat_malam = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV4.getText().toString()) - 1;
                    jumlahTV4.setText(String.valueOf(newValue));
                    pusat_malam = newValue;
                }
            }
        });

        addBTN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV4.getText().toString().isEmpty()){
                    jumlahTV4.setText(String.valueOf(0));
                    pusat_malam = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV4.getText().toString()) + 1;
                    jumlahTV4.setText(String.valueOf(newValue));
                    pusat_malam = newValue;
                }
            }
        });

        removeBTN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV5.getText().toString().isEmpty()){
                    jumlahTV5.setText(String.valueOf(0));
                    gapprint_siang = 0;
                } else if(Integer.parseInt(jumlahTV5.getText().toString()) == 0){
                    jumlahTV5.setText(String.valueOf(0));
                    gapprint_siang = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV5.getText().toString()) - 1;
                    jumlahTV5.setText(String.valueOf(newValue));
                    gapprint_siang = newValue;
                }
            }
        });

        addBTN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV5.getText().toString().isEmpty()){
                    jumlahTV5.setText(String.valueOf(0));
                    gapprint_siang = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV5.getText().toString()) + 1;
                    jumlahTV5.setText(String.valueOf(newValue));
                    gapprint_siang = newValue;
                }
            }
        });

        removeBTN6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV6.getText().toString().isEmpty()){
                    jumlahTV6.setText(String.valueOf(0));
                    gapprint_sore = 0;
                } else if(Integer.parseInt(jumlahTV6.getText().toString()) == 0){
                    jumlahTV6.setText(String.valueOf(0));
                    gapprint_sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV6.getText().toString()) - 1;
                    jumlahTV6.setText(String.valueOf(newValue));
                    gapprint_sore = newValue;
                }
            }
        });

        addBTN6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV6.getText().toString().isEmpty()){
                    jumlahTV6.setText(String.valueOf(0));
                    gapprint_sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV6.getText().toString()) + 1;
                    jumlahTV6.setText(String.valueOf(newValue));
                    gapprint_sore = newValue;
                }
            }
        });

        removeBTN7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV7.getText().toString().isEmpty()){
                    jumlahTV7.setText(String.valueOf(0));
                    gapprint_malam = 0;
                } else if(Integer.parseInt(jumlahTV7.getText().toString()) == 0){
                    jumlahTV7.setText(String.valueOf(0));
                    gapprint_malam = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV7.getText().toString()) - 1;
                    jumlahTV7.setText(String.valueOf(newValue));
                    gapprint_malam = newValue;
                }
            }
        });

        addBTN7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                jumlahTV5.clearFocus();
                jumlahTV6.clearFocus();
                jumlahTV7.clearFocus();
                if(jumlahTV7.getText().toString().isEmpty()){
                    jumlahTV7.setText(String.valueOf(0));
                    gapprint_sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV7.getText().toString()) + 1;
                    jumlahTV7.setText(String.valueOf(newValue));
                    gapprint_malam = newValue;
                }
            }
        });

        submitLabelTV.setText("KIRIM");
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void getTimeOut() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://hrisgelora.co.id/api/get_lunch_request_timeout";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            String time = response.getString("time");
                            timeOutRequest = time;
                            warningTV.setText("Pengajuan makan siang karyawan harus diajukan paling lambat H-1 dari tanggal yang dipilih, sedangkan untuk pengajuan makan sore dan malam paling lambat pukul "+time.substring(0,5)+" WIB pada tanggal yang dipilih.");

                            getData();
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

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_detail_lunch_request";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {
                                String bagian = data.getString("bagian");
                                String tanggal = data.getString("tanggal");
                                String pusat_siang_k = data.getString("pusat_siang_k");
                                String pusat_siang_s = data.getString("pusat_siang_s");
                                String pusat_sore = data.getString("pusat_sore");
                                String pusat_malam = data.getString("pusat_malam");
                                String gapprint_siang = data.getString("gapprint_siang");
                                String gapprint_sore = data.getString("gapprint_sore");
                                String gapprint_malam = data.getString("gapprint_malam");

                                bagianTV.setText(bagian);

                                String input_date = tanggal;
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                                Date dt1= null;
                                try {
                                    dt1 = format1.parse(input_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEE");
                                String finalDay = format2.format(dt1);
                                String hariName = "";

                                if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                                    hariName = "Senin";
                                } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                                    hariName = "Selasa";
                                } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                                    hariName = "Rabu";
                                } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                                    hariName = "Kamis";
                                } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                                    hariName = "Jumat";
                                } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                                    hariName = "Sabtu";
                                } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                                    hariName = "Minggu";
                                }

                                String dayDate = input_date.substring(8,10);
                                String yearDate = input_date.substring(0,4);
                                String bulanValue = input_date.substring(5,7);
                                String bulanName;

                                switch (bulanValue) {
                                    case "01":
                                        bulanName = "Januari";
                                        break;
                                    case "02":
                                        bulanName = "Februari";
                                        break;
                                    case "03":
                                        bulanName = "Maret";
                                        break;
                                    case "04":
                                        bulanName = "April";
                                        break;
                                    case "05":
                                        bulanName = "Mei";
                                        break;
                                    case "06":
                                        bulanName = "Juni";
                                        break;
                                    case "07":
                                        bulanName = "Juli";
                                        break;
                                    case "08":
                                        bulanName = "Agustus";
                                        break;
                                    case "09":
                                        bulanName = "September";
                                        break;
                                    case "10":
                                        bulanName = "Oktober";
                                        break;
                                    case "11":
                                        bulanName = "November";
                                        break;
                                    case "12":
                                        bulanName = "Desember";
                                        break;
                                    default:
                                        bulanName = "Not found";
                                        break;
                                }

                                tanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                                jumlahTV1.setText(pusat_siang_k);
                                jumlahTV2.setText(pusat_siang_s);
                                jumlahTV3.setText(pusat_sore);
                                jumlahTV4.setText(pusat_malam);
                                jumlahTV5.setText(gapprint_siang);
                                jumlahTV6.setText(gapprint_sore);
                                jumlahTV7.setText(gapprint_malam);

                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                Date date2 = null;
                                try {
                                    date = sdf.parse(String.valueOf(tanggal));
                                    date2 = sdf.parse(getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long pilih = date.getTime();
                                long sekarang = date2.getTime();

                                if (pilih==sekarang){
                                    String jamString = getTimeNow();
                                    String batasString = timeOutRequest;

                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                    try {
                                        Date jam = format.parse(jamString);
                                        Date batas = format.parse(batasString);

                                        if (jam.before(batas)) {
                                            inputPart.setVisibility(View.VISIBLE);
                                            siangPart1.setVisibility(View.GONE);
                                            siangPart2.setVisibility(View.GONE);
                                            soreMalamPart1.setVisibility(View.VISIBLE);
                                            soreMalamPart2.setVisibility(View.VISIBLE);
                                            closePart.setVisibility(View.GONE);

                                            submitLabelTV.setText("KIRIM");
                                            submitBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    submitData();
                                                }
                                            });
                                        } else {
                                            inputPart.setVisibility(View.GONE);
                                            siangPart1.setVisibility(View.GONE);
                                            siangPart2.setVisibility(View.GONE);
                                            soreMalamPart1.setVisibility(View.GONE);
                                            soreMalamPart2.setVisibility(View.GONE);
                                            closePart.setVisibility(View.VISIBLE);

                                            submitLabelTV.setText("KEMBALI");
                                            submitBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    onBackPressed();
                                                }
                                            });
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else {
                                    inputPart.setVisibility(View.VISIBLE);
                                    siangPart1.setVisibility(View.VISIBLE);
                                    siangPart2.setVisibility(View.VISIBLE);
                                    soreMalamPart1.setVisibility(View.VISIBLE);
                                    soreMalamPart2.setVisibility(View.VISIBLE);
                                    closePart.setVisibility(View.GONE);

                                    submitLabelTV.setText("KIRIM");
                                    submitBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            submitData();
                                        }
                                    });

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idPermohonan);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void submitData(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        jumlahTV1.clearFocus();
        jumlahTV2.clearFocus();
        jumlahTV3.clearFocus();
        jumlahTV4.clearFocus();
        jumlahTV5.clearFocus();
        jumlahTV6.clearFocus();
        jumlahTV7.clearFocus();

        jumlahTV1.setText(String.valueOf(pusat_siang1));
        jumlahTV2.setText(String.valueOf(pusat_siang2));
        jumlahTV3.setText(String.valueOf(pusat_sore));
        jumlahTV4.setText(String.valueOf(pusat_malam));
        jumlahTV5.setText(String.valueOf(gapprint_siang));
        jumlahTV6.setText(String.valueOf(gapprint_sore));
        jumlahTV7.setText(String.valueOf(gapprint_malam));

        new KAlertDialog(EditLunchRequestActivity.this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Perhatian")
                .setContentText("Kirim perubahan data sekarang?")
                .setCancelText("TIDAK")
                .setConfirmText("   YA   ")
                .showCancelButton(true)
                .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog sDialog) {
                        sDialog.dismiss();
                        pDialog = new KAlertDialog(EditLunchRequestActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                        pDialog.show();
                        pDialog.setCancelable(false);
                        new CountDownTimer(1300, 800) {
                            public void onTick(long millisUntilFinished) {
                                i++;
                                switch (i) {
                                    case 0:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (EditLunchRequestActivity.this, R.color.colorGradien));
                                        break;
                                    case 1:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (EditLunchRequestActivity.this, R.color.colorGradien2));
                                        break;
                                    case 2:
                                    case 6:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (EditLunchRequestActivity.this, R.color.colorGradien3));
                                        break;
                                    case 3:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (EditLunchRequestActivity.this, R.color.colorGradien4));
                                        break;
                                    case 4:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (EditLunchRequestActivity.this, R.color.colorGradien5));
                                        break;
                                    case 5:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (EditLunchRequestActivity.this, R.color.colorGradien6));
                                        break;
                                }
                            }
                            public void onFinish() {
                                i = -1;
                                sendData();
                            }
                        }.start();

                    }
                })
                .show();

    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/update_lunch_request";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);
                            } else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Tersimpan")
                                        .setContentText("Terjadi kesalahan saat mengirim data")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                        successPart.setVisibility(View.GONE);
                        formPart.setVisibility(View.VISIBLE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", idPermohonan);
                params.put("pusat_siang_k", String.valueOf(pusat_siang1));
                params.put("pusat_siang_s", String.valueOf(pusat_siang2));
                params.put("pusat_sore", String.valueOf(pusat_sore));
                params.put("pusat_malam", String.valueOf(pusat_malam));
                params.put("gapprint_siang", String.valueOf(gapprint_siang));
                params.put("gapprint_sore", String.valueOf(gapprint_sore));
                params.put("gapprint_malam", String.valueOf(gapprint_malam));

                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(EditLunchRequestActivity.this)
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

    private String getTimeNow() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}