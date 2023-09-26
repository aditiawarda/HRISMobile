package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class ExitClearanceActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    ImageView loadingDataImg, loadingDataImg2;
    LinearLayout backBTN, mainPart, actionBar, addBTN, dataInBTN, dataOutBTN, optionPart, dataMasukPart, dataKeluarPart, noDataPart, noDataPart2, loadingDataPart, loadingDataPart2;
    SwipeRefreshLayout refreshLayout;
    View fakeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        noDataPart = findViewById(R.id.no_data_part);
        noDataPart2 = findViewById(R.id.no_data_part_2);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingDataPart2 = findViewById(R.id.loading_data_part_2);
        loadingDataImg = findViewById(R.id.loading_data_img);
        loadingDataImg2 = findViewById(R.id.loading_data_img_2);
        optionPart = findViewById(R.id.option_part);
        dataInBTN = findViewById(R.id.data_in_btn);
        dataOutBTN = findViewById(R.id.data_out_btn);
        dataMasukPart = findViewById(R.id.data_masuk);
        dataKeluarPart = findViewById(R.id.data_saya);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);
        mainPart = findViewById(R.id.main_part);

        fakeData = findViewById(R.id.fake_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataImg);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataImg2);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
//                dataNotifikasiRV.setVisibility(View.GONE);
//                dataNotifikasi2RV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
//                        getData();
                    }
                }, 500);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExitClearanceActivity.this, FormExitClearanceActivity.class);
                startActivity(intent);
            }
        });

        dataOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBTN.setVisibility(View.VISIBLE);
                dataOutBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify_choice));
                dataInBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify));
                dataMasukPart.setVisibility(View.GONE);
                dataKeluarPart.setVisibility(View.VISIBLE);

                loadingDataPart.setVisibility(View.GONE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
//                dataNotifikasiRV.setVisibility(View.GONE);
//                dataNotifikasi2RV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        // getData();
                    }
                }, 300);

            }
        });

        dataInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBTN.setVisibility(View.GONE);
                dataInBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify_choice));
                dataOutBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify));
                dataMasukPart.setVisibility(View.VISIBLE);
                dataKeluarPart.setVisibility(View.GONE);

                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
//                dataNotifikasiRV.setVisibility(View.GONE);
//                dataNotifikasi2RV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        // getData();
                    }
                }, 300);

            }
        });

        if (sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || (sharedPrefManager.getSpIdJabatan().equals("4") && sharedPrefManager.getSpNik().equals("1309131210"))){
            optionPart.setVisibility(View.VISIBLE);
        } else if (sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")){
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            dataMasukPart.setVisibility(View.VISIBLE);
            dataKeluarPart.setVisibility(View.GONE);
        } else {
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            dataMasukPart.setVisibility(View.GONE);
            dataKeluarPart.setVisibility(View.VISIBLE);
        }

        fakeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExitClearanceActivity.this, DetailExitClearanceActivity.class);
                startActivity(intent);
            }
        });

    }
}