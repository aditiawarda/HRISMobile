package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FormPenilaianKaryawanActivity extends AppCompatActivity {

    LinearLayout backBTN, actionBar;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    RadioGroup fP1;
    RadioButton fP1_1, fP1_2, fP1_3, fP1_4, fP1_5;
    TextView fP1_nilai;
    int bobotFP1 = 10;
    int nilaiFP1 = 0;

    RadioGroup fP2;
    RadioButton fP2_1, fP2_2, fP2_3, fP2_4, fP2_5;
    TextView fP2_nilai;
    int bobotFP2 = 10;
    int nilaiFP2 = 0;

    RadioGroup fP3;
    RadioButton fP3_1, fP3_2, fP3_3, fP3_4, fP3_5;
    TextView fP3_nilai;
    int bobotFP3 = 10;
    int nilaiFP3 = 0;

    RadioGroup fP4;
    RadioButton fP4_1, fP4_2, fP4_3, fP4_4, fP4_5;
    TextView fP4_nilai;
    int bobotFP4 = 5;
    int nilaiFP4 = 0;

    RadioGroup fP5;
    RadioButton fP5_1, fP5_2, fP5_3, fP5_4, fP5_5;
    TextView fP5_nilai;
    int bobotFP5 = 5;
    int nilaiFP5 = 0;

    RadioGroup fP6;
    RadioButton fP6_1, fP6_2, fP6_3, fP6_4, fP6_5;
    TextView fP6_nilai;
    int bobotFP6 = 5;
    int nilaiFP6 = 0;

    RadioGroup fP7;
    RadioButton fP7_1, fP7_2, fP7_3, fP7_4, fP7_5;
    TextView fP7_nilai;
    int bobotFP7 = 10;
    int nilaiFP7 = 0;

    RadioGroup fP8;
    RadioButton fP8_1, fP8_2, fP8_3, fP8_4, fP8_5;
    TextView fP8_nilai;
    int bobotFP8 = 5;
    int nilaiFP8 = 0;

    RadioGroup fP9;
    RadioButton fP9_1, fP9_2, fP9_3, fP9_4, fP9_5;
    TextView fP9_nilai;
    int bobotFP9 = 5;
    int nilaiFP9 = 0;

    RadioGroup fP10;
    RadioButton fP10_1, fP10_2, fP10_3, fP10_4, fP10_5;
    TextView fP10_nilai;
    int bobotFP10 = 5;
    int nilaiFP10 = 0;

    RadioGroup fP11;
    RadioButton fP11_1, fP11_2, fP11_3, fP11_4, fP11_5;
    TextView fP11_nilai;
    int bobotFP11 = 10;
    int nilaiFP11 = 0;

    RadioGroup fP12;
    RadioButton fP12_1, fP12_2, fP12_3, fP12_4, fP12_5;
    TextView fP12_nilai;
    int bobotFP12 = 10;
    int nilaiFP12 = 0;

    RadioGroup fP13;
    RadioButton fP13_1, fP13_2, fP13_3, fP13_4, fP13_5;
    TextView fP13_nilai;
    int bobotFP13 = 5;
    int nilaiFP13 = 0;

    RadioGroup fP14;
    RadioButton fP14_1, fP14_2, fP14_3, fP14_4, fP14_5;
    TextView fP14_nilai;
    int bobotFP14 = 5;
    int nilaiFP14 = 0;

    TextView fp_total_nilai;
    TextView fp_predikat;
    int totalNilai = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_penilaian_karyawan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);

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

        fP1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fP1_1.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 1));
                    nilaiFP1 = bobotFP1 * 1;
                } else if (fP1_2.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 2));
                    nilaiFP1 = bobotFP1 * 2;
                } else if (fP1_3.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 3));
                    nilaiFP1 = bobotFP1 * 3;
                } else if (fP1_4.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 4));
                    nilaiFP1 = bobotFP1 * 4;
                } else if (fP1_5.isChecked()) {
                    fP1_nilai.setText(String.valueOf(bobotFP1 * 5));
                    nilaiFP1 = bobotFP1 * 5;
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
                } else if (fP2_2.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 2));
                    nilaiFP2 = bobotFP2 * 2;
                } else if (fP2_3.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 3));
                    nilaiFP2 = bobotFP2 * 3;
                } else if (fP2_4.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 4));
                    nilaiFP2 = bobotFP2 * 4;
                } else if (fP2_5.isChecked()) {
                    fP2_nilai.setText(String.valueOf(bobotFP2 * 5));
                    nilaiFP2 = bobotFP2 * 5;
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
                } else if (fP3_2.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 2));
                    nilaiFP3 = bobotFP3 * 2;
                } else if (fP3_3.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 3));
                    nilaiFP3 = bobotFP3 * 3;
                } else if (fP3_4.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 4));
                    nilaiFP3 = bobotFP3 * 4;
                } else if (fP3_5.isChecked()) {
                    fP3_nilai.setText(String.valueOf(bobotFP3 * 5));
                    nilaiFP3 = bobotFP3 * 5;
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
                } else if (fP4_2.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 2));
                    nilaiFP4 = bobotFP4 * 2;
                } else if (fP4_3.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 3));
                    nilaiFP4 = bobotFP4 * 3;
                } else if (fP4_4.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 4));
                    nilaiFP4 = bobotFP4 * 4;
                } else if (fP4_5.isChecked()) {
                    fP4_nilai.setText(String.valueOf(bobotFP4 * 5));
                    nilaiFP4 = bobotFP4 * 5;
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
                } else if (fP5_2.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 2));
                    nilaiFP5 = bobotFP5 * 2;
                } else if (fP5_3.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 3));
                    nilaiFP5 = bobotFP5 * 3;
                } else if (fP5_4.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 4));
                    nilaiFP5 = bobotFP5 * 4;
                } else if (fP5_5.isChecked()) {
                    fP5_nilai.setText(String.valueOf(bobotFP5 * 5));
                    nilaiFP5 = bobotFP5 * 5;
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
                } else if (fP6_2.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 2));
                    nilaiFP6 = bobotFP6 * 2;
                } else if (fP6_3.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 3));
                    nilaiFP6 = bobotFP6 * 3;
                } else if (fP6_4.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 4));
                    nilaiFP6 = bobotFP6 * 4;
                } else if (fP6_5.isChecked()) {
                    fP6_nilai.setText(String.valueOf(bobotFP6 * 5));
                    nilaiFP6 = bobotFP6 * 5;
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
                } else if (fP7_2.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 2));
                    nilaiFP7 = bobotFP7 * 2;
                } else if (fP7_3.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 3));
                    nilaiFP7 = bobotFP7 * 3;
                } else if (fP7_4.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 4));
                    nilaiFP7 = bobotFP7 * 4;
                } else if (fP7_5.isChecked()) {
                    fP7_nilai.setText(String.valueOf(bobotFP7 * 5));
                    nilaiFP7 = bobotFP7 * 5;
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
                } else if (fP8_2.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 2));
                    nilaiFP8 = bobotFP8 * 2;
                } else if (fP8_3.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 3));
                    nilaiFP8 = bobotFP8 * 3;
                } else if (fP8_4.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 4));
                    nilaiFP8 = bobotFP8 * 4;
                } else if (fP8_5.isChecked()) {
                    fP8_nilai.setText(String.valueOf(bobotFP8 * 5));
                    nilaiFP8 = bobotFP8 * 5;
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
                } else if (fP9_2.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 2));
                    nilaiFP9 = bobotFP9 * 2;
                } else if (fP9_3.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 3));
                    nilaiFP9 = bobotFP9 * 3;
                } else if (fP9_4.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 4));
                    nilaiFP9 = bobotFP9 * 4;
                } else if (fP9_5.isChecked()) {
                    fP9_nilai.setText(String.valueOf(bobotFP9 * 5));
                    nilaiFP9 = bobotFP9 * 5;
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
                } else if (fP10_2.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 2));
                    nilaiFP10 = bobotFP10 * 2;
                } else if (fP10_3.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 3));
                    nilaiFP10 = bobotFP10 * 3;
                } else if (fP10_4.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 4));
                    nilaiFP10 = bobotFP10 * 4;
                } else if (fP10_5.isChecked()) {
                    fP10_nilai.setText(String.valueOf(bobotFP10 * 5));
                    nilaiFP10 = bobotFP10 * 5;
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
                } else if (fP11_2.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 2));
                    nilaiFP11 = bobotFP11 * 2;
                } else if (fP11_3.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 3));
                    nilaiFP11 = bobotFP11 * 3;
                } else if (fP11_4.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 4));
                    nilaiFP11 = bobotFP11 * 4;
                } else if (fP11_5.isChecked()) {
                    fP11_nilai.setText(String.valueOf(bobotFP11 * 5));
                    nilaiFP11 = bobotFP11 * 5;
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
                } else if (fP12_2.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 2));
                    nilaiFP12 = bobotFP12 * 2;
                } else if (fP12_3.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 3));
                    nilaiFP12 = bobotFP12 * 3;
                } else if (fP12_4.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 4));
                    nilaiFP12 = bobotFP12 * 4;
                } else if (fP12_5.isChecked()) {
                    fP12_nilai.setText(String.valueOf(bobotFP12 * 5));
                    nilaiFP12 = bobotFP12 * 5;
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
                } else if (fP13_2.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 2));
                    nilaiFP13 = bobotFP13 * 2;
                } else if (fP13_3.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 3));
                    nilaiFP13 = bobotFP13 * 3;
                } else if (fP13_4.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 4));
                    nilaiFP13 = bobotFP13 * 4;
                } else if (fP13_5.isChecked()) {
                    fP13_nilai.setText(String.valueOf(bobotFP13 * 5));
                    nilaiFP13 = bobotFP13 * 5;
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
                } else if (fP14_2.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 2));
                    nilaiFP14 = bobotFP14 * 2;
                } else if (fP14_3.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 3));
                    nilaiFP14 = bobotFP14 * 3;
                } else if (fP14_4.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 4));
                    nilaiFP14 = bobotFP14 * 4;
                } else if (fP14_5.isChecked()) {
                    fP14_nilai.setText(String.valueOf(bobotFP14 * 5));
                    nilaiFP14 = bobotFP14 * 5;
                }
                totalNilai();
            }
        });

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

}