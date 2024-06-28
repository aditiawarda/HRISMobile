package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.gelora.absensi.adapter.AdapterPermohonanFinger;
import com.gelora.absensi.adapter.AdapterPermohonanFingerSaya;
import com.gelora.absensi.model.ListPermohonanFingerscan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListNotifikasiFingerscanActivity extends AppCompatActivity {

    private RecyclerView dataNotifikasiRV;
    private RecyclerView dataNotifikasi2RV;
    private ListPermohonanFingerscan[] listPermohonanFingerscans;
    private AdapterPermohonanFinger adapterPermohonanFinger;
    private AdapterPermohonanFingerSaya adapterPermohonanFingerSaya;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    LinearLayout backBTN, actionBar;
    LinearLayout mainPart, optionPart, countPartIn, countPartMe, permohonanMasukPart, permohonanSayaPart, notifyInBTN, notifySayaBTN, noDataPart, noDataPart2, loadingDataPart, loadingDataPart2;
    View rootview;
    TextView countNotifMasuk, countNotifSaya;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifikasi_fingerscan);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        dataNotifikasiRV = findViewById(R.id.data_notifikasi_rv);
        dataNotifikasi2RV = findViewById(R.id.data_notifikasi_2_rv);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        noDataPart = findViewById(R.id.no_data_part);
        noDataPart2 = findViewById(R.id.no_data_part_2);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingDataPart2 = findViewById(R.id.loading_data_part_2);
        notifyInBTN = findViewById(R.id.notify_in_btn);
        notifySayaBTN = findViewById(R.id.notify_out_btn);
        permohonanMasukPart = findViewById(R.id.permohonan_masuk);
        permohonanSayaPart = findViewById(R.id.permohonan_saya);
        countNotifMasuk = findViewById(R.id.count_notif_in_tv);
        countNotifSaya = findViewById(R.id.count_notif_me_tv);
        countPartIn = findViewById(R.id.count_notification_in);
        countPartMe = findViewById(R.id.count_notification_me);
        optionPart = findViewById(R.id.option_part);
        mainPart = findViewById(R.id.main_part);
        actionBar = findViewById(R.id.action_bar);

        dataNotifikasiRV.setLayoutManager(new LinearLayoutManager(this));
        dataNotifikasiRV.setHasFixedSize(true);
        dataNotifikasiRV.setNestedScrollingEnabled(false);
        dataNotifikasiRV.setItemAnimator(new DefaultItemAnimator());

        dataNotifikasi2RV.setLayoutManager(new LinearLayoutManager(this));
        dataNotifikasi2RV.setHasFixedSize(true);
        dataNotifikasi2RV.setNestedScrollingEnabled(false);
        dataNotifikasi2RV.setItemAnimator(new DefaultItemAnimator());

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataNotifikasiRV.setVisibility(View.GONE);
                dataNotifikasi2RV.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
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

        notifySayaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifySayaBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiFingerscanActivity.this, R.drawable.shape_notify_choice));
                notifyInBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiFingerscanActivity.this, R.drawable.shape_notify));
                permohonanMasukPart.setVisibility(View.GONE);
                permohonanSayaPart.setVisibility(View.VISIBLE);

                loadingDataPart.setVisibility(View.GONE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataNotifikasiRV.setVisibility(View.GONE);
                dataNotifikasi2RV.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 300);

            }
        });

        notifyInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyInBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiFingerscanActivity.this, R.drawable.shape_notify_choice));
                notifySayaBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiFingerscanActivity.this, R.drawable.shape_notify));
                permohonanMasukPart.setVisibility(View.VISIBLE);
                permohonanSayaPart.setVisibility(View.GONE);

                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataNotifikasiRV.setVisibility(View.GONE);
                dataNotifikasi2RV.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 300);

            }
        });

        if (sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || sharedPrefManager.getSpIdJabatan().equals("33") || sharedPrefManager.getSpIdJabatan().equals("35") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116"))){
            if(sharedPrefManager.getSpNik().equals("000112092023")){
                float scale = getResources().getDisplayMetrics().density;
                int side = (int) (17*scale + 0.5f);
                int top = (int) (85*scale + 0.5f);
                int bottom = (int) (20*scale + 0.5f);
                mainPart.setPadding(side,top,side,bottom);
                optionPart.setVisibility(View.GONE);
                permohonanMasukPart.setVisibility(View.VISIBLE);
                permohonanSayaPart.setVisibility(View.GONE);
            } else {
                optionPart.setVisibility(View.VISIBLE);
            }
        } else if (sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")) {
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            permohonanMasukPart.setVisibility(View.VISIBLE);
            permohonanSayaPart.setVisibility(View.GONE);
        } else {
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            permohonanMasukPart.setVisibility(View.GONE);
            permohonanSayaPart.setVisibility(View.VISIBLE);
        }

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_list_permohonan_finger";
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
                                String count2 = data.getString("count2");
                                String count_data = data.getString("count_data");
                                String count2_data = data.getString("count2_data");

                                countNotifMasuk.setText(count);
                                countNotifSaya.setText(count2);

                                if (count.equals("0")) {
                                    countPartIn.setVisibility(View.GONE);
                                    if(count_data.equals("0")){
                                        dataNotifikasiRV.setVisibility(View.GONE);
                                        noDataPart.setVisibility(View.VISIBLE);
                                        loadingDataPart.setVisibility(View.GONE);
                                    } else {
                                        noDataPart.setVisibility(View.GONE);
                                        loadingDataPart.setVisibility(View.GONE);
                                        dataNotifikasiRV.setVisibility(View.VISIBLE);
                                        String data_permohonan_masuk = data.getString("data");
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        listPermohonanFingerscans = gson.fromJson(data_permohonan_masuk, ListPermohonanFingerscan[].class);
                                        adapterPermohonanFinger = new AdapterPermohonanFinger(listPermohonanFingerscans,ListNotifikasiFingerscanActivity.this);
                                        dataNotifikasiRV.setAdapter(adapterPermohonanFinger);
                                    }
                                } else {
                                    countPartIn.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataNotifikasiRV.setVisibility(View.VISIBLE);
                                    String data_permohonan_masuk = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listPermohonanFingerscans = gson.fromJson(data_permohonan_masuk, ListPermohonanFingerscan[].class);
                                    adapterPermohonanFinger = new AdapterPermohonanFinger(listPermohonanFingerscans,ListNotifikasiFingerscanActivity.this);
                                    dataNotifikasiRV.setAdapter(adapterPermohonanFinger);
                                }

                                if (count2.equals("0")){
                                    countPartMe.setVisibility(View.GONE);
                                    if(count2_data.equals("0")){
                                        dataNotifikasi2RV.setVisibility(View.GONE);
                                        noDataPart2.setVisibility(View.VISIBLE);
                                        loadingDataPart2.setVisibility(View.GONE);
                                    } else {
                                        noDataPart2.setVisibility(View.GONE);
                                        loadingDataPart2.setVisibility(View.GONE);
                                        dataNotifikasi2RV.setVisibility(View.VISIBLE);
                                        String data_permohonan_saya = data.getString("data2");
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        listPermohonanFingerscans = gson.fromJson(data_permohonan_saya, ListPermohonanFingerscan[].class);
                                        adapterPermohonanFingerSaya = new AdapterPermohonanFingerSaya(listPermohonanFingerscans,ListNotifikasiFingerscanActivity.this);
                                        dataNotifikasi2RV.setAdapter(adapterPermohonanFingerSaya);
                                    }
                                } else {
                                    countPartMe.setVisibility(View.VISIBLE);
                                    noDataPart2.setVisibility(View.GONE);
                                    loadingDataPart2.setVisibility(View.GONE);
                                    dataNotifikasi2RV.setVisibility(View.VISIBLE);
                                    String data_permohonan_saya = data.getString("data2");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listPermohonanFingerscans = gson.fromJson(data_permohonan_saya, ListPermohonanFingerscan[].class);
                                    adapterPermohonanFingerSaya = new AdapterPermohonanFingerSaya(listPermohonanFingerscans,ListNotifikasiFingerscanActivity.this);
                                    dataNotifikasi2RV.setAdapter(adapterPermohonanFingerSaya);
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
                params.put("id_departemen", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDataPart.setVisibility(View.VISIBLE);
        loadingDataPart2.setVisibility(View.VISIBLE);
        noDataPart.setVisibility(View.GONE);
        noDataPart2.setVisibility(View.GONE);
        dataNotifikasiRV.setVisibility(View.GONE);
        dataNotifikasi2RV.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                getData();
            }
        }, 300);
    }

    private void connectionFailed(){
        CookieBar.build(ListNotifikasiFingerscanActivity.this)
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}