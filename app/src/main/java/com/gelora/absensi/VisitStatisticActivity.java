package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterSumaReport;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataReportSuma;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    LinearLayout noDataPart, bulanBTN, loadingDataPart, backBTN, actionBar;
    TextView bulanPilihTV, totalVisitTV;
    RequestQueue requestQueue;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    String selectMonth = getMonth();
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
                        public void onDateSet(int month, int year) { // on date set
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

                            bulanPilihTV.setText(bulanName+" "+String.valueOf(year));

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getPieCart();
                                }
                            }, 100);

                        }
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH));
                builder.setMinYear(1952)
                .setActivatedYear(now.get(Calendar.YEAR))
                .setMaxYear(now.get(Calendar.YEAR))
                .setActivatedMonth(now.get(Calendar.MONTH))
                .build()
                .show();
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
                                int bandung = totalPerWilayah.getInt("Bandung");
                                int semarang = totalPerWilayah.getInt("Semarang");
                                int surabaya = totalPerWilayah.getInt("Surabaya");
                                int akuntingKeuangan = totalPerWilayah.getInt("Akunting dan Keuangan");
                                int totalKeseluruhan = response.getInt("total_keseluruhan");

                                totalVisitTV.setText("Total Kunjungan : "+String.valueOf(totalKeseluruhan));

                                ArrayList<PieEntry> entries = new ArrayList<>();
                                entries.add(new PieEntry(jakarta1, "Jakarta 1"));
                                entries.add(new PieEntry(jakarta2, "Jakarta 2"));
                                entries.add(new PieEntry(jakarta3, "Jakarta 3"));
                                entries.add(new PieEntry(bandung, "Bandung"));
                                entries.add(new PieEntry(semarang, "Semarang"));
                                entries.add(new PieEntry(surabaya, "Surabaya"));
                                entries.add(new PieEntry(akuntingKeuangan, "Akunting dan Keuangan"));

                                PieDataSet dataSet = new PieDataSet(entries, "");
                                dataSet.setValueTextSize(25f);

                                // Custom colors for PieChart
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.parseColor("#FF5722")); // Orange
                                colors.add(Color.parseColor("#4CAF50")); // Green
                                colors.add(Color.parseColor("#2196F3")); // Blue
                                colors.add(Color.parseColor("#FFC107")); // Yellow
                                colors.add(Color.parseColor("#9C27B0")); // Purple
                                colors.add(Color.parseColor("#FF9800")); // Deep Orange
                                colors.add(Color.parseColor("#3F51B5")); // Indigo
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
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                pieChartPart.setVisibility(View.GONE);
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