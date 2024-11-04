package com.gelora.absensi;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterSales;
import com.gelora.absensi.adapter.AdapterWilayahSumaStatistik;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.WilayahSuma;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaleStatisticActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    EditText searchInput;
    LinearLayout suma1Part, suma2Part, suma3Part, sumaBandungPart, sumaSemarangPart, sumaSurabayaPart, sumaPalembangPart, jakartaAePart , digitalMarketingPart, wilayahChoiceBTN, connectBTN, closeBTN, backBTN, actionBar, tryWarningBTN;
    TextView wilayahChoiceTV, dataUpdateTV, pendingTV, processTV, completeTV, totalTV;
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
    private AdapterWilayahSumaStatistik adapterWilayahSuma;
    private WilayahSuma[] wilayahSumas;
    private Handler handler = new Handler();
    RecyclerView wilayahRV;
    private static final String REQUEST_TAG = "LIST_DATA_REQUEST";
    private RecyclerView recyclerView;
    private List<String> listSales = new ArrayList<>();
    private AdapterSales adapterSalesSuma;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_statistic);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
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

        wilayahChoiceTV = findViewById(R.id.wilayah_choice_tv);
        wilayahChoiceBTN = findViewById(R.id.wilayah_choice_btn);

        suma1Part = findViewById(R.id.suma_1_part);
        suma2Part = findViewById(R.id.suma_2_part);
        suma3Part = findViewById(R.id.suma_3_part);
        sumaBandungPart = findViewById(R.id.suma_bandung_part);
        sumaSemarangPart = findViewById(R.id.suma_semarang_part);
        sumaSurabayaPart = findViewById(R.id.suma_surabaya_part);
        sumaPalembangPart = findViewById(R.id.suma_palembang_part);
        jakartaAePart = findViewById(R.id.jakarta_ae_part);
        digitalMarketingPart = findViewById(R.id.digital_marketing_part);
        searchInput = findViewById(R.id.keyword_sales_ed);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_WILAYAH_SUMA_STATISTIK, "1");
        LocalBroadcastManager.getInstance(this).registerReceiver(wilayahSumaBroad, new IntentFilter("wilayah_suma_broad"));

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                String wilayah = wilayahChoiceTV.getText().toString();
                if(wilayah.equals("Suma Jakarta 1")) {
                    getDataPerSales("SUMA 1");
                } else if(wilayah.equals("Suma Jakarta 2")) {
                    getDataPerSales("SUMA 2");
                } else if(wilayah.equals("Suma Jakarta 3")) {
                    getDataPerSales("SUMA 3");
                } else if(wilayah.equals("Suma Bandung")) {
                    getDataPerSales("BANDUNG");
                } else if(wilayah.equals("Suma Semarang")) {
                    getDataPerSales("SEMARANG");
                } else if(wilayah.equals("Suma Surabaya")) {
                    getDataPerSales("SURABAYA");
                } else if(wilayah.equals("Suma Palembang")) {
                    getDataPerSales("PALEMBANG");
                } else if(wilayah.equals("Jakarta AE")) {
                    getDataPerSales("JAKARTA");
                } else if(wilayah.equals("Digital Marketing")) {
                    getDataPerSales("DIGITAL MARKETING");
                }
                getTryWarning();
                getData();
                searchInput.clearFocus();
                searchInput.setText("");
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
                searchInput.clearFocus();
                tryWarning();
            }
        });

        wilayahChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.clearFocus();
                wilayahPicker();
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterSalesSuma.filterByName(s.toString());
                } catch (NullPointerException e) {
                    Log.e("Error", e.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                 searchInput.clearFocus();
                 return false;
                }
                return false;
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
                                        String sale_until = response.getString("sale_until");

                                        String input_date_masuk = sale_until;
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

                                        suma1Part.setVisibility(View.VISIBLE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);

                                        dataUpdateTV.setText("* Update penjualan hingga "+Integer.parseInt(dayDateMasuk) +" "+bulanNameMasuk+" "+yearDateMasuk);

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
                                    } else {
                                        new KAlertDialog(SaleStatisticActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Terjadi kesalahan saat mengakses data")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
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

    private void getDataPerSales(String wilayah){
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
                                    listSales.clear();

                                    if(status.equals("Success")){
                                        JSONObject data = response.getJSONObject("data");
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

                                        String list_departemen = data.getString("list_departemen");
                                        JSONArray jsonArray = new JSONArray(list_departemen);
                                        int arrayLength = jsonArray.length();

                                        JSONObject dataPerDepartemen = new JSONObject();
                                        for (int i = 0; i < arrayLength; i++) {
                                            dataPerDepartemen = jsonArray.getJSONObject(i);
                                            String departemen = dataPerDepartemen.getString("departemen");
                                            if(departemen.equals(wilayah)){
                                                String list_sales = dataPerDepartemen.getString("sales");
                                                JSONArray jsonArraySales = new JSONArray(list_sales);
                                                int arraySalesLength = jsonArraySales.length();
                                                JSONObject dataPerSales = new JSONObject();
                                                for (int j = 0; j < arraySalesLength; j++) {
                                                    dataPerSales = jsonArraySales.getJSONObject(j);
                                                    String nama_sales = dataPerSales.getString("nama_sales");
                                                    String pending = dataPerSales.getString("pending");
                                                    String in_process = dataPerSales.getString("in_process");
                                                    String complete = dataPerSales.getString("complete");
                                                    String total = dataPerSales.getString("total");
                                                    listSales.add(nama_sales+"~"+pending+"~"+in_process+"~"+complete+"~"+total);
                                                }
                                            }
                                        }

                                        adapterSalesSuma = new AdapterSales(listSales);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(SaleStatisticActivity.this));
                                        recyclerView.setAdapter(adapterSalesSuma);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setNestedScrollingEnabled(false);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                                    } else {
                                        new KAlertDialog(SaleStatisticActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Terjadi kesalahan saat mengakses data")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
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

    private void wilayahPicker(){
        bottomSheetLayout.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_wilayah_suma, bottomSheetLayout, false));
        wilayahRV = findViewById(R.id.wilayah_rv);
        TextView semuaBTN = findViewById(R.id.semua_wilayah_btn);
        semuaBTN.setVisibility(View.GONE);
        try {
            wilayahRV.setLayoutManager(new LinearLayoutManager(this));
            wilayahRV.setHasFixedSize(true);
            wilayahRV.setNestedScrollingEnabled(false);
            wilayahRV.setItemAnimator(new DefaultItemAnimator());
            getWilayah();
        } catch (NullPointerException e){
            Log.e("Error", e.toString());
        }
    }

    public BroadcastReceiver wilayahSumaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String idWilayah = intent.getStringExtra("id_wilayah_suma");
            String namaWilayah = intent.getStringExtra("nama_wilayah_suma");

            if(namaWilayah.equals("Jakarta 1") || namaWilayah.equals("Jakarta 2") || namaWilayah.equals("Jakarta 3") || namaWilayah.equals("Bandung") || namaWilayah.equals("Semarang") || namaWilayah.equals("Surabaya") || namaWilayah.equals("Palembang")){
                wilayahChoiceTV.setText("Suma "+namaWilayah);
            } else {
                wilayahChoiceTV.setText(namaWilayah);
            }

            searchInput.setText("");

            InputMethodManager imm = (InputMethodManager) SaleStatisticActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = SaleStatisticActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(SaleStatisticActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            String wilayah = wilayahChoiceTV.getText().toString();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetLayout.dismissSheet();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(wilayah.equals("Suma Jakarta 1")) {
                                        suma1Part.setVisibility(View.VISIBLE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("SUMA 1");
                                    } else if(wilayah.equals("Suma Jakarta 2")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.VISIBLE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("SUMA 2");
                                    } else if(wilayah.equals("Suma Jakarta 3")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.VISIBLE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("SUMA 3");
                                    } else if(wilayah.equals("Suma Bandung")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.VISIBLE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("BANDUNG");
                                    } else if(wilayah.equals("Suma Semarang")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.VISIBLE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("SEMARANG");
                                    } else if(wilayah.equals("Suma Surabaya")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.VISIBLE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("SURABAYA");
                                    } else if(wilayah.equals("Suma Palembang")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.VISIBLE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("PALEMBANG");
                                    } else if(wilayah.equals("Jakarta AE")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.VISIBLE);
                                        digitalMarketingPart.setVisibility(View.GONE);
                                        getDataPerSales("JAKARTA");
                                    } else if(wilayah.equals("Digital Marketing")) {
                                        suma1Part.setVisibility(View.GONE);
                                        suma2Part.setVisibility(View.GONE);
                                        suma3Part.setVisibility(View.GONE);
                                        sumaBandungPart.setVisibility(View.GONE);
                                        sumaSemarangPart.setVisibility(View.GONE);
                                        sumaSurabayaPart.setVisibility(View.GONE);
                                        sumaPalembangPart.setVisibility(View.GONE);
                                        jakartaAePart.setVisibility(View.GONE);
                                        digitalMarketingPart.setVisibility(View.VISIBLE);
                                        getDataPerSales("DIGITAL MARKETING");
                                    }
                                }
                            });
                        }
                    }, 0);
                }
            }, 300);
        }
    };

    private void getWilayah() {
        final String url = "https://reporting.sumasistem.co.id/api/get_wilayah?asmen=0&role=statistik";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            Log.d("Success.Response", response.toString());
                            String data = response.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            wilayahSumas = gson.fromJson(data, WilayahSuma[].class);
                            adapterWilayahSuma = new AdapterWilayahSumaStatistik(wilayahSumas,SaleStatisticActivity.this);
                            try {
                                wilayahRV.setAdapter(adapterWilayahSuma);
                            } catch (NullPointerException e) {
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
                Log.d("Error.Response", error.toString());
                bottomSheetLayout.dismissSheet();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void connectionFailed(){
        CookieBar.build(SaleStatisticActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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
        getDataPerSales("SUMA 1");
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