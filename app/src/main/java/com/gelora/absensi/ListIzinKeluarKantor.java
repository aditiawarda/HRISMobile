package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterListKeluarKantor;
import com.gelora.absensi.databinding.ActivityListIzinKeluarKantorBinding;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.network.Repository;
import com.gelora.absensi.viewmodel.ConnectivityViewModel;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;


public class ListIzinKeluarKantor extends AppCompatActivity {

    private ActivityListIzinKeluarKantorBinding _binding = null;
    private ActivityListIzinKeluarKantorBinding getBinding() {
        return _binding;
    }
    private Handler handler = new Handler();
    private Repository repository;
    private AdapterListKeluarKantor rAdapter;
    private Boolean selectedLeftButton = true;
    private Boolean selectedRightButton = false;
    private int currentRoleIndex = 0;
    private boolean isConnected = true;
    private String preventRepetedCalls = "0";
    private boolean firstNetwork;
    private ConnectivityViewModel viewModel;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;

    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();
        _binding = ActivityListIzinKeluarKantorBinding.inflate(layoutInflater);
        View view = getBinding().getRoot();
        setContentView(view);
        rAdapter = new AdapterListKeluarKantor();
        repository = new Repository(this);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        viewModel = new ViewModelProvider(ListIzinKeluarKantor.this).get(ConnectivityViewModel.class);
        getBinding().appbar.setOnClickListener(view1 -> {});
        checkInternet();

        getBinding().swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        getBinding().swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);

        getBinding().swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataByJabatan();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().swipeToRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        getBinding().backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListIzinKeluarKantor.this.onBackPressed();
            }
        });

        getBinding().btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FormIzinKeluarKantor.class);
                view.getContext().startActivity(intent);
            }
        });

        if(sharedPrefManager.getSpIdDept().equals("21")){
            getBinding().scannerBtn.setVisibility(View.VISIBLE);
            getBinding().scannerBtn.setOnClickListener(view1 -> {
                Intent intent = new Intent(view.getContext(), OfficeLeftScannerActivity.class);
                view.getContext().startActivity(intent);
            });
        } else {
            getBinding().scannerBtn.setVisibility(View.GONE);
        }

        if ((!sharedPrefManager.getSpIdJabatan().equals("41") && !sharedPrefManager.getSpIdJabatan().equals("10") && !sharedPrefManager.getSpIdJabatan().equals("3")) && (!sharedPrefManager.getSpIdJabatan().equals("11") && !sharedPrefManager.getSpIdJabatan().equals("25") && !sharedPrefManager.getSpIdJabatan().equals("4")) && (!sharedPrefManager.getSpNik().equals("1280270910") && !sharedPrefManager.getSpNik().equals("1090080310") && !sharedPrefManager.getSpNik().equals("2840071116") && !sharedPrefManager.getSpNik().equals("1332240111")) && !sharedPrefManager.getSpIdDept().equals("21")) {
            getBinding().addBtnPart.setVisibility(View.VISIBLE);
        }

        handleSelectedButton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }

    private void checkInternet(){
        viewModel.getIsConnected().observe(this, isConnectedNow -> {
            handler.removeCallbacks(runnable);
            runnable = () -> {
                if (isConnectedNow) {
                    getDataByJabatan();
                } else {
                    connectionFailed();
                }
            };
            handler.postDelayed(runnable, 500);
        });
    }

    private void connectionFailed(){
        CookieBar.build(ListIzinKeluarKantor.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void getDataByJabatan(){
        if ((sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")) || (sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("4")) || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
            int topDp = 205;
            int bottomDp = 20;
            int leftDp = 17;
            int rightDp = 17;
            int topPx = dpToPx(topDp);
            int bottomPx = dpToPx(bottomDp);
            int leftPx = dpToPx(leftDp);
            int rightPx = dpToPx(rightDp);
            getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
            if (selectedLeftButton){
                getAllData(sharedPrefManager.getSpNik());
            } else {
                int topPaddingDp = 0;
                int bottomPaddingDp = 100;
                int leftPaddingDp = 20;
                int rightPaddingDp = 20;
                int topPaddingPx = dpToPx(topPaddingDp);
                int bottomPaddingPx = dpToPx(bottomPaddingDp);
                int leftPaddingPx = dpToPx(leftPaddingDp);
                int rightPaddingPx = dpToPx(rightPaddingDp);
                getBinding().expandableLayout.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
                getMyData(sharedPrefManager.getSpNik());
            }
            getCountWaiting();
        } else if (sharedPrefManager.getSpIdDept().equals("21")){
            if (selectedLeftButton){
                int topDp = 250;
                int bottomDp = 20;
                int leftDp = 17;
                int rightDp = 17;
                int topPx = dpToPx(topDp);
                int bottomPx = dpToPx(bottomDp);
                int leftPx = dpToPx(leftDp);
                int rightPx = dpToPx(rightDp);
                getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
                getAllDataForSatpam(sharedPrefManager.getSpNik());
            } else {
                int topDp = 205;
                int bottomDp = 20;
                int leftDp = 17;
                int rightDp = 17;
                int topPx = dpToPx(topDp);
                int bottomPx = dpToPx(bottomDp);
                int leftPx = dpToPx(leftDp);
                int rightPx = dpToPx(rightDp);
                getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
                int topPaddingDp = 0;
                int bottomPaddingDp = 100;
                int leftPaddingDp = 20;
                int rightPaddingDp = 20;
                int topPaddingPx = dpToPx(topPaddingDp);
                int bottomPaddingPx = dpToPx(bottomPaddingDp);
                int leftPaddingPx = dpToPx(leftPaddingDp);
                int rightPaddingPx = dpToPx(rightPaddingDp);
                getBinding().expandableLayout.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
                getMyData(sharedPrefManager.getSpNik());
            }
            getCountWaiting();
        } else {
            int topDp = 100;
            int bottomDp = 20;
            int leftDp = 17;
            int rightDp = 17;
            int topPx = dpToPx(topDp);
            int bottomPx = dpToPx(bottomDp);
            int leftPx = dpToPx(leftDp);
            int rightPx = dpToPx(rightDp);
            getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
            getBinding().addBtnPart.setVisibility(View.VISIBLE);
            int topPaddingDp = 73;
            int bottomPaddingDp = 100;
            int leftPaddingDp = 20;
            int rightPaddingDp = 20;
            int topPaddingPx = dpToPx(topPaddingDp);
            int bottomPaddingPx = dpToPx(bottomPaddingDp);
            int leftPaddingPx = dpToPx(leftPaddingDp);
            int rightPaddingPx = dpToPx(rightPaddingDp);
            getBinding().expandableLayout.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
            getMyData(sharedPrefManager.getSpNik());
        }
    }

    private void handleSelectedButton(){
        if ((sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")) || (sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpIdJabatan().equals("4")) || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111")) || sharedPrefManager.getSpIdDept().equals("21")){
            if(sharedPrefManager.getSpIdDept().equals("21")){
                int topDp = 250;
                int bottomDp = 20;
                int leftDp = 17;
                int rightDp = 17;
                int topPx = dpToPx(topDp);
                int bottomPx = dpToPx(bottomDp);
                int leftPx = dpToPx(leftDp);
                int rightPx = dpToPx(rightDp);
                getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
            } else {
                int topDp = 205;
                int bottomDp = 20;
                int leftDp = 17;
                int rightDp = 17;
                int topPx = dpToPx(topDp);
                int bottomPx = dpToPx(bottomDp);
                int leftPx = dpToPx(leftDp);
                int rightPx = dpToPx(rightDp);
                getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
            }
            getBinding().tabBar.setVisibility(View.VISIBLE);
            getCountWaiting();
        } else {
            int topDp = 100;
            int bottomDp = 20;
            int leftDp = 17;
            int rightDp = 17;
            int topPx = dpToPx(topDp);
            int bottomPx = dpToPx(bottomDp);
            int leftPx = dpToPx(leftDp);
            int rightPx = dpToPx(rightDp);
            getBinding().waitingData.setPadding(leftPx, topPx, rightPx, bottomPx);
            getBinding().tabBar.setVisibility(View.GONE);
            int topPaddingDp = 73;
            int bottomPaddingDp = 100;
            int leftPaddingDp = 20;
            int rightPaddingDp = 20;
            int topPaddingPx = dpToPx(topPaddingDp);
            int bottomPaddingPx = dpToPx(bottomPaddingDp);
            int leftPaddingPx = dpToPx(leftPaddingDp);
            int rightPaddingPx = dpToPx(rightPaddingDp);
            getBinding().expandableLayout.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
        }

        getBinding().permohonanMasuk.setOnClickListener(view1 -> {
            if(sharedPrefManager.getSpIdDept().equals("21")){
                getBinding().scannerBtn.setVisibility(View.VISIBLE);
                getBinding().scannerBtn.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), OfficeLeftScannerActivity.class);
                    view.getContext().startActivity(intent);
                });
            } else {
                getBinding().scannerBtn.setVisibility(View.GONE);
            }
            int topPaddingDp = 0;
            int bottomPaddingDp = 0;
            int leftPaddingDp = 20;
            int rightPaddingDp = 20;
            int topPaddingPx = dpToPx(topPaddingDp);
            int bottomPaddingPx = dpToPx(bottomPaddingDp);
            int leftPaddingPx = dpToPx(leftPaddingDp);
            int rightPaddingPx = dpToPx(rightPaddingDp);
            getBinding().expandableLayout.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
            getBinding().addBtnPart.setVisibility(View.GONE);
            selectedLeftButton = true;
            selectedRightButton = false;
            if(preventRepetedCalls.equals("0") || preventRepetedCalls.equals("1")){
                getBinding().parentLay.setVisibility(View.INVISIBLE);
                getBinding().loadingDataPart.setVisibility(View.VISIBLE);
                getBinding().noDataPart.setVisibility(View.GONE);
                getDataByJabatan();
            } else {
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            }
            if (preventRepetedCalls.equals("0")| preventRepetedCalls.equals("1")){
                preventRepetedCalls = "2";
            }

            if (selectedLeftButton){
                getBinding().permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListIzinKeluarKantor.this, R.color.selected_yellow))));
                getBinding().permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListIzinKeluarKantor.this, R.color.darker_gray))));
            } else {
                getBinding().permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListIzinKeluarKantor.this, R.color.darker_gray))));
            }

        });

        getBinding().permohonanSaya.setOnClickListener(view1 -> {
            getBinding().scannerBtn.setVisibility(View.GONE);
            int topPaddingDp = 0;
            int bottomPaddingDp = 100;
            int leftPaddingDp = 20;
            int rightPaddingDp = 20;
            int topPaddingPx = dpToPx(topPaddingDp);
            int bottomPaddingPx = dpToPx(bottomPaddingDp);
            int leftPaddingPx = dpToPx(leftPaddingDp);
            int rightPaddingPx = dpToPx(rightPaddingDp);
            getBinding().expandableLayout.setPadding(leftPaddingPx, topPaddingPx, rightPaddingPx, bottomPaddingPx);
            getBinding().addBtnPart.setVisibility(View.VISIBLE);
            selectedRightButton = true;
            selectedLeftButton = false;
            if(preventRepetedCalls.equals("0")||preventRepetedCalls.equals("2")){
                getBinding().parentLay.setVisibility(View.INVISIBLE);
                getBinding().loadingDataPart.setVisibility(View.VISIBLE);
                getBinding().noDataPart.setVisibility(View.GONE);
                getDataByJabatan();
            } else {
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            }
            if (preventRepetedCalls.equals("0") ||preventRepetedCalls.equals("2")){
                preventRepetedCalls = "1";
            }

            if (selectedRightButton){
                getBinding().permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListIzinKeluarKantor.this, R.color.selected_yellow))));
                getBinding().permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListIzinKeluarKantor.this, R.color.darker_gray))));
            } else {
                getBinding().permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListIzinKeluarKantor.this, R.color.darker_gray))));
            }
        });
    }

    void getMyData(String nik){
        repository.getUsers(nik, response -> {
            if(response.size() == 0){
                getBinding().noDataPart.setVisibility(View.VISIBLE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            } else {
                getBinding().noDataPart.setVisibility(View.GONE);
            }
            rAdapter.getData(this,  response);
            getBinding().expandableLayout.setLayoutManager(new LinearLayoutManager(this));
            getBinding().expandableLayout.setHasFixedSize(true);
            getBinding().expandableLayout.setNestedScrollingEnabled(false);
            getBinding().expandableLayout.setItemAnimator(new DefaultItemAnimator());
            getBinding().expandableLayout.setAdapter(rAdapter);
        }, responseCode->{
            if (responseCode.equals("Success")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().parentLay.setVisibility(View.VISIBLE);
                        getBinding().loadingDataPart.setVisibility(View.GONE);
                    }
                }, 600);
            } else {
                getBinding().noDataPart.setVisibility(View.VISIBLE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            }
        }, Throwable::printStackTrace);
    }

    void getAllData(String nik){
        repository.getAllUsers(nik, response -> {
            if(response.size()==0){
                getBinding().noDataPart.setVisibility(View.VISIBLE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            } else {
                getBinding().noDataPart.setVisibility(View.GONE);
            }
            rAdapter.getData(this,  response);
            getBinding().expandableLayout.setLayoutManager(new LinearLayoutManager(this));
            getBinding().expandableLayout.setHasFixedSize(true);
            getBinding().expandableLayout.setNestedScrollingEnabled(false);
            getBinding().expandableLayout.setItemAnimator(new DefaultItemAnimator());
            getBinding().expandableLayout.setAdapter(rAdapter);
        }, responseCode->{
            if (responseCode.equals("Success")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().parentLay.setVisibility(View.VISIBLE);
                        getBinding().loadingDataPart.setVisibility(View.GONE);
                    }
                }, 600);
            } else {
                getBinding().noDataPart.setVisibility(View.VISIBLE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            }
        }, Throwable::printStackTrace);
    }

    void getAllDataForSatpam(String nik){
        repository.getAllUsersForSatpam(nik, response -> {
            if(response.size()==0){
                getBinding().noDataPart.setVisibility(View.VISIBLE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            } else {
                getBinding().noDataPart.setVisibility(View.GONE);
            }
            rAdapter.getData(this,  response);
            getBinding().expandableLayout.setLayoutManager(new LinearLayoutManager(this));
            getBinding().expandableLayout.setHasFixedSize(true);
            getBinding().expandableLayout.setNestedScrollingEnabled(false);
            getBinding().expandableLayout.setItemAnimator(new DefaultItemAnimator());
            getBinding().expandableLayout.setAdapter(rAdapter);
        }, responseCode->{
            if (responseCode.equals("Success")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBinding().parentLay.setVisibility(View.VISIBLE);
                        getBinding().loadingDataPart.setVisibility(View.GONE);
                    }
                }, 600);
            } else {
                getBinding().noDataPart.setVisibility(View.VISIBLE);
                getBinding().parentLay.setVisibility(View.VISIBLE);
                getBinding().loadingDataPart.setVisibility(View.GONE);
            }
        }, Throwable::printStackTrace);
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void getCountWaiting() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/waiting_izin_keluar_kantor?nik="+sharedPrefManager.getSpNik();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                String jumlah = response.getString("waiting");
                                getBinding().countInPart.setVisibility(View.VISIBLE);
                                getBinding().countInTv.setText(jumlah);
                            } else {
                                getBinding().countInPart.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

}