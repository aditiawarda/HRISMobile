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
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterDataTerlambat;
import com.gelora.absensi.adapter.AdapterPermohonanIzin;
import com.gelora.absensi.model.DataTerlambat;
import com.gelora.absensi.model.ListPermohonanIzin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListNotifikasiActivity extends AppCompatActivity {

    private RecyclerView dataNotifikasiRV;
    private ListPermohonanIzin[] listPermohonanIzins;
    private AdapterPermohonanIzin adapterPermohonanIzin;
    SharedPrefManager sharedPrefManager;
    LinearLayout backBTN, homeBTN;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifikasi);

        sharedPrefManager = new SharedPrefManager(this);
        dataNotifikasiRV = findViewById(R.id.data_notifikasi_rv);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);

        dataNotifikasiRV.setLayoutManager(new LinearLayoutManager(this));
        dataNotifikasiRV.setHasFixedSize(true);
        dataNotifikasiRV.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 500);
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
                Intent intent = new Intent(ListNotifikasiActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_list_permohonan_izin";
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
                                String count = data.getString("count");

                                if (count.equals("0")){
                                    dataNotifikasiRV.setVisibility(View.GONE);
                                } else {
                                    dataNotifikasiRV.setVisibility(View.VISIBLE);
                                    String data_permohonan_izin = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listPermohonanIzins = gson.fromJson(data_permohonan_izin, ListPermohonanIzin[].class);
                                    adapterPermohonanIzin = new AdapterPermohonanIzin(listPermohonanIzins,ListNotifikasiActivity.this);
                                    dataNotifikasiRV.setAdapter(adapterPermohonanIzin);
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
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

}