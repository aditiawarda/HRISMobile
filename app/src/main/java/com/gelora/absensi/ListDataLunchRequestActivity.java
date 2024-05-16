package com.gelora.absensi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterStatusAbsen;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.StatusAbsen;
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
    LinearLayout backBTN, addBTN, actionBar;
    String bagianRL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_lunch_request);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        backBTN = findViewById(R.id.back_btn);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);

        if (sharedPrefManager.getSpIdHeadDept().equals("5")) {
            if (sharedPrefManager.getSpIdDept().equals("21")) {
                bagianRL = "Satpam";
            } else {
                bagianRL = "HRD";
            }
        } else if (sharedPrefManager.getSpIdHeadDept().equals("3")) {
            bagianRL = "Marketing";
        } else if (sharedPrefManager.getSpIdHeadDept().equals("4")) {
            bagianRL = "Akunting";
        } else if (sharedPrefManager.getSpIdHeadDept().equals("2")) {
            bagianRL = "EDP";
        } else {
            getBagian();
        }

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
                                Toast.makeText(ListDataLunchRequestActivity.this, bagianRL, Toast.LENGTH_SHORT).show();
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

}