package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataHadir;
import com.gelora.absensi.adapter.AdapterListChatMate;
import com.gelora.absensi.model.DataHadir;
import com.gelora.absensi.model.ListChatMate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListChatMateActivity extends AppCompatActivity {

    private RecyclerView listChatRV;
    private ListChatMate[] listChatMates;
    private AdapterListChatMate adapterListChatMate;
    SharedPrefManager sharedPrefManager;
    LinearLayout backBTN, noDataPart, loadingPart;
    ImageView loadingImage;
    LinearLayout newChatBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat_mate);

        sharedPrefManager = new SharedPrefManager(this);
        listChatRV = findViewById(R.id.data_list_chat_mate_rv);
        backBTN = findViewById(R.id.back_btn);
        noDataPart = findViewById(R.id.no_data_part);
        loadingPart = findViewById(R.id.loading_data_part);
        loadingImage = findViewById(R.id.loading_data_img);
        newChatBTN = findViewById(R.id.new_chat_btn);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingImage);

        listChatRV.setLayoutManager(new LinearLayoutManager(this));
        listChatRV.setHasFixedSize(true);
        listChatRV.setNestedScrollingEnabled(false);
        listChatRV.setItemAnimator(new DefaultItemAnimator());

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        newChatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListChatMateActivity.this, ChatContactActivity.class);
                startActivity(intent);
            }
        });

        getListChatMate();

    }

    private void getListChatMate() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_list_chatmate";
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataPart.setVisibility(View.GONE);
                                        loadingPart.setVisibility(View.GONE);
                                        listChatRV.setVisibility(View.VISIBLE);
                                    }
                                }, 500);

                                String data_list_chat = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                listChatMates = gson.fromJson(data_list_chat, ListChatMate[].class);
                                adapterListChatMate = new AdapterListChatMate(listChatMates, ListChatMateActivity.this);
                                listChatRV.setAdapter(adapterListChatMate);
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingPart.setVisibility(View.GONE);
                                listChatRV.setVisibility(View.GONE);
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
                        //connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("saya", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getListChatMate();
    }

}