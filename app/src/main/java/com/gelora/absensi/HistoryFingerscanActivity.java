package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataHistoryFingerscan;
import com.gelora.absensi.model.DataHistoryFinger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HistoryFingerscanActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, monthBTN, attantionPart, loadingDataPart, emptyDataPart;
    TextView nameUserTV, dataBulan, dataTahun, dataFinger;
    ImageView bulanLoading, fingerLoading, loadingData;
    String bulanPilih;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    private RecyclerView dataRV;
    private DataHistoryFinger[] dataFingers;
    private AdapterDataHistoryFingerscan adapterDataFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fingerscan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        monthBTN = findViewById(R.id.month_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        bulanLoading = findViewById(R.id.bulan_loading);
        fingerLoading = findViewById(R.id.finger_loading);
        dataFinger = findViewById(R.id.data_finger);
        attantionPart = findViewById(R.id.attantion_part_finger);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingData = findViewById(R.id.loading_data);
        emptyDataPart = findViewById(R.id.no_data_part);
        actionBar = findViewById(R.id.action_bar);

        dataRV = findViewById(R.id.data_rv);

        dataRV.setLayoutManager(new LinearLayoutManager(this));
        dataRV.setHasFixedSize(true);
        dataRV.setNestedScrollingEnabled(false);
        dataRV.setItemAnimator(new DefaultItemAnimator());

        bulanPilih = getBulanTahun();

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingData);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                fingerLoading.setVisibility(View.VISIBLE);
                dataFinger.setVisibility(View.GONE);

                dataRV.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                emptyDataPart.setVisibility(View.GONE);

                attantionPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataPermohonan();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(HistoryFingerscanActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(int month, int year) { // on date set
                                String bulan = "", bulanName = "";
                                if(month==0){
                                    bulan = "01";
                                } else if (month==1){
                                    bulan = "02";
                                } else if (month==2){
                                    bulan = "03";
                                } else if (month==3){
                                    bulan = "04";
                                } else if (month==4){
                                    bulan = "05";
                                } else if (month==5){
                                    bulan = "06";
                                } else if (month==6){
                                    bulan = "07";
                                } else if (month==7){
                                    bulan = "08";
                                } else if (month==8){
                                    bulan = "09";
                                } else if (month==9){
                                    bulan = "10";
                                } else if (month==10){
                                    bulan = "11";
                                } else if (month==11){
                                    bulan = "12";
                                }

                                bulanPilih = String.valueOf(year)+"-"+bulan;

                                bulanLoading.setVisibility(View.VISIBLE);
                                dataBulan.setVisibility(View.GONE);
                                dataTahun.setVisibility(View.GONE);

                                fingerLoading.setVisibility(View.VISIBLE);
                                dataFinger.setVisibility(View.GONE);

                                dataRV.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.VISIBLE);
                                emptyDataPart.setVisibility(View.GONE);

                                attantionPart.setVisibility(View.GONE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDataPermohonan();
                                    }
                                }, 500);

                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH));

                builder.setMinYear(1952)
                        .setActivatedYear(now.get(Calendar.YEAR))
                        .setMaxYear(now.get(Calendar.YEAR))
                        .setActivatedMonth(now.get(Calendar.MONTH))
                        .build()
                        .show();
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDataPermohonan();

    }

    private void getDataPermohonan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/total_permohonan_finger";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String fingerscan = data.getString("fingerscan");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataFinger.setText(fingerscan);

                                fingerLoading.setVisibility(View.GONE);
                                dataFinger.setVisibility(View.VISIBLE);

                                if (fingerscan.equals("0")){
                                    attantionPart.setVisibility(View.GONE);
                                    emptyDataPart.setVisibility(View.VISIBLE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataRV.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    emptyDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataRV.setVisibility(View.VISIBLE);
                                    String data_finger = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataFingers = gson.fromJson(data_finger, DataHistoryFinger[].class);
                                    adapterDataFinger = new AdapterDataHistoryFingerscan(dataFingers,HistoryFingerscanActivity.this);
                                    dataRV.setAdapter(adapterDataFinger);
                                }

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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", bulanPilih);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(HistoryFingerscanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

}