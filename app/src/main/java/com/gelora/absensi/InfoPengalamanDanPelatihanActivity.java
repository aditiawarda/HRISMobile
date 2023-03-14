package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class InfoPengalamanDanPelatihanActivity extends AppCompatActivity {

    LinearLayout addBTN, noDataPengalamanPart, noDataPelatihanPart, loadingDataPelatihanPart, backBTN, pengalamanBTN, pelatihanBTN, infoPengalaman, infoPelatihan, loadingDataPengalamanPart;
    ImageView loadingDataPelatihanImg, loadingDataPengalamanImg;
    RecyclerView dataPelatihanRV, dataPengalamanRV;
    SwipeRefreshLayout refreshLayout;
    String posisi = "pengalaman";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pengalaman_dan_pelatihan);

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        pengalamanBTN = findViewById(R.id.pengalaman_btn);
        pelatihanBTN = findViewById(R.id.pelatihan_btn);
        infoPengalaman = findViewById(R.id.info_pengalaman);
        infoPelatihan = findViewById(R.id.info_pelatihan);
        loadingDataPengalamanPart = findViewById(R.id.loading_data_pengalaman_part);
        loadingDataPelatihanPart = findViewById(R.id.loading_data_pelatihan_part);
        noDataPengalamanPart = findViewById(R.id.no_data_pengalaman_part);
        noDataPelatihanPart = findViewById(R.id.no_data_pelatihan_part);
        loadingDataPengalamanImg = findViewById(R.id.loading_data_pengalaman_img);
        loadingDataPelatihanImg = findViewById(R.id.loading_data_pelatihan_img);
        addBTN = findViewById(R.id.add_btn);
        dataPengalamanRV = findViewById(R.id.data_pengalaman_rv);
        dataPelatihanRV = findViewById(R.id.data_pelatihan_rv);

        dataPengalamanRV.setLayoutManager(new LinearLayoutManager(this));
        dataPengalamanRV.setHasFixedSize(true);
        dataPengalamanRV.setNestedScrollingEnabled(false);
        dataPengalamanRV.setItemAnimator(new DefaultItemAnimator());

        dataPelatihanRV.setLayoutManager(new LinearLayoutManager(this));
        dataPelatihanRV.setHasFixedSize(true);
        dataPelatihanRV.setNestedScrollingEnabled(false);
        dataPelatihanRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataPengalamanImg);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataPelatihanImg);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDataPelatihanPart.setVisibility(View.VISIBLE);
                loadingDataPengalamanPart.setVisibility(View.VISIBLE);
                noDataPelatihanPart.setVisibility(View.GONE);
                noDataPengalamanPart.setVisibility(View.GONE);
                dataPengalamanRV.setVisibility(View.GONE);
                dataPelatihanRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        // getData();
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

        pengalamanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posisi = "pengalaman";
                pengalamanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify_choice));
                pelatihanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify));
                infoPelatihan.setVisibility(View.GONE);
                infoPengalaman.setVisibility(View.VISIBLE);

                loadingDataPelatihanPart.setVisibility(View.GONE);
                loadingDataPengalamanPart.setVisibility(View.VISIBLE);
                noDataPelatihanPart.setVisibility(View.GONE);
                noDataPengalamanPart.setVisibility(View.GONE);
                dataPengalamanRV.setVisibility(View.GONE);
                dataPelatihanRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //getData();
                    }
                }, 300);

            }
        });

        pelatihanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posisi = "pelatihan";
                pelatihanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify_choice));
                pengalamanBTN.setBackground(ContextCompat.getDrawable(InfoPengalamanDanPelatihanActivity.this, R.drawable.shape_notify));
                infoPengalaman.setVisibility(View.GONE);
                infoPelatihan.setVisibility(View.VISIBLE);

                loadingDataPelatihanPart.setVisibility(View.VISIBLE);
                loadingDataPengalamanPart.setVisibility(View.GONE);
                noDataPelatihanPart.setVisibility(View.GONE);
                noDataPengalamanPart.setVisibility(View.GONE);
                dataPengalamanRV.setVisibility(View.GONE);
                dataPelatihanRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // getData();
                    }
                }, 300);

            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posisi.equals("pengalaman")){
                    Intent intent = new Intent(InfoPengalamanDanPelatihanActivity.this, FormInfoPengalamanActivity.class);
                    startActivity(intent);
                } else if(posisi.equals("pelatihan")){
                    Intent intent = new Intent(InfoPengalamanDanPelatihanActivity.this, FormInfoPelatihanActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}