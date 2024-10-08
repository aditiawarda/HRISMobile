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
import com.gelora.absensi.adapter.AdapterDataAlpa;
import com.gelora.absensi.adapter.AdapterDataIzin;
import com.gelora.absensi.model.DataAlpa;
import com.gelora.absensi.model.DataIzin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailTidakHadirActivity extends AppCompatActivity {

    LinearLayout actionBar, attantionPart,attantionPartIzin, markerWarningAlpha, monthBTN, emptyDataIzin, emptyDataAlpa, loadingIzinPart, loadingAlpaPart, backBTN;
    ImageView notificationWarningAlphaDetail, bulanLoading, tidakHadirLoading;
    TextView messageAlpha, messageIzin, dataTotalIzin, dataTotalAlpa, dataBulan, dataTahun, dataTidakHadir, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;
    View rootview;

    private RecyclerView dataIzinRV;
    private DataIzin[] dataIzins;
    private AdapterDataIzin adapterDataIzin;

    private RecyclerView dataAlpaRV;
    private DataAlpa[] dataAlpas;
    private AdapterDataAlpa adapterDataAlpa;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tidak_hadir);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        dataTidakHadir = findViewById(R.id.data_tidak_hadir);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        dataTotalIzin = findViewById(R.id.total_data_izin);
        dataTotalAlpa = findViewById(R.id.total_data_alpa);
        bulanLoading = findViewById(R.id.bulan_loading);
        tidakHadirLoading = findViewById(R.id.tidak_hadir_loading);
        loadingIzinPart = findViewById(R.id.loading_data_part_izin);
        loadingAlpaPart = findViewById(R.id.loading_data_part_alpa);
        emptyDataAlpa = findViewById(R.id.no_data_part_alpa);
        emptyDataIzin = findViewById(R.id.no_data_part_izin);
        monthBTN = findViewById(R.id.month_btn);
        markerWarningAlpha = findViewById(R.id.marker_warning_alpha_detail);
        attantionPart = findViewById(R.id.attantion_part_alpha);
        attantionPartIzin = findViewById(R.id.attantion_part_izin);
        messageAlpha = findViewById(R.id.message_alpha);
        messageIzin = findViewById(R.id.message_izin);
        notificationWarningAlphaDetail = findViewById(R.id.warning_gif_absen_nocheckout_detail);
        actionBar = findViewById(R.id.action_bar);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataIzinRV = findViewById(R.id.data_izin_rv);
        dataAlpaRV = findViewById(R.id.data_alpa_rv);

        dataIzinRV.setLayoutManager(new LinearLayoutManager(this));
        dataIzinRV.setHasFixedSize(true);
        dataIzinRV.setNestedScrollingEnabled(false);
        dataIzinRV.setItemAnimator(new DefaultItemAnimator());

        dataAlpaRV.setLayoutManager(new LinearLayoutManager(this));
        dataAlpaRV.setHasFixedSize(true);
        dataAlpaRV.setNestedScrollingEnabled(false);
        dataAlpaRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(tidakHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif_main)
                .into(notificationWarningAlphaDetail);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanPilih = getIntent().getExtras().getString("bulan");
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                tidakHadirLoading.setVisibility(View.VISIBLE);
                dataTidakHadir.setVisibility(View.GONE);

                dataIzinRV.setVisibility(View.GONE);
                loadingIzinPart.setVisibility(View.VISIBLE);
                emptyDataIzin.setVisibility(View.GONE);

                dataAlpaRV.setVisibility(View.GONE);
                loadingAlpaPart.setVisibility(View.VISIBLE);
                emptyDataAlpa.setVisibility(View.GONE);

                markerWarningAlpha.setVisibility(View.GONE);
                attantionPart.setVisibility(View.GONE);
                attantionPartIzin.setVisibility(View.GONE);

                dataTotalIzin.setText("");
                dataTotalAlpa.setText("");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataTidakHadir();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date date = sdf.parse(bulanPilih);
                    now.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(DetailTidakHadirActivity.this,
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

                                tidakHadirLoading.setVisibility(View.VISIBLE);
                                dataTidakHadir.setVisibility(View.GONE);

                                dataIzinRV.setVisibility(View.GONE);
                                loadingIzinPart.setVisibility(View.VISIBLE);
                                emptyDataIzin.setVisibility(View.GONE);

                                dataAlpaRV.setVisibility(View.GONE);
                                loadingAlpaPart.setVisibility(View.VISIBLE);
                                emptyDataAlpa.setVisibility(View.GONE);

                                markerWarningAlpha.setVisibility(View.GONE);
                                attantionPart.setVisibility(View.GONE);
                                attantionPartIzin.setVisibility(View.GONE);

                                dataTotalIzin.setText("");
                                dataTotalAlpa.setText("");

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDataTidakHadir();
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

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDataTidakHadir();

    }

    private void getDataTidakHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/total_tidak_hadir";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("message");
                            if (status.equals("Success")){
                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");
                                String total_tidak_hadir = data.getString("total_tidak_hadir");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);
                                dataTidakHadir.setText(total_tidak_hadir);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        bulanLoading.setVisibility(View.GONE);
                                        dataBulan.setVisibility(View.VISIBLE);
                                        dataTahun.setVisibility(View.VISIBLE);

                                        tidakHadirLoading.setVisibility(View.GONE);
                                        dataTidakHadir.setVisibility(View.VISIBLE);
                                    }
                                }, 500);

                                getDetailTidakHadir();

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

    private void getDetailTidakHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/detail_tidak_hadir";
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
                            String status = data.getString("message");
                            if (status.equals("Success")){
                                String total_izin = data.getString("total_izin");
                                String total_alpa = data.getString("total_alpa");
                                String izin = data.getString("izin");
                                String alpa = data.getString("alpa");

                                dataTotalIzin.setText(total_izin);
                                dataTotalAlpa.setText(total_alpa);

                                GsonBuilder builder =new GsonBuilder();
                                Gson gson = builder.create();

                                if (total_izin.equals("0")){
                                    attantionPartIzin.setVisibility(View.GONE);
                                    emptyDataIzin.setVisibility(View.VISIBLE);
                                    dataIzinRV.setVisibility(View.GONE);
                                    loadingIzinPart.setVisibility(View.GONE);
                                } else {
                                    attantionPartIzin.setVisibility(View.VISIBLE);
                                    messageIzin.setText("Terdapat "+total_izin+" data dengan keterangan, jika terdapat kekeliruan data harap segera hubungi bagian HRD.");
                                    dataIzinRV.setVisibility(View.VISIBLE);
                                    loadingIzinPart.setVisibility(View.GONE);
                                    dataIzins = gson.fromJson(izin, DataIzin[].class);
                                    adapterDataIzin = new AdapterDataIzin(dataIzins,DetailTidakHadirActivity.this);
                                    dataIzinRV.setAdapter(adapterDataIzin);
                                }

                                if (total_alpa.equals("0")){
                                    attantionPart.setVisibility(View.GONE);
                                    markerWarningAlpha.setVisibility(View.GONE);
                                    emptyDataAlpa.setVisibility(View.VISIBLE);
                                    dataAlpaRV.setVisibility(View.GONE);
                                    loadingAlpaPart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    messageAlpha.setText("Terdapat "+total_alpa+" data tanpa keterangan (A), harap segera lakukan prosedur fingerscan/form keterangan tidak absen jika pada tanggal tersebut anda hadir atau isi form izin jika pada tanggal tersebut anda tidak hadir.");
                                    markerWarningAlpha.setVisibility(View.VISIBLE);
                                    dataAlpaRV.setVisibility(View.VISIBLE);
                                    loadingAlpaPart.setVisibility(View.GONE);
                                    dataAlpas = gson.fromJson(alpa, DataAlpa[].class);
                                    adapterDataAlpa = new AdapterDataAlpa(dataAlpas,DetailTidakHadirActivity.this);
                                    dataAlpaRV.setAdapter(adapterDataAlpa);
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
        CookieBar.build(DetailTidakHadirActivity.this)
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}