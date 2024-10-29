package com.gelora.absensi;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaleStatisticActivity extends AppCompatActivity {

    LinearLayout connectBTN, closeBTN, backBTN, actionBar, tryWarningBTN;
    TextView dataUpdateTV, pendingTV, processTV, completeTV, totalTV;
    RequestQueue requestQueue;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheetLayout;
    private Handler handler = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_statistic);

        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        tryWarningBTN = findViewById(R.id.try_warning_btn);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        pendingTV = findViewById(R.id.pending_tv);
        processTV = findViewById(R.id.process_tv);
        completeTV = findViewById(R.id.complete_tv);
        totalTV = findViewById(R.id.total_tv);
        dataUpdateTV = findViewById(R.id.data_update_tv);

        String input_date_masuk = getDate();
        String dayDateMasuk = input_date_masuk.substring(8,10);
        String yearDateMasuk = input_date_masuk.substring(0,4);
        String bulanValueMasuk = input_date_masuk.substring(5,7);
        String bulanNameMasuk;

        switch (bulanValueMasuk) {
            case "01":
                bulanNameMasuk = "Januari";
                break;
            case "02":
                bulanNameMasuk = "Februari";
                break;
            case "03":
                bulanNameMasuk = "Maret";
                break;
            case "04":
                bulanNameMasuk = "April";
                break;
            case "05":
                bulanNameMasuk = "Mei";
                break;
            case "06":
                bulanNameMasuk = "Juni";
                break;
            case "07":
                bulanNameMasuk = "Juli";
                break;
            case "08":
                bulanNameMasuk = "Agustus";
                break;
            case "09":
                bulanNameMasuk = "September";
                break;
            case "10":
                bulanNameMasuk = "Oktober";
                break;
            case "11":
                bulanNameMasuk = "November";
                break;
            case "12":
                bulanNameMasuk = "Desember";
                break;
            default:
                bulanNameMasuk = "Not found";
                break;
        }

        dataUpdateTV.setText("* Update per "+Integer.parseInt(dayDateMasuk) +" "+bulanNameMasuk+" "+yearDateMasuk);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                getTryWarning();
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

        tryWarningBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWarning();
            }
        });

    }

    private void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://reporting.sumasistem.co.id/api/get_statistic_accurate?tanggal="+getDate();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("PaRSE JSON", response + "");
                                try {
                                    String status = response.getString("status");
                                    if(status.equals("Success")){
                                        JSONObject data = response.getJSONObject("data");
                                        JSONObject data_total = data.getJSONObject("total");
                                        long pending = data_total.getLong("pending");
                                        long process = data_total.getLong("in_process");
                                        long complete = data_total.getLong("complete");
                                        long total = data_total.getLong("total");

                                        animateCurrency("pending", 0, pending, 3000);
                                        animateCurrency("process", 0, process, 3000);
                                        animateCurrency("complete", 0, complete, 3000);
                                        animateCurrency("total", 0, total, 3000);

                                    } else {

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
                            new KAlertDialog(SaleStatisticActivity.this, KAlertDialog.ERROR_TYPE)
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
        }).start();
    }

    private void getTryWarning() {
        final String url = "https://hrisgelora.co.id/api/lhs_statistic_try_status";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            String visibility = response.getString("visibility");
                            if(visibility.equals("1")){
                                tryWarningBTN.setVisibility(View.VISIBLE);
                            } else {
                                tryWarningBTN.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new KAlertDialog(SaleStatisticActivity.this, KAlertDialog.ERROR_TYPE)
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
            }
        });

        requestQueue.add(request);

    }

    private void tryWarning(){
        bottomSheetLayout.showWithSheetView(LayoutInflater.from(SaleStatisticActivity.this).inflate(R.layout.layout_try_feature, bottomSheetLayout, false));
        closeBTN = findViewById(R.id.close_btn);
        connectBTN = findViewById(R.id.connect_btn);
        getContactIT();
    }

    private void getContactIT() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://hrisgelora.co.id/api/get_contact_it";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String bagian = response.getString("bagian");
                            String nama = response.getString("nama");
                            String whatsapp = response.getString("whatsapp");

                            closeBTN = findViewById(R.id.close_btn);
                            connectBTN = findViewById(R.id.connect_btn);

                            try {
                                closeBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bottomSheetLayout.dismissSheet();
                                    }
                                });
                                connectBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                                        try {
                                            startActivity(webIntent);
                                        } catch (SecurityException e) {
                                            e.printStackTrace();
                                            new KAlertDialog(SaleStatisticActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Tidak dapat terhubung ke Whatsapp, anda bisa hubungi secara langsung ke 0"+whatsapp.substring(2, whatsapp.length())+" atas nama Bapak "+nama+" bagian HRD")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    }
                                });
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    private void animateCurrency(String key, long start, long end, int duration) {
        long delta = end - start;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            long animatedValue = start + (long) (delta * fraction);
            if(key.equals("pending")){
                pendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("process")){
                processTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("complete")){
                completeTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("total")){
                totalTV.setText(formatToRupiah(animatedValue));
            }
        });
        animator.start();
    }

    private String formatToRupiah(long amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getTryWarning();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}