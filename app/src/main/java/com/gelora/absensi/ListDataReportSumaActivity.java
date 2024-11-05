package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterKaryawanSales;
import com.gelora.absensi.adapter.AdapterSumaReport;
import com.gelora.absensi.adapter.AdapterWilayahSuma;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataReportSuma;
import com.gelora.absensi.model.KaryawanSales;
import com.gelora.absensi.model.WilayahSuma;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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

    LinearLayout refreshBTN, refreshDataPartReport, statistikPart2, statistikBTN2, statistikPart, statistikBTN, pameranBTN, jvBTN, njvBTN, wilayahBTN, filterWilayahBTN, subCatBTN, attantionReportPart, salesChoiceBTN, salesBTN, catBTN, filterBarPart, dateBTN, noDataPartReport, loadingDataPartReport, rencanaBTN, aktivitasBTN, penagihanBTN, pengirimanBTN, promosiBTN, markRencana, markAktivitas, markNjv, markJv, markPameran, markPenagihan, markPengiriman, markKunjungan, actionBar, backBTN, addBTN, filterCategoryBTN, filterSubCategoryBTN;
    TextView semuaSubkatBTN, semuaWilayahBTN, wilayahChoiceTV, semuaDataBTN, salesChoiceTV, choiceDateTV, categoryChoiceTV, subCategoryChoiceTV, dateLabel;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    BottomSheetLayout bottomSheet;
    String statusGL = "", categoryCode = "", subCategoryCode = "", dateStartChoice = getDate(), dateEndChoice = getDate();
    private RecyclerView reportRV;
    private DataReportSuma[] dataReportSumas;
    private AdapterSumaReport adapterSumaReport;
    EditText keywordKaryawanSales;
    RecyclerView karyawanSalesRV, wilayahRV;
    LinearLayout contentPart, startAttantionPart, noDataPart, loadingDataPart;
    private KaryawanSales[] karyawanSales;
    private WilayahSuma[] wilayahSumas;
    private AdapterWilayahSuma adapterWilayahSuma;
    private AdapterKaryawanSales adapterKaryawanSales;
    private Handler handler = new Handler();
    private static final String REQUEST_TAG = "LIST_DATA_REQUEST";

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
        filterCategoryBTN = findViewById(R.id.filter_category_btn);
        filterSubCategoryBTN = findViewById(R.id.filter_sub_category_btn);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        categoryChoiceTV = findViewById(R.id.category_choice_tv);
        subCategoryChoiceTV = findViewById(R.id.sub_category_choice_tv);
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
        wilayahBTN = findViewById(R.id.wilayah_btn);
        filterWilayahBTN = findViewById(R.id.filter_wilayah_btn);
        wilayahChoiceTV = findViewById(R.id.wilayah_choice_tv);
        statistikBTN = findViewById(R.id.statistik_btn);
        statistikBTN2 = findViewById(R.id.statistik_btn_2);
        statistikPart = findViewById(R.id.statistik_part);
        statistikPart2 = findViewById(R.id.statistik_part_2);
        refreshDataPartReport = findViewById(R.id.refresh_data_part_report);
        refreshBTN = findViewById(R.id.refresh_btn);

        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();
        LocalBroadcastManager.getInstance(this).registerReceiver(wilayahSumaBroad, new IntentFilter("wilayah_suma_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(karyawanSalesBroad, new IntentFilter("karyawan_sales_broad"));

        catBTN.setVisibility(View.VISIBLE);
        dateBTN.setVisibility(View.VISIBLE);
        if(sharedPrefManager.getSpNik().equals("2151010115")         // Ibu Deborah
                || sharedPrefManager.getSpNik().equals("1504060711") // Ibu There
                || sharedPrefManager.getSpNik().equals("0121010900") // Bapak Nurcahyo
                || sharedPrefManager.getSpNik().equals("0981010210") // Ibu Ranti
                || sharedPrefManager.getSpNik().equals("3436170924") // Bapak Nico
                || sharedPrefManager.getSpNik().equals("0552260707") // Ibu Dina
                || sharedPrefManager.getSpNik().equals("3318060323") // Bapak Dominggus
                || sharedPrefManager.getSpNik().equals("0499070507") // Bapak Dawud
                || sharedPrefManager.getSpNik().equals("1405020311") // Bapak Panda
                || sharedPrefManager.getSpNik().equals("3320130323") // Bapak Daniel
                || sharedPrefManager.getSpIdJabatan().equals("43")   // GL Suma 1
                || sharedPrefManager.getSpIdJabatan().equals("45")   // GL Suma 2
                || sharedPrefManager.getSpIdJabatan().equals("47")   // GL Suma 3
                || sharedPrefManager.getSpIdJabatan().equals("49")   // GL Suma Semarang
                || sharedPrefManager.getSpIdJabatan().equals("51")   // GL Suma Bandung
                || sharedPrefManager.getSpIdJabatan().equals("53")   // GL Suma Surabaya
                || sharedPrefManager.getSpIdJabatan().equals("55")   // GL Suma Purwakarta
                || sharedPrefManager.getSpIdJabatan().equals("87")   // GL Suma Palembang
                || sharedPrefManager.getSpIdJabatan().equals("57")){ // GL AE
            if(sharedPrefManager.getSpIdJabatan().equals("43") || sharedPrefManager.getSpIdJabatan().equals("45") || sharedPrefManager.getSpIdJabatan().equals("47") || sharedPrefManager.getSpIdJabatan().equals("49") || sharedPrefManager.getSpIdJabatan().equals("51") || sharedPrefManager.getSpIdJabatan().equals("53") || sharedPrefManager.getSpIdJabatan().equals("55") || sharedPrefManager.getSpIdJabatan().equals("87") || sharedPrefManager.getSpIdJabatan().equals("57")){ //GL
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, sharedPrefManager.getSpNama());
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, sharedPrefManager.getSpNik());
                addBTN.setVisibility(View.VISIBLE);
                wilayahBTN.setVisibility(View.GONE);
                salesChoiceTV.setText(sharedPrefManager.getSpNama());
                statusGL = "1";
            } else { // Management
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_WILAYAH_SUMA, "");
                addBTN.setVisibility(View.VISIBLE);
                wilayahBTN.setVisibility(View.VISIBLE);
                salesChoiceTV.setText("Semua Sales");
            }
            salesBTN.setVisibility(View.VISIBLE);
        } else {
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, sharedPrefManager.getSpNama());
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, sharedPrefManager.getSpNik());
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_WILAYAH_SUMA, "");
            wilayahBTN.setVisibility(View.GONE);
            salesChoiceTV.setText(sharedPrefAbsen.getSpSalesActive());
            salesBTN.setVisibility(View.GONE);
            addBTN.setVisibility(View.VISIBLE);
        }
        categoryCode = "1";
        dateLabel.setText("Tanggal Rencana :");
        categoryChoiceTV.setText("Rencana Kunjungan");
        wilayahChoiceTV.setText("Semua Wilayah");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "1");
        subCatBTN.setVisibility(View.GONE);

        reportRV.setLayoutManager(new LinearLayoutManager(this));
        reportRV.setHasFixedSize(true);
        reportRV.setNestedScrollingEnabled(false);
        reportRV.setItemAnimator(new DefaultItemAnimator());

        getDateNow();

        if(sharedPrefManager.getSpNik().equals("1405020311")||sharedPrefManager.getSpNik().equals("0499070507")||sharedPrefManager.getSpNik().equals("1504060711")||sharedPrefManager.getSpNik().equals("2151010115")||sharedPrefManager.getSpNik().equals("0981010210")||sharedPrefManager.getSpNik().equals("0121010900")||sharedPrefManager.getSpNik().equals("3318060323")||sharedPrefManager.getSpNik().equals("0552260707")||sharedPrefManager.getSpNik().equals("3436170924")){
            statistikPart.setVisibility(View.VISIBLE);
            statistikPart2.setVisibility(View.GONE);
        } else {
            statistikPart.setVisibility(View.GONE);
            statistikPart2.setVisibility(View.VISIBLE);
        }

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
                refreshDataPartReport.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("1")){
                                    getData(categoryCode);
                                } else {
                                    getData(subCategoryCode);
                                }
                            }
                        }).start();
                    }
                }, 0);
            }
        });

        refreshBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attantionReportPart.setVisibility(View.GONE);
                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                refreshDataPartReport.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("1")){
                                    getData(categoryCode);
                                } else {
                                    getData(subCategoryCode);
                                }
                            }
                        }).start();
                    }
                }, 0);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        statistikBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataReportSumaActivity.this, VisitStatisticActivity.class);
                startActivity(intent);
            }
        });

        statistikBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataReportSumaActivity.this, SaleStatisticActivity.class);
                startActivity(intent);
            }
        });

        statistikPart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataReportSumaActivity.this, VisitStatisticActivity.class);
                startActivity(intent);
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
                //datePicker();
                materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        long startDate = ((Pair<Long, Long>) selection).first;
                        long endDate = ((Pair<Long, Long>) selection).second;
                        Date date1 = new Date(startDate);
                        Date date2 = new Date(endDate);
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf_show = new SimpleDateFormat("dd/MM/yyyy");
                        String tanggalMulai = sdf.format(date1);
                        String tanggalMulaiShow = sdf_show.format(date1);
                        String tanggalAkhir = sdf.format(date2);
                        String tanggalAkhirShow = sdf_show.format(date2);
                        choiceDateTV.setText(tanggalMulaiShow+"  -  "+tanggalAkhirShow);
                        dateStartChoice = tanggalMulai;
                        dateEndChoice = tanggalAkhir;

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(categoryCode.equals("1")){
                                            getData(categoryCode);
                                        } else {
                                            getData(subCategoryCode);
                                        }
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                });
            }
        });

        filterWilayahBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wilayahPicker();
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
        refreshDataPartReport.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(sharedPrefManager.getSpNik().equals("0499070507")){
                            getData(subCategoryCode);
                        } else {
                            getData(categoryCode);
                        }
                    }
                }).start();
            }
        }, 0);

    }

    @SuppressLint("SetTextI18n")
    private void getDateNow(){
        dateStartChoice = getDate();
        dateEndChoice = getDate();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf_show = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date start = sdf.parse(dateStartChoice);
            Date end = sdf.parse(dateEndChoice);
            String tanggalMulai = sdf.format(start);
            String tanggalMulaiShow = sdf_show.format(start);
            String tanggalAkhir = sdf.format(end);
            String tanggalAkhirShow = sdf_show.format(end);
            choiceDateTV.setText(tanggalMulaiShow+"  -  "+tanggalAkhirShow);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void getData(String category_code) {
        String baseUrl = "https://reporting.sumasistem.co.id/api/suma_report_list";
        String nikSalesActive = sharedPrefAbsen.getSpNikSalesActive().isEmpty() ? "0" : sharedPrefAbsen.getSpNikSalesActive();
        String url = String.format(
                "%s?nik=%s&tipe_laporan=%s&tanggal_mulai=%s&tanggal_akhir=%s&wilayah=%s&requester=%s",
                baseUrl,
                nikSalesActive,
                category_code,
                dateStartChoice,
                dateEndChoice,
                sharedPrefAbsen.getSpWilayahSuma(),
                sharedPrefManager.getSpNik()
        );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("ParseJSON", response.toString());
                    parseAndDisplayData(response);
                },
                error -> {
                    error.printStackTrace();
                    showErrorMessage("Gagal terhubung, harap periksa koneksi internet atau jaringan Anda");
                }
        );

        request.setTag(REQUEST_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void parseAndDisplayData(JSONObject response) {
        new Thread(() -> {
            try {
                String status = response.getString("status");
                runOnUiThread(() -> {
                    if ("Success".equals(status)) {
                        displayData(response);
                    } else {
                        showNoDataAvailable();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void displayData(JSONObject response) {
        try {
            String dataReport = response.getString("data");
            Gson gson = new Gson();
            dataReportSumas = gson.fromJson(dataReport, DataReportSuma[].class);
            if (adapterSumaReport == null) {
                adapterSumaReport = new AdapterSumaReport(dataReportSumas, this);
                reportRV.setAdapter(adapterSumaReport);
            } else {
                adapterSumaReport.updateData(dataReportSumas);
            }

            attantionReportPart.setVisibility(View.GONE);
            reportRV.setVisibility(View.VISIBLE);
            loadingDataPartReport.setVisibility(View.GONE);
            noDataPartReport.setVisibility(View.GONE);
            refreshDataPartReport.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
            showNoDataAvailable();
        }
    }

    private void showErrorMessage(String message) {
        attantionReportPart.setVisibility(View.GONE);
        reportRV.setVisibility(View.GONE);
        loadingDataPartReport.setVisibility(View.GONE);
        noDataPartReport.setVisibility(View.VISIBLE);
        refreshDataPartReport.setVisibility(View.GONE);

        try {
            new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Perhatian")
                    .setContentText(message)
                    .setConfirmText("    OK    ")
                    .setConfirmClickListener(KAlertDialog::dismiss)
                    .show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("Error", "Error: " + e.toString());
        }
    }

    private void showNoDataAvailable() {
        attantionReportPart.setVisibility(View.GONE);
        reportRV.setVisibility(View.GONE);
        loadingDataPartReport.setVisibility(View.GONE);
        noDataPartReport.setVisibility(View.VISIBLE);
        refreshDataPartReport.setVisibility(View.GONE);
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
            dateLabel.setText("Tanggal Rencana :");
            subCatBTN.setVisibility(View.GONE);
            markRencana.setVisibility(View.VISIBLE);
            markAktivitas.setVisibility(View.GONE);
            rencanaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
            aktivitasBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(categoryCode.equals("0")) {
            dateLabel.setText("Tanggal :");
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
                dateLabel.setText("Tanggal Rencana :");
                categoryCode = "1";
                categoryChoiceTV.setText("Rencana Kunjungan");

                subCatBTN.setVisibility(View.GONE);
                markRencana.setVisibility(View.VISIBLE);
                markAktivitas.setVisibility(View.GONE);
                rencanaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                aktivitasBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(categoryCode);
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);

            }
        });

        aktivitasBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "0");
                dateLabel.setText("Tanggal :");
                categoryCode = "0";
                subCategoryCode = "99";
                categoryChoiceTV.setText("Aktivitas Kunjungan");
                subCategoryChoiceTV.setText("Semua Sub Kategori");

                subCatBTN.setVisibility(View.VISIBLE);
                markRencana.setVisibility(View.GONE);
                markAktivitas.setVisibility(View.VISIBLE);
                rencanaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                aktivitasBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(categoryCode.equals("0")){
                                            getData(subCategoryCode);
                                        } else {
                                            getData(categoryCode);
                                        }
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void subCategoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ListDataReportSumaActivity.this).inflate(R.layout.layout_sub_kategori_report_suma_list_data, bottomSheet, false));
        semuaSubkatBTN = findViewById(R.id.semua_subkat_btn);
        promosiBTN = findViewById(R.id.promosi_btn);
        pengirimanBTN = findViewById(R.id.pengiriman_btn);
        penagihanBTN = findViewById(R.id.penagihan_btn);
        njvBTN = findViewById(R.id.njv_btn);
        jvBTN = findViewById(R.id.jv_btn);
        pameranBTN = findViewById(R.id.pameran_btn);
        markKunjungan = findViewById(R.id.mark_pesanan);
        markPengiriman = findViewById(R.id.mark_pengiriman);
        markPenagihan = findViewById(R.id.mark_penagihan);
        markNjv = findViewById(R.id.mark_njv);
        markJv = findViewById(R.id.mark_jv);
        markPameran = findViewById(R.id.mark_pameran);

        if(subCategoryCode.equals("99")) {
            dateLabel.setText("Tanggal :");
            markKunjungan.setVisibility(View.GONE);
            markPengiriman.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            markNjv.setVisibility(View.GONE);
            markJv.setVisibility(View.GONE);
            markPameran.setVisibility(View.GONE);
            promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(subCategoryCode.equals("2")) {
           dateLabel.setText("Tanggal :");
           markKunjungan.setVisibility(View.VISIBLE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.GONE);
           markNjv.setVisibility(View.GONE);
           markJv.setVisibility(View.GONE);
           markPameran.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
       } else if(subCategoryCode.equals("3")) {
           dateLabel.setText("Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.VISIBLE);
           markNjv.setVisibility(View.GONE);
           markJv.setVisibility(View.GONE);
           markPameran.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
       } else if(subCategoryCode.equals("4")) {
           dateLabel.setText("Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.VISIBLE);
           markPenagihan.setVisibility(View.GONE);
           markNjv.setVisibility(View.GONE);
           markJv.setVisibility(View.GONE);
           markPameran.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
       } else if(subCategoryCode.equals("5")) {
           dateLabel.setText("Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.GONE);
           markNjv.setVisibility(View.VISIBLE);
           markJv.setVisibility(View.GONE);
           markPameran.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
       } else if(subCategoryCode.equals("6")) {
           dateLabel.setText("Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.GONE);
           markNjv.setVisibility(View.GONE);
           markJv.setVisibility(View.VISIBLE);
           markPameran.setVisibility(View.GONE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           jvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
           pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
       } else if(subCategoryCode.equals("7")) {
           dateLabel.setText("Tanggal :");
           markKunjungan.setVisibility(View.GONE);
           markPengiriman.setVisibility(View.GONE);
           markPenagihan.setVisibility(View.GONE);
           markNjv.setVisibility(View.GONE);
           markJv.setVisibility(View.GONE);
           markPameran.setVisibility(View.VISIBLE);
           promosiBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pengirimanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           njvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           jvBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
           pameranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
       }

        semuaSubkatBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dateLabel.setText("Tanggal :");
                subCategoryCode = "99";
                subCategoryChoiceTV.setText("Semua Sub Kategori");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markNjv.setVisibility(View.GONE);
                markJv.setVisibility(View.GONE);
                markPameran.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);
            }
        });

        promosiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "0");
                dateLabel.setText("Tanggal :");
                subCategoryCode = "2";
                subCategoryChoiceTV.setText("Promosi");
                markKunjungan.setVisibility(View.VISIBLE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markNjv.setVisibility(View.GONE);
                markJv.setVisibility(View.GONE);
                markPameran.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);
            }
        });

        penagihanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "3");
                dateLabel.setText("Tanggal :");
                subCategoryCode = "3";
                subCategoryChoiceTV.setText("Penagihan");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.VISIBLE);
                markNjv.setVisibility(View.GONE);
                markJv.setVisibility(View.GONE);
                markPameran.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);

            }
        });

        pengirimanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "4");
                dateLabel.setText("Tanggal :");
                subCategoryCode = "4";
                subCategoryChoiceTV.setText("Pengiriman");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.VISIBLE);
                markPenagihan.setVisibility(View.GONE);
                markNjv.setVisibility(View.GONE);
                markJv.setVisibility(View.GONE);
                markPameran.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);

                    }
                }, 300);

            }
        });

        njvBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "4");
                dateLabel.setText("Tanggal :");
                subCategoryCode = "5";
                subCategoryChoiceTV.setText("Non Join Visit");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markNjv.setVisibility(View.VISIBLE);
                markJv.setVisibility(View.GONE);
                markPameran.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);

                    }
                }, 300);

            }
        });

        jvBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "4");
                dateLabel.setText("Tanggal :");
                subCategoryCode = "6";
                subCategoryChoiceTV.setText("Join Visit");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markNjv.setVisibility(View.GONE);
                markJv.setVisibility(View.VISIBLE);
                markPameran.setVisibility(View.GONE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);

                    }
                }, 300);

            }
        });

        pameranBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "4");
                dateLabel.setText("Tanggal :");
                subCategoryCode = "7";
                subCategoryChoiceTV.setText("Pameran");
                markKunjungan.setVisibility(View.GONE);
                markPengiriman.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markNjv.setVisibility(View.GONE);
                markJv.setVisibility(View.GONE);
                markPameran.setVisibility(View.VISIBLE);
                promosiBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pengirimanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                njvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                jvBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pameranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }, 0);

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

                dateStartChoice = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String input_date = dateStartChoice;
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
                refreshDataPartReport.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("1")){
                                    getData(categoryCode);
                                } else {
                                    getData(subCategoryCode);
                                }
                            }
                        }).start();
                    }
                }, 0);

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(ListDataReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateStartChoice = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String input_date = dateStartChoice;
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
                refreshDataPartReport.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("1")){
                                    getData(categoryCode);
                                } else {
                                    getData(subCategoryCode);
                                }
                            }
                        }).start();
                    }
                }, 0);

            }, y,m-1,d);
            dpd.show();
        }

    }

    private void wilayahPicker(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_wilayah_suma, bottomSheet, false));
        wilayahRV = findViewById(R.id.wilayah_rv);
        semuaWilayahBTN = findViewById(R.id.semua_wilayah_btn);

        try {
            wilayahRV.setLayoutManager(new LinearLayoutManager(this));
            wilayahRV.setHasFixedSize(true);
            wilayahRV.setNestedScrollingEnabled(false);
            wilayahRV.setItemAnimator(new DefaultItemAnimator());

            getWilayah();
        } catch (NullPointerException e){
            Log.e("Error", e.toString());
        }

        semuaWilayahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                wilayahChoiceTV.setText("Semua Wilayah");
                salesChoiceTV.setText("Semua Sales");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_WILAYAH_SUMA, "");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(categoryCode.equals("1")){
                                            getData(categoryCode);
                                        } else {
                                            getData(subCategoryCode);
                                        }
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);
            }
        });
    }

    private void getWilayah() {
        final String url = "https://reporting.sumasistem.co.id/api/get_wilayah?asmen="+sharedPrefManager.getSpNik();
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
                            adapterWilayahSuma = new AdapterWilayahSuma(wilayahSumas,ListDataReportSumaActivity.this);
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
                bottomSheet.dismissSheet();
                connectionFailed();
            }
        });

        requestQueue.add(request);

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
        semuaDataBTN = findViewById(R.id.semua_data_btn);

        if(statusGL.equals("1")){
            semuaDataBTN.setVisibility(View.GONE);
        } else {
            semuaDataBTN.setVisibility(View.VISIBLE);
        }

        karyawanSalesRV.setLayoutManager(new LinearLayoutManager(this));
        karyawanSalesRV.setHasFixedSize(true);
        karyawanSalesRV.setNestedScrollingEnabled(false);
        karyawanSalesRV.setItemAnimator(new DefaultItemAnimator());

        handler.postDelayed(new Runnable() {
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSales(keyWordSearch);
                        }
                    }, 500);
                } else {
                    handler.postDelayed(new Runnable() {
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
                wilayahChoiceTV.setText("Semua Wilayah");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NIK_SALES_ACTIVE, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_WILAYAH_SUMA, "");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        attantionReportPart.setVisibility(View.GONE);
                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        refreshDataPartReport.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(categoryCode.equals("1")){
                                            getData(categoryCode);
                                        } else {
                                            getData(subCategoryCode);
                                        }
                                    }
                                }).start();
                            }
                        }, 0);
                    }
                }, 300);
            }
        });

    }

    private void getSales(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cari_karyawan_sales";
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

            InputMethodManager imm = (InputMethodManager) ListDataReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = ListDataReportSumaActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(ListDataReportSumaActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();

                    attantionReportPart.setVisibility(View.GONE);
                    reportRV.setVisibility(View.GONE);
                    loadingDataPartReport.setVisibility(View.VISIBLE);
                    noDataPartReport.setVisibility(View.GONE);
                    refreshDataPartReport.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(categoryCode.equals("1")){
                                        getData(categoryCode);
                                    } else {
                                        getData(subCategoryCode);
                                    }
                                }
                            }).start();
                        }
                    }, 0);
                }
            }, 300);
        }
    };

    public BroadcastReceiver karyawanSalesBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nikKaryawanSales = intent.getStringExtra("nik_karyawan_sales");
            String namaKaryawanSales = intent.getStringExtra("nama_karyawan_sales");
            String wilayahSales = intent.getStringExtra("wilayah_sales");

            salesChoiceTV.setText(namaKaryawanSales);

            if(wilayahSales.equals("1")){
                wilayahChoiceTV.setText("Suma Jakarta 1");
            } else if(wilayahSales.equals("2")){
                wilayahChoiceTV.setText("Suma Jakarta 2");
            } else if(wilayahSales.equals("3")){
                wilayahChoiceTV.setText("Suma Jakarta 3");
            } else if(wilayahSales.equals("4")){
                wilayahChoiceTV.setText("Suma Bandung");
            } else if(wilayahSales.equals("5")){
                wilayahChoiceTV.setText("Suma Semarang");
            } else if(wilayahSales.equals("6")){
                wilayahChoiceTV.setText("Suma Surabaya");
            } else if(wilayahSales.equals("7")){
                wilayahChoiceTV.setText("Jakarta AE");
            } else if(wilayahSales.equals("8")){
                wilayahChoiceTV.setText("Akunting dan Keuangan");
            }

            InputMethodManager imm = (InputMethodManager) ListDataReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = ListDataReportSumaActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(ListDataReportSumaActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();

                    attantionReportPart.setVisibility(View.GONE);
                    reportRV.setVisibility(View.GONE);
                    loadingDataPartReport.setVisibility(View.VISIBLE);
                    noDataPartReport.setVisibility(View.GONE);
                    refreshDataPartReport.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if(categoryCode.equals("1")){
                                        getData(categoryCode);
                                    } else {
                                        getData(subCategoryCode);
                                    }
                                }
                            }).start();
                        }
                    }, 0);
                }
            }, 300);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if(sharedPrefAbsen.getSpReloadRequest().equals("true")){
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_RELOAD_REQUEST, "");
            if(sharedPrefAbsen.getSpReportCategoryActive().equals("")){
                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                refreshDataPartReport.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(categoryCode.equals("1")){
                                    getData(categoryCode);
                                } else {
                                    getData("0");
                                }
                            }
                        }).start();
                    }
                }, 0);
            } else {
                categoryCode = sharedPrefAbsen.getSpReportCategoryActive();
                if(categoryCode.equals("1")) {
                    dateLabel.setText("Tanggal Rencana :");
                    categoryChoiceTV.setText("Rencana Kunjungan");

                    reportRV.setVisibility(View.GONE);
                    loadingDataPartReport.setVisibility(View.VISIBLE);
                    noDataPartReport.setVisibility(View.GONE);
                    refreshDataPartReport.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getData(categoryCode);
                                }
                            }).start();
                        }
                    }, 0);
                } else {
                    categoryCode = "0";
                    subCategoryCode = sharedPrefAbsen.getSpReportCategoryActive().substring(sharedPrefAbsen.getSpReportCategoryActive().length()-1,sharedPrefAbsen.getSpReportCategoryActive().length());
                    if(subCategoryCode.equals("2")){
                        dateLabel.setText("Tanggal :");
                        categoryChoiceTV.setText("Aktivitas Kunjungan");
                        subCategoryChoiceTV.setText("Promosi");
                        dateStartChoice = getDate();
                        dateEndChoice = getDate();
                    } else if(subCategoryCode.equals("3")){
                        dateLabel.setText("Tanggal :");
                        categoryChoiceTV.setText("Aktivitas Kunjungan");
                        subCategoryChoiceTV.setText("Penagihan");
                        dateStartChoice = getDate();
                        dateEndChoice = getDate();
                    } else if(subCategoryCode.equals("4")){
                        dateLabel.setText("Tanggal :");
                        categoryChoiceTV.setText("Aktivitas Kunjungan");
                        subCategoryChoiceTV.setText("Pengiriman");
                        dateStartChoice = getDate();
                        dateEndChoice = getDate();
                    } else if(subCategoryCode.equals("5")){
                        dateLabel.setText("Tanggal :");
                        categoryChoiceTV.setText("Aktivitas Kunjungan");
                        subCategoryChoiceTV.setText("Non Join Visit");
                        dateStartChoice = getDate();
                        dateEndChoice = getDate();
                    }

                    reportRV.setVisibility(View.GONE);
                    loadingDataPartReport.setVisibility(View.VISIBLE);
                    noDataPartReport.setVisibility(View.GONE);
                    refreshDataPartReport.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(subCategoryChoiceTV.getText().toString().equals("Promosi")){
                                categoryCode = "0";
                                subCategoryCode = "2";
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            } else if(subCategoryChoiceTV.getText().toString().equals("Penagihan")){
                                categoryCode = "0";
                                subCategoryCode = "3";
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            } else if(subCategoryChoiceTV.getText().toString().equals("Pengiriman")){
                                categoryCode = "0";
                                subCategoryCode = "4";
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            } else if(subCategoryChoiceTV.getText().toString().equals("Non Join Visit")){
                                categoryCode = "0";
                                subCategoryCode = "5";
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData(subCategoryCode);
                                    }
                                }).start();
                            }
                        }
                    }, 0);
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (requestQueue != null) {
            if (dataReportSumas == null || dataReportSumas.length == 0) {
                requestQueue.cancelAll(REQUEST_TAG);

                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.GONE);
                noDataPartReport.setVisibility(View.GONE);
                refreshDataPartReport.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            if (dataReportSumas == null || dataReportSumas.length == 0) {
                requestQueue.cancelAll(REQUEST_TAG);

                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.GONE);
                noDataPartReport.setVisibility(View.GONE);
                refreshDataPartReport.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (requestQueue != null) {
            if (dataReportSumas == null || dataReportSumas.length == 0) {
                requestQueue.cancelAll(REQUEST_TAG);

                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.GONE);
                noDataPartReport.setVisibility(View.GONE);
                refreshDataPartReport.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()) {
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
            if (requestQueue != null) {
                if (dataReportSumas == null || dataReportSumas.length == 0) {
                    requestQueue.cancelAll(REQUEST_TAG);
                }
            }
        }
    }

}