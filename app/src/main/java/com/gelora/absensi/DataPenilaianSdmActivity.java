package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterListDataPenilaianSDM;
import com.gelora.absensi.model.DataPenilaianSDM;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataPenilaianSdmActivity extends AppCompatActivity {

    LinearLayout areaPart, addBTN, backBTN, loadingDataPart, noDataPart, countWaitingBTN, addBtnPart;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView countWaitingTV;

    private RecyclerView dataPenilaianSDMRV;
    private DataPenilaianSDM[] dataPenilaianSDMS;
    private AdapterListDataPenilaianSDM adapterListDataPenilaianSDM;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penilaian_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        addBTN = findViewById(R.id.add_btn);
        dataPenilaianSDMRV = findViewById(R.id.list_penilaian_sdm_rv);
        loadingDataPart = findViewById(R.id.loading_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        countWaitingBTN = findViewById(R.id.count_waiting_btn);
        countWaitingTV = findViewById(R.id.count_waiting_tv);
        addBtnPart = findViewById(R.id.add_btn_part);
        areaPart = findViewById(R.id.area_part);

        dataPenilaianSDMRV.setLayoutManager(new LinearLayoutManager(this));
        dataPenilaianSDMRV.setHasFixedSize(true);
        dataPenilaianSDMRV.setNestedScrollingEnabled(false);
        dataPenilaianSDMRV.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataPenilaianSDMRV.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
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

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataPenilaianSdmActivity.this, FormPenilaianKaryawanActivity.class);
                intent.putExtra("nik_karyawan", "");
                intent.putExtra("nama_karyawan", "");
                startActivity(intent);
            }
        });

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/list_penilaian_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {
                                String jumlah = data.getString("jumlah");
                                String add_penilaian = data.getString("add_penilaian");

                                if(add_penilaian.equals("1")){
                                    addBtnPart.setVisibility(View.VISIBLE);
                                    int top = 65;
                                    int bottom = 100;
                                    int paddingTop = dpToPixels(top, getBaseContext());
                                    int paddingBottom = dpToPixels(bottom, getBaseContext());
                                    areaPart.setPadding(0,paddingTop,0,paddingBottom);
                                } else {
                                    int top = 65;
                                    int bottom = 10;
                                    int paddingTop = dpToPixels(top, getBaseContext());
                                    int paddingBottom = dpToPixels(bottom, getBaseContext());
                                    addBtnPart.setVisibility(View.GONE);
                                    areaPart.setPadding(0,paddingTop,0,paddingBottom);
                                }

                                if(Integer.parseInt(jumlah)>0){
                                    countWaitingBTN.setVisibility(View.VISIBLE);
                                    countWaitingTV.setText(jumlah);
                                } else {
                                    countWaitingBTN.setVisibility(View.GONE);
                                    countWaitingTV.setText("");
                                }
                                noDataPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                dataPenilaianSDMRV.setVisibility(View.VISIBLE);
                                String data_penilaian = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                dataPenilaianSDMS = gson.fromJson(data_penilaian, DataPenilaianSDM[].class);
                                adapterListDataPenilaianSDM = new AdapterListDataPenilaianSDM(dataPenilaianSDMS, DataPenilaianSdmActivity.this);
                                dataPenilaianSDMRV.setAdapter(adapterListDataPenilaianSDM);
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                dataPenilaianSDMRV.setVisibility(View.GONE);
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
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("id_departemen", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public int dpToPixels(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private void connectionFailed(){
        CookieBar.build(DataPenilaianSdmActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDataPart.setVisibility(View.VISIBLE);
        noDataPart.setVisibility(View.GONE);
        dataPenilaianSDMRV.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                getData();
            }
        }, 800);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}