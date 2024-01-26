package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.warkiz.widget.IndicatorSeekBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateTaskActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN;
    EditText taskNameED;
    TextView picTV, targetTV, statusTV, startDateTV, endDateTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    KAlertDialog pDialog;
    IndicatorSeekBar persentaseProgress;

    // Before
    String projectId, taskName, picTask, dateTarget, statusTask, timlineTask, progressTask;
    String startDate, endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        taskNameED = findViewById(R.id.task_name_ed);
        picTV = findViewById(R.id.pic_tv);
        targetTV = findViewById(R.id.target_date_tv);
        statusTV = findViewById(R.id.status_tv);
        startDateTV = findViewById(R.id.start_date_tv);
        endDateTV = findViewById(R.id.end_date_tv);
        persentaseProgress = findViewById(R.id.persentase_progress);

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

        Toast.makeText(this, projectId+" - "+taskName+" - "+picTask+" - "+dateTarget+" - "+statusTask+" - "+timlineTask+" - "+progressTask, Toast.LENGTH_SHORT).show();

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

        applyData();

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
        } else if(statusTask.equals("4")){
            statusTV.setText("On Hold");
        } else if(statusTask.equals("3")){
            statusTV.setText("Waiting Approval");
        } else if(statusTask.equals("2")){
            statusTV.setText("On Progress");
        } else if(statusTask.equals("1")){
            statusTV.setText("Waiting");
        } else if(statusTask.equals("0")){
            statusTV.setText("To Do");
        } else {
            statusTV.setText("Undefined");
        }
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask);

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

        persentaseProgress.setProgress(Float.parseFloat(progressTask));

    }


}