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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataLayoff;
import com.gelora.absensi.adapter.AdapterPulangCepat;
import com.gelora.absensi.model.DataLayoff;
import com.gelora.absensi.model.DataPulangCepat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;
import com.shasin.notificationbanner.Banner;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailLayoffActivity extends AppCompatActivity {

    LinearLayout actionBar, attantionPart, monthBTN, emptyDataLayoff, loadingLayoffPart, backBTN;
    ImageView bulanLoading, layoffLoading, loadingDataLayoff;
    TextView messageLayoff, dataBulan, dataTahun, dataLayoff, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;
    View rootview;

    private RecyclerView dataLayoffRV;
    private DataLayoff[] dataLayoffs;
    private AdapterDataLayoff adapterDataLayoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layoff);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        bulanLoading = findViewById(R.id.bulan_loading);
        layoffLoading = findViewById(R.id.layoff_detail_loading);
        dataLayoff = findViewById(R.id.data_layoff_detail);
        loadingDataLayoff = findViewById(R.id.loading_data_layoff_detail);
        loadingLayoffPart = findViewById(R.id.loading_data_part_layoff_detail);
        emptyDataLayoff = findViewById(R.id.no_data_part_layoff_detail);
        monthBTN = findViewById(R.id.month_btn);
        attantionPart = findViewById(R.id.attantion_part_layoff_detail);
        messageLayoff = findViewById(R.id.message_layoff_detail);
        actionBar = findViewById(R.id.action_bar);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataLayoffRV = findViewById(R.id.data_layoff_rv);

        dataLayoffRV.setLayoutManager(new LinearLayoutManager(this));
        dataLayoffRV.setHasFixedSize(true);
        dataLayoffRV.setNestedScrollingEnabled(false);
        dataLayoffRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(layoffLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataLayoff);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                layoffLoading.setVisibility(View.VISIBLE);
                dataLayoff.setVisibility(View.GONE);

                dataLayoffRV.setVisibility(View.GONE);
                loadingLayoffPart.setVisibility(View.VISIBLE);
                emptyDataLayoff.setVisibility(View.GONE);

                attantionPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailLayoff();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(DetailLayoffActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(int month, int year) { // on date set
                                String bulan = "", bulanName = "";
                                if(month==0){
                                    bulan = "01";
                                } else if (month==1){
                                    bulan = "02";
                                } else if (month==2){
                                    bulan = "03";
                                } else if (month==3){
                                    bulan = "04";
                                } else if (month==4){
                                    bulan = "05";
                                } else if (month==5){
                                    bulan = "06";
                                } else if (month==6){
                                    bulan = "07";
                                } else if (month==7){
                                    bulan = "08";
                                } else if (month==8){
                                    bulan = "09";
                                } else if (month==9){
                                    bulan = "10";
                                } else if (month==10){
                                    bulan = "11";
                                } else if (month==11){
                                    bulan = "12";
                                }

                                bulanPilih = String.valueOf(year)+"-"+bulan;

                                bulanLoading.setVisibility(View.VISIBLE);
                                dataBulan.setVisibility(View.GONE);
                                dataTahun.setVisibility(View.GONE);

                                layoffLoading.setVisibility(View.VISIBLE);
                                dataLayoff.setVisibility(View.GONE);

                                dataLayoffRV.setVisibility(View.GONE);
                                loadingLayoffPart.setVisibility(View.VISIBLE);
                                emptyDataLayoff.setVisibility(View.GONE);

                                attantionPart.setVisibility(View.GONE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDetailLayoff();
                                    }
                                }, 500);

                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH));

                builder.setMinYear(1952)
                        .setActivatedYear(now.get(Calendar.YEAR))
                        .setMaxYear(now.get(Calendar.YEAR))
                        .setActivatedMonth(now.get(Calendar.MONTH))
                        .build()
                        .show();

            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDetailLayoff();

    }

    private void getDetailLayoff() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_layoff";
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
                                String total_layoff = data.getString("total_layoff");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataLayoff.setText(total_layoff);

                                layoffLoading.setVisibility(View.GONE);
                                dataLayoff.setVisibility(View.VISIBLE);

                                if (total_layoff.equals("0")) {
                                    attantionPart.setVisibility(View.GONE);
                                    emptyDataLayoff.setVisibility(View.VISIBLE);
                                    dataLayoffRV.setVisibility(View.GONE);
                                    loadingLayoffPart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    messageLayoff.setText("Di bulan "+bulan+" "+tahun+" terdapat "+total_layoff+" data diliburkan, jika terdapat kekeliruan data harap segera hubungi bagian HRD atau gunakan prosedur fingerscan/form keterangan tidak absen.");
                                    dataLayoffRV.setVisibility(View.VISIBLE);
                                    loadingLayoffPart.setVisibility(View.GONE);
                                    String layoff = data.getString("layoff");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataLayoffs = gson.fromJson(layoff, DataLayoff[].class);
                                    adapterDataLayoff = new AdapterDataLayoff(dataLayoffs, DetailLayoffActivity.this);
                                    dataLayoffRV.setAdapter(adapterDataLayoff);
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
                        connectionFailed();
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

    private void connectionFailed(){
        // Banner.make(rootview, DetailLayoffActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(DetailLayoffActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();

    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

}