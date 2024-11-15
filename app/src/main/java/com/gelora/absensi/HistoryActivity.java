package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterHistoryAbsen;
import com.gelora.absensi.model.HistoryAbsen;
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

public class HistoryActivity extends AppCompatActivity {

    LinearLayout noConnectPart, loadingPart, noDataPart, attantionPart, actionBar, filterDateBTN, backBTN, filterDateChoiceBTN, changeFilterDateBTN, filterDateChoice;
    TextView shiftName, shiftTime, nameOfUser, dateCheckinTV, dateCheckoutTV, filterDateChoiceTV, dateLastAbsenTV, timeCheckinLastAbsenTV, timeCheckoutLastAbsenTV, checkinPointLastAbsenTV, checkoutPointLastAbsenTV;
    SharedPrefManager sharedPrefManager;
    BottomSheetLayout bottomSheet;
    String dateChoiceForHistory;
    SwipeRefreshLayout refreshLayout;
    View rootview;

    private RecyclerView historyAbsenRV;
    private HistoryAbsen[] historyAbsens;
    private AdapterHistoryAbsen adapterHistoryAbsen;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sharedPrefManager = new SharedPrefManager(this);
        backBTN = findViewById(R.id.back_btn);
        filterDateBTN = findViewById(R.id.filter_date_btn);
        filterDateChoiceBTN = findViewById(R.id.filter_date_choice_btn);
        changeFilterDateBTN = findViewById(R.id.change_filter_date_btn);
        filterDateChoice = findViewById(R.id.filter_date_choice);
        filterDateChoiceTV = findViewById(R.id.filter_date_choice_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        dateLastAbsenTV = findViewById(R.id.date_absen_tv);
        timeCheckinLastAbsenTV = findViewById(R.id.time_checkin_tv);
        checkinPointLastAbsenTV = findViewById(R.id.checkin_point_tv);
        timeCheckoutLastAbsenTV = findViewById(R.id.time_checkout_tv);
        checkoutPointLastAbsenTV = findViewById(R.id.checkout_point_tv);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        attantionPart = findViewById(R.id.attantion_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingPart = findViewById(R.id.loading_data_part);
        rootview = findViewById(android.R.id.content);
        noConnectPart = findViewById(R.id.no_connect_part);
        dateCheckinTV = findViewById(R.id.date_checkin_tv);
        dateCheckoutTV = findViewById(R.id.date_checkout_tv);
        nameOfUser = findViewById(R.id.name_of_user_tv);
        shiftName = findViewById(R.id.shift_name);
        shiftTime = findViewById(R.id.shift_time);
        actionBar = findViewById(R.id.action_bar);
        nameOfUser.setText(sharedPrefManager.getSpNama().toUpperCase());

        historyAbsenRV = findViewById(R.id.history_absen_rv);

        historyAbsenRV.setLayoutManager(new LinearLayoutManager(this));
        historyAbsenRV.setHasFixedSize(true);
        historyAbsenRV.setNestedScrollingEnabled(false);
        historyAbsenRV.setItemAnimator(new DefaultItemAnimator());

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
                        lastAbsenHistory();
                        filterDateBTN.setVisibility(View.VISIBLE);
                        attantionPart.setVisibility(View.VISIBLE);
                        filterDateChoice.setVisibility(View.GONE);
                        loadingPart.setVisibility(View.GONE);
                        historyAbsenRV.setVisibility(View.GONE);
                        noDataPart.setVisibility(View.GONE);
                        noConnectPart.setVisibility(View.GONE);
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

        filterDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        filterDateChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        changeFilterDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        lastAbsenHistory();

    }

    @SuppressLint("SimpleDateFormat")
    private void datePicker(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(HistoryActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {
                filterDateBTN.setVisibility(View.GONE);
                filterDateChoice.setVisibility(View.VISIBLE);

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

                filterDateChoiceTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                loadingPart.setVisibility(View.VISIBLE);
                historyAbsenRV.setVisibility(View.GONE);
                attantionPart.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noConnectPart.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHistoryAbsensi();
                    }
                }, 1000);

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(HistoryActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {
                filterDateBTN.setVisibility(View.GONE);
                filterDateChoice.setVisibility(View.VISIBLE);

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
                        bulanName = "Not found";
                        break;
                }

                filterDateChoiceTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                loadingPart.setVisibility(View.VISIBLE);
                historyAbsenRV.setVisibility(View.GONE);
                attantionPart.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noConnectPart.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHistoryAbsensi();
                    }
                }, 1000);

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

    private void lastAbsenHistory(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/last_history_absen";
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
                            String tanggal = data.getString("tanggal");
                            String tanggal_checkin = data.getString("tanggal_checkin");
                            String tanggal_checkout = data.getString("tanggal_checkout");
                            String jam_checkin = data.getString("jam_checkin");
                            String timezone_checkin = data.getString("timezone_checkin");
                            String checkin_point = data.getString("checkin_point");
                            String jam_checkout = data.getString("jam_checkout");
                            String timezone_checkout = data.getString("timezone_checkout");
                            String checkout_point = data.getString("checkout_point");
                            String status_pulang = data.getString("status_pulang");
                            String status_absen = data.getString("status_absen");
                            String nama_shift = data.getString("nama_shift");
                            String jam_shift = data.getString("jam_shift");

                            if(status.equals("Success")){
                                dateLastAbsenTV.setText(tanggal);

                                String input_date_checkin = tanggal_checkin;
                                String dayDateCheckin = input_date_checkin.substring(8,10);
                                String yearDateCheckin = input_date_checkin.substring(0,4);
                                String bulanValueCheckin = input_date_checkin.substring(5,7);
                                String bulanNameCheckin;

                                switch (bulanValueCheckin) {
                                    case "01":
                                        bulanNameCheckin = "Januari";
                                        break;
                                    case "02":
                                        bulanNameCheckin = "Februari";
                                        break;
                                    case "03":
                                        bulanNameCheckin = "Maret";
                                        break;
                                    case "04":
                                        bulanNameCheckin = "April";
                                        break;
                                    case "05":
                                        bulanNameCheckin = "Mei";
                                        break;
                                    case "06":
                                        bulanNameCheckin = "Juni";
                                        break;
                                    case "07":
                                        bulanNameCheckin = "Juli";
                                        break;
                                    case "08":
                                        bulanNameCheckin = "Agustus";
                                        break;
                                    case "09":
                                        bulanNameCheckin = "September";
                                        break;
                                    case "10":
                                        bulanNameCheckin = "Oktober";
                                        break;
                                    case "11":
                                        bulanNameCheckin = "November";
                                        break;
                                    case "12":
                                        bulanNameCheckin = "Desember";
                                        break;
                                    default:
                                        bulanNameCheckin = "Not found";
                                        break;
                                }

                                dateCheckinTV.setText(String.valueOf(Integer.parseInt(dayDateCheckin))+" "+bulanNameCheckin+" "+yearDateCheckin);
                                timeCheckinLastAbsenTV.setText(jam_checkin+" "+timezone_checkin);

                                if (checkin_point.equals("")){
                                    checkinPointLastAbsenTV.setText(sharedPrefManager.getSpNama());
                                } else {
                                    checkinPointLastAbsenTV.setText(checkin_point);
                                }

                                if (status_pulang.equals("0")){
                                    dateCheckoutTV.setText("---- - -- - --");
                                    timeCheckoutLastAbsenTV.setText("-- : -- : -- ---");
                                } else {
                                    String input_date_checkout = tanggal_checkout;
                                    String dayDateCheckout = input_date_checkout.substring(8,10);
                                    String yearDateCheckout = input_date_checkout.substring(0,4);
                                    String bulanValueCheckout = input_date_checkout.substring(5,7);
                                    String bulanNameCheckout;

                                    switch (bulanValueCheckout) {
                                        case "01":
                                            bulanNameCheckout = "Januari";
                                            break;
                                        case "02":
                                            bulanNameCheckout = "Februari";
                                            break;
                                        case "03":
                                            bulanNameCheckout = "Maret";
                                            break;
                                        case "04":
                                            bulanNameCheckout = "April";
                                            break;
                                        case "05":
                                            bulanNameCheckout = "Mei";
                                            break;
                                        case "06":
                                            bulanNameCheckout = "Juni";
                                            break;
                                        case "07":
                                            bulanNameCheckout = "Juli";
                                            break;
                                        case "08":
                                            bulanNameCheckout = "Agustus";
                                            break;
                                        case "09":
                                            bulanNameCheckout = "September";
                                            break;
                                        case "10":
                                            bulanNameCheckout = "Oktober";
                                            break;
                                        case "11":
                                            bulanNameCheckout = "November";
                                            break;
                                        case "12":
                                            bulanNameCheckout = "Desember";
                                            break;
                                        default:
                                            bulanNameCheckout = "Not found";
                                            break;
                                    }
                                    dateCheckoutTV.setText(String.valueOf(Integer.parseInt(dayDateCheckout))+" "+bulanNameCheckout+" "+yearDateCheckout);
                                    timeCheckoutLastAbsenTV.setText(jam_checkout+" "+timezone_checkout);
                                }

                                if (checkout_point.equals("")){
                                    checkoutPointLastAbsenTV.setText(sharedPrefManager.getSpNama());
                                }  else if (checkout_point.equals(null)||checkout_point.equals("null")) {
                                    checkoutPointLastAbsenTV.setText("-");
                                } else {
                                    checkoutPointLastAbsenTV.setText(checkout_point);
                                }

                                shiftName.setText(status_absen+" - "+nama_shift);
                                shiftTime.setText(jam_shift);

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
                        if(!bottomSheet.isSheetShowing()){
                            connectionFailed();
                        }
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

        //requestQueue.add(postRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

    }

    private void connectionFailed(){
        // Banner.make(rootview, HistoryActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(HistoryActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();

    }

    private void getHistoryAbsensi() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/history_absen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String history_absen = data.getString("data");

                            loadingPart.setVisibility(View.GONE);
                            noConnectPart.setVisibility(View.GONE);
                            if (status.equals("Success")){
                                GsonBuilder builder =new GsonBuilder();
                                Gson gson = builder.create();
                                historyAbsens = gson.fromJson(history_absen, HistoryAbsen[].class);
                                adapterHistoryAbsen = new AdapterHistoryAbsen(historyAbsens,HistoryActivity.this);
                                historyAbsenRV.setAdapter(adapterHistoryAbsen);
                                historyAbsenRV.setVisibility(View.VISIBLE);
                                attantionPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                            } else {
                                historyAbsenRV.setVisibility(View.GONE);
                                attantionPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
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
                        loadingPart.setVisibility(View.GONE);
                        noConnectPart.setVisibility(View.VISIBLE);
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
                params.put("tanggal_pilih", dateChoiceForHistory);
                return params;
            }
        };

        //requestQueue.add(postRequest);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } else {
            requestQueue.add(postRequest);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}