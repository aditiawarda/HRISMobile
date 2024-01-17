package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
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
import com.gelora.absensi.adapter.AdapterAllKaryawan;
import com.gelora.absensi.adapter.AdapterKaryawanPengganti;
import com.gelora.absensi.adapter.AdapterKategoriIzin;
import com.gelora.absensi.adapter.AdapterProjectCategoryForm;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanAll;
import com.gelora.absensi.model.KaryawanPengganti;
import com.gelora.absensi.model.KategoriIzin;
import com.gelora.absensi.model.ProjectCategory;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormInputProjectActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, choiceCategoryBTN, submitBTN, projectLeaderBTN, startAttantionPart, noDataPart, loadingDataPart;
    TextView projectNameTV, categoryChoiceTV, projectLeaderTV;
    EditText projectNameED;
    ImageView loadingGif;
    MultiAutoCompleteTextView picMultyTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    private RecyclerView categoryProjectRV;
    private ProjectCategory[] projectCategories;
    private AdapterProjectCategoryForm adapterProjectCategory;
    RequestQueue requestQueue;
    String categoryChoice = "", projectDisimpan = "0";

    EditText keywordKaryawan;
    private RecyclerView karyawanRV;
    private KaryawanAll[] karyawanAlls;
    private AdapterAllKaryawan adapterAllKaryawan;
    String nikProjectLeader = "", namaProjectLeader = "", idBagianProjectLeader = "", idDepartemenProjectLeader = "", idJabatanProjectLeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_input_project);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        choiceCategoryBTN = findViewById(R.id.choice_category_btn);
        categoryChoiceTV = findViewById(R.id.category_choice_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        projectNameED = findViewById(R.id.project_name_ed);
        submitBTN = findViewById(R.id.submit_btn);
        projectLeaderBTN = findViewById(R.id.project_leader_btn);
        projectLeaderTV = findViewById(R.id.project_leader_tv);

        LocalBroadcastManager.getInstance(this).registerReceiver(projectLeaderBroad, new IntentFilter("project_leader"));
        LocalBroadcastManager.getInstance(this).registerReceiver(categoryProjectBroad, new IntentFilter("category_broad"));

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
                categoryChoice = "";
                categoryChoiceTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");
                projectLeaderTV.setText("");
                nikProjectLeader = "";
                namaProjectLeader = "";
                idBagianProjectLeader = "";
                idDepartemenProjectLeader = "";
                idJabatanProjectLeader = "";
//                taskRV.setVisibility(View.GONE);
//                loadingDataPart.setVisibility(View.VISIBLE);
//                noDataPart.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
//                        getDetailProject(projectId);
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

        projectLeaderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
//                alasanTV.clearFocus();
//                alamatSelamaCutiTV.clearFocus();
//                noHpTV.clearFocus();

                projectLeader();
            }
        });

        choiceCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryChoice();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //getAllUser();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_PROJECT, "");

    }

    private void projectLeader() {
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInputProjectActivity.this).inflate(R.layout.layout_karyawan_project_leader, bottomSheet, false));
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
//                alasanTV.clearFocus();
//                alamatSelamaCutiTV.clearFocus();
//                noHpTV.clearFocus();

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
                                adapterAllKaryawan = new AdapterAllKaryawan(karyawanAlls, FormInputProjectActivity.this);
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

    public BroadcastReceiver projectLeaderBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            nikProjectLeader = intent.getStringExtra("nik_project_leader");
            namaProjectLeader = intent.getStringExtra("nama_project_leader");
            idBagianProjectLeader = intent.getStringExtra("id_bagian_project_leader");
            idDepartemenProjectLeader = intent.getStringExtra("id_departemen_project_leader");
            idJabatanProjectLeader = intent.getStringExtra("id_jabatan_project_leader");

            projectLeaderTV.setText(namaProjectLeader);

            InputMethodManager imm = (InputMethodManager) FormInputProjectActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = FormInputProjectActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(FormInputProjectActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
//            alasanTV.clearFocus();
//            alamatSelamaCutiTV.clearFocus();
//            noHpTV.clearFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    public BroadcastReceiver categoryProjectBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String categoryId = intent.getStringExtra("id_kategori");
            String categoryName = intent.getStringExtra("nama_kategori");

            categoryChoice = categoryId;
            categoryChoiceTV.setText(categoryName);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInputProjectActivity.this).inflate(R.layout.layout_kategori_project, bottomSheet, false));
        categoryProjectRV = findViewById(R.id.kategori_project_rv);
        if(categoryProjectRV != null){
            categoryProjectRV.setLayoutManager(new LinearLayoutManager(this));
            categoryProjectRV.setHasFixedSize(true);
            categoryProjectRV.setNestedScrollingEnabled(false);
            categoryProjectRV.setItemAnimator(new DefaultItemAnimator());
        }

        getProjectCategory();

    }

    private void getProjectCategory() {
        final String url = "https://geloraaksara.co.id/absen-online/api/project_category_form";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String data_category = data.getString("data");
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            projectCategories = gson.fromJson(data_category, ProjectCategory[].class);
                            adapterProjectCategory = new AdapterProjectCategoryForm(projectCategories,FormInputProjectActivity.this);
                            try {
                                categoryProjectRV.setAdapter(adapterProjectCategory);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getAllUserr() {
        final String url = "https://geloraaksara.co.id/absen-online/api/get_all_data_user";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject event = data.getJSONObject(i);
                                    String nama_karyawan = event.getString("NmKaryawan");
                                    String bagian = event.getString("NmDept");
                                    //allUser.add(nama_karyawan+" - "+bagian);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateView() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void connectionFailed(){
        CookieBar.build(FormInputProjectActivity.this)
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
        if (!categoryChoice.equals("")||!nikProjectLeader.equals("")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (projectDisimpan.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormInputProjectActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    categoryChoice = "";
                                    nikProjectLeader = "";
                                    projectNameED.setText("");
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