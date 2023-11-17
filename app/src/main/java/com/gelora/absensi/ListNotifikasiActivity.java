package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
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
import com.gelora.absensi.adapter.AdapterPermohonanIzin;
import com.gelora.absensi.adapter.AdapterPermohonanSaya;
import com.gelora.absensi.model.ListPermohonanIzin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListNotifikasiActivity extends AppCompatActivity {

    private RecyclerView dataNotifikasiRV;
    private RecyclerView dataNotifikasi2RV;
    private ListPermohonanIzin[] listPermohonanIzins;
    private AdapterPermohonanIzin adapterPermohonanIzin;
    private AdapterPermohonanSaya adapterPermohonanSaya;
    SharedPrefManager sharedPrefManager;
    LinearLayout actionBar, mainPart, optionPart, countPartIn, countPartMe, permohonanMasukPart, permohonanSayaPart, notifyInBTN, notifySayaBTN, noDataPart, noDataPart2, loadingDataPart, loadingDataPart2, backBTN;
    ImageView loadingImage,loadingImage2;
    SwipeRefreshLayout refreshLayout;
    View rootview;
    TextView countNotifMasuk, countNotifSaya, labelPageIzin;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifikasi);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        dataNotifikasiRV = findViewById(R.id.data_notifikasi_rv);
        dataNotifikasi2RV = findViewById(R.id.data_notifikasi_2_rv);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        noDataPart = findViewById(R.id.no_data_part);
        noDataPart2 = findViewById(R.id.no_data_part_2);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingDataPart2 = findViewById(R.id.loading_data_part_2);
        loadingImage = findViewById(R.id.loading_data_img);
        loadingImage2 = findViewById(R.id.loading_data_img_2);
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
        labelPageIzin = findViewById(R.id.label_page);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingImage);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingImage2);

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 500);
            }
        });

        if(sharedPrefManager.getSpIdCor().equals("1")){
            labelPageIzin.setText("NOTIFIKASI IZIN/CUTI");
        } else if(sharedPrefManager.getSpIdCor().equals("3")){
            labelPageIzin.setText("NOTIFIKASI IZIN");
        }

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        notifySayaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifySayaBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiActivity.this, R.drawable.shape_notify_choice));
                notifyInBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiActivity.this, R.drawable.shape_notify));
                permohonanMasukPart.setVisibility(View.GONE);
                permohonanSayaPart.setVisibility(View.VISIBLE);

                loadingDataPart.setVisibility(View.GONE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataNotifikasiRV.setVisibility(View.GONE);
                dataNotifikasi2RV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
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
                notifyInBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiActivity.this, R.drawable.shape_notify_choice));
                notifySayaBTN.setBackground(ContextCompat.getDrawable(ListNotifikasiActivity.this, R.drawable.shape_notify));
                permohonanMasukPart.setVisibility(View.VISIBLE);
                permohonanSayaPart.setVisibility(View.GONE);

                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataNotifikasiRV.setVisibility(View.GONE);
                dataNotifikasi2RV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 300);

            }
        });

        if (sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || (sharedPrefManager.getSpIdJabatan().equals("4") && sharedPrefManager.getSpNik().equals("1309131210")) || sharedPrefManager.getSpIdJabatan().equals("33") || sharedPrefManager.getSpIdJabatan().equals("35")){
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
        } else if (sharedPrefManager.getSpIdJabatan().equals("8") || sharedPrefManager.getSpNik().equals("000112092023")){
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
                                        listPermohonanIzins = gson.fromJson(data_permohonan_masuk, ListPermohonanIzin[].class);
                                        adapterPermohonanIzin = new AdapterPermohonanIzin(listPermohonanIzins,ListNotifikasiActivity.this);
                                        dataNotifikasiRV.setAdapter(adapterPermohonanIzin);
                                    }
                                } else {
                                    countPartIn.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataNotifikasiRV.setVisibility(View.VISIBLE);
                                    String data_permohonan_masuk = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listPermohonanIzins = gson.fromJson(data_permohonan_masuk, ListPermohonanIzin[].class);
                                    adapterPermohonanIzin = new AdapterPermohonanIzin(listPermohonanIzins,ListNotifikasiActivity.this);
                                    dataNotifikasiRV.setAdapter(adapterPermohonanIzin);
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
                                        listPermohonanIzins = gson.fromJson(data_permohonan_saya, ListPermohonanIzin[].class);
                                        adapterPermohonanSaya = new AdapterPermohonanSaya(listPermohonanIzins,ListNotifikasiActivity.this);
                                        dataNotifikasi2RV.setAdapter(adapterPermohonanSaya);
                                    }
                                } else {
                                    countPartMe.setVisibility(View.VISIBLE);
                                    noDataPart2.setVisibility(View.GONE);
                                    loadingDataPart2.setVisibility(View.GONE);
                                    dataNotifikasi2RV.setVisibility(View.VISIBLE);
                                    String data_permohonan_saya = data.getString("data2");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listPermohonanIzins = gson.fromJson(data_permohonan_saya, ListPermohonanIzin[].class);
                                    adapterPermohonanSaya = new AdapterPermohonanSaya(listPermohonanIzins,ListNotifikasiActivity.this);
                                    dataNotifikasi2RV.setAdapter(adapterPermohonanSaya);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                getData();
            }
        }, 300);
    }

    private void connectionFailed(){
        CookieBar.build(ListNotifikasiActivity.this)
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