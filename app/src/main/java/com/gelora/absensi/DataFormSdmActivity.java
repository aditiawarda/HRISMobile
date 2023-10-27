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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterListDataFormSDM;
import com.gelora.absensi.adapter.AdapterListDataPenilaianSDM;
import com.gelora.absensi.model.DataFormSDM;
import com.gelora.absensi.model.DataPenilaianSDM;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataFormSdmActivity extends AppCompatActivity {

    LinearLayout addBTN, backBTN, loadingDataPart, noDataPart, countWaitingBTN;
    ImageView loadingData;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView countWaitingTV;

    private RecyclerView dataFormSDMRV;
    private DataFormSDM[] dataFormSDMS;
    private AdapterListDataFormSDM adapterListDataFormSDM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        addBTN = findViewById(R.id.add_btn);

        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingData = findViewById(R.id.loading_data);
        noDataPart = findViewById(R.id.no_data_part);
        countWaitingBTN = findViewById(R.id.count_waiting_btn);
        countWaitingTV = findViewById(R.id.count_waiting_tv);
        dataFormSDMRV = findViewById(R.id.list_form_sdm_rv);

        dataFormSDMRV.setLayoutManager(new LinearLayoutManager(this));
        dataFormSDMRV.setHasFixedSize(true);
        dataFormSDMRV.setNestedScrollingEnabled(false);
        dataFormSDMRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingData);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataFormSDMRV.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
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
                Intent intent = new Intent(DataFormSdmActivity.this, FormSdmActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_data_form_sdm";
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
                                String waiting_kadep = data.getString("waiting_kadep");
                                String waiting_kabag = data.getString("waiting_kabag");

                                if(sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3")){
                                    if(Integer.parseInt(waiting_kadep)>0){
                                        countWaitingBTN.setVisibility(View.VISIBLE);
                                        countWaitingTV.setText(waiting_kadep);
                                    } else {
                                        countWaitingBTN.setVisibility(View.GONE);
                                        countWaitingTV.setText("");
                                    }
                                } else if(sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25")){
                                    if(Integer.parseInt(waiting_kabag)>0){
                                        countWaitingBTN.setVisibility(View.VISIBLE);
                                        countWaitingTV.setText(waiting_kabag);
                                    } else {
                                        countWaitingBTN.setVisibility(View.GONE);
                                        countWaitingTV.setText("");
                                    }
                                }

                                if(Integer.parseInt(jumlah)>0){
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataFormSDMRV.setVisibility(View.VISIBLE);
                                    String data_record = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataFormSDMS = gson.fromJson(data_record, DataFormSDM[].class);
                                    adapterListDataFormSDM = new AdapterListDataFormSDM(dataFormSDMS, DataFormSdmActivity.this);
                                    dataFormSDMRV.setAdapter(adapterListDataFormSDM);
                                } else {
                                    noDataPart.setVisibility(View.VISIBLE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataFormSDMRV.setVisibility(View.GONE);
                                }
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                dataFormSDMRV.setVisibility(View.GONE);
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
        CookieBar.build(DataFormSdmActivity.this)
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
        dataFormSDMRV.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                getData();
            }
        }, 500);
    }

}