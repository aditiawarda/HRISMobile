package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;
import com.shasin.notificationbanner.Banner;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class LoginActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    LinearLayout registerBTN, connectBTN, closeBTN, contactServiceBTN, loginBTN, showPasswordBTN;
    EditText nikED, passwordED;
    SharedPrefManager sharedPrefManager;
    TextView showPassword, testBTN, icPerson, icPassword;
    String statusCheck = "", deviceID, visibilityPassword = "hide";
    BottomSheetLayout bottomSheet, bottomSheetCS;
    SwipeRefreshLayout refreshLayout;
    ProgressBar loadingProgressBar;
    View rootview;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getStatusCheckDivice();

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        rootview = findViewById(android.R.id.content);
        nikED = findViewById(R.id.nikED);
        passwordED = findViewById(R.id.passwordED);
        showPasswordBTN = findViewById(R.id.show_password_btn);
        showPassword = findViewById(R.id.show_password);
        loginBTN = findViewById(R.id.login_btn);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        bottomSheetCS = findViewById(R.id.bottom_sheet_cs);
        contactServiceBTN = findViewById(R.id.contact_service_btn);
        registerBTN = findViewById(R.id.register_btn);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        icPerson = findViewById(R.id.ic_person);
        icPassword = findViewById(R.id.ic_password);

        testBTN = findViewById(R.id.test_btn);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        loadingProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#A6441F"),android.graphics.PorterDuff.Mode.MULTIPLY);

        deviceID = String.valueOf(Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID)).toUpperCase();

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        nikED.setText("");
                        passwordED.setText("");

                        nikED.clearFocus();
                        passwordED.clearFocus();

                        nikED.setTextColor(Color.parseColor("#FFFFFF"));
                        passwordED.setTextColor(Color.parseColor("#FFFFFF"));

                        nikED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_feel_login));
                        icPerson.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_person));

                        passwordED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_feel));
                        icPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_lock));

                        showPasswordBTN.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_show));

                        visibilityPassword = "hide";
                        showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_off));

                        getStatusCheckDivice();
                    }
                }, 1000);
            }
        });

        testBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginTestUser();
            }
        });

        nikED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                nikED.setTextColor(Color.parseColor("#FFDFB8"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));
                nikED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_feel_login_aktif));
                icPerson.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_person_aktif));
                passwordED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_feel));
                icPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_lock));
                showPasswordBTN.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_show));

                if(visibilityPassword.equals("hide")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_on));
                } else if(visibilityPassword.equals("show")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_off));
                }

            }
        });

        passwordED.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFDFB8"));
                nikED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_feel_login));
                icPerson.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_person));
                passwordED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_feel_aktif));
                icPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_lock_aktif));
                showPasswordBTN.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_show_aktif));

                if(visibilityPassword.equals("hide")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_on_aktif));
                } else if(visibilityPassword.equals("show")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_off_aktif));
                }

            }
        });

        showPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View view) {
                Drawable show = getResources().getDrawable(R.drawable.ic_baseline_visibility_on);
                Drawable show_aktif = getResources().getDrawable(R.drawable.ic_baseline_visibility_on_aktif);
                Drawable hide = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                Drawable hide_aktif = getResources().getDrawable(R.drawable.ic_baseline_visibility_off_aktif);
                if(visibilityPassword.equals("hide")){
                    passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    if(passwordED.hasFocus()){
                        showPassword.setBackground(hide_aktif);
                    } else {
                        showPassword.setBackground(hide);
                    }
                    visibilityPassword = "show";
                }else if(visibilityPassword.equals("show")) {
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    if(passwordED.hasFocus()){
                        showPassword.setBackground(show_aktif);
                    } else {
                        showPassword.setBackground(show);
                    }
                    visibilityPassword = "hide";
                }
            }
        });

        passwordED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                nikED.clearFocus();
                passwordED.clearFocus();

                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));

                nikED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_feel_login));
                icPerson.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_person));

                passwordED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_feel));
                icPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_lock));

                showPasswordBTN.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_show));

                if(visibilityPassword.equals("hide")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_on));
                } else if(visibilityPassword.equals("show")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_off));
                }

                if (passwordED.getText().toString().equals("")) {
                    new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Masukkan Password!")
                            .setConfirmText("    OK    ")
                            .show();
                } else {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                        String nik = nikED.getText().toString();
                        String password = passwordED.getText().toString();
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        if (statusCheck.equals("1")){
                            checkDevice(nik, password, deviceID);
                        } else {
                            loginFunction(nik,password);
                        }
                    }
                }
                return false;
            }
        });

        contactServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactService();
            }
        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nikED.clearFocus();
                passwordED.clearFocus();

                nikED.setTextColor(Color.parseColor("#FFFFFF"));
                passwordED.setTextColor(Color.parseColor("#FFFFFF"));

                nikED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_feel_login));
                icPerson.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_person));

                passwordED.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_feel));
                icPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_lock));

                showPasswordBTN.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.shape_pw_show));

                if(visibilityPassword.equals("hide")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_on));
                } else if(visibilityPassword.equals("show")){
                    showPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_visibility_off));
                }

                nikED.setError(null);
                passwordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String nik = nikED.getText().toString();
                String password = passwordED.getText().toString();

                if (TextUtils.isEmpty(nik)){
                    if (TextUtils.isEmpty(password)) {
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan NIK dan Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = nikED;
                        cancel = true;
                    } else {
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan NIK!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = nikED;
                        cancel = true;
                    }
                } else if (TextUtils.isEmpty(password)){
                    if (TextUtils.isEmpty(nik)){
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan NIK dan Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = nikED;
                        cancel = true;
                    } else {
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan Password!")
                                .setConfirmText("    OK    ")
                                .show();
                        fokus = passwordED;
                        cancel = true;
                    }
                }

                if (cancel){
                    fokus.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    if (statusCheck.equals("1")){
                        checkDevice(nik, password, deviceID);
                    } else {
                        loginFunction(nik,password);
                    }
                }
            }
        });

    }

    private void loginFunction(final String nik, final String password) {
        String postUrl = "https://geloraaksara.co.id/absen-online/api/login_absen";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("nik", nik);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if(status.equals("Success")){
                        JSONObject jsonArray = response.getJSONObject("data_user");
                        String nikUser = jsonArray.getString("NIK");
                        String namaUser = jsonArray.getString("NmKaryawan");
                        String idCab = jsonArray.getString("IdCab");
                        String idHeadDept = jsonArray.getString("IdHeadDept");
                        String idDept = jsonArray.getString("IdDept");
                        String idJabatan = jsonArray.getString("IdJabatan");
                        String statusUser = jsonArray.getString("StatusUser");
                        String statusAktif = jsonArray.getString("status_aktif");
                        String tglBergabung = jsonArray.getString("tanggal_bergabung");
                        String statusKaryawan = jsonArray.getString("status_karyawan");

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, nikUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, namaUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, idCab);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, idHeadDept);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, idDept);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, idJabatan);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_USER, statusUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_AKTIF, statusAktif);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, tglBergabung);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_KARYAWAN, statusKaryawan);
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_HALAMAN, "1");

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        finishAffinity();

                    } else {
                        loadingProgressBar.setVisibility(View.GONE);
                        new KAlertDialog(LoginActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText(status+"!")
                                .setConfirmText("    OK    ")
                                .show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingProgressBar.setVisibility(View.GONE);
                connectionFailed();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void connectionFailed(){
        // Banner.make(rootview, LoginActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(LoginActivity.this)
                .setCustomView(R.layout.layout_custom_cookie)
                .setEnableAutoDismiss(true)
                .setSwipeToDismiss(false)
                .setCookiePosition(Gravity.TOP)
                .show();

    }

    private void contactService(){
        bottomSheetCS.showWithSheetView(LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_contact_service, bottomSheetCS, false));
        closeBTN = findViewById(R.id.close_btn);
        connectBTN = findViewById(R.id.connect_btn);
        getContact();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetCS.isSheetShowing()){
            bottomSheetCS.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    private void loginTestUser() {
        String postUrl = "https://geloraaksara.co.id/absen-online/api/login_absen";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("nik", "0000011");
            postData.put("password", "gapprint");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if(status.equals("Success")){
                        JSONObject jsonArray = response.getJSONObject("data_user");
                        String nikUser = jsonArray.getString("NIK");
                        String namaUser = jsonArray.getString("NmKaryawan");
                        String idCab = jsonArray.getString("IdCab");
                        String idHeadDept = jsonArray.getString("IdHeadDept");
                        String idDept = jsonArray.getString("IdDept");
                        String idJabatan = jsonArray.getString("IdJabatan");
                        String statusUser = jsonArray.getString("StatusUser");
                        String statusAktif = jsonArray.getString("status_aktif");

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, nikUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, namaUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, idCab);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, idHeadDept);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, idDept);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, idJabatan);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_USER, statusUser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_AKTIF, statusAktif);
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_HALAMAN, "1");

                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        loadingProgressBar.setVisibility(View.GONE);
                        new KAlertDialog(LoginActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText(status+"!")
                                .setConfirmText("    OK    ")
                                .show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingProgressBar.setVisibility(View.GONE);
                connectionFailed();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            requestQueue.add(jsonObjectRequest);
        }

    }

    private void checkDevice(String nik, String password, String device_id) {
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

                            if (status.equals("Success")){
                                loginFunction(nik,password);
                            } else if (status.equals("Warning")){
                                loadingProgressBar.setVisibility(View.GONE);
                                new KAlertDialog(LoginActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar!")
                                        .setConfirmText("    OK    ")
                                        .show();
                            } else {
                                loadingProgressBar.setVisibility(View.GONE);
                                String atas_nama = data.getString("atas_nama");
                                String nik = data.getString("NIK");
                                Intent intent = new Intent(LoginActivity.this, DeviceWarningActivity.class);
                                intent.putExtra("nik", nik);
                                intent.putExtra("atas_nama", atas_nama);
                                startActivity(intent);
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
                        loadingProgressBar.setVisibility(View.GONE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", nik);
                params.put("device_id_user", device_id);
                return params;
            }
        };

        requestQueue.add(postRequest);

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

                            closeBTN = findViewById(R.id.close_btn);
                            closeBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomSheetCS.dismissSheet();
                                }
                            });

                            connectBTN = findViewById(R.id.connect_btn);
                            connectBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                                    try {
                                        startActivity(webIntent);
                                    } catch (SecurityException e) {
                                        e.printStackTrace();
                                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
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

    private void getStatusCheckDivice() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/check_perangkat";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if (status.equals("aktif")){
                                statusCheck = "1";
                            } else {
                                statusCheck = "0";
                            }

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

}