package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
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
import com.gelora.absensi.adapter.AdapterDataTerlambat;
import com.gelora.absensi.model.DataTerlambat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailTerlambatActivity extends AppCompatActivity {

    LinearLayout actionBar, attantionPart, markerWarningLate, monthBTN, emptyDataLate, loadingLatePart, backBTN;
    ImageView notificationWarningLateDetail, bulanLoading, lateLoading;
    TextView messageLate, dataBulan, dataTahun, dataLate, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;
    View rootview;

    private RecyclerView dataLateRV;
    private DataTerlambat[] dataLates;
    private AdapterDataTerlambat adapterDataLate;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_terlambat);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        bulanLoading = findViewById(R.id.bulan_loading);
        lateLoading = findViewById(R.id.late_loading);
        dataLate = findViewById(R.id.data_late);
        loadingLatePart = findViewById(R.id.loading_data_part_telat);
        emptyDataLate = findViewById(R.id.no_data_part_late);
        monthBTN = findViewById(R.id.month_btn);
        markerWarningLate = findViewById(R.id.marker_warning_late_detail);
        attantionPart = findViewById(R.id.attantion_part_late);
        messageLate = findViewById(R.id.message_late);
        notificationWarningLateDetail = findViewById(R.id.warning_gif_absen_late_detail);
        actionBar = findViewById(R.id.action_bar);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataLateRV = findViewById(R.id.data_late_rv);

        dataLateRV.setLayoutManager(new LinearLayoutManager(this));
        dataLateRV.setHasFixedSize(true);
        dataLateRV.setNestedScrollingEnabled(false);
        dataLateRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(lateLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningLateDetail);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanPilih = getIntent().getExtras().getString("bulan");
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                lateLoading.setVisibility(View.VISIBLE);
                dataLate.setVisibility(View.GONE);

                dataLateRV.setVisibility(View.GONE);
                loadingLatePart.setVisibility(View.VISIBLE);
                emptyDataLate.setVisibility(View.GONE);

                markerWarningLate.setVisibility(View.GONE);
                attantionPart.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailTerlambat();
                    }
                }, 800);
            }
        });

        monthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date date = sdf.parse(bulanPilih);
                    now.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(DetailTerlambatActivity.this,
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

                                lateLoading.setVisibility(View.VISIBLE);
                                dataLate.setVisibility(View.GONE);

                                dataLateRV.setVisibility(View.GONE);
                                loadingLatePart.setVisibility(View.VISIBLE);
                                emptyDataLate.setVisibility(View.GONE);

                                markerWarningLate.setVisibility(View.GONE);
                                attantionPart.setVisibility(View.GONE);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDetailTerlambat();
                                    }
                                }, 500);

                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH));

                builder.setMinYear(1952)
                        .setActivatedYear(now.get(Calendar.YEAR))
                        .setMaxYear(Integer.parseInt(getYearOnly()))
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
        getDetailTerlambat();

    }

    private void getDetailTerlambat() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/total_terlambat";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String terlambat = data.getString("terlambat");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataLate.setText(terlambat);

                                lateLoading.setVisibility(View.GONE);
                                dataLate.setVisibility(View.VISIBLE);

                                if (terlambat.equals("0")){
                                    attantionPart.setVisibility(View.GONE);
                                    markerWarningLate.setVisibility(View.GONE);
                                    emptyDataLate.setVisibility(View.VISIBLE);
                                    dataLateRV.setVisibility(View.GONE);
                                    loadingLatePart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    messageLate.setText("Terdapat "+terlambat+" data keterlambatan, harap segera lakukan prosedur fingerscan/form keterangan tidak absen.");
                                    markerWarningLate.setVisibility(View.VISIBLE);
                                    dataLateRV.setVisibility(View.VISIBLE);
                                    loadingLatePart.setVisibility(View.GONE);
                                    String data_telat = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataLates = gson.fromJson(data_telat, DataTerlambat[].class);
                                    adapterDataLate = new AdapterDataTerlambat(dataLates,DetailTerlambatActivity.this);
                                    dataLateRV.setAdapter(adapterDataLate);
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
                params.put("bulan", bulanPilih);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailTerlambatActivity.this)
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

    private String getYearOnly() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}