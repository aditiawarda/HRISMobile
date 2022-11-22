package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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
import com.gelora.absensi.adapter.AdapterKehadiranBagianSearch;
import com.gelora.absensi.adapter.AdapterKetidakhadiranBagianSearch;
import com.gelora.absensi.adapter.AdapterStatusAbsen;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataMonitoringKehadiranBagian;
import com.gelora.absensi.model.DataMonitoringKetidakhadiranBagian;
import com.gelora.absensi.model.KaryawanPengganti;
import com.gelora.absensi.model.KategoriIzin;
import com.gelora.absensi.model.StatusAbsen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shasin.notificationbanner.Banner;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormPermohonanCutiActivity extends AppCompatActivity {

    LinearLayout backBTN, homeBTN, dariTanggalPicker, sampaiTanggalPicker, tipeCutiBTN, submitBTN, loadingDataPart, penggantiSelamaCutiBTN, startAttantionPart, noDataPart;
    SwipeRefreshLayout refreshLayout;
    TextView namaKaryawan, nikKaryawan, jabatanKaryawan, bagianKaryawan, penggantiSelamaCutiTV, tanggalMulaiBekerja, statuskaryawan, kategoriCutiPilihTV, sisaCuti, tahunCutiTelah, totalCutiTelah, dariTanggalTV, sampaiTanggalTV;
    String dateChoiceMulai = "", dateChoiceAkhir = "", idCuti = "", kodeCuti = "", descCuti = "", nikKaryawanPengganti, namaKaryawanPenganti;
    ImageView loadingGif;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_permohonan_cuti);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        rootview = findViewById(android.R.id.content);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
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
        tipeCutiBTN = findViewById(R.id.tipe_cuti);
        kategoriCutiPilihTV = findViewById(R.id.kategori_cuti_pilih);
        penggantiSelamaCutiTV = findViewById(R.id.pengganti_selama_cuti_tv);
        penggantiSelamaCutiBTN = findViewById(R.id.pengganti_selama_cuti_part);
        alasanTV = findViewById(R.id.alasan_tv);
        alamatSelamaCutiTV = findViewById(R.id.alamat_selama_cuti_tv);
        noHpTV = findViewById(R.id.no_hp_tv);
        submitBTN = findViewById(R.id.submit_btn);

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
                        dariTanggalTV.setText("");
                        sampaiTanggalTV.setText("");
                        kategoriCutiPilihTV.setText("");
                        penggantiSelamaCutiTV.setText("");
                        alasanTV.setText("");
                        alamatSelamaCutiTV.setText("");
                        noHpTV.setText("");

                        alasanTV.clearFocus();
                        alamatSelamaCutiTV.clearFocus();
                        noHpTV.clearFocus();

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

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormPermohonanCutiActivity.this, MapsActivity.class);
                startActivity(intent);
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

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                } else {
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
                                jabatanKaryawan.setText(jabatan);
                                bagianKaryawan.setText(bagian+" | "+department);
                                tanggalMulaiBekerja.setText(tanggal_masuk.substring(8,10)+"/"+tanggal_masuk.substring(5,7)+"/"+tanggal_masuk.substring(0,4));
                                statuskaryawan.setText(status_karyawan);
                                sisaCuti.setText(sisa_cuti);
                                tahunCutiTelah.setText(getDateY());
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

    private void kategoriCuti(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_kategori_cuti, bottomSheet, false));
        kategoriCutiRV = findViewById(R.id.kategori_cuti_rv);

        kategoriCutiRV.setLayoutManager(new LinearLayoutManager(this));
        kategoriCutiRV.setHasFixedSize(true);
        kategoriCutiRV.setNestedScrollingEnabled(false);
        kategoriCutiRV.setItemAnimator(new DefaultItemAnimator());

        getkategoriCuti();

    }

    private void karyawanPengganti(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_karyawan_pengganti, bottomSheet, false));
        keywordKaryawanPengganti = findViewById(R.id.keyword_user_ed);

        karyawanPenggantiRV = findViewById(R.id.karyawan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
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
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/izin_kategori";
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
        Banner.make(rootview, FormPermohonanCutiActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();
    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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

                    } else {
                        dariTanggalTV.setText("Pilih kembali !");
                        dateChoiceMulai = "";

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
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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

                    } else {
                        dariTanggalTV.setText("Pilih kembali !");
                        dateChoiceMulai = "";

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

            }, y,m,d);
            dpd.show();
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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

                    } else {
                        sampaiTanggalTV.setText("Pilih kembali !");
                        dateChoiceAkhir = "";

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
            DatePickerDialog dpd = new DatePickerDialog(FormPermohonanCutiActivity.this, (view1, year, month, dayOfMonth) -> {

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

                    } else {
                        sampaiTanggalTV.setText("Pilih kembali !");
                        dateChoiceAkhir = "";

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

            }, y,m,d);
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

    @Override
    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}