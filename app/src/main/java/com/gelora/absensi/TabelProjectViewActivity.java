package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterDataTask;
import com.gelora.absensi.adapter.AdapterDataTaskTabel;
import com.gelora.absensi.model.TaskData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabelProjectViewActivity extends AppCompatActivity {

    TextView projectNameTV, startDateTV, endDateTV, projectLeaderTV, dokumentasiProjectBTN;
    LinearLayout noDataPartTask;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    String projectId = "", AUTH_TOKEN = "";
    private RecyclerView taskRV;
    private TaskData[] taskData;
    private AdapterDataTaskTabel adapterDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabel_project_view);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);

        projectNameTV = findViewById(R.id.project_name_tv);
        projectLeaderTV = findViewById(R.id.project_leader_tv);

        AUTH_TOKEN = sharedPrefManager.getSpTokenTimeline();
        projectId = getIntent().getExtras().getString("id_project");

        noDataPartTask = findViewById(R.id.no_data_part_task);
        taskRV = findViewById(R.id.data_task_rv);
        taskRV.setLayoutManager(new LinearLayoutManager(this));
        taskRV.setHasFixedSize(true);
        taskRV.setNestedScrollingEnabled(false);
        taskRV.setItemAnimator(new DefaultItemAnimator());

        getDetailProject(projectId);

    }

    private void getDetailProject(String project_id) {
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
//                            startDateTV.setText(dateStart.substring(8,10)+"/"+dateStart.substring(5,7)+"/"+dateStart.substring(0,4));
//                            endDateTV.setText(dateEnd.substring(8,10)+"/"+dateEnd.substring(5,7)+"/"+dateEnd.substring(0,4));
//                            countDuration(dateStart.substring(0,10), dateEnd.substring(0,10));

                            String[] namaPIC = pic.split("-");
                            projectLeaderTV.setText(namaPIC[1]);

                            JSONArray jsonArray = new JSONArray(taskList);
                            int arrayLength = jsonArray.length();
                            if(arrayLength != 0) {
                                taskRV.setVisibility(View.VISIBLE);
                                noDataPartTask.setVisibility(View.GONE);
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                taskData = gson.fromJson(taskList, TaskData[].class);
                                adapterDataTask = new AdapterDataTaskTabel(taskData,TabelProjectViewActivity.this);
                                try {
                                    taskRV.setAdapter(adapterDataTask);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                taskRV.setVisibility(View.GONE);
                                noDataPartTask.setVisibility(View.VISIBLE);
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
//                        connectionFailed();
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

}