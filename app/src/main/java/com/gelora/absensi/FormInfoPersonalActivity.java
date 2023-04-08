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
import android.widget.Switch;
import android.widget.TextView;

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

import net.cachapa.expandablelayout.ExpandableLayout;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormInfoPersonalActivity extends AppCompatActivity {

    LinearLayout actionBar, lainnyaBTN, markLainnya, islamBTN, kristenBTN, hinduBTN, buddhaBTN, katolikBTN, konghuchuBTN, markIslam, markKristen, markHindu, markBuddha, markKatolik, markKonghuchu, backSuccessBTN, formPart, successPart, tanggalLAhirBTN, submitBTN, backBTN, agamaBTN, genderBTN, statusPernikahanBTN, maleBTN, femaleBTN, markMale, markFemale, belumMenikahBTN, sudahMenikahBTN, ceraiHidupBTN, ceraiMatiBTN, markBelumMenikah, markSudahMenikah, markCeraiHidup, markCeraiMati;
    TextView agamaPilihTV, namaTV, genderPilihTV, tanggalLahirPilihTV, statusPernikahanPilihTV;
    EditText emailED, tempatLahirED, noHanphoneED, alamatKTPED, alamatDomisiliED;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    ExpandableLayout expandableDomisili;
    BottomSheetLayout bottomSheet;
    ImageView successGif;
    String genderChoice = "", tanggalLAhir = "", statusPernikahanChoice = "", agamaChoice = "";
    KAlertDialog pDialog;
    private int i = -1;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch domisiliSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_personal);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        backSuccessBTN = findViewById(R.id.back_success_btn);
        namaTV = findViewById(R.id.nama_tv);
        emailED = findViewById(R.id.email_ed);
        genderPilihTV = findViewById(R.id.gender_pilih_tv);
        tempatLahirED = findViewById(R.id.tempat_lahir_ed);
        tanggalLahirPilihTV = findViewById(R.id.tanggal_lahir_pilih_tv);
        statusPernikahanPilihTV = findViewById(R.id.status_pernikahan_pilih_tv);
        noHanphoneED = findViewById(R.id.no_hanphone_ed);
        alamatKTPED = findViewById(R.id.alamat_ktp_ed);
        alamatDomisiliED = findViewById(R.id.alamat_domisili_ed);
        domisiliSwitch = findViewById(R.id.alamat_domisili_switch);
        expandableDomisili = findViewById(R.id.expandable_domisili);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        genderBTN = findViewById(R.id.gender_btn);
        agamaBTN = findViewById(R.id.agama_btn);
        agamaPilihTV = findViewById(R.id.agama_pilih_tv);
        tanggalLAhirBTN= findViewById(R.id.tanggal_lahir_btn);
        statusPernikahanBTN = findViewById(R.id.status_pernikahan_btn);
        submitBTN = findViewById(R.id.submit_btn);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);
        actionBar = findViewById(R.id.action_bar);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        tempatLahirED.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tempatLahirED.clearFocus();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
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

        genderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                tempatLahirED.clearFocus();
                genderChoice();
            }
        });

        tanggalLAhirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                tempatLahirED.clearFocus();
                bornDate();
            }
        });

        statusPernikahanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                tempatLahirED.clearFocus();
                statusPernikahanChoice();
            }
        });

        agamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                tempatLahirED.clearFocus();
                agamaChoice();
            }
        });

        domisiliSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                tempatLahirED.clearFocus();
                if (domisiliSwitch.isChecked()){
                    expandableDomisili.collapse();
                } else {
                    expandableDomisili.expand();
                }
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                tempatLahirED.clearFocus();
                if(
                    emailED.getText().toString().equals("")       ||
                    genderChoice.equals("")                       ||
                    tempatLahirED.getText().toString().equals("") ||
                    tanggalLAhir.equals("")                       ||
                    noHanphoneED.getText().toString().equals("")  ||
                    statusPernikahanChoice.equals("")             ||
                    agamaChoice.equals("")                        ||
                    alamatKTPED.getText().toString().equals("")
                ){
                    new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String emailUser = emailED.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (emailUser.matches(emailPattern) && emailUser.length() > 0) {
                        if(alamatDomisiliED.getText().toString().equals("")){
                            if(domisiliSwitch.isChecked()){
                                new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                pDialog = new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormInfoPersonalActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormInfoPersonalActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormInfoPersonalActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormInfoPersonalActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormInfoPersonalActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormInfoPersonalActivity.this, R.color.colorGradien6));
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
                            } else {
                                new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.ERROR_TYPE)
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
                            }
                        }
                        else {
                            new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.WARNING_TYPE)
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
                                            pDialog = new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoPersonalActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoPersonalActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoPersonalActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoPersonalActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoPersonalActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormInfoPersonalActivity.this, R.color.colorGradien6));
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
                        new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.ERROR_TYPE)
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
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_info_personal";
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
                                String nama = dataArray.getString("nama");
                                String email = dataArray.getString("email");
                                String jenis_kelamin = dataArray.getString("jenis_kelamin");
                                String tempat_lahir = dataArray.getString("tempat_lahir");
                                String tanggal_lahir = dataArray.getString("tanggal_lahir");
                                String handphone = dataArray.getString("handphone");
                                String status_pernikahan = dataArray.getString("status_pernikahan");
                                String agama = dataArray.getString("agama");
                                String alamat_ktp = dataArray.getString("alamat_ktp");
                                String alamat_domisili = dataArray.getString("alamat_domisili");

                                namaTV.setText(nama.toUpperCase());

                                if(email.equals("")||email.equals("null")){
                                    emailED.setText("");
                                } else {
                                    emailED.setText(email);
                                }

                                if(jenis_kelamin.equals("")||jenis_kelamin.equals("null")){
                                    genderPilihTV.setText("");
                                } else {
                                    genderPilihTV.setText(jenis_kelamin);
                                    if(jenis_kelamin.equals("Laki-Laki")){
                                        genderChoice = "male";
                                    } else if(jenis_kelamin.equals("Perempuan")){
                                        genderChoice = "female";
                                    }
                                }

                                if(tempat_lahir.equals("")||tempat_lahir.equals("null")){
                                    tempatLahirED.setText("");
                                } else {
                                    tempatLahirED.setText(tempat_lahir);
                                }

                                if(tanggal_lahir.equals("")||tanggal_lahir.equals("null")){
                                    tanggalLahirPilihTV.setText("");
                                } else {
                                    tanggalLAhir = tanggal_lahir;
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

                                }

                                if(handphone.equals("")||handphone.equals("null")){
                                    noHanphoneED.setText("");
                                } else {
                                    noHanphoneED.setText(handphone);
                                }

                                if(status_pernikahan.equals("")||status_pernikahan.equals("null")){
                                    statusPernikahanPilihTV.setText("");
                                } else {
                                    statusPernikahanPilihTV.setText(status_pernikahan);
                                    if(status_pernikahan.equals("Belum Menikah")){
                                        statusPernikahanChoice = "1";
                                    } else if(status_pernikahan.equals("Menikah")){
                                        statusPernikahanChoice = "2";
                                    } else if(status_pernikahan.equals("Cerai Hidup")){
                                        statusPernikahanChoice = "3";
                                    } else if(status_pernikahan.equals("Cerai Mati")){
                                        statusPernikahanChoice = "4";
                                    }
                                }

                                if(agama.equals("")||agama.equals("null")){
                                    agamaPilihTV.setText("");
                                } else {
                                    agamaChoice = agama;
                                    agamaPilihTV.setText(agama);
                                }

                                if(alamat_ktp.equals("")||alamat_ktp.equals("null")){
                                    alamatKTPED.setText("");
                                } else {
                                    alamatKTPED.setText(alamat_ktp);
                                }

                                if(alamat_domisili.equals("")||alamat_domisili.equals("null")){
                                    domisiliSwitch.setChecked(false);
                                    expandableDomisili.expand();
                                    alamatDomisiliED.setText("");
                                } else {
                                    if(alamat_domisili.equals(alamat_ktp)){
                                        domisiliSwitch.setChecked(true);
                                        expandableDomisili.collapse();
                                    } else {
                                        domisiliSwitch.setChecked(false);
                                        expandableDomisili.expand();
                                    }
                                    alamatDomisiliED.setText(alamat_domisili);
                                }

                            } else {
                                new KAlertDialog(FormInfoPersonalActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("nik", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void sendData() {
        String noPhone = noHanphoneED.getText().toString().replace("+", "").replace("-", "").replace(" ","");
        String noHandphone = "";

        if (noPhone.substring(0,2).equals("62")){
            noHandphone = "0"+noPhone.substring(2,noPhone.length());
        } else {
            noHandphone = noPhone;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/update_data_personal";
        String finalNoHandphone = noHandphone;
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("email", emailED.getText().toString());

                if(genderChoice.equals("male")){
                    params.put("jenis_kelamin", "Laki-Laki");
                } else if(genderChoice.equals("female")){
                    params.put("jenis_kelamin", "Perempuan");
                }

                params.put("tempat_lahir", tempatLahirED.getText().toString());
                params.put("tanggal_lahir", tanggalLAhir);
                params.put("handphone", finalNoHandphone);

                if(statusPernikahanChoice.equals("1")){
                    params.put("status_pernikahan", "Belum Menikah");
                } else if(statusPernikahanChoice.equals("2")){
                    params.put("status_pernikahan", "Menikah");
                } else if(statusPernikahanChoice.equals("3")){
                    params.put("status_pernikahan", "Cerai Hidup");
                } else if(statusPernikahanChoice.equals("4")){
                    params.put("status_pernikahan", "Cerai Mati");
                }

                params.put("agama", agamaChoice);
                params.put("alamat_ktp", alamatKTPED.getText().toString());

                if(domisiliSwitch.isChecked()){
                    params.put("alamat_domisili", alamatKTPED.getText().toString());
                } else {
                    params.put("alamat_domisili", alamatDomisiliED.getText().toString());
                }

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void genderChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoPersonalActivity.this).inflate(R.layout.layout_gender, bottomSheet, false));
        maleBTN = findViewById(R.id.male_btn);
        femaleBTN = findViewById(R.id.female_btn);
        markMale = findViewById(R.id.mark_male);
        markFemale = findViewById(R.id.mark_female);

        if (genderChoice.equals("male")){
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markMale.setVisibility(View.VISIBLE);
            markFemale.setVisibility(View.GONE);
        } else if (genderChoice.equals("female")){
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            markMale.setVisibility(View.GONE);
            markFemale.setVisibility(View.VISIBLE);
        } else {
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markMale.setVisibility(View.GONE);
            markFemale.setVisibility(View.GONE);
        }

        maleBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markMale.setVisibility(View.VISIBLE);
                markFemale.setVisibility(View.GONE);
                maleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                maleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
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

    private void agamaChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoPersonalActivity.this).inflate(R.layout.layout_agama, bottomSheet, false));
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
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.VISIBLE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Kristen")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.VISIBLE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Hindu")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.VISIBLE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Buddha")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.VISIBLE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Katolik")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.VISIBLE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Kong Hu Chu")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.VISIBLE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Lainnya")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
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

    @SuppressLint("SimpleDateFormat")
    private void bornDate(){
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormInfoPersonalActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                tanggalLAhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String dayDate = tanggalLAhir.substring(8,10);
                String yearDate = tanggalLAhir.substring(0,4);;
                String bulanValue = tanggalLAhir.substring(5,7);
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

            }, y,m,d);
            dpd.show();

    }

    private void statusPernikahanChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoPersonalActivity.this).inflate(R.layout.layout_status_pernikahan, bottomSheet, false));
        belumMenikahBTN = findViewById(R.id.belum_menikah_btn);
        sudahMenikahBTN = findViewById(R.id.sudah_menikah_btn);
        ceraiHidupBTN = findViewById(R.id.cerai_hidup_btn);
        ceraiMatiBTN = findViewById(R.id.cerai_mati_btn);
        markBelumMenikah = findViewById(R.id.mark_belum_menikah);
        markSudahMenikah = findViewById(R.id.mark_sudah_menikah);
        markCeraiHidup = findViewById(R.id.mark_cerai_hidup);
        markCeraiMati = findViewById(R.id.mark_cerai_mati);

        if (statusPernikahanChoice.equals("1")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.VISIBLE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("2")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.VISIBLE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("3")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.VISIBLE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("4")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.VISIBLE);
        }

        belumMenikahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
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
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoPersonalActivity.this, R.drawable.shape_option_choice));
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

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
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
        CookieBar.build(FormInfoPersonalActivity.this)
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