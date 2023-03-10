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
import android.widget.TextView;
import android.widget.Toast;

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
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormInfoKeluargaActivity extends AppCompatActivity {

    LinearLayout submitBTN, actionBar, lainnyaBTN, markLainnya, islamBTN, kristenBTN, hinduBTN, buddhaBTN, katolikBTN, konghuchuBTN, markIslam, markKristen, markHindu, markBuddha, markKatolik, markKonghuchu, backSuccessBTN, formPart, successPart, tanggalLAhirBTN, backBTN, agamaBTN, genderBTN, statusPernikahanBTN, maleBTN, femaleBTN, markMale, markFemale, belumMenikahBTN, sudahMenikahBTN, ceraiHidupBTN, ceraiMatiBTN, markBelumMenikah, markSudahMenikah, markCeraiHidup, markCeraiMati;
    TextView titlePageTV, agamaPilihTV, namaTV, genderPilihTV, tanggalLahirPilihTV, statusPernikahanPilihTV;
    LinearLayout hubunganBTN, hubunganLainnyaPart, bekerjaBTN, tidakBekerjaBTN, markBekerja, markTidakBekerja;
    LinearLayout sodaraLakiBTN, sodaraPerempuanBTN, markSodaraLaki, markSodaraPerempuan, ayahBTN, ibuBTN, suamiBTN, istriBTN, anakBTN, markAyah, markIbu, markSuami, markIstri, markAnak;
    LinearLayout statusBekerjaBTN, tanggalLahirBTN;
    TextView statusBekerjaPilihTV, hubunganPilihTV;
    EditText namaED, tempatLahirED, hubunganLainnyaED;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    KAlertDialog pDialog;
    ImageView successGif;
    private int i = -1;
    String tipeForm = "", idData = "";
    String statusBekerjaChoice = "", genderChoice = "", tanggalLahir = "", hubunganPilih = "", statusPernikahanChoice = "", agamaChoice = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_keluarga);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        successPart = findViewById(R.id.success_submit);
        formPart = findViewById(R.id.form_part);
        submitBTN = findViewById(R.id.submit_btn);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        namaED = findViewById(R.id.nama_ed);
        tempatLahirED = findViewById(R.id.tempat_lahir_ed);
        tanggalLahirBTN = findViewById(R.id.tanggal_lahir_btn);
        tanggalLahirPilihTV = findViewById(R.id.tanggal_lahir_pilih_tv);
        genderBTN = findViewById(R.id.gender_btn);
        genderPilihTV = findViewById(R.id.gender_pilih_tv);
        hubunganBTN = findViewById(R.id.hubungan_btn);
        hubunganPilihTV = findViewById(R.id.hubungan_pilih_tv);
        hubunganLainnyaPart = findViewById(R.id.hubungan_lainnya_part);
        hubunganLainnyaED = findViewById(R.id.hubungan_lainnya_ed);
        statusPernikahanBTN = findViewById(R.id.status_pernikahan_btn);
        statusPernikahanPilihTV = findViewById(R.id.status_pernikahan_pilih_tv);
        agamaBTN = findViewById(R.id.agama_btn);
        agamaPilihTV = findViewById(R.id.agama_pilih_tv);
        statusBekerjaBTN = findViewById(R.id.status_bekerja_btn);
        statusBekerjaPilihTV = findViewById(R.id.status_bekerja_pilih_tv);
        titlePageTV = findViewById(R.id.title_page_tv);
        submitBTN = findViewById(R.id.submit_btn);
        backSuccessBTN= findViewById(R.id.back_success_btn);
        successGif = findViewById(R.id.success_gif);

        namaED.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        tempatLahirED.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hubunganLainnyaED.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        tipeForm = getIntent().getExtras().getString("tipe");

        if(tipeForm.equals("edit")){
            idData = getIntent().getExtras().getString("id_data");
            titlePageTV.setText("EDIT INFO KELUARGA");
            getData();
        } else {
            titlePageTV.setText("FORM INFO KELUARGA");
        }

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(tipeForm.equals("edit")){
                    getData();
                } else {
                    namaED.setText("");
                    tempatLahirED.setText("");
                    tanggalLahirPilihTV.setText("");
                    tanggalLahir = "";
                    genderPilihTV.setText("");
                    genderChoice = "";
                    hubunganPilihTV.setText("");
                    hubunganPilih = "";
                    hubunganLainnyaPart.setVisibility(View.GONE);
                    hubunganLainnyaED.setText("");
                    statusPernikahanPilihTV.setText("");
                    statusPernikahanChoice = "";
                    agamaPilihTV.setText("");
                    agamaChoice = "";
                    statusBekerjaPilihTV.setText("");
                    statusBekerjaChoice = "";
                }

                namaED.clearFocus();
                tempatLahirED.clearFocus();
                hubunganLainnyaED.clearFocus();

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

        tanggalLahirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                bornDate();
            }
        });

        genderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderChoice();
            }
        });

        hubunganBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                hubunganChoice();
            }
        });

        agamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agamaChoice();
            }
        });

        statusPernikahanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusPernikahanChoice();
            }
        });

        statusBekerjaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBekerjaChoice();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(
                        namaED.getText().toString().equals("")        ||
                        tempatLahirED.getText().toString().equals("") ||
                        tanggalLahir.equals("")                       ||
                        genderChoice.equals("")                       ||
                        hubunganPilih.equals("")                      ||
                        statusPernikahanChoice.equals("")             ||
                        agamaChoice.equals("")                        ||
                        statusBekerjaChoice.equals("")
                ){
                    new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.ERROR_TYPE)
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

                    if(hubunganPilih.equals("Lainnya")){
                        if(hubunganLainnyaED.getText().toString().equals("")){
                            new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Simpan data keluarga?")
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
                                            pDialog = new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoKeluargaActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoKeluargaActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoKeluargaActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoKeluargaActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoKeluargaActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoKeluargaActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    sendData();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }

                    }
                    else {
                        new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Simpan data personal?")
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
                                        pDialog = new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInfoKeluargaActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInfoKeluargaActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInfoKeluargaActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInfoKeluargaActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInfoKeluargaActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInfoKeluargaActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }
                                            public void onFinish() {
                                                i = -1;
                                                sendData();
                                            }
                                        }.start();

                                    }
                                })
                                .show();
                    }

                }
            }
        });

    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/upload_data_keluarga";
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
                params.put("nama", namaED.getText().toString());

                if(hubunganPilih.equals("Lainnya")){
                    params.put("hubungan", hubunganPilih);
                    params.put("hubungan_lainnya", hubunganLainnyaED.getText().toString());
                } else {
                    params.put("hubungan", hubunganPilih);
                }

                params.put("tempat_lahir", tempatLahirED.getText().toString());
                params.put("tanggal_lahir", tanggalLahir);

                if(genderChoice.equals("male")){
                    params.put("jenis_kelamin", "Laki-Laki");
                } else if(genderChoice.equals("female")){
                    params.put("jenis_kelamin", "Perempuan");
                }

                params.put("agama", agamaChoice);

                if(statusPernikahanChoice.equals("1")){
                    params.put("status_pernikahan", "Belum Menikah");
                } else if(statusPernikahanChoice.equals("2")){
                    params.put("status_pernikahan", "Menikah");
                } else if(statusPernikahanChoice.equals("3")){
                    params.put("status_pernikahan", "Cerai Hidup");
                } else if(statusPernikahanChoice.equals("4")){
                    params.put("status_pernikahan", "Cerai Mati");
                }

                params.put("pekerjaan", statusBekerjaChoice);

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

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_detail_keluarga";
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
                                String nama = dataArray.getString("nama");
                                String hubungan = dataArray.getString("hubungan");
                                String hubungan_lainnya = dataArray.getString("hubungan_lainnya");
                                String tempat_lahir = dataArray.getString("tempat_lahir");
                                String tanggal_lahir = dataArray.getString("tanggal_lahir");
                                String jenis_kelamin = dataArray.getString("jenis_kelamin");
                                String agama = dataArray.getString("agama");
                                String status_pernikahan = dataArray.getString("status_pernikahan");
                                String pekerjaan = dataArray.getString("pekerjaan");

                                namaED.setText(nama);
                                tempatLahirED.setText(tempat_lahir);

                                String dayDate = tanggal_lahir.substring(8,10);
                                String yearDate = tanggal_lahir.substring(0,4);;
                                String bulanValue = tanggal_lahir.substring(5,7);
                                String bulanName;

                                switch (bulanValue) {
                                    case "01":
                                        bulanName = "Jan";
                                        break;
                                    case "02":
                                        bulanName = "Feb";
                                        break;
                                    case "03":
                                        bulanName = "Mar";
                                        break;
                                    case "04":
                                        bulanName = "Apr";
                                        break;
                                    case "05":
                                        bulanName = "Mei";
                                        break;
                                    case "06":
                                        bulanName = "Jun";
                                        break;
                                    case "07":
                                        bulanName = "Jul";
                                        break;
                                    case "08":
                                        bulanName = "Agu";
                                        break;
                                    case "09":
                                        bulanName = "Sep";
                                        break;
                                    case "10":
                                        bulanName = "Okt";
                                        break;
                                    case "11":
                                        bulanName = "Nov";
                                        break;
                                    case "12":
                                        bulanName = "Des";
                                        break;
                                    default:
                                        bulanName = "Not found!";
                                        break;
                                }

                                tanggalLahirPilihTV.setText(dayDate+" "+bulanName+" "+yearDate);
                                tanggalLahir = tanggal_lahir;

                                genderPilihTV.setText(jenis_kelamin);
                                if(jenis_kelamin.equals("Laki-Laki")){
                                    genderChoice = "male";
                                } else if(jenis_kelamin.equals("Perempuan")){
                                    genderChoice = "female";
                                }

                                hubunganPilihTV.setText(hubungan_lainnya);
                                if(hubungan.equals("Lainnya")){
                                    hubunganLainnyaPart.setVisibility(View.VISIBLE);
                                    hubunganLainnyaED.setText(hubungan_lainnya);
                                } else {
                                    hubunganLainnyaPart.setVisibility(View.GONE);
                                    hubunganPilihTV.setText(hubungan);
                                }

                                statusPernikahanPilihTV.setText(status_pernikahan);
                                statusPernikahanChoice = status_pernikahan;

                                agamaPilihTV.setText(agama);
                                agamaChoice = agama;

                                statusBekerjaPilihTV.setText(pekerjaan);
                                statusBekerjaChoice = pekerjaan;

                            } else {
                                new KAlertDialog(FormInfoKeluargaActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_data", idData);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SimpleDateFormat")
    private void bornDate(){
        int y = Integer.parseInt(getDateY());
        int m = Integer.parseInt(getDateM());
        int d = Integer.parseInt(getDateD());
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInfoKeluargaActivity.this, (view1, year, month, dayOfMonth) -> {

            tanggalLahir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            String dayDate = tanggalLahir.substring(8,10);
            String yearDate = tanggalLahir.substring(0,4);;
            String bulanValue = tanggalLahir.substring(5,7);
            String bulanName;

            switch (bulanValue) {
                case "01":
                    bulanName = "Jan";
                    break;
                case "02":
                    bulanName = "Feb";
                    break;
                case "03":
                    bulanName = "Mar";
                    break;
                case "04":
                    bulanName = "Apr";
                    break;
                case "05":
                    bulanName = "Mei";
                    break;
                case "06":
                    bulanName = "Jun";
                    break;
                case "07":
                    bulanName = "Jul";
                    break;
                case "08":
                    bulanName = "Agu";
                    break;
                case "09":
                    bulanName = "Sep";
                    break;
                case "10":
                    bulanName = "Okt";
                    break;
                case "11":
                    bulanName = "Nov";
                    break;
                case "12":
                    bulanName = "Des";
                    break;
                default:
                    bulanName = "Not found!";
                    break;
            }

            tanggalLahirPilihTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

        }, y,m,d);
        dpd.show();

    }

    private void genderChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_gender, bottomSheet, false));
        maleBTN = findViewById(R.id.male_btn);
        femaleBTN = findViewById(R.id.female_btn);
        markMale = findViewById(R.id.mark_male);
        markFemale = findViewById(R.id.mark_female);

        if (genderChoice.equals("male")){
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markMale.setVisibility(View.VISIBLE);
            markFemale.setVisibility(View.GONE);
        } else if (genderChoice.equals("female")){
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markMale.setVisibility(View.GONE);
            markFemale.setVisibility(View.VISIBLE);
        } else {
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markMale.setVisibility(View.GONE);
            markFemale.setVisibility(View.GONE);
        }

        maleBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markMale.setVisibility(View.VISIBLE);
                markFemale.setVisibility(View.GONE);
                maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                genderChoice = "male";
                genderPilihTV.setText("Laki-Laki");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        femaleBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markMale.setVisibility(View.GONE);
                markFemale.setVisibility(View.VISIBLE);
                maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                genderChoice = "female";
                genderPilihTV.setText("Perempuan");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void hubunganChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_hubungan, bottomSheet, false));
        ayahBTN = findViewById(R.id.ayah_btn);
        ibuBTN = findViewById(R.id.ibu_btn);
        suamiBTN = findViewById(R.id.suami_btn);
        istriBTN = findViewById(R.id.istri_btn);
        anakBTN = findViewById(R.id.anak_btn);
        sodaraLakiBTN = findViewById(R.id.sodara_laki_btn);
        sodaraPerempuanBTN = findViewById(R.id.sodara_perempuan_btn);
        lainnyaBTN = findViewById(R.id.lainnya_btn);
        markAyah = findViewById(R.id.mark_ayah);
        markIbu = findViewById(R.id.mark_ibu);
        markSuami = findViewById(R.id.mark_suami);
        markIstri = findViewById(R.id.mark_istri);
        markAnak = findViewById(R.id.mark_anak);
        markSodaraLaki = findViewById(R.id.mark_sodara_laki);
        markSodaraPerempuan = findViewById(R.id.mark_sodara_perempuan);
        markLainnya = findViewById(R.id.mark_lainnya);

        if (hubunganPilih.equals("Ayah")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.VISIBLE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Ibu")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.VISIBLE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Suami")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.VISIBLE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Istri")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.VISIBLE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Anak")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.VISIBLE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Saudara Laki-Laki")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.VISIBLE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Saudara Perempuan")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.VISIBLE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Lainnya")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.VISIBLE);

            hubunganLainnyaPart.setVisibility(View.VISIBLE);
        }

        ayahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.VISIBLE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Ayah";
                hubunganPilihTV.setText("Ayah");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        ibuBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.VISIBLE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Ibu";
                hubunganPilihTV.setText("Ibu");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        suamiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.VISIBLE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Suami";
                hubunganPilihTV.setText("Suami");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        istriBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.VISIBLE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Istri";
                hubunganPilihTV.setText("Istri");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        anakBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.VISIBLE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Anak";
                hubunganPilihTV.setText("Anak");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sodaraLakiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.VISIBLE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Saudara Laki-Laki";
                hubunganPilihTV.setText("Saudara Laki-Laki");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sodaraPerempuanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.VISIBLE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Saudara Perempuan";
                hubunganPilihTV.setText("Saudara Perempuan");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        lainnyaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.VISIBLE);

                hubunganLainnyaPart.setVisibility(View.VISIBLE);

                hubunganPilih = "Lainnya";
                hubunganPilihTV.setText("Lainnya");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void statusPernikahanChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_status_pernikahan, bottomSheet, false));
        belumMenikahBTN = findViewById(R.id.belum_menikah_btn);
        sudahMenikahBTN = findViewById(R.id.sudah_menikah_btn);
        ceraiHidupBTN = findViewById(R.id.cerai_hidup_btn);
        ceraiMatiBTN = findViewById(R.id.cerai_mati_btn);
        markBelumMenikah = findViewById(R.id.mark_belum_menikah);
        markSudahMenikah = findViewById(R.id.mark_sudah_menikah);
        markCeraiHidup = findViewById(R.id.mark_cerai_hidup);
        markCeraiMati = findViewById(R.id.mark_cerai_mati);

        if (statusPernikahanChoice.equals("1")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.VISIBLE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("2")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.VISIBLE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("3")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.VISIBLE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("4")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.VISIBLE);
        }

        belumMenikahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markBelumMenikah.setVisibility(View.VISIBLE);
                markSudahMenikah.setVisibility(View.GONE);
                markCeraiHidup.setVisibility(View.GONE);
                markCeraiMati.setVisibility(View.GONE);

                statusPernikahanChoice = "1";
                statusPernikahanPilihTV.setText("Belum Menikah");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sudahMenikahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markBelumMenikah.setVisibility(View.GONE);
                markSudahMenikah.setVisibility(View.VISIBLE);
                markCeraiHidup.setVisibility(View.GONE);
                markCeraiMati.setVisibility(View.GONE);

                statusPernikahanChoice = "2";
                statusPernikahanPilihTV.setText("Menikah");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        ceraiHidupBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markBelumMenikah.setVisibility(View.GONE);
                markSudahMenikah.setVisibility(View.GONE);
                markCeraiHidup.setVisibility(View.VISIBLE);
                markCeraiMati.setVisibility(View.GONE);

                statusPernikahanChoice = "3";
                statusPernikahanPilihTV.setText("Cerai Hidup");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        ceraiMatiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                markBelumMenikah.setVisibility(View.GONE);
                markSudahMenikah.setVisibility(View.GONE);
                markCeraiHidup.setVisibility(View.GONE);
                markCeraiMati.setVisibility(View.VISIBLE);

                statusPernikahanChoice = "4";
                statusPernikahanPilihTV.setText("Cerai Mati");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void agamaChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_agama, bottomSheet, false));
        islamBTN = findViewById(R.id.islam_btn);
        kristenBTN = findViewById(R.id.kristen_btn);
        hinduBTN = findViewById(R.id.hindu_btn);
        buddhaBTN = findViewById(R.id.buddha_btn);
        katolikBTN = findViewById(R.id.katolik_btn);
        konghuchuBTN = findViewById(R.id.konghuchu_btn);
        lainnyaBTN = findViewById(R.id.lainnya_btn);
        markIslam = findViewById(R.id.mark_islam);
        markKristen = findViewById(R.id.mark_kristen);
        markHindu = findViewById(R.id.mark_hindu);
        markBuddha = findViewById(R.id.mark_buddha);
        markKatolik = findViewById(R.id.mark_katolik);
        markKonghuchu = findViewById(R.id.mark_konghuchu);
        markLainnya = findViewById(R.id.mark_lainnya);

        if (agamaChoice.equals("Islam")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.VISIBLE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Kristen")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.VISIBLE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Hindu")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.VISIBLE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Buddha")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.VISIBLE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Katolik")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.VISIBLE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Kong Hu Chu")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.VISIBLE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Lainnya")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.VISIBLE);
        }

        islamBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.VISIBLE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Islam";
                agamaPilihTV.setText("Islam");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        kristenBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.VISIBLE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Kristen";
                agamaPilihTV.setText("Kristen");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        hinduBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.VISIBLE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Hindu";
                agamaPilihTV.setText("Hindu");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        buddhaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.VISIBLE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Buddha";
                agamaPilihTV.setText("Buddha");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        katolikBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.VISIBLE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Katolik";
                agamaPilihTV.setText("Katolik");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        konghuchuBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.VISIBLE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Kong Hu Chu";
                agamaPilihTV.setText("Kong Hu Chu");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        lainnyaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.VISIBLE);

                agamaChoice = "Lainnya";
                agamaPilihTV.setText("Lainnya");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void statusBekerjaChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_status_pekerjaan, bottomSheet, false));
        bekerjaBTN = findViewById(R.id.bekerja_btn);
        tidakBekerjaBTN = findViewById(R.id.tidak_bekerja_btn);
        markBekerja = findViewById(R.id.mark_bekerja);
        markTidakBekerja = findViewById(R.id.mark_tidak_bekerja);

        if (statusBekerjaChoice.equals("Bekerja")){
            bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBekerja.setVisibility(View.VISIBLE);
            markTidakBekerja.setVisibility(View.GONE);
        } else if (statusBekerjaChoice.equals("Belum/Tidak Bekerja")){
            bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markBekerja.setVisibility(View.GONE);
            markTidakBekerja.setVisibility(View.VISIBLE);
        } else {
            bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBekerja.setVisibility(View.GONE);
            markTidakBekerja.setVisibility(View.GONE);
        }

        bekerjaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markBekerja.setVisibility(View.VISIBLE);
                markTidakBekerja.setVisibility(View.GONE);
                bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                statusBekerjaChoice = "Bekerja";
                statusBekerjaPilihTV.setText("Bekerja");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        tidakBekerjaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markBekerja.setVisibility(View.GONE);
                markTidakBekerja.setVisibility(View.VISIBLE);
                bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                statusBekerjaChoice = "Belum/Tidak Bekerja";
                statusBekerjaPilihTV.setText("Belum/Tidak Bekerja");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void connectionFailed(){
        CookieBar.build(FormInfoKeluargaActivity.this)
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