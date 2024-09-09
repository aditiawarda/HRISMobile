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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    TextView jabatanApprover1, jabatanApprover2, catatanHRDTV, lampiranTV, noted2TV, noted1TV, tglApprover1, tglApprover2, tglApproverHRD, namaApprover1, namaApproverHRD, namaApprover2, namaKaryawanTV, namaPemohonTV, jabatanTV, bagianTV, mulaiBergabungTV, nikTV, statusKaryawanTV, tipeCutiTV, alamatTV, noHpTV, karyawanPenggantiTV, sisaCutiTV, alasanCutiTV, tahunCutiAmbilTV, totalCutiAmbilTV, tahunCutiTV, tglMulaiCutiTV, tglSelesaiCutiTV, totalCutiTV, tglPengajuanTV;
    String nikApprover = "", nikPemohon = "", statusKondisi = "0", idIzinRecord, kode;
    LinearLayout catatanHRDPart, actionBar, editPermohonanBTN, cancelPermohonanBTN, batalWakiliBTN, wakiliBTN, downloadBTN, viewLampiranBTN, backBTN, actionPart, approvedBTN, rejectedBTN, rejectedMark, acceptedMark;
    SwipeRefreshLayout refreshLayout;
    ImageView ttdPemohon, ttdApprover1, ttdApprover2, ttdApproverHRD;
    EditText catatanAtasanTV;
    Bitmap bitmap;
    SharedPrefManager sharedPrefManager;
    View rootview;
    KAlertDialog pDialog;
    private int i = -1;
    String file_url = "";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permohonan_cuti);

        rootview = findViewById(android.R.id.content);
        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
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
        catatanAtasanTV = findViewById(R.id.catatan_tv);
        noted1TV = findViewById(R.id.noted_1_tv);
        noted2TV = findViewById(R.id.noted_2_tv);
        lampiranTV = findViewById(R.id.lampiran_tv);
        viewLampiranBTN = findViewById(R.id.view_lampiran_btn);
        downloadBTN = findViewById(R.id.download_btn);
        wakiliBTN = findViewById(R.id.wakili_btn);
        batalWakiliBTN = findViewById(R.id.batal_wakili_btn);
        cancelPermohonanBTN = findViewById(R.id.cancel_permohonan_btn);
        editPermohonanBTN = findViewById(R.id.edit_permohonan_btn);
        actionBar = findViewById(R.id.action_bar);
        catatanHRDPart = findViewById(R.id.catatan_hrd_part);
        catatanHRDTV = findViewById(R.id.catatan_hrd_tv);
        jabatanApprover1 = findViewById(R.id.jabatan_approver_1);
        jabatanApprover2 = findViewById(R.id.jabatan_approver_2);

        kode = getIntent().getExtras().getString("kode");
        idIzinRecord = getIntent().getExtras().getString("id_izin");
        file_url = "https://hrisgelora.co.id/absen/pdf_form_cuti/"+idIzinRecord;

        catatanAtasanTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
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

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Unduh File?")
                        .setContentText("Perhatian! file hasil unduh hanya dapat dibuka dengan aplikasi WPS")
                        .setCancelText(" BATAL ")
                        .setConfirmText(" UNDUH ")
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

        editPermohonanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk merubah data permohonan cuti?")
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
                                Intent intent = new Intent(DetailPermohonanCutiActivity.this, EditPermohonanCutiActivity.class);
                                intent.putExtra("id_record", idIzinRecord);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        cancelPermohonanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk membatalkan permohonan cuti?")
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
                                        cancelPermohonan(idIzinRecord);
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
        });

        wakiliBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionPart.setVisibility(View.VISIBLE);
                batalWakiliBTN.setVisibility(View.VISIBLE);
                wakiliBTN.setVisibility(View.GONE);
            }
        });

        batalWakiliBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionPart.setVisibility(View.GONE);
                batalWakiliBTN.setVisibility(View.GONE);
                wakiliBTN.setVisibility(View.VISIBLE);
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

    }

    private void cancelPermohonan(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cancel_cuti";
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
                                pDialog.setTitleText("Permohonan Dibatalkan")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                if(kode.equals("form")){
                                                    Intent intent = new Intent(DetailPermohonanCutiActivity.this, HomeActivity.class);
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

    private void checkSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cek_ttd_digital";
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
                                String url = "https://hrisgelora.co.id/upload/digital_signature/"+signature;
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
        final String url = "https://hrisgelora.co.id/api/get_permohonan_cuti_detail";
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
                                String id_jabatan = detail.getString("id_jabatan");
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
                                String karyawan_pengganti = detail.getString("karyawan_pengganti");
                                String alamat_selama_cuti = detail.getString("alamat_selama_cuti");
                                String no_hp = detail.getString("no_hp");
                                String tanggal = detail.getString("tanggal");
                                String digital_signature = detail.getString("digital_signature");
                                String status_approve = detail.getString("status_approve");
                                String status_approve_kadept = detail.getString("status_approve_kadept");
                                String status_approve_hrd = detail.getString("status_approve_hrd");
                                String catatan1 = detail.getString("catatan1");
                                String catatan2 = detail.getString("catatan2");
                                String lampiran = detail.getString("lampiran");
                                String info_sisa_cuti = data.getString("info_sisa_cuti");

                                if(!catatan1.equals("null") && !catatan1.equals("") && !catatan1.equals(null)){
                                    noted1TV.setText(": "+catatan1+"  ");
                                }

                                if(!catatan2.equals("null") && !catatan2.equals("") && !catatan2.equals(null)){
                                    noted2TV.setText(": "+catatan2+"  ");
                                }

                                if(!lampiran.equals("null") && !lampiran.equals("") && !lampiran.equals(null)){
                                    lampiranTV.setText("Terlampir  ");
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    String url_lampiran = "https://hrisgelora.co.id/upload/lampiran_cuti/"+lampiran;

                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPermohonanCutiActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", url_lampiran);
                                            intent.putExtra("kode", "detail");
                                            intent.putExtra("jenis_detail", "cuti");
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    lampiranTV.setText(".......................................................  ");
                                    viewLampiranBTN.setVisibility(View.GONE);
                                }

                                nikPemohon = NIK;

                                namaPemohonTV.setText(NmKaryawan);
                                namaKaryawanTV.setText(NmKaryawan);
                                jabatanTV.setText(jabatan);
                                bagianTV.setText(bagian);

                                if(!tanggal_bergabung.equals("null")){
                                    mulaiBergabungTV.setText(tanggal_bergabung.substring(8,10)+"/"+tanggal_bergabung.substring(5,7)+"/"+tanggal_bergabung.substring(0,4));
                                } else {
                                    mulaiBergabungTV.setText(tanggal_bergabung);
                                }

                                nikTV.setText(NIK);
                                statusKaryawanTV.setText(status_karyawan);

                                if(info_sisa_cuti.equals("1")){
                                    sisaCutiTV.setText(sisa_cuti_sementara+" Hari");
                                } else {
                                    sisaCutiTV.setText("..........");
                                }

                                tahunCutiAmbilTV.setText("Tahun  "+tahun_cuti_telah_diambil);
                                totalCutiAmbilTV.setText("Total  "+total_cuti_telah_diambil+"  Hari");
                                tahunCutiTV.setText("Tahun  "+tanggal_mulai.substring(0,4));
                                tglMulaiCutiTV.setText("dari  "+tanggal_mulai.substring(8,10)+"/"+tanggal_mulai.substring(5,7)+"/"+tanggal_mulai.substring(0,4));
                                tglSelesaiCutiTV.setText("sampai  "+tanggal_akhir.substring(8,10)+"/"+tanggal_akhir.substring(5,7)+"/"+tanggal_akhir.substring(0,4));

                                String jumlah_hari = data.getString("jumlah_hari");
                                totalCutiTV.setText("Total  "+jumlah_hari+"  Hari");
                                alasanCutiTV.setText(alasan_cuti.replaceAll("\\s+$", ""));
                                alamatTV.setText(alamat_selama_cuti.replaceAll("\\s+$", ""));
                                noHpTV.setText(no_hp.replaceAll("\\s+$", ""));

                                if(tipe_cuti.equals("0")){
                                    tipeCutiTV.setText("Undefined");
                                } else if(tipe_cuti.equals("1")){
                                    tipeCutiTV.setText("Tahunan");
                                } else if(tipe_cuti.equals("2")){
                                    tipeCutiTV.setText("Khusus");
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

                                String url = "https://hrisgelora.co.id/upload/digital_signature/"+digital_signature;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(ttdPemohon);

                                karyawanPenggantiTV.setText(karyawan_pengganti);

                                if (kode.equals("form")){
                                    if(sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || sharedPrefManager.getSpIdJabatan().equals("33") || sharedPrefManager.getSpIdJabatan().equals("35") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
                                        String nik_approver = detail.getString("nik_approver");
                                        String approver = detail.getString("approver");
                                        String jabatan_approver = detail.getString("jabatan_kabag");
                                        String signature_approver = detail.getString("signature_approver");
                                        String timestamp_approve = detail.getString("timestamp_approve");

                                        nikApprover = nik_approver;

                                        String url_approver_1 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver;

                                        Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdApprover1);

                                        namaApprover1.setText(approver);
                                        jabatanApprover1.setText(jabatan_approver);

                                        if(!timestamp_approve.equals("null")){
                                            tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                        } else {
                                            tglApprover1.setVisibility(View.GONE);
                                        }

                                        if(status_approve_kadept.equals("1")) {
                                            cancelPermohonanBTN.setVisibility(View.GONE);
                                            editPermohonanBTN.setVisibility(View.GONE);
                                            String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                            String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                            String approver_kadept = detail.getString("approver_kadept");
                                            String jabatan_kadept = detail.getString("jabatan_kadept");
                                            String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                            String url_approver_2 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_kadept;

                                            Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover2);

                                            namaApprover2.setText(approver_kadept);
                                            jabatanApprover2.setText(jabatan_kadept);

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

                                                String url_approver_hrd = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_kadept;

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

                                                String approver_hrd = detail.getString("approver_hrd");
                                                namaApproverHRD.setText(approver_hrd);

                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }

                                        } else if(status_approve_kadept.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                            rejectedMark.setVisibility(View.VISIBLE);
                                            cancelPermohonanBTN.setVisibility(View.GONE);
                                            editPermohonanBTN.setVisibility(View.GONE);

                                            String approver_kadept = detail.getString("approver_kadept");
                                            String jabatan_kadept = detail.getString("jabatan_kadept");
                                            namaApprover2.setText(approver_kadept);
                                            jabatanApprover2.setText(jabatan_kadept);

                                        } else {
                                            actionPart.setVisibility(View.GONE);
                                            cancelPermohonanBTN.setVisibility(View.VISIBLE);
                                            editPermohonanBTN.setVisibility(View.VISIBLE);

                                            if(id_jabatan.equals("41") || id_jabatan.equals("10") || (NIK.equals("0015141287")||NIK.equals("3294031022"))) {
                                                jabatanApprover2.setText("Direktur Utama");
                                            } else {
                                                if(NIK.equals("0687260508") || NIK.equals("0113010500") || NIK.equals("0499070507") || NIK.equals("0056010793") || NIK.equals("0829030809") || NIK.equals("0552260707") || NIK.equals("3318060323")) {
                                                    jabatanApprover2.setText("General Manager");
                                                } else {
                                                    jabatanApprover2.setText("Kepala Departemen");
                                                }
                                            }
                                        }

                                    } else {
                                        actionPart.setVisibility(View.GONE);
                                        cancelPermohonanBTN.setVisibility(View.VISIBLE);
                                        editPermohonanBTN.setVisibility(View.VISIBLE);
                                        // noted
                                    }
                                }
                                else {
                                    if (NIK.equals(sharedPrefManager.getSpNik())){
                                        actionRead();
                                        if(sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3")|| sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || sharedPrefManager.getSpIdJabatan().equals("33") || sharedPrefManager.getSpIdJabatan().equals("35") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
                                            String nik_approver = detail.getString("nik_approver");
                                            String approver = detail.getString("approver");
                                            String jabatan_approver = detail.getString("jabatan_kabag");
                                            String signature_approver = detail.getString("signature_approver");
                                            String timestamp_approve = detail.getString("timestamp_approve");

                                            nikApprover = nik_approver;

                                            String url_approver_1 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver;

                                            Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover1);

                                            namaApprover1.setText(approver);
                                            jabatanApprover1.setText(jabatan_approver);

                                            if(!timestamp_approve.equals("null")){
                                                tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                            } else {
                                                tglApprover1.setVisibility(View.GONE);
                                            }

                                            if(status_approve_kadept.equals("1")) {
                                                cancelPermohonanBTN.setVisibility(View.GONE);
                                                editPermohonanBTN.setVisibility(View.GONE);
                                                String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                                String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                                String approver_kadept = detail.getString("approver_kadept");
                                                String jabatan_kadept = detail.getString("jabatan_kadept");
                                                String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                                String url_approver_2 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_kadept;

                                                Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdApprover2);

                                                namaApprover2.setText(approver_kadept);
                                                jabatanApprover2.setText(jabatan_kadept);

                                                if(!timestamp_approve_kadept.equals("null")){
                                                    tglApprover2.setText(timestamp_approve_kadept.substring(8,10)+"/"+timestamp_approve_kadept.substring(5,7)+"/"+timestamp_approve_kadept.substring(0,4));
                                                } else {
                                                    tglApprover2.setVisibility(View.GONE);
                                                }

                                                if(status_approve_hrd.equals("1")){
                                                    acceptedMark.setVisibility(View.VISIBLE);
                                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                                    editPermohonanBTN.setVisibility(View.GONE);
                                                    String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                                    String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                                    String approver_hrd = detail.getString("approver_hrd");
                                                    String signature_approver_hrd = detail.getString("signature_approver_hrd");

                                                    String url_approver_hrd = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_hrd;

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
                                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                                    editPermohonanBTN.setVisibility(View.GONE);

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

                                                    String approver_hrd = detail.getString("approver_hrd");
                                                    namaApproverHRD.setText(approver_hrd);

                                                } else {
                                                    actionPart.setVisibility(View.GONE);
                                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                                    editPermohonanBTN.setVisibility(View.GONE);
                                                }

                                            } else if(status_approve_kadept.equals("2")){
                                                actionPart.setVisibility(View.GONE);
                                                rejectedMark.setVisibility(View.VISIBLE);

                                                String approver_kadept = detail.getString("approver_kadept");
                                                String jabatan_kadept = detail.getString("jabatan_kadept");
                                                namaApprover2.setText(approver_kadept);
                                                jabatanApprover2.setText(jabatan_kadept);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                                cancelPermohonanBTN.setVisibility(View.VISIBLE);
                                                editPermohonanBTN.setVisibility(View.VISIBLE);

                                                if(id_jabatan.equals("41") || id_jabatan.equals("10") || (NIK.equals("0015141287")||NIK.equals("3294031022"))) {
                                                    jabatanApprover2.setText("Direktur Utama");
                                                } else {
                                                    if(NIK.equals("0687260508") || NIK.equals("0113010500") || NIK.equals("0499070507") || NIK.equals("0056010793") || NIK.equals("0829030809") || NIK.equals("0552260707") || NIK.equals("3318060323")) {
                                                        jabatanApprover2.setText("General Manager");
                                                    } else {
                                                        jabatanApprover2.setText("Kepala Departemen");
                                                    }
                                                }
                                            }

                                        } else {

                                            if(status_approve.equals("1")){
                                                cancelPermohonanBTN.setVisibility(View.GONE);
                                                editPermohonanBTN.setVisibility(View.GONE);
                                                String nik_approver = detail.getString("nik_approver");
                                                String approver = detail.getString("approver");
                                                String jabatan_approver = detail.getString("jabatan_kabag");
                                                String signature_approver = detail.getString("signature_approver");
                                                String timestamp_approve = detail.getString("timestamp_approve");

                                                nikApprover = nik_approver;

                                                String url_approver_1 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver;

                                                Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdApprover1);

                                                namaApprover1.setText(approver);
                                                jabatanApprover1.setText(jabatan_approver);

                                                if(!timestamp_approve.equals("null")){
                                                    tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                                } else {
                                                    tglApprover1.setVisibility(View.GONE);
                                                }

                                                if(status_approve_kadept.equals("1")) {
                                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                                    editPermohonanBTN.setVisibility(View.GONE);
                                                    String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                                    String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                                    String jabatan_kadept = detail.getString("jabatan_kadept");
                                                    String approver_kadept = detail.getString("approver_kadept");
                                                    String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                                    String url_approver_2 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_kadept;

                                                    Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(ttdApprover2);

                                                    namaApprover2.setText(approver_kadept);
                                                    jabatanApprover2.setText(jabatan_kadept);

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

                                                        String url_approver_hrd = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_hrd;

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

                                                        String approver_hrd = detail.getString("approver_hrd");
                                                        namaApproverHRD.setText(approver_hrd);

                                                    } else {
                                                        actionPart.setVisibility(View.GONE);
                                                        wakiliBTN.setVisibility(View.GONE);
                                                        batalWakiliBTN.setVisibility(View.GONE);
                                                    }

                                                } else if(status_approve_kadept.equals("2")){
                                                    actionPart.setVisibility(View.GONE);
                                                    rejectedMark.setVisibility(View.VISIBLE);
                                                    wakiliBTN.setVisibility(View.GONE);
                                                    batalWakiliBTN.setVisibility(View.GONE);

                                                    String approver_kadept = detail.getString("approver_kadept");
                                                    String jabatan_kadept = detail.getString("jabatan_kadept");
                                                    namaApprover2.setText(approver_kadept);
                                                    jabatanApprover2.setText(jabatan_kadept);

                                                } else {
                                                    if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")) {
                                                        if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")){
                                                            actionPart.setVisibility(View.VISIBLE);
                                                        } else if(sharedPrefManager.getSpIdJabatan().equals("3")){
                                                            actionPart.setVisibility(View.GONE);
                                                            wakiliBTN.setVisibility(View.VISIBLE);
                                                        }
                                                    } else {
                                                        // if(sharedPrefManager.getSpNik().equals("0056010793")){
                                                        //    actionPart.setVisibility(View.GONE);
                                                        //    wakiliBTN.setVisibility(View.VISIBLE);
                                                        // } else {
                                                            actionPart.setVisibility(View.GONE);
                                                        // }
                                                    }

                                                    if(id_jabatan.equals("41") || id_jabatan.equals("10") || (NIK.equals("0015141287")||NIK.equals("3294031022"))) {
                                                        jabatanApprover2.setText("Direktur Utama");
                                                    } else {
                                                        if(NIK.equals("0687260508") || NIK.equals("0113010500") || NIK.equals("0499070507") || NIK.equals("0056010793") || NIK.equals("0829030809") || NIK.equals("0552260707") || NIK.equals("3318060323")) {
                                                            jabatanApprover2.setText("General Manager");
                                                        } else {
                                                            jabatanApprover2.setText("Kepala Departemen");
                                                        }
                                                    }
                                                }

                                            } else if (status_approve.equals("2")){
                                                actionPart.setVisibility(View.GONE);
                                                rejectedMark.setVisibility(View.VISIBLE);
                                                cancelPermohonanBTN.setVisibility(View.GONE);
                                                editPermohonanBTN.setVisibility(View.GONE);

                                                String approver = detail.getString("approver");
                                                namaApprover1.setText(approver);

                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                                cancelPermohonanBTN.setVisibility(View.VISIBLE);
                                                editPermohonanBTN.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    }

                                    else {
                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);
                                        if(status_approve.equals("1")){
                                            String nik_approver = detail.getString("nik_approver");
                                            String approver = detail.getString("approver");
                                            String jabatan_approver = detail.getString("jabatan_kabag");
                                            String signature_approver = detail.getString("signature_approver");
                                            String timestamp_approve = detail.getString("timestamp_approve");

                                            nikApprover = nik_approver;

                                            String url_approver_1 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver;

                                            Picasso.get().load(url_approver_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdApprover1);

                                            namaApprover1.setText(approver);
                                            jabatanApprover1.setText(jabatan_approver);

                                            if(!timestamp_approve.equals("null")){
                                                tglApprover1.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(0,4));
                                            } else {
                                                tglApprover1.setVisibility(View.GONE);
                                            }

                                            if(status_approve_kadept.equals("1")) {
                                                String timestamp_approve_kadept = detail.getString("timestamp_approve_kadept");
                                                String nik_approver_kadept = detail.getString("nik_approver_kadept");
                                                String jabatan_kadept = detail.getString("jabatan_kadept");
                                                String approver_kadept = detail.getString("approver_kadept");
                                                String signature_approver_kadept = detail.getString("signature_approver_kadept");

                                                String url_approver_2 = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_kadept;

                                                Picasso.get().load(url_approver_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdApprover2);

                                                namaApprover2.setText(approver_kadept);
                                                jabatanApprover2.setText(jabatan_kadept);

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

                                                    String url_approver_hrd = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_hrd;

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

                                                    String approver_hrd = detail.getString("approver_hrd");
                                                    namaApproverHRD.setText(approver_hrd);

                                                } else {
                                                    actionPart.setVisibility(View.GONE);
                                                    wakiliBTN.setVisibility(View.GONE);
                                                    batalWakiliBTN.setVisibility(View.GONE);
                                                }

                                            } else if(status_approve_kadept.equals("2")){
                                                actionPart.setVisibility(View.GONE);
                                                rejectedMark.setVisibility(View.VISIBLE);
                                                wakiliBTN.setVisibility(View.GONE);
                                                batalWakiliBTN.setVisibility(View.GONE);

                                                String approver_kadept = detail.getString("approver_kadept");
                                                String jabatan_kadept = detail.getString("jabatan_kadept");
                                                namaApprover2.setText(approver_kadept);
                                                jabatanApprover2.setText(jabatan_kadept);

                                            } else {
                                                if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpIdJabatan().equals("33")) {
                                                    if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpIdJabatan().equals("33")){
                                                        actionPart.setVisibility(View.VISIBLE);
                                                    } else if(sharedPrefManager.getSpIdJabatan().equals("3")){
                                                        actionPart.setVisibility(View.GONE);
                                                        wakiliBTN.setVisibility(View.VISIBLE);
                                                    }
                                                } else {
                                                    // if(sharedPrefManager.getSpNik().equals("0056010793")){
                                                    //     actionPart.setVisibility(View.GONE);
                                                    //     wakiliBTN.setVisibility(View.VISIBLE);
                                                    // } else {
                                                        actionPart.setVisibility(View.GONE);
                                                    // }
                                                }

                                                if(id_jabatan.equals("41") || id_jabatan.equals("10") || (NIK.equals("0015141287")||NIK.equals("3294031022"))) {
                                                    jabatanApprover2.setText("Direktur Utama");
                                                } else {
                                                    if(NIK.equals("0687260508") || NIK.equals("0113010500") || NIK.equals("0499070507") || NIK.equals("0056010793") || NIK.equals("0829030809") || NIK.equals("0552260707") || NIK.equals("3318060323")) {
                                                        jabatanApprover2.setText("General Manager");
                                                    } else {
                                                        jabatanApprover2.setText("Kepala Departemen");
                                                    }
                                                }
                                            }

                                        } else if (status_approve.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                            rejectedMark.setVisibility(View.VISIBLE);

                                            String approver = detail.getString("approver");
                                            namaApprover1.setText(approver);

                                        } else {
                                            actionPart.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                if (kode.equals("delegation")) {
                                    actionPart.setVisibility(View.GONE);
                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                    editPermohonanBTN.setVisibility(View.GONE);
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

    private void actionRead() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/read_notif_izin";
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
        final String url = "https://hrisgelora.co.id/api/cek_ttd_digital";
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
        final String url = "https://hrisgelora.co.id/api/approve_action_cuti";
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
                                catatanAtasanTV.setText("");
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

                if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25") || sharedPrefManager.getSpIdJabatan().equals("35") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
                    if(!nikApprover.equals("null") && nikApprover.equals(sharedPrefManager.getSpNik())){
                        params.put("action", "kadep");
                    } else {
                        params.put("action", "kabag");
                    }
                } else if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("33")){
                    params.put("action", "kadep");
                } else if(sharedPrefManager.getSpIdJabatan().equals("8")){
                    params.put("action", "direksi");
                }

                params.put("catatan", catatanAtasanTV.getText().toString());

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void rejectedAction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/reject_action_cuti";
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
                                catatanAtasanTV.setText("");
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("timestamp_approve", getTimeStamp());
                params.put("updated_at", getTimeStamp());

                if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25") || sharedPrefManager.getSpIdJabatan().equals("35") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
                    if(!nikApprover.equals("null") && nikApprover.equals(sharedPrefManager.getSpNik())){
                        params.put("action", "kadep");
                    } else {
                        params.put("action", "kabag");
                    }
                } else if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("33")){
                    params.put("action", "kadep");
                } else if(sharedPrefManager.getSpIdJabatan().equals("8")){
                    params.put("action", "direksi");
                }

                params.put("catatan", catatanAtasanTV.getText().toString());

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
        getDataDetailPermohonan();
        if(statusKondisi.equals("1")){
            statusKondisi = "0";
            recheckSignature();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}