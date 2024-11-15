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
import com.gelora.absensi.adapter.AdapterDataNoCheckout;
import com.gelora.absensi.model.DataNoCheckout;
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

public class DetailTidakCheckoutActivity extends AppCompatActivity {

    LinearLayout actionBar, attantionPart, markerWarningNocheckout, monthBTN, emptyDataNoCheckout, loadingNoCheckoutPart, backBTN;
    ImageView notificationWarningNoCheckoutDetail, bulanLoading, noCheckoutLoading;
    TextView messageNoCheckout, dataBulan, dataTahun, dataNoCheckout, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String bulanPilih;
    View rootview;

    private RecyclerView dataNoCheckoutRV;
    private DataNoCheckout[] dataNoCheckouts;
    private AdapterDataNoCheckout adapterDataNoCheckout;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tidak_checkout);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        bulanLoading = findViewById(R.id.bulan_loading);
        noCheckoutLoading = findViewById(R.id.nocheckout_loading);
        dataNoCheckout = findViewById(R.id.data_nocheckout);
        loadingNoCheckoutPart = findViewById(R.id.loading_data_part_nocheckout);
        emptyDataNoCheckout = findViewById(R.id.no_data_part_nocheckout);
        monthBTN = findViewById(R.id.month_btn);
        markerWarningNocheckout = findViewById(R.id.marker_warning_nocheckout_detail);
        attantionPart = findViewById(R.id.attantion_part_nocheckout);
        messageNoCheckout = findViewById(R.id.message_nocheckout);
        notificationWarningNoCheckoutDetail = findViewById(R.id.warning_gif_absen_nocheckout_detail);
        actionBar = findViewById(R.id.action_bar);

        bulanPilih = getIntent().getExtras().getString("bulan");

        dataNoCheckoutRV = findViewById(R.id.data_nocheckout_rv);

        dataNoCheckoutRV.setLayoutManager(new LinearLayoutManager(this));
        dataNoCheckoutRV.setHasFixedSize(true);
        dataNoCheckoutRV.setNestedScrollingEnabled(false);
        dataNoCheckoutRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(noCheckoutLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningNoCheckoutDetail);

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

                noCheckoutLoading.setVisibility(View.VISIBLE);
                dataNoCheckout.setVisibility(View.GONE);

                dataNoCheckoutRV.setVisibility(View.GONE);
                loadingNoCheckoutPart.setVisibility(View.VISIBLE);
                emptyDataNoCheckout.setVisibility(View.GONE);

                markerWarningNocheckout.setVisibility(View.GONE);
                attantionPart.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailNoCheckout();
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
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(DetailTidakCheckoutActivity.this,
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

                                bulanLoading.setVisibility(View.VISIBLE);
                                dataBulan.setVisibility(View.GONE);
                                dataTahun.setVisibility(View.GONE);

                                noCheckoutLoading.setVisibility(View.VISIBLE);
                                dataNoCheckout.setVisibility(View.GONE);

                                dataNoCheckoutRV.setVisibility(View.GONE);
                                loadingNoCheckoutPart.setVisibility(View.VISIBLE);
                                emptyDataNoCheckout.setVisibility(View.GONE);

                                markerWarningNocheckout.setVisibility(View.GONE);
                                attantionPart.setVisibility(View.GONE);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDetailNoCheckout();
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
        getDetailNoCheckout();

    }

    private void getDetailNoCheckout() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/total_tidak_checkout";
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
                                String tidak_checkout = data.getString("tidak_checkout");

                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");

                                dataBulan.setText(bulan.toUpperCase());
                                dataTahun.setText(tahun);

                                dataBulan.setVisibility(View.VISIBLE);
                                dataTahun.setVisibility(View.VISIBLE);
                                bulanLoading.setVisibility(View.GONE);

                                dataNoCheckout.setText(tidak_checkout);

                                noCheckoutLoading.setVisibility(View.GONE);
                                dataNoCheckout.setVisibility(View.VISIBLE);

                                if (tidak_checkout.equals("0")){
                                    attantionPart.setVisibility(View.GONE);
                                    markerWarningNocheckout.setVisibility(View.GONE);
                                    emptyDataNoCheckout.setVisibility(View.VISIBLE);
                                    dataNoCheckoutRV.setVisibility(View.GONE);
                                    loadingNoCheckoutPart.setVisibility(View.GONE);
                                } else {
                                    attantionPart.setVisibility(View.VISIBLE);
                                    messageNoCheckout.setText("Terdapat "+tidak_checkout+" data tidak checkout, harap segera lakukan prosedur fingerscan/form keterangan tidak absen.");
                                    markerWarningNocheckout.setVisibility(View.VISIBLE);
                                    dataNoCheckoutRV.setVisibility(View.VISIBLE);
                                    loadingNoCheckoutPart.setVisibility(View.GONE);
                                    String data_no_checkout = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataNoCheckouts = gson.fromJson(data_no_checkout, DataNoCheckout[].class);
                                    adapterDataNoCheckout = new AdapterDataNoCheckout(dataNoCheckouts,DetailTidakCheckoutActivity.this);
                                    dataNoCheckoutRV.setAdapter(adapterDataNoCheckout);
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
        CookieBar.build(DetailTidakCheckoutActivity.this)
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