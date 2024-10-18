package com.gelora.absensi;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gelora.absensi.adapter.AdapterListPinjamKendaraan;
import com.gelora.absensi.databinding.ActivityListPinjamKendaraanBinding;
import com.gelora.absensi.network.KendaraanRepository;

import java.util.Objects;

public class ListPinjamKendaraan extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    private KendaraanRepository repository;
    private AdapterListPinjamKendaraan rAdapter;
    private Boolean selectedLeftButton = true;
    private Boolean selectedRightButton = false;

    private String currentUserNik;
    private String currentUserIdJabatan;

    private Handler handler = new Handler();

    private int currentRoleIndex = 0;
    private boolean isConnected = true;
    private String preventRepetedCalls = "0";
    private boolean firstNetwork;
    private ActivityListPinjamKendaraanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        currentUserNik = sharedPrefManager.getSpNik();
        currentUserIdJabatan = sharedPrefManager.getSpIdJabatan();

        binding = ActivityListPinjamKendaraanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = new KendaraanRepository(this);
        rAdapter = new AdapterListPinjamKendaraan();

        if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
            getListDataSaya(currentUserNik);
        } else {
            getListDataMasuk(currentUserNik);
        }

        binding.swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListPinjamKendaraan.this.onBackPressed();
            }
        });

        binding.btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ListPinjamKendaraan.this, FormPeminjamanKendaraan.class);
            startActivity(intent);
        });

        handleSelectedButton();

    }

    private void getListDataMasuk(String nik) {
        repository.getListMasuk(nik, response -> {
            if (response.size() == 0) {
                binding.noDataPart.setVisibility(View.VISIBLE);
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            } else {
                binding.noDataPart.setVisibility(View.GONE);
            }
            rAdapter.getData(this, response);
            binding.expandableLayout.setLayoutManager(new LinearLayoutManager(this));
            binding.expandableLayout.setAdapter(rAdapter);

        }, responseCode -> {
            if (responseCode.equals("success")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.parentLay.setVisibility(View.VISIBLE);
                        binding.loadingDataPart.setVisibility(View.GONE);
                    }
                }, 600);
            }
        }, error -> {

        });
    }

    private void getListDataSaya(String nik) {
        repository.getListSaya(nik, response -> {
            if (response.size() == 0) {
                binding.noDataPart.setVisibility(View.VISIBLE);
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            } else {
                binding.noDataPart.setVisibility(View.GONE);
            }
            rAdapter.getData(this, response);
            binding.expandableLayout.setLayoutManager(new LinearLayoutManager(this));

        }, responseCode -> {
            if (responseCode.equals("success")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.parentLay.setVisibility(View.VISIBLE);
                        binding.expandableLayout.setAdapter(rAdapter);
                        binding.loadingDataPart.setVisibility(View.GONE);
                    }
                }, 600);
            }
        }, error -> {

        });

    }

    private void handleSelectedButton() {
        if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
            binding.tabBar.setVisibility(View.GONE);
        } else {
            binding.tabBar.setVisibility(View.VISIBLE);
        }

        binding.permohonanMasuk.setOnClickListener(view1 -> {
            binding.parentLay.setVisibility(View.INVISIBLE);
            binding.loadingDataPart.setVisibility(View.VISIBLE);
            binding.permohonanMasuk.setEnabled(false);
            binding.permohonanSaya.setEnabled(false);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.permohonanMasuk.setEnabled(true);
                binding.permohonanSaya.setEnabled(true);
            }, 1300);

            selectedLeftButton = true;
            selectedRightButton = false;
            if (preventRepetedCalls.equals("0") || preventRepetedCalls.equals("1")) {
                binding.parentLay.setVisibility(View.INVISIBLE);
                binding.loadingDataPart.setVisibility(View.VISIBLE);
                binding.noDataPart.setVisibility(View.GONE);
                getListDataMasuk(currentUserNik);
            } else {
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            }

            if (preventRepetedCalls.equals("0") | preventRepetedCalls.equals("1")) {
                preventRepetedCalls = "2";
            }

            if (selectedLeftButton) {
                binding.permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.selected_yellow))));
                binding.permohonanSaya.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.darker_gray))));

            } else {
                binding.permohonanMasuk.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(ListPinjamKendaraan.this, R.color.darker_gray))));
            }

        });

        binding.permohonanSaya.setOnClickListener(view1 -> {
            binding.parentLay.setVisibility(View.INVISIBLE);
            binding.loadingDataPart.setVisibility(View.VISIBLE);
            binding.permohonanSaya.setEnabled(false);
            binding.permohonanMasuk.setEnabled(false);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.permohonanSaya.setEnabled(true);
                binding.permohonanMasuk.setEnabled(true);
            }, 1300);

            selectedRightButton = true;
            selectedLeftButton = false;

            if (preventRepetedCalls.equals("0") || preventRepetedCalls.equals("2")) {
                binding.parentLay.setVisibility(View.INVISIBLE);
                binding.loadingDataPart.setVisibility(View.VISIBLE);
                binding.noDataPart.setVisibility(View.GONE);
                getListDataSaya(currentUserNik);
            } else {
                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);
            }

            if (preventRepetedCalls.equals("0") || preventRepetedCalls.equals("2")) {
                preventRepetedCalls = "1";
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
        if (!Objects.equals(currentUserIdJabatan, "11") && !Objects.equals(currentUserIdJabatan, "10") && !Objects.equals(currentUserIdJabatan, "41") && !Objects.equals(currentUserIdJabatan, "3") && !Objects.equals(currentUserIdJabatan, "25")) {
            getListDataSaya(currentUserNik);
        } else {
            getListDataMasuk(currentUserNik);
        }
    }

}