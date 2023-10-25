package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.adapter.AdapterDataAlpa;
import com.gelora.absensi.adapter.AdapterDataDetailSerahTerima;
import com.gelora.absensi.kalert.KAlertDialog;
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
    String role, id_st, finalApprove, urutanST, idCore;
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
    KAlertDialog pDialog;
    private int i = -1;

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

        verifBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailDataSerahTerimaExitClearanceActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Verifikasi?")
                        .setContentText("Yakin untuk diverifikasi sekarang?")
                        .setCancelText("TIDAK")
                        .setConfirmText("   YA   ")
                        .showCancelButton(true)
                        .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();

                                pDialog = new KAlertDialog(DetailDataSerahTerimaExitClearanceActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataSerahTerimaExitClearanceActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataSerahTerimaExitClearanceActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataSerahTerimaExitClearanceActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataSerahTerimaExitClearanceActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataSerahTerimaExitClearanceActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailDataSerahTerimaExitClearanceActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        checkSignature();
                                    }
                                }.start();

                            }
                        })
                        .show();

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
                                urutanST = urutan_st;
                                idCore = id_core;

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

    private void checkSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cek_ttd_digital";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Available")){
                                String signature = data.getString("data");
                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature;
                                updateApproved();
                            } else {
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Anda belum mengisi tanda tangan digital. Harap isi terlebih dahulu")
                                        .setCancelText(" BATAL ")
                                        .setConfirmText("LANJUT")
                                        .showCancelButton(true)
                                        .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                Intent intent = new Intent(DetailDataSerahTerimaExitClearanceActivity.this, DigitalSignatureActivity.class);
                                                intent.putExtra("kode", "form");
                                                startActivity(intent);
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.WARNING_TYPE);
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void updateApproved(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/approval_st_exit_clearance";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                getData(id_st);
                                pDialog.setTitleText("Berhasil Disetujui")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                pDialog.setTitleText("Gagal Disetujui")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                params.put("urutan_st", urutanST);
                params.put("id_core", idCore);
                params.put("id_st", id_st);
                params.put("NIK", sharedPrefManager.getSpNik());

                if(urutanST.equals("1")){
                    String sm11, sk11;
                    if(sM11.isChecked()){
                        sm11 = "1";
                    } else {
                        sm11 = "0";
                    }

                    if(sK11.isChecked()){
                        sk11 = "1";
                    } else {
                        sk11 = "0";
                    }

                    String valueString11 = "Softcopy"+"-"+sm11+"-"+sk11+"-"+ket11.getText().toString();
                    params.put("value_string_11", valueString11);

                    String sm12, sk12;
                    if(sM12.isChecked()){
                        sm12 = "1";
                    } else {
                        sm12 = "0";
                    }

                    if(sK12.isChecked()){
                        sk12 = "1";
                    } else {
                        sk12 = "0";
                    }

                    String valueString12 = "Laporan"+"-"+sm12+"-"+sk12+"-"+ket12.getText().toString();
                    params.put("value_string_12", valueString12);

                    String sm13, sk13;
                    if(sM13.isChecked()){
                        sm13 = "1";
                    } else {
                        sm13 = "0";
                    }

                    if(sK13.isChecked()){
                        sk13 = "1";
                    } else {
                        sk13 = "0";
                    }

                    String valueString13 = "Seragam Kerja"+"-"+sm13+"-"+sk13+"-"+ket13.getText().toString();
                    params.put("value_string_13", valueString13);

                    String sm14, sk14;
                    if(sM14.isChecked()){
                        sm14 = "1";
                    } else {
                        sm14 = "0";
                    }

                    if(sK14.isChecked()){
                        sk14 = "1";
                    } else {
                        sk14 = "0";
                    }

                    String valueString14 = "Safety Shoes"+"-"+sm14+"-"+sk14+"-"+ket14.getText().toString();
                    params.put("value_string_14", valueString14);

                    String sm15, sk15;
                    if(sM15.isChecked()){
                        sm15 = "1";
                    } else {
                        sm15 = "0";
                    }

                    if(sK15.isChecked()){
                        sk15 = "1";
                    } else {
                        sk15 = "0";
                    }

                    String valueString15 = "Kunci Loker"+"-"+sm15+"-"+sk15+"-"+ket15.getText().toString();
                    params.put("value_string_15", valueString15);

                } else if(urutanST.equals("2")){
                    String sm21, sk21;
                    if(sM21.isChecked()){
                        sm21 = "1";
                    } else {
                        sm21 = "0";
                    }

                    if(sK21.isChecked()){
                        sk21 = "1";
                    } else {
                        sk21 = "0";
                    }

                    String valueString21 = "Mobil"+"-"+sm21+"-"+sk21+"-"+ket21.getText().toString();
                    params.put("value_string_21", valueString21);

                    String sm22, sk22;
                    if(sM22.isChecked()){
                        sm22 = "1";
                    } else {
                        sm22 = "0";
                    }

                    if(sK22.isChecked()){
                        sk22 = "1";
                    } else {
                        sk22 = "0";
                    }

                    String valueString22 = "Motor"+"-"+sm22+"-"+sk22+"-"+ket22.getText().toString();
                    params.put("value_string_22", valueString22);

                } else if(urutanST.equals("3")){
                    String sm31, sk31;
                    if(sM31.isChecked()){
                        sm31 = "1";
                    } else {
                        sm31 = "0";
                    }

                    if(sK31.isChecked()){
                        sk31 = "1";
                    } else {
                        sk31 = "0";
                    }

                    String valueString31 = "Notebook"+"-"+sm31+"-"+sk31+"-"+ket31.getText().toString();
                    params.put("value_string_31", valueString31);

                    String sm32, sk32;
                    if(sM32.isChecked()){
                        sm32 = "1";
                    } else {
                        sm32 = "0";
                    }

                    if(sK32.isChecked()){
                        sk32 = "1";
                    } else {
                        sk32 = "0";
                    }

                    String valueString32 = "Email"+"-"+sm32+"-"+sk32+"-"+ket32.getText().toString();
                    params.put("value_string_32", valueString32);

                } else if(urutanST.equals("4")){
                    String sm41, sk41;
                    if(sM41.isChecked()){
                        sm41 = "1";
                    } else {
                        sm41 = "0";
                    }

                    if(sK41.isChecked()){
                        sk41 = "1";
                    } else {
                        sk41 = "0";
                    }

                    String valueString41 = "Pinjaman Perusahaan"+"-"+sm41+"-"+sk41+"-"+ket41.getText().toString();
                    params.put("value_string_41", valueString41);

                } else if(urutanST.equals("5")){
                    String sm51, sk51;
                    if(sM51.isChecked()){
                        sm51 = "1";
                    } else {
                        sm51 = "0";
                    }

                    if(sK51.isChecked()){
                        sk51 = "1";
                    } else {
                        sk51 = "0";
                    }

                    String valueString51 = "Pinjaman Koperasi"+"-"+sm51+"-"+sk51+"-"+ket51.getText().toString();
                    params.put("value_string_51", valueString51);

                } else if(urutanST.equals("6")){
                    String sm61, sk61;
                    if(sM61.isChecked()){
                        sm61 = "1";
                    } else {
                        sm61 = "0";
                    }

                    if(sK61.isChecked()){
                        sk61 = "1";
                    } else {
                        sk61 = "0";
                    }

                    String valueString61 = "Ijazah"+"-"+sm61+"-"+sk61+"-"+ket61.getText().toString();
                    params.put("value_string_61", valueString61);

                    String sm62, sk62;
                    if(sM62.isChecked()){
                        sm62 = "1";
                    } else {
                        sm62 = "0";
                    }

                    if(sK62.isChecked()){
                        sk62 = "1";
                    } else {
                        sk62 = "0";
                    }

                    String valueString62 = "ID Card"+"-"+sm62+"-"+sk62+"-"+ket62.getText().toString();
                    params.put("value_string_62", valueString62);

                    String sm63, sk63;
                    if(sM63.isChecked()){
                        sm63 = "1";
                    } else {
                        sm63 = "0";
                    }

                    if(sK63.isChecked()){
                        sk63 = "1";
                    } else {
                        sk63 = "0";
                    }

                    String valueString63 = "Ikatan Dinas"+"-"+sm63+"-"+sk63+"-"+ket63.getText().toString();
                    params.put("value_string_63", valueString63);

                }

                return params;
            }
        };

        requestQueue.add(postRequest);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

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