package com.gelora.absensi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterKaryawanPengganti;
import com.gelora.absensi.adapter.AdapterKategoriIzin;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanPengganti;
import com.gelora.absensi.model.KategoriIzin;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.takisoft.datetimepicker.DatePickerDialog;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FormPermohonanCutiActivity extends AppCompatActivity {

    LinearLayout actionBar, infoCutiPart, viewUploadBTN, markUpload, uploadBTN, uploadLampiranPart, viewBTN, successPart, formPart, backBTN, dariTanggalPicker, sampaiTanggalPicker, tipeCutiBTN, submitBTN, loadingDataPart, penggantiSelamaCutiBTN, startAttantionPart, noDataPart;
    SwipeRefreshLayout refreshLayout;
    TextView notejumlahHari, jumlahHariTV, messageSuccessTV, statusUploadTV, labelUnggahTV, tipeCutiTV, namaKaryawan, nikKaryawan, jabatanKaryawan, bagianKaryawan, penggantiSelamaCutiTV, tanggalMulaiBekerja, statuskaryawan, kategoriCutiPilihTV, sisaCuti, tahunCutiTelah, totalCutiTelah, dariTanggalTV, sampaiTanggalTV;
    String lampiranWajibAtauTidak = "", uploadStatus = "", statusLampiran = "", tipeCuti = "", sisaCutiSementara = "", totalCutiDiambil = "", tahunCutiDiambil = "", idIzin = "", hp = "", alamat = "", alasanCuti = "", pengganti = "", dateChoiceMulai = "", kategoriCuti = "", dateChoiceAkhir = "", idCuti = "", kodeCuti = "", descCuti = "", nikKaryawanPengganti, namaKaryawanPenganti;
    ImageView loadingGif, successGif;
    EditText keywordKaryawanPengganti, alasanTV, alamatSelamaCutiTV, noHpTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    BottomSheetLayout bottomSheet;
    View rootview;
    private RecyclerView kategoriCutiRV, karyawanPenggantiRV;
    private KategoriIzin[] kategoriIzins;
    private AdapterKategoriIzin adapterKategoriIzin;
    private KaryawanPengganti[] karyawanPenggantis;
    private AdapterKaryawanPengganti adapterKaryawanPengganti;
    KAlertDialog pDialog;
    private int i = -1;
    String permohonanTerkirim = "0";
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private static final int PICKFILE_RESULT_CODE = 1;
    RequestQueue requestQueue;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_permohonan_cuti);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        rootview = findViewById(android.R.id.content);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        namaKaryawan = findViewById(R.id.nama_karyawan_tv);
        jabatanKaryawan = findViewById(R.id.jabatan_karyawan_tv);
        bagianKaryawan = findViewById(R.id.bagian_karyawan_tv);
        tanggalMulaiBekerja = findViewById(R.id.tgl_mulai_kerja_tv);
        nikKaryawan = findViewById(R.id.nik_karyawan_tv);
        statuskaryawan = findViewById(R.id.status_karyawan);
        sisaCuti = findViewById(R.id.sisa_cuti_tv);
        tahunCutiTelah = findViewById(R.id.tahun_telah_cuti_tv);
        totalCutiTelah = findViewById(R.id.total_telah_cuti_tv);
        dariTanggalPicker = findViewById(R.id.mulai_date);
        sampaiTanggalPicker = findViewById(R.id.akhir_date);
        dariTanggalTV = findViewById(R.id.mulai_date_pilih);
        sampaiTanggalTV = findViewById(R.id.akhir_date_pilih);
        tipeCutiBTN = findViewById(R.id.jenis_cuti);
        kategoriCutiPilihTV = findViewById(R.id.kategori_cuti_pilih);
        penggantiSelamaCutiTV = findViewById(R.id.pengganti_selama_cuti_tv);
        penggantiSelamaCutiBTN = findViewById(R.id.pengganti_selama_cuti_part);
        alasanTV = findViewById(R.id.alasan_tv);
        alamatSelamaCutiTV = findViewById(R.id.alamat_selama_cuti_tv);
        noHpTV = findViewById(R.id.no_hp_tv);
        submitBTN = findViewById(R.id.submit_btn);
        successPart = findViewById(R.id.success_submit_cuti);
        successGif = findViewById(R.id.success_gif);
        formPart = findViewById(R.id.form_part_cuti);
        viewBTN = findViewById(R.id.view_permohonan_btn);
        tipeCutiTV = findViewById(R.id.tipe_cuti_tv);
        uploadBTN = findViewById(R.id.upload_btn);
        uploadLampiranPart = findViewById(R.id.upload_file_part);
        markUpload = findViewById(R.id.mark_upload);
        statusUploadTV = findViewById(R.id.status_upload_tv);
        labelUnggahTV = findViewById(R.id.label_unggah);
        viewUploadBTN = findViewById(R.id.view_btn);
        messageSuccessTV = findViewById(R.id.message_tv);
        jumlahHariTV = findViewById(R.id.jumlah_hari_tv);
        notejumlahHari = findViewById(R.id.note_jumlah_hari);
        infoCutiPart = findViewById(R.id.info_cuti_part);
        actionBar = findViewById(R.id.action_bar);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        if(sharedPrefManager.getSpIdJabatan().equals("10")){
            messageSuccessTV.setText("Permohonan anda telah terkirim dan disampaikan kepada bagian HRD untuk persetujuan.");
        } else if(sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25")){
            messageSuccessTV.setText("Permohonan anda telah terkirim dan disampaikan kepada Kepala Departemen untuk persetujuan.");
        } else {
            messageSuccessTV.setText("Permohonan anda telah terkirim dan disampaikan kepada Kepala Bagian untuk persetujuan.");
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(kategoriCutiBroad, new IntentFilter("kategori_cuti_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(karyawanPenggantiBroad, new IntentFilter("karyawan_pengganti_broad"));

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
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_CUTI, "");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PENGGANTI, "");
                        dariTanggalTV.setText("");
                        sampaiTanggalTV.setText("");
                        kategoriCutiPilihTV.setText("");
                        penggantiSelamaCutiTV.setText("");
                        alasanTV.setText("");
                        alamatSelamaCutiTV.setText("");
                        noHpTV.setText("");

                        lampiranWajibAtauTidak = "";
                        dateChoiceMulai = "";
                        dateChoiceAkhir = "";
                        markUpload.setVisibility(View.GONE);
                        viewUploadBTN.setVisibility(View.GONE);
                        statusUploadTV.setText("Unggah Lampiran");
                        labelUnggahTV.setText("Unggah");
                        tipeCuti = "";
                        idCuti = "";
                        uploadStatus = "";
                        tipeCutiTV.setText("Pilih Jenis Cuti...");
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        alasanTV.clearFocus();
                        alamatSelamaCutiTV.clearFocus();
                        noHpTV.clearFocus();

                        notejumlahHari.setVisibility(View.VISIBLE);

                        getDataKaryawan();
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

        viewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormPermohonanCutiActivity.this, DetailPermohonanCutiActivity.class);
                intent.putExtra("kode", "form");
                intent.putExtra("id_izin", idIzin);
                startActivity(intent);
            }
        });

        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        dariTanggalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();
                dateMulai();
            }
        });

        sampaiTanggalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();
                dateAkhir();
            }
        });

        tipeCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                kategoriCuti();
            }
        });

        penggantiSelamaCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                karyawanPengganti();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                if(dateChoiceMulai.equals("")){
                    if(dateChoiceAkhir.equals("")){
                        if(kategoriCutiPilihTV.getText().toString().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi semua data

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi semua data!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alasan, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alasan dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alasan dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alasan, alamat, dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alasan, alamat selama cuti, dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alasan, dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alasan, dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alasan, dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, kategori, dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, kategori cuti, dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, dan kategori

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, dan kategori cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        } else {
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan, pengganti selama cuti dan alamat pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, alasan dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, alasan, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, alasan dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, alasan dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, tanggal akhir dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, tanggal akhir dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, tanggal akhir dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai dan tanggal akhir

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai dan tanggal akhir!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if(kategoriCutiPilihTV.getText().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori, alasan, dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan, dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, alasan, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori, alasan dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, alasan dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alasan dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, alamat dan no hp

                                             new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, kategori dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, kategori cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai dan kategori

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai dan kategori cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        } else {
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, alasan, dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan, dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, alasan, alamat dan no hp

                                              new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, alasan dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, alasan dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alasan dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai dan alamat

                                              new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal mulai

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal mulai!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    if(dateChoiceAkhir.equals("")){
                        if(kategoriCutiPilihTV.getText().toString().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori, alasan dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, alasan, alamat, dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan, alamat selama cuti, dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori, alasan, dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan, dan alamat alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, alasan, dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alasan, dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, kategori, dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti, dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, kategori cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir dan kategori

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir dan kategori cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        } else {
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan, pengganti selama cuti dan alamat pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, alasan dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, alasan, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, alasan dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, alasan dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alasan dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi tanggal akhir

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal akhir!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if(kategoriCutiPilihTV.getText().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori, alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, alasan, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori, alasan, dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan, dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, alasan, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori, alasan dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, alasan dan no hp

                                              new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alasan dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori dan alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, pengganti dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori dan pengganti

                                              new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori dan alamat

                                              new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori dan no hp

                                             new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi kategori

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi kategori cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            }
                        } else {
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan, pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi alasan, pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan, pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi alasan, pengganti dan no hp

                                               new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan, pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi alasan, dan pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan, dan pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi alasan, alamat dan no hp

                                             new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi alasan dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi alasan dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi alasan

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                }
                            } else {
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi pengganti, alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi pengganti selama cuti, alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi pengganti dan alamat

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi pengganti selama cuti dan alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi pengganti dan no hp

                                               new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi pengganti selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi pengganti

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi pengganti selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                } else {
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi alamat dan no hp

                                            new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alamat selama cuti dan no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            // Harap isi alamat

                                             new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi alamat selama cuti!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    } else {
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi no hp

                                             new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi no hp!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                        } else {

                                            // Diisi semua

                                             if(statusLampiran.equals("1")){
                                                if(uploadStatus.equals("1")){
                                                    new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                                                           .setTitleText("Perhatian")
                                                            .setContentText("Kirim permohonan sekarang?")
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
                                                                    pDialog = new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                                    pDialog.show();
                                                                    pDialog.setCancelable(false);
                                                                    new CountDownTimer(1300, 800) {
                                                                        public void onTick(long millisUntilFinished) {
                                                                            i++;
                                                                            switch (i) {
                                                                                case 0:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormPermohonanCutiActivity.this, R.color.colorGradien));
                                                                                    break;
                                                                                case 1:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormPermohonanCutiActivity.this, R.color.colorGradien2));
                                                                                    break;
                                                                                case 2:
                                                                                case 6:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormPermohonanCutiActivity.this, R.color.colorGradien3));
                                                                                    break;
                                                                                case 3:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormPermohonanCutiActivity.this, R.color.colorGradien4));
                                                                                    break;
                                                                                case 4:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormPermohonanCutiActivity.this, R.color.colorGradien5));
                                                                                    break;
                                                                                case 5:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormPermohonanCutiActivity.this, R.color.colorGradien6));
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
                                                else {
                                                    new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                                           .setTitleText("Perhatian")
                                                           .setContentText("Harap unggah lampiran!")
                                                           .setConfirmText("    OK    ")
                                                           .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                               }
                                             }
                                             else if(statusLampiran.equals("0")) {

                                                new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Kirim permohonan sekarang?")
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
                                                            pDialog = new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                            pDialog.show();
                                                            pDialog.setCancelable(false);
                                                            new CountDownTimer(1300, 800) {
                                                                public void onTick(long millisUntilFinished) {
                                                                    i++;
                                                                    switch (i) {
                                                                        case 0:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormPermohonanCutiActivity.this, R.color.colorGradien));
                                                                            break;
                                                                        case 1:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormPermohonanCutiActivity.this, R.color.colorGradien2));
                                                                            break;
                                                                        case 2:
                                                                        case 6:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormPermohonanCutiActivity.this, R.color.colorGradien3));
                                                                            break;
                                                                        case 3:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormPermohonanCutiActivity.this, R.color.colorGradien4));
                                                                            break;
                                                                        case 4:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormPermohonanCutiActivity.this, R.color.colorGradien5));
                                                                            break;
                                                                        case 5:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormPermohonanCutiActivity.this, R.color.colorGradien6));
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
                                    }
                                }
                            }
                        }
                    }

                }
            }
        });

        getDataKaryawan();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_CUTI, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PENGGANTI, "");

    }

    private void getDataKaryawan(){
        namaKaryawan.setText(sharedPrefManager.getSpNama().toUpperCase());
        nikKaryawan.setText(sharedPrefManager.getSpNik());
        getDataKaryawanDetail();
    }

    private void getDataKaryawanDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_karyawan_personal";
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
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                String tanggal_masuk = data.getString("tanggal_masuk");
                                String tanggal_bergabung = data.getString("tanggal_bergabung");
                                String status_karyawan = data.getString("status_karyawan");
                                String sisa_cuti = data.getString("sisa_cuti");
                                String cuti_ambil = data.getString("cuti_ambil");
                                String lampiran_cuti_kusus = data.getString("lampiran_cuti_kusus");
                                String info_sisa_cuti = data.getString("info_sisa_cuti");
                                String tahun_periode = data.getString("tahun_periode");

                                lampiranWajibAtauTidak = lampiran_cuti_kusus;
                                jabatanKaryawan.setText(jabatan);
                                bagianKaryawan.setText(bagian+" | "+department);
                                tanggalMulaiBekerja.setText(tanggal_masuk.substring(8,10)+"/"+tanggal_masuk.substring(5,7)+"/"+tanggal_masuk.substring(0,4));
                                statuskaryawan.setText(status_karyawan);
                                sisaCuti.setText(sisa_cuti+" Hari");
                                sisaCutiSementara = sisa_cuti;
                                tahunCutiTelah.setText(tahun_periode);
                                totalCutiTelah.setText(cuti_ambil+" Hari");
                                totalCutiDiambil = cuti_ambil;
                                tahunCutiDiambil = tahun_periode;

                                if(info_sisa_cuti.equals("1")){
                                    infoCutiPart.setVisibility(View.VISIBLE);
                                } else {
                                    infoCutiPart.setVisibility(View.GONE);
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
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
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
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Available")){
                                String signature = data.getString("data");
                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature;
                                submitCuti();
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
                                                Intent intent = new Intent(FormPermohonanCutiActivity.this, DigitalSignatureActivity.class);
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

    private void submitCuti(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cuti_input";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                JSONObject data_cuti = data.getJSONObject("data");
                                String id = data_cuti.getString("id");
                                idIzin = id;
                                permohonanTerkirim = "1";
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);

                                if(uploadStatus.equals("1")){
                                    uploadLampiran();
                                } else {
                                    permohonanTerkirim = "1";
                                    pDialog.dismiss();
                                    successPart.setVisibility(View.VISIBLE);
                                    formPart.setVisibility(View.GONE);
                                }

                            } else if (status.equals("Available")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Permohonan serupa sudah anda ajukan sebelumnya, harap tunggu persetujuan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if (status.equals("Available Checkin")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Anda telah melakukan check in pada tanggal yang dipilih, harap periksa kembali")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if (status.equals("Limit Stock")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Maaf, jumlah hari dari permohonan yang anda ajukan melebihi sisa cuti anda saat ini")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
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
                        successPart.setVisibility(View.GONE);
                        formPart.setVisibility(View.VISIBLE);
                        pDialog.setTitleText("Gagal Terkirim")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.ERROR_TYPE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("sisa_cuti_sementara", sisaCutiSementara);
                params.put("tahun_cuti_diambil", tahunCutiDiambil);
                params.put("total_cuti_diambil", totalCutiDiambil);
                params.put("alasan_cuti", alasanTV.getText().toString());
                params.put("pengganti", nikKaryawanPengganti);
                params.put("alamat_selama_cuti", alamatSelamaCutiTV.getText().toString());
                params.put("no_hp", noHpTV.getText().toString());
                params.put("tipe_cuti", tipeCuti);

                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("jenis_cuti", kategoriCuti);
                params.put("tanggal", getDate());
                params.put("time", getTime());
                params.put("tanggal_mulai", dateChoiceMulai);
                params.put("tanggal_akhir", dateChoiceAkhir);
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());

                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    public void uploadLampiran() {
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/upload_lampiran_cuti";
        String path1 = FilePathimage.getPath(this, uri);
        if (path1 == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                permohonanTerkirim = "1";
                pDialog.dismiss();
                successPart.setVisibility(View.VISIBLE);
                formPart.setVisibility(View.GONE);

                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path1, "file") //Adding file
                        .addParameter("id_izin_record", idIzin)
                        .addParameter("NIK", sharedPrefManager.getSpNik())
                        .addParameter("current_time", getDate().substring(0,4)+getDate().substring(5,7)+getDate().substring(8,10))//Adding text parameter to the request
                        .setMaxRetries(1)
                        .startUpload();

            } catch (Exception exc) {
                Log.e("PaRSE JSON", "Oke");
                pDialog.dismiss();
            }
        }
    }

    private void kategoriCuti(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormPermohonanCutiActivity.this).inflate(R.layout.layout_kategori_cuti, bottomSheet, false));
        kategoriCutiRV = findViewById(R.id.kategori_cuti_rv);

        kategoriCutiRV.setLayoutManager(new LinearLayoutManager(this));
        kategoriCutiRV.setHasFixedSize(true);
        kategoriCutiRV.setNestedScrollingEnabled(false);
        kategoriCutiRV.setItemAnimator(new DefaultItemAnimator());

        getkategoriCuti();

    }

    private void karyawanPengganti(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormPermohonanCutiActivity.this).inflate(R.layout.layout_karyawan_pengganti, bottomSheet, false));
        keywordKaryawanPengganti = findViewById(R.id.keyword_user_ed);
        keywordKaryawanPengganti.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        karyawanPenggantiRV = findViewById(R.id.karyawan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

        karyawanPenggantiRV.setLayoutManager(new LinearLayoutManager(this));
        karyawanPenggantiRV.setHasFixedSize(true);
        karyawanPenggantiRV.setNestedScrollingEnabled(false);
        karyawanPenggantiRV.setItemAnimator(new DefaultItemAnimator());

        keywordKaryawanPengganti.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                String keyWordSearch = keywordKaryawanPengganti.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                karyawanPenggantiRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDataKaryawanPengganti(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        keywordKaryawanPengganti.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = keywordKaryawanPengganti.getText().toString();
                    getDataKaryawanPengganti(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormPermohonanCutiActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormPermohonanCutiActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormPermohonanCutiActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    alasanTV.clearFocus();
                    alamatSelamaCutiTV.clearFocus();
                    noHpTV.clearFocus();

                    return true;
                }
                return false;
            }
        });

    }

    private void getDataKaryawanPengganti(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_pengganti";
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

                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                                karyawanPenggantiRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                karyawanPenggantis = gson.fromJson(data_list, KaryawanPengganti[].class);
                                adapterKaryawanPengganti = new AdapterKaryawanPengganti(karyawanPenggantis, FormPermohonanCutiActivity.this);
                                karyawanPenggantiRV.setAdapter(adapterKaryawanPengganti);
                            } else {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
                                karyawanPenggantiRV.setVisibility(View.GONE);
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

                        startAttantionPart.setVisibility(View.GONE);
                        loadingDataPart.setVisibility(View.GONE);
                        noDataPart.setVisibility(View.VISIBLE);
                        karyawanPenggantiRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getkategoriCuti() {
        RequestQueue requestQueue = Volley.newRequestQueue(FormPermohonanCutiActivity.this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cuti_kategori";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String list_data = response.getString("data");
                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            kategoriIzins = gson.fromJson(list_data, KategoriIzin[].class);
                            adapterKategoriIzin = new AdapterKategoriIzin(kategoriIzins,FormPermohonanCutiActivity.this);
                            kategoriCutiRV.setAdapter(adapterKategoriIzin);
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

        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void connectionFailed(){
        // Banner.make(rootview, FormPermohonanCutiActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(FormPermohonanCutiActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();

    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoiceMulai = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!dateChoiceAkhir.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(dateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(dateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = dateChoiceMulai;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
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

                        dariTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                            dayCalculate();
                        }

                    } else {
                        dariTanggalTV.setText("Pilih Kembali !");
                        dateChoiceMulai = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal mulai tidak bisa lebih besar dari tanggal akhir. Harap ulangi!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();

                    }

                }

                else {
                    String input_date = dateChoiceMulai;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
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

                    dariTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoiceMulai = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!dateChoiceAkhir.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(dateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(dateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = dateChoiceMulai;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
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

                        dariTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                            dayCalculate();
                        }

                    } else {
                        dariTanggalTV.setText("Pilih Kembali !");
                        dateChoiceMulai = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal mulai tidak bisa lebih besar dari tanggal akhir. Harap ulangi!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();

                    }

                }

                else {
                    String input_date = dateChoiceMulai;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
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

                    dariTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, y,m-1,d);
            dpd.show();
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoiceAkhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!dateChoiceMulai.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(dateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(dateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = dateChoiceAkhir;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
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

                        sampaiTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                            dayCalculate();
                        }

                    } else {
                        sampaiTanggalTV.setText("Pilih Kembali !");
                        dateChoiceAkhir = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal akhir tidak bisa lebih kecil dari tanggal mulai. Harap ulangi!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();

                    }

                }

                else {
                    String input_date = dateChoiceAkhir;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
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

                    sampaiTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoiceAkhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!dateChoiceMulai.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(dateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(dateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = dateChoiceAkhir;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
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

                        sampaiTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                            dayCalculate();
                        }

                    } else {
                        sampaiTanggalTV.setText("Pilih Kembali !");
                        dateChoiceAkhir = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal akhir tidak bisa lebih kecil dari tanggal mulai. Harap ulangi!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();

                    }

                }

                else {
                    String input_date = dateChoiceAkhir;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
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

                    sampaiTanggalTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, y,m-1,d);
            dpd.show();
        }

    }

    public BroadcastReceiver kategoriCutiBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            idCuti   = intent.getStringExtra("id_kategori_cuti");
            kodeCuti = intent.getStringExtra("kode_kategori_cuti");
            descCuti = intent.getStringExtra("desc_kategori_cuti");
            tipeCuti = intent.getStringExtra("tipe_kategori_cuti");

            if(tipeCuti.equals("1")){
                tipeCutiTV.setText("Tahunan");
            } else if(tipeCuti.equals("2")) {
                tipeCutiTV.setText("Khusus");
            }

            String lampiran = intent.getStringExtra("lampiran_kategori_cuti");

            if(lampiranWajibAtauTidak.equals("1")){
                statusLampiran = lampiran;
            } else {
                statusLampiran = "0";
            }

            if(!idCuti.equals("") && !dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                dayCalculate();
            }

            if(idCuti.equals("9")||idCuti.equals("13")){
                notejumlahHari.setVisibility(View.GONE);
            } else {
                notejumlahHari.setVisibility(View.VISIBLE);
            }

            kategoriCuti = idCuti;
            kategoriCutiPilihTV.setText(descCuti);

            alasanTV.clearFocus();
            alamatSelamaCutiTV.clearFocus();
            noHpTV.clearFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    public BroadcastReceiver karyawanPenggantiBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            nikKaryawanPengganti = intent.getStringExtra("nik_karyawan_pengganti");
            namaKaryawanPenganti = intent.getStringExtra("nama_karyawan_pengganti");

            penggantiSelamaCutiTV.setText(namaKaryawanPenganti);

            InputMethodManager imm = (InputMethodManager) FormPermohonanCutiActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = FormPermohonanCutiActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(FormPermohonanCutiActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            alasanTV.clearFocus();
            alamatSelamaCutiTV.clearFocus();
            noHpTV.clearFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    private void dayCalculate(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_hari";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Success")){
                                String jumlah_hari = data.getString("jumlah_hari");
                                jumlahHariTV.setText(jumlah_hari+" Hari");
                            } else {
                                jumlahHariTV.setText("Tentukan Tanggal...");
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

                if(idCuti.equals("")){
                    params.put("tipe_izin", "2");
                } else {
                    params.put("tipe_izin", idCuti);
                }

                params.put("tanggal_mulai", dateChoiceMulai);
                params.put("tanggal_akhir", dateChoiceAkhir);

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getFilePDF(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
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

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        alasanCuti = alasanTV.getText().toString();
        pengganti = penggantiSelamaCutiTV.getText().toString();
        alamat = alamatSelamaCutiTV.getText().toString();
        hp = noHpTV.getText().toString();

        if (!kategoriCuti.equals("") || !dateChoiceMulai.equals("") || !dateChoiceAkhir.equals("") || !alasanCuti.equals("") || !pengganti.equals("") || !alamat.equals("") || !hp.equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (permohonanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Apakah anda yakin untuk meninggalkan halaman ini?")
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
                                    kategoriCuti = "";
                                    dateChoiceMulai = "";
                                    dateChoiceAkhir = "";
                                    alasanCuti = "";
                                    pengganti = "";
                                    alamat = "";
                                    hp = "";
                                    permohonanTerkirim = "1";
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        } else {
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(FormPermohonanCutiActivity.this)
                    .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withActivity(FormPermohonanCutiActivity.this)
                    .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, "lampiran");
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(FormPermohonanCutiActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(FormPermohonanCutiActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 600);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 900);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(FormPermohonanCutiActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        String file_directori = getRealPathFromURIPath(uri, FormPermohonanCutiActivity.this);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        @SuppressLint("Recycle")
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                try {
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")||extension.equals(".png")||extension.equals(".PNG")){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, FormPermohonanCutiActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);
                        markUpload.setVisibility(View.VISIBLE);
                        viewUploadBTN.setVisibility(View.VISIBLE);
                        statusUploadTV.setText("Berhasil diunggah");
                        labelUnggahTV.setText("Ganti");
                        uploadStatus = "1";

                        viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FormPermohonanCutiActivity.this, ViewImageActivity.class);
                                intent.putExtra("url", String.valueOf(uri));
                                intent.putExtra("kode", "form");
                                intent.putExtra("jenis_form", "cuti");
                                startActivity(intent);
                            }
                        });
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(FormPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Format file tidak sesuai!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }, 800);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}