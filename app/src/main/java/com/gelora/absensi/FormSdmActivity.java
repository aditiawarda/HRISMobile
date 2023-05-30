package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;

public class FormSdmActivity extends AppCompatActivity {

    LinearLayout backBTN, pilihKeteranganPart, actionBar, submitBTN;
    LinearLayout f1Part;
    LinearLayout ket1BTN, ket2BTN, ket3BTN, ket4BTN, ket5BTN, ket6BTN, ket7BTN;
    LinearLayout markKet1, markKet2, markKet3, markKet4, markKet5, markKet6, markKet7;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView titleBTN, keteranganTV;
    BottomSheetLayout bottomSheet;
    String kodeKeterangan = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        titleBTN = findViewById(R.id.title_btn);
        pilihKeteranganPart = findViewById(R.id.pilih_keterangan_part);
        keteranganTV = findViewById(R.id.keterangan_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        f1Part = findViewById(R.id.form_1);
        submitBTN = findViewById(R.id.submit_btn);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                keteranganTV.setText("");
                kodeKeterangan = "0";
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
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

        titleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                startActivity(intent);
            }
        });

        pilihKeteranganPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keteranganChoice();
            }
        });

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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
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

                submitBTN.setVisibility(View.VISIBLE);
                f1Part.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

}