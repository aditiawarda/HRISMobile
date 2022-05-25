package com.gelora.absensi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.gelora.absensi.adapter.AdapterDataHadir;
import com.gelora.absensi.adapter.AdapterDataNoCheckout;
import com.gelora.absensi.model.DataHadir;
import com.gelora.absensi.model.DataNoCheckout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailHadirActivity extends AppCompatActivity {

    LinearLayout monthBTN, emptyDataHadir, loadingHadirPart, backBTN, homeBTN;
    ImageView bulanLoading, hadirLoading, loadingDataHadir;
    TextView dataBulan, dataTahun, dataHadir, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;

    private RecyclerView dataHadirRV;
    private DataHadir[] dataHadirs;
    private AdapterDataHadir adapterDataHadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hadir);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        bulanLoading = findViewById(R.id.bulan_loading);
        hadirLoading = findViewById(R.id.hadir_loading);
        dataHadir = findViewById(R.id.data_hadir);
        loadingDataHadir = findViewById(R.id.loading_data_hadir);
        loadingHadirPart = findViewById(R.id.loading_data_part_hadir);
        emptyDataHadir = findViewById(R.id.no_data_part_hadir);
        monthBTN = findViewById(R.id.month_btn);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataHadirRV = findViewById(R.id.data_hadir_rv);

        dataHadirRV.setLayoutManager(new LinearLayoutManager(this));
        dataHadirRV.setHasFixedSize(true);
        dataHadirRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(hadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataHadir);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                hadirLoading.setVisibility(View.VISIBLE);
                dataHadir.setVisibility(View.GONE);

                dataHadirRV.setVisibility(View.GONE);
                loadingHadirPart.setVisibility(View.VISIBLE);
                emptyDataHadir.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailHadir();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RackMonthPicker(DetailHadirActivity.this)
                        .setLocale(Locale.ENGLISH)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                String bulan = "";
                                if(month==1){
                                    bulan = "01";
                                } else if (month==2){
                                    bulan = "02";
                                } else if (month==3){
                                    bulan = "03";
                                } else if (month==4){
                                    bulan = "04";
                                } else if (month==5){
                                    bulan = "05";
                                } else if (month==6){
                                    bulan = "06";
                                } else if (month==7){
                                    bulan = "07";
                                } else if (month==8){
                                    bulan = "08";
                                } else if (month==9){
                                    bulan = "09";
                                } else{
                                    bulan = String.valueOf(month);
                                }
                                bulanPilih = String.valueOf(year)+"-"+bulan;

                                bulanLoading.setVisibility(View.VISIBLE);
                                dataBulan.setVisibility(View.GONE);
                                dataTahun.setVisibility(View.GONE);

                                hadirLoading.setVisibility(View.VISIBLE);
                                dataHadir.setVisibility(View.GONE);

                                dataHadirRV.setVisibility(View.GONE);
                                loadingHadirPart.setVisibility(View.VISIBLE);
                                emptyDataHadir.setVisibility(View.GONE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDetailHadir();
                                    }
                                }, 500);

                            }
                        })
                        .setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHadirActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDetailHadir();

    }

    private void getDetailHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/detail_kehadiran";
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
                            if (status.equals("Success")) {
                                String hadir = data.getString("hadir");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataHadir.setText(hadir);

                                hadirLoading.setVisibility(View.GONE);
                                dataHadir.setVisibility(View.VISIBLE);

                                if (hadir.equals("0")) {
                                    emptyDataHadir.setVisibility(View.VISIBLE);
                                    dataHadirRV.setVisibility(View.GONE);
                                    loadingHadirPart.setVisibility(View.GONE);
                                } else {
                                    dataHadirRV.setVisibility(View.VISIBLE);
                                    loadingHadirPart.setVisibility(View.GONE);
                                    String data_hadir = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataHadirs = gson.fromJson(data_hadir, DataHadir[].class);
                                    adapterDataHadir = new AdapterDataHadir(dataHadirs, DetailHadirActivity.this);
                                    dataHadirRV.setAdapter(adapterDataHadir);
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
                        //connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", bulanPilih);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

}