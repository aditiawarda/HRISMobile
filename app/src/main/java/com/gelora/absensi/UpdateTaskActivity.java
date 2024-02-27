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
import android.icu.util.Calendar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterAllKaryawanPIC;
import com.gelora.absensi.adapter.AdapterAllKaryawanPICUpdate;
import com.gelora.absensi.adapter.AdapterStatusTask;
import com.gelora.absensi.adapter.AdapterStatusTaskUpdate;
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

public class UpdateTaskActivity extends AppCompatActivity {

    LinearLayout deleteBTN, submitBTN, endDateBTN, startDateBTN, loadingDataPart, noDataPart, startAttantionPart, actionBar, backBTN, picBTN, targetDateBTN, statusBTN;
    EditText taskNameED, keywordKaryawan;
    TextView picTV, targetTV, statusTV, startDateTV, endDateTV, persentaseProgressTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    KAlertDialog pDialog;
    private int i = -1;
    ImageView loadingGif;
    private RecyclerView karyawanRV, statusTaskRV;
    private KaryawanAll[] karyawanAlls;
    private StatusTask[] statusTasks;
    private AdapterAllKaryawanPICUpdate adapterAllKaryawan;
    private AdapterStatusTaskUpdate adapterStatusTask;
    IndicatorSeekBar persentaseProgress;

    // Before
    String projectId, taskName, picTask, dateTarget, statusTask, timlineTask, progressTask, startDate, endDate;

    // After
    int persentasePregressNumber = 0, persentasePregressNumberBefore = 0;
    String statusIdTaskBeforeBARU = "", statusIdTaskBARU = "", projectIdBARU = "", picNikBARU = "", picNameBARU = "", targetDateBARU = "", startDateBARU = "", startDateParBARU = "", endDateBARU = "", endDateParBARU = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        taskNameED = findViewById(R.id.task_name_ed);
        picTV = findViewById(R.id.pic_tv);
        targetTV = findViewById(R.id.target_date_tv);
        statusTV = findViewById(R.id.status_tv);
        startDateTV = findViewById(R.id.start_date_tv);
        endDateTV = findViewById(R.id.end_date_tv);
        persentaseProgress = findViewById(R.id.persentase_progress);
        persentaseProgressTV = findViewById(R.id.persentase_progress_tv);
        picBTN = findViewById(R.id.pic_btn);
        targetDateBTN = findViewById(R.id.target_date_btn);
        statusBTN = findViewById(R.id.status_btn);
        startDateBTN = findViewById(R.id.start_date_btn);
        endDateBTN = findViewById(R.id.end_date_btn);
        submitBTN = findViewById(R.id.submit_btn);
        deleteBTN = findViewById(R.id.delete_btn);

        // Data Source (Before)
        projectId    = getIntent().getExtras().getString("id_project");
        taskName     = getIntent().getExtras().getString("taskname");
        picTask      = getIntent().getExtras().getString("pic");
        dateTarget   = getIntent().getExtras().getString("date");
        statusTask   = getIntent().getExtras().getString("status");
        timlineTask  = getIntent().getExtras().getString("timeline");
        progressTask = getIntent().getExtras().getString("progress");
        startDate = timlineTask.substring(6,10)+"-"+timlineTask.substring(0,2)+"-"+timlineTask.substring(3,5);
        endDate = timlineTask.substring(19,23)+"-"+timlineTask.substring(13,15)+"-"+timlineTask.substring(16,18);

        LocalBroadcastManager.getInstance(this).registerReceiver(taskPicBroadUpdate, new IntentFilter("task_pic_broad_update"));
        LocalBroadcastManager.getInstance(this).registerReceiver(taskStatusBroadUpdate, new IntentFilter("task_status_broad_update"));

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
                applyData();
                statusIdTaskBeforeBARU = "";
                statusIdTaskBARU = "";
                projectIdBARU = "";
                picNikBARU = "";
                picNameBARU = "";
                targetDateBARU = "";
                startDateBARU = "";
                startDateParBARU = "";
                endDateBARU = "";
                endDateParBARU = "";
                new Handler().postDelayed(new Runnable() {
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

                picTaskPicker();
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

        persentaseProgress.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                persentasePregressNumber = Math.round(seekParams.progressFloat);
                persentaseProgressTV.setText(String.valueOf(persentasePregressNumber)+"%");
                if(Math.round(seekParams.progressFloat) == 100){
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "5");
                    statusIdTaskBARU = "5";
                    statusIdTaskBeforeBARU = statusIdTaskBARU;
                    statusTV.setText("Done");
                } else {
                    if(statusIdTaskBeforeBARU.equals("5")){
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                        statusIdTaskBeforeBARU = "";
                        statusIdTaskBARU = "";
                        statusTV.setText("Pilih kembali !");
                    }
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
                new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.WARNING_TYPE)
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
                                pDialog = new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien6));
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
        });

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Apakah anda yakin untuk menghapus task ini sekarang?")
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
                                pDialog = new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (UpdateTaskActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        deleteData();
                                    }
                                }.start();
                            }
                        })
                        .show();
            }
        });

        applyData();

    }

    private void submitData() {
        String URL = "https://geloraaksara.co.id/absen-online/api/update_task_timeline";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("id_project", projectId);
            requestBody.put("key1", taskName);
            requestBody.put("key2", picTask);

            if(taskNameED.getText().toString().equals(taskName)){
                requestBody.put("taskname", taskName);
            } else {
                requestBody.put("taskname", taskNameED.getText().toString());
            }

            String picParram = sharedPrefAbsen.getSpIdKaryawanProject()+"-"+picTV.getText().toString();
            if(picParram.equals(picTask)){
                requestBody.put("pic", picTask);
            } else {
                requestBody.put("pic", picNikBARU+"-"+picNameBARU);
            }

            if(targetDateBARU.equals("")||targetDateBARU.equals(dateTarget)){
                requestBody.put("date", dateTarget);
            } else {
                requestBody.put("date", targetDateBARU);
            }

            if(statusIdTaskBARU.equals("")||statusIdTaskBARU.equals(statusTask)){
                requestBody.put("status", statusTask);
            } else {
                requestBody.put("status", statusIdTaskBARU);
            }

            if(startDateBARU.equals("")||startDateBARU.equals(startDate)){
                if(endDateBARU.equals("")||endDateBARU.equals(endDate)){
                    requestBody.put("timeline", timlineTask);
                } else {
                    requestBody.put("timeline", startDate+" - "+endDateParBARU);
                }
            } else {
                if(endDateBARU.equals("")||endDateBARU.equals(endDate)){
                    requestBody.put("timeline", startDateParBARU+" - "+endDate);
                } else {
                    requestBody.put("timeline", startDateParBARU+" - "+endDateParBARU);
                }
            }

            if(persentasePregressNumber == 0 || String.valueOf(persentasePregressNumber).equals(progressTask)){
                requestBody.put("progress", progressTask);
            } else {
                requestBody.put("progress", String.valueOf(persentasePregressNumber));
            }

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
                                picNameBARU = "";
                                picNikBARU = "";
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
                                targetDateBARU = "";
                                targetTV.setText("");
                                startDateBARU = "";
                                startDateTV.setText("");
                                endDateBARU = "";
                                endDateParBARU = "";
                                endDateTV.setText("");
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                                statusIdTaskBARU = "";
                                statusIdTaskBeforeBARU = "";
                                statusTV.setText("");
                                persentasePregressNumber = 0;
                                persentasePregressNumberBefore = 0;
                                persentaseProgress.setProgress(0);
                                persentaseProgressTV.setText("0%");

                                pDialog.setTitleText("Berhasil")
                                        .setContentText("Task berhasil diupdate")
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

    }

    private void deleteData() {
        String URL = "https://geloraaksara.co.id/absen-online/api/delete_task_timeline";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("id_project", projectId);
            requestBody.put("taskname", taskName);
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
                                picNameBARU = "";
                                picNikBARU = "";
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
                                targetDateBARU = "";
                                targetTV.setText("");
                                startDateBARU = "";
                                startDateTV.setText("");
                                endDateBARU = "";
                                endDateParBARU = "";
                                endDateTV.setText("");
                                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
                                statusIdTaskBARU = "";
                                statusIdTaskBeforeBARU = "";
                                statusTV.setText("");
                                persentasePregressNumber = 0;
                                persentasePregressNumberBefore = 0;
                                persentaseProgress.setProgress(0);
                                persentaseProgressTV.setText("0%");

                                pDialog.setTitleText("Dihapus")
                                        .setContentText("Task berhasil dihapus")
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

    }

    @SuppressLint("SetTextI18n")
    private void applyData(){
        taskNameED.setText(taskName);

        String[] namaPIC = picTask.split("-");
        picTV.setText(namaPIC[1]);
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, namaPIC[0]);

        String input_date = dateTarget;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEE");
        @SuppressLint("SimpleDateFormat") DateFormat getweek = new SimpleDateFormat("W");
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

        targetTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

        if(statusTask.equals("5")){
            statusTV.setText("Done");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);
        } else if(statusTask.equals("4")){
            statusTV.setText("On Hold");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);
        } else if(statusTask.equals("3")){
            statusTV.setText("Waiting Approval");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);
        } else if(statusTask.equals("2")){
            statusTV.setText("On Progress");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);
        } else if(statusTask.equals("1")){
            statusTV.setText("Waiting");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);
        } else if(statusTask.equals("0")){
            statusTV.setText("To Do");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);
        } else {
            statusTV.setText("Pilih status task !");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, "");
        }

        String input_date2 = startDate;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format12 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt12 = null;
        try {
            dt12 = format12.parse(input_date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat format22 = new SimpleDateFormat("EEE");
        @SuppressLint("SimpleDateFormat") DateFormat getweek2 = new SimpleDateFormat("W");
        String finalDay2 = format22.format(dt12);
        String week2 = getweek2.format(dt12);
        String hariName2 = "";

        if (finalDay2.equals("Mon") || finalDay2.equals("Sen")) {
            hariName2 = "Senin";
        } else if (finalDay2.equals("Tue") || finalDay2.equals("Sel")) {
            hariName2 = "Selasa";
        } else if (finalDay2.equals("Wed") || finalDay2.equals("Rab")) {
            hariName2 = "Rabu";
        } else if (finalDay2.equals("Thu") || finalDay2.equals("Kam")) {
            hariName2 = "Kamis";
        } else if (finalDay2.equals("Fri") || finalDay2.equals("Jum")) {
            hariName2 = "Jumat";
        } else if (finalDay2.equals("Sat") || finalDay2.equals("Sab")) {
            hariName2 = "Sabtu";
        } else if (finalDay2.equals("Sun") || finalDay2.equals("Min")) {
            hariName2 = "Minggu";
        }

        String dayDate2 = input_date2.substring(8,10);
        String yearDate2 = input_date2.substring(0,4);
        String bulanValue2 = input_date2.substring(5,7);
        String bulanName2;

        switch (bulanValue2) {
            case "01":
                bulanName2 = "Januari";
                break;
            case "02":
                bulanName2 = "Februari";
                break;
            case "03":
                bulanName2 = "Maret";
                break;
            case "04":
                bulanName2 = "April";
                break;
            case "05":
                bulanName2 = "Mei";
                break;
            case "06":
                bulanName2 = "Juni";
                break;
            case "07":
                bulanName2 = "Juli";
                break;
            case "08":
                bulanName2 = "Agustus";
                break;
            case "09":
                bulanName2 = "September";
                break;
            case "10":
                bulanName2 = "Oktober";
                break;
            case "11":
                bulanName2 = "November";
                break;
            case "12":
                bulanName2 = "Desember";
                break;
            default:
                bulanName2 = "Not found!";
                break;
        }

        startDateTV.setText(hariName2+", "+String.valueOf(Integer.parseInt(dayDate2))+" "+bulanName2+" "+yearDate2);

        String input_date3 = endDate;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format13 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt13 = null;
        try {
            dt13 = format13.parse(input_date3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat format23 = new SimpleDateFormat("EEE");
        @SuppressLint("SimpleDateFormat") DateFormat getweek3 = new SimpleDateFormat("W");
        String finalDay3 = format23.format(dt13);
        String week3 = getweek3.format(dt13);
        String hariName3 = "";

        if (finalDay3.equals("Mon") || finalDay3.equals("Sen")) {
            hariName3 = "Senin";
        } else if (finalDay3.equals("Tue") || finalDay3.equals("Sel")) {
            hariName3 = "Selasa";
        } else if (finalDay3.equals("Wed") || finalDay3.equals("Rab")) {
            hariName3 = "Rabu";
        } else if (finalDay3.equals("Thu") || finalDay3.equals("Kam")) {
            hariName3 = "Kamis";
        } else if (finalDay3.equals("Fri") || finalDay3.equals("Jum")) {
            hariName3 = "Jumat";
        } else if (finalDay3.equals("Sat") || finalDay3.equals("Sab")) {
            hariName3 = "Sabtu";
        } else if (finalDay3.equals("Sun") || finalDay3.equals("Min")) {
            hariName3 = "Minggu";
        }

        String dayDate3 = input_date3.substring(8,10);
        String yearDate3 = input_date3.substring(0,4);
        String bulanValue3 = input_date3.substring(5,7);
        String bulanName3;

        switch (bulanValue3) {
            case "01":
                bulanName3 = "Januari";
                break;
            case "02":
                bulanName3 = "Februari";
                break;
            case "03":
                bulanName3 = "Maret";
                break;
            case "04":
                bulanName3 = "April";
                break;
            case "05":
                bulanName3 = "Mei";
                break;
            case "06":
                bulanName3 = "Juni";
                break;
            case "07":
                bulanName3 = "Juli";
                break;
            case "08":
                bulanName3 = "Agustus";
                break;
            case "09":
                bulanName3 = "September";
                break;
            case "10":
                bulanName3 = "Oktober";
                break;
            case "11":
                bulanName3 = "November";
                break;
            case "12":
                bulanName3 = "Desember";
                break;
            default:
                bulanName3 = "Not found!";
                break;
        }

        endDateTV.setText(hariName3+", "+String.valueOf(Integer.parseInt(dayDate3))+" "+bulanName3+" "+yearDate3);

        persentaseProgress.setProgress(Math.round(Float.parseFloat(progressTask)));
        persentaseProgressTV.setText(String.valueOf(Math.round(Float.parseFloat(progressTask)))+"%");

    }

    private void picTaskPicker() {
        bottomSheet.showWithSheetView(LayoutInflater.from(UpdateTaskActivity.this).inflate(R.layout.layout_karyawan_pic_task, bottomSheet, false));
        keywordKaryawan = findViewById(R.id.keyword_user_ed);
        keywordKaryawan.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        karyawanRV = findViewById(R.id.karyawan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

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
                    new Handler().postDelayed(new Runnable() {
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
        final String url = "https://geloraaksara.co.id/absen-online/api/get_all_data_user_by_keyword";
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
                                adapterAllKaryawan = new AdapterAllKaryawanPICUpdate(karyawanAlls, UpdateTaskActivity.this);
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

    public BroadcastReceiver taskPicBroadUpdate = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            picNikBARU = intent.getStringExtra("nik_pic");
            picNameBARU = intent.getStringExtra("nama_pic");

            picTV.setText(picNameBARU);

            InputMethodManager imm = (InputMethodManager) UpdateTaskActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = UpdateTaskActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(UpdateTaskActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            taskNameED.clearFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void targetDate(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(UpdateTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                targetDateBARU = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String input_date = targetDateBARU;
                SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
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

                targetTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(UpdateTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                targetDateBARU = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                String input_date = targetDateBARU;
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

                targetTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            }, y,m-1,d);
            dpd.show();
        }

    }

    private void statusPicker() {
        bottomSheet.showWithSheetView(LayoutInflater.from(UpdateTaskActivity.this).inflate(R.layout.layout_task_status, bottomSheet, false));
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
        final String url = "https://geloraaksara.co.id/absen-online/api/task_status";
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
                            adapterStatusTask = new AdapterStatusTaskUpdate(statusTasks,UpdateTaskActivity.this);
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
                new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.ERROR_TYPE)
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

    public BroadcastReceiver taskStatusBroadUpdate = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            statusIdTaskBARU = intent.getStringExtra("id_status");
            String namaStatus = intent.getStringExtra("nama_status");
            statusIdTaskBeforeBARU = statusIdTaskBARU;

            if(statusIdTaskBARU.equals("5")){
                persentasePregressNumber = 100;
                persentasePregressNumberBefore = persentasePregressNumber;
                persentaseProgress.setProgress(100);
                persentaseProgressTV.setText(String.valueOf(persentasePregressNumber)+"%");
            } else {
                if(persentasePregressNumberBefore == 100){
                    persentasePregressNumberBefore = 0;
                    persentasePregressNumber = 0;
                    persentaseProgress.setProgress(0);
                    persentaseProgressTV.setText(String.valueOf(persentasePregressNumber)+"%");
                }
            }

            statusTV.setText(namaStatus);

            InputMethodManager imm = (InputMethodManager) UpdateTaskActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = UpdateTaskActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(UpdateTaskActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            taskNameED.clearFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(UpdateTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                startDateBARU = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                startDateParBARU = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

                if (!endDateBARU.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(startDateBARU));
                        date2 = sdf.parse(String.valueOf(endDateBARU));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = startDateBARU;
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

                        startDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    } else {
                        startDateTV.setText("Pilih Kembali !");
                        startDateBARU = "";

                        new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String input_date = startDateBARU;
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

                    startDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(UpdateTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                startDateBARU = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                startDateParBARU = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

                if (!endDateBARU.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(startDateBARU));
                        date2 = sdf.parse(String.valueOf(endDateBARU));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = startDateBARU;
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
                        startDateBARU = "";

                        new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String input_date = startDateBARU;
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

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(UpdateTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                endDateBARU = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                endDateParBARU = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

                if (!startDateBARU.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(startDateBARU));
                        date2 = sdf.parse(String.valueOf(endDateBARU));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = endDateBARU;
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
                        endDateBARU = "";

                        new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String input_date = endDateBARU;
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

                }

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(UpdateTaskActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                endDateBARU = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                endDateParBARU = String.format("%02d", month + 1)+"/"+String.format("%02d", dayOfMonth)+"/"+String.format("%d", year);

                if (!startDateBARU.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(startDateBARU));
                        date2 = sdf.parse(String.valueOf(endDateBARU));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = endDateBARU;
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
                        endDateBARU = "";

                        new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.ERROR_TYPE)
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
                    String input_date = endDateBARU;
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
        CookieBar.build(UpdateTaskActivity.this)
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
        if (!taskNameED.getText().toString().equals("")||!picNameBARU.equals("")||!targetDateBARU.equals("")||!startDateBARU.equals("")||!endDateBARU.equals("")||!statusIdTaskBARU.equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                new KAlertDialog(UpdateTaskActivity.this, KAlertDialog.WARNING_TYPE)
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
                                statusIdTaskBARU = "";
                                projectIdBARU = "";
                                picNikBARU = "";
                                picNameBARU = "";
                                targetDateBARU = "";
                                startDateBARU = "";
                                startDateParBARU = "";
                                endDateBARU = "";
                                endDateParBARU = "";
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

}