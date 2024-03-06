package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterKaryawanSales;
import com.gelora.absensi.adapter.AdapterSumaReport;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataReportSuma;
import com.gelora.absensi.model.KaryawanSales;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ListDataReportSumaActivity extends AppCompatActivity {

    LinearLayout subCatBTN, attantionReportPart, salesChoiceBTN, salesBTN, catBTN, filterBarPart, dateBTN, noDataPartReport, loadingDataPartReport, rencanaBTN, aktivitasBTN, penagihanBTN, pengirimanBTN, promosiBTN, markRencana, markAktivitas, markPenagihan, markPengiriman, markKunjungan, actionBar, backBTN, addBTN, filterCategoryBTN, filterSubCategoryBTN;
    TextView semuaDataBTN, salesChoiceTV, choiceDateTV, categoryChoiceTV, subCategoryChoiceTV, dateLabel;
    ImageView loadingDataReport;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    BottomSheetLayout bottomSheet;
    String categoryCode = "", subCategoryCode = "", dateChoice = getDate();
    private RecyclerView reportRV;
    private DataReportSuma[] dataReportSumas;
    private AdapterSumaReport adapterSumaReport;
    EditText keywordKaryawanSales;
    RecyclerView karyawanSalesRV;
    LinearLayout contentPart, startAttantionPart, noDataPart, loadingDataPart;
    ImageView loadingGif;
    private KaryawanSales[] karyawanSales;
    private AdapterKaryawanSales adapterKaryawanSales;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_report_suma);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        addBTN = findViewById(R.id.add_btn);
        loadingDataReport = findViewById(R.id.loading_data_report);
        filterCategoryBTN = findViewById(R.id.filter_category_btn);
        filterSubCategoryBTN = findViewById(R.id.filter_sub_category_btn);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        categoryChoiceTV = findViewById(R.id.category_choice_tv);
        loadingDataPartReport = findViewById(R.id.loading_data_part_report);
        noDataPartReport = findViewById(R.id.no_data_part_report);
        attantionReportPart = findViewById(R.id.attantion_data_part_report);
        reportRV = findViewById(R.id.data_report_rv);
        dateBTN = findViewById(R.id.date_btn);
        choiceDateTV = findViewById(R.id.choice_date_tv);
        filterBarPart = findViewById(R.id.filter_bar_part);
        dateLabel = findViewById(R.id.date_label);
        catBTN = findViewById(R.id.cat_btn);
        subCatBTN = findViewById(R.id.sub_cat_btn);
        salesBTN = findViewById(R.id.sales_btn);
        salesChoiceBTN = findViewById(R.id.sales_choice_btn);
        salesChoiceTV = findViewById(R.id.sales_choice_tv);
        contentPart = findViewById(R.id.content_part);
        subCategoryChoiceTV = findViewById(R.id.sub_category_choice_tv);

        LocalBroadcastManager.getInstance(this).registerReceiver(karyawanSalesBroad, new IntentFilter("karyawan_sales_broad"));

        if(sharedPrefManager.getSpNik().equals("0499070507")){ //Pa Dawud
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, "");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, "");
            salesChoiceTV.setText("Semua Sales");

            catBTN.setVisibility(View.GONE);
            subCatBTN.setVisibility(View.GONE);
            dateBTN.setVisibility(View.VISIBLE);
            salesBTN.setVisibility(View.VISIBLE);
            addBTN.setVisibility(View.GONE);
            categoryCode = "1";
            subCategoryCode = "3";
            dateLabel.setText("Filter Tanggal Penagihan :");
            subCategoryChoiceTV.setText("Aktivitas Penagihan");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "3");
            float density = getResources().getDisplayMetrics().density;
            contentPart.setPadding((int)(20*density),(int)(20*density),(int)(20*density),(int)(20*density));
        } else {
            catBTN.setVisibility(View.VISIBLE);
            dateBTN.setVisibility(View.VISIBLE);
            if(sharedPrefManager.getSpNik().equals("0121010900") || sharedPrefManager.getSpNik().equals("0981010210") || sharedPrefManager.getSpNik().equals("0552260707") || sharedPrefManager.getSpNik().equals("3318060323") || sharedPrefManager.getSpIdJabatan().equals("43") || sharedPrefManager.getSpIdJabatan().equals("45") || sharedPrefManager.getSpIdJabatan().equals("47") || sharedPrefManager.getSpIdJabatan().equals("49") || sharedPrefManager.getSpIdJabatan().equals("51") || sharedPrefManager.getSpIdJabatan().equals("53") || sharedPrefManager.getSpIdJabatan().equals("55") || sharedPrefManager.getSpIdJabatan().equals("57")){ //Pimpinan Marketing dan GL
                if(sharedPrefManager.getSpIdJabatan().equals("43") || sharedPrefManager.getSpIdJabatan().equals("45") || sharedPrefManager.getSpIdJabatan().equals("47") || sharedPrefManager.getSpIdJabatan().equals("49") || sharedPrefManager.getSpIdJabatan().equals("51") || sharedPrefManager.getSpIdJabatan().equals("53") || sharedPrefManager.getSpIdJabatan().equals("55") || sharedPrefManager.getSpIdJabatan().equals("57")){ //GL
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, sharedPrefManager.getSpNama());
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, sharedPrefManager.getSpNik());
                    addBTN.setVisibility(View.VISIBLE);
                } else { //Asisten Kepala Departemen
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, "");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, "");
                    addBTN.setVisibility(View.GONE);
                    float density = getResources().getDisplayMetrics().density;
                    contentPart.setPadding((int)(20*density),(int)(20*density),(int)(20*density),(int)(20*density));
                }
                salesChoiceTV.setText("Semua Sales");
                salesBTN.setVisibility(View.VISIBLE);
            } else {
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, sharedPrefManager.getSpNama());
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, sharedPrefManager.getSpNik());
                salesChoiceTV.setText(sharedPrefAbsen.getSpSalesActive());
                salesBTN.setVisibility(View.GONE);
                addBTN.setVisibility(View.VISIBLE);
            }
            categoryCode = "1";
            dateLabel.setText("Filter Tanggal Rencana :");
            categoryChoiceTV.setText("Rencana Kunjungan");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "1");
            subCatBTN.setVisibility(View.GONE);
        }

        reportRV.setLayoutManager(new LinearLayoutManager(this));
        reportRV.setHasFixedSize(true);
        reportRV.setNestedScrollingEnabled(false);
        reportRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataReport);

        getDateNow();

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                attantionReportPart.setVisibility(View.GONE);
                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        if(categoryCode.equals("1")){
                            getData(categoryCode);
                        } else {
                            getData(subCategoryCode);
                        }
                    }
                }, 500);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        filterCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryChoice();
            }
        });

        filterSubCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoryChoice();
            }
        });

        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        salesChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesPicker();
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataReportSumaActivity.this, ReportSumaActivity.class);
                startActivity(intent);
            }
        });

        reportRV.setVisibility(View.GONE);
        loadingDataPartReport.setVisibility(View.VISIBLE);
        noDataPartReport.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPrefManager.getSpNik().equals("0499070507")){
                    getData(subCategoryCode);
                } else {
                    getData(categoryCode);
                }

            }
        }, 500);

    }

    @SuppressLint("SetTextI18n")
    private void getDateNow(){
        dateChoice = getDate();
        String input_date = dateChoice;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat")
        DateFormat format2 = new SimpleDateFormat("EEE");
        @SuppressLint("SimpleDateFormat")
        DateFormat getweek = new SimpleDateFormat("W");
        String finalDay = format2.format(dt1);
        String week = getweek.format(dt1);
        String hariName = "";

        if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
            hariName = "Senin";
        } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
            hariName = "Selasa";
        } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
            hariName = "Rabu";
        } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
            hariName = "Kamis";
        } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
            hariName = "Jumat";
        } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
            hariName = "Sabtu";
        } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
            hariName = "Minggu";
        }

        String dayDate = input_date.substring(8,10);
        String yearDate = input_date.substring(0,4);
        String bulanValue = input_date.substring(5,7);
        String bulanName;

        switch (bulanValue) {
            case "01":
                bulanName = "Januari";
                break;
            case "02":
                bulanName = "Februari";
                break;
            case "03":
                bulanName = "Maret";
                break;
            case "04":
                bulanName = "April";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Juni";
                break;
            case "07":
                bulanName = "Juli";
                break;
            case "08":
                bulanName = "Agustus";
                break;
            case "09":
                bulanName = "September";
                break;
            case "10":
                bulanName = "Oktober";
                break;
            case "11":
                bulanName = "November";
                break;
            case "12":
                bulanName = "Desember";
                break;
            default:
                bulanName = "Not found!";
                break;
        }

        choiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

    }

    private void getData(String category_code){
        String url;
        if(sharedPrefAbsen.getSpNikSalesActive().equals("")){
            url = "https://reporting.sumasistem.co.id/api/suma_report_mobile?nik=0&tipe_laporan="+category_code+"&tanggal="+dateChoice+"&requester="+sharedPrefManager.getSpNik();
        } else {
            url = "https://reporting.sumasistem.co.id/api/suma_report_mobile?nik="+sharedPrefAbsen.getSpNikSalesActive()+"&tipe_laporan="+category_code+"&tanggal="+dateChoice+"&requester="+sharedPrefManager.getSpNik();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");

                            if(status.equals("Success")){
                                attantionReportPart.setVisibility(View.GONE);
                                reportRV.setVisibility(View.VISIBLE);
                                loadingDataPartReport.setVisibility(View.GONE);
                                noDataPartReport.setVisibility(View.GONE);

                                String data_report = response.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                dataReportSumas = gson.fromJson(data_report, DataReportSuma[].class);
                                adapterSumaReport = new AdapterSumaReport(dataReportSumas, ListDataReportSumaActivity.this);
                                reportRV.setAdapter(adapterSumaReport);
                            } else {
                                attantionReportPart.setVisibility(View.GONE);
                                reportRV.setVisibility(View.GONE);
                                loadingDataPartReport.setVisibility(View.GONE);
                                noDataPartReport.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                attantionReportPart.setVisibility(View.GONE);
                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.GONE);
                noDataPartReport.setVisibility(View.VISIBLE);

                new KAlertDialog(ListDataReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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

    private void connectionFailed(){
        CookieBar.build(ListDataReportSumaActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }


    @SuppressLint("SetTextI18n")
    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ListDataReportSumaActivity.this).inflate(R.layout.layout_kategori_report_suma_list_data, bottomSheet, false));
        rencanaBTN = findViewById(R.id.rencana_btn);
        aktivitasBTN = findViewById(R.id.aktivitas_btn);
        markRencana = findViewById(R.id.mark_rencana);
        markAktivitas = findViewById(R.id.mark_aktivitas);

        if(categoryCode.equals("1")) {
            dateLabel.setText("Filter Tanggal Rencana :");
            subCatBTN.setVisibility(View.GONE);
            markRencana.setVisibility(View.VISIBLE);
            markAktivitas.setVisibility(View.GONE);
            rencanaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
            aktivitasBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(categoryCode.equals("0")) {
            dateLabel.setText("Filter Tanggal :");
            subCatBTN.setVisibility(View.VISIBLE);
            markRencana.setVisibility(View.GONE);
            markAktivitas.setVisibility(View.VISIBLE);
            rencanaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            aktivitasBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
        }

        rencanaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "1");
                dateLabel.setText("Filter Tanggal Rencana :");
                categoryCode = "1";
                categoryChoiceTV.setText("Rencana Kunjungan");

                subCatBTN.setVisibility(View.GONE);
                markRencana.setVisibility(View.VISIBLE);
                markAktivitas.setVisibility(View.GONE);
                rencanaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                aktivitasBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(categoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        aktivitasBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "0");
                dateLabel.setText("Filter Tanggal :");
                categoryCode = "0";
                subCategoryCode = "2";
                categoryChoiceTV.setText("Aktivitas Kunjungan");
                subCategoryChoiceTV.setText("Aktivitas Promosi");

                subCatBTN.setVisibility(View.VISIBLE);
                markRencana.setVisibility(View.GONE);
                markAktivitas.setVisibility(View.VISIBLE);
                rencanaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                aktivitasBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("0")){
                                    getData(subCategoryCode);
                                } else {
                                    getData(categoryCode);
                                }
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void subCategoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ListDataReportSumaActivity.this).inflate(R.layout.layout_sub_kategori_report_suma_list_data, bottomSheet, false));
        promosiBTN = findViewById(R.id.promosi_btn);
        pengirimanBTN = findViewById(R.id.pengiriman_btn);
        penagihanBTN = findViewById(R.id.penagihan_btn);
        markKunjungan = findViewById(R.id.mark_pesanan);
        markPengiriman = findViewById(R.id.mark_pengiriman);
        markPenagihan = findViewById(R.id.mark_penagihan);

       if(subCategoryCode.equals("2")) {
           dateLabel.setText("Filter Tanggal :");
           markKunjungan.setVisibility(View.VISIBLE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
       } else if(subCategoryCode.equals("3")) {
           dateLabel.setText("Filter Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.VISIBLE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
       } else if(subCategoryCode.equals("4")) {
           dateLabel.setText("Filter Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.VISIBLE);
           markPenagihan.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        }

        promosiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "0");
                dateLabel.setText("Filter Tanggal :");
                subCategoryCode = "2";
                subCategoryChoiceTV.setText("Aktivitas Promosi");
                markKunjungan.setVisibility(View.VISIBLE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(subCategoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        penagihanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "3");
                dateLabel.setText("Filter Tanggal :");
                subCategoryCode = "3";
                subCategoryChoiceTV.setText("Aktivitas Penagihan");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.VISIBLE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(subCategoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        pengirimanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "4");
                dateLabel.setText("Filter Tanggal :");
                subCategoryCode = "4";
                subCategoryChoiceTV.setText("Aktivitas Pengiriman");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.VISIBLE);
                markPenagihan.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(subCategoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private void datePicker(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(ListDataReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoice = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String input_date = dateChoice;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                Date dt1= null;
                try {
                    dt1 = format1.parse(input_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEE");
                DateFormat getweek = new SimpleDateFormat("W");
                String finalDay = format2.format(dt1);
                String week = getweek.format(dt1);
                String hariName = "";

                if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                    hariName = "Senin";
                } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                    hariName = "Selasa";
                } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                    hariName = "Rabu";
                } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                    hariName = "Kamis";
                } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                    hariName = "Jumat";
                } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                    hariName = "Sabtu";
                } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                    hariName = "Minggu";
                }

                String dayDate = input_date.substring(8,10);
                String yearDate = input_date.substring(0,4);
                String bulanValue = input_date.substring(5,7);
                String bulanName;

                switch (bulanValue) {
                    case "01":
                        bulanName = "Januari";
                        break;
                    case "02":
                        bulanName = "Februari";
                        break;
                    case "03":
                        bulanName = "Maret";
                        break;
                    case "04":
                        bulanName = "April";
                        break;
                    case "05":
                        bulanName = "Mei";
                        break;
                    case "06":
                        bulanName = "Juni";
                        break;
                    case "07":
                        bulanName = "Juli";
                        break;
                    case "08":
                        bulanName = "Agustus";
                        break;
                    case "09":
                        bulanName = "September";
                        break;
                    case "10":
                        bulanName = "Oktober";
                        break;
                    case "11":
                        bulanName = "November";
                        break;
                    case "12":
                        bulanName = "Desember";
                        break;
                    default:
                        bulanName = "Not found!";
                        break;
                }

                choiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                attantionReportPart.setVisibility(View.GONE);
                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(categoryCode.equals("1")){
                            getData(categoryCode);
                        } else {
                            getData(subCategoryCode);
                        }
                    }
                }, 1000);

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(ListDataReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoice = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String input_date = dateChoice;
                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                Date dt1= null;
                try {
                    dt1 = format1.parse(input_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEE");
                DateFormat getweek = new SimpleDateFormat("W");
                String finalDay = format2.format(dt1);
                String week = getweek.format(dt1);
                String hariName = "";

                if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                    hariName = "Senin";
                } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                    hariName = "Selasa";
                } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                    hariName = "Rabu";
                } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                    hariName = "Kamis";
                } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                    hariName = "Jumat";
                } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                    hariName = "Sabtu";
                } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                    hariName = "Minggu";
                }

                String dayDate = input_date.substring(8,10);
                String yearDate = input_date.substring(0,4);
                String bulanValue = input_date.substring(5,7);
                String bulanName;

                switch (bulanValue) {
                    case "01":
                        bulanName = "Januari";
                        break;
                    case "02":
                        bulanName = "Februari";
                        break;
                    case "03":
                        bulanName = "Maret";
                        break;
                    case "04":
                        bulanName = "April";
                        break;
                    case "05":
                        bulanName = "Mei";
                        break;
                    case "06":
                        bulanName = "Juni";
                        break;
                    case "07":
                        bulanName = "Juli";
                        break;
                    case "08":
                        bulanName = "Agustus";
                        break;
                    case "09":
                        bulanName = "September";
                        break;
                    case "10":
                        bulanName = "Oktober";
                        break;
                    case "11":
                        bulanName = "November";
                        break;
                    case "12":
                        bulanName = "Desember";
                        break;
                    default:
                        bulanName = "Not found!";
                        break;
                }

                choiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                attantionReportPart.setVisibility(View.GONE);
                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(categoryCode.equals("1")){
                            getData(categoryCode);
                        } else {
                            getData(subCategoryCode);
                        }
                    }
                }, 1000);

            }, y,m-1,d);
            dpd.show();
        }

    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void salesPicker(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ListDataReportSumaActivity.this).inflate(R.layout.layout_karyawan_sales, bottomSheet, false));
        keywordKaryawanSales = findViewById(R.id.keyword_user_sales_ed);
        keywordKaryawanSales.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        karyawanSalesRV = findViewById(R.id.karyawan_sales_rv);
        startAttantionPart = findViewById(R.id.attantion_data_sales_part);
        noDataPart = findViewById(R.id.no_data_sales_part);
        loadingDataPart = findViewById(R.id.loading_data_sales_part);
        loadingGif = findViewById(R.id.loading_sales_data);
        semuaDataBTN = findViewById(R.id.semua_data_btn);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

        karyawanSalesRV.setLayoutManager(new LinearLayoutManager(this));
        karyawanSalesRV.setHasFixedSize(true);
        karyawanSalesRV.setNestedScrollingEnabled(false);
        karyawanSalesRV.setItemAnimator(new DefaultItemAnimator());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSales("Semua");
            }
        }, 800);

        keywordKaryawanSales.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWordSearch = keywordKaryawanSales.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                karyawanSalesRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSales(keyWordSearch);
                        }
                    }, 500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSales("Semua");
                        }
                    }, 500);
                }
            }

        });

        keywordKaryawanSales.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = keywordKaryawanSales.getText().toString();
                    getSales(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) ListDataReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = ListDataReportSumaActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(ListDataReportSumaActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        semuaDataBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                salesChoiceTV.setText("Semua Sales");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("1")){
                                    getData(categoryCode);
                                } else {
                                    getData(subCategoryCode);
                                }
                            }
                        }, 1000);
                    }
                }, 300);
            }
        });

    }

    private void getSales(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sales";
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
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                                karyawanSalesRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                karyawanSales = gson.fromJson(data_list, KaryawanSales[].class);
                                adapterKaryawanSales = new AdapterKaryawanSales(karyawanSales, ListDataReportSumaActivity.this);
                                karyawanSalesRV.setAdapter(adapterKaryawanSales);
                            } else {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
                                karyawanSalesRV.setVisibility(View.GONE);
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
                        startAttantionPart.setVisibility(View.GONE);
                        loadingDataPart.setVisibility(View.GONE);
                        noDataPart.setVisibility(View.VISIBLE);
                        karyawanSalesRV.setVisibility(View.GONE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("requester", sharedPrefManager.getSpIdJabatan());
                if(sharedPrefManager.getSpIdJabatan().equals("3")){
                    params.put("nik", sharedPrefManager.getSpNik());
                }
                params.put("keyword_sales", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public BroadcastReceiver karyawanSalesBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nikKaryawanSales = intent.getStringExtra("nik_karyawan_sales");
            String namaKaryawanSales = intent.getStringExtra("nama_karyawan_sales");

            salesChoiceTV.setText(namaKaryawanSales);

            InputMethodManager imm = (InputMethodManager) ListDataReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = ListDataReportSumaActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(ListDataReportSumaActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();

                    attantionReportPart.setVisibility(View.GONE);
                    reportRV.setVisibility(View.GONE);
                    loadingDataPartReport.setVisibility(View.VISIBLE);
                    noDataPartReport.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(categoryCode.equals("1")){
                                getData(categoryCode);
                            } else {
                                getData(subCategoryCode);
                            }

                        }
                    }, 1000);
                }
            }, 300);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if(sharedPrefAbsen.getSpReportCategoryActive().equals("")){
            reportRV.setVisibility(View.GONE);
            loadingDataPartReport.setVisibility(View.VISIBLE);
            noDataPartReport.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                    if(categoryCode.equals("1")){
                        getData(categoryCode);
                    } else {
                        getData(subCategoryCode);
                    }
                }
            }, 1000);
        } else {
            categoryCode = sharedPrefAbsen.getSpReportCategoryActive();
             if(categoryCode.equals("1")) {
                dateLabel.setText("Filter Tanggal Rencana :");
                categoryChoiceTV.setText("Rencana Kunjungan");

                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         refreshLayout.setRefreshing(false);
                         getData(categoryCode);
                     }
                }, 1000);
            } else {
                categoryCode = "0";
                subCategoryCode = sharedPrefAbsen.getSpReportCategoryActive().substring(sharedPrefAbsen.getSpReportCategoryActive().length()-1,sharedPrefAbsen.getSpReportCategoryActive().length());
                if(subCategoryCode.equals("2")){
                    dateLabel.setText("Filter Tanggal :");
                    categoryChoiceTV.setText("Aktivitas Kunjungan");
                    subCategoryChoiceTV.setText("Aktivitas Promosi");
                    dateChoice = getDate();
                } else if(subCategoryCode.equals("3")){
                    dateLabel.setText("Filter Tanggal :");
                    categoryChoiceTV.setText("Aktivitas");
                    subCategoryChoiceTV.setText("Aktivitas Penagihan");
                    dateChoice = getDate();
                } else if(subCategoryCode.equals("4")){
                    dateLabel.setText("Filter Tanggal :");
                    categoryChoiceTV.setText("Aktivitas");
                    subCategoryChoiceTV.setText("Aktivitas Pengiriman");
                    dateChoice = getDate();
                }

                 reportRV.setVisibility(View.GONE);
                 loadingDataPartReport.setVisibility(View.VISIBLE);
                 noDataPartReport.setVisibility(View.GONE);
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         refreshLayout.setRefreshing(false);
                         getData(subCategoryCode);
                     }
                 }, 1000);
            }

        }

    }

    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()) {
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}