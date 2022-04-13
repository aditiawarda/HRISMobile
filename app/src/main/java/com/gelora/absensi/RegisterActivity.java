package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;
import com.shasin.notificationbanner.Banner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class RegisterActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    EditText nikED, passwordED, repasswordED;
    TextView namaTV, showPassword, matchPassword, indicatorMatchPass, toLoginBTN;
    String statusPass = "hide";
    LinearLayout registerBTN, contactServiceBTN, connectBTN, closeBTN;
    BottomSheetLayout bottomSheetCS;
    View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        //RegisterActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);

        rootview = findViewById(android.R.id.content);
        nikED = findViewById(R.id.nikED);
        namaTV = findViewById(R.id.namaTV);
        passwordED = findViewById(R.id.passwordED);
        repasswordED = findViewById(R.id.repasswordED);
        showPassword = findViewById(R.id.show_password_register);
        matchPassword = findViewById(R.id.match_password);
        indicatorMatchPass = findViewById(R.id.indicator_match_pass);
        registerBTN = findViewById(R.id.register_btn);
        contactServiceBTN = findViewById(R.id.contact_service_btn);
        bottomSheetCS = findViewById(R.id.bottom_sheet_cs);
        toLoginBTN = findViewById(R.id.to_login_btn);

        toLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        contactServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactService();
            }
        });

        nikED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nik = nikED.getText().toString();
                getNamaKaryawan(nik);
            }

        });

        passwordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (!passwordED.getText().toString().equals("")||!repasswordED.getText().toString().equals("")) {
                    matchPassword.setVisibility(View.VISIBLE);
                    indicatorMatchPass.setVisibility(View.VISIBLE);
                    if (passwordED.getText().toString().equals(repasswordED.getText().toString())) {
                        matchPassword.setText("Macthing");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_green));
                    } else {
                        matchPassword.setText("No Matching");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_red));
                    }
                } else {
                    indicatorMatchPass.setVisibility(View.GONE);
                    matchPassword.setVisibility(View.GONE);
                }
            }

        });

        repasswordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (!passwordED.getText().toString().equals("")||!repasswordED.getText().toString().equals("")){
                    matchPassword.setVisibility(View.VISIBLE);
                    indicatorMatchPass.setVisibility(View.VISIBLE);
                    if (passwordED.getText().toString().equals(repasswordED.getText().toString())) {
                        matchPassword.setText("Macthing");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_green));
                    } else {
                        matchPassword.setText("No Matching");
                        indicatorMatchPass.setBackground(getResources().getDrawable(R.drawable.shape_ring_red));
                    }
                } else {
                    indicatorMatchPass.setVisibility(View.GONE);
                    matchPassword.setVisibility(View.GONE);
                }
            }

        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View view) {
                Drawable show = getResources().getDrawable(R.drawable.ic_baseline_visibility_on);
                Drawable hide = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                if(statusPass.equals("hide")){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    repasswordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setCompoundDrawablesWithIntrinsicBounds(hide, null, null, null);
                    showPassword.setText("Hide");
                    statusPass = "show";
                } else if(statusPass.equals("show")) {
                    //Jika tidak, maka password akan di sembuyikan
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    repasswordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setCompoundDrawablesWithIntrinsicBounds(show, null, null, null);
                    showPassword.setText("Show");
                    statusPass = "hide";
                }
            }
        });

        repasswordED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                nikED.setError(null);
                passwordED.setError(null);
                repasswordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String nik = nikED.getText().toString();
                String nama = namaTV.getText().toString();
                String password = passwordED.getText().toString();
                String repassword = repasswordED.getText().toString();

                if (nama.equals("Data tidak ditemukan")||nama.equals("Nama Karyawan")){
                    if (TextUtils.isEmpty(nik)){
                        if (TextUtils.isEmpty(password)){
                            if (TextUtils.isEmpty(repassword)){
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan data!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Re-Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(password)){
                            if (TextUtils.isEmpty(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan Password kosong!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lenkapi Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lengkapi Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                if (!password.equals(repassword)){
                                    new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("NIK tidak terdaftar dan Password tidak cocok!")
                                            .setConfirmText("OK")
                                            .show();
                                    fokus = repasswordED;
                                    cancel = true;
                                } else {
                                    new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("NIK tidak terdaftar!")
                                            .setConfirmText("OK")
                                            .show();
                                    fokus = namaTV;
                                    cancel = true;
                                }
                            }
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(password)) {
                        if (TextUtils.isEmpty(repassword)) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password dan Re-Passwword!")
                                    .setConfirmText("OK")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        } else {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password!")
                                    .setConfirmText("OK")
                                    .show();
                            fokus = passwordED;
                            cancel = true;
                        }
                    } else {
                        if (TextUtils.isEmpty(repassword)) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Re-Password!")
                                    .setConfirmText("OK")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        } else {
                            if (!password.equals(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Password tidak cocok!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
                        }
                    }
                }

                if (cancel){
                    fokus.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    registerFunction(nik,password);
                }

                return false;
            }

        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nikED.setError(null);
                passwordED.setError(null);
                repasswordED.setError(null);
                View fokus = null;
                boolean cancel = false;

                /* Mengambil text dari form User dan form Password dengan variable baru bertipe String*/
                String nik = nikED.getText().toString();
                String nama = namaTV.getText().toString();
                String password = passwordED.getText().toString();
                String repassword = repasswordED.getText().toString();

                if (nama.equals("Data tidak ditemukan")||nama.equals("Nama Karyawan")){
                    if (TextUtils.isEmpty(nik)){
                        if (TextUtils.isEmpty(password)){
                            if (TextUtils.isEmpty(repassword)){
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan data!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK dan Re-Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Masukkan NIK!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = nikED;
                                cancel = true;
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(password)){
                            if (TextUtils.isEmpty(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan Password kosong!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lenkapi Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
                        } else {
                            if (TextUtils.isEmpty(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("NIK tidak terdaftar dan lengkapi Password!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            } else {
                                if (!password.equals(repassword)){
                                    new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("NIK tidak terdaftar dan Password tidak cocok!")
                                            .setConfirmText("OK")
                                            .show();
                                    fokus = repasswordED;
                                    cancel = true;
                                } else {
                                    new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("NIK tidak terdaftar!")
                                            .setConfirmText("OK")
                                            .show();
                                    fokus = namaTV;
                                    cancel = true;
                                }
                            }
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(password)) {
                        if (TextUtils.isEmpty(repassword)) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password dan Re-Passwword!")
                                    .setConfirmText("OK")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        } else {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Password!")
                                    .setConfirmText("OK")
                                    .show();
                            fokus = passwordED;
                            cancel = true;
                        }
                    } else {
                        if (TextUtils.isEmpty(repassword)) {
                            new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Lengkapi Re-Password!")
                                    .setConfirmText("OK")
                                    .show();
                            fokus = repasswordED;
                            cancel = true;
                        } else {
                            if (!password.equals(repassword)) {
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Password tidak cocok!")
                                        .setConfirmText("OK")
                                        .show();
                                fokus = repasswordED;
                                cancel = true;
                            }
                        }
                    }
                }

                if (cancel){
                    fokus.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    registerFunction(nik,password);
                }
            }
        });

    }

    private void getNamaKaryawan(String nik){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/nama_karyawan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String nama_karyawan = data.getString("nama_karyawan");

                            if(status.equals("Success")){
                                namaTV.setText(nama_karyawan);
                            } else if (status.equals("Warning")){
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                                new KAlertDialog(RegisterActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Akun sudah teregistrasi!")
                                        .setConfirmText("OK")
                                        .show();
                            } else {
                                namaTV.setText("Data tidak ditemukan");
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
                params.put("NIK", nik);
                return params;
            }
        };

        //requestQueue.add(postRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

    }


    private void registerFunction(String nik,String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/register_akun";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String namaKaryawan = data.getString("nama_karyawan");
                                String nikKaryawan = data.getString("nik_karyawan");

                                Intent intent = new Intent(RegisterActivity.this, NotifyActivity.class);
                                intent.putExtra("nama_karyawan", namaKaryawan);
                                intent.putExtra("nik_karyawan", nikKaryawan);
                                startActivity(intent);
                                finish();

                            } else {

                                new KAlertDialog(RegisterActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Opps")
                                        .setContentText("Not Found!")
                                        .setConfirmText("OK")
                                        .show();

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
                params.put("NIK", nik);
                params.put("password", password);
                return params;
            }
        };

        //requestQueue.add(postRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

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

    private void connectionFailed(){
        Banner.make(rootview, RegisterActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 4000).show();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetCS.isSheetShowing()){
            bottomSheetCS.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}