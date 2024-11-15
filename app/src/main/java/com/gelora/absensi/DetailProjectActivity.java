package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterDataTask;
import com.gelora.absensi.model.TaskData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailProjectActivity extends AppCompatActivity {

    LinearLayout dokumentasiBTN, tabelBTN, actionBar, backBTN, loadingDataPart, noDataPart, addBTN;
    TextView projectNameTV, startDateTV, endDateTV, projectLeaderTV, dokumentasiProjectBTN;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    String projectId = "", AUTH_TOKEN = "";
    private RecyclerView taskRV;
    private TaskData[] taskData;
    private AdapterDataTask adapterDataTask;
    private Handler handler = new Handler();

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
        loadingDataPart = findViewById(R.id.loading_data_part_task);
        noDataPart = findViewById(R.id.no_data_part_task);
        taskRV = findViewById(R.id.data_task_rv);
        projectNameTV = findViewById(R.id.project_name_tv);
        addBTN = findViewById(R.id.add_btn);
        tabelBTN = findViewById(R.id.table_btn);
        startDateTV = findViewById(R.id.start_date_tv);
        endDateTV = findViewById(R.id.end_date_tv);
        projectLeaderTV = findViewById(R.id.project_leader_tv);
        dokumentasiProjectBTN = findViewById(R.id.dokumentasi_project_btn);
        dokumentasiBTN = findViewById(R.id.dokumentasi_btn);

        AUTH_TOKEN = sharedPrefManager.getSpTokenTimeline();
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDetailProject(projectId);
                    }
                }, 500);
            }
        });

        dokumentasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectActivity.this, ViewDokumentasiProjectActivity.class);
                intent.putExtra("id_project",projectId);
                startActivity(intent);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectActivity.this, TabelProjectViewActivity.class);
                intent.putExtra("id_project",projectId);
                startActivity(intent);
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProjectActivity.this, FormInputTaskActivity.class);
                intent.putExtra("id_project",projectId);
                startActivity(intent);
            }
        });

    }

    private void getDetailProject2(String project_id) {
        final String API_ENDPOINT_CATEGORY = "https://timeline.geloraaksara.co.id/project/detail?id="+project_id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CATEGORY,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = response.getJSONObject("data");
                            String projectName = data.getString("projectName");
                            String createdBy = data.getString("createdBy");
                            String pic = data.getString("pic");
                            String dateStart = data.getString("dateStart");
                            String dateEnd = data.getString("dateEnd");
                            String taskList = data.getString("taskList");
                            projectNameTV.setText(projectName);
                            startDateTV.setText(dateStart.substring(8,10)+"/"+dateStart.substring(5,7)+"/"+dateStart.substring(0,4));
                            endDateTV.setText(dateEnd.substring(8,10)+"/"+dateEnd.substring(5,7)+"/"+dateEnd.substring(0,4));
                            countDuration(dateStart.substring(0,10), dateEnd.substring(0,10));

                            String[] namaPIC = pic.split("-");
                            projectLeaderTV.setText(namaPIC[1]);

                            JSONArray jsonArray = new JSONArray(taskList);
                            int arrayLength = jsonArray.length();
                            if(arrayLength != 0) {
                                taskRV.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                taskData = gson.fromJson(taskList, TaskData[].class);
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
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

    private void getDetailProject(String project_id) {
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PROJECT_OPEN, project_id);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_project_detail";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            data =  new JSONObject(response);
                            String status = data.getString("status");
                            if(status.equals("Success")){
                                String main_data = data.getString("data");
                                JSONObject main =  new JSONObject(main_data);
                                String projectName = main.getString("projectName");
                                String pic = main.getString("pic");
                                String dateStart = main.getString("dateStart");
                                String dateEnd = main.getString("dateEnd");
                                String taskList = main.getString("taskList");

                                projectNameTV.setText(projectName);
                                startDateTV.setText(dateStart.substring(8,10)+"/"+dateStart.substring(5,7)+"/"+dateStart.substring(0,4));
                                endDateTV.setText(dateEnd.substring(8,10)+"/"+dateEnd.substring(5,7)+"/"+dateEnd.substring(0,4));
                                countDuration(dateStart.substring(0,10), dateEnd.substring(0,10));

                                // JSONArray jsonArrayPIC = new JSONArray(pic);
                                // JSONObject data_pic =  new JSONObject(jsonArrayPIC.getString(0));
                                // String projectLeader = data_pic.getString("picName");
                                // projectLeaderTV.setText(projectLeader);

                                if(pic.contains("-")){
                                    String[] namaPIC = pic.split("-");
                                    projectLeaderTV.setText(namaPIC[1]);
                                } else {
                                    projectLeaderTV.setText(pic);
                                }

                                if(taskList.equals("null")||taskList.equals("")||taskList.equals("[]")){
                                    taskRV.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.VISIBLE);
                                } else {
                                    JSONArray jsonArray = new JSONArray(taskList);
                                    int arrayLength = jsonArray.length();
                                    if(arrayLength != 0) {

                                        // Iterate through the JSONArray in reverse order
                                        JSONArray sortedJsonArray = new JSONArray();
                                        for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                            try {
                                                sortedJsonArray.put(jsonArray.getJSONObject(i));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        String ascData = taskList;
                                        String descData = sortedJsonArray.toString();

                                        taskRV.setVisibility(View.VISIBLE);
                                        loadingDataPart.setVisibility(View.GONE);
                                        noDataPart.setVisibility(View.GONE);
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        taskData = gson.fromJson(descData, TaskData[].class);
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
                params.put("project_id", project_id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SetTextI18n")
    public void countDuration(String startDate, String endDate){
        String startDateString = startDate;
        String endDateString = endDate;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(endDateString);
            date2 = format.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = waktu1 - waktu2;

        long totalDay = (selisih_waktu / (24 * 60 * 60 * 1000)) + 1;

        String startDateString2 = startDate;
        String endDateString2 = getDate();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1_2 = null;
        Date date2_2 = null;
        try {
            date1_2 = format.parse(endDateString2);
            date2_2 = format.parse(startDateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1_2 = date1_2.getTime();
        long waktu2_2 = date2_2.getTime();
        long selisih_waktu_2 = waktu1_2 - waktu2_2;

        long progressDay = (selisih_waktu_2 / (24 * 60 * 60 * 1000)) + 1;

        String startDateString3 = endDate;
        String endDateString3 = getDate();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1_3 = null;
        Date date2_3 = null;
        try {
            date1_3 = format.parse(endDateString3);
            date2_3 = format.parse(startDateString3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1_3 = date1_3.getTime();
        long waktu2_3 = date2_3.getTime();
        long selisih_waktu_3 = waktu1_3 - waktu2_3;

        long overDay = (selisih_waktu_3 / (24 * 60 * 60 * 1000)) + 1;

        if(overDay >= 1){
            progressDay = totalDay;
        }

        float persentase = ((float) progressDay / (float) totalDay) * 100;

        LinearLayout progress = findViewById(R.id.timeline_progress_project);
        LinearLayout left = findViewById(R.id.timeline_left_project);

        LinearLayout.LayoutParams layoutParamsProgress = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.0f);
        LinearLayout.LayoutParams layoutParamsLeft = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.10f);

        float weightProgress = (float) Math.round(persentase) / 100.0f;
        float weightLeft = (float) (100 - Math.round(persentase)) / 100.0f;

        layoutParamsProgress.weight = weightProgress;
        layoutParamsLeft.weight = weightLeft;

        progress.setLayoutParams(layoutParamsProgress);
        left.setLayoutParams(layoutParamsLeft);

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        getDetailProject(projectId);
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}