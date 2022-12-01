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
import com.gelora.absensi.adapter.AdapterDataHistoryCuti;
import com.gelora.absensi.adapter.AdapterDataHistoryIzin;
import com.gelora.absensi.adapter.AdapterDataIzin;
import com.gelora.absensi.model.DataAlpa;
import com.gelora.absensi.model.DataHistoryCuti;
import com.gelora.absensi.model.DataHistoryIzin;
import com.gelora.absensi.model.DataIzin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HistoryCutiIzinActivity extends AppCompatActivity {

    LinearLayout backBTN, homeBTN, loadingDataPartCuti, loadingDataPartIzin, noDataPartCuti, noDataPartIzin, dataDiambilPart, dataSisaPart;
    TextView totalDataCuti, totalDataIzin, nameUserTV, periodeData, dataDiambilTV, dataSisaTV, tahunCutiDiambilTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    ImageView diambilLoading, sisaLoading, periodeLoading, loadingDataCuti, loadingDataIzin;
    View rootview;

    private RecyclerView dataHistoryCutiRV;
    private DataHistoryCuti[] dataHistoryCutis;
    private AdapterDataHistoryCuti adapterDataHistoryCuti;

    private RecyclerView dataHistoryIzinRV;
    private DataHistoryIzin[] dataHistoryIzins;
    private AdapterDataHistoryIzin adapterDataHistoryIzin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cuti_izin);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        diambilLoading = findViewById(R.id.sisa_loading);
        sisaLoading = findViewById(R.id.diambil_loading);
        periodeLoading = findViewById(R.id.periode_loading);
        loadingDataCuti = findViewById(R.id.loading_data_cuti);
        loadingDataIzin = findViewById(R.id.loading_data_izin);
        loadingDataPartCuti = findViewById(R.id.loading_data_part_cuti);
        loadingDataPartIzin = findViewById(R.id.loading_data_part_izin);
        noDataPartCuti = findViewById(R.id.no_data_part_cuti);
        noDataPartIzin = findViewById(R.id.no_data_part_izin);
        periodeData = findViewById(R.id.periode_data);
        dataDiambilTV = findViewById(R.id.data_diambil_tv);
        dataSisaTV = findViewById(R.id.data_sisa_tv);
        dataDiambilPart = findViewById(R.id.data_diambil_part);
        dataSisaPart = findViewById(R.id.data_sisa_part);
        tahunCutiDiambilTV = findViewById(R.id.tahun_cuti_diambil_tv);
        totalDataCuti = findViewById(R.id.total_data_cuti);
        totalDataIzin = findViewById(R.id.total_data_izin);

        dataHistoryCutiRV = findViewById(R.id.data_cuti_rv);
        dataHistoryIzinRV = findViewById(R.id.data_izin_rv);

        dataHistoryCutiRV.setLayoutManager(new LinearLayoutManager(this));
        dataHistoryCutiRV.setHasFixedSize(true);
        dataHistoryCutiRV.setNestedScrollingEnabled(false);
        dataHistoryCutiRV.setItemAnimator(new DefaultItemAnimator());

        dataHistoryIzinRV.setLayoutManager(new LinearLayoutManager(this));
        dataHistoryIzinRV.setHasFixedSize(true);
        dataHistoryIzinRV.setNestedScrollingEnabled(false);
        dataHistoryIzinRV.setItemAnimator(new DefaultItemAnimator());

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
                .load(R.drawable.loading)
                .into(loadingDataIzin);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataCuti);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                periodeLoading.setVisibility(View.VISIBLE);
                periodeData.setVisibility(View.GONE);

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

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryCutiIzinActivity.this, MapsActivity.class);
                startActivity(intent);
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
                                String cuti_ambil = data.getString("cuti_ambil");
                                String sisa_cuti = data.getString("sisa_cuti");
                                String tahun = data.getString("tahun");
                                String jumlah_cuti = data.getString("jumlah_cuti");
                                String jumlah_izin = data.getString("jumlah_izin");

                                periodeData.setText(periode_mulai.substring(8,10)+"/"+periode_mulai.substring(5,7)+"/"+periode_mulai.substring(0,4)+"  s.d. "+periode_akhir.substring(8,10)+"/"+periode_akhir.substring(5,7)+"/"+periode_akhir.substring(0,4));
                                periodeLoading.setVisibility(View.GONE);
                                periodeData.setVisibility(View.VISIBLE);

                                dataDiambilTV.setText(cuti_ambil);
                                dataSisaTV.setText(sisa_cuti);
                                tahunCutiDiambilTV.setText("Di Tahun "+tahun);
                                diambilLoading.setVisibility(View.GONE);
                                dataDiambilPart.setVisibility(View.VISIBLE);
                                sisaLoading.setVisibility(View.GONE);
                                dataSisaPart.setVisibility(View.VISIBLE);

                                totalDataCuti.setText(jumlah_cuti);
                                totalDataIzin.setText(jumlah_izin);

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