package com.gelora.absensi;

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
import com.gelora.absensi.adapter.AdapterKehadiranBagian;
import com.gelora.absensi.adapter.AdapterPulangCepat;
import com.gelora.absensi.model.DataMonitoringKehadiranBagian;
import com.gelora.absensi.model.DataPulangCepat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MonitoringAbsensiBagianActivity extends AppCompatActivity {

    TextView jumlahKaryawanTV, currentDateTV, currentDayTV, namaDepartemen, namaBagian, monitorDataHadir, monitorDataTidakHadir;
    String currentDay = "";
    SharedPrefManager sharedPrefManager;
    ImageView dataTidakHadirLoading, dataHadirLoading, loadingDataKehadiranBagian;
    SwipeRefreshLayout refreshLayout;
    LinearLayout backBTN, homeBTN, loadingDataKehadiranPart;

    private RecyclerView dataKehadiranBagianRV;
    private DataMonitoringKehadiranBagian[] dataMonitoringKehadiranBagians;
    private AdapterKehadiranBagian adapterKehadiranBagian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_absensi_bagian);

        sharedPrefManager = new SharedPrefManager(this);
        jumlahKaryawanTV = findViewById(R.id.jumlah_karyawan_tv);
        currentDateTV = findViewById(R.id.current_date);
        currentDayTV = findViewById(R.id.current_day);
        namaDepartemen = findViewById(R.id.nama_departemen);
        namaBagian = findViewById(R.id.nama_bagian);
        monitorDataHadir = findViewById(R.id.monitor_data_hadir);
        monitorDataTidakHadir = findViewById(R.id.monitor_data_tidak_hadir);
        dataHadirLoading = findViewById(R.id.monitor_data_hadir_loading);
        dataTidakHadirLoading = findViewById(R.id.monitor_data_tidak_hadir_loading);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        loadingDataKehadiranPart = findViewById(R.id.loading_data_kehadiran_bagian_part);
        loadingDataKehadiranBagian = findViewById(R.id.loading_data_kehadiran_bagian);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(dataHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(dataTidakHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataKehadiranBagian);

        namaBagian.setText(getIntent().getExtras().getString("nama_bagian"));
        namaDepartemen.setText(getIntent().getExtras().getString("nama_departemen"));

        dataKehadiranBagianRV = findViewById(R.id.data_kehadiran_bagian_rv);
        dataKehadiranBagianRV.setLayoutManager(new LinearLayoutManager(this));
        dataKehadiranBagianRV.setHasFixedSize(true);
        dataKehadiranBagianRV.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dataHadirLoading.setVisibility(View.VISIBLE);
                dataTidakHadirLoading.setVisibility(View.VISIBLE);
                monitorDataHadir.setVisibility(View.GONE);
                monitorDataTidakHadir.setVisibility(View.GONE);

                loadingDataKehadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getKehadiranBagian();
                        getCurrentDay();
                    }
                }, 800);
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
                Intent intent = new Intent(MonitoringAbsensiBagianActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        getKehadiranBagian();
        getCurrentDay();

    }

    private void getKehadiranBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/monitoring_bagian";
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
                                String jumlah_karyawan = data.getString("jumlah_karyawan");
                                String hadir = data.getString("hadir");
                                String tidak_hadir = data.getString("tidak_hadir");

                                jumlahKaryawanTV.setText(jumlah_karyawan);
                                monitorDataHadir.setText(hadir);
                                monitorDataTidakHadir.setText(tidak_hadir);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataHadirLoading.setVisibility(View.GONE);
                                        dataTidakHadirLoading.setVisibility(View.GONE);
                                        monitorDataHadir.setVisibility(View.VISIBLE);
                                        monitorDataTidakHadir.setVisibility(View.VISIBLE);
                                    }
                                }, 1000);


                                if (hadir.equals("0")) {
                                    dataKehadiranBagianRV.setVisibility(View.GONE);
                                    loadingDataKehadiranPart.setVisibility(View.GONE);
                                } else {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingDataKehadiranPart.setVisibility(View.GONE);
                                            dataKehadiranBagianRV.setVisibility(View.VISIBLE);
                                        }
                                    }, 1000);

                                    String data_kehadiran_bagian = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataMonitoringKehadiranBagians = gson.fromJson(data_kehadiran_bagian, DataMonitoringKehadiranBagian[].class);
                                    adapterKehadiranBagian = new AdapterKehadiranBagian(dataMonitoringKehadiranBagians, MonitoringAbsensiBagianActivity.this);
                                    dataKehadiranBagianRV.setAdapter(adapterKehadiranBagian);
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
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("tanggal", getDate());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SetTextI18n")
    private void dateLive(){
        currentDayTV.setText(currentDay+",");
        switch (getDateM()) {
            case "01":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                break;
            case "02":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                break;
            case "03":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                break;
            case "04":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                break;
            case "05":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                break;
            case "06":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                break;
            case "07":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                break;
            case "08":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                break;
            case "09":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                break;
            case "10":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                break;
            case "11":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                break;
            case "12":
                currentDateTV.setText(String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                break;
            default:
                currentDateTV.setText("Not found!");
                break;
        }

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
        //return ("11");
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

    private void getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay = "Minggu";
                break;
            case Calendar.MONDAY:
                currentDay = "Senin";
                break;
            case Calendar.TUESDAY:
                currentDay = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "Rabu";
                break;
            case Calendar.THURSDAY:
                currentDay = "Kamis";
                break;
            case Calendar.FRIDAY:
                currentDay = "Jumat";
                break;
            case Calendar.SATURDAY:
                currentDay = "Sabtu";
                break;
        }

        dateLive();

    }

}