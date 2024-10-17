package com.gelora.absensi;

import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.gelora.absensi.databinding.ActivityDetailPinjamKendaraanBinding;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.CurrentUser;
import com.gelora.absensi.model.DetailPkResponse;
import com.gelora.absensi.network.KendaraanRepository;
import com.gelora.absensi.support.FuelGaugeView;
import com.gelora.absensi.viewmodel.ConnectivityViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailPinjamKendaraan extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    private KendaraanRepository repository;

    private ActivityDetailPinjamKendaraanBinding binding;

    private int status;
    private String getIdPK;
    DetailPkResponse detail;


    private String nik1;
    private String nama1;

    private String nik2;
    private String nama2;

    private String nik3;
    private String nama3;

    private String nik4;
    private String nama4;

    private String getCurrentUserNik; // ganti dari shared pref / login session

    private int updateState;
    private ConnectivityViewModel viewModel;
    KAlertDialog pDialog;
    private Boolean isConnected;
    private Boolean isClickSendWhileOffline = false;
    private int i = -1;

    private FuelGaugeView fuelGaugeView;
    private SeekBar fuelSeekBar;
    private TextView percentageTextView;

    private ArrayAdapter<String> autoCompleteAdapter;

    private Handler handler = new Handler();
    private Runnable runnable;
    private Boolean nomerSuratFilled = false;
    private Boolean tanggalFilled = false;

    private Boolean jamFilled = false;

    private Boolean kmKeluarFilled = false;
    private Boolean kmMasukFilled = false;

    private Boolean bbmKeluarFilled = false;
    private Boolean bbmMasukFilled = false;

    private Boolean bersihFilled = false;
    private boolean isSeekBarAdjusted = false;

    private String jam;
    private String bk;
    private String bbm;
    private String km;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPinjamKendaraanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        getCurrentUserNik = sharedPrefManager.getSpNik();

        updateKondisiValidation();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.loadingDataPart.setVisibility(View.VISIBLE);
        fuelGaugeView = binding.fuelGaugeView;
        fuelSeekBar = binding.fuelSeekBar;
        percentageTextView = binding.percentageTextView;
        fuelGaugeView.setFuelLevel(0.5f);
        fuelSeekBar.setProgress(50);
        percentageTextView.setText("Harap Tentukan BBM");

        seekBarInteractive();
        bersihKotorSpinner();

        binding.parentLay.setVisibility(View.INVISIBLE);
        binding.editLayout.setVisibility(View.GONE);

        isConnected = true;


//        binding.tvDetail.setOnClickListener(view -> {
//            // Toggle between the values
//
//            if (getCurrentUserNik.equals("3417150724")) {
//                getCurrentUserNik = "0981010210";
//                userForDebug = "Bu Ranti";
//            } else if (getCurrentUserNik.equals("0981010210")) {
//                getCurrentUserNik = "0071280396";
//                userForDebug = "Bu Maurina";
//            } else if (getCurrentUserNik.equals("0071280396") && userForDebug.equals("Bu Maurina")) {
//                getCurrentUserNik = "0071280396";
//                userForDebug = "Bu Maurina Kadept";
//            } else if (getCurrentUserNik.equals("0071280396")) {
//                getCurrentUserNik = "0363170306";
//                userForDebug = "Pa Ajat";
//            } else {
//                getCurrentUserNik = "3417150724";
//                userForDebug = "Andika";
//            }
//
//            // Optional: Display the current value in the TextView or log it for debugging
//            // This will display the value in tvDetail
//            handleLayoutByStatus(getIdPK);
//            Toast.makeText(DetailPinjamKendaraan.this, userForDebug, Toast.LENGTH_SHORT).show();
//
//        });


        binding.backBtn.setOnClickListener(view -> {
            DetailPinjamKendaraan.this.onBackPressed();

        });
        binding.parentLay.setVisibility(View.GONE);
        repository = new KendaraanRepository(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getIdPK = extras.getString("current_id_pk");

        }
        getDetail(getIdPK);

        handleLayoutByStatus(getIdPK);
    }

    private void getDetail(String idPk) {


        repository.getDetailPk(idPk, response -> {
            // Handle successful response
            detail = response;

            String noSurat = detail.getNoSurat();
            String bulanSurat = detail.getBulanSurat();
            String tahunSurat = detail.getTahunSurat();

            binding.detailNoSurat.setText(noSurat + "/EXP/GAP/" + bulanSurat + "/" + tahunSurat);
            binding.detailNama.setText(detail.getNamaPemohon());

            binding.detailTujuan.setText(detail.getTujuan());
            binding.detailKeperluan.setText(detail.getKeperluan());
            binding.detailJenis.setText(detail.getNamaKendaraan());
            binding.detailNomer.setText(detail.getPlatNomer());
            binding.detailBagian.setText(detail.getBagianPemohon());

            getSignature1(detail.getApp1Nik());
            getSignature2(detail.getApp2Nik());
            getSignature3(detail.getApp3Nik());
            getSignature4(detail.getApp4Nik());


            status = detail.getStatus();

            String inputDateString = detail.getTanggalKeluar();

// Update the input format to match the format of your date string
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale("id", "ID"));

            try {
                // Parse the input date string into a Date object
                Date date = inputFormat.parse(inputDateString);

                // Format the Date object into the desired output format
                String formattedDate = outputFormat.format(date);

                // Set the formatted date to the TextView
                binding.detailTanggal.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                binding.detailTanggal.setText(inputDateString); // Fallback to the original date string if parsing fails
            }


        }, responseCode -> {
            if (responseCode.equals("success")) {


            }
        }, error -> {


        });
    }

    private void getSignature1(String nik) {
        repository.getCurrentUsers(nik, new Response.Listener<CurrentUser>() {
            @Override
            public void onResponse(CurrentUser user) {

                binding.namaPemohon.setText("( " + user.getNama() + " )");
                Glide.with(DetailPinjamKendaraan.this)
                        .load(user.getSignature())// Optional error image if loading fails
                        .into(binding.ttdPemohon);
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String status) {
                Log.d("Status", "API response status: " + status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Failed to fetch current user: " + error.getMessage());
            }
        });
    }

    private void getSignature2(String nik) {
        repository.getCurrentUsers(nik, new Response.Listener<CurrentUser>() {
            @Override
            public void onResponse(CurrentUser user) {

                binding.namaAtasan.setText("( " + user.getNama() + " )");
                Glide.with(DetailPinjamKendaraan.this)
                        .load(user.getSignature())// Optional error image if loading fails
                        .into(binding.ttdAtasan);
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String status) {
                Log.d("Status", "API response status: " + status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Failed to fetch current user: " + error.getMessage());
            }
        });
    }

    private void getSignature3(String nik) {

        repository.getCurrentUsers(nik, new Response.Listener<CurrentUser>() {

            @Override
            public void onResponse(CurrentUser user) {

                binding.namaKabagExp.setText("( " + user.getNama() + " )");
                Glide.with(DetailPinjamKendaraan.this)
                        .load(user.getSignature())// Optional error image if loading fails
                        .into(binding.ttdKabagExp);
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String status) {
                Log.d("Status", "API response status: " + status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Failed to fetch current user: " + error.getMessage());
            }
        });
    }

    private void getSignature4(String nik) {
        repository.getCurrentUsers(nik, new Response.Listener<CurrentUser>() {
            @Override
            public void onResponse(CurrentUser user) {

                binding.namaKadeptLogistik.setText("( " + user.getNama() + " )");
                Glide.with(DetailPinjamKendaraan.this)
                        .load(user.getSignature())// Optional error image if loading fails
                        .into(binding.ttdKadeptLogistik);
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String status) {
                Log.d("Status", "API response status: " + status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Failed to fetch current user: " + error.getMessage());
            }
        });
    }

    private void updateSign(String idPk) {
        binding.parentLay.setVisibility(View.INVISIBLE);
        if (updateState == 0) {
            repository.updateApprovalPk1(idPk, response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();
            }, error -> {

            });
        } else if (updateState == 2) {
            repository.updateApprovalPk2(idPk, getCurrentUserNik, "0", response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();
            }, error -> {

            });
        } else if (updateState == 3) {
            repository.updateApprovalPk2(idPk, getCurrentUserNik, "1", response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();
            }, error -> {

            });
        } else if (updateState == 4) {
            repository.updateApprovalPk3(idPk, getCurrentUserNik, "0", response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();
            }, error -> {

            });


        } else if (updateState == 5) {

            if (getCurrentUserNik.equals("0071280396")) {
                repository.updateApprovalPk3(idPk, getCurrentUserNik, "1", response -> {
                    handleLayoutByStatus(idPk);
                    pDialog.dismiss();

                    repository.updateApprovalPk4(idPk, getCurrentUserNik, "1", response2 -> {
                        handleLayoutByStatus(idPk);
                        getSignature3(getCurrentUserNik);
                        getSignature4(getCurrentUserNik);
                        pDialog.dismiss();

                    }, error -> {

                    });
                }, error -> {

                });


            } else {
                repository.updateApprovalPk3(idPk, getCurrentUserNik, "1", response -> {
                    handleLayoutByStatus(idPk);
                    pDialog.dismiss();
                }, error -> {

                });
            }

        } else if (updateState == 6) {
            repository.updateApprovalPk4(idPk, getCurrentUserNik, "0", response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();

            }, error -> {

            });

        } else if (updateState == 7) {
            repository.updateApprovalPk4(idPk, getCurrentUserNik, "1", response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();

            }, error -> {

            });
        } else if (updateState == 8) {


            String getBk = String.valueOf(binding.bersihSpinner.getSelectedItemPosition());
            if (getBk.equals("Bersih")) {
                bk = "1";
            } else {
                bk = "0";
            }
            km = binding.km.getText().toString();
            bbm = binding.percentageTextView.getText().toString();
            repository.updateKondisiPk(idPk, "0", bk, bbm, km, jam, response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();
                Intent intent = new Intent(DetailPinjamKendaraan.this, ListPinjamKendaraan.class);
                startActivity(intent);


            }, error -> {

            });
        } else if (updateState == 9) {


            String getBk = String.valueOf(binding.bersihSpinner.getSelectedItemPosition());
            if (getBk.equals("Bersih")) {
                bk = "1";
            } else {
                bk = "0";
            }
            km = binding.km.getText().toString();
            bbm = binding.percentageTextView.getText().toString();
            repository.updateKondisiPk(idPk, "1", bk, bbm, km, jam, response -> {
                handleLayoutByStatus(idPk);
                pDialog.dismiss();
                Intent intent = new Intent(DetailPinjamKendaraan.this, ListPinjamKendaraan.class);
                startActivity(intent);

            }, error -> {

            });
        }

    }

    private void updateKondisiValidation() {


        binding.km.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Optional: Perform actions before the text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Optional: Handle changes to the text while it is being typed
                if (s.toString().trim().isEmpty()) {
                    kmKeluarFilled = false;
                    kmMasukFilled = false;
                } else {
                    kmKeluarFilled = true;
                    kmMasukFilled = true;// Clear the TextView if the input is empty
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if the text is not empty before updating the TextView

            }
        });
    }


    private void handleLayoutByStatus(String idPk) {

        repository.getDetailPk(idPk, response -> {
            detail = response;
            Log.d("statusPK", String.valueOf(detail.getStatus()));
            binding.kondisiKeluarLayout.setVisibility(View.GONE);
            binding.kondisiMasukLayout.setVisibility(View.GONE);
            binding.kondisiText.setVisibility(View.GONE);


            if (detail.getStatus() == 8) {
                binding.leftBtn.setVisibility(View.GONE);
                binding.rightBtn.setVisibility(View.GONE);
            }
            if (detail.getStatus() != 9) {
                timePicker();
            }
            if (detail.getStatus() == 9) {
                binding.kirimBtn.setVisibility(View.GONE);
                binding.editLayout.setVisibility(View.GONE);
                binding.fuelSeekBar.setVisibility(View.GONE);


            }
            if (detail.getStatus() == 7) {
                binding.editLayout.setVisibility(View.VISIBLE);
                binding.kirimBtn.setOnClickListener(view -> {
                    if (jamFilled && kmMasukFilled && bersihFilled && isSeekBarAdjusted) {
                        kAlertDialog(8, idPk);
                    } else {
                        fillDataAlert();
                    }
                });
            }
            if (detail.getStatus() == 8) {
                binding.kondisiText.setVisibility(View.VISIBLE);
                binding.editLayout.setVisibility(View.VISIBLE);
                binding.kondisiTitle.setText("Kondisi Kendaraan Saat Masuk");
                binding.tvJam.setText("Jam Masuk");
                binding.kirimBtn.setOnClickListener(view -> {
                    if (jamFilled && kmMasukFilled && bersihFilled && isSeekBarAdjusted) {
                        kAlertDialog(9, idPk);
                    } else {
                        fillDataAlert();
                    }
                });
                binding.kondisiKeluarLayout.setVisibility(View.VISIBLE);
                String jamKeluar = detail.getJamKeluar();
                if (jamKeluar != null && jamKeluar.length() >= 5) {
                    jamKeluar = jamKeluar.substring(0, 5);
                    binding.detailJamKeluar.setText(jamKeluar);
                }
                binding.detailKmKeluar.setText(detail.getKmKeluar());
                binding.percentageKeluar.setText(detail.getBbmKeluar() + "%");
                binding.fuelGaugeViewKeluar.setFuelLevel(1 - Float.parseFloat(detail.getBbmKeluar()) / 100);
                if (detail.getBkMasuk().equals("1")) {
                    binding.detailBersihKeluar.setText("Bersih");
                } else {
                    binding.detailBersihKeluar.setText("Kotor");
                }
            }
            if (detail.getStatus() == 9) {
                binding.kondisiText.setVisibility(View.VISIBLE);
                binding.editLayout.setVisibility(View.GONE);
                binding.buttonLayout.setVisibility(View.GONE);
                binding.kondisiKeluarLayout.setVisibility(View.VISIBLE);
                binding.kondisiMasukLayout.setVisibility(View.VISIBLE);
                String jamMasuk = detail.getJamKeluar();
                if (jamMasuk != null && jamMasuk.length() >= 5) {
                    jamMasuk = jamMasuk.substring(0, 5);
                    binding.detailJamMasuk.setText(jamMasuk);
                }
                binding.detailKmMasuk.setText(detail.getKmMasuk());
                binding.percentageKeluar.setText(detail.getBbmKeluar() + "%");
                binding.percentageMasuk.setText(detail.getBbmMasuk() + "%");
                binding.fuelGaugeViewKeluar.setFuelLevel(1 - Float.parseFloat(detail.getBbmKeluar()) / 100);
                binding.fuelGaugeViewMasuk.setFuelLevel(1 - Float.parseFloat(detail.getBbmMasuk()) / 100);
                if (detail.getBkMasuk().equals("1")) {
                    binding.detailBersihMasuk.setText("Bersih");
                } else {
                    binding.detailBersihMasuk.setText("Kotor");

                }

                String jamKeluar = detail.getJamKeluar();


                if (jamMasuk != null && jamMasuk.length() >= 5) {
                    jamKeluar = jamKeluar.substring(0, 5);
                    binding.detailJamKeluar.setText(jamKeluar);
                }


                binding.detailKmKeluar.setText(detail.getKmKeluar());

                if (detail.getBkMasuk().equals("1")) {
                    binding.detailBersihKeluar.setText("Bersih");
                } else {
                    binding.detailBersihKeluar.setText("Kotor");
                }

                binding.pdfLayout.setVisibility(View.VISIBLE);
                binding.pdfBtn.setOnClickListener(view->{
                    String pdfUrl = "https://family.geloraaksara.co.id/gap-f/api/print_peminjaman_kendaraan_mobile/" + idPk; // Replace with your actual URL
                    String fileName = "Surat_Peminjaman_Kendaraan_No_" + detail.getNoSurat() + ".pdf" ; // Customize this if needed

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
                    request.setTitle("Downloading PDF");
                    request.setDescription("Downloading " + fileName);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                    DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                        Toast.makeText(view.getContext(), "Mengunduh PDF", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Failed to start download.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            if (detail.getStatus() == 2 || detail.getStatus() == 4 || detail.getStatus() == 6) {
                binding.stampleImg.setImageResource(R.drawable.rejected_img);

            } else if (detail.getStatus() == 7) {
                binding.stampleImg.setImageResource(R.drawable.accepted_img);

            } else if (detail.getStatus() == 0) {
                binding.stampleImg.setImageResource(R.drawable.canceled_img);

            }
            if (detail.getStatus() == 0) {
                binding.rightBtn.setVisibility(View.GONE);
                binding.leftBtn.setVisibility(View.GONE);
                binding.batas.setVisibility(View.GONE);
            }

            if (detail.getStatus() == 7) {
                binding.rightBtn.setVisibility(View.GONE);
                binding.leftBtn.setVisibility(View.GONE);
                binding.batas.setVisibility(View.GONE);
            }

            if (detail.getStatus() == 2) {
                binding.rightBtn.setVisibility(View.GONE);
                binding.leftBtn.setVisibility(View.GONE);
                binding.batas.setVisibility(View.GONE);

            }

            if (detail.getStatus() == 4) {
                getSignature3(detail.getApp3Nik());
                getSignature4(detail.getApp4Nik());
                binding.rightBtn.setVisibility(View.GONE);
                binding.leftBtn.setVisibility(View.GONE);
                binding.batas.setVisibility(View.GONE);

            }

            if (detail.getStatus() == 6) {
                getSignature3(detail.getApp3Nik());
                getSignature4(detail.getApp4Nik());
                binding.rightBtn.setVisibility(View.GONE);
                binding.leftBtn.setVisibility(View.GONE);
                binding.batas.setVisibility(View.GONE);

            }
            if (getCurrentUserNik.equals(detail.getApp1Nik())) {

                if (detail.getStatus() != 1) {
                    binding.rightBtn.setVisibility(View.GONE);
                    binding.leftBtn.setVisibility(View.GONE);
                    binding.batas.setVisibility(View.GONE);
                }

                if (detail.getStatus() == 1) {
                    binding.rightBtn.setVisibility(View.GONE);
                    binding.batas.setVisibility(View.GONE);
                    binding.leftBtn.setText("Batalkan");

                    binding.leftBtn.setOnClickListener(view -> {
                        kAlertDialog(0, idPk);
                    });
                }


            } else if (!getCurrentUserNik.equals(detail.getApp1Nik())) {
                if (detail.getStatus() == 1) {
                    binding.rightBtn.setVisibility(View.VISIBLE);
                    binding.batas.setVisibility(View.VISIBLE);
                    binding.leftBtn.setText("Tolak");
                    binding.leftBtn.setOnClickListener(view -> {
                        kAlertDialog(2, idPk);

                    });

                    binding.rightBtn.setOnClickListener(view -> {
                        kAlertDialog(3, idPk);

                    });

                } else if (detail.getStatus() == 3) {

                    getSignature2(detail.getApp2Nik());

                    binding.leftBtn.setVisibility(View.VISIBLE);
                    binding.rightBtn.setVisibility(View.VISIBLE);
                    binding.batas.setVisibility(View.VISIBLE);
                    binding.leftBtn.setText("Tolak");

                    binding.leftBtn.setOnClickListener(view -> {
                        kAlertDialog(4, idPk);

                    });


                    binding.rightBtn.setOnClickListener(view -> {
                        kAlertDialog(5, idPk);

                    });
                } else if (detail.getStatus() == 5) {
                    binding.leftBtn.setVisibility(View.VISIBLE);


                    getSignature3(detail.getApp3Nik());
                    getSignature4(detail.getApp4Nik());
                    binding.rightBtn.setVisibility(View.VISIBLE);
                    binding.batas.setVisibility(View.VISIBLE);
                    binding.leftBtn.setText("Tolak");
                    binding.leftBtn.setOnClickListener(view -> {
                        kAlertDialog(6, idPk);

                    });

                    binding.rightBtn.setOnClickListener(view -> {
                        kAlertDialog(7, idPk);

                    });
                } else if (detail.getStatus() == 7) {
                    binding.leftBtn.setVisibility(View.VISIBLE);

                    getSignature3(detail.getApp3Nik());
                    getSignature4(detail.getApp4Nik());
                    binding.rightBtn.setVisibility(View.VISIBLE);
                    binding.batas.setVisibility(View.VISIBLE);
                    binding.leftBtn.setText("Tolak");
                    binding.leftBtn.setOnClickListener(view -> {
                        kAlertDialog(6, idPk);

                    });

                    binding.rightBtn.setOnClickListener(view -> {
                        kAlertDialog(7, idPk);

                    });
                }

                if (getCurrentUserNik.equals(detail.getApp2Nik())) {
                    if (detail.getApp2Nik().isEmpty()) {
                        if (detail.getStatus() != 2 || detail.getStatus() != 3) {

                            binding.leftBtn.setVisibility(View.VISIBLE);

                            binding.rightBtn.setVisibility(View.VISIBLE);
                            binding.leftBtn.setVisibility(View.VISIBLE);
                            binding.batas.setVisibility(View.VISIBLE);
                            binding.leftBtn.setText("Batalkan");
                        } else {
                            binding.rightBtn.setVisibility(View.GONE);
                            binding.leftBtn.setVisibility(View.GONE);
                            binding.batas.setVisibility(View.GONE);

                        }

                    } else {
                        binding.rightBtn.setVisibility(View.GONE);
                        binding.leftBtn.setVisibility(View.GONE);
                        binding.batas.setVisibility(View.GONE);
                    }


                }

                if (getCurrentUserNik.equals(detail.getApp3Nik())) {
                    if (detail.getApp3Nik().isEmpty()) {
                        if (detail.getStatus() != 3 || detail.getStatus() != 4 || detail.getStatus() != 7 || detail.getStatus() != 8) {

                            binding.leftBtn.setVisibility(View.VISIBLE);

                            binding.rightBtn.setVisibility(View.VISIBLE);
                            binding.leftBtn.setVisibility(View.VISIBLE);
                            binding.batas.setVisibility(View.VISIBLE);
                            binding.leftBtn.setText("Tolak");
                        } else {
                            binding.rightBtn.setVisibility(View.GONE);
                            binding.leftBtn.setVisibility(View.GONE);
                            binding.batas.setVisibility(View.GONE);

                        }
                    } else {
                        binding.rightBtn.setVisibility(View.GONE);
                        binding.leftBtn.setVisibility(View.GONE);
                        binding.batas.setVisibility(View.GONE);
                    }


                }

                if (getCurrentUserNik.equals(detail.getApp4Nik())) {
                    if (detail.getApp4Nik().isEmpty()) {
                        if (detail.getStatus() != 5 || detail.getStatus() != 6) {

                            binding.leftBtn.setVisibility(View.VISIBLE);

                            binding.rightBtn.setVisibility(View.VISIBLE);
                            binding.leftBtn.setVisibility(View.VISIBLE);
                            binding.batas.setVisibility(View.VISIBLE);
                            binding.leftBtn.setText("Tolak");
                        } else {
                            binding.rightBtn.setVisibility(View.GONE);
                            binding.leftBtn.setVisibility(View.GONE);
                            binding.batas.setVisibility(View.GONE);

                        }
                    } else {
                        binding.rightBtn.setVisibility(View.GONE);
                        binding.leftBtn.setVisibility(View.GONE);
                        binding.batas.setVisibility(View.GONE);
                    }


                }


            }


        }, responseCode -> {
            if (responseCode.equals("success")) {

                binding.parentLay.setVisibility(View.VISIBLE);
                binding.loadingDataPart.setVisibility(View.GONE);

            }
        }, error -> {


        });

    }

    private void timePicker() {
        binding.jamPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailPinjamKendaraan.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tanggalFilled = true;

                                // Get the current date in the format yyyy-MM-dd
                                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());

                                // Format the time to HH:mm:ss
                                String formattedTime = String.format("%02d:%02d:00", hourOfDay, minute);

                                // Combine date and time to create a timestamp
                                String timestamp = currentDate + " " + formattedTime;

                                // Optionally, set only the time (HH:mm) to the TextView for display
                                binding.jamKeluar.setText(String.format("%02d:%02d", hourOfDay, minute));


                                jamFilled = true;
                                // Set the 'jam' variable to the SQL-compatible timestamp format
                                jam = String.format("%02d:%02d", hourOfDay, minute) + ":00";
                            }


                        }, hour, minute, false);

                timePickerDialog.show();
            }
        });

    }

    private void seekBarInteractive() {
        fuelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Reverse the SeekBar progress: 0 = full, 100 = empty
                float reversedProgress = 1.0f - (progress / 100f); // Invert the progress value
                fuelGaugeView.setFuelLevel(reversedProgress);

                isSeekBarAdjusted = true;
                // Update the percentage TextView with the current progress value
                percentageTextView.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle event when user starts dragging the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Handle event when user stops dragging the SeekBar
            }
        });


    }


    private void bersihKotorSpinner() {
        // Create a custom adapter for the Spinner
        binding.bersihSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Perform actions based on the selected item
                if (selectedItem.equals("Pilih")) { // Skip the "Pilih" option if it's selected
                    bersihFilled = false;
                } else {
                    bersihFilled = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        ArrayAdapter<String> kondisiAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[]{"Pilih", "Bersih", "Kotor"}) {

            @Override
            public boolean isEnabled(int position) {
                // Disable the "Pilih" option (position 0)
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                // Grey out the "Pilih" option to indicate that it's not selectable
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }

                return view;
            }


        };

        kondisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        binding.bersihSpinner.setAdapter(kondisiAdapter);
    }


    private void kAlertDialog(int newUpdateState, String idPk) {
        new KAlertDialog(DetailPinjamKendaraan.this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Perhatian")
                .setContentText("Kirim permohonan sekarang?")
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
                        pDialog = new KAlertDialog(DetailPinjamKendaraan.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                        pDialog.show();
                        pDialog.setCancelable(false);
                        new CountDownTimer(1300, 800) {
                            public void onTick(long millisUntilFinished) {
                                i++;
                                switch (i) {
                                    case 0:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (DetailPinjamKendaraan.this, R.color.colorGradien));
                                        break;
                                    case 1:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (DetailPinjamKendaraan.this, R.color.colorGradien2));
                                        break;
                                    case 2:
                                    case 6:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (DetailPinjamKendaraan.this, R.color.colorGradien3));
                                        break;
                                    case 3:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (DetailPinjamKendaraan.this, R.color.colorGradien4));
                                        break;
                                    case 4:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (DetailPinjamKendaraan.this, R.color.colorGradien5));
                                        break;
                                    case 5:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (DetailPinjamKendaraan.this, R.color.colorGradien6));
                                        break;
                                }
                            }

                            public void onFinish() {
                                i = -1;

                                if (isConnected) {
                                    isClickSendWhileOffline = false;
                                    updateState = newUpdateState;

                                    updateSign(idPk);


                                    handleLayoutByStatus(idPk);
                                } else {
                                    isClickSendWhileOffline = true;

                                }


                            }
                        }.start();


                    }
                })
                .show();
    }


    private void fillDataAlert() {
        new KAlertDialog(DetailPinjamKendaraan.this, KAlertDialog.ERROR_TYPE)
                .setTitleText("Perhatian")
                .setContentText("Harap isi semua data")
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