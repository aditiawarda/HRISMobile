package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.gelora.absensi.adapter.AdapterListDataVisitStatisticSales;
import com.gelora.absensi.adapter.AdapterProductInputSuma;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.SalesVisitStatistic;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitStatisticActivity extends AppCompatActivity {

    PieChart pieChart;
    RelativeLayout pieChartPart;
    LinearLayout loadingDataSalesPart, noDataSalesPart, noDataPart, bulanBTN, loadingDataPart, backBTN, actionBar;
    TextView titleSalesListTV, bulanPilihTV, totalVisitTV;
    RecyclerView listSalesRV;
    private SalesVisitStatistic[] salesVisitStatistics;
    AdapterListDataVisitStatisticSales adapterListDataVisitStatisticSales;
    RequestQueue requestQueue;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    String selectMonth = getMonth();
    EditText searchInput;
    private Handler handler = new Handler();

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

        searchInput.setVisibility(View.GONE);

        listSalesRV.setLayoutManager(new LinearLayoutManager(this));
        listSalesRV.setHasFixedSize(true);
        listSalesRV.setNestedScrollingEnabled(false);
        listSalesRV.setItemAnimator(new DefaultItemAnimator());

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

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterListDataVisitStatisticSales.filterByName(s.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        getPieCart();

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

                                noDataPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                pieChartPart.setVisibility(View.VISIBLE);

                                getDataStatisticSales();
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                pieChartPart.setVisibility(View.GONE);

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

}