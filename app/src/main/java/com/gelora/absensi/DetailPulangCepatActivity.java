package com.gelora.absensi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
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
import com.gelora.absensi.adapter.AdapterDataHadir;
import com.gelora.absensi.adapter.AdapterPulangCepat;
import com.gelora.absensi.model.DataHadir;
import com.gelora.absensi.model.DataPulangCepat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailPulangCepatActivity extends AppCompatActivity {

    LinearLayout attantionPart, monthBTN, emptyDataPulangCepat, loadingPulangCepatPart, backBTN, homeBTN;
    ImageView bulanLoading, pulangCepatLoading, loadingDataPulangCepat;
    TextView messagePulangCepat, dataBulan, dataTahun, dataPulangCepat, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;

    private RecyclerView dataPulangCepatRV;
    private DataPulangCepat[] dataPulangCepats;
    private AdapterPulangCepat adapterPulangCepat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pulang_cepat);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv_detail_pulang_cepat);
        bulanLoading = findViewById(R.id.bulan_loading);
        pulangCepatLoading = findViewById(R.id.data_pulang_cepat_loading);
        dataPulangCepat = findViewById(R.id.data_pulang_cepat_detail);
        loadingDataPulangCepat = findViewById(R.id.loading_data_pulang_cepat);
        loadingPulangCepatPart = findViewById(R.id.loading_data_part_pulang_cepat);
        emptyDataPulangCepat = findViewById(R.id.no_data_part_pulang_cepat);
        monthBTN = findViewById(R.id.month_btn);
        attantionPart = findViewById(R.id.attantion_part_pulang_cepat);
        messagePulangCepat = findViewById(R.id.message_pulang_cepat);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataPulangCepatRV = findViewById(R.id.data_pulang_cepat_rv);

        dataPulangCepatRV.setLayoutManager(new LinearLayoutManager(this));
        dataPulangCepatRV.setHasFixedSize(true);
        dataPulangCepatRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(pulangCepatLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataPulangCepat);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                pulangCepatLoading.setVisibility(View.VISIBLE);
                dataPulangCepat.setVisibility(View.GONE);

                dataPulangCepatRV.setVisibility(View.GONE);
                loadingPulangCepatPart.setVisibility(View.VISIBLE);
                emptyDataPulangCepat.setVisibility(View.GONE);

                attantionPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailPulangCepat();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RackMonthPicker(DetailPulangCepatActivity.this)
                        .setLocale(Locale.ENGLISH)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                String bulan = "";
                                if(month==1){
                                    bulan = "01";
                                } else if (month==2){
                                    bulan = "02";
                                } else if (month==3){
                                    bulan = "03";
                                } else if (month==4){
                                    bulan = "04";
                                } else if (month==5){
                                    bulan = "05";
                                } else if (month==6){
                                    bulan = "06";
                                } else if (month==7){
                                    bulan = "07";
                                } else if (month==8){
                                    bulan = "08";
                                } else if (month==9){
                                    bulan = "09";
                                } else{
                                    bulan = String.valueOf(month);
                                }
                                bulanPilih = String.valueOf(year)+"-"+bulan;

                                bulanLoading.setVisibility(View.VISIBLE);
                                dataBulan.setVisibility(View.GONE);
                                dataTahun.setVisibility(View.GONE);

                                pulangCepatLoading.setVisibility(View.VISIBLE);
                                dataPulangCepat.setVisibility(View.GONE);

                                dataPulangCepatRV.setVisibility(View.GONE);
                                loadingPulangCepatPart.setVisibility(View.VISIBLE);
                                emptyDataPulangCepat.setVisibility(View.GONE);

                                attantionPart.setVisibility(View.GONE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDetailPulangCepat();
                                    }
                                }, 500);

                            }
                        })
                        .setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
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
                Intent intent = new Intent(DetailPulangCepatActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDetailPulangCepat();

    }

    private void getDetailPulangCepat() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_pulang_cepat";
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
                                String pulang_cepat = data.getString("pulang_cepat");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataPulangCepat.setText(pulang_cepat);

                                pulangCepatLoading.setVisibility(View.GONE);
                                dataPulangCepat.setVisibility(View.VISIBLE);

                                if (pulang_cepat.equals("0")) {
                                    attantionPart.setVisibility(View.GONE);
                                    emptyDataPulangCepat.setVisibility(View.VISIBLE);
                                    dataPulangCepatRV.setVisibility(View.GONE);
                                    loadingPulangCepatPart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    messagePulangCepat.setText("Di bulan "+bulan+" "+tahun+" terdapat "+pulang_cepat+" data pulang lebih cepat, jika terdapat kekeliruan data harap segera hubungi bagian HRD atau gunakan prosedur fingerscan.");
                                    dataPulangCepatRV.setVisibility(View.VISIBLE);
                                    loadingPulangCepatPart.setVisibility(View.GONE);
                                    String data_pulang_cepat = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataPulangCepats = gson.fromJson(data_pulang_cepat, DataPulangCepat[].class);
                                    adapterPulangCepat = new AdapterPulangCepat(dataPulangCepats, DetailPulangCepatActivity.this);
                                    dataPulangCepatRV.setAdapter(adapterPulangCepat);
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
                        //connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", bulanPilih);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

}