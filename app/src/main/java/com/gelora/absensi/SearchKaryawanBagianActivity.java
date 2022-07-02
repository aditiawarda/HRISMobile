package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterBagian;
import com.gelora.absensi.adapter.AdapterBagianSearch;
import com.gelora.absensi.adapter.AdapterKehadiranBagian;
import com.gelora.absensi.adapter.AdapterKehadiranBagianSearch;
import com.gelora.absensi.adapter.AdapterKetidakhadiranBagian;
import com.gelora.absensi.adapter.AdapterKetidakhadiranBagianSearch;
import com.gelora.absensi.model.Bagian;
import com.gelora.absensi.model.DataMonitoringKehadiranBagian;
import com.gelora.absensi.model.DataMonitoringKetidakhadiranBagian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shasin.notificationbanner.Banner;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchKaryawanBagianActivity extends AppCompatActivity {

    String dateChoiceForHistory = "", currentDay="", keyWordSearch = "", idBagianChoice = "", kdBagianChoice = "";
    TextView attantionDesc, currentDateTV, bagianChoiceTV;
    LinearLayout choiceBagianBTN, attantionPart, choiceDateBTN, backBTN, homeBTN, loadingDataPart, emptyDataPart;
    EditText keywordUserED;
    SharedPrefManager sharedPrefManager;
    ImageView loadingData;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    View rootview;

    private RecyclerView dataAbsensiKaryawanRV;
    private DataMonitoringKehadiranBagian[] dataMonitoringKehadiranBagians;
    private AdapterKehadiranBagianSearch adapterKehadiranBagian;
    private DataMonitoringKetidakhadiranBagian[] dataMonitoringKetidakhadiranBagians;
    private AdapterKetidakhadiranBagianSearch adapterKetidakhadiranBagian;

    private RecyclerView bagianRV;
    private Bagian[] bagians;
    private AdapterBagianSearch adapterBagian;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_karyawan_bagian);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        currentDateTV = findViewById(R.id.current_date);
        choiceDateBTN = findViewById(R.id.choice_date);
        backBTN = findViewById(R.id.back_btn);
        homeBTN = findViewById(R.id.home_btn);
        keywordUserED = findViewById(R.id.keyword_user_ed);
        loadingDataPart = findViewById(R.id.loading_data_part);
        emptyDataPart = findViewById(R.id.no_data_part);
        loadingData = findViewById(R.id.loading_data);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        attantionPart = findViewById(R.id.attantion_part);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        choiceBagianBTN = findViewById(R.id.choice_bagian);
        bagianChoiceTV = findViewById(R.id.bagian_choice);
        attantionDesc = findViewById(R.id.attantion_desc);

        idBagianChoice = getIntent().getExtras().getString("id_bagian");
        kdBagianChoice = getIntent().getExtras().getString("nama_bagian");
        bagianChoiceTV.setText(kdBagianChoice);

        LocalBroadcastManager.getInstance(this).registerReceiver(bagianBroad, new IntentFilter("bagian_broad"));

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(loadingData);

        showSoftKeyboard(keywordUserED);

        dataAbsensiKaryawanRV = findViewById(R.id.data_absensi_karyawan_rv);

        dataAbsensiKaryawanRV.setLayoutManager(new LinearLayoutManager(this));
        dataAbsensiKaryawanRV.setHasFixedSize(true);
        dataAbsensiKaryawanRV.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dateChoiceForHistory = getDate();

                attantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                emptyDataPart.setVisibility(View.GONE);
                dataAbsensiKaryawanRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataAbsensiUser(keyWordSearch);
                        getCurrentDay();
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

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchKaryawanBagianActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        choiceDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        choiceBagianBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                choiceBagian();
            }
        });

        keywordUserED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWordSearch = keywordUserED.getText().toString();

                attantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                emptyDataPart.setVisibility(View.GONE);
                dataAbsensiKaryawanRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataAbsensiUser(keyWordSearch);
                    }
                }, 500);
            }

        });

        keywordUserED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });

        if (sharedPrefManager.getSpIdJabatan().equals("10")) {
            choiceBagianBTN.setVisibility(View.VISIBLE);
            attantionDesc.setText("Fitur ini dibuat khusus untuk Kepala Departemen agar dapat memantau kehadiran karyawan di departemennya masing-masing.");
        } else if (sharedPrefManager.getSpIdJabatan().equals("11")) {
            choiceBagianBTN.setVisibility(View.GONE);
            attantionDesc.setText("Fitur ini dibuat khusus untuk Kepala Bagian agar dapat memantau kehadiran karyawan di bagiannya masing-masing.");
        }

        dateChoiceForHistory = getDate();
        getCurrentDay();

    }

    public BroadcastReceiver bagianBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String idbagian = intent.getStringExtra("id_bagian");
            String namaBagian = intent.getStringExtra("nama_bagian");
            String descBagian = intent.getStringExtra("desc_bagian");
            bagianChoiceTV.setText(namaBagian);
            idBagianChoice = idbagian;

            attantionPart.setVisibility(View.GONE);
            loadingDataPart.setVisibility(View.VISIBLE);
            emptyDataPart.setVisibility(View.GONE);
            dataAbsensiKaryawanRV.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    getDataAbsensiUser(keyWordSearch);
                }
            }, 300);

        }
    };

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

    private void getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay = "Minggu";
                break;
            case Calendar.MONDAY:
                currentDay = "Senin";
                break;
            case Calendar.TUESDAY:
                currentDay = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "Rabu";
                break;
            case Calendar.THURSDAY:
                currentDay = "Kamis";
                break;
            case Calendar.FRIDAY:
                currentDay = "Jumat";
                break;
            case Calendar.SATURDAY:
                currentDay = "Sabtu";
                break;
        }

        dateLive();

    }

    @SuppressLint("SetTextI18n")
    private void dateLive(){
        switch (getDateM()) {
            case "01":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                break;
            case "02":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                break;
            case "03":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                break;
            case "04":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                break;
            case "05":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                break;
            case "06":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                break;
            case "07":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                break;
            case "08":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                break;
            case "09":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                break;
            case "10":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                break;
            case "11":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                break;
            case "12":
                currentDateTV.setText(currentDay +", "+ String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                break;
            default:
                currentDateTV.setText("Not found!");
                break;
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void datePicker(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(SearchKaryawanBagianActivity.this, (view1, year, month, dayOfMonth) -> {

                dateChoiceForHistory = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                String input_date = dateChoiceForHistory;
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

                currentDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                attantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                emptyDataPart.setVisibility(View.GONE);
                dataAbsensiKaryawanRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataAbsensiUser(keyWordSearch);
                    }
                }, 500);


            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(SearchKaryawanBagianActivity.this, (view1, year, month, dayOfMonth) -> {

                dateChoiceForHistory = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);
                String input_date = dateChoiceForHistory;
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
                        bulanName = "Not found";
                        break;
                }

                currentDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                attantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                emptyDataPart.setVisibility(View.GONE);
                dataAbsensiKaryawanRV.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataAbsensiUser(keyWordSearch);
                    }
                }, 500);

            }, y,m,d);
            dpd.show();
        }

    }

    private void getDataAbsensiUser(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/search_karyawan_bagian";
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
                                String absen = data.getString("absen");

                                if (absen.equals("1")){
                                    String data_hadir = data.getString("data_hadir");
                                    if (!data_hadir.equals("0")){

                                        loadingDataPart.setVisibility(View.GONE);
                                        emptyDataPart.setVisibility(View.GONE);
                                        dataAbsensiKaryawanRV.setVisibility(View.VISIBLE);

                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        dataMonitoringKehadiranBagians = gson.fromJson(data_hadir, DataMonitoringKehadiranBagian[].class);
                                        adapterKehadiranBagian = new AdapterKehadiranBagianSearch(dataMonitoringKehadiranBagians, SearchKaryawanBagianActivity.this);
                                        dataAbsensiKaryawanRV.setAdapter(adapterKehadiranBagian);
                                    } else {

                                        loadingDataPart.setVisibility(View.GONE);
                                        emptyDataPart.setVisibility(View.VISIBLE);
                                        dataAbsensiKaryawanRV.setVisibility(View.GONE);

                                    }
                                } else if (absen.equals("0")){
                                    String data_tidak_hadir = data.getString("data_tidak_hadir");
                                    if (!data_tidak_hadir.equals("0")){

                                        loadingDataPart.setVisibility(View.GONE);
                                        emptyDataPart.setVisibility(View.GONE);
                                        dataAbsensiKaryawanRV.setVisibility(View.VISIBLE);

                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        dataMonitoringKetidakhadiranBagians = gson.fromJson(data_tidak_hadir, DataMonitoringKetidakhadiranBagian[].class);
                                        adapterKetidakhadiranBagian = new AdapterKetidakhadiranBagianSearch(dataMonitoringKetidakhadiranBagians, SearchKaryawanBagianActivity.this);
                                        dataAbsensiKaryawanRV.setAdapter(adapterKetidakhadiranBagian);
                                    } else {

                                        loadingDataPart.setVisibility(View.GONE);
                                        emptyDataPart.setVisibility(View.VISIBLE);
                                        dataAbsensiKaryawanRV.setVisibility(View.GONE);

                                    }
                                }

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", idBagianChoice);
                params.put("tanggal", dateChoiceForHistory);
                params.put("keyword", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void choiceBagian(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_list_bagian, bottomSheet, false));
        bagianRV = findViewById(R.id.bagian_rv);

        bagianRV.setLayoutManager(new LinearLayoutManager(this));
        bagianRV.setHasFixedSize(true);
        bagianRV.setItemAnimator(new DefaultItemAnimator());

        getListBagian();

    }

    private void getListBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_list_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String data_bagian = data.getString("data_bagian");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            bagians = gson.fromJson(data_bagian, Bagian[].class);
                            adapterBagian = new AdapterBagianSearch(bagians,SearchKaryawanBagianActivity.this);
                            bagianRV.setAdapter(adapterBagian);

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
                params.put("id_departemen", sharedPrefManager.getSpIdHeadDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    private void connectionFailed(){
        Banner.make(rootview, SearchKaryawanBagianActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 4000).show();
    }

}