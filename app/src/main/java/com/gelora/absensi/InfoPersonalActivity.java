package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoPersonalActivity extends AppCompatActivity {

    LinearLayout actionBar, ubahBTN, emptyWarningBTN, backBTN, warningEmail, warningGender, warningTempatLahir, warningTanggalLahir, warningHandphone, warningStatusPernikahan, warningAgama, warningAlamatKTP, warningAlamatDomisili;
    TextView namaTV, emailTV, genderTV, tempatLahirTV, tanggalLAhirTV, hanphoneTV, statusPernikahanTV, agamaTV, alamatKTPTV, alamatDomisiliTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_personal);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        namaTV = findViewById(R.id.nama);
        emailTV = findViewById(R.id.email);
        genderTV = findViewById(R.id.jenis_kelamin);
        tempatLahirTV = findViewById(R.id.tempat_lahir);
        tanggalLAhirTV = findViewById(R.id.tanggal_lahir);
        hanphoneTV = findViewById(R.id.handphone);
        statusPernikahanTV = findViewById(R.id.status_pernikahan);
        agamaTV = findViewById(R.id.agama);
        alamatKTPTV = findViewById(R.id.alamat_ktp);
        alamatDomisiliTV = findViewById(R.id.alamat_domisili);
        warningEmail = findViewById(R.id.warning_email);
        warningGender = findViewById(R.id.warning_gender);
        warningTempatLahir = findViewById(R.id.warning_tempat_lahir);
        warningTanggalLahir = findViewById(R.id.warning_tanggal_lahir);
        warningHandphone = findViewById(R.id.warning_handphone);
        warningStatusPernikahan = findViewById(R.id.warning_status_pernikahan);
        warningAgama = findViewById(R.id.warning_agama);
        warningAlamatKTP = findViewById(R.id.warning_alamat_ktp);
        warningAlamatDomisili = findViewById(R.id.warning_alamat_domisili);
        emptyWarningBTN = findViewById(R.id.empty_warning_btn);
        ubahBTN = findViewById(R.id.ubah_btn);
        actionBar = findViewById(R.id.action_bar);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 1000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        emptyWarningBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoPersonalActivity.this, FormInfoPersonalActivity.class);
                startActivity(intent);
            }
        });

        ubahBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoPersonalActivity.this, FormInfoPersonalActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_info_personal";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String button_update = data.getString("button_update");
                                JSONObject dataArray = data.getJSONObject("data");
                                String nama = dataArray.getString("nama");
                                String email = dataArray.getString("email");
                                String jenis_kelamin = dataArray.getString("jenis_kelamin");
                                String tempat_lahir = dataArray.getString("tempat_lahir");
                                String tanggal_lahir = dataArray.getString("tanggal_lahir");
                                String handphone = dataArray.getString("handphone");
                                String status_pernikahan = dataArray.getString("status_pernikahan");
                                String agama = dataArray.getString("agama");
                                String alamat_ktp = dataArray.getString("alamat_ktp");
                                String alamat_domisili = dataArray.getString("alamat_domisili");

                                namaTV.setText(nama);

                                if(
                                   (!email.equals("")            &&!email.equals("null"))            &&
                                   (!jenis_kelamin.equals("")    &&!jenis_kelamin.equals("null"))    &&
                                   (!tempat_lahir.equals("")     &&!tempat_lahir.equals("null"))     &&
                                   (!tanggal_lahir.equals("")    &&!tanggal_lahir.equals("null"))    &&
                                   (!handphone.equals("")        &&!handphone.equals("null"))        &&
                                   (!status_pernikahan.equals("")&&!status_pernikahan.equals("null"))&&
                                   (!agama.equals("")            &&!agama.equals("null"))            &&
                                   (!alamat_ktp.equals("")       &&!alamat_ktp.equals("null"))       &&
                                   (!alamat_domisili.equals("")  &&!alamat_domisili.equals("null"))
                                ){
                                    if(button_update.equals("1")){
                                        ubahBTN.setVisibility(View.VISIBLE);
                                    } else {
                                        ubahBTN.setVisibility(View.GONE);
                                    }
                                } else {
                                    ubahBTN.setVisibility(View.GONE);
                                }

                                if(email.equals("")||email.equals("null")){
                                    emailTV.setText("-");
                                    warningEmail.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    emailTV.setText(email);
                                    warningEmail.setVisibility(View.GONE);
                                }

                                if(jenis_kelamin.equals("")||jenis_kelamin.equals("null")){
                                    genderTV.setText("-");
                                    warningGender.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    genderTV.setText(jenis_kelamin);
                                    warningGender.setVisibility(View.GONE);
                                }

                                if(tempat_lahir.equals("")||tempat_lahir.equals("null")){
                                    tempatLahirTV.setText("-");
                                    warningTempatLahir.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    tempatLahirTV.setText(tempat_lahir);
                                    warningTempatLahir.setVisibility(View.GONE);
                                }

                                if(tanggal_lahir.equals("")||tanggal_lahir.equals("null")){
                                    tanggalLAhirTV.setText("-");
                                    warningTanggalLahir.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    warningTanggalLahir.setVisibility(View.GONE);
                                    String dayDate = tanggal_lahir.substring(8,10);
                                    String yearDate = tanggal_lahir.substring(0,4);
                                    String bulanValue = tanggal_lahir.substring(5,7);
                                    String bulanName;

                                    switch (bulanValue) {
                                        case "01":
                                            bulanName = "Januari";
                                            break;
                                        case "02":
                                            bulanName = "Februari";
                                            break;
                                        case "03":
                                            bulanName = "Maret";
                                            break;
                                        case "04":
                                            bulanName = "April";
                                            break;
                                        case "05":
                                            bulanName = "Mei";
                                            break;
                                        case "06":
                                            bulanName = "Juni";
                                            break;
                                        case "07":
                                            bulanName = "Juli";
                                            break;
                                        case "08":
                                            bulanName = "Agustus";
                                            break;
                                        case "09":
                                            bulanName = "September";
                                            break;
                                        case "10":
                                            bulanName = "Oktober";
                                            break;
                                        case "11":
                                            bulanName = "November";
                                            break;
                                        case "12":
                                            bulanName = "Desember";
                                            break;
                                        default:
                                            bulanName = "Not found!";
                                            break;
                                    }

                                    tanggalLAhirTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                                }

                                if(handphone.equals("")||handphone.equals("null")){
                                    hanphoneTV.setText("-");
                                    warningHandphone.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    hanphoneTV.setText(handphone);
                                    warningHandphone.setVisibility(View.GONE);
                                }

                                if(status_pernikahan.equals("")||status_pernikahan.equals("null")){
                                    statusPernikahanTV.setText("-");
                                    warningStatusPernikahan.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    statusPernikahanTV.setText(status_pernikahan);
                                    warningStatusPernikahan.setVisibility(View.GONE);
                                }

                                if(agama.equals("")||agama.equals("null")){
                                    agamaTV.setText("-");
                                    warningAgama.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    agamaTV.setText(agama);
                                    warningAgama.setVisibility(View.GONE);
                                }

                                if(alamat_ktp.equals("")||alamat_ktp.equals("null")){
                                    alamatKTPTV.setText("-");
                                    warningAlamatKTP.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    alamatKTPTV.setText(alamat_ktp.replaceAll("\\s+$", ""));
                                    warningAlamatKTP.setVisibility(View.GONE);
                                }

                                if(alamat_domisili.equals("")||alamat_domisili.equals("null")){
                                    alamatDomisiliTV.setText("-");
                                    warningAlamatDomisili.setVisibility(View.VISIBLE);
                                    emptyWarningBTN.setVisibility(View.VISIBLE);
                                } else {
                                    alamatDomisiliTV.setText(alamat_domisili.replaceAll("\\s+$", ""));
                                    warningAlamatDomisili.setVisibility(View.GONE);
                                }

                            } else {
                                new KAlertDialog(InfoPersonalActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan saat mengakses data")
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
                params.put("nik", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(InfoPersonalActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        emptyWarningBTN.setVisibility(View.GONE);
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}