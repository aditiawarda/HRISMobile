package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import com.bumptech.glide.Glide;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailDataExitClearanceActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, approveHRD, waitingApproveHRD, cancelBTN, downloadBTN;
    SwipeRefreshLayout refreshLayout;
    ImageView statusGif;
    TextView namaKaryawanTV, nikKaryawanTV, detailKaryawanTV, tanggalMasukTV, tanggalKeluarTV, alasanTV, saranTV, tglApproveHRD, approverHRD;
    TextView st1FileTV, statusTV1, tglApprove1TV;
    LinearLayout st1UploadIcBTN, viewDetailBTN1;
    TextView st2FileTV, statusTV2, tglApprove2TV;
    LinearLayout st2UploadIcBTN, viewDetailBTN2;
    TextView st3FileTV, statusTV3, tglApprove3TV;
    LinearLayout st3UploadIcBTN, viewDetailBTN3;
    TextView st4FileTV, statusTV4, tglApprove4TV;
    LinearLayout st4UploadIcBTN, viewDetailBTN4;
    TextView st5FileTV, statusTV5, tglApprove5TV;
    LinearLayout st5UploadIcBTN, viewDetailBTN5;
    TextView st6FileTV, statusTV6, tglApprove6TV;
    LinearLayout st6UploadIcBTN, viewDetailBTN6;
    SharedPrefManager sharedPrefManager;
    String idData, finalApprove = "0";
    KAlertDialog pDialog;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        statusGif = findViewById(R.id.status_gif);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        detailKaryawanTV = findViewById(R.id.detail_karyawan_tv);
        tanggalMasukTV = findViewById(R.id.tanggal_masuk_tv);
        tanggalKeluarTV = findViewById(R.id.tanggal_keluar_tv);
        alasanTV = findViewById(R.id.alasan_tv);
        saranTV = findViewById(R.id.saran_tv);
        approveHRD = findViewById(R.id.approve_hrd);
        waitingApproveHRD = findViewById(R.id.waiting_approve_hrd);
        tglApproveHRD = findViewById(R.id.tgl_approve_hrd);
        approverHRD = findViewById(R.id.approver_hrd);
        cancelBTN = findViewById(R.id.cancel_btn);
        downloadBTN = findViewById(R.id.download_btn);

        st1FileTV = findViewById(R.id.st_1_file_tv);
        st1UploadIcBTN = findViewById(R.id.st_1_upload_ic_btn);
        statusTV1 = findViewById(R.id.status_tv_1);
        tglApprove1TV = findViewById(R.id.tgl_approve_tv_1);
        viewDetailBTN1 = findViewById(R.id.view_detail_btn_1);

        st2FileTV = findViewById(R.id.st_2_file_tv);
        st2UploadIcBTN = findViewById(R.id.st_2_upload_ic_btn);
        statusTV2 = findViewById(R.id.status_tv_2);
        tglApprove2TV = findViewById(R.id.tgl_approve_tv_2);
        viewDetailBTN2 = findViewById(R.id.view_detail_btn_2);

        st3FileTV = findViewById(R.id.st_3_file_tv);
        st3UploadIcBTN = findViewById(R.id.st_3_upload_ic_btn);
        statusTV3 = findViewById(R.id.status_tv_3);
        tglApprove3TV = findViewById(R.id.tgl_approve_tv_3);
        viewDetailBTN3 = findViewById(R.id.view_detail_btn_3);

        st4FileTV = findViewById(R.id.st_4_file_tv);
        st4UploadIcBTN = findViewById(R.id.st_4_upload_ic_btn);
        statusTV4 = findViewById(R.id.status_tv_4);
        tglApprove4TV = findViewById(R.id.tgl_approve_tv_4);
        viewDetailBTN4 = findViewById(R.id.view_detail_btn_4);

        st5FileTV = findViewById(R.id.st_5_file_tv);
        st5UploadIcBTN = findViewById(R.id.st_5_upload_ic_btn);
        statusTV5 = findViewById(R.id.status_tv_5);
        tglApprove5TV = findViewById(R.id.tgl_approve_tv_5);
        viewDetailBTN5 = findViewById(R.id.view_detail_btn_5);

        st6FileTV = findViewById(R.id.st_6_file_tv);
        st6UploadIcBTN = findViewById(R.id.st_6_upload_ic_btn);
        statusTV6 = findViewById(R.id.status_tv_6);
        tglApprove6TV = findViewById(R.id.tgl_approve_tv_6);
        viewDetailBTN6 = findViewById(R.id.view_detail_btn_6);

        idData = getIntent().getExtras().getString("id_record");

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
                }, 2000);
            }
        });

        backBTN.setOnClickListener(v -> onBackPressed());

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file_url = "https://geloraaksara.co.id/absen-online/absen/pdf_clearance_form/"+idData;
                new KAlertDialog(DetailDataExitClearanceActivity.this, KAlertDialog.WARNING_TYPE)
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

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailDataExitClearanceActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk membatalkan exit clearance?")
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

                                pDialog = new KAlertDialog(DetailDataExitClearanceActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataExitClearanceActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataExitClearanceActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataExitClearanceActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataExitClearanceActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataExitClearanceActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataExitClearanceActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        cancelPermohonan(idData);
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
        final String url = "https://geloraaksara.co.id/absen-online/api/get_my_exit_clearance";
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
                            if (status.equals("Available")){
                                JSONObject detail = data.getJSONObject("data");
                                String nama = detail.getString("nama");
                                String NIK = detail.getString("NIK");
                                String jabatan = detail.getString("jabatan");
                                String bagian = detail.getString("bagian");
                                String departemen = detail.getString("departemen");
                                String tanggal_masuk = detail.getString("tanggal_masuk");
                                String tanggal_keluar = detail.getString("tanggal_keluar");
                                String alasan_keluar = detail.getString("alasan_keluar");
                                String saran = detail.getString("saran");
                                String approve_hrd = detail.getString("approve_hrd");
                                String tgl_approve_hrd = detail.getString("tgl_approve_hrd");
                                String nama_hrd = detail.getString("nama_hrd");

                                namaKaryawanTV.setText(nama.toUpperCase());
                                nikKaryawanTV.setText(NIK);
                                detailKaryawanTV.setText(jabatan+" | "+bagian+" | "+departemen);

                                String input_date_masuk = tanggal_masuk;
                                String dayDateMasuk = input_date_masuk.substring(8,10);
                                String yearDateMasuk = input_date_masuk.substring(0,4);
                                String bulanValueMasuk = input_date_masuk.substring(5,7);
                                String bulanNameMasuk;

                                switch (bulanValueMasuk) {
                                    case "01":
                                        bulanNameMasuk = "Januari";
                                        break;
                                    case "02":
                                        bulanNameMasuk = "Februari";
                                        break;
                                    case "03":
                                        bulanNameMasuk = "Maret";
                                        break;
                                    case "04":
                                        bulanNameMasuk = "April";
                                        break;
                                    case "05":
                                        bulanNameMasuk = "Mei";
                                        break;
                                    case "06":
                                        bulanNameMasuk = "Juni";
                                        break;
                                    case "07":
                                        bulanNameMasuk = "Juli";
                                        break;
                                    case "08":
                                        bulanNameMasuk = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameMasuk = "September";
                                        break;
                                    case "10":
                                        bulanNameMasuk = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameMasuk = "November";
                                        break;
                                    case "12":
                                        bulanNameMasuk = "Desember";
                                        break;
                                    default:
                                        bulanNameMasuk = "Not found";
                                        break;
                                }

                                tanggalMasukTV.setText(Integer.parseInt(dayDateMasuk) +" "+bulanNameMasuk+" "+yearDateMasuk);

                                String input_date_keluar = tanggal_keluar;
                                String dayDateKeluar = input_date_keluar.substring(8,10);
                                String yearDateKeluar = input_date_keluar.substring(0,4);
                                String bulanValueKeluar = input_date_keluar.substring(5,7);
                                String bulanNameKeluar;

                                switch (bulanValueKeluar) {
                                    case "01":
                                        bulanNameKeluar = "Januari";
                                        break;
                                    case "02":
                                        bulanNameKeluar = "Februari";
                                        break;
                                    case "03":
                                        bulanNameKeluar = "Maret";
                                        break;
                                    case "04":
                                        bulanNameKeluar = "April";
                                        break;
                                    case "05":
                                        bulanNameKeluar = "Mei";
                                        break;
                                    case "06":
                                        bulanNameKeluar = "Juni";
                                        break;
                                    case "07":
                                        bulanNameKeluar = "Juli";
                                        break;
                                    case "08":
                                        bulanNameKeluar = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameKeluar = "September";
                                        break;
                                    case "10":
                                        bulanNameKeluar = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameKeluar = "November";
                                        break;
                                    case "12":
                                        bulanNameKeluar = "Desember";
                                        break;
                                    default:
                                        bulanNameKeluar = "Not found";
                                        break;
                                }

                                tanggalKeluarTV.setText(Integer.parseInt(dayDateKeluar) +" "+bulanNameKeluar+" "+yearDateKeluar);

                                alasanTV.setText(alasan_keluar);
                                saranTV.setText(saran);

                                JSONArray serah_terima = data.getJSONArray("serah_terima");
                                for (int i = 0; i < serah_terima.length(); i++) {
                                    JSONObject data_serah_terima = serah_terima.getJSONObject(i);
                                    String id = data_serah_terima.getString("id");
                                    String urutan_st = data_serah_terima.getString("urutan_st");
                                    String lampiran = data_serah_terima.getString("lampiran");
                                    String approval = data_serah_terima.getString("approval");
                                    String tgl_approval = data_serah_terima.getString("tgl_approval");

                                    if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                        finalApprove = "1";
                                    } else {
                                        finalApprove = "0";
                                    }

                                    if(urutan_st.equals("1")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st1FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV1.setText("Disetujui");
                                                statusTV1.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV1.setText("Diproses");
                                                statusTV1.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setText("Menunggu verifikasi");
                                            }
                                            st1UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st1FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV1.setText("Disetujui");
                                                statusTV1.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV1.setText("Diproses");
                                                statusTV1.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setText("Menunggu verifikasi");
                                            }
                                            st1UploadIcBTN.setVisibility(View.GONE);
                                        }
                                        viewDetailBTN1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailDataExitClearanceActivity.this, DetailDataSerahTerimaExitClearanceActivity.class);
                                                intent.putExtra("role", "me");
                                                intent.putExtra("id_st", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if(urutan_st.equals("2")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st2FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV2.setText("Disetujui");
                                                statusTV2.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV2.setText("Diproses");
                                                statusTV2.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setText("Menunggu verifikasi");
                                            }
                                            st2UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st2FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV2.setText("Disetujui");
                                                statusTV2.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV2.setText("Diproses");
                                                statusTV2.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setText("Menunggu verifikasi");
                                            }
                                            st2UploadIcBTN.setVisibility(View.GONE);
                                        }
                                        viewDetailBTN2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailDataExitClearanceActivity.this, DetailDataSerahTerimaExitClearanceActivity.class);
                                                intent.putExtra("role", "me");
                                                intent.putExtra("id_st", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if(urutan_st.equals("3")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st3FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV3.setText("Disetujui");
                                                statusTV3.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV3.setText("Diproses");
                                                statusTV3.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setText("Menunggu verifikasi");
                                            }
                                            st3UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st3FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV3.setText("Disetujui");
                                                statusTV3.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV3.setText("Diproses");
                                                statusTV3.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setText("Menunggu verifikasi");
                                            }
                                            st3UploadIcBTN.setVisibility(View.GONE);
                                        }
                                        viewDetailBTN3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailDataExitClearanceActivity.this, DetailDataSerahTerimaExitClearanceActivity.class);
                                                intent.putExtra("role", "me");
                                                intent.putExtra("id_st", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if(urutan_st.equals("4")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st4FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV4.setText("Disetujui");
                                                statusTV4.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV4.setText("Diproses");
                                                statusTV4.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setText("Menunggu verifikasi");
                                            }
                                            st4UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st4FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV4.setText("Disetujui");
                                                statusTV4.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV4.setText("Diproses");
                                                statusTV4.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setText("Menunggu verifikasi");
                                            }
                                            st4UploadIcBTN.setVisibility(View.GONE);
                                        }
                                        viewDetailBTN4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailDataExitClearanceActivity.this, DetailDataSerahTerimaExitClearanceActivity.class);
                                                intent.putExtra("role", "me");
                                                intent.putExtra("id_st", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if(urutan_st.equals("5")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st5FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV5.setText("Disetujui");
                                                statusTV5.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV5.setText("Diproses");
                                                statusTV5.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setText("Menunggu verifikasi");
                                            }
                                            st5UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st5FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV5.setText("Disetujui");
                                                statusTV5.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV5.setText("Diproses");
                                                statusTV5.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setText("Menunggu verifikasi");
                                            }
                                            st5UploadIcBTN.setVisibility(View.GONE);
                                        }
                                        viewDetailBTN5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailDataExitClearanceActivity.this, DetailDataSerahTerimaExitClearanceActivity.class);
                                                intent.putExtra("role", "me");
                                                intent.putExtra("id_st", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if(urutan_st.equals("6")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st6FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV6.setText("Disetujui");
                                                statusTV6.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV6.setText("Diproses");
                                                statusTV6.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setText("Menunggu verifikasi");
                                            }
                                            st6UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st6FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV6.setText("Disetujui");
                                                statusTV6.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV6.setText("Diproses");
                                                statusTV6.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setText("Menunggu verifikasi");
                                            }
                                            st6UploadIcBTN.setVisibility(View.GONE);
                                        }
                                        viewDetailBTN6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailDataExitClearanceActivity.this, DetailDataSerahTerimaExitClearanceActivity.class);
                                                intent.putExtra("role", "me");
                                                intent.putExtra("id_st", id);
                                                startActivity(intent);
                                            }
                                        });

                                    }

                                }

                                if(finalApprove.equals("1") && (!approve_hrd.equals("") && !approve_hrd.equals("null") && approve_hrd!=null)){
                                    cancelBTN.setVisibility(View.GONE);
                                    approveHRD.setVisibility(View.VISIBLE);
                                    waitingApproveHRD.setVisibility(View.GONE);
                                    tglApproveHRD.setText("Tanggal verifikasi : "+tgl_approve_hrd.substring(8,10)+"/"+tgl_approve_hrd.substring(5,7)+"/"+tgl_approve_hrd.substring(0,4));
                                    approverHRD.setText(nama_hrd);
                                    statusGif.setPadding(0,0,0,0);
                                    Glide.with(getApplicationContext())
                                            .load(R.drawable.success_ic)
                                            .into(statusGif);
                                } else if(finalApprove.equals("0")){
                                    cancelBTN.setVisibility(View.VISIBLE);
                                    approveHRD.setVisibility(View.GONE);
                                    waitingApproveHRD.setVisibility(View.VISIBLE);
                                    statusGif.setPadding(2,2,2,2);
                                    Glide.with(getApplicationContext())
                                            .load(R.drawable.process_ic)
                                            .into(statusGif);
                                }

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
                params.put("id_record", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void cancelPermohonan(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cancel_exit_clearance";
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
                                pDialog.setTitleText("Permohonan Dibatalkan")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
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
                        pDialog.dismiss();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_record", id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailDataExitClearanceActivity.this)
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