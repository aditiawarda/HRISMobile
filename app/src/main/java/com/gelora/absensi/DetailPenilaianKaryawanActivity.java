package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class DetailPenilaianKaryawanActivity extends AppCompatActivity {

    LinearLayout backBTN;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    TextView karNama, karDepbag, karJabatan, karTglmasuk;
    TextView bobot1, bobot2, bobot3, bobot4, bobot5, bobot6, bobot7, bobot8, bobot9, bobot10, bobot11, bobot12, bobot13, bobot14;
    TextView rat1, rat2, rat3, rat4, rat5, rat6, rat7, rat8, rat9, rat10, rat11, rat12, rat13, rat14;
    TextView nilai1, nilai2, nilai3, nilai4, nilai5, nilai6, nilai7, nilai8, nilai9, nilai10, nilai11, nilai12, nilai13, nilai14;
    TextView totalBobotTV, totalNilaiTV, predikatTV, markLulus, markTidakLulus, namaPenilai, tglPenilai, namaAtasanPenilai, tglAtasanPenilai, catatanHRDTV;
    ImageView ttdPenilai, ttdAtasanPenilai;
    String idPenilaian = "";
    int totalBobot = 0, totalNilai = 0;

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

        bobot1 = findViewById(R.id.bobot_1);
        bobot2 = findViewById(R.id.bobot_2);
        bobot3 = findViewById(R.id.bobot_3);
        bobot4 = findViewById(R.id.bobot_4);
        bobot5 = findViewById(R.id.bobot_5);
        bobot6 = findViewById(R.id.bobot_6);
        bobot7 = findViewById(R.id.bobot_7);
        bobot8 = findViewById(R.id.bobot_8);
        bobot9 = findViewById(R.id.bobot_9);
        bobot10 = findViewById(R.id.bobot_10);
        bobot11 = findViewById(R.id.bobot_11);
        bobot12 = findViewById(R.id.bobot_12);
        bobot13 = findViewById(R.id.bobot_13);
        bobot14 = findViewById(R.id.bobot_14);

        rat1 = findViewById(R.id.rate_1);
        rat2 = findViewById(R.id.rate_2);
        rat3 = findViewById(R.id.rate_3);
        rat4 = findViewById(R.id.rate_4);
        rat5 = findViewById(R.id.rate_5);
        rat6 = findViewById(R.id.rate_6);
        rat7 = findViewById(R.id.rate_7);
        rat8 = findViewById(R.id.rate_8);
        rat9 = findViewById(R.id.rate_9);
        rat10 = findViewById(R.id.rate_10);
        rat11 = findViewById(R.id.rate_11);
        rat12 = findViewById(R.id.rate_12);
        rat13 = findViewById(R.id.rate_13);
        rat14 = findViewById(R.id.rate_14);

        nilai1 = findViewById(R.id.nilai_1);
        nilai2 = findViewById(R.id.nilai_2);
        nilai3 = findViewById(R.id.nilai_3);
        nilai4 = findViewById(R.id.nilai_4);
        nilai5 = findViewById(R.id.nilai_5);
        nilai6 = findViewById(R.id.nilai_6);
        nilai7 = findViewById(R.id.nilai_7);
        nilai8 = findViewById(R.id.nilai_8);
        nilai9 = findViewById(R.id.nilai_9);
        nilai10 = findViewById(R.id.nilai_10);
        nilai11 = findViewById(R.id.nilai_11);
        nilai12 = findViewById(R.id.nilai_12);
        nilai13 = findViewById(R.id.nilai_13);
        nilai14 = findViewById(R.id.nilai_14);

        totalBobotTV = findViewById(R.id.total_bobot);
        totalNilaiTV = findViewById(R.id.total_nilai);
        predikatTV = findViewById(R.id.predikat_tv);
        markLulus = findViewById(R.id.mark_lulus);
        markTidakLulus = findViewById(R.id.mark_tidak_lulus);
        namaPenilai = findViewById(R.id.nama_penilai);
        tglPenilai = findViewById(R.id.tgl_penilai);
        ttdPenilai = findViewById(R.id.ttd_penilai);
        namaAtasanPenilai = findViewById(R.id.nama_atasan_penilai);
        tglAtasanPenilai = findViewById(R.id.tgl_atasan_penilai);
        ttdAtasanPenilai = findViewById(R.id.ttd_atasan_penilai);
        catatanHRDTV = findViewById(R.id.catatan_hrd_tv);

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
                                JSONArray dataRating = data.getJSONArray("rating");
                                String status_kelulusan = dataArray.getString("status_kelulusan");
                                String nama_approver_kabag = dataArray.getString("nama_approver_kabag");
                                String tgl_approve_kabag = dataArray.getString("tgl_approve_kabag");
                                String ttd_approver_kabag = dataArray.getString("ttd_approver_kabag");
                                String nama_approver_kadept = dataArray.getString("nama_approver_kadept");
                                String tgl_approve_kadept = dataArray.getString("tgl_approve_kadept");
                                String ttd_approver_kadept = dataArray.getString("ttd_approver_kadept");
                                String catatan_hrd = dataArray.getString("catatan_hrd");

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
                                for(int i = 0; i < dataRating.length(); i++){
                                    JSONObject penilaian = dataRating.getJSONObject(i);
                                    if (penilaian.getString("id_faktor_penilaian").equals("1")){
                                        bobot1.setText(penilaian.getString("bobot"));
                                        rat1.setText(penilaian.getString("rating"));
                                        nilai1.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("3")){
                                        bobot2.setText(penilaian.getString("bobot"));
                                        rat2.setText(penilaian.getString("rating"));
                                        nilai2.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("5")){
                                        bobot3.setText(penilaian.getString("bobot"));
                                        rat3.setText(penilaian.getString("rating"));
                                        nilai3.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("7")){
                                        bobot4.setText(penilaian.getString("bobot"));
                                        rat4.setText(penilaian.getString("rating"));
                                        nilai4.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("9")){
                                        bobot5.setText(penilaian.getString("bobot"));
                                        rat5.setText(penilaian.getString("rating"));
                                        nilai5.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("11")){
                                        bobot6.setText(penilaian.getString("bobot"));
                                        rat6.setText(penilaian.getString("rating"));
                                        nilai6.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("13")){
                                        bobot7.setText(penilaian.getString("bobot"));
                                        rat7.setText(penilaian.getString("rating"));
                                        nilai7.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("15")){
                                        bobot8.setText(penilaian.getString("bobot"));
                                        rat8.setText(penilaian.getString("rating"));
                                        nilai8.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("17")){
                                        bobot9.setText(penilaian.getString("bobot"));
                                        rat9.setText(penilaian.getString("rating"));
                                        nilai9.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("19")){
                                        bobot10.setText(penilaian.getString("bobot"));
                                        rat10.setText(penilaian.getString("rating"));
                                        nilai10.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("21")){
                                        bobot11.setText(penilaian.getString("bobot"));
                                        rat11.setText(penilaian.getString("rating"));
                                        nilai11.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("23")){
                                        bobot12.setText(penilaian.getString("bobot"));
                                        rat12.setText(penilaian.getString("rating"));
                                        nilai12.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("25")){
                                        bobot13.setText(penilaian.getString("bobot"));
                                        rat13.setText(penilaian.getString("rating"));
                                        nilai13.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    } else if(penilaian.getString("id_faktor_penilaian").equals("27")){
                                        bobot14.setText(penilaian.getString("bobot"));
                                        rat14.setText(penilaian.getString("rating"));
                                        nilai14.setText(String.valueOf(Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"))));
                                    }

                                    totalBobot = totalBobot + Integer.parseInt(penilaian.getString("bobot"));
                                    totalNilai = totalNilai + Integer.parseInt(penilaian.getString("rating"))*Integer.parseInt(penilaian.getString("bobot"));

                                }

                                totalBobotTV.setText(String.valueOf(totalBobot));
                                totalNilaiTV.setText(String.valueOf(totalNilai));

                                if(totalNilai <= 100){
                                    predikatTV.setText("KS");
                                } else if(totalNilai > 100 && totalNilai <= 200){
                                    predikatTV.setText("K");
                                } else if(totalNilai > 200 && totalNilai <= 300){
                                    predikatTV.setText("C");
                                } else if(totalNilai > 300 && totalNilai <= 400){
                                    predikatTV.setText("B");
                                } else if(totalNilai > 400 && totalNilai <= 500){
                                    predikatTV.setText("BS");
                                }

                                if(status_kelulusan.equals("1")){
                                    markLulus.setText("✓");
                                    markTidakLulus.setText("");
                                } else if(status_kelulusan.equals("2")){
                                    markLulus.setText("");
                                    markTidakLulus.setText("✓");
                                }

                                namaPenilai.setText(nama_approver_kabag);
                                String dayDatePenilai = tgl_approve_kabag.substring(0, 10).substring(8,10);
                                String yearDatePenilai = tgl_approve_kabag.substring(0, 10).substring(0,4);
                                String monthDatePenilai = tgl_approve_kabag.substring(0, 10).substring(5,7);
                                tglPenilai.setText(dayDatePenilai+"/"+monthDatePenilai+"/"+yearDatePenilai);
                                String url_ttd_penilai = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_approver_kabag;

                                Picasso.get().load(url_ttd_penilai).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(ttdPenilai);

                                if(!nama_approver_kadept.equals("") && !nama_approver_kadept.equals("null") && nama_approver_kadept!=null){
                                    namaAtasanPenilai.setText(nama_approver_kadept);
                                    String dayDateAtasanPenilai = tgl_approve_kadept.substring(0, 10).substring(8,10);
                                    String yearDateAtasanPenilai = tgl_approve_kadept.substring(0, 10).substring(0,4);
                                    String monthDateAtasanPenilai = tgl_approve_kadept.substring(0, 10).substring(5,7);
                                    tglAtasanPenilai.setText(dayDatePenilai+"/"+monthDatePenilai+"/"+yearDatePenilai);
                                    String url_ttd_atasan_penilai = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+ttd_approver_kadept;

                                    Picasso.get().load(url_ttd_atasan_penilai).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(ttdAtasanPenilai);
                                }

                                if(!catatan_hrd.equals("") && !catatan_hrd.equals("null") && catatan_hrd!=null){
                                    catatanHRDTV.setText(catatan_hrd+"   ");
                                } else {
                                    catatanHRDTV.setText("");
                                }

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