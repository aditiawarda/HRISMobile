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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterExitClearanceIn;
import com.gelora.absensi.adapter.AdapterExitClearanceOut;
import com.gelora.absensi.adapter.AdapterPermohonanIzin;
import com.gelora.absensi.adapter.AdapterPermohonanSaya;
import com.gelora.absensi.model.ListDataExitClearanceIn;
import com.gelora.absensi.model.ListDataExitClearanceOut;
import com.gelora.absensi.model.ListPermohonanIzin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExitClearanceActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    ImageView loadingDataImg, loadingDataImg2;
    LinearLayout countNotificationInPart, backBTN, mainPart, actionBar, addBTN, dataInBTN, dataOutBTN, optionPart, dataMasukPart, dataKeluarPart, noDataPart, noDataPart2, loadingDataPart, loadingDataPart2;
    SwipeRefreshLayout refreshLayout;
    TextView countNotificationInTV;
    String otoritorEC;

    private RecyclerView dataOutRV, dataInRV;
    private ListDataExitClearanceOut[] listDataExitClearanceOuts;
    private AdapterExitClearanceOut adapterExitClearanceOut;
    private ListDataExitClearanceIn[] listDataExitClearanceIns;
    private AdapterExitClearanceIn adapterExitClearanceIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        countNotificationInPart = findViewById(R.id.count_notification_in);
        countNotificationInTV = findViewById(R.id.count_notif_in_tv);
        noDataPart = findViewById(R.id.no_data_part);
        noDataPart2 = findViewById(R.id.no_data_part_2);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingDataPart2 = findViewById(R.id.loading_data_part_2);
        loadingDataImg = findViewById(R.id.loading_data_img);
        loadingDataImg2 = findViewById(R.id.loading_data_img_2);
        optionPart = findViewById(R.id.option_part);
        dataInBTN = findViewById(R.id.data_in_btn);
        dataOutBTN = findViewById(R.id.data_out_btn);
        dataMasukPart = findViewById(R.id.data_masuk);
        dataKeluarPart = findViewById(R.id.data_saya);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);
        mainPart = findViewById(R.id.main_part);

        dataOutRV = findViewById(R.id.data_out_rv);
        dataInRV = findViewById(R.id.data_in_rv);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataImg);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataImg2);

        dataOutRV.setLayoutManager(new LinearLayoutManager(this));
        dataOutRV.setHasFixedSize(true);
        dataOutRV.setNestedScrollingEnabled(false);
        dataOutRV.setItemAnimator(new DefaultItemAnimator());

        dataInRV.setLayoutManager(new LinearLayoutManager(this));
        dataInRV.setHasFixedSize(true);
        dataInRV.setNestedScrollingEnabled(false);
        dataInRV.setItemAnimator(new DefaultItemAnimator());

        otoritorEC = getIntent().getExtras().getString("otoritor");

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
                dataInRV.setVisibility(View.GONE);
                dataOutRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataIn(otoritorEC);
                        getDataOut();
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

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExitClearanceActivity.this, FormExitClearanceActivity.class);
                startActivity(intent);
            }
        });

        dataOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataOutBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify_choice));
                dataInBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify));
                dataMasukPart.setVisibility(View.GONE);
                dataKeluarPart.setVisibility(View.VISIBLE);

                loadingDataPart.setVisibility(View.GONE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataInRV.setVisibility(View.GONE);
                dataOutRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataOut();
                    }
                }, 300);

            }
        });

        dataInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBTN.setVisibility(View.GONE);
                dataInBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify_choice));
                dataOutBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify));
                dataMasukPart.setVisibility(View.VISIBLE);
                dataKeluarPart.setVisibility(View.GONE);

                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataInRV.setVisibility(View.GONE);
                dataOutRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataIn(otoritorEC);
                    }
                }, 300);

            }
        });

        if (sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || !otoritorEC.equals("0")){
            optionPart.setVisibility(View.VISIBLE);
        } else if (sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")){
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            dataMasukPart.setVisibility(View.VISIBLE);
            dataKeluarPart.setVisibility(View.GONE);
        } else {
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            dataMasukPart.setVisibility(View.GONE);
            dataKeluarPart.setVisibility(View.VISIBLE);
        }

        getDataIn(otoritorEC);
        getDataOut();

    }

    private void getDataOut() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_data_keluar_exit_clearance";
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
                                addBTN.setVisibility(View.GONE);
                                noDataPart2.setVisibility(View.GONE);
                                loadingDataPart2.setVisibility(View.GONE);
                                dataOutRV.setVisibility(View.VISIBLE);
                                String data_keluar = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                listDataExitClearanceOuts = gson.fromJson(data_keluar, ListDataExitClearanceOut[].class);
                                adapterExitClearanceOut = new AdapterExitClearanceOut(listDataExitClearanceOuts,ExitClearanceActivity.this);
                                dataOutRV.setAdapter(adapterExitClearanceOut);
                            } else {
                                addBTN.setVisibility(View.VISIBLE);
                                noDataPart2.setVisibility(View.VISIBLE);
                                loadingDataPart2.setVisibility(View.GONE);
                                dataOutRV.setVisibility(View.GONE);
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
                params.put("nik", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getDataIn(String nomor_st) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_data_masuk_exit_clearance";
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
                                String all_count = data.getString("waiting_count");
                                String waiting_count = data.getString("waiting_count");

                                if(Integer.parseInt(waiting_count)>0){
                                    countNotificationInPart.setVisibility(View.VISIBLE);
                                    countNotificationInTV.setText(waiting_count);
                                } else {
                                    countNotificationInPart.setVisibility(View.GONE);
                                }

                                if(Integer.parseInt(all_count)>0){
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataInRV.setVisibility(View.VISIBLE);
                                    String data_masuk = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listDataExitClearanceIns = gson.fromJson(data_masuk, ListDataExitClearanceIn[].class);
                                    adapterExitClearanceIn = new AdapterExitClearanceIn(listDataExitClearanceIns,ExitClearanceActivity.this);
                                    dataInRV.setAdapter(adapterExitClearanceIn);
                                } else {
                                    noDataPart.setVisibility(View.VISIBLE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataInRV.setVisibility(View.GONE);
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
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("nomor_st", nomor_st);
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
        dataInRV.setVisibility(View.GONE);
        dataOutRV.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                getDataIn(otoritorEC);
                getDataOut();
            }
        }, 300);
    }

    private void connectionFailed(){
        CookieBar.build(ExitClearanceActivity.this)
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