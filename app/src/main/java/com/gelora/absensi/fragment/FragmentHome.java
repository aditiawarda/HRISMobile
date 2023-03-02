package com.gelora.absensi.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
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
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.CalendarPageActivity;
import com.gelora.absensi.ChatSplashScreenActivity;
import com.gelora.absensi.ComingSoonActivity;
import com.gelora.absensi.DetailCuacaActivity;
import com.gelora.absensi.DetailPengumumanActivity;
import com.gelora.absensi.DigitalCardActivity;
import com.gelora.absensi.DigitalSignatureActivity;
import com.gelora.absensi.FetchAddressIntentServices;
import com.gelora.absensi.FormFingerscanActivity;
import com.gelora.absensi.FormPermohonanCutiActivity;
import com.gelora.absensi.FormPermohonanIzinActivity;
import com.gelora.absensi.InfoPersonalActivity;
import com.gelora.absensi.ListAllPengumumanActivity;
import com.gelora.absensi.LoginActivity;
import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.UserActivity;
import com.gelora.absensi.UserDetailActivity;
import com.gelora.absensi.adapter.AdapterListPengumumanNew;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataPengumuman;
import com.github.jinatonic.confetti.CommonConfetti;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

    LinearLayout bannerPengumumanPart, congratTahunanPart, ulangTahunPart, cutiPart, pengaduanPart, countNotificationMessage, chatBTN, noDataPart, loadingDataPart, detailUserBTN, homePart, menuAbsensiBTN, menuIzinBTN, menuCutiBTN, menuPengaduanBTN, menuFingerBTN, menuLemburBTN, menuSignatureBTN, menuCardBTN, menuCalendarBTN;
    TextView highlightPengumuman, judulPengumuman, congratCelebrate, ulangTahunCelebrate, countMessage, pengumumanSelengkapnyaBTN, currentDate, hTime, mTime, sTime, nameOfUser, positionOfUser ,mainWeather, weatherTemp, feelsLikeTemp, currentAddress;
    ProgressBar loadingProgressBarCuaca;
    ImageView avatarUser, weatherIcon, loadingData;
    RelativeLayout dataCuaca, dataCuacaEmpty;

    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String currentDay = "", refeatConfeti = "1", locationNow = "";
    ResultReceiver resultReceiver;
    Context mContext;
    Activity mActivity;
    protected ViewGroup mainParent;

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
        bottomSheet = view.findViewById(R.id.bottom_sheet_layout);
        mainParent = view.findViewById(R.id.main_parent);
        refreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);
        countNotificationMessage = view.findViewById(R.id.count_notification_message);
        countMessage = view.findViewById(R.id.count_message);
        nameOfUser = view.findViewById(R.id.name_of_user);
        avatarUser = view.findViewById(R.id.avatar_user);
        detailUserBTN = view.findViewById(R.id.detail_user_btn);
        menuAbsensiBTN = view.findViewById(R.id.menu_absensi_btn);
        positionOfUser = view.findViewById(R.id.position_of_user);
        menuIzinBTN = view.findViewById(R.id.menu_izin_btn);
        menuCutiBTN = view.findViewById(R.id.menu_cuti_btn);
        menuPengaduanBTN = view.findViewById(R.id.menu_pengaduan_btn);
        menuFingerBTN = view.findViewById(R.id.menu_finger_btn);
        menuLemburBTN = view.findViewById(R.id.menu_lembur_btn);
        menuSignatureBTN = view.findViewById(R.id.menu_signature_btn);
        menuCardBTN = view.findViewById(R.id.menu_card_btn);
        menuCalendarBTN = view.findViewById(R.id.menu_calendar_btn);
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

        ulangTahunPart = view.findViewById(R.id.ulang_tahun_part);
        congratTahunanPart = view.findViewById(R.id.congrat_tahunan);
        bannerPengumumanPart = view.findViewById(R.id.banner_pengumuman);

        ulangTahunCelebrate = view.findViewById(R.id.ulang_tahun_celebrate);
        congratCelebrate = view.findViewById(R.id.congrat_celebrate);
        judulPengumuman = view.findViewById(R.id.judul_pengumuman);
        highlightPengumuman = view.findViewById(R.id.highlight_pengumuman);

        cutiPart = view.findViewById(R.id.cuti_part);
        pengaduanPart = view.findViewById(R.id.pengaduan_part);

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

        menuAbsensiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
            }
        });

        menuIzinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormPermohonanIzinActivity.class);
                startActivity(intent);
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
                        .setCancelText("       BATAL       ")
                        .setConfirmText("HUBUNGKAN")
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
                Intent intent = new Intent(mContext, FormFingerscanActivity.class);
                startActivity(intent);
            }
        });

        menuLemburBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComingSoonActivity.class);
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

        menuCalendarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CalendarPageActivity.class);
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

        if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")||sharedPrefManager.getSpStatusKaryawan().equals("Kontrak")){
            if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")){
                cutiPart.setVisibility(View.VISIBLE);
                pengaduanPart.setVisibility(View.GONE);
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
        } else {
            cutiPart.setVisibility(View.GONE);
            pengaduanPart.setVisibility(View.VISIBLE);
        }

        getDataKaryawan();
        getCurrentDay();
        nameOfUser.setText(sharedPrefManager.getSpNama());

        return view;
    }

    private void getDataKaryawan() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                                String fitur_pengumuman = data.getString("fitur_pengumuman");
                                String join_reminder = data.getString("join_reminder");
                                String pengumuman_date = data.getString("pengumuman_date");
                                String pengumuman_title = data.getString("pengumuman_title");
                                String pengumuman_desc = data.getString("pengumuman_desc");
                                String pengumuman_image = data.getString("pengumuman_image");
                                String pengumuman_time = data.getString("pengumuman_time");

                                String id_cab = data.getString("id_cab");
                                String id_dept = data.getString("id_dept");
                                String id_bagian = data.getString("id_bagian");
                                String id_jabatan = data.getString("id_jabatan");

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
                                        .into(avatarUser);
                                } else {
                                    String avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                    Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
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
                                        int usia = Integer.parseInt(getDateY()) - Integer.parseInt(tahunLahir);
                                        ulangTahunCelebrate.setText("Selamat Merayakan Ulang Tahun ke " + String.valueOf(usia) + " Tahun.");
                                        CommonConfetti.rainingConfetti(mainParent, colors).stream(3500);
                                        ulangTahunPart.setOnClickListener(new View.OnClickListener() {
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
                                        ulangTahunPart.setVisibility(View.GONE);
                                    }
                                } else {
                                    ulangTahunPart.setVisibility(View.GONE);
                                }

                                // Perayaan tanggal masuk
                                if (!tanggal_masuk.equals("")&&!tanggal_masuk.equals("null")) {
                                    String tglBulanMasuk = tanggal_masuk.substring(5, 10);
                                    String tahunMasuk = tanggal_masuk.substring(0, 4);
                                    if (tglBulanMasuk.equals(getDayMonth())) {
                                        int masaKerja = Integer.parseInt(getDateY()) - Integer.parseInt(tahunMasuk);
                                        congratCelebrate.setText("Selamat Merayakan " + String.valueOf(masaKerja) + " Tahun Masa Kerja.");
                                        if (join_reminder.equals("1")) {
                                            congratTahunanPart.setVisibility(View.VISIBLE);
                                            CommonConfetti.rainingConfetti(mainParent, colors).stream(3500);
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
                                        }
                                    } else {
                                        congratTahunanPart.setVisibility(View.GONE);
                                    }
                                } else {
                                    congratTahunanPart.setVisibility(View.GONE);
                                }

                                // Pengumuman
                                if(fitur_pengumuman.equals("0")){
                                    bannerPengumumanPart.setVisibility(View.GONE);
                                } else if(fitur_pengumuman.equals("1")) {
                                    if(pengumuman_date.equals(getDate())){
                                        bannerPengumumanPart.setVisibility(View.VISIBLE);
                                        judulPengumuman.setText(pengumuman_title.toUpperCase());
                                        highlightPengumuman.setText(pengumuman_desc);
                                        bannerPengumumanPart.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(mContext, DetailPengumumanActivity.class);
                                                intent.putExtra("image", String.valueOf(pengumuman_image));
                                                intent.putExtra("title", String.valueOf(pengumuman_title));
                                                intent.putExtra("deskripsi", String.valueOf(pengumuman_desc));
                                                intent.putExtra("date", String.valueOf(pengumuman_date));
                                                intent.putExtra("time", String.valueOf(pengumuman_time));
                                                mContext.startActivity(intent);
                                            }
                                        });

                                    } else {
                                        bannerPengumumanPart.setVisibility(View.GONE);
                                    }
                                }


                                getCurrentLocation(weather_key);
                                getDataPengumumanNew();

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
                        }
                    }
                }, Looper.getMainLooper());

    }

    private void getDataPengumumanNew() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                params.put("request", "request");
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
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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

                                dataCuaca.setOnClickListener(new View.OnClickListener() {
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
                dataCuaca.setOnClickListener(null);
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
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                            startActivity(webIntent);

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
        getDataKaryawan();
    }

}
