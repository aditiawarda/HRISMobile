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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
    LinearLayout actionBar, backBTN, statusCard, lampiranBTN, verifBTN, verifPart;
    LinearLayout stRincian1, stRincian2, stRincian3, stRincian4, stRincian5, stRincian6;
    TextView namaKaryawanTV, nikKaryawanTV, detailKaryawanTV, serahTerimaTV, statusTV, tglMasukTV, tglKeluarTV, lampiranTV;
    ImageView statusGif;
    String role, id_st, finalApprove;
    CheckBox sM21, sK21, sM22, sK22;
    CheckBox sM31, sK31, sM32, sK32;
    EditText ket31, ket32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_serah_terima_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);

        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        detailKaryawanTV = findViewById(R.id.detail_karyawan_tv);
        statusGif = findViewById(R.id.status_gif);
        verifBTN = findViewById(R.id.verif_btn);
        verifPart = findViewById(R.id.verif_part);

        statusCard = findViewById(R.id.status_card);
        tglMasukTV = findViewById(R.id.tgl_masuk);
        tglKeluarTV = findViewById(R.id.tgl_keluar);
        serahTerimaTV = findViewById(R.id.serah_terima_tv);
        statusTV = findViewById(R.id.status_tv);
        lampiranBTN = findViewById(R.id.lampiran_btn);
        lampiranTV = findViewById(R.id.lampiran_tv);

        stRincian1 = findViewById(R.id.st_rincian_1);
        stRincian2 = findViewById(R.id.st_rincian_2);
        stRincian3 = findViewById(R.id.st_rincian_3);
        stRincian4 = findViewById(R.id.st_rincian_4);
        stRincian5 = findViewById(R.id.st_rincian_5);
        stRincian6 = findViewById(R.id.st_rincian_6);

        sK21 = findViewById(R.id.sk_2_1);
        sM21 = findViewById(R.id.sm_2_1);
        sK22 = findViewById(R.id.sk_2_2);
        sM22 = findViewById(R.id.sm_2_2);
        ket31 = findViewById(R.id.ket_3_1);
        ket32 = findViewById(R.id.ket_3_2);

        sK31 = findViewById(R.id.sk_3_1);
        sM31 = findViewById(R.id.sm_3_1);
        sK32 = findViewById(R.id.sk_3_2);
        sM32 = findViewById(R.id.sm_3_2);

        role = getIntent().getExtras().getString("role");
        id_st = getIntent().getExtras().getString("id_st");

        if(role.equals("me")){
            namaKaryawanTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        } else if(role.equals("approval")){
            String nama = getIntent().getExtras().getString("nama");
            namaKaryawanTV.setText(nama.toUpperCase());
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
                                String nik_requester = detail.getString("nik_requester");
                                String jabatan_requester = detail.getString("jabatan_requester");
                                String bagian_requester = detail.getString("bagian_requester");
                                String departemen_requester = detail.getString("departemen_requester");
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

                                nikKaryawanTV.setText(nik_requester);
                                detailKaryawanTV.setText(jabatan_requester+" | "+bagian_requester+" | "+departemen_requester);

                                tglMasukTV.setText(tgl_masuk.substring(8,10)+"/"+tgl_masuk.substring(5,7)+"/"+tgl_masuk.substring(0,4));
                                tglKeluarTV.setText(tgl_keluar.substring(8,10)+"/"+tgl_keluar.substring(5,7)+"/"+tgl_keluar.substring(0,4));

                                serahTerimaTV.setText(serah_terima);
                                if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                    finalApprove = "1";
                                    statusTV.setText("Sudah Diverifikasi");
                                    statusTV.setTextColor(Color.parseColor("#279A2B"));
                                    statusCard.setBackgroundResource(R.drawable.shape_attantion_green);
                                    statusGif.setPadding(0,0,0,0);
                                    Glide.with(getApplicationContext())
                                            .load(R.drawable.success_ic)
                                            .into(statusGif);
                                    verifPart.setVisibility(View.GONE);
                                } else {
                                    finalApprove = "0";
                                    if(role.equals("me")){
                                        statusTV.setText("Sedang Diproses");
                                        verifPart.setVisibility(View.GONE);
                                    } else if(role.equals("approval")){
                                        statusTV.setText("Menunggu Verifikasi");
                                        verifPart.setVisibility(View.VISIBLE);
                                        verifBTN.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onBackPressed();
                                            }
                                        });
                                    }
                                    statusTV.setTextColor(Color.parseColor("#D37E00"));
                                    statusCard.setBackgroundResource(R.drawable.shape_attantion);
                                    statusGif.setPadding(2,2,2,2);
                                    Glide.with(getApplicationContext())
                                            .load(R.drawable.process_ic)
                                            .into(statusGif);
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

                                if (urutan_st.equals("1")){
                                    stRincian1.setVisibility(View.VISIBLE);
                                    stRincian2.setVisibility(View.GONE);
                                    stRincian3.setVisibility(View.GONE);
                                    stRincian4.setVisibility(View.GONE);
                                    stRincian5.setVisibility(View.GONE);
                                    stRincian6.setVisibility(View.GONE);
                                } else if (urutan_st.equals("2")){
                                    stRincian1.setVisibility(View.GONE);
                                    stRincian2.setVisibility(View.VISIBLE);
                                    stRincian3.setVisibility(View.GONE);
                                    stRincian4.setVisibility(View.GONE);
                                    stRincian5.setVisibility(View.GONE);
                                    stRincian6.setVisibility(View.GONE);
                                    if(finalApprove.equals("1")){
                                        sK21.setEnabled(false);
                                        sM21.setEnabled(false);
                                        sK22.setEnabled(false);
                                        sM22.setEnabled(false);
                                    } else {
                                        if(role.equals("approval")){
                                            sK21.setEnabled(true);
                                            sM21.setEnabled(true);
                                            sK22.setEnabled(true);
                                            sM22.setEnabled(true);
                                        } else {
                                            sK21.setEnabled(false);
                                            sM21.setEnabled(false);
                                            sK22.setEnabled(false);
                                            sM22.setEnabled(false);
                                        }
                                    }
                                } else if (urutan_st.equals("3")){
                                    stRincian1.setVisibility(View.GONE);
                                    stRincian2.setVisibility(View.GONE);
                                    stRincian3.setVisibility(View.VISIBLE);
                                    stRincian4.setVisibility(View.GONE);
                                    stRincian5.setVisibility(View.GONE);
                                    stRincian6.setVisibility(View.GONE);
                                    if(finalApprove.equals("1")){
                                        sK31.setEnabled(false);
                                        sM31.setEnabled(false);
                                        sK32.setEnabled(false);
                                        sM32.setEnabled(false);

                                        ket31.setEnabled(false);
                                        ket32.setEnabled(false);
                                    } else {
                                        if(role.equals("approval")){
                                            sK31.setEnabled(true);
                                            sM31.setEnabled(true);
                                            sK32.setEnabled(true);
                                            sM32.setEnabled(true);

                                            ket31.setEnabled(true);
                                            ket32.setEnabled(true);
                                        } else {
                                            sK31.setEnabled(false);
                                            sM31.setEnabled(false);
                                            sK32.setEnabled(false);
                                            sM32.setEnabled(false);

                                            ket31.setEnabled(false);
                                            ket32.setEnabled(false);
                                        }
                                    }
                                } else if (urutan_st.equals("4")){
                                    stRincian1.setVisibility(View.GONE);
                                    stRincian2.setVisibility(View.GONE);
                                    stRincian3.setVisibility(View.GONE);
                                    stRincian4.setVisibility(View.VISIBLE);
                                    stRincian5.setVisibility(View.GONE);
                                    stRincian6.setVisibility(View.GONE);
                                } else if (urutan_st.equals("5")){
                                    stRincian1.setVisibility(View.GONE);
                                    stRincian2.setVisibility(View.GONE);
                                    stRincian3.setVisibility(View.GONE);
                                    stRincian4.setVisibility(View.GONE);
                                    stRincian5.setVisibility(View.VISIBLE);
                                    stRincian6.setVisibility(View.GONE);
                                } else if (urutan_st.equals("6")){
                                    stRincian1.setVisibility(View.GONE);
                                    stRincian2.setVisibility(View.GONE);
                                    stRincian3.setVisibility(View.GONE);
                                    stRincian4.setVisibility(View.GONE);
                                    stRincian5.setVisibility(View.GONE);
                                    stRincian6.setVisibility(View.VISIBLE);
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