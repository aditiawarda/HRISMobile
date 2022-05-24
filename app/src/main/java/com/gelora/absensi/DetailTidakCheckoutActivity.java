package com.gelora.absensi;

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
import com.gelora.absensi.adapter.AdapterDataNoCheckout;
import com.gelora.absensi.adapter.AdapterDataTerlambat;
import com.gelora.absensi.model.DataNoCheckout;
import com.gelora.absensi.model.DataTerlambat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailTidakCheckoutActivity extends AppCompatActivity {

    LinearLayout emptyDataNoCheckout, loadingNoCheckoutPart, backBTN, homeBTN;
    ImageView bulanLoading, noCheckoutLoading, loadingDataNoCheckout;
    TextView dataBulan, dataTahun, dataNoCheckout, nameUserTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    private RecyclerView dataNoCheckoutRV;
    private DataNoCheckout[] dataNoCheckouts;
    private AdapterDataNoCheckout adapterDataNoCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tidak_checkout);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        dataBulan = findViewById(R.id.bulan_data);
        dataTahun = findViewById(R.id.tahun_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        bulanLoading = findViewById(R.id.bulan_loading);
        noCheckoutLoading = findViewById(R.id.nocheckout_loading);
        dataNoCheckout = findViewById(R.id.data_nocheckout);
        loadingDataNoCheckout = findViewById(R.id.loading_data_nocheckout);
        loadingNoCheckoutPart = findViewById(R.id.loading_data_part_nocheckout);
        emptyDataNoCheckout = findViewById(R.id.no_data_part_nocheckout);

        dataNoCheckoutRV = findViewById(R.id.data_nocheckout_rv);

        dataNoCheckoutRV.setLayoutManager(new LinearLayoutManager(this));
        dataNoCheckoutRV.setHasFixedSize(true);
        dataNoCheckoutRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(noCheckoutLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataNoCheckout);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bulanLoading.setVisibility(View.VISIBLE);
                dataBulan.setVisibility(View.GONE);
                dataTahun.setVisibility(View.GONE);

                noCheckoutLoading.setVisibility(View.VISIBLE);
                dataNoCheckout.setVisibility(View.GONE);

                dataNoCheckoutRV.setVisibility(View.GONE);
                loadingNoCheckoutPart.setVisibility(View.VISIBLE);
                emptyDataNoCheckout.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailNoCheckout();
                    }
                }, 800);
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
                Intent intent = new Intent(DetailTidakCheckoutActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        nameUserTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        getDetailNoCheckout();

    }

    private void getDetailNoCheckout() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_tidak_checkout";
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
                                    emptyDataNoCheckout.setVisibility(View.VISIBLE);
                                    dataNoCheckoutRV.setVisibility(View.GONE);
                                    loadingNoCheckoutPart.setVisibility(View.GONE);
                                } else {
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
                        //connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", getBulanTahun());
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