package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.gelora.absensi.kalert.KAlertDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailFormSdmActivity extends AppCompatActivity {

    LinearLayout ttdDireksiPart, downloadBTN, backBTN, actionBar, accMark, rejMark, actionPart, rejectedBTN, appovedBTN;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView labelDireksi, markCeklis1, markCeklis2, markCeklis3, markCeklis4, markCeklis5, markCeklis6, markCeklis7;
    TextView namaBaruTV, unitBisnisBaruTV, departemenBaruTV, bagianBaruTV, jabatanBaruTV, komponenGajiBaruTV;
    TextView namaLamaTV, unitBisnisLamaTV, departemenLamaTV, bagianLamaTV, jabatanLamaTV, komponenGajiLamaTV;
    TextView jabatanSlashDepartemenTV, deskripsiSlashJabatanTV, syaratPenerimaanTV, tglDibutuhkan1TV, tglPemenuhan1TV, tglDibutuhkan2TV, tglPemenuhan2TV;
    TextView syaratYaTV, syaratTidakTV;
    TextView persetujuanYaTV, persetujuanTidakTV;
    TextView lainLainTV, jabatanLamaDetailTV, jabatanBaruDetailTV, tglPengangkatanJabatanLamaDetailTV, tglPengangkatanJabatanBaruDetailTV, alasanPengangkatanTV, catatanTV, namaKabagTV, namaKadeptTV, namaDirekturTV, tglApproveKabag, tglApproveKadept, tglApproveDireksi, tglPenerimaanTV;
    ImageView ttdPemohon, ttdKadept, ttdDireksi, ttdPenerima;
    String idData = "", urlDownload = "";
    KAlertDialog pDialog;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        downloadBTN = findViewById(R.id.download_btn);

        accMark = findViewById(R.id.acc_mark);
        rejMark = findViewById(R.id.rej_mark);

        markCeklis1 = findViewById(R.id.mark_ceklis_1);
        markCeklis2 = findViewById(R.id.mark_ceklis_2);
        markCeklis3 = findViewById(R.id.mark_ceklis_3);
        markCeklis4 = findViewById(R.id.mark_ceklis_4);
        markCeklis5 = findViewById(R.id.mark_ceklis_5);
        markCeklis6 = findViewById(R.id.mark_ceklis_6);
        markCeklis7 = findViewById(R.id.mark_ceklis_7);

        namaBaruTV = findViewById(R.id.nama_baru_tv);
        unitBisnisBaruTV = findViewById(R.id.unit_bisnis_baru_tv);
        departemenBaruTV = findViewById(R.id.departemen_baru_tv);
        bagianBaruTV = findViewById(R.id.bagian_baru_tv);
        jabatanBaruTV = findViewById(R.id.jabatan_baru_tv);
        komponenGajiBaruTV = findViewById(R.id.komponen_gaji_baru_tv);

        namaLamaTV = findViewById(R.id.nama_lama_tv);
        unitBisnisLamaTV = findViewById(R.id.unit_bisnis_lama_tv);
        departemenLamaTV = findViewById(R.id.departemen_lama_tv);
        bagianLamaTV = findViewById(R.id.bagian_lama_tv);
        jabatanLamaTV = findViewById(R.id.jabatan_lama_tv);
        komponenGajiLamaTV = findViewById(R.id.komponen_gaji_lama_tv);

        jabatanSlashDepartemenTV = findViewById(R.id.jabatan_slash_departemen_tv);
        deskripsiSlashJabatanTV = findViewById(R.id.deskripsi_slash_jabatan_tv);
        syaratPenerimaanTV = findViewById(R.id.syarat_penerimaan_tv);
        tglDibutuhkan1TV = findViewById(R.id.tgl_dibutuhkan_1_tv);
        tglPemenuhan1TV = findViewById(R.id.tgl_pemenuhan_1_tv);
        tglDibutuhkan2TV = findViewById(R.id.tgl_dibutuhkan_2_tv);
        tglPemenuhan2TV = findViewById(R.id.tgl_pemenuhan_2_tv);

        jabatanLamaDetailTV = findViewById(R.id.jabatan_lama_detail_tv);
        jabatanBaruDetailTV = findViewById(R.id.jabatan_baru_detail_tv);
        tglPengangkatanJabatanLamaDetailTV = findViewById(R.id.tgl_pengangkatan_jabatan_lama_detail_tv);
        tglPengangkatanJabatanBaruDetailTV = findViewById(R.id.tgl_pengangkatan_jabatan_baru_detail_tv);
        alasanPengangkatanTV = findViewById(R.id.alasan_pengangkatan_tv);

        syaratYaTV = findViewById(R.id.syarat_ya_tv);
        syaratTidakTV = findViewById(R.id.syarat_tidak_tv);
        persetujuanYaTV = findViewById(R.id.persetujuan_ya_tv);
        persetujuanTidakTV = findViewById(R.id.persetujuan_tidak_tv);

        catatanTV = findViewById(R.id.catatan_tv);
        lainLainTV = findViewById(R.id.lain_lain_tv);

        namaKabagTV = findViewById(R.id.nama_kabag_tv);
        namaKadeptTV = findViewById(R.id.nama_kadept_tv);
        namaDirekturTV = findViewById(R.id.nama_direktur_tv);

        ttdPemohon = findViewById(R.id.ttd_pemohon);
        ttdKadept = findViewById(R.id.ttd_kadept);
        ttdDireksi = findViewById(R.id.ttd_direksi);
        ttdPenerima = findViewById(R.id.ttd_penerima);

        tglApproveKabag = findViewById(R.id.tgl_approve_kabag);
        tglApproveKadept = findViewById(R.id.tgl_approve_kadept);
        tglApproveDireksi = findViewById(R.id.tgl_approve_direksi);

        tglPenerimaanTV = findViewById(R.id.tgl_penerimaan_tv);
        labelDireksi = findViewById(R.id.label_direksi);
        ttdDireksiPart = findViewById(R.id.ttd_direksi_part);

        actionPart = findViewById(R.id.action_part);
        appovedBTN = findViewById(R.id.appoved_btn);
        rejectedBTN = findViewById(R.id.rejected_btn);

        idData = getIntent().getExtras().getString("id_data");
        urlDownload = "https://geloraaksara.co.id/absen-online/absen/pdf_formulir_sdm/"+idData;

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
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

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Unduh File?")
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
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlDownload));
                                try {
                                    startActivity(browserIntent);
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .show();
            }
        });

        appovedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Konfirmasi?")
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

                                pDialog = new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien6));
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
                new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Tolak?")
                        .setContentText("Yakin untuk menolak ajuan?")
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

                                pDialog = new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailFormSdmActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        rejectFunc();
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_detail_form_sdm";
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
                                JSONObject dataArray           = data.getJSONObject("data");
                                String id_record               = dataArray.getString("id_record");
                                String keterangan              = dataArray.getString("keterangan");
                                String nik                     = dataArray.getString("nik");
                                String nama                    = dataArray.getString("nama");
                                String unit_bisnis             = dataArray.getString("unit_bisnis");
                                String nama_unit_bisnis        = dataArray.getString("nama_unit_bisnis");
                                String departemen              = dataArray.getString("departemen");
                                String nama_departemen         = dataArray.getString("nama_departemen");
                                String bagian                  = dataArray.getString("bagian");
                                String nama_bagian             = dataArray.getString("nama_bagian");
                                String jabatan                 = dataArray.getString("jabatan");
                                String nama_jabatan            = dataArray.getString("nama_jabatan");
                                String komponen_gaji           = dataArray.getString("komponen_gaji");
                                String nik_lama                = dataArray.getString("nik_lama");
                                String nama_lama               = dataArray.getString("nama_lama");
                                String unit_bisnis_lama        = dataArray.getString("unit_bisnis_lama");
                                String nama_unit_bisnis_lama   = dataArray.getString("nama_unit_bisnis_lama");
                                String departemen_lama         = dataArray.getString("departemen_lama");
                                String nama_departemen_lama    = dataArray.getString("nama_departemen_lama");
                                String bagian_lama             = dataArray.getString("bagian_lama");
                                String nama_bagian_lama        = dataArray.getString("nama_bagian_lama");
                                String jabatan_lama            = dataArray.getString("jabatan_lama");
                                String nama_jabatan_lama       = dataArray.getString("nama_jabatan_lama");
                                String komponen_gaji_lama      = dataArray.getString("komponen_gaji_lama");
                                String status_approve_kabag    = dataArray.getString("status_approve_kabag");
                                String tgl_approve_kabag       = dataArray.getString("tgl_approve_kabag");
                                String approver_kabag          = dataArray.getString("approver_kabag");
                                String nama_kabag              = dataArray.getString("nama_kabag");
                                String ttd_kabag               = dataArray.getString("ttd_kabag");
                                String status_approve_kadept   = dataArray.getString("status_approve_kadept");
                                String tgl_approve_kadept      = dataArray.getString("tgl_approve_kadept");
                                String approver_kadept         = dataArray.getString("approver_kadept");
                                String nama_kadept             = dataArray.getString("nama_kadept");
                                String ttd_kadept              = dataArray.getString("ttd_kadept");
                                String status_approve_direktur = dataArray.getString("status_approve_direktur");
                                String tgl_approve_direktur    = dataArray.getString("tgl_approve_direktur");
                                String approver_direktur       = dataArray.getString("approver_direktur");
                                String nama_direktur           = dataArray.getString("nama_direktur");
                                String ttd_direktur            = dataArray.getString("ttd_direktur");
                                String status_approve_hrd      = dataArray.getString("status_approve_hrd");
                                String tgl_diterima            = dataArray.getString("tgl_diterima");
                                String diterima_oleh           = dataArray.getString("diterima_oleh");
                                String nama_penerima           = dataArray.getString("nama_penerima");
                                String ttd_penerima            = dataArray.getString("ttd_penerima");
                                String catatan                 = dataArray.getString("catatan");
                                String no_frm                  = dataArray.getString("no_frm");
                                String created_at              = dataArray.getString("created_at");
                                String updated_at              = dataArray.getString("updated_at");
                                String dibuat_oleh             = dataArray.getString("dibuat_oleh");

                                String jabatan_departemen      = dataArray.getString("jabatan_departemen");
                                String deskripsi_jabatan       = dataArray.getString("deskripsi_jabatan");
                                String syarat_penerimaan       = dataArray.getString("syarat_penerimaan");
                                String jabatan_lama_detail     = dataArray.getString("jabatan_lama_detail");
                                String nama_jabatan_lama_detail= dataArray.getString("nama_jabatan_lama_detail");
                                String tgl_pengangkatan_lama   = dataArray.getString("tgl_pengangkatan_lama");
                                String jabatan_baru_detail     = dataArray.getString("jabatan_baru_detail");
                                String nama_jabatan_baru_detail= dataArray.getString("nama_jabatan_baru_detail");
                                String tgl_pengangkatan_baru   = dataArray.getString("tgl_pengangkatan_baru");
                                String alasan_pengangkatan     = dataArray.getString("alasan_pengangkatan");
                                String tgl_dibutuhkan          = dataArray.getString("tgl_dibutuhkan");
                                String tgl_pemenuhan           = dataArray.getString("tgl_pemenuhan");
                                String memenuhi_syarat         = dataArray.getString("memenuhi_syarat");
                                String lain_lain               = dataArray.getString("lain_lain");
                                String persetujuan             = dataArray.getString("persetujuan");
                                String id_departemen           = dataArray.getString("id_departemen");

                                if(catatan.equals("null")){
                                    catatan = "";
                                }

                                if(keterangan.equals("1")){
                                    markCeklis1.setText("✓");
                                    labelDireksi.setText("Direktur");
                                    ttdDireksiPart.setVisibility(View.VISIBLE);
                                    namaDirekturTV.setVisibility(View.VISIBLE);
                                    labelDireksi.setVisibility(View.VISIBLE);

                                    unitBisnisBaruTV.setText(nama_unit_bisnis);
                                    departemenBaruTV.setText(nama_departemen);
                                    bagianBaruTV.setText(nama_bagian);
                                    jabatanBaruTV.setText(nama_jabatan);
                                    komponenGajiBaruTV.setText(komponen_gaji);

                                    jabatanSlashDepartemenTV.setText(jabatan_departemen);
                                    deskripsiSlashJabatanTV.setText(deskripsi_jabatan);
                                    syaratPenerimaanTV.setText(syarat_penerimaan);
                                    tglDibutuhkan1TV.setText(tgl_dibutuhkan.substring(8,10)+"/"+tgl_dibutuhkan.substring(5,7)+"/"+tgl_dibutuhkan.substring(0,4));
                                    tglPemenuhan1TV.setText(tgl_pemenuhan.substring(8,10)+"/"+tgl_pemenuhan.substring(5,7)+"/"+tgl_pemenuhan.substring(0,4));

                                    if(status_approve_kabag.equals("1")){
                                        String url_ttd_kabag = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kabag;
                                        Picasso.get().load(url_ttd_kabag).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdPemohon);
                                        namaKabagTV.setText(nama_kabag);
                                        tglApproveKabag.setText(tgl_approve_kabag.substring(8,10)+"/"+tgl_approve_kabag.substring(5,7)+"/"+tgl_approve_kabag.substring(0,4));

                                        if(status_approve_kadept.equals("1")){
                                            String url_ttd_kadept = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kadept;
                                            Picasso.get().load(url_ttd_kadept).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdKadept);
                                            namaKadeptTV.setText(nama_kadept);
                                            tglApproveKadept.setText(tgl_approve_kadept.substring(8,10)+"/"+tgl_approve_kadept.substring(5,7)+"/"+tgl_approve_kadept.substring(0,4));

                                            if(status_approve_direktur.equals("1")){
                                                String url_ttd_direksi = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_direktur;
                                                Picasso.get().load(url_ttd_direksi).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdDireksi);
                                                namaDirekturTV.setText(nama_direktur);
                                                tglApproveDireksi.setText(tgl_approve_direktur.substring(8,10)+"/"+tgl_approve_direktur.substring(5,7)+"/"+tgl_approve_direktur.substring(0,4));

                                                if(status_approve_hrd.equals("1")){
                                                    String url_ttd_penerima = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_penerima;
                                                    Picasso.get().load(url_ttd_penerima).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(ttdPenerima);
                                                    tglPenerimaanTV.setText(tgl_diterima.substring(8,10)+"/"+tgl_diterima.substring(5,7)+"/"+tgl_diterima.substring(0,4));

                                                    accMark.setVisibility(View.VISIBLE);
                                                    rejMark.setVisibility(View.GONE);

                                                } else if(status_approve_hrd.equals("2")){
                                                    accMark.setVisibility(View.GONE);
                                                    rejMark.setVisibility(View.VISIBLE);
                                                }

                                            } else if(status_approve_direktur.equals("2")){
                                                accMark.setVisibility(View.GONE);
                                                rejMark.setVisibility(View.VISIBLE);
                                            }

                                        } else if(status_approve_kadept.equals("2")){
                                            accMark.setVisibility(View.GONE);
                                            rejMark.setVisibility(View.VISIBLE);
                                        } else if(status_approve_kadept.equals("0")){
                                            if(sharedPrefManager.getSpIdHeadDept().equals(id_departemen) && sharedPrefManager.getSpIdJabatan().equals("10")){
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }
                                        }

                                    } else if(status_approve_kabag.equals("2")){
                                        accMark.setVisibility(View.GONE);
                                        rejMark.setVisibility(View.VISIBLE);
                                    }

                                    catatanTV.setText(catatan);

                                } else if(keterangan.equals("2")||keterangan.equals("3")||keterangan.equals("4")){
                                    if(keterangan.equals("2")){
                                        markCeklis2.setText("✓");
                                        labelDireksi.setText("Direktur");
                                        ttdDireksiPart.setVisibility(View.VISIBLE);
                                        namaDirekturTV.setVisibility(View.VISIBLE);
                                        labelDireksi.setVisibility(View.VISIBLE);
                                    } else if(keterangan.equals("3")){
                                        markCeklis3.setText("✓");
                                        labelDireksi.setText("");
                                        ttdDireksiPart.setVisibility(View.GONE);
                                        namaDirekturTV.setVisibility(View.GONE);
                                        labelDireksi.setVisibility(View.GONE);
                                    } else if(keterangan.equals("4")){
                                        markCeklis4.setText("✓");
                                        labelDireksi.setText("");
                                        ttdDireksiPart.setVisibility(View.GONE);
                                        namaDirekturTV.setVisibility(View.GONE);
                                        labelDireksi.setVisibility(View.GONE);
                                    }

                                    namaBaruTV.setText(nama);
                                    unitBisnisBaruTV.setText(nama_unit_bisnis);
                                    departemenBaruTV.setText(nama_departemen);
                                    bagianBaruTV.setText(nama_bagian);
                                    jabatanBaruTV.setText(nama_jabatan);
                                    komponenGajiBaruTV.setText(komponen_gaji);

                                    namaLamaTV.setText(nama_lama);
                                    unitBisnisLamaTV.setText(nama_unit_bisnis_lama);
                                    departemenLamaTV.setText(nama_departemen_lama);
                                    bagianLamaTV.setText(nama_bagian_lama);
                                    jabatanLamaTV.setText(nama_jabatan_lama);
                                    komponenGajiLamaTV.setText(komponen_gaji_lama);

                                    if(memenuhi_syarat.equals("1")){
                                        syaratYaTV.setText("✓");
                                        syaratTidakTV.setText("");
                                    } else if(memenuhi_syarat.equals("2")){
                                        syaratYaTV.setText("");
                                        syaratTidakTV.setText("✓");
                                    } else {
                                        syaratYaTV.setText("");
                                        syaratTidakTV.setText("");
                                    }

                                    if(status_approve_kabag.equals("1")){
                                        String url_ttd_kabag = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kabag;
                                        Picasso.get().load(url_ttd_kabag).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdPemohon);
                                        namaKabagTV.setText(nama_kabag);
                                        tglApproveKabag.setText(tgl_approve_kabag.substring(8,10)+"/"+tgl_approve_kabag.substring(5,7)+"/"+tgl_approve_kabag.substring(0,4));

                                        if(status_approve_kadept.equals("1")){
                                            String url_ttd_kadept = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kadept;
                                            Picasso.get().load(url_ttd_kadept).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdKadept);
                                            namaKadeptTV.setText(nama_kadept);
                                            tglApproveKadept.setText(tgl_approve_kadept.substring(8,10)+"/"+tgl_approve_kadept.substring(5,7)+"/"+tgl_approve_kadept.substring(0,4));

                                            if(keterangan.equals("2")){
                                                if(status_approve_direktur.equals("1")){
                                                    String url_ttd_direksi = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_direktur;
                                                    Picasso.get().load(url_ttd_direksi).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(ttdDireksi);
                                                    namaDirekturTV.setText(nama_direktur);
                                                    tglApproveDireksi.setText(tgl_approve_direktur.substring(8,10)+"/"+tgl_approve_direktur.substring(5,7)+"/"+tgl_approve_direktur.substring(0,4));

                                                    if(status_approve_hrd.equals("1")){
                                                        String url_ttd_penerima = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_penerima;
                                                        Picasso.get().load(url_ttd_penerima).networkPolicy(NetworkPolicy.NO_CACHE)
                                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                                .into(ttdPenerima);
                                                        tglPenerimaanTV.setText(tgl_diterima.substring(8,10)+"/"+tgl_diterima.substring(5,7)+"/"+tgl_diterima.substring(0,4));

                                                    } else if(status_approve_hrd.equals("2")){
                                                        accMark.setVisibility(View.GONE);
                                                        rejMark.setVisibility(View.VISIBLE);
                                                    }

                                                } else if(status_approve_direktur.equals("2")){
                                                    accMark.setVisibility(View.GONE);
                                                    rejMark.setVisibility(View.VISIBLE);
                                                }
                                            } else if(keterangan.equals("3")||keterangan.equals("4")){
                                                if(status_approve_hrd.equals("1")){
                                                    String url_ttd_penerima = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_penerima;
                                                    Picasso.get().load(url_ttd_penerima).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(ttdPenerima);
                                                    tglPenerimaanTV.setText(tgl_diterima.substring(8,10)+"/"+tgl_diterima.substring(5,7)+"/"+tgl_diterima.substring(0,4));

                                                } else if(status_approve_hrd.equals("2")){
                                                    accMark.setVisibility(View.GONE);
                                                    rejMark.setVisibility(View.VISIBLE);
                                                }
                                            }

                                        } else if(status_approve_kadept.equals("2")){
                                            accMark.setVisibility(View.GONE);
                                            rejMark.setVisibility(View.VISIBLE);
                                        } else if(status_approve_kadept.equals("0")){
                                            if(sharedPrefManager.getSpIdHeadDept().equals(id_departemen) && sharedPrefManager.getSpIdJabatan().equals("10")){
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }
                                        }

                                    } else if(status_approve_kabag.equals("2")){
                                        accMark.setVisibility(View.GONE);
                                        rejMark.setVisibility(View.VISIBLE);
                                    }

                                    catatanTV.setText(catatan);

                                } else if(keterangan.equals("5")||keterangan.equals("6")){
                                    if(keterangan.equals("5")){
                                        markCeklis5.setText("✓");
                                        labelDireksi.setText("");
                                        ttdDireksiPart.setVisibility(View.GONE);
                                        namaDirekturTV.setVisibility(View.GONE);
                                        labelDireksi.setVisibility(View.GONE);
                                    } else if(keterangan.equals("6")){
                                        markCeklis6.setText("✓");
                                        labelDireksi.setText("");
                                        ttdDireksiPart.setVisibility(View.GONE);
                                        namaDirekturTV.setVisibility(View.GONE);
                                        labelDireksi.setVisibility(View.GONE);
                                    }

                                    namaBaruTV.setText(nama);
                                    unitBisnisBaruTV.setText(nama_unit_bisnis);
                                    departemenBaruTV.setText(nama_departemen);
                                    bagianBaruTV.setText(nama_bagian);
                                    jabatanBaruTV.setText(nama_jabatan);
                                    komponenGajiBaruTV.setText(komponen_gaji);

                                    namaLamaTV.setText(nama_lama);
                                    unitBisnisLamaTV.setText(nama_unit_bisnis_lama);
                                    departemenLamaTV.setText(nama_departemen_lama);
                                    bagianLamaTV.setText(nama_bagian_lama);
                                    jabatanLamaTV.setText(nama_jabatan_lama);
                                    komponenGajiLamaTV.setText(komponen_gaji_lama);

                                    jabatanLamaDetailTV.setText(nama_jabatan_lama_detail);
                                    jabatanBaruDetailTV.setText(nama_jabatan_baru_detail);
                                    tglPengangkatanJabatanLamaDetailTV.setText(tgl_pengangkatan_lama.substring(8,10)+"/"+tgl_pengangkatan_lama.substring(5,7)+"/"+tgl_pengangkatan_lama.substring(0,4));
                                    tglPengangkatanJabatanBaruDetailTV.setText(tgl_pengangkatan_baru.substring(8,10)+"/"+tgl_pengangkatan_baru.substring(5,7)+"/"+tgl_pengangkatan_baru.substring(0,4));
                                    alasanPengangkatanTV.setText(alasan_pengangkatan);

                                    tglDibutuhkan2TV.setText(tgl_dibutuhkan.substring(8,10)+"/"+tgl_dibutuhkan.substring(5,7)+"/"+tgl_dibutuhkan.substring(0,4));
                                    tglPemenuhan2TV.setText(tgl_pemenuhan.substring(8,10)+"/"+tgl_pemenuhan.substring(5,7)+"/"+tgl_pemenuhan.substring(0,4));

                                    if(status_approve_kabag.equals("1")){
                                        String url_ttd_kabag = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kabag;
                                        Picasso.get().load(url_ttd_kabag).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdPemohon);
                                        namaKabagTV.setText(nama_kabag);
                                        tglApproveKabag.setText(tgl_approve_kabag.substring(8,10)+"/"+tgl_approve_kabag.substring(5,7)+"/"+tgl_approve_kabag.substring(0,4));

                                        if(status_approve_kadept.equals("1")){
                                            String url_ttd_kadept = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kadept;
                                            Picasso.get().load(url_ttd_kadept).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdKadept);
                                            namaKadeptTV.setText(nama_kadept);
                                            tglApproveKadept.setText(tgl_approve_kadept.substring(8,10)+"/"+tgl_approve_kadept.substring(5,7)+"/"+tgl_approve_kadept.substring(0,4));

                                            if(status_approve_hrd.equals("1")){
                                                String url_ttd_penerima = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_penerima;
                                                Picasso.get().load(url_ttd_penerima).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdPenerima);
                                                tglPenerimaanTV.setText(tgl_diterima.substring(8,10)+"/"+tgl_diterima.substring(5,7)+"/"+tgl_diterima.substring(0,4));

                                            } else if(status_approve_hrd.equals("2")){
                                                accMark.setVisibility(View.GONE);
                                                rejMark.setVisibility(View.VISIBLE);
                                            }

                                        } else if(status_approve_kadept.equals("2")){
                                            accMark.setVisibility(View.GONE);
                                            rejMark.setVisibility(View.VISIBLE);
                                        } else if(status_approve_kadept.equals("0")){
                                            if(sharedPrefManager.getSpIdHeadDept().equals(id_departemen) && sharedPrefManager.getSpIdJabatan().equals("10")){
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }
                                        }

                                    } else if(status_approve_kabag.equals("2")){
                                        accMark.setVisibility(View.GONE);
                                        rejMark.setVisibility(View.VISIBLE);
                                    }

                                    catatanTV.setText(catatan);

                                } else if(keterangan.equals("7")){
                                    markCeklis7.setText("✓");
                                    labelDireksi.setText("");
                                    ttdDireksiPart.setVisibility(View.GONE);
                                    namaDirekturTV.setVisibility(View.GONE);
                                    labelDireksi.setVisibility(View.GONE);

                                    namaBaruTV.setText(nama);
                                    unitBisnisBaruTV.setText(nama_unit_bisnis);
                                    departemenBaruTV.setText(nama_departemen);
                                    bagianBaruTV.setText(nama_bagian);
                                    jabatanBaruTV.setText(nama_jabatan);
                                    komponenGajiBaruTV.setText(komponen_gaji);

                                    namaLamaTV.setText(nama_lama);
                                    unitBisnisLamaTV.setText(nama_unit_bisnis_lama);
                                    departemenLamaTV.setText(nama_departemen_lama);
                                    bagianLamaTV.setText(nama_bagian_lama);
                                    jabatanLamaTV.setText(nama_jabatan_lama);
                                    komponenGajiLamaTV.setText(komponen_gaji_lama);

                                    lainLainTV.setText(lain_lain);
                                    if(persetujuan.equals("1")){
                                        persetujuanYaTV.setText("✓");
                                        persetujuanTidakTV.setText("");
                                    } else if(persetujuan.equals("2")){
                                        persetujuanYaTV.setText("");
                                        persetujuanTidakTV.setText("✓");
                                    } else {
                                        persetujuanYaTV.setText("");
                                        persetujuanTidakTV.setText("");
                                    }

                                    if(status_approve_kabag.equals("1")){
                                        String url_ttd_kabag = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kabag;
                                        Picasso.get().load(url_ttd_kabag).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdPemohon);
                                        namaKabagTV.setText(nama_kabag);
                                        tglApproveKabag.setText(tgl_approve_kabag.substring(8,10)+"/"+tgl_approve_kabag.substring(5,7)+"/"+tgl_approve_kabag.substring(0,4));

                                        if(status_approve_kadept.equals("1")){
                                            String url_ttd_kadept = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kadept;
                                            Picasso.get().load(url_ttd_kadept).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdKadept);
                                            namaKadeptTV.setText(nama_kadept);
                                            tglApproveKadept.setText(tgl_approve_kadept.substring(8,10)+"/"+tgl_approve_kadept.substring(5,7)+"/"+tgl_approve_kadept.substring(0,4));

                                            if(status_approve_hrd.equals("1")){
                                                String url_ttd_penerima = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_penerima;
                                                Picasso.get().load(url_ttd_penerima).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdPenerima);
                                                tglPenerimaanTV.setText(tgl_diterima.substring(8,10)+"/"+tgl_diterima.substring(5,7)+"/"+tgl_diterima.substring(0,4));

                                            } else if(status_approve_hrd.equals("2")){
                                                accMark.setVisibility(View.GONE);
                                                rejMark.setVisibility(View.VISIBLE);
                                            }

                                        } else if(status_approve_kadept.equals("2")){
                                            accMark.setVisibility(View.GONE);
                                            rejMark.setVisibility(View.VISIBLE);
                                        } else if(status_approve_kadept.equals("0")){
                                            if(sharedPrefManager.getSpIdHeadDept().equals(id_departemen) && sharedPrefManager.getSpIdJabatan().equals("10")){
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }
                                        }

                                    } else if(status_approve_kabag.equals("2")){
                                        accMark.setVisibility(View.GONE);
                                        rejMark.setVisibility(View.VISIBLE);
                                    }

                                    catatanTV.setText(catatan);

                                }

                            } else {
                                new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

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
                                updateApproved();
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
                                                Intent intent = new Intent(DetailFormSdmActivity.this, DigitalSignatureActivity.class);
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

    private void updateApproved(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/update_approve_kadept_form_sdm";
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
                                getData();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("id", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void rejectFunc(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/update_reject_kadept_form_sdm";
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
                                getData();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("id", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailFormSdmActivity.this)
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