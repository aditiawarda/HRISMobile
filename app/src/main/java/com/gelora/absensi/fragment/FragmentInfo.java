package com.gelora.absensi.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.CompanyActivity;
import com.gelora.absensi.DetailHadirActivity;
import com.gelora.absensi.DetailKelebihanJamActivity;
import com.gelora.absensi.DetailLayoffActivity;
import com.gelora.absensi.DetailPulangCepatActivity;
import com.gelora.absensi.DetailTerlambatActivity;
import com.gelora.absensi.DetailTidakCheckoutActivity;
import com.gelora.absensi.DetailTidakHadirActivity;
import com.gelora.absensi.FaqActivity;
import com.gelora.absensi.HistoryActivity;
import com.gelora.absensi.HistoryCutiIzinActivity;
import com.gelora.absensi.HumanResourceActivity;
import com.gelora.absensi.ListNotifikasiActivity;
import com.gelora.absensi.ListNotifikasiFingerscanActivity;
import com.gelora.absensi.MonitoringAbsensiBagianActivity;
import com.gelora.absensi.NewsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.kalert.KAlertDialog;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.aviran.cookiebar2.CookieBar;
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
public class FragmentInfo extends Fragment {

    TextView descContactHRDTV, titlePage, dateNowTV, countNotifFingerTV, countNotifIzinTV;
    ExpandableLayout aboutAppField, privacyPolicyField, contactServiceField;
    LinearLayout newsPart, sdmBTN, dasboardStatistikAbsen, countNotificationIzin, countNotificationFinger, sisaCutiData, sisaCutiBTN, monitoringStaffBTN, faqBTN, connectBTN, contactServiceBTN, privacyPolicyBTN, aboutAppBTN, aboutCompanyBTN, permohonanCutiBTN, permohonanFingerBTN, selectMonthBTN, markerWarningAlpha, markerWarningLate, markerWarningNoCheckout, kelebihanJamBTN, pulangCepatBTN, layoffBTN, tidakCheckoutBTN, terlambatBTN, hadirBTN, tidakHadirBTN;
    TextView bagianNameTVSDM, historyBTN, tglBergabungMainTV, yearCR, sisaCutiTV, periodeUpdateSisaCutiTV, dateUpdateSisaCutiTV, countMessage, countNotifTV, notePantau, titlePantau, bagianNameTV, hTime, mTime, sTime, kelebihanJamData, pulangCepatData, layoffData, noCheckoutData, terlambatData, currentDate, mainWeather, feelsLikeTemp, weatherTemp, currentAddress, batasBagDept, bulanData, tahunData, hadirData, tidakHadirData, statusIndicator, descAvailable, descEmtpy, statusUserTV, eventCalender, yearTV, monthTV, nameUserTV, nikTV, departemenTV, bagianTV, jabatanTV;
    ImageView notifFiturLoading, sisaCutiLoading, positionLoadingImg, notificationWarningAlpha, notificationWarningNocheckout, notificationWarningLate, kelebihanJamLoading, pulangCepatLoading, layoffLoading, noCheckoutLoading, terlambatLoading, weatherIcon, bulanLoading, hadirLoading, tidakHadirLoading, avatarUser, imageUserBS;
    SwipeRefreshLayout refreshLayout;
    String selectMonth = "", statusFiturIzinCuti = "1", statusFiturFinger = "1", currentDay = "";
    Context mContext;
    Activity mActivity;
    SharedPrefManager sharedPrefManager;
    RequestQueue requestQueue;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mContext = getContext();
        mActivity = getActivity();

        sharedPrefManager = new SharedPrefManager(mContext);
        requestQueue = Volley.newRequestQueue(mContext);
        refreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);
        titlePage = view.findViewById(R.id.title_page);
        dateNowTV = view.findViewById(R.id.date_tv);
        permohonanCutiBTN = view.findViewById(R.id.permohonan_cuti_btn);
        permohonanFingerBTN = view.findViewById(R.id.permohonan_finger_btn);
        historyBTN = view.findViewById(R.id.history_btn);
        selectMonthBTN = view.findViewById(R.id.select_month_btn);
        bulanData = view.findViewById(R.id.bulan_data);
        tahunData = view.findViewById(R.id.tahun_data);
        hadirData = view.findViewById(R.id.data_hadir);
        tidakHadirData = view.findViewById(R.id.data_tidak_hadir);
        bulanLoading = view.findViewById(R.id.bulan_loading);
        hadirLoading = view.findViewById(R.id.hadir_loading);
        tidakHadirLoading = view.findViewById(R.id.tidak_hadir_loading);
        terlambatLoading = view.findViewById(R.id.terlambat_loading);
        noCheckoutLoading = view.findViewById(R.id.no_checkout_loading);
        pulangCepatLoading = view.findViewById(R.id.pulang_cepat_loading);
        kelebihanJamLoading = view.findViewById(R.id.kelebihan_jam_loading);
        layoffLoading = view.findViewById(R.id.layoff_loading);
        terlambatBTN = view.findViewById(R.id.terlambat_btn);
        tidakCheckoutBTN = view.findViewById(R.id.tidak_checkout_btn);
        pulangCepatBTN = view.findViewById(R.id.pulang_cepat_btn);
        layoffBTN = view.findViewById(R.id.layoff_btn);
        kelebihanJamBTN = view.findViewById(R.id.kelebihan_jam_btn);
        tidakHadirBTN = view.findViewById(R.id.btn_tidak_hadir);
        hadirBTN = view.findViewById(R.id.btn_hadir);
        terlambatData = view.findViewById(R.id.data_terlambat);
        noCheckoutData = view.findViewById(R.id.data_no_checkout);
        pulangCepatData = view.findViewById(R.id.data_pulang_cepat);
        kelebihanJamData = view.findViewById(R.id.data_Kelebihan_jam);
        layoffData = view.findViewById(R.id.data_layoff);
        markerWarningAlpha = view.findViewById(R.id.marker_warning_alpha);
        markerWarningLate = view.findViewById(R.id.marker_warning_late);
        markerWarningNoCheckout = view.findViewById(R.id.marker_warning_nocheckout);
        notificationWarningLate = view.findViewById(R.id.warning_gif_absen_late);
        notificationWarningNocheckout = view.findViewById(R.id.warning_gif_absen_nocheckout);
        notificationWarningAlpha = view.findViewById(R.id.warning_gif_absen_alpha);
        aboutCompanyBTN = view.findViewById(R.id.about_company_btn);
        aboutAppBTN = view.findViewById(R.id.about_app_btn);
        privacyPolicyBTN = view.findViewById(R.id.privacy_policy_btn);
        aboutAppField = view.findViewById(R.id.about_app_field);
        privacyPolicyField = view.findViewById(R.id.privacy_policy_field);
        contactServiceBTN = view.findViewById(R.id.contact_service_btn);
        contactServiceField = view.findViewById(R.id.contact_service_field);
        connectBTN = view.findViewById(R.id.connect_btn);
        faqBTN = view.findViewById(R.id.faq_btn);
        monitoringStaffBTN = view.findViewById(R.id.monitoring_staff_btn);
        titlePantau = view.findViewById(R.id.title_pantau);
        notePantau = view.findViewById(R.id.note_pantau);
        sisaCutiBTN = view.findViewById(R.id.sisa_cuti_btn);
        dateUpdateSisaCutiTV = view.findViewById(R.id.date_update_sisa_cuti_tv);
        periodeUpdateSisaCutiTV = view.findViewById(R.id.periode_update_sisa_cuti_tv);
        sisaCutiTV = view.findViewById(R.id.sisa_cuti_tv);
        sisaCutiData = view.findViewById(R.id.sisa_cuti_data);
        sisaCutiLoading = view.findViewById(R.id.sisa_cuti_loading);
        countNotificationIzin = view.findViewById(R.id.count_notification_izin);
        countNotificationFinger = view.findViewById(R.id.count_notification_finger);
        countNotifIzinTV = view.findViewById(R.id.count_notif_izin_tv);
        countNotifFingerTV = view.findViewById(R.id.count_notif_finger_tv);
        dasboardStatistikAbsen = view.findViewById(R.id.dasboard_statistik_absen);
        bagianNameTV = view.findViewById(R.id.bagian_name_tv);
        bagianNameTVSDM = view.findViewById(R.id.bagian_name_tv_sdm);
        sdmBTN = view.findViewById(R.id.sdm_btn);
        descContactHRDTV = view.findViewById(R.id.desc_contact_hrd_tv);
        newsPart = view.findViewById(R.id.news_part);

        selectMonth = getBulanTahun();
        dateNowTV.setText(getDate().substring(8,10)+"/"+getDate().substring(5,7)+"/"+getDate().substring(0,4));

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(bulanLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(hadirLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(tidakHadirLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(noCheckoutLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(terlambatLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(pulangCepatLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(kelebihanJamLoading);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(layoffLoading);

        Glide.with(mContext)
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningLate);

        Glide.with(mContext)
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningNocheckout);

        Glide.with(mContext)
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningAlpha);

        Glide.with(mContext)
                .load(R.drawable.loading_dots)
                .into(sisaCutiLoading);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aboutAppField.collapse();
                privacyPolicyField.collapse();
                contactServiceField.collapse();

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

                sisaCutiData.setVisibility(View.GONE);
                sisaCutiLoading.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getPersonalization();
                        getCurrentDay();
                    }
                }, 1000);
            }
        });

        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HistoryActivity.class);
                startActivity(intent);
            }
        });

        permohonanCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListNotifikasiActivity.class);
                startActivity(intent);
            }
        });

        permohonanFingerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListNotifikasiFingerscanActivity.class);
                startActivity(intent);
            }
        });

        selectMonthBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(mContext,
                        new MonthPickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(int month, int year) { // on date set
                                String bulan = "", bulanName = "";
                                if(month==0){
                                    bulan = "01";
                                } else if (month==1){
                                    bulan = "02";
                                } else if (month==2){
                                    bulan = "03";
                                } else if (month==3){
                                    bulan = "04";
                                } else if (month==4){
                                    bulan = "05";
                                } else if (month==5){
                                    bulan = "06";
                                } else if (month==6){
                                    bulan = "07";
                                } else if (month==7){
                                    bulan = "08";
                                } else if (month==8){
                                    bulan = "09";
                                } else if (month==9){
                                    bulan = "10";
                                } else if (month==10){
                                    bulan = "11";
                                } else if (month==11){
                                    bulan = "12";
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
                                        getDataAbsensi();
                                    }
                                }, 100);

                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH));

                builder.setMinYear(1952)
                        .setActivatedYear(now.get(Calendar.YEAR))
                        .setMaxYear(now.get(Calendar.YEAR))
                        .setActivatedMonth(now.get(Calendar.MONTH))
                        .build()
                        .show();

            }
        });

        layoffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailLayoffActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        kelebihanJamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailKelebihanJamActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        pulangCepatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPulangCepatActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        tidakCheckoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailTidakCheckoutActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        terlambatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailTerlambatActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        tidakHadirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailTidakHadirActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        hadirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailHadirActivity.class);
                intent.putExtra("bulan", selectMonth);
                startActivity(intent);
            }
        });

        sisaCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HistoryCutiIzinActivity.class);
                startActivity(intent);
            }
        });

        monitoringStaffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MonitoringAbsensiBagianActivity.class);
                startActivity(intent);
            }
        });

        sdmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HumanResourceActivity.class);
                startActivity(intent);
            }
        });

        aboutCompanyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CompanyActivity.class);
                startActivity(intent);
            }
        });

        aboutAppBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aboutAppField.isExpanded()){
                    aboutAppField.collapse();
                } else {
                    aboutAppField.expand();
                }
            }
        });

        privacyPolicyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(privacyPolicyField.isExpanded()){
                    privacyPolicyField.collapse();
                } else {
                    privacyPolicyField.expand();
                }
            }
        });

        contactServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactServiceField.isExpanded()){
                    contactServiceField.collapse();
                } else {
                    contactServiceField.expand();
                    getContact();
                }
            }
        });

        connectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CompanyActivity.class);
                startActivity(intent);
            }
        });

        faqBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FaqActivity.class);
                intent.putExtra("status_karyawan", sharedPrefManager.getSpStatusKaryawan());
                intent.putExtra("tanggal_bergabung", sharedPrefManager.getSpTglBergabung());
                intent.putExtra("status_fitur", statusFiturIzinCuti);
                intent.putExtra("status_finger", statusFiturFinger);
                startActivity(intent);
            }
        });

        if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")){
            titlePage.setText("HRIS Mobile");
            dasboardStatistikAbsen.setVisibility(View.GONE);
            faqBTN.setVisibility(View.GONE);
        } else {
            titlePage.setText("Info");
            dasboardStatistikAbsen.setVisibility(View.VISIBLE);
            faqBTN.setVisibility(View.VISIBLE);
        }

        getPersonalization();
        getCurrentDay();

        return view;
    }

    private void getDataAbsensi() {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                                String alpa = data.getString("alpa");
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

    private void getPersonalization() {

        if(sharedPrefManager.getSpNik().equals("80085")){
            sdmBTN.setVisibility(View.VISIBLE);
        } else {
            sdmBTN.setVisibility(View.GONE);
        }

        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/personalization";
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
                                String fitur_izin = data.getString("fitur_izin");
                                String fitur_finger = data.getString("fitur_finger");
                                String monitoring = data.getString("monitoring");
                                String sisa_cuti_info = data.getString("sisa_cuti_info");
                                String sisa_cuti = data.getString("sisa_cuti");
                                String periode_mulai = data.getString("periode_mulai");
                                String periode_akhir = data.getString("periode_akhir");
                                String bagian = data.getString("bagian");
                                String departemen = data.getString("departemen");
                                String id_corporate = data.getString("id_corporate");
                                String news_part = data.getString("news_part");
                                String base_news_api = data.getString("base_news_api");
                                String defaut_news_category = data.getString("defaut_news_category");

                                statusFiturIzinCuti = fitur_izin;
                                statusFiturFinger = fitur_finger;

                                if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")){
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
                                } else {
                                    newsPart.setVisibility(View.GONE);
                                }

                                if (monitoring.equals("1")) {
                                    if (sharedPrefManager.getSpIdJabatan().equals("10")) {
                                        monitoringStaffBTN.setVisibility(View.VISIBLE);
                                        titlePantau.setText("Pantau kehadiran departemen*");
                                        notePantau.setText("*Fitur khusus Kepala Departemen");
                                        bagianNameTV.setText(departemen);
                                        bagianNameTVSDM.setText(departemen);
                                    } else if (sharedPrefManager.getSpIdJabatan().equals("3")){
                                        monitoringStaffBTN.setVisibility(View.VISIBLE);
                                        titlePantau.setText("Pantau kehadiran departemen*");
                                        notePantau.setText("*Fitur khusus Ka.Dept & Ast.Ka.Dept");
                                        bagianNameTV.setText(departemen);
                                        bagianNameTVSDM.setText(departemen);
                                    } else if (sharedPrefManager.getSpIdJabatan().equals("11")){
                                        monitoringStaffBTN.setVisibility(View.VISIBLE);
                                        titlePantau.setText("Pantau kehadiran bagian*");
                                        notePantau.setText("*Fitur khusus Kepala Bagian");
                                        bagianNameTV.setText(bagian);
                                        bagianNameTVSDM.setText(bagian);
                                    } else {
                                        monitoringStaffBTN.setVisibility(View.GONE);
                                    }
                                } else {
                                    monitoringStaffBTN.setVisibility(View.GONE);
                                }

                                if(sisa_cuti_info.equals("1")){
                                    if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")){
                                        sisaCutiBTN.setVisibility(View.VISIBLE);
                                        sisaCutiTV.setText(sisa_cuti);
                                        periodeUpdateSisaCutiTV.setText("Periode "+periode_mulai.substring(8,10)+"/"+periode_mulai.substring(5,7)+"/"+periode_mulai.substring(0,4)+"  s.d. "+periode_akhir.substring(8,10)+"/"+periode_akhir.substring(5,7)+"/"+periode_akhir.substring(0,4));
                                        sisaCutiData.setVisibility(View.VISIBLE);
                                        sisaCutiLoading.setVisibility(View.GONE);

                                    } else if(sharedPrefManager.getSpStatusKaryawan().equals("Kontrak")){
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
                                            sisaCutiBTN.setVisibility(View.VISIBLE);
                                            sisaCutiTV.setText(sisa_cuti);
                                            periodeUpdateSisaCutiTV.setText("Periode "+periode_mulai.substring(8,10)+"/"+periode_mulai.substring(5,7)+"/"+periode_mulai.substring(0,4)+"  s.d. "+periode_akhir.substring(8,10)+"/"+periode_akhir.substring(5,7)+"/"+periode_akhir.substring(0,4));
                                            sisaCutiData.setVisibility(View.VISIBLE);
                                            sisaCutiLoading.setVisibility(View.GONE);
                                        } else {
                                            sisaCutiBTN.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    sisaCutiBTN.setVisibility(View.GONE);
                                }

                                if(!id_corporate.equals("1")){ // PT. Gelora Aksara Pratama
                                    aboutCompanyBTN.setVisibility(View.GONE);
                                    descContactHRDTV.setText("Untuk layanan pengaduan anda akan terhubung dengan bagian HRD perusahaan");
                                } else {
                                    aboutCompanyBTN.setVisibility(View.VISIBLE);
                                    descContactHRDTV.setText("Untuk layanan pengaduan anda akan terhubung dengan bagian HRD PT. Gelora Aksara Pratama");
                                }

                            }

                            checkNotification();

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

    private void checkNotification() {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_notification_info";
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
                                String count = data.getString("count_izin");
                                String count_finger = data.getString("count_finger");

                                countNotifIzinTV.setText(count);
                                countNotifFingerTV.setText(count_finger);

                                if (count.equals("0") && count_finger.equals("0")){
                                    countNotificationIzin.setVisibility(View.GONE);
                                    countNotificationFinger.setVisibility(View.GONE);
                                } else if (!count.equals("0") && count_finger.equals("0")) {
                                    countNotificationIzin.setVisibility(View.VISIBLE);
                                    countNotificationFinger.setVisibility(View.GONE);
                                } else if (count.equals("0") && !count_finger.equals("0")) {
                                    countNotificationIzin.setVisibility(View.GONE);
                                    countNotificationFinger.setVisibility(View.VISIBLE);
                                } else if (!count.equals("0") && !count_finger.equals("0")) {
                                    countNotificationIzin.setVisibility(View.VISIBLE);
                                    countNotificationFinger.setVisibility(View.VISIBLE);
                                }

                            } else {
                                countNotificationIzin.setVisibility(View.GONE);
                                countNotificationFinger.setVisibility(View.GONE);
                            }

                            getDataAbsensi();

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

    private void getContact() {
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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

                            connectBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
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

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
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

        dateLive();

    }

    @SuppressLint("SetTextI18n")
    private void dateLive(){
        switch (getDateM()) {
            case "01":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                break;
            case "02":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                break;
            case "03":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                break;
            case "04":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                break;
            case "05":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                break;
            case "06":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                dateUpdateSisaCutiTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                break;
            case "07":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                break;
            case "08":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                break;
            case "09":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                break;
            case "10":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                break;
            case "11":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                break;
            case "12":
                dateNowTV.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                dateUpdateSisaCutiTV.setText("Per "+currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                break;
            default:
                dateNowTV.setText("Not found!");
                dateUpdateSisaCutiTV.setText("Not found!");
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        requestQueue = Volley.newRequestQueue(mContext);
        getDataAbsensi();
        getPersonalization();
        aboutAppField.collapse();
        privacyPolicyField.collapse();
        contactServiceField.collapse();
    }

}
