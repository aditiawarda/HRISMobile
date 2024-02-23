package com.gelora.absensi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterInvoicePiutang;
import com.gelora.absensi.adapter.AdapterPelangganLama;
import com.gelora.absensi.adapter.AdapterPelangganList;
import com.gelora.absensi.adapter.AdapterProductInputSuma;
import com.gelora.absensi.adapter.AdapterProductSuma;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataInvoicePiutang;
import com.gelora.absensi.model.PelangganLama;
import com.gelora.absensi.model.PelangganList;
import com.gelora.absensi.model.ProductSuma;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ReportSumaActivity extends AppCompatActivity {

    LinearLayout loadingFormPart, attantionNoForm, rencanaKunjunganFormPart, penagihanFormPart, penawaranFormPart, kunjunganFormPart, actionBar, backBTN, reportCategoryBTN;
    LinearLayout viewPermohonanBTN, formPart, successPart, loadingDataPart, loadingDataPartProduk, noDataPart, noDataPartProduk, startAttantionPart, startAttantionPartProduk, penawaranBTN, kunjunganBTN, penagihanBTN, rencanaKunjunaganBTN, markKunjungan, markPenawaran, markPenagihan, markRencanaKunjungan;

    EditText f1KeteranganKunjunganED, f1NamaPelangganBaruED, f1AlamatPelangganBaruED, f1PicPelangganBaruED, f1TeleponPelangganBaruED;
    LinearLayout f1AddPelangganBTN, f1AddPelangganPart, f1ChoiceDateBTN, f1DetailPelanggan, f1NamaPelangganLamaBTN, f1SubmitPesananBTN, f1PelangganAttantionPart, f1PelangganBaruPart, f1PelangganLamaPart;
    RadioGroup f1PelangganOption;
    RadioButton f1PelangganOptionBaru, f1PelangganOptionLama;
    String f1DateChoice = "", f1JenisPelanggan = "", f1IdPelangganLama = "";
    TextView f1ChoiceDateTV, f1NamaPelangganLamaChoiceTV, f1LabelLampiranTV, f1TotalPesananTV, f1SubTotalTV, f1AlamatPelangganLamaTV, f1PicPelangganLamaTV, f1TeleponPelangganLamaTV;
    RecyclerView f1PelangganRV;
    JSONArray f1JsonArrayPelanggan = new JSONArray();
    private PelangganList[] pelangganLists;
    private AdapterPelangganList adapterPelangganList;

    EditText f2KeteranganKunjunganED, keywordED, keywordEDProduk, f2NamaPelangganBaruED, f2AlamatPelangganBaruED, f2PicPelangganBaruED, f2TeleponPelangganBaruED;
    LinearLayout f2SubmitPesananBTN, f2GPSLocationBTN, f2ViewLampiranBTN, f2LampiranFotoBTN, f2ProductInputDetailPart, f2AddProductBTN, f2ProductChoiceBTN, f2DetailPesananPart, f2DetailPelanggan, f2NamaPelangganLamaBTN, f2PelangganAttantionPart, f2PelangganBaruPart, f2PelangganLamaPart;
    RadioGroup f2PelangganOption;
    RadioButton f2PelangganOptionBaru, f2PelangganOptionLama;
    TextView f2KategoriPelangganBaruPilih, f2DetailLocationTV, f2LabelLampiranTV, f2TotalPesananTV, f2SubTotalTV, f2ProductHargaSatuanTV, f2ProductChoiceTV, f2TeleponPelangganLamaTV, f2NamaPelangganLamaChoiceTV, f2AlamatPelangganLamaTV, f2PicPelangganLamaTV;
    NumberPicker f2QtyProductPicker;
    RecyclerView pelangganRV, produkRV, f2ListProductInputRV;
    private PelangganLama[] pelangganLamas;
    private ProductSuma[] productSumas;
    private AdapterPelangganLama adapterPelangganLama;
    private AdapterProductSuma adapterProductSuma;
    Spinner f2SpinnerKategoriPelanggan;

    String[] SubKategoriListPilih = {"Nilai 1", "Nilai 2", "Nilai 3"};
    String f2IdPelangganLama = "", f2JenisPelanggan = "", f2TotalPesanan = "", f2FullDataProduct = "", f2QtyProduct = "", f2IdProduct = "", f2ProductName = "", f2ProductHargaSatuan = "", f2SubTotal = "";

    LinearLayout f3SubmitPesananBTN, f3LampiranFotoBTN, f3ViewLampiranBTN, f3GPSLocationBTN, f3NoDataPiutang, f3LoadingDataPiutang, f3NamaPelangganLamaBTN, f3DetailPelanggan, f3DetailListInvPart;
    TextView f3DetailLocationTV, f3LabelLampiranTV, f3TotalPiutangTV, f3TeleponPelangganLamaTV, f3NamaPelangganLamaChoiceTV, f3AlamatPelangganLamaTV, f3PicPelangganLamaTV;
    String f3IdPelangganLama = "";
    RecyclerView f3InvRV;
    ImageView f3LoadingDataPiutangImg;
    EditText f3KeteranganED;
    private DataInvoicePiutang[] dataInvoicePiutangs;
    private AdapterInvoicePiutang adapterInvoicePiutang;

    TextView viewPermohonanTV, reportKategoriChoiceTV, namaKaryawanTV, nikKaryawanTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    BottomSheetLayout bottomSheet;
    ImageView loadingForm, loadingGif, loadingGifProduk, successGif;
    String salesLat = "", salesLong = "", categoryReport = "", laporanTerkirim = "", fullBase64String = "";

    int totalLaporan = 0;
    private List<String> dataProduct = new ArrayList<>();
    private AdapterProductInputSuma adapterProductInputSuma;
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    KAlertDialog pDialog;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_suma);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        reportCategoryBTN = findViewById(R.id.report_kategori_btn);
        reportKategoriChoiceTV = findViewById(R.id.report_kategori_choice_tv);
        penagihanFormPart = findViewById(R.id.penagihan_form_part);
        kunjunganFormPart = findViewById(R.id.kunjungan_form_part);
        penawaranFormPart = findViewById(R.id.penawaran_form_part);
        rencanaKunjunganFormPart = findViewById(R.id.rencana_kunjungan_form_part);
        attantionNoForm = findViewById(R.id.attantion_no_form);
        loadingFormPart = findViewById(R.id.loading_form_part);
        loadingForm = findViewById(R.id.loading_form);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);
        viewPermohonanBTN = findViewById(R.id.view_permohonan_btn);
        viewPermohonanTV = findViewById(R.id.view_permohonan_tv);

        f1KeteranganKunjunganED = findViewById(R.id.f1_keterangan_kunjungan_ed);
        f1ChoiceDateBTN = findViewById(R.id.f1_choice_date_btn);
        f1ChoiceDateTV = findViewById(R.id.f1_choice_date_tv);
        f1PelangganOption = findViewById(R.id.f1_pelanggan_option);
        f1PelangganOptionBaru = findViewById(R.id.f1_pelanggan_option_baru);
        f1PelangganOptionLama = findViewById(R.id.f1_pelanggan_option_lama);
        f1PelangganAttantionPart = findViewById(R.id.f1_pelanggan_attantion);
        f1PelangganBaruPart = findViewById(R.id.f1_pelanggan_baru);
        f1PelangganLamaPart = findViewById(R.id.f1_pelanggan_lama);
        f1NamaPelangganLamaBTN = findViewById(R.id.f1_nama_pelanggan_lama_btn);
        f1NamaPelangganLamaChoiceTV = findViewById(R.id.f1_nama_pelanggan_lama_choice_tv);
        f1DetailPelanggan = findViewById(R.id.f1_detail_pelanggan);
        f1AlamatPelangganLamaTV = findViewById(R.id.f1_alamat_pelanggan_lama_tv);
        f1PicPelangganLamaTV = findViewById(R.id.f1_pic_pelanggan_lama_tv);
        f1TeleponPelangganLamaTV = findViewById(R.id.f1_telepon_pelanggan_lama_tv);
        f1SubmitPesananBTN = findViewById(R.id.f1_submit_data_btn);
        f1NamaPelangganBaruED = findViewById(R.id.f1_nama_pelanggan_baru_ed);
        f1AlamatPelangganBaruED = findViewById(R.id.f1_alamat_pelanggan_baru_ed);
        f1PicPelangganBaruED = findViewById(R.id.f1_pic_pelanggan_baru_ed);
        f1TeleponPelangganBaruED = findViewById(R.id.f1_telepon_pelanggan_baru_ed);
        f1AddPelangganPart = findViewById(R.id.f1_add_pelanggan_part);
        f1AddPelangganBTN = findViewById(R.id.f1_add_pelanggan_btn);
        f1PelangganRV = findViewById(R.id.item_pelanggan_rv);

        f2KeteranganKunjunganED = findViewById(R.id.f2_keterangan_kunjungan_ed);
        f2PelangganAttantionPart = findViewById(R.id.f2_pelanggan_attantion);
        f2PelangganBaruPart = findViewById(R.id.f2_pelanggan_baru);
        f2PelangganLamaPart = findViewById(R.id.f2_pelanggan_lama);
        f2PelangganOption = findViewById(R.id.f2_pelanggan_option);
        f2PelangganOptionBaru = findViewById(R.id.f2_pelanggan_option_baru);
        f2PelangganOptionLama = findViewById(R.id.f2_pelanggan_option_lama);
        f2NamaPelangganLamaBTN = findViewById(R.id.f2_nama_pelanggan_lama_btn);
        f2NamaPelangganLamaChoiceTV = findViewById(R.id.f2_nama_pelanggan_lama_choice_tv);
        f2DetailPelanggan = findViewById(R.id.f2_detail_pelanggan);
        f2AlamatPelangganLamaTV = findViewById(R.id.f2_alamat_pelanggan_lama_tv);
        f2PicPelangganLamaTV = findViewById(R.id.f2_pic_pelanggan_lama_tv);
        f2TeleponPelangganLamaTV = findViewById(R.id.f2_telepon_pelanggan_lama_tv);
        f2AddProductBTN = findViewById(R.id.f2_add_product_btn);
        f2DetailPesananPart = findViewById(R.id.f2_detail_pesanan_part);
        f2ProductChoiceBTN = findViewById(R.id.f2_product_choice_btn);
        f2ProductChoiceTV = findViewById(R.id.f2_product_choice_tv);
        f2ProductHargaSatuanTV = findViewById(R.id.f2_product_harga_satuan_tv);
        f2SubTotalTV = findViewById(R.id.f2_sub_total_tv);
        f2QtyProductPicker = findViewById(R.id.f2_qty_product_picker);
        f2ProductInputDetailPart = findViewById(R.id.f2_product_input_detail_part);
        f2TotalPesananTV = findViewById(R.id.f2_total_pesanan_tv);
        f2LampiranFotoBTN = findViewById(R.id.f2_foto_lampiran_btn);
        f2ViewLampiranBTN = findViewById(R.id.f2_view_lampiran_btn);
        f2ListProductInputRV = findViewById(R.id.item_produk_input_rv);
        f2LabelLampiranTV = findViewById(R.id.f2_label_lampiran_tv);
        f2GPSLocationBTN = findViewById(R.id.f2_gps_btn);
        f2SubmitPesananBTN = findViewById(R.id.f2_submit_data_btn);
        f2NamaPelangganBaruED = findViewById(R.id.f2_nama_pelanggan_baru_ed);
        f2AlamatPelangganBaruED = findViewById(R.id.f2_alamat_pelanggan_baru_ed);
        f2PicPelangganBaruED = findViewById(R.id.f2_pic_pelanggan_baru_ed);
        f2TeleponPelangganBaruED = findViewById(R.id.f2_telepon_pelanggan_baru_ed);
        f2DetailLocationTV = findViewById(R.id.f2_detail_location_tv);
        f2KategoriPelangganBaruPilih = findViewById(R.id.f2_kategori_pelanggan_baru_pilih);
        f2SpinnerKategoriPelanggan = findViewById(R.id.f2_kategori_pelanggan_baru_sp);

        f3NamaPelangganLamaBTN = findViewById(R.id.f3_nama_pelanggan_lama_btn);
        f3DetailPelanggan = findViewById(R.id.f3_detail_pelanggan);
        f3NamaPelangganLamaChoiceTV = findViewById(R.id.f3_nama_pelanggan_lama_choice_tv);
        f3DetailPelanggan = findViewById(R.id.f3_detail_pelanggan);
        f3AlamatPelangganLamaTV = findViewById(R.id.f3_alamat_pelanggan_lama_tv);
        f3PicPelangganLamaTV = findViewById(R.id.f3_pic_pelanggan_lama_tv);
        f3TeleponPelangganLamaTV = findViewById(R.id.f3_telepon_pelanggan_lama_tv);
        f3DetailListInvPart = findViewById(R.id.f3_detail_list_inv_part);
        f3NoDataPiutang = findViewById(R.id.f3_no_data_piutang);
        f3GPSLocationBTN = findViewById(R.id.f3_gps_btn);
        f3LampiranFotoBTN = findViewById(R.id.f3_foto_lampiran_btn);
        f3ViewLampiranBTN = findViewById(R.id.f3_view_lampiran_btn);
        f3LabelLampiranTV = findViewById(R.id.f3_label_lampiran_tv);
        f3InvRV = findViewById(R.id.inv_rv);
        f3TotalPiutangTV = findViewById(R.id.f3_total_piutang_tv);
        f3LoadingDataPiutang = findViewById(R.id.f3_loading_data_piutang);
        f3LoadingDataPiutangImg = findViewById(R.id.f3_loading_data_piutang_img);
        f3KeteranganED = findViewById(R.id.f3_keterangan_ed);
        f3DetailLocationTV = findViewById(R.id.f3_detail_location_tv);
        f3SubmitPesananBTN = findViewById(R.id.f3_submit_data_btn);

        adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
        f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(this));
        f2ListProductInputRV.setAdapter(adapterProductInputSuma);
        f2ListProductInputRV.setHasFixedSize(true);
        f2ListProductInputRV.setNestedScrollingEnabled(false);
        f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

        f2SpinnerKategoriPelanggan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                f2SpinnerKategoriPelanggan.setBackground(getResources().getDrawable(R.drawable.bg_spinner_up));
                f2KategoriPelangganBaruPilih.setText(SubKategoriListPilih[position]);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //getData();
                    }
                },50);
            }

            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                f2KategoriPelangganBaruPilih.setBackground(getResources().getDrawable(R.drawable.bg_spinner));
                f2KategoriPelangganBaruPilih.setText("Sub Kategori");
            }
        });

        ArrayAdapter adapterPelangganBaruKategori = new ArrayAdapter(this, android.R.layout.simple_list_item_1, SubKategoriListPilih);
        adapterPelangganBaruKategori.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        f2SpinnerKategoriPelanggan.setAdapter(adapterPelangganBaruKategori);

        salesPosition();

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingForm);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f3LoadingDataPiutangImg);

        namaKaryawanTV.setText(sharedPrefManager.getSpNama());
        nikKaryawanTV.setText(sharedPrefManager.getSpNik());

        LocalBroadcastManager.getInstance(this).registerReceiver(pelangganLamaBroad, new IntentFilter("pelanggan_lama_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(productSumaBroad, new IntentFilter("product_suma_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(productDeleteBroad, new IntentFilter("product_delete_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(removePelanggan, new IntentFilter("remove_pelanggan_list"));

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
                categoryReport = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "");
                reportKategoriChoiceTV.setText("");
                kunjunganFormPart.setVisibility(View.GONE);
                penawaranFormPart.setVisibility(View.GONE);
                penagihanFormPart.setVisibility(View.GONE);
                rencanaKunjunganFormPart.setVisibility(View.GONE);
                attantionNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);

                f1KeteranganKunjunganED.setText("");
                f1PelangganOption.clearCheck();
                f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                f1PelangganBaruPart.setVisibility(View.GONE);
                f1PelangganLamaPart.setVisibility(View.GONE);
                f1DateChoice = "";
                f1ChoiceDateTV.setText("");
                f1JenisPelanggan = "";
                f1NamaPelangganLamaChoiceTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                f1DetailPelanggan.setVisibility(View.GONE);
                f1AlamatPelangganLamaTV.setText("");
                f1PicPelangganLamaTV.setText("");
                f1TeleponPelangganLamaTV.setText("");
                f1NamaPelangganBaruED.setText("");
                f1AlamatPelangganBaruED.setText("");
                f1PicPelangganBaruED.setText("");
                f1TeleponPelangganBaruED.setText("");
                f1AddPelangganPart.setVisibility(View.GONE);
                f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);

                f2KeteranganKunjunganED.setText("");
                f2PelangganOption.clearCheck();
                f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                f2PelangganBaruPart.setVisibility(View.GONE);
                f2PelangganLamaPart.setVisibility(View.GONE);
                f2NamaPelangganLamaChoiceTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                f2DetailPelanggan.setVisibility(View.GONE);
                f2AlamatPelangganLamaTV.setText("");
                f2PicPelangganLamaTV.setText("");
                f2TeleponPelangganLamaTV.setText("");
                f2DetailPesananPart.setVisibility(View.GONE);
                dataProduct.clear();

                adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                f2ListProductInputRV.setHasFixedSize(true);
                f2ListProductInputRV.setNestedScrollingEnabled(false);
                f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                f2ProductChoiceTV.setText("Pilih produk untuk menambahkan...");
                f2ProductHargaSatuanTV.setText("Rp 0");
                f2SubTotalTV.setText("Rp 0");
                f2QtyProductPicker.setMin(0);
                f2QtyProductPicker.setValue(0);
                f2IdProduct = "";
                f2ProductName = "";
                f2ProductHargaSatuan = "";
                f2QtyProduct = "";
                f2SubTotal = "";
                f2ProductInputDetailPart.setVisibility(View.GONE);
                f2TotalPesananTV.setText("Rp 0");
                f2TotalPesanan = "";
                f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                f2ViewLampiranBTN.setVisibility(View.GONE);
                f2JenisPelanggan = "";
                f2IdPelangganLama = "";
                f2NamaPelangganBaruED.setText("");
                f2AlamatPelangganBaruED.setText("");
                f2PicPelangganBaruED.setText("");
                f2TeleponPelangganBaruED.setText("");

                f3NamaPelangganLamaChoiceTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                f3DetailPelanggan.setVisibility(View.GONE);
                f3DetailListInvPart.setVisibility(View.GONE);
                f3AlamatPelangganLamaTV.setText("");
                f3PicPelangganLamaTV.setText("");
                f3TeleponPelangganLamaTV.setText("");
                f3IdPelangganLama = "";
                f3TotalPiutangTV.setText("Rp 0");
                f3KeteranganED.setText("");
                f3LabelLampiranTV.setText("+ Lampiran Foto/SP");
                f3ViewLampiranBTN.setVisibility(View.GONE);

                uri = null;
                fullBase64String = "";
                totalLaporan = 0;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        kunjunganFormPart.setVisibility(View.GONE);
                        penawaranFormPart.setVisibility(View.GONE);
                        penagihanFormPart.setVisibility(View.GONE);
                        rencanaKunjunganFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.VISIBLE);
                        loadingFormPart.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reportCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportCategory();
            }
        });

        f1ChoiceDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                f1KeteranganKunjunganED.clearFocus();

                f1DateVisit();
            }
        });

        f1PelangganOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                f1KeteranganKunjunganED.clearFocus();
                f1PelangganAttantionPart.setVisibility(View.GONE);
                if (f1PelangganOptionBaru.isChecked()) {
                    f1JenisPelanggan = "1";
                    f1PelangganBaruPart.setVisibility(View.VISIBLE);
                    f1PelangganLamaPart.setVisibility(View.GONE);

                    f1NamaPelangganLamaChoiceTV.setText("");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                    f1DetailPelanggan.setVisibility(View.GONE);
                    f1AlamatPelangganLamaTV.setText("");
                    f1PicPelangganLamaTV.setText("");
                    f1TeleponPelangganLamaTV.setText("");
                    f1AddPelangganPart.setVisibility(View.VISIBLE);
                } else if (f1PelangganOptionLama.isChecked()) {
                    f1JenisPelanggan = "2";
                    f1PelangganBaruPart.setVisibility(View.GONE);
                    f1PelangganLamaPart.setVisibility(View.VISIBLE);

                    f1NamaPelangganBaruED.setText("");
                    f1AlamatPelangganBaruED.setText("");
                    f1PicPelangganBaruED.setText("");
                    f1TeleponPelangganBaruED.setText("");
                    f1AddPelangganPart.setVisibility(View.GONE);
                }
            }
        });

        f1NamaPelangganLamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1PelangganLamaBottomSheet();
            }
        });

        f1AddPelangganBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                if (f1JenisPelanggan.equals("1")) {
                    if(!f1NamaPelangganBaruED.getText().toString().equals("") && !f1AlamatPelangganBaruED.getText().toString().equals("") && !f1PicPelangganBaruED.getText().toString().equals("") && !f1TeleponPelangganBaruED.getText().toString().equals("") && !f1KeteranganKunjunganED.getText().toString().equals("")){
                        JSONObject dataPelangganBaru = new JSONObject();
                        try {
                            dataPelangganBaru.put("idPelanggan", "null");
                            dataPelangganBaru.put("kategoriPelanggan", f1JenisPelanggan);
                            dataPelangganBaru.put("namaPelanggan", f1NamaPelangganBaruED.getText().toString());
                            dataPelangganBaru.put("alamatPelanggan", f1AlamatPelangganBaruED.getText().toString());
                            dataPelangganBaru.put("picPelanggan", f1PicPelangganBaruED.getText().toString());
                            dataPelangganBaru.put("teleponPelanggan", f1TeleponPelangganBaruED.getText().toString());
                            dataPelangganBaru.put("keteranganKunjunganPelanggan", f1KeteranganKunjunganED.getText().toString());
                            f1JsonArrayPelanggan.put(dataPelangganBaru);

                            f1PelangganOption.clearCheck();
                            f1JenisPelanggan = "";
                            f1NamaPelangganBaruED.setText("");
                            f1AlamatPelangganBaruED.setText("");
                            f1PicPelangganBaruED.setText("");
                            f1TeleponPelangganBaruED.setText("");
                            f1KeteranganKunjunganED.setText("");
                            f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                            f1PelangganLamaPart.setVisibility(View.GONE);
                            f1PelangganBaruPart.setVisibility(View.GONE);
                            f1AddPelangganPart.setVisibility(View.GONE);

                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                            f1NamaPelangganLamaChoiceTV.setText("");
                            f1DetailPelanggan.setVisibility(View.GONE);
                            f1AlamatPelangganLamaTV.setText("");
                            f1PicPelangganLamaTV.setText("");
                            f1TeleponPelangganLamaTV.setText("");

                            updateListPelanggan(f1JsonArrayPelanggan.toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi semua data pelanggan baru!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } else if (f1JenisPelanggan.equals("2")) {
                    if(!f1IdPelangganLama.equals("") && !f1KeteranganKunjunganED.getText().toString().equals("")){
                        JSONObject dataPelangganLama = new JSONObject();
                        try {
                            dataPelangganLama.put("idPelanggan", f1IdPelangganLama);
                            dataPelangganLama.put("kategoriPelanggan", f1JenisPelanggan);
                            dataPelangganLama.put("namaPelanggan", f1NamaPelangganLamaChoiceTV.getText().toString());
                            dataPelangganLama.put("alamatPelanggan", f1AlamatPelangganLamaTV.getText().toString());
                            dataPelangganLama.put("picPelanggan", f1PicPelangganLamaTV.getText().toString());
                            dataPelangganLama.put("teleponPelanggan", f1TeleponPelangganLamaTV.getText().toString());
                            dataPelangganLama.put("keteranganKunjunganPelanggan", f1KeteranganKunjunganED.getText().toString());
                            f1JsonArrayPelanggan.put(dataPelangganLama);

                            f1PelangganOption.clearCheck();
                            f1JenisPelanggan = "";

                            f1NamaPelangganBaruED.setText("");
                            f1AlamatPelangganBaruED.setText("");
                            f1PicPelangganBaruED.setText("");
                            f1TeleponPelangganBaruED.setText("");
                            f1KeteranganKunjunganED.setText("");
                            f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                            f1PelangganLamaPart.setVisibility(View.GONE);
                            f1PelangganBaruPart.setVisibility(View.GONE);
                            f1AddPelangganPart.setVisibility(View.GONE);

                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                            f1NamaPelangganLamaChoiceTV.setText("");
                            f1DetailPelanggan.setVisibility(View.GONE);
                            f1AlamatPelangganLamaTV.setText("");
                            f1PicPelangganLamaTV.setText("");
                            f1TeleponPelangganLamaTV.setText("");

                            updateListPelanggan(f1JsonArrayPelanggan.toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi semua data!")
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
            }
        });

        f2PelangganOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                f2KeteranganKunjunganED.clearFocus();
                f2PelangganAttantionPart.setVisibility(View.GONE);
                if (f2PelangganOptionBaru.isChecked()) {
                    f2JenisPelanggan = "1";
                    f2PelangganBaruPart.setVisibility(View.VISIBLE);
                    f2PelangganLamaPart.setVisibility(View.GONE);

                    f2NamaPelangganLamaChoiceTV.setText("");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                    f2IdPelangganLama = "";
                    f2DetailPelanggan.setVisibility(View.GONE);
                    f2AlamatPelangganLamaTV.setText("");
                    f2PicPelangganLamaTV.setText("");
                    f2TeleponPelangganLamaTV.setText("");
                    f2TotalPesananTV.setText("Rp 0");
                    f2TotalPesanan = "";
                    f2DetailPesananPart.setVisibility(View.VISIBLE);
                    dataProduct.clear();
                    f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                    f2ViewLampiranBTN.setVisibility(View.GONE);

                    adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                    f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                    f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                    f2ListProductInputRV.setHasFixedSize(true);
                    f2ListProductInputRV.setNestedScrollingEnabled(false);
                    f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());
                } else if (f2PelangganOptionLama.isChecked()) {
                    f2JenisPelanggan = "2";
                    f2PelangganBaruPart.setVisibility(View.GONE);
                    f2PelangganLamaPart.setVisibility(View.VISIBLE);
                    f2TotalPesananTV.setText("Rp 0");
                    f2TotalPesanan = "";
                    f2DetailPesananPart.setVisibility(View.GONE);
                    dataProduct.clear();
                    f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                    f2ViewLampiranBTN.setVisibility(View.GONE);

                    adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                    f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                    f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                    f2ListProductInputRV.setHasFixedSize(true);
                    f2ListProductInputRV.setNestedScrollingEnabled(false);
                    f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                    f2NamaPelangganBaruED.setText("");
                    f2AlamatPelangganBaruED.setText("");
                    f2PicPelangganBaruED.setText("");
                    f2TeleponPelangganBaruED.setText("");
                }
            }
        });

        f2SpinnerKategoriPelanggan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Di sini Anda bisa menanggapi perubahan opsi yang dipilih
                String selectedOption = (String) parentView.getItemAtPosition(position);
                // Contoh: Tampilkan opsi yang dipilih dalam Toast
                Toast.makeText(getApplicationContext(), "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Metode ini dipanggil jika tidak ada opsi yang dipilih
            }
        });


        f2NamaPelangganLamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2PelangganLamaBottomSheet();
            }
        });

        f2ProductChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2ProductListBottomSheet();
            }
        });

        f2AddProductBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                if (f2IdProduct.equals("")) {
                    f2QtyProductPicker.setValue(0);
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih produk terlebih dahulu")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    f2FullDataProduct = f2IdProduct + "/" + f2ProductName + "/" + f2ProductHargaSatuan + "/" + f2QtyProduct + "/" + f2SubTotal;
                    if (!f2FullDataProduct.isEmpty() || f2FullDataProduct.equals("")) {
                        dataProduct.add(f2FullDataProduct);
                        adapterProductInputSuma.notifyDataSetChanged();
                        f2ProductChoiceTV.setText("Pilih produk untuk menambahkan...");
                        f2ProductHargaSatuanTV.setText("Rp 0");
                        f2SubTotalTV.setText("Rp 0");
                        f2QtyProductPicker.setMin(0);
                        f2QtyProductPicker.setValue(0);
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                        f2FullDataProduct = "";
                        f2IdProduct = "";
                        f2ProductName = "";
                        f2ProductHargaSatuan = "";
                        f2QtyProduct = "";
                        f2SubTotal = "";
                        f2ProductInputDetailPart.setVisibility(View.GONE);
                        f2HitungTotalPesanan();
                    }
                }
            }
        });

        f2QtyProductPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                f2QtyProduct = String.valueOf(value);
                if (f2ProductHargaSatuan.equals("")) {
                    f2QtyProductPicker.setValue(0);
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih produk terlebih dahulu")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Locale localeID = new Locale("id", "ID");
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                    decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                    decimalFormat.setMaximumFractionDigits(0);
                    f2SubTotalTV.setText(decimalFormat.format(Integer.parseInt(String.valueOf(Integer.parseInt(f2ProductHargaSatuan) * value))));
                    f2SubTotal = String.valueOf(Integer.parseInt(f2ProductHargaSatuan) * value);
                }
            }
        });

        f2GPSLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportSumaActivity.this, LocationPickerActivity.class);
                startActivity(intent);
            }
        });

        f2LampiranFotoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        f3NamaPelangganLamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1PelangganLamaBottomSheet();
            }
        });

        f3GPSLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportSumaActivity.this, LocationPickerActivity.class);
                startActivity(intent);
            }
        });

        f3LampiranFotoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        f1SubmitPesananBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!categoryReport.equals("") && !f1DateChoice.equals("") && f1JsonArrayPelanggan.length() > 0){
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    pDialog = new KAlertDialog(ReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (ReportSumaActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (ReportSumaActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (ReportSumaActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (ReportSumaActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (ReportSumaActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (ReportSumaActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            submitLaporan();
                                            Log.d("Data Customer : ", f1JsonArrayPelanggan.toString());
                                        }
                                    }.start();

                                }
                            })
                            .show();
                } else {
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi semua data!")
                            .setConfirmText("   OK   ")
                            .showCancelButton(true)
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

        f2SubmitPesananBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2KeteranganKunjunganED.clearFocus();
                if(!categoryReport.equals("") && !f2KeteranganKunjunganED.getText().toString().equals("") && !f2JenisPelanggan.equals("")){
                    if(f2JenisPelanggan.equals("1")){
                        if(!f2NamaPelangganBaruED.getText().toString().equals("") && !f2AlamatPelangganBaruED.getText().toString().equals("") && !f2PicPelangganBaruED.getText().toString().equals("") && !f2TeleponPelangganBaruED.getText().toString().equals("")){
                            if(salesLat.equals("")||salesLong.equals("")||String.valueOf(uri).equals("null")){
                                new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi semua data!")
                                        .setConfirmText("   OK   ")
                                        .showCancelButton(true)
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                pDialog = new KAlertDialog(ReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;
                                                        submitLaporan();
                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }
                        } else {
                            new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi semua data pelanggan baru!")
                                    .setConfirmText("   OK   ")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    } else if(f2JenisPelanggan.equals("2")){
                        if(f2IdPelangganLama.equals("")){
                            new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Harap isi semua data!")
                                    .setConfirmText("   OK   ")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            if(salesLat.equals("")||salesLong.equals("")||String.valueOf(uri).equals("null")){
                                new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Harap isi semua data!")
                                        .setConfirmText("   OK   ")
                                        .showCancelButton(true)
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                                pDialog = new KAlertDialog(ReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (ReportSumaActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;
                                                        submitLaporan();
                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                } else {
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi semua data!")
                            .setConfirmText("   OK   ")
                            .showCancelButton(true)
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

        f3SubmitPesananBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3KeteranganED.clearFocus();
                if(!categoryReport.equals("") && !f3IdPelangganLama.equals("") && !f3KeteranganED.getText().toString().equals("")){
                    if(salesLat.equals("")||salesLong.equals("")||String.valueOf(uri).equals("null")){
                        new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Harap isi semua data!")
                                .setConfirmText("   OK   ")
                                .showCancelButton(true)
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                        pDialog = new KAlertDialog(ReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (ReportSumaActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (ReportSumaActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (ReportSumaActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (ReportSumaActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (ReportSumaActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (ReportSumaActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }
                                            public void onFinish() {
                                                i = -1;
                                                submitLaporan();
                                            }
                                        }.start();

                                    }
                                })
                                .show();
                    }
                } else {
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi semua data!")
                            .setConfirmText("   OK   ")
                            .showCancelButton(true)
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

        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
        loadingFormPart.setVisibility(View.VISIBLE);
        attantionNoForm.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingFormPart.setVisibility(View.GONE);
                attantionNoForm.setVisibility(View.VISIBLE);
            }
        }, 1800);

    }

    private void reportCategory() {
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_kategori_report_suma, bottomSheet, false));
        rencanaKunjunaganBTN = findViewById(R.id.rencana_kunjunagan_btn);
        kunjunganBTN = findViewById(R.id.kunjungan_btn);
        penawaranBTN = findViewById(R.id.penawaran_btn);
        penagihanBTN = findViewById(R.id.penagihan_btn);
        markRencanaKunjungan = findViewById(R.id.mark_rencana_kunjungan);
        markKunjungan = findViewById(R.id.mark_kunjungan);
        markPenawaran = findViewById(R.id.mark_penawaran);
        markPenagihan = findViewById(R.id.mark_penagihan);

        if (categoryReport.equals("1")) {
            markRencanaKunjungan.setVisibility(View.VISIBLE);
            markKunjungan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
            kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
        } else if (categoryReport.equals("2")) {
            markRencanaKunjungan.setVisibility(View.GONE);
            markKunjungan.setVisibility(View.VISIBLE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
            penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
        } else if (categoryReport.equals("4")) {
            markRencanaKunjungan.setVisibility(View.GONE);
            markKunjungan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.VISIBLE);
            markPenagihan.setVisibility(View.GONE);
            rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
            penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
        } else if (categoryReport.equals("3")) {
            markRencanaKunjungan.setVisibility(View.GONE);
            markKunjungan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.VISIBLE);
            rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
        } else {
            markRencanaKunjungan.setVisibility(View.GONE);
            markKunjungan.setVisibility(View.GONE);
            markPenawaran.setVisibility(View.GONE);
            markPenagihan.setVisibility(View.GONE);
            rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
        }

        rencanaKunjunaganBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryReport = "1";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
                reportKategoriChoiceTV.setText("Rencana Kunjungan");
                markRencanaKunjungan.setVisibility(View.VISIBLE);
                markKunjungan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
                kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        kunjunganFormPart.setVisibility(View.GONE);
                        penawaranFormPart.setVisibility(View.GONE);
                        penagihanFormPart.setVisibility(View.GONE);
                        rencanaKunjunganFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.GONE);
                        loadingFormPart.setVisibility(View.VISIBLE);

                        f1KeteranganKunjunganED.setText("");
                        f1PelangganOption.clearCheck();
                        f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f1PelangganBaruPart.setVisibility(View.GONE);
                        f1PelangganLamaPart.setVisibility(View.GONE);
                        f1DateChoice = "";
                        f1ChoiceDateTV.setText("");
                        f1JenisPelanggan = "";
                        f1NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f1DetailPelanggan.setVisibility(View.GONE);
                        f1AlamatPelangganLamaTV.setText("");
                        f1PicPelangganLamaTV.setText("");
                        f1TeleponPelangganLamaTV.setText("");
                        f1AddPelangganPart.setVisibility(View.GONE);
                        f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);

                        f2KeteranganKunjunganED.setText("");
                        f2PelangganOption.clearCheck();
                        f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f2PelangganBaruPart.setVisibility(View.GONE);
                        f2PelangganLamaPart.setVisibility(View.GONE);
                        f2NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f2DetailPelanggan.setVisibility(View.GONE);
                        f2AlamatPelangganLamaTV.setText("");
                        f2PicPelangganLamaTV.setText("");
                        f2TeleponPelangganLamaTV.setText("");
                        f2DetailPesananPart.setVisibility(View.GONE);
                        dataProduct.clear();
                        f2JenisPelanggan = "";
                        f2IdPelangganLama = "";

                        adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                        f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                        f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                        f2ListProductInputRV.setHasFixedSize(true);
                        f2ListProductInputRV.setNestedScrollingEnabled(false);
                        f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                        f2ProductChoiceTV.setText("Pilih produk untuk menambahkan...");
                        f2ProductHargaSatuanTV.setText("Rp 0");
                        f2SubTotalTV.setText("Rp 0");
                        f2QtyProductPicker.setMin(0);
                        f2QtyProductPicker.setValue(0);
                        f2IdProduct = "";
                        f2ProductName = "";
                        f2ProductHargaSatuan = "";
                        f2QtyProduct = "";
                        f2SubTotal = "";
                        f2ProductInputDetailPart.setVisibility(View.GONE);
                        f2TotalPesananTV.setText("Rp 0");
                        f2TotalPesanan = "";
                        f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f2ViewLampiranBTN.setVisibility(View.GONE);

                        f1NamaPelangganBaruED.setText("");
                        f1AlamatPelangganBaruED.setText("");
                        f1PicPelangganBaruED.setText("");
                        f1TeleponPelangganBaruED.setText("");
                        f2NamaPelangganBaruED.setText("");
                        f2AlamatPelangganBaruED.setText("");
                        f2PicPelangganBaruED.setText("");
                        f2TeleponPelangganBaruED.setText("");

                        f3NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f3DetailPelanggan.setVisibility(View.GONE);
                        f3DetailListInvPart.setVisibility(View.GONE);
                        f3AlamatPelangganLamaTV.setText("");
                        f3PicPelangganLamaTV.setText("");
                        f3TeleponPelangganLamaTV.setText("");
                        f3IdPelangganLama = "";
                        f3TotalPiutangTV.setText("Rp 0");
                        f3KeteranganED.setText("");
                        f3LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f3ViewLampiranBTN.setVisibility(View.GONE);

                        uri = null;
                        fullBase64String = "";
                        totalLaporan = 0;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                kunjunganFormPart.setVisibility(View.GONE);
                                penawaranFormPart.setVisibility(View.GONE);
                                penagihanFormPart.setVisibility(View.GONE);
                                rencanaKunjunganFormPart.setVisibility(View.VISIBLE);
                                attantionNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                            }
                        }, 1300);

                    }
                }, 300);
            }
        });

        kunjunganBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryReport = "2";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
                reportKategoriChoiceTV.setText("Laporan Kunjungan");
                markRencanaKunjungan.setVisibility(View.GONE);
                markKunjungan.setVisibility(View.VISIBLE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.GONE);
                rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        kunjunganFormPart.setVisibility(View.GONE);
                        penawaranFormPart.setVisibility(View.GONE);
                        penagihanFormPart.setVisibility(View.GONE);
                        rencanaKunjunganFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.GONE);
                        loadingFormPart.setVisibility(View.VISIBLE);

                        f1KeteranganKunjunganED.setText("");
                        f1PelangganOption.clearCheck();
                        f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f1PelangganBaruPart.setVisibility(View.GONE);
                        f1PelangganLamaPart.setVisibility(View.GONE);
                        f1DateChoice = "";
                        f1ChoiceDateTV.setText("");
                        f1JenisPelanggan = "";
                        f1NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f1DetailPelanggan.setVisibility(View.GONE);
                        f1AlamatPelangganLamaTV.setText("");
                        f1PicPelangganLamaTV.setText("");
                        f1TeleponPelangganLamaTV.setText("");
                        f1AddPelangganPart.setVisibility(View.GONE);
                        f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);

                        f2KeteranganKunjunganED.setText("");
                        f2PelangganOption.clearCheck();
                        f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f2PelangganBaruPart.setVisibility(View.GONE);
                        f2PelangganLamaPart.setVisibility(View.GONE);
                        f2NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f2DetailPelanggan.setVisibility(View.GONE);
                        f2AlamatPelangganLamaTV.setText("");
                        f2PicPelangganLamaTV.setText("");
                        f2TeleponPelangganLamaTV.setText("");
                        f2DetailPesananPart.setVisibility(View.GONE);
                        dataProduct.clear();
                        f2JenisPelanggan = "";
                        f2IdPelangganLama = "";

                        adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                        f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                        f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                        f2ListProductInputRV.setHasFixedSize(true);
                        f2ListProductInputRV.setNestedScrollingEnabled(false);
                        f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                        f2ProductChoiceTV.setText("Pilih produk untuk menambahkan...");
                        f2ProductHargaSatuanTV.setText("Rp 0");
                        f2SubTotalTV.setText("Rp 0");
                        f2QtyProductPicker.setMin(0);
                        f2QtyProductPicker.setValue(0);
                        f2IdProduct = "";
                        f2ProductName = "";
                        f2ProductHargaSatuan = "";
                        f2QtyProduct = "";
                        f2SubTotal = "";
                        f2ProductInputDetailPart.setVisibility(View.GONE);
                        f2TotalPesananTV.setText("Rp 0");
                        f2TotalPesanan = "";
                        f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f2ViewLampiranBTN.setVisibility(View.GONE);

                        f1NamaPelangganBaruED.setText("");
                        f1AlamatPelangganBaruED.setText("");
                        f1PicPelangganBaruED.setText("");
                        f1TeleponPelangganBaruED.setText("");
                        f2NamaPelangganBaruED.setText("");
                        f2AlamatPelangganBaruED.setText("");
                        f2PicPelangganBaruED.setText("");
                        f2TeleponPelangganBaruED.setText("");

                        f3NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f3DetailPelanggan.setVisibility(View.GONE);
                        f3DetailListInvPart.setVisibility(View.GONE);
                        f3AlamatPelangganLamaTV.setText("");
                        f3PicPelangganLamaTV.setText("");
                        f3TeleponPelangganLamaTV.setText("");
                        f3IdPelangganLama = "";
                        f3TotalPiutangTV.setText("Rp 0");
                        f3KeteranganED.setText("");
                        f3LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f3ViewLampiranBTN.setVisibility(View.GONE);

                        uri = null;
                        fullBase64String = "";
                        totalLaporan = 0;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                kunjunganFormPart.setVisibility(View.VISIBLE);
                                penawaranFormPart.setVisibility(View.GONE);
                                penagihanFormPart.setVisibility(View.GONE);
                                rencanaKunjunganFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                            }
                        }, 1300);

                    }
                }, 300);
            }
        });

        penagihanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryReport = "3";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
                reportKategoriChoiceTV.setText("Aktivitas Penagihan");
                markKunjungan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.GONE);
                markPenagihan.setVisibility(View.VISIBLE);
                markRencanaKunjungan.setVisibility(View.GONE);
                kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
                rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        kunjunganFormPart.setVisibility(View.GONE);
                        penawaranFormPart.setVisibility(View.GONE);
                        penagihanFormPart.setVisibility(View.GONE);
                        rencanaKunjunganFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.GONE);
                        loadingFormPart.setVisibility(View.VISIBLE);

                        f2KeteranganKunjunganED.setText("");
                        f1PelangganOption.clearCheck();
                        f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f1PelangganBaruPart.setVisibility(View.GONE);
                        f1PelangganLamaPart.setVisibility(View.GONE);
                        f1DateChoice = "";
                        f1ChoiceDateTV.setText("");
                        f1JenisPelanggan = "";
                        f1NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f1DetailPelanggan.setVisibility(View.GONE);
                        f1AlamatPelangganLamaTV.setText("");
                        f1PicPelangganLamaTV.setText("");
                        f1TeleponPelangganLamaTV.setText("");
                        f1AddPelangganPart.setVisibility(View.GONE);
                        f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);

                        f2KeteranganKunjunganED.setText("");
                        f2PelangganOption.clearCheck();
                        f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f2PelangganBaruPart.setVisibility(View.GONE);
                        f2PelangganLamaPart.setVisibility(View.GONE);
                        f2NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f2DetailPelanggan.setVisibility(View.GONE);
                        f2AlamatPelangganLamaTV.setText("");
                        f2PicPelangganLamaTV.setText("");
                        f2TeleponPelangganLamaTV.setText("");
                        f2DetailPesananPart.setVisibility(View.GONE);
                        dataProduct.clear();
                        f2JenisPelanggan = "";
                        f2IdPelangganLama = "";

                        adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                        f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                        f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                        f2ListProductInputRV.setHasFixedSize(true);
                        f2ListProductInputRV.setNestedScrollingEnabled(false);
                        f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                        f2ProductChoiceTV.setText("Pilih produk untuk menambahkan...");
                        f2ProductHargaSatuanTV.setText("Rp 0");
                        f2SubTotalTV.setText("Rp 0");
                        f2QtyProductPicker.setMin(0);
                        f2QtyProductPicker.setValue(0);
                        f2IdProduct = "";
                        f2ProductName = "";
                        f2ProductHargaSatuan = "";
                        f2QtyProduct = "";
                        f2SubTotal = "";
                        f2ProductInputDetailPart.setVisibility(View.GONE);
                        f2TotalPesananTV.setText("Rp 0");
                        f2TotalPesanan = "";
                        f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f2ViewLampiranBTN.setVisibility(View.GONE);

                        f1NamaPelangganBaruED.setText("");
                        f1AlamatPelangganBaruED.setText("");
                        f1PicPelangganBaruED.setText("");
                        f1TeleponPelangganBaruED.setText("");
                        f2NamaPelangganBaruED.setText("");
                        f2AlamatPelangganBaruED.setText("");
                        f2PicPelangganBaruED.setText("");
                        f2TeleponPelangganBaruED.setText("");

                        f3NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f3DetailPelanggan.setVisibility(View.GONE);
                        f3DetailListInvPart.setVisibility(View.GONE);
                        f3AlamatPelangganLamaTV.setText("");
                        f3PicPelangganLamaTV.setText("");
                        f3TeleponPelangganLamaTV.setText("");
                        f3IdPelangganLama = "";
                        f3TotalPiutangTV.setText("Rp 0");
                        f3KeteranganED.setText("");
                        f3LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f3ViewLampiranBTN.setVisibility(View.GONE);

                        uri = null;
                        fullBase64String = "";
                        totalLaporan = 0;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                kunjunganFormPart.setVisibility(View.GONE);
                                penawaranFormPart.setVisibility(View.GONE);
                                penagihanFormPart.setVisibility(View.VISIBLE);
                                rencanaKunjunganFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                            }
                        }, 1300);

                    }
                }, 300);
            }
        });

        penawaranBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryReport = "4";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
                reportKategoriChoiceTV.setText("Penawaran");
                markKunjungan.setVisibility(View.GONE);
                markPenawaran.setVisibility(View.VISIBLE);
                markPenagihan.setVisibility(View.GONE);
                markRencanaKunjungan.setVisibility(View.GONE);
                kunjunganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                penawaranBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
                penagihanBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                rencanaKunjunaganBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        kunjunganFormPart.setVisibility(View.GONE);
                        penawaranFormPart.setVisibility(View.GONE);
                        penagihanFormPart.setVisibility(View.GONE);
                        rencanaKunjunganFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.GONE);
                        loadingFormPart.setVisibility(View.VISIBLE);

                        f2KeteranganKunjunganED.setText("");
                        f1PelangganOption.clearCheck();
                        f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f1PelangganBaruPart.setVisibility(View.GONE);
                        f1PelangganLamaPart.setVisibility(View.GONE);
                        f1DateChoice = "";
                        f1ChoiceDateTV.setText("");
                        f1JenisPelanggan = "";
                        f1NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f1DetailPelanggan.setVisibility(View.GONE);
                        f1AlamatPelangganLamaTV.setText("");
                        f1PicPelangganLamaTV.setText("");
                        f1TeleponPelangganLamaTV.setText("");
                        f1AddPelangganPart.setVisibility(View.GONE);
                        f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);

                        f2KeteranganKunjunganED.setText("");
                        f2PelangganOption.clearCheck();
                        f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f2PelangganBaruPart.setVisibility(View.GONE);
                        f2PelangganLamaPart.setVisibility(View.GONE);
                        f2NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f2DetailPelanggan.setVisibility(View.GONE);
                        f2AlamatPelangganLamaTV.setText("");
                        f2PicPelangganLamaTV.setText("");
                        f2TeleponPelangganLamaTV.setText("");
                        f2DetailPesananPart.setVisibility(View.GONE);
                        dataProduct.clear();
                        f2JenisPelanggan = "";
                        f2IdPelangganLama = "";

                        adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                        f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                        f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                        f2ListProductInputRV.setHasFixedSize(true);
                        f2ListProductInputRV.setNestedScrollingEnabled(false);
                        f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                        f2ProductChoiceTV.setText("Pilih produk untuk menambahkan...");
                        f2ProductHargaSatuanTV.setText("Rp 0");
                        f2SubTotalTV.setText("Rp 0");
                        f2QtyProductPicker.setMin(0);
                        f2QtyProductPicker.setValue(0);
                        f2IdProduct = "";
                        f2ProductName = "";
                        f2ProductHargaSatuan = "";
                        f2QtyProduct = "";
                        f2SubTotal = "";
                        f2ProductInputDetailPart.setVisibility(View.GONE);
                        f2TotalPesananTV.setText("Rp 0");
                        f2TotalPesanan = "";
                        f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f2ViewLampiranBTN.setVisibility(View.GONE);

                        f1NamaPelangganBaruED.setText("");
                        f1AlamatPelangganBaruED.setText("");
                        f1PicPelangganBaruED.setText("");
                        f1TeleponPelangganBaruED.setText("");
                        f2NamaPelangganBaruED.setText("");
                        f2AlamatPelangganBaruED.setText("");
                        f2PicPelangganBaruED.setText("");
                        f2TeleponPelangganBaruED.setText("");

                        f3NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f3DetailPelanggan.setVisibility(View.GONE);
                        f3DetailListInvPart.setVisibility(View.GONE);
                        f3AlamatPelangganLamaTV.setText("");
                        f3PicPelangganLamaTV.setText("");
                        f3TeleponPelangganLamaTV.setText("");
                        f3IdPelangganLama = "";
                        f3TotalPiutangTV.setText("Rp 0");
                        f3KeteranganED.setText("");
                        f3LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f3ViewLampiranBTN.setVisibility(View.GONE);

                        uri = null;
                        fullBase64String = "";
                        totalLaporan = 0;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                kunjunganFormPart.setVisibility(View.GONE);
                                penawaranFormPart.setVisibility(View.VISIBLE);
                                penagihanFormPart.setVisibility(View.GONE);
                                rencanaKunjunganFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                            }
                        }, 1300);

                    }
                }, 300);
            }
        });


    }

    @SuppressLint("SimpleDateFormat")
    private void f1DateVisit(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(ReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1DateChoice = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1DateChoice));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long choice = date.getTime();
                long now = date2.getTime();

                if (choice>=now){
                    String input_date = f1DateChoice;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
                    DateFormat getweek = new SimpleDateFormat("W");
                    String finalDay = format2.format(dt1);
                    String week = getweek.format(dt1);
                    String hariName = "";

                    if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                        hariName = "Senin";
                    } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                        hariName = "Selasa";
                    } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                        hariName = "Rabu";
                    } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                        hariName = "Kamis";
                    } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                        hariName = "Jumat";
                    } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                        hariName = "Sabtu";
                    } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                        hariName = "Minggu";
                    }

                    String dayDate = input_date.substring(8,10);
                    String yearDate = input_date.substring(0,4);
                    String bulanValue = input_date.substring(5,7);
                    String bulanName;

                    switch (bulanValue) {
                        case "01":
                            bulanName = "Januari";
                            break;
                        case "02":
                            bulanName = "Februari";
                            break;
                        case "03":
                            bulanName = "Maret";
                            break;
                        case "04":
                            bulanName = "April";
                            break;
                        case "05":
                            bulanName = "Mei";
                            break;
                        case "06":
                            bulanName = "Juni";
                            break;
                        case "07":
                            bulanName = "Juli";
                            break;
                        case "08":
                            bulanName = "Agustus";
                            break;
                        case "09":
                            bulanName = "September";
                            break;
                        case "10":
                            bulanName = "Oktober";
                            break;
                        case "11":
                            bulanName = "November";
                            break;
                        case "12":
                            bulanName = "Desember";
                            break;
                        default:
                            bulanName = "Not found!";
                            break;
                    }

                    f1ChoiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                } else {
                    f1ChoiceDateTV.setText("Pilih Kembali !");
                    f1DateChoice = "";

                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tidak bisa memilih tanggal lampau!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(ReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1DateChoice = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1DateChoice));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long choice = date.getTime();
                long now = date2.getTime();

                if (choice>=now){
                    String input_date = f1DateChoice;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat format2 = new SimpleDateFormat("EEE");
                    DateFormat getweek = new SimpleDateFormat("W");
                    String finalDay = format2.format(dt1);
                    String week = getweek.format(dt1);
                    String hariName = "";

                    if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                        hariName = "Senin";
                    } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                        hariName = "Selasa";
                    } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                        hariName = "Rabu";
                    } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                        hariName = "Kamis";
                    } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                        hariName = "Jumat";
                    } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                        hariName = "Sabtu";
                    } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                        hariName = "Minggu";
                    }

                    String dayDate = input_date.substring(8,10);
                    String yearDate = input_date.substring(0,4);
                    String bulanValue = input_date.substring(5,7);
                    String bulanName;

                    switch (bulanValue) {
                        case "01":
                            bulanName = "Januari";
                            break;
                        case "02":
                            bulanName = "Februari";
                            break;
                        case "03":
                            bulanName = "Maret";
                            break;
                        case "04":
                            bulanName = "April";
                            break;
                        case "05":
                            bulanName = "Mei";
                            break;
                        case "06":
                            bulanName = "Juni";
                            break;
                        case "07":
                            bulanName = "Juli";
                            break;
                        case "08":
                            bulanName = "Agustus";
                            break;
                        case "09":
                            bulanName = "September";
                            break;
                        case "10":
                            bulanName = "Oktober";
                            break;
                        case "11":
                            bulanName = "November";
                            break;
                        case "12":
                            bulanName = "Desember";
                            break;
                        default:
                            bulanName = "Not found!";
                            break;
                    }

                    f1ChoiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                } else {
                    f1ChoiceDateTV.setText("Pilih Kembali !");
                    f1DateChoice = "";

                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tidak bisa memilih tanggal lampau!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }

            }, y,m-1,d);
            dpd.show();
        }


    }

    private void f1PelangganLamaBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_nama_pelanggan_lama, bottomSheet, false));
        keywordED = findViewById(R.id.keyword_ed);
        keywordED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        pelangganRV = findViewById(R.id.pelanggan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

        pelangganRV.setLayoutManager(new LinearLayoutManager(this));
        pelangganRV.setHasFixedSize(true);
        pelangganRV.setNestedScrollingEnabled(false);
        pelangganRV.setItemAnimator(new DefaultItemAnimator());

        keywordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                f1KeteranganKunjunganED.clearFocus();

                String keyWordSearch = keywordED.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                pelangganRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

    }

    private void f2PelangganLamaBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_nama_pelanggan_lama, bottomSheet, false));
        keywordED = findViewById(R.id.keyword_ed);
        keywordED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        pelangganRV = findViewById(R.id.pelanggan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

        pelangganRV.setLayoutManager(new LinearLayoutManager(this));
        pelangganRV.setHasFixedSize(true);
        pelangganRV.setNestedScrollingEnabled(false);
        pelangganRV.setItemAnimator(new DefaultItemAnimator());

        keywordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                f2KeteranganKunjunganED.clearFocus();

                String keyWordSearch = keywordED.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                pelangganRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

    }

    private void f3PelangganLamaBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_nama_pelanggan_lama, bottomSheet, false));
        keywordED = findViewById(R.id.keyword_ed);
        keywordED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        pelangganRV = findViewById(R.id.pelanggan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);
        loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGif);

        pelangganRV.setLayoutManager(new LinearLayoutManager(this));
        pelangganRV.setHasFixedSize(true);
        pelangganRV.setNestedScrollingEnabled(false);
        pelangganRV.setItemAnimator(new DefaultItemAnimator());

        keywordED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //f3KeteranganKunjunganED.clearFocus();

                String keyWordSearch = keywordED.getText().toString();

                startAttantionPart.setVisibility(View.GONE);
                loadingDataPart.setVisibility(View.VISIBLE);
                noDataPart.setVisibility(View.GONE);
                pelangganRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

    }

    private void f2ProductListBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_product, bottomSheet, false));
        keywordEDProduk = findViewById(R.id.keyword_ed_produk);
        keywordEDProduk.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        produkRV = findViewById(R.id.produk_rv);
        startAttantionPartProduk = findViewById(R.id.attantion_data_part_produk);
        noDataPartProduk = findViewById(R.id.no_data_part_produk);
        loadingDataPartProduk = findViewById(R.id.loading_data_part_produk);
        loadingGifProduk = findViewById(R.id.loading_data_produk);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingGifProduk);

        produkRV.setLayoutManager(new LinearLayoutManager(this));
        produkRV.setHasFixedSize(true);
        produkRV.setNestedScrollingEnabled(false);
        produkRV.setItemAnimator(new DefaultItemAnimator());

        keywordEDProduk.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                f2KeteranganKunjunganED.clearFocus();

                String keyWordSearch = keywordEDProduk.getText().toString();

                startAttantionPartProduk.setVisibility(View.GONE);
                loadingDataPartProduk.setVisibility(View.VISIBLE);
                noDataPartProduk.setVisibility(View.GONE);
                produkRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getProduct(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

    }

    private void getPelangganLama(String keyword) {
        final String API_ENDPOINT_CUSTOMER = "https://reporting.sumasistem.co.id/api/list_customer_lama/" + keyword;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CUSTOMER,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            Log.d("Success.Response", response.toString());
                            String status = response.getString("status");

                            if (status.equals("Success")) {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.GONE);
                                pelangganRV.setVisibility(View.VISIBLE);

                                String data = response.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                pelangganLamas = gson.fromJson(data, PelangganLama[].class);
                                adapterPelangganLama = new AdapterPelangganLama(pelangganLamas, ReportSumaActivity.this);
                                pelangganRV.setAdapter(adapterPelangganLama);
                            } else {
                                startAttantionPart.setVisibility(View.GONE);
                                loadingDataPart.setVisibility(View.GONE);
                                noDataPart.setVisibility(View.VISIBLE);
                                pelangganRV.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                }) {
        };

        requestQueue.add(jsonObjectRequest);

    }

    private void getProduct(String keyword) {
        String wilayah = sharedPrefManager.getSpCabName();
        final String API_ENDPOINT_CUSTOMER = "https://reporting.sumasistem.co.id/api/list_produk?keyword="+keyword+"&wilayah="+wilayah;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CUSTOMER,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            Log.d("Success.Response", response.toString());
                            String status = response.getString("status");

                            if (status.equals("Success")) {
                                startAttantionPartProduk.setVisibility(View.GONE);
                                loadingDataPartProduk.setVisibility(View.GONE);
                                noDataPartProduk.setVisibility(View.GONE);
                                produkRV.setVisibility(View.VISIBLE);

                                String data = response.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                productSumas = gson.fromJson(data, ProductSuma[].class);
                                adapterProductSuma = new AdapterProductSuma(productSumas, ReportSumaActivity.this);
                                produkRV.setAdapter(adapterProductSuma);
                            } else {
                                startAttantionPartProduk.setVisibility(View.GONE);
                                loadingDataPartProduk.setVisibility(View.GONE);
                                noDataPartProduk.setVisibility(View.VISIBLE);
                                produkRV.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                }) {
        };

        requestQueue.add(jsonObjectRequest);

    }

    public BroadcastReceiver pelangganLamaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String namaPelangganLama = intent.getStringExtra("nama_pelanggan_lama");
            String idPelanggan = intent.getStringExtra("id_pelanggan_lama");
            String alamatPelanggan = intent.getStringExtra("alamat_pelanggan_lama");
            String picPelanggan = intent.getStringExtra("pic_pelanggan_lama");
            String teleponPelanggan = intent.getStringExtra("telepon_pelanggan_lama");

            if(categoryReport.equals("1")){
                f1AlamatPelangganLamaTV.setText(alamatPelanggan);
                f1PicPelangganLamaTV.setText(picPelanggan);
                f1TeleponPelangganLamaTV.setText(teleponPelanggan);

                f1IdPelangganLama = idPelanggan;
                f1DetailPelanggan.setVisibility(View.VISIBLE);

                f1NamaPelangganLamaChoiceTV.setText(namaPelangganLama);
                f1AddPelangganPart.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = ReportSumaActivity.this.getCurrentFocus();
                if (view == null) {
                    view = new View(ReportSumaActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                f1KeteranganKunjunganED.clearFocus();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            } else if(categoryReport.equals("2")){
                f2AlamatPelangganLamaTV.setText(alamatPelanggan);
                f2PicPelangganLamaTV.setText(picPelanggan);
                f2TeleponPelangganLamaTV.setText(teleponPelanggan);

                f2IdPelangganLama = idPelanggan;
                f2DetailPelanggan.setVisibility(View.VISIBLE);
                f2DetailPesananPart.setVisibility(View.VISIBLE);

                f2NamaPelangganLamaChoiceTV.setText(namaPelangganLama);

                InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = ReportSumaActivity.this.getCurrentFocus();
                if (view == null) {
                    view = new View(ReportSumaActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                f2KeteranganKunjunganED.clearFocus();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            } else if(categoryReport.equals("3")){
                f3AlamatPelangganLamaTV.setText(alamatPelanggan);
                f3PicPelangganLamaTV.setText(picPelanggan);
                f3TeleponPelangganLamaTV.setText(teleponPelanggan);

                f3IdPelangganLama = idPelanggan;
                f3DetailPelanggan.setVisibility(View.VISIBLE);
                f3DetailListInvPart.setVisibility(View.VISIBLE);

                f3NamaPelangganLamaChoiceTV.setText(namaPelangganLama);

                InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = ReportSumaActivity.this.getCurrentFocus();
                if (view == null) {
                    view = new View(ReportSumaActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                f3KeteranganED.clearFocus();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        f3LoadingDataPiutang.setVisibility(View.VISIBLE);
                        f3InvRV.setVisibility(View.GONE);
                        f3NoDataPiutang.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDataInvoice(f3IdPelangganLama);
                            }
                        }, 300);
                    }
                }, 300);
            }

        }
    };

    private void getDataInvoice(String id_pelanggan) {
        final String API_ENDPOINT_CUSTOMER = "https://reporting.sumasistem.co.id/api/tagihan_per_invoice?customerId="+id_pelanggan;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT_CUSTOMER,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());
                        try {
                            Log.d("Success.Response", response.toString());
                            String status = response.getString("status");

                            if (status.equals("200")) {
                                JSONArray mainData = response.getJSONArray("data");
                                if(mainData.length() > 0){
                                    f3LoadingDataPiutang.setVisibility(View.GONE);
                                    f3InvRV.setVisibility(View.VISIBLE);
                                    f3NoDataPiutang.setVisibility(View.GONE);

                                    f3InvRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                                    f3InvRV.setHasFixedSize(true);
                                    f3InvRV.setNestedScrollingEnabled(false);
                                    f3InvRV.setItemAnimator(new DefaultItemAnimator());

                                    JSONArray jsonArray = new JSONArray();
                                    JSONObject data = new JSONObject();
                                    JSONObject prodctData = new JSONObject();
                                    try {
                                        int grandTotal = 0;

                                        for (int i = 0; i < mainData.length(); i++) {
                                            JSONObject dataRecap = new JSONObject();
                                            data = mainData.getJSONObject(i);
                                            String nomor_Invoice = data.getString("nomor_Invoice");
                                            String tanggal_pemesanan = data.getString("tanggal_pemesanan");
                                            JSONArray product = data.getJSONArray("produk");
                                            int total = 0;
                                            for (int j = 0; j < product.length(); j++) {
                                                prodctData = product.getJSONObject(j);
                                                total = total + Integer.parseInt(prodctData.getString("total"));
                                                if((product.length()-1)==j){
                                                    grandTotal = grandTotal + total;
                                                    dataRecap.put("noInvoice", nomor_Invoice);
                                                    dataRecap.put("tglPemesanan", tanggal_pemesanan);
                                                    dataRecap.put("piutang", total);
                                                    jsonArray.put(dataRecap);
                                                }
                                            }
                                        }

                                        totalLaporan = grandTotal;

                                        String jsonString = jsonArray.toString();
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        dataInvoicePiutangs = gson.fromJson(jsonString, DataInvoicePiutang[].class);
                                        adapterInvoicePiutang = new AdapterInvoicePiutang(dataInvoicePiutangs, ReportSumaActivity.this);
                                        f3InvRV.setAdapter(adapterInvoicePiutang);

                                        Locale localeID = new Locale("id", "ID");
                                        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                        decimalFormat.setMaximumFractionDigits(0);

                                        f3TotalPiutangTV.setText(decimalFormat.format(grandTotal));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    f3LoadingDataPiutang.setVisibility(View.GONE);
                                    f3InvRV.setVisibility(View.GONE);
                                    f3NoDataPiutang.setVisibility(View.VISIBLE);
                                }

                            } else {
                                f3LoadingDataPiutang.setVisibility(View.GONE);
                                f3InvRV.setVisibility(View.GONE);
                                f3NoDataPiutang.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                }) {
        };

        requestQueue.add(jsonObjectRequest);

    }

    public BroadcastReceiver productSumaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_product = intent.getStringExtra("id_product");
            String nama_product = intent.getStringExtra("nama_product");
            String harga_satuan = intent.getStringExtra("harga_satuan");

            Locale localeID = new Locale("id", "ID");
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
            decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
            decimalFormat.setMaximumFractionDigits(0);

            f2ProductInputDetailPart.setVisibility(View.VISIBLE);
            f2IdProduct = id_product;
            f2ProductName = nama_product;
            f2ProductHargaSatuan = harga_satuan;
            f2QtyProductPicker.setMin(1);
            f2QtyProductPicker.setValue(1);
            f2QtyProduct = String.valueOf(f2QtyProductPicker.getValue());
            f2SubTotal = String.valueOf(f2QtyProductPicker.getValue() * Integer.parseInt(harga_satuan));

            f2ProductChoiceTV.setText(nama_product);
            f2ProductHargaSatuanTV.setText(decimalFormat.format(Integer.parseInt(harga_satuan)));
            f2SubTotalTV.setText(decimalFormat.format(Integer.parseInt(String.valueOf(f2QtyProductPicker.getValue() * Integer.parseInt(harga_satuan)))));

            InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = ReportSumaActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(ReportSumaActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            f2KeteranganKunjunganED.clearFocus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };


    public BroadcastReceiver productDeleteBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String index = intent.getStringExtra("index");

            new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Perhatian")
                    .setContentText("Yakin untuk menghapus produk pesanan?")
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
                            dataProduct.remove(Integer.parseInt(index));

                            adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                            f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                            f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                            f2ListProductInputRV.setHasFixedSize(true);
                            f2ListProductInputRV.setNestedScrollingEnabled(false);
                            f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                            f2HitungTotalPesanan();
                        }
                    })
                    .show();
        }
    };

    public BroadcastReceiver removePelanggan = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("nama_pelanggan");

            new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Perhatian")
                    .setContentText("Yakin untuk menghapus pelanggan?")
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
                            String valueToRemove = key;
                            JSONArray modifiedArray = removeElementsByValue(f1JsonArrayPelanggan, valueToRemove);
                            f1JsonArrayPelanggan = modifiedArray;
                            updateListPelanggan(f1JsonArrayPelanggan.toString());
                        }
                    })
                    .show();
        }
    };

    private JSONArray removeElementsByValue(JSONArray jsonArray, String value) {
        JSONArray modifiedArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject currentObject = jsonArray.getJSONObject(i);
                String namaPelanggan = currentObject.getString("namaPelanggan");
                if (!namaPelanggan.equals(value)) {
                    modifiedArray.put(currentObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return modifiedArray;
    }

    private void f2HitungTotalPesanan() {
        Locale localeID = new Locale("id", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
        decimalFormat.setMaximumFractionDigits(0);

        String[] array = new String[dataProduct.size()];
        array = dataProduct.toArray(array);

        int jumlah = 0;

        for (int i = 0; i < array.length; i++) {
            String[] arrayData = array[i].split("/");
            jumlah += Integer.parseInt(arrayData[4]);
        }
        totalLaporan = jumlah;
        f2TotalPesananTV.setText(decimalFormat.format(jumlah));

    }

    @SuppressLint("MissingPermission")
    private void salesPosition() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    Log.e("TAG", "GPS is on" + String.valueOf(location));
                    salesLat = String.valueOf(location.getLatitude());
                    salesLong = String.valueOf(location.getLongitude());

                    Location getLoc = new Location("dummyProvider");
                    getLoc.setLatitude(location.getLatitude());
                    getLoc.setLongitude(location.getLongitude());
                    new ReverseGeocodingTask().execute(getLoc);
                }  else {
                    gpsEnableAction();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error trying to get last GPS location");
                e.printStackTrace();
                gpsEnableAction();
            }
        });

    }

    private void gpsEnableAction(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(ReportSumaActivity.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        ReportSumaActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... params) {
            Location location = params[0];
            String addressText = "";

            Geocoder geocoder = new Geocoder(ReportSumaActivity.this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressBuilder.append(address.getAddressLine(i)).append(", ");
                    }
                    addressText = addressBuilder.toString();
                }
            } catch (IOException e) {
                Log.e(ContentValues.TAG, "Error fetching address: " + e.getMessage());
            }

            return addressText;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (!address.isEmpty()) {
                Log.d(ContentValues.TAG, "Alamat: " + address);
                f2DetailLocationTV.setText(address.substring(0,address.length()-2));
                f3DetailLocationTV.setText(address.substring(0,address.length()-2));
            } else {
                Log.e(ContentValues.TAG, "Alamat tidak ditemukan");
                f2DetailLocationTV.setText("Alamat tidak ditemukan");
                f3DetailLocationTV.setText("Alamat tidak ditemukan");
            }
        }
    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(ReportSumaActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withActivity(ReportSumaActivity.this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void showImagePickerOptions() {
        String code = "";
        if(categoryReport.equals("1") || categoryReport.equals("2")){
            code = "suma_report";
        } else if(categoryReport.equals("3")){
            code = "suma_report_penagihan";
        }

        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, code);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(ReportSumaActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ReportSumaActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 600);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 900);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ReportSumaActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 4); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        String file_directori = getRealPathFromURIPath(uri, ReportSumaActivity.this);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        @SuppressLint("Recycle")
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                try {
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")||extension.equals(".png")||extension.equals(".PNG")){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, ReportSumaActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);

                        if(categoryReport.equals("2")){
                            f2ViewLampiranBTN.setVisibility(View.VISIBLE);
                            f2LabelLampiranTV.setText("Ubah Lampiran");
                            f2ViewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ReportSumaActivity.this, ViewImageActivity.class);
                                    intent.putExtra("url", String.valueOf(uri));
                                    intent.putExtra("kode", "form");
                                    intent.putExtra("jenis_form", "suma");
                                    startActivity(intent);
                                }
                            });
                        } else if(categoryReport.equals("3")) {
                            f3ViewLampiranBTN.setVisibility(View.VISIBLE);
                            f3LabelLampiranTV.setText("Ubah Lampiran");
                            f3ViewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ReportSumaActivity.this, ViewImageActivity.class);
                                    intent.putExtra("url", String.valueOf(uri));
                                    intent.putExtra("kode", "form");
                                    intent.putExtra("jenis_form", "suma");
                                    startActivity(intent);
                                }
                            });
                        }
                        uriToBase64(uri);

                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Format file tidak sesuai!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }, 800);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String listToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list) {
            stringBuilder.append(item).append(", ");
        }
        // Remove the trailing comma and space
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }

    private void submitLaporan(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/create_suma_report";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")) {
                                String idLaporan = data.getString("idLaporan");

                                if(categoryReport.equals("1")){
                                    laporanTerkirim = "1";
                                    successPart.setVisibility(View.VISIBLE);
                                    formPart.setVisibility(View.GONE);
                                    pDialog.dismiss();

                                    viewPermohonanTV.setText("OK");
                                    viewPermohonanBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                           onBackPressed();
                                        }
                                    });
                                } else if(categoryReport.equals("2")){
                                    String filename = data.getString("file_name");
                                    uploadLampiran(filename, idLaporan);
                                } else if(categoryReport.equals("3")){
                                    String filename = data.getString("file_name");
                                    uploadLampiran(filename, idLaporan);
                                }

                            } else {

                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                Log.d("Error Mang: ", response.toString());

                                pDialog.setTitleText("Gagal Terkirim")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                        successPart.setVisibility(View.GONE);
                        formPart.setVisibility(View.VISIBLE);
                        pDialog.setTitleText("Gagal Terkirim")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.ERROR_TYPE);
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                if(categoryReport.equals("1")){
                    params.put("kategori_laporan", categoryReport);
                    params.put("tanggal_rencana", f1DateChoice);
                    params.put("list_pelanggan", f1JsonArrayPelanggan.toString());
                    params.put("nik_sales", sharedPrefManager.getSpNik());
                    params.put("created_at", getTimeStamp());
                } else if(categoryReport.equals("2")){
                    params.put("kategori_laporan", categoryReport);
                    params.put("keterangan_kunjungan", f2KeteranganKunjunganED.getText().toString());
                    params.put("tipe_pelanggan", f2JenisPelanggan);

                    if(f2JenisPelanggan.equals("1")){
                        params.put("nama_pelanggan", f2NamaPelangganBaruED.getText().toString());
                        params.put("alamat_pelanggan", f2AlamatPelangganBaruED.getText().toString());
                        params.put("pic_pelanggan", f2PicPelangganBaruED.getText().toString());
                        params.put("telepon_pelanggan", f2TeleponPelangganBaruED.getText().toString());
                    } else if(f2JenisPelanggan.equals("2")){
                        params.put("id_pelanggan", f2IdPelangganLama);
                    }

                    params.put("nik_sales", sharedPrefManager.getSpNik());
                    params.put("latitude", salesLat);
                    params.put("longitude", salesLong);
                    params.put("created_at", getTimeStamp());
                    params.put("total_laporan", String.valueOf(totalLaporan));

                    params.put("data_produk", listToString(dataProduct));
                } else if(categoryReport.equals("3")){
                    params.put("kategori_laporan", categoryReport);
                    params.put("id_pelanggan", f3IdPelangganLama);
                    params.put("nik_sales", sharedPrefManager.getSpNik());
                    params.put("keterangan", f3KeteranganED.getText().toString());
                    params.put("latitude", salesLat);
                    params.put("longitude", salesLong);
                    params.put("total_laporan", String.valueOf(totalLaporan));
                    params.put("created_at", getTimeStamp());
                }
                Log.d("Params Cek", params.toString());
                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    @SuppressLint("SetTextI18n")
    public void uploadLampiran(String filename, String idReport) {
        String path = FilePathimage.getPath(this, uri);
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/upload_lampiran";

        if (path == null) {
            Toast.makeText(this, "Please move your image file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            String uploadId = UUID.randomUUID().toString();
            try {
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "image")
                        .addParameter("filename", filename)
                        .setMaxRetries(2)
                        .startUpload();

                laporanTerkirim = "1";
                successPart.setVisibility(View.VISIBLE);
                formPart.setVisibility(View.GONE);
                pDialog.dismiss();

                viewPermohonanTV.setText("LIHAT DETAIL LAPORAN");
                viewPermohonanBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReportSumaActivity.this, DetailReportSumaActivity.class);
                        intent.putExtra("report_id",idReport);
                        startActivity(intent);
                    }
                });
            } catch (Exception exc) {
                Log.e("UploadError", "Error uploading file", exc);
            }

        }
    }

    private String getTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void connectionFailed() {
        CookieBar.build(ReportSumaActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void uriToBase64(Uri uri){
        try {
            byte[] content = new byte[0];
            String base64String = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                content = Files.readAllBytes(Paths.get(URI.create(uri.toString())));
                base64String = Base64.getEncoder().encodeToString(content);
                Log.d("Base64 : ", base64String);
                fullBase64String = base64String;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateListPelanggan(String jsonStringPlanggan){
        f1PelangganRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
        f1PelangganRV.setHasFixedSize(true);
        f1PelangganRV.setNestedScrollingEnabled(false);
        f1PelangganRV.setItemAnimator(new DefaultItemAnimator());

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        pelangganLists = gson.fromJson(jsonStringPlanggan, PelangganList[].class);
        adapterPelangganList = new AdapterPelangganList(pelangganLists, ReportSumaActivity.this);
        f1PelangganRV.setAdapter(adapterPelangganList);
    }

    public void onBackPressed() {
        if (!categoryReport.equals("") || !f2IdPelangganLama.equals("") || f2PelangganOption.isActivated()) {
            if (bottomSheet.isSheetShowing()) {
                bottomSheet.dismissSheet();
            } else {
                if (laporanTerkirim.equals("1")) {
                    super.onBackPressed();
                } else {
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    categoryReport = "";
                                    f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);
                                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, "");
                                    f2IdPelangganLama = "";
                                    reportKategoriChoiceTV.setText("");
                                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                                    f2AlamatPelangganLamaTV.setText("");
                                    f2PicPelangganLamaTV.setText("");
                                    f2TeleponPelangganLamaTV.setText("");
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        } else {
            if (bottomSheet.isSheetShowing()) {
                bottomSheet.dismissSheet();
            } else {
                super.onBackPressed();
            }
        }
    }

    private JSONArray clearJSONArray(JSONArray jsonArray) {
        while (jsonArray.length() > 0) {
            jsonArray.remove(0);
        }
        return jsonArray;
    }

}