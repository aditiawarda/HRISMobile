package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterListPelatihan;
import com.gelora.absensi.adapter.AdapterListPelatihanResume;
import com.gelora.absensi.adapter.AdapterListPengalaman;
import com.gelora.absensi.adapter.AdapterListPengalamanResume;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataPelatihan;
import com.gelora.absensi.model.DataPengalaman;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResumeKaryawanActivity extends AppCompatActivity {

    TextView namaTV, emailTV, genderTV, tempatLahirTV, tanggalLAhirTV, hanphoneTV, statusPernikahanTV, agamaTV, alamatKTPTV, alamatDomisiliTV;
    TextView hubunganKontakTV, namaKontakDaruratTV, kontakDaruratTV, nikTV, cabangTV, departemenTV, bagianTV, jabatanTV, statusKaryawanTV, tanggalBergabungTV, masaKerjaTV, golonganKaryawanTV;
    CircleImageView profileImage;
    LinearLayout partEmptyPelatihan, partEmptyPengalaman, partEmptyKontakDarurat, partAvailableKontakDarurat, backBTN, actionBar, phoneBTN, phoneKontakPart, phoneDaruratBTN;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String nikResumer = "";

    RecyclerView dataPelatihanRV, dataPengalamanRV;
    private DataPengalaman[] dataPengalamans;
    private AdapterListPengalamanResume adapterListPengalaman;
    private DataPelatihan[] dataPelatihans;
    private AdapterListPelatihanResume adapterListPelatihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_karyawan);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        profileImage = findViewById(R.id.profile_image);
        namaTV = findViewById(R.id.nama);
        emailTV = findViewById(R.id.email);
        genderTV = findViewById(R.id.jenis_kelamin);
        tempatLahirTV = findViewById(R.id.tempat_lahir);
        tanggalLAhirTV = findViewById(R.id.tanggal_lahir);
        hanphoneTV = findViewById(R.id.handphone);
        statusPernikahanTV = findViewById(R.id.status_pernikahan);
        agamaTV = findViewById(R.id.agama);
        alamatKTPTV = findViewById(R.id.alamat_ktp);
        alamatDomisiliTV = findViewById(R.id.alamat_domisili);
        nikTV = findViewById(R.id.nik);
        cabangTV = findViewById(R.id.cabang);
        departemenTV = findViewById(R.id.departemen);
        bagianTV = findViewById(R.id.bagian);
        jabatanTV = findViewById(R.id.jabatan);
        statusKaryawanTV = findViewById(R.id.status_karyawan);
        tanggalBergabungTV = findViewById(R.id.tanggal_bergabung);
        masaKerjaTV = findViewById(R.id.masa_kerja);
        golonganKaryawanTV = findViewById(R.id.golongan_karyawan);
        namaKontakDaruratTV = findViewById(R.id.nama_kontak_darurat_tv);
        kontakDaruratTV = findViewById(R.id.kontak_darurat_tv);
        hubunganKontakTV = findViewById(R.id.hubungan_kontak_tv);
        phoneBTN = findViewById(R.id.phone_btn);
        phoneKontakPart = findViewById(R.id.phone_kontak_part);
        phoneDaruratBTN = findViewById(R.id.phone_kontak_btn);
        partEmptyKontakDarurat = findViewById(R.id.part_empty_kontak_darurat);
        partAvailableKontakDarurat = findViewById(R.id.part_available_kontak_darurat);
        partEmptyPengalaman = findViewById(R.id.part_empty_pengalaman);
        dataPengalamanRV = findViewById(R.id.data_pengalaman_rv);
        partEmptyPelatihan = findViewById(R.id.part_empty_pelatihan);
        dataPelatihanRV = findViewById(R.id.data_pelatihan_rv);

        dataPengalamanRV.setLayoutManager(new LinearLayoutManager(this));
        dataPengalamanRV.setHasFixedSize(true);
        dataPengalamanRV.setNestedScrollingEnabled(false);
        dataPengalamanRV.setItemAnimator(new DefaultItemAnimator());

        dataPelatihanRV.setLayoutManager(new LinearLayoutManager(this));
        dataPelatihanRV.setHasFixedSize(true);
        dataPelatihanRV.setNestedScrollingEnabled(false);
        dataPelatihanRV.setItemAnimator(new DefaultItemAnimator());

        nikResumer = getIntent().getExtras().getString("NIK");

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 1500);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_resume";
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
                                JSONObject dataArray = data.getJSONObject("data");
                                String avatar = dataArray.getString("avatar");
                                String nama = dataArray.getString("nama");
                                String email = dataArray.getString("email");
                                String jenis_kelamin = dataArray.getString("jenis_kelamin");
                                String tempat_lahir = dataArray.getString("tempat_lahir");
                                String tanggal_lahir = dataArray.getString("tanggal_lahir");
                                String handphone = dataArray.getString("handphone");
                                String status_pernikahan = dataArray.getString("status_pernikahan");
                                String agama = dataArray.getString("agama");
                                String alamat_ktp = dataArray.getString("alamat_ktp");
                                String alamat_domisili = dataArray.getString("alamat_domisili");
                                String nik = dataArray.getString("nik");
                                String cabang = dataArray.getString("cabang");
                                String departemen = dataArray.getString("departemen");
                                String bagian = dataArray.getString("bagian");
                                String jabatan = dataArray.getString("jabatan");
                                String status_karyawan = dataArray.getString("status_karyawan");
                                String tanggal_bergabung = dataArray.getString("tanggal_bergabung");
                                String golongan_karyawan = dataArray.getString("golongan_karyawan");
                                String nama_kontak = dataArray.getString("nama_kontak");
                                String notelp = dataArray.getString("notelp");
                                String hubungan = dataArray.getString("hubungan");
                                String hubungan_lainnya = dataArray.getString("hubungan_lainnya");

                                if(avatar!=null && !avatar.equals("") && !avatar.equals("null")){
                                    Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .resize(800, 800)
                                            .into(profileImage);
                                } else {
                                    Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/default_profile.jpg").networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .resize(800, 800)
                                            .into(profileImage);
                                }

                                phoneBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", handphone, null));
                                        startActivity(intent);
                                    }
                                });

                                profileImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ResumeKaryawanActivity.this, ViewImageActivity.class);
                                        if(avatar!=null && !avatar.equals("")){
                                            intent.putExtra("url","https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar);
                                        } else {
                                            intent.putExtra("url","https://geloraaksara.co.id/absen-online/upload/avatar/default_profile.jpg");
                                        }
                                        intent.putExtra("kode","profile");
                                        startActivity(intent);
                                    }
                                });

                                namaTV.setText(nama);

                                if(email.equals("")||email.equals("null")){
                                    emailTV.setText("-");
                                } else {
                                    emailTV.setText(email);
                                }

                                if(jenis_kelamin.equals("")||jenis_kelamin.equals("null")){
                                    genderTV.setText("-");
                                } else {
                                    genderTV.setText(jenis_kelamin);
                                }

                                if(tempat_lahir.equals("")||tempat_lahir.equals("null")){
                                    tempatLahirTV.setText("-");
                                } else {
                                    tempatLahirTV.setText(tempat_lahir);
                                }

                                if(tanggal_lahir.equals("")||tanggal_lahir.equals("null")){
                                    tanggalLAhirTV.setText("-");
                                } else {
                                    String dayDate = tanggal_lahir.substring(8,10);
                                    String yearDate = tanggal_lahir.substring(0,4);;
                                    String bulanValue = tanggal_lahir.substring(5,7);
                                    String bulanName;

                                    switch (bulanValue) {
                                        case "01":
                                            bulanName = "Jan";
                                            break;
                                        case "02":
                                            bulanName = "Feb";
                                            break;
                                        case "03":
                                            bulanName = "Mar";
                                            break;
                                        case "04":
                                            bulanName = "Apr";
                                            break;
                                        case "05":
                                            bulanName = "Mei";
                                            break;
                                        case "06":
                                            bulanName = "Jun";
                                            break;
                                        case "07":
                                            bulanName = "Jul";
                                            break;
                                        case "08":
                                            bulanName = "Agu";
                                            break;
                                        case "09":
                                            bulanName = "Sep";
                                            break;
                                        case "10":
                                            bulanName = "Okt";
                                            break;
                                        case "11":
                                            bulanName = "Nov";
                                            break;
                                        case "12":
                                            bulanName = "Des";
                                            break;
                                        default:
                                            bulanName = "Not found!";
                                            break;
                                    }

                                    tanggalLAhirTV.setText(dayDate+" "+bulanName+" "+yearDate);

                                }

                                if(handphone.equals("")||handphone.equals("null")){
                                    hanphoneTV.setText("-");
                                } else {
                                    hanphoneTV.setText(handphone);
                                }

                                if(status_pernikahan.equals("")||status_pernikahan.equals("null")){
                                    statusPernikahanTV.setText("-");
                                } else {
                                    statusPernikahanTV.setText(status_pernikahan);
                                }

                                if(agama.equals("")||agama.equals("null")){
                                    agamaTV.setText("-");
                                } else {
                                    agamaTV.setText(agama);
                                }

                                if(alamat_ktp.equals("")||alamat_ktp.equals("null")){
                                    alamatKTPTV.setText("-");
                                } else {
                                    alamatKTPTV.setText(alamat_ktp);
                                }

                                if(alamat_domisili.equals("")||alamat_domisili.equals("null")){
                                    alamatDomisiliTV.setText("-");
                                } else {
                                    alamatDomisiliTV.setText(alamat_domisili);
                                }

                                nikTV.setText(nik);

                                if(cabang.equals("")||cabang.equals("null")||cabang.equals("0")){
                                    cabangTV.setText("-");
                                } else {
                                    cabangTV.setText(cabang);
                                }

                                if(departemen.equals("")||departemen.equals("null")){
                                    departemenTV.setText("-");
                                } else {
                                    departemenTV.setText(departemen);
                                }

                                if(bagian.equals("")||bagian.equals("null")){
                                    bagianTV.setText("-");
                                } else {
                                    bagianTV.setText(bagian);
                                }

                                if(jabatan.equals("")||jabatan.equals("null")){
                                    jabatanTV.setText("-");
                                } else {
                                    jabatanTV.setText(jabatan);
                                }

                                if(status_karyawan.equals("")||status_karyawan.equals("null")){
                                    statusKaryawanTV.setText("-");
                                } else {
                                    statusKaryawanTV.setText(status_karyawan);
                                }

                                if(tanggal_bergabung.equals("")||tanggal_bergabung.equals("null")){
                                    tanggalBergabungTV.setText("-");
                                    masaKerjaTV.setText("-");
                                } else {
                                    String dayDate = tanggal_bergabung.substring(8,10);
                                    String yearDate = tanggal_bergabung.substring(0,4);;
                                    String bulanValue = tanggal_bergabung.substring(5,7);
                                    String bulanName;

                                    switch (bulanValue) {
                                        case "01":
                                            bulanName = "Jan";
                                            break;
                                        case "02":
                                            bulanName = "Feb";
                                            break;
                                        case "03":
                                            bulanName = "Mar";
                                            break;
                                        case "04":
                                            bulanName = "Apr";
                                            break;
                                        case "05":
                                            bulanName = "Mei";
                                            break;
                                        case "06":
                                            bulanName = "Jun";
                                            break;
                                        case "07":
                                            bulanName = "Jul";
                                            break;
                                        case "08":
                                            bulanName = "Agu";
                                            break;
                                        case "09":
                                            bulanName = "Sep";
                                            break;
                                        case "10":
                                            bulanName = "Okt";
                                            break;
                                        case "11":
                                            bulanName = "Nov";
                                            break;
                                        case "12":
                                            bulanName = "Des";
                                            break;
                                        default:
                                            bulanName = "Not found!";
                                            break;
                                    }

                                    tanggalBergabungTV.setText(dayDate+" "+bulanName+" "+yearDate);

                                    String tglSekarang = getDate();
                                    String tglMasukKerja = tanggal_bergabung;

                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        date1 = format.parse(tglSekarang);
                                        date2 = format.parse(tglMasukKerja);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long waktu1 = date1.getTime();
                                    long waktu2 = date2.getTime();
                                    long selisih_waktu = waktu1 - waktu2;

                                    long diffDays = selisih_waktu / (24 * 60 * 60 * 1000);

                                    long tahun = (diffDays / 365);
                                    long bulan = (diffDays - (tahun * 365)) / 30;
                                    long hari = (diffDays - ((tahun * 365) + (bulan * 30)));

                                    int y = Integer.parseInt(getDateY());
                                    int m = Integer.parseInt(getDateM());
                                    int d = Integer.parseInt(getDateD());

                                    LocalDate dob = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        dob = LocalDate.of(Integer.parseInt(yearDate),  Integer.parseInt(bulanValue), Integer.parseInt(dayDate));
                                        LocalDate curDate = LocalDate.now();
                                        Period period = Period.between(dob, curDate);
                                        if (period.getYears() == 0){
                                            if(period.getMonths() == 0){
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText("-");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getDays())+" Hari");
                                                }
                                            } else {
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText(String.valueOf(period.getMonths())+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getMonths())+" Bulan "+String.valueOf(period.getDays())+" Hari");
                                                }
                                            }
                                        } else {
                                            if(period.getMonths() == 0){
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun "+String.valueOf(period.getDays())+" Hari");
                                                }
                                            } else {
                                                if(period.getDays() == 0){
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun "+String.valueOf(period.getMonths())+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(period.getYears())+" Tahun "+String.valueOf(period.getMonths())+" Bulan "+String.valueOf(period.getDays())+" Hari");
                                                }
                                            }
                                        }
                                    } else {
                                        if (tahun == 0){
                                            if(bulan == 0){
                                                if(hari == 0){
                                                    masaKerjaTV.setText("-");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(hari)+" Hari");
                                                }
                                            } else {
                                                if(hari == 0){
                                                    masaKerjaTV.setText(String.valueOf(bulan)+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(bulan)+" Bulan "+String.valueOf(hari)+" Hari");
                                                }
                                            }
                                        } else {
                                            if(bulan == 0){
                                                if(hari == 0){
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun "+String.valueOf(hari)+" Hari");
                                                }
                                            } else {
                                                if(hari == 0){
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun "+String.valueOf(bulan)+" Bulan");
                                                } else {
                                                    masaKerjaTV.setText(String.valueOf(tahun)+" Tahun "+String.valueOf(bulan)+" Bulan "+String.valueOf(hari)+" Hari");
                                                }
                                            }
                                        }
                                    }

                                }

                                if(golongan_karyawan.equals("")||golongan_karyawan.equals("null")){
                                    golonganKaryawanTV.setText("-");
                                } else {
                                    golonganKaryawanTV.setText(golongan_karyawan);
                                }

                                if(nama_kontak.equals("")||nama_kontak.equals("null")){
                                    namaKontakDaruratTV.setText("-");
                                    kontakDaruratTV.setText("-");
                                    phoneKontakPart.setVisibility(View.GONE);
                                    partEmptyKontakDarurat.setVisibility(View.VISIBLE);
                                    partAvailableKontakDarurat.setVisibility(View.GONE);
                                } else {
                                    partEmptyKontakDarurat.setVisibility(View.GONE);
                                    partAvailableKontakDarurat.setVisibility(View.VISIBLE);
                                    namaKontakDaruratTV.setText(nama_kontak);
                                    if(notelp.equals("")||notelp.equals("null")){
                                        kontakDaruratTV.setText("-");
                                    } else {
                                        kontakDaruratTV.setText(notelp);
                                        phoneKontakPart.setVisibility(View.VISIBLE);

                                        phoneDaruratBTN.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", notelp, null));
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                }

                                if(hubungan.equals("")||hubungan.equals("null")){
                                    hubunganKontakTV.setText("-");
                                } else if(hubungan.equals("Lainnya")){
                                    if(hubungan_lainnya.equals("")||hubungan_lainnya.equals("null")){
                                        hubunganKontakTV.setText("-");
                                    } else {
                                        hubunganKontakTV.setText(hubungan_lainnya);
                                    }
                                } else {
                                    hubunganKontakTV.setText(hubungan);
                                }

                                String jumlah_pengalaman = data.getString("jumlah_pengalaman");
                                String jumlah_pelatihan = data.getString("jumlah_pelatihan");

                                if (jumlah_pengalaman.equals("0")){
                                    dataPengalamanRV.setVisibility(View.GONE);
                                    partEmptyPengalaman.setVisibility(View.VISIBLE);
                                } else {
                                    dataPengalamanRV.setVisibility(View.VISIBLE);
                                    partEmptyPengalaman.setVisibility(View.GONE);
                                    String data_pengalaman = data.getString("data_pengalaman");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataPengalamans = gson.fromJson(data_pengalaman, DataPengalaman[].class);
                                    adapterListPengalaman = new AdapterListPengalamanResume(dataPengalamans, ResumeKaryawanActivity.this);
                                    dataPengalamanRV.setAdapter(adapterListPengalaman);
                                }

                                if (jumlah_pelatihan.equals("0")){
                                    dataPelatihanRV.setVisibility(View.GONE);
                                    partEmptyPelatihan.setVisibility(View.VISIBLE);
                                } else {
                                    dataPelatihanRV.setVisibility(View.VISIBLE);
                                    partEmptyPelatihan.setVisibility(View.GONE);
                                    String data_pelatihan = data.getString("data_pelatihan");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataPelatihans = gson.fromJson(data_pelatihan, DataPelatihan[].class);
                                    adapterListPelatihan = new AdapterListPelatihanResume(dataPelatihans, ResumeKaryawanActivity.this);
                                    dataPelatihanRV.setAdapter(adapterListPelatihan);
                                }

                            } else {
                                new KAlertDialog(ResumeKaryawanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan saat mengakses data")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
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
                params.put("nik", nikResumer);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
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

    private void connectionFailed(){
        CookieBar.build(ResumeKaryawanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

}