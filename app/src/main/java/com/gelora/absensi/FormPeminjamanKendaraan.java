package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gelora.absensi.databinding.ActivityFormPeminjamanKendaraanBinding;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.CurrentUser;
import com.gelora.absensi.network.ApiClient;
import com.gelora.absensi.network.KendaraanRepository;
import com.gelora.absensi.support.FuelGaugeView;
import com.gelora.absensi.viewmodel.ConnectivityViewModel;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FormPeminjamanKendaraan extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    private ActivityFormPeminjamanKendaraanBinding binding;
    private FuelGaugeView fuelGaugeView;
    private SeekBar fuelSeekBar;
    private TextView percentageTextView;
    private KendaraanRepository repository;
    private ArrayAdapter<String> autoCompleteAdapter;
    private String nik;
    private Handler handler = new Handler();
    private Runnable runnable;
    private Boolean nomerSuratFilled = false;
    private Boolean tanggalFilled = false;
    private Boolean tujuanFilled = false;
    private Boolean keperluanFilled = false;
    private Boolean jenisFilled = false;
    private Boolean platValid = false;
    private Boolean noSuratValid = false;
    private ConnectivityViewModel viewModel;
    private Boolean nomerKendaraanFilled = false;
    private Boolean refreshTap = false;
    KAlertDialog pDialog;
    private Boolean isConnected;
    private String monthRoman;
    private int currentYear;
    private String postDate;
    private int idKendaraan;
    private Boolean isClickSendWhileOffline = false;
    private int i = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormPeminjamanKendaraanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager = new SharedPrefManager(this);
        nik = sharedPrefManager.getSpNik();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormPeminjamanKendaraan.this.onBackPressed();
            }
        });

        fuelGaugeView = binding.fuelGaugeView;
        fuelSeekBar = binding.fuelSeekBar;
        percentageTextView = binding.percentageTextView;
        viewModel = new ViewModelProvider(FormPeminjamanKendaraan.this).get(ConnectivityViewModel.class);

        checkInternet();
        repository = new KendaraanRepository(this);

        fuelGaugeView.setFuelLevel(0.5f);
        fuelSeekBar.setProgress(50);
        percentageTextView.setText("50%");

        fetchCurrentUserData(nik);

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        currentYear = calendar.get(Calendar.YEAR);

        binding.detailKendaraan.setVisibility(View.GONE);

        monthRoman = convertMonthToRoman(month + 1);
        String formatSurat = "/EXP/GAP/" + monthRoman + "/" + (currentYear % 100);
        binding.tanggalSurat.setText(formatSurat);

        binding.swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        binding.swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTap = true;
                binding.nomerSurat.clearFocus();
                binding.nomerSurat.setText("");

                binding.hari.setText("");
                binding.tujuan.setText("");
                binding.keperluan.setText("");
                tanggalFilled = false;
                nomerKendaraanFilled = false;
                nomerSuratFilled = false;
                keperluanFilled = false;
                tujuanFilled = false;
                binding.noKendaraan.setText("");
                binding.detailKendaraan.setVisibility(View.GONE);
                binding.noSuratValidation.setVisibility(View.GONE);
                binding.platValidation.setVisibility(View.GONE);
                binding.platValidation.setText("Memuat data...");
                binding.noSuratValidation.setText("Memuat data...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeToRefreshLayout.setRefreshing(false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshTap = false;
                            }
                        }, 1000);
                    }
                }, 500);
            }
        });

        datePicker();
        seekBarInteractive();
        timePicker();
        bersihKotorSpinner();
        kendaraanSpinner();
        setupAutoCompleteTextView();
        handleSubmit();
        nomerSuratTextWatcher();
        formValidation();

    }

    private void nomerSuratTextWatcher() {
        binding.nomerSurat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!refreshTap){
                    nomerSuratValidation();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void nomerSuratValidation() {
        String noSuratInput = binding.nomerSurat.getText().toString();
        String url = "https://family.geloraaksara.co.id/gap-f/api/cek_nomer_surat_pinjam_kendaraan=" + noSuratInput;
        Log.d("API Request", "Fetching suggestions from: " + url);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Valid Response", response);
                    if (response.trim().equals("unique")) {
                        noSuratValid = true;
                        enableSubmitByNoSurat();
                    } else {
                        noSuratValid = false;
                        disableSubmitByNoSurat();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("API Error", "Error fetching suggestions: " + error.toString());
                    disableSubmitByNoSurat();
                }
        );
        ApiClient.getInstance(this).addToRequestQueue(request);
    }

    private String convertMonthToRoman(int month) {
        switch (month) {
            case 1:
                return "I";  // January
            case 2:
                return "II"; // February
            case 3:
                return "III"; // March
            case 4:
                return "IV"; // April
            case 5:
                return "V";  // May
            case 6:
                return "VI"; // June
            case 7:
                return "VII"; // July
            case 8:
                return "VIII"; // August
            case 9:
                return "IX";  // September
            case 10:
                return "X";  // October
            case 11:
                return "XI"; // November
            case 12:
                return "XII"; // December
            default:
                return "";   // Should never happen
        }
    }

    private void datePicker() {
        binding.keluarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormPeminjamanKendaraan.this, R.style.datePickerStyle,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
                                String formattedDate = dateFormat.format(selectedDate.getTime());
                                SimpleDateFormat dateConversion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                tanggalFilled = true;
                                postDate = dateConversion.format(selectedDate.getTime());
                                binding.hari.setText(formattedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void timePicker() {
        binding.jamPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(FormPeminjamanKendaraan.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tanggalFilled = true;
                                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat(" dd-MM-yyyy").format(Calendar.getInstance().getTime());
                                binding.jamKeluar.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

    }

    private void seekBarInteractive() {
        fuelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float reversedProgress = 1.0f - (progress / 100f);
                fuelGaugeView.setFuelLevel(reversedProgress);
                percentageTextView.setText(progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void fetchCurrentUserData(String nik) {
        repository.getCurrentUsers(nik, new Response.Listener<CurrentUser>() {
            @Override
            public void onResponse(CurrentUser user) {
                binding.etNama.setText(user.getNama());
                binding.etBagian.setText(user.getBagian());
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

    private void kendaraanSpinner() {
        List<String> kendaraanOptions = new ArrayList<>(Arrays.asList("Pilih Jenis Kendaraan", "MOBIL", "SEPEDA MOTOR", "TRUCK", "FORKLIFT", "HANDPALLET", "TROLLEY"));
        ArrayAdapter<String> kendaraanAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kendaraanOptions) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };

        kendaraanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.kendaraanSpinner.setAdapter(kendaraanAdapter);
        binding.kendaraanSpinner.setSelection(0);
        binding.kendaraanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void formValidation() {
        binding.nomerSurat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nomerSuratFilled = true;
                } else {
                    nomerSuratFilled = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.tujuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tujuanFilled = true;
                } else {
                    tujuanFilled = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.keperluan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    keperluanFilled = true;
                } else {
                    keperluanFilled = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.noKendaraan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nomerKendaraanFilled = true;
                } else {
                    nomerKendaraanFilled = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void bersihKotorSpinner() {
        ArrayAdapter<String> kondisiAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Bersih", "Kotor"});
        kondisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bersihSpinner.setAdapter(kondisiAdapter);
    }

    private boolean isSuggestionSelected = false;
    private boolean isFocused = false;

    private void setupAutoCompleteTextView() {
        binding.noKendaraan.setThreshold(1);
        binding.noKendaraan.setOnFocusChangeListener((v, hasFocus) -> {
            isFocused = hasFocus;
        });

        binding.noKendaraan.setOnItemClickListener((parent, view, position, id) -> {
            isSuggestionSelected = true;
            binding.noKendaraan.clearFocus();
            hideKeyboard(binding.noKendaraan);
        });

        binding.noKendaraan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isSuggestionSelected) {
                    isSuggestionSelected = false;
                }
                if (!isFocused || isSuggestionSelected) {
                    return;
                } else {
                    if (s.length() > 0 && s.length() < 7) {
                        String selectedJenisKendaraan = binding.kendaraanSpinner.getSelectedItem().toString();
                        fetchSuggestions(s.toString(), selectedJenisKendaraan);
                    }
                }
                disableSubmitByPlat();
                platValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isSuggestionSelected) {
                    isSuggestionSelected = false;
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void platValidation() {
        String platInput = binding.noKendaraan.getText().toString();
        String url = "https://family.geloraaksara.co.id/gap-f/api/cek_kendaraan_by_plat?nik=" + nik + "&no_plat="
                + platInput;
        Log.d("API Request", "Fetching suggestions from: " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("Valid Response", response.toString());
                        if (response.getString("status").equals("success")) {
                            binding.detailKendaraan.setVisibility(View.VISIBLE);
                            platValid = true;
                            JSONObject data = response.getJSONObject("data");
                            String namaDetail = data.optString("nama_kendaraan", "");
                            String jenisDetail = data.optString("jenis_kendaraan", "");
                            String kategoriDetail = data.optString("kategori", "");
                            String platDetail = data.optString("plat_nomor", "");
                            binding.detailNama.setText(namaDetail);
                            binding.detailJenis.setText(jenisDetail);
                            binding.detailKategori.setText(kategoriDetail);
                            binding.detailPlat.setText(platDetail);
                            idKendaraan = data.getInt("id_kendaraan");
                            enableSubmitByPlat();
                        } else {
                            platValid = false;
                            disableSubmitByPlat();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        disableSubmitByPlat();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("API Error", "Error fetching suggestions: " + error.toString());
                    disableSubmitByPlat();
                });

        ApiClient.getInstance(this).addToRequestQueue(request);
    }


    private void fetchSuggestions(String noPlat, String jenisKendaraan) {
        String url = "https://family.geloraaksara.co.id/gap-f/api/cari_kendaraan_by_plat?nik=123&no_plat="
                + Uri.encode(noPlat) + "&jenis_kendaraan=Mobil";
        Log.d("API Request", "Fetching suggestions from: " + url); // Log the URL being requested
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("API Response", response.toString());
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            List<String> suggestions = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject vehicle = dataArray.getJSONObject(i);
                                String suggestion = vehicle.getString("plat_nomor"); // or any other field you want
                                suggestions.add(suggestion);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                    android.R.layout.simple_dropdown_item_1line, suggestions);
                            binding.noKendaraan.setAdapter(adapter);
                            binding.noKendaraan.showDropDown();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("API Error", "Error fetching suggestions: " + error.toString());
                });

        ApiClient.getInstance(this).addToRequestQueue(request);

    }

    @SuppressLint("SetTextI18n")
    private void disableSubmitByPlat() {
        if (isConnected){
            binding.platValidation.setVisibility(View.VISIBLE);
            binding.platValidation.setText("Nomor kendaraan tidak ditemukan");
            binding.platValidation.setTextColor(Color.parseColor("#B83633"));
            binding.kirimBtn.setEnabled(false);
            binding.kirimBtn.setAlpha(0.5f);
        } else {
            binding.platValidation.setVisibility(View.VISIBLE);
            binding.platValidation.setText("Koneksi terputus...");
            binding.platValidation.setTextColor(Color.parseColor("#B83633"));
            binding.kirimBtn.setEnabled(false);
            binding.kirimBtn.setAlpha(0.5f);
        }
    }

    @SuppressLint("SetTextI18n")
    private void disableSubmitByNoSurat() {
        if (isConnected){
            binding.noSuratValidation.setVisibility(View.VISIBLE);
            binding.noSuratValidation.setText("Nomor sudah pernah digunakan");
            binding.noSuratValidation.setTextColor(Color.parseColor("#B83633"));
            binding.kirimBtn.setEnabled(false);
            binding.kirimBtn.setAlpha(0.5f);
        } else {
            binding.noSuratValidation.setVisibility(View.VISIBLE);
            binding.noSuratValidation.setText("Koneksi terputus...");
            binding.noSuratValidation.setTextColor(Color.parseColor("#B83633"));
            binding.kirimBtn.setEnabled(false);
            binding.kirimBtn.setAlpha(0.5f);
        }
    }

    @SuppressLint("SetTextI18n")
    private void enableSubmitByPlat() {
        binding.platValidation.setVisibility(View.VISIBLE);
        binding.platValidation.setText("Nomor kendaraan valid");
        binding.platValidation.setTextColor(Color.parseColor("#309A35"));
        binding.kirimBtn.setEnabled(true);
        binding.kirimBtn.setAlpha(1f);
    }

    @SuppressLint("SetTextI18n")
    private void enableSubmitByNoSurat() {
        binding.noSuratValidation.setVisibility(View.VISIBLE);
        binding.noSuratValidation.setText("Nomor dapat digunakan");
        binding.noSuratValidation.setTextColor(Color.parseColor("#309A35"));
        binding.kirimBtn.setEnabled(true);
        binding.kirimBtn.setAlpha(1f);
    }

    private void sendData() {
        repository.postPK(nik, binding.nomerSurat.getText().toString(), monthRoman, String.valueOf(currentYear), postDate, binding.tujuan.getText().toString(), binding.keperluan.getText().toString(), String.valueOf(idKendaraan), response -> {
                    Log.d("Response", "Success: " + response);
                    pDialog.dismiss();
                    onBackPressed();
                    finish();
                },
                error -> {
                    Log.e("Error", "Error: " + error.toString());
                    pDialog.setTitleText("Gagal Tersimpan")
                            .setContentText("Terjadi kesalahan saat mengirim data")
                            .setConfirmText("    OK    ")
                            .changeAlertType(KAlertDialog.ERROR_TYPE);
                });
    }

    private void checkInternet() {
        viewModel.getIsConnected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnectedNow) {
                if (isConnectedNow) {
                    isConnected = true;
                    fetchCurrentUserData(nik);
                    if (!binding.nomerSurat.getText().toString().equals("")){
                        nomerSuratValidation();
                    }
                    if (!binding.noKendaraan.getText().toString().equals("")){
                        platValidation();
                    }
                    if (isClickSendWhileOffline) {
                        sendData();
                        isClickSendWhileOffline = false;
                    }
                } else {
                    isConnected = false;
                    if (pDialog != null) {
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    }
                    isConnected = false;
                }
            }
        });
    }

    private void connectionFailed() {
        CookieBar.build(FormPeminjamanKendaraan.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void handleSubmit() {
        binding.kirimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                if (nomerSuratFilled && nomerKendaraanFilled && tujuanFilled && tanggalFilled && keperluanFilled) {
                    new KAlertDialog(FormPeminjamanKendaraan.this, KAlertDialog.WARNING_TYPE)
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
                                    pDialog = new KAlertDialog(FormPeminjamanKendaraan.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormPeminjamanKendaraan.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormPeminjamanKendaraan.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormPeminjamanKendaraan.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormPeminjamanKendaraan.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormPeminjamanKendaraan.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormPeminjamanKendaraan.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }

                                        public void onFinish() {
                                            i = -1;
                                            if (isConnected) {
                                                isClickSendWhileOffline = false;
                                                sendData();
                                            } else {
                                                isClickSendWhileOffline = true;
                                                checkInternet();
                                                connectionFailed();
                                            }
                                        }
                                    }.start();
                                }
                            })
                            .show();
                } else {
                    new KAlertDialog(FormPeminjamanKendaraan.this, KAlertDialog.ERROR_TYPE)
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
        });

    }

}
