package com.gelora.absensi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.takisoft.datetimepicker.DatePickerDialog;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FormPermohonanIzinActivity extends AppCompatActivity {

    LinearLayout actionBar, viewUploadBTN, markUpload, uploadBTN, uploadFilePart, markStatusSakit, markStatusIzin, izinBTN, sakitBTN, tipeChoiceBTN, viewBTN, formPart, successPart, submitBTN, backBTN, dateMulaiPicker, dateAkhirPicker;
    TextView jumlahHariTV, messageSuccessTV, labelUnggahTV, statusUploadTV, tipeChoiceTV, mulaiDateTV, akhirDateTV, namaTV, nikTV, detailTV;
    String permohonanTerkirim = "0", uploadStatus = "", idIzin = "", tipeIzin = "", dateChoiceMulai = "", dateChoiceAkhir = "", alasanIzin = "";
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    EditText alasanED;
    BottomSheetLayout bottomSheet;
    ImageView successGif;
    View rootview;
    KAlertDialog pDialog;
    private int i = -1;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    RequestQueue requestQueue;
    private Handler handler = new Handler();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_permohonan_izin);

        sharedPrefManager = new SharedPrefManager(this);
        requestQueue = Volley.newRequestQueue(this);
        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        dateMulaiPicker = findViewById(R.id.mulai_date);
        dateAkhirPicker = findViewById(R.id.akhir_date);
        mulaiDateTV = findViewById(R.id.mulai_date_pilih);
        akhirDateTV = findViewById(R.id.akhir_date_pilih);
        namaTV = findViewById(R.id.nama_karyawan_tv);
        nikTV = findViewById(R.id.nik_karyawan_tv);
        detailTV = findViewById(R.id.detail_karyawan_tv);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        alasanED = findViewById(R.id.alasan_tv);
        submitBTN = findViewById(R.id.submit_btn);
        successGif = findViewById(R.id.success_gif);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        viewBTN = findViewById(R.id.view_permohonan_btn);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        tipeChoiceBTN = findViewById(R.id.tipe_choice_btn);
        tipeChoiceTV = findViewById(R.id.tipe_choice_tv);
        uploadFilePart = findViewById(R.id.upload_file_part);
        uploadBTN = findViewById(R.id.upload_btn);
        markUpload = findViewById(R.id.mark_upload);
        statusUploadTV = findViewById(R.id.status_upload_tv);
        labelUnggahTV = findViewById(R.id.label_unggah);
        viewUploadBTN = findViewById(R.id.view_btn);
        messageSuccessTV = findViewById(R.id.message_tv);
        jumlahHariTV = findViewById(R.id.jumlah_hari_tv);
        actionBar = findViewById(R.id.action_bar);

        alasanED.setFilters(new InputFilter[]{new FormPermohonanIzinActivity.EmojiExcludeFilter()});
        alasanED.clearComposingText();
        alasanED.clearFocus();
        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        if(sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("90")){
            messageSuccessTV.setText("Permohonan anda telah terkirim dan disampaikan kepada bagian HRD untuk persetujuan.");
        } else if(sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
            messageSuccessTV.setText("Permohonan anda telah terkirim dan disampaikan kepada Kepala Departemen untuk persetujuan.");
        } else {
            messageSuccessTV.setText("Permohonan anda telah terkirim dan disampaikan kepada Supervisor untuk persetujuan.");
        }

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        tipeIzin = "";
                        dateChoiceMulai = "";
                        dateChoiceAkhir = "";
                        alasanIzin = "";
                        uploadStatus = "";
                        mulaiDateTV.setText("");
                        akhirDateTV.setText("");
                        tipeChoiceTV.setText("");
                        alasanED.setText("");
                        uploadFilePart.setVisibility(View.GONE);
                        markUpload.setVisibility(View.GONE);
                        viewUploadBTN.setVisibility(View.GONE);
                        statusUploadTV.setText("Unggah Surat Sakit");
                        labelUnggahTV.setText("Unggah");
                        jumlahHariTV.setText("Tentukan Tanggal...");
                        alasanED.clearFocus();

                        getDataKaryawan();
                    }
                }, 800);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormPermohonanIzinActivity.this, DetailPermohonanIzinActivity.class);
                intent.putExtra("kode", "form");
                intent.putExtra("id_izin", idIzin);
                startActivity(intent);
            }
        });

        tipeChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                tipeChoice();
            }
        });

        dateMulaiPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                dateMulai();
            }
        });

        dateAkhirPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                alasanED.clearFocus();
                dateAkhir();
            }
        });

        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionIzin();
            }
        });

        getDataKaryawan();

    }

    private void getDataKaryawan(){
        namaTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        nikTV.setText(sharedPrefManager.getSpNik());
        getDataKaryawanDetail();
    }

    private void getDataKaryawanDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/data_karyawan_personal";
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
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                detailTV.setText(jabatan+" | "+bagian+" | "+department);
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
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        int y, m, d;
        if(!dateChoiceMulai.equals("")){
            y = Integer.parseInt(dateChoiceMulai.substring(0,4));
            m = Integer.parseInt(dateChoiceMulai.substring(5,7));
            d = Integer.parseInt(dateChoiceMulai.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormPermohonanIzinActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            dateChoiceMulai = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            if (!dateChoiceAkhir.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(dateChoiceMulai));
                    date2 = sdf.parse(String.valueOf(dateChoiceAkhir));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    String input_date = dateChoiceMulai;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
                    String finalDay = format2.format(dt1);
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

                    mulaiDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    if(!dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                        dayCalculate();
                    }

                } else {
                    mulaiDateTV.setText("Pilih Kembali !");
                    dateChoiceMulai = "";
                    jumlahHariTV.setText("Tentukan Tanggal...");

                    new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal mulai tidak bisa lebih besar dari tanggal akhir. Harap ulangi!")
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
                String input_date = dateChoiceMulai;
                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                Date dt1= null;
                try {
                    dt1 = format1.parse(input_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEE");
                String finalDay = format2.format(dt1);
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

                mulaiDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }

        }, y,m-1,d);
        dpd.show();

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        int y, m, d;
        if(!dateChoiceAkhir.equals("")){
            y = Integer.parseInt(dateChoiceAkhir.substring(0,4));
            m = Integer.parseInt(dateChoiceAkhir.substring(5,7));
            d = Integer.parseInt(dateChoiceAkhir.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormPermohonanIzinActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            dateChoiceAkhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            if (!dateChoiceMulai.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(dateChoiceMulai));
                    date2 = sdf.parse(String.valueOf(dateChoiceAkhir));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    String input_date = dateChoiceAkhir;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
                    String finalDay = format2.format(dt1);
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

                    akhirDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    if(!dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                        dayCalculate();
                    }

                } else {
                    akhirDateTV.setText("Pilih Kembali !");
                    dateChoiceAkhir = "";
                    jumlahHariTV.setText("Tentukan Tanggal...");

                    new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal akhir tidak bisa lebih kecil dari tanggal mulai. Harap ulangi!")
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
                String input_date = dateChoiceAkhir;
                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                Date dt1= null;
                try {
                    dt1 = format1.parse(input_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEE");
                String finalDay = format2.format(dt1);
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

                akhirDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }

        }, y,m-1,d);
        dpd.show();

    }

    private void actionIzin(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        alasanED.clearFocus();
        alasanIzin = alasanED.getText().toString();
        if (!tipeIzin.equals("")){
            if(tipeIzin.equals("5")){
                if (!dateChoiceMulai.equals("")){
                    if(!dateChoiceAkhir.equals("")){
                        if(!alasanIzin.equals("")){
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Kirim permohonan sekarang?")
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
                                                pDialog = new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormPermohonanIzinActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormPermohonanIzinActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormPermohonanIzinActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormPermohonanIzinActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormPermohonanIzinActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (FormPermohonanIzinActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;
                                                        checkSignature();
                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap unggah surat sakit!")
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
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi alasan!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi alasan dan unggah surat sakit!")
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
                    } else {
                        if(!alasanIzin.equals("")){
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal akhir!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal akhir dan unggah surat sakit!")
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
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal akhir dan alasan!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal akhir, alasan dan unggah surat sakit!")
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
                else {
                    if(!dateChoiceAkhir.equals("")){
                        if(!alasanIzin.equals("")){
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai dan unggah surat sakit!")
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
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai dan alasan!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai, alasan dan unggah surat sakit!")
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
                    } else {
                        if(!alasanIzin.equals("")){
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai dan tanggal akhir!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai, tanggal akhir dan unggah surat sakit!")
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
                            if(!uploadStatus.equals("")){
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai, tanggal akhir dan alasan!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi tanggal mulai, tanggal akhir, alasan dan unggah surat sakit!")
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
            }
            else {
                if (!dateChoiceMulai.equals("")){
                    if(!dateChoiceAkhir.equals("")){
                        if(!alasanIzin.equals("")){
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim permohonan sekarang?")
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
                                            pDialog = new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormPermohonanIzinActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormPermohonanIzinActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormPermohonanIzinActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormPermohonanIzinActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormPermohonanIzinActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormPermohonanIzinActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    checkSignature();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();

                        }
                        else {
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi alasan!")
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
                        if(!alasanIzin.equals("")){
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal akhir!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal akhir dan alasan!")
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
                else {
                    if(!dateChoiceAkhir.equals("")){
                        if(!alasanIzin.equals("")){
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal mulai!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal mulai dan alasan!")
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
                        if(!alasanIzin.equals("")){
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal mulai dan tanggal akhir!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi tanggal mulai, tanggal akhir dan alasan!")
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
        } else {
            if (!dateChoiceMulai.equals("")){
                if(!dateChoiceAkhir.equals("")){
                    if(!alasanIzin.equals("")){
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    else {
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin dan alasan!")
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
                    if(!alasanIzin.equals("")){
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin dan tanggal akhir!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    else {
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin, tanggal akhir dan alasan!")
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
            else {
                if(!dateChoiceAkhir.equals("")){
                    if(!alasanIzin.equals("")){
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin dan tanggal mulai!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    else {
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin, tanggal mulai dan alasan!")
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
                    if(!alasanIzin.equals("")){
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tipe izin, tanggal mulai dan tanggal akhir!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    else {
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi semua data!")
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

    }

    private void dayCalculate(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/total_hari";
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

                            if (status.equals("Success")){
                                String jumlah_hari = data.getString("jumlah_hari");
                                jumlahHariTV.setText(jumlah_hari+" Hari");
                            } else {
                                jumlahHariTV.setText("Tentukan Tanggal...");
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

                if(tipeIzin.equals("")){
                    params.put("tipe_izin", "4");
                } else {
                    params.put("tipe_izin", tipeIzin);
                }

                params.put("tanggal_mulai", dateChoiceMulai);
                params.put("tanggal_akhir", dateChoiceAkhir);

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void submitIzin(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/izin_input";
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
                                JSONObject data_izin = data.getJSONObject("data");
                                String id = data_izin.getString("id");
                                idIzin = id;
                                if(tipeIzin.equals("5")){
                                    uploadSuratSakit();
                                } else {
                                    permohonanTerkirim = "1";
                                    pDialog.dismiss();
                                    successPart.setVisibility(View.VISIBLE);
                                    formPart.setVisibility(View.GONE);
                                }
                            } else if(status.equals("Available")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Permohonan serupa sudah anda ajukan sebelumnya, harap periksa notifikasi permohonan izin anda")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Available Checkin")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Anda telah melakukan check in pada tanggal yang dipilih, harap periksa kembali")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Limit Stock")){
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Maaf, jumlah hari dari permohonan yang anda ajukan melebihi sisa cuti anda saat ini")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Over Day")){
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Permohonan izin/sakit tidak dapat diajukan lebih dari 2 hari dari tanggal yang dipilih")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Terkirim")
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
                        successPart.setVisibility(View.GONE);
                        formPart.setVisibility(View.VISIBLE);
                        pDialog.setTitleText("Gagal Terkirim")
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("tipe_izin", tipeIzin);
                params.put("tanggal", getDate());
                params.put("time", getTime());
                params.put("tanggal_mulai", dateChoiceMulai);
                params.put("tanggal_akhir", dateChoiceAkhir);
                params.put("keterangan", alasanIzin);
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    private void connectionFailed(){
        CookieBar.build(FormPermohonanIzinActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void checkSignature(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cek_ttd_digital";
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

                            if (status.equals("Available")){
                                String signature = data.getString("data");
                                String url = "https://hrisgelora.co.id/upload/digital_signature/"+signature;
                                submitIzin();
                            } else {
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Anda belum mengisi tanda tangan digital. Harap isi terlebih dahulu")
                                        .setCancelText(" BATAL ")
                                        .setConfirmText("LANJUT")
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
                                                Intent intent = new Intent(FormPermohonanIzinActivity.this, DigitalSignatureActivity.class);
                                                intent.putExtra("kode", "form");
                                                startActivity(intent);
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.WARNING_TYPE);
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

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
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

    private void tipeChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormPermohonanIzinActivity.this).inflate(R.layout.layout_list_tipe_izin, bottomSheet, false));
        izinBTN = findViewById(R.id.izin_btn);
        sakitBTN = findViewById(R.id.sakit_btn);
        markStatusSakit = findViewById(R.id.mark_status_sakit);
        markStatusIzin = findViewById(R.id.mark_status_izin);

        if (tipeIzin.equals("4")){
            izinBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option_choice));
            sakitBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option));
            markStatusIzin.setVisibility(View.VISIBLE);
            markStatusSakit.setVisibility(View.GONE);
        } else if (tipeIzin.equals("5")){
            izinBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option));
            sakitBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option_choice));
            markStatusIzin.setVisibility(View.GONE);
            markStatusSakit.setVisibility(View.VISIBLE);
        } else {
            izinBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option));
            sakitBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option));
            markStatusIzin.setVisibility(View.GONE);
            markStatusSakit.setVisibility(View.GONE);
        }

        izinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markStatusIzin.setVisibility(View.VISIBLE);
                markStatusSakit.setVisibility(View.GONE);
                izinBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option_choice));
                sakitBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option));
                tipeIzin = "4";
                tipeChoiceTV.setText("Izin");
                uploadFilePart.setVisibility(View.GONE);

                if(!tipeIzin.equals("") && !dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                    dayCalculate();
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sakitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markStatusIzin.setVisibility(View.GONE);
                markStatusSakit.setVisibility(View.VISIBLE);
                sakitBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option_choice));
                izinBTN.setBackground(ContextCompat.getDrawable(FormPermohonanIzinActivity.this, R.drawable.shape_option));
                tipeIzin = "5";
                tipeChoiceTV.setText("Sakit");
                uploadFilePart.setVisibility(View.VISIBLE);

                if(!tipeIzin.equals("") && !dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                    dayCalculate();
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(FormPermohonanIzinActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withActivity(FormPermohonanIzinActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, "surat");
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(FormPermohonanIzinActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(FormPermohonanIzinActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 600);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 900);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(FormPermohonanIzinActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        String file_directori = getRealPathFromURIPath(uri, FormPermohonanIzinActivity.this);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        @SuppressLint("Recycle")
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                try {
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, FormPermohonanIzinActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.d("PaRSE JSON", a);
                        markUpload.setVisibility(View.VISIBLE);
                        viewUploadBTN.setVisibility(View.VISIBLE);
                        statusUploadTV.setText("Berhasil diunggah");
                        labelUnggahTV.setText("Ganti");
                        uploadStatus = "1";

                        viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FormPermohonanIzinActivity.this, ViewImageActivity.class);
                                intent.putExtra("url", String.valueOf(uri));
                                intent.putExtra("kode", "form");
                                intent.putExtra("jenis_form", "izin");
                                startActivity(intent);
                            }
                        });

                    } else if(extension.equals(".png")||extension.equals(".PNG")){
                        String pngImagePath = FilePathimage.getPath(this, uri);
                        new ConvertImageTask().execute(pngImagePath);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Format file tidak sesuai!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }, 800);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadSuratSakit() {
        String UPLOAD_URL = "https://hrisgelora.co.id/api/upload_surat_sakit";
        String path1 = FilePathimage.getPath(this, uri);
        if (path1 == null) {
            Toast.makeText(this, "Please move your .jpg file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                permohonanTerkirim = "1";
                pDialog.dismiss();
                successPart.setVisibility(View.VISIBLE);
                formPart.setVisibility(View.GONE);

                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path1, "file") //Adding file
                    .addParameter("id_izin_record", idIzin)
                    .addParameter("NIK", sharedPrefManager.getSpNik())
                    .addParameter("current_time", getDate().substring(0,4)+getDate().substring(5,7)+getDate().substring(8,10))//Adding text parameter to the request
                    .setMaxRetries(1)
                    .startUpload();
            }
            catch (Exception exc) {
                Log.e("PaRSE JSON", exc.toString());
                pDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ConvertImageTask extends AsyncTask<String, Void, Uri> {
        @Override
        protected Uri doInBackground(String... params) {
            String imagePath = params[0];
            try {
                return convertPNGtoJPEG(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Uri resultUri) {
            if (resultUri != null) {
                uri = resultUri;
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    String file_directori = getRealPathFromURIPath(uri, FormPermohonanIzinActivity.this);
                    String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                    Log.d("PaRSE JSON", a);
                    markUpload.setVisibility(View.VISIBLE);
                    viewUploadBTN.setVisibility(View.VISIBLE);
                    statusUploadTV.setText("Berhasil diunggah");
                    labelUnggahTV.setText("Ganti");
                    uploadStatus = "1";

                    viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FormPermohonanIzinActivity.this, ViewImageActivity.class);
                            intent.putExtra("url", String.valueOf(uri));
                            intent.putExtra("kode", "form");
                            intent.putExtra("jenis_form", "izin");
                            startActivity(intent);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                }, 800);
            }
        }

        private Uri convertPNGtoJPEG(String inputFilePath) throws IOException {
            Bitmap pngImage = BitmapFactory.decodeFile(inputFilePath);
            File jpgFile = new File(getExternalFilesDir(null), "output.jpg");
            FileOutputStream out = new FileOutputStream(jpgFile);
            pngImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return Uri.fromFile(jpgFile);
        }
    }

    @Override
    public void onBackPressed() {
        if (!tipeIzin.equals("") || !dateChoiceMulai.equals("") || !dateChoiceAkhir.equals("") || !alasanIzin.equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (permohonanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    tipeIzin = "";
                                    dateChoiceMulai = "";
                                    dateChoiceAkhir = "";
                                    alasanIzin = "";
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

    public class EmojiExcludeFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}