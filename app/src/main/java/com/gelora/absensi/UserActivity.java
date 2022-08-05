package com.gelora.absensi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.gelora.absensi.support.Preferences;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shasin.notificationbanner.Banner;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.service.controls.ControlsProviderService.TAG;

public class UserActivity extends AppCompatActivity {

    LinearLayout notificationPart, fiturPart, positionPart, positionLoadingPart, digitalSignatureBTN, notifikationBTN, countNotification, permohonanBTN, monitoringStaffBTN, markerWarningAlpha, markerWarningLate, markerWarningNoCheckout, idCardDigitalBTN, updateBTN, webBTN, selectMonthBTN, kelebihanJamBTN, pulangCepatBTN, layoffBTN, tidakCheckoutBTN, terlambatBTN, hadirBTN, tidakHadirBTN, prevBTN, nextBTN, editImg, uploadImg, logoutPart, chatBTN, removeAvatarBTN, closeBSBTN, viewAvatarBTN, updateAvatarBTN, emptyAvatarBTN, availableAvatarBTN, emptyAvatarPart, availableAvatarPart, actionBar, covidBTN, companyBTN, connectBTN, closeBTN, reminderBTN, privacyPolicyBTN, contactServiceBTN, aboutAppBTN, backBTN, logoutBTN, historyBTN;
    TextView countNotifTV, notePantau, titlePantau, bagianNameTV, hTime, mTime, sTime, kelebihanJamData, pulangCepatData, layoffData, noCheckoutData, terlambatData, currentDate, mainWeather, feelsLikeTemp, weatherTemp, currentAddress, batasBagDept, bulanData, tahunData, hadirData, tidakHadirData, statusIndicator, descAvailable, descEmtpy, statusUserTV, eventCalender, yearTV, monthTV, nameUserTV, nikTV, departemenTV, bagianTV, jabatanTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    BottomSheetLayout bottomSheet;
    SwipeRefreshLayout refreshLayout;
    NestedScrollView scrollView;
    RelativeLayout dataCuaca;
    ProgressBar loadingProgressBarCuaca;
    ImageView positionLoadingImg, notificationWarningAlpha, notificationWarningNocheckout, notificationWarningLate, kelebihanJamLoading, pulangCepatLoading, layoffLoading, noCheckoutLoading, terlambatLoading, weatherIcon, bulanLoading, hadirLoading, tidakHadirLoading, avatarUser, imageUserBS;
    View rootview;
    String selectMonth = "", currentDay = "", avatarStatus = "0", avatarPath = "";

    AlarmManager alarmManager;
    CompactCalendarView compactCalendarView;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private int i = -1;
    ResultReceiver resultReceiver;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        resultReceiver = new AddressResultReceiver(new Handler());

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        scrollView = findViewById(R.id.scrollView);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        logoutBTN = findViewById(R.id.logout_btn);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        nikTV = findViewById(R.id.nik_user_tv);
        departemenTV = findViewById(R.id.departemen_tv);
        bagianTV = findViewById(R.id.bagian_tv);
        jabatanTV = findViewById(R.id.jabatan_tv);
        historyBTN = findViewById(R.id.history_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        aboutAppBTN = findViewById(R.id.about_app_btn);
        privacyPolicyBTN = findViewById(R.id.privacy_policy_btn);
        contactServiceBTN = findViewById(R.id.contact_service_btn);
        reminderBTN = findViewById(R.id.reminder_btn);
        rootview = findViewById(android.R.id.content);
        companyBTN = findViewById(R.id.about_company_btn);
        covidBTN = findViewById(R.id.covid_btn);
        statusUserTV = findViewById(R.id.status_user_tv);
        avatarUser = findViewById(R.id.avatar_user);
        avatarUser = findViewById(R.id.avatar_user);
        emptyAvatarPart = findViewById(R.id.empty_avatar_part);
        availableAvatarPart = findViewById(R.id.available_avatar_part);
        uploadImg = findViewById(R.id.upload_file);
        editImg = findViewById(R.id.edit_file);
        chatBTN = findViewById(R.id.chat_btn);
        logoutPart = findViewById(R.id.logout_part);
        statusIndicator = findViewById(R.id.status_indikator);
        bulanData = findViewById(R.id.bulan_data);
        tahunData = findViewById(R.id.tahun_data);
        hadirData = findViewById(R.id.data_hadir);
        tidakHadirData = findViewById(R.id.data_tidak_hadir);
        bulanLoading = findViewById(R.id.bulan_loading);
        hadirLoading = findViewById(R.id.hadir_loading);
        tidakHadirLoading = findViewById(R.id.tidak_hadir_loading);
        terlambatLoading = findViewById(R.id.terlambat_loading);
        noCheckoutLoading = findViewById(R.id.no_checkout_loading);
        pulangCepatLoading = findViewById(R.id.pulang_cepat_loading);
        kelebihanJamLoading = findViewById(R.id.kelebihan_jam_loading);
        layoffLoading = findViewById(R.id.layoff_loading);
        batasBagDept = findViewById(R.id.batas_bag_dept);
        currentAddress = findViewById(R.id.current_address);
        weatherTemp = findViewById(R.id.weather_temp);
        feelsLikeTemp = findViewById(R.id.feels_like_temp);
        weatherIcon = findViewById(R.id.weather_icon);
        mainWeather = findViewById(R.id.main_weather);
        dataCuaca = findViewById(R.id.data_cuaca);
        tidakHadirBTN = findViewById(R.id.btn_tidak_hadir);
        hadirBTN = findViewById(R.id.btn_hadir);
        currentDate = findViewById(R.id.current_date);
        terlambatData = findViewById(R.id.data_terlambat);
        noCheckoutData = findViewById(R.id.data_no_checkout);
        pulangCepatData = findViewById(R.id.data_pulang_cepat);
        kelebihanJamData = findViewById(R.id.data_Kelebihan_jam);
        layoffData = findViewById(R.id.data_layoff);
        terlambatBTN = findViewById(R.id.terlambat_btn);
        tidakCheckoutBTN = findViewById(R.id.tidak_checkout_btn);
        pulangCepatBTN = findViewById(R.id.pulang_cepat_btn);
        layoffBTN = findViewById(R.id.layoff_btn);
        kelebihanJamBTN = findViewById(R.id.kelebihan_jam_btn);
        selectMonthBTN = findViewById(R.id.select_month_btn);
        webBTN = findViewById(R.id.go_web_btn);
        idCardDigitalBTN = findViewById(R.id.id_card_digital_btn);
        markerWarningAlpha = findViewById(R.id.marker_warning_alpha);
        markerWarningLate = findViewById(R.id.marker_warning_late);
        markerWarningNoCheckout = findViewById(R.id.marker_warning_nocheckout);
        updateBTN = findViewById(R.id.update_app_btn);
        notificationWarningLate = findViewById(R.id.warning_gif_absen_late);
        notificationWarningNocheckout = findViewById(R.id.warning_gif_absen_nocheckout);
        notificationWarningAlpha = findViewById(R.id.warning_gif_absen_alpha);
        monitoringStaffBTN = findViewById(R.id.monitoring_staff_btn);
        bagianNameTV = findViewById(R.id.bagian_name_tv);
        titlePantau = findViewById(R.id.title_pantau);
        notePantau = findViewById(R.id.note_pantau);
        permohonanBTN = findViewById(R.id.permohonan_btn);
        countNotification = findViewById(R.id.count_notification);
        countNotifTV = findViewById(R.id.count_notif_tv);
        notifikationBTN = findViewById(R.id.notifikation_btn);
        digitalSignatureBTN = findViewById(R.id.signature_digital_btn);
        positionPart = findViewById(R.id.position_part);
        positionLoadingPart = findViewById(R.id.position_loading_part);
        positionLoadingImg = findViewById(R.id.position_loading);
        fiturPart = findViewById(R.id.fitur_part);
        notificationPart = findViewById(R.id.notification_part);
        loadingProgressBarCuaca = findViewById(R.id.loadingProgressBar_cuaca);
        hTime = findViewById(R.id.h_time);
        mTime = findViewById(R.id.m_time);
        sTime = findViewById(R.id.s_time);

        selectMonth = getBulanTahun();

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(positionLoadingImg);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(hadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(tidakHadirLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(noCheckoutLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(terlambatLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(pulangCepatLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(kelebihanJamLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_dots)
                .into(layoffLoading);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningLate);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningNocheckout);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningAlpha);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bottomSheet.dismissSheet();

                selectMonth = getBulanTahun();

                bulanLoading.setVisibility(View.VISIBLE);
                bulanData.setVisibility(View.GONE);
                tahunData.setVisibility(View.GONE);

                hadirLoading.setVisibility(View.VISIBLE);
                hadirData.setVisibility(View.GONE);

                tidakHadirLoading.setVisibility(View.VISIBLE);
                tidakHadirData.setVisibility(View.GONE);

                terlambatLoading.setVisibility(View.VISIBLE);
                terlambatData.setVisibility(View.GONE);

                noCheckoutLoading.setVisibility(View.VISIBLE);
                noCheckoutData.setVisibility(View.GONE);

                pulangCepatLoading.setVisibility(View.VISIBLE);
                pulangCepatData.setVisibility(View.GONE);

                kelebihanJamLoading.setVisibility(View.VISIBLE);
                kelebihanJamData.setVisibility(View.GONE);

                layoffLoading.setVisibility(View.VISIBLE);
                layoffData.setVisibility(View.GONE);

                markerWarningAlpha.setVisibility(View.GONE);
                markerWarningLate.setVisibility(View.GONE);
                markerWarningNoCheckout.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataUser();
                    }
                }, 1000);
            }
        });

        selectMonthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RackMonthPicker(UserActivity.this)
                        .setLocale(Locale.ENGLISH)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                String bulan = "";
                                if(month==1){
                                    bulan = "01";
                                } else if (month==2){
                                    bulan = "02";
                                } else if (month==3){
                                    bulan = "03";
                                } else if (month==4){
                                    bulan = "04";
                                } else if (month==5){
                                    bulan = "05";
                                } else if (month==6){
                                    bulan = "06";
                                } else if (month==7){
                                    bulan = "07";
                                } else if (month==8){
                                    bulan = "08";
                                } else if (month==9){
                                    bulan = "09";
                                } else{
                                    bulan = String.valueOf(month);
                                }
                                selectMonth = String.valueOf(year)+"-"+bulan;

                                bulanLoading.setVisibility(View.VISIBLE);
                                bulanData.setVisibility(View.GONE);
                                tahunData.setVisibility(View.GONE);

                                hadirLoading.setVisibility(View.VISIBLE);
                                hadirData.setVisibility(View.GONE);

                                tidakHadirLoading.setVisibility(View.VISIBLE);
                                tidakHadirData.setVisibility(View.GONE);

                                terlambatLoading.setVisibility(View.VISIBLE);
                                terlambatData.setVisibility(View.GONE);

                                noCheckoutLoading.setVisibility(View.VISIBLE);
                                noCheckoutData.setVisibility(View.GONE);

                                pulangCepatLoading.setVisibility(View.VISIBLE);
                                pulangCepatData.setVisibility(View.GONE);

                                kelebihanJamLoading.setVisibility(View.VISIBLE);
                                kelebihanJamData.setVisibility(View.GONE);

                                layoffLoading.setVisibility(View.VISIBLE);
                                layoffData.setVisibility(View.GONE);

                                markerWarningAlpha.setVisibility(View.GONE);
                                markerWarningLate.setVisibility(View.GONE);
                                markerWarningNoCheckout.setVisibility(View.GONE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDataHadir();
                                        checkWarning();
                                    }
                                }, 100);

                            }
                        })
                        .setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        permohonanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, FormPermohonanIzinActivity.class);
                startActivity(intent);
            }
        });

        monitoringStaffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MonitoringAbsensiBagianActivity.class);
                startActivity(intent);
            }
        });

        digitalSignatureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DigitalSignatureActivity.class);
                intent.putExtra("kode", "dashboard");
                startActivity(intent);
            }
        });

        idCardDigitalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DigitalCardActivity.class);
                intent.putExtra("nama", sharedPrefManager.getSpNama());
                intent.putExtra("nik", sharedPrefManager.getSpNik());
                startActivity(intent);
            }
        });

        layoffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailLayoffActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        kelebihanJamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailKelebihanJamActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        pulangCepatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailPulangCepatActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        tidakCheckoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailTidakCheckoutActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        terlambatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailTerlambatActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        tidakHadirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailTidakHadirActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        hadirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DetailHadirActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        reminderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderBar();
            }
        });

        chatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, PersonalChatActivity.class);
                startActivity(intent);
            }
        });

        avatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarFuction();
            }
        });

        companyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, CompanyActivity.class);
                startActivity(intent);
            }
        });

        notifikationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ListNotifikasiActivity.class);
                startActivity(intent);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        covidBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, CovidActivity.class);
                startActivity(intent);
            }
        });

        aboutAppBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutApp();
            }
        });

        privacyPolicyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyPolicy();
            }
        });

        contactServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactService();
            }
        });

        webBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://geloraaksara.co.id/absen-online"));
                startActivity(webIntent);
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(UserActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Apakah anda yakin untuk logout?")
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
                                logoutFunction();
                            }
                        })
                        .show();
            }
        });

        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        getDataUser();

    }

    @SuppressLint("SetTextI18n")
    private void getDataUser(){
        String nama = sharedPrefManager.getSpNama();
        String nik = sharedPrefManager.getSpNik();
        String status = sharedPrefManager.getSpStatusUser();
        getCurrentDay();
        getDataKaryawan();
        getDataHadir();
        checkWarning();
        checkVersion();
        getCountNotification();
        nameUserTV.setText(nama.toUpperCase());
        nikTV.setText(nik);

        if(status.equals("1")){
            statusUserTV.setText("Aktif");
            statusIndicator.setBackground(ContextCompat.getDrawable(UserActivity.this, R.drawable.shape_ring_aktif));
        } else {
            statusUserTV.setText("Non-Aktif");
            statusIndicator.setBackground(ContextCompat.getDrawable(UserActivity.this, R.drawable.shape_ring_nonaktif));
        }
    }

    private void logoutFunction(){
        Preferences.setLoggedInStatus(this,false);
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_USER, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_AKTIF, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_HALAMAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        Preferences.clearLoggedInUser(UserActivity.this);
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDataKaryawan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_karyawan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                String avatar = data.getString("avatar");
                                String weather_key = data.getString("weather_key");
                                String info_covid = data.getString("info_covid");
                                String logout_part = data.getString("logout_part");
                                String chat_room = data.getString("chat_room");
                                String web_btn = data.getString("web_btn");

                                batasBagDept.setVisibility(View.VISIBLE);
                                departemenTV.setText(department);
                                bagianTV.setText(bagian);
                                jabatanTV.setText(jabatan);

                                positionLoadingPart.setVisibility(View.GONE);
                                positionPart.setVisibility(View.VISIBLE);

                                if (sharedPrefManager.getSpIdJabatan().equals("10")){
                                    bagianNameTV.setText(department);
                                } else if (sharedPrefManager.getSpIdJabatan().equals("11")){
                                    bagianNameTV.setText(bagian);
                                }

                                if(!avatar.equals("null")){
                                    if(!avatar.equals("default_profile.jpg")){
                                        uploadImg.setVisibility(View.GONE);
                                        editImg.setVisibility(View.VISIBLE);
                                        avatarStatus = "1";
                                        avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                        Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(avatarUser);
                                    } else {
                                        avatarStatus = "0";
                                        uploadImg.setVisibility(View.VISIBLE);
                                        editImg.setVisibility(View.GONE);
                                        avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                        Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(avatarUser);
                                    }
                                } else {
                                    avatarStatus = "0";
                                    uploadImg.setVisibility(View.VISIBLE);
                                    editImg.setVisibility(View.GONE);
                                }

                                if(info_covid.equals("1")){
                                    covidBTN.setVisibility(View.VISIBLE);
                                } else {
                                    covidBTN.setVisibility(View.GONE);
                                }

                                if(logout_part.equals("1")){
                                    logoutPart.setVisibility(View.VISIBLE);
                                } else {
                                    logoutPart.setVisibility(View.GONE);
                                }

                                if(chat_room.equals("1")){
                                    chatBTN.setVisibility(View.VISIBLE);
                                } else {
                                    chatBTN.setVisibility(View.GONE);
                                }

                                if(web_btn.equals("0")){
                                    webBTN.setVisibility(View.VISIBLE);
                                } else {
                                    webBTN.setVisibility(View.GONE);
                                }

                                getCurrentLocation(weather_key);

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getDataHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_hadir";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String bulan = data.getString("bulan");
                                String tahun = data.getString("tahun");
                                String hadir = data.getString("jumlah_hadir");
                                String tidak_hadir = data.getString("jumlah_tidak_hadir");
                                String terlambat = data.getString("terlambat");
                                String tidak_checkout = data.getString("tidak_checkout");
                                String layoff = data.getString("layoff");
                                String pulang_cepat = data.getString("pulang_cepat");
                                String kelebihan_jam = data.getString("kelebihan_jam");

                                bulanData.setText(bulan.toUpperCase());
                                tahunData.setText(tahun);
                                hadirData.setText(hadir);
                                tidakHadirData.setText(tidak_hadir);
                                terlambatData.setText(terlambat);
                                noCheckoutData.setText(tidak_checkout);
                                pulangCepatData.setText(pulang_cepat);
                                kelebihanJamData.setText(kelebihan_jam);
                                layoffData.setText(layoff);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        bulanLoading.setVisibility(View.GONE);
                                        bulanData.setVisibility(View.VISIBLE);
                                        tahunData.setVisibility(View.VISIBLE);

                                        hadirLoading.setVisibility(View.GONE);
                                        hadirData.setVisibility(View.VISIBLE);

                                        tidakHadirLoading.setVisibility(View.GONE);
                                        tidakHadirData.setVisibility(View.VISIBLE);

                                        terlambatLoading.setVisibility(View.GONE);
                                        terlambatData.setVisibility(View.VISIBLE);

                                        noCheckoutLoading.setVisibility(View.GONE);
                                        noCheckoutData.setVisibility(View.VISIBLE);

                                        pulangCepatLoading.setVisibility(View.GONE);
                                        pulangCepatData.setVisibility(View.VISIBLE);

                                        kelebihanJamLoading.setVisibility(View.GONE);
                                        kelebihanJamData.setVisibility(View.VISIBLE);

                                        layoffLoading.setVisibility(View.GONE);
                                        layoffData.setVisibility(View.VISIBLE);

                                    }
                                }, 100);

                            } else {
                                bulanLoading.setVisibility(View.GONE);
                                bulanData.setVisibility(View.VISIBLE);
                                tahunData.setVisibility(View.VISIBLE);

                                hadirLoading.setVisibility(View.GONE);
                                hadirData.setVisibility(View.VISIBLE);

                                tidakHadirLoading.setVisibility(View.GONE);
                                tidakHadirData.setVisibility(View.VISIBLE);

                                terlambatLoading.setVisibility(View.GONE);
                                terlambatData.setVisibility(View.VISIBLE);

                                noCheckoutLoading.setVisibility(View.GONE);
                                noCheckoutData.setVisibility(View.VISIBLE);

                                pulangCepatLoading.setVisibility(View.GONE);
                                pulangCepatData.setVisibility(View.VISIBLE);

                                kelebihanJamLoading.setVisibility(View.GONE);
                                kelebihanJamData.setVisibility(View.VISIBLE);

                                layoffLoading.setVisibility(View.GONE);
                                layoffData.setVisibility(View.VISIBLE);
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
                        bottomSheet.dismissSheet();
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
                params.put("bulan", selectMonth);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getCountNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_list_permohonan_izin";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String count = data.getString("count");
                                String count2 = data.getString("count2");

                                if (count.equals("0") && count2.equals("0")){
                                    countNotification.setVisibility(View.GONE);
                                } else {
                                    countNotification.setVisibility(View.VISIBLE);
                                    countNotifTV.setText(String.valueOf(Integer.parseInt(count)+Integer.parseInt(count2)));
                                }

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_departemen", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void connectionFailed(){
        Banner.make(rootview, UserActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();
    }

    private void aboutApp(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_about_app, bottomSheet, false));
        closeBTN = findViewById(R.id.close_btn);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
    }

    private void privacyPolicy(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_privacy_policy, bottomSheet, false));
        closeBTN = findViewById(R.id.close_btn);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
    }

    private void contactService(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_contact_service, bottomSheet, false));
        closeBTN = findViewById(R.id.close_btn);
        connectBTN = findViewById(R.id.connect_btn);
        getContact();
    }

    private void getContact() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/get_contact";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String bagian = response.getString("bagian");
                            String nama = response.getString("nama");
                            String whatsapp = response.getString("whatsapp");

                            closeBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomSheet.dismissSheet();
                                }
                            });
                            connectBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                                    startActivity(webIntent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    private void reminderBar(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_calender, bottomSheet, false));
        monthTV = findViewById(R.id.month_calender);
        yearTV = findViewById(R.id.year_calender);
        closeBTN = findViewById(R.id.close_btn);
        eventCalender = findViewById(R.id.event_calender);
        prevBTN = findViewById(R.id.prevBTN);
        nextBTN = findViewById(R.id.nextBTN);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        String month = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(4,7);
        String year = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(30,34);

        String bulanName;
        switch (month) {
            case "Jan":
                bulanName = "Januari";
                break;
            case "Feb":
                bulanName = "Februari";
                break;
            case "Mar":
                bulanName = "Maret";
                break;
            case "Apr":
                bulanName = "April";
                break;
            case "May":
                bulanName = "Mei";
                break;
            case "Jun":
                bulanName = "Juni";
                break;
            case "Jul":
                bulanName = "Juli";
                break;
            case "Aug":
                bulanName = "Agustus";
                break;
            case "Sep":
                bulanName = "September";
                break;
            case "Oct":
                bulanName = "Oktober";
                break;
            case "Nov":
                bulanName = "November";
                break;
            case "Dec":
                bulanName = "Desember";
                break;
            default:
                bulanName = "Not found!";
                break;
        }

        monthTV.setText(bulanName);
        yearTV.setText(year);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        getEventCalender();

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                if(events.size()<=0){
                    eventCalender.setText("");
                } else {
                    eventCalender.setText(String.valueOf(events.get(0).getData()));
                }
            }

            @SuppressLint("InlinedApi")
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                String month = String.valueOf(firstDayOfNewMonth).substring(4,7);
                String year = String.valueOf(firstDayOfNewMonth).substring(30,34);

                String bulanName;
                switch (month) {
                    case "Jan":
                        bulanName = "Januari";
                        break;
                    case "Feb":
                        bulanName = "Februari";
                        break;
                    case "Mar":
                        bulanName = "Maret";
                        break;
                    case "Apr":
                        bulanName = "April";
                        break;
                    case "May":
                        bulanName = "Mei";
                        break;
                    case "Jun":
                        bulanName = "Juni";
                        break;
                    case "Jul":
                        bulanName = "Juli";
                        break;
                    case "Aug":
                        bulanName = "Agustus";
                        break;
                    case "Sep":
                        bulanName = "September";
                        break;
                    case "Oct":
                        bulanName = "Oktober";
                        break;
                    case "Nov":
                        bulanName = "November";
                        break;
                    case "Dec":
                        bulanName = "Desember";
                        break;
                    default:
                        bulanName = "Not found!";
                        break;
                }

                monthTV.setText(bulanName);
                yearTV.setText(year);
                eventCalender.setText("");

            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    private void getEventCalender() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/holiday";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject event = data.getJSONObject(i);
                                String nama = event.getString("nama");
                                String tanggal = event.getString("tanggal");

                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = sdf.parse(tanggal);
                                long millis = date.getTime();
                                Event ev1 = new Event(Color.RED, millis, nama);
                                compactCalendarView.addEvent(ev1);
                            }


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
        }, "avatar");
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(UserActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(UserActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        String file_directori = getRealPathFromURIPath(uri, UserActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    String file_directori = getRealPathFromURIPath(uri, UserActivity.this);
                    String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                    Log.e("PaRSE JSON", a);
                    uploadMultipart();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadMultipart() {
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/update_profilepic";
        String path1 = FilePathimage.getPath(this, uri);
        if (path1 == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path1, "file") //Adding file
                        .addParameter("NIK", sharedPrefManager.getSpNik()) //Adding text parameter to the request
                        .setMaxRetries(1)
                        .startUpload();
            } catch (Exception exc) {
                Log.e("PaRSE JSON", "Oke");
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final KAlertDialog pDialog = new KAlertDialog(UserActivity.this, KAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this, R.color.colorGradien));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this, R.color.colorGradien2));
                                break;
                            case 2:
                            case 6:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this, R.color.colorGradien3));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this, R.color.colorGradien4));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this, R.color.colorGradien5));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this, R.color.colorGradien6));
                                break;
                        }
                    }
                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Unggah Berhasil")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        bottomSheet.dismissSheet();
                        getDataKaryawan();
                    }
                }.start();

            }
        }, 1);
    }

    @SuppressLint("SetTextI18n")
    private void avatarFuction(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_change_avatar, bottomSheet, false));
        emptyAvatarPart = findViewById(R.id.empty_avatar_part);
        availableAvatarPart = findViewById(R.id.available_avatar_part);
        descAvailable = findViewById(R.id.desc_available);
        descEmtpy = findViewById(R.id.desc_empty);
        emptyAvatarBTN = findViewById(R.id.empty_avatar_btn);
        availableAvatarBTN = findViewById(R.id.available_avatar_btn);
        updateAvatarBTN = findViewById(R.id.update_avatar_btn);
        viewAvatarBTN = findViewById(R.id.view_avatar_btn);
        imageUserBS = findViewById(R.id.image_user_bs);
        closeBSBTN = findViewById(R.id.close_bs_btn);
        removeAvatarBTN = findViewById(R.id.hapus_avatar_btn);

        descAvailable.setText("Halo "+sharedPrefManager.getSpNama()+", anda bisa atur foto profil sesuai keinginan anda.");
        descEmtpy.setText("Halo "+sharedPrefManager.getSpNama()+", anda bisa tambahkan foto profil sesuai keinginan anda.");

        if (avatarStatus.equals("1")){
            emptyAvatarPart.setVisibility(View.GONE);
            emptyAvatarBTN.setVisibility(View.GONE);
            availableAvatarPart.setVisibility(View.VISIBLE);
            availableAvatarBTN.setVisibility(View.VISIBLE);
            Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageUserBS);
        } else {
            emptyAvatarPart.setVisibility(View.VISIBLE);
            emptyAvatarBTN.setVisibility(View.VISIBLE);
            availableAvatarPart.setVisibility(View.GONE);
            availableAvatarBTN.setVisibility(View.GONE);
        }

        closeBSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

        emptyAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
                bottomSheet.dismissSheet();
            }
        });

        updateAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dexterCall();
                    }
                }, 240);
            }
        });

        viewAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(UserActivity.this, ViewImageActivity.class);
                        intent.putExtra("url", avatarPath);
                        intent.putExtra("kode", "profile");
                        startActivity(intent);
                    }
                }, 200);
            }
        });

        removeAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new KAlertDialog(UserActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Yakin untuk menghapus foto profil?")
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
                                        removePic();
                                    }
                                })
                                .show();
                    }
                }, 240);
            }
        });

    }

    private void dexterCall(){
        Dexter.withActivity(UserActivity.this)
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

    private void removePic() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/remove_pic";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                final KAlertDialog pDialog = new KAlertDialog(UserActivity.this, KAlertDialog.PROGRESS_TYPE)
                                        .setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UserActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UserActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UserActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UserActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UserActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UserActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        pDialog.setTitleText("Foto Berhasil Dihapus")
                                                .setConfirmText("    OK    ")
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        getDataKaryawan();
                                    }
                                }.start();

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
                        bottomSheet.dismissSheet();
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

    private void getCurrentLocation(String weather_key) {
        //progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(UserActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            //Toast.makeText(UserActivity.this, String.format("Latitude : %s\n Longitude: %s", lati, longi), Toast.LENGTH_SHORT).show();

                            dateLive();
                            timeLive();

                            getCurrentWeather(weather_key, String.valueOf(lati), String.valueOf(longi));
                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchaddressfromlocation(location);

                        }
                    }
                }, Looper.getMainLooper());

    }

    private void timeLive() {
        hTime.setText(getTimeH());
        mTime.setText(getTimeM());
        sTime.setText(getTimeS());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeLive();
            }
        }, 1000);
    }

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTimeM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTimeS() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("ss");
        Date date = new Date();
        return dateFormat.format(date);
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
                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
            } else {
                currentAddress.setText(resultData.getString(Constants.NO_ADDRESS));
            }
        }

    }

    private void fetchaddressfromlocation(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEVIER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private void getCurrentWeather(String api_key, String lat, String lon) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+api_key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        JSONArray data = null;
                        try {
                            data = response.getJSONArray("weather");
                            JSONObject suhu = response.getJSONObject("main");
                            String name = response.getString("name");
                            String temp = suhu.getString("temp");
                            String feels_like = suhu.getString("feels_like");
                            float f = Float.parseFloat(temp);
                            float f2 = Float.parseFloat(feels_like);
                            weatherTemp.setText(String.valueOf(Math.round(convertFromKelvinToCelsius(f)))+"°");
                            feelsLikeTemp.setText("Terasa seperti "+String.valueOf(Math.round(convertFromKelvinToCelsius(f2)))+"°");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject weather = data.getJSONObject(i);
                                String main = weather.getString("main");
                                String description = weather.getString("description");
                                String icon = weather.getString("icon");
                                String desc_idn = "";

                                //Thunderstorm
                                if (description.equals("thunderstorm with light rain")){
                                    desc_idn = "Badai petir disertai hujan ringan";
                                } else if (description.equals("thunderstorm with rain")){
                                    desc_idn = "Badai petir dengan hujan";
                                } else if (description.equals("thunderstorm with heavy rain")){
                                    desc_idn = "Badai petir dengan hujan lebat";
                                } else if (description.equals("light thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("heavy thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("ragged thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("thunderstorm with light drizzle")){
                                    desc_idn = "Badai petir dengan gerimis";
                                } else if (description.equals("thunderstorm with drizzle")){
                                    desc_idn = "Badai petir dengan gerimis";
                                } else if (description.equals("thunderstorm with heavy drizzle")){
                                    desc_idn = "Badai petir dengan hujan";
                                }

                                //Drizzle
                                else if (description.equals("light intensity drizzle")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("drizzle")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("heavy intensity drizzle")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("light intensity drizzle rain")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("drizzle rain")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("heavy intensity drizzle rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("shower rain and drizzle")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("heavy shower rain and drizzle")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("shower drizzle")){
                                    desc_idn = "Gerimis";
                                }

                                //Rain
                                else if (description.equals("light rain")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("moderate rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("heavy intensity rain")){
                                    desc_idn = "Hujan deras";
                                } else if (description.equals("very heavy rain")){
                                    desc_idn = "Hujan deras";
                                } else if (description.equals("extreme rain")){
                                    desc_idn = "Hujan ekstrim";
                                } else if (description.equals("freezing rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("light intensity shower rain")){
                                    desc_idn = "Hujan ringan";
                                } else if (description.equals("shower rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("heavy intensity shower rain")){
                                    desc_idn = "Hujan deras";
                                } else if (description.equals("ragged shower rain")){
                                    desc_idn = "Hujan deras";
                                }

                                //Snow
                                else if (description.equals("light snow")){
                                    desc_idn = "Salju ringan";
                                } else if (description.equals("snow")){
                                    desc_idn = "Salju";
                                } else if (description.equals("heavy snow")){
                                    desc_idn = "Salju tebal";
                                } else if (description.equals("sleet")){
                                    desc_idn = "Hujan es";
                                } else if (description.equals("light shower sleet")){
                                    desc_idn = "Hujan es ringan";
                                } else if (description.equals("shower sleet")){
                                    desc_idn = "Hujan es";
                                } else if (description.equals("light rain and snow")){
                                    desc_idn = "Hujan ringan dan salju";
                                } else if (description.equals("rain and snow")){
                                    desc_idn = "Hujan salju";
                                } else if (description.equals("light shower snow")){
                                    desc_idn = "Hujan salju ringan";
                                } else if (description.equals("shower snow")){
                                    desc_idn = "Hujan salju";
                                } else if (description.equals("heavy shower snow")){
                                    desc_idn = "Hujan salju lebat";
                                }

                                //Atmosphere
                                else if (description.equals("mist")){
                                    desc_idn = "Berkabut";
                                } else if (description.equals("smoke")){
                                    desc_idn = "Kabut asap";
                                } else if (description.equals("haze")){
                                    desc_idn = "Berkabut";
                                } else if (description.equals("sand/ dust whirls")){
                                    desc_idn = "Badai pasir/debu";
                                } else if (description.equals("fog")){
                                    desc_idn = "Berkabut";
                                } else if (description.equals("sand")){
                                    desc_idn = "Berpasir";
                                } else if (description.equals("dust")){
                                    desc_idn = "Berdebu";
                                } else if (description.equals("volcanic ash")){
                                    desc_idn = "Abu vulkanik";
                                } else if (description.equals("squalls")){
                                    desc_idn = "Badai";
                                } else if (description.equals("tornado")) {
                                    desc_idn = "Angin topan";
                                }

                                //Clear
                                else if (description.equals("clear sky")){
                                    desc_idn = "Langit cerah";
                                }

                                //Clouds
                                else if (description.equals("few clouds")){
                                    desc_idn = "Sebagian berawan";
                                } else if (description.equals("scattered clouds")){
                                    desc_idn = "Berawan";
                                } else if (description.equals("broken clouds")){
                                    desc_idn = "Berawan";
                                } else if (description.equals("overcast clouds")){
                                    desc_idn = "Awan mendung";
                                }

                                mainWeather.setText(desc_idn);
                                loadingProgressBarCuaca.setVisibility(View.GONE);
                                weatherIcon.setVisibility(View.VISIBLE);
                                String url = "https://geloraaksara.co.id/absen-online/upload/weather_icon/"+icon+".png";

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(weatherIcon);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dataCuaca.setVisibility(View.GONE);
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private static float convertFromKelvinToCelsius(float value) {
        return value - 273.15f;
    }

    private void checkWarning() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/warning_absen";
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
                                String alpa = data.getString("alpa");
                                String terlambat = data.getString("terlambat");
                                String tidak_checkout = data.getString("tidak_checkout");
                                String cuaca_button = data.getString("cuaca_button");
                                String monitoring = data.getString("monitoring");
                                String fitur_izin = data.getString("fitur_izin");

                                if (cuaca_button.equals("1")){
                                    dataCuaca.setVisibility(View.VISIBLE);
                                } else {
                                    dataCuaca.setVisibility(View.GONE);
                                }

                                if (monitoring.equals("1")){
                                    if (sharedPrefManager.getSpIdJabatan().equals("10")){
                                        monitoringStaffBTN.setVisibility(View.VISIBLE);
                                        titlePantau.setText("Pantau kehadiran departemen*");
                                        notePantau.setText("*Fitur khusus Kepala Departemen");
                                    } else if (sharedPrefManager.getSpIdJabatan().equals("11")){
                                        monitoringStaffBTN.setVisibility(View.VISIBLE);
                                        titlePantau.setText("Pantau kehadiran bagian*");
                                        notePantau.setText("*Fitur khusus Kepala Bagian");
                                    } else {
                                        monitoringStaffBTN.setVisibility(View.GONE);
                                    }
                                } else {
                                    monitoringStaffBTN.setVisibility(View.GONE);
                                }

                                int alpaNumb = Integer.parseInt(alpa);
                                int lateNumb = Integer.parseInt(terlambat);
                                int noCheckoutNumb = Integer.parseInt(tidak_checkout);

                                if(noCheckoutNumb > 0) {
                                    markerWarningNoCheckout.setVisibility(View.VISIBLE);
                                } else {
                                    markerWarningNoCheckout.setVisibility(View.GONE);
                                }

                                if (alpaNumb > 0){
                                    markerWarningAlpha.setVisibility(View.VISIBLE);
                                } else {
                                    markerWarningAlpha.setVisibility(View.GONE);
                                }

                                if(lateNumb > 0){
                                    markerWarningLate.setVisibility(View.VISIBLE);
                                } else {
                                    markerWarningLate.setVisibility(View.GONE);
                                }

                                if(fitur_izin.equals("1")){
                                    fiturPart.setVisibility(View.VISIBLE);
                                    notificationPart.setVisibility(View.VISIBLE);
                                } else {
                                    fiturPart.setVisibility(View.GONE);
                                    notificationPart.setVisibility(View.GONE);
                                }

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
                params.put("bulan", selectMonth);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SetTextI18n")
    private void dateLive(){
        switch (getDateM()) {
            case "01":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                break;
            case "02":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                break;
            case "03":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                break;
            case "04":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                break;
            case "05":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                break;
            case "06":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                break;
            case "07":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                break;
            case "08":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                break;
            case "09":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                break;
            case "10":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                break;
            case "11":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                break;
            case "12":
                currentDate.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                break;
            default:
                currentDate.setText("Not found!");
                break;
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

    private void getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay = "Minggu";
                break;
            case Calendar.MONDAY:
                currentDay = "Senin";
                break;
            case Calendar.TUESDAY:
                currentDay = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "Rabu";
                break;
            case Calendar.THURSDAY:
                currentDay = "Kamis";
                break;
            case Calendar.FRIDAY:
                currentDay = "Jumat";
                break;
            case Calendar.SATURDAY:
                currentDay = "Sabtu";
                break;
        }
    }

    private void checkVersion() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/version_app";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            String version = response.getString("version");
                            String btn_update = response.getString("btn_update");

                            if (status.equals("Success")){
                                String currentVersion = "1.1.33"; //harus disesuaikan
                                if (!currentVersion.equals(version) && btn_update.equals("1")){
                                    updateBTN.setVisibility(View.VISIBLE);
                                    updateBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.gelora.absensi"));
                                            startActivity(webIntent);
                                        }
                                    });
                                } else {
                                    updateBTN.setVisibility(View.GONE);
                                }
                            } else {
                                updateBTN.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCountNotification();
    }

}
