package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

    LinearLayout backBTN, actionBar;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView markCeklis1, markCeklis2, markCeklis3, markCeklis4, markCeklis5, markCeklis6, markCeklis7;
    TextView namaBaruTV, unitBisnisBaruTV, departemenBaruTV, bagianBaruTV, jabatanBaruTV, komponenGajiBaruTV;
    TextView namaLamaTV, unitBisnisLamaTV, departemenLamaTV, bagianLamaTV, jabatanLamaTV, komponenGajiLamaTV;
    TextView jabatanSlashDepartemenTV, deskripsiSlashJabatanTV, syaratPenerimaanTV, tglDibutuhkan1TV, tglPemenuhan1TV;
    TextView syaratYaTV, syaratTidakTV;
    TextView catatanTV, namaKabagTV, namaKadeptTV, namaDirekturTV, tglApproveKabag, tglApproveKadept, tglApproveDireksi;
    ImageView ttdPemohon, ttdKadept, ttdDireksi, ttdPenerima;
    String idData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        markCeklis1 = findViewById(R.id.mark_ceklis_1);
        markCeklis2 = findViewById(R.id.mark_ceklis_2);
        markCeklis3 = findViewById(R.id.mark_ceklis_3);
        markCeklis4 = findViewById(R.id.mark_ceklis_4);
        markCeklis5 = findViewById(R.id.mark_ceklis_5);
        markCeklis6 = findViewById(R.id.mark_ceklis_6);
        markCeklis7 = findViewById(R.id.mark_ceklis_7);

        namaBaruTV = findViewById(R.id.nama_baru_tv);
        unitBisnisBaruTV = findViewById(R.id.unit_bisnis_baru_tv);
        departemenBaruTV = findViewById(R.id.departemen_baru_tv);
        bagianBaruTV = findViewById(R.id.bagian_baru_tv);
        jabatanBaruTV = findViewById(R.id.jabatan_baru_tv);
        komponenGajiBaruTV = findViewById(R.id.komponen_gaji_baru_tv);

        namaLamaTV = findViewById(R.id.nama_lama_tv);
        unitBisnisLamaTV = findViewById(R.id.unit_bisnis_lama_tv);
        departemenLamaTV = findViewById(R.id.departemen_lama_tv);
        bagianLamaTV = findViewById(R.id.bagian_lama_tv);
        jabatanLamaTV = findViewById(R.id.jabatan_lama_tv);
        komponenGajiLamaTV = findViewById(R.id.komponen_gaji_lama_tv);

        jabatanSlashDepartemenTV = findViewById(R.id.jabatan_slash_departemen_tv);
        deskripsiSlashJabatanTV = findViewById(R.id.deskripsi_slash_jabatan_tv);
        syaratPenerimaanTV = findViewById(R.id.syarat_penerimaan_tv);
        tglDibutuhkan1TV = findViewById(R.id.tgl_dibutuhkan_1_tv);
        tglPemenuhan1TV = findViewById(R.id.tgl_pemenuhan_1_tv);

        syaratYaTV = findViewById(R.id.syarat_ya_tv);
        syaratTidakTV = findViewById(R.id.syarat_tidak_tv);

        catatanTV = findViewById(R.id.catatan_tv);

        namaKabagTV = findViewById(R.id.nama_kabag_tv);
        namaKadeptTV = findViewById(R.id.nama_kadept_tv);
        namaDirekturTV = findViewById(R.id.nama_direktur_tv);

        ttdPemohon = findViewById(R.id.ttd_pemohon);
        ttdKadept = findViewById(R.id.ttd_kadept);
        ttdDireksi = findViewById(R.id.ttd_direksi);
        ttdPenerima = findViewById(R.id.ttd_penerima);

        tglApproveKabag = findViewById(R.id.tgl_approve_kabag);
        tglApproveKadept = findViewById(R.id.tgl_approve_kadept);
        tglApproveDireksi = findViewById(R.id.tgl_approve_direksi);

        idData = getIntent().getExtras().getString("id_data");

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
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
                                String nik                     = dataArray.getString("nik");
                                String nama                    = dataArray.getString("nama");
                                String unit_bisnis             = dataArray.getString("unit_bisnis");
                                String nama_unit_bisnis        = dataArray.getString("nama_unit_bisnis");
                                String departemen              = dataArray.getString("departemen");
                                String nama_departemen         = dataArray.getString("nama_departemen");
                                String bagian                  = dataArray.getString("bagian");
                                String nama_bagian             = dataArray.getString("nama_bagian");
                                String jabatan                 = dataArray.getString("jabatan");
                                String nama_jabatan            = dataArray.getString("nama_jabatan");
                                String komponen_gaji           = dataArray.getString("komponen_gaji");
                                String nik_lama                = dataArray.getString("nik_lama");
                                String nama_lama               = dataArray.getString("nama_lama");
                                String unit_bisnis_lama        = dataArray.getString("unit_bisnis_lama");
                                String nama_unit_bisnis_lama   = dataArray.getString("nama_unit_bisnis_lama");
                                String departemen_lama         = dataArray.getString("departemen_lama");
                                String nama_departemen_lama    = dataArray.getString("nama_departemen_lama");
                                String bagian_lama             = dataArray.getString("bagian_lama");
                                String nama_bagian_lama        = dataArray.getString("nama_bagian_lama");
                                String jabatan_lama            = dataArray.getString("jabatan_lama");
                                String nama_jabatan_lama       = dataArray.getString("nama_jabatan_lama");
                                String komponen_gaji_lama      = dataArray.getString("komponen_gaji_lama");
                                String status_approve_kabag    = dataArray.getString("status_approve_kabag");
                                String tgl_approve_kabag       = dataArray.getString("tgl_approve_kabag");
                                String approver_kabag          = dataArray.getString("approver_kabag");
                                String nama_kabag              = dataArray.getString("nama_kabag");
                                String ttd_kabag               = dataArray.getString("ttd_kabag");
                                String status_approve_kadept   = dataArray.getString("status_approve_kadept");
                                String tgl_approve_kadept      = dataArray.getString("tgl_approve_kadept");
                                String approver_kadept         = dataArray.getString("approver_kadept");
                                String nama_kadept             = dataArray.getString("nama_kadept");
                                String ttd_kadept              = dataArray.getString("ttd_kadept");
                                String status_approve_direktur = dataArray.getString("status_approve_direktur");
                                String tgl_approve_direktur    = dataArray.getString("tgl_approve_direktur");
                                String approver_direktur       = dataArray.getString("approver_direktur");
                                String nama_direktur           = dataArray.getString("nama_direktur");
                                String ttd_direktur            = dataArray.getString("ttd_direktur");
                                String status_approve_hrd      = dataArray.getString("status_approve_hrd");
                                String tgl_diterima            = dataArray.getString("tgl_diterima");
                                String diterima_oleh           = dataArray.getString("diterima_oleh");
                                String nama_penerima           = dataArray.getString("nama_penerima");
                                String ttd_penerima            = dataArray.getString("ttd_penerima");
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

                                if(keterangan.equals("1")){
                                    markCeklis1.setText("✓");

                                    unitBisnisBaruTV.setText(nama_unit_bisnis);
                                    departemenBaruTV.setText(nama_departemen);
                                    bagianBaruTV.setText(nama_bagian);
                                    jabatanBaruTV.setText(nama_jabatan);
                                    komponenGajiBaruTV.setText(komponen_gaji);

                                    jabatanSlashDepartemenTV.setText(jabatan_departemen);
                                    deskripsiSlashJabatanTV.setText(deskripsi_jabatan);
                                    syaratPenerimaanTV.setText(syarat_penerimaan);
                                    tglDibutuhkan1TV.setText(tgl_dibutuhkan.substring(8,10)+"/"+tgl_dibutuhkan.substring(5,7)+"/"+tgl_dibutuhkan.substring(0,4));
                                    tglPemenuhan1TV.setText(tgl_pemenuhan.substring(8,10)+"/"+tgl_pemenuhan.substring(5,7)+"/"+tgl_pemenuhan.substring(0,4));

                                    if(status_approve_kabag.equals("1")){
                                        String url_ttd_kabag = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kabag;
                                        Picasso.get().load(url_ttd_kabag).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdPemohon);
                                        namaKabagTV.setText(nama_kabag);
                                        tglApproveKabag.setText(tgl_approve_kabag.substring(8,10)+"/"+tgl_approve_kabag.substring(5,7)+"/"+tgl_approve_kabag.substring(0,4));

                                        if(status_approve_kadept.equals("1")){
                                            String url_ttd_kadept = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_kadept;
                                            Picasso.get().load(url_ttd_kadept).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdKadept);
                                            namaKadeptTV.setText(nama_kadept);
                                            tglApproveKadept.setText(tgl_approve_kadept.substring(8,10)+"/"+tgl_approve_kadept.substring(5,7)+"/"+tgl_approve_kadept.substring(0,4));

                                            if(status_approve_direktur.equals("1")){
                                                String url_ttd_direksi = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_direktur;
                                                Picasso.get().load(url_ttd_direksi).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(ttdDireksi);
                                                namaDirekturTV.setText(nama_direktur);
                                                tglApproveDireksi.setText(tgl_approve_direktur.substring(8,10)+"/"+tgl_approve_direktur.substring(5,7)+"/"+tgl_approve_direktur.substring(0,4));

                                            } else if(status_approve_direktur.equals("2")){

                                            } else if(status_approve_direktur.equals("0")){

                                            }

                                        } else if(status_approve_kadept.equals("2")){

                                        } else if(status_approve_kadept.equals("0")){

                                        }

                                    } else if(status_approve_kabag.equals("2")){

                                    } else if(status_approve_kabag.equals("0")){

                                    }

                                    catatanTV.setText(catatan);

                                } else if(keterangan.equals("2")){
                                    markCeklis2.setText("✓");

                                    namaBaruTV.setText(nama);
                                    unitBisnisBaruTV.setText(nama_unit_bisnis);
                                    departemenBaruTV.setText(nama_departemen);
                                    bagianBaruTV.setText(nama_bagian);
                                    jabatanBaruTV.setText(nama_jabatan);
                                    komponenGajiBaruTV.setText(komponen_gaji);

                                    namaLamaTV.setText(nama_lama);
                                    unitBisnisLamaTV.setText(nama_unit_bisnis_lama);
                                    departemenLamaTV.setText(nama_departemen_lama);
                                    bagianLamaTV.setText(nama_bagian_lama);
                                    jabatanLamaTV.setText(nama_jabatan_lama);
                                    komponenGajiLamaTV.setText(komponen_gaji_lama);

                                    if(memenuhi_syarat.equals("1")){
                                        syaratYaTV.setText("✓");
                                        syaratTidakTV.setText("");
                                    } else if(memenuhi_syarat.equals("2")){
                                        syaratYaTV.setText("");
                                        syaratTidakTV.setText("✓");
                                    } else {
                                        syaratYaTV.setText("");
                                        syaratTidakTV.setText("");
                                    }

                                    catatanTV.setText(catatan);

                                } else if(keterangan.equals("3")){
                                    markCeklis3.setText("✓");

                                } else if(keterangan.equals("4")){
                                    markCeklis4.setText("✓");

                                } else if(keterangan.equals("5")){
                                    markCeklis5.setText("✓");

                                } else if(keterangan.equals("6")){
                                    markCeklis6.setText("✓");

                                } else if(keterangan.equals("7")){
                                    markCeklis7.setText("✓");

                                }

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