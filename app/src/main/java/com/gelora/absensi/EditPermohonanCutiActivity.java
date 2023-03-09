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
import com.gelora.absensi.adapter.AdapterKaryawanPenggantiEdit;
import com.gelora.absensi.adapter.AdapterKategoriIzin;
import com.gelora.absensi.adapter.AdapterKategoriIzinEdit;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
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

public class EditPermohonanCutiActivity extends AppCompatActivity {

    LinearLayout infoCutiPart, removeLampiranBTN, submitBTN, loadingDataPart, noDataPart, startAttantionPart, penggantiSelamaCutiBTN, tipeCutiBTN, viewUploadBTN, markUpload, uploadBTN, uploadLampiranPart, backBTN, dariTanggalPicker, sampaiTanggalPicker;
    TextView notejumlahHari, jumlahHariTV, labelUnggahTV, statusUploadTV, noHpTV, alamatSelamaCutiTV, penggantiSelamaCutiTV, jenisCutiTV, tipeCutiTV, sampaiTanggalTV, dariTanggalTV, totalCutiDiambilTV, tahunCutiDiambilTV, sisaCutiTV, namaKaryawanTV, jabatanKaryawanTV, detailKaryawanTV, tglMulaiKerjaTV, nikKaryawanTV, statusKaryawanTV;
    EditText alasanTV, keywordKaryawanPengganti;
    ImageView loadingGif;
    BottomSheetLayout bottomSheet;
    String removeLampiranStatus = "0", gantiLampiran = "", permohonanTerkirim = "0", uploadStatus = "", idRecord, dateChoiceMulai = "", dateChoiceAkhir = "", idCuti = "";
    String lampiranWajibAtauTidak = "", statusLampiran = "", tipeCuti = "", sisaCutiSementara = "", totalCutiDiambil = "", idIzin = "", hp = "", alamat = "", alasanCuti = "", pengganti = "", kategoriCuti = "", kodeCuti = "", descCuti = "", nikKaryawanPengganti, namaKaryawanPenganti;
    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    KAlertDialog pDialog;
    private int i = -1;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    RequestQueue requestQueue;

    private RecyclerView kategoriCutiRV, karyawanPenggantiRV;
    private KategoriIzin[] kategoriIzins;
    private AdapterKategoriIzinEdit adapterKategoriIzin;
    private KaryawanPengganti[] karyawanPenggantis;
    private AdapterKaryawanPenggantiEdit adapterKaryawanPengganti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_permohonan_cuti);

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        backBTN = findViewById(R.id.back_btn);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        jabatanKaryawanTV = findViewById(R.id.jabatan_karyawan_tv);
        detailKaryawanTV = findViewById(R.id.bagian_karyawan_tv);
        tglMulaiKerjaTV = findViewById(R.id.tgl_mulai_kerja_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        statusKaryawanTV = findViewById(R.id.status_karyawan);
        sisaCutiTV = findViewById(R.id.sisa_cuti_tv);
        tahunCutiDiambilTV = findViewById(R.id.tahun_telah_cuti_tv);
        totalCutiDiambilTV = findViewById(R.id.total_telah_cuti_tv);
        dariTanggalPicker = findViewById(R.id.mulai_date);
        sampaiTanggalPicker = findViewById(R.id.akhir_date);
        dariTanggalTV = findViewById(R.id.mulai_date_pilih);
        sampaiTanggalTV = findViewById(R.id.akhir_date_pilih);
        jenisCutiTV = findViewById(R.id.kategori_cuti_pilih);
        tipeCutiTV = findViewById(R.id.tipe_cuti_tv);
        alasanTV = findViewById(R.id.alasan_tv);
        penggantiSelamaCutiBTN = findViewById(R.id.pengganti_selama_cuti_part);
        penggantiSelamaCutiTV = findViewById(R.id.pengganti_selama_cuti_tv);
        alamatSelamaCutiTV = findViewById(R.id.alamat_selama_cuti_tv);
        noHpTV = findViewById(R.id.no_hp_tv);
        tipeCutiTV = findViewById(R.id.tipe_cuti_tv);
        uploadBTN = findViewById(R.id.upload_btn);
        uploadLampiranPart = findViewById(R.id.upload_file_part);
        markUpload = findViewById(R.id.mark_upload);
        statusUploadTV = findViewById(R.id.status_upload_tv);
        labelUnggahTV = findViewById(R.id.label_unggah);
        viewUploadBTN = findViewById(R.id.view_btn);
        removeLampiranBTN = findViewById(R.id.remove_lampiran_btn);
        tipeCutiBTN = findViewById(R.id.jenis_cuti);
        jumlahHariTV = findViewById(R.id.jumlah_hari_tv);
        notejumlahHari = findViewById(R.id.note_jumlah_hari);
        submitBTN = findViewById(R.id.submit_btn);
        infoCutiPart = findViewById(R.id.info_cuti_part);

        idRecord = getIntent().getExtras().getString("id_record");

        LocalBroadcastManager.getInstance(this).registerReceiver(kategoriCutiBroad, new IntentFilter("kategori_cuti_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(karyawanPenggantiBroad, new IntentFilter("karyawan_pengganti_broad"));

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
                        getDataPermohonan();
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

        dariTanggalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateMulai();
            }
        });

        sampaiTanggalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateAkhir();
            }
        });

        tipeCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                kategoriCuti();
            }
        });

        penggantiSelamaCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                karyawanPengganti();
            }
        });

        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        removeLampiranBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLampiranStatus = "1";
                uploadStatus = "";
                markUpload.setVisibility(View.GONE);
                viewUploadBTN.setVisibility(View.GONE);
                removeLampiranBTN.setVisibility(View.GONE);
                statusUploadTV.setText("Belum diunggah");
                labelUnggahTV.setText("Unggah");
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alasanTV.clearFocus();
                alamatSelamaCutiTV.clearFocus();
                noHpTV.clearFocus();

                if(dateChoiceMulai.equals("")){
                    if(dateChoiceAkhir.equals("")){
                        if(jenisCutiTV.getText().toString().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi semua data

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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
                        if(jenisCutiTV.getText().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal mulai, kategori, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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
                } else {
                    if(dateChoiceAkhir.equals("")){
                        if(jenisCutiTV.getText().toString().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi tanggal akhir, kategori, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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
                        if(jenisCutiTV.getText().equals("")){
                            if(alasanTV.getText().toString().equals("")){
                                if(penggantiSelamaCutiTV.getText().toString().equals("")){
                                    if(alamatSelamaCutiTV.getText().toString().equals("")){
                                        if(noHpTV.getText().toString().equals("")){
                                            // Harap isi kategori, alasan, pengganti, alamat dan no hp

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                            new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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
                                                    new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                                    pDialog = new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                                    pDialog.show();
                                                                    pDialog.setCancelable(false);
                                                                    new CountDownTimer(1300, 800) {
                                                                        public void onTick(long millisUntilFinished) {
                                                                            i++;
                                                                            switch (i) {
                                                                                case 0:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (EditPermohonanCutiActivity.this, R.color.colorGradien));
                                                                                    break;
                                                                                case 1:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (EditPermohonanCutiActivity.this, R.color.colorGradien2));
                                                                                    break;
                                                                                case 2:
                                                                                case 6:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (EditPermohonanCutiActivity.this, R.color.colorGradien3));
                                                                                    break;
                                                                                case 3:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (EditPermohonanCutiActivity.this, R.color.colorGradien4));
                                                                                    break;
                                                                                case 4:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (EditPermohonanCutiActivity.this, R.color.colorGradien5));
                                                                                    break;
                                                                                case 5:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (EditPermohonanCutiActivity.this, R.color.colorGradien6));
                                                                                    break;
                                                                            }
                                                                        }
                                                                        public void onFinish() {
                                                                            i = -1;
                                                                            submitCuti();
                                                                        }
                                                                    }.start();

                                                                }
                                                            })
                                                            .show();
                                                }
                                                else {
                                                    new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

                                                new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                                pDialog = new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                                pDialog.show();
                                                                pDialog.setCancelable(false);
                                                                new CountDownTimer(1300, 800) {
                                                                    public void onTick(long millisUntilFinished) {
                                                                        i++;
                                                                        switch (i) {
                                                                            case 0:
                                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                        (EditPermohonanCutiActivity.this, R.color.colorGradien));
                                                                                break;
                                                                            case 1:
                                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                        (EditPermohonanCutiActivity.this, R.color.colorGradien2));
                                                                                break;
                                                                            case 2:
                                                                            case 6:
                                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                        (EditPermohonanCutiActivity.this, R.color.colorGradien3));
                                                                                break;
                                                                            case 3:
                                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                        (EditPermohonanCutiActivity.this, R.color.colorGradien4));
                                                                                break;
                                                                            case 4:
                                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                        (EditPermohonanCutiActivity.this, R.color.colorGradien5));
                                                                                break;
                                                                            case 5:
                                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                        (EditPermohonanCutiActivity.this, R.color.colorGradien6));
                                                                                break;
                                                                        }
                                                                    }
                                                                    public void onFinish() {
                                                                        i = -1;
                                                                        submitCuti();
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

        getDataPermohonan();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_CUTI, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PENGGANTI, "");

    }

    private void getDataPermohonan() {
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
                            String lampiran_cuti_kusus = data.getString("lampiran_cuti_kusus");

                            lampiranWajibAtauTidak = lampiran_cuti_kusus;
                            if (status.equals("Success")) {
                                JSONObject detail = data.getJSONObject("data");
                                String NIK = detail.getString("NIK");
                                String NmKaryawan = detail.getString("NmKaryawan");
                                String bagian = detail.getString("NmDept");
                                String departemen = detail.getString("NmHeadDept");
                                String jabatan = detail.getString("NmJabatan");
                                String tanggal_bergabung = detail.getString("tanggal_bergabung");
                                String status_karyawan = detail.getString("status_karyawan");
                                String sisa_cuti_sementara = detail.getString("sisa_cuti_sementara");
                                String tahun_cuti_telah_diambil = detail.getString("tahun_cuti_telah_diambil");
                                String total_cuti_telah_diambil = detail.getString("total_cuti_telah_diambil");
                                String tanggal_mulai = detail.getString("tanggal_mulai");
                                String tanggal_akhir = detail.getString("tanggal_akhir");
                                String alasan_cuti = detail.getString("alasan_cuti");
                                String jenis_cuti = detail.getString("tipe_izin");
                                String jenis_cuti_deskripsi = detail.getString("deskripsi_izin");
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
                                String jumlah_hari = data.getString("jumlah_hari");
                                String info_sisa_cuti = data.getString("info_sisa_cuti");

                                if(info_sisa_cuti.equals("1")){
                                    infoCutiPart.setVisibility(View.VISIBLE);
                                } else {
                                    infoCutiPart.setVisibility(View.GONE);
                                }

                                idCuti   = jenis_cuti;
                                descCuti = jenis_cuti_deskripsi;
                                tipeCuti = tipe_cuti;

                                if(tipeCuti.equals("1")){
                                    tipeCutiTV.setText("Tahunan");
                                } else if(tipeCuti.equals("2")) {
                                    tipeCutiTV.setText("Khusus");
                                }

                                if(lampiranWajibAtauTidak.equals("1")){
                                    if(tipeCuti.equals("2")){
                                        statusLampiran = "1";
                                    } else if(tipeCuti.equals("1")) {
                                        statusLampiran = "0";
                                    }
                                } else {
                                    statusLampiran = "0";
                                }

                                kategoriCuti = idCuti;
                                nikKaryawanPengganti = pengganti;
                                namaKaryawanPenganti = karyawan_pengganti;

                                namaKaryawanTV.setText(NmKaryawan.toUpperCase());
                                jabatanKaryawanTV.setText(jabatan);
                                detailKaryawanTV.setText(bagian+" | "+departemen);
                                tglMulaiKerjaTV.setText(tanggal_bergabung.substring(8,10)+"/"+tanggal_bergabung.substring(5,7)+"/"+tanggal_bergabung.substring(0,4));
                                nikKaryawanTV.setText(NIK);
                                statusKaryawanTV.setText(status_karyawan);
                                sisaCutiTV.setText(sisa_cuti_sementara);
                                tahunCutiDiambilTV.setText(tahun_cuti_telah_diambil);
                                totalCutiDiambilTV.setText(total_cuti_telah_diambil);

                                dateChoiceMulai = tanggal_mulai;
                                dateChoiceAkhir = tanggal_akhir;

                                String input_date = dateChoiceMulai;
                                SimpleDateFormat format1 =new SimpleDateFormat("yyyy-MM-dd");
                                Date dt1 = null;
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

                                String input_date_akhir = dateChoiceAkhir;
                                SimpleDateFormat format1_akhir = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt1_akhir = null;
                                try {
                                    dt1_akhir = format1_akhir.parse(input_date_akhir);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                DateFormat format2_akhir = new SimpleDateFormat("EEE");
                                String finalDay_akhir = format2_akhir.format(dt1_akhir);
                                String hariName_akhir = "";

                                if (finalDay_akhir.equals("Mon") || finalDay_akhir.equals("Sen")) {
                                    hariName_akhir = "Senin";
                                } else if (finalDay_akhir.equals("Tue") || finalDay_akhir.equals("Sel")) {
                                    hariName_akhir = "Selasa";
                                } else if (finalDay_akhir.equals("Wed") || finalDay_akhir.equals("Rab")) {
                                    hariName_akhir = "Rabu";
                                } else if (finalDay_akhir.equals("Thu") || finalDay_akhir.equals("Kam")) {
                                    hariName_akhir = "Kamis";
                                } else if (finalDay_akhir.equals("Fri") || finalDay_akhir.equals("Jum")) {
                                    hariName_akhir = "Jumat";
                                } else if (finalDay_akhir.equals("Sat") || finalDay_akhir.equals("Sab")) {
                                    hariName_akhir = "Sabtu";
                                } else if (finalDay_akhir.equals("Sun") || finalDay_akhir.equals("Min")) {
                                    hariName_akhir = "Minggu";
                                }

                                String dayDate_akhir = input_date_akhir.substring(8,10);
                                String yearDate_akhir = input_date_akhir.substring(0,4);;
                                String bulanValue_akhir = input_date_akhir.substring(5,7);
                                String bulanName_akhir;

                                switch (bulanValue_akhir) {
                                    case "01":
                                        bulanName_akhir = "Januari";
                                        break;
                                    case "02":
                                        bulanName_akhir = "Februari";
                                        break;
                                    case "03":
                                        bulanName_akhir = "Maret";
                                        break;
                                    case "04":
                                        bulanName_akhir = "April";
                                        break;
                                    case "05":
                                        bulanName_akhir = "Mei";
                                        break;
                                    case "06":
                                        bulanName_akhir = "Juni";
                                        break;
                                    case "07":
                                        bulanName_akhir = "Juli";
                                        break;
                                    case "08":
                                        bulanName_akhir = "Agustus";
                                        break;
                                    case "09":
                                        bulanName_akhir = "September";
                                        break;
                                    case "10":
                                        bulanName_akhir = "Oktober";
                                        break;
                                    case "11":
                                        bulanName_akhir = "November";
                                        break;
                                    case "12":
                                        bulanName_akhir = "Desember";
                                        break;
                                    default:
                                        bulanName_akhir = "Not found!";
                                        break;
                                }

                                sampaiTanggalTV.setText(hariName_akhir+", "+String.valueOf(Integer.parseInt(dayDate_akhir))+" "+bulanName_akhir+" "+yearDate_akhir);

                                jumlahHariTV.setText(jumlah_hari+" Hari");
                                jenisCutiTV.setText(jenis_cuti_deskripsi);
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_CUTI, jenis_cuti);

                                alasanTV.setText(alasan_cuti);
                                penggantiSelamaCutiTV.setText(karyawan_pengganti);
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PENGGANTI, pengganti);
                                alamatSelamaCutiTV.setText(alamat_selama_cuti);
                                noHpTV.setText(no_hp);

                                if(tipeCuti.equals("1")){
                                    if(lampiran.equals("null") || lampiran.equals("") || lampiran.equals(null)){
                                        removeLampiranBTN.setVisibility(View.GONE);
                                    } else {
                                        removeLampiranBTN.setVisibility(View.VISIBLE);
                                    }
                                } else if(tipeCuti.equals("2")){
                                    removeLampiranBTN.setVisibility(View.GONE);
                                }

                                if(!lampiran.equals("null") && !lampiran.equals("") && !lampiran.equals(null)){
                                    idIzin = idRecord;

                                    markUpload.setVisibility(View.VISIBLE);
                                    viewUploadBTN.setVisibility(View.VISIBLE);
                                    statusUploadTV.setText("Diunggah");
                                    labelUnggahTV.setText("Ganti");
                                    uploadStatus = "1";
                                    String url_lampiran = "https://geloraaksara.co.id/absen-online/upload/lampiran_cuti/"+lampiran;

                                    viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(EditPermohonanCutiActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", url_lampiran);
                                            intent.putExtra("kode", "detail");
                                            intent.putExtra("jenis_detail", "cuti");
                                            startActivity(intent);
                                        }
                                    });
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
                params.put("id_izin_record", idRecord);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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
                        dariTanggalTV.setText("Pilih kembali !");
                        dateChoiceMulai = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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
                        dariTanggalTV.setText("Pilih kembali !");
                        dateChoiceMulai = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

            }, y,m,d);
            dpd.show();
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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
                        sampaiTanggalTV.setText("Pilih kembali !");
                        dateChoiceAkhir = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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
                        sampaiTanggalTV.setText("Pilih kembali !");
                        dateChoiceAkhir = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

            }, y,m,d);
            dpd.show();
        }

    }

    private void kategoriCuti(){
        bottomSheet.showWithSheetView(LayoutInflater.from(EditPermohonanCutiActivity.this).inflate(R.layout.layout_kategori_cuti, bottomSheet, false));
        kategoriCutiRV = findViewById(R.id.kategori_cuti_rv);

        kategoriCutiRV.setLayoutManager(new LinearLayoutManager(this));
        kategoriCutiRV.setHasFixedSize(true);
        kategoriCutiRV.setNestedScrollingEnabled(false);
        kategoriCutiRV.setItemAnimator(new DefaultItemAnimator());

        getkategoriCuti();

    }

    private void getkategoriCuti() {
        RequestQueue requestQueue = Volley.newRequestQueue(EditPermohonanCutiActivity.this);
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
                            adapterKategoriIzin = new AdapterKategoriIzinEdit(kategoriIzins,EditPermohonanCutiActivity.this);
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

    }

    private void karyawanPengganti(){
        bottomSheet.showWithSheetView(LayoutInflater.from(EditPermohonanCutiActivity.this).inflate(R.layout.layout_karyawan_pengganti, bottomSheet, false));
        keywordKaryawanPengganti = findViewById(R.id.keyword_user_ed);

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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataKaryawanPengganti(keyWordSearch);
                    }
                }, 500);
            }

        });

        keywordKaryawanPengganti.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = keywordKaryawanPengganti.getText().toString();
                    getDataKaryawanPengganti(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) EditPermohonanCutiActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = EditPermohonanCutiActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(EditPermohonanCutiActivity.this);
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
                                adapterKaryawanPengganti = new AdapterKaryawanPenggantiEdit(karyawanPenggantis, EditPermohonanCutiActivity.this);
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

            if(tipeCuti.equals("1") && uploadStatus.equals("1")){
                removeLampiranBTN.setVisibility(View.VISIBLE);
            } else {
                removeLampiranBTN.setVisibility(View.GONE);
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
            jenisCutiTV.setText(descCuti);

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

            InputMethodManager imm = (InputMethodManager) EditPermohonanCutiActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = EditPermohonanCutiActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(EditPermohonanCutiActivity.this);
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

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(EditPermohonanCutiActivity.this)
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
            Dexter.withActivity(EditPermohonanCutiActivity.this)
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
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(EditPermohonanCutiActivity.this);
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
        Intent intent = new Intent(EditPermohonanCutiActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(EditPermohonanCutiActivity.this, ImagePickerActivity.class);
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
        String file_directori = getRealPathFromURIPath(uri, EditPermohonanCutiActivity.this);
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
                        String file_directori = getRealPathFromURIPath(uri, EditPermohonanCutiActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);
                        markUpload.setVisibility(View.VISIBLE);
                        viewUploadBTN.setVisibility(View.VISIBLE);
                        statusUploadTV.setText("Berhasil diunggah");
                        labelUnggahTV.setText("Ganti");
                        uploadStatus = "1";
                        gantiLampiran = "1";
                        removeLampiranStatus = "0";

                        if(tipeCuti.equals("1")){
                            removeLampiranBTN.setVisibility(View.VISIBLE);
                        } else if(tipeCuti.equals("2")){
                            removeLampiranBTN.setVisibility(View.GONE);
                        }

                        viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EditPermohonanCutiActivity.this, ViewImageActivity.class);
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
                                new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.ERROR_TYPE)
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

    private void submitCuti(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cuti_edit";
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
                                idIzin = idRecord;

                                if(uploadStatus.equals("1")){
                                    if(gantiLampiran.equals("1")){
                                        uploadLampiran();
                                    } else {
                                        permohonanTerkirim = "1";

                                        pDialog.setTitleText("Berhasil Terupdate")
                                                .setContentText("Data permohonan berhasil terupdate")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                        onBackPressed();
                                                    }
                                                })
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    }

                                } else {
                                    if(gantiLampiran.equals("1")){
                                        uploadLampiran();
                                    } else {
                                        permohonanTerkirim = "1";

                                        pDialog.setTitleText("Berhasil Terupdate")
                                                .setContentText("Data permohonan berhasil terupdate")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                        onBackPressed();
                                                    }
                                                })
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    }
                                }

                            } else if (status.equals("Available")){
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Permohonan dengan tanggal yang sama sudah pernah anda ajukan, harap ubah tanggal permohonan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else {
                                pDialog.setTitleText("Gagal Terupdate")
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
                        pDialog.setTitleText("Gagal Terupdate")
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
                params.put("id_izin_record", idRecord);
                params.put("NIK", sharedPrefManager.getSpNik());

                params.put("tanggal", getDate());
                params.put("time", getTime());
                params.put("jenis_cuti", kategoriCuti);
                params.put("tanggal_mulai", dateChoiceMulai);
                params.put("tanggal_akhir", dateChoiceAkhir);

                params.put("tipe_cuti", tipeCuti);
                params.put("alasan_cuti", alasanTV.getText().toString());
                params.put("pengganti", nikKaryawanPengganti);
                params.put("alamat_selama_cuti", alamatSelamaCutiTV.getText().toString());
                params.put("no_hp", noHpTV.getText().toString());

                params.put("remove_lampiran", removeLampiranStatus);

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
                pDialog.setTitleText("Berhasil Terupdate")
                        .setContentText("Data permohonan berhasil terupdate")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                                onBackPressed();
                            }
                        })
                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);


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

    private void connectionFailed(){
        CookieBar.build(EditPermohonanCutiActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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
                    new KAlertDialog(EditPermohonanCutiActivity.this, KAlertDialog.WARNING_TYPE)
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

}