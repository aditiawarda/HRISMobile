package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterDataProject;
import com.gelora.absensi.adapter.AdapterProjectCategory;
import com.gelora.absensi.adapter.AdapterPulangCepat;
import com.gelora.absensi.model.DataPulangCepat;
import com.gelora.absensi.model.ProjectCategory;
import com.gelora.absensi.model.ProjectData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectViewActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, choiceCategoryBTN, noDataPart, loadingPart, addBTN, allBTN, markAll;
    TextView categoryChoiceTV;
    ImageView loadingDataProject;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    String categoryNow = "";
    private RecyclerView categoryProjectRV;
    private ProjectCategory[] projectCategories;
    private AdapterProjectCategory adapterProjectCategory;
    private RecyclerView projectRV;
    private ProjectData[] projectData;
    private AdapterDataProject adapterDataProject;
    String AUTH_TOKEN = "";

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

        AUTH_TOKEN = sharedPrefManager.getSpTokenTimeline();

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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        if(categoryNow.equals("")){
                            getProjectAll();
                        } else {
                            getProject(categoryNow);
                        }
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

        getProjectAll();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, "");
        categoryChoiceTV.setText("Semua");

    }

    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ProjectViewActivity.this).inflate(R.layout.layout_kategori_project, bottomSheet, false));
        categoryProjectRV = findViewById(R.id.kategori_project_rv);
        allBTN = findViewById(R.id.all_btn);
        markAll = findViewById(R.id.mark_all);

        if (sharedPrefAbsen.getSpKategoriProject().equals("")) {
            markAll.setVisibility(View.VISIBLE);
            allBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option_choice));
        } else {
            markAll.setVisibility(View.GONE);
            allBTN.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_option));
        }

        allBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markAll.setVisibility(View.VISIBLE);
                allBTN.setBackground(ContextCompat.getDrawable(ProjectViewActivity.this, R.drawable.shape_option_choice));
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, "");

                categoryNow = "";
                categoryChoiceTV.setText("Semua");

                getProjectCategory();

                projectRV.setVisibility(View.GONE);
                loadingPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getProjectAll();
                    }
                }, 1300);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 500);

            }
        });


        if(categoryProjectRV != null){
            categoryProjectRV.setLayoutManager(new LinearLayoutManager(this));
            categoryProjectRV.setHasFixedSize(true);
            categoryProjectRV.setNestedScrollingEnabled(false);
            categoryProjectRV.setItemAnimator(new DefaultItemAnimator());
        }

        getProjectCategory();

    }

    private void getProjectCategory2() {
        final String API_ENDPOINT_CATEGORY = "https://timeline.geloraaksara.co.id/category/list";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CATEGORY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = response.getJSONObject("data");
                            String data_category = data.getString("response");
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                    }
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    private void getProjectCategory() {
        final String API_ENDPOINT_CATEGORY = "https://geloraaksara.co.id/absen-online/api/get_project_category";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CATEGORY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            Log.d("Success.Response", response.toString());
                            String status = response.getString("status");
                            if(status.equals("Success")) {
                                String data_category = response.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                projectCategories = gson.fromJson(data_category, ProjectCategory[].class);
                                adapterProjectCategory = new AdapterProjectCategory(projectCategories, ProjectViewActivity.this);
                                try {
                                    categoryProjectRV.setAdapter(adapterProjectCategory);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }

    private void getProjectAll2() {
        final String API_ENDPOINT_CATEGORY = "https://timeline.geloraaksara.co.id/project/list?limit=30";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CATEGORY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = response.getJSONObject("data");
                            String data_project = data.getString("response");
                            JSONArray jsonArray = new JSONArray(data_project);
                            int arrayLength = jsonArray.length();
                            if(arrayLength != 0) {
                                projectRV.setVisibility(View.VISIBLE);
                                loadingPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        bottomSheet.dismissSheet();
                        connectionFailed();
                        projectRV.setVisibility(View.GONE);
                        loadingPart.setVisibility(View.GONE);
                        noDataPart.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    private void getProjectAll() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_project_all";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if(status.equals("Success")){
                                String data_project = data.getString("data");
                                JSONArray jsonArray = new JSONArray(data_project);
                                int arrayLength = jsonArray.length();
                                if(arrayLength != 0) {
                                    projectRV.setVisibility(View.VISIBLE);
                                    loadingPart.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.GONE);
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", sharedPrefManager.getSpNik()+"-"+sharedPrefManager.getSpNama());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getProject2(String category_id) {
        final String API_ENDPOINT_CATEGORY = "https://timeline.geloraaksara.co.id/category/detail?id="+category_id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CATEGORY,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = response.getJSONObject("data");
                            String data_project = data.getString("dataProject");
                            JSONArray jsonArray = new JSONArray(data_project);
                            int arrayLength = jsonArray.length();
                            if(arrayLength != 0) {
                                projectRV.setVisibility(View.VISIBLE);
                                loadingPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        bottomSheet.dismissSheet();
                        connectionFailed();
                        projectRV.setVisibility(View.GONE);
                        loadingPart.setVisibility(View.GONE);
                        noDataPart.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    private void getProject(String category_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_project_by_category";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if(status.equals("Success")){
                                String data_project = data.getString("data");
                                JSONArray jsonArray = new JSONArray(data_project);
                                int arrayLength = jsonArray.length();
                                if(arrayLength != 0) {
                                    projectRV.setVisibility(View.VISIBLE);
                                    loadingPart.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.GONE);
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_category", category_id);
                params.put("key", sharedPrefManager.getSpNik()+"-"+sharedPrefManager.getSpNama());
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

            markAll.setVisibility(View.GONE);
            allBTN.setBackground(ContextCompat.getDrawable(ProjectViewActivity.this, R.drawable.shape_option));

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, "");
        categoryNow = "";
        categoryChoiceTV.setText("Semua");
        projectRV.setVisibility(View.GONE);
        loadingPart.setVisibility(View.VISIBLE);
        noDataPart.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProjectAll();
            }
        }, 1300);
    }

}