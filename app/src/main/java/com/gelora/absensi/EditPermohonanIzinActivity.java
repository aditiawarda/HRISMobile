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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditPermohonanIzinActivity extends AppCompatActivity {

    LinearLayout actionBar, submitBTN, dateMulaiPicker, dateAkhirPicker, viewUploadBTN, markUpload, uploadBTN, backBTN, tipeChoiceBTN, izinBTN, sakitBTN, markStatusSakit, markStatusIzin, uploadFilePart;
    TextView jumlahHariTV, akhirDateTV, mulaiDateTV, namaKaryawanTV, nikKaryawanTV, detailKaryawanTV, tipeChoiceTV, statusUploadTV, labelUnggahTV;
    String uploadGanti = "", permohonanTerkirim = "0", alasanIzin = "", idRecord, tipeIzin = "", uploadStatus = "", dateChoiceMulai = "", dateChoiceAkhir = "";
    EditText alasanED;
    SharedPrefManager sharedPrefManager;
    BottomSheetLayout bottomSheet;
    SwipeRefreshLayout refreshLayout;
    KAlertDialog pDialog;
    private int i = -1;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_permohonan_izin);

        sharedPrefManager = new SharedPrefManager(this);
        requestQueue = Volley.newRequestQueue(this);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        detailKaryawanTV = findViewById(R.id.detail_karyawan_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        uploadFilePart = findViewById(R.id.upload_file_part);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        viewUploadBTN = findViewById(R.id.view_btn);
        dateMulaiPicker = findViewById(R.id.mulai_date);
        dateAkhirPicker = findViewById(R.id.akhir_date);
        mulaiDateTV = findViewById(R.id.mulai_date_pilih);
        akhirDateTV = findViewById(R.id.akhir_date_pilih);
        markUpload = findViewById(R.id.mark_upload);
        statusUploadTV = findViewById(R.id.status_upload_tv);
        labelUnggahTV = findViewById(R.id.label_unggah);
        uploadBTN = findViewById(R.id.upload_btn);
        jumlahHariTV = findViewById(R.id.jumlah_hari_tv);
        alasanED = findViewById(R.id.alasan_tv);
        submitBTN = findViewById(R.id.submit_btn);
        actionBar = findViewById(R.id.action_bar);

        tipeChoiceBTN = findViewById(R.id.tipe_choice_btn);
        tipeChoiceTV = findViewById(R.id.tipe_choice_tv);

        idRecord = getIntent().getExtras().getString("id_record");

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataPermohonan();
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

        getDataPermohonan();

    }

    private void getDataPermohonan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_permohonan_izin_detail";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {
                                JSONObject detail = data.getJSONObject("data");
                                String nik_karyawan = detail.getString("NIK");
                                String nama_karyawan = detail.getString("NmKaryawan");
                                String bagian = detail.getString("NmDept");
                                String departemen = detail.getString("NmHeadDept");
                                String jabatan = detail.getString("NmJabatan");
                                String alasan = detail.getString("keterangan");
                                String tgl_mulai = detail.getString("tanggal_mulai");
                                String tgl_akhir = detail.getString("tanggal_akhir");
                                String tgl_permohonan = detail.getString("tanggal");
                                String digital_signature = detail.getString("digital_signature");
                                String tipe_izin = detail.getString("tipe_izin");
                                String nik_approver_hrd = detail.getString("nik_approver_hrd");
                                String nama_approver_hrd = detail.getString("nama_approver_hrd");

                                namaKaryawanTV.setText(nama_karyawan.toUpperCase());
                                nikKaryawanTV.setText(nik_karyawan);
                                detailKaryawanTV.setText(jabatan+" | "+bagian+" | "+departemen);

                                tipeIzin = tipe_izin;
                                if(tipe_izin.equals("4")){
                                    tipeChoiceTV.setText("Izin");
                                    uploadFilePart.setVisibility(View.GONE);
                                } else if(tipe_izin.equals("5")){
                                    tipeChoiceTV.setText("Sakit");
                                    uploadFilePart.setVisibility(View.VISIBLE);

                                    String foto_surat_sakit = detail.getString("foto_surat_sakit");
                                    String url_surat_sakit = "https://geloraaksara.co.id/absen-online/upload/surat_sakit/"+foto_surat_sakit;

                                    markUpload.setVisibility(View.VISIBLE);
                                    viewUploadBTN.setVisibility(View.VISIBLE);
                                    statusUploadTV.setText("Diunggah");
                                    labelUnggahTV.setText("Ganti");

                                    uploadStatus = "1";

                                    viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(EditPermohonanIzinActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", url_surat_sakit);
                                            intent.putExtra("kode", "detail");
                                            intent.putExtra("jenis_detail", "izin");
                                            startActivity(intent);
                                        }
                                    });

                                }

                                dateChoiceMulai = tgl_mulai;
                                dateChoiceAkhir = tgl_akhir;

                                String input_date = dateChoiceMulai;
                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt1 = null;
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
                                String yearDate = input_date.substring(0,4);;
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

                                String input_date_akhir = dateChoiceAkhir;
                                SimpleDateFormat format1_akhir = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt1_akhir = null;
                                try {
                                    dt1_akhir = format1_akhir.parse(input_date_akhir);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                DateFormat format2_akhir = new SimpleDateFormat("EEE");
                                String finalDay_akhir = format2_akhir.format(dt1_akhir);
                                String hariName_akhir = "";

                                if (finalDay_akhir.equals("Mon") || finalDay_akhir.equals("Sen")) {
                                    hariName_akhir = "Senin";
                                } else if (finalDay_akhir.equals("Tue") || finalDay_akhir.equals("Sel")) {
                                    hariName_akhir = "Selasa";
                                } else if (finalDay_akhir.equals("Wed") || finalDay_akhir.equals("Rab")) {
                                    hariName_akhir = "Rabu";
                                } else if (finalDay_akhir.equals("Thu") || finalDay_akhir.equals("Kam")) {
                                    hariName_akhir = "Kamis";
                                } else if (finalDay_akhir.equals("Fri") || finalDay_akhir.equals("Jum")) {
                                    hariName_akhir = "Jumat";
                                } else if (finalDay_akhir.equals("Sat") || finalDay_akhir.equals("Sab")) {
                                    hariName_akhir = "Sabtu";
                                } else if (finalDay_akhir.equals("Sun") || finalDay_akhir.equals("Min")) {
                                    hariName_akhir = "Minggu";
                                }

                                String dayDate_akhir = input_date_akhir.substring(8,10);
                                String yearDate_akhir = input_date_akhir.substring(0,4);;
                                String bulanValue_akhir = input_date_akhir.substring(5,7);
                                String bulanName_akhir;

                                switch (bulanValue_akhir) {
                                    case "01":
                                        bulanName_akhir = "Januari";
                                        break;
                                    case "02":
                                        bulanName_akhir = "Februari";
                                        break;
                                    case "03":
                                        bulanName_akhir = "Maret";
                                        break;
                                    case "04":
                                        bulanName_akhir = "April";
                                        break;
                                    case "05":
                                        bulanName_akhir = "Mei";
                                        break;
                                    case "06":
                                        bulanName_akhir = "Juni";
                                        break;
                                    case "07":
                                        bulanName_akhir = "Juli";
                                        break;
                                    case "08":
                                        bulanName_akhir = "Agustus";
                                        break;
                                    case "09":
                                        bulanName_akhir = "September";
                                        break;
                                    case "10":
                                        bulanName_akhir = "Oktober";
                                        break;
                                    case "11":
                                        bulanName_akhir = "November";
                                        break;
                                    case "12":
                                        bulanName_akhir = "Desember";
                                        break;
                                    default:
                                        bulanName_akhir = "Not found!";
                                        break;
                                }

                                akhirDateTV.setText(hariName_akhir+", "+String.valueOf(Integer.parseInt(dayDate_akhir))+" "+bulanName_akhir+" "+yearDate_akhir);

                                String jumlah_hari = data.getString("jumlah_hari");
                                jumlahHariTV.setText(jumlah_hari+" Hari");

                                alasanIzin = alasan;
                                alasanED.setText(alasan);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_izin_record", idRecord);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void tipeChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(EditPermohonanIzinActivity.this).inflate(R.layout.layout_list_tipe_izin, bottomSheet, false));
        izinBTN = findViewById(R.id.izin_btn);
        sakitBTN = findViewById(R.id.sakit_btn);
        markStatusSakit = findViewById(R.id.mark_status_sakit);
        markStatusIzin = findViewById(R.id.mark_status_izin);

        if (tipeIzin.equals("4")){
            izinBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option_choice));
            sakitBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option));
            markStatusIzin.setVisibility(View.VISIBLE);
            markStatusSakit.setVisibility(View.GONE);
        } else if (tipeIzin.equals("5")){
            izinBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option));
            sakitBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option_choice));
            markStatusIzin.setVisibility(View.GONE);
            markStatusSakit.setVisibility(View.VISIBLE);
        } else {
            izinBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option));
            sakitBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option));
            markStatusIzin.setVisibility(View.GONE);
            markStatusSakit.setVisibility(View.GONE);
        }

        izinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markStatusIzin.setVisibility(View.VISIBLE);
                markStatusSakit.setVisibility(View.GONE);
                izinBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option_choice));
                sakitBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option));
                tipeIzin = "4";
                tipeChoiceTV.setText("Izin");
                uploadFilePart.setVisibility(View.GONE);

                if(!tipeIzin.equals("") && !dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                    dayCalculate();
                }

                new Handler().postDelayed(new Runnable() {
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
                sakitBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option_choice));
                izinBTN.setBackground(ContextCompat.getDrawable(EditPermohonanIzinActivity.this, R.drawable.shape_option));
                tipeIzin = "5";
                tipeChoiceTV.setText("Sakit");
                uploadFilePart.setVisibility(View.VISIBLE);

                if(!tipeIzin.equals("") && !dateChoiceMulai.equals("") && !dateChoiceAkhir.equals("")){
                    dayCalculate();
                }

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
    private void dateMulai(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanIzinActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

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
                        String yearDate = input_date.substring(0,4);;
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
                        mulaiDateTV.setText("Pilih kembali !");
                        dateChoiceMulai = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String yearDate = input_date.substring(0,4);;
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

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanIzinActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

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
                        String yearDate = input_date.substring(0,4);;
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
                        mulaiDateTV.setText("Pilih kembali !");
                        dateChoiceMulai = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String yearDate = input_date.substring(0,4);;
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

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanIzinActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

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
                        String yearDate = input_date.substring(0,4);;
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
                        akhirDateTV.setText("Pilih kembali !");
                        dateChoiceAkhir = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String yearDate = input_date.substring(0,4);;
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

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(EditPermohonanIzinActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

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
                        String yearDate = input_date.substring(0,4);;
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
                        akhirDateTV.setText("Pilih kembali !");
                        dateChoiceAkhir = "";
                        jumlahHariTV.setText("Tentukan Tanggal...");

                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String yearDate = input_date.substring(0,4);;
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

    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(EditPermohonanIzinActivity.this)
                    .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
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
            Dexter.withActivity(EditPermohonanIzinActivity.this)
                    .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(EditPermohonanIzinActivity.this);
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
        Intent intent = new Intent(EditPermohonanIzinActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(EditPermohonanIzinActivity.this, ImagePickerActivity.class);
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
        String file_directori = getRealPathFromURIPath(uri, EditPermohonanIzinActivity.this);
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
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")||extension.equals(".png")||extension.equals(".PNG")){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, EditPermohonanIzinActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);
                        markUpload.setVisibility(View.VISIBLE);
                        viewUploadBTN.setVisibility(View.VISIBLE);
                        statusUploadTV.setText("Berhasil diunggah");
                        labelUnggahTV.setText("Ganti");
                        uploadStatus = "1";
                        uploadGanti = "1";

                        viewUploadBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EditPermohonanIzinActivity.this, ViewImageActivity.class);
                                intent.putExtra("url", String.valueOf(uri));
                                intent.putExtra("kode", "form");
                                intent.putExtra("jenis_form", "izin");
                                startActivity(intent);
                            }
                        });

                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                pDialog = new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (EditPermohonanIzinActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (EditPermohonanIzinActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (EditPermohonanIzinActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (EditPermohonanIzinActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (EditPermohonanIzinActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (EditPermohonanIzinActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;
                                                        submitIzin();
                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }
                            else {
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                                new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
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
                                            pDialog = new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (EditPermohonanIzinActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (EditPermohonanIzinActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (EditPermohonanIzinActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (EditPermohonanIzinActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (EditPermohonanIzinActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (EditPermohonanIzinActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    submitIzin();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();

                        }
                        else {
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                            new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.ERROR_TYPE)
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

    private void submitIzin(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/izin_edit";
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
                                if(tipeIzin.equals("5")){
                                    if(uploadGanti.equals("1")){
                                        uploadSuratSakit();
                                    } else {
                                        permohonanTerkirim = "1";

                                        pDialog.setTitleText("Berhasil Terupdate")
                                                .setContentText("Data permohonan berhasil terupdate")
                                                .setConfirmText("    OK    ")
                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                        onBackPressed();
                                                    }
                                                })
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    }
                                } else {
                                    permohonanTerkirim = "1";

                                    pDialog.setTitleText("Berhasil Terupdate")
                                            .setContentText("Data permohonan berhasil terupdate")
                                            .setConfirmText("    OK    ")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                    onBackPressed();
                                                }
                                            })
                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                                }
                            } else if(status.equals("Available")){
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Permohonan dengan tanggal yang sama sudah pernah anda ajukan, harap ubah tanggal permohonan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Available Checkin")){
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Anda telah melakukan check in pada tanggal yang dipilih, harap periksa kembali")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if (status.equals("Limit Stock")){
                                pDialog.setTitleText("Gagal Terupdate")
                                        .setContentText("Maaf, jumlah hari dari permohonan yang anda ajukan melebihi sisa cuti anda saat ini")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else if(status.equals("Over Day")){
                                pDialog.setTitleText("Gagal Terkirim")
                                        .setContentText("Permohonan izin/sakit tidak dapat diajukan lebih dari 2 hari dari tanggal yang dipilih")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            } else {
                                pDialog.setTitleText("Gagal Terupdate")
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
                        pDialog.setTitleText("Gagal Terupdate")
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
                params.put("id_izin_record", idRecord);
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

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private void dayCalculate(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/total_hari";
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

    public void uploadSuratSakit() {
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/upload_surat_sakit";
        String path1 = FilePathimage.getPath(this, uri);
        if (path1 == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                permohonanTerkirim = "1";

                pDialog.setTitleText("Berhasil Terupdate")
                        .setContentText("Data permohonan berhasil terupdate")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                                onBackPressed();
                            }
                        })
                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path1, "file") //Adding file
                        .addParameter("id_izin_record", idRecord)
                        .addParameter("NIK", sharedPrefManager.getSpNik())
                        .addParameter("current_time", getDate().substring(0,4)+getDate().substring(5,7)+getDate().substring(8,10))//Adding text parameter to the request
                        .setMaxRetries(1)
                        .startUpload();
                
            } catch (Exception exc) {
                Log.e("PaRSE JSON", "Oke");
                pDialog.dismiss();
            }
        }
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

    private void connectionFailed(){
        CookieBar.build(EditPermohonanIzinActivity.this)
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
    public void onBackPressed() {
        if (!tipeIzin.equals("") || !dateChoiceMulai.equals("") || !dateChoiceAkhir.equals("") || !alasanIzin.equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (permohonanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(EditPermohonanIzinActivity.this, KAlertDialog.WARNING_TYPE)
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

}