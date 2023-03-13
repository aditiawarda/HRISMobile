package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")) {
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

    private void deviceIdFunction() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                                if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")){
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

                                String shortName = sharedPrefManager.getSpNama()+" ";
                                if(shortName.contains(" ")){
                                    shortName = shortName.substring(0, shortName.indexOf(" "));
                                    System.out.println(shortName);
                                }

                                if (count.equals("0") && count_finger.equals("0")){
                                    int alpaNumb = Integer.parseInt(alpa);
                                    int lateNumb = Integer.parseInt(terlambat);
                                    int noCheckoutNumb = Integer.parseInt(tidak_checkout);
                                    if (alpaNumb > 0 || lateNumb > 0 || noCheckoutNumb > 0){
                                        infoMark.setVisibility(View.VISIBLE);

                                        if(notif.equals("0")){
                                            notif = "1";
                                            Notify.build(getApplicationContext())
                                                    .setTitle("HRIS Mobile Gelora")
                                                    .setContent("Halo "+shortName+", terdapat notifikasi yang belum anda lihat")
                                                    .setSmallIcon(R.drawable.ic_skylight_notification)
                                                    .setColor(R.color.colorPrimary)
                                                    .largeCircularIcon()
                                                    .enableVibration(true)
                                                    .show();

                                            // Vibrate for 500 milliseconds
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //deprecated in API 26
                                                vibrate.vibrate(500);
                                            }
                                        }

                                    } else {
                                        notif = "0";
                                        infoMark.setVisibility(View.GONE);
                                    }
                                } else if (!count.equals("0") && count_finger.equals("0")) {
                                    infoMark.setVisibility(View.VISIBLE);

                                    if(notif.equals("0")){
                                        notif = "1";
                                        Notify.build(getApplicationContext())
                                                .setTitle("HRIS Mobile Gelora")
                                                .setContent("Halo "+shortName+", terdapat notifikasi yang belum anda lihat")
                                                .setSmallIcon(R.drawable.ic_skylight_notification)
                                                .setColor(R.color.colorPrimary)
                                                .largeCircularIcon()
                                                .enableVibration(true)
                                                .show();

                                        // Vibrate for 500 milliseconds
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                        } else {
                                            //deprecated in API 26
                                            vibrate.vibrate(500);
                                        }
                                    }

                                } else if (count.equals("0") && !count_finger.equals("0")) {
                                    infoMark.setVisibility(View.VISIBLE);

                                    if(notif.equals("0")){
                                        notif = "1";
                                        Notify.build(getApplicationContext())
                                                .setTitle("HRIS Mobile Gelora")
                                                .setContent("Halo "+shortName+", terdapat notifikasi yang belum anda lihat")
                                                .setSmallIcon(R.drawable.ic_skylight_notification)
                                                .setColor(R.color.colorPrimary)
                                                .largeCircularIcon()
                                                .enableVibration(true)
                                                .show();

                                        // Vibrate for 500 milliseconds
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                        } else {
                                            //deprecated in API 26
                                            vibrate.vibrate(500);
                                        }
                                    }

                                } else if (!count.equals("0") && !count_finger.equals("0")) {
                                    infoMark.setVisibility(View.VISIBLE);

                                    if(notif.equals("0")){
                                        notif = "1";
                                        Notify.build(getApplicationContext())
                                                .setTitle("HRIS Mobile Gelora")
                                                .setContent("Halo "+shortName+", terdapat notifikasi yang belum anda lihat")
                                                .setSmallIcon(R.drawable.ic_skylight_notification)
                                                .setColor(R.color.colorPrimary)
                                                .largeCircularIcon()
                                                .enableVibration(true)
                                                .show();

                                        // Vibrate for 500 milliseconds
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                        } else {
                                            //deprecated in API 26
                                            vibrate.vibrate(500);
                                        }
                                    }

                                }

                            } else {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

                            String shortName = sharedPrefManager.getSpNama()+" ";
                            if(shortName.contains(" ")){
                                shortName = shortName.substring(0, shortName.indexOf(" "));
                                System.out.println(shortName);
                            }

                            if (status.equals("Success")){
                                String data_personal = data.getString("data_personal");
                                String data_kontak = data.getString("data_kontak");
                                if(data_personal.equals("tidak lengkap")){
                                    if(data_kontak.equals("tidak tersedia")){
                                        profileMark.setVisibility(View.VISIBLE);
                                        if(profile.equals("0")){
                                            profile = "1";
                                            Intent intent = new Intent(HomeActivity.this, InfoPersonalActivity.class);
                                            Notify.build(getApplicationContext())
                                                    .setTitle("HRIS Mobile Gelora")
                                                    .setContent("Halo "+shortName+", harap lengkapi data personal dan kontak darurat anda")
                                                    .setSmallIcon(R.drawable.ic_skylight_notification)
                                                    .setColor(R.color.colorPrimary)
                                                    .largeCircularIcon()
                                                    .enableVibration(true)
                                                    .setAction(intent)
                                                    .show();

                                            // Vibrate for 500 milliseconds
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //deprecated in API 26
                                                vibrate.vibrate(500);
                                            }
                                        }

                                    } else {
                                        profileMark.setVisibility(View.VISIBLE);

                                        if(profile.equals("0")){
                                            profile = "1";
                                            Intent intent = new Intent(HomeActivity.this, InfoPersonalActivity.class);
                                            Notify.build(getApplicationContext())
                                                    .setTitle("HRIS Mobile Gelora")
                                                    .setContent("Halo "+shortName+", harap lengkapi data personal anda")
                                                    .setSmallIcon(R.drawable.ic_skylight_notification)
                                                    .setColor(R.color.colorPrimary)
                                                    .largeCircularIcon()
                                                    .enableVibration(true)
                                                    .setAction(intent)
                                                    .show();

                                            // Vibrate for 500 milliseconds
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //deprecated in API 26
                                                vibrate.vibrate(500);
                                            }
                                        }
                                    }
                                } else {
                                    if(data_kontak.equals("tidak tersedia")){
                                        profileMark.setVisibility(View.VISIBLE);

                                        if(profile.equals("0")) {
                                            profile = "1";
                                            Intent intent = new Intent(HomeActivity.this, InfoKontakDaruratActivity.class);
                                            Notify.build(getApplicationContext())
                                                    .setTitle("HRIS Mobile Gelora")
                                                    .setContent("Halo "+shortName+", harap lengkapi data kontak darurat anda")
                                                    .setSmallIcon(R.drawable.ic_skylight_notification)
                                                    .setColor(R.color.colorPrimary)
                                                    .largeCircularIcon()
                                                    .enableVibration(true)
                                                    .setAction(intent)
                                                    .show();

                                            // Vibrate for 500 milliseconds
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //deprecated in API 26
                                                vibrate.vibrate(500);
                                            }
                                        }
                                    } else {
                                        profileMark.setVisibility(View.GONE);
                                    }
                                }
                            } else {
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
            deviceIdFunction();
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
                if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")) {
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

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

}