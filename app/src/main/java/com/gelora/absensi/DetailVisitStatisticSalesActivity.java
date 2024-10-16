package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterListDataVisitStatisticSales;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.SalesVisitStatistic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailVisitStatisticSalesActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    LinearLayout backBTN, actionBar;
    String nikSales, month;
    CircleImageView profileImage;
    TextView monthTV, namaSalesTV, totalKunjunganTV, promosiTV, penagihanTV, pengoirimanTV, pameranTV, jvTV, njvTV, lainnyaTV;
    ImageView promosiLoading, penagihanLoading, pengirimanLoading, pameranLoading, jvLoading, njvLoading, lainnyaLoading;
    private Handler handler = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_visit_statistic_sales);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        promosiLoading = findViewById(R.id.promosi_loading);
        penagihanLoading = findViewById(R.id.penagihan_loading);
        pengirimanLoading = findViewById(R.id.pengiriman_loading);
        pameranLoading = findViewById(R.id.pameran_loading);
        jvLoading = findViewById(R.id.jv_loading);
        njvLoading = findViewById(R.id.njv_loading);
        lainnyaLoading = findViewById(R.id.lainnya_loading);
        promosiTV = findViewById(R.id.promosi_tv);
        penagihanTV = findViewById(R.id.penagihan_tv);
        pengoirimanTV = findViewById(R.id.pengiriman_tv);
        pameranTV = findViewById(R.id.pameran_tv);
        jvTV = findViewById(R.id.jv_tv);
        njvTV = findViewById(R.id.njv_tv);
        lainnyaTV = findViewById(R.id.lainnya_tv);
        totalKunjunganTV = findViewById(R.id.total_kunjungan);
        namaSalesTV = findViewById(R.id.nama_sales_tv);
        profileImage = findViewById(R.id.profile_image);
        monthTV = findViewById(R.id.month_tv);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(promosiLoading);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(penagihanLoading);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(pengirimanLoading);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(pameranLoading);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(jvLoading);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(njvLoading);

        Glide.with(DetailVisitStatisticSalesActivity.this)
                .load(R.drawable.loading_dots)
                .into(lainnyaLoading);

        nikSales = getIntent().getExtras().getString("NIK");
        month = getIntent().getExtras().getString("month");

        String bulan = month.split("-")[1], bulanName = "";
        if(bulan.equals("01")){
            bulanName = "Januari";
        } else if (bulan.equals("02")){
            bulanName = "Februari";
        } else if (bulan.equals("03")){
            bulanName = "Maret";
        } else if (bulan.equals("04")){
            bulanName = "April";
        } else if (bulan.equals("05")){
            bulanName = "Mei";
        } else if (bulan.equals("06")){
            bulanName = "Juni";
        } else if (bulan.equals("07")){
            bulanName = "Juli";
        } else if (bulan.equals("08")){
            bulanName = "Agustus";
        } else if (bulan.equals("09")){
            bulanName = "September";
        } else if (bulan.equals("10")){
            bulanName = "Oktober";
        } else if (bulan.equals("11")){
            bulanName = "November";
        } else if (bulan.equals("12")){
            bulanName = "Desember";
        }

        monthTV.setText(bulanName+" "+month.split("-")[0]);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                promosiLoading.setVisibility(View.VISIBLE);
                penagihanLoading.setVisibility(View.VISIBLE);
                pengirimanLoading.setVisibility(View.VISIBLE);
                pameranLoading.setVisibility(View.VISIBLE);
                jvLoading.setVisibility(View.VISIBLE);
                njvLoading.setVisibility(View.VISIBLE);
                lainnyaLoading.setVisibility(View.VISIBLE);

                promosiTV.setVisibility(View.GONE);
                penagihanTV.setVisibility(View.GONE);
                pengoirimanTV.setVisibility(View.GONE);
                pameranTV.setVisibility(View.GONE);
                jvTV.setVisibility(View.GONE);
                njvTV.setVisibility(View.GONE);
                lainnyaTV.setVisibility(View.GONE);

                getData();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData();

    }

    private void getData() {
        String url = "https://reporting.sumasistem.co.id/api/get_detail_visit_statistic_sales?nik="+nikSales+"&month="+month;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                String jumlah_promosi = response.getString("jumlah_promosi");
                                String jumlah_penagihan = response.getString("jumlah_penagihan");
                                String jumlah_pengiriman = response.getString("jumlah_pengiriman");
                                String jumlah_pameran = response.getString("jumlah_pameran");
                                String jumlah_jv = response.getString("jumlah_jv");
                                String jumlah_njv = response.getString("jumlah_njv");
                                String jumlah_multi = response.getString("jumlah_multi");
                                String detail = response.getString("detail");

                                JSONObject detailObject = new JSONObject(detail);
                                String total_kunjungan = detailObject.getString("total_kunjungan");
                                String namaKaryawan = detailObject.getString("namaKaryawan");
                                String avatar = detailObject.getString("avatar");
                                totalKunjunganTV.setText(total_kunjungan);
                                namaSalesTV.setText(namaKaryawan.toUpperCase());

                                Picasso.get().load(avatar).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .resize(150, 150)
                                        .into(profileImage);

                                promosiLoading.setVisibility(View.GONE);
                                penagihanLoading.setVisibility(View.GONE);
                                pengirimanLoading.setVisibility(View.GONE);
                                pameranLoading.setVisibility(View.GONE);
                                jvLoading.setVisibility(View.GONE);
                                njvLoading.setVisibility(View.GONE);
                                lainnyaLoading.setVisibility(View.GONE);

                                promosiTV.setVisibility(View.VISIBLE);
                                penagihanTV.setVisibility(View.VISIBLE);
                                pengoirimanTV.setVisibility(View.VISIBLE);
                                pameranTV.setVisibility(View.VISIBLE);
                                jvTV.setVisibility(View.VISIBLE);
                                njvTV.setVisibility(View.VISIBLE);
                                lainnyaTV.setVisibility(View.VISIBLE);

                                promosiTV.setText(jumlah_promosi);
                                penagihanTV.setText(jumlah_penagihan);
                                pengoirimanTV.setText(jumlah_pengiriman);
                                pameranTV.setText(jumlah_pameran);
                                jvTV.setText(jumlah_jv);
                                njvTV.setText(jumlah_njv);
                                lainnyaTV.setText(jumlah_multi);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                try {
                    new KAlertDialog(DetailVisitStatisticSalesActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Gagal terhubung, harap periksa koneksi internet atau jaringan anda")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } catch (WindowManager.BadTokenException e){
                    Log.e("Error", "Error : "+e.toString());
                }
            }
        });
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        requestQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}