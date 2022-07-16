package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataMonitoringKehadiranBagian;
import com.gelora.absensi.model.DataMonitoringKetidakhadiranBagian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DetailPermohonanIzinActivity extends AppCompatActivity {

    TextView idPermohonanTV, namaKaryawanTV, nikKaryawanTV, bagianKaryawanTV, jabatanKaryawanTV, alasanIzinTV, tglMulaiTV, tglAkhirTV, totalHariTV, tglPermohonanTV, pemohonTV, supervisorTV, hrdTV;
    String idIzinRecord;
    LinearLayout backBTN, homeBTN, approvedBTN, rejectedBTN;
    SwipeRefreshLayout refreshLayout;
    ImageView ttdPemohon;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permohonan_izin);

        idPermohonanTV = findViewById(R.id.id_permohonan);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        approvedBTN = findViewById(R.id.appoved_btn);
        rejectedBTN = findViewById(R.id.rejected_btn);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        bagianKaryawanTV = findViewById(R.id.bagian_karyawan_tv);
        jabatanKaryawanTV = findViewById(R.id.jabatan_karyawan_tv);
        alasanIzinTV = findViewById(R.id.alasan_izin_tv);
        tglMulaiTV = findViewById(R.id.tgl_mulai_tv);
        tglAkhirTV = findViewById(R.id.tgl_akhir_tv);
        totalHariTV = findViewById(R.id.jumlah_hari_tv);
        tglPermohonanTV = findViewById(R.id.tgl_permohonan_tv);
        pemohonTV = findViewById(R.id.pemohon_tv);
        supervisorTV = findViewById(R.id.supervisor_tv);
        hrdTV = findViewById(R.id.hrd_tv);
        ttdPemohon = findViewById(R.id.ttd_pemohon);

        idIzinRecord = getIntent().getExtras().getString("id_izin");

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
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
                Intent intent = new Intent(DetailPermohonanIzinActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        approvedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Setujui?")
                        .setContentText("Yakin untuk disetujui sekarang?")
                        .setCancelText("NO")
                        .setConfirmText("YES")
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

                                KAlertDialog pDialog;
                                pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        pDialog.setTitleText("Permohonan Berhasil Disetujui")
                                                .setConfirmText("OK")
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    }
                                }.start();

                            }
                        })
                        .show();

            }
        });

        rejectedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Tolak?")
                        .setContentText("Yakin untuk tolak permohonan?")
                        .setCancelText("NO")
                        .setConfirmText("YES")
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

                                KAlertDialog pDialog;
                                pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        pDialog.setTitleText("Permohonan Berhasil Ditolak")
                                                .setConfirmText("OK")
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                                    }
                                }.start();

                            }
                        })
                        .show();

            }
        });

        idPermohonanTV.setText(idIzinRecord);

        getDataDetailPermohonan();

    }

    private void getDataDetailPermohonan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_permohonan_izin_detail";
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
                            if (status.equals("Success")) {
                                JSONObject detail = data.getJSONObject("data");
                                String nik_karyawan = detail.getString("NIK");
                                String nama_karyawan = detail.getString("NmKaryawan");
                                String bagian = detail.getString("NmDept");
                                String jabatan = detail.getString("NmJabatan");
                                String alasan = detail.getString("keterangan");
                                String tgl_mulai = detail.getString("tanggal_mulai");
                                String tgl_akhir = detail.getString("tanggal_akhir");
                                String tgl_permohonan = detail.getString("tanggal");
                                String digital_signature = detail.getString("digital_signature");

                                namaKaryawanTV.setText(nama_karyawan.toUpperCase());
                                nikKaryawanTV.setText(nik_karyawan);
                                bagianKaryawanTV.setText(bagian);
                                jabatanKaryawanTV.setText(jabatan);
                                alasanIzinTV.setText(alasan);

                                String input_date_mulai = tgl_mulai;
                                String dayDateMulai = input_date_mulai.substring(8,10);
                                String yearDateMulai = input_date_mulai.substring(0,4);
                                String bulanValueMulai = input_date_mulai.substring(5,7);
                                String bulanNameMulai;

                                switch (bulanValueMulai) {
                                    case "01":
                                        bulanNameMulai = "Januari";
                                        break;
                                    case "02":
                                        bulanNameMulai = "Februari";
                                        break;
                                    case "03":
                                        bulanNameMulai = "Maret";
                                        break;
                                    case "04":
                                        bulanNameMulai = "April";
                                        break;
                                    case "05":
                                        bulanNameMulai = "Mei";
                                        break;
                                    case "06":
                                        bulanNameMulai = "Juni";
                                        break;
                                    case "07":
                                        bulanNameMulai = "Juli";
                                        break;
                                    case "08":
                                        bulanNameMulai = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameMulai = "September";
                                        break;
                                    case "10":
                                        bulanNameMulai = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameMulai = "November";
                                        break;
                                    case "12":
                                        bulanNameMulai = "Desember";
                                        break;
                                    default:
                                        bulanNameMulai = "Not found";
                                        break;
                                }

                                tglMulaiTV.setText(String.valueOf(Integer.parseInt(dayDateMulai))+" "+bulanNameMulai+" "+yearDateMulai);

                                String input_date_akhir = tgl_akhir;
                                String dayDateAkhir = input_date_akhir.substring(8,10);
                                String yearDateAkhir = input_date_akhir.substring(0,4);
                                String bulanValueAkhir = input_date_akhir.substring(5,7);
                                String bulanNameAkhir;

                                switch (bulanValueAkhir) {
                                    case "01":
                                        bulanNameAkhir = "Januari";
                                        break;
                                    case "02":
                                        bulanNameAkhir = "Februari";
                                        break;
                                    case "03":
                                        bulanNameAkhir = "Maret";
                                        break;
                                    case "04":
                                        bulanNameAkhir = "April";
                                        break;
                                    case "05":
                                        bulanNameAkhir = "Mei";
                                        break;
                                    case "06":
                                        bulanNameAkhir = "Juni";
                                        break;
                                    case "07":
                                        bulanNameAkhir = "Juli";
                                        break;
                                    case "08":
                                        bulanNameAkhir = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameAkhir = "September";
                                        break;
                                    case "10":
                                        bulanNameAkhir = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameAkhir = "November";
                                        break;
                                    case "12":
                                        bulanNameAkhir = "Desember";
                                        break;
                                    default:
                                        bulanNameAkhir = "Not found";
                                        break;
                                }

                                tglAkhirTV.setText(String.valueOf(Integer.parseInt(dayDateAkhir))+" "+bulanNameAkhir+" "+yearDateAkhir);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                Date dateBefore = sdf.parse(String.valueOf(tgl_mulai));
                                Date dateAfter = sdf.parse(String.valueOf(tgl_akhir));
                                long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
                                long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

                                totalHariTV.setText("( "+String.valueOf(daysDiff+1)+" Hari )");

                                String input_date_permohonan = tgl_permohonan;
                                String dayDatePermohonan = input_date_permohonan.substring(8,10);
                                String yearDatePermohonan = input_date_permohonan.substring(0,4);
                                String bulanValuePermohonan = input_date_permohonan.substring(5,7);
                                String bulanNamePermohonan;

                                switch (bulanValuePermohonan) {
                                    case "01":
                                        bulanNamePermohonan = "Januari";
                                        break;
                                    case "02":
                                        bulanNamePermohonan = "Februari";
                                        break;
                                    case "03":
                                        bulanNamePermohonan = "Maret";
                                        break;
                                    case "04":
                                        bulanNamePermohonan = "April";
                                        break;
                                    case "05":
                                        bulanNamePermohonan = "Mei";
                                        break;
                                    case "06":
                                        bulanNamePermohonan = "Juni";
                                        break;
                                    case "07":
                                        bulanNamePermohonan = "Juli";
                                        break;
                                    case "08":
                                        bulanNamePermohonan = "Agustus";
                                        break;
                                    case "09":
                                        bulanNamePermohonan = "September";
                                        break;
                                    case "10":
                                        bulanNamePermohonan = "Oktober";
                                        break;
                                    case "11":
                                        bulanNamePermohonan = "November";
                                        break;
                                    case "12":
                                        bulanNamePermohonan = "Desember";
                                        break;
                                    default:
                                        bulanNamePermohonan = "Not found";
                                        break;
                                }

                                tglPermohonanTV.setText("JAKARTA, "+String.valueOf(Integer.parseInt(dayDatePermohonan))+" "+bulanNamePermohonan+" "+yearDatePermohonan);

                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+digital_signature;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(ttdPemohon);

                                String shortName = nama_karyawan;
                                if(shortName.contains(" ")){
                                    shortName = shortName.substring(0, shortName.indexOf(" "));
                                    pemohonTV.setText(shortName.toUpperCase());
                                }

                            }

                        } catch (JSONException | ParseException e) {
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
                params.put("id_izin_record", idIzinRecord);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

}