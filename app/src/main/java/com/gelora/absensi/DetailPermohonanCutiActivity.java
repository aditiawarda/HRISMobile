package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DetailPermohonanCutiActivity extends AppCompatActivity {

    TextView tglApprover1, tglApprover2, tglApproverHRD, namaApprover1, namaApproverHRD, namaApprover2, namaKaryawanTV, namaPemohonTV, jabatanTV, bagianTV, mulaiBergabungTV, nikTV, statusKaryawanTV, tipeCutiTV, alamatTV, noHpTV, karyawanPenggantiTV, sisaCutiTV, alasanCutiTV, tahunCutiAmbilTV, totalCutiAmbilTV, tahunCutiTV, tglMulaiCutiTV, tglSelesaiCutiTV, totalCutiTV, tglPengajuanTV;
    String nikPemohon = "", statusKondisi = "0", idIzinRecord, kode;
    LinearLayout backBTN, homeBTN, actionPart, approvedBTN, rejectedBTN, rejectedMark, acceptedMark;
    SwipeRefreshLayout refreshLayout;
    ImageView ttdPemohon, ttdApprover1, ttdApprover2, ttdApproverHRD;
    KAlertDialog pDialog;
    Bitmap bitmap;
    SharedPrefManager sharedPrefManager;
    View rootview;
    private int i = -1;

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
        actionPart = findViewById(R.id.action_part);
        approvedBTN = findViewById(R.id.appoved_btn);
        rejectedBTN = findViewById(R.id.rejected_btn);
        ttdApprover1 = findViewById(R.id.ttd_approver_1);
        ttdApprover2 = findViewById(R.id.ttd_approver_2);
        ttdApproverHRD = findViewById(R.id.ttd_approver_hrd);
        namaApprover1 = findViewById(R.id.nama_approver_1);
        tglApprover1 = findViewById(R.id.tgl_approver_1);
        namaApprover2 = findViewById(R.id.nama_approver_2);
        namaApproverHRD = findViewById(R.id.nama_approver_hrd);
        tglApprover2 = findViewById(R.id.tgl_approver_2);
        tglApproverHRD = findViewById(R.id.tgl_approver_hrd);
        rejectedMark = findViewById(R.id.rejected_mark);
        acceptedMark = findViewById(R.id.accepted_mark);

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

        approvedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Setujui?")
                        .setContentText("Yakin untuk disetujui sekarang?")
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

                                pDialog = new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        checkSignature();
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
                new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Tolak?")
                        .setContentText("Yakin untuk tolak permohonan?")
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

                                pDialog = new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanCutiActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        rejectedAction();

                                    }
                                }.start();

                            }
                        })
                        .show();

            }
        });

        getDataDetailPermohonan();

    }

    private void checkSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cek_ttd_digital";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Available")){
                                String signature = data.getString("data");
                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature;
                                approvedAction();
                            } else {
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Anda belum mengisi tanda tangan digital. Harap isi terlebih dahulu")
                                        .setCancelText(" BATAL ")
                                        .setConfirmText("LANJUT")
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
                                                statusKondisi = "1";
                                                Intent intent = new Intent(DetailPermohonanCutiActivity.this, DigitalSignatureActivity.class);
                                                intent.putExtra("kode", "form");
                                                startActivity(intent);
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.WARNING_TYPE);
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

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

                                String status_approve = detail.getString("status_approve");
                                String status_approve_kadept = detail.getString("status_approve_kadept");
                                String status_approve_hrd = detail.getString("status_approve_hrd");

                                nikPemohon = NIK;

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
                                    if(sharedPrefManager.getSpIdJabatan().equals("10")) {
                                        String nik_approver = detail.getString("nik_approver");
                                        String approver = detail.getString("approver");
                                        String signature_approver = detail.getString("signature_approver");
                                        String timestamp_approve = detail.getString("timestamp_approve");

                                        String url_approver_1 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver;

                                        Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdApprover1);

                                        namaApprover1.setText(approver);

                                        if(!timestamp_approve.equals("null")){
                                            tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                        } else {
                                            tglApprover1.setVisibility(View.GONE);
                                        }

                                        String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                        String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                        String approver_kadept = detail.getString("approver_kadept");
                                        String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                        String url_approver_2 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                        Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdApprover2);

                                        namaApprover2.setText(approver_kadept);

                                        if(!timestamp_approve_kadept.equals("null")){
                                            tglApprover2.setText(timestamp_approve_kadept.substring(8,10)+"/"+timestamp_approve_kadept.substring(5,7)+"/"+timestamp_approve_kadept.substring(0,4));
                                        } else {
                                            tglApprover2.setVisibility(View.GONE);
                                        }

                                        if(status_approve_hrd.equals("1")){
                                            acceptedMark.setVisibility(View.VISIBLE);
                                            String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                            String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                            String approver_hrd = detail.getString("approver_hrd");
                                            String signature_approver_hrd = detail.getString("signature_approver_hrd");

                                            String url_approver_hrd = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                            Picasso.get().load(url_approver_hrd).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApproverHRD);

                                            namaApproverHRD.setText(approver_hrd);

                                            if(!timestamp_approve_hrd.equals("null")){
                                                tglApproverHRD.setText(timestamp_approve_hrd.substring(8,10)+"/"+timestamp_approve_hrd.substring(5,7)+"/"+timestamp_approve_hrd.substring(0,4));
                                            } else {
                                                tglApproverHRD.setVisibility(View.GONE);
                                            }

                                        } else if(status_approve_hrd.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                            rejectedMark.setVisibility(View.VISIBLE);
                                        } else {
                                            actionPart.setVisibility(View.GONE);
                                        }

                                    } else if(sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25")){
                                        String nik_approver = detail.getString("nik_approver");
                                        String approver = detail.getString("approver");
                                        String signature_approver = detail.getString("signature_approver");
                                        String timestamp_approve = detail.getString("timestamp_approve");

                                        String url_approver_1 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver;

                                        Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdApprover1);

                                        namaApprover1.setText(approver);

                                        if(!timestamp_approve.equals("null")){
                                            tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                        } else {
                                            tglApprover1.setVisibility(View.GONE);
                                        }

                                        if(status_approve_kadept.equals("1")) {
                                            String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                            String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                            String approver_kadept = detail.getString("approver_kadept");
                                            String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                            String url_approver_2 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                            Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover2);

                                            namaApprover2.setText(approver_kadept);

                                            if(!timestamp_approve_kadept.equals("null")){
                                                tglApprover2.setText(timestamp_approve_kadept.substring(8,10)+"/"+timestamp_approve_kadept.substring(5,7)+"/"+timestamp_approve_kadept.substring(0,4));
                                            } else {
                                                tglApprover2.setVisibility(View.GONE);
                                            }

                                            if(status_approve_hrd.equals("1")){
                                                acceptedMark.setVisibility(View.VISIBLE);
                                                String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                                String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                                String approver_hrd = detail.getString("approver_hrd");
                                                String signature_approver_hrd = detail.getString("signature_approver_hrd");

                                                String url_approver_hrd = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                                Picasso.get().load(url_approver_hrd).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdApproverHRD);

                                                namaApproverHRD.setText(approver_hrd);

                                                if(!timestamp_approve_hrd.equals("null")){
                                                    tglApproverHRD.setText(timestamp_approve_hrd.substring(8,10)+"/"+timestamp_approve_hrd.substring(5,7)+"/"+timestamp_approve_hrd.substring(0,4));
                                                } else {
                                                    tglApproverHRD.setVisibility(View.GONE);
                                                }

                                            } else if(status_approve_hrd.equals("2")){
                                                actionPart.setVisibility(View.GONE);
                                                rejectedMark.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }

                                        } else if(status_approve_kadept.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                            rejectedMark.setVisibility(View.VISIBLE);
                                        } else {
                                            actionPart.setVisibility(View.GONE);
                                        }

                                    } else {
                                        actionPart.setVisibility(View.GONE);
                                        // noted
                                    }
                                }
                                else {
                                    if (NIK.equals(sharedPrefManager.getSpNik())){
                                        actionRead();
                                        if(sharedPrefManager.getSpIdJabatan().equals("10")) {
                                            String nik_approver = detail.getString("nik_approver");
                                            String approver = detail.getString("approver");
                                            String signature_approver = detail.getString("signature_approver");
                                            String timestamp_approve = detail.getString("timestamp_approve");

                                            String url_approver_1 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver;

                                            Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover1);

                                            namaApprover1.setText(approver);

                                            if(!timestamp_approve.equals("null")){
                                                tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                            } else {
                                                tglApprover1.setVisibility(View.GONE);
                                            }

                                            String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                            String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                            String approver_kadept = detail.getString("approver_kadept");
                                            String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                            String url_approver_2 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                            Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover2);

                                            namaApprover2.setText(approver_kadept);

                                            if(!timestamp_approve_kadept.equals("null")){
                                                tglApprover2.setText(timestamp_approve_kadept.substring(8,10)+"/"+timestamp_approve_kadept.substring(5,7)+"/"+timestamp_approve_kadept.substring(0,4));
                                            } else {
                                                tglApprover2.setVisibility(View.GONE);
                                            }

                                        } else if(sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25")){
                                            String nik_approver = detail.getString("nik_approver");
                                            String approver = detail.getString("approver");
                                            String signature_approver = detail.getString("signature_approver");
                                            String timestamp_approve = detail.getString("timestamp_approve");

                                            String url_approver_1 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver;

                                            Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover1);

                                            namaApprover1.setText(approver);

                                            if(!timestamp_approve.equals("null")){
                                                tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                            } else {
                                                tglApprover1.setVisibility(View.GONE);
                                            }

                                            if(status_approve_kadept.equals("1")) {
                                                String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                                String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                                String approver_kadept = detail.getString("approver_kadept");
                                                String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                                String url_approver_2 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                                Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdApprover2);

                                                namaApprover2.setText(approver_kadept);

                                                if(!timestamp_approve_kadept.equals("null")){
                                                    tglApprover2.setText(timestamp_approve_kadept.substring(8,10)+"/"+timestamp_approve_kadept.substring(5,7)+"/"+timestamp_approve_kadept.substring(0,4));
                                                } else {
                                                    tglApprover2.setVisibility(View.GONE);
                                                }

                                                if(status_approve_hrd.equals("1")){
                                                    acceptedMark.setVisibility(View.VISIBLE);
                                                    String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                                    String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                                    String approver_hrd = detail.getString("approver_hrd");
                                                    String signature_approver_hrd = detail.getString("signature_approver_hrd");

                                                    String url_approver_hrd = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                                    Picasso.get().load(url_approver_hrd).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(ttdApproverHRD);

                                                    namaApproverHRD.setText(approver_hrd);

                                                    if(!timestamp_approve_hrd.equals("null")){
                                                        tglApproverHRD.setText(timestamp_approve_hrd.substring(8,10)+"/"+timestamp_approve_hrd.substring(5,7)+"/"+timestamp_approve_hrd.substring(0,4));
                                                    } else {
                                                        tglApproverHRD.setVisibility(View.GONE);
                                                    }

                                                } else if(status_approve_hrd.equals("2")){
                                                    actionPart.setVisibility(View.GONE);
                                                    rejectedMark.setVisibility(View.VISIBLE);
                                                } else {
                                                    actionPart.setVisibility(View.GONE);
                                                }

                                            } else if(status_approve_kadept.equals("2")){
                                                actionPart.setVisibility(View.GONE);
                                                rejectedMark.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }

                                        } else {
                                            actionPart.setVisibility(View.GONE);
                                        }

                                    } else {
                                        if(status_approve.equals("1")){
                                            String nik_approver = detail.getString("nik_approver");
                                            String approver = detail.getString("approver");
                                            String signature_approver = detail.getString("signature_approver");
                                            String timestamp_approve = detail.getString("timestamp_approve");

                                            String url_approver_1 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver;

                                            Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover1);

                                            namaApprover1.setText(approver);

                                            if(!timestamp_approve.equals("null")){
                                                tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                            } else {
                                                tglApprover1.setVisibility(View.GONE);
                                            }

                                            if(status_approve_kadept.equals("1")) {
                                                String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                                String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                                String approver_kadept = detail.getString("approver_kadept");
                                                String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                                String url_approver_2 = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                                Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdApprover2);

                                                namaApprover2.setText(approver_kadept);

                                                if(!timestamp_approve_kadept.equals("null")){
                                                    tglApprover2.setText(timestamp_approve_kadept.substring(8,10)+"/"+timestamp_approve_kadept.substring(5,7)+"/"+timestamp_approve_kadept.substring(0,4));
                                                } else {
                                                    tglApprover2.setVisibility(View.GONE);
                                                }

                                                if(status_approve_hrd.equals("1")){
                                                    acceptedMark.setVisibility(View.VISIBLE);
                                                    String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                                    String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                                    String approver_hrd = detail.getString("approver_hrd");
                                                    String signature_approver_hrd = detail.getString("signature_approver_hrd");

                                                    String url_approver_hrd = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature_approver_kadept;

                                                    Picasso.get().load(url_approver_hrd).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(ttdApproverHRD);

                                                    namaApproverHRD.setText(approver_hrd);

                                                    if(!timestamp_approve_hrd.equals("null")){
                                                        tglApproverHRD.setText(timestamp_approve_hrd.substring(8,10)+"/"+timestamp_approve_hrd.substring(5,7)+"/"+timestamp_approve_hrd.substring(0,4));
                                                    } else {
                                                        tglApproverHRD.setVisibility(View.GONE);
                                                    }

                                                } else if(status_approve_hrd.equals("2")){
                                                    actionPart.setVisibility(View.GONE);
                                                    rejectedMark.setVisibility(View.VISIBLE);
                                                } else {
                                                    actionPart.setVisibility(View.GONE);
                                                }

                                            } else if(status_approve_kadept.equals("2")){
                                                actionPart.setVisibility(View.GONE);
                                                rejectedMark.setVisibility(View.VISIBLE);
                                            } else {
                                                if(sharedPrefManager.getSpIdJabatan().equals("10")) {
                                                    actionPart.setVisibility(View.VISIBLE);
                                                } else {
                                                    actionPart.setVisibility(View.GONE);
                                                }
                                            }

                                        } else if (status_approve.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                            rejectedMark.setVisibility(View.VISIBLE);
                                        } else {
                                            actionPart.setVisibility(View.VISIBLE);
                                        }
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

    private void recheckSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cek_ttd_digital";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Available")){
                                approvedAction();
                            } else {
                                getDataDetailPermohonan();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void approvedAction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/approve_action_cuti";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                getDataDetailPermohonan();
                                actionPart.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Disetujui")
                                    .setConfirmText("    OK    ")
                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Disetujui")
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_izin_record", idIzinRecord);
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("timestamp_approve", getTimeStamp());
                params.put("updated_at", getTimeStamp());

                if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")){
                    params.put("action", "kabag");
                } else if(sharedPrefManager.getSpIdJabatan().equals("10")){
                    params.put("action", "kadep");
                }

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void rejectedAction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/reject_action";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                getDataDetailPermohonan();
                                actionPart.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Ditolak")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Ditolak")
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_izin_record", idIzinRecord);
                params.put("updated_at", getTimeStamp());

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(statusKondisi.equals("1")){
            statusKondisi = "0";
            recheckSignature();
        }
    }

}