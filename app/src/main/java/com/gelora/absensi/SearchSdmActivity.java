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
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterBagianSearchSDM;
import com.gelora.absensi.adapter.AdapterListSDMSearch;
import com.gelora.absensi.model.Bagian;
import com.gelora.absensi.model.HumanResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchSdmActivity extends AppCompatActivity {

    LinearLayout backBTN, choiceBagianBTN, actionBar, loadingDataPart, emptyDataPart, attantionPart;
    EditText keywordUserED;
    TextView bagianChoiceTV;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    String bagianPilih = "", keyWordSearch = "";

    private RecyclerView dataSdmRV;
    private HumanResource[] humanResources;
    private AdapterListSDMSearch adapterListSDMSearch;

    private RecyclerView bagianRV;
    private Bagian[] bagians;
    private AdapterBagianSearchSDM adapterBagian;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        choiceBagianBTN = findViewById(R.id.choice_bagian);
        bagianChoiceTV = findViewById(R.id.bagian_choice);
        backBTN = findViewById(R.id.back_btn);
        keywordUserED = findViewById(R.id.keyword_user_ed);

        loadingDataPart = findViewById(R.id.loading_data_part);
        emptyDataPart = findViewById(R.id.no_data_part);
        attantionPart = findViewById(R.id.attantion_part);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);

        dataSdmRV = findViewById(R.id.data_sdm_rv);

        dataSdmRV.setLayoutManager(new LinearLayoutManager(this));
        dataSdmRV.setHasFixedSize(true);
        dataSdmRV.setNestedScrollingEnabled(false);
        dataSdmRV.setItemAnimator(new DefaultItemAnimator());

        keywordUserED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        showSoftKeyboard(keywordUserED);
        LocalBroadcastManager.getInstance(this).registerReceiver(bagianBroad, new IntentFilter("bagian_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(closeKeyboard, new IntentFilter("close_keyboard"));

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                loadingDataPart.setVisibility(View.VISIBLE);
                emptyDataPart.setVisibility(View.GONE);
                attantionPart.setVisibility(View.GONE);
                dataSdmRV.setVisibility(View.GONE);

                keywordUserED.setText("");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);

                        loadingDataPart.setVisibility(View.GONE);
                        emptyDataPart.setVisibility(View.GONE);
                        attantionPart.setVisibility(View.VISIBLE);
                        dataSdmRV.setVisibility(View.GONE);

                        checkUser();
                    }
                }, 1000);

            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                onBackPressed();
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
                dataSdmRV.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!keyWordSearch.equals("") && !keyWordSearch.equals(" ")){
                            getDataSDM(keyWordSearch);
                        } else {
                            attantionPart.setVisibility(View.VISIBLE);
                            loadingDataPart.setVisibility(View.GONE);
                            emptyDataPart.setVisibility(View.GONE);
                            dataSdmRV.setVisibility(View.GONE);
                        }
                    }
                }, 1000);

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

        checkUser();

    }

    public BroadcastReceiver closeKeyboard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    };

    public BroadcastReceiver bagianBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String idbagian = intent.getStringExtra("id_bagian");
            String namaBagian = intent.getStringExtra("nama_bagian");
            String descBagian = intent.getStringExtra("desc_bagian");
            bagianChoiceTV.setText(namaBagian);
            bagianPilih = idbagian;

            attantionPart.setVisibility(View.GONE);
            loadingDataPart.setVisibility(View.VISIBLE);
            emptyDataPart.setVisibility(View.GONE);
            dataSdmRV.setVisibility(View.GONE);

            keyWordSearch = keywordUserED.getText().toString();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    if(!keyWordSearch.equals("") && !keyWordSearch.equals(" ")){
                        getDataSDM(keyWordSearch);
                    } else {
                        attantionPart.setVisibility(View.VISIBLE);
                        loadingDataPart.setVisibility(View.GONE);
                        emptyDataPart.setVisibility(View.GONE);
                        dataSdmRV.setVisibility(View.GONE);
                    }
                }
            }, 300);

        }
    };

    public void checkUser(){
        bagianPilih = sharedPrefManager.getSpIdDept();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_BAGIAN, bagianPilih);
        if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")){
            choiceBagianBTN.setVisibility(View.VISIBLE);
        } else if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
            choiceBagianBTN.setVisibility(View.GONE);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_bagian";
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
                                String nama_bagian = data.getString("nama_bagian");
                                bagianChoiceTV.setText(nama_bagian);
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
                params.put("id_bagian", bagianPilih);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void choiceBagian(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_list_bagian, bottomSheet, false));
        bagianRV = findViewById(R.id.bagian_rv);

        bagianRV.setLayoutManager(new LinearLayoutManager(this));
        bagianRV.setHasFixedSize(true);
        bagianRV.setNestedScrollingEnabled(false);
        bagianRV.setItemAnimator(new DefaultItemAnimator());

        getListBagian();

    }

    private void getListBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_list_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String data_bagian = data.getString("data_bagian");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            bagians = gson.fromJson(data_bagian, Bagian[].class);
                            adapterBagian = new AdapterBagianSearchSDM(bagians,SearchSdmActivity.this);
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
                params.put("nik", sharedPrefManager.getSpNik());
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

    private void getDataSDM(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/list_sdm_search";
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
                                String jumlah_data = data.getString("jumlah");
                                if(Integer.parseInt(jumlah_data)>0){
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataSdmRV.setVisibility(View.VISIBLE);
                                    emptyDataPart.setVisibility(View.GONE);
                                    String data_karyawan = data.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    humanResources = gson.fromJson(data_karyawan, HumanResource[].class);
                                    adapterListSDMSearch = new AdapterListSDMSearch(humanResources, SearchSdmActivity.this);
                                    dataSdmRV.setAdapter(adapterListSDMSearch);
                                } else {
                                    loadingDataPart.setVisibility(View.GONE);
                                    dataSdmRV.setVisibility(View.GONE);
                                    emptyDataPart.setVisibility(View.VISIBLE);
                                }
                            } else {
                                loadingDataPart.setVisibility(View.GONE);
                                dataSdmRV.setVisibility(View.GONE);
                                emptyDataPart.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            loadingDataPart.setVisibility(View.GONE);
                            dataSdmRV.setVisibility(View.GONE);
                            emptyDataPart.setVisibility(View.VISIBLE);
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("id_bagian", bagianPilih);
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

    private void connectionFailed(){
        CookieBar.build(SearchSdmActivity.this)
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}