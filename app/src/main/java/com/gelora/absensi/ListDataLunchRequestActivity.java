package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterLunchRequest;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataListLunchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListDataLunchRequestActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    LinearLayout backBTN, addBTN, actionBar, loadingDataPart, noDataPart;
    String bagianRL = "";
    RecyclerView listDataRV;
    private DataListLunchRequest[] dataListLunchRequests;
    private AdapterLunchRequest adapterLunchRequest;
    SwipeRefreshLayout refreshLayout;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_lunch_request);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);
        loadingDataPart = findViewById(R.id.loading_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        listDataRV = findViewById(R.id.list_data_rv);

        listDataRV.setLayoutManager(new LinearLayoutManager(this));
        listDataRV.setHasFixedSize(true);
        listDataRV.setNestedScrollingEnabled(false);
        listDataRV.setItemAnimator(new DefaultItemAnimator());

        getBagian();

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listDataRV.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 800);
            }
        });

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(!bagianRL.isEmpty()){
                    Intent intent = new Intent(ListDataLunchRequestActivity.this, FormLunchRequestActivity.class);
                    intent.putExtra("bagianRL", bagianRL);
                    startActivity(intent);
                }
            }
        });

    }

    private void getBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_bagian_independen";
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
                                bagianRL = data.getString("bagian");
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_BAGIAN_RL, bagianRL);

                                getTimeOut();
                            } else {
                                new KAlertDialog(ListDataLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
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
                params.put("idDept", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(postRequest);

    }

    private void getTimeOut() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://hrisgelora.co.id/api/get_lunch_request_timeout";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            String time = response.getString("time");
                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_LUNCH_REQUEST_TIMEOUT, time);

                            getData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_list_lunch_request";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Success")) {
                                String jumlah_data = data.getString("jumlah_data");
                                if(Integer.parseInt(jumlah_data)>0){
                                    loadingDataPart.setVisibility(View.GONE);
                                    listDataRV.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    String data_list = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataListLunchRequests = gson.fromJson(data_list, DataListLunchRequest[].class);
                                    adapterLunchRequest = new AdapterLunchRequest(dataListLunchRequests, ListDataLunchRequestActivity.this);
                                    listDataRV.setAdapter(adapterLunchRequest);
                                } else {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingDataPart.setVisibility(View.GONE);
                                            listDataRV.setVisibility(View.GONE);
                                            noDataPart.setVisibility(View.VISIBLE);
                                        }
                                    }, 1000);
                                }
                            } else {
                                loadingDataPart.setVisibility(View.GONE);
                                listDataRV.setVisibility(View.GONE);
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
                params.put("bagian", bagianRL);
                params.put("nik", sharedPrefManager.getSpNik());
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(ListDataLunchRequestActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDataPart.setVisibility(View.VISIBLE);
        noDataPart.setVisibility(View.GONE);
        listDataRV.setVisibility(View.GONE);
        getBagian();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}