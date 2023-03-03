package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class FormInfoPersonalActivity extends AppCompatActivity {

    LinearLayout backBTN;
    TextView namaTV, genderPilihTV, tanggalLahirPilihTV, statusPernikahanPilihTV;
    EditText emailED, tempatLahirED, noHanphoneED, agamaED, alamatKTPED, alamatDomisiliED;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    Switch emailSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_personal);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        namaTV = findViewById(R.id.nama_tv);
        emailED = findViewById(R.id.email_ed);
        genderPilihTV = findViewById(R.id.gender_pilih_tv);
        tempatLahirED = findViewById(R.id.tempat_lahir_ed);
        tanggalLahirPilihTV = findViewById(R.id.tanggal_lahir_pilih_tv);
        statusPernikahanPilihTV = findViewById(R.id.status_pernikahan_pilih_tv);
        noHanphoneED = findViewById(R.id.no_hanphone_ed);
        agamaED = findViewById(R.id.agama_ed);
        alamatKTPED = findViewById(R.id.alamat_ktp_ed);
        alamatDomisiliED = findViewById(R.id.alamat_domisili_ed);
        emailSwitch = findViewById(R.id.alamat_domisili_switch);

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
                }, 1000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        emailSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailSwitch.isChecked()){
                    alamatDomisiliED.setVisibility(View.GONE);
                } else {
                    alamatDomisiliED.setVisibility(View.VISIBLE);
                }
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_info_personal";
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

                                namaTV.setText(nama.toUpperCase());

                                if(email.equals("")||email.equals("null")){
                                    emailED.setText("");
                                } else {
                                    emailED.setText(email);
                                }

                                if(jenis_kelamin.equals("")||jenis_kelamin.equals("null")){
                                    genderPilihTV.setText("");
                                } else {
                                    genderPilihTV.setText(jenis_kelamin);
                                }

                                if(tempat_lahir.equals("")||tempat_lahir.equals("null")){
                                    tempatLahirED.setText("");
                                } else {
                                    tempatLahirED.setText(tempat_lahir);
                                }

                                if(tanggal_lahir.equals("")||tanggal_lahir.equals("null")){
                                    tanggalLahirPilihTV.setText("");
                                } else {
                                    String dayDate = tanggal_lahir.substring(8,10);
                                    String yearDate = tanggal_lahir.substring(0,4);;
                                    String bulanValue = tanggal_lahir.substring(5,7);
                                    String bulanName;

                                    switch (bulanValue) {
                                        case "01":
                                            bulanName = "Jan";
                                            break;
                                        case "02":
                                            bulanName = "Feb";
                                            break;
                                        case "03":
                                            bulanName = "Mar";
                                            break;
                                        case "04":
                                            bulanName = "Apr";
                                            break;
                                        case "05":
                                            bulanName = "Mei";
                                            break;
                                        case "06":
                                            bulanName = "Jun";
                                            break;
                                        case "07":
                                            bulanName = "Jul";
                                            break;
                                        case "08":
                                            bulanName = "Agu";
                                            break;
                                        case "09":
                                            bulanName = "Sep";
                                            break;
                                        case "10":
                                            bulanName = "Okt";
                                            break;
                                        case "11":
                                            bulanName = "Nov";
                                            break;
                                        case "12":
                                            bulanName = "Des";
                                            break;
                                        default:
                                            bulanName = "Not found!";
                                            break;
                                    }

                                    tanggalLahirPilihTV.setText(dayDate+" "+bulanName+" "+yearDate);

                                }

                                if(handphone.equals("")||handphone.equals("null")){
                                    noHanphoneED.setText("");
                                } else {
                                    noHanphoneED.setText(handphone);
                                }

                                if(status_pernikahan.equals("")||status_pernikahan.equals("null")){
                                    statusPernikahanPilihTV.setText("");
                                } else {
                                    statusPernikahanPilihTV.setText(status_pernikahan);
                                }

                                if(agama.equals("")||agama.equals("null")){
                                    agamaED.setText("");
                                } else {
                                    agamaED.setText(agama);
                                }

                                if(alamat_ktp.equals("")||alamat_ktp.equals("null")){
                                    alamatKTPED.setText("");
                                } else {
                                    alamatKTPED.setText(alamat_ktp);
                                }

                                if(alamat_domisili.equals("")||alamat_domisili.equals("null")){
                                    alamatDomisiliED.setText("");
                                } else {
                                    alamatDomisiliED.setText(alamat_domisili);
                                }

                            } else {
                                new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.ERROR_TYPE)
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
        CookieBar.build(FormInfoPersonalActivity.this)
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