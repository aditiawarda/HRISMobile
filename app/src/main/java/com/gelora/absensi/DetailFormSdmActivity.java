package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailFormSdmActivity extends AppCompatActivity {

    LinearLayout backBTN;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    String idData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);

        idData = getIntent().getExtras().getString("id_data");

        Toast.makeText(this, idData, Toast.LENGTH_SHORT).show();

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
        final String url = "https://geloraaksara.co.id/absen-online/api/get_detail_form_sdm";
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
                                JSONObject dataArray           = data.getJSONObject("data");
                                String id_record               = dataArray.getString("id_record");
                                String keterangan              = dataArray.getString("keterangan");
                                String nama                    = dataArray.getString("nama");
                                String unit_bisnis             = dataArray.getString("unit_bisnis");
                                String departemen              = dataArray.getString("departemen");
                                String bagian                  = dataArray.getString("bagian");
                                String jabatan                 = dataArray.getString("jabatan");
                                String komponen_gaji           = dataArray.getString("komponen_gaji");
                                String nama_lama               = dataArray.getString("nama_lama");
                                String unit_bisnis_lama        = dataArray.getString("unit_bisnis_lama");
                                String departemen_lama         = dataArray.getString("departemen_lama");
                                String bagian_lama             = dataArray.getString("bagian_lama");
                                String jabatan_lama            = dataArray.getString("jabatan_lama");
                                String komponen_gaji_lama      = dataArray.getString("komponen_gaji_lama");
                                String status_approve_kabag    = dataArray.getString("status_approve_kabag");
                                String tgl_approve_kabag       = dataArray.getString("tgl_approve_kabag");
                                String approver_kabag          = dataArray.getString("approver_kabag");
                                String status_approve_kadept   = dataArray.getString("status_approve_kadept");
                                String tgl_approve_kadept      = dataArray.getString("tgl_approve_kadept");
                                String status_approve_direktur = dataArray.getString("status_approve_direktur");
                                String tgl_approve_direktur    = dataArray.getString("tgl_approve_direktur");
                                String approver_direktur       = dataArray.getString("approver_direktur");
                                String status_approve_hrd      = dataArray.getString("status_approve_hrd");
                                String tgl_diterima            = dataArray.getString("tgl_diterima");
                                String diterima_oleh           = dataArray.getString("diterima_oleh");
                                String catatan                 = dataArray.getString("catatan");
                                String no_frm                  = dataArray.getString("no_frm");
                                String created_at              = dataArray.getString("created_at");
                                String updated_at              = dataArray.getString("updated_at");
                                String dibuat_oleh             = dataArray.getString("dibuat_oleh");
                                String jabatan_departemen      = dataArray.getString("jabatan_departemen");
                                String deskripsi_jabatan       = dataArray.getString("deskripsi_jabatan");
                                String syarat_penerimaan       = dataArray.getString("syarat_penerimaan");
                                String tgl_pengangkatan        = dataArray.getString("tgl_pengangkatan");
                                String jabatan_baru            = dataArray.getString("jabatan_baru");
                                String tgl_pengangkatan_baru   = dataArray.getString("tgl_pengangkatan_baru");
                                String alasan_pengangkatan     = dataArray.getString("alasan_pengangkatan");
                                String tgl_dibutuhkan          = dataArray.getString("tgl_dibutuhkan");
                                String tgl_pemenuhan           = dataArray.getString("tgl_pemenuhan");
                                String memenuhi_syarat         = dataArray.getString("memenuhi_syarat");
                                String lain_lain               = dataArray.getString("lain_lain");
                                String persetujuan             = dataArray.getString("persetujuan");

//                                String input_date = tanggal_bergabung;
//                                String dayDate = input_date.substring(8,10);
//                                String yearDate = input_date.substring(0,4);
//                                String bulanValue = input_date.substring(5,7);
//                                String bulanName;
//
//                                switch (bulanValue) {
//                                    case "01":
//                                        bulanName = "Jan";
//                                        break;
//                                    case "02":
//                                        bulanName = "Feb";
//                                        break;
//                                    case "03":
//                                        bulanName = "Mar";
//                                        break;
//                                    case "04":
//                                        bulanName = "Apr";
//                                        break;
//                                    case "05":
//                                        bulanName = "Mei";
//                                        break;
//                                    case "06":
//                                        bulanName = "Jun";
//                                        break;
//                                    case "07":
//                                        bulanName = "Jul";
//                                        break;
//                                    case "08":
//                                        bulanName = "Agu";
//                                        break;
//                                    case "09":
//                                        bulanName = "Sep";
//                                        break;
//                                    case "10":
//                                        bulanName = "Okt";
//                                        break;
//                                    case "11":
//                                        bulanName = "Nov";
//                                        break;
//                                    case "12":
//                                        bulanName = "Des";
//                                        break;
//                                    default:
//                                        bulanName = "Not found";
//                                        break;
//                                }
//
//                                karTglmasuk.setText(dayDate+" "+bulanName+" "+yearDate);


                            } else {
                                new KAlertDialog(DetailFormSdmActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailFormSdmActivity.this)
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