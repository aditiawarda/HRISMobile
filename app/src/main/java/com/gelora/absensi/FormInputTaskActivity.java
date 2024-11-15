package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterAllKaryawanPIC;
import com.gelora.absensi.adapter.AdapterStatusTask;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanAll;
import com.gelora.absensi.model.StatusTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormInputTaskActivity extends AppCompatActivity {

    LinearLayout actualStartDateBTN, actualEndDateBTN, actualPart, statusBTN, startDateBTN, endDateBTN, actionBar, backBTN, submitBTN, picBTN, startAttantionPart, noDataPart, loadingDataPart, targetDateBTN;
    TextView actualDurationTV, actualEndDateTV, actualStartDateTV, picTV, targetDateTV, startDateTV, endDateTV, statusTV, persentaseProgressTV;
    EditText taskNameED;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    KAlertDialog pDialog;
    private int i = -1;
    int persentasePregressNumber = 0, persentasePregressNumberBefore = 0;
    String actualStartDate = "", actualEndDate = "", statusIdTaskBefore = "", statusIdTask = "", projectId = "", picNik = "", picName = "", targetDate = "", targetDatePar = "", startDate = "", startDatePar = "", endDate = "", endDatePar = "", actualStartDatePar = "", actualEndDatePar = "";
    EditText keywordKaryawan;
    private RecyclerView karyawanRV, statusTaskRV;
    private KaryawanAll[] karyawanAlls;
    private StatusTask[] statusTasks;
    private AdapterAllKaryawanPIC adapterAllKaryawan;
    private AdapterStatusTask adapterStatusTask;
    IndicatorSeekBar persentaseProgress;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_input_task);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        submitBTN = findViewById(R.id.submit_btn);
        taskNameED = findViewById(R.id.task_name_ed);
        picBTN = findViewById(R.id.pic_btn);
        picTV = findViewById(R.id.pic_tv);
        targetDateBTN = findViewById(R.id.target_date_btn);
        targetDateTV = findViewById(R.id.target_date_tv);
        startDateBTN = findViewById(R.id.start_date_btn);
        startDateTV = findViewById(R.id.start_date_tv);
        endDateBTN = findViewById(R.id.end_date_btn);
        endDateTV = findViewById(R.id.end_date_tv);
        statusBTN = findViewById(R.id.status_btn);
        statusTV = findViewById(R.id.status_tv);
        actualPart = findViewById(R.id.actual_part);
        actualStartDateBTN = findViewById(R.id.actual_start_date_btn);
        actualEndDateBTN = findViewById(R.id.actual_end_date_btn);
        actualStartDateTV = findViewById(R.id.actual_start_date_tv);
        actualEndDateTV = findViewById(R.id.actual_end_date_tv);
        persentaseProgress = findViewById(R.id.persentase_progress);
        persentaseProgressTV = findViewById(R.id.persentase_progress_tv);
        actualDurationTV = findViewById(R.id.actual_duration_tv);

        projectId = getIntent().getExtras().getString("id_project");

        LocalBroadcastManager.getInstance(this).registerReceiver(taskPicBroad, new IntentFilter("task_pic_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(taskStatusBroad, new IntentFilter("task_status_broad"));

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                taskNameED.setText("");
                picTV.setText("");
                picName = "";
                picNik = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
                targetDate = "";
                targetDatePar = "";
                targetDateTV.setText("");
                startDate = "";
                startDatePar = "";
                startDateTV.setText("");
                endDate = "";
                endDatePar = "";
                endDateTV.setText("");
                actualStartDate = "";
                actualEndDate = "";
                actualStartDatePar = "";
                actualEndDatePar = "";
                actualStartDateTV.setText("");
                actualEndDateTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                statusIdTask = "";
                statusIdTaskBefore = "";
                statusTV.setText("");
                actualDurationTV.setText("Pilih tanggal...");
                persentasePregressNumber = 0;
                persentasePregressNumberBefore = 0;
                persentaseProgress.setProgress(0);
                persentaseProgressTV.setText("0%");
                actualPart.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        picBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                picTask();
            }
        });

        targetDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                targetDate();
            }
        });

        statusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                statusPicker();
            }
        });

        startDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                dateMulai();
            }
        });

        endDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                dateAkhir();
            }
        });

        actualStartDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                dateMulaiActual();
            }
        });

        actualEndDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                taskNameED.clearFocus();

                dateAkhirActual();
            }
        });

        persentaseProgress.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                persentasePregressNumber = Math.round(seekParams.progressFloat);
                persentaseProgressTV.setText(String.valueOf(persentasePregressNumber)+"%");
                if(Math.round(seekParams.progressFloat) == 100){
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "5");
                    statusIdTask = "5";
                    statusIdTaskBefore = statusIdTask;
                    statusTV.setText("Done");
                    actualPart.setVisibility(View.VISIBLE);
                } else {
                    if(statusIdTaskBefore.equals("5")){
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                        statusIdTaskBefore = "";
                        statusIdTask = "";
                        statusTV.setText("Pilih kembali !");
                    }
                    actualPart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskNameED.getText().toString().equals("")||picName.equals("")||picNik.equals("")||statusIdTask.equals("")||targetDate.equals("")||startDate.equals("")||endDate.equals("")){
                    new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Pastikan semua data telah terisi!")
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
                    if(statusTV.getText().toString().equals("Done") && (actualStartDate.equals("") || actualEndDate.equals(""))){
                        new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi tanggal aktualisasi task")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else if(statusTV.getText().toString().equals("Pilih kembali !")) {
                        new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap pilih status task")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Simpan task baru sekarang?")
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
                                        pDialog = new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInputTaskActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInputTaskActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInputTaskActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInputTaskActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInputTaskActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormInputTaskActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }

                                            public void onFinish() {
                                                i = -1;
                                                submitData();
                                            }
                                        }.start();
                                    }
                                })
                                .show();
                    }
                }
            }
        });

        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");

    }

    private void statusPicker() {
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInputTaskActivity.this).inflate(R.layout.layout_task_status, bottomSheet, false));
        statusTaskRV = findViewById(R.id.status_task_rv);
        if(statusTaskRV != null){
            statusTaskRV.setLayoutManager(new LinearLayoutManager(this));
            statusTaskRV.setHasFixedSize(true);
            statusTaskRV.setNestedScrollingEnabled(false);
            statusTaskRV.setItemAnimator(new DefaultItemAnimator());
        }
        getStatus();
    }

    private void getStatus() {
        final String url = "https://hrisgelora.co.id/api/task_status";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            String data = response.getString("data");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            statusTasks = gson.fromJson(data, StatusTask[].class);
                            adapterStatusTask = new AdapterStatusTask(statusTasks,FormInputTaskActivity.this);
                            try {
                                statusTaskRV.setAdapter(adapterStatusTask);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Gagal terhubung, harap periksa koneksi internet atau jaringan anda")
                        .setConfirmText("    OK    ")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        requestQueue.add(request);

    }

    public BroadcastReceiver taskStatusBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            statusIdTask = intent.getStringExtra("id_status");
            String namaStatus = intent.getStringExtra("nama_status");
            statusIdTaskBefore = statusIdTask;

            if(statusIdTask.equals("5")){
                persentasePregressNumber = 100;
                persentasePregressNumberBefore = persentasePregressNumber;
                persentaseProgress.setProgress(100);
                persentaseProgressTV.setText(String.valueOf(persentasePregressNumber)+"%");
                actualPart.setVisibility(View.VISIBLE);
            } else {
                if(persentasePregressNumberBefore == 100){
                    persentasePregressNumberBefore = 0;
                    persentasePregressNumber = 0;
                    persentaseProgress.setProgress(0);
                    persentaseProgressTV.setText(String.valueOf(persentasePregressNumber)+"%");
                }
                actualPart.setVisibility(View.GONE);
            }

            statusTV.setText(namaStatus);

            InputMethodManager imm = (InputMethodManager) FormInputTaskActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = FormInputTaskActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(FormInputTaskActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            taskNameED.clearFocus();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void targetDate(){
        int y, m, d;
        if(!targetDate.equals("")){
            y = Integer.parseInt(targetDate.substring(0,4));
            m = Integer.parseInt(targetDate.substring(5,7));
            d = Integer.parseInt(targetDate.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInputTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            targetDate = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
            targetDatePar = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

            String input_date = targetDate;
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

            targetDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

        }, y,m-1,d);
        dpd.show();

    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        int y, m, d;
        if(!startDate.equals("")){
            y = Integer.parseInt(startDate.substring(0,4));
            m = Integer.parseInt(startDate.substring(5,7));
            d = Integer.parseInt(startDate.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInputTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            startDate = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
            startDatePar = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

            if (!endDate.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(startDate));
                    date2 = sdf.parse(String.valueOf(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    String input_date = startDate;
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

                    startDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                } else {
                    startDateTV.setText("Pilih Kembali !");
                    startDate = "";

                    new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
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
                String input_date = startDate;
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

                startDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }

        }, y,m-1,d);
        dpd.show();

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        int y, m, d;
        if(!endDate.equals("")){
            y = Integer.parseInt(endDate.substring(0,4));
            m = Integer.parseInt(endDate.substring(5,7));
            d = Integer.parseInt(endDate.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInputTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            endDate = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
            endDatePar = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

            if (!startDate.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(startDate));
                    date2 = sdf.parse(String.valueOf(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    String input_date = endDate;
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

                    endDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                } else {
                    endDateTV.setText("Pilih Kembali !");
                    endDate = "";

                    new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
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
                String input_date = endDate;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                endDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }

        }, y,m-1,d);
        dpd.show();

    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulaiActual(){
        int y, m, d;
        if(!actualStartDate.equals("")){
            y = Integer.parseInt(actualStartDate.substring(0,4));
            m = Integer.parseInt(actualStartDate.substring(5,7));
            d = Integer.parseInt(actualStartDate.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInputTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            actualStartDate = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
            actualStartDatePar = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

            if (!actualEndDate.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(actualStartDate));
                    date2 = sdf.parse(String.valueOf(actualEndDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    String input_date = actualStartDate;
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

                    actualStartDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    countDuration(actualStartDate, actualEndDate);
                } else {
                    actualDurationTV.setText("-");
                    actualStartDateTV.setText("Pilih Kembali !");
                    actualStartDate = "";

                    new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal mulai aktual tidak bisa lebih besar dari tanggal akhir aktual. Harap ulangi!")
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
                String input_date = actualStartDate;
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

                actualStartDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                actualDurationTV.setText("-");
            }

        }, y,m-1,d);
        dpd.show();

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhirActual(){
        int y, m, d;
        if(!actualEndDate.equals("")){
            y = Integer.parseInt(actualEndDate.substring(0,4));
            m = Integer.parseInt(actualEndDate.substring(5,7));
            d = Integer.parseInt(actualEndDate.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInputTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            actualEndDate = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
            actualEndDatePar = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

            if (!actualStartDate.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(actualStartDate));
                    date2 = sdf.parse(String.valueOf(actualEndDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mulai = date.getTime();
                long akhir = date2.getTime();

                if (mulai<=akhir){
                    String input_date = actualEndDate;
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

                    actualEndDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    countDuration(actualStartDate, actualEndDate);
                } else {
                    actualDurationTV.setText("-");
                    actualEndDateTV.setText("Pilih Kembali !");
                    actualEndDate = "";

                    new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal akhir aktual tidak bisa lebih kecil dari tanggal mulai aktual. Harap ulangi!")
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
                String input_date = actualEndDate;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                actualEndDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                actualDurationTV.setText("-");
            }

        }, y,m-1,d);
        dpd.show();

    }

    @SuppressLint("SetTextI18n")
    public void countDuration(String start, String end){
        String startDateString = start;
        String endDateString = end;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(endDateString);
            date2 = format.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = waktu1 - waktu2;

        long diffDays = (selisih_waktu / (24 * 60 * 60 * 1000)) + 1;

        long years = (diffDays / 365);
        long months = (diffDays - (years * 365)) / 30;
        long days = (diffDays - ((years * 365) + (months * 30)));

        // Print the resulting duration
        if (years == 0){
            if(months == 0){
                if(days == 0){
                    actualDurationTV.setText("-");
                } else {
                    actualDurationTV.setText(days +" Hari");
                }
            } else {
                if(days == 0){
                    actualDurationTV.setText(months + " Bulan");
                } else {
                    actualDurationTV.setText(months + " Bulan " + days + " Hari");
                }
            }
        } else {
            if(months == 0){
                if(days == 0){
                    actualDurationTV.setText(years + " Tahun");
                } else {
                    actualDurationTV.setText(years + " Tahun " + days + " Hari");
                }
            } else {
                if(days == 0){
                    actualDurationTV.setText(years + " Tahun " + months + " Bulan");
                } else {
                    actualDurationTV.setText(years + " Tahun " + months + " Bulan " + days + " Hari");
                }
            }
        }

    }

    public BroadcastReceiver taskPicBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            picNik = intent.getStringExtra("nik_pic");
            picName = intent.getStringExtra("nama_pic");

            picTV.setText(picName);

            InputMethodManager imm = (InputMethodManager) FormInputTaskActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = FormInputTaskActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(FormInputTaskActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            taskNameED.clearFocus();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    private void picTask() {
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInputTaskActivity.this).inflate(R.layout.layout_karyawan_pic_task, bottomSheet, false));
        keywordKaryawan = findViewById(R.id.keyword_user_ed);
        keywordKaryawan.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        karyawanRV = findViewById(R.id.karyawan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);

        karyawanRV.setLayoutManager(new LinearLayoutManager(this));
        karyawanRV.setHasFixedSize(true);
        karyawanRV.setNestedScrollingEnabled(false);
        karyawanRV.setItemAnimator(new DefaultItemAnimator());

        keywordKaryawan.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                taskNameED.clearFocus();

                String keyWordSearch = keywordKaryawan.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                karyawanRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAllUser(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

    }

    private void getAllUser(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_all_data_user_by_keyword";
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
                            if (status.equals("Success")) {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                                karyawanRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                karyawanAlls = gson.fromJson(data_list, KaryawanAll[].class);
                                adapterAllKaryawan = new AdapterAllKaryawanPIC(karyawanAlls, FormInputTaskActivity.this);
                                karyawanRV.setAdapter(adapterAllKaryawan);
                            } else {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
                                karyawanRV.setVisibility(View.GONE);
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

                        startAttantionPart.setVisibility(View.GONE);
                        loadingDataPart.setVisibility(View.GONE);
                        noDataPart.setVisibility(View.VISIBLE);
                        karyawanRV.setVisibility(View.GONE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void submitData() {
        String URL = "https://hrisgelora.co.id/api/create_task_timeline";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("id_project", projectId);
            requestBody.put("taskname", taskNameED.getText().toString());
            requestBody.put("pic", picNik+"-"+picName);
            requestBody.put("date", targetDatePar);
            requestBody.put("status", statusIdTask);
            requestBody.put("timeline", startDatePar+" - "+endDatePar);
            requestBody.put("scheduleTimeline", startDatePar+" - "+endDatePar);
            if(statusIdTask.equals("5")){
                requestBody.put("actualTimeline", actualStartDatePar+" - "+actualEndDatePar);
            } else {
                requestBody.put("actualTimeline", "00/00/0000 - 00/00/0000");
            }
            requestBody.put("progress", String.valueOf(persentasePregressNumber));
            requestBody.put("progressDate", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            JSONObject data = new JSONObject(response.toString());
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                taskNameED.setText("");
                                picTV.setText("");
                                picName = "";
                                picNik = "";
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
                                targetDate = "";
                                targetDateTV.setText("");
                                startDate = "";
                                startDatePar = "";
                                startDateTV.setText("");
                                endDate = "";
                                endDatePar = "";
                                endDateTV.setText("");
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                                statusIdTask = "";
                                statusIdTaskBefore = "";
                                statusTV.setText("");
                                persentasePregressNumber = 0;
                                persentasePregressNumberBefore = 0;
                                persentaseProgress.setProgress(0);

                                pDialog.setTitleText("Berhasil")
                                        .setContentText("Task berhasil ditambahkan")
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
                                pDialog.setTitleText("Gagal Tersimpan")
                                        .setContentText("Terjadi kesalahan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                });

        requestQueue.add(jsonObjectRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);

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
        CookieBar.build(FormInputTaskActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!taskNameED.getText().toString().equals("")||!picName.equals("")||!targetDate.equals("")||!startDate.equals("")||!endDate.equals("")||!statusIdTask.equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                new KAlertDialog(FormInputTaskActivity.this, KAlertDialog.WARNING_TYPE)
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
                                taskNameED.setText("");
                                picTV.setText("");
                                picName = "";
                                picNik = "";
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
                                targetDate = "";
                                targetDatePar = "";
                                targetDateTV.setText("");
                                startDate = "";
                                startDatePar = "";
                                startDateTV.setText("");
                                endDate = "";
                                endDatePar = "";
                                endDateTV.setText("");
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                                statusIdTask = "";
                                statusIdTaskBefore = "";
                                statusTV.setText("");
                                persentasePregressNumber = 0;
                                persentasePregressNumberBefore = 0;
                                persentaseProgress.setProgress(0);
                                onBackPressed();
                            }
                        })
                        .show();
            }
        } else {
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}