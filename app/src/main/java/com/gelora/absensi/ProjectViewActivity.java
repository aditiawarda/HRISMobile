package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterDataProject;
import com.gelora.absensi.adapter.AdapterProjectCategory;
import com.gelora.absensi.model.ProjectCategory;
import com.gelora.absensi.model.ProjectData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectViewActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, choiceCategoryBTN, noDataPart, loadingPart, addBTN;
    TextView categoryChoiceTV;
    ImageView loadingDataProject;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;

    String categoryNow = "1";

    private RecyclerView categoryProjectRV;
    private ProjectCategory[] projectCategories;
    private AdapterProjectCategory adapterProjectCategory;

    private RecyclerView projectRV;
    private ProjectData[] projectData;
    private AdapterDataProject adapterDataProject;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        loadingDataProject = findViewById(R.id.loading_data_project);
        choiceCategoryBTN = findViewById(R.id.choice_category);
        categoryChoiceTV = findViewById(R.id.category_choice_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);

        loadingPart = findViewById(R.id.loading_data_part_project);
        noDataPart = findViewById(R.id.no_data_part_project);
        addBTN = findViewById(R.id.add_btn);
        projectRV = findViewById(R.id.data_project_rv);

        projectRV.setLayoutManager(new LinearLayoutManager(this));
        projectRV.setHasFixedSize(true);
        projectRV.setNestedScrollingEnabled(false);
        projectRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataProject);

        LocalBroadcastManager.getInstance(this).registerReceiver(categoryProjectBroad, new IntentFilter("category_broad"));

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectViewActivity.this, FormInputProjectActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                projectRV.setVisibility(View.GONE);
                loadingPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, "1");
                categoryNow = "1";
                categoryChoiceTV.setText("Semua");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getProject(categoryNow);
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

        getProject(categoryNow);
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, "1");
        categoryChoiceTV.setText("Semua");

    }

    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ProjectViewActivity.this).inflate(R.layout.layout_kategori_project, bottomSheet, false));
        categoryProjectRV = findViewById(R.id.kategori_project_rv);
        if(categoryProjectRV != null){
            categoryProjectRV.setLayoutManager(new LinearLayoutManager(this));
            categoryProjectRV.setHasFixedSize(true);
            categoryProjectRV.setNestedScrollingEnabled(false);
            categoryProjectRV.setItemAnimator(new DefaultItemAnimator());
        }

        getProjectCategory();

    }

    private void getProjectCategory() {
        final String url = "https://geloraaksara.co.id/absen-online/api/project_category";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String data_category = data.getString("data");
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            projectCategories = gson.fromJson(data_category, ProjectCategory[].class);
                            adapterProjectCategory = new AdapterProjectCategory(projectCategories,ProjectViewActivity.this);
                            try {
                                categoryProjectRV.setAdapter(adapterProjectCategory);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getProject(String category_id) {
        final String url = "https://geloraaksara.co.id/absen-online/api/project_list";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            if(status.equals("Success")){
                                projectRV.setVisibility(View.VISIBLE);
                                loadingPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);

                                String data_project = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                projectData = gson.fromJson(data_project, ProjectData[].class);
                                adapterDataProject = new AdapterDataProject(projectData,ProjectViewActivity.this);
                                try {
                                    projectRV.setAdapter(adapterDataProject);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                projectRV.setVisibility(View.GONE);
                                loadingPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
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
                params.put("categoryId", category_id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public BroadcastReceiver categoryProjectBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String categoryId = intent.getStringExtra("id_kategori");
            String categoryName = intent.getStringExtra("nama_kategori");

            categoryNow = categoryId;
            categoryChoiceTV.setText(categoryName);

            projectRV.setVisibility(View.GONE);
            loadingPart.setVisibility(View.VISIBLE);
            noDataPart.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getProject(categoryId);
                }
            }, 1300);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    private void connectionFailed(){
        CookieBar.build(ProjectViewActivity.this)
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