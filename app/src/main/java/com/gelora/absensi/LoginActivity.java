package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;
import com.shasin.notificationbanner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class LoginActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    LinearLayout connectBTN, closeBTN, contactServiceBTN, loginBTN, showPasswordBTN;
    EditText nikED, passwordED;
    SharedPrefManager sharedPrefManager;
    TextView showPassword, registerBTN, testBTN;
    String visibilityPassword = "hide";
    BottomSheetLayout bottomSheet, bottomSheetCS;
    View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefManager = new SharedPrefManager(this);
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

        testBTN = findViewById(R.id.test_btn);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        //LoginActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);

        testBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginTestUser();
            }
        });

        showPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View view) {
                Drawable show = getResources().getDrawable(R.drawable.ic_baseline_visibility_on);
                Drawable hide = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                if(visibilityPassword.equals("hide")){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setBackground(hide);
                    visibilityPassword = "show";
                }else if(visibilityPassword.equals("show")) {
                    //Jika tidak, maka password akan di sembunyikan
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setBackground(show);
                    visibilityPassword = "hide";
                }
            }
        });

        passwordED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (passwordED.getText().toString().equals("")) {
                    new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Masukkan Password!")
                            .setConfirmText("OK")
                            .show();
                } else {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                        String nik = nikED.getText().toString();
                        String password = passwordED.getText().toString();
                        loginFunction(nik,password);
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
                //Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Intent intent = new Intent(LoginActivity.this, DeviceWarningActivity.class);
                startActivity(intent);
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                .setConfirmText("OK")
                                .show();
                        fokus = nikED;
                        cancel = true;
                    } else {
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan NIK!")
                                .setConfirmText("OK")
                                .show();
                        fokus = nikED;
                        cancel = true;
                    }
                } else if (TextUtils.isEmpty(password)){
                    if (TextUtils.isEmpty(nik)){
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan NIK dan Password!")
                                .setConfirmText("OK")
                                .show();
                        fokus = passwordED;
                        cancel = true;
                    } else {
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Masukkan Password!")
                                .setConfirmText("OK")
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
                    loginFunction(nik,password);
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

                        new KAlertDialog(LoginActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Opps")
                                .setContentText(status+"!")
                                .setConfirmText("OK")
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
                connectionFailed();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void connectionFailed(){
        Banner.make(rootview, LoginActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 4000).show();
    }

    private void contactService(){
        bottomSheetCS.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_contact_service, bottomSheetCS, false));
        closeBTN = findViewById(R.id.close_btn);
        connectBTN = findViewById(R.id.connect_btn);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetCS.dismissSheet();
            }
        });
        connectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+6281281898552&text="));
                startActivity(webIntent);
            }
        });
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

                        new KAlertDialog(LoginActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Opps")
                                .setContentText(status+"!")
                                .setConfirmText("OK")
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
                connectionFailed();
            }
        });

        //requestQueue.add(jsonObjectRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            requestQueue.add(jsonObjectRequest);
        }

    }

}