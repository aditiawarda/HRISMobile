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
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailDataExitClearanceActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN;
    SwipeRefreshLayout refreshLayout;
    TextView namaKaryawanTV, nikKaryawanTV, detailKaryawanTV, tanggalMasukTV, tanggalKeluarTV, alasanTV, saranTV;
    TextView st1FileTV, statusTV1, tglApprove1TV;
    LinearLayout st1UploadIcBTN;
    TextView st2FileTV, statusTV2, tglApprove2TV;
    LinearLayout st2UploadIcBTN;
    TextView st3FileTV, statusTV3, tglApprove3TV;
    LinearLayout st3UploadIcBTN;
    TextView st4FileTV, statusTV4, tglApprove4TV;
    LinearLayout st4UploadIcBTN;
    TextView st5FileTV, statusTV5, tglApprove5TV;
    LinearLayout st5UploadIcBTN;
    TextView st6FileTV, statusTV6, tglApprove6TV;
    LinearLayout st6UploadIcBTN;
    SharedPrefManager sharedPrefManager;
    String idData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        detailKaryawanTV = findViewById(R.id.detail_karyawan_tv);
        tanggalMasukTV = findViewById(R.id.tanggal_masuk_tv);
        tanggalKeluarTV = findViewById(R.id.tanggal_keluar_tv);
        alasanTV = findViewById(R.id.alasan_tv);
        saranTV = findViewById(R.id.saran_tv);

        st1FileTV = findViewById(R.id.st_1_file_tv);
        st1UploadIcBTN = findViewById(R.id.st_1_upload_ic_btn);
        statusTV1 = findViewById(R.id.status_tv_1);
        tglApprove1TV = findViewById(R.id.tgl_approve_tv_1);

        st2FileTV = findViewById(R.id.st_2_file_tv);
        st2UploadIcBTN = findViewById(R.id.st_2_upload_ic_btn);
        statusTV2 = findViewById(R.id.status_tv_2);
        tglApprove2TV = findViewById(R.id.tgl_approve_tv_2);

        st3FileTV = findViewById(R.id.st_3_file_tv);
        st3UploadIcBTN = findViewById(R.id.st_3_upload_ic_btn);
        statusTV3 = findViewById(R.id.status_tv_3);
        tglApprove3TV = findViewById(R.id.tgl_approve_tv_3);

        st4FileTV = findViewById(R.id.st_4_file_tv);
        st4UploadIcBTN = findViewById(R.id.st_4_upload_ic_btn);
        statusTV4 = findViewById(R.id.status_tv_4);
        tglApprove4TV = findViewById(R.id.tgl_approve_tv_4);

        st5FileTV = findViewById(R.id.st_5_file_tv);
        st5UploadIcBTN = findViewById(R.id.st_5_upload_ic_btn);
        statusTV5 = findViewById(R.id.status_tv_5);
        tglApprove5TV = findViewById(R.id.tgl_approve_tv_5);

        st6FileTV = findViewById(R.id.st_6_file_tv);
        st6UploadIcBTN = findViewById(R.id.st_6_upload_ic_btn);
        statusTV6 = findViewById(R.id.status_tv_6);
        tglApprove6TV = findViewById(R.id.tgl_approve_tv_6);

        idData = getIntent().getExtras().getString("id_record");

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
                        getData();
                    }
                }, 500);
            }
        });

        backBTN.setOnClickListener(v -> onBackPressed());

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_my_exit_clearance";
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
                            if (status.equals("Available")){
                                JSONObject detail = data.getJSONObject("data");
                                String nama = detail.getString("nama");
                                String NIK = detail.getString("NIK");
                                String jabatan = detail.getString("jabatan");
                                String bagian = detail.getString("bagian");
                                String departemen = detail.getString("departemen");
                                String tanggal_masuk = detail.getString("tanggal_masuk");
                                String tanggal_keluar = detail.getString("tanggal_keluar");
                                String alasan_keluar = detail.getString("alasan_keluar");
                                String saran = detail.getString("saran");

                                namaKaryawanTV.setText(nama.toUpperCase());
                                nikKaryawanTV.setText(NIK);
                                detailKaryawanTV.setText(jabatan+" | "+bagian+" | "+departemen);

                                String input_date_masuk = tanggal_masuk;
                                String dayDateMasuk = input_date_masuk.substring(8,10);
                                String yearDateMasuk = input_date_masuk.substring(0,4);
                                String bulanValueMasuk = input_date_masuk.substring(5,7);
                                String bulanNameMasuk;

                                switch (bulanValueMasuk) {
                                    case "01":
                                        bulanNameMasuk = "Januari";
                                        break;
                                    case "02":
                                        bulanNameMasuk = "Februari";
                                        break;
                                    case "03":
                                        bulanNameMasuk = "Maret";
                                        break;
                                    case "04":
                                        bulanNameMasuk = "April";
                                        break;
                                    case "05":
                                        bulanNameMasuk = "Mei";
                                        break;
                                    case "06":
                                        bulanNameMasuk = "Juni";
                                        break;
                                    case "07":
                                        bulanNameMasuk = "Juli";
                                        break;
                                    case "08":
                                        bulanNameMasuk = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameMasuk = "September";
                                        break;
                                    case "10":
                                        bulanNameMasuk = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameMasuk = "November";
                                        break;
                                    case "12":
                                        bulanNameMasuk = "Desember";
                                        break;
                                    default:
                                        bulanNameMasuk = "Not found";
                                        break;
                                }

                                tanggalMasukTV.setText(Integer.parseInt(dayDateMasuk) +" "+bulanNameMasuk+" "+yearDateMasuk);

                                String input_date_keluar = tanggal_keluar;
                                String dayDateKeluar = input_date_keluar.substring(8,10);
                                String yearDateKeluar = input_date_keluar.substring(0,4);
                                String bulanValueKeluar = input_date_keluar.substring(5,7);
                                String bulanNameKeluar;

                                switch (bulanValueKeluar) {
                                    case "01":
                                        bulanNameKeluar = "Januari";
                                        break;
                                    case "02":
                                        bulanNameKeluar = "Februari";
                                        break;
                                    case "03":
                                        bulanNameKeluar = "Maret";
                                        break;
                                    case "04":
                                        bulanNameKeluar = "April";
                                        break;
                                    case "05":
                                        bulanNameKeluar = "Mei";
                                        break;
                                    case "06":
                                        bulanNameKeluar = "Juni";
                                        break;
                                    case "07":
                                        bulanNameKeluar = "Juli";
                                        break;
                                    case "08":
                                        bulanNameKeluar = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameKeluar = "September";
                                        break;
                                    case "10":
                                        bulanNameKeluar = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameKeluar = "November";
                                        break;
                                    case "12":
                                        bulanNameKeluar = "Desember";
                                        break;
                                    default:
                                        bulanNameKeluar = "Not found";
                                        break;
                                }

                                tanggalKeluarTV.setText(Integer.parseInt(dayDateKeluar) +" "+bulanNameKeluar+" "+yearDateKeluar);

                                alasanTV.setText(alasan_keluar);
                                saranTV.setText(saran);

                                JSONArray serah_terima = data.getJSONArray("serah_terima");
                                for (int i = 0; i < serah_terima.length(); i++) {
                                    JSONObject data_serah_terima = serah_terima.getJSONObject(i);
                                    String urutan_st = data_serah_terima.getString("urutan_st");
                                    String lampiran = data_serah_terima.getString("lampiran");
                                    String approval = data_serah_terima.getString("approval");
                                    String tgl_approval = data_serah_terima.getString("tgl_approval");

                                    if(urutan_st.equals("1")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st1FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV1.setText("Disetujui");
                                                statusTV1.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV1.setText("Diproses");
                                                statusTV1.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setText("Menunggu verifikasi");
                                            }
                                            st1UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st1FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV1.setText("Disetujui");
                                                statusTV1.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove1TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV1.setText("Diproses");
                                                statusTV1.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove1TV.setText("Menunggu verifikasi");
                                            }
                                            st1UploadIcBTN.setVisibility(View.GONE);
                                        }
                                    } else if(urutan_st.equals("2")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st2FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV2.setText("Disetujui");
                                                statusTV2.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV2.setText("Diproses");
                                                statusTV2.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setText("Menunggu verifikasi");
                                            }
                                            st2UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st2FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV2.setText("Disetujui");
                                                statusTV2.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove2TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV2.setText("Diproses");
                                                statusTV2.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove2TV.setText("Menunggu verifikasi");
                                            }
                                            st2UploadIcBTN.setVisibility(View.GONE);
                                        }
                                    } else if(urutan_st.equals("3")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st3FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV3.setText("Disetujui");
                                                statusTV3.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV3.setText("Diproses");
                                                statusTV3.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setText("Menunggu verifikasi");
                                            }
                                            st3UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st3FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV3.setText("Disetujui");
                                                statusTV3.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove3TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV3.setText("Diproses");
                                                statusTV3.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove3TV.setText("Menunggu verifikasi");
                                            }
                                            st3UploadIcBTN.setVisibility(View.GONE);
                                        }
                                    } else if(urutan_st.equals("4")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st4FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV4.setText("Disetujui");
                                                statusTV4.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV4.setText("Diproses");
                                                statusTV4.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setText("Menunggu verifikasi");
                                            }
                                            st4UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st4FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV4.setText("Disetujui");
                                                statusTV4.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove4TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV4.setText("Diproses");
                                                statusTV4.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove4TV.setText("Menunggu verifikasi");
                                            }
                                            st4UploadIcBTN.setVisibility(View.GONE);
                                        }
                                    } else if(urutan_st.equals("5")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st5FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV5.setText("Disetujui");
                                                statusTV5.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV5.setText("Diproses");
                                                statusTV5.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setText("Menunggu verifikasi");
                                            }
                                            st5UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st5FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV5.setText("Disetujui");
                                                statusTV5.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove5TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV5.setText("Diproses");
                                                statusTV5.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove5TV.setText("Menunggu verifikasi");
                                            }
                                            st5UploadIcBTN.setVisibility(View.GONE);
                                        }
                                    } else if(urutan_st.equals("6")){
                                        if(!lampiran.equals("null") && lampiran!=null && !lampiran.equals("")){
                                            st6FileTV.setText(lampiran);
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV6.setText("Disetujui");
                                                statusTV6.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV6.setText("Diproses");
                                                statusTV6.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setText("Menunggu verifikasi");
                                            }
                                            st6UploadIcBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(DetailDataExitClearanceActivity.this, PdfViewerActivity.class);
                                                    intent.putExtra("initialisasi", "detail");
                                                    intent.putExtra("kode_st", urutan_st);
                                                    intent.putExtra("uri", "https://geloraaksara.co.id/absen-online/upload/exit_clearance/"+lampiran);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            st6FileTV.setText("Lampiran tidak tersedia");
                                            if(!approval.equals("null") && approval!=null && !approval.equals("")){
                                                statusTV6.setText("Disetujui");
                                                statusTV6.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#279A2B"));
                                                tglApprove6TV.setText("Disetujui tanggal "+tgl_approval.substring(8,10)+"/"+tgl_approval.substring(5,7)+"/"+tgl_approval.substring(0,4));
                                            } else {
                                                statusTV6.setText("Diproses");
                                                statusTV6.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setTextColor(Color.parseColor("#D37E00"));
                                                tglApprove6TV.setText("Menunggu verifikasi");
                                            }
                                            st6UploadIcBTN.setVisibility(View.GONE);
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
                        //connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_record", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

}