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
import com.gelora.absensi.adapter.AdapterDataAlpa;
import com.gelora.absensi.adapter.AdapterDataDetailSerahTerima;
import com.gelora.absensi.model.DataAlpa;
import com.gelora.absensi.model.DataDetailSerahTerima;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailDataSerahTerimaExitClearanceActivity extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;
    LinearLayout actionBar, backBTN, statusCard, lampiranBTN, verifBTN, verifPart, waitingApproval, doneApproval;
    LinearLayout stRincian1, stRincian2, stRincian3, stRincian4, stRincian5, stRincian6;
    TextView namaKaryawanTV, nikKaryawanTV, detailKaryawanTV, serahTerimaTV, statusTV, tglMasukTV, tglKeluarTV, lampiranTV;
    ImageView statusGif;
    String role, id_st, finalApprove;
    CheckBox sM11, sK11, sM12, sK12, sM13, sK13, sM14, sK14, sM15, sK15;
    CheckBox sM21, sK21, sM22, sK22;
    CheckBox sM31, sK31, sM32, sK32;
    CheckBox sM41, sK41;
    CheckBox sM51, sK51;
    CheckBox sM61, sK61, sM62, sK62, sM63, sK63;
    EditText ket11, ket12, ket13, ket14, ket15, ket21, ket22, ket31, ket32, ket41, ket51, ket61, ket62, ket63;

    RecyclerView serahTerimaRV;
    TextView tglVerifTV, verifikatorTV;
    private DataDetailSerahTerima[] dataDetailSerahTerimas;
    private AdapterDataDetailSerahTerima adapterDataDetailSerahTerima;

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

        waitingApproval = findViewById(R.id.waiting_approval);
        doneApproval = findViewById(R.id.done_approval);

        stRincian1 = findViewById(R.id.st_rincian_1);
        stRincian2 = findViewById(R.id.st_rincian_2);
        stRincian3 = findViewById(R.id.st_rincian_3);
        stRincian4 = findViewById(R.id.st_rincian_4);
        stRincian5 = findViewById(R.id.st_rincian_5);
        stRincian6 = findViewById(R.id.st_rincian_6);

        sM11 = findViewById(R.id.sm_1_1);
        sK11 = findViewById(R.id.sk_1_1);
        ket11 = findViewById(R.id.ket_1_1);

        sM12 = findViewById(R.id.sm_1_2);
        sK12 = findViewById(R.id.sk_1_2);
        ket12 = findViewById(R.id.ket_1_2);

        sM13 = findViewById(R.id.sm_1_3);
        sK13 = findViewById(R.id.sk_1_3);
        ket13 = findViewById(R.id.ket_1_3);

        sM14 = findViewById(R.id.sm_1_4);
        sK14 = findViewById(R.id.sk_1_4);
        ket14 = findViewById(R.id.ket_1_4);

        sM15 = findViewById(R.id.sm_1_5);
        sK15 = findViewById(R.id.sk_1_5);
        ket15 = findViewById(R.id.ket_1_5);

        sM21 = findViewById(R.id.sm_2_1);
        sK21 = findViewById(R.id.sk_2_1);
        ket21 = findViewById(R.id.ket_2_1);

        sM22 = findViewById(R.id.sm_2_2);
        sK22 = findViewById(R.id.sk_2_2);
        ket22 = findViewById(R.id.ket_2_2);

        sM31 = findViewById(R.id.sm_3_1);
        sK31 = findViewById(R.id.sk_3_1);
        ket31 = findViewById(R.id.ket_3_1);

        sM32 = findViewById(R.id.sm_3_2);
        sK32 = findViewById(R.id.sk_3_2);
        ket32 = findViewById(R.id.ket_3_2);

        sM41 = findViewById(R.id.sm_4_1);
        sK41 = findViewById(R.id.sk_4_1);
        ket41 = findViewById(R.id.ket_4_1);

        sM51 = findViewById(R.id.sm_5_1);
        sK51 = findViewById(R.id.sk_5_1);
        ket51 = findViewById(R.id.ket_5_1);

        sM61 = findViewById(R.id.sm_6_1);
        sK61 = findViewById(R.id.sk_6_1);
        ket61 = findViewById(R.id.ket_6_1);

        sM62 = findViewById(R.id.sm_6_2);
        sK62 = findViewById(R.id.sk_6_2);
        ket62 = findViewById(R.id.ket_6_2);

        sM63 = findViewById(R.id.sm_6_3);
        sK63 = findViewById(R.id.sk_6_3);
        ket63 = findViewById(R.id.ket_6_3);

        serahTerimaRV = findViewById(R.id.serah_terima_rv);
        tglVerifTV = findViewById(R.id.tgl_verifikasi_tv);
        verifikatorTV = findViewById(R.id.verifikator_tv);
        serahTerimaRV.setLayoutManager(new LinearLayoutManager(this));
        serahTerimaRV.setHasFixedSize(true);
        serahTerimaRV.setNestedScrollingEnabled(false);
        serahTerimaRV.setItemAnimator(new DefaultItemAnimator());

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

                                if(role.equals("approval")){
                                    if(finalApprove.equals("1")){
                                        doneApproval.setVisibility(View.VISIBLE);
                                        waitingApproval.setVisibility(View.GONE);
                                        getApprovedData(id_st);
                                        tglVerifTV.setText("Tanggal verifikasi : "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                        verifikatorTV.setText("Oleh : "+nama_approval);
                                    } else {
                                        doneApproval.setVisibility(View.GONE);
                                        waitingApproval.setVisibility(View.VISIBLE);
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
                                        } else if (urutan_st.equals("3")){
                                            stRincian1.setVisibility(View.GONE);
                                            stRincian2.setVisibility(View.GONE);
                                            stRincian3.setVisibility(View.VISIBLE);
                                            stRincian4.setVisibility(View.GONE);
                                            stRincian5.setVisibility(View.GONE);
                                            stRincian6.setVisibility(View.GONE);
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
                                } else if(role.equals("me")){
                                    if(finalApprove.equals("1")){
                                        doneApproval.setVisibility(View.VISIBLE);
                                        waitingApproval.setVisibility(View.GONE);
                                        getApprovedData(id_st);
                                        tglVerifTV.setText("Tanggal verifikasi : "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                        verifikatorTV.setText("Oleh : "+nama_approval);
                                    } else {
                                        doneApproval.setVisibility(View.GONE);
                                        waitingApproval.setVisibility(View.VISIBLE);
                                        if (urutan_st.equals("1")){
                                            stRincian1.setVisibility(View.VISIBLE);
                                            stRincian2.setVisibility(View.GONE);
                                            stRincian3.setVisibility(View.GONE);
                                            stRincian4.setVisibility(View.GONE);
                                            stRincian5.setVisibility(View.GONE);
                                            stRincian6.setVisibility(View.GONE);

                                            sM11.setEnabled(false);
                                            sK11.setEnabled(false);
                                            ket11.setEnabled(false);
                                            ket11.setHint("Belum tersedia...");

                                            sM12.setEnabled(false);
                                            sK12.setEnabled(false);
                                            ket12.setEnabled(false);
                                            ket12.setHint("Belum tersedia...");

                                            sM13.setEnabled(false);
                                            sK13.setEnabled(false);
                                            ket13.setEnabled(false);
                                            ket13.setHint("Belum tersedia...");

                                            sM14.setEnabled(false);
                                            sK14.setEnabled(false);
                                            ket14.setEnabled(false);
                                            ket14.setHint("Belum tersedia...");

                                            sM15.setEnabled(false);
                                            sK15.setEnabled(false);
                                            ket15.setEnabled(false);
                                            ket15.setHint("Belum tersedia...");

                                        } else if (urutan_st.equals("2")){
                                            stRincian1.setVisibility(View.GONE);
                                            stRincian2.setVisibility(View.VISIBLE);
                                            stRincian3.setVisibility(View.GONE);
                                            stRincian4.setVisibility(View.GONE);
                                            stRincian5.setVisibility(View.GONE);
                                            stRincian6.setVisibility(View.GONE);

                                            sM21.setEnabled(false);
                                            sK21.setEnabled(false);
                                            ket21.setEnabled(false);
                                            ket21.setHint("Belum tersedia...");

                                            sM22.setEnabled(false);
                                            sK22.setEnabled(false);
                                            ket22.setEnabled(false);
                                            ket22.setHint("Belum tersedia...");

                                        } else if (urutan_st.equals("3")){
                                            stRincian1.setVisibility(View.GONE);
                                            stRincian2.setVisibility(View.GONE);
                                            stRincian3.setVisibility(View.VISIBLE);
                                            stRincian4.setVisibility(View.GONE);
                                            stRincian5.setVisibility(View.GONE);
                                            stRincian6.setVisibility(View.GONE);

                                            sM31.setEnabled(false);
                                            sK31.setEnabled(false);
                                            ket31.setEnabled(false);
                                            ket31.setHint("Belum tersedia...");

                                            sM32.setEnabled(false);
                                            sK32.setEnabled(false);
                                            ket32.setEnabled(false);
                                            ket32.setHint("Belum tersedia...");

                                        } else if (urutan_st.equals("4")){
                                            stRincian1.setVisibility(View.GONE);
                                            stRincian2.setVisibility(View.GONE);
                                            stRincian3.setVisibility(View.GONE);
                                            stRincian4.setVisibility(View.VISIBLE);
                                            stRincian5.setVisibility(View.GONE);
                                            stRincian6.setVisibility(View.GONE);

                                            sM41.setEnabled(false);
                                            sK41.setEnabled(false);
                                            ket41.setEnabled(false);
                                            ket41.setHint("Belum tersedia...");

                                        } else if (urutan_st.equals("5")){
                                            stRincian1.setVisibility(View.GONE);
                                            stRincian2.setVisibility(View.GONE);
                                            stRincian3.setVisibility(View.GONE);
                                            stRincian4.setVisibility(View.GONE);
                                            stRincian5.setVisibility(View.VISIBLE);
                                            stRincian6.setVisibility(View.GONE);

                                            sM51.setEnabled(false);
                                            sK51.setEnabled(false);
                                            ket51.setEnabled(false);
                                            ket51.setHint("Belum tersedia...");

                                        } else if (urutan_st.equals("6")){
                                            stRincian1.setVisibility(View.GONE);
                                            stRincian2.setVisibility(View.GONE);
                                            stRincian3.setVisibility(View.GONE);
                                            stRincian4.setVisibility(View.GONE);
                                            stRincian5.setVisibility(View.GONE);
                                            stRincian6.setVisibility(View.VISIBLE);

                                            sM61.setEnabled(false);
                                            sK61.setEnabled(false);
                                            ket61.setEnabled(false);
                                            ket61.setHint("Belum tersedia...");

                                            sM62.setEnabled(false);
                                            sK62.setEnabled(false);
                                            ket62.setEnabled(false);
                                            ket62.setHint("Belum tersedia...");

                                            sM63.setEnabled(false);
                                            sK63.setEnabled(false);
                                            ket63.setEnabled(false);
                                            ket63.setHint("Belum tersedia...");

                                        }

                                    }

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
                        connectionFailed();
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

    private void getApprovedData(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_ec_approved_data";
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
                                String data_st = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                dataDetailSerahTerimas = gson.fromJson(data_st, DataDetailSerahTerima[].class);
                                adapterDataDetailSerahTerima = new AdapterDataDetailSerahTerima(dataDetailSerahTerimas,DetailDataSerahTerimaExitClearanceActivity.this);
                                serahTerimaRV.setAdapter(adapterDataDetailSerahTerima);
                            } else {
                                connectionFailed();
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