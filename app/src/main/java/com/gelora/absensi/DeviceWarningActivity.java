package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceWarningActivity extends AppCompatActivity {

    TextView messageWarningDevice;
    String userNIK, userName;
    LinearLayout loginBTN, contactIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_warning);

        loginBTN = findViewById(R.id.to_login_btn);
        messageWarningDevice = findViewById(R.id.message_warning_device);
        contactIT = findViewById(R.id.contact_to_it);
        userNIK = getIntent().getExtras().getString("nik");
        userName = getIntent().getExtras().getString("atas_nama");

        if (userName.equals("")){
            messageWarningDevice.setVisibility(View.GONE);
        } else {
            messageWarningDevice.setVisibility(View.VISIBLE);
            messageWarningDevice.setText("Perangkat ini terdaftar atas nama "+userName.toUpperCase()+" dengan NIK : "+userNIK);
        }

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceWarningActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        contactIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactIT();
            }
        });

    }

    private void getContactIT() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/get_contact_it";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String bagian = response.getString("bagian");
                            String nama = response.getString("nama");
                            String whatsapp = response.getString("whatsapp");

                            Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                            startActivity(webIntent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

}