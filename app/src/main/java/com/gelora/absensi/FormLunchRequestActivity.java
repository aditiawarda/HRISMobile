package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterUnitBagianLunchRequest;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.BagianLunchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormLunchRequestActivity extends AppCompatActivity {

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

    SwipeRefreshLayout refreshLayout;
    LinearLayout submitBTN, backBTN, bagianBTN, dateBTN, formPart, successPart, rebackBTN, actionBar;
    TextView bagianPilihTV, tanggalPilihTV;
    ImageView successGif;

    private RecyclerView bagianRV;
    AdapterUnitBagianLunchRequest adapterUnitBagianLunchRequest;
    private BagianLunchRequest[] bagianLunchRequests;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    int pusat_siang1 = 0, pusat_siang2 = 0, pusat_sore = 0, pusat_malam = 0;
    int gapprint_siang = 0, gapprint_sore = 0, gapprint_malam = 0;
    String permohonanTerkirim = "0", tanggal = "", bagianRL = "";
    KAlertDialog pDialog;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lunch_request);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);

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
        backBTN = findViewById(R.id.back_btn);
        bagianBTN = findViewById(R.id.bagian_btn);
        bagianPilihTV = findViewById(R.id.bagian_pilih_tv);
        dateBTN = findViewById(R.id.date_btn);
        tanggalPilihTV = findViewById(R.id.date_pilih_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);
        rebackBTN = findViewById(R.id.reback_btn);
        actionBar = findViewById(R.id.action_bar);

        bagianRL = getIntent().getExtras().getString("bagianRL");
        bagianPilihTV.setText(bagianRL);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        LocalBroadcastManager.getInstance(this).registerReceiver(bagianBroadRL, new IntentFilter("bagian_broad_rl"));

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bagianPilihTV.setText(bagianRL);
                pusat_siang1 = 0;
                pusat_siang2 = 0;
                pusat_sore = 0;
                pusat_malam = 0;
                gapprint_siang = 0;
                gapprint_sore = 0;
                gapprint_malam = 0;
                tanggal = "";
                tanggalPilihTV.setText("");
                jumlahTV1.setText("0");
                jumlahTV2.setText("0");
                jumlahTV3.setText("0");
                jumlahTV5.setText("0");
                jumlahTV6.setText("0");
                jumlahTV7.setText("0");
                new Handler().postDelayed(new Runnable() {
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

        bagianBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceBagian();
            }
        });

        rebackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dateBTN.setOnClickListener(new View.OnClickListener() {
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
                choiceDate();
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

        submitBTN.setOnClickListener(new View.OnClickListener() {
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

                jumlahTV1.setText(String.valueOf(pusat_siang1));
                jumlahTV2.setText(String.valueOf(pusat_siang2));
                jumlahTV3.setText(String.valueOf(pusat_sore));
                jumlahTV4.setText(String.valueOf(pusat_malam));
                jumlahTV5.setText(String.valueOf(gapprint_siang));
                jumlahTV6.setText(String.valueOf(gapprint_sore));
                jumlahTV7.setText(String.valueOf(gapprint_malam));

                if(bagianPilihTV.getText().toString().isEmpty()){
                    new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih bagian")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if(tanggal.isEmpty()){
                        new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap pilih tanggal")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        if(pusat_siang1 ==0 && pusat_siang2 ==0 && pusat_sore ==0 && pusat_malam ==0 && gapprint_siang ==0 && gapprint_sore ==0  && gapprint_malam ==0){
                            new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi jumlah permohonan")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim permohonan sekarang?")
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
                                            pDialog = new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormLunchRequestActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormLunchRequestActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormLunchRequestActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormLunchRequestActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormLunchRequestActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormLunchRequestActivity.this, R.color.colorGradien6));
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
                    }

                }
            }
        });

    }

    private void choiceBagian(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_bagian_lunch_request, bottomSheet, false));
        bagianRV = findViewById(R.id.bagian_rv);

        try {
            bagianRV.setLayoutManager(new LinearLayoutManager(this));
            bagianRV.setHasFixedSize(true);
            bagianRV.setNestedScrollingEnabled(false);
            bagianRV.setItemAnimator(new DefaultItemAnimator());

            getBagian();
        } catch (NullPointerException e){
            Log.e("Error", e.toString());
        }

    }

    private void getBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_bagian_lunch_request";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String bagian = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            bagianLunchRequests = gson.fromJson(bagian, BagianLunchRequest[].class);
                            adapterUnitBagianLunchRequest = new AdapterUnitBagianLunchRequest(bagianLunchRequests,FormLunchRequestActivity.this);
                            bagianRV.setAdapter(adapterUnitBagianLunchRequest);

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("idHeadDept", sharedPrefManager.getSpIdHeadDept());
                params.put("idDept", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormLunchRequestActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    public BroadcastReceiver bagianBroadRL = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_bagian = intent.getStringExtra("id_bagian");
            String nama_bagian = intent.getStringExtra("nama_bagian");
            bagianPilihTV.setText(nama_bagian);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void choiceDate(){
        int y, m, d;
        if(!tanggal.isEmpty()){
            y = Integer.parseInt(tanggal.substring(0,4));
            m = Integer.parseInt(tanggal.substring(5,7));
            d = Integer.parseInt(tanggal.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormLunchRequestActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            tanggal = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

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

            if (pilih<sekarang){
                tanggalPilihTV.setText("Pilih Kembali !");
                tanggal = "";

                new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Anda tidak dapat memilih tanggal lampau. Harap pilih kembali!")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
            } else {
                String input_date = tanggal;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                Date dt1= null;
                try {
                    dt1 = format1.parse(input_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEE");
                DateFormat getweek = new SimpleDateFormat("W");
                String finalDay = format2.format(dt1);
                String week = getweek.format(dt1);
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
                        bulanName = "Not found!";
                        break;
                }

                tanggalPilihTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }

        }, y,m-1,d);
        dpd.show();

    }

    @Override
    public void onBackPressed() {
        if (pusat_siang1 !=0 || pusat_siang2 !=0 || pusat_sore !=0 || pusat_malam !=0 || gapprint_siang !=0 || gapprint_sore !=0 || gapprint_malam !=0 || !bagianPilihTV.getText().toString().isEmpty() || !tanggal.isEmpty()){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (permohonanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Apakah anda yakin untuk meninggalkan halaman ini?")
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
                                    pusat_siang1 = 0;
                                    pusat_siang2 = 0;
                                    pusat_sore = 0;
                                    pusat_malam = 0;
                                    gapprint_siang = 0;
                                    gapprint_sore = 0;
                                    gapprint_malam = 0;
                                    tanggal = "";
                                    bagianPilihTV.setText("");
                                    jumlahTV1.setText("0");
                                    jumlahTV2.setText("0");
                                    jumlahTV3.setText("0");
                                    jumlahTV4.setText("0");
                                    jumlahTV5.setText("0");
                                    jumlahTV6.setText("0");
                                    jumlahTV7.setText("0");
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        } else {
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/craete_lunch_request";
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
                                pusat_siang1 = 0;
                                pusat_siang2 = 0;
                                pusat_sore = 0;
                                pusat_malam = 0;
                                gapprint_siang = 0;
                                gapprint_sore = 0;
                                gapprint_malam = 0;
                                tanggal = "";
                                bagianPilihTV.setText("");
                                jumlahTV1.setText("0");
                                jumlahTV2.setText("0");
                                jumlahTV3.setText("0");
                                jumlahTV4.setText("0");
                                jumlahTV5.setText("0");
                                jumlahTV6.setText("0");
                                jumlahTV7.setText("0");
                            } else if(status.equals("Available")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Ajuan makan karyawan pada tanggal yang dipilih telah diajukan sebelumnya")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Time Out")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Pengajuan makan karyawan paling lambat H-1 untuk tanggal yang dipilih")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Time Out in Same Day")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                String time_close = data.getString("time_close");
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Pengajuan makan karyawan paling pukul "+time_close+" WIB")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                params.put("tanggal", tanggal);
                params.put("bagian", bagianPilihTV.getText().toString());
                params.put("pusat_siang_k", String.valueOf(pusat_siang1));
                params.put("pusat_siang_s", String.valueOf(pusat_siang2));
                params.put("pusat_sore", String.valueOf(pusat_sore));
                params.put("pusat_malam", String.valueOf(pusat_malam));
                params.put("gapprint_siang", String.valueOf(gapprint_siang));
                params.put("gapprint_sore", String.valueOf(gapprint_sore));
                params.put("gapprint_malam", String.valueOf(gapprint_malam));
                params.put("requester", sharedPrefManager.getSpNik());

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

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}