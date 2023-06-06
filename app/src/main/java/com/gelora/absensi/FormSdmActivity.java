package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.gelora.absensi.adapter.AdapterKaryawanBaruSDM;
import com.gelora.absensi.adapter.AdapterKaryawanLamaSDM;
import com.gelora.absensi.adapter.AdapterUnitBagian;
import com.gelora.absensi.adapter.AdapterUnitBisnis;
import com.gelora.absensi.adapter.AdapterUnitBisnis2;
import com.gelora.absensi.adapter.AdapterUnitBisnis2Lama;
import com.gelora.absensi.adapter.AdapterUnitDepartemen;
import com.gelora.absensi.adapter.AdapterUnitJabatan;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanSDM;
import com.gelora.absensi.model.UnitBagian;
import com.gelora.absensi.model.UnitBisnis;
import com.gelora.absensi.model.UnitDepartemen;
import com.gelora.absensi.model.UnitJabatan;
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

public class FormSdmActivity extends AppCompatActivity {

    LinearLayout backBTN, pilihKeteranganPart, actionBar, submitBTN, attantionNoForm, loadingFormPart, formPart, successPart;
    LinearLayout f1Part, f2Part;
    LinearLayout ket1BTN, ket2BTN, ket3BTN, ket4BTN, ket5BTN, ket6BTN, ket7BTN;
    LinearLayout markKet1, markKet2, markKet3, markKet4, markKet5, markKet6, markKet7;

    //Form 1
    LinearLayout f1UnitBisnisPart, f1DepartemenPart, f1BagianPart, f1JabatanPart, f1TglDibutuhkanPart, f1TglPemenuhanPart;
    TextView f1UnitBisnisTV, f1DepartemenTV, f1BagianTV, f1JabatanTV, f1TglDibutuhkanTV, f1TglPemenuhanTV;
    EditText f1KomponenGajiTV, f1DeskripsiJabatanTV, f1SyaratTV, f1CatatanTV;
    String f1IdUnitBisnis = "", f1IdDepartemen = "", f1IdBagian = "", f1IdJabatan = "", f1TglDibutuhkan = "", f1TglPemenuhan = "";
    private RecyclerView f1UnitBisnisRV;
    private UnitBisnis[] unitBisnis;
    private AdapterUnitBisnis f1AdapterUnitBisnis;
    private RecyclerView f1DepartemenRV;
    private UnitDepartemen[] unitDepartemen;
    private AdapterUnitDepartemen adapterUnitDepartemen;
    private RecyclerView f1BagianRV;
    private UnitBagian[] unitBagian;
    private AdapterUnitBagian adapterUnitBagian;
    private RecyclerView f1JabatanRV;
    private UnitJabatan[] unitJabatans;
    private AdapterUnitJabatan adapterUnitJabatan;

    //Form 2
    LinearLayout f2NamaKaryawanPart, f2UnitBisnisPart, f2StartAttantionKaryawanBaruPart, f2NoDataKaryawanBaruPart, f2loadingDataKaryawanBaruPart;
    LinearLayout f2NamaKaryawanLamaPart, f2UnitBisnisLamaPart, f2StartAttantionKaryawanLamaPart, f2NoDataKaryawanLamaPart, f2loadingDataKaryawanLamaPart;
    ImageView f2loadingGif, f2loadingLamaGif;
    TextView f2NamaKaryawanTV, f2UnitBisnisTV, f2DepartemenTV, f2BagianTV, f2JabatanTV;
    TextView f2NamaKaryawanLamaTV, f2UnitBisnisLamaTV, f2DepartemenLamaTV, f2BagianLamaTV, f2JabatanLamaTV;
    EditText f2keywordKaryawanBaru, f2KomponenGajiTV;
    EditText f2keywordKaryawanLama, f2KomponenGajiLamaTV, f2CatatanTV;
    String f2NikBaru = "", f2IdUnitBisnis = "", f2DepartemenBaru = "", f2BagianBaru = "", f2JabatanBaru = "";
    String f2NikLama = "", f2IdUnitBisnisLama = "", f2DepartemenLama = "", f2BagianLama = "", f2JabatanLama= "", f2PemenuhanSyarat = "";
    private RecyclerView f2KaryawanBaruRV, f2KaryawanLamaRV, f2UnitBisnisRV, f2UnitBisnisLamaRV;
    private KaryawanSDM[] f2KaryawanSDMS;
    private AdapterKaryawanBaruSDM f2AdapterKaryawanBaruSDM;
    private AdapterKaryawanLamaSDM f2AdapterKaryawanLamaSDM;
    private AdapterUnitBisnis2 f2AdapterUnitBisnis;
    private AdapterUnitBisnis2Lama f2AdapterUnitBisnisLama;
    RadioGroup f2VerifSyaratGroup;
    RadioButton f2OptionYa, f2OptionTidak;

    ImageView loadingForm;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    TextView keteranganTV;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    KAlertDialog pDialog;
    ImageView successGif;
    private int i = -1;
    String kodeKeterangan = "0", perngajuanTerkirim = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        pilihKeteranganPart = findViewById(R.id.pilih_keterangan_part);
        keteranganTV = findViewById(R.id.keterangan_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        f1Part = findViewById(R.id.form_1);
        f2Part = findViewById(R.id.form_2);
        submitBTN = findViewById(R.id.submit_btn);
        attantionNoForm = findViewById(R.id.attantion_no_form);
        loadingFormPart = findViewById(R.id.loading_form_part);
        loadingForm = findViewById(R.id.loading_form);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);

        //Form 1
        f1UnitBisnisPart = findViewById(R.id.f1_unit_bisnis_part);
        f1UnitBisnisTV = findViewById(R.id.f1_unit_bisnis_tv);
        f1DepartemenPart = findViewById(R.id.f1_departemen_part);
        f1DepartemenTV = findViewById(R.id.f1_departemen_tv);
        f1BagianPart = findViewById(R.id.f1_bagian_part);
        f1BagianTV = findViewById(R.id.f1_bagian_tv);
        f1JabatanPart = findViewById(R.id.f1_jabatan_part);
        f1JabatanTV = findViewById(R.id.f1_jabatan_tv);
        f1KomponenGajiTV = findViewById(R.id.f1_komponen_gaji_tv);
        f1DeskripsiJabatanTV = findViewById(R.id.f1_deskripsi_jabatan_tv);
        f1SyaratTV = findViewById(R.id.f1_syarat_tv);
        f1TglDibutuhkanPart = findViewById(R.id.f1_tgl_dibutuhkan_part);
        f1TglDibutuhkanTV = findViewById(R.id.f1_tgl_dibutuhkan_tv);
        f1TglPemenuhanPart = findViewById(R.id.f1_tgl_pemenuhan_part);
        f1TglPemenuhanTV = findViewById(R.id.f1_tgl_pemenuhan_tv);
        f1CatatanTV = findViewById(R.id.f1_catatan_tv);

        //Form 2
        f2NamaKaryawanPart = findViewById(R.id.f2_nama_karyawan_part);
        f2NamaKaryawanTV = findViewById(R.id.f2_nama_karyawan_tv);
        f2UnitBisnisPart = findViewById(R.id.f2_unit_bisnis_part);
        f2UnitBisnisTV = findViewById(R.id.f2_unit_bisnis_tv);
        f2DepartemenTV = findViewById(R.id.f2_departemen_tv);
        f2BagianTV = findViewById(R.id.f2_bagian_tv);
        f2JabatanTV = findViewById(R.id.f2_jabatan_tv);
        f2KomponenGajiTV = findViewById(R.id.f2_komponen_gaji_tv);
        f2NamaKaryawanLamaPart = findViewById(R.id.f2_nama_karyawan_lama_part);
        f2NamaKaryawanLamaTV = findViewById(R.id.f2_nama_karyawan_lama_tv);
        f2UnitBisnisLamaPart = findViewById(R.id.f2_unit_bisnis_lama_part);
        f2UnitBisnisLamaTV = findViewById(R.id.f2_unit_bisnis_lama_tv);
        f2DepartemenLamaTV = findViewById(R.id.f2_departemen_lama_tv);
        f2BagianLamaTV = findViewById(R.id.f2_bagian_lama_tv);
        f2JabatanLamaTV = findViewById(R.id.f2_jabatan_lama_tv);
        f2KomponenGajiLamaTV = findViewById(R.id.f2_komponen_gaji_lama_tv);
        f2VerifSyaratGroup = findViewById(R.id.f2_option);
        f2OptionYa = findViewById(R.id.f2_option_ya);
        f2OptionTidak = findViewById(R.id.f2_option_tidak);
        f2CatatanTV = findViewById(R.id.f2_catatan_tv);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingForm);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                keteranganTV.setText("");
                kodeKeterangan = "0";
                attantionNoForm.setVisibility(View.VISIBLE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        loadingFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        });

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pilihKeteranganPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keteranganChoice();
            }
        });

        //Form 1
        LocalBroadcastManager.getInstance(this).registerReceiver(f1UnitBisnisBoard, new IntentFilter("f1_unit_bisnis_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1DepartemenBoard, new IntentFilter("f1_departemen_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1BagianBoard, new IntentFilter("f1_bagian_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1JabatanBoard, new IntentFilter("f1_jabatan_board"));
        f1UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1UnitBisnisWay();
            }
        });
        f1DepartemenPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1DepartemenWay();
            }
        });
        f1BagianPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f1IdDepartemen.equals("")){
                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih departemen terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    f1BagianWay();
                }
            }
        });
        f1JabatanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1JabatanWay();
            }
        });
        f1TglDibutuhkanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDibutuhkan();
            }
        });
        f1TglPemenuhanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePemenuhan();
            }
        });
        //End Form 1

        //Form 2
        LocalBroadcastManager.getInstance(this).registerReceiver(f2KaryawanBaruBroad, new IntentFilter("f2_karyawan_baru_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f2KaryawanLamaBroad, new IntentFilter("f2_karyawan_lama_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f2UnitBisnisBoard, new IntentFilter("f2_unit_bisnis_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f2UnitBisnisLamaBoard, new IntentFilter("f2_unit_bisnis_lama_board"));
        f2NamaKaryawanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2KaryawanBaruFunc();
            }
        });
        f2UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2UnitBisnisWay();
            }
        });
        f2NamaKaryawanLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2KaryawanLamaFunc();
            }
        });
        f2UnitBisnisLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2UnitBisnisLamaWay();
            }
        });
        f2VerifSyaratGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                if (f2OptionYa.isChecked()) {
                    f2PemenuhanSyarat = "0";
                } else if (f2OptionTidak.isChecked()) {
                    f2PemenuhanSyarat = "1";
                }
            }
        });
        //End Form 2

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kodeKeterangan.equals("1")) { //Form 1
                    if (f1UnitBisnisTV.getText().toString().equals("") || f1IdDepartemen.equals("") || f1IdBagian.equals("") || f1IdJabatan.equals("") || f1KomponenGajiTV.equals("") || f1DeskripsiJabatanTV.getText().toString().equals("") || f1SyaratTV.getText().toString().equals("") || f1TglDibutuhkan.equals("") || f1TglPemenuhan.equals("")) {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Kirim data sekarang?")
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
                                        pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien6));
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
                } else if(kodeKeterangan.equals("2")){ //Form 2
                    if (f2NikBaru.equals("") || f2IdUnitBisnis.equals("") || f2KomponenGajiTV.getText().toString().equals("") || f2NikLama.equals("") || f2IdUnitBisnisLama.equals("") || f2KomponenGajiLamaTV.getText().toString().equals("")) {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Pastikan kolom nama, unit bisnis dan komponen gaji terisi!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Kirim data sekarang?")
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
                                        pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien6));
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
                    Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                    startActivity(intent);
                }
            }
        });

        loadingFormPart.setVisibility(View.VISIBLE);
        attantionNoForm.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingFormPart.setVisibility(View.GONE);
                attantionNoForm.setVisibility(View.VISIBLE);
            }
        }, 2000);

        //Form 1
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");

        //Form 2
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");

    }

    private void keteranganChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_keterangan_form_sdm, bottomSheet, false));
        ket1BTN = findViewById(R.id.ket_1_btn);
        ket2BTN = findViewById(R.id.ket_2_btn);
        ket3BTN = findViewById(R.id.ket_3_btn);
        ket4BTN = findViewById(R.id.ket_4_btn);
        ket5BTN = findViewById(R.id.ket_5_btn);
        ket6BTN = findViewById(R.id.ket_6_btn);
        ket7BTN = findViewById(R.id.ket_7_btn);
        markKet1 = findViewById(R.id.mark_ket_1);
        markKet2 = findViewById(R.id.mark_ket_2);
        markKet3 = findViewById(R.id.mark_ket_3);
        markKet4 = findViewById(R.id.mark_ket_4);
        markKet5 = findViewById(R.id.mark_ket_5);
        markKet6 = findViewById(R.id.mark_ket_6);
        markKet7 = findViewById(R.id.mark_ket_7);

        if (kodeKeterangan.equals("1")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.VISIBLE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("2")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.VISIBLE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("3")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.VISIBLE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("4")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.VISIBLE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("5")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.VISIBLE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("6")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.VISIBLE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("7")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.VISIBLE);
        }

        ket1BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.VISIBLE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "1";
                keteranganTV.setText("Penerimaan");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.VISIBLE);
                                f2Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket2BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.VISIBLE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "2";
                keteranganTV.setText("Pengangkatan");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.VISIBLE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket3BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.VISIBLE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "3";
                keteranganTV.setText("Penugasan Kembali");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket4BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.VISIBLE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "4";
                keteranganTV.setText("Pensiun/PHK");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket5BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.VISIBLE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "5";
                keteranganTV.setText("Promosi/Mutasi");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket6BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.VISIBLE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "6";
                keteranganTV.setText("Penyesuaian Gaji");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket7BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.VISIBLE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                kodeKeterangan = "7";
                keteranganTV.setText("Lain-lain");

                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f1KomponenGajiTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2
                f2NamaKaryawanTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2PemenuhanSyarat = "";
                f2VerifSyaratGroup.clearCheck();
                f2CatatanTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

    }

    //Form 1
    private void f1UnitBisnisWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f1UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f1UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f1UnitBisnisRV.setHasFixedSize(true);
                f1UnitBisnisRV.setNestedScrollingEnabled(false);
                f1UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f1GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f1UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f1UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f1UnitBisnisRV.setHasFixedSize(true);
                f1UnitBisnisRV.setNestedScrollingEnabled(false);
                f1UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f1GetUnitBisnis();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetUnitBisnis() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f1AdapterUnitBisnis = new AdapterUnitBisnis(unitBisnis,FormSdmActivity.this);
                            f1UnitBisnisRV.setAdapter(f1AdapterUnitBisnis);

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
    public BroadcastReceiver f1UnitBisnisBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit = intent.getStringExtra("id_unit");
            String nama_unit = intent.getStringExtra("nama_unit");
            f1IdUnitBisnis = id_unit;
            f1UnitBisnisTV.setText(nama_unit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f1DepartemenWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f1DepartemenRV = findViewById(R.id.unit_departemen_rv);

                f1DepartemenRV.setLayoutManager(new LinearLayoutManager(this));
                f1DepartemenRV.setHasFixedSize(true);
                f1DepartemenRV.setNestedScrollingEnabled(false);
                f1DepartemenRV.setItemAnimator(new DefaultItemAnimator());

                f1GetDepartemen();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f1DepartemenRV = findViewById(R.id.unit_departemen_rv);

                f1DepartemenRV.setLayoutManager(new LinearLayoutManager(this));
                f1DepartemenRV.setHasFixedSize(true);
                f1DepartemenRV.setNestedScrollingEnabled(false);
                f1DepartemenRV.setItemAnimator(new DefaultItemAnimator());

                f1GetDepartemen();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetDepartemen() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_departemen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String departemen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitDepartemen = gson.fromJson(departemen, UnitDepartemen[].class);
                            adapterUnitDepartemen = new AdapterUnitDepartemen(unitDepartemen,FormSdmActivity.this);
                            f1DepartemenRV.setAdapter(adapterUnitDepartemen);

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
    public BroadcastReceiver f1DepartemenBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_departemen = intent.getStringExtra("id_departemen");
            String nama_departemen = intent.getStringExtra("nama_departemen");
            f1IdDepartemen = id_departemen;
            f1DepartemenTV.setText(nama_departemen);
            f1IdBagian = "";
            f1BagianTV.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f1BagianWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f1BagianRV = findViewById(R.id.unit_bagian_rv);

                f1BagianRV.setLayoutManager(new LinearLayoutManager(this));
                f1BagianRV.setHasFixedSize(true);
                f1BagianRV.setNestedScrollingEnabled(false);
                f1BagianRV.setItemAnimator(new DefaultItemAnimator());

                f1GetBagian();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f1BagianRV = findViewById(R.id.unit_bagian_rv);

                f1BagianRV.setLayoutManager(new LinearLayoutManager(this));
                f1BagianRV.setHasFixedSize(true);
                f1BagianRV.setNestedScrollingEnabled(false);
                f1BagianRV.setItemAnimator(new DefaultItemAnimator());

                f1GetBagian();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String bagian = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBagian = gson.fromJson(bagian, UnitBagian[].class);
                            adapterUnitBagian = new AdapterUnitBagian(unitBagian,FormSdmActivity.this);
                            f1BagianRV.setAdapter(adapterUnitBagian);

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
                params.put("id_departemen", f1IdDepartemen);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f1BagianBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_bagian = intent.getStringExtra("id_bagian");
            String nama_bagian = intent.getStringExtra("nama_bagian");
            f1IdBagian = id_bagian;
            f1BagianTV.setText(nama_bagian);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f1JabatanWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f1JabatanRV = findViewById(R.id.unit_jabatan_rv);
                f1JabatanRV.setLayoutManager(new LinearLayoutManager(this));
                f1JabatanRV.setHasFixedSize(true);
                f1JabatanRV.setNestedScrollingEnabled(false);
                f1JabatanRV.setItemAnimator(new DefaultItemAnimator());
                f1GetJabatan();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f1JabatanRV = findViewById(R.id.unit_jabatan_rv);
                f1JabatanRV.setLayoutManager(new LinearLayoutManager(this));
                f1JabatanRV.setHasFixedSize(true);
                f1JabatanRV.setNestedScrollingEnabled(false);
                f1JabatanRV.setItemAnimator(new DefaultItemAnimator());
                f1GetJabatan();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetJabatan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_jabatan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String jabatan = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitJabatans = gson.fromJson(jabatan, UnitJabatan[].class);
                            adapterUnitJabatan = new AdapterUnitJabatan(unitJabatans,FormSdmActivity.this);
                            f1JabatanRV.setAdapter(adapterUnitJabatan);

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
    public BroadcastReceiver f1JabatanBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_jabatan = intent.getStringExtra("id_jabatan");
            String nama_jabatan = intent.getStringExtra("nama_jabatan");
            f1IdJabatan = id_jabatan;
            f1JabatanTV.setText(nama_jabatan);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    @SuppressLint("SimpleDateFormat")
    private void dateDibutuhkan(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglDibutuhkan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglDibutuhkan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglDibutuhkanTV.setText("Pilih Kembali !");
                    f1TglDibutuhkan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal dibutuhkan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglPemenuhan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglDibutuhkan;
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

                            f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglDibutuhkanTV.setText("Pilih Kembali !");
                            f1TglDibutuhkan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal dibutuhkan tidak bisa lebih besar dari tanggal pemenuhan. Harap pilih kembali!")
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
                        String input_date = f1TglDibutuhkan;
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

                        f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglDibutuhkan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglDibutuhkan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglDibutuhkanTV.setText("Pilih Kembali !");
                    f1TglDibutuhkan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal dibutuhkan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglPemenuhan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglDibutuhkan;
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

                            f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglDibutuhkanTV.setText("Pilih Kembali !");
                            f1TglDibutuhkan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal dibutuhkan tidak bisa lebih besar dari tanggal pemenuhan. Harap pilih kembali!")
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
                        String input_date = f1TglDibutuhkan;
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

                        f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, y,m,d);
            dpd.show();
        }


    }
    @SuppressLint("SimpleDateFormat")
    private void datePemenuhan(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglPemenuhan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglPemenuhan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglPemenuhanTV.setText("Pilih Kembali !");
                    f1TglPemenuhan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglDibutuhkan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglPemenuhan;
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

                            f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglPemenuhanTV.setText("Pilih Kembali !");
                            f1TglPemenuhan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal dibutuhkan. Harap pilih kembali!")
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
                        String input_date = f1TglPemenuhan;
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

                        f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglPemenuhan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglPemenuhan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglPemenuhanTV.setText("Pilih Kembali !");
                    f1TglPemenuhan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglDibutuhkan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglPemenuhan;
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

                            f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglPemenuhanTV.setText("Pilih Kembali !");
                            f1TglPemenuhan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal dibutuhkan. Harap pilih kembali!")
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
                        String input_date = f1TglPemenuhan;
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

                        f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, y,m,d);
            dpd.show();
        }


    }
    private void f1SendData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/input_formulir_sdm";
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
                                String id = data.getString("id_data");

                                perngajuanTerkirim = "1";
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);

                            }  else {
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
                params.put("pembuat", sharedPrefManager.getSpNik());

                params.put("keterangan", kodeKeterangan);
                params.put("catatan", f1CatatanTV.getText().toString());

                params.put("unit_bisnis", f1IdUnitBisnis);
                params.put("departemen", f1IdDepartemen);
                params.put("bagian", f1IdBagian);
                params.put("jabatan", f1IdJabatan);
                params.put("komponen_gaji", f1KomponenGajiTV.getText().toString());

                params.put("jabatan_penerimaan", f1JabatanTV.getText().toString());
                params.put("deskripsi_penerimaan", f1DeskripsiJabatanTV.getText().toString());
                params.put("syarat", f1SyaratTV.getText().toString());
                params.put("tgl_dibutuhkan_penerimaan", f1TglDibutuhkan);
                params.put("tgl_pemenuhan_penerimaan", f1TglPemenuhan);

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }
    //Form 1 End

    //Form 2
    private void f2KaryawanBaruFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f2keywordKaryawanBaru = findViewById(R.id.keyword_user_ed);

        f2KaryawanBaruRV = findViewById(R.id.karyawan_rv);
        f2StartAttantionKaryawanBaruPart = findViewById(R.id.attantion_data_part);
        f2NoDataKaryawanBaruPart = findViewById(R.id.no_data_part);
        f2loadingDataKaryawanBaruPart = findViewById(R.id.loading_data_part);
        f2loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f2loadingGif);

        f2KaryawanBaruRV.setLayoutManager(new LinearLayoutManager(this));
        f2KaryawanBaruRV.setHasFixedSize(true);
        f2KaryawanBaruRV.setNestedScrollingEnabled(false);
        f2KaryawanBaruRV.setItemAnimator(new DefaultItemAnimator());

        f2keywordKaryawanBaru.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f2keywordKaryawanBaru.getText().toString();

                f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                f2loadingDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                f2NoDataKaryawanBaruPart.setVisibility(View.GONE);
                f2KaryawanBaruRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f2GetDataKaryawanBaru(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f2keywordKaryawanBaru.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f2keywordKaryawanBaru.getText().toString();
                    f2GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f2GetDataKaryawanBaru(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
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

                                f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f2NoDataKaryawanBaruPart.setVisibility(View.GONE);
                                f2KaryawanBaruRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f2KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f2AdapterKaryawanBaruSDM = new AdapterKaryawanBaruSDM(f2KaryawanSDMS, FormSdmActivity.this);
                                f2KaryawanBaruRV.setAdapter(f2AdapterKaryawanBaruSDM);
                            } else {
                                f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f2NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                                f2KaryawanBaruRV.setVisibility(View.GONE);
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

                        f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                        f2loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                        f2NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                        f2KaryawanBaruRV.setVisibility(View.GONE);

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
    public BroadcastReceiver f2KaryawanBaruBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_baru = intent.getStringExtra("nik_karyawan_baru");
            String nama_karyawan_baru = intent.getStringExtra("nama_karyawan_baru");
            String departemen_karyawan_baru = intent.getStringExtra("departemen_karyawan_baru");
            String id_departemen_karyawan_baru = intent.getStringExtra("id_departemen_karyawan_baru");
            String bagian_karyawan_baru = intent.getStringExtra("bagian_karyawan_baru");
            String id_bagian_karyawan_baru = intent.getStringExtra("id_bagian_karyawan_baru");
            String jabatan_karyawan_baru = intent.getStringExtra("jabatan_karyawan_baru");
            String id_jabatan_karyawan_baru = intent.getStringExtra("id_jabatan_karyawan_baru");

            f2NikBaru = nik_karyawan_baru;
            f2NamaKaryawanTV.setText(nama_karyawan_baru);
            f2DepartemenBaru = departemen_karyawan_baru;
            f2DepartemenTV.setText(departemen_karyawan_baru);
            f2BagianBaru = id_bagian_karyawan_baru;
            f2BagianTV.setText(bagian_karyawan_baru);
            f2JabatanBaru = id_jabatan_karyawan_baru;
            f2JabatanTV.setText(jabatan_karyawan_baru);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f2UnitBisnisWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisRV.setHasFixedSize(true);
                f2UnitBisnisRV.setNestedScrollingEnabled(false);
                f2UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisRV.setHasFixedSize(true);
                f2UnitBisnisRV.setNestedScrollingEnabled(false);
                f2UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnis();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f2GetUnitBisnis() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f2AdapterUnitBisnis = new AdapterUnitBisnis2(unitBisnis,FormSdmActivity.this);
                            f2UnitBisnisRV.setAdapter(f2AdapterUnitBisnis);

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
    public BroadcastReceiver f2UnitBisnisBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit = intent.getStringExtra("id_unit");
            String nama_unit = intent.getStringExtra("nama_unit");
            f2IdUnitBisnis = id_unit;
            f2UnitBisnisTV.setText(nama_unit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f2KaryawanLamaFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f2keywordKaryawanLama = findViewById(R.id.keyword_user_ed);

        f2KaryawanLamaRV = findViewById(R.id.karyawan_rv);
        f2StartAttantionKaryawanLamaPart = findViewById(R.id.attantion_data_part);
        f2NoDataKaryawanLamaPart = findViewById(R.id.no_data_part);
        f2loadingDataKaryawanLamaPart = findViewById(R.id.loading_data_part);
        f2loadingLamaGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f2loadingLamaGif);

        f2KaryawanLamaRV.setLayoutManager(new LinearLayoutManager(this));
        f2KaryawanLamaRV.setHasFixedSize(true);
        f2KaryawanLamaRV.setNestedScrollingEnabled(false);
        f2KaryawanLamaRV.setItemAnimator(new DefaultItemAnimator());

        f2keywordKaryawanLama.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f2keywordKaryawanLama.getText().toString();

                f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                f2loadingDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                f2NoDataKaryawanLamaPart.setVisibility(View.GONE);
                f2KaryawanLamaRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f2GetDataKaryawanLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f2keywordKaryawanLama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f2keywordKaryawanLama.getText().toString();
                    f2GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f2GetDataKaryawanLama(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
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

                                f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f2NoDataKaryawanLamaPart.setVisibility(View.GONE);
                                f2KaryawanLamaRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f2KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f2AdapterKaryawanLamaSDM = new AdapterKaryawanLamaSDM(f2KaryawanSDMS, FormSdmActivity.this);
                                f2KaryawanLamaRV.setAdapter(f2AdapterKaryawanLamaSDM);
                            } else {
                                f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f2NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                                f2KaryawanLamaRV.setVisibility(View.GONE);
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

                        f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                        f2loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                        f2NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                        f2KaryawanLamaRV.setVisibility(View.GONE);

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
    public BroadcastReceiver f2KaryawanLamaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_lama = intent.getStringExtra("nik_karyawan_lama");
            String nama_karyawan_lama = intent.getStringExtra("nama_karyawan_lama");
            String departemen_karyawan_lama = intent.getStringExtra("departemen_karyawan_lama");
            String id_departemen_karyawan_lama = intent.getStringExtra("id_departemen_karyawan_lama");
            String bagian_karyawan_lama = intent.getStringExtra("bagian_karyawan_lama");
            String id_bagian_karyawan_lama = intent.getStringExtra("id_bagian_karyawan_lama");
            String jabatan_karyawan_lama = intent.getStringExtra("jabatan_karyawan_lama");
            String id_jabatan_karyawan_lama = intent.getStringExtra("id_jabatan_karyawan_lama");

            f2NikLama = nik_karyawan_lama;
            f2NamaKaryawanLamaTV.setText(nama_karyawan_lama);
            f2DepartemenLama = departemen_karyawan_lama;
            f2DepartemenLamaTV.setText(departemen_karyawan_lama);
            f2BagianLama = id_bagian_karyawan_lama;
            f2BagianLamaTV.setText(bagian_karyawan_lama);
            f2JabatanLama = id_jabatan_karyawan_lama;
            f2JabatanLamaTV.setText(jabatan_karyawan_lama);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f2UnitBisnisLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisLamaRV.setHasFixedSize(true);
                f2UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f2UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnisLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisLamaRV.setHasFixedSize(true);
                f2UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f2UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnisLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f2GetUnitBisnisLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f2AdapterUnitBisnisLama = new AdapterUnitBisnis2Lama(unitBisnis,FormSdmActivity.this);
                            f2UnitBisnisLamaRV.setAdapter(f2AdapterUnitBisnisLama);

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
    public BroadcastReceiver f2UnitBisnisLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit_lama = intent.getStringExtra("id_unit_lama");
            String nama_unit_lama = intent.getStringExtra("nama_unit_lama");
            f2IdUnitBisnisLama = id_unit_lama;
            f2UnitBisnisLamaTV.setText(nama_unit_lama);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    private void checkSignature(){
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
                                if(kodeKeterangan.equals("1")){
                                    f1SendData();
                                } else  if(kodeKeterangan.equals("2")){
                                    Toast.makeText(FormSdmActivity.this, "Goww", Toast.LENGTH_SHORT).show();
                                }
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
                                                Intent intent = new Intent(FormSdmActivity.this, DigitalSignatureActivity.class);
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

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormSdmActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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

    @Override
    public void onBackPressed() {
        if (!kodeKeterangan.equals("0")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (perngajuanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    kodeKeterangan = "0";
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