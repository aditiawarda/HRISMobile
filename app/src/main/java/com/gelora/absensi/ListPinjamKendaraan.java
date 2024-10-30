package com.gelora.absensi;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gelora.absensi.adapter.AdapterListPinjamKendaraan;
import com.gelora.absensi.databinding.ActivityListPinjamKendaraanBinding;
import com.gelora.absensi.network.ApiClient;
import com.gelora.absensi.network.KendaraanRepository;
import com.gelora.absensi.viewmodel.ButtonViewModel;
import com.gelora.absensi.viewmodel.ConnectivityViewModel;

import org.aviran.cookiebar2.CookieBar;

import java.util.Objects;

public class ListPinjamKendaraan extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    private KendaraanRepository repository;
    private AdapterListPinjamKendaraan rAdapter;
    private Boolean selectedLeftButton = true;
    private Boolean selectedRightButton = false;
    private Boolean isApiCallInProgress = false;
    private Boolean isLeftApiCallInProgress = false;
    private Boolean isRightApiCallInProgress = false;
    private String currentUserNik;
    private String currentUserIdJabatan;
    private ButtonViewModel buttonViewModel;
    private Handler handler = new Handler();
    private ConnectivityViewModel viewModel;
    private int currentRoleIndex = 0;
    private boolean isConnected;
    private String preventRepetedCalls = "0";
    private boolean firstNetwork;
    private ApiClient apiClient;
    private ActivityListPinjamKendaraanBinding binding;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListPinjamKendaraanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        currentUserNik = sharedPrefManager.getSpNik();
        currentUserIdJabatan = sharedPrefManager.getSpIdJabatan();
        repository = new KendaraanRepository(this);
        rAdapter = new AdapterListPinjamKendaraan();
        buttonViewModel = new ViewModelProvider(this).get(ButtonViewModel.class);

        viewModel = new ViewModelProvider(ListPinjamKendaraan.this).get(ConnectivityViewModel.class);

        apiClient = new ApiClient(this);
        checkInternet();

        buttonViewModel.getSelectedLeftButton().observe(this, isSelected -> {
            if (isSelected) {
                binding.noDataPart2.setVisibility(View.GONE);
                binding.noDataPart.setVisibility(View.GONE);
                binding.expandableLayout.setVisibility(View.VISIBLE);
                binding.expandableLayout2.setVisibility(View.GONE);
                getListDataMasuk(currentUserNik);
                binding.addBtnPart.setVisibility(View.GONE);
            }
        });

        buttonViewModel.getSelectedRightButton().observe(this, isSelected -> {
            if (isSelected) {
                binding.noDataPart2.setVisibility(View.GONE);
                binding.noDataPart.setVisibility(View.GONE);
                binding.expandableLayout.setVisibility(View.GONE);
                binding.expandableLayout2.setVisibility(View.VISIBLE);
                getListDataSaya(currentUserNik);
                binding.addBtnPart.setVisibility(View.VISIBLE);
            }
        });

        binding.swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        binding.swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.parentLay.setVisibility(View.GONE);
                binding.loadingDataPart.setVisibility(View.VISIBLE);
                binding.noDataPart.setVisibility(View.GONE);
                binding.noDataPart2.setVisibility(View.GONE);
                if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
                    getListDataSaya(currentUserNik);
                } else {
                    getListDataMasuk(currentUserNik);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeToRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        binding.backBtn.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ListPinjamKendaraan.this, FormPeminjamanKendaraan.class);
            startActivity(intent);
        });

        handleSelectedButton();

    }

    private void getListDataMasuk(String nik) {
        if (isLeftApiCallInProgress) return;
        isLeftApiCallInProgress = true;
        repository.getListMasuk(nik, response -> {
            isLeftApiCallInProgress = false;
            if (response.size() == 0) {
                if (selectedLeftButton) {
                    binding.noDataPart.setVisibility(View.VISIBLE);
                }
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            } else {
                binding.noDataPart.setVisibility(View.GONE);
            }
            rAdapter.getData(this, response);
            binding.expandableLayout.setLayoutManager(new LinearLayoutManager(this));
        }, responseCode -> {
            if (responseCode.equals("success")) {
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
                isLeftApiCallInProgress = false;
                binding.expandableLayout.setAdapter(rAdapter);
            }
        }, error -> {
            isLeftApiCallInProgress = false;
        });
    }

    private void getListDataSaya(String nik) {
        if (isRightApiCallInProgress) return;
        isRightApiCallInProgress = true;
        repository.getListSaya(nik, response -> {
            isRightApiCallInProgress = false;
            if (response.size() == 0) {
                if (selectedRightButton) {
                    binding.noDataPart2.setVisibility(View.VISIBLE);
                }
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            } else {
                binding.noDataPart2.setVisibility(View.GONE);
            }
            rAdapter.getData(this, response);
            binding.expandableLayout2.setLayoutManager(new LinearLayoutManager(this));
        }, responseCode -> {
            if (responseCode.equals("success")) {
                binding.parentLay.setVisibility(View.VISIBLE);
                isRightApiCallInProgress = false;
                binding.expandableLayout2.setAdapter(rAdapter);
                binding.loadingDataPart.setVisibility(View.GONE);
            }
        }, error -> {
            isRightApiCallInProgress = false;
        });
    }

    private void connectionFailed() {
        CookieBar.build(ListPinjamKendaraan.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();

    }

    private void checkInternet() {
        viewModel.getIsConnected().observe(this, isConnectedNow -> {
            handler.removeCallbacks(runnable);
            if (isConnectedNow) {
                isConnected = true;
                if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
                    getListDataSaya(currentUserNik);
                } else {
                    handleSelectedButton();
                    if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
                        getListDataSaya(currentUserNik);
                    } else {
                        getListDataMasuk(currentUserNik);
                    }
                }
            } else {
                isConnected = false;
                connectionFailed();
            }
            handler.postDelayed(runnable, 500);
        });
    }

    private void handleSelectedButton() {
        if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
            binding.tabBar.setVisibility(View.GONE);
            selectedRightButton = true;

            int top = 70;
            int paddingTop = dpToPixels(top, getBaseContext());
            binding.parentLay.setPadding(0,paddingTop,0,0);
            binding.addBtnPart.setVisibility(View.VISIBLE);

            int top2 = 140;
            int paddingTop2 = dpToPixels(top2, getBaseContext());
            binding.loadingDataPart.setPadding(0,paddingTop2,0,0);

            int top3 = 120;
            int paddingTop3 = dpToPixels(top3, getBaseContext());
            binding.noDataPart2.setPadding(0,paddingTop3,0,0);
        } else {
            binding.tabBar.setVisibility(View.VISIBLE);
            selectedLeftButton = true;

            int top = 10;
            int paddingTop = dpToPixels(top, getBaseContext());
            binding.parentLay.setPadding(0,paddingTop,0,0);
            binding.addBtnPart.setVisibility(View.GONE);
        }

        binding.permohonanMasuk.setOnClickListener(view1 -> {
            apiClient.getRequestQueue().cancelAll("get_list_saya_pk");
            selectedLeftButton = true;
            selectedRightButton = false;
            if (preventRepetedCalls.equals("0") || preventRepetedCalls.equals("1")) {
                binding.parentLay.setVisibility(View.INVISIBLE);
                binding.loadingDataPart.setVisibility(View.VISIBLE);
                binding.noDataPart.setVisibility(View.GONE);
                buttonViewModel.selectLeftButton();
                preventRepetedCalls = "2";
            } else {
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            }

            if (selectedLeftButton) {
                binding.permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.selected_yellow))));
                binding.permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.darker_gray))));
            } else {
                binding.permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.darker_gray))));
            }
        });

        binding.permohonanSaya.setOnClickListener(view1 -> {
            apiClient.getRequestQueue().cancelAll("get_list_masuk_pk");
            binding.parentLay.setVisibility(View.INVISIBLE);
            binding.loadingDataPart.setVisibility(View.VISIBLE);
            selectedRightButton = true;
            selectedLeftButton = false;
            if (preventRepetedCalls.equals("0") || preventRepetedCalls.equals("2")) {
                binding.parentLay.setVisibility(View.INVISIBLE);
                binding.loadingDataPart.setVisibility(View.VISIBLE);
                binding.noDataPart.setVisibility(View.GONE);
                buttonViewModel.selectRightButton();
                preventRepetedCalls = "1";
            } else {
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            }

            if (selectedRightButton) {
                binding.permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.selected_yellow))));
                binding.permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.darker_gray))));
            } else {
                binding.permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.darker_gray))));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.parentLay.setVisibility(View.GONE);
        binding.loadingDataPart.setVisibility(View.VISIBLE);
        binding.noDataPart.setVisibility(View.GONE);
        binding.noDataPart2.setVisibility(View.GONE);
        if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
            getListDataSaya(currentUserNik);
        } else {
            getListDataMasuk(currentUserNik);
            if (selectedRightButton) {
                getListDataSaya(currentUserNik);
            }
        }
    }

    public int dpToPixels(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

}