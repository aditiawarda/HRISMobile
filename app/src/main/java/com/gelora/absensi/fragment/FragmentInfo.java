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

import androidx.appcompat.app.AlertDialog;
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
import com.gelora.absensi.ChatSplashScreenActivity;
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
import com.gelora.absensi.ListNotifikasiActivity;
import com.gelora.absensi.ListNotifikasiFingerscanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.UserActivity;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentInfo extends Fragment {

    TextView dateNowTV;
    ExpandableLayout aboutAppField, privacyPolicyField, contactServiceField;
    LinearLayout faqBTN, connectBTN, contactServiceBTN, privacyPolicyBTN, aboutAppBTN, aboutCompanyBTN, permohonanCutiBTN, permohonanFingerBTN, selectMonthBTN, markerWarningAlpha, markerWarningLate, markerWarningNoCheckout, kelebihanJamBTN, pulangCepatBTN, layoffBTN, tidakCheckoutBTN, terlambatBTN, hadirBTN, tidakHadirBTN;
    TextView historyBTN, countNotifFingerTV, tglBergabungMainTV, yearCR, sisaCutiTV, periodeUpdateSisaCutiTV, dateUpdateSisaCutiTV, countMessage, countNotifTV, notePantau, titlePantau, bagianNameTV, hTime, mTime, sTime, kelebihanJamData, pulangCepatData, layoffData, noCheckoutData, terlambatData, currentDate, mainWeather, feelsLikeTemp, weatherTemp, currentAddress, batasBagDept, bulanData, tahunData, hadirData, tidakHadirData, statusIndicator, descAvailable, descEmtpy, statusUserTV, eventCalender, yearTV, monthTV, nameUserTV, nikTV, departemenTV, bagianTV, jabatanTV;
    ImageView notifFiturLoading, sisaCutiLoading, positionLoadingImg, notificationWarningAlpha, notificationWarningNocheckout, notificationWarningLate, kelebihanJamLoading, pulangCepatLoading, layoffLoading, noCheckoutLoading, terlambatLoading, weatherIcon, bulanLoading, hadirLoading, tidakHadirLoading, avatarUser, imageUserBS;
    SwipeRefreshLayout refreshLayout;
    String selectMonth = "";
    Context mContext;
    Activity mActivity;
    SharedPrefManager sharedPrefManager;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mContext = getContext();
        mActivity = getActivity();

        sharedPrefManager = new SharedPrefManager(mContext);
        refreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);
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
                .into(notificationWarningNocheckout);

        Glide.with(mContext)
                .load(R.drawable.ic_warning_notification_gif)
                .into(notificationWarningAlpha);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                        getDataHadir();
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
                new RackMonthPicker(mContext)
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
                intent.putExtra("status_fitur", "1");
                intent.putExtra("status_finger", "1");
                startActivity(intent);
                startActivity(intent);
            }
        });

        getDataHadir();

        return view;
    }

    private void getDataHadir() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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

}
