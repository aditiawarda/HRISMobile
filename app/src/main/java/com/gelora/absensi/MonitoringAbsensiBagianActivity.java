package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterKehadiranBagian;
import com.gelora.absensi.adapter.AdapterKetidakhadiranBagian;
import com.gelora.absensi.adapter.AdapterPulangCepat;
import com.gelora.absensi.model.DataMonitoringKehadiranBagian;
import com.gelora.absensi.model.DataMonitoringKetidakhadiranBagian;
import com.gelora.absensi.model.DataPulangCepat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MonitoringAbsensiBagianActivity extends AppCompatActivity {

    TextView indHadir, indTidakhadir, jumlahKaryawanTV, currentDateTV, namaDepartemen, namaBagian, monitorDataHadir, monitorDataTidakHadir;
    String currentDay = "", dateChoiceForHistory;
    SharedPrefManager sharedPrefManager;
    ImageView dataTidakHadirLoading, dataHadirLoading, loadingDataKehadiranBagian, loadingDataKetidakhadiranBagian;
    SwipeRefreshLayout refreshLayout;
    LinearLayout seacrhKaryawanBTN, indikatorHadirBTN, indikatorTidakHadirBTN, hadirBTN, tidakHadirBTN, hadirPart, tidakHadirPart, backBTN, homeBTN, loadingDataKehadiranPart, loadingDataKetidakhadiranPart, choiceDateBTN, noDataHadirBagian, noDataTidakHadirBagian;

    private RecyclerView dataKehadiranBagianRV, dataKeTidakhadiranBagianRV;
    private DataMonitoringKehadiranBagian[] dataMonitoringKehadiranBagians;
    private AdapterKehadiranBagian adapterKehadiranBagian;
    private DataMonitoringKetidakhadiranBagian[] dataMonitoringKetidakhadiranBagians;
    private AdapterKetidakhadiranBagian adapterKetidakhadiranBagian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_absensi_bagian);

        sharedPrefManager = new SharedPrefManager(this);
        jumlahKaryawanTV = findViewById(R.id.jumlah_karyawan_tv);
        currentDateTV = findViewById(R.id.current_date);
        namaDepartemen = findViewById(R.id.nama_departemen);
        namaBagian = findViewById(R.id.nama_bagian);
        monitorDataHadir = findViewById(R.id.monitor_data_hadir);
        monitorDataTidakHadir = findViewById(R.id.monitor_data_tidak_hadir);
        dataHadirLoading = findViewById(R.id.monitor_data_hadir_loading);
        dataTidakHadirLoading = findViewById(R.id.monitor_data_tidak_hadir_loading);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        loadingDataKehadiranPart = findViewById(R.id.loading_data_kehadiran_bagian_part);
        loadingDataKehadiranBagian = findViewById(R.id.loading_data_kehadiran_bagian);
        choiceDateBTN = findViewById(R.id.choice_date);
        noDataHadirBagian = findViewById(R.id.no_data_hadir_bagian);
        hadirBTN = findViewById(R.id.hadir_button);
        hadirPart = findViewById(R.id.kehadiran_part);
        tidakHadirBTN = findViewById(R.id.tidak_hadir_button);
        tidakHadirPart = findViewById(R.id.ketidakhadiran_part);
        indikatorHadirBTN = findViewById(R.id.indicator_hadir);
        indikatorTidakHadirBTN = findViewById(R.id.indicator_tidak_hadir);
        loadingDataKetidakhadiranBagian = findViewById(R.id.loading_data_ketidakhadiran_bagian);
        loadingDataKetidakhadiranPart = findViewById(R.id.loading_data_ketidakhadiran_bagian_part);
        noDataTidakHadirBagian = findViewById(R.id.no_data_tidak_hadir_bagian);
        indHadir = findViewById(R.id.ind_hadir);
        indTidakhadir = findViewById(R.id.ind_tidak_hadir);
        seacrhKaryawanBTN = findViewById(R.id.seacrh_karyawan_btn);

        dateChoiceForHistory = getDate();

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(dataHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(dataTidakHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataKehadiranBagian);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingDataKetidakhadiranBagian);

        namaBagian.setText(getIntent().getExtras().getString("nama_bagian"));
        namaDepartemen.setText(getIntent().getExtras().getString("nama_departemen"));

        dataKehadiranBagianRV = findViewById(R.id.data_kehadiran_bagian_rv);
        dataKeTidakhadiranBagianRV = findViewById(R.id.data_ketidakhadiran_bagian_rv);

        dataKehadiranBagianRV.setLayoutManager(new LinearLayoutManager(this));
        dataKehadiranBagianRV.setHasFixedSize(true);
        dataKehadiranBagianRV.setItemAnimator(new DefaultItemAnimator());

        dataKeTidakhadiranBagianRV.setLayoutManager(new LinearLayoutManager(this));
        dataKeTidakhadiranBagianRV.setHasFixedSize(true);
        dataKeTidakhadiranBagianRV.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dateChoiceForHistory = getDate();

                indHadir.setVisibility(View.GONE);
                indTidakhadir.setVisibility(View.GONE);
                dataHadirLoading.setVisibility(View.VISIBLE);
                dataTidakHadirLoading.setVisibility(View.VISIBLE);
                monitorDataHadir.setVisibility(View.GONE);
                monitorDataTidakHadir.setVisibility(View.GONE);

                noDataHadirBagian.setVisibility(View.GONE);
                noDataTidakHadirBagian.setVisibility(View.GONE);

                loadingDataKehadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);

                loadingDataKetidakhadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);
                dataKeTidakhadiranBagianRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getKehadiranBagian();
                        getCurrentDay();
                    }
                }, 800);
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
                Intent intent = new Intent(MonitoringAbsensiBagianActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        seacrhKaryawanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonitoringAbsensiBagianActivity.this, SearchKaryawanBagianActivity.class);
                startActivity(intent);
            }
        });

        choiceDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        hadirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indHadir.setVisibility(View.GONE);
                indTidakhadir.setVisibility(View.GONE);
                indikatorHadirBTN.setVisibility(View.VISIBLE);
                indikatorTidakHadirBTN.setVisibility(View.GONE);
                hadirPart.setVisibility(View.VISIBLE);
                tidakHadirPart.setVisibility(View.GONE);

                noDataHadirBagian.setVisibility(View.GONE);
                noDataTidakHadirBagian.setVisibility(View.GONE);
                loadingDataKehadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);

                loadingDataKetidakhadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);
                dataKeTidakhadiranBagianRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getKehadiranBagian();
                    }
                }, 800);

            }
        });

        tidakHadirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indHadir.setVisibility(View.GONE);
                indTidakhadir.setVisibility(View.GONE);
                indikatorTidakHadirBTN.setVisibility(View.VISIBLE);
                indikatorHadirBTN.setVisibility(View.GONE);
                tidakHadirPart.setVisibility(View.VISIBLE);
                hadirPart.setVisibility(View.GONE);

                noDataHadirBagian.setVisibility(View.GONE);
                noDataTidakHadirBagian.setVisibility(View.GONE);
                loadingDataKehadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);

                loadingDataKetidakhadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);
                dataKeTidakhadiranBagianRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getKehadiranBagian();
                    }
                }, 800);

            }
        });

        getKehadiranBagian();
        getCurrentDay();

    }

    private void getKehadiranBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/monitoring_bagian";
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
                                String jumlah_karyawan = data.getString("jumlah_karyawan");
                                String hadir = data.getString("hadir");
                                String tidak_hadir = data.getString("tidak_hadir");

                                jumlahKaryawanTV.setText(jumlah_karyawan);
                                monitorDataHadir.setText(hadir);
                                monitorDataTidakHadir.setText(tidak_hadir);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataHadirLoading.setVisibility(View.GONE);
                                        dataTidakHadirLoading.setVisibility(View.GONE);
                                        monitorDataHadir.setVisibility(View.VISIBLE);
                                        monitorDataTidakHadir.setVisibility(View.VISIBLE);
                                    }
                                }, 800);

                                if (hadir.equals("0")) {
                                    indHadir.setVisibility(View.GONE);
                                    noDataHadirBagian.setVisibility(View.VISIBLE);
                                    dataKehadiranBagianRV.setVisibility(View.GONE);
                                    loadingDataKehadiranPart.setVisibility(View.GONE);

                                    if (tidak_hadir.equals("0")){
                                        indTidakhadir.setVisibility(View.GONE);
                                        noDataTidakHadirBagian.setVisibility(View.VISIBLE);
                                        dataKeTidakhadiranBagianRV.setVisibility(View.GONE);
                                        loadingDataKetidakhadiranPart.setVisibility(View.GONE);
                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                indTidakhadir.setVisibility(View.VISIBLE);
                                                noDataTidakHadirBagian.setVisibility(View.GONE);
                                                dataKeTidakhadiranBagianRV.setVisibility(View.VISIBLE);
                                                loadingDataKetidakhadiranPart.setVisibility(View.GONE);
                                            }
                                        }, 800);

                                        String data_ketidakhadiran_bagian = data.getString("data_tidak_hadir");
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        dataMonitoringKetidakhadiranBagians = gson.fromJson(data_ketidakhadiran_bagian, DataMonitoringKetidakhadiranBagian[].class);
                                        adapterKetidakhadiranBagian = new AdapterKetidakhadiranBagian(dataMonitoringKetidakhadiranBagians, MonitoringAbsensiBagianActivity.this);
                                        dataKeTidakhadiranBagianRV.setAdapter(adapterKetidakhadiranBagian);
                                    }

                                } else {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            indHadir.setVisibility(View.VISIBLE);
                                            noDataHadirBagian.setVisibility(View.GONE);
                                            loadingDataKehadiranPart.setVisibility(View.GONE);
                                            dataKehadiranBagianRV.setVisibility(View.VISIBLE);
                                        }
                                    }, 800);

                                    String data_kehadiran_bagian = data.getString("data_hadir");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataMonitoringKehadiranBagians = gson.fromJson(data_kehadiran_bagian, DataMonitoringKehadiranBagian[].class);
                                    adapterKehadiranBagian = new AdapterKehadiranBagian(dataMonitoringKehadiranBagians, MonitoringAbsensiBagianActivity.this);
                                    dataKehadiranBagianRV.setAdapter(adapterKehadiranBagian);

                                    if (tidak_hadir.equals("0")){
                                        indTidakhadir.setVisibility(View.GONE);
                                        noDataTidakHadirBagian.setVisibility(View.VISIBLE);
                                        dataKeTidakhadiranBagianRV.setVisibility(View.GONE);
                                        loadingDataKetidakhadiranPart.setVisibility(View.GONE);
                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                indTidakhadir.setVisibility(View.VISIBLE);
                                                noDataTidakHadirBagian.setVisibility(View.GONE);
                                                dataKeTidakhadiranBagianRV.setVisibility(View.VISIBLE);
                                                loadingDataKetidakhadiranPart.setVisibility(View.GONE);
                                            }
                                        }, 800);

                                        String data_ketidakhadiran_bagian = data.getString("data_tidak_hadir");
                                        dataMonitoringKetidakhadiranBagians = gson.fromJson(data_ketidakhadiran_bagian, DataMonitoringKetidakhadiranBagian[].class);
                                        adapterKetidakhadiranBagian = new AdapterKetidakhadiranBagian(dataMonitoringKetidakhadiranBagians, MonitoringAbsensiBagianActivity.this);
                                        dataKeTidakhadiranBagianRV.setAdapter(adapterKetidakhadiranBagian);
                                    }

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        //connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("tanggal", dateChoiceForHistory);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SetTextI18n")
    private void dateLive(){
        switch (getDateM()) {
            case "01":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                break;
            case "02":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                break;
            case "03":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                break;
            case "04":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                break;
            case "05":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                break;
            case "06":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                break;
            case "07":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                break;
            case "08":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                break;
            case "09":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                break;
            case "10":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                break;
            case "11":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                break;
            case "12":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                break;
            default:
                currentDateTV.setText("Not found!");
                break;
        }

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("11");
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

    private void getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay = "Minggu";
                break;
            case Calendar.MONDAY:
                currentDay = "Senin";
                break;
            case Calendar.TUESDAY:
                currentDay = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "Rabu";
                break;
            case Calendar.THURSDAY:
                currentDay = "Kamis";
                break;
            case Calendar.FRIDAY:
                currentDay = "Jumat";
                break;
            case Calendar.SATURDAY:
                currentDay = "Sabtu";
                break;
        }

        dateLive();

    }

    @SuppressLint("SimpleDateFormat")
    private void datePicker(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(MonitoringAbsensiBagianActivity.this, (view1, year, month, dayOfMonth) -> {

                dateChoiceForHistory = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                String input_date = dateChoiceForHistory;
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

                currentDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                dataHadirLoading.setVisibility(View.VISIBLE);
                dataTidakHadirLoading.setVisibility(View.VISIBLE);
                monitorDataHadir.setVisibility(View.GONE);
                monitorDataTidakHadir.setVisibility(View.GONE);

                indHadir.setVisibility(View.GONE);
                indTidakhadir.setVisibility(View.GONE);
                noDataHadirBagian.setVisibility(View.GONE);
                noDataTidakHadirBagian.setVisibility(View.GONE);
                loadingDataKehadiranPart.setVisibility(View.VISIBLE);
                loadingDataKetidakhadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);
                dataKeTidakhadiranBagianRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getKehadiranBagian();
                    }
                }, 1000);

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(MonitoringAbsensiBagianActivity.this, (view1, year, month, dayOfMonth) -> {

                dateChoiceForHistory = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                String input_date = dateChoiceForHistory;
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
                        bulanName = "Not found";
                        break;
                }

                currentDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                dataHadirLoading.setVisibility(View.VISIBLE);
                dataTidakHadirLoading.setVisibility(View.VISIBLE);
                monitorDataHadir.setVisibility(View.GONE);
                monitorDataTidakHadir.setVisibility(View.GONE);

                indHadir.setVisibility(View.GONE);
                indTidakhadir.setVisibility(View.GONE);
                noDataHadirBagian.setVisibility(View.GONE);
                noDataTidakHadirBagian.setVisibility(View.GONE);
                loadingDataKehadiranPart.setVisibility(View.VISIBLE);
                loadingDataKetidakhadiranPart.setVisibility(View.VISIBLE);
                dataKehadiranBagianRV.setVisibility(View.GONE);
                dataKeTidakhadiranBagianRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getKehadiranBagian();
                    }
                }, 1000);

            }, y,m,d);
            dpd.show();
        }


    }

}