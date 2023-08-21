package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterShiftAbsenFinger;
import com.gelora.absensi.adapter.AdapterStatusAbsenFinger;
import com.gelora.absensi.adapter.AdapterTitikAbsenFinger;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.ShiftAbsen;
import com.gelora.absensi.model.StatusAbsen;
import com.gelora.absensi.model.TitikAbsensi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormFingerscanActivity extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    LinearLayout formPart, successPart, viewBTN, actionBar;
    LinearLayout fingerscanHistoryBTN, closeBTN, okBTN, backBTN, dateBTN, submitBTN, detailKeterangan1, detailKeterangan3, detailKeterangan6;
    TextView namaTV, nikTV, detailTV, datePilihTV, labelDetail;
    EditText alasanED;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    RadioGroup keteranganGroup;
    RadioButton keterangan1, keterangan2, keterangan3, keterangan4, keterangan5, keterangan6, keterangan7;
    String idPermohonan = "", kategoriKeterangan = "", pilihanKeterangan = "",dateChoicePulang = "", dateChoiceMasuk = "", currentDay = "", alasanDesc = "", permohonanTerkirim = "0";
    BottomSheetLayout bottomSheet;
    KAlertDialog pDialog;
    ImageView successGif;
    private int i = -1;

    //Kategori 1
    LinearLayout statusAbsensiBTNK1, shiftAbsensiBTNK1, jamPulangBTNK1, titikBTNK1, tglPulangBTNK1, tglPulangPartK1;
    TextView statusAbsensiPilihK1, shiftAbsensiPilihK1, jamMasukTVK1, jamPulangPilihK1, titikPilihK1, datePulangPilihK1;
    String statusAbsensi = "", shiftAbsensi = "", tanggalPulang = "0000-00-00", jamMasuk = "", jamPulang = "", namaTitikAbsensi = "";

    //Kategori 3
    LinearLayout tglPulangPartK3, tglPulangBTNK3, jamPulangBTNK3, titikBTNK3;
    TextView tglPulangPilihK3, shiftTVK3, jamPulangPilihK3, titikPilihK3;

    //Kategori 6
    LinearLayout statusAbsensiBTNK6, shiftAbsensiBTNK6;
    TextView statusAbsensiPilihK6, shiftAbsensiPilihK6;

    LinearLayout timepickerBackground, timepickerDialog;
    TimePicker timepicker;

    private RecyclerView statusAbsenRV;
    private StatusAbsen[] statusAbsens;
    private AdapterStatusAbsenFinger adapterStatusAbsen;

    private RecyclerView shifAbsenRV;
    private ShiftAbsen[] shiftAbsens;
    private AdapterShiftAbsenFinger adapterShiftAbsen;

    private RecyclerView titikAbsenRV;
    private TitikAbsensi[] titikAbsensis;
    private AdapterTitikAbsenFinger adapterTitikAbsenFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fingerscan);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        fingerscanHistoryBTN = findViewById(R.id.history_fingerscan_btn);
        viewBTN = findViewById(R.id.view_permohonan_btn);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        backBTN = findViewById(R.id.back_btn);
        namaTV = findViewById(R.id.nama_karyawan_tv);
        nikTV = findViewById(R.id.nik_karyawan_tv);
        detailTV = findViewById(R.id.detail_karyawan_tv);
        submitBTN = findViewById(R.id.submit_btn);
        keteranganGroup = findViewById(R.id.pilihan_keterangan);
        keterangan1 = findViewById(R.id.keterangan_1);
        keterangan2 = findViewById(R.id.keterangan_2);
        keterangan3 = findViewById(R.id.keterangan_3);
        keterangan4 = findViewById(R.id.keterangan_4);
        keterangan5 = findViewById(R.id.keterangan_5);
        keterangan6 = findViewById(R.id.keterangan_6);
        keterangan7 = findViewById(R.id.keterangan_7);
        labelDetail = findViewById(R.id.label_detail);
        detailKeterangan1 = findViewById(R.id.detail_keterangan_1);
        detailKeterangan3 = findViewById(R.id.detail_keterangan_3);
        detailKeterangan6 = findViewById(R.id.detail_keterangan_6);
        dateBTN = findViewById(R.id.date_btn);
        datePilihTV = findViewById(R.id.date_pilih);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        successGif = findViewById(R.id.success_gif);
        actionBar = findViewById(R.id.action_bar);

        //Kategori Keterangan 1
        statusAbsensiBTNK1 = findViewById(R.id.status_absensi_btn_k1);
        shiftAbsensiBTNK1 = findViewById(R.id.shift_absensi_btn_k1);
        statusAbsensiPilihK1 = findViewById(R.id.status_absensi_pilih_k1);
        shiftAbsensiPilihK1 = findViewById(R.id.shift_absensi_pilih_k1);
        jamMasukTVK1 = findViewById(R.id.jam_masuk_tv_k1);
        jamPulangBTNK1 = findViewById(R.id.jam_pulang_btn_k1);
        jamPulangPilihK1 = findViewById(R.id.jam_pulang_pilih_k1);
        titikBTNK1 = findViewById(R.id.titik_btn_k1);
        titikPilihK1 = findViewById(R.id.titik_pilih_k1);
        tglPulangBTNK1 = findViewById(R.id.tgl_pulang_btn_k1);
        datePulangPilihK1 = findViewById(R.id.tgl_pulang_pilih_k1);
        tglPulangPartK1 = findViewById(R.id.tgl_pulang_part_k1);

        //Kategori Keterangan 3
        shiftTVK3 = findViewById(R.id.shift_tv_k3);
        tglPulangPartK3 = findViewById(R.id.tgl_pulang_part_k3);
        tglPulangBTNK3 = findViewById(R.id.tgl_pulang_btn_k3);
        tglPulangPilihK3 = findViewById(R.id.tgl_pulang_pilih_k3);
        jamPulangBTNK3 = findViewById(R.id.jam_pulang_btn_k3);
        jamPulangPilihK3 = findViewById(R.id.jam_pulang_pilih_k3);
        titikBTNK3 = findViewById(R.id.titik_btn_k3);
        titikPilihK3 = findViewById(R.id.titik_pilih_k3);

        //Kategori Keterangan 6
        statusAbsensiBTNK6 = findViewById(R.id.status_absensi_btn_k6);
        shiftAbsensiBTNK6 = findViewById(R.id.shift_absensi_btn_k6);
        statusAbsensiPilihK6 = findViewById(R.id.status_absensi_pilih_k6);
        shiftAbsensiPilihK6 = findViewById(R.id.shift_absensi_pilih_k6);

        alasanED = findViewById(R.id.alasan_tv);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        LocalBroadcastManager.getInstance(this).registerReceiver(statusAbsenBroad, new IntentFilter("status_absen_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(shiftAbsenBroad, new IntentFilter("shift_absen_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(titikAbsenBroad, new IntentFilter("titik_absen_broad"));

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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);

                        jamMasuk = "";
                        currentDay = "";
                        dateChoiceMasuk = "";
                        dateChoicePulang = "";
                        alasanDesc = "";
                        pilihanKeterangan = "";
                        kategoriKeterangan = "";
                        statusAbsensi = "";
                        shiftAbsensi = "";
                        tanggalPulang = "0000-00-00";
                        jamPulang = "";
                        namaTitikAbsensi = "";
                        keteranganGroup.clearCheck();
                        detailKeterangan1.setVisibility(View.GONE);
                        detailKeterangan3.setVisibility(View.GONE);
                        detailKeterangan6.setVisibility(View.GONE);
                        labelDetail.setVisibility(View.GONE);

                        alasanED.setText("");
                        datePilihTV.setText("");

                        statusAbsensiPilihK1.setText("");
                        shiftAbsensiPilihK1.setText("Tentukan Status Absensi...");
                        jamMasukTVK1.setText("Tentukan Shift Absensi...");
                        jamPulangPilihK1.setText("");
                        titikPilihK1.setText("");

                        tglPulangPilihK3.setText("");
                        jamPulangPilihK3.setText("");
                        titikPilihK3.setText("");
                        shiftTVK3.setText("Tentukan Tanggal Masuk...");

                        statusAbsensiPilihK6.setText("");
                        shiftAbsensiPilihK6.setText("Tentukan Status Absensi...");

                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_TITIK_ABSENSI, "");

                        getDataKaryawan();
                        getKeterangan();

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

        fingerscanHistoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormFingerscanActivity.this, HistoryFingerscanActivity.class);
                startActivity(intent);
            }
        });

        viewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormFingerscanActivity.this, DetailPermohonanFingerscanActivity.class);
                intent.putExtra("kode", "form");
                intent.putExtra("id_permohonan", idPermohonan);
                startActivity(intent);
            }
        });

        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                datePicker();
            }
        });

        keteranganGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if (keterangan1.isChecked()) {
                    pilihanKeterangan = keterangan1.getText().toString();
                    kategoriKeterangan = "1";
                    detailKeterangan1.setVisibility(View.VISIBLE);
                    detailKeterangan3.setVisibility(View.GONE);
                    detailKeterangan6.setVisibility(View.GONE);
                    labelDetail.setVisibility(View.VISIBLE);
                } else if (keterangan2.isChecked()) {
                    pilihanKeterangan = keterangan2.getText().toString();
                    kategoriKeterangan = "2";
                    detailKeterangan1.setVisibility(View.GONE);
                    detailKeterangan3.setVisibility(View.GONE);
                    detailKeterangan6.setVisibility(View.GONE);
                    labelDetail.setVisibility(View.GONE);
                } else if (keterangan3.isChecked()) {
                    pilihanKeterangan = keterangan3.getText().toString();
                    kategoriKeterangan = "3";
                    detailKeterangan1.setVisibility(View.GONE);
                    detailKeterangan3.setVisibility(View.VISIBLE);
                    detailKeterangan6.setVisibility(View.GONE);
                    labelDetail.setVisibility(View.VISIBLE);

                    if(!dateChoiceMasuk.equals("")){
                        getShift();
                    }

                } else if (keterangan4.isChecked()) {
                    pilihanKeterangan = keterangan4.getText().toString();
                    kategoriKeterangan = "4";
                    detailKeterangan1.setVisibility(View.GONE);
                    detailKeterangan3.setVisibility(View.GONE);
                    detailKeterangan6.setVisibility(View.GONE);
                    labelDetail.setVisibility(View.GONE);
                } else if (keterangan5.isChecked()) {
                    pilihanKeterangan = keterangan5.getText().toString();
                    kategoriKeterangan = "5";
                    detailKeterangan1.setVisibility(View.GONE);
                    detailKeterangan3.setVisibility(View.GONE);
                    detailKeterangan6.setVisibility(View.GONE);
                    labelDetail.setVisibility(View.GONE);
                } else if (keterangan6.isChecked()) {
                    pilihanKeterangan = keterangan6.getText().toString();
                    kategoriKeterangan = "6";
                    detailKeterangan1.setVisibility(View.GONE);
                    detailKeterangan3.setVisibility(View.GONE);
                    detailKeterangan6.setVisibility(View.VISIBLE);
                    labelDetail.setVisibility(View.VISIBLE);
                } else if (keterangan7.isChecked()) {
                    pilihanKeterangan = keterangan6.getText().toString();
                    kategoriKeterangan = "7";
                    detailKeterangan1.setVisibility(View.GONE);;
                    detailKeterangan3.setVisibility(View.GONE);
                    detailKeterangan6.setVisibility(View.GONE);
                    labelDetail.setVisibility(View.GONE);
                }
            }
        });

        //Kategori Keterangan 1
        statusAbsensiBTNK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(currentDay.equals("")){
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Harap tentukan tanggal masuk terlebih dahulu!")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
                } else {
                    statusAbsen();
                }
            }
        });
        shiftAbsensiBTNK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(currentDay.equals("")){
                    if(statusAbsensiPilihK1.getText().toString().equals("")){
                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal masuk dan pilih status absensi terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                    } else {
                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal masuk terlebih dahulu!")
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
                    if(statusAbsensiPilihK1.getText().toString().equals("")){
                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih status absensi terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                    } else {
                        shiftAbsen();
                    }
                }
            }
        });
        tglPulangBTNK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(dateChoiceMasuk.equals("")){
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal masuk terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    datePickerPulang();
                }
            }
        });
        jamPulangBTNK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(datePulangPilihK1.getText().toString().equals("")){
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal pulang terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                      jamPulangPicker();
                    } else {
                        int h = Integer.parseInt(getTimeH());
                        int m = Integer.parseInt(getTimeM());

                        @SuppressLint({"DefaultLocale", "SetTextI18n"})
                        TimePickerDialog tpd = new TimePickerDialog(FormFingerscanActivity.this, R.style.timePickerStyle, (view1, hourOfDay, minute) -> {
                            jamPulang = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00";

                            if(tanggalPulang.equals(getDate())){
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                Date time = null;
                                Date time2 = null;
                                try {
                                    time = sdf.parse(String.valueOf(jamPulang));
                                    time2 = sdf.parse(getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long pilih = time.getTime();
                                long sekarang = time2.getTime();

                                if (pilih<=sekarang){
                                    jamPulangPilihK1.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00");
                                    jamPulang = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00";
                                } else {
                                    jamPulang = "";
                                    jamPulangPilihK1.setText("Pilih kembali !");

                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("Jam pulang tidak dapat lebih besar dari jam saat ini. Harap ulangi!")
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
                                jamPulangPilihK1.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00");
                                jamPulang = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00";
                            }

                        },
                        h,
                        m,
                        android.text.format.DateFormat.is24HourFormat(FormFingerscanActivity.this));
                        tpd.show();

                    }
                }
            }
        });
        titikBTNK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                titikAbsen();
            }
        });

        //Kategori Keterangan 3
        tglPulangBTNK3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(dateChoiceMasuk.equals("")){
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal masuk terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    datePickerPulang();
                }
            }
        });
        jamPulangBTNK3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(tglPulangPilihK3.getText().toString().equals("")){
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal pulang terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        jamPulangPicker();
                    }
                    else {
                        int h = Integer.parseInt(getTimeH());
                        int m = Integer.parseInt(getTimeM());

                        @SuppressLint({"DefaultLocale", "SetTextI18n"})
                        TimePickerDialog tpd = new TimePickerDialog(FormFingerscanActivity.this, R.style.timePickerStyle, (view1, hourOfDay, minute) -> {
                            jamPulang = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00";

                            if(tanggalPulang.equals(getDate())){
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                Date time = null;
                                Date time2 = null;
                                try {
                                    time = sdf.parse(String.valueOf(jamPulang));
                                    time2 = sdf.parse(getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long pilih = time.getTime();
                                long sekarang = time2.getTime();

                                if (pilih<=sekarang){
                                    if(kategoriKeterangan.equals("1")){
                                        jamPulangPilihK1.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00");
                                    } else if(kategoriKeterangan.equals("3")){
                                        jamPulangPilihK3.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00");
                                    }
                                    jamPulang = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00";
                                } else {
                                    jamPulang = "";
                                    jamPulangPilihK3.setText("Pilih kembali !");

                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("Jam pulang tidak dapat lebih besar dari jam saat ini. Harap ulangi!")
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
                                jamPulangPilihK3.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00");
                                jamPulang = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":00";
                            }

                        },
                                h,
                                m,
                                android.text.format.DateFormat.is24HourFormat(FormFingerscanActivity.this));
                        tpd.show();

                    }
                }

            }
        });
        titikBTNK3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                titikAbsen();
            }
        });

        //Kategori Keterangan 6
        statusAbsensiBTNK6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(currentDay.equals("")){
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap tentukan tanggal masuk terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    statusAbsen();
                }
            }
        });
        shiftAbsensiBTNK6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(currentDay.equals("")){
                    if(statusAbsensiPilihK6.getText().toString().equals("")){
                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap tentukan tanggal masuk dan pilih status absensi terlebih dahulu!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap tentukan tanggal masuk terlebih dahulu!")
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
                    if(statusAbsensiPilihK6.getText().toString().equals("")){
                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap pilih status absensi terlebih dahulu!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        shiftAbsen();
                    }
                }
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                if(dateChoiceMasuk.equals("")){
                    if(kategoriKeterangan.equals("")){
                        if(alasanED.getText().toString().equals("")){
                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal masuk dan keterangan!")
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
                        if(kategoriKeterangan.equals("1")){
                            if(statusAbsensi.equals("")){
                                if(shiftAbsensi.equals("")){
                                    if(jamPulang.equals("")){
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, jam pulang, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, jam pulang dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, jam pulang dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift dan jam pulang!")
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
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi dan shift!")
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
                                    if(shiftAbsensi.equals("6")||shiftAbsensi.equals("9")||shiftAbsensi.equals("14")||shiftAbsensi.equals("15")||shiftAbsensi.equals("41")||shiftAbsensi.equals("81")||shiftAbsensi.equals("91")||shiftAbsensi.equals("95")||shiftAbsensi.equals("85")||shiftAbsensi.equals("87")){
                                        if(tanggalPulang.equals("0000-00-00")){
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang, jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, tanggal pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi dan tanggal pulang!")
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
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, status absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk dan status absensi!")
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
                                        if(jamPulang.equals("")){
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi, jam pulang, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi, jam pulang dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi, jam pulang dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi dan jam pulang!")
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
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, status absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk dan status absensi!")
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
                                if(shiftAbsensi.equals("")){
                                    if(jamPulang.equals("")){
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, jam pulang, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, jam pulang dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, jam pulang dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift dan jam pulang!")
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
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi, shift dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi tanggal masuk, status absensi dan shift!")
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
                                    if(shiftAbsensi.equals("6")||shiftAbsensi.equals("9")||shiftAbsensi.equals("14")||shiftAbsensi.equals("15")||shiftAbsensi.equals("41")||shiftAbsensi.equals("81")||shiftAbsensi.equals("91")||shiftAbsensi.equals("95")||shiftAbsensi.equals("85")||shiftAbsensi.equals("87")){
                                        if(tanggalPulang.equals("0000-00-00")){
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang, jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, tanggal pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk dan tanggal pulang!")
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
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal masuk dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap tanggal masuk dan isi alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap tanggal masuk!")
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
                                        if(jamPulang.equals("")){
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, jam pulang, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, jam pulang dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, jam pulang dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk dan jam pulang!")
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
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi tanggal masuk!")
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
                        else if(kategoriKeterangan.equals("3")){
                            if(tglPulangPilihK3.getText().toString().equals("")){
                                if(jamPulangPilihK3.getText().toString().equals("")){
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang, jam pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang, jam pulang dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang, jam pulang dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang dan jam pulang!")
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
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, tanggal pulang dan titik absensi!")
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
                                if(jamPulangPilihK3.getText().toString().equals("")){
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, jam pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, jam pulang dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, jam pulang dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk dan jam pulang!")
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
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal masuk!")
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
                        else if(kategoriKeterangan.equals("6")){
                            if(statusAbsensiPilihK6.getText().toString().equals("")){
                                if(shiftAbsensiPilihK6.getText().toString().equals("")){
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk, status absensi, shift dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk, status absensi dan shift!")
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
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk, status absensi dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk dan status absensi!")
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
                                if(shiftAbsensiPilihK6.getText().toString().equals("")){
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk, shift dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk dan shift!")
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
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi tanggal masuk!")
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
                        else {
                            if(alasanED.getText().toString().equals("")){
                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal masuk dan alasan!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal masuk!")
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
                    if(kategoriKeterangan.equals("")){
                        if(alasanED.getText().toString().equals("")){
                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi keterangan dan alasan!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi keterangan!")
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
                        if(kategoriKeterangan.equals("1")){
                            if(statusAbsensi.equals("")){
                                if(shiftAbsensi.equals("")){
                                    if(jamPulang.equals("")){
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, jam pulang, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, jam pulang dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, jam pulang dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift dan jam pulang!")
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
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi dan shift!")
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
                                    if(shiftAbsensi.equals("6")||shiftAbsensi.equals("9")||shiftAbsensi.equals("14")||shiftAbsensi.equals("15")||shiftAbsensi.equals("41")||shiftAbsensi.equals("81")||shiftAbsensi.equals("91")||shiftAbsensi.equals("95")||shiftAbsensi.equals("85")||shiftAbsensi.equals("87")){
                                        if(tanggalPulang.equals("0000-00-00")){
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang, jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, tanggal pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi dan tanggal pulang!")
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
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, jam pulang,titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi status absensi!")
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
                                        if(jamPulang.equals("")){
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi, jam pulang, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi, jam pulang dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi, jam pulang dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi dan jam pulang!")
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
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi status absensi!")
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
                                if(shiftAbsensi.equals("")){
                                    if(jamPulang.equals("")){
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, jam pulang, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, jam pulang dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, jam pulang dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift dan jam pulang!")
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
                                        if(namaTitikAbsensi.equals("")){
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift, titik absensi dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift dan titik absensi!")
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
                                            if(alasanED.getText().toString().equals("")){
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi, shift dan alasan!")
                                                        .setConfirmText("    OK    ")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Harap isi status absensi dan shift!")
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
                                    if(shiftAbsensi.equals("6")||shiftAbsensi.equals("9")||shiftAbsensi.equals("14")||shiftAbsensi.equals("15")||shiftAbsensi.equals("41")||shiftAbsensi.equals("81")||shiftAbsensi.equals("91")||shiftAbsensi.equals("95")||shiftAbsensi.equals("85")||shiftAbsensi.equals("87")){
                                        if(tanggalPulang.equals("0000-00-00")){
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang, jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang, jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang, jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang dan jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi tanggal pulang!")
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
                                            if(jamPulang.equals("")){
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi jam pulang, titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi jam pulang dan titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi jam pulang dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi jam pulang!")
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
                                                if(namaTitikAbsensi.equals("")){
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi titik absensi dan alasan!")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Harap isi titik absensi!")
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
                                                    if(alasanED.getText().toString().equals("")){
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
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
                                                    } else {
                                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                                        pDialog = new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                                        pDialog.show();
                                                                        pDialog.setCancelable(false);
                                                                        new CountDownTimer(1300, 800) {
                                                                            public void onTick(long millisUntilFinished) {
                                                                                i++;
                                                                                switch (i) {
                                                                                    case 0:
                                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                                (FormFingerscanActivity.this, R.color.colorGradien));
                                                                                        break;
                                                                                    case 1:
                                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                                (FormFingerscanActivity.this, R.color.colorGradien2));
                                                                                        break;
                                                                                    case 2:
                                                                                    case 6:
                                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                                (FormFingerscanActivity.this, R.color.colorGradien3));
                                                                                        break;
                                                                                    case 3:
                                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                                (FormFingerscanActivity.this, R.color.colorGradien4));
                                                                                        break;
                                                                                    case 4:
                                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                                (FormFingerscanActivity.this, R.color.colorGradien5));
                                                                                        break;
                                                                                    case 5:
                                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                                (FormFingerscanActivity.this, R.color.colorGradien6));
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
                                    } else {
                                        if(jamPulang.equals("")){
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi jam pulang, titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi jam pulang dan titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi jam pulang dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi jam pulang!")
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
                                            if(namaTitikAbsensi.equals("")){
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi titik absensi dan alasan!")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Harap isi titik absensi!")
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
                                                if(alasanED.getText().toString().equals("")){
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
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
                                                } else {
                                                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                                    pDialog = new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                                    pDialog.show();
                                                                    pDialog.setCancelable(false);
                                                                    new CountDownTimer(1300, 800) {
                                                                        public void onTick(long millisUntilFinished) {
                                                                            i++;
                                                                            switch (i) {
                                                                                case 0:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormFingerscanActivity.this, R.color.colorGradien));
                                                                                    break;
                                                                                case 1:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormFingerscanActivity.this, R.color.colorGradien2));
                                                                                    break;
                                                                                case 2:
                                                                                case 6:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormFingerscanActivity.this, R.color.colorGradien3));
                                                                                    break;
                                                                                case 3:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormFingerscanActivity.this, R.color.colorGradien4));
                                                                                    break;
                                                                                case 4:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormFingerscanActivity.this, R.color.colorGradien5));
                                                                                    break;
                                                                                case 5:
                                                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                            (FormFingerscanActivity.this, R.color.colorGradien6));
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
                        else if(kategoriKeterangan.equals("3")){
                            if(tglPulangPilihK3.getText().toString().equals("")){
                                if(jamPulangPilihK3.getText().toString().equals("")){
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang, jam pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang, jam pulang dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang, jam pulang dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang dan jam pulang!")
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
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi tanggal pulang dan titik absensi!")
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
                                if(jamPulangPilihK3.getText().toString().equals("")){
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi jam pulang, titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi jam pulang dan titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi jam pulang dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi jam pulang!")
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
                                    if(titikPilihK3.getText().toString().equals("")){
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi titik absensi dan alasan!")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Harap isi titik absensi!")
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
                                        if(alasanED.getText().toString().equals("")){
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
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
                                        } else {
                                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                            pDialog = new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                            pDialog.show();
                                                            pDialog.setCancelable(false);
                                                            new CountDownTimer(1300, 800) {
                                                                public void onTick(long millisUntilFinished) {
                                                                    i++;
                                                                    switch (i) {
                                                                        case 0:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormFingerscanActivity.this, R.color.colorGradien));
                                                                            break;
                                                                        case 1:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormFingerscanActivity.this, R.color.colorGradien2));
                                                                            break;
                                                                        case 2:
                                                                        case 6:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormFingerscanActivity.this, R.color.colorGradien3));
                                                                            break;
                                                                        case 3:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormFingerscanActivity.this, R.color.colorGradien4));
                                                                            break;
                                                                        case 4:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormFingerscanActivity.this, R.color.colorGradien5));
                                                                            break;
                                                                        case 5:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (FormFingerscanActivity.this, R.color.colorGradien6));
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
                        else if(kategoriKeterangan.equals("6")){
                            if(statusAbsensiPilihK6.getText().toString().equals("")){
                                if(shiftAbsensiPilihK6.getText().toString().equals("")){
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi status absensi, shift dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi status absensi dan shift!")
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
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi status absensi dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi status absensi!")
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
                                if(shiftAbsensiPilihK6.getText().toString().equals("")){
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi shift dan alasan!")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Harap isi shift!")
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
                                    if(alasanED.getText().toString().equals("")){
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
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
                                    } else {
                                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                        pDialog = new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                        pDialog.show();
                                                        pDialog.setCancelable(false);
                                                        new CountDownTimer(1300, 800) {
                                                            public void onTick(long millisUntilFinished) {
                                                                i++;
                                                                switch (i) {
                                                                    case 0:
                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                (FormFingerscanActivity.this, R.color.colorGradien));
                                                                        break;
                                                                    case 1:
                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                (FormFingerscanActivity.this, R.color.colorGradien2));
                                                                        break;
                                                                    case 2:
                                                                    case 6:
                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                (FormFingerscanActivity.this, R.color.colorGradien3));
                                                                        break;
                                                                    case 3:
                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                (FormFingerscanActivity.this, R.color.colorGradien4));
                                                                        break;
                                                                    case 4:
                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                (FormFingerscanActivity.this, R.color.colorGradien5));
                                                                        break;
                                                                    case 5:
                                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                (FormFingerscanActivity.this, R.color.colorGradien6));
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
                        else {
                            if(alasanED.getText().toString().equals("")){
                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
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
                            } else {
                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                pDialog = new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormFingerscanActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormFingerscanActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormFingerscanActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormFingerscanActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormFingerscanActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormFingerscanActivity.this, R.color.colorGradien6));
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
        });

        getDataKaryawan();
        getKeterangan();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_TITIK_ABSENSI, "");

    }

    private void getDataKaryawan(){
        namaTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        nikTV.setText(sharedPrefManager.getSpNik());
        getDataKaryawanDetail();
    }

    private void getKeterangan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_keterangan_form_finger";
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
                                String keterangan_1 = data.getString("keterangan_1");
                                String keterangan_6 = data.getString("keterangan_6");
                                String keterangan_7 = data.getString("keterangan_7");
                                if(keterangan_1.equals("1")){
                                    keterangan1.setVisibility(View.VISIBLE);
                                } else {
                                    keterangan1.setVisibility(View.GONE);
                                }

                                if(keterangan_6.equals("1")){
                                    keterangan6.setVisibility(View.VISIBLE);
                                } else {
                                    keterangan6.setVisibility(View.GONE);
                                }

                                if(keterangan_7.equals("1")){
                                    keterangan7.setVisibility(View.VISIBLE);
                                } else {
                                    keterangan7.setVisibility(View.GONE);
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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

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
                                detailTV.setText(jabatan+" | "+bagian+" | "+department);
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

    private void getShift() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_shift_form_finger";
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
                                String id = data.getString("id");
                                String shift = data.getString("shift");
                                shiftTVK3.setText(shift);
                            } else {
                                datePilihTV.setText("Pilih Kembali !");
                                dateChoiceMasuk = "";
                                shiftTVK3.setText("Tentukan Tanggal Masuk...");
                                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Anda tidak melakukan absen masuk pada tanggal yang dipilih")
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
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("tanggal_masuk", dateChoiceMasuk);
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
                                submitPermohonan();
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
                                                Intent intent = new Intent(FormFingerscanActivity.this, DigitalSignatureActivity.class);
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

    private void submitPermohonan(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/proses_finger";
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

                            if(status.equals("Success")) {
                                String id_permohonan = data.getString("id_permohonan");
                                idPermohonan = id_permohonan;
                                permohonanTerkirim = "1";
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);
                                pDialog.dismiss();
                            } else if(status.equals("Available")) {
                                if(kategoriKeterangan.equals("1")){
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setContentText("Anda telah melakukan absen masuk pada tanggal masuk yang dipilih")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                                } else if(kategoriKeterangan.equals("2")){
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setContentText("Anda telah melakukan absen masuk dan tidak terlambat pada tanggal yang dipilih")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                                } else if(kategoriKeterangan.equals("3")){
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setContentText("Anda telah melakukan absen pulang pada tanggal yang dipilih")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                                } else if(kategoriKeterangan.equals("4")){
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setContentText("Jam absen masuk anda tidak terlambat")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                                } else if(kategoriKeterangan.equals("5")){
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setContentText("Anda belum melakukan absen pulang pada tanggal yang dipilih")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                                } else if(kategoriKeterangan.equals("7")){
                                    pDialog.setTitleText("Gagal Terkirim")
                                            .setContentText("Anda sudah melakukan absen masuk pada tanggal yang dipilih")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                                }
                            } else if(status.equals("Not Available")) {
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Anda belum melakukan absen masuk pada tanggal masuk yang dipilih")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Limit Out")) {
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Bulan ini anda telah mengajukan 3 permohonan form keterangan tidak absen/batas maksimum pengajuan permohonan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Over Day")) {
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Permohonan form keterangan tidak absen tidak dapat diajukan lebih dari 2 hari dari tanggal yang dipilih")
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("tanggal", dateChoiceMasuk);
                params.put("keterangan", kategoriKeterangan);

                if(kategoriKeterangan.equals("1")){
                    params.put("status_absen", statusAbsensi);
                    params.put("shift", shiftAbsensi);
                    params.put("jam_masuk", jamMasuk);
                    params.put("tgl_pulang", tanggalPulang);
                    params.put("jam_pulang", jamPulang);
                    params.put("titik_absen", namaTitikAbsensi);
                } else if(kategoriKeterangan.equals("3")){
                    params.put("tgl_pulang", tanggalPulang);
                    params.put("jam_pulang", jamPulang);
                    params.put("titik_absen", namaTitikAbsensi);
                } else if(kategoriKeterangan.equals("6")){
                    params.put("status_absen", statusAbsensi);
                    params.put("shift", shiftAbsensi);
                }

                params.put("alasan", alasanED.getText().toString());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    @SuppressLint("SimpleDateFormat")
    private void datePicker(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormFingerscanActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoiceMasuk = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(dateChoiceMasuk));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    if (!dateChoicePulang.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(dateChoiceMasuk));
                            date2_2 = sdf.parse(String.valueOf(dateChoicePulang));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long mulai_2 = date_2.getTime();
                        long akhir_2 = date2_2.getTime();

                        if (mulai_2<=akhir_2){
                            String input_date = dateChoiceMasuk;
                            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                            Date dt1= null;
                            try {
                                dt1 = format1.parse(input_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat format2 = new SimpleDateFormat("EEE");
                            DateFormat getweek = new SimpleDateFormat("W");
                            String finalDay = format2.format(dt1);
                            String week = getweek.format(dt1);
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

                            currentDay = hariName;
                            datePilihTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                            statusAbsensiPilihK1.setText("");
                            shiftAbsensiPilihK1.setText("Tentukan Status Absensi...");
                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");

                            if(!kategoriKeterangan.equals("")&&kategoriKeterangan.equals("3")){
                                getShift();
                            }
                        } else {
                            datePilihTV.setText("Pilih Kembali !");
                            dateChoiceMasuk = "";

                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal masuk tidak bisa lebih besar dari tanggal pulang. Harap ulangi!")
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
                        String input_date = dateChoiceMasuk;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
                        DateFormat getweek = new SimpleDateFormat("W");
                        String finalDay = format2.format(dt1);
                        String week = getweek.format(dt1);
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

                        currentDay = hariName;
                        datePilihTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                        statusAbsensiPilihK1.setText("");
                        shiftAbsensiPilihK1.setText("Tentukan Status Absensi...");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");

                        if(!kategoriKeterangan.equals("")&&kategoriKeterangan.equals("3")){
                            getShift();
                        }
                    }
                } else {
                    datePilihTV.setText("Pilih Kembali !");
                    dateChoiceMasuk = "";

                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Pengajuan form keterangan tidak absen tidak dapat ditujukan untuk tanggal yang akan datang. Harap ulangi!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormFingerscanActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoiceMasuk = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(dateChoiceMasuk));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    if (!dateChoicePulang.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(dateChoiceMasuk));
                            date2_2 = sdf.parse(String.valueOf(dateChoicePulang));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long mulai_2 = date_2.getTime();
                        long akhir_2 = date2_2.getTime();

                        if (mulai_2<=akhir_2){
                            String input_date = dateChoiceMasuk;
                            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                            Date dt1= null;
                            try {
                                dt1 = format1.parse(input_date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            DateFormat format2 = new SimpleDateFormat("EEE");
                            DateFormat getweek = new SimpleDateFormat("W");
                            String finalDay = format2.format(dt1);
                            String week = getweek.format(dt1);
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

                            currentDay = hariName;
                            datePilihTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                            statusAbsensiPilihK1.setText("");
                            shiftAbsensiPilihK1.setText("Tentukan Status Absensi...");
                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");

                            if(!kategoriKeterangan.equals("")&&kategoriKeterangan.equals("3")){
                                getShift();
                            }
                        } else {
                            datePilihTV.setText("Pilih Kembali !");
                            dateChoiceMasuk = "";

                            new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal masuk tidak bisa lebih besar dari tanggal pulang. Harap ulangi!")
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
                        String input_date = dateChoiceMasuk;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
                        DateFormat getweek = new SimpleDateFormat("W");
                        String finalDay = format2.format(dt1);
                        String week = getweek.format(dt1);
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

                        currentDay = hariName;
                        datePilihTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                        statusAbsensiPilihK1.setText("");
                        shiftAbsensiPilihK1.setText("Tentukan Status Absensi...");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");

                        if(!kategoriKeterangan.equals("")&&kategoriKeterangan.equals("3")){
                            getShift();
                        }
                    }
                } else {
                    datePilihTV.setText("Pilih Kembali !");
                    dateChoiceMasuk = "";

                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Pengajuan form keterangan tidak absen tidak dapat ditujukan untuk tanggal yang akan datang. Harap ulangi!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, y,m,d);
            dpd.show();
        }


    }

    @SuppressLint("SimpleDateFormat")
    private void datePickerPulang(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormFingerscanActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                dateChoicePulang = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(dateChoiceMasuk));
                    date2 = sdf.parse(String.valueOf(dateChoicePulang));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                    Date datef = null;
                    Date datef2 = null;
                    try {
                        datef = sdfo.parse(String.valueOf(dateChoicePulang));
                        datef2 = sdfo.parse(getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long pilih = datef.getTime();
                    long sekarang = datef2.getTime();

                    if (pilih<=sekarang){
                        String input_date = dateChoicePulang;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
                        DateFormat getweek = new SimpleDateFormat("W");
                        String finalDay = format2.format(dt1);
                        String week = getweek.format(dt1);
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

                        if(kategoriKeterangan.equals("1")){
                            datePulangPilihK1.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                            jamPulangPilihK1.setText("");
                        } else if(kategoriKeterangan.equals("3")){
                            tglPulangPilihK3.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                            jamPulangPilihK3.setText("");
                        }

                        tanggalPulang = dateChoicePulang;
                        jamPulang = "";

                    } else {
                        if(kategoriKeterangan.equals("1")){
                            datePulangPilihK1.setText("Pilih Kembali !");
                            jamPulangPilihK1.setText("");
                        } else if(kategoriKeterangan.equals("3")){
                            tglPulangPilihK3.setText("Pilih Kembali !");
                            jamPulangPilihK3.setText("");
                        }

                        dateChoicePulang = "";
                        tanggalPulang = "0000-00-00";
                        jamPulang = "";

                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal pulang tidak bisa melebihi tanggal hari ini. Harap ulangi!")
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
                    if(kategoriKeterangan.equals("1")){
                        datePulangPilihK1.setText("Pilih Kembali !");
                        jamPulangPilihK1.setText("");
                    } else if(kategoriKeterangan.equals("3")){
                        tglPulangPilihK3.setText("Pilih Kembali !");
                        jamPulangPilihK3.setText("");
                    }

                    dateChoicePulang = "";
                    tanggalPulang = "0000-00-00";
                    jamPulang = "";

                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pulang tidak bisa lebih kecil dari tanggal masuk. Harap ulangi!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormFingerscanActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {
                dateChoicePulang = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(dateChoiceMasuk));
                    date2 = sdf.parse(String.valueOf(dateChoicePulang));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                    Date datef = null;
                    Date datef2 = null;
                    try {
                        datef = sdfo.parse(String.valueOf(dateChoicePulang));
                        datef2 = sdfo.parse(getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long pilih = datef.getTime();
                    long sekarang = datef2.getTime();

                    if (pilih<=sekarang){
                        String input_date = dateChoicePulang;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat format2 = new SimpleDateFormat("EEE");
                        DateFormat getweek = new SimpleDateFormat("W");
                        String finalDay = format2.format(dt1);
                        String week = getweek.format(dt1);
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
                                bulanName = "Not found";
                                break;
                        }

                        if(kategoriKeterangan.equals("1")){
                            datePulangPilihK1.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                            jamPulangPilihK1.setText("");
                        } else if(kategoriKeterangan.equals("3")){
                            tglPulangPilihK3.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                            jamPulangPilihK3.setText("");
                        }

                        tanggalPulang = dateChoicePulang;
                        jamPulang = "";

                    } else {
                        if(kategoriKeterangan.equals("1")){
                            datePulangPilihK1.setText("Pilih Kembali !");
                            jamPulangPilihK1.setText("");
                        } else if(kategoriKeterangan.equals("3")){
                            tglPulangPilihK3.setText("Pilih Kembali !");
                            jamPulangPilihK3.setText("");
                        }

                        dateChoicePulang = "";
                        tanggalPulang = "0000-00-00";
                        jamPulang = "";

                        new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal pulang tidak bisa melebihi tanggal hari ini. Harap ulangi!")
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
                    if(kategoriKeterangan.equals("1")){
                        datePulangPilihK1.setText("Pilih Kembali !");
                        jamPulangPilihK1.setText("");
                    } else if(kategoriKeterangan.equals("3")){
                        tglPulangPilihK3.setText("Pilih Kembali !");
                        jamPulangPilihK3.setText("");
                    }

                    dateChoicePulang = "";
                    tanggalPulang = "0000-00-00";
                    jamPulang = "";

                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pulang tidak bisa lebih kecil dari tanggal masuk. Harap ulangi!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, y,m,d);
            dpd.show();
        }

    }

    private void statusAbsen(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_status_absen, bottomSheet, false));
        statusAbsenRV = findViewById(R.id.status_absen_rv);

        if(statusAbsenRV != null){
            statusAbsenRV.setLayoutManager(new LinearLayoutManager(this));
            statusAbsenRV.setHasFixedSize(true);
            statusAbsenRV.setNestedScrollingEnabled(false);
            statusAbsenRV.setItemAnimator(new DefaultItemAnimator());
        }

        getStatusAbsenBagian();

    }

    private void shiftAbsen(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_shift_absen, bottomSheet, false));
        shifAbsenRV = findViewById(R.id.shift_absen_rv);

        if(shifAbsenRV != null){
            shifAbsenRV.setLayoutManager(new LinearLayoutManager(this));
            shifAbsenRV.setHasFixedSize(true);
            shifAbsenRV.setNestedScrollingEnabled(false);
            shifAbsenRV.setItemAnimator(new DefaultItemAnimator());
        }

        getShiftAbsenBagian();

    }

    private void titikAbsen(){
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_titik_absen, bottomSheet, false));
                titikAbsenRV = findViewById(R.id.titik_absen_rv);

                titikAbsenRV.setLayoutManager(new LinearLayoutManager(this));
                titikAbsenRV.setHasFixedSize(true);
                titikAbsenRV.setNestedScrollingEnabled(false);
                titikAbsenRV.setItemAnimator(new DefaultItemAnimator());

                getTitikAbsensi();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_titik_absen, bottomSheet, false));
                titikAbsenRV = findViewById(R.id.titik_absen_rv);

                titikAbsenRV.setLayoutManager(new LinearLayoutManager(this));
                titikAbsenRV.setHasFixedSize(true);
                titikAbsenRV.setNestedScrollingEnabled(false);
                titikAbsenRV.setItemAnimator(new DefaultItemAnimator());

                getTitikAbsensi();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }

    private void jamPulangPicker(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_time_picker, bottomSheet, false));
        timepicker = findViewById(R.id.timepicker);
        closeBTN = findViewById(R.id.close_btn);
        okBTN = findViewById(R.id.ok_btn);
        timepicker.setIs24HourView(true);
        timepicker.isInEditMode();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int h = Integer.parseInt(getTimeH());
            int m = Integer.parseInt(getTimeM());
            timepicker.setHour(h);
            timepicker.setMinute(m);

            closeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheet.dismissSheet();
                }
            });

            okBTN.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if(String.valueOf(timepicker.getHour()).length()==1){
                        if(String.valueOf(timepicker.getMinute()).length()==1){
                            checkJam("0"+String.valueOf(timepicker.getHour())+":0"+String.valueOf(timepicker.getMinute())+":00");
                        } else {
                            checkJam("0"+String.valueOf(timepicker.getHour())+":"+String.valueOf(timepicker.getMinute())+":00");
                        }
                    } else {
                        if(String.valueOf(timepicker.getMinute()).length()==1){
                            checkJam(String.valueOf(timepicker.getHour())+":0"+String.valueOf(timepicker.getMinute())+":00");
                        } else {
                            checkJam(String.valueOf(timepicker.getHour())+":"+String.valueOf(timepicker.getMinute())+":00");
                        }
                    }
                    bottomSheet.dismissSheet();
                }
            });

        }

    }

    @SuppressLint("SetTextI18n")
    private void checkJam(String jamPulangPilih){
        if(tanggalPulang.equals(getDate())) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date time = null;
            Date time2 = null;
            try {
                time = sdf.parse(String.valueOf(jamPulangPilih));
                time2 = sdf.parse(getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long pilih = time.getTime();
            long sekarang = time2.getTime();

            if (pilih <= sekarang) {
                if(kategoriKeterangan.equals("1")){
                    jamPulangPilihK1.setText(jamPulangPilih);
                } else if(kategoriKeterangan.equals("3")){
                    jamPulangPilihK3.setText(jamPulangPilih);
                }

                jamPulang = jamPulangPilih;
            } else {
                jamPulang = "";

                if(kategoriKeterangan.equals("1")){
                    jamPulangPilihK1.setText("Pilih kembali !");
                } else if(kategoriKeterangan.equals("3")){
                    jamPulangPilihK3.setText("Pilih kembali !");
                }

                new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Jam pulang tidak dapat lebih besar dari jam saat ini. Harap ulangi!")
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
            if(kategoriKeterangan.equals("1")){
                jamPulangPilihK1.setText(jamPulangPilih);
            } else if(kategoriKeterangan.equals("3")){
                jamPulangPilihK3.setText(jamPulangPilih);
            }

            jamPulang = jamPulangPilih;
        }

    }

    private void getStatusAbsenBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/status_absen_bagian_form_finger";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status_absen = data.getString("data");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            statusAbsens = gson.fromJson(status_absen, StatusAbsen[].class);
                            adapterStatusAbsen = new AdapterStatusAbsenFinger(statusAbsens,FormFingerscanActivity.this);
                            statusAbsenRV.setAdapter(adapterStatusAbsen);

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getShiftAbsenBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/shift_absen_bagian_form_finger";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String shift_absen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            shiftAbsens = gson.fromJson(shift_absen, ShiftAbsen[].class);
                            adapterShiftAbsen = new AdapterShiftAbsenFinger(shiftAbsens,FormFingerscanActivity.this);
                            shifAbsenRV.setAdapter(adapterShiftAbsen);

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("tanggal", dateChoiceMasuk);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getTitikAbsensi() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_lokasi_absen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String titik_absen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            titikAbsensis = gson.fromJson(titik_absen, TitikAbsensi[].class);
                            adapterTitikAbsenFinger = new AdapterTitikAbsenFinger(titikAbsensis,FormFingerscanActivity.this);
                            titikAbsenRV.setAdapter(adapterTitikAbsenFinger);

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public BroadcastReceiver statusAbsenBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String idStatusAbsen = intent.getStringExtra("id_status_absen");
            String namaStatusAbsen = intent.getStringExtra("nama_status_absen");
            String descStatusAbsen = intent.getStringExtra("desc_status_absen");

            if(kategoriKeterangan.equals("1")){
                statusAbsensiPilihK1.setText(namaStatusAbsen);
                shiftAbsensiPilihK1.setText("");
            } else if(kategoriKeterangan.equals("6")){
                statusAbsensiPilihK6.setText(namaStatusAbsen);
                shiftAbsensiPilihK6.setText("");
            }

            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");
            statusAbsensi = idStatusAbsen;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    public BroadcastReceiver shiftAbsenBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String idShiftAbsen = intent.getStringExtra("id_shift_absen");
            String namaShiftAbsen = intent.getStringExtra("nama_shift_absen");
            String datangShiftAbsen = intent.getStringExtra("datang_shift_absen");
            String pulangShiftAbsen = intent.getStringExtra("pulang_shift_absen");

            if(kategoriKeterangan.equals("1")){
                shiftAbsensiPilihK1.setText(namaShiftAbsen+" ("+datangShiftAbsen+" - "+pulangShiftAbsen+")");
                jamMasukTVK1.setText(datangShiftAbsen);
            } else if(kategoriKeterangan.equals("6")){
                shiftAbsensiPilihK6.setText(namaShiftAbsen+" ("+datangShiftAbsen+" - "+pulangShiftAbsen+")");
            }

            jamMasuk = datangShiftAbsen;
            shiftAbsensi = idShiftAbsen;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    public BroadcastReceiver titikAbsenBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String latitude = intent.getStringExtra("latitude_titik");
            String longitude = intent.getStringExtra("longitude_titik");
            String namaTitik = intent.getStringExtra("nama_titik");

            if(kategoriKeterangan.equals("1")){
                titikPilihK1.setText(namaTitik);
            } else if(kategoriKeterangan.equals("3")){
                titikPilihK3.setText(namaTitik);
            }

            namaTitikAbsensi = namaTitik;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    private void connectionFailed(){
        CookieBar.build(FormFingerscanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTimeM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        if (!dateChoiceMasuk.equals("") || !pilihanKeterangan.equals("") || !alasanED.getText().toString().equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (permohonanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormFingerscanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    dateChoiceMasuk = "";
                                    pilihanKeterangan = "";
                                    alasanED.setText("");
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