package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
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
import com.google.zxing.WriterException;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DetailPermohonanIzinActivity extends AppCompatActivity {

    TextView catatanHRDTV, approverHrdTV, notedTV, appoveStatusHRD, idPermohonanTV, namaKaryawanTV, nikKaryawanTV, bagianKaryawanTV, jabatanKaryawanTV, alasanIzinTV, tglMulaiTV, tglAkhirTV, totalHariTV, tglPermohonanTV, pemohonTV, tanggalApproveTV, tanggalApproveHRDTV, supervisorTV, hrdTV;
    String nikIzin = "", uriImage, uriImage2, idIzinRecord, statusKondisi = "0", kode, title;
    LinearLayout kopGap, kopErlass, catatanHRDPart, actionBar, editPermohonanBTN, cancelPermohonanBTN, pdfBTN, viewSuratSakitBTN, downloadBTN, suratIzinPart, rejectedMark, acceptedMark, backBTN, approvedBTN, rejectedBTN, actionPart;
    SwipeRefreshLayout refreshLayout;
    ImageView ttdPemohon, ttdSupervisor, ttdHRD, qrDocument;
    KAlertDialog pDialog;
    Bitmap bitmap;
    SharedPrefManager sharedPrefManager;
    View rootview;
    private int i = -1;

    ProgressDialog prDialog;
    public static final int progress_bar_type = 0;
    String file_url = "";

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permohonan_izin);

        rootview = findViewById(android.R.id.content);
        sharedPrefManager = new SharedPrefManager(this);
        idPermohonanTV = findViewById(R.id.id_permohonan);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        kopGap = findViewById(R.id.kop_gap);
        kopErlass = findViewById(R.id.kop_erlass);
        approvedBTN = findViewById(R.id.appoved_btn);
        rejectedBTN = findViewById(R.id.rejected_btn);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        bagianKaryawanTV = findViewById(R.id.bagian_karyawan_tv);
        jabatanKaryawanTV = findViewById(R.id.jabatan_karyawan_tv);
        alasanIzinTV = findViewById(R.id.alasan_izin_tv);
        tglMulaiTV = findViewById(R.id.tgl_mulai_tv);
        tglAkhirTV = findViewById(R.id.tgl_akhir_tv);
        totalHariTV = findViewById(R.id.jumlah_hari_tv);
        tglPermohonanTV = findViewById(R.id.tgl_permohonan_tv);
        pemohonTV = findViewById(R.id.pemohon_tv);
        supervisorTV = findViewById(R.id.supervisor_tv);
        hrdTV = findViewById(R.id.hrd_tv);
        ttdPemohon = findViewById(R.id.ttd_pemohon);
        ttdSupervisor = findViewById(R.id.ttd_supervisor);
        ttdHRD = findViewById(R.id.ttd_hrd);
        actionPart = findViewById(R.id.action_part);
        rejectedMark = findViewById(R.id.rejected_mark);
        acceptedMark = findViewById(R.id.accepted_mark);
        suratIzinPart = findViewById(R.id.surat_izin_part);
        downloadBTN = findViewById(R.id.download_btn);
        pdfBTN = findViewById(R.id.print_pdf);
        qrDocument = findViewById(R.id.qr_document);
        appoveStatusHRD = findViewById(R.id.appove_status_hrd);
        tanggalApproveHRDTV = findViewById(R.id.tanggal_approve_hrd);
        viewSuratSakitBTN = findViewById(R.id.view_surat_sakit_btn);
        tanggalApproveTV = findViewById(R.id.tanggal_approve);
        notedTV = findViewById(R.id.noted_tv);
        approverHrdTV = findViewById(R.id.approver_hrd_tv);
        cancelPermohonanBTN = findViewById(R.id.cancel_permohonan_btn);
        editPermohonanBTN = findViewById(R.id.edit_permohonan_btn);
        actionBar = findViewById(R.id.action_bar);
        catatanHRDPart = findViewById(R.id.catatan_hrd_part);
        catatanHRDTV = findViewById(R.id.catatan_hrd_tv);

        kode = getIntent().getExtras().getString("kode");
        idIzinRecord = getIntent().getExtras().getString("id_izin");
        file_url = "https://hrisgelora.co.id/absen/pdf_form_izin/"+idIzinRecord;

        if (sharedPrefManager.getSpIdCor().equals("1")){
            kopGap.setVisibility(View.VISIBLE);
            kopErlass.setVisibility(View.GONE);
        } else if (sharedPrefManager.getSpIdCor().equals("3")){
            kopGap.setVisibility(View.GONE);
            kopErlass.setVisibility(View.VISIBLE);
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataDetailPermohonan();
                    }
                }, 800);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancelPermohonanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk membatalkan permohonan izin?")
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

                                pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        cancelPermohonan(idIzinRecord);
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
        });

        editPermohonanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk merubah data permohonan izin?")
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
                                Intent intent = new Intent(DetailPermohonanIzinActivity.this, EditPermohonanIzinActivity.class);
                                intent.putExtra("id_record", idIzinRecord);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
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
                                String link = "https://hrisgelora.co.id/api/download_pdf_form_izin/"+idIzinRecord;
                                pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
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

        pdfBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(file_url));
                startActivity(browserIntent);
            }
        });

        approvedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Setujui?")
                        .setContentText("Yakin untuk disetujui sekarang?")
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

                                pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
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
                new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Tolak?")
                        .setContentText("Yakin untuk tolak permohonan?")
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

                                pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1000, 500) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        rejectedAction();
                                    }
                                }.start();

                            }
                        })
                        .show();

            }
        });

        idPermohonanTV.setText(idIzinRecord);

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
                    new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                URL url = new URL("https://hrisgelora.co.id/download/izin/" + nikIzin + ".pdf");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    Log.e("Download Error", "Server returned HTTP " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
                    return;
                }
                File hrisFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "HRIS Mobile");
                if (!hrisFolder.exists()) {
                    hrisFolder.mkdirs();
                }
                File pdfFile = new File(hrisFolder, "Izin_" + nikIzin + "_" + idIzinRecord + ".pdf");
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
                    new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Unduh Berhasil")
                            .setContentText("File permohonan berhasil diunduh, periksa folder download anda")
                            .setConfirmText("    OK    ")
                            .setCancelClickListener(KAlertDialog::dismiss)
                            .setConfirmClickListener(AppCompatDialog::dismiss)
                            .show();
                    showDownloadNotification(pdfFile);
                });

            } catch (Exception e) {
                Log.e("Download Error", e.getMessage());
                runOnUiThread(() -> {
                    pDialog.dismiss();
                    new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("File permohonan gagal diunduh")
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
                .setContentText("Tap untuk melihat dokumen permohonan izin yang telah diunduh.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());
    }

    private void generateQRCode(){
        if (TextUtils.isEmpty(idIzinRecord)){
            CookieBar.build(DetailPermohonanIzinActivity.this)
                    .setTitle("Perhatian")
                    .setMessage("QR Code gagal di generate!")
                    .setTitleColor(R.color.colorPrimaryDark)
                    .setMessageColor(R.color.colorPrimaryDark)
                    .setBackgroundColor(R.color.warningBottom)
                    .setIcon(R.drawable.warning_connection_mini)
                    .setCookiePosition(CookieBar.BOTTOM)
                    .show();
            return;
        }
        try {
            QRGEncoder qrgEncoder = new QRGEncoder(idIzinRecord,null, QRGContents.Type.TEXT,200);
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qrDocument.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void cancelPermohonan(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cancel_izin";
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
                                actionPart.setVisibility(View.GONE);
                                pDialog.setTitleText("Permohonan Dibatalkan")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                if(kode.equals("form")){
                                                    Intent intent = new Intent(DetailPermohonanIzinActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    onBackPressed();
                                                }
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Permohonan Gagal Dibatalkan")
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
                params.put("id", id);
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
                                approvedAction();
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
                                                statusKondisi = "1";
                                                Intent intent = new Intent(DetailPermohonanIzinActivity.this, DigitalSignatureActivity.class);
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

    private void recheckSignature(){
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
                                approvedAction();
                            } else {
                                getDataDetailPermohonan();
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

    private void approvedAction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/approve_action_izin";
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
                                getDataDetailPermohonan();
                                actionPart.setVisibility(View.GONE);
                                pDialog.setTitleText("Berhasil Disetujui")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                actionPart.setVisibility(View.VISIBLE);
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
                params.put("id_izin_record", idIzinRecord);
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("timestamp_approve", getTimeStamp());
                params.put("updated_at", getTimeStamp());

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void rejectedAction(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/reject_action_izin";
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
                                getDataDetailPermohonan();
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
                params.put("id_izin_record", idIzinRecord);
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("timestamp_approve", getTimeStamp());
                params.put("updated_at", getTimeStamp());

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getDataDetailPermohonan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_permohonan_izin_detail";
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
                            if (status.equals("Success")) {
                                generateQRCode();
                                JSONObject detail = data.getJSONObject("data");
                                String nik_karyawan = detail.getString("NIK");
                                String nama_karyawan = detail.getString("NmKaryawan");
                                String bagian = detail.getString("NmDept");
                                String jabatan = detail.getString("NmJabatan");
                                String alasan = detail.getString("keterangan");
                                String tgl_mulai = detail.getString("tanggal_mulai");
                                String tgl_akhir = detail.getString("tanggal_akhir");
                                String tgl_permohonan = detail.getString("tanggal");
                                String digital_signature = detail.getString("digital_signature");
                                String tipe_izin = detail.getString("tipe_izin");
                                String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                String nama_approver_hrd = detail.getString("nama_approver_hrd");

                                String jumlah_hari = data.getString("jumlah_hari");
                                nikIzin = nik_karyawan;

                                if(tipe_izin.equals("5")){
                                    notedTV.setVisibility(View.INVISIBLE);
                                    String foto_surat_sakit = detail.getString("foto_surat_sakit");
                                    String url_surat_sakit = "https://hrisgelora.co.id/upload/surat_sakit/"+foto_surat_sakit;
                                    viewSuratSakitBTN.setVisibility(View.VISIBLE);
                                    viewSuratSakitBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPermohonanIzinActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", url_surat_sakit);
                                            intent.putExtra("kode", "detail");
                                            intent.putExtra("jenis_detail", "izin");
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    if(sharedPrefManager.getSpIdCor().equals("1")){
                                        notedTV.setVisibility(View.VISIBLE);
                                    } else if(sharedPrefManager.getSpIdCor().equals("3")){
                                        notedTV.setVisibility(View.INVISIBLE);
                                    }
                                    viewSuratSakitBTN.setVisibility(View.GONE);
                                }

                                namaKaryawanTV.setText(nama_karyawan.toUpperCase());
                                nikKaryawanTV.setText(nik_karyawan);
                                bagianKaryawanTV.setText(bagian);
                                jabatanKaryawanTV.setText(jabatan);
                                alasanIzinTV.setText(alasan.replaceAll("\\s+$", ""));

                                String input_date_mulai = tgl_mulai;
                                String dayDateMulai = input_date_mulai.substring(8,10);
                                String yearDateMulai = input_date_mulai.substring(0,4);
                                String bulanValueMulai = input_date_mulai.substring(5,7);
                                String bulanNameMulai;

                                switch (bulanValueMulai) {
                                    case "01":
                                        bulanNameMulai = "Januari";
                                        break;
                                    case "02":
                                        bulanNameMulai = "Februari";
                                        break;
                                    case "03":
                                        bulanNameMulai = "Maret";
                                        break;
                                    case "04":
                                        bulanNameMulai = "April";
                                        break;
                                    case "05":
                                        bulanNameMulai = "Mei";
                                        break;
                                    case "06":
                                        bulanNameMulai = "Juni";
                                        break;
                                    case "07":
                                        bulanNameMulai = "Juli";
                                        break;
                                    case "08":
                                        bulanNameMulai = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameMulai = "September";
                                        break;
                                    case "10":
                                        bulanNameMulai = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameMulai = "November";
                                        break;
                                    case "12":
                                        bulanNameMulai = "Desember";
                                        break;
                                    default:
                                        bulanNameMulai = "Not found";
                                        break;
                                }

                                tglMulaiTV.setText(Integer.parseInt(dayDateMulai) +" "+bulanNameMulai+" "+yearDateMulai);

                                String input_date_akhir = tgl_akhir;
                                String dayDateAkhir = input_date_akhir.substring(8,10);
                                String yearDateAkhir = input_date_akhir.substring(0,4);
                                String bulanValueAkhir = input_date_akhir.substring(5,7);
                                String bulanNameAkhir;

                                switch (bulanValueAkhir) {
                                    case "01":
                                        bulanNameAkhir = "Januari";
                                        break;
                                    case "02":
                                        bulanNameAkhir = "Februari";
                                        break;
                                    case "03":
                                        bulanNameAkhir = "Maret";
                                        break;
                                    case "04":
                                        bulanNameAkhir = "April";
                                        break;
                                    case "05":
                                        bulanNameAkhir = "Mei";
                                        break;
                                    case "06":
                                        bulanNameAkhir = "Juni";
                                        break;
                                    case "07":
                                        bulanNameAkhir = "Juli";
                                        break;
                                    case "08":
                                        bulanNameAkhir = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameAkhir = "September";
                                        break;
                                    case "10":
                                        bulanNameAkhir = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameAkhir = "November";
                                        break;
                                    case "12":
                                        bulanNameAkhir = "Desember";
                                        break;
                                    default:
                                        bulanNameAkhir = "Not found";
                                        break;
                                }

                                tglAkhirTV.setText(Integer.parseInt(dayDateAkhir) +" "+bulanNameAkhir+" "+yearDateAkhir);
                                totalHariTV.setText("( "+ jumlah_hari +" Hari )");

                                String input_date_permohonan = tgl_permohonan;
                                String dayDatePermohonan = input_date_permohonan.substring(8,10);
                                String yearDatePermohonan = input_date_permohonan.substring(0,4);
                                String bulanValuePermohonan = input_date_permohonan.substring(5,7);
                                String bulanNamePermohonan;

                                switch (bulanValuePermohonan) {
                                    case "01":
                                        bulanNamePermohonan = "Januari";
                                        break;
                                    case "02":
                                        bulanNamePermohonan = "Februari";
                                        break;
                                    case "03":
                                        bulanNamePermohonan = "Maret";
                                        break;
                                    case "04":
                                        bulanNamePermohonan = "April";
                                        break;
                                    case "05":
                                        bulanNamePermohonan = "Mei";
                                        break;
                                    case "06":
                                        bulanNamePermohonan = "Juni";
                                        break;
                                    case "07":
                                        bulanNamePermohonan = "Juli";
                                        break;
                                    case "08":
                                        bulanNamePermohonan = "Agustus";
                                        break;
                                    case "09":
                                        bulanNamePermohonan = "September";
                                        break;
                                    case "10":
                                        bulanNamePermohonan = "Oktober";
                                        break;
                                    case "11":
                                        bulanNamePermohonan = "November";
                                        break;
                                    case "12":
                                        bulanNamePermohonan = "Desember";
                                        break;
                                    default:
                                        bulanNamePermohonan = "Not found";
                                        break;
                                }

                                tglPermohonanTV.setText("JAKARTA, "+ Integer.parseInt(dayDatePermohonan) +" "+bulanNamePermohonan+" "+yearDatePermohonan);

                                String url = "https://hrisgelora.co.id/upload/digital_signature/"+digital_signature;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(ttdPemohon);

                                String[] shortName = nama_karyawan.split(" ");

                                if(shortName.length>1){
                                    if(shortName[0].length()<3){
                                        pemohonTV.setText(shortName[1].toUpperCase());
                                    } else {
                                        pemohonTV.setText(shortName[0].toUpperCase());
                                    }
                                } else {
                                    pemohonTV.setText(shortName[0].toUpperCase());
                                }

                                String status_approve = detail.getString("status_approve");
                                if(status_approve.equals("1")){
                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                    editPermohonanBTN.setVisibility(View.GONE);
                                    actionPart.setVisibility(View.GONE);
                                    rejectedMark.setVisibility(View.GONE);
                                    supervisorTV.setVisibility(View.VISIBLE);
                                    String approver = detail.getString("approver");
                                    String signature_approver = detail.getString("signature_approver");
                                    String timestamp_approve = detail.getString("timestamp_approve");

                                    String url_approver = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver;

                                    Picasso.get().load(url_approver).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(ttdSupervisor);

                                    tanggalApproveTV.setText(timestamp_approve.substring(8,10)+"/"+timestamp_approve.substring(5,7)+"/"+timestamp_approve.substring(2,4));

                                    String[] shortName2 = approver.split(" ");

                                    if(shortName2.length>1){
                                        if(shortName2[0].length()<3){
                                            supervisorTV.setText(shortName2[1].toUpperCase());
                                        } else {
                                            supervisorTV.setText(shortName2[0].toUpperCase());
                                        }
                                    } else {
                                        supervisorTV.setText(shortName2[0].toUpperCase());
                                    }

                                    // String shortName2 = approver+" ";
                                    // if(shortName2.contains(" ")){
                                    //    shortName2 = shortName2.substring(0, shortName2.indexOf(" "));
                                    //    supervisorTV.setText(shortName2.toUpperCase());
                                    // }

                                    String status_approve_hrd = detail.getString("status_approve_hrd");
                                    if (status_approve_hrd.equals("1")){
                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);
                                        if(nik_approver_hrd.equals("null") || nik_approver_hrd.equals("") || nik_approver_hrd.equals(null)){
                                            String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                            tanggalApproveHRDTV.setText(timestamp_approve_hrd.substring(8,10)+"/"+timestamp_approve_hrd.substring(5,7)+"/"+timestamp_approve_hrd.substring(2,4));
                                            acceptedMark.setVisibility(View.VISIBLE);
                                            rejectedMark.setVisibility(View.GONE);
                                            appoveStatusHRD.setVisibility(View.VISIBLE);
                                            ttdHRD.setVisibility(View.GONE);
                                        } else {
                                            String timestamp_approve_hrd = detail.getString("timestamp_approve_hrd");
                                            tanggalApproveHRDTV.setText(timestamp_approve_hrd.substring(8,10)+"/"+timestamp_approve_hrd.substring(5,7)+"/"+timestamp_approve_hrd.substring(2,4));
                                            acceptedMark.setVisibility(View.VISIBLE);
                                            rejectedMark.setVisibility(View.GONE);
                                            appoveStatusHRD.setVisibility(View.GONE);
                                            ttdHRD.setVisibility(View.VISIBLE);
                                            approverHrdTV.setVisibility(View.VISIBLE);

                                            String signature_approver_hrd = detail.getString("signature_approver_hrd");
                                            String url_approver_hrd = "https://hrisgelora.co.id/upload/digital_signature/"+signature_approver_hrd;

                                            Picasso.get().load(url_approver_hrd).networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(ttdHRD);

                                            String[] shortName3 = nama_approver_hrd.split(" ");

                                            if(shortName3.length>1){
                                                if(shortName3[0].length()<3){
                                                    approverHrdTV.setText(shortName3[1].toUpperCase());
                                                } else {
                                                    approverHrdTV.setText(shortName3[0].toUpperCase());
                                                }
                                            } else {
                                                approverHrdTV.setText(shortName3[0].toUpperCase());
                                            }

                                            // String namaPendek = nama_approver_hrd;
                                            // if(namaPendek.contains(" ")){
                                            //    namaPendek = namaPendek.substring(0, namaPendek.indexOf(" "));
                                            //    approverHrdTV.setText(namaPendek.toUpperCase());
                                            // }
                                        }
                                    } else if (status_approve_hrd.equals("2")){
                                        acceptedMark.setVisibility(View.GONE);
                                        rejectedMark.setVisibility(View.VISIBLE);
                                        appoveStatusHRD.setVisibility(View.GONE);
                                        approverHrdTV.setVisibility(View.VISIBLE);
                                        catatanHRDPart.setVisibility(View.VISIBLE);

                                        String keterangan_penolakan = detail.getString("keterangan_penolakan");
                                        if(keterangan_penolakan.equals("")||keterangan_penolakan.equals("null")){
                                            catatanHRDPart.setVisibility(View.GONE);
                                        } else {
                                            if(keterangan_penolakan.length()==1){
                                                catatanHRDPart.setVisibility(View.GONE);
                                            } else {
                                                catatanHRDPart.setVisibility(View.VISIBLE);
                                                catatanHRDTV.setText(keterangan_penolakan);
                                            }
                                        }

                                        String[] shortName3 = nama_approver_hrd.split(" ");

                                        if(shortName3.length>1){
                                            if(shortName3[0].length()<3){
                                                approverHrdTV.setText(shortName3[1].toUpperCase());
                                            } else {
                                                approverHrdTV.setText(shortName3[0].toUpperCase());
                                            }
                                        } else {
                                            approverHrdTV.setText(shortName3[0].toUpperCase());
                                        }

                                        // String namaPendek = nama_approver_hrd;
                                        // if(namaPendek.contains(" ")){
                                        //    namaPendek = namaPendek.substring(0, namaPendek.indexOf(" "));
                                        //    approverHrdTV.setText(namaPendek.toUpperCase());
                                        // }

                                    } else {
                                        acceptedMark.setVisibility(View.GONE);
                                        rejectedMark.setVisibility(View.GONE);
                                        appoveStatusHRD.setVisibility(View.GONE);

                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);

                                    }

                                } else if(status_approve.equals("2")) {
                                    actionPart.setVisibility(View.GONE);
                                    rejectedMark.setVisibility(View.VISIBLE);
                                    supervisorTV.setVisibility(View.VISIBLE);
                                    cancelPermohonanBTN.setVisibility(View.GONE);
                                    editPermohonanBTN.setVisibility(View.GONE);

                                    String approver = detail.getString("approver");

                                    String[] shortName2 = approver.split(" ");

                                    if(shortName2.length>1){
                                        if(shortName2[0].length()<3){
                                            supervisorTV.setText(shortName2[1].toUpperCase());
                                        } else {
                                            supervisorTV.setText(shortName2[0].toUpperCase());
                                        }
                                    } else {
                                        supervisorTV.setText(shortName2[0].toUpperCase());
                                    }

                                    // String shortName2 = approver+" ";
                                    // if(shortName2.contains(" ")){
                                    //    shortName2 = shortName2.substring(0, shortName2.indexOf(" "));
                                    //    supervisorTV.setText(shortName2.toUpperCase());
                                    // }

                                } else {
                                    actionPart.setVisibility(View.VISIBLE);
                                    rejectedMark.setVisibility(View.GONE);
                                    supervisorTV.setVisibility(View.GONE);

                                    cancelPermohonanBTN.setVisibility(View.VISIBLE);
                                    editPermohonanBTN.setVisibility(View.VISIBLE);

                                }

                                if (kode.equals("form")){
                                    actionPart.setVisibility(View.GONE);
                                } else {
                                    if (nik_karyawan.equals(sharedPrefManager.getSpNik())){
                                        actionRead();
                                        actionPart.setVisibility(View.GONE);
                                    } else {
                                        cancelPermohonanBTN.setVisibility(View.GONE);
                                        editPermohonanBTN.setVisibility(View.GONE);
                                        if(status_approve.equals("1")){
                                            actionPart.setVisibility(View.GONE);
                                        } else if (status_approve.equals("2")){
                                            actionPart.setVisibility(View.GONE);
                                        } else {
                                            actionPart.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_izin_record", idIzinRecord);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void actionRead() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/read_notif_izin";
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_izin_record", idIzinRecord);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataDetailPermohonan();
        if(statusKondisi.equals("1")){
            statusKondisi = "0";
            recheckSignature();
        }
    }

    private void createPDF(Bitmap bitmap) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(960,1280,1).create();
        PdfDocument.Page page = pdfDocument.startPage(myPageInfo);

        page.getCanvas().drawBitmap(bitmap,10,10, null);
        pdfDocument.finishPage(page);

        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"HRIS Mobile");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return;
        }
        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+".pdf";
        File myPDFFile = new File(filename);

        try {
            myPDFFile.createNewFile();
            pdfDocument.writeTo(new FileOutputStream(myPDFFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanDocuments(DetailPermohonanIzinActivity.this,myPDFFile.getAbsolutePath());
        pdfDocument.close();
    }

    private File saveBitMap(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"HRIS Mobile");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
        File pictureFile = new File(filename);
        bitmap = getBitmapFromView(drawView);
        //Bitmap resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //relative
            createPDF(bitmap);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context,pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    uriImage = String.valueOf(uri);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanDocuments(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    uriImage2 = String.valueOf(uri);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectionFailed(){
        CookieBar.build(DetailPermohonanIzinActivity.this)
            .setTitle("Perhatian")
            .setMessage("Koneksi anda terputus!")
            .setTitleColor(R.color.colorPrimaryDark)
            .setMessageColor(R.color.colorPrimaryDark)
            .setBackgroundColor(R.color.warningBottom)
            .setIcon(R.drawable.warning_connection_mini)
            .setCookiePosition(CookieBar.BOTTOM)
            .show();
    }

    private void downloadPermohonan(){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(file_url));
        title = URLUtil.guessFileName(file_url, null, null);
        request.setTitle(title);
        request.setDescription("Mengunduh Permohonan Izin...");
        String cookie = CookieManager.getInstance().getCookie(file_url);
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        pDialog = new KAlertDialog(DetailPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Downloading...");
        pDialog.show();
        pDialog.setCancelable(false);
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                i++;
                switch (i) {
                    case 0:
                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                (DetailPermohonanIzinActivity.this, R.color.colorGradien));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                (DetailPermohonanIzinActivity.this, R.color.colorGradien2));
                        break;
                    case 2:
                    case 6:
                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                (DetailPermohonanIzinActivity.this, R.color.colorGradien3));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                (DetailPermohonanIzinActivity.this, R.color.colorGradien4));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                (DetailPermohonanIzinActivity.this, R.color.colorGradien5));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                (DetailPermohonanIzinActivity.this, R.color.colorGradien6));
                        break;
                }
            }
            public void onFinish() {
                i = -1;
            }
        }.start();


    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        pDialog.setTitleText("Download Berhasil")
                .setContentText("File permohonan berhasil diunduh")
                .setConfirmText("    OK    ")
                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}