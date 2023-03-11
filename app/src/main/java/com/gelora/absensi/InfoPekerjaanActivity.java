package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoPekerjaanActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, nonGapPart;
    TextView nikTV, cabangTV, departemenTV, bagianTV, jabatanTV, statusKaryawanTV, tanggalBergabungTV, masaKerjaTV, golonganKaryawanTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pekerjaan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        nikTV = findViewById(R.id.nik);
        cabangTV = findViewById(R.id.cabang);
        departemenTV = findViewById(R.id.departemen);
        bagianTV = findViewById(R.id.bagian);
        jabatanTV = findViewById(R.id.jabatan);
        statusKaryawanTV = findViewById(R.id.status_karyawan);
        tanggalBergabungTV = findViewById(R.id.tanggal_bergabung);
        masaKerjaTV = findViewById(R.id.masa_kerja);
        golonganKaryawanTV = findViewById(R.id.golongan_karyawan);
        nonGapPart = findViewById(R.id.non_gap_part);
        actionBar = findViewById(R.id.action_bar);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
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

        if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpIdJabatan().equals("31")||sharedPrefManager.getSpNik().equals("80085")) {
           nonGapPart.setVisibility(View.GONE);
        } else {
            nonGapPart.setVisibility(View.VISIBLE);
        }

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_info_pekerjaan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                JSONObject dataArray = data.getJSONObject("data");
                                String nik = dataArray.getString("nik");
                                String cabang = dataArray.getString("cabang");
                                String departemen = dataArray.getString("departemen");
                                String bagian = dataArray.getString("bagian");
                                String jabatan = dataArray.getString("jabatan");
                                String status_karyawan = dataArray.getString("status_karyawan");
                                String tanggal_bergabung = dataArray.getString("tanggal_bergabung");
                                String golongan_karyawan = dataArray.getString("golongan_karyawan");

                                nikTV.setText(nik);

                                if(cabang.equals("")||cabang.equals("null")||cabang.equals("0")){
                                    cabangTV.setText("-");
                                } else {
                                    cabangTV.setText(cabang);
                                }

                                if(departemen.equals("")||departemen.equals("null")){
                                    departemenTV.setText("-");
                                } else {
                                    departemenTV.setText(departemen);
                                }

                                if(bagian.equals("")||bagian.equals("null")){
                                    bagianTV.setText("-");
                                } else {
                                    bagianTV.setText(bagian);
                                }

                                if(jabatan.equals("")||jabatan.equals("null")){
                                    jabatanTV.setText("-");
                                } else {
                                    jabatanTV.setText(jabatan);
                                }

                                if(status_karyawan.equals("")||status_karyawan.equals("null")){
                                    statusKaryawanTV.setText("-");
                                } else {
                                    statusKaryawanTV.setText(status_karyawan);
                                }

                                if(tanggal_bergabung.equals("")||tanggal_bergabung.equals("null")){
                                    tanggalBergabungTV.setText("-");
                                    masaKerjaTV.setText("-");
                                } else {
                                    String dayDate = tanggal_bergabung.substring(8,10);
                                    String yearDate = tanggal_bergabung.substring(0,4);;
                                    String bulanValue = tanggal_bergabung.substring(5,7);
                                    String bulanName;

                                    switch (bulanValue) {
                                        case "01":
                                            bulanName = "Jan";
                                            break;
                                        case "02":
                                            bulanName = "Feb";
                                            break;
                                        case "03":
                                            bulanName = "Mar";
                                            break;
                                        case "04":
                                            bulanName = "Apr";
                                            break;
                                        case "05":
                                            bulanName = "Mei";
                                            break;
                                        case "06":
                                            bulanName = "Jun";
                                            break;
                                        case "07":
                                            bulanName = "Jul";
                                            break;
                                        case "08":
                                            bulanName = "Agu";
                                            break;
                                        case "09":
                                            bulanName = "Sep";
                                            break;
                                        case "10":
                                            bulanName = "Okt";
                                            break;
                                        case "11":
                                            bulanName = "Nov";
                                            break;
                                        case "12":
                                            bulanName = "Des";
                                            break;
                                        default:
                                            bulanName = "Not found!";
                                            break;
                                    }

                                    tanggalBergabungTV.setText(dayDate+" "+bulanName+" "+yearDate);

                                    String tglSekarang = getDate();
                                    String tglMasukKerja = tanggal_bergabung;

                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        date1 = format.parse(tglSekarang);
                                        date2 = format.parse(tglMasukKerja);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long waktu1 = date1.getTime();
                                    long waktu2 = date2.getTime();
                                    long selisih_waktu = waktu1 - waktu2;

                                    long diffDays = selisih_waktu / (24 * 60 * 60 * 1000);

                                    long tahun = (diffDays / 365);
                                    long bulan = (diffDays - (tahun * 365)) / 30;
                                    long hari = (diffDays - ((tahun * 365) + (bulan * 30)));

                                    int y = Integer.parseInt(getDateY());
                                    int m = Integer.parseInt(getDateM());
                                    int d = Integer.parseInt(getDateD());

                                    LocalDate dob = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        dob = LocalDate.of(Integer.parseInt(yearDate),  Integer.parseInt(bulanValue), Integer.parseInt(dayDate));
                                        LocalDate curDate = LocalDate.now();
                                        Period period = Period.between(dob, curDate);
                                        if (period.getYears() == 0){
                                            if(period.getMonths() == 0){
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText("-");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getDays())+" Hari");
                                                }
                                            } else {
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText(String.valueOf(period.getMonths())+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getMonths())+" Bulan "+String.valueOf(period.getDays())+" Hari");
                                                }
                                            }
                                        } else {
                                            if(period.getMonths() == 0){
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun "+String.valueOf(period.getDays())+" Hari");
                                                }
                                            } else {
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun "+String.valueOf(period.getMonths())+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun "+String.valueOf(period.getMonths())+" Bulan "+String.valueOf(period.getDays())+" Hari");
                                                }
                                            }
                                        }
                                    } else {
                                        if (tahun == 0){
                                            if(bulan == 0){
                                                if(hari == 0){
                                                    masaKerjaTV.setText("-");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(hari)+" Hari");
                                                }
                                            } else {
                                                if(hari == 0){
                                                    masaKerjaTV.setText(String.valueOf(bulan)+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(bulan)+" Bulan "+String.valueOf(hari)+" Hari");
                                                }
                                            }
                                        } else {
                                            if(bulan == 0){
                                                if(hari == 0){
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun "+String.valueOf(hari)+" Hari");
                                                }
                                            } else {
                                                if(hari == 0){
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun "+String.valueOf(bulan)+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun "+String.valueOf(bulan)+" Bulan "+String.valueOf(hari)+" Hari");
                                                }
                                            }
                                        }
                                    }

                                }

                                if(golongan_karyawan.equals("")||golongan_karyawan.equals("null")){
                                    golonganKaryawanTV.setText("-");
                                } else {
                                    golonganKaryawanTV.setText(status_karyawan);
                                }

                            } else {
                                new KAlertDialog(InfoPekerjaanActivity.this, KAlertDialog.ERROR_TYPE)
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
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nik", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(InfoPekerjaanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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

}