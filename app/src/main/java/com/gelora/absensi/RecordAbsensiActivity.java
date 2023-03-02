package com.gelora.absensi;

import androidx.appcompat.app.AlertDialog;
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
import com.gelora.absensi.adapter.AdapterDataAbsensi;
import com.gelora.absensi.adapter.AdapterDataAbsensiMore;
import com.gelora.absensi.model.DataRecordAbsensi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecordAbsensiActivity extends AppCompatActivity {

    LinearLayout backBTN, loadingRecordPart, noDataPart, bulanBTN;
    ImageView loadingDataRecord;
    TextView bulanPilihTV;
    String selectMonth = "";
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    private RecyclerView dataAbsensiRV;
    private DataRecordAbsensi[] dataAbsensis;
    private AdapterDataAbsensiMore adapterDataAbsensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_absensi);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        loadingDataRecord = findViewById(R.id.loading_data);
        loadingRecordPart = findViewById(R.id.loading_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        bulanBTN = findViewById(R.id.bulan_btn);
        bulanPilihTV = findViewById(R.id.bulan_pilih);

        dataAbsensiRV = findViewById(R.id.data_absensi_rv);

        dataAbsensiRV.setLayoutManager(new LinearLayoutManager(this));
        dataAbsensiRV.setHasFixedSize(true);
        dataAbsensiRV.setNestedScrollingEnabled(false);
        dataAbsensiRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataRecord);

        selectMonth = getMonth();

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                selectMonth = getMonth();

                loadingRecordPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                dataAbsensiRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                        currentMonth();
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

        bulanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RackMonthPicker(RecordAbsensiActivity.this)
                        .setLocale(Locale.ENGLISH)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                String bulan = "", bulanName = "";
                                if(month==1){
                                    bulan = "01";
                                    bulanName = "Jan";
                                } else if (month==2){
                                    bulan = "02";
                                    bulanName = "Feb";
                                } else if (month==3){
                                    bulan = "03";
                                    bulanName = "Mar";
                                } else if (month==4){
                                    bulan = "04";
                                    bulanName = "Apr";
                                } else if (month==5){
                                    bulan = "05";
                                    bulanName = "01";
                                } else if (month==6){
                                    bulan = "06";
                                    bulanName = "Jun";
                                } else if (month==7){
                                    bulan = "07";
                                    bulanName = "Jul";
                                } else if (month==8){
                                    bulan = "08";
                                    bulanName = "Agu";
                                } else if (month==9){
                                    bulan = "09";
                                    bulanName = "Sep";
                                } else {
                                    bulan = String.valueOf(month);
                                    if (month==10){
                                        bulanName = "Okt";
                                    } else if(month==11){
                                        bulanName = "Nov";
                                    } else if(month==12){
                                        bulanName = "Des";
                                    }
                                }
                                selectMonth = String.valueOf(year)+"-"+bulan;

                                loadingRecordPart.setVisibility(View.VISIBLE);
                                noDataPart.setVisibility(View.GONE);
                                dataAbsensiRV.setVisibility(View.GONE);

                                bulanPilihTV.setText(bulanName+" "+String.valueOf(year));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData();
                                    }
                                }, 100);

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

        currentMonth();
        getData();

    }

    @SuppressLint("SetTextI18n")
    private void currentMonth(){
        String yearDate = getMonth().substring(0,4);;
        String bulanValue = getMonth().substring(5,7);
        String bulanName;

        switch (bulanValue) {
            case "01":
                bulanName = "Jan";
                break;
            case "02":
                bulanName = "Feb";
                break;
            case "03":
                bulanName = "Mar";
                break;
            case "04":
                bulanName = "Apr";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Jun";
                break;
            case "07":
                bulanName = "Jul";
                break;
            case "08":
                bulanName = "Agu";
                break;
            case "09":
                bulanName = "Sep";
                break;
            case "10":
                bulanName = "Okt";
                break;
            case "11":
                bulanName = "Nov";
                break;
            case "12":
                bulanName = "Des";
                break;
            default:
                bulanName = "Not found!";
                break;
        }

        bulanPilihTV.setText(bulanName+" "+yearDate);

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_record_absensi_more";
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
                                String jumlah_data = data.getString("jumlah_data");
                                if(Integer.parseInt(jumlah_data)>0){
                                    loadingRecordPart.setVisibility(View.GONE);
                                    dataAbsensiRV.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    String data_hadir = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataAbsensis = gson.fromJson(data_hadir, DataRecordAbsensi[].class);
                                    adapterDataAbsensi = new AdapterDataAbsensiMore(dataAbsensis, RecordAbsensiActivity.this);
                                    dataAbsensiRV.setAdapter(adapterDataAbsensi);
                                } else {
                                    loadingRecordPart.setVisibility(View.GONE);
                                    dataAbsensiRV.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.VISIBLE);
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
                params.put("bulan", selectMonth);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(RecordAbsensiActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getMonth() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

}