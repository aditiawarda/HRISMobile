package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
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
import com.gelora.absensi.adapter.AdapterListKeluarga;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataKeluarga;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoKeluargaActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, noDataPart, loadingDataPart, addBTN;
    ImageView loadingData;
    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;

    KAlertDialog pDialog;
    private final int i = -1;

    private RecyclerView listKeluargaRV;
    private DataKeluarga[] dataKeluargas;
    private AdapterListKeluarga adapterListKeluarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_keluarga);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingData = findViewById(R.id.loading_data);
        noDataPart = findViewById(R.id.no_data_part);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);

        listKeluargaRV = findViewById(R.id.list_keluarga_rv);

        listKeluargaRV.setLayoutManager(new LinearLayoutManager(this));
        listKeluargaRV.setHasFixedSize(true);
        listKeluargaRV.setNestedScrollingEnabled(false);
        listKeluargaRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingData);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                listKeluargaRV.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataKeluarga();
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

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoKeluargaActivity.this, FormInfoKeluargaActivity.class);
                intent.putExtra("tipe","tambah");
                startActivity(intent);
            }
        });

        getDataKeluarga();

    }

    private void getDataKeluarga() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/list_keluarga";
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
                                String jumlah = data.getString("jumlah");
                                String action = data.getString("action");

                                if(action.equals("1")){
                                    addBTN.setVisibility(View.VISIBLE);
                                } else {
                                    addBTN.setVisibility(View.GONE);
                                }

                                if (jumlah.equals("0")){
                                    listKeluargaRV.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.VISIBLE);
                                    loadingDataPart.setVisibility(View.GONE);
                                } else {
                                    listKeluargaRV.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    String data_keluarga = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataKeluargas = gson.fromJson(data_keluarga, DataKeluarga[].class);
                                    adapterListKeluarga = new AdapterListKeluarga(dataKeluargas, InfoKeluargaActivity.this);
                                    listKeluargaRV.setAdapter(adapterListKeluarga);
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
        CookieBar.build(InfoKeluargaActivity.this)
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
        getDataKeluarga();
    }

}