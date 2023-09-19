package com.gelora.absensi.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.isradeleon.notify.Notify;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.AllMenuActivity;
import com.gelora.absensi.CalendarPageActivity;
import com.gelora.absensi.ChatSplashScreenActivity;
import com.gelora.absensi.ComingSoonActivity;
import com.gelora.absensi.DetailCuacaActivity;
import com.gelora.absensi.DetailExitClearanceActivity;
import com.gelora.absensi.DetailFormSdmActivity;
import com.gelora.absensi.DetailPengumumanActivity;
import com.gelora.absensi.DigitalCardActivity;
import com.gelora.absensi.DigitalSignatureActivity;
import com.gelora.absensi.ExitClearanceActivity;
import com.gelora.absensi.FetchAddressIntentServices;
import com.gelora.absensi.FormExitClearanceActivity;
import com.gelora.absensi.FormFingerscanActivity;
import com.gelora.absensi.FormPermohonanCutiActivity;
import com.gelora.absensi.FormPermohonanIzinActivity;
import com.gelora.absensi.HomeActivity;
import com.gelora.absensi.HumanResourceActivity;
import com.gelora.absensi.InfoPersonalActivity;
import com.gelora.absensi.ListAllPengumumanActivity;
import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.NewsActivity;
import com.gelora.absensi.PersonalNotificationActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.adapter.AdapterListPengumumanNew;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataPengumuman;
import com.github.jinatonic.confetti.CommonConfetti;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class FragmentHome extends Fragment {

    LinearLayout countNotificationClearancePart, clearancePart, calendarPart, weatherBTN, newsPart, countNotificationPersonalPart, personalNotifBTN, countNotificationPenilaian, menuSdmBTN, sdmPart, cardPart, pausePart, playPart, bannerPengumumanPart, congratTahunanPart, ulangTahunPart, cutiPart, pengaduanPart, countNotificationMessage, chatBTN, noDataPart, loadingDataPart, detailUserBTN, homePart, menuAbsensiBTN, menuIzinBTN, menuCutiBTN, menuPengaduanBTN, menuFingerBTN, menuLainnyaBTN, menuSignatureBTN, menuCardBTN, menuCalendarBTN, menuClearanceBTN;
    TextView countNotifClearanceTV, countNotificationPersonalTV, countNotifPenilaianTV, nikTV, ulangTahunTo, highlightPengumuman, judulPengumuman, congratCelebrate, ulangTahunCelebrate, countMessage, pengumumanSelengkapnyaBTN, currentDate, hTime, mTime, sTime, nameOfUser, positionOfUser ,mainWeather, weatherTemp, feelsLikeTemp, currentAddress;
    ProgressBar loadingProgressBarCuaca;
    ImageView avatarUser, weatherIcon, loadingData;
    RelativeLayout dataCuaca, dataCuacaEmpty;

    MediaPlayer musicUlangTahun;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    String currentDay = "", refeatConfeti = "1", locationNow = "" , musicPlay = "off";
    ResultReceiver resultReceiver;
    Context mContext;
    Activity mActivity;
    protected ViewGroup mainParent;
    Vibrator vibrate;
    RequestQueue requestQueue;

    private RecyclerView listPengumumanNewRV;
    private DataPengumuman[] dataPengumumanNews;
    private AdapterListPengumumanNew adapterListPengumunanNew;

    protected int color1, color2, color3, color4, color5, color6, color7;
    protected ViewGroup viewGroup;
    protected int[] colors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        resultReceiver = new AddressResultReceiver(new Handler());
        mContext = getContext();
        mActivity = getActivity();

        final Resources res = getResources();
        color1 = res.getColor(R.color.color1);
        color2 = res.getColor(R.color.color2);
        color3 = res.getColor(R.color.color3);
        color4 = res.getColor(R.color.color4);
        color5 = res.getColor(R.color.color5);
        color6 = res.getColor(R.color.color6);
        color7 = res.getColor(R.color.color7);
        colors = new int[] { color1, color2, color3, color4, color5, color6, color7 };

        sharedPrefManager = new SharedPrefManager(mContext);
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        requestQueue = Volley.newRequestQueue(mContext);
        vibrate = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        bottomSheet = view.findViewById(R.id.bottom_sheet_layout);
        mainParent = view.findViewById(R.id.main_parent);
        refreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);
        countNotificationMessage = view.findViewById(R.id.count_notification_message);
        countMessage = view.findViewById(R.id.count_message);
        nameOfUser = view.findViewById(R.id.name_of_user);
        nikTV = view.findViewById(R.id.nik_tv);
        avatarUser = view.findViewById(R.id.avatar_user);
        detailUserBTN = view.findViewById(R.id.detail_user_btn);
        menuAbsensiBTN = view.findViewById(R.id.menu_absensi_btn);
        positionOfUser = view.findViewById(R.id.position_of_user);
        menuIzinBTN = view.findViewById(R.id.menu_izin_btn);
        menuCutiBTN = view.findViewById(R.id.menu_cuti_btn);
        menuPengaduanBTN = view.findViewById(R.id.menu_pengaduan_btn);
        menuFingerBTN = view.findViewById(R.id.menu_finger_btn);
        menuLainnyaBTN = view.findViewById(R.id.menu_lainnya_btn);
        menuSignatureBTN = view.findViewById(R.id.menu_signature_btn);
        menuCardBTN = view.findViewById(R.id.menu_card_btn);
        menuCalendarBTN = view.findViewById(R.id.menu_calendar_btn);
        menuSdmBTN = view.findViewById(R.id.menu_sdm_btn);
        menuClearanceBTN = view.findViewById(R.id.menu_clearance_btn);
        dataCuaca = view.findViewById(R.id.data_cuaca);
        dataCuacaEmpty = view.findViewById(R.id.data_cuaca_empty);
        hTime = view.findViewById(R.id.h_time);
        mTime = view.findViewById(R.id.m_time);
        sTime = view.findViewById(R.id.s_time);
        currentDate = view.findViewById(R.id.current_date);
        mainWeather = view.findViewById(R.id.main_weather);
        weatherIcon = view.findViewById(R.id.weather_icon);
        loadingProgressBarCuaca = view.findViewById(R.id.loadingProgressBar_cuaca);
        weatherTemp = view.findViewById(R.id.weather_temp);
        feelsLikeTemp = view.findViewById(R.id.feels_like_temp);
        currentAddress = view.findViewById(R.id.current_address);
        loadingDataPart = view.findViewById(R.id.loading_data_part);
        loadingData = view.findViewById(R.id.loading_data);
        noDataPart = view.findViewById(R.id.no_data_part);
        pengumumanSelengkapnyaBTN = view.findViewById(R.id.pengumuman_selengkapnya_btn);
        chatBTN = view.findViewById(R.id.chat_btn);
        playPart = view.findViewById(R.id.play_part);
        pausePart = view.findViewById(R.id.pause_part);
        ulangTahunTo = view.findViewById(R.id.ulang_tahun_to);
        sdmPart = view.findViewById(R.id.sdm_part);
        cardPart = view.findViewById(R.id.card_part);
        countNotificationPenilaian = view.findViewById(R.id.count_notification_penilaian);
        countNotifPenilaianTV = view.findViewById(R.id.count_notif_penilaian_tv);
        personalNotifBTN = view.findViewById(R.id.personal_notif_btn);
        countNotificationPersonalPart = view.findViewById(R.id.count_notification_personal);
        countNotificationPersonalTV = view.findViewById(R.id.count_personal);
        weatherBTN = view.findViewById(R.id.weather_btn);

        ulangTahunPart = view.findViewById(R.id.ulang_tahun_part);
        congratTahunanPart = view.findViewById(R.id.congrat_tahunan);
        bannerPengumumanPart = view.findViewById(R.id.banner_pengumuman);

        ulangTahunCelebrate = view.findViewById(R.id.ulang_tahun_celebrate);
        congratCelebrate = view.findViewById(R.id.congrat_celebrate);
        judulPengumuman = view.findViewById(R.id.judul_pengumuman);
        highlightPengumuman = view.findViewById(R.id.highlight_pengumuman);

        cutiPart = view.findViewById(R.id.cuti_part);
        pengaduanPart = view.findViewById(R.id.pengaduan_part);
        newsPart = view.findViewById(R.id.news_part);
        clearancePart = view.findViewById(R.id.clearance_part);
        calendarPart = view.findViewById(R.id.calendar_part);
        countNotificationClearancePart = view.findViewById(R.id.count_notification_clearance);
        countNotifClearanceTV = view.findViewById(R.id.count_notif_clearance_tv);

        listPengumumanNewRV = view.findViewById(R.id.list_pengumuman_rv);
        listPengumumanNewRV.setLayoutManager(new LinearLayoutManager(mContext));
        listPengumumanNewRV.setHasFixedSize(true);
        listPengumumanNewRV.setNestedScrollingEnabled(false);
        listPengumumanNewRV.setItemAnimator(new DefaultItemAnimator());

        Glide.with(mContext)
                .load(R.drawable.loading_sgn_digital)
                .into(loadingData);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    listPengumumanNewRV.setVisibility(View.GONE);
                    noDataPart.setVisibility(View.GONE);
                    loadingDataPart.setVisibility(View.VISIBLE);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataKaryawan();
                    }
                }, 1000);
            }
        });

        chatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatSplashScreenActivity.class);
                startActivity(intent);
            }
        });

        personalNotifBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonalNotificationActivity.class);
                startActivity(intent);
            }
        });

        menuAbsensiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
            }
        });

        menuIzinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefManager.getSpIdJabatan().equals("31")){
                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Khusus PKL, pengajuan izin dapat langsung menghubungi HRD")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
                } else {
                    Intent intent = new Intent(mContext, FormPermohonanIzinActivity.class);
                    startActivity(intent);
                }
            }
        });

        menuCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormPermohonanCutiActivity.class);
                startActivity(intent);
            }
        });

        menuPengaduanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Anda akan terhubung dengan Bagian HRD")
                        .setCancelText("    BATAL    ")
                        .setConfirmText("  LANJUT  ")
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
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getContact();
                                    }
                                }, 500);
                            }
                        })
                        .show();
            }
        });

        menuFingerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefManager.getSpIdJabatan().equals("31")){
                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Khusus PKL, pengajuan fingerscan/keterangan tidak absen dapat langsung menghubungi HRD")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Intent intent = new Intent(mContext, FormFingerscanActivity.class);
                    startActivity(intent);
                }
            }
        });

        menuLainnyaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllMenuActivity.class);
                startActivity(intent);
            }
        });

        menuSignatureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DigitalSignatureActivity.class);
                intent.putExtra("kode", "home");
                startActivity(intent);
            }
        });

        menuCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DigitalCardActivity.class);
                intent.putExtra("nama", sharedPrefManager.getSpNama());
                intent.putExtra("nik", sharedPrefManager.getSpNik());
                startActivity(intent);
            }
        });

        menuSdmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HumanResourceActivity.class);
                startActivity(intent);
            }
        });

        menuCalendarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CalendarPageActivity.class);
                startActivity(intent);
            }
        });

        menuClearanceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormExitClearanceActivity.class);
                startActivity(intent);
            }
        });

        pengumumanSelengkapnyaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListAllPengumumanActivity.class);
                startActivity(intent);
            }
        });

        getDataKaryawan();
        getCurrentDay();
        nameOfUser.setText(sharedPrefManager.getSpNama());
        nikTV.setText(sharedPrefManager.getSpNik());

        return view;
    }

    private void getDataKaryawan() {

        if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")||sharedPrefManager.getSpStatusKaryawan().equals("Kontrak")){
            if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")){
                cutiPart.setVisibility(View.VISIBLE);
                pengaduanPart.setVisibility(View.GONE);
                clearancePart.setVisibility(View.VISIBLE);
                calendarPart.setVisibility(View.GONE);
            } else if(sharedPrefManager.getSpStatusKaryawan().equals("Kontrak")){
                if(!sharedPrefManager.getSpTglBergabung().equals("") || !sharedPrefManager.getSpTglBergabung().equals("null")){
                    String tanggalMulaiBekerja = sharedPrefManager.getSpTglBergabung();
                    String tanggalSekarang = getDate();

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = format.parse(tanggalSekarang);
                        date2 = format.parse(tanggalMulaiBekerja);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long waktu1 = date1.getTime();
                    long waktu2 = date2.getTime();
                    long selisih_waktu = waktu1 - waktu2;
                    long diffDays = selisih_waktu / (24 * 60 * 60 * 1000);
                    long diffMonths = (selisih_waktu / (24 * 60 * 60 * 1000)) / 30;
                    long diffYears =  ((selisih_waktu / (24 * 60 * 60 * 1000)) / 30) / 12;

                    if(diffMonths >= 12){
                        cutiPart.setVisibility(View.VISIBLE);
                        pengaduanPart.setVisibility(View.GONE);
                    } else {
                        cutiPart.setVisibility(View.GONE);
                        pengaduanPart.setVisibility(View.VISIBLE);
                    }
                } else {
                    cutiPart.setVisibility(View.GONE);
                    pengaduanPart.setVisibility(View.VISIBLE);
                }
            }
            clearancePart.setVisibility(View.VISIBLE);
            calendarPart.setVisibility(View.GONE);
        } else {
            cutiPart.setVisibility(View.GONE);
            pengaduanPart.setVisibility(View.VISIBLE);
            if(sharedPrefManager.getSpIdCor().equals("1")){
                if(sharedPrefManager.getSpIdJabatan().equals("29")||sharedPrefManager.getSpIdJabatan().equals("31")){
                    clearancePart.setVisibility(View.GONE);
                    calendarPart.setVisibility(View.VISIBLE);
                } else {
                    clearancePart.setVisibility(View.VISIBLE);
                    calendarPart.setVisibility(View.GONE);
                }
            } else if(sharedPrefManager.getSpIdCor().equals("3")){
                clearancePart.setVisibility(View.GONE);
                calendarPart.setVisibility(View.VISIBLE);
            }
        }

        if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpNik().equals("1309131210")){
            cardPart.setVisibility(View.GONE);
            sdmPart.setVisibility(View.VISIBLE);
        } else {
            cardPart.setVisibility(View.VISIBLE);
            sdmPart.setVisibility(View.GONE);
        }

        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_karyawan";
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
                                String tanggal_lahir = data.getString("tanggal_lahir");
                                String tanggal_masuk = data.getString("tanggal_masuk");
                                String status_karyawan = data.getString("status_karyawan");
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                String avatar = data.getString("avatar");
                                String weather_key = data.getString("weather_key");
                                String chat_room = data.getString("chat_room");
                                String news_part = data.getString("news_part");
                                String base_news_api = data.getString("base_news_api");
                                String defaut_news_category = data.getString("defaut_news_category");
                                String fitur_pengumuman = data.getString("fitur_pengumuman");
                                String join_reminder = data.getString("join_reminder");
                                String pengumuman_id = data.getString("pengumuman_id");
                                String pengumuman_date = data.getString("pengumuman_date");
                                String pengumuman_title = data.getString("pengumuman_title");
                                String pengumuman_desc = data.getString("pengumuman_desc");
                                String pengumuman_time = data.getString("pengumuman_time");

                                String id_corporate = data.getString("id_corporate");
                                String id_cab = data.getString("id_cab");
                                String id_dept = data.getString("id_dept");
                                String id_bagian = data.getString("id_bagian");
                                String id_jabatan = data.getString("id_jabatan");

                                if(news_part.equals("1")){
                                    newsPart.setVisibility(View.VISIBLE);
                                    newsPart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mContext, NewsActivity.class);
                                            intent.putExtra("api_url", base_news_api);
                                            intent.putExtra("defaut_news_category", defaut_news_category);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    newsPart.setVisibility(View.GONE);
                                }

                                if(!sharedPrefManager.getSpIdCor().equals(id_corporate)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_COR, id_corporate);
                                }
                                if(!sharedPrefManager.getSpIdCab().equals(id_cab)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, id_cab);
                                }
                                if(!sharedPrefManager.getSpIdHeadDept().equals(id_dept)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, id_dept);
                                }
                                if(!sharedPrefManager.getSpIdDept().equals(id_bagian)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, id_bagian);
                                }
                                if(!sharedPrefManager.getSpIdJabatan().equals(id_jabatan)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, id_jabatan);
                                }
                                if(!sharedPrefManager.getSpTglBergabung().equals(tanggal_masuk)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, tanggal_masuk);
                                }
                                if(!sharedPrefManager.getSpStatusKaryawan().equals(status_karyawan)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_KARYAWAN, status_karyawan);
                                }

                                if(!avatar.equals("default_profile.jpg")){
                                    String avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                    Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .resize(108, 108)
                                        .into(avatarUser);
                                } else {
                                    String avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                    Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .resize(108, 108)
                                        .into(avatarUser);
                                }

                                detailUserBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext, InfoPersonalActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                nameOfUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext, InfoPersonalActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, tanggal_masuk);
                                positionOfUser.setText(jabatan+" | "+bagian+" | "+department);

                                // Ulang tahun
                                if (!tanggal_lahir.equals("")&&!tanggal_lahir.equals("null")) {
                                    String tglBulanLahir = tanggal_lahir.substring(5, 10);
                                    String tahunLahir = tanggal_lahir.substring(0, 4);
                                    if (tglBulanLahir.equals(getDayMonth())) {
                                        ulangTahunPart.setVisibility(View.VISIBLE);
                                        String shortName = sharedPrefManager.getSpNama()+" ";
                                        if(shortName.contains(" ")){
                                            shortName = shortName.substring(0, shortName.indexOf(" "));
                                            System.out.println(shortName);
                                        }
                                        int usia = Integer.parseInt(getDateY()) - Integer.parseInt(tahunLahir);
                                        ulangTahunTo.setText("Happy Birthday " + shortName + ",");
                                        ulangTahunCelebrate.setText("Selamat Merayakan Ulang Tahun ke " + String.valueOf(usia) + " Tahun.");
                                        musicUlangTahun = MediaPlayer.create(mContext, R.raw.ringtone_birthday);
                                        playPart.setVisibility(View.VISIBLE);
                                        pausePart.setVisibility(View.GONE);

                                        if(!sharedPrefAbsen.getSpNotifUltah().equals("1")){
                                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_ULTAH, "1");
                                            getNotifBirthday(String.valueOf(usia));
                                        }

                                        ulangTahunPart.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (musicPlay.equals("off")){
                                                    playPart.setVisibility(View.GONE);
                                                    pausePart.setVisibility(View.VISIBLE);
                                                    musicUlangTahun.start();
                                                    musicPlay = "on";
                                                    checkMusic();
                                                } else {
                                                    playPart.setVisibility(View.VISIBLE);
                                                    pausePart.setVisibility(View.GONE);
                                                    musicUlangTahun.pause();
                                                    musicPlay = "off";
                                                }

                                                if (refeatConfeti.equals("1")) {
                                                    CommonConfetti.rainingConfetti(mainParent, colors).stream(3000);
                                                    refeatConfeti = "0";
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            refeatConfeti = "1";
                                                        }
                                                    }, 3000);
                                                }
                                            }
                                        });
                                    } else {
                                        ulangTahunPart.setVisibility(View.GONE);
                                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_ULTAH, "");
                                    }
                                } else {
                                    ulangTahunPart.setVisibility(View.GONE);
                                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_ULTAH, "");
                                }

                                // Perayaan tanggal masuk
                                if (!tanggal_masuk.equals("")&&!tanggal_masuk.equals("null")) {
                                    String tglBulanMasuk = tanggal_masuk.substring(5, 10);
                                    String tahunMasuk = tanggal_masuk.substring(0, 4);
                                    if (tglBulanMasuk.equals(getDayMonth())) {
                                        int masaKerja = Integer.parseInt(getDateY()) - Integer.parseInt(tahunMasuk);
                                        if(masaKerja > 0 && sharedPrefManager.getSpIdCor().equals("1")){
                                            congratCelebrate.setText("Selamat Merayakan " + String.valueOf(masaKerja) + " Tahun Masa Kerja.");
                                            if (join_reminder.equals("1")) {
                                                if(!sharedPrefAbsen.getSpNotifJoinRemainder().equals("1")){
                                                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_JOIN_REMAINDER, "1");
                                                    getNotifMasaKerja(String.valueOf(masaKerja));
                                                }

                                                congratTahunanPart.setVisibility(View.VISIBLE);
                                                congratTahunanPart.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (refeatConfeti.equals("1")) {
                                                            CommonConfetti.rainingConfetti(mainParent, colors).stream(3000);
                                                            refeatConfeti = "0";
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    refeatConfeti = "1";
                                                                }
                                                            }, 3000);
                                                        }
                                                    }
                                                });
                                            } else {
                                                congratTahunanPart.setVisibility(View.GONE);
                                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_JOIN_REMAINDER, "");
                                            }
                                        } else {
                                            congratTahunanPart.setVisibility(View.GONE);
                                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_JOIN_REMAINDER, "");
                                        }
                                    } else {
                                        congratTahunanPart.setVisibility(View.GONE);
                                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_JOIN_REMAINDER, "");
                                    }
                                } else {
                                    congratTahunanPart.setVisibility(View.GONE);
                                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_JOIN_REMAINDER, "");
                                }

                                // Pengumuman
                                if(fitur_pengumuman.equals("0")){
                                    bannerPengumumanPart.setVisibility(View.GONE);
                                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_PENGUMUMAN, "");
                                } else if(fitur_pengumuman.equals("1")) {
                                    if(pengumuman_date.equals(getDate())){
                                        if(!sharedPrefAbsen.getSpNotifPengumuman().equals("1")){
                                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_PENGUMUMAN, "1");
                                            getNotifPengumuman(pengumuman_id, pengumuman_title);
                                        }

                                        bannerPengumumanPart.setVisibility(View.VISIBLE);
                                        judulPengumuman.setText(pengumuman_title.toUpperCase());
                                        highlightPengumuman.setText("Tap untuk melihat informasi selengkapnya...");

                                        bannerPengumumanPart.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(mContext, DetailPengumumanActivity.class);
                                                intent.putExtra("id_pengumuman", String.valueOf(pengumuman_id));
                                                mContext.startActivity(intent);
                                            }
                                        });

                                    } else {
                                        bannerPengumumanPart.setVisibility(View.GONE);
                                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_PENGUMUMAN, "");
                                    }
                                }

                                if(id_corporate.equals("1")){
                                    if(chat_room.equals("1")){
                                        chatBTN.setVisibility(View.VISIBLE);
                                    } else {
                                        chatBTN.setVisibility(View.GONE);
                                    }
                                } else {
                                    chatBTN.setVisibility(View.GONE);
                                    personalNotifBTN.setVisibility(View.INVISIBLE);
                                    countNotificationPersonalPart.setVisibility(View.INVISIBLE);
                                }

                                getCurrentLocation(weather_key);
                                getDataPengumumanNew();
                                getCountPersonalNotif();

                                if(sharedPrefManager.getSpIdJabatan().equals("10")){
                                    getWaitingConfirm();
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
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getCurrentLocation(String weather_key) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            gpsEnableAction();
        }
        LocationServices.getFusedLocationProviderClient(mContext)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(mContext).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();

                            dateLive();
                            timeLive();

                            getCurrentWeather(weather_key, String.valueOf(lati), String.valueOf(longi));
                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchaddressfromlocation(location);
                        } else {
                            gpsEnableAction();
                        }
                    }
                }, Looper.getMainLooper());

    }

    private void gpsEnableAction(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(mActivity).checkLocationSettings(builder.build());
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
                                resolvable.startResolutionForResult(mActivity, LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    private void getDataPengumumanNew() {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/list_data_pengumuman_new";
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
                                String jumlah = data.getString("jumlah");

                                if (jumlah.equals("0")){
                                    listPengumumanNewRV.setVisibility(View.GONE);
                                    noDataPart.setVisibility(View.VISIBLE);
                                    loadingDataPart.setVisibility(View.GONE);
                                } else {
                                    listPengumumanNewRV.setVisibility(View.VISIBLE);
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    String data_pengumuman_baru = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataPengumumanNews = gson.fromJson(data_pengumuman_baru, DataPengumuman[].class);
                                    adapterListPengumunanNew = new AdapterListPengumumanNew(dataPengumumanNews, mContext);
                                    listPengumumanNewRV.setAdapter(adapterListPengumunanNew);
                                }

                                getCountMessageYet();

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
                params.put("id_cor", sharedPrefManager.getSpIdCor());
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
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY));
                                locationNow = resultData.getString(Constants.LOCAITY);
                            }
                        }
                    }
                } else {
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText(resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.POST_CODE);
                            } else {
                                currentAddress.setText("Lokasi tidak ditemukan");
                                locationNow = "Lokasi tidak ditemukan";
                            }
                        }
                    }
                }
            } else {
                currentAddress.setText(resultData.getString(Constants.NO_ADDRESS));
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

    private void getCurrentWeather(String api_key, String lat, String lon) {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                                } else if (description.equals("thunderstorm")){
                                    desc_idn = "Badai petir";
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
                                String url = "https://geloraaksara.co.id/absen-online/upload/weather_icon/vector/"+icon+".png";
                                String weatherMain = desc_idn;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(weatherIcon);

                                dataCuaca.setVisibility(View.VISIBLE);
                                dataCuacaEmpty.setVisibility(View.GONE);

                                weatherBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Detail Cuaca
                                        Intent intent = new Intent(mContext, DetailCuacaActivity.class);
                                        intent.putExtra("url_icon", url);
                                        intent.putExtra("main_weather", weatherMain);
                                        intent.putExtra("temp", String.valueOf(Math.round(convertFromKelvinToCelsius(f))));
                                        intent.putExtra("feel_like", String.valueOf(Math.round(convertFromKelvinToCelsius(f2))));
                                        intent.putExtra("location", locationNow);
                                        startActivity(intent);
                                    }
                                });

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
                dataCuacaEmpty.setVisibility(View.VISIBLE);
                weatherBTN.setOnClickListener(null);
            }
        });

        requestQueue.add(request);

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

    private void getCountMessageYet() {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_message_yet_read";
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
                                String message_count = data.getString("message_yet");
                                if (message_count.equals("0")){
                                    countNotificationMessage.setVisibility(View.GONE);
                                } else {
                                    countNotificationMessage.setVisibility(View.VISIBLE);
                                    countMessage.setText(String.valueOf(Integer.parseInt(message_count)));

                                    if(!sharedPrefAbsen.getSpNotifMessenger().equals("1")){
                                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_YET_BEFORE_MESSENGER, message_count);
                                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_MESSENGER, "1");

                                        String shortName = sharedPrefManager.getSpNama()+" ";
                                        if(shortName.contains(" ")){
                                            shortName = shortName.substring(0, shortName.indexOf(" "));
                                            System.out.println(shortName);
                                        }

                                        try {
                                            Intent intent = new Intent(mContext, ChatSplashScreenActivity.class);
                                            Notify.build(mContext)
                                                    .setTitle("HRIS Mobile Gelora")
                                                    .setContent("Halo "+shortName+", terdapat "+message_count+" pesan yang belum dibaca di Gelora Messenger")
                                                    .setSmallIcon(R.drawable.ic_skylight_notification)
                                                    .setColor(R.color.colorPrimary)
                                                    .largeCircularIcon()
                                                    .enableVibration(true)
                                                    .setAction(intent)
                                                    .show();
                                        } catch (IllegalArgumentException e){
                                            e.printStackTrace();
                                        }

                                        // Vibrate for 500 milliseconds
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                        } else {
                                            //deprecated in API 26
                                            vibrate.vibrate(500);
                                        }
                                    } else {
                                        if(!sharedPrefAbsen.getSpYetBeforeMessenger().equals(message_count)){
                                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_YET_BEFORE_MESSENGER, message_count);
                                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_MESSENGER, "1");

                                            String shortName = sharedPrefManager.getSpNama()+" ";
                                            if(shortName.contains(" ")){
                                                shortName = shortName.substring(0, shortName.indexOf(" "));
                                                System.out.println(shortName);
                                            }

                                            try {
                                                Intent intent = new Intent(mContext, ChatSplashScreenActivity.class);
                                                Notify.build(mContext)
                                                        .setTitle("HRIS Mobile Gelora")
                                                        .setContent("Halo "+shortName+", terdapat "+message_count+" pesan yang belum dibaca di Gelora Messenger")
                                                        .setSmallIcon(R.drawable.ic_skylight_notification)
                                                        .setColor(R.color.colorPrimary)
                                                        .largeCircularIcon()
                                                        .enableVibration(true)
                                                        .setAction(intent)
                                                        .show();
                                            } catch (IllegalArgumentException e){
                                                e.printStackTrace();
                                            }

                                            // Vibrate for 500 milliseconds
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //deprecated in API 26
                                                vibrate.vibrate(500);
                                            }
                                        }
                                    }

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
                return params;
            }
        };

        requestQueue.add(postRequest);

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

    private void checkMusic() {
        if(String.valueOf(musicUlangTahun.isPlaying()).equals("false")){
            playPart.setVisibility(View.VISIBLE);
            pausePart.setVisibility(View.GONE);
            musicUlangTahun.pause();
            musicPlay = "off";
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkMusic();
                }
            }, 1000);
        }
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

    private String getDayMonth() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM-dd");
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

    private static float convertFromKelvinToCelsius(float value) {
        return value - 273.15f;
    }

    private void getContact() {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        if(sharedPrefManager.getSpIdCor().equals("1")){
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

                                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                                try {
                                    startActivity(webIntent);
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("Tidak dapat terhubung ke Whatsapp, anda bisa hubungi secara langsung ke 0"+whatsapp.substring(2, whatsapp.length())+" atas nama Bapak "+nama+" bagian HRD")
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
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    new KAlertDialog(mContext, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Gagal terhubung, harap periksa jaringan anda")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            requestQueue.add(request);

        } else if(sharedPrefManager.getSpIdCor().equals("3")){
            final String url = "https://geloraaksara.co.id/absen-online/api/get_contact_erlass";
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

                                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                                try {
                                    startActivity(webIntent);
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("Tidak dapat terhubung ke Whatsapp, anda bisa hubungi secara langsung ke 0"+whatsapp.substring(2, whatsapp.length())+" atas nama Bapak "+nama+" bagian HRD")
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
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    new KAlertDialog(mContext, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Gagal terhubung, harap periksa jaringan anda")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            requestQueue.add(request);

        }

    }

    private void getNotifMasaKerja(String lama) {
        String shortName = sharedPrefManager.getSpNama()+" ";
        if(shortName.contains(" ")){
            shortName = shortName.substring(0, shortName.indexOf(" "));
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "notif_masa_kerja";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Selamat Merayakan " + lama + " Tahun Masa Kerja di PT. Gelora Aksara Pratama"))
                .setContentText("Selamat Merayakan " + lama + " Tahun Masa Kerja di PT. Gelora Aksara Pratama");

        Intent notificationIntent = new Intent(mContext, HomeActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void getNotifBirthday(String age) {
        String shortName = sharedPrefManager.getSpNama()+" ";
        if(shortName.contains(" ")){
            shortName = shortName.substring(0, shortName.indexOf(" "));
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "notif_birthday";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Happy Birthday "+shortName+", Selamat Merayakan Ulang Tahun ke " + age + " Tahun."))
                .setContentText("Happy Birthday "+shortName+", Selamat Merayakan Ulang Tahun ke " + age + " Tahun.");

        Intent notificationIntent = new Intent(mContext, HomeActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void getNotifPengumuman(String id, String title) {
        String shortName = sharedPrefManager.getSpNama()+" ";
        if(shortName.contains(" ")){
            shortName = shortName.substring(0, shortName.indexOf(" "));
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "notif_pengumuman";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title+"\nTap untuk melihat informasi selengkapnya..."))
                .setContentText(title);

        Intent notificationIntent = new Intent(mContext, DetailPengumumanActivity.class);
        notificationIntent.putExtra("id_pengumuman", id);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void getWaitingConfirm(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_waiting_data";
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
                            if (status.equals("Success")) {
                                String jumlah_penilaian = data.getString("jumlah_penilaian");
                                String jumlah_sdm = data.getString("jumlah_sdm");
                                if(Integer.parseInt(jumlah_penilaian)+Integer.parseInt(jumlah_sdm)>0){
                                    countNotificationPenilaian.setVisibility(View.VISIBLE);
                                    countNotifPenilaianTV.setText(String.valueOf(Integer.parseInt(jumlah_penilaian)+Integer.parseInt(jumlah_sdm)));
                                } else {
                                    countNotificationPenilaian.setVisibility(View.GONE);
                                    countNotifPenilaianTV.setText("");
                                }
                            } else {
                                countNotificationPenilaian.setVisibility(View.GONE);
                                countNotifPenilaianTV.setText("");
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
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("id_departemen", sharedPrefManager.getSpIdHeadDept());
                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    private void getCountPersonalNotif(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/count_notif_yet_read";
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
                            if (status.equals("Success")) {
                                String jumlah_data = data.getString("jumlah");
                                if(Integer.parseInt(jumlah_data)>0){
                                    countNotificationPersonalPart.setVisibility(View.VISIBLE);
                                    countNotificationPersonalTV.setText(jumlah_data);
                                    String shortName = sharedPrefManager.getSpNama()+" ";
                                    if(shortName.contains(" ")){
                                        shortName = shortName.substring(0, shortName.indexOf(" "));
                                        System.out.println(shortName);
                                    }

                                    try {
                                        Intent intent = new Intent(mContext, PersonalNotificationActivity.class);
                                        Notify.build(mContext)
                                                .setTitle("HRIS Mobile Gelora")
                                                .setContent("Halo "+shortName+", terdapat "+jumlah_data+" notifikasi personal yang belum dilihat")
                                                .setSmallIcon(R.drawable.ic_skylight_notification)
                                                .setColor(R.color.colorPrimary)
                                                .largeCircularIcon()
                                                .enableVibration(true)
                                                .setAction(intent)
                                                .show();
                                    } catch (IllegalArgumentException e){
                                        e.printStackTrace();
                                    }

                                    // Vibrate for 500 milliseconds
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        //deprecated in API 26
                                        vibrate.vibrate(500);
                                    }
                                } else {
                                    countNotificationPersonalPart.setVisibility(View.GONE);
                                    countNotificationPersonalTV.setText("");
                                }
                            } else {
                                countNotificationPersonalPart.setVisibility(View.GONE);
                                countNotificationPersonalTV.setText("");
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);
    }

    private void connectionFailed(){
        CookieBar.build(mActivity)
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
    public void onResume() {
        super.onResume();
        requestQueue = Volley.newRequestQueue(mContext);
        getDataKaryawan();
    }

}
