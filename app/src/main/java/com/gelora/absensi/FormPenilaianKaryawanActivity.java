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
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.gelora.absensi.adapter.AdapterKaryawanPengganti;
import com.gelora.absensi.adapter.AdapterKaryawanPenilaian;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanPengganti;
import com.gelora.absensi.model.KaryawanPenilaian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormPenilaianKaryawanActivity extends AppCompatActivity {

    LinearLayout viewBTN, successPart, formPart, backBTN, actionBar, pilihKaryawanPart, startAttantionPart, loadingDataPart, noDataPart, submitBTN;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    TextView namaKaryawanTV, messageTV;
    EditText keywordKaryawan;
    ImageView loadingGif, successGif;
    private RecyclerView karyawanRV;
    private KaryawanPenilaian[] karyawans;
    private AdapterKaryawanPenilaian adapterKaryawan;
    String nikKaryawan = "", namaKaryawan = "";
    KAlertDialog pDialog;
    private int i = -1;
    RequestQueue requestQueue;

    RadioGroup fP1;
    RadioButton fP1_1, fP1_2, fP1_3, fP1_4, fP1_5;
    TextView fP1_nilai;
    int bobotFP1 = 10;
    int nilaiFP1 = 0;
    int rating1 = 0;

    RadioGroup fP2;
    RadioButton fP2_1, fP2_2, fP2_3, fP2_4, fP2_5;
    TextView fP2_nilai;
    int bobotFP2 = 10;
    int nilaiFP2 = 0;
    int rating2 = 0;

    RadioGroup fP3;
    RadioButton fP3_1, fP3_2, fP3_3, fP3_4, fP3_5;
    TextView fP3_nilai;
    int bobotFP3 = 10;
    int nilaiFP3 = 0;
    int rating3 = 0;

    RadioGroup fP4;
    RadioButton fP4_1, fP4_2, fP4_3, fP4_4, fP4_5;
    TextView fP4_nilai;
    int bobotFP4 = 5;
    int nilaiFP4 = 0;
    int rating4 = 0;

    RadioGroup fP5;
    RadioButton fP5_1, fP5_2, fP5_3, fP5_4, fP5_5;
    TextView fP5_nilai;
    int bobotFP5 = 5;
    int nilaiFP5 = 0;
    int rating5 = 0;

    RadioGroup fP6;
    RadioButton fP6_1, fP6_2, fP6_3, fP6_4, fP6_5;
    TextView fP6_nilai;
    int bobotFP6 = 5;
    int nilaiFP6 = 0;
    int rating6 = 0;

    RadioGroup fP7;
    RadioButton fP7_1, fP7_2, fP7_3, fP7_4, fP7_5;
    TextView fP7_nilai;
    int bobotFP7 = 10;
    int nilaiFP7 = 0;
    int rating7 = 0;

    RadioGroup fP8;
    RadioButton fP8_1, fP8_2, fP8_3, fP8_4, fP8_5;
    TextView fP8_nilai;
    int bobotFP8 = 5;
    int nilaiFP8 = 0;
    int rating8 = 0;

    RadioGroup fP9;
    RadioButton fP9_1, fP9_2, fP9_3, fP9_4, fP9_5;
    TextView fP9_nilai;
    int bobotFP9 = 5;
    int nilaiFP9 = 0;
    int rating9 = 0;

    RadioGroup fP10;
    RadioButton fP10_1, fP10_2, fP10_3, fP10_4, fP10_5;
    TextView fP10_nilai;
    int bobotFP10 = 5;
    int nilaiFP10 = 0;
    int rating10 = 0;

    RadioGroup fP11;
    RadioButton fP11_1, fP11_2, fP11_3, fP11_4, fP11_5;
    TextView fP11_nilai;
    int bobotFP11 = 10;
    int nilaiFP11 = 0;
    int rating11 = 0;

    RadioGroup fP12;
    RadioButton fP12_1, fP12_2, fP12_3, fP12_4, fP12_5;
    TextView fP12_nilai;
    int bobotFP12 = 10;
    int nilaiFP12 = 0;
    int rating12 = 0;

    RadioGroup fP13;
    RadioButton fP13_1, fP13_2, fP13_3, fP13_4, fP13_5;
    TextView fP13_nilai;
    int bobotFP13 = 5;
    int nilaiFP13 = 0;
    int rating13 = 0;

    RadioGroup fP14;
    RadioButton fP14_1, fP14_2, fP14_3, fP14_4, fP14_5;
    TextView fP14_nilai;
    int bobotFP14 = 5;
    int nilaiFP14 = 0;
    int rating14 = 0;

    TextView fp_total_nilai;
    TextView fp_predikat;
    int totalNilai = 0;
    String listRating = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_penilaian_karyawan);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        pilihKaryawanPart = findViewById(R.id.pilih_karyawan_part);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        submitBTN = findViewById(R.id.submit_btn);
        successGif = findViewById(R.id.success_gif);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        messageTV = findViewById(R.id.message_tv);
        viewBTN = findViewById(R.id.view_btn);

        fP1 = findViewById(R.id.rgf_1);
        fP1_1 = findViewById(R.id.rgf_1_rating_1);
        fP1_2 = findViewById(R.id.rgf_1_rating_2);
        fP1_3 = findViewById(R.id.rgf_1_rating_3);
        fP1_4 = findViewById(R.id.rgf_1_rating_4);
        fP1_5 = findViewById(R.id.rgf_1_rating_5);
        fP1_nilai = findViewById(R.id.rgf_1_nilai);

        fP2 = findViewById(R.id.rgf_2);
        fP2_1 = findViewById(R.id.rgf_2_rating_1);
        fP2_2 = findViewById(R.id.rgf_2_rating_2);
        fP2_3 = findViewById(R.id.rgf_2_rating_3);
        fP2_4 = findViewById(R.id.rgf_2_rating_4);
        fP2_5 = findViewById(R.id.rgf_2_rating_5);
        fP2_nilai = findViewById(R.id.rgf_2_nilai);

        fP3 = findViewById(R.id.rgf_3);
        fP3_1 = findViewById(R.id.rgf_3_rating_1);
        fP3_2 = findViewById(R.id.rgf_3_rating_2);
        fP3_3 = findViewById(R.id.rgf_3_rating_3);
        fP3_4 = findViewById(R.id.rgf_3_rating_4);
        fP3_5 = findViewById(R.id.rgf_3_rating_5);
        fP3_nilai = findViewById(R.id.rgf_3_nilai);

        fP4 = findViewById(R.id.rgf_4);
        fP4_1 = findViewById(R.id.rgf_4_rating_1);
        fP4_2 = findViewById(R.id.rgf_4_rating_2);
        fP4_3 = findViewById(R.id.rgf_4_rating_3);
        fP4_4 = findViewById(R.id.rgf_4_rating_4);
        fP4_5 = findViewById(R.id.rgf_4_rating_5);
        fP4_nilai = findViewById(R.id.rgf_4_nilai);

        fP5 = findViewById(R.id.rgf_5);
        fP5_1 = findViewById(R.id.rgf_5_rating_1);
        fP5_2 = findViewById(R.id.rgf_5_rating_2);
        fP5_3 = findViewById(R.id.rgf_5_rating_3);
        fP5_4 = findViewById(R.id.rgf_5_rating_4);
        fP5_5 = findViewById(R.id.rgf_5_rating_5);
        fP5_nilai = findViewById(R.id.rgf_5_nilai);

        fP6 = findViewById(R.id.rgf_6);
        fP6_1 = findViewById(R.id.rgf_6_rating_1);
        fP6_2 = findViewById(R.id.rgf_6_rating_2);
        fP6_3 = findViewById(R.id.rgf_6_rating_3);
        fP6_4 = findViewById(R.id.rgf_6_rating_4);
        fP6_5 = findViewById(R.id.rgf_6_rating_5);
        fP6_nilai = findViewById(R.id.rgf_6_nilai);

        fP7 = findViewById(R.id.rgf_7);
        fP7_1 = findViewById(R.id.rgf_7_rating_1);
        fP7_2 = findViewById(R.id.rgf_7_rating_2);
        fP7_3 = findViewById(R.id.rgf_7_rating_3);
        fP7_4 = findViewById(R.id.rgf_7_rating_4);
        fP7_5 = findViewById(R.id.rgf_7_rating_5);
        fP7_nilai = findViewById(R.id.rgf_7_nilai);

        fP8 = findViewById(R.id.rgf_8);
        fP8_1 = findViewById(R.id.rgf_8_rating_1);
        fP8_2 = findViewById(R.id.rgf_8_rating_2);
        fP8_3 = findViewById(R.id.rgf_8_rating_3);
        fP8_4 = findViewById(R.id.rgf_8_rating_4);
        fP8_5 = findViewById(R.id.rgf_8_rating_5);
        fP8_nilai = findViewById(R.id.rgf_8_nilai);

        fP9 = findViewById(R.id.rgf_9);
        fP9_1 = findViewById(R.id.rgf_9_rating_1);
        fP9_2 = findViewById(R.id.rgf_9_rating_2);
        fP9_3 = findViewById(R.id.rgf_9_rating_3);
        fP9_4 = findViewById(R.id.rgf_9_rating_4);
        fP9_5 = findViewById(R.id.rgf_9_rating_5);
        fP9_nilai = findViewById(R.id.rgf_9_nilai);

        fP10 = findViewById(R.id.rgf_10);
        fP10_1 = findViewById(R.id.rgf_10_rating_1);
        fP10_2 = findViewById(R.id.rgf_10_rating_2);
        fP10_3 = findViewById(R.id.rgf_10_rating_3);
        fP10_4 = findViewById(R.id.rgf_10_rating_4);
        fP10_5 = findViewById(R.id.rgf_10_rating_5);
        fP10_nilai = findViewById(R.id.rgf_10_nilai);

        fP11 = findViewById(R.id.rgf_11);
        fP11_1 = findViewById(R.id.rgf_11_rating_1);
        fP11_2 = findViewById(R.id.rgf_11_rating_2);
        fP11_3 = findViewById(R.id.rgf_11_rating_3);
        fP11_4 = findViewById(R.id.rgf_11_rating_4);
        fP11_5 = findViewById(R.id.rgf_11_rating_5);
        fP11_nilai = findViewById(R.id.rgf_11_nilai);

        fP12 = findViewById(R.id.rgf_12);
        fP12_1 = findViewById(R.id.rgf_12_rating_1);
        fP12_2 = findViewById(R.id.rgf_12_rating_2);
        fP12_3 = findViewById(R.id.rgf_12_rating_3);
        fP12_4 = findViewById(R.id.rgf_12_rating_4);
        fP12_5 = findViewById(R.id.rgf_12_rating_5);
        fP12_nilai = findViewById(R.id.rgf_12_nilai);

        fP13 = findViewById(R.id.rgf_13);
        fP13_1 = findViewById(R.id.rgf_13_rating_1);
        fP13_2 = findViewById(R.id.rgf_13_rating_2);
        fP13_3 = findViewById(R.id.rgf_13_rating_3);
        fP13_4 = findViewById(R.id.rgf_13_rating_4);
        fP13_5 = findViewById(R.id.rgf_13_rating_5);
        fP13_nilai = findViewById(R.id.rgf_13_nilai);

        fP14 = findViewById(R.id.rgf_14);
        fP14_1 = findViewById(R.id.rgf_14_rating_1);
        fP14_2 = findViewById(R.id.rgf_14_rating_2);
        fP14_3 = findViewById(R.id.rgf_14_rating_3);
        fP14_4 = findViewById(R.id.rgf_14_rating_4);
        fP14_5 = findViewById(R.id.rgf_14_rating_5);
        fP14_nilai = findViewById(R.id.rgf_14_nilai);

        fp_total_nilai = findViewById(R.id.fp_total_nilai);
        fp_predikat = findViewById(R.id.fp_predikat);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        LocalBroadcastManager.getInstance(this).registerReceiver(karyawanBroad, new IntentFilter("karyawan_broad"));

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                listRating = "";
                nikKaryawan = "";
                namaKaryawan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PENILAIAN, "");
                namaKaryawanTV.setText("");
                fP1.clearCheck();
                fP2.clearCheck();
                fP3.clearCheck();
                fP4.clearCheck();
                fP5.clearCheck();
                fP6.clearCheck();
                fP6.clearCheck();
                fP7.clearCheck();
                fP8.clearCheck();
                fP9.clearCheck();
                fP10.clearCheck();
                fP11.clearCheck();
                fP12.clearCheck();
                fP13.clearCheck();
                fP14.clearCheck();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pilihKaryawanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihKaryawan();
            }
        });

        fP1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP1_1.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 1));
                    nilaiFP1 = bobotFP1 * 1;
                    rating1 = 1;
                } else if (fP1_2.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 2));
                    nilaiFP1 = bobotFP1 * 2;
                    rating1 = 2;
                } else if (fP1_3.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 3));
                    nilaiFP1 = bobotFP1 * 3;
                    rating1 = 3;
                } else if (fP1_4.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 4));
                    nilaiFP1 = bobotFP1 * 4;
                    rating1 = 4;
                } else if (fP1_5.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 5));
                    nilaiFP1 = bobotFP1 * 5;
                    rating1 = 5;
                }
                totalNilai();
            }
        });

        fP2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP2_1.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 1));
                    nilaiFP2 = bobotFP2 * 1;
                    rating2 = 1;
                } else if (fP2_2.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 2));
                    nilaiFP2 = bobotFP2 * 2;
                    rating2 = 2;
                } else if (fP2_3.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 3));
                    nilaiFP2 = bobotFP2 * 3;
                    rating2 = 3;
                } else if (fP2_4.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 4));
                    nilaiFP2 = bobotFP2 * 4;
                    rating2 = 4;
                } else if (fP2_5.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 5));
                    nilaiFP2 = bobotFP2 * 5;
                    rating2 = 5;
                }
                totalNilai();
            }
        });

        fP3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP3_1.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 1));
                    nilaiFP3 = bobotFP3 * 1;
                    rating3 = 1;
                } else if (fP3_2.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 2));
                    nilaiFP3 = bobotFP3 * 2;
                    rating3 = 2;
                } else if (fP3_3.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 3));
                    nilaiFP3 = bobotFP3 * 3;
                    rating3 = 3;
                } else if (fP3_4.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 4));
                    nilaiFP3 = bobotFP3 * 4;
                    rating3 = 4;
                } else if (fP3_5.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 5));
                    nilaiFP3 = bobotFP3 * 5;
                    rating3 = 5;
                }
                totalNilai();
            }
        });

        fP4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP4_1.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 1));
                    nilaiFP4 = bobotFP4 * 1;
                    rating4 = 1;
                } else if (fP4_2.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 2));
                    nilaiFP4 = bobotFP4 * 2;
                    rating4 = 2;
                } else if (fP4_3.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 3));
                    nilaiFP4 = bobotFP4 * 3;
                    rating4 = 3;
                } else if (fP4_4.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 4));
                    nilaiFP4 = bobotFP4 * 4;
                    rating4 = 4;
                } else if (fP4_5.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 5));
                    nilaiFP4 = bobotFP4 * 5;
                    rating4 = 5;
                }
                totalNilai();
            }
        });

        fP5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP5_1.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 1));
                    nilaiFP5 = bobotFP5 * 1;
                    rating5 = 1;
                } else if (fP5_2.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 2));
                    nilaiFP5 = bobotFP5 * 2;
                    rating5 = 2;
                } else if (fP5_3.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 3));
                    nilaiFP5 = bobotFP5 * 3;
                    rating5 = 3;
                } else if (fP5_4.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 4));
                    nilaiFP5 = bobotFP5 * 4;
                    rating5 = 4;
                } else if (fP5_5.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 5));
                    nilaiFP5 = bobotFP5 * 5;
                    rating5 = 5;
                }
                totalNilai();
            }
        });

        fP6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP6_1.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 1));
                    nilaiFP6 = bobotFP6 * 1;
                    rating6 = 1;
                } else if (fP6_2.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 2));
                    nilaiFP6 = bobotFP6 * 2;
                    rating6 = 2;
                } else if (fP6_3.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 3));
                    nilaiFP6 = bobotFP6 * 3;
                    rating6 = 3;
                } else if (fP6_4.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 4));
                    nilaiFP6 = bobotFP6 * 4;
                    rating6 = 4;
                } else if (fP6_5.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 5));
                    nilaiFP6 = bobotFP6 * 5;
                    rating6 = 5;
                }
                totalNilai();
            }
        });

        fP7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP7_1.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 1));
                    nilaiFP7 = bobotFP7 * 1;
                    rating7 = 1;
                } else if (fP7_2.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 2));
                    nilaiFP7 = bobotFP7 * 2;
                    rating7 = 2;
                } else if (fP7_3.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 3));
                    nilaiFP7 = bobotFP7 * 3;
                    rating7 = 3;
                } else if (fP7_4.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 4));
                    nilaiFP7 = bobotFP7 * 4;
                    rating7 = 4;
                } else if (fP7_5.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 5));
                    nilaiFP7 = bobotFP7 * 5;
                    rating7 = 5;
                }
                totalNilai();
            }
        });

        fP8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP8_1.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 1));
                    nilaiFP8 = bobotFP8 * 1;
                    rating8 = 1;
                } else if (fP8_2.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 2));
                    nilaiFP8 = bobotFP8 * 2;
                    rating8 = 2;
                } else if (fP8_3.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 3));
                    nilaiFP8 = bobotFP8 * 3;
                    rating8 = 3;
                } else if (fP8_4.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 4));
                    nilaiFP8 = bobotFP8 * 4;
                    rating8 = 4;
                } else if (fP8_5.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 5));
                    nilaiFP8 = bobotFP8 * 5;
                    rating8 = 5;
                }
                totalNilai();
            }
        });

        fP9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP9_1.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 1));
                    nilaiFP9 = bobotFP9 * 1;
                    rating9 = 1;
                } else if (fP9_2.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 2));
                    nilaiFP9 = bobotFP9 * 2;
                    rating9 = 2;
                } else if (fP9_3.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 3));
                    nilaiFP9 = bobotFP9 * 3;
                    rating9 = 3;
                } else if (fP9_4.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 4));
                    nilaiFP9 = bobotFP9 * 4;
                    rating9 = 4;
                } else if (fP9_5.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 5));
                    nilaiFP9 = bobotFP9 * 5;
                    rating9 = 5;
                }
                totalNilai();
            }
        });

        fP10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP10_1.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 1));
                    nilaiFP10 = bobotFP10 * 1;
                    rating10 = 1;
                } else if (fP10_2.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 2));
                    nilaiFP10 = bobotFP10 * 2;
                    rating10 = 2;
                } else if (fP10_3.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 3));
                    nilaiFP10 = bobotFP10 * 3;
                    rating10 = 3;
                } else if (fP10_4.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 4));
                    nilaiFP10 = bobotFP10 * 4;
                    rating10 = 4;
                } else if (fP10_5.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 5));
                    nilaiFP10 = bobotFP10 * 5;
                    rating10 = 5;
                }
                totalNilai();
            }
        });

        fP11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP11_1.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 1));
                    nilaiFP11 = bobotFP11 * 1;
                    rating11 = 1;
                } else if (fP11_2.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 2));
                    nilaiFP11 = bobotFP11 * 2;
                    rating11 = 2;
                } else if (fP11_3.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 3));
                    nilaiFP11 = bobotFP11 * 3;
                    rating11 = 3;
                } else if (fP11_4.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 4));
                    nilaiFP11 = bobotFP11 * 4;
                    rating11 = 4;
                } else if (fP11_5.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 5));
                    nilaiFP11 = bobotFP11 * 5;
                    rating11 = 5;
                }
                totalNilai();
            }
        });

        fP12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP12_1.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 1));
                    nilaiFP12 = bobotFP12 * 1;
                    rating12 = 1;
                } else if (fP12_2.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 2));
                    nilaiFP12 = bobotFP12 * 2;
                    rating12 = 2;
                } else if (fP12_3.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 3));
                    nilaiFP12 = bobotFP12 * 3;
                    rating12 = 3;
                } else if (fP12_4.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 4));
                    nilaiFP12 = bobotFP12 * 4;
                    rating12 = 4;
                } else if (fP12_5.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 5));
                    nilaiFP12 = bobotFP12 * 5;
                    rating12 = 5;
                }
                totalNilai();
            }
        });

        fP13.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP13_1.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 1));
                    nilaiFP13 = bobotFP13 * 1;
                    rating13 = 1;
                } else if (fP13_2.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 2));
                    nilaiFP13 = bobotFP13 * 2;
                    rating13 = 2;
                } else if (fP13_3.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 3));
                    nilaiFP13 = bobotFP13 * 3;
                    rating13 = 3;
                } else if (fP13_4.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 4));
                    nilaiFP13 = bobotFP13 * 4;
                    rating13 = 4;
                } else if (fP13_5.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 5));
                    nilaiFP13 = bobotFP13 * 5;
                    rating13 = 5;
                }
                totalNilai();
            }
        });

        fP14.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP14_1.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 1));
                    nilaiFP14 = bobotFP14 * 1;
                    rating14 = 1;
                } else if (fP14_2.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 2));
                    nilaiFP14 = bobotFP14 * 2;
                    rating14 = 2;
                } else if (fP14_3.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 3));
                    nilaiFP14 = bobotFP14 * 3;
                    rating14 = 3;
                } else if (fP14_4.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 4));
                    nilaiFP14 = bobotFP14 * 4;
                    rating14 = 4;
                } else if (fP14_5.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 5));
                    nilaiFP14 = bobotFP14 * 5;
                    rating14 = 5;
                }
                totalNilai();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nikKaryawan.equals("")){
                    if(nilaiFP1 != 0 && nilaiFP2 != 0 && nilaiFP3 != 0 && nilaiFP4 != 0 && nilaiFP5 != 0 && nilaiFP6 != 0 && nilaiFP7 != 0 && nilaiFP8 != 0 && nilaiFP9 != 0 && nilaiFP10 != 0 && nilaiFP12 != 0 && nilaiFP13 != 0 && nilaiFP14 != 0){
                        listRating = "1."+String.valueOf(rating1)+"-"+"3."+String.valueOf(rating2)+"-"+"5."+String.valueOf(rating3)+"-"+"7."+String.valueOf(rating4)+"-"+"9."+String.valueOf(rating5)+"-"+"11."+String.valueOf(rating6)+"-"+"13."+String.valueOf(rating7)+"-"+"15."+String.valueOf(rating8)+"-"+"17."+String.valueOf(rating9)+"-"+"19."+String.valueOf(rating10)+"-"+"21."+String.valueOf(rating11)+"-"+"23."+String.valueOf(rating12)+"-"+"25."+String.valueOf(rating13)+"-"+"27."+String.valueOf(rating14);

                        new KAlertDialog(FormPenilaianKaryawanActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Submit penilaian sekarang?")
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
                                        pDialog = new KAlertDialog(FormPenilaianKaryawanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormPenilaianKaryawanActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormPenilaianKaryawanActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormPenilaianKaryawanActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormPenilaianKaryawanActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormPenilaianKaryawanActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormPenilaianKaryawanActivity.this, R.color.colorGradien6));
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

                    } else {
                        new KAlertDialog(FormPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi semua penilaian!")
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
                    new KAlertDialog(FormPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih SDM/Karyawan terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();}
            }
        });

        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PENILAIAN, "");

    }

    @SuppressLint("SetTextI18n")
    public void totalNilai(){
        totalNilai = nilaiFP1 + nilaiFP2 + nilaiFP3 + nilaiFP4 + nilaiFP5 + nilaiFP6 + nilaiFP7 + nilaiFP8 + nilaiFP9 + nilaiFP10 + nilaiFP11 + nilaiFP12 + nilaiFP13 + nilaiFP14;
        fp_total_nilai.setText(String.valueOf(totalNilai));

        if(totalNilai <= 100){
            fp_predikat.setText("KS");
        } else if(totalNilai > 100 && totalNilai <= 200){
            fp_predikat.setText("K");
        } else if(totalNilai > 200 && totalNilai <= 300){
            fp_predikat.setText("C");
        } else if(totalNilai > 300 && totalNilai <= 400){
            fp_predikat.setText("B");
        } else if(totalNilai > 400 && totalNilai <= 500){
            fp_predikat.setText("BS");
        }
    }

    public BroadcastReceiver karyawanBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            nikKaryawan = intent.getStringExtra("nik_karyawan");
            namaKaryawan = intent.getStringExtra("nama_karyawan");

            namaKaryawanTV.setText(namaKaryawan.toUpperCase());

            InputMethodManager imm = (InputMethodManager) FormPenilaianKaryawanActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = FormPenilaianKaryawanActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(FormPenilaianKaryawanActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    private void pilihKaryawan(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormPenilaianKaryawanActivity.this).inflate(R.layout.layout_karyawan_form_penilaian, bottomSheet, false));
        keywordKaryawan = findViewById(R.id.keyword_user_ed);

        karyawanRV = findViewById(R.id.karyawan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

        karyawanRV.setLayoutManager(new LinearLayoutManager(this));
        karyawanRV.setHasFixedSize(true);
        karyawanRV.setNestedScrollingEnabled(false);
        karyawanRV.setItemAnimator(new DefaultItemAnimator());

        keywordKaryawan.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWordSearch = keywordKaryawan.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                karyawanRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDataKaryawan(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        keywordKaryawan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = keywordKaryawan.getText().toString();
                    getDataKaryawan(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormPenilaianKaryawanActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormPenilaianKaryawanActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormPenilaianKaryawanActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }

    private void getDataKaryawan(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_penilaian";
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
                                karyawanRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                karyawans = gson.fromJson(data_list, KaryawanPenilaian[].class);
                                adapterKaryawan = new AdapterKaryawanPenilaian(karyawans, FormPenilaianKaryawanActivity.this);
                                karyawanRV.setAdapter(adapterKaryawan);
                            } else {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
                                karyawanRV.setVisibility(View.GONE);
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
                        karyawanRV.setVisibility(View.GONE);

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

    private void checkSignature(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                                submitPenilaian();
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
                                                Intent intent = new Intent(FormPenilaianKaryawanActivity.this, DigitalSignatureActivity.class);
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

    private void submitPenilaian(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/penilaian_sdm_input";
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
                                JSONObject data_penilaian = data.getJSONObject("data");
                                String id = data_penilaian.getString("id");
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);
                                messageTV.setText("Penilaian SDM atas nama "+namaKaryawan+" dengan NIK "+nikKaryawan+" telah berhasil.");

                                viewBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(FormPenilaianKaryawanActivity.this, DetailPenilaianKaryawanActivity.class);
                                        intent.putExtra("id_penilaian", String.valueOf(id));
                                        startActivity(intent);
                                    }
                                });

                            } else if (status.equals("Available")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Data penilaian serupa sudah anda submit sebelumnya")
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
                params.put("NIK", nikKaryawan);
                params.put("approver_kabag", sharedPrefManager.getSpNik());
                params.put("status", "1");
                params.put("no_frm", "FRM.HRD.01.04/ rev-1");
                params.put("list_rating", listRating);

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    private void connectionFailed(){
        CookieBar.build(FormPenilaianKaryawanActivity.this)
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