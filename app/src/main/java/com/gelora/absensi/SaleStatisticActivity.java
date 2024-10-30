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
    TextView aeCompleteTV, aeProcessTV, aePendingTV, aeTotalTV;
    TextView suma1CompleteTV, suma1ProcessTV, suma1PendingTV, suma1TotalTV;
    TextView suma2CompleteTV, suma2ProcessTV, suma2PendingTV, suma2TotalTV;
    TextView suma3CompleteTV, suma3ProcessTV, suma3PendingTV, suma3TotalTV;
    TextView sumaBandungCompleteTV, sumaBandungProcessTV, sumaBandungPendingTV, sumaBandungTotalTV;
    TextView sumaSemarangCompleteTV, sumaSemarangProcessTV, sumaSemarangPendingTV, sumaSemarangTotalTV;
    TextView sumaSurabayaCompleteTV, sumaSurabayaProcessTV, sumaSurabayaPendingTV, sumaSurabayaTotalTV;
    TextView sumaPalembangCompleteTV, sumaPalembangProcessTV, sumaPalembangPendingTV, sumaPalembangTotalTV;
    TextView digitalMarketingCompleteTV, digitalMarketingProcessTV, digitalMarketingPendingTV, digitalMarketingTotalTV;
    RequestQueue requestQueue;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheetLayout;
    private Handler handler = new Handler();
    private static final String REQUEST_TAG = "LIST_DATA_REQUEST";

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

        suma1PendingTV = findViewById(R.id.suma_1_pending_tv);
        suma1ProcessTV = findViewById(R.id.suma_1_process_tv);
        suma1CompleteTV = findViewById(R.id.suma_1_complete_tv);
        suma1TotalTV = findViewById(R.id.suma_1_total_tv);

        suma2PendingTV = findViewById(R.id.suma_2_pending_tv);
        suma2ProcessTV = findViewById(R.id.suma_2_process_tv);
        suma2CompleteTV = findViewById(R.id.suma_2_complete_tv);
        suma2TotalTV = findViewById(R.id.suma_2_total_tv);

        suma3PendingTV = findViewById(R.id.suma_3_pending_tv);
        suma3ProcessTV = findViewById(R.id.suma_3_process_tv);
        suma3CompleteTV = findViewById(R.id.suma_3_complete_tv);
        suma3TotalTV = findViewById(R.id.suma_3_total_tv);

        sumaBandungPendingTV = findViewById(R.id.suma_bandung_pending_tv);
        sumaBandungProcessTV = findViewById(R.id.suma_bandung_process_tv);
        sumaBandungCompleteTV = findViewById(R.id.suma_bandung_complete_tv);
        sumaBandungTotalTV = findViewById(R.id.suma_bandung_total_tv);

        sumaSemarangPendingTV = findViewById(R.id.suma_semarang_pending_tv);
        sumaSemarangProcessTV = findViewById(R.id.suma_semarang_process_tv);
        sumaSemarangCompleteTV = findViewById(R.id.suma_semarang_complete_tv);
        sumaSemarangTotalTV = findViewById(R.id.suma_semarang_total_tv);

        sumaSurabayaPendingTV = findViewById(R.id.suma_surabaya_pending_tv);
        sumaSurabayaProcessTV = findViewById(R.id.suma_surabaya_process_tv);
        sumaSurabayaCompleteTV = findViewById(R.id.suma_surabaya_complete_tv);
        sumaSurabayaTotalTV = findViewById(R.id.suma_surabaya_total_tv);

        sumaPalembangPendingTV = findViewById(R.id.suma_palembang_pending_tv);
        sumaPalembangProcessTV = findViewById(R.id.suma_palembang_process_tv);
        sumaPalembangCompleteTV = findViewById(R.id.suma_palembang_complete_tv);
        sumaPalembangTotalTV = findViewById(R.id.suma_palembang_total_tv);

        digitalMarketingPendingTV = findViewById(R.id.digital_marketing_pending_tv);
        digitalMarketingProcessTV = findViewById(R.id.digital_marketing_process_tv);
        digitalMarketingCompleteTV = findViewById(R.id.digital_marketing_complete_tv);
        digitalMarketingTotalTV = findViewById(R.id.digital_marketing_total_tv);

        aePendingTV = findViewById(R.id.ae_pending_tv);
        aeProcessTV = findViewById(R.id.ae_process_tv);
        aeCompleteTV = findViewById(R.id.ae_complete_tv);
        aeTotalTV = findViewById(R.id.ae_total_tv);

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

                                        animateCurrency("total_pending", 0, pending, 3000);
                                        animateCurrency("total_process", 0, process, 3000);
                                        animateCurrency("total_complete", 0, complete, 3000);
                                        animateCurrency("total_total", 0, total, 3000);

                                        JSONObject total_per_departemen = data.getJSONObject("total_per_departemen");
                                        JSONObject suma_1 = total_per_departemen.getJSONObject("SUMA 1");
                                        long suma_1_pending = suma_1.getLong("pending");
                                        long suma_1_process = suma_1.getLong("in_process");
                                        long suma_1_complete = suma_1.getLong("complete");
                                        long suma_1_total = suma_1.getLong("total");

                                        animateCurrency("suma_1_pending", 0, suma_1_pending, 3000);
                                        animateCurrency("suma_1_process", 0, suma_1_process, 3000);
                                        animateCurrency("suma_1_complete", 0, suma_1_complete, 3000);
                                        animateCurrency("suma_1_total", 0, suma_1_total, 3000);

                                        JSONObject suma_2 = total_per_departemen.getJSONObject("SUMA 2");
                                        long suma_2_pending = suma_2.getLong("pending");
                                        long suma_2_process = suma_2.getLong("in_process");
                                        long suma_2_complete = suma_2.getLong("complete");
                                        long suma_2_total = suma_2.getLong("total");

                                        animateCurrency("suma_2_pending", 0, suma_2_pending, 3000);
                                        animateCurrency("suma_2_process", 0, suma_2_process, 3000);
                                        animateCurrency("suma_2_complete", 0, suma_2_complete, 3000);
                                        animateCurrency("suma_2_total", 0, suma_2_total, 3000);

                                        JSONObject suma_3 = total_per_departemen.getJSONObject("SUMA 3");
                                        long suma_3_pending = suma_3.getLong("pending");
                                        long suma_3_process = suma_3.getLong("in_process");
                                        long suma_3_complete = suma_3.getLong("complete");
                                        long suma_3_total = suma_3.getLong("total");

                                        animateCurrency("suma_3_pending", 0, suma_3_pending, 3000);
                                        animateCurrency("suma_3_process", 0, suma_3_process, 3000);
                                        animateCurrency("suma_3_complete", 0, suma_3_complete, 3000);
                                        animateCurrency("suma_3_total", 0, suma_3_total, 3000);

                                        JSONObject suma_bandung = total_per_departemen.getJSONObject("BANDUNG");
                                        long suma_bandung_pending = suma_bandung.getLong("pending");
                                        long suma_bandung_process = suma_bandung.getLong("in_process");
                                        long suma_bandung_complete = suma_bandung.getLong("complete");
                                        long suma_bandung_total = suma_bandung.getLong("total");

                                        animateCurrency("suma_bandung_pending", 0, suma_bandung_pending, 3000);
                                        animateCurrency("suma_bandung_process", 0, suma_bandung_process, 3000);
                                        animateCurrency("suma_bandung_complete", 0, suma_bandung_complete, 3000);
                                        animateCurrency("suma_bandung_total", 0, suma_bandung_total, 3000);

                                        JSONObject suma_semarang = total_per_departemen.getJSONObject("SEMARANG");
                                        long suma_semarang_pending = suma_semarang.getLong("pending");
                                        long suma_semarang_process = suma_semarang.getLong("in_process");
                                        long suma_semarang_complete = suma_semarang.getLong("complete");
                                        long suma_semarang_total = suma_semarang.getLong("total");

                                        animateCurrency("suma_semarang_pending", 0, suma_semarang_pending, 3000);
                                        animateCurrency("suma_semarang_process", 0, suma_semarang_process, 3000);
                                        animateCurrency("suma_semarang_complete", 0, suma_semarang_complete, 3000);
                                        animateCurrency("suma_semarang_total", 0, suma_semarang_total, 3000);

                                        JSONObject suma_surabaya = total_per_departemen.getJSONObject("SURABAYA");
                                        long suma_surabaya_pending = suma_surabaya.getLong("pending");
                                        long suma_surabaya_process = suma_surabaya.getLong("in_process");
                                        long suma_surabaya_complete = suma_surabaya.getLong("complete");
                                        long suma_surabaya_total = suma_surabaya.getLong("total");

                                        animateCurrency("suma_surabaya_pending", 0, suma_surabaya_pending, 3000);
                                        animateCurrency("suma_surabaya_process", 0, suma_surabaya_process, 3000);
                                        animateCurrency("suma_surabaya_complete", 0, suma_surabaya_complete, 3000);
                                        animateCurrency("suma_surabaya_total", 0, suma_surabaya_total, 3000);

                                        // JSONObject suma_palembang = total_per_departemen.getJSONObject("PALEMBANG");
                                        // long suma_palembang_pending = suma_palembang.getLong("pending");
                                        // long suma_palembang_process = suma_palembang.getLong("in_process");
                                        // long suma_palembang_complete = suma_palembang.getLong("complete");
                                        // long suma_palembang_total = suma_palembang.getLong("total");
                                         long suma_palembang_pending = 0;
                                         long suma_palembang_process = 0;
                                         long suma_palembang_complete = 0;
                                         long suma_palembang_total = 0;

                                        animateCurrency("suma_palembang_pending", 0, suma_palembang_pending, 3000);
                                        animateCurrency("suma_palembang_process", 0, suma_palembang_process, 3000);
                                        animateCurrency("suma_palembang_complete", 0, suma_palembang_complete, 3000);
                                        animateCurrency("suma_palembang_total", 0, suma_palembang_total, 3000);


                                        JSONObject digital_marketing = total_per_departemen.getJSONObject("DIGITAL MARKETING");
                                        long digital_marketing_pending = digital_marketing.getLong("pending");
                                        long digital_marketing_process = digital_marketing.getLong("in_process");
                                        long digital_marketing_complete = digital_marketing.getLong("complete");
                                        long digital_marketing_total = digital_marketing.getLong("total");

                                        animateCurrency("digital_marketing_pending", 0, digital_marketing_pending, 3000);
                                        animateCurrency("digital_marketing_process", 0, digital_marketing_process, 3000);
                                        animateCurrency("digital_marketing_complete", 0, digital_marketing_complete, 3000);
                                        animateCurrency("digital_marketing_total", 0, digital_marketing_total, 3000);

                                        JSONObject ae = total_per_departemen.getJSONObject("JAKARTA");
                                        long ae_pending = ae.getLong("pending");
                                        long ae_process = ae.getLong("in_process");
                                        long ae_complete = ae.getLong("complete");
                                        long ae_total = ae.getLong("total");

                                        animateCurrency("ae_pending", 0, ae_pending, 3000);
                                        animateCurrency("ae_process", 0, ae_process, 3000);
                                        animateCurrency("ae_complete", 0, ae_complete, 3000);
                                        animateCurrency("ae_total", 0, ae_total, 3000);

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

                request.setTag(REQUEST_TAG);
                request.setRetryPolicy(new DefaultRetryPolicy(5000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
            if(key.equals("total_pending")){
                pendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("total_process")){
                processTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("total_complete")){
                completeTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("total_total")){
                totalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_1_pending")){
                suma1PendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_1_process")){
                suma1ProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_1_complete")){
                suma1CompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_1_total")){
                suma1TotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_2_pending")){
                suma2PendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_2_process")){
                suma2ProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_2_complete")){
                suma2CompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_2_total")){
                suma2TotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_3_pending")){
                suma3PendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_3_process")){
                suma3ProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_3_complete")){
                suma3CompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_3_total")){
                suma3TotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_bandung_pending")){
                sumaBandungPendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_bandung_process")){
                sumaBandungProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_bandung_complete")){
                sumaBandungCompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_bandung_total")){
                sumaBandungTotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_semarang_pending")){
                sumaSemarangPendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_semarang_process")){
                sumaSemarangProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_semarang_complete")){
                sumaSemarangCompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_semarang_total")){
                sumaSemarangTotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_surabaya_pending")){
                sumaSurabayaPendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_surabaya_process")){
                sumaSurabayaProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_surabaya_complete")){
                sumaSurabayaCompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_surabaya_total")){
                sumaSurabayaTotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_palembang_pending")){
                sumaPalembangPendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_palembang_process")){
                sumaPalembangProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_palembang_complete")){
                sumaPalembangCompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("suma_palembang_total")){
                sumaPalembangTotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("digital_marketing_pending")){
                digitalMarketingPendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("digital_marketing_process")){
                digitalMarketingProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("digital_marketing_complete")){
                digitalMarketingCompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("digital_marketing_total")){
                digitalMarketingTotalTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("ae_pending")){
                aePendingTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("ae_process")){
                aeProcessTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("ae_complete")){
                aeCompleteTV.setText(formatToRupiah(animatedValue));
            } else if(key.equals("ae_total")){
                aeTotalTV.setText(formatToRupiah(animatedValue));
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
    protected void onResume() {
        super.onResume();
        getData();
        getTryWarning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUEST_TAG);
        }
    }

    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        } else {
            super.onBackPressed();
            if (requestQueue != null) {
                requestQueue.cancelAll(REQUEST_TAG);
            }
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}