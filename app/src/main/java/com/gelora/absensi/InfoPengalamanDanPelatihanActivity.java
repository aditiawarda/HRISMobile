package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.gelora.absensi.adapter.AdapterListPelatihan;
import com.gelora.absensi.adapter.AdapterListPengalaman;
import com.gelora.absensi.model.DataPelatihan;
import com.gelora.absensi.model.DataPengalaman;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoPengalamanDanPelatihanActivity extends AppCompatActivity {

    LinearLayout addBTN, noDataPengalamanPart, noDataPelatihanPart, loadingDataPelatihanPart, backBTN, pengalamanBTN, pelatihanBTN, infoPengalaman, infoPelatihan, loadingDataPengalamanPart;
    ImageView loadingDataPelatihanImg, loadingDataPengalamanImg;
    SwipeRefreshLayout refreshLayout;
    String posisi = "pengalaman";
    SharedPrefManager sharedPrefManager;

    RecyclerView dataPelatihanRV, dataPengalamanRV;
    private DataPengalaman[] dataPengalamans;
    private AdapterListPengalaman adapterListPengalaman;
    private DataPelatihan[] dataPelatihans;
    private AdapterListPelatihan adapterListPelatihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pengalaman_dan_pelatihan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        pengalamanBTN = findViewById(R.id.pengalaman_btn);
        pelatihanBTN = findViewById(R.id.pelatihan_btn);
        infoPengalaman = findViewById(R.id.info_pengalaman);
        infoPelatihan = findViewById(R.id.info_pelatihan);
        loadingDataPengalamanPart = findViewById(R.id.loading_data_pengalaman_part);
        loadingDataPelatihanPart = findViewById(R.id.loading_data_pelatihan_part);
        noDataPengalamanPart = findViewById(R.id.no_data_pengalaman_part);
        noDataPelatihanPart = findViewById(R.id.no_data_pelatihan_part);
        loadingDataPengalamanImg = findViewById(R.id.loading_data_pengalaman_img);
        loadingDataPelatihanImg = findViewById(R.id.loading_data_pelatihan_img);
        addBTN = findViewById(R.id.add_btn);
        dataPengalamanRV = findViewById(R.id.data_pengalaman_rv);
        dataPelatihanRV = findViewById(R.id.data_pelatihan_rv);

        dataPengalamanRV.setLayoutManager(new LinearLayoutManager(this));
        dataPengalamanRV.setHasFixedSize(true);
        dataPengalamanRV.setNestedScrollingEnabled(false);
        dataPengalamanRV.setItemAnimator(new DefaultItemAnimator());

        dataPelatihanRV.setLayoutManager(new LinearLayoutManager(this));
        dataPelatihanRV.setHasFixedSize(true);
        dataPelatihanRV.setNestedScrollingEnabled(false);
        dataPelatihanRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataPengalamanImg);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataPelatihanImg);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDataPelatihanPart.setVisibility(View.VISIBLE);
                loadingDataPengalamanPart.setVisibility(View.VISIBLE);
                noDataPelatihanPart.setVisibility(View.GONE);
                noDataPengalamanPart.setVisibility(View.GONE);
                dataPengalamanRV.setVisibility(View.GONE);
                dataPelatihanRV.setVisibility(View.GONE);
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

        pengalamanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posisi = "pengalaman";
                pengalamanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify_choice));
                pelatihanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify));
                infoPelatihan.setVisibility(View.GONE);
                infoPengalaman.setVisibility(View.VISIBLE);

                loadingDataPelatihanPart.setVisibility(View.GONE);
                loadingDataPengalamanPart.setVisibility(View.VISIBLE);
                noDataPelatihanPart.setVisibility(View.GONE);
                noDataPengalamanPart.setVisibility(View.GONE);
                dataPengalamanRV.setVisibility(View.GONE);
                dataPelatihanRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 300);

            }
        });

        pelatihanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posisi = "pelatihan";
                pelatihanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify_choice));
                pengalamanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify));
                infoPengalaman.setVisibility(View.GONE);
                infoPelatihan.setVisibility(View.VISIBLE);

                loadingDataPelatihanPart.setVisibility(View.VISIBLE);
                loadingDataPengalamanPart.setVisibility(View.GONE);
                noDataPelatihanPart.setVisibility(View.GONE);
                noDataPengalamanPart.setVisibility(View.GONE);
                dataPengalamanRV.setVisibility(View.GONE);
                dataPelatihanRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 300);

            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posisi.equals("pengalaman")){
                    Intent intent = new Intent(InfoPengalamanDanPelatihanActivity.this, FormInfoPengalamanActivity.class);
                    intent.putExtra("tipe","tambah");
                    startActivity(intent);
                } else if(posisi.equals("pelatihan")){
                    Intent intent = new Intent(InfoPengalamanDanPelatihanActivity.this, FormInfoPelatihanActivity.class);
                    intent.putExtra("tipe","tambah");
                    startActivity(intent);
                }
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/list_pengalaman_dan_pelatihan";
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
                            if (status.equals("Success")){
                                String jumlah_pengalaman = data.getString("jumlah_pengalaman");
                                String jumlah_pelatihan = data.getString("jumlah_pelatihan");
                                String action = data.getString("action");

                                if (jumlah_pengalaman.equals("0")){
                                    dataPengalamanRV.setVisibility(View.GONE);
                                    noDataPengalamanPart.setVisibility(View.VISIBLE);
                                    loadingDataPengalamanPart.setVisibility(View.GONE);
                                } else {
                                    dataPengalamanRV.setVisibility(View.VISIBLE);
                                    noDataPengalamanPart.setVisibility(View.GONE);
                                    loadingDataPengalamanPart.setVisibility(View.GONE);
                                    String data_pengalaman = data.getString("data_pengalaman");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataPengalamans = gson.fromJson(data_pengalaman, DataPengalaman[].class);
                                    adapterListPengalaman = new AdapterListPengalaman(dataPengalamans, InfoPengalamanDanPelatihanActivity.this);
                                    dataPengalamanRV.setAdapter(adapterListPengalaman);
                                }

                                if (jumlah_pelatihan.equals("0")){
                                    dataPelatihanRV.setVisibility(View.GONE);
                                    noDataPelatihanPart.setVisibility(View.VISIBLE);
                                    loadingDataPelatihanPart.setVisibility(View.GONE);
                                } else {
                                    dataPelatihanRV.setVisibility(View.VISIBLE);
                                    noDataPelatihanPart.setVisibility(View.GONE);
                                    loadingDataPelatihanPart.setVisibility(View.GONE);
                                    String data_pelatihan = data.getString("data_pelatihan");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataPelatihans = gson.fromJson(data_pelatihan, DataPelatihan[].class);
                                    adapterListPelatihan = new AdapterListPelatihan(dataPelatihans, InfoPengalamanDanPelatihanActivity.this);
                                    dataPelatihanRV.setAdapter(adapterListPelatihan);
                                }

                                if(action.equals("1")){
                                    addBTN.setVisibility(View.VISIBLE);
                                } else {
                                    addBTN.setVisibility(View.GONE);
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
        CookieBar.build(InfoPengalamanDanPelatihanActivity.this)
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
        getData();
    }

}