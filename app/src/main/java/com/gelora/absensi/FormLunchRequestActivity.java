package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterUnitBagian2;
import com.gelora.absensi.adapter.AdapterUnitBagianLunchRequest;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.BagianLunchRequest;
import com.gelora.absensi.model.UnitBagian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormLunchRequestActivity extends AppCompatActivity {

    LinearLayout removeBTN1, addBTN1;
    EditText jumlahTV1;

    LinearLayout removeBTN2, addBTN2;
    EditText jumlahTV2;

    LinearLayout removeBTN3, addBTN3;
    EditText jumlahTV3;

    LinearLayout removeBTN4, addBTN4;
    EditText jumlahTV4;

    LinearLayout submitBTN, backBTN, bagianBTN, dateBTN;
    TextView bagianPilihTV, tanggalPilihTV;

    private RecyclerView bagianRV;
    AdapterUnitBagianLunchRequest adapterUnitBagianLunchRequest;
    private BagianLunchRequest[] bagianLunchRequests;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    int siang1 = 0, siang2 = 0, sore = 0, malam = 0;
    String permohonanTerkirim = "0", tanggal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lunch_request);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);

        removeBTN1 = findViewById(R.id.remove_1_btn);
        addBTN1 = findViewById(R.id.add_1_btn);
        jumlahTV1 = findViewById(R.id.jumlah_1_tv);

        removeBTN2 = findViewById(R.id.remove_2_btn);
        addBTN2 = findViewById(R.id.add_2_btn);
        jumlahTV2 = findViewById(R.id.jumlah_2_tv);

        removeBTN3 = findViewById(R.id.remove_3_btn);
        addBTN3 = findViewById(R.id.add_3_btn);
        jumlahTV3 = findViewById(R.id.jumlah_3_tv);

        removeBTN4 = findViewById(R.id.remove_4_btn);
        addBTN4 = findViewById(R.id.add_4_btn);
        jumlahTV4 = findViewById(R.id.jumlah_4_tv);

        submitBTN = findViewById(R.id.submit_btn);
        backBTN = findViewById(R.id.back_btn);
        bagianBTN = findViewById(R.id.bagian_btn);
        bagianPilihTV = findViewById(R.id.bagian_pilih_tv);
        dateBTN = findViewById(R.id.date_btn);
        tanggalPilihTV = findViewById(R.id.date_pilih_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);

        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_BAGIAN_RL, "");
        LocalBroadcastManager.getInstance(this).registerReceiver(bagianBroadRL, new IntentFilter("bagian_broad_rl"));

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bagianBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceBagian();
            }
        });

        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                choiceDate();
            }
        });

        jumlahTV1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    jumlahTV1.clearFocus();
                    jumlahTV1.setText("0");
                    siang1 = 0;
                }
            }
        });

        jumlahTV2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    jumlahTV2.clearFocus();
                    jumlahTV2.setText("0");
                    siang2 = 0;
                }
            }
        });

        jumlahTV3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    jumlahTV3.clearFocus();
                    jumlahTV3.setText("0");
                    sore = 0;
                }
            }
        });

        jumlahTV4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    jumlahTV4.clearFocus();
                    jumlahTV4.setText("0");
                    malam = 0;
                }
            }
        });

        jumlahTV4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();

                return false;
            }
        });

        removeBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV1.getText().toString().isEmpty()){
                    jumlahTV1.setText(String.valueOf(0));
                    siang1 = 0;
                } else if(Integer.parseInt(jumlahTV1.getText().toString()) == 0){
                    jumlahTV1.setText(String.valueOf(0));
                    siang1 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV1.getText().toString()) - 1;
                    jumlahTV1.setText(String.valueOf(newValue));
                    siang1 = newValue;
                }
            }
        });

        addBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV1.getText().toString().isEmpty()){
                    jumlahTV1.setText(String.valueOf(0));
                    siang1 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV1.getText().toString()) + 1;
                    jumlahTV1.setText(String.valueOf(newValue));
                    siang1 = newValue;
                }
            }
        });

        removeBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV2.getText().toString().isEmpty()){
                    jumlahTV2.setText(String.valueOf(0));
                    siang2 = 0;
                } else if(Integer.parseInt(jumlahTV2.getText().toString()) == 0){
                    jumlahTV2.setText(String.valueOf(0));
                    siang2 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV2.getText().toString()) - 1;
                    jumlahTV2.setText(String.valueOf(newValue));
                    siang2 = newValue;
                }
            }
        });

        addBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV2.getText().toString().isEmpty()){
                    jumlahTV2.setText(String.valueOf(0));
                    siang2 = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV2.getText().toString()) + 1;
                    jumlahTV2.setText(String.valueOf(newValue));
                    siang2 = newValue;
                }
            }
        });

        removeBTN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV3.getText().toString().isEmpty()){
                    jumlahTV3.setText(String.valueOf(0));
                    sore = 0;
                } else if(Integer.parseInt(jumlahTV3.getText().toString()) == 0){
                    jumlahTV3.setText(String.valueOf(0));
                    sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV3.getText().toString()) - 1;
                    jumlahTV3.setText(String.valueOf(newValue));
                    sore = newValue;
                }
            }
        });

        addBTN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV3.getText().toString().isEmpty()){
                    jumlahTV3.setText(String.valueOf(0));
                    sore = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV3.getText().toString()) + 1;
                    jumlahTV3.setText(String.valueOf(newValue));
                    sore = newValue;
                }
            }
        });

        removeBTN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV4.getText().toString().isEmpty()){
                    jumlahTV4.setText(String.valueOf(0));
                    malam = 0;
                } else if(Integer.parseInt(jumlahTV4.getText().toString()) == 0){
                    jumlahTV4.setText(String.valueOf(0));
                    malam = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV4.getText().toString()) - 1;
                    jumlahTV4.setText(String.valueOf(newValue));
                    malam = newValue;
                }
            }
        });

        addBTN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();
                if(jumlahTV4.getText().toString().isEmpty()){
                    jumlahTV4.setText(String.valueOf(0));
                    malam = 0;
                } else {
                    int newValue = Integer.parseInt(jumlahTV4.getText().toString()) + 1;
                    jumlahTV4.setText(String.valueOf(newValue));
                    malam = newValue;
                }
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                jumlahTV1.clearFocus();
                jumlahTV2.clearFocus();
                jumlahTV3.clearFocus();
                jumlahTV4.clearFocus();

                jumlahTV1.setText(String.valueOf(Integer.parseInt(jumlahTV1.getText().toString())));
                jumlahTV2.setText(String.valueOf(Integer.parseInt(jumlahTV2.getText().toString())));
                jumlahTV3.setText(String.valueOf(Integer.parseInt(jumlahTV3.getText().toString())));
                jumlahTV4.setText(String.valueOf(Integer.parseInt(jumlahTV4.getText().toString())));

                if(bagianPilihTV.getText().toString().isEmpty()){
                    new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi pilih bagian")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if(tanggal.isEmpty()){
                        new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi pilih tanggal")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        if(Integer.parseInt(jumlahTV1.getText().toString())==0 && Integer.parseInt(jumlahTV2.getText().toString())==0 && Integer.parseInt(jumlahTV3.getText().toString())==0 && Integer.parseInt(jumlahTV4.getText().toString())==0){
                            new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi jumlah permohonan")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sendData();
                                }
                            }, 1);
                        }
                    }

                }
            }
        });

    }

    private void choiceBagian(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_bagian_lunch_request, bottomSheet, false));
        bagianRV = findViewById(R.id.bagian_rv);

        try {
            bagianRV.setLayoutManager(new LinearLayoutManager(this));
            bagianRV.setHasFixedSize(true);
            bagianRV.setNestedScrollingEnabled(false);
            bagianRV.setItemAnimator(new DefaultItemAnimator());

            getBagian();
        } catch (NullPointerException e){
            Log.e("Error", e.toString());
        }

    }

    private void getBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_bagian_lunch_request";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String bagian = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            bagianLunchRequests = gson.fromJson(bagian, BagianLunchRequest[].class);
                            adapterUnitBagianLunchRequest = new AdapterUnitBagianLunchRequest(bagianLunchRequests,FormLunchRequestActivity.this);
                            bagianRV.setAdapter(adapterUnitBagianLunchRequest);

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
                        bottomSheet.dismissSheet();
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("idHeadDept", sharedPrefManager.getSpIdHeadDept());
                params.put("idDept", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormLunchRequestActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    public BroadcastReceiver bagianBroadRL = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_bagian = intent.getStringExtra("id_bagian");
            String nama_bagian = intent.getStringExtra("nama_bagian");
            bagianPilihTV.setText(nama_bagian);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void choiceDate(){
        int y, m, d;
        if(!tanggal.isEmpty()){
            y = Integer.parseInt(tanggal.substring(0,4));
            m = Integer.parseInt(tanggal.substring(5,7));
            d = Integer.parseInt(tanggal.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormLunchRequestActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            tanggal = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            Date date2 = null;
            try {
                date = sdf.parse(String.valueOf(tanggal));
                date2 = sdf.parse(getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long pilih = date.getTime();
            long sekarang = date2.getTime();

            if (pilih<sekarang){
                tanggalPilihTV.setText("Pilih Kembali !");
                tanggal = "";

                new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Anda tidak dapat memilih tanggal lampau. Harap pilih kembali!")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
            } else {
                String input_date = tanggal;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                Date dt1= null;
                try {
                    dt1 = format1.parse(input_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEE");
                DateFormat getweek = new SimpleDateFormat("W");
                String finalDay = format2.format(dt1);
                String week = getweek.format(dt1);
                String hariName = "";

                if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                    hariName = "Senin";
                } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                    hariName = "Selasa";
                } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                    hariName = "Rabu";
                } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                    hariName = "Kamis";
                } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                    hariName = "Jumat";
                } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                    hariName = "Sabtu";
                } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                    hariName = "Minggu";
                }

                String dayDate = input_date.substring(8,10);
                String yearDate = input_date.substring(0,4);
                String bulanValue = input_date.substring(5,7);
                String bulanName;

                switch (bulanValue) {
                    case "01":
                        bulanName = "Januari";
                        break;
                    case "02":
                        bulanName = "Februari";
                        break;
                    case "03":
                        bulanName = "Maret";
                        break;
                    case "04":
                        bulanName = "April";
                        break;
                    case "05":
                        bulanName = "Mei";
                        break;
                    case "06":
                        bulanName = "Juni";
                        break;
                    case "07":
                        bulanName = "Juli";
                        break;
                    case "08":
                        bulanName = "Agustus";
                        break;
                    case "09":
                        bulanName = "September";
                        break;
                    case "10":
                        bulanName = "Oktober";
                        break;
                    case "11":
                        bulanName = "November";
                        break;
                    case "12":
                        bulanName = "Desember";
                        break;
                    default:
                        bulanName = "Not found!";
                        break;
                }

                tanggalPilihTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }

        }, y,m-1,d);
        dpd.show();

    }

    @Override
    public void onBackPressed() {
        if (siang1!=0 || siang2!=0 || sore!=0 || malam!=0 || !bagianPilihTV.getText().toString().isEmpty() || !tanggal.isEmpty()){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (permohonanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormLunchRequestActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Apakah anda yakin untuk meninggalkan halaman ini?")
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
                                    siang1 = 0;
                                    siang2 = 0;
                                    sore = 0;
                                    malam = 0;
                                    tanggal = "";
                                    bagianPilihTV.setText("");
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        } else {
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/craete_lunch_request";
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
                            if (status.equals("Success")) {
                                Toast.makeText(FormLunchRequestActivity.this, "Terkirim", Toast.LENGTH_SHORT).show();
//                                pDialog.dismiss();
//                                successPart.setVisibility(View.VISIBLE);
//                                formPart.setVisibility(View.GONE);
                            } else if(status.equals("Available")){
                                Toast.makeText(FormLunchRequestActivity.this, "Sudah tersedia", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(FormLunchRequestActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
//                                successPart.setVisibility(View.GONE);
//                                formPart.setVisibility(View.VISIBLE);
//                                pDialog.setTitleText("Gagal Tersimpan")
//                                        .setContentText("Terjadi kesalahan saat mengirim data")
//                                        .setConfirmText("    OK    ")
//                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
//                        successPart.setVisibility(View.GONE);
//                        formPart.setVisibility(View.VISIBLE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tanggal", tanggal);
                params.put("bagian", bagianPilihTV.getText().toString());
                params.put("siang_k", String.valueOf(siang1));
                params.put("siang_s", String.valueOf(siang2));
                params.put("sore", String.valueOf(sore));
                params.put("malam", String.valueOf(malam));

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

}