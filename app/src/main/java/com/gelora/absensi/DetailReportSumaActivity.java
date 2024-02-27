package com.gelora.absensi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.takisoft.datetimepicker.DatePickerDialog;

import net.cachapa.expandablelayout.ExpandableLayout;
import net.gotev.uploadservice.MultipartUploadRequest;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DetailReportSumaActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout noSuratJalanPart, submitRescheduleBTN, choiceDateBTN, rescheduleBTN, reschedulePart, totalPenagihanPart, totalPesananPart, viewRealisasiBTN, realMark, submitRealisasiBTN, viewLampiranRealisasiBTN, fotoLampiranRealisasiBTN, gpsRealisasiBTN, updateRealisasiBTN, viewLampiranBTN, tglRencanaPart, backBTN, actionBar, mapsPart, updateRealisasiPart;
    SharedPrefManager sharedPrefManager;
    EditText keteranganKunjunganRealisasiED;
    ExpandableLayout updateRealisasiForm, rescheduleForm;
    RequestQueue requestQueue;
    TextView noSuratJalanTV, countImageTV, choiceDateTV, totalPenagihanTV, totalPesananTV, tanggalBuatTV, labelLampiranTV, detailLocationRealisasiTV, tglRencanaTV, nikSalesTV, namaSalesTV, detailLocationTV, reportKategoriTV, namaPelangganTV, alamatPelangganTV, picPelangganTV, teleponPelangganTV, keteranganTV;
    String idReport = "";
    SwipeRefreshLayout refreshLayout;
    SharedPrefAbsen sharedPrefAbsen;
    private StatusBarColorManager mStatusBarColorManager;
    private LatLng userPoint;
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    ResultReceiver resultReceiver;
    Context mContext;
    Activity mActivity;
    String locationNow = "", salesLat = "", salesLong = "", choiceDateReschedule = "";
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private List<String> lampiranImage = new ArrayList<>();
    KAlertDialog pDialog;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report_suma);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        resultReceiver = new DetailReportSumaActivity.AddressResultReceiver(new Handler());
        mContext = getBaseContext();
        mActivity = DetailReportSumaActivity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
        idReport = getIntent().getExtras().getString("report_id");

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        reportKategoriTV = findViewById(R.id.report_kategori_tv);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        namaPelangganTV = findViewById(R.id.nama_pelanggan_tv);
        alamatPelangganTV = findViewById(R.id.alamat_pelanggan_tv);
        picPelangganTV = findViewById(R.id.pic_pelanggan_tv);
        teleponPelangganTV = findViewById(R.id.telepon_pelanggan_tv);
        keteranganTV = findViewById(R.id.keterangan_tv);
        detailLocationTV = findViewById(R.id.detail_location_tv);
        namaSalesTV = findViewById(R.id.nama_sales_tv);
        nikSalesTV = findViewById(R.id.nik_sales_tv);
        mapsPart = findViewById(R.id.maps_part);
        tglRencanaPart = findViewById(R.id.tgl_rencana_part);
        tglRencanaTV = findViewById(R.id.tgl_rencana_tv);
        viewLampiranBTN = findViewById(R.id.view_lampiran_btn);
        updateRealisasiBTN = findViewById(R.id.update_realisasi_btn);
        updateRealisasiForm = findViewById(R.id.update_realisasi_form);
        updateRealisasiPart = findViewById(R.id.update_realisasi_part);
        gpsRealisasiBTN = findViewById(R.id.gps_realisasi_btn);
        detailLocationRealisasiTV = findViewById(R.id.detail_location_realisasi_tv);
        fotoLampiranRealisasiBTN = findViewById(R.id.foto_lampiran_realisasi_btn);
        viewLampiranRealisasiBTN = findViewById(R.id.view_lampiran_realisasi_btn);
        labelLampiranTV = findViewById(R.id.label_lampiran_tv);
        submitRealisasiBTN = findViewById(R.id.submit_realisasi_btn);
        realMark = findViewById(R.id.real_mark);
        viewRealisasiBTN = findViewById(R.id.view_realisasi_btn);
        tanggalBuatTV = findViewById(R.id.tanggal_buat_tv);
        totalPesananPart = findViewById(R.id.total_pesanan_part);
        totalPesananTV = findViewById(R.id.total_pesanan_tv);
        totalPenagihanPart = findViewById(R.id.total_penagihan_part);
        totalPenagihanTV = findViewById(R.id.total_piutang_tv);
        noSuratJalanPart = findViewById(R.id.no_surat_jalan_part);
        noSuratJalanTV = findViewById(R.id.no_surat_jalan_tv);
        reschedulePart = findViewById(R.id.reschedule_part);
        rescheduleBTN = findViewById(R.id.reschedule_btn);
        rescheduleForm = findViewById(R.id.reschedule_form);
        choiceDateBTN = findViewById(R.id.choice_date_btn);
        choiceDateTV = findViewById(R.id.choice_date_tv);
        submitRescheduleBTN = findViewById(R.id.submit_reschedule_btn);
        keteranganKunjunganRealisasiED = findViewById(R.id.keterangan_kunjungan_ed);
        countImageTV = findViewById(R.id.count_image_tv);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rescheduleForm.collapse();
                rescheduleForm.collapse();
                choiceDateTV.setText("");
                choiceDateReschedule = "";
                lampiranImage.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        permissionLoc();
                    }
                }, 1000);
            }
        });

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rescheduleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rescheduleForm.isExpanded()){
                    rescheduleForm.collapse();
                } else {
                    rescheduleForm.expand();
                    updateRealisasiForm.collapse();
                }
            }
        });

        choiceDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerOfDate();
            }
        });

        updateRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateRealisasiForm.isExpanded()){
                    updateRealisasiForm.collapse();
                } else {
                    updateRealisasiForm.expand();
                    rescheduleForm.collapse();
                }
            }
        });

        gpsRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailReportSumaActivity.this, LocationPickerActivity.class);
                startActivity(intent);
            }
        });

        fotoLampiranRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        submitRescheduleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!choiceDateReschedule.equals("")){
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Kirim penjadwalan ulang?")
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
                                    pDialog = new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            submitReschedule();
                                        }
                                    }.start();
                                }
                            })
                            .show();

                } else {
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi data tanggal!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        submitRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!salesLat.equals("0")&&!salesLong.equals("0")) && !salesLat.equals("") && !salesLong.equals("") && lampiranImage.size()!=0 && !keteranganKunjunganRealisasiED.getText().toString().equals("")){
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Update realisasi sekarang?")
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
                                    pDialog = new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            submitRealisasi();
                                        }
                                    }.start();
                                }
                            })
                            .show();

                } else {
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap masukkan keterangan, unggah lampiran dan pastikan posisi GPS sesuai!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0,0,0,0);
        permissionLoc();
    }

    private void permissionLoc(){
        if (ContextCompat.checkSelfPermission(DetailReportSumaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(DetailReportSumaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailReportSumaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            mMap.clear();
            getData();
            userPosition();
        }
    }

    @SuppressLint("MissingPermission")
    private void userPosition() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    Log.e("TAG", "GPS is on" + String.valueOf(location));
                    salesLat = String.valueOf(location.getLatitude());
                    salesLong = String.valueOf(location.getLongitude());

                    Location getLoc = new Location("dummyProvider");
                    getLoc.setLatitude(location.getLatitude());
                    getLoc.setLongitude(location.getLongitude());
                    new ReverseGeocodingTaskRealisasi().execute(getLoc);
                }  else {
                    gpsEnableAction();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error trying to get last GPS location");
                e.printStackTrace();
                gpsEnableAction();
            }
        });

    }

    private void gpsEnableAction(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(DetailReportSumaActivity.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        DetailReportSumaActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private void pickerOfDate(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(DetailReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                choiceDateReschedule = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(choiceDateReschedule);
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long choice = date.getTime();
                long now = date2.getTime();

                if (choice>=now){
                    String input_date = choiceDateReschedule;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
                    DateFormat getweek = new SimpleDateFormat("W");
                    String finalDay = format2.format(dt1);
                    String week = getweek.format(dt1);
                    String hariName = "";

                    if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                        hariName = "Senin";
                    } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                        hariName = "Selasa";
                    } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                        hariName = "Rabu";
                    } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                        hariName = "Kamis";
                    } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                        hariName = "Jumat";
                    } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                        hariName = "Sabtu";
                    } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                        hariName = "Minggu";
                    }

                    String dayDate = input_date.substring(8,10);
                    String yearDate = input_date.substring(0,4);
                    String bulanValue = input_date.substring(5,7);
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

                    choiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                } else {
                    choiceDateTV.setText("Pilih Kembali !");
                    choiceDateReschedule = "";

                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tidak bisa memilih tanggal lampau!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(DetailReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                choiceDateReschedule = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(choiceDateReschedule);
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long choice = date.getTime();
                long now = date2.getTime();

                if (choice>=now){
                    String input_date = choiceDateReschedule;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
                    DateFormat getweek = new SimpleDateFormat("W");
                    String finalDay = format2.format(dt1);
                    String week = getweek.format(dt1);
                    String hariName = "";

                    if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                        hariName = "Senin";
                    } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                        hariName = "Selasa";
                    } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                        hariName = "Rabu";
                    } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                        hariName = "Kamis";
                    } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                        hariName = "Jumat";
                    } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                        hariName = "Sabtu";
                    } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                        hariName = "Minggu";
                    }

                    String dayDate = input_date.substring(8,10);
                    String yearDate = input_date.substring(0,4);
                    String bulanValue = input_date.substring(5,7);
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

                    choiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                } else {
                    choiceDateTV.setText("Pilih Kembali !");
                    choiceDateReschedule = "";

                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tidak bisa memilih tanggal lampau!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, y,m-1,d);
            dpd.show();
        }


    }

    private void submitReschedule(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/update_reschedule";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")) {
                                String input_date = choiceDateReschedule;
                                String dayDate = input_date.substring(8,10);
                                String yearDate = input_date.substring(0,4);
                                String bulanValue = input_date.substring(5,7);
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

                                String tanggal = String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate;
                                pDialog.setTitleText("Berhasil")
                                        .setContentText("Tanggal rencana kunjungan berhasil dijadwalkan ulang ke tanggal "+tanggal)
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                getData();
                                                rescheduleForm.collapse();
                                                choiceDateReschedule = "";
                                                choiceDateTV.setText("");
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                pDialog.setTitleText("Gagal Terkirim")
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
                        Log.d("Error.Response", error.toString());
                        pDialog.setTitleText("Gagal Terkirim")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.ERROR_TYPE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_report", idReport);
                params.put("tanggal_rencana", choiceDateReschedule);
                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    private void submitRealisasi(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/update_realisasi";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")) {
                                String idLaporan = data.getString("idLaporan");
                                String filename = data.getString("file_name");
                                uploadLampiran(filename, idLaporan);
                            } else {
                                pDialog.setTitleText("Gagal Terkirim")
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
                        Log.d("Error.Response", error.toString());
                        pDialog.setTitleText("Gagal Terkirim")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.ERROR_TYPE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_report", idReport);
                params.put("latitude", salesLat);
                params.put("longitude", salesLong);
                params.put("keterangan", keteranganKunjunganRealisasiED.getText().toString());
                params.put("jumlah_lampiran", String.valueOf(lampiranImage.size()));
                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    @SuppressLint("SetTextI18n")
    public void uploadLampiran(String filename, String idReport) {
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/upload_lampiran";
        String cleanString = filename.substring(1, filename.length() - 1);
        String[] parts = cleanString.split(",");

        for (int i = 0; i < lampiranImage.size(); i++) {
            String path = FilePathimage.getPath(this, Uri.parse(String.valueOf(lampiranImage.get(i))));
            if (path == null) {
                Toast.makeText(this, "Please move your image file to internal storage and retry", Toast.LENGTH_LONG).show();
            } else {
                String uploadId = UUID.randomUUID().toString();
                try {
                    new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                            .addFileToUpload(path, "image")
                            .addParameter("filename", parts[i])
                            .setMaxRetries(2)
                            .startUpload();
                } catch (Exception exc) {
                    Log.e("UploadError", "Error uploading file", exc);
                }
            }
            if(lampiranImage.size()-1==i){
                pDialog.dismiss();
                updateRealisasiPart.setVisibility(View.GONE);
                viewRealisasiBTN.setVisibility(View.VISIBLE);
                viewRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                        intent.putExtra("report_id",idReport);
                        startActivity(intent);
                    }
                });

                getData();
            }
        }
    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(DetailReportSumaActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withActivity(DetailReportSumaActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, "suma_report");
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(DetailReportSumaActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(DetailReportSumaActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 600);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 900);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(DetailReportSumaActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        String file_directori = getRealPathFromURIPath(uri, DetailReportSumaActivity.this);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        @SuppressLint("Recycle")
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                try {
                    if(extension.equals(".jpg")||extension.equals(".JPG")){
                        lampiranImage.add(stringUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, DetailReportSumaActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);

                        countImageTV.setText(String.valueOf(lampiranImage.size()));
                        if(lampiranImage.size()>=2){
                            fotoLampiranRealisasiBTN.setVisibility(View.GONE);
                        } else {
                            fotoLampiranRealisasiBTN.setVisibility(View.VISIBLE);
                        }
                        viewLampiranRealisasiBTN.setVisibility(View.VISIBLE);
                        labelLampiranTV.setText("+ Lampiran Foto/SP");
                        viewLampiranRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                intent.putExtra("data", String.valueOf(lampiranImage));
                                startActivity(intent);
                            }
                        });

                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Format file tidak sesuai!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }, 800);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/report_detail";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "MissingPermission"})
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
                                String latitude = dataArray.getString("latitude");
                                String longitude = dataArray.getString("longitude");
                                String idSales = dataArray.getString("idSales");
                                getSales(idSales);
                                String tipeLaporan = dataArray.getString("tipeLaporan");
                                String createdAt = dataArray.getString("createdAt");

                                tanggalBuatTV.setText(createdAt.substring(8,10)+"/"+createdAt.substring(5,7)+"/"+createdAt.substring(0,4)+" "+createdAt.substring(10,16));

                                if(tipeLaporan.equals("1")){
                                    viewLampiranBTN.setVisibility(View.GONE);
                                    reportKategoriTV.setText("RENCANA KUNJUNGAN");
                                    tglRencanaPart.setVisibility(View.VISIBLE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);
                                    String tgl_rencana = dataArray.getString("tanggalRencana");

                                    String input_date = tgl_rencana;
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dt1 = null;
                                    try {
                                        dt1 = format1.parse(input_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    @SuppressLint("SimpleDateFormat")
                                    DateFormat format2 = new SimpleDateFormat("EEE");
                                    String finalDay = format2.format(dt1);
                                    String hariName = "";

                                    if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                                        hariName = "Senin";
                                    } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                                        hariName = "Selasa";
                                    } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                                        hariName = "Rabu";
                                    } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                                        hariName = "Kamis";
                                    } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                                        hariName = "Jumat";
                                    } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                                        hariName = "Sabtu";
                                    } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                                        hariName = "Minggu";
                                    }

                                    String dayDate = input_date.substring(8,10);
                                    String yearDate = input_date.substring(0,4);
                                    String bulanValue = input_date.substring(5,7);
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

                                    tglRencanaTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                                    String statusRealisasi = dataArray.getString("statusRealisasi");
                                    if(statusRealisasi.equals("0")){
                                        reschedulePart.setVisibility(View.VISIBLE);
                                        @SuppressLint("SimpleDateFormat")
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = null;
                                        Date date2 = null;
                                        try {
                                            date = sdf.parse(tgl_rencana);
                                            date2 = sdf.parse(getDate());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        long rencana = date.getTime();
                                        long now = date2.getTime();

                                        if (rencana<=now){
                                            updateRealisasiPart.setVisibility(View.VISIBLE);
                                        } else {
                                            updateRealisasiPart.setVisibility(View.GONE);
                                        }

                                        realMark.setVisibility(View.GONE);
                                        viewRealisasiBTN.setVisibility(View.GONE);
                                    } else if(statusRealisasi.equals("1")){
                                        reschedulePart.setVisibility(View.GONE);
                                        updateRealisasiPart.setVisibility(View.GONE);
                                        realMark.setVisibility(View.VISIBLE);
                                        viewRealisasiBTN.setVisibility(View.VISIBLE);
                                        String realisasi = data.getString("realisasi");
                                        viewRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                                                intent.putExtra("report_id",realisasi);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                } else if(tipeLaporan.equals("2")){
                                    updateRealisasiPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("LAPORAN KUNJUNGAN");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.VISIBLE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    Locale localeID = new Locale("id", "ID");
                                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                    decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                    decimalFormat.setMaximumFractionDigits(0);

                                    String totalLaporan = dataArray.getString("totalLaporan");
                                    if(totalLaporan.equals("")||totalLaporan.equals("null")||totalLaporan.equals("0")){
                                        totalPesananTV.setText("Terlihat pada SP Manual");
                                    } else {
                                        totalPesananTV.setText(decimalFormat.format(Integer.parseInt(totalLaporan)));
                                    }

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("3")){
                                    updateRealisasiPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("AKTIVITAS PENAGIHAN");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.VISIBLE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    Locale localeID = new Locale("id", "ID");
                                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                    decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                    decimalFormat.setMaximumFractionDigits(0);

                                    String totalLaporan = dataArray.getString("totalLaporan");
                                    if(totalLaporan.equals("")||totalLaporan.equals("null")||totalLaporan.equals("0")){
                                        totalPenagihanTV.setText("Terlihat pada Faktur");
                                    } else {
                                        totalPenagihanTV.setText(decimalFormat.format(Integer.parseInt(totalLaporan)));
                                    }

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("4")){
                                    updateRealisasiPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("LAPORAN PENGIRIMAN");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.VISIBLE);

                                    String noSuratJalan = dataArray.getString("noSuratJalan");
                                    noSuratJalanTV.setText(noSuratJalan);

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                }

                                String namaPelanggan = dataArray.getString("namaPelanggan");
                                String alamat_customer = dataArray.getString("alamat_customer");
                                String pic = dataArray.getString("pic");
                                String no_telp = dataArray.getString("noTelp");
                                String keterangan = dataArray.getString("keterangan");
                                namaPelangganTV.setText(namaPelanggan);
                                alamatPelangganTV.setText(alamat_customer);
                                picPelangganTV.setText(pic);
                                teleponPelangganTV.setText(no_telp);
                                keteranganTV.setText(keterangan);

                                if(mMap != null && (tipeLaporan.equals("2") || tipeLaporan.equals("3") || tipeLaporan.equals("4"))){
                                    mapsPart.setVisibility(View.VISIBLE);
                                    mMap.setMyLocationEnabled(false);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                    userPoint = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    float zoomLevel = 17.8f;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                                    mMap.getUiSettings().setCompassEnabled(false);
                                    mMap.addMarker(new MarkerOptions().position(userPoint).title(sharedPrefManager.getSpNama()).snippet(latitude+","+longitude).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_picker_ic)));
                                    Location location = new Location("dummyProvider");
                                    location.setLatitude(Double.parseDouble(latitude));
                                    location.setLongitude(Double.parseDouble(longitude));
                                    new ReverseGeocodingTask().execute(location);
                                } else {
                                    mapsPart.setVisibility(View.GONE);
                                }

                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_report", idReport);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getSales(String nik) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_sales";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "MissingPermission"})
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String nama = data.getString("nama");
                                String NIK = data.getString("NIK");
                                namaSalesTV.setText(nama);
                                nikSalesTV.setText(NIK);
                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("nik", nik);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                if(resultData.getString(Constants.LOCAITY)!=null){
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY));
                                locationNow = resultData.getString(Constants.LOCAITY);
                            }
                        }
                    }
                } else {
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText("Lokasi tidak ditemukan");
                                locationNow = "Lokasi tidak ditemukan";
                            }
                        }
                    }
                }
            } else {
                detailLocationTV.setText(resultData.getString(Constants.NO_ADDRESS));
                locationNow = resultData.getString(Constants.NO_ADDRESS);
            }
        }

    }

    private void fetchaddressfromlocation(Location location) {
        try {
            Context context = mContext;
            Intent intent = new Intent(context, FetchAddressIntentServices.class);
            intent.putExtra(Constants.RECEVIER, resultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
            context.startService(intent);
        }
        catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void connectionFailed(){
        CookieBar.build(DetailReportSumaActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @SuppressLint("StaticFieldLeak")
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, String> {
        @Override
        protected String doInBackground(Location... params) {
            Location location = params[0];
            String addressText = "";

            Geocoder geocoder = new Geocoder(DetailReportSumaActivity.this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressBuilder.append(address.getAddressLine(i)).append(", ");
                    }
                    addressText = addressBuilder.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching address: " + e.getMessage());
            }

            return addressText;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (!address.isEmpty()) {
                Log.d(TAG, "Alamat: " + address);
                detailLocationTV.setText(address.substring(0,address.length()-2));
            } else {
                Log.e(TAG, "Alamat tidak ditemukan");
                detailLocationTV.setText("Alamat tidak ditemukan");
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ReverseGeocodingTaskRealisasi extends AsyncTask<Location, Void, String> {
        @Override
        protected String doInBackground(Location... params) {
            Location location = params[0];
            String addressText = "";

            Geocoder geocoder = new Geocoder(DetailReportSumaActivity.this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressBuilder.append(address.getAddressLine(i)).append(", ");
                    }
                    addressText = addressBuilder.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching address: " + e.getMessage());
            }

            return addressText;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (!address.isEmpty()) {
                Log.d(TAG, "Alamat: " + address);
                detailLocationRealisasiTV.setText(address.substring(0,address.length()-2));
            } else {
                Log.e(TAG, "Alamat tidak ditemukan");
                detailLocationRealisasiTV.setText("Alamat tidak ditemukan");
            }
        }
    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}