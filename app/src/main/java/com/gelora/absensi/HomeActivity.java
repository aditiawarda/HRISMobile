package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.isradeleon.notify.Notify;
import com.bumptech.glide.Glide;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.gelora.absensi.adapter.ViewPagerAdapter;
import com.gelora.absensi.fragment.FragmentHome;
import com.gelora.absensi.fragment.FragmentInfo;
import com.gelora.absensi.fragment.FragmentProfile;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.Preferences;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    BubbleToggleView mItemHome, mItemInfo, mItemProfile;
    BubbleNavigationLinearView bubbleNavigation, bubbleNavigationNonGap;
    ImageView infoMark, profileMark;
    Vibrator vibrate;
    LinearLayout shapeBG;
    RequestQueue requestQueue;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    String profile = "0", notif = "0", warningPerangkat = "", deviceID = "", beforeLayout = "0", nowLayout = "0", temp = "0";
    KAlertDialog pDialog;
    private final int i = -1;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);

        if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")) {
            setContentView(R.layout.activity_home_non_gap);

            bubbleNavigationNonGap = findViewById(R.id.equal_navigation_bar_non_gap);
            viewPager = findViewById(R.id.viewPager);
            shapeBG = findViewById(R.id.shape_bg);
            vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new FragmentInfo());
            viewPagerAdapter.addFragment(new FragmentProfile());

            deviceID = String.valueOf(Settings.Secure.getString(HomeActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID)).toUpperCase();

            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }
                @Override
                public void onPageSelected(int i) {
                    bubbleNavigationNonGap.setCurrentActiveItem(i);
                }
                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });

            bubbleNavigationNonGap.setNavigationChangeListener(new BubbleNavigationChangeListener() {
                @Override
                public void onNavigationChanged(View view, int position) {
                    viewPager.setCurrentItem(position, true);
                }
            });

            shapeBG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            checkLogin();

        } else {
            setContentView(R.layout.activity_home);

            bubbleNavigation = findViewById(R.id.equal_navigation_bar);
            viewPager = findViewById(R.id.viewPager);
            shapeBG = findViewById(R.id.shape_bg);
            infoMark = findViewById(R.id.info_mark);
            profileMark = findViewById(R.id.profile_mark);
            vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new FragmentHome());
            viewPagerAdapter.addFragment(new FragmentInfo());
            viewPagerAdapter.addFragment(new FragmentProfile());

            deviceID = String.valueOf(Settings.Secure.getString(HomeActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID)).toUpperCase();

            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }
                @Override
                public void onPageSelected(int i) {
                    bubbleNavigation.setCurrentActiveItem(i);
                    beforeLayout = temp;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nowLayout = String.valueOf(i);
                            temp = nowLayout;
                        }
                    }, 50);
                }
                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });

            bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
                @Override
                public void onNavigationChanged(View view, int position) {
                    viewPager.setCurrentItem(position, true);
                }
            });

            shapeBG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            Glide.with(getApplicationContext())
                    .load(R.drawable.mark_notif_info)
                    .into(infoMark);

            Glide.with(getApplicationContext())
                    .load(R.drawable.mark_notif_info)
                    .into(profileMark);

            checkLogin();

        }

    }

    private void activeCheck() {
        final String url = "https://geloraaksara.co.id/absen-online/api/check_active_user";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Tidak Aktif")){
                                warningPerangkat = "aktif";

                                if(pDialog==null){
                                    pDialog = new KAlertDialog(HomeActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("AKUN ANDA SUDAH TIDAK AKTIF, HARAP HUBUNGI HRD JIKA TERJADI KESALAHAN!")
                                            .setConfirmText("KELUAR");
                                    pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            logoutFunction();
                                        }
                                    });
                                    pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            dialog.dismiss();
                                            logoutFunction();
                                        }
                                    });
                                    pDialog.show();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        vibrate.vibrate(500);
                                    }
                                }

                            } else {
                                deviceIdFunction();
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
                params.put("device_id_user", deviceID);
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private void deviceIdFunction() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_device_id";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String message = data.getString("message");

                            if (message.equals("Device ID tidak sesuai")){
                                warningPerangkat = "aktif";

                                if(pDialog==null){
                                    pDialog = new KAlertDialog(HomeActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("PERANGKAT ANDA TELAH DIGANTI, HARAP GUNAKAN PERANGKAT YANG TERAKHIR DIDAFTARKAN!")
                                            .setConfirmText("KELUAR");
                                    pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            logoutFunction();
                                        }
                                    });
                                    pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            dialog.dismiss();
                                            logoutFunction();
                                        }
                                    });
                                    pDialog.show();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        vibrate.vibrate(500);
                                    }

                                }

                            } else {
                                if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")){
                                    // Nothing else
                                } else {
                                    checkNotification();
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
                params.put("device_id_user", deviceID);
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private void logoutFunction(){
        Preferences.setLoggedInStatus(HomeActivity.this,false);
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
        sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_KARYAWAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_ULTAH, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_PENGUMUMAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_JOIN_REMAINDER, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NOTIF_MESSENGER, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_YET_BEFORE_MESSENGER, "");
        Preferences.clearLoggedInUser(HomeActivity.this);
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
    }

    private void checkNotification() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                                String alpa = data.getString("alpa");
                                String terlambat = data.getString("terlambat");
                                String tidak_checkout = data.getString("tidak_checkout");
                                String status_notif = data.getString("status_notif");

                                if (count.equals("0") && count_finger.equals("0")){
                                    int alpaNumb = Integer.parseInt(alpa);
                                    int lateNumb = Integer.parseInt(terlambat);
                                    int noCheckoutNumb = Integer.parseInt(tidak_checkout);
                                    if (alpaNumb > 0 || lateNumb > 0 || noCheckoutNumb > 0){
                                        infoMark.setVisibility(View.VISIBLE);
                                        if(notif.equals("0")){
                                            if(status_notif.equals("1")){
                                                notif = "1";
                                                if(alpaNumb > 0){
                                                    if(lateNumb > 0){
                                                        if(noCheckoutNumb > 0){
                                                            warningNotifNoCheckout();
                                                        } else {
                                                            warningNotifLate();
                                                        }
                                                    } else {
                                                        if(noCheckoutNumb > 0){
                                                            warningNotifNoCheckout();
                                                        } else {
                                                            warningNotifAlpa();
                                                        }
                                                    }
                                                } else {
                                                    if(lateNumb > 0){
                                                        if(noCheckoutNumb > 0){
                                                            warningNotifNoCheckout();
                                                        } else {
                                                            warningNotifLate();
                                                        }
                                                    } else {
                                                        if(noCheckoutNumb > 0){
                                                            warningNotifNoCheckout();
                                                        }
                                                    }
                                                }
                                            } else {
                                                notif = "0";
                                            }
                                        } else {
                                            notif = "0";
                                        }
                                    } else {
                                        notif = "0";
                                        infoMark.setVisibility(View.GONE);
                                    }
                                } else if (!count.equals("0") && count_finger.equals("0")) {
                                    infoMark.setVisibility(View.VISIBLE);
                                    if(notif.equals("0")){
                                        if(status_notif.equals("1")){
                                            notif = "1";
                                            warningNotifIzin();
                                        } else {
                                            notif = "0";
                                        }
                                    } else {
                                        notif = "0";
                                    }
                                } else if (count.equals("0") && !count_finger.equals("0")) {
                                    infoMark.setVisibility(View.VISIBLE);
                                    if(notif.equals("0")){
                                        if(status_notif.equals("1")){
                                            notif = "1";
                                            warningNotifFinger();
                                        } else {
                                            notif = "0";
                                        }
                                    } else {
                                        notif = "0";
                                    }
                                } else if (!count.equals("0") && !count_finger.equals("0")) {
                                    infoMark.setVisibility(View.VISIBLE);
                                    if(notif.equals("0")){
                                        if(status_notif.equals("1")){
                                            notif = "1";
                                            warningNotifIzin();
                                        } else {
                                            notif = "0";
                                        }
                                    } else {
                                        notif = "0";
                                    }
                                }
                            } else {
                                notif = "0";
                                infoMark.setVisibility(View.GONE);
                            }

                            checkDataUser();

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

    private void checkDataUser() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cek_kelengkapan_data_info_user";
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
                                String data_personal = data.getString("data_personal");
                                String data_kontak = data.getString("data_kontak");
                                String notif_personal = data.getString("notif_personal");
                                String notif_kontak = data.getString("notif_kontak");
                                if(data_personal.equals("tidak lengkap")){
                                    if(data_kontak.equals("tidak tersedia")){
                                        profileMark.setVisibility(View.VISIBLE);
                                        if(profile.equals("0")){
                                            if(notif_personal.equals("1")){
                                                profile = "1";
                                                warningNotifInfoPersonal();
                                            } else {
                                                profile = "0";
                                            }
                                        } else {
                                            profile = "0";
                                        }
                                    } else {
                                        profileMark.setVisibility(View.VISIBLE);
                                        if(profile.equals("0")){
                                            if(notif_personal.equals("1")){
                                                profile = "1";
                                                warningNotifInfoPersonal();
                                            } else {
                                                profile = "0";
                                            }
                                        } else {
                                            profile = "0";
                                        }
                                    }
                                } else {
                                    if(data_kontak.equals("tidak tersedia")){
                                        profileMark.setVisibility(View.VISIBLE);
                                        if(profile.equals("0")) {
                                            if(notif_kontak.equals("1")){
                                                profile = "1";
                                                warningNotifKontakDarurat();
                                            } else {
                                                profile = "0";
                                            }
                                        } else {
                                            profile = "0";
                                        }
                                    } else {
                                        profile = "0";
                                        profileMark.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                profile = "0";
                                profileMark.setVisibility(View.GONE);
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

    private void connectionFailed(){
        CookieBar.build(this)
            .setTitle("Perhatian")
            .setMessage("Koneksi anda terputus!")
            .setTitleColor(R.color.colorPrimaryDark)
            .setMessageColor(R.color.colorPrimaryDark)
            .setBackgroundColor(R.color.warningBottom)
            .setIcon(R.drawable.warning_connection_mini)
            .setCookiePosition(CookieBar.BOTTOM)
            .show();
    }

    @SuppressLint("SetTextI18n")
    private void checkLogin() {
        if(!sharedPrefManager.getSpSudahLogin()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            activeCheck();
        }
    }

    @Override
    public void onBackPressed() {
        if (warningPerangkat.equals("aktif")){
            logoutFunction();
        } else {
            if(nowLayout.equals("0")){
                super.onBackPressed();
            } else {
                if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")) {
                    viewPager.setCurrentItem(Integer.parseInt(beforeLayout), true);
                } else {
                    if(nowLayout.equals("2") && beforeLayout.equals("1")){
                        temp = "0";
                        viewPager.setCurrentItem(Integer.parseInt(beforeLayout), true);
                    } else {
                        viewPager.setCurrentItem(Integer.parseInt(beforeLayout), true);
                    }
                }
            }
        }
    }

    private void warningNotifNoCheckout() {
        String shortName;
        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_notif_no_checkout";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", terdapat warning bahwa anda tidak melakukan absensi checkout pada tanggal sebelumnya, cek untuk info lebih lanjut"))
                .setContentText("Halo "+shortName+", terdapat warning bahwa anda tidak melakukan absensi checkout pada tanggal sebelumnya, cek untuk info lebih lanjut");

        Intent notificationIntent = new Intent(this, DetailTidakCheckoutActivity.class);
        notificationIntent.putExtra("bulan", getBulanTahun());
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void warningNotifLate() {
        String shortName;
        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_notif_late";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", terdapat data keterlambatan absensi, harap segera gunakan form keterangan tidak absen atau form fingerscan"))
                .setContentText("Halo "+shortName+", terdapat data keterlambatan absensi, harap segera gunakan form keterangan tidak absen atau form fingerscan");

        Intent notificationIntent = new Intent(this, DetailTerlambatActivity.class);
        notificationIntent.putExtra("bulan", getBulanTahun());
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void warningNotifAlpa() {
        String shortName;
        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_notif_alpa";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", terdapat data tanpa keterangan atau alpha, harap segera gunakan form keterangan tidak absen atau form fingerscan jika pada tanggal tersebut anda masuk kerja atau form izin jika pada tanggal tersebut anda tidak masuk kerja"))
                .setContentText("Halo "+shortName+", terdapat data tanpa keterangan atau alpha, harap segera gunakan form keterangan tidak absen atau form fingerscan jika pada tanggal tersebut anda masuk kerja atau form izin jika pada tanggal tersebut anda tidak masuk kerja");

        Intent notificationIntent = new Intent(this, DetailTidakHadirActivity.class);
        notificationIntent.putExtra("bulan", getBulanTahun());
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void warningNotifIzin() {
        String shortName;
        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_notif_izin";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", terdapat notifikasi permohonan izin/cuti yang belum anda lihat"))
                .setContentText("Halo "+shortName+", terdapat notifikasi permohonan izin/cuti yang belum anda lihat");

        Intent notificationIntent = new Intent(this, ListNotifikasiActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void warningNotifFinger() {
        String shortName;
        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_notif_finger";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", terdapat notifikasi permohonan fingerscan atau form keterangan tidak absen yang belum anda lihat"))
                .setContentText("Halo "+shortName+", terdapat notifikasi permohonan fingerscan atau form keterangan tidak absen yang belum anda lihat");

        Intent notificationIntent = new Intent(this, ListNotifikasiActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void warningNotifKontakDarurat() {
        String shortName;
        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_kontak_darurat";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", harap lengkapi data kontak darurat anda"))
                .setContentText("Halo "+shortName+", harap lengkapi data kontak darurat anda");

        Intent notificationIntent = new Intent(this, InfoKontakDaruratActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private void warningNotifInfoPersonal() {
        String shortName;

        // if(shortName.contains(" ")){
        //    shortName = shortName.substring(0, shortName.indexOf(" "));
        //    System.out.println(shortName);
        // }

        String[] shortNameArray = sharedPrefManager.getSpNama().split(" ");

        if(shortNameArray.length>1){
            if(shortNameArray[0].length()<3){
                shortName = shortNameArray[1];
                System.out.println(shortName);
            } else {
                shortName = shortNameArray[0];
                System.out.println(shortName);
            }
        } else {
            shortName = shortNameArray[0];
            System.out.println(shortName);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "warning_info_personal";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_skylight_notification)
                .setColor(Color.parseColor("#A6441F"))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("HRIS Mobile Gelora")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Halo "+shortName+", harap lengkapi data personal anda"))
                .setContentText("Halo "+shortName+", harap lengkapi data personal anda");

        Intent notificationIntent = new Intent(this, InfoPersonalActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            @SuppressLint("UnspecifiedImmutableFlag")
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestQueue = Volley.newRequestQueue(this);
        checkLogin();
    }

}