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
import com.gelora.absensi.adapter.AdapterKelebihanJam;
import com.gelora.absensi.adapter.AdapterPulangCepat;
import com.gelora.absensi.model.DataKelebihanJam;
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

public class DetailKelebihanJamActivity extends AppCompatActivity {

    LinearLayout attantionPart, monthBTN, emptyDataKelebihanJam, loadingKelebihanJamPart, backBTN, homeBTN;
    ImageView bulanLoading, kelebihanJamLoading, loadingDataKelebihanJam;
    TextView messageKelebihanJam, dataBulan, dataTahun, dataKelebihanJam, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;

    private RecyclerView dataKelebihanJamRV;
    private DataKelebihanJam[] dataKelebihanJams;
    private AdapterKelebihanJam adapterKelebihanJam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kelebihan_jam);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv_detail_kelebihan_jam);
        bulanLoading = findViewById(R.id.bulan_loading);
        kelebihanJamLoading = findViewById(R.id.data_kelebihan_jam_loading);
        dataKelebihanJam = findViewById(R.id.data_kelebihan_jam_detail);
        loadingDataKelebihanJam = findViewById(R.id.loading_data_kelebihan_jam);
        loadingKelebihanJamPart = findViewById(R.id.loading_data_part_kelebihan_jam);
        emptyDataKelebihanJam = findViewById(R.id.no_data_part_kelebihan_jam);
        monthBTN = findViewById(R.id.month_btn);
        attantionPart = findViewById(R.id.attantion_part_kelebihan_jam);
        messageKelebihanJam = findViewById(R.id.message_kelebihan_jam);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataKelebihanJamRV = findViewById(R.id.data_kelebihan_jam_rv);

        dataKelebihanJamRV.setLayoutManager(new LinearLayoutManager(this));
        dataKelebihanJamRV.setHasFixedSize(true);
        dataKelebihanJamRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(kelebihanJamLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataKelebihanJam);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                kelebihanJamLoading.setVisibility(View.VISIBLE);
                dataKelebihanJam.setVisibility(View.GONE);

                dataKelebihanJamRV.setVisibility(View.GONE);
                loadingKelebihanJamPart.setVisibility(View.VISIBLE);
                emptyDataKelebihanJam.setVisibility(View.GONE);

                attantionPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailKelebihanJam();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RackMonthPicker(DetailKelebihanJamActivity.this)
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

                                kelebihanJamLoading.setVisibility(View.VISIBLE);
                                dataKelebihanJam.setVisibility(View.GONE);

                                dataKelebihanJamRV.setVisibility(View.GONE);
                                loadingKelebihanJamPart.setVisibility(View.VISIBLE);
                                emptyDataKelebihanJam.setVisibility(View.GONE);

                                attantionPart.setVisibility(View.GONE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDetailKelebihanJam();
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
                Intent intent = new Intent(DetailKelebihanJamActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDetailKelebihanJam();

    }

    private void getDetailKelebihanJam() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_kelebihan_jam";
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
                                String kelebihan_jam = data.getString("kelebihan_jam");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataKelebihanJam.setText(kelebihan_jam);

                                kelebihanJamLoading.setVisibility(View.GONE);
                                dataKelebihanJam.setVisibility(View.VISIBLE);

                                if (kelebihan_jam.equals("0")) {
                                    attantionPart.setVisibility(View.GONE);
                                    emptyDataKelebihanJam.setVisibility(View.VISIBLE);
                                    dataKelebihanJamRV.setVisibility(View.GONE);
                                    loadingKelebihanJamPart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    messageKelebihanJam.setText("Di bulan "+bulan+" "+tahun+" terdapat "+kelebihan_jam+" data kelebihan jam, jika terdapat kekeliruan data harap segera hubungi bagian HRD atau gunakan prosedur fingerscan.");
                                    dataKelebihanJamRV.setVisibility(View.VISIBLE);
                                    loadingKelebihanJamPart.setVisibility(View.GONE);
                                    String data_kelebihan_jam = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataKelebihanJams = gson.fromJson(data_kelebihan_jam, DataKelebihanJam[].class);
                                    adapterKelebihanJam = new AdapterKelebihanJam(dataKelebihanJams, DetailKelebihanJamActivity.this);
                                    dataKelebihanJamRV.setAdapter(adapterKelebihanJam);
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