package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataAbsensiMore;
import com.gelora.absensi.adapter.AdapterListSDM;
import com.gelora.absensi.model.DataRecordAbsensi;
import com.gelora.absensi.model.HumanResource;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HumanResourceActivity extends AppCompatActivity {

    LinearLayout backBTN, formSdmBTN, penilaianSdmBTN, loadingRecordPart, noDataPart;
    ImageView loadingDataRecord;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    private RecyclerView dataListKaryawanRV;
    private HumanResource[] humanResources;
    private AdapterListSDM adapterListSDM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_resource);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        formSdmBTN = findViewById(R.id.form_sdm_btn);
        penilaianSdmBTN = findViewById(R.id.penilaian_sdm_btn);
        loadingDataRecord = findViewById(R.id.loading_data);
        loadingRecordPart = findViewById(R.id.loading_data_part);
        noDataPart = findViewById(R.id.no_data_part);

        dataListKaryawanRV = findViewById(R.id.data_absensi_rv);

        dataListKaryawanRV.setLayoutManager(new LinearLayoutManager(this));
        dataListKaryawanRV.setHasFixedSize(true);
        dataListKaryawanRV.setNestedScrollingEnabled(false);
        dataListKaryawanRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataRecord);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingRecordPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                dataListKaryawanRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 1000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        formSdmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HumanResourceActivity.this, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        penilaianSdmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HumanResourceActivity.this, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/list_sdm";
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
                                String jumlah_data = data.getString("jumlah");
                                if(Integer.parseInt(jumlah_data)>0){
                                    loadingRecordPart.setVisibility(View.GONE);
                                    dataListKaryawanRV.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    String data_karyawan = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    humanResources = gson.fromJson(data_karyawan, HumanResource[].class);
                                    adapterListSDM = new AdapterListSDM(humanResources, HumanResourceActivity.this);
                                    dataListKaryawanRV.setAdapter(adapterListSDM);
                                } else {
                                    loadingRecordPart.setVisibility(View.GONE);
                                    dataListKaryawanRV.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.VISIBLE);
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(HumanResourceActivity.this)
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