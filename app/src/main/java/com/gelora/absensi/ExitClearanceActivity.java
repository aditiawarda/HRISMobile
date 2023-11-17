package com.gelora.absensi;

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
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterExitClearanceIn;
import com.gelora.absensi.adapter.AdapterExitClearanceOut;
import com.gelora.absensi.adapter.AdapterKaryawanClearance;
import com.gelora.absensi.model.KaryawanClearance;
import com.gelora.absensi.model.ListDataExitClearanceIn;
import com.gelora.absensi.model.ListDataExitClearanceOut;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExitClearanceActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    ImageView loadingDataImg, loadingDataImg2, loadingGif;
    LinearLayout startAttantionPart, countNotificationInPart, backBTN, mainPart, actionBar, addBTN, dataInBTN, dataOutBTN, optionPart, dataMasukPart, dataKeluarPart, noDataPart, noDataPart2, loadingDataPart, loadingDataPart2;
    SwipeRefreshLayout refreshLayout;
    TextView countNotificationInTV;
    String otoritorEC;
    BottomSheetLayout bottomSheet;
    EditText keywordKaryawan;
    private KaryawanClearance[] karyawanClearances;
    private RecyclerView dataOutRV, dataInRV, karyawanRV;
    private ListDataExitClearanceOut[] listDataExitClearanceOuts;
    private AdapterExitClearanceOut adapterExitClearanceOut;
    private ListDataExitClearanceIn[] listDataExitClearanceIns;
    private AdapterExitClearanceIn adapterExitClearanceIn;
    private AdapterKaryawanClearance adapterKaryawanClearance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        countNotificationInPart = findViewById(R.id.count_notification_in);
        countNotificationInTV = findViewById(R.id.count_notif_in_tv);
        noDataPart = findViewById(R.id.no_data_part);
        noDataPart2 = findViewById(R.id.no_data_part_2);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingDataPart2 = findViewById(R.id.loading_data_part_2);
        loadingDataImg = findViewById(R.id.loading_data_img);
        loadingDataImg2 = findViewById(R.id.loading_data_img_2);
        optionPart = findViewById(R.id.option_part);
        dataInBTN = findViewById(R.id.data_in_btn);
        dataOutBTN = findViewById(R.id.data_out_btn);
        dataMasukPart = findViewById(R.id.data_masuk);
        dataKeluarPart = findViewById(R.id.data_saya);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);
        mainPart = findViewById(R.id.main_part);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);

        dataOutRV = findViewById(R.id.data_out_rv);
        dataInRV = findViewById(R.id.data_in_rv);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataImg);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingDataImg2);

        dataOutRV.setLayoutManager(new LinearLayoutManager(this));
        dataOutRV.setHasFixedSize(true);
        dataOutRV.setNestedScrollingEnabled(false);
        dataOutRV.setItemAnimator(new DefaultItemAnimator());

        dataInRV.setLayoutManager(new LinearLayoutManager(this));
        dataInRV.setHasFixedSize(true);
        dataInRV.setNestedScrollingEnabled(false);
        dataInRV.setItemAnimator(new DefaultItemAnimator());

        otoritorEC = getIntent().getExtras().getString("otoritor");

        LocalBroadcastManager.getInstance(this).registerReceiver(karyawanBoard, new IntentFilter("karyawan_broad"));

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataInRV.setVisibility(View.GONE);
                dataOutRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataIn(otoritorEC);
                        getDataOut();
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

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefManager.getSpIdJabatan().equals("1")){
                    pilihKaryawan();
                } else {
                    Intent intent = new Intent(ExitClearanceActivity.this, FormExitClearanceActivity.class);
                    intent.putExtra("nik", sharedPrefManager.getSpNik());
                    intent.putExtra("nama", sharedPrefManager.getSpNama());
                    intent.putExtra("id_bagian", sharedPrefManager.getSpIdDept());
                    intent.putExtra("id_dept", sharedPrefManager.getSpIdHeadDept());
                    intent.putExtra("id_jabatan", sharedPrefManager.getSpIdJabatan());
                    startActivity(intent);
                }
            }
        });

        dataOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataOutBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify_choice));
                dataInBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify));
                dataMasukPart.setVisibility(View.GONE);
                dataKeluarPart.setVisibility(View.VISIBLE);

                loadingDataPart.setVisibility(View.GONE);
                loadingDataPart2.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataInRV.setVisibility(View.GONE);
                dataOutRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataOut();
                    }
                }, 300);

            }
        });

        dataInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBTN.setVisibility(View.GONE);
                dataInBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify_choice));
                dataOutBTN.setBackground(ContextCompat.getDrawable(ExitClearanceActivity.this, R.drawable.shape_notify));
                dataMasukPart.setVisibility(View.VISIBLE);
                dataKeluarPart.setVisibility(View.GONE);

                loadingDataPart.setVisibility(View.VISIBLE);
                loadingDataPart2.setVisibility(View.GONE);
                noDataPart.setVisibility(View.GONE);
                noDataPart2.setVisibility(View.GONE);
                dataInRV.setVisibility(View.GONE);
                dataOutRV.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataIn(otoritorEC);
                    }
                }, 300);

            }
        });

        if (sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25") || !otoritorEC.equals("0")){
            if(sharedPrefManager.getSpNik().equals("000112092023")){
                float scale = getResources().getDisplayMetrics().density;
                int side = (int) (17*scale + 0.5f);
                int top = (int) (85*scale + 0.5f);
                int bottom = (int) (20*scale + 0.5f);
                mainPart.setPadding(side,top,side,bottom);
                optionPart.setVisibility(View.GONE);
                dataMasukPart.setVisibility(View.VISIBLE);
                dataKeluarPart.setVisibility(View.GONE);
            } else {
                optionPart.setVisibility(View.VISIBLE);
            }
        } else if (sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("000112092023")){
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            dataMasukPart.setVisibility(View.VISIBLE);
            dataKeluarPart.setVisibility(View.GONE);
        } else {
            addBTN.setVisibility(View.VISIBLE);
            float scale = getResources().getDisplayMetrics().density;
            int side = (int) (17*scale + 0.5f);
            int top = (int) (85*scale + 0.5f);
            int bottom = (int) (20*scale + 0.5f);
            mainPart.setPadding(side,top,side,bottom);
            optionPart.setVisibility(View.GONE);
            dataMasukPart.setVisibility(View.GONE);
            dataKeluarPart.setVisibility(View.VISIBLE);
        }

        getDataIn(otoritorEC);
        getDataOut();

    }

    private void pilihKaryawan(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_karyawan_clearance, bottomSheet, false));
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
                String keyWordSearch = keywordKaryawan.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                karyawanRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDataKaryawan(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        keywordKaryawan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = keywordKaryawan.getText().toString();
                    getDataKaryawan(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) ExitClearanceActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = ExitClearanceActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(ExitClearanceActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }

    public BroadcastReceiver karyawanBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            bottomSheet.dismissSheet();
        }
    };

    private void getDataKaryawan(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_clearance";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
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
                                karyawanClearances = gson.fromJson(data_list, KaryawanClearance[].class);
                                adapterKaryawanClearance = new AdapterKaryawanClearance(karyawanClearances, ExitClearanceActivity.this);
                                karyawanRV.setAdapter(adapterKaryawanClearance);
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
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getDataOut() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_data_keluar_exit_clearance";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                if(sharedPrefManager.getSpIdJabatan().equals("1")){
                                    addBTN.setVisibility(View.VISIBLE);
                                } else {
                                    addBTN.setVisibility(View.GONE);
                                }
                                noDataPart2.setVisibility(View.GONE);
                                loadingDataPart2.setVisibility(View.GONE);
                                dataOutRV.setVisibility(View.VISIBLE);
                                String data_keluar = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                listDataExitClearanceOuts = gson.fromJson(data_keluar, ListDataExitClearanceOut[].class);
                                adapterExitClearanceOut = new AdapterExitClearanceOut(listDataExitClearanceOuts,ExitClearanceActivity.this);
                                dataOutRV.setAdapter(adapterExitClearanceOut);
                            } else {
                                if(String.valueOf(dataMasukPart.getVisibility()).equals("0")){
                                    addBTN.setVisibility(View.GONE);
                                } else {
                                    addBTN.setVisibility(View.VISIBLE);
                                }
                                noDataPart2.setVisibility(View.VISIBLE);
                                loadingDataPart2.setVisibility(View.GONE);
                                dataOutRV.setVisibility(View.GONE);
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

    private void getDataIn(String nomor_st) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_data_masuk_exit_clearance";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String all_count = data.getString("all_count");
                                String waiting_count = data.getString("waiting_count");

                                if(Integer.parseInt(waiting_count)>0){
                                    countNotificationInPart.setVisibility(View.VISIBLE);
                                    countNotificationInTV.setText(waiting_count);
                                } else {
                                    countNotificationInPart.setVisibility(View.GONE);
                                }

                                if(Integer.parseInt(all_count)>0){
                                    noDataPart.setVisibility(View.GONE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataInRV.setVisibility(View.VISIBLE);
                                    String data_masuk = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    listDataExitClearanceIns = gson.fromJson(data_masuk, ListDataExitClearanceIn[].class);
                                    adapterExitClearanceIn = new AdapterExitClearanceIn(listDataExitClearanceIns,ExitClearanceActivity.this);
                                    dataInRV.setAdapter(adapterExitClearanceIn);
                                } else {
                                    noDataPart.setVisibility(View.VISIBLE);
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataInRV.setVisibility(View.GONE);
                                }
                            } else {
                                countNotificationInPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingDataPart.setVisibility(View.GONE);
                                dataInRV.setVisibility(View.GONE);
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
                params.put("nomor_st", nomor_st);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDataPart.setVisibility(View.VISIBLE);
        loadingDataPart2.setVisibility(View.VISIBLE);
        noDataPart.setVisibility(View.GONE);
        noDataPart2.setVisibility(View.GONE);
        dataInRV.setVisibility(View.GONE);
        dataOutRV.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                getDataIn(otoritorEC);
                getDataOut();
            }
        }, 300);
    }

    private void connectionFailed(){
        CookieBar.build(ExitClearanceActivity.this)
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
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}