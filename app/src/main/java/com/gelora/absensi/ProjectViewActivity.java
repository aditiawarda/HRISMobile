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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;

public class ProjectViewActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, choiceCategoryBTN, testItemBTN;
    ImageView loadingDataProject;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        sharedPrefManager = new SharedPrefManager(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        loadingDataProject = findViewById(R.id.loading_data_project);
        choiceCategoryBTN = findViewById(R.id.choice_category);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        testItemBTN = findViewById(R.id.test_item_btn);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataProject);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                dataProjectRV.setVisibility(View.GONE);
//                loadingProjectPart.setVisibility(View.VISIBLE);
//                emptyDataProject.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        //getNews(categoryNews);
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

        choiceCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryChoice();
            }
        });

        testItemBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectViewActivity.this, DetailProjectActivity.class);
                startActivity(intent);
            }
        });

    }

    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ProjectViewActivity.this).inflate(R.layout.layout_kategori_project, bottomSheet, false));
    }

}