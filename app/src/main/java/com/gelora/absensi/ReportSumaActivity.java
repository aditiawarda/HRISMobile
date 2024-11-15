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
import android.graphics.BitmapFactory;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.gelora.absensi.adapter.AdapterNoSuratJalan;
import com.gelora.absensi.adapter.AdapterPelangganLama;
import com.gelora.absensi.adapter.AdapterPelangganList;
import com.gelora.absensi.adapter.AdapterProductInputSuma;
import com.gelora.absensi.adapter.AdapterProductSuma;
import com.gelora.absensi.adapter.AdapterTokoPelanggan;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataInvoicePiutang;
import com.gelora.absensi.model.DataNoSuratJalan;
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
import com.hmdevcoders.awesometoast.AwesomeToast;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;;
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

    LinearLayout loadingFormPart, attantionNoForm, rencanaKunjunganFormPart, aktivitasKunjunganFormPart, actionBar, backBTN, reportCategoryBTN;
    LinearLayout markBaru, baruBTN, aktivitasBTN, markAktivitas, viewPermohonanBTN, formPart, successPart, loadingDataPart, loadingDataPartProduk, noDataPart, noDataPartProduk, startAttantionPart, startAttantionTokoPart, noDataTokoPart, loadingDataTokoPart, startAttantionPartProduk, rencanaBTN, markRencana;

    EditText keywordTokoED, f1KeteranganKunjunganED, f1NamaPelangganBaruED, f1AlamatPelangganBaruED;
    LinearLayout f1TokoChoiceBTN, f1TokoBTN, f1AgendaOptionPart, f1AddPelangganBTN, f1AddPelangganPart, f1ChoiceDateBTN, f1DetailPelanggan, f1NamaPelangganLamaBTN, f1SubmitPesananBTN, f1PelangganAttantionPart, f1PelangganBaruPart, f1PelangganLamaPart;
    RadioGroup f1PelangganOption;
    RadioButton f1PelangganOptionBaru, f1PelangganOptionLama;
    String devModeStatus = "", f1DateChoice = "", f1JenisPelanggan = "", f1IdPelangganLama = "";
    TextView titleMessageTV, f1TokoChoiceTV, f1ChoiceDateTV, f1NamaPelangganLamaChoiceTV, f1LabelLampiranTV, f1TotalPesananTV, f1SubTotalTV, f1AlamatPelangganLamaTV;
    RecyclerView f1PelangganRV, tokoRV;
    JSONArray f1JsonArrayPelanggan = new JSONArray();
    private PelangganList[] pelangganLists;
    private AdapterPelangganList adapterPelangganList;
    CheckBox f1CB1, f1CB2, f1CB3, f1CB4, f1CB5, f1CB6;

    EditText f2KeteranganKunjunganED, keywordED, keywordEDProduk, f2NamaPelangganBaruED, f2AlamatPelangganBaruED;
    LinearLayout f2TokoChoiceBTN, f2TokoBTN, f2LoadingDataPartSj, f2NoDataPartSj, f2NoSuratJalanBTN, f2PengirimanFormPart, f2NoDataInv, f2LoadingDataInv, f2PenagihanPart, f2SubmitPesananBTN, f2GPSLocationBTN, f2ViewLampiranBTN, f2LampiranFotoBTN, f2ProductInputDetailPart, f2AddProductBTN, f2ProductChoiceBTN, f2AgendaOptionPart, f2PromosiPart, f2DetailPelanggan, f2NamaPelangganLamaBTN, f2PelangganAttantionPart, f2PelangganBaruPart, f2PelangganLamaPart;
    RadioGroup f2PelangganOption;
    RadioButton f2PelangganOptionBaru, f2PelangganOptionLama;
    TextView f2TokoChoiceTV, f2NoSuratJalanChoiceTV, f2TotalTagihanInvTV, f2CountImageTV, f2DetailLocationTV, f2LabelLampiranTV, f2TotalPesananTV, f2SubTotalTV, f2ProductHargaSatuanTV, f2ProductChoiceTV, f2NamaPelangganLamaChoiceTV, f2AlamatPelangganLamaTV;
    NumberPicker f2QtyProductPicker;
    RecyclerView pelangganRV, produkRV, f2ListProductInputRV;
    private PelangganLama[] pelangganLamas;
    private ProductSuma[] productSumas;
    private AdapterTokoPelanggan adapterTokoPelanggan;
    private AdapterPelangganLama adapterPelangganLama;
    private AdapterProductSuma adapterProductSuma;
    String statuspelangganBaru = "0", f2IdPelangganLama = "", f2JenisPelanggan = "", f2FullDataProduct = "", f2QtyProduct = "", f2IdProduct = "", f2ProductName = "", f2ProductHargaSatuan = "", f2SubTotal = "";
    CheckBox f2CB1, f2CB2, f2CB3, f2CB4, f2CB5, f2CB6;
    int f2TotalInvTagihan = 0;
    RecyclerView f2InvRV;
    private DataInvoicePiutang[] dataInvoicePiutangs;
    private AdapterInvoicePiutang adapterInvoicePiutang;
    RecyclerView f2NoSjRV;
    private DataNoSuratJalan[] dataNoSuratJalans;
    private AdapterNoSuratJalan adapterNoSuratJalan;

    TextView messageTV, titlePageTV, viewPermohonanTV, reportKategoriChoiceTV, namaKaryawanTV, nikKaryawanTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    BottomSheetLayout bottomSheet;
    ImageView successGif;
    String devModCheck = "", salesLat = "", salesLong = "", categoryReport = "", laporanTerkirim = "", fullBase64String = "";

    int f2TotalPesanan = 0;
    private List<String> dataProduct = new ArrayList<>();
    private AdapterProductInputSuma adapterProductInputSuma;
    private static final int INITIAL_REQUEST = 1337;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private List<String> lampiranImage = new ArrayList<>();
    private List<String> extentionImage = new ArrayList<>();
    KAlertDialog pDialog;
    private int i = -1;
    private Handler handler = new Handler();

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
        rencanaKunjunganFormPart = findViewById(R.id.rencana_kunjungan_form_part);
        aktivitasKunjunganFormPart = findViewById(R.id.aktivitas_kunjungan_form_part);
        attantionNoForm = findViewById(R.id.attantion_no_form);
        loadingFormPart = findViewById(R.id.loading_form_part);
        namaKaryawanTV = findViewById(R.id.nama_karyawan_tv);
        nikKaryawanTV = findViewById(R.id.nik_karyawan_tv);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);
        viewPermohonanBTN = findViewById(R.id.view_permohonan_btn);
        viewPermohonanTV = findViewById(R.id.view_permohonan_tv);
        titlePageTV = findViewById(R.id.title_page_tv);
        messageTV = findViewById(R.id.message_tv);
        titleMessageTV = findViewById(R.id.title_message_tv);

        f1KeteranganKunjunganED = findViewById(R.id.f1_keterangan_kunjungan_ed);
        f1ChoiceDateBTN = findViewById(R.id.f1_choice_date_btn);
        f1ChoiceDateTV = findViewById(R.id.f1_choice_date_tv);
        f1TokoBTN = findViewById(R.id.f1_toko_btn);
        f1TokoChoiceBTN = findViewById(R.id.f1_toko_choice_btn);
        f1TokoChoiceTV = findViewById(R.id.f1_toko_choice_tv);
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
        f1SubmitPesananBTN = findViewById(R.id.f1_submit_data_btn);
        f1NamaPelangganBaruED = findViewById(R.id.f1_nama_pelanggan_baru_ed);
        f1AlamatPelangganBaruED = findViewById(R.id.f1_alamat_pelanggan_baru_ed);
        f1AddPelangganPart = findViewById(R.id.f1_add_pelanggan_part);
        f1AddPelangganBTN = findViewById(R.id.f1_add_pelanggan_btn);
        f1PelangganRV = findViewById(R.id.item_pelanggan_rv);
        f1AgendaOptionPart = findViewById(R.id.f1_agenda_option);
        f1CB1 = findViewById(R.id.f1_cb_1);
        f1CB2 = findViewById(R.id.f1_cb_2);
        f1CB3 = findViewById(R.id.f1_cb_3);
        f1CB4 = findViewById(R.id.f1_cb_4);
        f1CB5 = findViewById(R.id.f1_cb_5);
        f1CB6 = findViewById(R.id.f1_cb_6);

        f2TokoBTN = findViewById(R.id.f2_toko_btn);
        f2TokoChoiceBTN = findViewById(R.id.f2_toko_choice_btn);
        f2TokoChoiceTV = findViewById(R.id.f2_toko_choice_tv);
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
        f2AddProductBTN = findViewById(R.id.f2_add_product_btn);
        f2AgendaOptionPart = findViewById(R.id.f2_agenda_option_part);
        f2PromosiPart = findViewById(R.id.f2_promosi_form_part);
        f2PenagihanPart = findViewById(R.id.f2_penagihan_form_part);
        f2PengirimanFormPart = findViewById(R.id.f2_pengiriman_form_part);
        f2LoadingDataInv = findViewById(R.id.f2_loading_data_inv);
        f2NoDataInv = findViewById(R.id.f2_no_data_inv);
        f2InvRV = findViewById(R.id.f2_inv_rv);
        f2NoSuratJalanBTN = findViewById(R.id.f2_no_surat_jalan_btn);
        f2NoSuratJalanChoiceTV = findViewById(R.id.f2_no_surat_jalan_choice_tv);
        f2TotalTagihanInvTV = findViewById(R.id.f2_total_tagihan_inv_tv);
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
        f2DetailLocationTV = findViewById(R.id.f2_detail_location_tv);
        f2CountImageTV = findViewById(R.id.f2_count_image_tv);
        f2CB1 = findViewById(R.id.f2_cb_1);
        f2CB2 = findViewById(R.id.f2_cb_2);
        f2CB3 = findViewById(R.id.f2_cb_3);
        f2CB4 = findViewById(R.id.f2_cb_4);
        f2CB5 = findViewById(R.id.f2_cb_5);
        f2CB6 = findViewById(R.id.f2_cb_6);

        adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
        f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(this));
        f2ListProductInputRV.setAdapter(adapterProductInputSuma);
        f2ListProductInputRV.setHasFixedSize(true);
        f2ListProductInputRV.setNestedScrollingEnabled(false);
        f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

        salesPosition();
        devModeCheck();

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        namaKaryawanTV.setText(sharedPrefManager.getSpNama());
        nikKaryawanTV.setText(sharedPrefManager.getSpNik());

        LocalBroadcastManager.getInstance(this).registerReceiver(pelangganTokoBroad, new IntentFilter("pelanggan_toko_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(pelangganLamaBroad, new IntentFilter("pelanggan_lama_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(productSumaBroad, new IntentFilter("product_suma_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(productDeleteBroad, new IntentFilter("product_delete_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(removePelanggan, new IntentFilter("remove_pelanggan_list"));
        LocalBroadcastManager.getInstance(this).registerReceiver(noSuratJalanBroad, new IntentFilter("list_no_sj"));

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
                f1KeteranganKunjunganED.setText("");
                f1PelangganOption.clearCheck();
                f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                f1PelangganBaruPart.setVisibility(View.GONE);
                f1PelangganLamaPart.setVisibility(View.GONE);
                f1DateChoice = "";
                f1ChoiceDateTV.setText("");
                f1JenisPelanggan = "";
                f1NamaPelangganLamaChoiceTV.setText("");
                f1TokoChoiceTV.setText("");
                f2TokoChoiceTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                f1DetailPelanggan.setVisibility(View.GONE);
                f1AlamatPelangganLamaTV.setText("");
                f1NamaPelangganBaruED.setText("");
                f1AlamatPelangganBaruED.setText("");
                f1AddPelangganPart.setVisibility(View.GONE);
                f1AgendaOptionPart.setVisibility(View.GONE);
                f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);
                f1CB1.setChecked(false);
                f1CB2.setChecked(false);
                f1CB3.setChecked(false);
                f1CB4.setChecked(false);
                f1CB5.setChecked(false);
                f1CB6.setChecked(false);

                f2KeteranganKunjunganED.setText("");
                f2PelangganOption.clearCheck();
                f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                f2PelangganBaruPart.setVisibility(View.GONE);
                f2PelangganLamaPart.setVisibility(View.GONE);
                f2NamaPelangganLamaChoiceTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                f2DetailPelanggan.setVisibility(View.GONE);
                f2AlamatPelangganLamaTV.setText("");
                f2AgendaOptionPart.setVisibility(View.GONE);
                f2PromosiPart.setVisibility(View.GONE);
                dataProduct.clear();
                f2JenisPelanggan = "";
                f2IdPelangganLama = "";
                f2CB1.setChecked(false);
                f2CB2.setChecked(false);
                f2CB3.setChecked(false);
                f2CB4.setChecked(false);
                f2CB5.setChecked(false);
                f2CB6.setChecked(false);
                f2PromosiPart.setVisibility(View.GONE);
                f2PenagihanPart.setVisibility(View.GONE);
                f2PengirimanFormPart.setVisibility(View.GONE);
                f2TotalPesanan = 0;
                f2TotalPesananTV.setText("Rp 0");
                f2TotalInvTagihan = 0;
                f2TotalTagihanInvTV.setText("Rp 0");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                f2NoSuratJalanChoiceTV.setText("");

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
                f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                f2ViewLampiranBTN.setVisibility(View.GONE);
                f2JenisPelanggan = "";
                f2IdPelangganLama = "";
                f2NamaPelangganBaruED.setText("");
                f2AlamatPelangganBaruED.setText("");

                uri = null;
                lampiranImage.clear();
                extentionImage.clear();
                fullBase64String = "";
                f2TotalPesanan = 0;

                salesPosition();
                devModeCheck();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
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

        f1TokoChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1TokoPicker();
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
                    f1AddPelangganPart.setVisibility(View.VISIBLE);
                    f1AgendaOptionPart.setVisibility(View.VISIBLE);
                    f1CB1.setChecked(false);
                    f1CB2.setChecked(false);
                    f1CB3.setChecked(false);
                    f1CB4.setChecked(false);
                    f1CB5.setChecked(false);
                    f1CB6.setChecked(false);
                } else if (f1PelangganOptionLama.isChecked()) {
                    f1JenisPelanggan = "2";
                    f1PelangganBaruPart.setVisibility(View.GONE);
                    f1PelangganLamaPart.setVisibility(View.VISIBLE);

                    f1NamaPelangganBaruED.setText("");
                    f1AlamatPelangganBaruED.setText("");
                    f1AddPelangganPart.setVisibility(View.GONE);
                    f1AgendaOptionPart.setVisibility(View.VISIBLE);
                    f1CB1.setChecked(false);
                    f1CB2.setChecked(false);
                    f1CB3.setChecked(false);
                    f1CB4.setChecked(false);
                    f1CB5.setChecked(false);
                    f1CB6.setChecked(false);
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
                String arrayAgenda;
                if(f1CB1.isChecked()){
                    if(f1CB2.isChecked()){
                        if(f1CB3.isChecked()){
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\"]";
                                    }
                                }
                            }
                        }
                    } else {
                        if(f1CB3.isChecked()){
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\"]";
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if(f1CB2.isChecked()){
                        if(f1CB3.isChecked()){
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\"]";
                                    }
                                }
                            }
                        }
                    } else {
                        if(f1CB3.isChecked()){
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f1CB4.isChecked()){
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"5\"]";
                                    }
                                }
                            } else {
                                if(f1CB5.isChecked()){
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"6\"]";
                                    }
                                } else {
                                    if(f1CB6.isChecked()){
                                        arrayAgenda = "[\"7\"]";
                                    } else {
                                        arrayAgenda = "[]";
                                    }
                                }
                            }
                        }
                    }
                }

                if (f1JenisPelanggan.equals("1")) {
                    if(!f1NamaPelangganBaruED.getText().toString().equals("") && !f1KeteranganKunjunganED.getText().toString().equals("") && !arrayAgenda.equals("[]")){
                        JSONObject dataPelangganBaru = new JSONObject();
                        try {
                            AwesomeToast.makeText(getApplicationContext(), "Data berhasil ditambahkan", AwesomeToast.LENGTH_LONG, AwesomeToast.SUCCESS);
                            statuspelangganBaru = "0";
                            f1TokoChoiceTV.setText("");
                            f2TokoChoiceTV.setText("");

                            dataPelangganBaru.put("idPelanggan", "null");
                            dataPelangganBaru.put("kategoriPelanggan", f1JenisPelanggan);
                            dataPelangganBaru.put("namaPelanggan", f1NamaPelangganBaruED.getText().toString());
                            dataPelangganBaru.put("alamatPelanggan", f1AlamatPelangganBaruED.getText().toString());
                            dataPelangganBaru.put("keteranganKunjunganPelanggan", f1KeteranganKunjunganED.getText().toString());
                            dataPelangganBaru.put("tipeRencanaLaporan", arrayAgenda);
                            f1JsonArrayPelanggan.put(dataPelangganBaru);

                            f1PelangganOption.clearCheck();
                            f1JenisPelanggan = "";
                            f1NamaPelangganBaruED.setText("");
                            f1AlamatPelangganBaruED.setText("");
                            f1KeteranganKunjunganED.setText("");
                            f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                            f1PelangganLamaPart.setVisibility(View.GONE);
                            f1PelangganBaruPart.setVisibility(View.GONE);
                            f1AddPelangganPart.setVisibility(View.GONE);
                            f1AgendaOptionPart.setVisibility(View.GONE);

                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                            f1NamaPelangganLamaChoiceTV.setText("");
                            f1DetailPelanggan.setVisibility(View.GONE);
                            f1AlamatPelangganLamaTV.setText("");

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
                }
                else if (f1JenisPelanggan.equals("2")) {
                    if(!f1IdPelangganLama.equals("") && !f1KeteranganKunjunganED.getText().toString().equals("") && !arrayAgenda.equals("[]")){
                        JSONObject dataPelangganLama = new JSONObject();
                        try {
                            AwesomeToast.makeText(getApplicationContext(), "Data berhasil ditambahkan", AwesomeToast.LENGTH_LONG, AwesomeToast.SUCCESS);
                            statuspelangganBaru = "0";
                            f1TokoChoiceTV.setText("");
                            f2TokoChoiceTV.setText("");

                            dataPelangganLama.put("idPelanggan", f1IdPelangganLama);
                            dataPelangganLama.put("kategoriPelanggan", f1JenisPelanggan);
                            dataPelangganLama.put("namaPelanggan", f1NamaPelangganLamaChoiceTV.getText().toString());
                            dataPelangganLama.put("alamatPelanggan", f1AlamatPelangganLamaTV.getText().toString());
                            dataPelangganLama.put("keteranganKunjunganPelanggan", f1KeteranganKunjunganED.getText().toString());
                            dataPelangganLama.put("tipeRencanaLaporan", arrayAgenda);
                            f1JsonArrayPelanggan.put(dataPelangganLama);

                            f1PelangganOption.clearCheck();
                            f1JenisPelanggan = "";

                            f1NamaPelangganBaruED.setText("");
                            f1AlamatPelangganBaruED.setText("");
                            f1KeteranganKunjunganED.setText("");
                            f1PelangganAttantionPart.setVisibility(View.VISIBLE);
                            f1PelangganLamaPart.setVisibility(View.GONE);
                            f1PelangganBaruPart.setVisibility(View.GONE);
                            f1AddPelangganPart.setVisibility(View.GONE);
                            f1AgendaOptionPart.setVisibility(View.GONE);

                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                            f1NamaPelangganLamaChoiceTV.setText("");
                            f1DetailPelanggan.setVisibility(View.GONE);
                            f1AlamatPelangganLamaTV.setText("");

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
                    f2TotalPesananTV.setText("Rp 0");
                    f2TotalTagihanInvTV.setText("Rp 0");
                    f2TotalInvTagihan = 0;
                    f2AgendaOptionPart.setVisibility(View.VISIBLE);
                    dataProduct.clear();
                    f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                    f2ViewLampiranBTN.setVisibility(View.GONE);

                    adapterProductInputSuma = new AdapterProductInputSuma(dataProduct);
                    f2ListProductInputRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                    f2ListProductInputRV.setAdapter(adapterProductInputSuma);
                    f2ListProductInputRV.setHasFixedSize(true);
                    f2ListProductInputRV.setNestedScrollingEnabled(false);
                    f2ListProductInputRV.setItemAnimator(new DefaultItemAnimator());

                    if(f2CB2.isChecked()){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                f2LoadingDataInv.setVisibility(View.VISIBLE);
                                f2InvRV.setVisibility(View.GONE);
                                f2NoDataInv.setVisibility(View.GONE);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(f2IdPelangganLama.equals("")){
                                            getDataInvoice("-");
                                        } else {
                                            getDataInvoice(f2IdPelangganLama);
                                        }
                                    }
                                }, 100);
                            }
                        }, 300);
                    }
                } else if (f2PelangganOptionLama.isChecked()) {
                    f2JenisPelanggan = "2";
                    f2PelangganBaruPart.setVisibility(View.GONE);
                    f2PelangganLamaPart.setVisibility(View.VISIBLE);
                    f2TotalPesananTV.setText("Rp 0");
                    f2TotalPesanan = 0;
                    if(f2CB1.isChecked()||f2CB2.isChecked()||f2CB3.isChecked()||f2CB4.isChecked()||f2CB5.isChecked()||f2CB6.isChecked()){
                        f2AgendaOptionPart.setVisibility(View.VISIBLE);
                    } else {
                        f2AgendaOptionPart.setVisibility(View.GONE);
                    }
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
                }
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                f2NoSuratJalanChoiceTV.setText("");
            }
        });

        f2TokoChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2TokoPicker();
            }
        });

        f2NamaPelangganLamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2PelangganLamaBottomSheet();
            }
        });

        f2CB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    f2PromosiPart.setVisibility(View.VISIBLE);
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport+"-2");
                } else {
                    f2PromosiPart.setVisibility(View.GONE);
                }
            }
        });

        f2CB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    f2PenagihanPart.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheet.dismissSheet();
                            f2LoadingDataInv.setVisibility(View.VISIBLE);
                            f2InvRV.setVisibility(View.GONE);
                            f2NoDataInv.setVisibility(View.GONE);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(f2IdPelangganLama.equals("")){
                                        getDataInvoice("-");
                                    } else {
                                        getDataInvoice(f2IdPelangganLama);
                                    }
                                }
                            }, 100);
                        }
                    }, 300);
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport+"-3");
                } else {
                    f2PenagihanPart.setVisibility(View.GONE);
                    f2TotalTagihanInvTV.setText("Rp 0");
                    f2TotalInvTagihan = 0;
                }
            }
        });

        f2CB3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    f2PengirimanFormPart.setVisibility(View.VISIBLE);
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport+"-4");
                } else {
                    f2PengirimanFormPart.setVisibility(View.GONE);
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                    f2NoSuratJalanChoiceTV.setText("");
                }
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

        f2NoSuratJalanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f2IdPelangganLama.equals("")){
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih pelanggan terlebih dahulu!")
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
                    f2SuratJalanPicker();
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

        f1SubmitPesananBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!categoryReport.equals("") && !f1DateChoice.equals("") && f1JsonArrayPelanggan.length() > 0){
                    new KAlertDialog(ReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Kirim laporan sekarang?")
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
                                            if (isDeveloperModeEnabled() && devModeStatus.equals("on")){
                                                pDialog.dismiss();
                                                new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                        .setCancelText(" TUTUP ")
                                                        .setConfirmText("SETTING")
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
                                                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                            }
                                                        })
                                                        .show();

                                            }
                                            else {
                                                submitLaporan();
                                            }
                                        }
                                    }.start();

                                }
                            })
                            .show();
                }
                else {
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
                String arrayAgenda;
                if(f2CB1.isChecked()){
                    if(f2CB2.isChecked()){
                        if(f2CB3.isChecked()){
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"3\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"3\"]";
                                    }
                                }
                            }
                        }
                    } else {
                        if(f2CB3.isChecked()){
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"2\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"2\"]";
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if(f2CB2.isChecked()){
                        if(f2CB3.isChecked()){
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"3\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"3\"]";
                                    }
                                }
                            }
                        }
                    } else {
                        if(f2CB3.isChecked()){
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\",\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\",\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"4\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"4\"]";
                                    }
                                }
                            }
                        } else {
                            if(f2CB4.isChecked()){
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"5\",\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"5\",\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"5\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"5\"]";
                                    }
                                }
                            } else {
                                if(f2CB5.isChecked()){
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"6\",\"7\"]";
                                    } else {
                                        arrayAgenda = "[\"6\"]";
                                    }
                                } else {
                                    if(f2CB6.isChecked()){
                                        arrayAgenda = "[\"7\"]";
                                    } else {
                                        arrayAgenda = "[]";
                                    }
                                }
                            }
                        }
                    }
                }

                f2KeteranganKunjunganED.clearFocus();
                if(!categoryReport.equals("") && !f2KeteranganKunjunganED.getText().toString().equals("") && !f2JenisPelanggan.equals("") && !arrayAgenda.equals("[]")){
                    if(f2JenisPelanggan.equals("1")){
                        if(!f2NamaPelangganBaruED.getText().toString().equals("")){
                            if((salesLat.equals("0")&&salesLong.equals("0"))||salesLat.equals("")||salesLong.equals("")||lampiranImage.size()==0){
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
                                        .setContentText("Kirim laporan sekarang?")
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
                                                        if (isDeveloperModeEnabled() && devModeStatus.equals("on")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                        }
                                                        else {
                                                            submitLaporan();
                                                        }
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
                            if((salesLat.equals("0")&&salesLong.equals("0"))||salesLat.equals("")||salesLong.equals("")||lampiranImage.size()==0){
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
                                        .setContentText("Kirim laporan sekarang?")
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
                                                        if (isDeveloperModeEnabled() && devModeStatus.equals("on")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                        }
                                                        else {
                                                            submitLaporan();
                                                            Log.d("Data Customer : ", f1JsonArrayPelanggan.toString());
                                                        }
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

        categoryReport = sharedPrefAbsen.getSpReportCategoryActive();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
        f2NoSuratJalanChoiceTV.setText("");
        loadingFormPart.setVisibility(View.VISIBLE);
        attantionNoForm.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCatecoryForm();
            }
        }, 800);

    }

    @SuppressLint("SetTextI18n")
    private void getCatecoryForm(){
        if (categoryReport.equals("1")) {
            categoryReport = "1";
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
            titlePageTV.setText("FORM RENCANA KUNJUNGAN SALES");
            reportKategoriChoiceTV.setText("Rencana Kunjungan");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();

                    aktivitasKunjunganFormPart.setVisibility(View.GONE);
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
                    f1TokoChoiceTV.setText("");
                    f2TokoChoiceTV.setText("");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                    f1DetailPelanggan.setVisibility(View.GONE);
                    f1AlamatPelangganLamaTV.setText("");
                    f1AddPelangganPart.setVisibility(View.GONE);
                    f1AgendaOptionPart.setVisibility(View.GONE);
                    f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);
                    f1CB1.setChecked(false);
                    f1CB2.setChecked(false);
                    f1CB3.setChecked(false);
                    f1CB4.setChecked(false);
                    f1CB5.setChecked(false);
                    f1CB6.setChecked(false);

                    f2KeteranganKunjunganED.setText("");
                    f2PelangganOption.clearCheck();
                    f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                    f2PelangganBaruPart.setVisibility(View.GONE);
                    f2PelangganLamaPart.setVisibility(View.GONE);
                    f2NamaPelangganLamaChoiceTV.setText("");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                    f2DetailPelanggan.setVisibility(View.GONE);
                    f2AlamatPelangganLamaTV.setText("");
                    f2AgendaOptionPart.setVisibility(View.GONE);
                    f2PromosiPart.setVisibility(View.GONE);
                    dataProduct.clear();
                    f2JenisPelanggan = "";
                    f2IdPelangganLama = "";
                    f2CB1.setChecked(false);
                    f2CB2.setChecked(false);
                    f2CB3.setChecked(false);
                    f2CB4.setChecked(false);
                    f2CB5.setChecked(false);
                    f2CB6.setChecked(false);
                    f2PromosiPart.setVisibility(View.GONE);
                    f2PenagihanPart.setVisibility(View.GONE);
                    f2PengirimanFormPart.setVisibility(View.GONE);
                    f2TotalPesanan = 0;
                    f2TotalPesananTV.setText("Rp 0");
                    f2TotalInvTagihan = 0;
                    f2TotalTagihanInvTV.setText("Rp 0");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                    f2NoSuratJalanChoiceTV.setText("");

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
                    f2TotalPesanan = 0;
                    f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                    f2ViewLampiranBTN.setVisibility(View.GONE);

                    f1NamaPelangganBaruED.setText("");
                    f1AlamatPelangganBaruED.setText("");
                    f2NamaPelangganBaruED.setText("");
                    f2AlamatPelangganBaruED.setText("");

                    uri = null;
                    lampiranImage.clear();
                    extentionImage.clear();
                    fullBase64String = "";
                    f2TotalPesanan = 0;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aktivitasKunjunganFormPart.setVisibility(View.GONE);
                            rencanaKunjunganFormPart.setVisibility(View.VISIBLE);
                            attantionNoForm.setVisibility(View.GONE);
                            loadingFormPart.setVisibility(View.GONE);
                        }
                    }, 1000);

                }
            }, 300);

        }
        else if (categoryReport.equals("0")) {
            categoryReport = "0";
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
            titlePageTV.setText("FORM LAPORAN HARIAN SALES");
            reportKategoriChoiceTV.setText("Aktivitas Kunjungan");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();

                    aktivitasKunjunganFormPart.setVisibility(View.GONE);
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
                    f1TokoChoiceTV.setText("");
                    f2TokoChoiceTV.setText("");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                    f1DetailPelanggan.setVisibility(View.GONE);
                    f1AlamatPelangganLamaTV.setText("");
                    f1AddPelangganPart.setVisibility(View.GONE);
                    f1AgendaOptionPart.setVisibility(View.GONE);
                    f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);
                    f1CB1.setChecked(false);
                    f1CB2.setChecked(false);
                    f1CB3.setChecked(false);
                    f1CB4.setChecked(false);
                    f1CB5.setChecked(false);
                    f1CB6.setChecked(false);

                    f2KeteranganKunjunganED.setText("");
                    f2PelangganOption.clearCheck();
                    f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                    f2PelangganBaruPart.setVisibility(View.GONE);
                    f2PelangganLamaPart.setVisibility(View.GONE);
                    f2NamaPelangganLamaChoiceTV.setText("");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                    f2DetailPelanggan.setVisibility(View.GONE);
                    f2AlamatPelangganLamaTV.setText("");
                    f2AgendaOptionPart.setVisibility(View.GONE);
                    f2PromosiPart.setVisibility(View.GONE);
                    dataProduct.clear();
                    f2JenisPelanggan = "";
                    f2IdPelangganLama = "";
                    f2CB1.setChecked(false);
                    f2CB2.setChecked(false);
                    f2CB3.setChecked(false);
                    f2CB4.setChecked(false);
                    f2CB5.setChecked(false);
                    f2CB6.setChecked(false);
                    f2PromosiPart.setVisibility(View.GONE);
                    f2PenagihanPart.setVisibility(View.GONE);
                    f2PengirimanFormPart.setVisibility(View.GONE);
                    f2TotalPesanan = 0;
                    f2TotalPesananTV.setText("Rp 0");
                    f2TotalInvTagihan = 0;
                    f2TotalTagihanInvTV.setText("Rp 0");
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                    f2NoSuratJalanChoiceTV.setText("");

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
                    f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                    f2ViewLampiranBTN.setVisibility(View.GONE);

                    f1NamaPelangganBaruED.setText("");
                    f1AlamatPelangganBaruED.setText("");
                    f2NamaPelangganBaruED.setText("");
                    f2AlamatPelangganBaruED.setText("");

                    uri = null;
                    lampiranImage.clear();
                    extentionImage.clear();
                    fullBase64String = "";
                    f2TotalPesanan = 0;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aktivitasKunjunganFormPart.setVisibility(View.VISIBLE);
                            rencanaKunjunganFormPart.setVisibility(View.GONE);
                            attantionNoForm.setVisibility(View.GONE);
                            loadingFormPart.setVisibility(View.GONE);
                        }
                    }, 1000);

                }
            }, 300);

        }
        else {
            titlePageTV.setText("FORM LAPORAN HARIAN SALES");
            loadingFormPart.setVisibility(View.GONE);
            attantionNoForm.setVisibility(View.VISIBLE);
        }
    }

    private void reportCategory() {
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_kategori_report_suma, bottomSheet, false));
        rencanaBTN = findViewById(R.id.rencana_kunjunagan_btn);
        aktivitasBTN = findViewById(R.id.aktivitas_btn);
        markRencana = findViewById(R.id.mark_rencana_kunjungan);
        markAktivitas = findViewById(R.id.mark_aktivitas);

        if (categoryReport.equals("1")) {
            markRencana.setVisibility(View.VISIBLE);
            markAktivitas.setVisibility(View.GONE);
            rencanaBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
            aktivitasBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
        } else if (categoryReport.equals("0")){
            markRencana.setVisibility(View.GONE);
            markAktivitas.setVisibility(View.VISIBLE);
            rencanaBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
            aktivitasBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
        }

        rencanaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryReport = "1";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
                titlePageTV.setText("FORM RENCANA KUNJUNGAN SALES");
                reportKategoriChoiceTV.setText("Rencana Kunjungan");
                markRencana.setVisibility(View.VISIBLE);
                markAktivitas.setVisibility(View.GONE);
                rencanaBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
                aktivitasBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        aktivitasKunjunganFormPart.setVisibility(View.GONE);
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
                        f1TokoChoiceTV.setText("");
                        f2TokoChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f1DetailPelanggan.setVisibility(View.GONE);
                        f1AlamatPelangganLamaTV.setText("");
                        f1AddPelangganPart.setVisibility(View.GONE);
                        f1AgendaOptionPart.setVisibility(View.GONE);
                        f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);
                        f1CB1.setChecked(false);
                        f1CB2.setChecked(false);
                        f1CB3.setChecked(false);
                        f1CB4.setChecked(false);
                        f1CB5.setChecked(false);
                        f1CB6.setChecked(false);

                        f2KeteranganKunjunganED.setText("");
                        f2PelangganOption.clearCheck();
                        f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f2PelangganBaruPart.setVisibility(View.GONE);
                        f2PelangganLamaPart.setVisibility(View.GONE);
                        f2NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f2DetailPelanggan.setVisibility(View.GONE);
                        f2AlamatPelangganLamaTV.setText("");
                        f2AgendaOptionPart.setVisibility(View.GONE);
                        f2PromosiPart.setVisibility(View.GONE);
                        dataProduct.clear();
                        f2JenisPelanggan = "";
                        f2IdPelangganLama = "";
                        f2CB1.setChecked(false);
                        f2CB2.setChecked(false);
                        f2CB3.setChecked(false);
                        f2CB4.setChecked(false);
                        f2CB5.setChecked(false);
                        f2CB6.setChecked(false);
                        f2PromosiPart.setVisibility(View.GONE);
                        f2PenagihanPart.setVisibility(View.GONE);
                        f2PengirimanFormPart.setVisibility(View.GONE);
                        f2TotalPesananTV.setText("Rp 0");
                        f2TotalInvTagihan = 0;
                        f2TotalTagihanInvTV.setText("Rp 0");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                        f2NoSuratJalanChoiceTV.setText("");

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
                        f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f2ViewLampiranBTN.setVisibility(View.GONE);

                        f1NamaPelangganBaruED.setText("");
                        f1AlamatPelangganBaruED.setText("");
                        f2NamaPelangganBaruED.setText("");
                        f2AlamatPelangganBaruED.setText("");

                        uri = null;
                        lampiranImage.clear();
                        extentionImage.clear();
                        fullBase64String = "";
                        f2TotalPesanan = 0;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                aktivitasKunjunganFormPart.setVisibility(View.GONE);
                                rencanaKunjunganFormPart.setVisibility(View.VISIBLE);
                                attantionNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                            }
                        }, 1000);

                    }
                }, 300);
            }
        });

        aktivitasBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                categoryReport = "0";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_REPORT_CATEGORY_ACTIVE, categoryReport);
                titlePageTV.setText("FORM LAPORAN HARIAN SALES");
                reportKategoriChoiceTV.setText("Aktivitas Kunjungan");
                markRencana.setVisibility(View.GONE);
                markAktivitas.setVisibility(View.VISIBLE);
                rencanaBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option));
                aktivitasBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        aktivitasKunjunganFormPart.setVisibility(View.GONE);
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
                        f1TokoChoiceTV.setText("");
                        f2TokoChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f1DetailPelanggan.setVisibility(View.GONE);
                        f1AlamatPelangganLamaTV.setText("");
                        f1AddPelangganPart.setVisibility(View.GONE);
                        f1AgendaOptionPart.setVisibility(View.GONE);
                        f1JsonArrayPelanggan = clearJSONArray(f1JsonArrayPelanggan);
                        f1CB1.setChecked(false);
                        f1CB2.setChecked(false);
                        f1CB3.setChecked(false);
                        f1CB4.setChecked(false);
                        f1CB5.setChecked(false);
                        f1CB6.setChecked(false);

                        f2KeteranganKunjunganED.setText("");
                        f2PelangganOption.clearCheck();
                        f2PelangganAttantionPart.setVisibility(View.VISIBLE);
                        f2PelangganBaruPart.setVisibility(View.GONE);
                        f2PelangganLamaPart.setVisibility(View.GONE);
                        f2NamaPelangganLamaChoiceTV.setText("");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                        f2DetailPelanggan.setVisibility(View.GONE);
                        f2AlamatPelangganLamaTV.setText("");
                        f2AgendaOptionPart.setVisibility(View.GONE);
                        f2PromosiPart.setVisibility(View.GONE);
                        dataProduct.clear();
                        f2JenisPelanggan = "";
                        f2IdPelangganLama = "";
                        f2CB1.setChecked(false);
                        f2CB2.setChecked(false);
                        f2CB3.setChecked(false);
                        f2CB4.setChecked(false);
                        f2CB5.setChecked(false);
                        f2CB6.setChecked(false);
                        f2PromosiPart.setVisibility(View.GONE);
                        f2PenagihanPart.setVisibility(View.GONE);
                        f2PengirimanFormPart.setVisibility(View.GONE);
                        f2TotalPesananTV.setText("Rp 0");
                        f2TotalInvTagihan = 0;
                        f2TotalTagihanInvTV.setText("Rp 0");
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                        f2NoSuratJalanChoiceTV.setText("");

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
                        f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                        f2ViewLampiranBTN.setVisibility(View.GONE);

                        f1NamaPelangganBaruED.setText("");
                        f1AlamatPelangganBaruED.setText("");
                        f2NamaPelangganBaruED.setText("");
                        f2AlamatPelangganBaruED.setText("");

                        uri = null;
                        lampiranImage.clear();
                        extentionImage.clear();
                        fullBase64String = "";
                        f2TotalPesanan = 0;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                aktivitasKunjunganFormPart.setVisibility(View.VISIBLE);
                                rencanaKunjunganFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                }, 300);
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private void f1DateVisit(){
        int y, m, d;
        if(!f1DateChoice.equals("")){
            y = Integer.parseInt(f1DateChoice.substring(0,4));
            m = Integer.parseInt(f1DateChoice.substring(5,7));
            d = Integer.parseInt(f1DateChoice.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

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

    private void f1TokoPicker(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_pelanggan_toko, bottomSheet, false));
        keywordTokoED = findViewById(R.id.keyword_toko_ed);
        baruBTN = findViewById(R.id.baru_btn);
        markBaru = findViewById(R.id.mark_baru);
        keywordTokoED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        tokoRV = findViewById(R.id.toko_rv);
        startAttantionTokoPart = findViewById(R.id.attantion_data_toko_part);
        noDataTokoPart = findViewById(R.id.no_data_toko_part);
        loadingDataTokoPart = findViewById(R.id.loading_data_toko_part);

        tokoRV.setLayoutManager(new LinearLayoutManager(this));
        tokoRV.setHasFixedSize(true);
        tokoRV.setNestedScrollingEnabled(false);
        tokoRV.setItemAnimator(new DefaultItemAnimator());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getToko("Semua");
            }
        }, 800);

        if (statuspelangganBaru.equals("1")) {
            markBaru.setVisibility(View.VISIBLE);
            baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
        } else {
            markBaru.setVisibility(View.GONE);
            baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_another));
        }

        baruBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                statuspelangganBaru = "1";
                f1TokoChoiceTV.setText("Pelanggan Baru");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                markBaru.setVisibility(View.VISIBLE);
                baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));

                f1PelangganOptionBaru.setChecked(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);

            }
        });

        keywordTokoED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                f1KeteranganKunjunganED.clearFocus();

                String keyWordSearch = keywordTokoED.getText().toString();

                startAttantionTokoPart.setVisibility(View.GONE);
                loadingDataTokoPart.setVisibility(View.VISIBLE);
                noDataTokoPart.setVisibility(View.GONE);
                tokoRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getToko(keyWordSearch);
                        }
                    }, 500);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getToko("Semua");
                        }
                    }, 500);
                }
            }

        });

    }

    private void f1PelangganLamaBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_nama_pelanggan_lama, bottomSheet, false));
        keywordED = findViewById(R.id.keyword_ed);
        keywordED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        pelangganRV = findViewById(R.id.pelanggan_rv);
        startAttantionPart = findViewById(R.id.attantion_data_part);
        noDataPart = findViewById(R.id.no_data_part);
        loadingDataPart = findViewById(R.id.loading_data_part);

        pelangganRV.setLayoutManager(new LinearLayoutManager(this));
        pelangganRV.setHasFixedSize(true);
        pelangganRV.setNestedScrollingEnabled(false);
        pelangganRV.setItemAnimator(new DefaultItemAnimator());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPelangganLama("Semua");
            }
        }, 800);

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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama(keyWordSearch);
                        }
                    }, 500);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama("Semua");
                        }
                    }, 500);
                }
            }

        });

    }

    private void f2TokoPicker(){
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_pelanggan_toko, bottomSheet, false));
        keywordTokoED = findViewById(R.id.keyword_toko_ed);
        baruBTN = findViewById(R.id.baru_btn);
        markBaru = findViewById(R.id.mark_baru);
        keywordTokoED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        tokoRV = findViewById(R.id.toko_rv);
        startAttantionTokoPart = findViewById(R.id.attantion_data_toko_part);
        noDataTokoPart = findViewById(R.id.no_data_toko_part);
        loadingDataTokoPart = findViewById(R.id.loading_data_toko_part);

        tokoRV.setLayoutManager(new LinearLayoutManager(this));
        tokoRV.setHasFixedSize(true);
        tokoRV.setNestedScrollingEnabled(false);
        tokoRV.setItemAnimator(new DefaultItemAnimator());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getToko("Semua");
            }
        }, 800);

        if (statuspelangganBaru.equals("1")) {
            markBaru.setVisibility(View.VISIBLE);
            baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));
        } else {
            markBaru.setVisibility(View.GONE);
            baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_another));
        }

        baruBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                statuspelangganBaru = "1";
                f2TokoChoiceTV.setText("Pelanggan Baru");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, "");
                markBaru.setVisibility(View.VISIBLE);
                baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_choice));

                f2PelangganOptionBaru.setChecked(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);

            }
        });

        keywordTokoED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                f1KeteranganKunjunganED.clearFocus();

                String keyWordSearch = keywordTokoED.getText().toString();

                startAttantionTokoPart.setVisibility(View.GONE);
                loadingDataTokoPart.setVisibility(View.VISIBLE);
                noDataTokoPart.setVisibility(View.GONE);
                tokoRV.setVisibility(View.GONE);

                if (!keyWordSearch.equals("")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getToko(keyWordSearch);
                        }
                    }, 500);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getToko("Semua");
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

        pelangganRV.setLayoutManager(new LinearLayoutManager(this));
        pelangganRV.setHasFixedSize(true);
        pelangganRV.setNestedScrollingEnabled(false);
        pelangganRV.setItemAnimator(new DefaultItemAnimator());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPelangganLama("Semua");
            }
        }, 800);

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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama(keyWordSearch);
                        }
                    }, 500);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPelangganLama("Semua");
                        }
                    }, 500);
                }
            }

        });

    }

    private void f2SuratJalanPicker() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_no_sj, bottomSheet, false));
        f2NoSjRV = findViewById(R.id.no_sj_rv);
        f2NoDataPartSj = findViewById(R.id.no_data_part_sj);
        f2LoadingDataPartSj = findViewById(R.id.loading_data_part_sj);

        f2NoSjRV.setLayoutManager(new LinearLayoutManager(this));
        f2NoSjRV.setHasFixedSize(true);
        f2NoSjRV.setNestedScrollingEnabled(false);
        f2NoSjRV.setItemAnimator(new DefaultItemAnimator());

        f2LoadingDataPartSj.setVisibility(View.VISIBLE);
        f2NoDataPartSj.setVisibility(View.GONE);
        f2NoSjRV.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(f2IdPelangganLama.equals("")){
                    getNoSJ("-");
                } else {
                    getNoSJ(f2IdPelangganLama);
                }
            }
        }, 800);

    }

    private void f2ProductListBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(ReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_product, bottomSheet, false));
        keywordEDProduk = findViewById(R.id.keyword_ed_produk);
        keywordEDProduk.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        produkRV = findViewById(R.id.produk_rv);
        startAttantionPartProduk = findViewById(R.id.attantion_data_part_produk);
        noDataPartProduk = findViewById(R.id.no_data_part_produk);
        loadingDataPartProduk = findViewById(R.id.loading_data_part_produk);

        produkRV.setLayoutManager(new LinearLayoutManager(this));
        produkRV.setHasFixedSize(true);
        produkRV.setNestedScrollingEnabled(false);
        produkRV.setItemAnimator(new DefaultItemAnimator());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProduct("Semua");
            }
        }, 800);

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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getProduct(keyWordSearch);
                        }
                    }, 500);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getProduct("Semua");
                        }
                    }, 500);
                }
            }

        });

    }

    private void getToko(String keyword) {
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
                                startAttantionTokoPart.setVisibility(View.GONE);
                                loadingDataTokoPart.setVisibility(View.GONE);
                                noDataTokoPart.setVisibility(View.GONE);
                                tokoRV.setVisibility(View.VISIBLE);

                                String data = response.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                pelangganLamas = gson.fromJson(data, PelangganLama[].class);
                                adapterTokoPelanggan = new AdapterTokoPelanggan(pelangganLamas, ReportSumaActivity.this);
                                tokoRV.setAdapter(adapterTokoPelanggan);
                            } else {
                                startAttantionTokoPart.setVisibility(View.GONE);
                                loadingDataTokoPart.setVisibility(View.GONE);
                                noDataTokoPart.setVisibility(View.VISIBLE);
                                tokoRV.setVisibility(View.GONE);
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

    private void getNoSJ(String id_pelanggan) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/get_no_sj";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "MissingPermission"})
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String data_surat_jalan = data.getString("data");
                                JSONArray jsonArray = new JSONArray(data_surat_jalan);
                                int arrayLength = jsonArray.length();
                                if(arrayLength != 0) {
                                    f2LoadingDataPartSj.setVisibility(View.GONE);
                                    f2NoDataPartSj.setVisibility(View.GONE);
                                    f2NoSjRV.setVisibility(View.VISIBLE);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataNoSuratJalans = gson.fromJson(data_surat_jalan, DataNoSuratJalan[].class);
                                    adapterNoSuratJalan = new AdapterNoSuratJalan(dataNoSuratJalans, ReportSumaActivity.this);
                                    f2NoSjRV.setAdapter(adapterNoSuratJalan);
                                } else {
                                    f2LoadingDataPartSj.setVisibility(View.GONE);
                                    f2NoDataPartSj.setVisibility(View.VISIBLE);
                                    f2NoSjRV.setVisibility(View.GONE);
                                }
                            } else {
                                f2LoadingDataPartSj.setVisibility(View.GONE);
                                f2NoDataPartSj.setVisibility(View.VISIBLE);
                                f2NoSjRV.setVisibility(View.GONE);
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
                params.put("id_pelanggan", id_pelanggan);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getProduct(String keyword) {
        String wilayah = sharedPrefManager.getSpCabName();
        String idSales = sharedPrefManager.getSpNik();
        final String API_ENDPOINT_CUSTOMER = "https://reporting.sumasistem.co.id/api/list_produk?keyword="+keyword+"&wilayah="+wilayah+"&id_sales="+idSales;
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

    public BroadcastReceiver pelangganTokoBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String namaPelangganToko = intent.getStringExtra("nama_pelanggan_toko");
            String idPelanggan = intent.getStringExtra("id_pelanggan_toko");
            String alamatPelanggan = intent.getStringExtra("alamat_pelanggan_toko");
            String picPelanggan = intent.getStringExtra("pic_pelanggan_toko");
            String teleponPelanggan = intent.getStringExtra("telepon_pelanggan_toko");

            try {
                if(categoryReport.equals("1")){
                    f1PelangganOptionLama.setChecked(true);
                } else {
                    f2PelangganOptionLama.setChecked(true);
                }

                statuspelangganBaru = "0";
                baruBTN.setBackground(ContextCompat.getDrawable(ReportSumaActivity.this, R.drawable.shape_option_another));

                if(categoryReport.equals("1")){
                    f1TokoChoiceTV.setText(namaPelangganToko);
                    f1IdPelangganLama = idPelanggan;
                    f1AlamatPelangganLamaTV.setText(alamatPelanggan);
                    f1DetailPelanggan.setVisibility(View.VISIBLE);
                    f1NamaPelangganLamaChoiceTV.setText(namaPelangganToko);
                    f1PelangganLamaPart.setVisibility(View.GONE);
                    f1AddPelangganPart.setVisibility(View.VISIBLE);
                    f1AgendaOptionPart.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = ReportSumaActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(ReportSumaActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    f1KeteranganKunjunganED.clearFocus();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheet.dismissSheet();
                        }
                    }, 300);
                } else if(categoryReport.equals("0")){
                    f2TokoChoiceTV.setText(namaPelangganToko);
                    f2AlamatPelangganLamaTV.setText(alamatPelanggan);
                    f2IdPelangganLama = idPelanggan;
                    f2DetailPelanggan.setVisibility(View.VISIBLE);
                    f2AgendaOptionPart.setVisibility(View.VISIBLE);
                    f2NamaPelangganLamaChoiceTV.setText(namaPelangganToko);

                    InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = ReportSumaActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(ReportSumaActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    f2KeteranganKunjunganED.clearFocus();
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                    f2NoSuratJalanChoiceTV.setText("");

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheet.dismissSheet();
                            if(f2CB2.isChecked()){
                                f2TotalTagihanInvTV.setText("Rp 0");
                                f2TotalInvTagihan = 0;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        bottomSheet.dismissSheet();
                                        f2LoadingDataInv.setVisibility(View.VISIBLE);
                                        f2InvRV.setVisibility(View.GONE);
                                        f2NoDataInv.setVisibility(View.GONE);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(f2IdPelangganLama.equals("")){
                                                    getDataInvoice("-");
                                                } else {
                                                    getDataInvoice(f2IdPelangganLama);
                                                }
                                            }
                                        }, 100);

                                    }
                                }, 300);
                            }
                        }
                    }, 300);
                }

            } catch(NullPointerException e) {
                Log.e("Error", e.toString());
            }
        }
    };

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
                f1IdPelangganLama = idPelanggan;
                f1DetailPelanggan.setVisibility(View.VISIBLE);
                f1NamaPelangganLamaChoiceTV.setText(namaPelangganLama);
                f1AddPelangganPart.setVisibility(View.VISIBLE);
                f1AgendaOptionPart.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = ReportSumaActivity.this.getCurrentFocus();
                if (view == null) {
                    view = new View(ReportSumaActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                f1KeteranganKunjunganED.clearFocus();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            } else if(categoryReport.equals("0")){
                f2AlamatPelangganLamaTV.setText(alamatPelanggan);
                f2IdPelangganLama = idPelanggan;
                f2DetailPelanggan.setVisibility(View.VISIBLE);
                f2AgendaOptionPart.setVisibility(View.VISIBLE);
                f2NamaPelangganLamaChoiceTV.setText(namaPelangganLama);

                InputMethodManager imm = (InputMethodManager) ReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = ReportSumaActivity.this.getCurrentFocus();
                if (view == null) {
                    view = new View(ReportSumaActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                f2KeteranganKunjunganED.clearFocus();
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                f2NoSuratJalanChoiceTV.setText("");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                        if(f2CB2.isChecked()){
                            f2TotalTagihanInvTV.setText("Rp 0");
                            f2TotalInvTagihan = 0;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bottomSheet.dismissSheet();
                                    f2LoadingDataInv.setVisibility(View.VISIBLE);
                                    f2InvRV.setVisibility(View.GONE);
                                    f2NoDataInv.setVisibility(View.GONE);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(f2IdPelangganLama.equals("")){
                                                getDataInvoice("-");
                                            } else {
                                                getDataInvoice(f2IdPelangganLama);
                                            }
                                        }
                                    }, 100);

                                }
                            }, 300);
                        }
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
                                    f2LoadingDataInv.setVisibility(View.GONE);
                                    f2InvRV.setVisibility(View.VISIBLE);
                                    f2NoDataInv.setVisibility(View.GONE);

                                    f2InvRV.setLayoutManager(new LinearLayoutManager(ReportSumaActivity.this));
                                    f2InvRV.setHasFixedSize(true);
                                    f2InvRV.setNestedScrollingEnabled(false);
                                    f2InvRV.setItemAnimator(new DefaultItemAnimator());

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

                                        f2TotalInvTagihan = grandTotal;

                                        String jsonString = jsonArray.toString();
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        dataInvoicePiutangs = gson.fromJson(jsonString, DataInvoicePiutang[].class);
                                        adapterInvoicePiutang = new AdapterInvoicePiutang(dataInvoicePiutangs, ReportSumaActivity.this);
                                        f2InvRV.setAdapter(adapterInvoicePiutang);

                                        Locale localeID = new Locale("id", "ID");
                                        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                        decimalFormat.setMaximumFractionDigits(0);

                                        f2TotalTagihanInvTV.setText(decimalFormat.format(grandTotal));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    f2LoadingDataInv.setVisibility(View.GONE);
                                    f2InvRV.setVisibility(View.GONE);
                                    f2NoDataInv.setVisibility(View.VISIBLE);
                                }

                            } else {
                                f2LoadingDataInv.setVisibility(View.GONE);
                                f2InvRV.setVisibility(View.GONE);
                                f2NoDataInv.setVisibility(View.VISIBLE);
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

            handler.postDelayed(new Runnable() {
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

            try {
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
            catch (WindowManager.BadTokenException e){
                Log.e("Error", e.toString());
            }

        }
    };

    public BroadcastReceiver noSuratJalanBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String no_sj = intent.getStringExtra("no_sj");
            f2NoSuratJalanChoiceTV.setText(no_sj);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    public BroadcastReceiver removePelanggan = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("nama_pelanggan");

            try {
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
            catch (WindowManager.BadTokenException e){
                Log.e("Error", e.toString());
            }

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
        f2TotalPesanan = jumlah;
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
                    Log.d("TAG", "GPS is on " + String.valueOf(location));
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
            } else {
                Log.e(ContentValues.TAG, "Alamat tidak ditemukan");
                f2DetailLocationTV.setText("Alamat tidak ditemukan");
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
        String code = "suma_report";
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
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")) {
                        lampiranImage.add(stringUri);
                        extentionImage.add(extension);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, ReportSumaActivity.this);
                        String a = "File Directory : " + file_directori + " URI: " + String.valueOf(uri);
                        Log.e("PaRSE JSON", a);

                        if (categoryReport.equals("0")) {
                            f2CountImageTV.setText(String.valueOf(lampiranImage.size()));
                            if (lampiranImage.size() >= 3) {
                                f2LampiranFotoBTN.setVisibility(View.GONE);
                            } else {
                                f2LampiranFotoBTN.setVisibility(View.VISIBLE);
                            }
                            f2ViewLampiranBTN.setVisibility(View.VISIBLE);
                            f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                            f2ViewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ReportSumaActivity.this, ViewImageSliderActivity.class);
                                    intent.putExtra("data", String.valueOf(lampiranImage));
                                    startActivity(intent);
                                }
                            });
                        }
                        uriToBase64(uri);

                    } else if(extension.equals(".png")||extension.equals(".PNG")){
                        String pngImagePath = FilePathimage.getPath(this, uri);
                        new ConvertImageTask().execute(pngImagePath);
                    } else {
                        handler.postDelayed(new Runnable() {
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
                                    titleMessageTV.setText("Rencana Terkirim");
                                    messageTV.setText("Terima kasih, rencana anda telah terkirim dan disimpan.");
                                    successPart.setVisibility(View.VISIBLE);
                                    formPart.setVisibility(View.GONE);
                                    pDialog.dismiss();

                                    viewPermohonanTV.setText("LIHAT LIST DATA");
                                    viewPermohonanBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_RELOAD_REQUEST, "true");
                                            onBackPressed();
                                        }
                                    });
                                } else if(categoryReport.equals("0")){
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
                } else if(categoryReport.equals("0")){
                    params.put("kategori_laporan", categoryReport);
                    params.put("keterangan_kunjungan", f2KeteranganKunjunganED.getText().toString());
                    params.put("tipe_pelanggan", f2JenisPelanggan);

                    if(f2JenisPelanggan.equals("1")){
                        params.put("nama_pelanggan", f2NamaPelangganBaruED.getText().toString());
                        params.put("alamat_pelanggan", f2AlamatPelangganBaruED.getText().toString());
                    } else if(f2JenisPelanggan.equals("2")){
                        params.put("id_pelanggan", f2IdPelangganLama);
                    }

                    if(f2CB1.isChecked()){
                        params.put("promosi", "true");
                        params.put("total_pesanan", String.valueOf(f2TotalPesanan));
                        params.put("data_produk", listToString(dataProduct));
                    } else {
                        params.put("promosi", "false");
                    }
                    if(f2CB2.isChecked()){
                        params.put("penagihan", "true");
                        params.put("total_tagihan", String.valueOf(f2TotalInvTagihan));
                    } else {
                        params.put("penagihan", "false");
                    }
                    if(f2CB3.isChecked()){
                        params.put("pengiriman", "true");
                        params.put("no_suratjalan", f2NoSuratJalanChoiceTV.getText().toString());
                    } else {
                        params.put("pengiriman", "false");
                    }
                    if(f2CB4.isChecked()) {
                        params.put("njv", "true");
                    } else {
                        params.put("njv", "false");
                    }
                    if(f2CB5.isChecked()) {
                        params.put("jv", "true");
                    } else {
                        params.put("jv", "false");
                    }
                    if(f2CB6.isChecked()) {
                        params.put("pameran", "true");
                    } else {
                        params.put("pameran", "false");
                    }

                    params.put("nik_sales", sharedPrefManager.getSpNik());
                    params.put("latitude", salesLat);
                    params.put("longitude", salesLong);
                    params.put("created_at", getTimeStamp());
                    params.put("total_laporan", String.valueOf(f2TotalPesanan));
                    params.put("jumlah_lampiran", String.valueOf(lampiranImage.size()));
                    params.put("extensi_lampiran", listToString(extentionImage));

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
        String UPLOAD_URL = "https://hrisgelora.co.id/api/upload_lampiran";
        String cleanString = filename.substring(1, filename.length() - 1);
        String[] parts = cleanString.split(",");

        for (int i = 0; i < lampiranImage.size(); i++) {
            String path = FilePathimage.getPath(this, Uri.parse(String.valueOf(lampiranImage.get(i))));
            if (path == null) {
                Toast.makeText(this, "Please move your image file to internal storage and retry", Toast.LENGTH_LONG).show();
            } else {
                String uploadId = UUID.randomUUID().toString();
                try {
                    new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                            .addFileToUpload(path, "image")
                            .addParameter("filename", parts[i])
                            .setMaxRetries(2)
                            .startUpload();
                } catch (Exception exc) {
                    Log.e("UploadError", "Error uploading file", exc);
                }
            }
            if(lampiranImage.size()-1==i){
                laporanTerkirim = "1";
                titleMessageTV.setText("Laporan Terkirim");
                messageTV.setText("Terima kasih, laporan anda telah terkirim dan disimpan.");
                successPart.setVisibility(View.VISIBLE);
                formPart.setVisibility(View.GONE);
                pDialog.dismiss();
                viewPermohonanTV.setText("LIHAT LIST DATA");
                viewPermohonanBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_RELOAD_REQUEST, "true");
                        onBackPressed();
                    }
                });
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

    @SuppressLint("StaticFieldLeak")
    private class ConvertImageTask extends AsyncTask<String, Void, Uri> {
        @Override
        protected Uri doInBackground(String... params) {
            String imagePath = params[0];
            try {
                return convertPNGtoJPEG(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Uri resultUri) {
            if (resultUri != null) {
                uri = resultUri;
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                lampiranImage.add(stringUri);
                extentionImage.add(extension);

                if (categoryReport.equals("0")) {
                    f2CountImageTV.setText(String.valueOf(lampiranImage.size()));
                    if (lampiranImage.size() >= 3) {
                        f2LampiranFotoBTN.setVisibility(View.GONE);
                    } else {
                        f2LampiranFotoBTN.setVisibility(View.VISIBLE);
                    }
                    f2ViewLampiranBTN.setVisibility(View.VISIBLE);
                    f2LabelLampiranTV.setText("+ Lampiran Foto/SP");
                    f2ViewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ReportSumaActivity.this, ViewImageSliderActivity.class);
                            intent.putExtra("data", String.valueOf(lampiranImage));
                            startActivity(intent);
                        }
                    });
                }
                uriToBase64(uri);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new KAlertDialog(ReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Terjadi kesalahan")
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
        }

        private Uri convertPNGtoJPEG(String inputFilePath) throws IOException {
            Bitmap pngImage = BitmapFactory.decodeFile(inputFilePath);
            File jpgFile = new File(getExternalFilesDir(null), "output.jpg");
            FileOutputStream out = new FileOutputStream(jpgFile);
            pngImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return Uri.fromFile(jpgFile);
        }
    }

    private void devModeCheck() {
        final String url = "https://hrisgelora.co.id/api/dev_mode_check";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            if(status.equals("on")){
                                devModeStatus = "on";
                            } else {
                                devModeStatus = "off";
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

    public boolean isDeveloperModeEnabled(){
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 17) {
            return android.provider.Settings.Secure.getInt(ReportSumaActivity.this.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}