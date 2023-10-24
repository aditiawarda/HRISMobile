package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailDataSerahTerimaExitClearanceActivity extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;
    LinearLayout actionBar, backBTN, nameCard, lampiranBTN;
    TextView nameUser, serahTerimaTV, statusTV, tglMasukTV, tglKeluarTV, lampiranTV;

    String role, id_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_serah_terima_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);

        nameCard = findViewById(R.id.name_card);
        nameUser = findViewById(R.id.name_of_user);
        tglMasukTV = findViewById(R.id.tgl_masuk);
        tglKeluarTV = findViewById(R.id.tgl_keluar);
        serahTerimaTV = findViewById(R.id.serah_terima_tv);
        statusTV = findViewById(R.id.status_tv);
        lampiranBTN = findViewById(R.id.lampiran_btn);
        lampiranTV = findViewById(R.id.lampiran_tv);

        role = getIntent().getExtras().getString("role");
        id_st = getIntent().getExtras().getString("id_st");

        if(role.equals("me")){
            nameUser.setText(sharedPrefManager.getSpNama().toUpperCase());
        } else if(role.equals("approval")){
            String nama = getIntent().getExtras().getString("nama");
            nameUser.setText(nama.toUpperCase());
        }

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData(id_st);
                    }
                }, 2000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData(id_st);

    }

    private void getData(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_st_exit_clearance";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                JSONObject detail = data.getJSONObject("data");
                                String id = detail.getString("id");
                                String id_core = detail.getString("id_core");
                                String tgl_masuk = detail.getString("tgl_masuk");
                                String tgl_keluar = detail.getString("tgl_keluar");
                                String urutan_st = detail.getString("urutan_st");
                                String serah_terima = detail.getString("serah_terima");
                                String lampiran = detail.getString("lampiran");
                                String approval = detail.getString("approval");
                                String tgl_approval = detail.getString("tgl_approval");
                                String nama_approval = detail.getString("nama_approval");
                                String jabatan_aproval = detail.getString("jabatan_aproval");
                                String bagian_approval = detail.getString("bagian_approval");
                                String departemen_approval = detail.getString("departemen_approval");

                                tglMasukTV.setText(tgl_masuk.substring(8,10)+"/"+tgl_masuk.substring(5,7)+"/"+tgl_masuk.substring(0,4));
                                tglKeluarTV.setText(tgl_keluar.substring(8,10)+"/"+tgl_keluar.substring(5,7)+"/"+tgl_keluar.substring(0,4));

                                serahTerimaTV.setText(serah_terima);
                                if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                    statusTV.setText("Disetujui");
                                    statusTV.setTextColor(Color.parseColor("#279A2B"));
                                    nameUser.setTextColor(Color.parseColor("#279A2B"));
                                    nameCard.setBackgroundResource(R.drawable.shape_attantion_green);
                                } else {
                                    if(role.equals("me")){
                                        statusTV.setText("Diproses");
                                    } else if(role.equals("approval")){
                                        statusTV.setText("Menunggu");
                                    }
                                    statusTV.setTextColor(Color.parseColor("#D37E00"));
                                    nameUser.setTextColor(Color.parseColor("#D37E00"));
                                    nameCard.setBackgroundResource(R.drawable.shape_attantion);
                                }

                                if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                    lampiranBTN.setVisibility(View.VISIBLE);
                                    lampiranTV.setText(lampiran);
                                    lampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailDataSerahTerimaExitClearanceActivity.this, PdfViewerActivity.class);
                                            intent.putExtra("initialisasi", "detail");
                                            intent.putExtra("kode_st", urutan_st);
                                            intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    lampiranBTN.setVisibility(View.GONE);
                                    lampiranTV.setText("Lampiran tidak tersedia");
                                }

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
                        //connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_st", id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailDataSerahTerimaExitClearanceActivity.this)
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