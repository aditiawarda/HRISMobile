package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.databinding.ActivityDetailIzinKeluarBinding;
import com.gelora.absensi.databinding.DialogQrIzinKeluarBinding;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.ApprovalAtasanResponse;
import com.gelora.absensi.model.ApprovalSatpamResponse;
import com.gelora.absensi.model.DetailKaryawanKeluar;
import com.gelora.absensi.network.Repository;
import com.gelora.absensi.support.EncryptionUtils;
import com.gelora.absensi.viewmodel.ConnectivityViewModel;
import com.gelora.absensi.viewmodel.ScreenshotViewModel;
import com.google.zxing.WriterException;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailIzinKeluar extends AppCompatActivity {
    private Repository repository;
    private ActivityDetailIzinKeluarBinding _binding = null;
    private ActivityDetailIzinKeluarBinding getBinding() {
        return _binding;
    }
    private DialogQrIzinKeluarBinding qrDialogBinding;
    private String checkApprovalAtasan ;
    private String checkApprovalSatpam ;
    ApprovalAtasanResponse approvalAtasanResponse;
    ApprovalSatpamResponse approvalSatpamResponse;
    DetailKaryawanKeluar detail;
    String getApprovalId;
    String getNikPemohon;
    String statusStop = "0";
    KAlertDialog pDialog;
    private int i = -1;
    private ConnectivityViewModel networkViewModel;
    private Handler handler = new Handler();
    String getNamaSatpam;
    String isKembali = null;
    private ScreenshotViewModel viewModel;
    private int currentRoleIndex = 0;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();
        _binding = ActivityDetailIzinKeluarBinding.inflate(layoutInflater);
        View view = getBinding().getRoot();
        setContentView(view);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        networkViewModel = new ViewModelProvider(DetailIzinKeluar.this).get(ConnectivityViewModel.class);
        viewModel = new ViewModelProvider(this).get(ScreenshotViewModel.class);

        getBinding().backBtn.setOnClickListener(view1 ->  {
            DetailIzinKeluar.this.finish();
        });

        getBinding().appbar.setOnClickListener(view1 -> {});

        getBinding().downloadBtn.setOnClickListener(view1 -> {
            new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Unduh File?")
                    .setContentText("File dokumen permohonan hasil unduh berformat PDF")
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
                            String link = "https://hrisgelora.co.id/api/download_pdf_izin_keluar_kantor/"+getApprovalId;
                            pDialog = new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                            pDialog.show();
                            pDialog.setCancelable(false);
                            new CountDownTimer(1000, 500) {
                                public void onTick(long millisUntilFinished) {
                                    i++;
                                    switch (i) {
                                        case 0:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien));
                                            break;
                                        case 1:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien2));
                                            break;
                                        case 2:
                                        case 6:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien3));
                                            break;
                                        case 3:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien4));
                                            break;
                                        case 4:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien5));
                                            break;
                                        case 5:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien6));
                                            break;
                                    }
                                }
                                public void onFinish() {
                                    i = -1;
                                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                                    webIntent.setData(Uri.parse(link));
                                    try {
                                        startActivity(webIntent);
                                        pDialog.dismiss();
                                    } catch (SecurityException e) {
                                        e.printStackTrace();
                                        new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.WARNING_TYPE)
                                                .setTitleText("Perhatian")
                                                .setContentText("Terjadi kesalahan, tidak dapat membuka browser")
                                                .setConfirmText("TUTUP")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }.start();
                        }
                    })
                    .show();
        });

        getBinding().swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        getBinding().swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        getBinding().swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail();
                handleApprovalByJabatan();
                screenshotRestriction();
                checkInternet();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().swipeToRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getApprovalId = extras.getString("current_id");
            getNikPemohon = extras.getString("nik_pemohon");
        }

        getDetail();
        handleApprovalByJabatan();
        screenshotRestriction();
        checkInternet();

    }

    private void checkInternet(){
        networkViewModel.getIsConnected().observe(this, isConnectedNow -> {
            if (isConnectedNow) {
                getDetail();
            } else {
                connectionFailed();
            }
        });
    }

    private static String formatTime(int hours, int minutes) {
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours);
            sb.append(" jam ");
        }
        sb.append(minutes);
        sb.append(" menit");
        return sb.toString();
    }
    private void handleApprovalByJabatan(){
        getBinding().leftBtn.setOnClickListener(view -> {
            new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Perhatian")
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
                            pDialog = new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                            pDialog.show();
                            pDialog.setCancelable(false);
                            new CountDownTimer(1300, 800) {
                                public void onTick(long millisUntilFinished) {
                                    i++;
                                    switch (i) {
                                        case 0:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien));
                                            break;
                                        case 1:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien2));
                                            break;
                                        case 2:
                                        case 6:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien3));
                                            break;
                                        case 3:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien4));
                                            break;
                                        case 4:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien5));
                                            break;
                                        case 5:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien6));
                                            break;
                                    }
                                }
                                public void onFinish() {
                                    i = -1;
                                    if ((sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")) || (sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("4")) || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
                                        approvalAtasanResponse = new ApprovalAtasanResponse(
                                                getApprovalId,
                                                sharedPrefManager.getSpNik(),
                                                "2"
                                        );
                                        repository.updateApproveAtasan( approvalAtasanResponse, response-> {
                                            if (Objects.equals(response, "Success")){
                                                getDetail();
                                                pDialog.setTitleText("Berhasil Ditolak")
                                                        .setConfirmText("    OK    ")
                                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                            } else {
                                                pDialog.setTitleText("Gagal Ditolak")
                                                        .setConfirmText("    OK    ")
                                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                                            }
                                        }, Throwable::printStackTrace);
                                    } else if (sharedPrefManager.getSpIdDept().equals("21")){
                                        approvalSatpamResponse = new ApprovalSatpamResponse(
                                                getApprovalId,
                                                sharedPrefManager.getSpNik(),
                                                "2"
                                        );
                                        repository.updateApproveSatpam( approvalSatpamResponse, response-> {
                                            if (Objects.equals(response, "Success")){
                                                getDetail();
                                                pDialog.setTitleText("Berhasil Ditolak")
                                                        .setConfirmText("    OK    ")
                                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                            } else {
                                                pDialog.setTitleText("Gagal Ditolak")
                                                        .setConfirmText("    OK    ")
                                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                                            }
                                        }, Throwable::printStackTrace);
                                    }
                                }
                            }.start();
                        }
                    })
                    .show();
        });

        getBinding().rightBtn.setOnClickListener(view1 -> {
            new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Perhatian")
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
                            pDialog = new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                            pDialog.show();
                            pDialog.setCancelable(false);
                            new CountDownTimer(1300, 800) {
                                public void onTick(long millisUntilFinished) {
                                    i++;
                                    switch (i) {
                                        case 0:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien));
                                            break;
                                        case 1:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien2));
                                            break;
                                        case 2:
                                        case 6:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien3));
                                            break;
                                        case 3:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien4));
                                            break;
                                        case 4:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien5));
                                            break;
                                        case 5:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien6));
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

        });

        getBinding().batalkanBtn.setOnClickListener(view2 -> {
            new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Perhatian")
                    .setContentText("Yakin untuk membatalkan permohonan?")
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
                            pDialog = new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                            pDialog.show();
                            pDialog.setCancelable(false);
                            new CountDownTimer(1300, 800) {
                                public void onTick(long millisUntilFinished) {
                                    i++;
                                    switch (i) {
                                        case 0:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien));
                                            break;
                                        case 1:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien2));
                                            break;
                                        case 2:
                                        case 6:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien3));
                                            break;
                                        case 3:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien4));
                                            break;
                                        case 4:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien5));
                                            break;
                                        case 5:
                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                    (DetailIzinKeluar.this, R.color.colorGradien6));
                                            break;
                                    }
                                }
                                public void onFinish() {
                                    i = -1;
                                    repository.cancelIzinKeluarKantor(getApprovalId, DetailIzinKeluar.this, new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            // Handle successful response if needed
                                            pDialog.setTitleText("Permohonan Dibatalkan")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                            onBackPressed();
                                                        }
                                                    })
                                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        }
                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            // Handle failure response or network errors
                                            pDialog.setTitleText("Permohonan Gagal Dibatalkan")
                                                    .setConfirmText("    OK    ")
                                                    .changeAlertType(KAlertDialog.ERROR_TYPE);
                                        }
                                    });

                                }
                            }.start();
                        }
                    })
                    .show();
        });

        getBinding().qrBtn.setOnClickListener(view -> {
            qrDialog();
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void getDetail (){
        repository = new Repository(this);
        repository.getDetail(Integer.parseInt(getApprovalId), response-> {
            runOnUiThread(() -> {
                Log.d("Woi1010", response.toString());
                detail = response;
                checkApprovalAtasan = detail.getStatusApproval();
                checkApprovalSatpam = detail.getStatusApprovalSatpam();
                isKembali = detail.getVerifikatorKembali();

                if (isKembali == null){
                    isKembali = "0";
                }

                if (detail.getDurasi() != null) {
                    String cleanedTimeString = detail.getDurasi().replaceAll("[0:]", "");
                    try {
                        if (isKembali.equals("1")){
                            String[] parts =  detail.getDurasiAktual().split(":");
                            int hours = Integer.parseInt(parts[0]);
                            int minutes = Integer.parseInt(parts[1]);
                            String formattedTime = formatTime(hours, minutes);
                            getBinding().detailDurasi.setText(cleanedTimeString + " Jam" + " ( Aktual : " + formattedTime + " )");
                        } else {
                            getBinding().detailDurasi.setText(cleanedTimeString + " Jam");
                        }
                    } catch (NullPointerException e){
                        getBinding().detailDurasi.setText(cleanedTimeString + " Jam");
                    }
                } else {
                    getBinding().detailDurasi.setText("-");
                }

                if(checkApprovalAtasan.equals("1") && checkApprovalSatpam.equals("1") && isKembali.equals("0")){
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    getBinding().jamKembali.setText("BELUM KEMBALI");
                    getBinding().satpamVerifikator.setText("-");
                    getBinding().jamKembali.setTextColor(ContextCompat.getColor(DetailIzinKeluar.this, R.color.heavyRed));
                } else if (checkApprovalAtasan.equals("1") && checkApprovalSatpam.equals("1") && isKembali.equals("1")){
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    getBinding().jamKembali.setText(detail.getJamKembali());
                    getBinding().satpamVerifikator.setText(detail.getNamaVerifSatpam());
                    getBinding().jamKembali.setTextColor(Color.parseColor("#515151"));
                } else {
                    if(checkApprovalAtasan.equals("1")){
                        getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    } else {
                        if(sharedPrefManager.getSpNik().equals(getNikPemohon) && checkApprovalAtasan.equals("0") && checkApprovalSatpam.equals("0")){
                            getBinding().layoutUserBiasa.setVisibility(View.VISIBLE);
                        } else {
                            getBinding().layoutUserBiasa.setVisibility(View.GONE);
                        }
                    }
                    getBinding().jamKembali.setText("-");
                    getBinding().jamKembali.setTextColor(Color.parseColor("#515151"));
                }

                if (Objects.equals(checkApprovalAtasan, "2") || Objects.equals(checkApprovalAtasan, "1") && Objects.equals(checkApprovalSatpam, "2")){
                    getBinding().stampleImg.setImageDrawable(getResources().getDrawable(R.drawable.rejected_img));
                    getBinding().stampleImg.setVisibility(View.VISIBLE);
                }
                if (Objects.equals(checkApprovalAtasan, "1") && Objects.equals(checkApprovalSatpam, "1")){
                    getBinding().stampleImg.setImageDrawable(getResources().getDrawable(R.drawable.accepted_img));
                    getBinding().stampleImg.setVisibility(View.VISIBLE);
                }

                if(getDate().equals(detail.getTanggal())){
                    handleLayoutBaseOnJabatan();
                } else {
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    getBinding().layoutQr.setVisibility(View.GONE);
                }

                String originalDateString = response.getTanggal();

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                try {
                    date = inputFormat.parse(originalDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.forLanguageTag("id-ID"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
                String formattedDate = outputFormat.format(date);

                getBinding().waktuPengajuan.setText("JAKARTA, " + formattedDate);
                getBinding().detailJam.setText(detail.getJamKeluar());
                getBinding().detailNama.setText(detail.getNamaKaryawan().toUpperCase());
                getBinding().detailBagian.setText(detail.getBagian());

                if (!Objects.equals(detail.getStatusApproval(), "0") && detail.getStatusApproval() != null) {
                    String[] short_name = detail.getNamaAtasan().split(" ");
                    if(short_name.length>1){
                        if(short_name[0].length()<3){
                            getBinding().namaAtasan.setText("( "+short_name[1].toUpperCase()+" )");
                        } else {
                            getBinding().namaAtasan.setText("( "+short_name[0].toUpperCase()+" )");
                        }
                    } else {
                        getBinding().namaAtasan.setText("( "+short_name[0].toUpperCase()+" )");
                    }
                    if ("1".equals(detail.getStatusApproval())) {
                        String ttdAtasan = repository.getSignature(detail.getTtdAtasan());
                        try {
                            Glide.with(DetailIzinKeluar.this)
                                    .load(ttdAtasan)
                                    .into(getBinding().ttdSupervisor);
                        } catch (IllegalArgumentException e){
                            Log.e("Error : ", e.toString());
                        }
                    }
                } else {
                    getBinding().namaAtasan.setText("( ............... )");
                }

                if (!Objects.equals(detail.getStatusApprovalSatpam(), "0") && detail.getStatusApprovalSatpam() != null) {
                    String[] short_name = detail.getNamaSatpam().split(" ");
                    if(short_name.length>1){
                        if(short_name[0].length()<3){
                            getBinding().namaSatpam.setText("( "+short_name[1].toUpperCase()+" )");
                        } else {
                            getBinding().namaSatpam.setText("( "+short_name[0].toUpperCase()+" )");
                        }
                    } else {
                        getBinding().namaSatpam.setText("( "+short_name[0].toUpperCase()+" )");
                    }
                    if ("1".equals(detail.getStatusApprovalSatpam())) {
                        String ttdSatpam = repository.getSignature(detail.getTtdSatpam());
                        Glide.with(DetailIzinKeluar.this)
                                .load(ttdSatpam)
                                .into(getBinding().ttdSatpam);
                    }
                } else {
                    getBinding().namaSatpam.setText("( ............... )");
                }

                getBinding().detailKeperluan.setText(detail.getKeperluan());
                String[] short_name = detail.getNamaKaryawan().split(" ");
                if(short_name.length>1){
                    if(short_name[0].length()<3){
                        getBinding().namaPemohon.setText("( "+short_name[1].toUpperCase()+" )");
                    } else {
                        getBinding().namaPemohon.setText("( "+short_name[0].toUpperCase()+" )");
                    }
                } else {
                    getBinding().namaPemohon.setText("( "+short_name[0].toUpperCase()+" )");
                }

                String ttdPemohon =  repository.getSignature(detail.getTtdPemohon());
                Glide.with(DetailIzinKeluar.this)
                        .load(ttdPemohon)
                        .into(getBinding().ttdPemohon);
            });

        }, codeResponse -> {
            if (codeResponse.equals("Success")){
                getBinding().loadingDataPart.setVisibility(View.GONE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
            }
        }, Throwable::printStackTrace);
    }

    private void connectionFailed(){
        CookieBar.build(DetailIzinKeluar.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void handleLayoutBaseOnJabatan (){
        if ("1".equals(checkApprovalAtasan) && "1".equals(checkApprovalSatpam) && isKembali.equals("0")) {
            if(sharedPrefManager.getSpNik().equals(getNikPemohon)){
                getBinding().layoutQr.setVisibility(View.VISIBLE);
            } else {
                getBinding().layoutQr.setVisibility(View.GONE);
            }
        }

        if ((sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")) || (sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("4")) || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
            if (Objects.equals(sharedPrefManager.getSpNik(), getNikPemohon)){
                getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                if(checkApprovalAtasan.equals("1")){
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                } else {
                    if(sharedPrefManager.getSpNik().equals(getNikPemohon) && checkApprovalAtasan.equals("0") && checkApprovalSatpam.equals("0")){
                        getBinding().layoutUserBiasa.setVisibility(View.VISIBLE);
                    } else {
                        getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    }
                }
            } else {
                if (Objects.equals(checkApprovalAtasan, "0")) {
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.VISIBLE);
                    if(sharedPrefManager.getSpNik().equals(getNikPemohon) && checkApprovalAtasan.equals("0") && checkApprovalSatpam.equals("0")){
                        getBinding().layoutUserBiasa.setVisibility(View.VISIBLE);
                    } else {
                        getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    }
                } else {
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                }
            }
        } else if ("1".equals(checkApprovalAtasan) && sharedPrefManager.getSpIdDept().equals("21")) {
            if (Objects.equals(sharedPrefManager.getSpNik(), getNikPemohon)){
                getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                getBinding().layoutUserBiasa.setVisibility(View.GONE);
            } else {
                if (Objects.equals(checkApprovalSatpam, "0")){
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.VISIBLE);
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                } else {
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                }
            }
        } else if ("0".equals(checkApprovalAtasan) && sharedPrefManager.getSpIdDept().equals("21")) {
                getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                if(sharedPrefManager.getSpNik().equals(getNikPemohon) && checkApprovalAtasan.equals("0") && checkApprovalSatpam.equals("0")){
                    getBinding().layoutUserBiasa.setVisibility(View.VISIBLE);
                } else {
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                }
        } else {
            if (Objects.equals(sharedPrefManager.getSpNik(), getNikPemohon)){
                if("0".equals(checkApprovalAtasan)){
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                    if(sharedPrefManager.getSpNik().equals(getNikPemohon) && checkApprovalAtasan.equals("0") && checkApprovalSatpam.equals("0")){
                        getBinding().layoutUserBiasa.setVisibility(View.VISIBLE);
                    } else {
                        getBinding().layoutUserBiasa.setVisibility(View.GONE);
                    }
                } else {
                    getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                    getBinding().layoutUserBiasa.setVisibility(View.GONE);
                }
            } else {
                getBinding().layoutAtasanDanSatpam.setVisibility(View.GONE);
                getBinding().layoutUserBiasa.setVisibility(View.GONE);
            }
        }
    }

    private void screenshotRestriction(){
        viewModel.getIsDialogShowing().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDialogShowing) {
                if (isDialogShowing) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            }
        });
    }

    private void qrDialog() {
        qrDialogBinding = DialogQrIzinKeluarBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);
        builder.setView(qrDialogBinding.getRoot());

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                qrDialogBinding.closeBtn.setOnClickListener(view -> {
                    dialog.dismiss();
                });
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (statusStop.equals("0")) {
                    cekKembali(getApprovalId, dialog);
                } else {
                    handler.removeCallbacks(this);
                }
                handler.postDelayed(this, 3000);
            }
        });

        generateQRCode(qrDialogBinding.qrImg);

        dialog.show();
        viewModel.setDialogShowing(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                viewModel.setDialogShowing(false);
            }
        });
    }

    private void cekKembali(String id, Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cek_kembali_izin_keluar_kantor";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Done")){
                                String jam_kembali = data.getString("jam_kembali");
                                dialog.dismiss();
                                getDetail();
                                statusStop = "1";
                                getBinding().qrBtn.setVisibility(View.GONE);
                                try {
                                    new KAlertDialog(DetailIzinKeluar.this, KAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Berhasil")
                                            .setContentText("Data izin keluar kantor yang anda ajukan berhasil diverifikasi. jam kembali anda pukul "+jam_kembali)
                                            .setConfirmText("    OK    ")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                }
                                            })
                                            .show();
                                } catch (WindowManager.BadTokenException e){
                                    Log.e("Error", e.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()
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

    private void generateQRCode(ImageView imgView) {
        String secureToken = UUID.randomUUID().toString();
        String key = "hris_ijin_keluar";
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject.put("key", key);
            jsonObject.put("id", getApprovalId);
            jsonObject.put("nik", getNikPemohon);
            String jsonString = jsonObject.toString();
            String encryptedData = EncryptionUtils.encrypt(jsonString);
            String qrContent = encryptedData + ",Tolong Scan Menggunakan Aplikasi HRIS";
            QRGEncoder qrgEncoder = new QRGEncoder(qrContent, null, QRGContents.Type.TEXT, 500);
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            imgView.setImageBitmap(bitmap);
            Log.d("woi010", jsonString);
        } catch (JSONException | WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private void approvedPermohonan(){
        if ((sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")) || (sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("4")) || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
            approvalAtasanResponse = new ApprovalAtasanResponse(
                    getApprovalId,
                    sharedPrefManager.getSpNik(),
                    "1"
            );
            repository.updateApproveAtasan( approvalAtasanResponse, response-> {
                if (Objects.equals(response, "Success")){
                    getDetail();
                    pDialog.setTitleText("Berhasil Disetujui")
                            .setConfirmText("    OK    ")
                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                } else {
                    pDialog.setTitleText("Gagal Disetujui")
                            .setConfirmText("    OK    ")
                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                }
            }, Throwable::printStackTrace);
        }
        else if (sharedPrefManager.getSpIdDept().equals("21")){
            approvalSatpamResponse = new ApprovalSatpamResponse(
                    getApprovalId,
                    sharedPrefManager.getSpNik(),
                    "1"
            );
            repository.updateApproveSatpam( approvalSatpamResponse, response-> {
                if (Objects.equals(response, "Success")){
                    getDetail();
                    pDialog.setTitleText("Berhasil Disetujui")
                            .setConfirmText("    OK    ")
                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                } else {
                    pDialog.setTitleText("Gagal Disetujui")
                            .setConfirmText("    OK    ")
                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                }
            }, Throwable::printStackTrace);
        }
    }

    private void checkSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cek_ttd_digital";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
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
                                approvedPermohonan();
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
                                                Intent intent = new Intent(DetailIzinKeluar.this, DigitalSignatureActivity.class);
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
                new com.android.volley.Response.ErrorListener()
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

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}