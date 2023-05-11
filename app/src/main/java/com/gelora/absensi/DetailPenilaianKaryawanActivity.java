package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailPenilaianKaryawanActivity extends AppCompatActivity {

    LinearLayout backBTN;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    TextView karNama, karDepbag, karJabatan, karTglmasuk;
    String idPenilaian = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penilaian_karyawan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);

        karNama = findViewById(R.id.kar_nama);
        karDepbag = findViewById(R.id.kar_depbag);
        karJabatan = findViewById(R.id.kar_jabatan);
        karTglmasuk = findViewById(R.id.kar_tglmasuk);

        idPenilaian = getIntent().getExtras().getString("id_penilaian");

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_detail_penilaian_karyawan";
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
                                String nama_karyawan = dataArray.getString("nama_karyawan");
                                String jabatan = dataArray.getString("jabatan");
                                String dept = dataArray.getString("dept");
                                String bag = dataArray.getString("bag");
                                String tanggal_bergabung = dataArray.getString("tanggal_bergabung");

                                karNama.setText(nama_karyawan);
                                karJabatan.setText(jabatan);
                                karDepbag.setText(dept+"/"+bag);

                                String input_date = tanggal_bergabung;
                                String dayDate = input_date.substring(8,10);
                                String yearDate = input_date.substring(0,4);
                                String bulanValue = input_date.substring(5,7);
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
                                        bulanName = "Not found";
                                        break;
                                }

                                karTglmasuk.setText(dayDate+" "+bulanName+" "+yearDate);

                            } else {
                                new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_penilaian", idPenilaian);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailPenilaianKaryawanActivity.this)
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