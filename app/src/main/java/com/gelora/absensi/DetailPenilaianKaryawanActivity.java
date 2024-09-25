package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DetailPenilaianKaryawanActivity extends AppCompatActivity {

    LinearLayout backBTN, downloadBTN, actionPart, confirmBTN, rejectedBTN, actionBar, accMark, rejMark;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;

    TextView karNama, karDepbag, karJabatan, karTglmasuk;
    TextView bobot1, bobot2, bobot3, bobot4, bobot5, bobot6, bobot7, bobot8, bobot9, bobot10, bobot11, bobot12, bobot13, bobot14;
    TextView rat1, rat2, rat3, rat4, rat5, rat6, rat7, rat8, rat9, rat10, rat11, rat12, rat13, rat14;
    TextView nilai1, nilai2, nilai3, nilai4, nilai5, nilai6, nilai7, nilai8, nilai9, nilai10, nilai11, nilai12, nilai13, nilai14;
    TextView totalBobotTV, totalNilaiTV, predikatTV, markLulus, markTidakLulus, namaPenilai, tglPenilai, namaAtasanPenilai, tglAtasanPenilai, catatanHRDTV;
    ImageView ttdPenilai, ttdAtasanPenilai;
    String nikDitilai = "", idPenilaian = "";
    int totalBobot = 0, totalNilai = 0;
    String file_url = "";
    KAlertDialog pDialog;
    private int i = -1;
    private Handler handler = new Handler();

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
        downloadBTN = findViewById(R.id.download_btn);
        actionPart = findViewById(R.id.action_part);
        confirmBTN = findViewById(R.id.confirm_btn);
        rejectedBTN = findViewById(R.id.rejected_btn);
        actionBar = findViewById(R.id.action_bar);
        accMark = findViewById(R.id.acc_mark);
        rejMark = findViewById(R.id.rej_mark);

        idPenilaian = getIntent().getExtras().getString("id_penilaian");
        file_url = "https://hrisgelora.co.id/absen/download_pdf_penilaian_karyawan/"+idPenilaian;

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
                handler.postDelayed(new Runnable() {
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

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Unduh File?")
                        .setContentText("File hasil unduh akan disimpan pada folder Download/HRIS Mobile")
                        .setCancelText(" BATAL ")
                        .setConfirmText(" UNDUH ")
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
                                String link = "https://hrisgelora.co.id/api/download_pdf_penilaian_sdm/"+idPenilaian;
                                pDialog = new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        checkLinkStatus(link);
                                    }
                                }.start();
                            }
                        })
                        .show();
            }
        });

        confirmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Konfirmasi?")
                        .setContentText("Yakin untuk dikonfirmasi sekarang?")
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

                                pDialog = new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien6));
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

        rejectedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Tolak?")
                        .setContentText("Yakin untuk menolak penilaian?")
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

                                pDialog = new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPenilaianKaryawanActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        rejectFunc();
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
        });

        getData();

    }

    private void checkLinkStatus(String link) {
        new Thread(() -> {
            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("HEAD");
                urlConnection.setConnectTimeout(3000);
                urlConnection.setReadTimeout(3000);
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                String result;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    downloadFile();
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tidak dapat mengakses file")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tidak dapat mengakses file")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Terjadi kesalahan")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                });
            }
        }).start();
    }

    private void downloadFile() {
        new Thread(() -> {
            try {
                URL url = new URL("https://hrisgelora.co.id/download/penilaian/" + nikDitilai + ".pdf");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        pDialog.dismiss();
                        new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tidak dapat mengakses file")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(AppCompatDialog::dismiss)
                                .show();
                    });
                    Log.e("Download Error", "Server returned HTTP " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
                    return;
                }

                File hrisFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "HRIS Mobile");
                if (!hrisFolder.exists()) {
                    hrisFolder.mkdirs();
                }
                File pdfFile = new File(hrisFolder, "Penilaian_" + nikDitilai + "_" + idPenilaian + ".pdf");

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(pdfFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                Uri fileUri = Uri.fromFile(pdfFile);
                runOnUiThread(() -> {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Unduh Berhasil")
                            .setContentText("File penilaian berhasil diunduh, periksa folder download anda")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(AppCompatDialog::dismiss)
                            .show();
                    showDownloadNotification(pdfFile);
                });

            } catch (Exception e) {
                Log.e("Download Error", "Error : " + e.getMessage());
                runOnUiThread(() -> {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPenilaianKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("File penilaian gagal diunduh")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(AppCompatDialog::dismiss)
                            .show();
                });
            }
        }).start();
    }

    private void showDownloadNotification(File pdfFile) {
        Uri pdfUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "pdf_view_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "PDF View Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setContentTitle("Berhasil Diunduh")
                .setContentText("Tap untuk melihat dokumen penilaian SDM yang telah diunduh.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_detail_penilaian_karyawan";
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
                                String NIK = dataArray.getString("NIK");
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
                                String status_approve_kadept = dataArray.getString("status_approve_kadept");
                                String nama_approver_kadept = dataArray.getString("nama_approver_kadept");
                                String tgl_approve_kadept = dataArray.getString("tgl_approve_kadept");
                                String ttd_approver_kadept = dataArray.getString("ttd_approver_kadept");
                                String catatan_hrd = dataArray.getString("catatan_hrd");
                                String id_departemen = dataArray.getString("id_departemen");
                                String id_bagian = dataArray.getString("id_bagian");

                                nikDitilai = NIK;
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

                                if(!nama_approver_kabag.equals("") && !nama_approver_kabag.equals("null") && nama_approver_kabag != null){
                                    namaPenilai.setText(nama_approver_kabag);
                                    String dayDatePenilai = tgl_approve_kabag.substring(0, 10).substring(8,10);
                                    String yearDatePenilai = tgl_approve_kabag.substring(0, 10).substring(0,4);
                                    String monthDatePenilai = tgl_approve_kabag.substring(0, 10).substring(5,7);
                                    tglPenilai.setText(dayDatePenilai+"/"+monthDatePenilai+"/"+yearDatePenilai);
                                    String url_ttd_penilai = "https://hrisgelora.co.id/upload/digital_signature/"+ttd_approver_kabag;

                                    Picasso.get().load(url_ttd_penilai).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(ttdPenilai);

                                    if(status_approve_kadept.equals("1")){
                                        actionPart.setVisibility(View.GONE);
                                        namaAtasanPenilai.setText(nama_approver_kadept);
                                        String dayDateAtasanPenilai = tgl_approve_kadept.substring(0, 10).substring(8,10);
                                        String yearDateAtasanPenilai = tgl_approve_kadept.substring(0, 10).substring(0,4);
                                        String monthDateAtasanPenilai = tgl_approve_kadept.substring(0, 10).substring(5,7);
                                        tglAtasanPenilai.setText(dayDateAtasanPenilai+"/"+monthDateAtasanPenilai+"/"+yearDateAtasanPenilai);
                                        String url_ttd_atasan_penilai = "https://hrisgelora.co.id/upload/digital_signature/"+ttd_approver_kadept;

                                        Picasso.get().load(url_ttd_atasan_penilai).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(ttdAtasanPenilai);

                                        accMark.setVisibility(View.VISIBLE);
                                        rejMark.setVisibility(View.GONE);

                                    } else if(status_approve_kadept.equals("2")){
                                        accMark.setVisibility(View.GONE);
                                        rejMark.setVisibility(View.VISIBLE);
                                    } else if(status_approve_kadept.equals("0")){
                                        if(sharedPrefManager.getSpIdHeadDept().equals(id_departemen) && (sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10"))){
                                            actionPart.setVisibility(View.VISIBLE);
                                        } else {
                                            if(sharedPrefManager.getSpNik().equals("0829030809") && (id_bagian.equals("20") || id_bagian.equals("27"))) {
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else if(sharedPrefManager.getSpNik().equals("0687260508") && (id_bagian.equals("16") || id_bagian.equals("17") || id_bagian.equals("22"))) {
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else if(sharedPrefManager.getSpNik().equals("0113010500") && (id_bagian.equals("4") || id_bagian.equals("5") || id_bagian.equals("6"))) {
                                                actionPart.setVisibility(View.VISIBLE);
                                            } else {
                                                actionPart.setVisibility(View.GONE);
                                            }
                                        }
                                    }
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

    private void checkSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cek_ttd_digital";
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
                                String url = "https://hrisgelora.co.id/upload/digital_signature/"+signature;
                                updateConfirm();
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
                                                Intent intent = new Intent(DetailPenilaianKaryawanActivity.this, DigitalSignatureActivity.class);
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

    private void updateConfirm(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/update_confirm_penilaian_sdm";
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
                                getData();
                                actionPart.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Dikonfirmasi")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Dikonfirmasi")
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("id", idPenilaian);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void rejectFunc(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/update_reject_penilaian_sdm";
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
                                getData();
                                actionPart.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Ditolak")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Ditolak")
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("id", idPenilaian);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}