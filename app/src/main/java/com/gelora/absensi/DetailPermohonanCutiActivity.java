package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DetailPermohonanCutiActivity extends AppCompatActivity {

    TextView namaKaryawanTV, namaPemohonTV, jabatanTV, bagianTV, mulaiBergabungTV, nikTV, statusKaryawanTV, tipeCutiTV, alamatTV, noHpTV, karyawanPenggantiTV, sisaCutiTV, alasanCutiTV, tahunCutiAmbilTV, totalCutiAmbilTV, tahunCutiTV, tglMulaiCutiTV, tglSelesaiCutiTV, totalCutiTV, tglPengajuanTV;
    String idIzinRecord, kode;
    LinearLayout backBTN, homeBTN;
    SwipeRefreshLayout refreshLayout;
    ImageView ttdPemohon;
    KAlertDialog pDialog;
    Bitmap bitmap;
    SharedPrefManager sharedPrefManager;
    View rootview;
    private final int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permohonan_cuti);

        rootview = findViewById(android.R.id.content);
        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        namaKaryawanTV = findViewById(R.id.nama_tv);
        jabatanTV = findViewById(R.id.jabatan_tv);
        bagianTV = findViewById(R.id.bagian_tv);
        mulaiBergabungTV = findViewById(R.id.tgl_bergabung_tv);
        nikTV = findViewById(R.id.nik_tv);
        statusKaryawanTV = findViewById(R.id.status_karyawan_tv);
        sisaCutiTV = findViewById(R.id.sisa_cuti_tv);
        tahunCutiAmbilTV = findViewById(R.id.tahun_cuti_diambil_tv);
        totalCutiAmbilTV = findViewById(R.id.total_cuti_diambil_tv);
        tahunCutiTV = findViewById(R.id.tahun_cuti_tv);
        tglMulaiCutiTV = findViewById(R.id.tgl_mulai_cuti_tv);
        tglSelesaiCutiTV = findViewById(R.id.tgl_selesai_cuti_tv);
        totalCutiTV = findViewById(R.id.total_cuti_tv);
        alasanCutiTV = findViewById(R.id.alasan_cuti_tv);
        karyawanPenggantiTV = findViewById(R.id.karyawan_pengganti_tv);
        alamatTV = findViewById(R.id.alamat_tv);
        noHpTV = findViewById(R.id.no_hp_tv);
        tglPengajuanTV = findViewById(R.id.tgl_permohonan_tv);
        namaPemohonTV = findViewById(R.id.nama_pemeohon_tv);
        ttdPemohon = findViewById(R.id.ttd_pemohon);
        tipeCutiTV = findViewById(R.id.tipe_cuti_tv);

        kode = getIntent().getExtras().getString("kode");
        idIzinRecord = getIntent().getExtras().getString("id_izin");

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataDetailPermohonan();
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
                Intent intent = new Intent(DetailPermohonanCutiActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        getDataDetailPermohonan();

    }

    private void getDataDetailPermohonan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_permohonan_cuti_detail";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                JSONObject detail = data.getJSONObject("data");
                                String NIK = detail.getString("NIK");
                                String NmKaryawan = detail.getString("NmKaryawan");
                                String bagian = detail.getString("NmDept");
                                String jabatan = detail.getString("NmJabatan");
                                String tanggal_bergabung = detail.getString("tanggal_bergabung");
                                String status_karyawan = detail.getString("status_karyawan");
                                String sisa_cuti_sementara = detail.getString("sisa_cuti_sementara");
                                String tahun_cuti_telah_diambil = detail.getString("tahun_cuti_telah_diambil");
                                String total_cuti_telah_diambil = detail.getString("total_cuti_telah_diambil");
                                String tanggal_mulai = detail.getString("tanggal_mulai");
                                String tanggal_akhir = detail.getString("tanggal_akhir");
                                String alasan_cuti = detail.getString("alasan_cuti");
                                String tipe_cuti = detail.getString("tipe_cuti");
                                String pengganti = detail.getString("pengganti");
                                String alamat_selama_cuti = detail.getString("alamat_selama_cuti");
                                String no_hp = detail.getString("no_hp");
                                String tanggal = detail.getString("tanggal");
                                String digital_signature = detail.getString("digital_signature");

                                namaPemohonTV.setText(NmKaryawan);
                                namaKaryawanTV.setText(":  "+NmKaryawan);
                                jabatanTV.setText(":  "+jabatan);
                                bagianTV.setText(":  "+bagian);
                                mulaiBergabungTV.setText(":  "+tanggal_bergabung.substring(8,10)+"/"+tanggal_bergabung.substring(5,7)+"/"+tanggal_bergabung.substring(0,4));
                                nikTV.setText(":  "+NIK);
                                statusKaryawanTV.setText(":  "+status_karyawan);
                                sisaCutiTV.setText(":  "+sisa_cuti_sementara+" Hari");
                                tahunCutiAmbilTV.setText("Tahun  "+tahun_cuti_telah_diambil);
                                totalCutiAmbilTV.setText("Total  "+total_cuti_telah_diambil+"  Hari");
                                tahunCutiTV.setText("Tahun  "+tanggal_mulai.substring(0,4));
                                tglMulaiCutiTV.setText("dari  "+tanggal_mulai.substring(8,10)+"/"+tanggal_mulai.substring(5,7)+"/"+tanggal_mulai.substring(0,4));
                                tglSelesaiCutiTV.setText("sampai  "+tanggal_akhir.substring(8,10)+"/"+tanggal_akhir.substring(5,7)+"/"+tanggal_akhir.substring(0,4));

                                String jumlah_hari = data.getString("jumlah_hari");
                                totalCutiTV.setText("Total  "+jumlah_hari+"  Hari");
                                alasanCutiTV.setText(":  "+alasan_cuti);
                                alamatTV.setText(":  "+alamat_selama_cuti);
                                noHpTV.setText(":  "+no_hp);

                                if(tipe_cuti.equals("0")){
                                    tipeCutiTV.setText(":  Undefined");
                                } else if(tipe_cuti.equals("1")){
                                    tipeCutiTV.setText(":  Tahunan");
                                } else if(tipe_cuti.equals("2")){
                                    tipeCutiTV.setText(":  Khusus");
                                }

                                String pengajuan = tanggal;
                                String dayDatePengajuan = pengajuan.substring(8,10);
                                String yearDatePengajuan = pengajuan.substring(0,4);
                                String bulanValuePengajuan = pengajuan.substring(5,7);
                                String bulanNamePengajuan;

                                switch (bulanValuePengajuan) {
                                    case "01":
                                        bulanNamePengajuan = "Januari";
                                        break;
                                    case "02":
                                        bulanNamePengajuan = "Februari";
                                        break;
                                    case "03":
                                        bulanNamePengajuan = "Maret";
                                        break;
                                    case "04":
                                        bulanNamePengajuan = "April";
                                        break;
                                    case "05":
                                        bulanNamePengajuan = "Mei";
                                        break;
                                    case "06":
                                        bulanNamePengajuan = "Juni";
                                        break;
                                    case "07":
                                        bulanNamePengajuan = "Juli";
                                        break;
                                    case "08":
                                        bulanNamePengajuan = "Agustus";
                                        break;
                                    case "09":
                                        bulanNamePengajuan = "September";
                                        break;
                                    case "10":
                                        bulanNamePengajuan = "Oktober";
                                        break;
                                    case "11":
                                        bulanNamePengajuan = "November";
                                        break;
                                    case "12":
                                        bulanNamePengajuan = "Desember";
                                        break;
                                    default:
                                        bulanNamePengajuan = "Not found";
                                        break;
                                }

                                tglPengajuanTV.setText("JAKARTA,  "+Integer.parseInt(dayDatePengajuan) +" "+bulanNamePengajuan+" "+yearDatePengajuan);

                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+digital_signature;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(ttdPemohon);

                                karyawanPengganti(pengganti);

                                if (kode.equals("form")){
                                    // actionPart.setVisibility(View.GONE);
                                } else {
                                    if (NIK.equals(sharedPrefManager.getSpNik())){
                                        actionRead();
//                                        if(sharedPrefManager.getSpIdJabatan().equals("10")){
//                                            if(status_approve.equals("1")){
//                                                actionPart.setVisibility(View.GONE);
//                                            } else if (status_approve.equals("2")){
//                                                actionPart.setVisibility(View.GONE);
//                                            } else {
//                                                actionPart.setVisibility(View.VISIBLE);
//                                            }
//                                        } else {
//                                            actionPart.setVisibility(View.GONE);
//                                        }
                                    } else {
//                                        if(status_approve.equals("1")){
//                                            actionPart.setVisibility(View.GONE);
//                                        } else if (status_approve.equals("2")){
//                                            actionPart.setVisibility(View.GONE);
//                                        } else {
//                                            actionPart.setVisibility(View.VISIBLE);
//                                        }
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
                        connectionFailed();
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

    private void karyawanPengganti(String nik) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/karyawan_pengganti_cuti";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String nama = data.getString("nama");
                                karyawanPenggantiTV.setText(":  "+nama);
                            } else {
                                karyawanPenggantiTV.setText(":  Data tidak ditemukan");
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
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("NIK", nik);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void actionRead() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/read_notif_izin";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");

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
                        connectionFailed();
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

    private void connectionFailed(){
        CookieBar.build(DetailPermohonanCutiActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

}