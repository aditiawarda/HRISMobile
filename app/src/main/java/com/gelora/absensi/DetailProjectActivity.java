package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataProject;
import com.gelora.absensi.adapter.AdapterDataTask;
import com.gelora.absensi.model.ProjectData;
import com.gelora.absensi.model.TaskData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailProjectActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, loadingDataPart, noDataPart, addBTN;
    TextView projectNameTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    String projectId = "";
    ImageView loadingDataTask;
    private RecyclerView taskRV;
    private TaskData[] taskData;
    private AdapterDataTask adapterDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        loadingDataTask = findViewById(R.id.loading_data_task);
        loadingDataPart = findViewById(R.id.loading_data_part_task);
        noDataPart = findViewById(R.id.no_data_part_task);
        taskRV = findViewById(R.id.data_task_rv);
        projectNameTV = findViewById(R.id.project_name_tv);
        addBTN = findViewById(R.id.add_btn);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataTask);

        projectId = getIntent().getExtras().getString("id_project");

        taskRV.setLayoutManager(new LinearLayoutManager(this));
        taskRV.setHasFixedSize(true);
        taskRV.setNestedScrollingEnabled(false);
        taskRV.setItemAnimator(new DefaultItemAnimator());

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                taskRV.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailProject(projectId);
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
                Intent intent = new Intent(DetailProjectActivity.this, FormInputTaskActivity.class);
                startActivity(intent);
            }
        });

        getDetailProject(projectId);

    }

    private void getDetailProject(String category_id) {
        final String url = "https://geloraaksara.co.id/absen-online/api/project_detail";
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
                                taskRV.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                                String projectName = data.getString("projectName");
                                projectNameTV.setText(projectName);
                                String task_list = data.getString("task_list");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                taskData = gson.fromJson(task_list, TaskData[].class);
                                adapterDataTask = new AdapterDataTask(taskData,DetailProjectActivity.this);
                                try {
                                    taskRV.setAdapter(adapterDataTask);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                taskRV.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_project", projectId);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailProjectActivity.this)
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