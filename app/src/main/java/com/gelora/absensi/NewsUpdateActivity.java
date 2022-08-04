package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterDataTerlambat;
import com.gelora.absensi.adapter.AdapterNewsUpdate;
import com.gelora.absensi.model.DataNews;
import com.gelora.absensi.model.DataTerlambat;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewsUpdateActivity extends AppCompatActivity {

    LinearLayout backBTN, homeBTN;

    private RecyclerView newsRV;
    private DataNews[] dataNews;
    private AdapterNewsUpdate adapterNewsUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_update);

        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);

        newsRV = findViewById(R.id.news_rv);

        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setHasFixedSize(true);
        newsRV.setItemAnimator(new DefaultItemAnimator());

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsUpdateActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        getNewsUpdate();

    }

    private void getNewsUpdate() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://newsapi.org/v2/top-headlines?country=id&apiKey=d7046d646c9644318ca1feafb8b8389b";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");

                        String status = null;
                        try {
                            status = response.getString("status");
                            Toast.makeText(NewsUpdateActivity.this, status, Toast.LENGTH_SHORT).show();
                            String articles = response.getString("articles");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //connectionFailed();
                Toast.makeText(NewsUpdateActivity.this, "datapet", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);

    }


}