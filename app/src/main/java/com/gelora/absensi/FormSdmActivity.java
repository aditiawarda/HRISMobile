package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterUnitBagian;
import com.gelora.absensi.adapter.AdapterUnitBisnis;
import com.gelora.absensi.adapter.AdapterUnitDepartemen;
import com.gelora.absensi.adapter.AdapterUnitJabatan;
import com.gelora.absensi.kalert.KAlertDialog;
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

    LinearLayout backBTN, pilihKeteranganPart, actionBar, submitBTN, attantionNoForm, loadingFormPart;
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
    private AdapterUnitBisnis adapterUnitBisnis;
    private RecyclerView f1DepartemenRV;
    private UnitDepartemen[] unitDepartemen;
    private AdapterUnitDepartemen adapterUnitDepartemen;
    private RecyclerView f1BagianRV;
    private UnitBagian[] unitBagian;
    private AdapterUnitBagian adapterUnitBagian;
    private RecyclerView f1JabatanRV;
    private UnitJabatan[] unitJabatans;
    private AdapterUnitJabatan adapterUnitJabatan;

    ImageView loadingForm;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    TextView keteranganTV;
    BottomSheetLayout bottomSheet;
    String kodeKeterangan = "0", perngajuanTerkirim = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
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

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingForm);

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

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kodeKeterangan.equals("1")){ //Form 1
                    if(f1UnitBisnisTV.getText().toString().equals("") || f1IdDepartemen.equals("") || f1IdBagian.equals("") || f1IdJabatan.equals("") || f1KomponenGajiTV.equals("") || f1DeskripsiJabatanTV.getText().toString().equals("") || f1SyaratTV.getText().toString().equals("") || f1TglDibutuhkan.equals("") || f1TglPemenuhan.equals("")){
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
                        f1SendData();
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
                            adapterUnitBisnis = new AdapterUnitBisnis(unitBisnis,FormSdmActivity.this);
                            f1UnitBisnisRV.setAdapter(adapterUnitBisnis);

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
        Toast.makeText(FormSdmActivity.this, "gass", Toast.LENGTH_SHORT).show();
    }
    //Form 1 End

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