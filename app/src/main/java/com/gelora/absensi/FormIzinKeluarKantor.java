package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gelora.absensi.databinding.ActivityFormIzinKeluarKantorBinding;
import com.gelora.absensi.databinding.TimePickerDialogBinding;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanKeluar;
import com.gelora.absensi.model.PostIzinResponse;
import com.gelora.absensi.network.Repository;
import com.gelora.absensi.viewmodel.ConnectivityViewModel;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormIzinKeluarKantor extends AppCompatActivity {

    private ActivityFormIzinKeluarKantorBinding _binding = null;
    private ActivityFormIzinKeluarKantorBinding getBinding() {
        return _binding;
    }
    private Repository repository;
    int currentJam = 0;
    int value = 0;
    String postJamKeluar = "";
    String durasi = "";
    String keperluan = "";
    PostIzinResponse postResponse;
    boolean jamKeluarFilled = false;
    boolean keperluanFilled = false;
    boolean durasiKeluarFilled = true;
    KAlertDialog pDialog;
    private int i = -1;
    private Handler handler = new Handler();
    private Boolean isConnected;
    private Boolean isClickSendWhileOffline = false;
    private ConnectivityViewModel viewModel;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    private int currentRoleIndex = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();
        _binding = ActivityFormIzinKeluarKantorBinding.inflate(layoutInflater);
        View view = getBinding().getRoot();
        setContentView(view);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        viewModel = new ViewModelProvider(FormIzinKeluarKantor.this).get(ConnectivityViewModel.class);
        repository = new Repository(this);
        checkInternet();

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(getBinding().successGif);

        getDataKaryawan();

        getBinding().swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        getBinding().swipeToRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        getBinding().swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                FormIzinKeluarKantor.this.onBackPressed();
            }
        });

        durasiKeluarTextWatcher();
        keperluanTextWatcher();

        getBinding().keluarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialogBinding timeBinding = TimePickerDialogBinding.inflate(getLayoutInflater());
                TimePicker timePicker = timeBinding.timePicker;
                timePicker.setIs24HourView(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(FormIzinKeluarKantor.this, R.style.CustomAlertDialogTheme);
                builder.setView(timeBinding.getRoot());

                AlertDialog timeDialog = builder.create();
                timeBinding.confirmBtn.setOnClickListener(view2->{
                    int selectedHour;
                    int selectedMinute;
                    if (Build.VERSION.SDK_INT >= 23) {
                        selectedHour = timePicker.getHour();
                        selectedMinute = timePicker.getMinute();
                    } else {
                        selectedHour = timePicker.getCurrentHour();
                        selectedMinute = timePicker.getCurrentMinute();
                    }
                    @SuppressLint("SimpleDateFormat")
                    String timeStamp = new SimpleDateFormat(" dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    if(selectedHour<10){
                        if(selectedMinute<10){
                            getBinding().selectedDateTv.setText("0"+selectedHour + ":" + "0"+selectedMinute + ":00");
                            postJamKeluar = "0"+selectedHour + ":" + "0"+selectedMinute + ":00";
                        } else {
                            getBinding().selectedDateTv.setText("0"+selectedHour + ":" + selectedMinute + ":00");
                            postJamKeluar = "0"+selectedHour + ":" + selectedMinute + ":00";
                        }
                    } else {
                        if(selectedMinute<10){
                            getBinding().selectedDateTv.setText(selectedHour + ":" + "0"+selectedMinute + ":00");
                            postJamKeluar = selectedHour + ":" + "0"+selectedMinute + ":00";
                        } else {
                            getBinding().selectedDateTv.setText(selectedHour + ":" + selectedMinute + ":00");
                            postJamKeluar = selectedHour + ":" + selectedMinute + ":00";
                        }
                    }
                    jamKeluarFilled = true;
                    timeDialog.dismiss();
                });

                timeBinding.cancel.setOnClickListener(view2->{
                    timeDialog.dismiss();
                });
                timeDialog.show();
                timeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        getBinding().submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                final Calendar c = Calendar.getInstance();
                postJamKeluar = getBinding().selectedDateTv.getText().toString();
                keperluan = getBinding().etKeperluan.getText().toString();
                durasi =   getBinding().etJumlahJam.getText().toString() + ":00" + ":00";
                final String NamaKaryawan = getBinding().namaKaryawanKeluarTv.getText().toString();
                final String NikKaryawan = getBinding().nikKaryawanKeluarTv.getText().toString();
                final String DetailKaryawan = getBinding().detailKaryawanKeluarTv.getText().toString();

                if (keperluanFilled && jamKeluarFilled && durasiKeluarFilled){
                    new KAlertDialog(FormIzinKeluarKantor.this, KAlertDialog.WARNING_TYPE)
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
                                    pDialog = new KAlertDialog(FormIzinKeluarKantor.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormIzinKeluarKantor.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormIzinKeluarKantor.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormIzinKeluarKantor.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormIzinKeluarKantor.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormIzinKeluarKantor.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (FormIzinKeluarKantor.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            if (isConnected){
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
                    new KAlertDialog(FormIzinKeluarKantor.this, KAlertDialog.ERROR_TYPE)
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

    private void getDataKaryawan(){
        getBinding().namaKaryawanKeluarTv.setText(sharedPrefManager.getSpNama().toUpperCase());
        getBinding().nikKaryawanKeluarTv.setText(sharedPrefManager.getSpNik());
        getDataKaryawanDetail();
    }

    private void getDataKaryawanDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/data_karyawan_personal";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Success")){
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                 getBinding().detailKaryawanKeluarTv.setText(jabatan+" | "+bagian+" | "+department);
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
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormIzinKeluarKantor.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    public void keperluanTextWatcher(){
        getBinding().etKeperluan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()!=0){
                    keperluanFilled = true;
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void durasiKeluarTextWatcher (){
        getBinding().etJumlahJam.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0){
                    durasiKeluarFilled = false;
                    value = 1;
                } else {
                    value = Integer.parseInt(editable.toString());
                    durasiKeluarFilled = true;
                }
                if (value < 1) {
                    currentJam = 1;
                    getBinding().etJumlahJam.setText("1");
                    getBinding().etJumlahJam.setSelection(getBinding().etJumlahJam.getText().length());
                } else if (value > 8) {
                    currentJam = 8;
                    getBinding().etJumlahJam.setText("8");
                    getBinding().etJumlahJam.setSelection(getBinding().etJumlahJam.getText().length());
                } else {
                    currentJam = value;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        getBinding().btnDecreament.setOnClickListener(view -> {
            if (currentJam < 1) {
                currentJam = 1;
                getBinding().etJumlahJam.setText("" + currentJam);
            } else if (currentJam > 8) {
                currentJam = 8;
                getBinding().etJumlahJam.setText("" + currentJam);
            } else {
                currentJam--;
                getBinding().etJumlahJam.setText("" + currentJam);
                getBinding().etJumlahJam.setSelection(getBinding().etJumlahJam.getText().length());
            }
        });

        getBinding().btnIncrement.setOnClickListener(view -> {
            if (currentJam < 1) {
                currentJam = 1;
                getBinding().etJumlahJam.setText("" + currentJam);
            } else if (currentJam > 8) {
                currentJam = 8;
                getBinding().etJumlahJam.setText("" + currentJam);
            }else{
                currentJam++;
                getBinding().etJumlahJam.setText("" + currentJam);
                getBinding().etJumlahJam.setSelection(getBinding().etJumlahJam.getText().length());
            }
        });
    }

    private void checkInternet(){
        viewModel.getIsConnected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnectedNow) {
                if (isConnectedNow) {
                    isConnected = true;
                    if (isClickSendWhileOffline){
                        sendData();
                    }
                } else {
                    if (pDialog != null){
                        if (pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                    }
                    isConnected = false;
                }
            }
        });
    }

    public void sendData(){
        postResponse = new PostIzinResponse(
                sharedPrefManager.getSpNik(),
                postJamKeluar,
                durasi,
                keperluan
        );
        repository.postData(postResponse, response -> {
            String[] parts = response.split("-");
            if (Objects.equals(parts[0], "Success")){
                getBinding().successSubmit.setVisibility(View.VISIBLE);
                getBinding().formPart.setVisibility(View.GONE);
                getBinding().viewBtn.setOnClickListener(view -> {
                    Intent intent = new Intent(FormIzinKeluarKantor.this, DetailIzinKeluar.class);
                    intent.putExtra("current_id",parts[1]);
                    intent.putExtra("nik_pemohon",sharedPrefManager.getSpNik());
                    startActivity(intent);
                });
            }
        }, Throwable::printStackTrace);
        pDialog.dismiss();
    }

}