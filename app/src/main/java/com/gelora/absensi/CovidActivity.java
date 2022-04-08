package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.R;
import com.shasin.notificationbanner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CovidActivity extends AppCompatActivity {

    TextView vaksin1Add, vaksin2Add, dateVaksin, vaksin1, vaksin2, dateData, confirmTV, healtTV, deadTV, penambahanConfirm, penambahanHealt, penambahanDead, activeCase;
    NestedScrollView scrollView;
    SwipeRefreshLayout refreshLayout;
    LinearLayout actionBar, backBTN, homeBTN;
    View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);

        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        confirmTV = findViewById(R.id.confirm_covid);
        healtTV = findViewById(R.id.healt_covid);
        deadTV = findViewById(R.id.dead_covid);
        penambahanConfirm = findViewById(R.id.penambahan_positif);
        penambahanHealt = findViewById(R.id.penambahan_sembuh);
        penambahanDead = findViewById(R.id.penambahan_meninggal);
        activeCase = findViewById(R.id.active_case);
        dateData = findViewById(R.id.date_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        scrollView = findViewById(R.id.scrollView);
        actionBar = findViewById(R.id.action_bar);
        vaksin1 = findViewById(R.id.vaksin_1);
        vaksin2 = findViewById(R.id.vaksin_2);
        dateVaksin = findViewById(R.id.date_vaksin);
        vaksin1Add = findViewById(R.id.vaksin_1_add);
        vaksin2Add = findViewById(R.id.vaksin_2_add);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getCovidData();
                        getVaksinData();
                    }
                }, 1000);
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
                Intent intent = new Intent(CovidActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY()>=15){
                    actionBar.setBackground(ContextCompat.getDrawable(CovidActivity.this, R.drawable.shape_action_bar));
                } else {
                    actionBar.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        getCovidData();
        getVaksinData();
    }


    private void getCovidData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://data.covid19.go.id/public/api/update.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {

                            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("in", "ID"));

                            JSONObject update = response.getJSONObject("update");
                            JSONObject total = update.getJSONObject("total");
                            JSONObject penambahan = update.getJSONObject("penambahan");
                            String confirm = total.getString("jumlah_positif");
                            String dead = total.getString("jumlah_meninggal");
                            String healt = total.getString("jumlah_sembuh");
                            String active = total.getString("jumlah_dirawat");

                            String confirmAdd = penambahan.getString("jumlah_positif");
                            String deadAdd = penambahan.getString("jumlah_meninggal");
                            String healtAdd = penambahan.getString("jumlah_sembuh");
                            String date = penambahan.getString("tanggal");

                            confirmTV.setText(numberFormat.format(Double.parseDouble(confirm)));
                            healtTV.setText(numberFormat.format(Double.parseDouble(healt)));
                            deadTV.setText(numberFormat.format(Double.parseDouble(dead)));
                            activeCase.setText(numberFormat.format(Double.parseDouble(active)));

                            String input_date = date;
                            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                            Date dt1= null;
                            try {
                                dt1 = format1.parse(input_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat format2=new SimpleDateFormat("EEE");
                            String finalDay = format2.format(dt1);
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
                            String yearDate = input_date.substring(0,4);;
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

                            dateData.setText("Update : "+hariName+", "+dayDate+" "+bulanName+" "+yearDate);

                            penambahanConfirm.setText(numberFormat.format(Double.parseDouble(confirmAdd)));
                            penambahanHealt.setText(numberFormat.format(Double.parseDouble(healtAdd)));
                            penambahanDead.setText(numberFormat.format(Double.parseDouble(deadAdd)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void getVaksinData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://data.covid19.go.id/public/api/pemeriksaan-vaksinasi.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("in", "ID"));

                            JSONObject vaksinasi = response.getJSONObject("vaksinasi");
                            JSONObject total = vaksinasi.getJSONObject("total");
                            JSONObject penambahan = vaksinasi.getJSONObject("penambahan");
                            String jumlah_vaksinasi_1 = total.getString("jumlah_vaksinasi_1");
                            String jumlah_vaksinasi_2 = total.getString("jumlah_vaksinasi_2");
                            String jumlah_vaksinasi_1_add = penambahan.getString("jumlah_vaksinasi_1");
                            String jumlah_vaksinasi_2_add = penambahan.getString("jumlah_vaksinasi_2");
                            String date_vaksin = penambahan.getString("tanggal");

                            vaksin1.setText(numberFormat.format(Double.parseDouble(jumlah_vaksinasi_1)));
                            vaksin2.setText(numberFormat.format(Double.parseDouble(jumlah_vaksinasi_2)));

                            vaksin1Add.setText(numberFormat.format(Double.parseDouble(jumlah_vaksinasi_1_add)));
                            vaksin2Add.setText(numberFormat.format(Double.parseDouble(jumlah_vaksinasi_2_add)));

                            String input_date = date_vaksin;
                            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                            Date dt1= null;
                            try {
                                dt1 = format1.parse(input_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat format2=new SimpleDateFormat("EEE");
                            String finalDay = format2.format(dt1);
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
                            String yearDate = input_date.substring(0,4);;
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

                            dateVaksin.setText("Update : "+hariName+", "+dayDate+" "+bulanName+" "+yearDate);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void connectionFailed(){
        Banner.make(rootview, CovidActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 4000).show();
    }

}