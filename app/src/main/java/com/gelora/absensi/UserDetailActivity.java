package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends AppCompatActivity {

    LinearLayout lengkapPart, backBTN, backHeader, submitBTN;
    CircleImageView profileImage;
    String jabatan = "", emailData = "", phoneData = "", avatar, avatarPath, bagian, department, tanggalBergabung;
    TextView jabatanTV, namaKaryawanTV, nikTV, bagianTV, departemenTV, bergabungTV;
    EditText emailED, phoneED;
    SharedPrefManager sharedPrefManager;
    KAlertDialog pDialog;
    SwipeRefreshLayout refreshLayout;
    private int i = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        backHeader = findViewById(R.id.back_header);
        profileImage = findViewById(R.id.profile_image);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikTV = findViewById(R.id.nik_tv);
        jabatanTV = findViewById(R.id.jabatan_tv);
        bagianTV = findViewById(R.id.bagian_tv);
        departemenTV = findViewById(R.id.departemen_tv);
        bergabungTV = findViewById(R.id.bergabung_tv);
        emailED = findViewById(R.id.email_tv);
        phoneED = findViewById(R.id.no_hp_tv);
        submitBTN = findViewById(R.id.submit_btn);
        lengkapPart = findViewById(R.id.lengkap_part);

        avatar = getIntent().getExtras().getString("avatar");
        jabatan = getIntent().getExtras().getString("jabatan");
        bagian = getIntent().getExtras().getString("bagian");
        department = getIntent().getExtras().getString("departemen");
        tanggalBergabung = getIntent().getExtras().getString("tanggal_bergabung");

        if(!avatar.equals("null")){
            if(!avatar.equals("default_profile.jpg")){
                avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(profileImage);
            }
        }

        namaKaryawanTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        jabatanTV.setText(jabatan);
        nikTV.setText(sharedPrefManager.getSpNik());
        bagianTV.setText(bagian);
        departemenTV.setText(department);

        if(tanggalBergabung.equals("0000-00-00")){
            bergabungTV.setText("Tidak diketahui");
        } else {
            bergabungTV.setText(tanggalBergabung.substring(8,10)+"/"+tanggalBergabung.substring(5,7)+"/"+tanggalBergabung.substring(0,4));
        }

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        cekEmailHP();
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

        backHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailED.getText().toString().equals("")){
                    if(phoneED.getText().toString().equals("")){
                        new KAlertDialog(UserDetailActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap masukkan email dan no hp!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(UserDetailActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap masukkan email!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } else {
                    if(phoneED.getText().toString().equals("")){
                        new KAlertDialog(UserDetailActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap no hp!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {

                        String emailUser = emailED.getText().toString().trim();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (emailUser.matches(emailPattern) && emailUser.length() > 0) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                            new KAlertDialog(UserDetailActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Simpan data sekarang?")
                                    .setCancelText("TIDAK")
                                    .setConfirmText("   YA   ")
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
                                            pDialog = new KAlertDialog(UserDetailActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (UserDetailActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (UserDetailActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (UserDetailActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (UserDetailActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (UserDetailActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (UserDetailActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    saveEmailHP();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();

                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            new KAlertDialog(UserDetailActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Format email tidak sesuai!")
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
                }

            }
        });

        cekEmailHP();

    }

    private void cekEmailHP() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cek_email_nohp";
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
                            if (status.equals("Available")){
                                String email = data.getString("email");
                                String hp = data.getString("hp");

                                emailData = email;
                                phoneData = hp;

                                emailED.setText(email);
                                phoneED.setText(hp);

                                submitBTN.setVisibility(View.GONE);
                                lengkapPart.setVisibility(View.VISIBLE);

                                emailED.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if((!emailED.equals(emailData) || !phoneED.equals(phoneData))){
                                            submitBTN.setVisibility(View.VISIBLE);
                                            lengkapPart.setVisibility(View.GONE);
                                        } else {
                                            submitBTN.setVisibility(View.GONE);
                                            lengkapPart.setVisibility(View.VISIBLE);
                                        }
                                    }

                                });

                                phoneED.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if((!emailED.equals(emailData) || !phoneED.equals(phoneData))){
                                            submitBTN.setVisibility(View.VISIBLE);
                                            lengkapPart.setVisibility(View.GONE);
                                        } else {
                                            submitBTN.setVisibility(View.GONE);
                                            lengkapPart.setVisibility(View.VISIBLE);
                                        }
                                    }

                                });

                            } else {

                                emailData = "";
                                phoneData = "";

                                emailED.setHint(R.string.hint_email);
                                phoneED.setHint(R.string.hint_phone);

                                submitBTN.setVisibility(View.VISIBLE);
                                lengkapPart.setVisibility(View.GONE);
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

    private void saveEmailHP() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/input_email_nohp";
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
                                emailED.clearFocus();
                                phoneED.clearFocus();

                                pDialog.setTitleText("Tersimpan")
                                        .setContentText("Data berhasil disimpan")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                cekEmailHP();
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                            } else {
                                emailED.clearFocus();
                                phoneED.clearFocus();

                                pDialog.setTitleText("Gagal Tersimpan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                params.put("email", emailED.getText().toString());
                params.put("phone", phoneED.getText().toString());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(UserDetailActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

}