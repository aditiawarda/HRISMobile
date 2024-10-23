package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterListDataVisitStatisticSales;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.SalesVisitStatistic;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VisitStatisticActivity extends AppCompatActivity {

    PieChart pieChart;
    RelativeLayout pieChartPart;
    LinearLayout submitkomplainBTN, komplainBTN, connectBTN, closeBTN, tryWarningBTN, loadingDataSalesPart, noDataSalesPart, noDataPart, bulanBTN, loadingDataPart, backBTN, actionBar;
    TextView titleSalesListTV, bulanPilihTV, totalVisitTV;
    RecyclerView listSalesRV;
    private SalesVisitStatistic[] salesVisitStatistics;
    AdapterListDataVisitStatisticSales adapterListDataVisitStatisticSales;
    RequestQueue requestQueue;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    String selectMonth = getMonth();
    EditText searchInput, komplainED;
    private Handler handler = new Handler();
    private boolean isActivityOpened = false;
    BottomSheetLayout bottomSheetLayout;
    ExpandableLayout komplainField;
    KAlertDialog pDialog;
    private int i = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_statistic);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        pieChart = findViewById(R.id.piechart);
        loadingDataPart = findViewById(R.id.loading_data_part);
        bulanBTN = findViewById(R.id.bulan_btn);
        bulanPilihTV = findViewById(R.id.bulan_pilih);
        noDataPart = findViewById(R.id.no_data_part);
        pieChartPart = findViewById(R.id.piechart_part);
        totalVisitTV = findViewById(R.id.total_visit_tv);
        listSalesRV = findViewById(R.id.list_sales_rv);
        noDataSalesPart = findViewById(R.id.no_data_sales_part);
        loadingDataSalesPart = findViewById(R.id.loading_data_sales_part);
        titleSalesListTV = findViewById(R.id.title_sales_list);
        searchInput = findViewById(R.id.keyword_sales_ed);
        tryWarningBTN = findViewById(R.id.try_warning_btn);
        bottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        komplainBTN = findViewById(R.id.komplain_btn);
        komplainField = findViewById(R.id.komplain_field);
        komplainED = findViewById(R.id.komplain_ed);
        submitkomplainBTN = findViewById(R.id.submit_komplain_btn);

        searchInput.setVisibility(View.GONE);

        listSalesRV.setLayoutManager(new LinearLayoutManager(this));
        listSalesRV.setHasFixedSize(true);
        listSalesRV.setNestedScrollingEnabled(false);
        listSalesRV.setItemAnimator(new DefaultItemAnimator());

        LocalBroadcastManager.getInstance(this).registerReceiver(toDetail, new IntentFilter("to_detail"));

        String bulan = getMonthOnly(), bulanName = "";
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

        bulanPilihTV.setText(bulanName+" "+getYearOnly());
        titleSalesListTV.setText("Statistik Kunjungan Sales Bulan "+bulanPilihTV.getText().toString());

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                String bulan = getMonthOnly(), bulanName = "";
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

                selectMonth = getMonth();

                bulanPilihTV.setText(bulanName+" "+getYearOnly());
                titleSalesListTV.setText("Statistik Kunjungan Sales Bulan "+bulanPilihTV.getText().toString());

                loadingDataPart.setVisibility(View.VISIBLE);
                pieChartPart.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);

                searchInput.setVisibility(View.GONE);
                listSalesRV.setVisibility(View.GONE);
                loadingDataSalesPart.setVisibility(View.VISIBLE);
                noDataSalesPart.setVisibility(View.GONE);

                getTryWarning();
                getPieCart();

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

        submitkomplainBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(komplainED.getText().toString().equals("")){
                    new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Kolom kompain belum terisi")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Kirim komplain anda sekarang?")
                            .setCancelText("TIDAK")
                            .setConfirmText("   YA   ")
                            .showCancelButton(true)
                            .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();

                                    pDialog = new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1000, 500) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (VisitStatisticActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (VisitStatisticActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (VisitStatisticActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (VisitStatisticActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (VisitStatisticActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (VisitStatisticActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }

                                        public void onFinish() {
                                            i = -1;
                                            kirimKomplain(komplainED.getText().toString());
                                        }
                                    }.start();

                                }
                            })
                            .show();
                }
            }
        });

        bulanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date date = sdf.parse(selectMonth);
                    now.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(VisitStatisticActivity.this,
                    new MonthPickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(int month, int year) {
                            String bulan = "", bulanName = "";
                            if(month==0){
                                bulan = "01";
                                bulanName = "Januari";
                            } else if (month==1){
                                bulan = "02";
                                bulanName = "Februari";
                            } else if (month==2){
                                bulan = "03";
                                bulanName = "Maret";
                            } else if (month==3){
                                bulan = "04";
                                bulanName = "April";
                            } else if (month==4){
                                bulan = "05";
                                bulanName = "Mei";
                            } else if (month==5){
                                bulan = "06";
                                bulanName = "Juni";
                            } else if (month==6){
                                bulan = "07";
                                bulanName = "Juli";
                            } else if (month==7){
                                bulan = "08";
                                bulanName = "Agustus";
                            } else if (month==8){
                                bulan = "09";
                                bulanName = "September";
                            } else if (month==9){
                                bulan = "10";
                                bulanName = "Oktober";
                            } else if (month==10){
                                bulan = "11";
                                bulanName = "November";
                            } else if (month==11){
                                bulan = "12";
                                bulanName = "Desember";
                            }

                            selectMonth = String.valueOf(year)+"-"+bulan;

                            loadingDataPart.setVisibility(View.VISIBLE);
                            pieChartPart.setVisibility(View.GONE);
                            noDataPart.setVisibility(View.GONE);

                            searchInput.setVisibility(View.GONE);
                            listSalesRV.setVisibility(View.GONE);
                            loadingDataSalesPart.setVisibility(View.VISIBLE);
                            noDataSalesPart.setVisibility(View.GONE);

                            bulanPilihTV.setText(bulanName+" "+String.valueOf(year));
                            titleSalesListTV.setText("Statistik Kunjungan Sales Bulan "+bulanPilihTV.getText().toString());

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getPieCart();
                                }
                            }, 0);

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

        komplainBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(komplainField.isExpanded()){
                    komplainField.collapse();
                } else {
                    komplainField.expand();
                }
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(!s.toString().isEmpty()){
                        adapterListDataVisitStatisticSales.filterByName(s.toString());
                    }
                } catch (NullPointerException e) {
                    Log.e("Error", e.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void kirimKomplain(String komplain){
        final String url = "https://reporting.sumasistem.co.id/api/insert_statistic_complain";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                Log.d("Success.Response", response.toString());
                                JSONObject data = new JSONObject(response);
                                String status = data.getString("status");
                                if(status.equals("Success")){
                                    komplainED.setText("");
                                    komplainED.clearFocus();
                                    komplainField.collapse();
                                    pDialog.setTitleText("Berhasil Terkirim")
                                            .setContentText("Ajuan komplain berhasil terkirim")
                                            .setConfirmText("    OK    ")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                }
                                            })
                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                } else {
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                            pDialog.setTitleText("Perhatian")
                                    .setContentText("Terjadi kesalahan!")
                                    .setConfirmText("TUTUP")
                                    .changeAlertType(KAlertDialog.ERROR_TYPE);
                        }
                    }
            )
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("id_sales", sharedPrefManager.getSpNik());
                    params.put("komplain", komplain);
                    return params;
                }
            };

            DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                    0,
                    -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(retryPolicy);

            requestQueue.add(postRequest);

    }

    private void tryWarning(){
        bottomSheetLayout.showWithSheetView(LayoutInflater.from(VisitStatisticActivity.this).inflate(R.layout.layout_try_feature, bottomSheetLayout, false));
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
                                            new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.WARNING_TYPE)
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

    private void getPieCart(){
        String url = "https://reporting.sumasistem.co.id/api/get_visit_statistic?month="+selectMonth;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                JSONObject totalPerWilayah = response.getJSONObject("total_per_wilayah");
                                int jakarta1 = totalPerWilayah.getInt("Jakarta 1");
                                int jakarta2 = totalPerWilayah.getInt("Jakarta 2");
                                int jakarta3 = totalPerWilayah.getInt("Jakarta 3");
                                int bandung  = totalPerWilayah.getInt("Bandung");
                                int semarang = totalPerWilayah.getInt("Semarang");
                                int surabaya = totalPerWilayah.getInt("Surabaya");
                                int jakartaae = totalPerWilayah.getInt("Jakarta AE");
                                int akuntingKeuangan = totalPerWilayah.getInt("Akunting dan Keuangan");
                                int totalKeseluruhan = response.getInt("total_keseluruhan");

                                totalVisitTV.setText("Total Kunjungan : "+String.valueOf(totalKeseluruhan));

                                ArrayList<PieEntry> entries = new ArrayList<>();
                                entries.add(new PieEntry(jakarta1, "Suma Jakarta 1"));
                                entries.add(new PieEntry(jakarta2, "Suma Jakarta 2"));
                                entries.add(new PieEntry(jakarta3, "Suma Jakarta 3"));
                                entries.add(new PieEntry(bandung, "Suma Bandung"));
                                entries.add(new PieEntry(semarang, "Suma Semarang"));
                                entries.add(new PieEntry(surabaya, "Suma Surabaya"));
                                entries.add(new PieEntry(jakartaae, "Jakarta AE"));
                                entries.add(new PieEntry(akuntingKeuangan, "Akunting dan Keuangan"));

                                PieDataSet dataSet = new PieDataSet(entries, "");
                                dataSet.setValueTextSize(25f);

                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#FFFF8A65")); // Jakarta 1
                                colors.add(Color.parseColor("#FF80E27E")); // Jakarta 2
                                colors.add(Color.parseColor("#FF82B1FF")); // Jakarta 3
                                colors.add(Color.parseColor("#FFFFF176")); // Bandung
                                colors.add(Color.parseColor("#FFEA80FC")); // Semarang
                                colors.add(Color.parseColor("#FFFFD180")); // Surabaya
                                colors.add(Color.parseColor("#FFFF867C")); // Jakarta AE
                                colors.add(Color.parseColor("#FF9FA8DA")); // Akunting dan Keuangan
                                dataSet.setColors(colors);

                                PieData data = new PieData(dataSet);
                                data.setValueTextSize(16f);
                                data.setValueTextColor(Color.BLACK);
                                data.setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {
                                        return String.valueOf((int) value);
                                    }
                                });
                                pieChart.setData(data);
                                pieChart.setDrawHoleEnabled(false);
                                pieChart.setUsePercentValues(false);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setEntryLabelTextSize(12f);
                                pieChart.setEntryLabelColor(Color.BLACK);
                                pieChart.animateY(1000);
                                pieChart.getLegend().setEnabled(false);

                                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {
                                        PieEntry pieEntry = (PieEntry) e;
                                        String label = pieEntry.getLabel();
                                        float value = pieEntry.getValue();

                                        String wilayah = "";
                                        if(label.equals("Suma Jakarta 1")){
                                            wilayah = "Jakarta 1";
                                        } else if(label.equals("Suma Jakarta 2")){
                                            wilayah = "Jakarta 2";
                                        } else if(label.equals("Suma Jakarta 3")){
                                            wilayah = "Jakarta 3";
                                        } else if(label.equals("Suma Bandung")){
                                            wilayah = "Bandung";
                                        } else if(label.equals("Suma Semarang")){
                                            wilayah = "Semarang";
                                        } else if(label.equals("Suma Surabaya")){
                                            wilayah = "Surabaya";
                                        } else if(label.equals("Jakarta AE")){
                                            wilayah = "Jakarta AE";
                                        } else if(label.equals("Akunting dan Keuangan")){
                                            wilayah = "Akunting dan Keuangan";
                                        }

                                        Intent intentPush = new Intent(VisitStatisticActivity.this, DetailVisitStatisticAreaActivity.class);
                                        intentPush.putExtra("wilayah_full", label);
                                        intentPush.putExtra("wilayah", wilayah);
                                        intentPush.putExtra("month", selectMonth);
                                        intentPush.putExtra("month_text", bulanPilihTV.getText().toString());
                                        startActivity(intentPush);

                                    }
                                    @Override
                                    public void onNothingSelected() {
                                    }
                                });

                                noDataPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                pieChartPart.setVisibility(View.VISIBLE);

                                getDataStatisticSales();
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                pieChartPart.setVisibility(View.GONE);

                                searchInput.setVisibility(View.GONE);
                                listSalesRV.setVisibility(View.GONE);
                                loadingDataSalesPart.setVisibility(View.GONE);
                                noDataSalesPart.setVisibility(View.VISIBLE);
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
                    new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.ERROR_TYPE)
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

    private void getDataStatisticSales() {
        String url = "https://reporting.sumasistem.co.id/api/get_visit_statistic_sales?month="+selectMonth;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                titleSalesListTV.setText("Statistik Kunjungan Sales Bulan "+bulanPilihTV.getText().toString());
                                String data_statistik = response.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                salesVisitStatistics = gson.fromJson(data_statistik, SalesVisitStatistic[].class);
                                adapterListDataVisitStatisticSales = new AdapterListDataVisitStatisticSales(salesVisitStatistics, VisitStatisticActivity.this);
                                listSalesRV.setAdapter(adapterListDataVisitStatisticSales);

                                searchInput.setVisibility(View.VISIBLE);
                                listSalesRV.setVisibility(View.VISIBLE);
                                loadingDataSalesPart.setVisibility(View.GONE);
                                noDataSalesPart.setVisibility(View.GONE);
                            } else {
                                searchInput.setVisibility(View.GONE);
                                listSalesRV.setVisibility(View.GONE);
                                loadingDataSalesPart.setVisibility(View.GONE);
                                noDataSalesPart.setVisibility(View.VISIBLE);
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
                    new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.ERROR_TYPE)
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

    public BroadcastReceiver toDetail = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isActivityOpened) {
                isActivityOpened = true;
                String NIK = intent.getStringExtra("NIK");
                Intent intentPush = new Intent(VisitStatisticActivity.this, DetailVisitStatisticSalesActivity.class);
                intentPush.putExtra("NIK", NIK);
                intentPush.putExtra("month", selectMonth);
                startActivity(intentPush);
            }
        }
    };

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
                new KAlertDialog(VisitStatisticActivity.this, KAlertDialog.ERROR_TYPE)
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

    @Override
    protected void onResume() {
        super.onResume();
        isActivityOpened = false;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        searchInput.setText("");
        searchInput.clearFocus();
        getPieCart();
        getTryWarning();
    }

    private String getMonth() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getMonthOnly() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
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

    @Override
    public void onBackPressed() {
        if (bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}