package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class EmailShowActivity extends AppCompatActivity {

    LinearLayout icSending, backBTN, notAvailableEmail, availableEmail, submitBTN;
    TextView namaTV, nikTV, jabatanTV, bagianTV, emailTV;
    CircleImageView profileImg;
    String userNIK, userEmail;
    EditText mailED;
    ProgressBar loadingSending;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_show);

        backBTN = findViewById(R.id.back_btn);
        namaTV = findViewById(R.id.nama_tv);
        nikTV = findViewById(R.id.nik_tv);
        jabatanTV = findViewById(R.id.jabatan_tv);
        bagianTV = findViewById(R.id.detail_tv);
        profileImg = findViewById(R.id.profile_img);
        emailTV = findViewById(R.id.email_tv);
        notAvailableEmail = findViewById(R.id.not_available_email);
        availableEmail = findViewById(R.id.available_email);
        submitBTN = findViewById(R.id.submit_btn);
        mailED = findViewById(R.id.mail_ed);
        icSending = findViewById(R.id.ic_sending);
        loadingSending = findViewById(R.id.loading_sending);

        userNIK = getIntent().getExtras().getString("nik");

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingSending.setVisibility(View.VISIBLE);
                icSending.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        String emailUser = "", mode = "";
                        if (userEmail.equals("")||userEmail.equals("null")||userEmail==null){
                            emailUser = mailED.getText().toString();
                            mode = "not_available";
                        } else {
                            emailUser = userEmail;
                            mode = "available";
                        }

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (emailUser.matches(emailPattern) && emailUser.length() > 0) {
                            sendOTPtoEmail(userNIK, emailUser);
                        } else {
                            if(mode.equals("not_available") && emailUser.equals("")){
                                loadingSending.setVisibility(View.GONE);
                                icSending.setVisibility(View.VISIBLE);
                                new KAlertDialog(EmailShowActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText(("Harap masukkan email"))
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                loadingSending.setVisibility(View.GONE);
                                icSending.setVisibility(View.VISIBLE);
                                new KAlertDialog(EmailShowActivity.this, KAlertDialog.ERROR_TYPE)
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
                }, 800);
            }
        });

        getDataUser(userNIK);

    }

    private void getDataUser(String nikUser) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://hrisgelora.co.id/api/get_karyawan?nik="+nikUser;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            JSONObject data = response.getJSONObject("data");

                            if (status.equals("Success")){
                                String NIK = data.getString("NIK");
                                String nama = data.getString("nama");
                                String jabatan = data.getString("jabatan");
                                String bagian = data.getString("bagian");
                                String departemen = data.getString("departemen");
                                String avatar = data.getString("avatar");
                                String email = data.getString("email");

                                namaTV.setText(nama);
                                nikTV.setText(NIK);
                                jabatanTV.setText(jabatan);
                                bagianTV.setText(bagian+" | "+departemen);

                                if(!avatar.equals("null")){
                                    Picasso.get().load(avatar).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .resize(100, 100)
                                            .into(profileImg);
                                }

                                if (email.equals("")||email.equals("null")||email==null){
                                    availableEmail.setVisibility(View.GONE);
                                    notAvailableEmail.setVisibility(View.VISIBLE);
                                } else {
                                    availableEmail.setVisibility(View.VISIBLE);
                                    notAvailableEmail.setVisibility(View.GONE);
                                    emailTV.setText(email);
                                }

                                userEmail = email;

                            } else {
                                new KAlertDialog(EmailShowActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Data tidak ditemukan")
                                        .setConfirmText("    OK    ")
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
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void sendOTPtoEmail(String nik, String email) {
        int MY_SOCKET_TIMEOUT_MS = 180000;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/reset_password";
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
                                JSONObject data_otp = data.getJSONObject("data");
                                String otp = data_otp.getString("otp");
                                String expired_at = data_otp.getString("expired_at");
                                Intent intent = new Intent(EmailShowActivity.this, ForgotPasswordVerificationActivity.class);
                                intent.putExtra("nik", nik);
                                intent.putExtra("email", email);
                                intent.putExtra("otp", otp);
                                intent.putExtra("expired", expired_at);
                                startActivity(intent);
                                handler.postDelayed(new Runnable() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void run() {
                                        loadingSending.setVisibility(View.GONE);
                                        icSending.setVisibility(View.VISIBLE);
                                    }
                                }, 500);
                                finish();
                            } else {
                                loadingSending.setVisibility(View.GONE);
                                icSending.setVisibility(View.VISIBLE);
                                new KAlertDialog(EmailShowActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Email gagal terkirim")
                                        .setConfirmText("    OK    ")
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingSending.setVisibility(View.GONE);
                            icSending.setVisibility(View.VISIBLE);
                            new KAlertDialog(EmailShowActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Terjadi kesalahan")
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
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        loadingSending.setVisibility(View.GONE);
                        icSending.setVisibility(View.VISIBLE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("email", email);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(EmailShowActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}