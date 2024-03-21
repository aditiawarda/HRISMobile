package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormInfoPelatihanActivity extends AppCompatActivity {

    LinearLayout closeBTN, okBTN, hapusBTN, backBTN, successPart, formPart, backSuccess, submitBTN, tahunBTN;
    TextView titlePage, tahunPilihTV;
    EditText namaPelatihanED, lembagaPelatihanED;
    NumberPicker yearPicker;
    ImageView successGif;
    String tahunPilih = "", tipeForm = "", idData = "";
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    KAlertDialog pDialog;
    private int i = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_pelatihan);

        sharedPrefManager = new SharedPrefManager(this);
        titlePage = findViewById(R.id.title_page_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        namaPelatihanED = findViewById(R.id.nama_pelatihan_ed);
        lembagaPelatihanED = findViewById(R.id.lembaga_pelatihan_ed);
        successPart = findViewById(R.id.success_submit);
        formPart = findViewById(R.id.form_part);
        backSuccess = findViewById(R.id.back_success_btn);
        tahunBTN = findViewById(R.id.tahun_btn);
        tahunPilihTV = findViewById(R.id.tahun_pilih_tv);
        submitBTN = findViewById(R.id.submit_btn);
        successGif = findViewById(R.id.success_gif);
        hapusBTN = findViewById(R.id.hapus_btn);

        namaPelatihanED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        lembagaPelatihanED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        tipeForm = getIntent().getExtras().getString("tipe");

        if(tipeForm.equals("edit")){
            idData = getIntent().getExtras().getString("id_pelatihan");
            titlePage.setText("EDIT DATA PELATIHAN");
            hapusBTN.setVisibility(View.VISIBLE);

            hapusBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new KAlertDialog(FormInfoPelatihanActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Yakin untuk menghapus data pelatihan?")
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
                                    pDialog = new KAlertDialog(FormInfoPelatihanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            deletePelatihan(idData);
                                        }
                                    }.start();

                                }
                            })
                            .show();
                }
            });

            getData();

        } else {
            titlePage.setText("FORM DATA PELATIHAN");
            hapusBTN.setVisibility(View.GONE);
        }

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(tipeForm.equals("edit")){
                    getData();
                } else {
                    namaPelatihanED.setText("");
                    lembagaPelatihanED.setText("");
                    tahunPilihTV.setText("");
                    tahunPilih = "";
                }

                namaPelatihanED.clearFocus();
                lembagaPelatihanED.clearFocus();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
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

        backSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tahunBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                namaPelatihanED.clearFocus();
                lembagaPelatihanED.clearFocus();
                yearPicker();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                namaPelatihanED.clearFocus();
                lembagaPelatihanED.clearFocus();
                if(namaPelatihanED.getText().toString().equals("")||lembagaPelatihanED.getText().toString().equals("")||tahunPilih.equals("")){
                    new KAlertDialog(FormInfoPelatihanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi semua data")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    new KAlertDialog(FormInfoPelatihanActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Simpan data pelatihan?")
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
                                    pDialog = new KAlertDialog(FormInfoPelatihanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPelatihanActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            pDialog.dismiss();
                                            sendData();
                                        }
                                    }.start();

                                }
                            })
                            .show();
                }
            }
        });

    }

    private void yearPicker(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_year_picker, bottomSheet, false));
        Calendar now = Calendar.getInstance();

        closeBTN = findViewById(R.id.close_btn);
        okBTN = findViewById(R.id.ok_btn);
        yearPicker = findViewById(R.id.yearPicker);
        yearPicker.setMinValue(1952);
        yearPicker.setMaxValue(now.get(Calendar.YEAR)+5);
        yearPicker.setValue(now.get(Calendar.YEAR));

        okBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tahunPilih = String.valueOf(yearPicker.getValue());
                tahunPilihTV.setText(tahunPilih);
                bottomSheet.dismissSheet();

            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/upload_data_pelatihan";
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
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);
                            } else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Tersimpan")
                                        .setContentText("Terjadi kesalahan saat mengirim data")
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
                        successPart.setVisibility(View.GONE);
                        formPart.setVisibility(View.VISIBLE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tipe", tipeForm);

                if(tipeForm.equals("edit")){
                    params.put("id_data", idData);
                }

                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("nama_pelatihan", namaPelatihanED.getText().toString());
                params.put("lembaga_pelatihan", lembagaPelatihanED.getText().toString());
                params.put("tahun", tahunPilih);
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(postRequest);

    }

    private void deletePelatihan(String id_pelatihan) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/delete_pelatihan";
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
                                pDialog.setTitleText("Berhasil Dihapus")
                                        .setContentText("Data pelatihan berhasil dihapus")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                pDialog.setTitleText("Gagal Dihapus")
                                        .setContentText("Data pelatihan gagal dihapus")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
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
                        pDialog.setTitleText("Gagal Dihapus")
                                .setContentText("Data pelatihan gagal dihapus")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.ERROR_TYPE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_pelatihan", id_pelatihan);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/data_detail_pelatihan";
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
                                JSONObject dataArray = data.getJSONObject("data");
                                String id = dataArray.getString("id");
                                String NIK = dataArray.getString("NIK");
                                String nama_pelatihan = dataArray.getString("nama_pelatihan");
                                String lembaga_pelatihan = dataArray.getString("lembaga_pelatihan");
                                String tahun = dataArray.getString("tahun");

                                namaPelatihanED.setText(nama_pelatihan);
                                lembagaPelatihanED.setText(lembaga_pelatihan);
                                tahunPilihTV.setText(tahun);
                                tahunPilih = tahun;

                            } else {
                                new KAlertDialog(FormInfoPelatihanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan saat mengakses data")
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
                params.put("id_pelatihan", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormInfoPelatihanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    public void onBackPressed() {
        if(bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}