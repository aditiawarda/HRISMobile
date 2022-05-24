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
import com.gelora.absensi.adapter.AdapterDataAlpa;
import com.gelora.absensi.adapter.AdapterDataIzin;
import com.gelora.absensi.adapter.AdapterHistoryAbsen;
import com.gelora.absensi.model.DataAlpa;
import com.gelora.absensi.model.DataIzin;
import com.gelora.absensi.model.HistoryAbsen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailTidakHadirActivity extends AppCompatActivity {

    LinearLayout emptyDataIzin, emptyDataAlpa, loadingIzinPart, loadingAlpaPart, backBTN, homeBTN;
    ImageView bulanLoading, tidakHadirLoading, loadingDataIzin, loadingDataAlpa;
    TextView dataTotalIzin, dataTotalAlpa, dataBulan, dataTahun, dataTidakHadir, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    private RecyclerView dataIzinRV;
    private DataIzin[] dataIzins;
    private AdapterDataIzin adapterDataIzin;

    private RecyclerView dataAlpaRV;
    private DataAlpa[] dataAlpas;
    private AdapterDataAlpa adapterDataAlpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tidak_hadir);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        dataTidakHadir = findViewById(R.id.data_tidak_hadir);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        dataTotalIzin = findViewById(R.id.total_data_izin);
        dataTotalAlpa = findViewById(R.id.total_data_alpa);
        bulanLoading = findViewById(R.id.bulan_loading);
        tidakHadirLoading = findViewById(R.id.tidak_hadir_loading);
        loadingDataAlpa = findViewById(R.id.loading_data_alpa);
        loadingDataIzin = findViewById(R.id.loading_data_izin);
        loadingIzinPart = findViewById(R.id.loading_data_part_izin);
        loadingAlpaPart = findViewById(R.id.loading_data_part_alpa);
        emptyDataAlpa = findViewById(R.id.no_data_part_alpa);
        emptyDataIzin = findViewById(R.id.no_data_part_izin);

        dataIzinRV = findViewById(R.id.data_izin_rv);
        dataAlpaRV = findViewById(R.id.data_alpa_rv);

        dataIzinRV.setLayoutManager(new LinearLayoutManager(this));
        dataIzinRV.setHasFixedSize(true);
        dataIzinRV.setItemAnimator(new DefaultItemAnimator());

        dataAlpaRV.setLayoutManager(new LinearLayoutManager(this));
        dataAlpaRV.setHasFixedSize(true);
        dataAlpaRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(tidakHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataIzin);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataAlpa);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataTidakHadir();
                        getDetailTidakHadir();
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
                Intent intent = new Intent(DetailTidakHadirActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDataTidakHadir();
        getDetailTidakHadir();

    }

    private void getDataTidakHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_tidak_hadir";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("message");
                            if (status.equals("Success")){
                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");
                                String total_tidak_hadir = data.getString("total_tidak_hadir");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);
                                dataTidakHadir.setText(total_tidak_hadir);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        bulanLoading.setVisibility(View.GONE);
                                        dataBulan.setVisibility(View.VISIBLE);
                                        dataTahun.setVisibility(View.VISIBLE);

                                        tidakHadirLoading.setVisibility(View.GONE);
                                        dataTidakHadir.setVisibility(View.VISIBLE);
                                    }
                                }, 500);

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
                        //connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", getBulanTahun());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getDetailTidakHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/detail_tidak_hadir";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
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
                                dataIzins = gson.fromJson(izin, DataIzin[].class);
                                adapterDataIzin = new AdapterDataIzin(dataIzins,DetailTidakHadirActivity.this);
                                dataIzinRV.setAdapter(adapterDataIzin);
                                dataAlpas = gson.fromJson(alpa, DataAlpa[].class);
                                adapterDataAlpa = new AdapterDataAlpa(dataAlpas,DetailTidakHadirActivity.this);
                                dataAlpaRV.setAdapter(adapterDataAlpa);

                                if (total_izin.equals("0")){
                                    emptyDataIzin.setVisibility(View.VISIBLE);
                                    dataIzinRV.setVisibility(View.GONE);
                                    loadingIzinPart.setVisibility(View.GONE);
                                } else {
                                    dataIzinRV.setVisibility(View.VISIBLE);
                                    loadingIzinPart.setVisibility(View.GONE);
                                }

                                if (total_alpa.equals("0")){
                                    emptyDataAlpa.setVisibility(View.VISIBLE);
                                    dataAlpaRV.setVisibility(View.GONE);
                                    loadingAlpaPart.setVisibility(View.GONE);
                                } else {
                                    dataAlpaRV.setVisibility(View.VISIBLE);
                                    loadingAlpaPart.setVisibility(View.GONE);
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
                        //connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", getBulanTahun());
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