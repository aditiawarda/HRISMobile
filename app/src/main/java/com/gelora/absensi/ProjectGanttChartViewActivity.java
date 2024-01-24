package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Resource;
import com.anychart.enums.AvailabilityPeriod;
import com.anychart.enums.TimeTrackingMode;
import com.anychart.scales.calendar.Availability;
import com.gelora.absensi.adapter.AdapterDataTask;
import com.gelora.absensi.model.TaskData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectGanttChartViewActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    LinearLayout noDataPart;
    String projectId, AUTH_TOKEN;
    AnyChartView anyChartView;
    List<DataEntry> timelineTask = new ArrayList<>();
    Resource resource;
    TextView projectNameTV;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_gantt_chart_view);

        projectNameTV = findViewById(R.id.project_name_tv);
        anyChartView = findViewById(R.id.any_chart_view);
        noDataPart = findViewById(R.id.no_data_part_task);
        progressBar = findViewById(R.id.progress_bar);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);

        AUTH_TOKEN = sharedPrefManager.getSpTokenTimeline();
        projectId = getIntent().getExtras().getString("id_project");

        resource = AnyChart.resource();
        resource.zoomLevel(1d)
                .timeTrackingMode(TimeTrackingMode.ACTIVITY_PER_CHART)
                .currentStartDate("2024-01-01");

        resource.resourceListWidth(200);

        resource.calendar().availabilities(new Availability[] {
                new Availability(AvailabilityPeriod.DAY, (Double) null, 10d, true, (Double) null, (Double) null, 18d),
                new Availability(AvailabilityPeriod.DAY, (Double) null, 14d, false, (Double) null, (Double) null, 15d),
                new Availability(AvailabilityPeriod.WEEK, (Double) null, (Double) null, false, 5d, (Double) null, 18d),
                new Availability(AvailabilityPeriod.WEEK, (Double) null, (Double) null, false, 6d, (Double) null, 18d)
        });

        getDataTimeline(projectId);

    }

    private void getDataTimeline(String project_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_project_detail";
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
                            String projectName = data.getString("projectName");
                            String createdBy = data.getString("createdBy");
                            String dateStart = data.getString("dateStart");
                            String dateEnd = data.getString("dateEnd");
                            String taskList = data.getString("taskList");

                            projectNameTV.setText(projectName);

                            JSONArray jsonArrayTaskList = new JSONArray(taskList);
                            Activity ganttActivity = null;

                            if(jsonArrayTaskList.length()>0){
                                for (int i = 0; i < jsonArrayTaskList.length(); i++) {
                                    JSONObject task = jsonArrayTaskList.getJSONObject(i);
                                    String pic = task.getString("pic");
                                    String taskname = task.getString("taskname");
                                    String timeline = task.getString("timeline");
                                    String status = task.getString("status");

                                    String tanggalMulai = timeline.substring(6,10)+"-"+timeline.substring(0,2)+"-"+timeline.substring(3,5);
                                    String tanggalSelesai = timeline.substring(19,23)+"-"+timeline.substring(13,15)+"-"+timeline.substring(16,18);

                                    String colorFill;
                                    if(status.equals("5")){
                                        colorFill = "#5abf64"; // Done
                                    } else if(status.equals("4")){
                                        colorFill = "#c66f6d"; // On Hold
                                    } else if(status.equals("3")){
                                        colorFill = "#bf9a5a"; // Waiting Approval
                                    } else if(status.equals("2")){
                                        colorFill = "#d4c962"; // On Progress
                                    } else if(status.equals("1")){
                                        colorFill = "#d79ce5"; // Waiting
                                    } else {
                                        colorFill = "#9c9c9c";
                                    }

                                    ganttActivity = new Activity(
                                            taskname,
                                            new Interval[]{new Interval(tanggalMulai, tanggalSelesai)},
                                            colorFill);

                                    if(pic.equals("")||pic.equals(" ")||pic.equals("null")){
                                        timelineTask.add(new ResourceDataEntry(
                                                pic,
                                                pic,
                                                new Activity[]{
                                                        ganttActivity
                                                })
                                        );
                                        resource.data(timelineTask);
                                        resource.height();

                                    } else {
                                        String shortName;
                                        String[] shortNameArray = pic.substring(11, pic.length()).split(" ");

                                        if(shortNameArray.length>1){
                                            if(shortNameArray[0].length()<3){
                                                shortName = shortNameArray[1];
                                                System.out.println(shortName);
                                            } else {
                                                shortName = shortNameArray[0];
                                                System.out.println(shortName);
                                            }
                                        } else {
                                            shortName = shortNameArray[0];
                                            System.out.println(shortName);
                                        }

                                        timelineTask.add(new ResourceDataEntry(
                                                shortName,
                                                pic.substring(0,10),
                                                new Activity[]{
                                                        ganttActivity
                                                })
                                        );

                                        resource.data(timelineTask);
                                        resource.height();

                                    }

                                }

                                anyChartView.setChart(resource);
                                noDataPart.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
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

    private void getDataTimeline2(String project_id) {
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
                            String dateStart = data.getString("dateStart");
                            String dateEnd = data.getString("dateEnd");
                            String taskList = data.getString("taskList");

                            projectNameTV.setText(projectName);

                            JSONArray jsonArrayTaskList = new JSONArray(taskList);
                            Activity ganttActivity = null;

                            if(jsonArrayTaskList.length()>0){
                                for (int i = 0; i < jsonArrayTaskList.length(); i++) {
                                    JSONObject task = jsonArrayTaskList.getJSONObject(i);
                                    String pic = task.getString("pic");
                                    String taskname = task.getString("taskname");
                                    String timeline = task.getString("timeline");
                                    String status = task.getString("status");

                                    String tanggalMulai = timeline.substring(6,10)+"-"+timeline.substring(0,2)+"-"+timeline.substring(3,5);
                                    String tanggalSelesai = timeline.substring(19,23)+"-"+timeline.substring(13,15)+"-"+timeline.substring(16,18);

                                    String colorFill;
                                    if(status.equals("5")){
                                        colorFill = "#5abf64"; // Done
                                    } else if(status.equals("4")){
                                        colorFill = "#c66f6d"; // On Hold
                                    } else if(status.equals("3")){
                                        colorFill = "#bf9a5a"; // Waiting Approval
                                    } else if(status.equals("2")){
                                        colorFill = "#d4c962"; // On Progress
                                    } else if(status.equals("1")){
                                        colorFill = "#d79ce5"; // Waiting
                                    } else {
                                        colorFill = "#9c9c9c";
                                    }

                                    ganttActivity = new Activity(
                                            taskname,
                                            new Interval[]{new Interval(tanggalMulai, tanggalSelesai)},
                                            colorFill);

                                    if(pic.equals("")||pic.equals(" ")||pic.equals("null")){
                                        timelineTask.add(new ResourceDataEntry(
                                                pic,
                                                pic,
                                                new Activity[]{
                                                        ganttActivity
                                                })
                                        );
                                        resource.data(timelineTask);
                                        resource.height();

                                    } else {
                                        String shortName;
                                        String[] shortNameArray = pic.substring(11, pic.length()).split(" ");

                                        if(shortNameArray.length>1){
                                            if(shortNameArray[0].length()<3){
                                                shortName = shortNameArray[1];
                                                System.out.println(shortName);
                                            } else {
                                                shortName = shortNameArray[0];
                                                System.out.println(shortName);
                                            }
                                        } else {
                                            shortName = shortNameArray[0];
                                            System.out.println(shortName);
                                        }

                                        timelineTask.add(new ResourceDataEntry(
                                                shortName,
                                                pic.substring(0,10),
                                                new Activity[]{
                                                        ganttActivity
                                                })
                                        );

                                        resource.data(timelineTask);
                                        resource.height();

                                    }

                                }

                                anyChartView.setChart(resource);
                                noDataPart.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
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

    private class ResourceDataEntry extends DataEntry {
        ResourceDataEntry(String name, String description, Activity[] activities) {
            setValue("name", name);
            setValue("description", description);
            setValue("activities", activities);
        }
    }

    private class Activity extends DataEntry {
        Activity(String name, Interval[] intervals, String fill) {
            setValue("name", name);
            setValue("intervals", intervals);
            setValue("fill", fill);
        }
    }

    private class Interval extends DataEntry {
        Interval(String start, String end) {
            setValue("start", start);
            setValue("end", end);
        }
    }

    private void connectionFailed(){
        CookieBar.build(ProjectGanttChartViewActivity.this)
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