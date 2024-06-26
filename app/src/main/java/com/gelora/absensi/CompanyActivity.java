package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyActivity extends AppCompatActivity {

    LinearLayout backBTN, actionBar, visiMisiPart;
    TextView p1;
    TextView visiTV, misiTV;
    SharedPrefManager sharedPrefManager;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        requestQueue = Volley.newRequestQueue(this);
        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        visiMisiPart = findViewById(R.id.visi_misi_part);
        visiTV = findViewById(R.id.visi_tv);
        misiTV = findViewById(R.id.misi_tv);

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

        getVisiMisi();

    }

    private void getVisiMisi() {
        final String url = "https://hrisgelora.co.id/api/get_visi_misi";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                visiMisiPart.setVisibility(View.VISIBLE);
                                String visi = response.getString("visi");
                                String misi = response.getString("misi");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    visiTV.setText(Html.fromHtml(visi, Html.FROM_HTML_MODE_COMPACT));
                                    misiTV.setText(Html.fromHtml(misi, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    visiTV.setText(Html.fromHtml(visi));
                                    misiTV.setText(Html.fromHtml(misi));
                                }
                            } else {
                                visiMisiPart.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            visiMisiPart.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                visiMisiPart.setVisibility(View.GONE);
            }
        });

        requestQueue.add(request);

    }

}