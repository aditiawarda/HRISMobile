package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;

public class ListDataReportSumaActivity extends AppCompatActivity {

    LinearLayout noDataPartReport, loadingDataPartReport, rencanaKunjunganBTN, penagihanBTN, penawaranBTN, pesananBTN, markRencanaKunjungan, markPenagihan, markPenawaran, markPesanan, markSemua, semuaBTN, actionBar, backBTN, addBTN, filterCategoryBTN;
    TextView categoryChoiceTV;
    ImageView loadingDataReport;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    BottomSheetLayout bottomSheet;
    String categoryCode = "0";
    RecyclerView reportRV;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_report_suma);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        addBTN = findViewById(R.id.add_btn);
        loadingDataReport = findViewById(R.id.loading_data_report);
        filterCategoryBTN = findViewById(R.id.filter_category_btn);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        categoryChoiceTV = findViewById(R.id.category_choice_tv);
        loadingDataPartReport = findViewById(R.id.loading_data_part_report);
        noDataPartReport = findViewById(R.id.no_data_part_report);
        reportRV = findViewById(R.id.data_report_rv);

        categoryChoiceTV.setText("Semua");

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataReport);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryCode = "0";
                categoryChoiceTV.setText("Semua");

                reportRV.setVisibility(View.GONE);
                loadingDataPartReport.setVisibility(View.VISIBLE);
                noDataPartReport.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData(categoryCode);
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

        filterCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryChoice();
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataReportSumaActivity.this, ReportSumaActivity.class);
                startActivity(intent);
            }
        });

        reportRV.setVisibility(View.GONE);
        loadingDataPartReport.setVisibility(View.VISIBLE);
        noDataPartReport.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(categoryCode);
            }
        }, 1000);

    }

    private void getData(String category_code){
        reportRV.setVisibility(View.GONE);
        loadingDataPartReport.setVisibility(View.GONE);
        noDataPartReport.setVisibility(View.VISIBLE);
    }

    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ListDataReportSumaActivity.this).inflate(R.layout.layout_kategori_report_suma_list_data, bottomSheet, false));
        semuaBTN = findViewById(R.id.semua_btn);
        pesananBTN = findViewById(R.id.pesanan_btn);
        penawaranBTN = findViewById(R.id.penawaran_btn);
        penagihanBTN = findViewById(R.id.penagihan_btn);
        rencanaKunjunganBTN = findViewById(R.id.rencana_kunjunagan_btn);
        markSemua = findViewById(R.id.mark_semua);
        markPesanan = findViewById(R.id.mark_pesanan);
        markPenawaran = findViewById(R.id.mark_penawaran);
        markPenagihan = findViewById(R.id.mark_penagihan);
        markRencanaKunjungan = findViewById(R.id.mark_rencana_kunjungan);

        if (categoryCode.equals("0")) {
            markSemua.setVisibility(View.VISIBLE);
            markPesanan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            markRencanaKunjungan.setVisibility(View.GONE);
            semuaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
            pesananBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(categoryCode.equals("1")) {
            markSemua.setVisibility(View.GONE);
            markPesanan.setVisibility(View.VISIBLE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            markRencanaKunjungan.setVisibility(View.GONE);
            semuaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            pesananBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
            penawaranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(categoryCode.equals("2")) {
            markSemua.setVisibility(View.GONE);
            markPesanan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.VISIBLE);
            markPenagihan.setVisibility(View.GONE);
            markRencanaKunjungan.setVisibility(View.GONE);
            semuaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            pesananBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
            penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(categoryCode.equals("3")) {
            markSemua.setVisibility(View.GONE);
            markPesanan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.VISIBLE);
            markRencanaKunjungan.setVisibility(View.GONE);
            semuaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            pesananBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
            rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        } else if(categoryCode.equals("4")) {
            markSemua.setVisibility(View.GONE);
            markPesanan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            markRencanaKunjungan.setVisibility(View.VISIBLE);
            semuaBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            pesananBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
            rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
        }

        semuaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryCode = "0";
                categoryChoiceTV.setText("Semua");
                markSemua.setVisibility(View.VISIBLE);
                markPesanan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markRencanaKunjungan.setVisibility(View.GONE);
                semuaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                pesananBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(categoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        pesananBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryCode = "1";
                categoryChoiceTV.setText("Pesanan");
                markSemua.setVisibility(View.GONE);
                markPesanan.setVisibility(View.VISIBLE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markRencanaKunjungan.setVisibility(View.GONE);
                semuaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pesananBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(categoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        penawaranBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryCode = "2";
                categoryChoiceTV.setText("Penawaran");
                markSemua.setVisibility(View.GONE);
                markPesanan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.VISIBLE);
                markPenagihan.setVisibility(View.GONE);
                markRencanaKunjungan.setVisibility(View.GONE);
                semuaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pesananBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(categoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        penagihanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryCode = "3";
                categoryChoiceTV.setText("Penagihan");
                markSemua.setVisibility(View.GONE);
                markPesanan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.VISIBLE);
                markRencanaKunjungan.setVisibility(View.GONE);
                semuaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pesananBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(categoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });

        rencanaKunjunganBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryCode = "4";
                categoryChoiceTV.setText("Rencana Kunjungan");
                markSemua.setVisibility(View.GONE);
                markPesanan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                markRencanaKunjungan.setVisibility(View.VISIBLE);
                semuaBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                pesananBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option));
                rencanaKunjunganBTN.setBackground(ContextCompat.getDrawable(ListDataReportSumaActivity.this, R.drawable.shape_option_choice));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        reportRV.setVisibility(View.GONE);
                        loadingDataPartReport.setVisibility(View.VISIBLE);
                        noDataPartReport.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData(categoryCode);
                            }
                        }, 1000);

                    }
                }, 300);

            }
        });


    }

}