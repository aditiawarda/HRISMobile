package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DataFormSdmActivity extends AppCompatActivity {

    LinearLayout addBTN, backBTN, loadingDataPart, noDataPart, countWaitingBTN;
    ImageView loadingData;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView countWaitingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        addBTN = findViewById(R.id.add_btn);

        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingData = findViewById(R.id.loading_data);
        noDataPart = findViewById(R.id.no_data_part);
        countWaitingBTN = findViewById(R.id.count_waiting_btn);
        countWaitingTV = findViewById(R.id.count_waiting_tv);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingData);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //dataPenilaianSDMRV.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        //getData();
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

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataFormSdmActivity.this, FormSdmActivity.class);
                startActivity(intent);
            }
        });

    }
}