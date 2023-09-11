package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterPersonalNotification;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataPersonalNotification;
import com.github.jinatonic.confetti.CommonConfetti;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllMenuActivity extends AppCompatActivity {

    LinearLayout cutiPart, pengaduanPart, cardPart, sdmPart, calendarPart, clearancePart, messengerPart, newsPart, newsPartSub, calendarPartSub, idCardPartSub, pengaduanPartSub;
    LinearLayout actionBar, backBTN, menuAbsensiBTN, menuIzinBTN, menuCutiBTN, menuPengaduanBTN, menuFingerBTN, menuSdmBTN, menuCardBTN, menuSignatureBTN, menuClearanceBTN, menuCalendarBTN, menuMessengerBTN, menuNewsBTN, menuIdCardBTNSub, menuNewsBTNSub, menuCalendarBTNSub, menuPengaduanBTNSub;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_menu);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        cutiPart = findViewById(R.id.cuti_part);
        cardPart = findViewById(R.id.card_part);
        sdmPart = findViewById(R.id.sdm_part);
        newsPart = findViewById(R.id.news_part);
        messengerPart = findViewById(R.id.messenger_part);
        calendarPart = findViewById(R.id.calendar_part);
        clearancePart = findViewById(R.id.clearance_part);
        pengaduanPart = findViewById(R.id.pengaduan_part);
        newsPartSub = findViewById(R.id.news_part_sub);
        calendarPartSub = findViewById(R.id.calendar_part_sub);
        idCardPartSub = findViewById(R.id.id_card_part_sub);
        pengaduanPartSub = findViewById(R.id.pengaduan_part_sub);
        menuAbsensiBTN = findViewById(R.id.menu_absensi_btn);
        menuIzinBTN = findViewById(R.id.menu_izin_btn);
        menuCutiBTN = findViewById(R.id.menu_cuti_btn);
        menuPengaduanBTN = findViewById(R.id.menu_pengaduan_btn);
        menuFingerBTN = findViewById(R.id.menu_finger_btn);
        menuSdmBTN = findViewById(R.id.menu_sdm_btn);
        menuCardBTN = findViewById(R.id.menu_card_btn);
        menuSignatureBTN = findViewById(R.id.menu_signature_btn);
        menuClearanceBTN = findViewById(R.id.menu_clearance_btn);
        menuCalendarBTN = findViewById(R.id.menu_calendar_btn);
        menuMessengerBTN = findViewById(R.id.menu_messenger_btn);
        menuNewsBTN = findViewById(R.id.menu_news_btn);
        menuIdCardBTNSub = findViewById(R.id.menu_id_card_btn_sub);
        menuNewsBTNSub = findViewById(R.id.menu_news_btn_sub);
        menuCalendarBTNSub = findViewById(R.id.menu_calendar_btn_sub);
        menuPengaduanBTNSub = findViewById(R.id.menu_pengaduan_btn_sub);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        roleMenu();
                        //getData();
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

        menuAbsensiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, MapsActivity.class);
                intent.putExtra("from", "all_menu");
                startActivity(intent);
            }
        });

        menuIzinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefManager.getSpIdJabatan().equals("31")){
                    new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
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
                    Intent intent = new Intent(AllMenuActivity.this, FormPermohonanIzinActivity.class);
                    startActivity(intent);
                }
            }
        });

        menuCutiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, FormPermohonanCutiActivity.class);
                startActivity(intent);
            }
        });

        menuPengaduanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
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

        menuPengaduanBTNSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
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
                    new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
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
                    Intent intent = new Intent(AllMenuActivity.this, FormFingerscanActivity.class);
                    startActivity(intent);
                }
            }
        });

        menuCardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, DigitalCardActivity.class);
                intent.putExtra("nama", sharedPrefManager.getSpNama());
                intent.putExtra("nik", sharedPrefManager.getSpNik());
                startActivity(intent);
            }
        });

        menuIdCardBTNSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, DigitalCardActivity.class);
                intent.putExtra("nama", sharedPrefManager.getSpNama());
                intent.putExtra("nik", sharedPrefManager.getSpNik());
                startActivity(intent);
            }
        });

        menuSdmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, HumanResourceActivity.class);
                startActivity(intent);
            }
        });

        menuSignatureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, DigitalSignatureActivity.class);
                intent.putExtra("kode", "home");
                startActivity(intent);
            }
        });

        menuCalendarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, CalendarPageActivity.class);
                startActivity(intent);
            }
        });

        menuCalendarBTNSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, CalendarPageActivity.class);
                startActivity(intent);
            }
        });

        menuClearanceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllMenuActivity.this, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        roleMenu();

    }

    private void roleMenu() {
        if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")||sharedPrefManager.getSpStatusKaryawan().equals("Kontrak")){
            if(sharedPrefManager.getSpStatusKaryawan().equals("Tetap")){
                cutiPart.setVisibility(View.VISIBLE);
                pengaduanPart.setVisibility(View.GONE);
                clearancePart.setVisibility(View.VISIBLE);
                calendarPart.setVisibility(View.GONE);
                messengerPart.setVisibility(View.VISIBLE);
                newsPart.setVisibility(View.GONE);
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
            messengerPart.setVisibility(View.VISIBLE);
            newsPart.setVisibility(View.GONE);

            if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpNik().equals("1309131210")){
                cardPart.setVisibility(View.GONE);
                sdmPart.setVisibility(View.VISIBLE);

                newsPartSub.setVisibility(View.VISIBLE);
                calendarPartSub.setVisibility(View.VISIBLE);
                idCardPartSub.setVisibility(View.VISIBLE);
                pengaduanPartSub.setVisibility(View.VISIBLE);
            } else {
                cardPart.setVisibility(View.VISIBLE);
                sdmPart.setVisibility(View.GONE);

                newsPartSub.setVisibility(View.VISIBLE);
                calendarPartSub.setVisibility(View.VISIBLE);
                idCardPartSub.setVisibility(View.GONE);
                pengaduanPartSub.setVisibility(View.VISIBLE);
            }

        } else {
            cardPart.setVisibility(View.VISIBLE);
            sdmPart.setVisibility(View.GONE);

            cutiPart.setVisibility(View.GONE);
            pengaduanPart.setVisibility(View.VISIBLE);
            if(sharedPrefManager.getSpIdCor().equals("1")){ //PKL dan Freelance
                if(sharedPrefManager.getSpIdJabatan().equals("29")||sharedPrefManager.getSpIdJabatan().equals("31")){
                    clearancePart.setVisibility(View.GONE);
                    calendarPart.setVisibility(View.VISIBLE);

                    newsPartSub.setVisibility(View.VISIBLE);
                    calendarPartSub.setVisibility(View.GONE);
                    idCardPartSub.setVisibility(View.GONE);
                    pengaduanPartSub.setVisibility(View.GONE);
                } else {
                    clearancePart.setVisibility(View.VISIBLE);
                    calendarPart.setVisibility(View.GONE);

                    newsPartSub.setVisibility(View.VISIBLE);
                    calendarPartSub.setVisibility(View.VISIBLE);
                    idCardPartSub.setVisibility(View.GONE);
                    pengaduanPartSub.setVisibility(View.GONE);
                }
                messengerPart.setVisibility(View.VISIBLE);
                newsPart.setVisibility(View.GONE);

            } else if(sharedPrefManager.getSpIdCor().equals("3")){ //Erlass
                clearancePart.setVisibility(View.GONE);
                calendarPart.setVisibility(View.VISIBLE);
                messengerPart.setVisibility(View.GONE);
                newsPart.setVisibility(View.VISIBLE);

                newsPartSub.setVisibility(View.GONE);
                calendarPartSub.setVisibility(View.GONE);
                idCardPartSub.setVisibility(View.GONE);
                pengaduanPartSub.setVisibility(View.GONE);
            }
        }

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

                                menuNewsBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(news_part.equals("1")){
                                            Intent intent = new Intent(AllMenuActivity.this, NewsActivity.class);
                                            intent.putExtra("api_url", base_news_api);
                                            intent.putExtra("defaut_news_category", defaut_news_category);
                                            startActivity(intent);
                                        } else {
                                            new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Maaf, menu sedang dalam tahap maintenance")
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

                                menuNewsBTNSub.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(news_part.equals("1")){
                                            Intent intent = new Intent(AllMenuActivity.this, NewsActivity.class);
                                            intent.putExtra("api_url", base_news_api);
                                            intent.putExtra("defaut_news_category", defaut_news_category);
                                            startActivity(intent);
                                        } else {
                                            new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Maaf, menu sedang dalam tahap maintenance")
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

                                menuMessengerBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(chat_room.equals("1")){
                                            Intent intent = new Intent(AllMenuActivity.this, ChatSplashScreenActivity.class);
                                            startActivity(intent);
                                        } else {
                                            new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian")
                                                    .setContentText("Maaf, menu sedang dalam tahap maintenance")
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
                                    new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
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
                    new KAlertDialog(AllMenuActivity.this, KAlertDialog.ERROR_TYPE)
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
                                    new KAlertDialog(AllMenuActivity.this, KAlertDialog.WARNING_TYPE)
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
                    new KAlertDialog(AllMenuActivity.this, KAlertDialog.ERROR_TYPE)
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

    private void connectionFailed(){
        CookieBar.build(AllMenuActivity.this)
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

}