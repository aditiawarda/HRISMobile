package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
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
import com.gelora.absensi.adapter.AdapterDataHadir;
import com.gelora.absensi.adapter.AdapterListDataPenilaianSDM;
import com.gelora.absensi.model.DataHadir;
import com.gelora.absensi.model.DataPenilaianSDM;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataPenilaianSdmActivity extends AppCompatActivity {

    LinearLayout addBTN, backBTN, loadingDataPart, noDataPart;
    ImageView loadingData;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    private RecyclerView dataPenilaianSDMRV;
    private DataPenilaianSDM[] dataPenilaianSDMS;
    private AdapterListDataPenilaianSDM adapterListDataPenilaianSDM;

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
        loadingData = findViewById(R.id.loading_data);
        noDataPart = findViewById(R.id.no_data_part);

        dataPenilaianSDMRV.setLayoutManager(new LinearLayoutManager(this));
        dataPenilaianSDMRV.setHasFixedSize(true);
        dataPenilaianSDMRV.setNestedScrollingEnabled(false);
        dataPenilaianSDMRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingData);

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
                startActivity(intent);
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/list_penilaian_sdm";
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
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

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

}