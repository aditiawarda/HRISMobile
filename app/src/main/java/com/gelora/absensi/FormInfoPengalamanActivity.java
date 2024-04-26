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
import android.widget.Switch;
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

import net.cachapa.expandablelayout.ExpandableLayout;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormInfoPengalamanActivity extends AppCompatActivity {

    LinearLayout hapusBTN, submitBTN, successPart, formPart, backSuccessBTN, backBTN, dariTahunBTN, sampaiTahunBTN, closeBTNMulai, okBTNMulai, closeBTNAkhir, okBTNAkhir;
    TextView titlePageTV, dariTahunPilihTV, sampaiTahunPilihTV;
    EditText posisiED;
    SharedPrefManager sharedPrefManager;
    BottomSheetLayout bottomSheet;
    NumberPicker yearPickerMulai, yearPickerAkhir;
    String tahunMulai = "", tahunAkhir = "", tipeForm = "", idData = "";
    ExpandableLayout expandableTahunAkhir;
    SwipeRefreshLayout refreshLayout;
    KAlertDialog pDialog;
    ImageView successGif;
    private int i = -1;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch tahunAkhirSwitch;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_pengalaman);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        titlePageTV = findViewById(R.id.title_page_tv);
        successPart = findViewById(R.id.success_submit);
        formPart = findViewById(R.id.form_part);
        backBTN = findViewById(R.id.back_btn);
        posisiED = findViewById(R.id.posisi_ed);
        backSuccessBTN = findViewById(R.id.back_success_btn);
        dariTahunBTN = findViewById(R.id.dari_tahun_btn);
        dariTahunPilihTV = findViewById(R.id.dari_tahun_pilih_tv);
        sampaiTahunBTN = findViewById(R.id.sampai_tahun_btn);
        sampaiTahunPilihTV = findViewById(R.id.sampai_tahun_pilih_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        tahunAkhirSwitch = findViewById(R.id.tahun_akhir_switch);
        expandableTahunAkhir = findViewById(R.id.expandable_tahun_akhir);
        submitBTN = findViewById(R.id.submit_btn);
        successGif = findViewById(R.id.success_gif);
        hapusBTN = findViewById(R.id.hapus_btn);

        posisiED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        tipeForm = getIntent().getExtras().getString("tipe");

        if(tipeForm.equals("edit")){
            idData = getIntent().getExtras().getString("id_pengalaman");
            titlePageTV.setText("EDIT DATA PENGALAMAN");
            hapusBTN.setVisibility(View.VISIBLE);

            hapusBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Yakin untuk menghapus data pengalaman?")
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
                                    pDialog = new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            deletePengalaman(idData);
                                        }
                                    }.start();

                                }
                            })
                            .show();
                }
            });

            getData();

        } else {
            titlePageTV.setText("FORM DATA PENGALAMAN");
            hapusBTN.setVisibility(View.GONE);
        }

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(tipeForm.equals("edit")){
                    getData();
                } else {
                    posisiED.setText("");
                    dariTahunPilihTV.setText("");
                    sampaiTahunPilihTV.setText("");
                    tahunMulai = "";
                    tahunAkhir = "";
                    tahunAkhirSwitch.setChecked(false);
                    expandableTahunAkhir.expand();
                }

                posisiED.clearFocus();

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

        backSuccessBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dariTahunBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                posisiED.clearFocus();
                dariTahunPicker();
            }
        });

        tahunAkhirSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                posisiED.clearFocus();
                if (tahunAkhirSwitch.isChecked()){
                    expandableTahunAkhir.collapse();
                    tahunAkhir = getDateY();
                } else {
                    expandableTahunAkhir.expand();
                    tahunAkhir = "";
                }
            }
        });

        sampaiTahunBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                posisiED.clearFocus();
                sampaiTahunPicker();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                posisiED.clearFocus();
                if(posisiED.getText().toString().equals("")||tahunMulai.equals("")||tahunAkhir.equals("")){
                    new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Simpan data pengalaman?")
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
                                    pDialog = new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormInfoPengalamanActivity.this, R.color.colorGradien6));
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

    private void deletePengalaman(String id_pengalaman) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/delete_pengalaman";
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
                                        .setContentText("Data pengalaman berhasil dihapus")
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
                                        .setContentText("Data pengalaman gagal dihapus")
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
                                .setContentText("Data pengalaman gagal dihapus")
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
                params.put("id_pengalaman", id_pengalaman);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/data_detail_pengalaman";
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
                                String deskripsi_posisi = dataArray.getString("deskripsi_posisi");
                                String dari_tahun = dataArray.getString("dari_tahun");
                                String sampai_tahun = dataArray.getString("sampai_tahun");

                                posisiED.setText(deskripsi_posisi);
                                dariTahunPilihTV.setText(dari_tahun);
                                tahunMulai = dari_tahun;

                                if(sampai_tahun.equals("Sekarang")){
                                    tahunAkhir = getDateY();
                                    expandableTahunAkhir.collapse();
                                    tahunAkhirSwitch.setChecked(true);
                                } else {
                                    sampaiTahunPilihTV.setText(sampai_tahun);
                                    tahunAkhir = sampai_tahun;
                                    expandableTahunAkhir.expand();
                                    tahunAkhirSwitch.setChecked(false);
                                }

                            } else {
                                new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_pengalaman", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void dariTahunPicker (){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_year_picker_mulai, bottomSheet, false));
        Calendar now = Calendar.getInstance();

        closeBTNMulai = findViewById(R.id.close_btn);
        okBTNMulai = findViewById(R.id.ok_btn);
        yearPickerMulai = findViewById(R.id.yearPicker);
        yearPickerMulai.setMinValue(1952);
        yearPickerMulai.setMaxValue(now.get(Calendar.YEAR)+5);
        yearPickerMulai.setValue(now.get(Calendar.YEAR));

        okBTNMulai.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tahunMulai = String.valueOf(yearPickerMulai.getValue());
                if(!tahunAkhir.equals("")){
                    if(Integer.parseInt(tahunMulai)<=Integer.parseInt(tahunAkhir)){
                        dariTahunPilihTV.setText(tahunMulai);
                        bottomSheet.dismissSheet();
                    } else {
                        tahunMulai = "";
                        dariTahunPilihTV.setText("Pilih kembali !");
                        new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tahun mulai tidak dapat lebih besar dari tahun akhir, harap ulangi!")
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
                    dariTahunPilihTV.setText(tahunMulai);
                    bottomSheet.dismissSheet();
                }
            }
        });

        closeBTNMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

    }

    private void sampaiTahunPicker (){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_year_picker_akhir, bottomSheet, false));
        Calendar now = Calendar.getInstance();

        closeBTNAkhir = findViewById(R.id.close_btn);
        okBTNAkhir = findViewById(R.id.ok_btn);
        yearPickerAkhir = findViewById(R.id.yearPicker);
        yearPickerAkhir.setMinValue(1952);
        yearPickerAkhir.setMaxValue(now.get(Calendar.YEAR)+5);
        yearPickerAkhir.setValue(now.get(Calendar.YEAR));

        okBTNAkhir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tahunAkhir = String.valueOf(yearPickerAkhir.getValue());
                if(!tahunMulai.equals("")){
                    if(Integer.parseInt(tahunMulai)<=Integer.parseInt(tahunAkhir)){
                        sampaiTahunPilihTV.setText(tahunAkhir);
                        bottomSheet.dismissSheet();
                    } else {
                        tahunAkhir = "";
                        sampaiTahunPilihTV.setText("Pilih kembali !");
                        new KAlertDialog(FormInfoPengalamanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tahun akhir tidak dapat lebih kecil dari tahun mulai, harap ulangi!")
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
                    sampaiTahunPilihTV.setText(tahunAkhir);
                    bottomSheet.dismissSheet();
                }
            }
        });

        closeBTNAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/upload_data_pengalaman";
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
                params.put("deskripsi_posisi", posisiED.getText().toString());
                params.put("dari_tahun", tahunMulai);

                if(tahunAkhir.equals(getDateY()) && tahunAkhirSwitch.isChecked()){
                    params.put("sampai_tahun", "Sekarang");
                } else {
                    params.put("sampai_tahun", tahunAkhir);
                }

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

    private void connectionFailed(){
        CookieBar.build(FormInfoPengalamanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void onBackPressed() {
        if(bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}