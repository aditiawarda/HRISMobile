package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    BubbleToggleView m_item_shop;
    BubbleNavigationLinearView bubbleNavigation;
    Vibrator vibrate;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    String warningPerangkat = "", deviceID = "";
    KAlertDialog pDialog;
    private final int i = -1;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        bubbleNavigation = findViewById(R.id.equal_navigation_bar);
        viewPager = findViewById(R.id.viewPager);
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

        checkLogin();

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
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        Preferences.clearLoggedInUser(HomeActivity.this);
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
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
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

}