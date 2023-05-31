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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
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
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterTitikAbsenFinger;
import com.gelora.absensi.adapter.AdapterUnitBisnis;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.TitikAbsensi;
import com.gelora.absensi.model.UnitBisnis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormSdmActivity extends AppCompatActivity {

    LinearLayout backBTN, pilihKeteranganPart, actionBar, submitBTN, attantionNoForm, loadingFormPart;
    LinearLayout f1Part, f2Part;
    LinearLayout ket1BTN, ket2BTN, ket3BTN, ket4BTN, ket5BTN, ket6BTN, ket7BTN;
    LinearLayout markKet1, markKet2, markKet3, markKet4, markKet5, markKet6, markKet7;

    //Form 1
    LinearLayout f1UnitBisnisPart;
    TextView f1UnitBisnisTV;
    String f1IdUnitBisnis = "";
    private RecyclerView f1IdUnitBisnisRV;
    private UnitBisnis[] unitBisnis;
    private AdapterUnitBisnis adapterUnitBisnis;

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

        f1UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1UnitBisnisWay();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                startActivity(intent);
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

        //F1
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");

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
                f1IdUnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f1IdUnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f1IdUnitBisnisRV.setHasFixedSize(true);
                f1IdUnitBisnisRV.setNestedScrollingEnabled(false);
                f1IdUnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f1GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f1IdUnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f1IdUnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f1IdUnitBisnisRV.setHasFixedSize(true);
                f1IdUnitBisnisRV.setNestedScrollingEnabled(false);
                f1IdUnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

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
                            f1IdUnitBisnisRV.setAdapter(adapterUnitBisnis);

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