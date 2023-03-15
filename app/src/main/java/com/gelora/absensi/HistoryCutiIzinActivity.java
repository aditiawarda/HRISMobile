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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataHistoryPenambahanCuti;
import com.gelora.absensi.adapter.AdapterDataHistoryCuti;
import com.gelora.absensi.adapter.AdapterDataHistoryCutiBersama;
import com.gelora.absensi.adapter.AdapterDataHistoryIzin;
import com.gelora.absensi.model.DataHistorPenambahanCuti;
import com.gelora.absensi.model.DataHistoryCuti;
import com.gelora.absensi.model.DataHistoryCutiBersama;
import com.gelora.absensi.model.DataHistoryIzin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HistoryCutiIzinActivity extends AppCompatActivity {

    LinearLayout actionBar, loadingDataPartPenambahanCuti, noDataPartPenambahanCuti, attantionPart, backBTN, loadingDataPartCutiBersama, loadingDataPartCuti, loadingDataPartIzin, noDataPartCutiBersama, noDataPartCuti, noDataPartIzin, dataDiambilPart, dataSisaPart;
    TextView totalDataPenambahanCuti, hakCutiTV, totalDataCutiBersama, totalDataCuti, totalDataIzin, nameUserTV, periodeData, dataDiambilTV, dataSisaTV;
    RelativeLayout periodeDataPart;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    ImageView loadingDataPenambahanCuti, diambilLoading, sisaLoading, periodeLoading, loadingDataCutiBersama, loadingDataCuti, loadingDataIzin;
    View rootview;

    private RecyclerView dataHistoryCutiRV;
    private DataHistoryCuti[] dataHistoryCutis;
    private AdapterDataHistoryCuti adapterDataHistoryCuti;

    private RecyclerView dataHistoryCutiBersamaRV;
    private DataHistoryCutiBersama[] dataHistoryCutiBersamas;
    private AdapterDataHistoryCutiBersama adapterDataHistoryCutiBersama;

    private RecyclerView dataHistoryIzinRV;
    private DataHistoryIzin[] dataHistoryIzins;
    private AdapterDataHistoryIzin adapterDataHistoryIzin;

    private RecyclerView dataHistoryPenambahanCutiRV;
    private DataHistorPenambahanCuti[] dataHistorPenambahanCutis;
    private AdapterDataHistoryPenambahanCuti adapterDataHistoryPenambahanCuti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cuti_izin);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        backBTN = findViewById(R.id.back_btn);
        diambilLoading = findViewById(R.id.sisa_loading);
        sisaLoading = findViewById(R.id.diambil_loading);
        periodeLoading = findViewById(R.id.periode_loading);
        loadingDataCuti = findViewById(R.id.loading_data_cuti);
        loadingDataCutiBersama = findViewById(R.id.loading_data_cuti_bersama);
        loadingDataPenambahanCuti = findViewById(R.id.loading_data_penambahan_cuti);
        loadingDataIzin = findViewById(R.id.loading_data_izin);
        loadingDataPartCuti = findViewById(R.id.loading_data_part_cuti);
        loadingDataPartCutiBersama = findViewById(R.id.loading_data_part_cuti_bersama);
        loadingDataPartIzin = findViewById(R.id.loading_data_part_izin);
        loadingDataPartPenambahanCuti = findViewById(R.id.loading_data_part_penambahan_cuti);
        noDataPartCuti = findViewById(R.id.no_data_part_cuti);
        noDataPartCutiBersama = findViewById(R.id.no_data_part_cuti_bersama);
        noDataPartIzin = findViewById(R.id.no_data_part_izin);
        noDataPartPenambahanCuti = findViewById(R.id.no_data_part_penambahan_cuti);
        periodeData = findViewById(R.id.periode_data);
        dataDiambilTV = findViewById(R.id.data_diambil_tv);
        dataSisaTV = findViewById(R.id.data_sisa_tv);
        dataDiambilPart = findViewById(R.id.data_diambil_part);
        dataSisaPart = findViewById(R.id.data_sisa_part);
        totalDataCuti = findViewById(R.id.total_data_cuti);
        totalDataCutiBersama = findViewById(R.id.total_data_cuti_bersama);
        totalDataPenambahanCuti = findViewById(R.id.total_data_penambahan_cuti);
        totalDataIzin = findViewById(R.id.total_data_izin);
        periodeDataPart = findViewById(R.id.periode_data_part);
        hakCutiTV = findViewById(R.id.hak_cuti_tv);
        attantionPart = findViewById(R.id.attantion_part);
        actionBar = findViewById(R.id.action_bar);

        dataHistoryCutiRV = findViewById(R.id.data_cuti_rv);
        dataHistoryIzinRV = findViewById(R.id.data_izin_rv);
        dataHistoryCutiBersamaRV = findViewById(R.id.data_cuti_bersama_rv);
        dataHistoryPenambahanCutiRV = findViewById(R.id.data_penambahan_cuti_rv);

        dataHistoryCutiRV.setLayoutManager(new LinearLayoutManager(this));
        dataHistoryCutiRV.setHasFixedSize(true);
        dataHistoryCutiRV.setNestedScrollingEnabled(false);
        dataHistoryCutiRV.setItemAnimator(new DefaultItemAnimator());

        dataHistoryIzinRV.setLayoutManager(new LinearLayoutManager(this));
        dataHistoryIzinRV.setHasFixedSize(true);
        dataHistoryIzinRV.setNestedScrollingEnabled(false);
        dataHistoryIzinRV.setItemAnimator(new DefaultItemAnimator());

        dataHistoryCutiBersamaRV.setLayoutManager(new LinearLayoutManager(this));
        dataHistoryCutiBersamaRV.setHasFixedSize(true);
        dataHistoryCutiBersamaRV.setNestedScrollingEnabled(false);
        dataHistoryCutiBersamaRV.setItemAnimator(new DefaultItemAnimator());

        dataHistoryPenambahanCutiRV.setLayoutManager(new LinearLayoutManager(this));
        dataHistoryPenambahanCutiRV.setHasFixedSize(true);
        dataHistoryPenambahanCutiRV.setNestedScrollingEnabled(false);
        dataHistoryPenambahanCutiRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(periodeLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(diambilLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(sisaLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataIzin);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataCuti);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataCutiBersama);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataPenambahanCuti);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                totalDataCuti.setText("");
                totalDataCutiBersama.setText("");
                totalDataIzin.setText("");
                totalDataPenambahanCuti.setText("");

                periodeLoading.setVisibility(View.VISIBLE);
                periodeDataPart.setVisibility(View.GONE);

                diambilLoading.setVisibility(View.VISIBLE);
                dataDiambilPart.setVisibility(View.GONE);

                sisaLoading.setVisibility(View.VISIBLE);
                dataSisaPart.setVisibility(View.GONE);

                noDataPartCuti.setVisibility(View.GONE);
                loadingDataPartCuti.setVisibility(View.VISIBLE);
                dataHistoryCutiRV.setVisibility(View.GONE);

                noDataPartIzin.setVisibility(View.GONE);
                loadingDataPartIzin.setVisibility(View.VISIBLE);
                dataHistoryIzinRV.setVisibility(View.GONE);

                noDataPartCutiBersama.setVisibility(View.GONE);
                loadingDataPartCutiBersama.setVisibility(View.VISIBLE);
                dataHistoryCutiBersamaRV.setVisibility(View.GONE);

                noDataPartPenambahanCuti.setVisibility(View.GONE);
                loadingDataPartPenambahanCuti.setVisibility(View.VISIBLE);
                dataHistoryPenambahanCutiRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataHitoryCuti();
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

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDataHitoryCuti();

    }

    private void getDataHitoryCuti() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_cuti_history";
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
                            if (status.equals("Success")){
                                String periode_mulai = data.getString("periode_mulai");
                                String periode_akhir = data.getString("periode_akhir");
                                String hak_cuti_tahunan = data.getString("hak_cuti_tahunan");
                                String cuti_ambil = data.getString("cuti_ambil");
                                String sisa_cuti = data.getString("sisa_cuti");
                                String tahun = data.getString("tahun");
                                String jumlah_cuti = data.getString("jumlah_cuti");
                                String jumlah_izin = data.getString("jumlah_izin");
                                String jumlah_cuti_bersama = data.getString("jumlah_cuti_bersama");
                                String jumlah_penambahan_cuti = data.getString("jumlah_penambahan_cuti");

                                if(hak_cuti_tahunan.equals("10")){
                                    attantionPart.setVisibility(View.VISIBLE);
                                } else if(hak_cuti_tahunan.equals("12")) {
                                    attantionPart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                }

                                hakCutiTV.setText(hak_cuti_tahunan);
                                periodeData.setText(periode_mulai.substring(8,10)+"/"+periode_mulai.substring(5,7)+"/"+periode_mulai.substring(0,4)+"  s.d. "+periode_akhir.substring(8,10)+"/"+periode_akhir.substring(5,7)+"/"+periode_akhir.substring(0,4));
                                periodeLoading.setVisibility(View.GONE);
                                periodeDataPart.setVisibility(View.VISIBLE);

                                dataDiambilTV.setText(cuti_ambil);
                                dataSisaTV.setText(sisa_cuti);
                                diambilLoading.setVisibility(View.GONE);
                                dataDiambilPart.setVisibility(View.VISIBLE);
                                sisaLoading.setVisibility(View.GONE);
                                dataSisaPart.setVisibility(View.VISIBLE);

                                totalDataCuti.setText(jumlah_cuti);
                                totalDataCutiBersama.setText(jumlah_cuti_bersama);
                                totalDataIzin.setText(jumlah_izin);
                                totalDataPenambahanCuti.setText(jumlah_penambahan_cuti);

                                if(jumlah_cuti.equals("0")){
                                    noDataPartCuti.setVisibility(View.VISIBLE);
                                    loadingDataPartCuti.setVisibility(View.GONE);
                                    dataHistoryCutiRV.setVisibility(View.GONE);
                                } else {
                                    String history_cuti_data = data.getString("history_cuti_data");

                                    noDataPartCuti.setVisibility(View.GONE);
                                    loadingDataPartCuti.setVisibility(View.GONE);
                                    dataHistoryCutiRV.setVisibility(View.VISIBLE);

                                    GsonBuilder builder =new GsonBuilder();
                                    Gson gson = builder.create();

                                    dataHistoryCutis = gson.fromJson(history_cuti_data, DataHistoryCuti[].class);
                                    adapterDataHistoryCuti = new AdapterDataHistoryCuti(dataHistoryCutis,HistoryCutiIzinActivity.this);
                                    dataHistoryCutiRV.setAdapter(adapterDataHistoryCuti);
                                }

                                if(jumlah_izin.equals("0")){
                                    noDataPartIzin.setVisibility(View.VISIBLE);
                                    loadingDataPartIzin.setVisibility(View.GONE);
                                    dataHistoryIzinRV.setVisibility(View.GONE);
                                } else {
                                    String history_izin_data = data.getString("history_izin_data");

                                    noDataPartIzin.setVisibility(View.GONE);
                                    loadingDataPartIzin.setVisibility(View.GONE);
                                    dataHistoryIzinRV.setVisibility(View.VISIBLE);

                                    GsonBuilder builder =new GsonBuilder();
                                    Gson gson = builder.create();

                                    dataHistoryIzins = gson.fromJson(history_izin_data, DataHistoryIzin[].class);
                                    adapterDataHistoryIzin = new AdapterDataHistoryIzin(dataHistoryIzins,HistoryCutiIzinActivity.this);
                                    dataHistoryIzinRV.setAdapter(adapterDataHistoryIzin);
                                }

                                if(jumlah_cuti_bersama.equals("0")){
                                    noDataPartCutiBersama.setVisibility(View.VISIBLE);
                                    loadingDataPartCutiBersama.setVisibility(View.GONE);
                                    dataHistoryCutiBersamaRV.setVisibility(View.GONE);
                                } else {
                                    String history_cuti_bersama_data = data.getString("history_cuti_bersama_data");

                                    noDataPartCutiBersama.setVisibility(View.GONE);
                                    loadingDataPartCutiBersama.setVisibility(View.GONE);
                                    dataHistoryCutiBersamaRV.setVisibility(View.VISIBLE);

                                    GsonBuilder builder =new GsonBuilder();
                                    Gson gson = builder.create();

                                    dataHistoryCutiBersamas = gson.fromJson(history_cuti_bersama_data, DataHistoryCutiBersama[].class);
                                    adapterDataHistoryCutiBersama = new AdapterDataHistoryCutiBersama(dataHistoryCutiBersamas,HistoryCutiIzinActivity.this);
                                    dataHistoryCutiBersamaRV.setAdapter(adapterDataHistoryCutiBersama);
                                }

                                if(jumlah_penambahan_cuti.equals("0")){
                                    noDataPartPenambahanCuti.setVisibility(View.VISIBLE);
                                    loadingDataPartPenambahanCuti.setVisibility(View.GONE);
                                    dataHistoryPenambahanCutiRV.setVisibility(View.GONE);
                                } else {
                                    String history_cuti_penambahan_cuti = data.getString("history_cuti_penambahan_cuti");

                                    noDataPartPenambahanCuti.setVisibility(View.GONE);
                                    loadingDataPartPenambahanCuti.setVisibility(View.GONE);
                                    dataHistoryPenambahanCutiRV.setVisibility(View.VISIBLE);

                                    GsonBuilder builder =new GsonBuilder();
                                    Gson gson = builder.create();

                                    dataHistorPenambahanCutis = gson.fromJson(history_cuti_penambahan_cuti, DataHistorPenambahanCuti[].class);
                                    adapterDataHistoryPenambahanCuti = new AdapterDataHistoryPenambahanCuti(dataHistorPenambahanCutis,HistoryCutiIzinActivity.this);
                                    dataHistoryPenambahanCutiRV.setAdapter(adapterDataHistoryPenambahanCuti);
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
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(HistoryCutiIzinActivity.this)
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