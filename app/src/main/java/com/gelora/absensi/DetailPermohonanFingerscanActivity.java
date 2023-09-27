package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailPermohonanFingerscanActivity extends AppCompatActivity {

    String file_url, kode, idPermohonan, keteranganForm = "", statusKondisi = "0", ketLemburStatus = "";
    TextView catatanHRDTV, ketLemburChoiceTV, noPermohonan, tanggalTV, nikNamaTV, deptBagianTV, keteranganTV, alasanTV, pemohonTV, tanggalApproveTV, approverTV, jabatanApproverTV, tanggalApproveHRDTV, approverHRDTV;
    LinearLayout catatanHRDPart, actionBar, downloadBTN, markStatusTidakLembur, markStatusLembur, lemburBTN, tidakLemburBTN, ketLemburBTN, opsiKetLembur, detailKeteranganPart, backBTN, cancelPermohonanBTN, editPermohonanBTN, rejectedMark, acceptedMark, actionPart, approvedBTN, rejectedBTN;
    ImageView ttdPemohon, ttdApprover,ttdApproverHRD;
    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;
    BottomSheetLayout bottomSheet;

    KAlertDialog pDialog;
    private int i = -1;

    LinearLayout dStatusAbsen, dShiftAbsen, dTanggalMasuk, dJamMasuk, dTanggalPulang, dJamPulang, dTitikAbsen, dKetLembur;
    TextView dStatusAbsenTV, dShiftAbsenTV, dTanggalMasukTV, dJamMasukTV, dTanggalPulangTV, dJamPulangTV, dTitikAbsenTV, dKetLemburTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permohonan_fingerscan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        backBTN = findViewById(R.id.back_btn);
        noPermohonan = findViewById(R.id.no_permohonan);
        tanggalTV = findViewById(R.id.tanggal_tv);
        nikNamaTV = findViewById(R.id.nik_nama_tv);
        deptBagianTV = findViewById(R.id.dept_bagian_tv);
        keteranganTV = findViewById(R.id.keterangan_tv);
        alasanTV = findViewById(R.id.alasan_tv);
        ttdPemohon = findViewById(R.id.ttd_pemohon);
        ttdApprover = findViewById(R.id.ttd_approver);
        ttdApproverHRD = findViewById(R.id.ttd_approver_hrd);
        pemohonTV = findViewById(R.id.pemohon_tv);
        tanggalApproveTV = findViewById(R.id.tanggal_approve);
        tanggalApproveHRDTV = findViewById(R.id.tanggal_approve_hrd);
        approverTV = findViewById(R.id.approver_tv);
        jabatanApproverTV = findViewById(R.id.jabatan_approver);
        approverHRDTV = findViewById(R.id.approver_hrd_tv);
        cancelPermohonanBTN = findViewById(R.id.cancel_permohonan_btn);
        editPermohonanBTN = findViewById(R.id.edit_permohonan_btn);
        rejectedMark = findViewById(R.id.rejected_mark);
        acceptedMark = findViewById(R.id.accepted_mark);
        opsiKetLembur = findViewById(R.id.opsi_ket_lembur);
        ketLemburBTN = findViewById(R.id.ket_lembur_btn);
        ketLemburChoiceTV = findViewById(R.id.ket_lembur_choice_tv);
        actionPart = findViewById(R.id.action_part);
        approvedBTN = findViewById(R.id.appoved_btn);
        rejectedBTN = findViewById(R.id.rejected_btn);
        downloadBTN = findViewById(R.id.download_btn);
        catatanHRDPart = findViewById(R.id.catatan_hrd_part);
        catatanHRDTV = findViewById(R.id.catatan_hrd_tv);

        detailKeteranganPart = findViewById(R.id.detail_keterangan_part);
        dStatusAbsen = findViewById(R.id.d_status_absen);
        dShiftAbsen = findViewById(R.id.d_shift_absen);
        dTanggalMasuk = findViewById(R.id.d_tanggal_masuk);
        dJamMasuk = findViewById(R.id.d_jam_masuk);
        dTanggalPulang = findViewById(R.id.d_tanggal_pulang);
        dJamPulang = findViewById(R.id.d_jam_pulang);
        dTitikAbsen = findViewById(R.id.d_titik_absen);
        dStatusAbsenTV = findViewById(R.id.d_status_absen_tv);
        dShiftAbsenTV = findViewById(R.id.d_shift_absen_tv);
        dTanggalMasukTV = findViewById(R.id.d_tanggal_masuk_tv);
        dJamMasukTV = findViewById(R.id.d_jam_masuk_tv);
        dTanggalPulangTV = findViewById(R.id.d_tanggal_pulang_tv);
        dJamPulangTV = findViewById(R.id.d_jam_pulang_tv);
        dTitikAbsenTV = findViewById(R.id.d_titik_absen_tv);
        dKetLembur = findViewById(R.id.d_ket_lembur);
        dKetLemburTV = findViewById(R.id.d_ket_lembur_tv);
        actionBar = findViewById(R.id.action_bar);

        kode = getIntent().getExtras().getString("kode");
        idPermohonan = getIntent().getExtras().getString("id_permohonan");

        file_url = "https://geloraaksara.co.id/absen-online/absen/pdf_form_finger/"+idPermohonan;

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
                        ketLemburStatus = "";
                        ketLemburChoiceTV.setText("Pilih keterangan lembur...");

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

        cancelPermohonanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk membatalkan permohonan fingerscan?")
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

                                pDialog = new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        cancelPermohonan(idPermohonan);
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
        });

        ketLemburBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lemburChoice();
            }
        });

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Perhatian")
                    .setContentText("Unduh File Permohonan?")
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
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(file_url));
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

        approvedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(keteranganForm.equals("3")){
                    if(ketLemburStatus.equals("")){
                        new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap pilih keterangan lembur!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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

                                        pDialog = new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1000, 500) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (DetailPermohonanFingerscanActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (DetailPermohonanFingerscanActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (DetailPermohonanFingerscanActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (DetailPermohonanFingerscanActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (DetailPermohonanFingerscanActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (DetailPermohonanFingerscanActivity.this, R.color.colorGradien6));
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
                } else {
                    new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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

                                    pDialog = new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1000, 500) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailPermohonanFingerscanActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailPermohonanFingerscanActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailPermohonanFingerscanActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailPermohonanFingerscanActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailPermohonanFingerscanActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailPermohonanFingerscanActivity.this, R.color.colorGradien6));
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

            }
        });

        rejectedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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

                                pDialog = new KAlertDialog(DetailPermohonanFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanFingerscanActivity.this, R.color.colorGradien6));
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

    private void getDataDetailPermohonan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_permohonan_fingerscan_detail";
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
                                String nomor = data.getString("nomor");

                                JSONObject detail = data.getJSONObject("data");
                                String id = detail.getString("id");
                                String tanggal = detail.getString("tanggal");
                                String nik = detail.getString("NIK");
                                String nama_karyawan = detail.getString("Nmkaryawan");
                                String bagian = detail.getString("NmDept");
                                String departemen = detail.getString("NmHeadDept");
                                String keterangan = detail.getString("keterangan");
                                String alasan = detail.getString("alasan");
                                String ttd_pemohon = detail.getString("ttd_pemohon");

                                keteranganForm = keterangan;
                                noPermohonan.setText(nomor);
                                tanggalTV.setText(tanggal.substring(8,10)+"/"+tanggal.substring(5,7)+"/"+tanggal.substring(0,4));
                                nikNamaTV.setText(nik+"/"+nama_karyawan);
                                deptBagianTV.setText(departemen+"/"+bagian);

                                if(keterangan.equals("1")){
                                    keteranganTV.setText("Tidak Absen Masuk dan Pulang");
                                } else if(keterangan.equals("2")){
                                    keteranganTV.setText("Tidak Absen Masuk");
                                } else if(keterangan.equals("3")){
                                    keteranganTV.setText("Tidak Absen Pulang");
                                } else if(keterangan.equals("4")){
                                    keteranganTV.setText("Datang Terlambat");
                                } else if(keterangan.equals("5")){
                                    keteranganTV.setText("Pulang Lebih Awal");
                                } else if(keterangan.equals("6")){
                                    keteranganTV.setText("Salah Pilih Shift");
                                } else if(keterangan.equals("7")){
                                    keteranganTV.setText("Tidak Absen Diliburkan");
                                }

                                alasanTV.setText(alasan.replaceAll("\\s+$", ""));

                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_pemohon;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(ttdPemohon);

                                String[] short_name = nama_karyawan.split(" ");

                                if(short_name.length>1){
                                    if(short_name[0].length()<3){
                                        pemohonTV.setText("( "+short_name[1].toUpperCase()+" )");
                                    } else {
                                        pemohonTV.setText("( "+short_name[0].toUpperCase()+" )");
                                    }
                                } else {
                                    pemohonTV.setText("( "+short_name[0].toUpperCase()+" )");
                                }

                                // String shortName = nama_karyawan+" ";
                                // if(shortName.contains(" ")){
                                //    shortName = shortName.substring(0, shortName.indexOf(" "));
                                //    pemohonTV.setText("( "+shortName.toUpperCase()+" )");
                                // }

                                String status_approve = detail.getString("status_approve");

                                if(status_approve.equals("1")){
                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                    editPermohonanBTN.setVisibility(View.GONE);
                                    actionPart.setVisibility(View.GONE);
                                    opsiKetLembur.setVisibility(View.GONE);
                                    rejectedMark.setVisibility(View.GONE);

                                    String nik_approver = detail.getString("approver");
                                    String nama_approver = detail.getString("NmApprover");
                                    String timestamp_approve = detail.getString("timestamp_approve");
                                    String ttd_approver = detail.getString("ttd_approver");
                                    String jabatan_approver = detail.getString("jabatan_approver");

                                    String url_approver = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_approver;

                                    Picasso.get().load(url_approver).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(ttdApprover);

                                    tanggalApproveTV.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(2,4));

                                    String[] short_name_2 = nama_approver.split(" ");

                                    if(short_name_2.length>1){
                                        if(short_name_2[0].length()<3){
                                            approverTV.setText("( "+short_name_2[1].toUpperCase()+" )");
                                        } else {
                                            approverTV.setText("( "+short_name_2[0].toUpperCase()+" )");
                                        }
                                    } else {
                                        approverTV.setText("( "+short_name_2[0].toUpperCase()+" )");
                                    }

                                    // String shortName2 = nama_approver+" ";
                                    // if(shortName2.contains(" ")){
                                    //    shortName2 = shortName2.substring(0, shortName2.indexOf(" "));
                                    //    approverTV.setText("( "+shortName2.toUpperCase()+" )");
                                    // }

                                    jabatanApproverTV.setText(jabatan_approver);

                                    String status_approve_hrd = detail.getString("status_approve_hrd");
                                    if (status_approve_hrd.equals("1")) {
                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);
                                        acceptedMark.setVisibility(View.VISIBLE);
                                        rejectedMark.setVisibility(View.GONE);

                                        String nik_approver_hrd = detail.getString("approver_hrd");
                                        String nama_approver_hrd = detail.getString("NmApproverHRD");
                                        String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                        String ttd_approver_hrd = detail.getString("ttd_approver_hrd");

                                        String url_approver_hrd = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_approver_hrd;

                                        Picasso.get().load(url_approver_hrd).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdApproverHRD);

                                        tanggalApproveHRDTV.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(2,4));

                                        String[] short_name_3 = nama_approver_hrd.split(" ");

                                        if(short_name_3.length>1){
                                            if(short_name_3[0].length()<3){
                                                approverHRDTV.setText("( "+short_name_3[1].toUpperCase()+" )");
                                            } else {
                                                approverHRDTV.setText("( "+short_name_3[0].toUpperCase()+" )");
                                            }
                                        } else {
                                            approverHRDTV.setText("( "+short_name_3[0].toUpperCase()+" )");
                                        }

                                        // String shortName3 = nama_approver_hrd+" ";
                                        // if(shortName3.contains(" ")){
                                        //    shortName3 = shortName3.substring(0, shortName3.indexOf(" "));
                                        //    approverHRDTV.setText("( "+shortName3.toUpperCase()+" )");
                                        // }

                                    } else if (status_approve_hrd.equals("2")){
                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);
                                        acceptedMark.setVisibility(View.GONE);
                                        rejectedMark.setVisibility(View.VISIBLE);

                                        String keterangan_penolakan = detail.getString("keterangan_penolakan");
                                        if(keterangan_penolakan.equals("")||keterangan_penolakan.equals("null")){
                                            catatanHRDPart.setVisibility(View.GONE);
                                        } else {
                                            if(keterangan_penolakan.length()==1){
                                                catatanHRDPart.setVisibility(View.GONE);
                                            } else {
                                                catatanHRDPart.setVisibility(View.VISIBLE);
                                                catatanHRDTV.setText(keterangan_penolakan);
                                            }
                                        }

                                        String nama_approver_hrd = detail.getString("NmApproverHRD");

                                        String[] short_name_3 = nama_approver_hrd.split(" ");

                                        if(short_name_3.length>1){
                                            if(short_name_3[0].length()<3){
                                                approverHRDTV.setText("( "+short_name_3[1].toUpperCase()+" )");
                                            } else {
                                                approverHRDTV.setText("( "+short_name_3[0].toUpperCase()+" )");
                                            }
                                        } else {
                                            approverHRDTV.setText("( "+short_name_3[0].toUpperCase()+" )");
                                        }

                                        // String namaPendek = nama_approver_hrd;
                                        // if(namaPendek.contains(" ")){
                                        //    namaPendek = namaPendek.substring(0, namaPendek.indexOf(" "));
                                        //    approverHRDTV.setText("( "+namaPendek.toUpperCase()+" )");
                                        // }
                                    } else {
                                        acceptedMark.setVisibility(View.GONE);
                                        rejectedMark.setVisibility(View.GONE);

                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);

                                    }

                                } else if(status_approve.equals("2")) {
                                    actionPart.setVisibility(View.GONE);
                                    opsiKetLembur.setVisibility(View.GONE);
                                    rejectedMark.setVisibility(View.VISIBLE);
                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                    editPermohonanBTN.setVisibility(View.GONE);

                                    String nama_approver = detail.getString("NmApprover");
                                    String jabatan_approver = detail.getString("jabatan_approver");

                                    String[] short_name_2 = nama_approver.split(" ");

                                    if(short_name_2.length>1){
                                        if(short_name_2[0].length()<3){
                                            approverTV.setText("( "+short_name_2[1].toUpperCase()+" )");
                                        } else {
                                            approverTV.setText("( "+short_name_2[0].toUpperCase()+" )");
                                        }
                                    } else {
                                        approverTV.setText("( "+short_name_2[0].toUpperCase()+" )");
                                    }

                                    // String shortName2 = nama_approver+" ";
                                    // if(shortName2.contains(" ")){
                                    //    shortName2 = shortName2.substring(0, shortName2.indexOf(" "));
                                    //    approverTV.setText("( "+shortName2.toUpperCase()+" )");
                                    // }

                                    jabatanApproverTV.setText(jabatan_approver);

                                } else {
                                    actionPart.setVisibility(View.VISIBLE);
                                    rejectedMark.setVisibility(View.GONE);
                                    if(keterangan.equals("3")){
                                        opsiKetLembur.setVisibility(View.VISIBLE);
                                    } else {
                                        opsiKetLembur.setVisibility(View.GONE);
                                    }

                                    cancelPermohonanBTN.setVisibility(View.VISIBLE);
                                    //editPermohonanBTN.setVisibility(View.VISIBLE);

                                }

                                if (kode.equals("form")){
                                    actionPart.setVisibility(View.GONE);
                                    opsiKetLembur.setVisibility(View.GONE);
                                } else {
                                    if (nik.equals(sharedPrefManager.getSpNik())){
                                        actionRead();
                                        actionPart.setVisibility(View.GONE);
                                        opsiKetLembur.setVisibility(View.GONE);
                                    } else {
                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);
                                        if(status_approve.equals("1")){
                                            actionPart.setVisibility(View.GONE);
                                            opsiKetLembur.setVisibility(View.GONE);
                                        } else if (status_approve.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                            opsiKetLembur.setVisibility(View.GONE);
                                        } else {
                                            actionPart.setVisibility(View.VISIBLE);
                                            if(keterangan.equals("3")){
                                                opsiKetLembur.setVisibility(View.VISIBLE);
                                            } else {
                                                opsiKetLembur.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }

                                if(keterangan.equals("1")) {
                                    detailKeteranganPart.setVisibility(View.VISIBLE);
                                    JSONObject detail_keterangan = data.getJSONObject("detail_keterangan");
                                    String nama_status    = detail_keterangan.getString("nama_status");
                                    String kode_status    = detail_keterangan.getString("kode_status");
                                    String nama_shift     = detail_keterangan.getString("nama_shift");
                                    String shift_datang   = detail_keterangan.getString("shift_datang");
                                    String shift_pulang   = detail_keterangan.getString("shift_pulang");
                                    String tanggal_masuk  = detail_keterangan.getString("tanggal_masuk");
                                    String jam_masuk      = detail_keterangan.getString("jam_masuk");
                                    String tanggal_pulang = detail_keterangan.getString("tanggal_pulang");
                                    String jam_pulang     = detail_keterangan.getString("jam_pulang");
                                    String titik_absen    = detail_keterangan.getString("titik_absen");

                                    dStatusAbsen.setVisibility(View.VISIBLE);
                                    dShiftAbsen.setVisibility(View.VISIBLE);
                                    dTanggalMasuk.setVisibility(View.VISIBLE);
                                    dJamMasuk.setVisibility(View.VISIBLE);
                                    dTanggalPulang.setVisibility(View.VISIBLE);
                                    dJamPulang.setVisibility(View.VISIBLE);
                                    dTitikAbsen.setVisibility(View.VISIBLE);

                                    dStatusAbsenTV.setText(nama_status+" ("+kode_status+")");
                                    dShiftAbsenTV.setText(nama_shift+" ("+shift_datang+" - "+shift_pulang+")");
                                    dTanggalMasukTV.setText(tanggal_masuk.substring(8,10)+"/"+tanggal_masuk.substring(5,7)+"/"+tanggal_masuk.substring(0,4));
                                    dJamMasukTV.setText(jam_masuk);
                                    dTanggalPulangTV.setText(tanggal_pulang.substring(8,10)+"/"+tanggal_pulang.substring(5,7)+"/"+tanggal_pulang.substring(0,4));
                                    dJamPulangTV.setText(jam_pulang);
                                    dTitikAbsenTV.setText(titik_absen);
                                } else if(keterangan.equals("2")){
                                    detailKeteranganPart.setVisibility(View.VISIBLE);
                                    JSONObject detail_keterangan = data.getJSONObject("detail_keterangan");
                                    String nama_status    = detail_keterangan.getString("nama_status");
                                    String kode_status    = detail_keterangan.getString("kode_status");
                                    String nama_shift     = detail_keterangan.getString("nama_shift");
                                    String shift_datang   = detail_keterangan.getString("shift_datang");
                                    String shift_pulang   = detail_keterangan.getString("shift_pulang");
                                    String tanggal_masuk  = detail_keterangan.getString("tanggal_masuk");

                                    dStatusAbsen.setVisibility(View.VISIBLE);
                                    dShiftAbsen.setVisibility(View.VISIBLE);
                                    dTanggalMasuk.setVisibility(View.VISIBLE);

                                    dStatusAbsenTV.setText(nama_status+" ("+kode_status+")");
                                    dShiftAbsenTV.setText(nama_shift+" ("+shift_datang+" - "+shift_pulang+")");
                                    dTanggalMasukTV.setText(tanggal_masuk.substring(8,10)+"/"+tanggal_masuk.substring(5,7)+"/"+tanggal_masuk.substring(0,4));
                                } else if(keterangan.equals("3")){
                                    detailKeteranganPart.setVisibility(View.VISIBLE);
                                    JSONObject detail_keterangan = data.getJSONObject("detail_keterangan");
                                    String nama_status    = detail_keterangan.getString("nama_status");
                                    String kode_status    = detail_keterangan.getString("kode_status");
                                    String nama_shift     = detail_keterangan.getString("nama_shift");
                                    String shift_datang   = detail_keterangan.getString("shift_datang");
                                    String shift_pulang   = detail_keterangan.getString("shift_pulang");
                                    String tanggal_pulang = detail_keterangan.getString("tanggal_pulang");
                                    String jam_pulang     = detail_keterangan.getString("jam_pulang");
                                    String titik_absen    = detail_keterangan.getString("titik_absen");

                                    dStatusAbsen.setVisibility(View.VISIBLE);
                                    dShiftAbsen.setVisibility(View.VISIBLE);
                                    dTanggalPulang.setVisibility(View.VISIBLE);
                                    dJamPulang.setVisibility(View.VISIBLE);
                                    dTitikAbsen.setVisibility(View.VISIBLE);

                                    dStatusAbsenTV.setText(nama_status+" ("+kode_status+")");
                                    dShiftAbsenTV.setText(nama_shift+" ("+shift_datang+" - "+shift_pulang+")");
                                    dTanggalPulangTV.setText(tanggal_pulang.substring(8,10)+"/"+tanggal_pulang.substring(5,7)+"/"+tanggal_pulang.substring(0,4));
                                    dJamPulangTV.setText(jam_pulang);
                                    dTitikAbsenTV.setText(titik_absen);

                                    if(status_approve.equals("1")){
                                        String status_lembur = detail.getString("status_lembur");
                                        dKetLembur.setVisibility(View.VISIBLE);
                                        if(status_lembur.equals("1")){
                                            dKetLemburTV.setText("Lembur");
                                        } else if(status_lembur.equals("0")){
                                            dKetLemburTV.setText("Tidak Lembur");
                                        }
                                    } else {
                                        dKetLembur.setVisibility(View.GONE);
                                    }

                                } else if(keterangan.equals("4")){
                                    detailKeteranganPart.setVisibility(View.VISIBLE);
                                    JSONObject detail_keterangan = data.getJSONObject("detail_keterangan");
                                    String nama_status    = detail_keterangan.getString("nama_status");
                                    String kode_status    = detail_keterangan.getString("kode_status");
                                    String nama_shift     = detail_keterangan.getString("nama_shift");
                                    String shift_datang   = detail_keterangan.getString("shift_datang");
                                    String shift_pulang   = detail_keterangan.getString("shift_pulang");
                                    String tanggal_masuk  = detail_keterangan.getString("tanggal_masuk");

                                    dStatusAbsen.setVisibility(View.VISIBLE);
                                    dShiftAbsen.setVisibility(View.VISIBLE);
                                    dTanggalMasuk.setVisibility(View.VISIBLE);

                                    dStatusAbsenTV.setText(nama_status+" ("+kode_status+")");
                                    dShiftAbsenTV.setText(nama_shift+" ("+shift_datang+" - "+shift_pulang+")");
                                    dTanggalMasukTV.setText(tanggal_masuk.substring(8,10)+"/"+tanggal_masuk.substring(5,7)+"/"+tanggal_masuk.substring(0,4));
                                } else if(keterangan.equals("5")){
                                    detailKeteranganPart.setVisibility(View.VISIBLE);
                                    JSONObject detail_keterangan = data.getJSONObject("detail_keterangan");
                                    String nama_status    = detail_keterangan.getString("nama_status");
                                    String kode_status    = detail_keterangan.getString("kode_status");
                                    String nama_shift     = detail_keterangan.getString("nama_shift");
                                    String shift_datang   = detail_keterangan.getString("shift_datang");
                                    String shift_pulang   = detail_keterangan.getString("shift_pulang");

                                    dStatusAbsen.setVisibility(View.VISIBLE);
                                    dShiftAbsen.setVisibility(View.VISIBLE);

                                    dStatusAbsenTV.setText(nama_status+" ("+kode_status+")");
                                    dShiftAbsenTV.setText(nama_shift+" ("+shift_datang+" - "+shift_pulang+")");
                                } else if(keterangan.equals("6")){
                                    detailKeteranganPart.setVisibility(View.VISIBLE);
                                    JSONObject detail_keterangan = data.getJSONObject("detail_keterangan");
                                    String nama_status   = detail_keterangan.getString("nama_status");
                                    String kode_status   = detail_keterangan.getString("kode_status");
                                    String nama_shift    = detail_keterangan.getString("nama_shift");
                                    String shift_datang  = detail_keterangan.getString("shift_datang");
                                    String shift_pulang  = detail_keterangan.getString("shift_pulang");

                                    dStatusAbsen.setVisibility(View.VISIBLE);
                                    dShiftAbsen.setVisibility(View.VISIBLE);

                                    dStatusAbsenTV.setText(nama_status+" ("+kode_status+")");
                                    dShiftAbsenTV.setText(nama_shift+" ("+shift_datang+" - "+shift_pulang+")");
                                } else if(keterangan.equals("7")){
                                    detailKeteranganPart.setVisibility(View.GONE);
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
                params.put("id_permohonan_finger", idPermohonan);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void actionRead() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/read_notif_finger";
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
                params.put("id_permohonan_finger", idPermohonan);
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
                                                Intent intent = new Intent(DetailPermohonanFingerscanActivity.this, DigitalSignatureActivity.class);
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
        final String url = "https://geloraaksara.co.id/absen-online/api/approve_action_finger";
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
                                opsiKetLembur.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Disetujui")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                if(keteranganForm.equals("3")){
                                    opsiKetLembur.setVisibility(View.VISIBLE);
                                } else {
                                    opsiKetLembur.setVisibility(View.GONE);
                                }
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
                params.put("id_permohonan_finger", idPermohonan);
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("timestamp_approve", getTimeStamp());

                if(keteranganForm.equals("3")){
                    params.put("status_lembur", ketLemburStatus);
                }

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void rejectedAction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/reject_action_finger";
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
                                opsiKetLembur.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Ditolak")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                if(keteranganForm.equals("3")){
                                    opsiKetLembur.setVisibility(View.VISIBLE);
                                } else {
                                    opsiKetLembur.setVisibility(View.GONE);
                                }
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
                params.put("id_permohonan_finger", idPermohonan);
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("timestamp_approve", getTimeStamp());

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void cancelPermohonan(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cancel_finger";
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
                                actionPart.setVisibility(View.GONE);
                                opsiKetLembur.setVisibility(View.GONE);
                                pDialog.setTitleText("Permohonan Dibatalkan")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                if(kode.equals("form")){
                                                    Intent intent = new Intent(DetailPermohonanFingerscanActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    onBackPressed();
                                                }
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                if(keteranganForm.equals("3")){
                                    opsiKetLembur.setVisibility(View.VISIBLE);
                                } else {
                                    opsiKetLembur.setVisibility(View.GONE);
                                }
                                pDialog.setTitleText("Permohonan Gagal Dibatalkan")
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
                params.put("id", id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void lemburChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(DetailPermohonanFingerscanActivity.this).inflate(R.layout.layout_opsi_lembur, bottomSheet, false));
        lemburBTN = findViewById(R.id.lembur_btn);
        tidakLemburBTN = findViewById(R.id.tidak_lembur_btn);
        markStatusLembur = findViewById(R.id.mark_status_lembur);
        markStatusTidakLembur = findViewById(R.id.mark_status_tidak_lembur);

        if (ketLemburStatus.equals("1")){
            lemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option_choice));
            tidakLemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option));
            markStatusLembur.setVisibility(View.VISIBLE);
            markStatusTidakLembur.setVisibility(View.GONE);
        } else if (ketLemburStatus.equals("0")){
            lemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option));
            tidakLemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option_choice));
            markStatusLembur.setVisibility(View.GONE);
            markStatusTidakLembur.setVisibility(View.VISIBLE);
        } else {
            lemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option));
            tidakLemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option));
            markStatusLembur.setVisibility(View.GONE);
            markStatusTidakLembur.setVisibility(View.GONE);
        }

        lemburBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markStatusLembur.setVisibility(View.VISIBLE);
                markStatusTidakLembur.setVisibility(View.GONE);
                lemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option_choice));
                tidakLemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option));
                ketLemburStatus = "1";
                ketLemburChoiceTV.setText("Lembur");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        tidakLemburBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markStatusLembur.setVisibility(View.GONE);
                markStatusTidakLembur.setVisibility(View.VISIBLE);
                tidakLemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option_choice));
                lemburBTN.setBackground(ContextCompat.getDrawable(DetailPermohonanFingerscanActivity.this, R.drawable.shape_option));
                ketLemburStatus = "0";
                ketLemburChoiceTV.setText("Tidak Lembur");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private String getTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void connectionFailed(){
        CookieBar.build(DetailPermohonanFingerscanActivity.this)
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