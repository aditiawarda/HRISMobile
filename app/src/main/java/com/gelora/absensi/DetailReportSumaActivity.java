package com.gelora.absensi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import android.os.ResultReceiver;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gelora.absensi.adapter.AdapterInvoicePiutangRealisasi;
import com.gelora.absensi.adapter.AdapterNoSuratJalanRealisasi;
import com.gelora.absensi.adapter.AdapterProductInputSumaRealisasi;
import com.gelora.absensi.adapter.AdapterProductSumaRealisasi;
import com.gelora.absensi.adapter.AdapterReportComment;
import com.gelora.absensi.adapter.AdapterSumaReport;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataInvoicePiutang;
import com.gelora.absensi.model.DataNoSuratJalan;
import com.gelora.absensi.model.DataReportSuma;
import com.gelora.absensi.model.ProductSuma;
import com.gelora.absensi.model.ReportComment;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import net.cachapa.expandablelayout.ExpandableLayout;
import net.gotev.uploadservice.MultipartUploadRequest;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DetailReportSumaActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout noDataComment, commentLoading, commentSendBTNPart, addProductBTN, productInputDetailPart, loadingDataPartProduk, noDataPartProduk, startAttantionPart, startAttantionPartProduk, productChoiceBTN, markRealKunjungan, markRealPenagihan, markRealPengiriman, realKunjunganBTN, realPenagihanBTN, realPengirimanBTN, noDataPiutang, loadingDataPiutang, loadingDataPartSj, noDataPartSj, noSuratJalanBTN, promosiLayoutTambahan, penagihanLayoutTambahan, pengirimanLayoutTambahan, submitUpdateBTN, fotoLamBTN, updateBTN, updatePart, deletePart, deleteBTN, noSuratJalanPart, submitRescheduleBTN, choiceDateBTN, rescheduleBTN, reschedulePart, totalPenagihanPart, totalPesananPart, viewRealisasiPart, viewRealisasiBTN, realMark, submitRealisasiBTN, viewLampiranRealisasiBTN, viewLampiranUpdateBTN, fotoLampiranRealisasiBTN, gpsRealisasiBTN, updateRealisasiBTN, viewLampiranBTN, tglRencanaPart, backBTN, actionBar, mapsPart, updateRealisasiKunjunganPart;
    SharedPrefManager sharedPrefManager;
    EditText commentED, keywordEDProduk, keteranganKunjunganRealisasiED, keteranganUpdateED;
    ExpandableLayout updateRealisasiKunjunganForm, rescheduleForm, updateForm;
    RequestQueue requestQueue;
    TextView keteranganLabelTV, titlePageTV, jumlahKomenTV, inputTotalPesananTV, subTotalTV, productHargaSatuanTV, productChoiceTV, agendaLabel, totalInvTV, noSuratJalanChoiceTV, noSuratJalanTV, countImageUpdateTV, countImageTV, choiceDateTV, totalPenagihanTV, totalPesananTV, tanggalBuatTV, labelLampiranTV, labelLampTV, detailLocationRealisasiTV, tglRencanaTV, nikSalesTV, namaSalesTV, detailLocationTV, reportKategoriTV, namaPelangganTV, alamatPelangganTV, picPelangganTV, teleponPelangganTV, keteranganTV;
    String subTotal = "", qtyProduct = "", tipeLaporan = "", idReport = "", idProduct = "", productName = "", productHargaSatuan = "";
    SwipeRefreshLayout refreshLayout;
    SharedPrefAbsen sharedPrefAbsen;
    private StatusBarColorManager mStatusBarColorManager;
    private LatLng userPoint;
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    ResultReceiver resultReceiver;
    Context mContext;
    Activity mActivity;
    String fullDataProduct = "", idPelanggan = "", locationNow = "", salesLat = "", salesLong = "", choiceDateReschedule = "";
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private List<String> lampiranImage = new ArrayList<>();
    private List<String> extentionImage = new ArrayList<>();
    KAlertDialog pDialog;
    private int i = -1;
    NestedScrollView scrollView;
    CheckBox agendaCB1, agendaCB2, agendaCB3, agendaCB4, agendaCB5, agendaCB6;
    CheckBox realisasiCB1, realisasiCB2, realisasiCB3, realisasiCB4, realisasiCB5, realisasiCB6;
    BottomSheetLayout bottomSheet;
    RecyclerView noSjRV;
    private DataNoSuratJalan[] dataNoSuratJalans;
    private AdapterNoSuratJalanRealisasi adapterNoSuratJalan;
    private AdapterReportComment adapterReportComment;
    RecyclerView invRV;
    int totalPesanan = 0;
    int totalTagihan = 0;
    private DataInvoicePiutang[] dataInvoicePiutangs;
    private AdapterInvoicePiutangRealisasi adapterInvoicePiutang;
    RecyclerView produkRV, listProductInputRV, commentRV;
    private ProductSuma[] productSumas;
    private ReportComment[] reportComments;
    private AdapterProductSumaRealisasi adapterProductSuma;
    NumberPicker qtyProductPicker;
    private List<String> dataProductRealisasi = new ArrayList<>();
    private AdapterProductInputSumaRealisasi adapterProductInputSuma;
    private boolean isFirstRun = true;
    ImageButton commentSendBTN;
    private Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report_suma);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        isFirstRun = false;

        resultReceiver = new DetailReportSumaActivity.AddressResultReceiver(new Handler());
        mContext = getBaseContext();
        mActivity = DetailReportSumaActivity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
        idReport = getIntent().getExtras().getString("report_id");

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        titlePageTV = findViewById(R.id.title_page_tv);
        scrollView = findViewById(R.id.scrollView);
        reportKategoriTV = findViewById(R.id.report_kategori_tv);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        namaPelangganTV = findViewById(R.id.nama_pelanggan_tv);
        alamatPelangganTV = findViewById(R.id.alamat_pelanggan_tv);
        picPelangganTV = findViewById(R.id.pic_pelanggan_tv);
        teleponPelangganTV = findViewById(R.id.telepon_pelanggan_tv);
        keteranganTV = findViewById(R.id.keterangan_tv);
        detailLocationTV = findViewById(R.id.detail_location_tv);
        namaSalesTV = findViewById(R.id.nama_sales_tv);
        nikSalesTV = findViewById(R.id.nik_sales_tv);
        mapsPart = findViewById(R.id.maps_part);
        tglRencanaPart = findViewById(R.id.tgl_rencana_part);
        tglRencanaTV = findViewById(R.id.tgl_rencana_tv);
        viewLampiranBTN = findViewById(R.id.view_lampiran_btn);
        updateRealisasiBTN = findViewById(R.id.update_realisasi_btn);
        updateRealisasiKunjunganForm = findViewById(R.id.update_realisasi_kunjungan_form);
        updateRealisasiKunjunganPart = findViewById(R.id.update_realisasi_kunjungan_part);
        gpsRealisasiBTN = findViewById(R.id.gps_realisasi_btn);
        detailLocationRealisasiTV = findViewById(R.id.detail_location_realisasi_tv);
        fotoLampiranRealisasiBTN = findViewById(R.id.foto_lampiran_realisasi_btn);
        viewLampiranRealisasiBTN = findViewById(R.id.view_lampiran_realisasi_btn);
        viewLampiranUpdateBTN = findViewById(R.id.view_lam_btn);
        labelLampiranTV = findViewById(R.id.label_lampiran_tv);
        labelLampTV = findViewById(R.id.label_lam_tv);
        submitRealisasiBTN = findViewById(R.id.submit_realisasi_btn);
        realMark = findViewById(R.id.real_mark);
        viewRealisasiBTN = findViewById(R.id.view_realisasi_btn);
        viewRealisasiPart = findViewById(R.id.view_realisasi_part);
        tanggalBuatTV = findViewById(R.id.tanggal_buat_tv);
        totalPesananPart = findViewById(R.id.total_pesanan_part);
        totalPesananTV = findViewById(R.id.total_pesanan_tv);
        inputTotalPesananTV = findViewById(R.id.input_total_pesanan_tv);
        totalPenagihanPart = findViewById(R.id.total_penagihan_part);
        totalPenagihanTV = findViewById(R.id.total_piutang_tv);
        noSuratJalanPart = findViewById(R.id.no_surat_jalan_part);
        noSuratJalanTV = findViewById(R.id.no_surat_jalan_tv);
        reschedulePart = findViewById(R.id.reschedule_part);
        rescheduleBTN = findViewById(R.id.reschedule_btn);
        rescheduleForm = findViewById(R.id.reschedule_form);
        choiceDateBTN = findViewById(R.id.choice_date_btn);
        choiceDateTV = findViewById(R.id.choice_date_tv);
        submitRescheduleBTN = findViewById(R.id.submit_reschedule_btn);
        keteranganKunjunganRealisasiED = findViewById(R.id.keterangan_kunjungan_ed);
        countImageTV = findViewById(R.id.count_image_tv);
        countImageUpdateTV = findViewById(R.id.count_img_tv);
        deletePart = findViewById(R.id.delete_part);
        deleteBTN = findViewById(R.id.delete_btn);
        updatePart = findViewById(R.id.update_part);
        updateForm = findViewById(R.id.update_form);
        updateBTN = findViewById(R.id.update_btn);
        keteranganUpdateED = findViewById(R.id.keterangan_ed);
        fotoLamBTN = findViewById(R.id.foto_lam_btn);
        submitUpdateBTN = findViewById(R.id.submit_update_btn);
        agendaCB1 = findViewById(R.id.agenda_cb_1);
        agendaCB2 = findViewById(R.id.agenda_cb_2);
        agendaCB3 = findViewById(R.id.agenda_cb_3);
        agendaCB4 = findViewById(R.id.agenda_cb_4);
        agendaCB5 = findViewById(R.id.agenda_cb_5);
        agendaCB6 = findViewById(R.id.agenda_cb_6);
        realisasiCB1 = findViewById(R.id.realisasi_cb_1);
        realisasiCB2 = findViewById(R.id.realisasi_cb_2);
        realisasiCB3 = findViewById(R.id.realisasi_cb_3);
        realisasiCB4 = findViewById(R.id.realisasi_cb_4);
        realisasiCB5 = findViewById(R.id.realisasi_cb_5);
        realisasiCB6 = findViewById(R.id.realisasi_cb_6);
        noDataPiutang = findViewById(R.id.no_data_inv);
        loadingDataPiutang = findViewById(R.id.loading_data_inv);
        pengirimanLayoutTambahan = findViewById(R.id.pengiriman_layout_tambahan);
        penagihanLayoutTambahan = findViewById(R.id.penagihan_layout_tambahan);
        promosiLayoutTambahan = findViewById(R.id.promosi_layout_tambahan);
        noSuratJalanBTN = findViewById(R.id.no_surat_jalan_btn);
        noSuratJalanChoiceTV = findViewById(R.id.no_surat_jalan_choice_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        totalInvTV = findViewById(R.id.total_inv_tv);
        invRV = findViewById(R.id.inv_rv);
        agendaLabel = findViewById(R.id.agenda_label);
        productChoiceBTN = findViewById(R.id.product_choice_btn);
        productChoiceTV = findViewById(R.id.product_choice_tv);
        productInputDetailPart = findViewById(R.id.product_input_detail_part);
        qtyProductPicker = findViewById(R.id.qty_product_picker);
        productHargaSatuanTV = findViewById(R.id.product_harga_satuan_tv);
        subTotalTV = findViewById(R.id.sub_total_tv);
        addProductBTN = findViewById(R.id.add_product_btn);
        listProductInputRV = findViewById(R.id.item_produk_input_rv);
        commentED = findViewById(R.id.comment_ed);
        commentSendBTN = findViewById(R.id.comment_send_btn);
        commentSendBTNPart = findViewById(R.id.comment_send_btn_part);
        commentLoading = findViewById(R.id.comment_loading);
        commentRV = findViewById(R.id.comment_rv);
        noDataComment = findViewById(R.id.no_data_comment);
        jumlahKomenTV = findViewById(R.id.jumlah_komen_tv);
        keteranganLabelTV = findViewById(R.id.keterangan_label_tv);

        commentRV.setLayoutManager(new LinearLayoutManager(DetailReportSumaActivity.this));
        commentRV.setHasFixedSize(true);
        commentRV.setNestedScrollingEnabled(false);
        commentRV.setItemAnimator(new DefaultItemAnimator());

        adapterProductInputSuma = new AdapterProductInputSumaRealisasi(dataProductRealisasi);
        listProductInputRV.setLayoutManager(new LinearLayoutManager(this));
        listProductInputRV.setAdapter(adapterProductInputSuma);
        listProductInputRV.setHasFixedSize(true);
        listProductInputRV.setNestedScrollingEnabled(false);
        listProductInputRV.setItemAnimator(new DefaultItemAnimator());

        LocalBroadcastManager.getInstance(this).registerReceiver(productSumaBroad, new IntentFilter("product_suma_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(noSuratJalanBroad, new IntentFilter("list_no_sj"));
        LocalBroadcastManager.getInstance(this).registerReceiver(productDeleteBroad, new IntentFilter("product_delete_broad_realisasi"));

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                rescheduleForm.collapse();
                updateForm.collapse();
                updateRealisasiKunjunganForm.collapse();
                choiceDateTV.setText("");
                choiceDateReschedule = "";
                lampiranImage.clear();
                extentionImage.clear();
                keteranganKunjunganRealisasiED.setText("");

                dataProductRealisasi.clear();
                realisasiCB1.setChecked(false);
                realisasiCB2.setChecked(false);
                realisasiCB3.setChecked(false);
                realisasiCB4.setChecked(false);
                realisasiCB5.setChecked(false);
                realisasiCB6.setChecked(false);
                promosiLayoutTambahan.setVisibility(View.GONE);
                penagihanLayoutTambahan.setVisibility(View.GONE);
                pengirimanLayoutTambahan.setVisibility(View.GONE);
                totalPesanan = 0;
                inputTotalPesananTV.setText("Rp 0");
                totalTagihan = 0;
                totalInvTV.setText("Rp 0");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                noSuratJalanChoiceTV.setText("");

                adapterProductInputSuma = new AdapterProductInputSumaRealisasi(dataProductRealisasi);
                listProductInputRV.setLayoutManager(new LinearLayoutManager(DetailReportSumaActivity.this));
                listProductInputRV.setAdapter(adapterProductInputSuma);
                listProductInputRV.setHasFixedSize(true);
                listProductInputRV.setNestedScrollingEnabled(false);
                listProductInputRV.setItemAnimator(new DefaultItemAnimator());

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                productChoiceTV.setText("Pilih produk untuk menambahkan...");
                productHargaSatuanTV.setText("Rp 0");
                subTotalTV.setText("Rp 0");
                qtyProductPicker.setMin(0);
                qtyProductPicker.setValue(0);
                idProduct = "";
                productName = "";
                productHargaSatuan = "";
                qtyProduct = "";
                subTotal = "";
                productInputDetailPart.setVisibility(View.GONE);
                totalPesananTV.setText("Rp 0");
                labelLampTV.setText("+ Lampiran Foto/SP");
                viewLampiranRealisasiBTN.setVisibility(View.GONE);

                uri = null;
                lampiranImage.clear();
                extentionImage.clear();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        permissionLoc();
                    }
                }, 1000);
            }
        });

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        choiceDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerOfDate();
            }
        });

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateForm.isExpanded()){
                    updateForm.collapse();
                } else {
                    updateForm.expand();
                    rescheduleForm.collapse();
                    updateRealisasiKunjunganForm.collapse();
                }
            }
        });

        rescheduleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rescheduleForm.isExpanded()){
                    rescheduleForm.collapse();
                } else {
                    rescheduleForm.expand();
                    updateRealisasiKunjunganForm.collapse();
                    updateForm.collapse();
                }
            }
        });

        updateRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateRealisasiKunjunganForm.isExpanded()){
                    updateRealisasiKunjunganForm.collapse();
                } else {
                    updateRealisasiKunjunganForm.expand();
                    rescheduleForm.collapse();
                    updateForm.collapse();
                }
            }
        });

        realisasiCB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    promosiLayoutTambahan.setVisibility(View.VISIBLE);
                } else {
                    promosiLayoutTambahan.setVisibility(View.GONE);
                }
            }
        });

        realisasiCB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    penagihanLayoutTambahan.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheet.dismissSheet();
                            loadingDataPiutang.setVisibility(View.VISIBLE);
                            invRV.setVisibility(View.GONE);
                            noDataPiutang.setVisibility(View.GONE);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getDataInvoice(idPelanggan);
                                }
                            }, 300);
                        }
                    }, 300);
                } else {
                    penagihanLayoutTambahan.setVisibility(View.GONE);
                    totalInvTV.setText("Rp 0");
                    totalTagihan = 0;
                }
            }
        });

        realisasiCB3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pengirimanLayoutTambahan.setVisibility(View.VISIBLE);
                } else {
                    pengirimanLayoutTambahan.setVisibility(View.GONE);
                    sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
                }
            }
        });

        productChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productListBottomSheet();
            }
        });

        qtyProductPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                qtyProduct = String.valueOf(value);
                if (productHargaSatuan.equals("")) {
                    qtyProductPicker.setValue(0);
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                    subTotalTV.setText(decimalFormat.format(Integer.parseInt(String.valueOf(Integer.parseInt(productHargaSatuan) * value))));
                    subTotal = String.valueOf(Integer.parseInt(productHargaSatuan) * value);
                }
            }
        });

        addProductBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                if (idProduct.equals("")) {
                    qtyProductPicker.setValue(0);
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                    fullDataProduct = idProduct + "/" + productName + "/" + productHargaSatuan + "/" + qtyProduct + "/" + subTotal;
                    if (!fullDataProduct.isEmpty() || fullDataProduct.equals("")) {
                        dataProductRealisasi.add(fullDataProduct);
                        adapterProductInputSuma.notifyDataSetChanged();
                        productChoiceTV.setText("Pilih produk untuk menambahkan...");
                        productHargaSatuanTV.setText("Rp 0");
                        subTotalTV.setText("Rp 0");
                        qtyProductPicker.setMin(0);
                        qtyProductPicker.setValue(0);
                        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");
                        fullDataProduct = "";
                        idProduct = "";
                        productName = "";
                        productHargaSatuan = "";
                        qtyProduct = "";
                        subTotal = "";
                        productInputDetailPart.setVisibility(View.GONE);
                        hitungTotalPesanan();
                    }
                }
            }
        });

        noSuratJalanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suratJalanPicker();
            }
        });

        gpsRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailReportSumaActivity.this, LocationPickerActivity.class);
                startActivity(intent);
            }
        });

        fotoLamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        fotoLampiranRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        submitUpdateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Simpan perubahan data?")
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
                                pDialog = new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }

                                    public void onFinish() {
                                        i = -1;
                                        submitUpdateData();
                                    }
                                }.start();
                            }
                        })
                        .show();
            }
        });

        submitRescheduleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!choiceDateReschedule.equals("")){
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Kirim penjadwalan ulang?")
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
                                    pDialog = new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            submitReschedule();
                                        }
                                    }.start();
                                }
                            })
                            .show();

                } else {
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi data tanggal!")
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

        submitRealisasiBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!salesLat.equals("0")&&!salesLong.equals("0")) && !salesLat.equals("") && !salesLong.equals("") && lampiranImage.size()!=0 && !keteranganKunjunganRealisasiED.getText().toString().equals("")){
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Update realisasi sekarang?")
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
                                    pDialog = new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (DetailReportSumaActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            if (isDeveloperModeEnabled()){
                                                pDialog.dismiss();
                                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                                                submitRealisasi();
                                            }
                                        }
                                    }.start();
                                }
                            })
                            .show();

                } else {
                    new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap masukkan keterangan, unggah lampiran dan pastikan posisi GPS sesuai!")
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

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if(tipeLaporan.equals("1")){
                    message = "Apakah anda yakin untuk menghapus data rencana?";
                } else {
                    message = "Apakah anda yakin untuk menghapus data laporan?";
                }
                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText(message)
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
                                pDialog = new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (DetailReportSumaActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        deleteData(idReport);
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
        });

        commentED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!commentED.getText().toString().equals("")){
                    commentLoading.setVisibility(View.GONE);
                    commentSendBTNPart.setVisibility(View.VISIBLE);
                } else {
                    commentLoading.setVisibility(View.GONE);
                    commentSendBTNPart.setVisibility(View.GONE);
                }
            }

        });

        commentSendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentED.getText().toString();
                commentED.setText("");
                commentLoading.setVisibility(View.VISIBLE);
                commentSendBTNPart.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendComment(comment);
                    }
                }, 1000);
            }
        });

        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, "");

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0,0,0,0);
        permissionLoc();
    }

    private void permissionLoc(){
        if (ContextCompat.checkSelfPermission(DetailReportSumaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(DetailReportSumaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailReportSumaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            mMap.clear();
            getData();
            userPosition();
        }
    }

    public BroadcastReceiver productDeleteBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String index = intent.getStringExtra("index");

            try {
                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                dataProductRealisasi.remove(Integer.parseInt(index));

                                adapterProductInputSuma = new AdapterProductInputSumaRealisasi(dataProductRealisasi);
                                listProductInputRV.setLayoutManager(new LinearLayoutManager(DetailReportSumaActivity.this));
                                listProductInputRV.setAdapter(adapterProductInputSuma);
                                listProductInputRV.setHasFixedSize(true);
                                listProductInputRV.setNestedScrollingEnabled(false);
                                listProductInputRV.setItemAnimator(new DefaultItemAnimator());

                                hitungTotalPesanan();
                            }
                        })
                        .show();
            } catch (WindowManager.BadTokenException e){
                Log.e("Error", e.toString());
            }

        }
    };

    public BroadcastReceiver noSuratJalanBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String no_sj = intent.getStringExtra("no_sj");
            noSuratJalanChoiceTV.setText(no_sj);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("MissingPermission")
    private void userPosition() {
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
                    new ReverseGeocodingTaskRealisasi().execute(getLoc);
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
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(DetailReportSumaActivity.this).checkLocationSettings(builder.build());
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
                                        DetailReportSumaActivity.this,
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

    private void deleteData(String idReport){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/nonactive_reporting";
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
                                pDialog.setTitleText("Dihapus")
                                        .setContentText("Data berhasil dihapus")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                pDialog.setTitleText("Gagal")
                                    .setContentText("Terjadi kesalahan")
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
                        pDialog.setTitleText("Gagal")
                                .setContentText("Terjadi kesalahan")
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
                params.put("id_report", idReport);
                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);


    }

    private void productListBottomSheet() {
        bottomSheet.showWithSheetView(LayoutInflater.from(DetailReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_product, bottomSheet, false));
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
                keteranganKunjunganRealisasiED.clearFocus();
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
                        Log.d(MotionEffect.TAG, "Response: " + response.toString());
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
                                adapterProductSuma = new AdapterProductSumaRealisasi(productSumas, DetailReportSumaActivity.this);
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
                        Log.e(MotionEffect.TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                }) {
        };

        requestQueue.add(jsonObjectRequest);

    }

    @SuppressLint("SimpleDateFormat")
    private void pickerOfDate(){
        int y, m, d;
        if(!choiceDateReschedule.equals("")){
            y = Integer.parseInt(choiceDateReschedule.substring(0,4));
            m = Integer.parseInt(choiceDateReschedule.substring(5,7));
            d = Integer.parseInt(choiceDateReschedule.substring(8,10));
        } else {
            y = Integer.parseInt(getDateY());
            m = Integer.parseInt(getDateM());
            d = Integer.parseInt(getDateD());
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(DetailReportSumaActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            choiceDateReschedule = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            Date date2 = null;
            try {
                date = sdf.parse(choiceDateReschedule);
                date2 = sdf.parse(getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long choice = date.getTime();
            long now = date2.getTime();

            if (choice>=now){
                String input_date = choiceDateReschedule;
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

                choiceDateTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

            } else {
                choiceDateTV.setText("Pilih Kembali !");
                choiceDateReschedule = "";

                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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

    private void submitUpdateData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/update_reporting";
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
                                String file_name = data.getString("file_name");
                                if(file_name.equals("No data")){
                                    scrollView.fullScroll(NestedScrollView.FOCUS_UP);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            pDialog.setTitleText("Berhasil")
                                                    .setContentText("Data laporan berhasil diupdate")
                                                    .setConfirmText("    OK    ")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                            getData();
                                                            updateForm.collapse();
                                                            lampiranImage.clear();
                                                            fotoLamBTN.setVisibility(View.VISIBLE);
                                                            viewLampiranUpdateBTN.setVisibility(View.GONE);
                                                        }
                                                    })
                                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        }
                                    }, 500);
                                } else {
                                    uploadLampiran(file_name, idReport);
                                }
                            } else {
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
                params.put("id_report", idReport);
                params.put("keterangan", keteranganUpdateED.getText().toString());
                params.put("jumlah_lampiran_update", String.valueOf(lampiranImage.size()));
                params.put("extensi_lampiran", listToString(extentionImage));
                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    private void submitReschedule(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/update_reschedule";
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
                                String input_date = choiceDateReschedule;
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

                                String tanggal = String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate;
                                pDialog.setTitleText("Berhasil")
                                        .setContentText("Tanggal rencana berhasil dijadwalkan ulang ke tanggal "+tanggal)
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                getData();
                                                rescheduleForm.collapse();
                                                choiceDateReschedule = "";
                                                choiceDateTV.setText("");
                                                scrollView.fullScroll(NestedScrollView.FOCUS_UP);
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
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
                params.put("id_report", idReport);
                params.put("tanggal_rencana", choiceDateReschedule);
                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    private void submitRealisasi(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/update_realisasi";
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
                                String filename = data.getString("file_name");
                                uploadLampiran(filename, idLaporan);
                                updateForm.collapse();
                                rescheduleForm.collapse();
                                updateRealisasiKunjunganForm.collapse();
                            } else {
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
                params.put("id_report", idReport);
                params.put("latitude", salesLat);
                params.put("longitude", salesLong);
                params.put("keterangan", keteranganKunjunganRealisasiED.getText().toString());
                if(realisasiCB1.isChecked()){
                    params.put("promosi", "true");
                    params.put("total_pesanan", String.valueOf(totalPesanan));
                    params.put("data_produk", listToString(dataProductRealisasi));
                } else {
                    params.put("promosi", "false");
                }
                if(realisasiCB2.isChecked()){
                    params.put("penagihan", "true");
                    params.put("total_tagihan", String.valueOf(totalTagihan));
                } else {
                    params.put("penagihan", "false");
                }
                if(realisasiCB3.isChecked()){
                    params.put("pengiriman", "true");
                    params.put("no_suratjalan", noSuratJalanChoiceTV.getText().toString());
                } else {
                    params.put("pengiriman", "false");
                }
                if(realisasiCB4.isChecked()){
                    params.put("njv", "true");
                } else {
                    params.put("njv", "false");
                }
                if(realisasiCB5.isChecked()){
                    params.put("jv", "true");
                } else {
                    params.put("jv", "false");
                }
                if(realisasiCB6.isChecked()){
                    params.put("pameran", "true");
                } else {
                    params.put("pameran", "false");
                }
                params.put("jumlah_lampiran", String.valueOf(lampiranImage.size()));
                params.put("extensi_lampiran", listToString(extentionImage));
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
                scrollView.fullScroll(NestedScrollView.FOCUS_UP);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(tipeLaporan.equals("1")){
                            pDialog.dismiss();
                            updateRealisasiKunjunganPart.setVisibility(View.GONE);
                            viewRealisasiPart.setVisibility(View.VISIBLE);
                            viewRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                                    intent.putExtra("report_id",idReport);
                                    startActivity(intent);
                                }
                            });
                            getData();
                        } else {
                            pDialog.setTitleText("Berhasil")
                                    .setContentText("Data laporan berhasil diupdate")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            getData();
                                            updateForm.collapse();
                                            lampiranImage.clear();
                                            extentionImage.clear();
                                            fotoLamBTN.setVisibility(View.VISIBLE);
                                            viewLampiranUpdateBTN.setVisibility(View.GONE);
                                        }
                                    })
                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        }
                    }
                }, 500);
            }
        }
    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(DetailReportSumaActivity.this)
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
            Dexter.withActivity(DetailReportSumaActivity.this)
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
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, "suma_report");
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(DetailReportSumaActivity.this);
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
        Intent intent = new Intent(DetailReportSumaActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(DetailReportSumaActivity.this, ImagePickerActivity.class);
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
        String file_directori = getRealPathFromURIPath(uri, DetailReportSumaActivity.this);
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
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")){
                        lampiranImage.add(stringUri);
                        extentionImage.add(extension);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, DetailReportSumaActivity.this);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);

                        if(tipeLaporan.equals("1")){
                            countImageTV.setText(String.valueOf(lampiranImage.size()));
                            if(lampiranImage.size()>=3){
                                fotoLampiranRealisasiBTN.setVisibility(View.GONE);
                            } else {
                                fotoLampiranRealisasiBTN.setVisibility(View.VISIBLE);
                            }
                            viewLampiranRealisasiBTN.setVisibility(View.VISIBLE);
                            labelLampiranTV.setText("+ Lampiran Foto/SP");
                            viewLampiranRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                    intent.putExtra("data", String.valueOf(lampiranImage));
                                    startActivity(intent);
                                }
                            });
                        } else {
                            countImageUpdateTV.setText(String.valueOf(lampiranImage.size()));
                            if(lampiranImage.size()>=3){
                                fotoLamBTN.setVisibility(View.GONE);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,61
                                );
                                layoutParams.setMargins(0, 0, 0, 0);
                                viewLampiranUpdateBTN.setLayoutParams(layoutParams);
                            } else {
                                fotoLamBTN.setVisibility(View.VISIBLE);
                            }
                            viewLampiranUpdateBTN.setVisibility(View.VISIBLE);
                            labelLampTV.setText("Ubah Lampiran Foto/SP");
                            viewLampiranUpdateBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                    intent.putExtra("data", String.valueOf(lampiranImage));
                                    startActivity(intent);
                                }
                            });
                        }
                    } else if(extension.equals(".png")||extension.equals(".PNG")){
                        String pngImagePath = FilePathimage.getPath(this, uri);
                        new ConvertImageTask().execute(pngImagePath);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/report_detail";
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
                                JSONObject dataArray = data.getJSONObject("data");
                                String latitude = dataArray.getString("latitude");
                                String longitude = dataArray.getString("longitude");
                                String idSales = dataArray.getString("idSales");
                                getSales(idSales, latitude, longitude);
                                tipeLaporan = dataArray.getString("tipeLaporan");
                                String createdAt = dataArray.getString("createdAt");
                                idPelanggan = dataArray.getString("idPelanggan");

                                if(tipeLaporan.equals("1")){
                                    titlePageTV.setText("DETAIL RENCANA");
                                    keteranganLabelTV.setText("Keterangan");
                                } else {
                                    titlePageTV.setText("DETAIL LAPORAN");
                                    keteranganLabelTV.setText("Ket. Hasil");
                                }

                                getComment(idReport);

                                String date_create = createdAt;
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt0 = null;
                                try {
                                    dt0 = format0.parse(date_create);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                @SuppressLint("SimpleDateFormat")
                                DateFormat format00 = new SimpleDateFormat("EEE");
                                String fixDay = format00.format(dt0);
                                String hariNameFix = "";

                                if (fixDay.equals("Mon") || fixDay.equals("Sen")) {
                                    hariNameFix = "Senin";
                                } else if (fixDay.equals("Tue") || fixDay.equals("Sel")) {
                                    hariNameFix = "Selasa";
                                } else if (fixDay.equals("Wed") || fixDay.equals("Rab")) {
                                    hariNameFix = "Rabu";
                                } else if (fixDay.equals("Thu") || fixDay.equals("Kam")) {
                                    hariNameFix = "Kamis";
                                } else if (fixDay.equals("Fri") || fixDay.equals("Jum")) {
                                    hariNameFix = "Jumat";
                                } else if (fixDay.equals("Sat") || fixDay.equals("Sab")) {
                                    hariNameFix = "Sabtu";
                                } else if (fixDay.equals("Sun") || fixDay.equals("Min")) {
                                    hariNameFix = "Minggu";
                                }

                                String dayDateFix = date_create.substring(8,10);
                                String yearDateFix = date_create.substring(0,4);
                                String bulanValueFix = date_create.substring(5,7);
                                String bulanNameFix;

                                switch (bulanValueFix) {
                                    case "01":
                                        bulanNameFix = "Jan";
                                        break;
                                    case "02":
                                        bulanNameFix = "Feb";
                                        break;
                                    case "03":
                                        bulanNameFix = "Mar";
                                        break;
                                    case "04":
                                        bulanNameFix = "Apr";
                                        break;
                                    case "05":
                                        bulanNameFix = "Mei";
                                        break;
                                    case "06":
                                        bulanNameFix = "Jun";
                                        break;
                                    case "07":
                                        bulanNameFix = "Jul";
                                        break;
                                    case "08":
                                        bulanNameFix = "Agu";
                                        break;
                                    case "09":
                                        bulanNameFix = "Sep";
                                        break;
                                    case "10":
                                        bulanNameFix = "Okt";
                                        break;
                                    case "11":
                                        bulanNameFix = "Nov";
                                        break;
                                    case "12":
                                        bulanNameFix = "Des";
                                        break;
                                    default:
                                        bulanNameFix = "Not found!";
                                        break;
                                }

                                tanggalBuatTV.setText(hariNameFix+", "+String.valueOf(Integer.parseInt(dayDateFix))+" "+bulanNameFix+" "+yearDateFix+" "+createdAt.substring(10,16));

                                if(idSales.equals(sharedPrefManager.getSpNik())){
                                    deletePart.setVisibility(View.VISIBLE);
                                } else {
                                    deletePart.setVisibility(View.GONE);
                                }

                                if(tipeLaporan.equals("1")){
                                    updatePart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.GONE);
                                    reportKategoriTV.setText("RENCANA KUNJUNGAN");
                                    tglRencanaPart.setVisibility(View.VISIBLE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);
                                    String tgl_rencana = dataArray.getString("tanggalRencana");
                                    String tipeRencanaLaporan = dataArray.getString("tipeRencanaLaporan");

                                    ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorAccent2));
                                    ColorStateList defaultColor = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.cbDefault));
                                    String dataAgenda = tipeRencanaLaporan.replaceAll("\\[|\\]|\"", ""); // Hapus kurung siku dan tanda kutip
                                    String[] dataArrayAgenda = dataAgenda.split(",");

                                    agendaCB1.setButtonTintList(defaultColor);
                                    agendaCB2.setButtonTintList(defaultColor);
                                    agendaCB3.setButtonTintList(defaultColor);
                                    agendaCB4.setButtonTintList(defaultColor);
                                    agendaCB5.setButtonTintList(defaultColor);
                                    agendaCB6.setButtonTintList(defaultColor);
                                    agendaCB1.setChecked(false);
                                    agendaCB2.setChecked(false);
                                    agendaCB3.setChecked(false);
                                    agendaCB4.setChecked(false);
                                    agendaCB5.setChecked(false);
                                    agendaCB6.setChecked(false);

                                    for (int x=0; x<dataArrayAgenda.length; x++){
                                        if(dataArrayAgenda[x].equals("2")){
                                            agendaCB1.setButtonTintList(colorStateList);
                                            agendaCB1.setChecked(true);
                                        } else if(dataArrayAgenda[x].equals("3")){
                                            agendaCB2.setButtonTintList(colorStateList);
                                            agendaCB2.setChecked(true);
                                        } else if(dataArrayAgenda[x].equals("4")){
                                            agendaCB3.setButtonTintList(colorStateList);
                                            agendaCB3.setChecked(true);
                                        } else if(dataArrayAgenda[x].equals("5")){
                                            agendaCB4.setButtonTintList(colorStateList);
                                            agendaCB4.setChecked(true);
                                        } else if(dataArrayAgenda[x].equals("6")){
                                            agendaCB5.setButtonTintList(colorStateList);
                                            agendaCB5.setChecked(true);
                                        } else if(dataArrayAgenda[x].equals("7")){
                                            agendaCB6.setButtonTintList(colorStateList);
                                            agendaCB6.setChecked(true);
                                        }
                                    }

                                    String input_date = tgl_rencana;
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dt1 = null;
                                    try {
                                        dt1 = format1.parse(input_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    @SuppressLint("SimpleDateFormat")
                                    DateFormat format2 = new SimpleDateFormat("EEE");
                                    String finalDay = format2.format(dt1);
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

                                    tglRencanaTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                                    String statusRealisasi = dataArray.getString("statusRealisasi");
                                    if(statusRealisasi.equals("0")){
                                        agendaLabel.setText("Rencana");
                                        if(idSales.equals(sharedPrefManager.getSpNik())){
                                            reschedulePart.setVisibility(View.VISIBLE);
                                        } else {
                                            reschedulePart.setVisibility(View.GONE);
                                        }
                                        @SuppressLint("SimpleDateFormat")
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = null;
                                        Date date2 = null;
                                        try {
                                            date = sdf.parse(tgl_rencana);
                                            date2 = sdf.parse(getDate());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        long rencana = date.getTime();
                                        long now = date2.getTime();

                                        if (rencana==now){
                                            if(idSales.equals(sharedPrefManager.getSpNik())){
                                                updateRealisasiKunjunganPart.setVisibility(View.VISIBLE);
                                            } else {
                                                updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                            }
                                        } else {
                                            updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                        }

                                        realMark.setVisibility(View.GONE);
                                        viewRealisasiPart.setVisibility(View.GONE);
                                    } else if(statusRealisasi.equals("1")){
                                        agendaLabel.setText("Realisasi");
                                        reschedulePart.setVisibility(View.GONE);
                                        updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                        realMark.setVisibility(View.VISIBLE);
                                        viewRealisasiPart.setVisibility(View.VISIBLE);
                                        String jumlah_realisasi = data.getString("jumlah_realisasi");

                                        if(jumlah_realisasi.equals("0")){
                                            viewRealisasiPart.setVisibility(View.GONE);
                                        } else {
                                            if(jumlah_realisasi.equals("1")){
                                                String realisasi = data.getString("realisasi");
                                                JSONObject dataRealisasi = new JSONObject(realisasi);
                                                String id = dataRealisasi.getString("id");
                                                String tipe = dataRealisasi.getString("tipeLaporan");
                                                viewRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                                                        intent.putExtra("report_id",id);
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                String realisasi = data.getString("realisasi");
                                                JSONArray jsonArray = new JSONArray(realisasi);
                                                int arrayLength = jsonArray.length();

                                                String arrayString = "";
                                                JSONObject dataListRealisasi = new JSONObject();
                                                for (int i = 0; i < arrayLength; i++) {
                                                    dataListRealisasi = jsonArray.getJSONObject(i);
                                                    String tipeLaporan = dataListRealisasi.getString("tipeLaporan");
                                                    String id = dataListRealisasi.getString("id");
                                                    arrayString = arrayString+tipeLaporan+"-"+id+",";
                                                }

                                                String finalArrayString = arrayString.substring(0,arrayString.length()-1);
                                                viewRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        realisasiDirect(finalArrayString);
                                                    }
                                                });

                                            }
                                        }

                                    }

                                } else if(tipeLaporan.equals("2")){
                                    if(idSales.equals(sharedPrefManager.getSpNik())){
                                        updatePart.setVisibility(View.VISIBLE);
                                    } else {
                                        updatePart.setVisibility(View.GONE);
                                    }
                                    updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("PROMOSI");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.VISIBLE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    Locale localeID = new Locale("id", "ID");
                                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                    decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                    decimalFormat.setMaximumFractionDigits(0);

                                    String totalLaporan = dataArray.getString("totalLaporan");
                                    if(totalLaporan.equals("")||totalLaporan.equals("null")||totalLaporan.equals("0")){
                                        totalPesananTV.setText("Tidak dicantumkan");
                                    } else {
                                        totalPesananTV.setText(decimalFormat.format(Integer.parseInt(totalLaporan)));
                                    }

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("3")){
                                    if(idSales.equals(sharedPrefManager.getSpNik())){
                                        updatePart.setVisibility(View.VISIBLE);
                                    } else {
                                        updatePart.setVisibility(View.GONE);
                                    }
                                    updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("PENAGIHAN");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.VISIBLE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    Locale localeID = new Locale("id", "ID");
                                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                    decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                    decimalFormat.setMaximumFractionDigits(0);

                                    String totalLaporan = dataArray.getString("totalLaporan");
                                    if(totalLaporan.equals("")||totalLaporan.equals("null")||totalLaporan.equals("0")){
                                        totalPenagihanTV.setText("Tidak dicantumkan");
                                    } else {
                                        totalPenagihanTV.setText(decimalFormat.format(Integer.parseInt(totalLaporan)));
                                    }

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("4")){
                                    if(idSales.equals(sharedPrefManager.getSpNik())){
                                        updatePart.setVisibility(View.VISIBLE);
                                    } else {
                                        updatePart.setVisibility(View.GONE);
                                    }
                                    updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("PENGIRIMAN");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.VISIBLE);

                                    String noSuratJalan = dataArray.getString("noSuratJalan");
                                    if(noSuratJalan.equals("null")||noSuratJalan.equals("")){
                                        noSuratJalanTV.setText("Tidak dicantumkan");
                                    } else {
                                        noSuratJalanTV.setText(noSuratJalan);
                                    }

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("5")){
                                    if(idSales.equals(sharedPrefManager.getSpNik())){
                                        updatePart.setVisibility(View.VISIBLE);
                                    } else {
                                        updatePart.setVisibility(View.GONE);
                                    }
                                    updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("NON JOIN VISIT");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("6")){
                                    if(idSales.equals(sharedPrefManager.getSpNik())){
                                        updatePart.setVisibility(View.VISIBLE);
                                    } else {
                                        updatePart.setVisibility(View.GONE);
                                    }
                                    updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("JOIN VISIT");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                } else if(tipeLaporan.equals("7")){
                                    if(idSales.equals(sharedPrefManager.getSpNik())){
                                        updatePart.setVisibility(View.VISIBLE);
                                    } else {
                                        updatePart.setVisibility(View.GONE);
                                    }
                                    updateRealisasiKunjunganPart.setVisibility(View.GONE);
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("PAMERAN");
                                    tglRencanaPart.setVisibility(View.GONE);
                                    totalPesananPart.setVisibility(View.GONE);
                                    totalPenagihanPart.setVisibility(View.GONE);
                                    noSuratJalanPart.setVisibility(View.GONE);

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                                            intent.putExtra("data", file);
                                            startActivity(intent);
                                        }
                                    });
                                }

                                String namaPelanggan = dataArray.getString("namaPelanggan");
                                String alamat_customer = dataArray.getString("alamat_customer");
                                String pic = dataArray.getString("pic");
                                String no_telp = dataArray.getString("noTelp");
                                String keterangan = dataArray.getString("keterangan");

                                keteranganUpdateED.setText(keterangan);
                                namaPelangganTV.setText(namaPelanggan);

                                if(alamat_customer.equals("null") || alamat_customer.equals("")){
                                    alamatPelangganTV.setText("Belum diketahui");
                                } else {
                                    alamatPelangganTV.setText(alamat_customer);
                                }

                                if(pic.equals("null")){
                                    picPelangganTV.setText("Belum diketahui");
                                } else {
                                    picPelangganTV.setText(pic);
                                }

                                if(no_telp.equals("null")){
                                    teleponPelangganTV.setText("Belum diketahui");
                                } else {
                                    teleponPelangganTV.setText(no_telp);
                                }

                                keteranganTV.setText(keterangan);

                                if(mMap != null && (tipeLaporan.equals("2") || tipeLaporan.equals("3") || tipeLaporan.equals("4") || tipeLaporan.equals("5") || tipeLaporan.equals("6") || tipeLaporan.equals("7"))){
                                    mapsPart.setVisibility(View.VISIBLE);
                                    mMap.setMyLocationEnabled(false);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                    userPoint = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    float zoomLevel = 17.8f;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                                    mMap.getUiSettings().setCompassEnabled(false);
                                    Location location = new Location("dummyProvider");
                                    location.setLatitude(Double.parseDouble(latitude));
                                    location.setLongitude(Double.parseDouble(longitude));
                                    new ReverseGeocodingTask().execute(location);
                                } else {
                                    mapsPart.setVisibility(View.GONE);
                                }

                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_report", idReport);
                params.put("from", "android");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getComment(String id_report){
        String url = "https://reporting.sumasistem.co.id/api/get_report_comment?id_report="+id_report;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String status = response.getString("status");
                            if(status.equals("Success")){
                                String jumlah = response.getString("jumlah");
                                if(Integer.parseInt(jumlah) > 0){
                                    jumlahKomenTV.setText(jumlah+" Komentar");
                                    commentRV.setVisibility(View.VISIBLE);
                                    noDataComment.setVisibility(View.GONE);
                                    String data_comment = response.getString("data");
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    reportComments = gson.fromJson(data_comment, ReportComment[].class);
                                    adapterReportComment = new AdapterReportComment(reportComments, DetailReportSumaActivity.this);
                                    commentRV.setAdapter(adapterReportComment);
                                } else {
                                    jumlahKomenTV.setText("Komentar");
                                    commentRV.setVisibility(View.GONE);
                                    noDataComment.setVisibility(View.VISIBLE);
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
            }
        });

        requestQueue.add(request);

    }

    private void getSales(String nik, String latitude, String longitude) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_sales";
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
                                String nama = data.getString("nama");
                                String NIK = data.getString("NIK");
                                namaSalesTV.setText(nama);
                                nikSalesTV.setText(NIK);
                                if(mMap != null && (tipeLaporan.equals("2") || tipeLaporan.equals("3") || tipeLaporan.equals("4"))){
                                    mMap.addMarker(new MarkerOptions().position(userPoint).title(nama).snippet(latitude+","+longitude).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_picker_ic)));
                                }
                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("nik", nik);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void sendComment(String comment) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/insert_report_comment";
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
                                getComment(idReport);
                                commentLoading.setVisibility(View.GONE);
                                commentSendBTNPart.setVisibility(View.GONE);
                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_report", idReport);
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("comment", comment);
                params.put("created_at", getTimeStamp());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                if(resultData.getString(Constants.LOCAITY)!=null){
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY));
                                locationNow = resultData.getString(Constants.LOCAITY);
                            }
                        }
                    }
                } else {
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText("Lokasi tidak ditemukan");
                                locationNow = "Lokasi tidak ditemukan";
                            }
                        }
                    }
                }
            } else {
                detailLocationTV.setText(resultData.getString(Constants.NO_ADDRESS));
                locationNow = resultData.getString(Constants.NO_ADDRESS);
            }
        }

    }

    private void fetchaddressfromlocation(Location location) {
        try {
            Context context = mContext;
            Intent intent = new Intent(context, FetchAddressIntentServices.class);
            intent.putExtra(Constants.RECEVIER, resultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
            context.startService(intent);
        }
        catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void connectionFailed(){
        CookieBar.build(DetailReportSumaActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @SuppressLint("StaticFieldLeak")
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, String> {
        @Override
        protected String doInBackground(Location... params) {
            Location location = params[0];
            String addressText = "";

            Geocoder geocoder = new Geocoder(DetailReportSumaActivity.this, Locale.getDefault());

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
                Log.e(TAG, "Error fetching address: " + e.getMessage());
            }

            return addressText;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (!address.isEmpty()) {
                Log.d(TAG, "Alamat: " + address);
                detailLocationTV.setText(address.substring(0,address.length()-2));
            } else {
                Log.e(TAG, "Alamat tidak ditemukan");
                detailLocationTV.setText("Alamat tidak ditemukan");
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ReverseGeocodingTaskRealisasi extends AsyncTask<Location, Void, String> {
        @Override
        protected String doInBackground(Location... params) {
            Location location = params[0];
            String addressText = "";

            Geocoder geocoder = new Geocoder(DetailReportSumaActivity.this, Locale.getDefault());

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
                Log.e(TAG, "Error fetching address: " + e.getMessage());
            }

            return addressText;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (!address.isEmpty()) {
                Log.d(TAG, "Alamat: " + address);
                detailLocationRealisasiTV.setText(address.substring(0,address.length()-2));
            } else {
                Log.e(TAG, "Alamat tidak ditemukan");
                detailLocationRealisasiTV.setText("Alamat tidak ditemukan");
            }
        }
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

    private void suratJalanPicker() {
        bottomSheet.showWithSheetView(LayoutInflater.from(DetailReportSumaActivity.this).inflate(R.layout.layout_report_suma_list_no_sj, bottomSheet, false));
        noSjRV = findViewById(R.id.no_sj_rv);
        noDataPartSj = findViewById(R.id.no_data_part_sj);
        loadingDataPartSj = findViewById(R.id.loading_data_part_sj);

        noSjRV.setLayoutManager(new LinearLayoutManager(this));
        noSjRV.setHasFixedSize(true);
        noSjRV.setNestedScrollingEnabled(false);
        noSjRV.setItemAnimator(new DefaultItemAnimator());

        loadingDataPartSj.setVisibility(View.VISIBLE);
        noDataPartSj.setVisibility(View.GONE);
        noSjRV.setVisibility(View.GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNoSJ(idPelanggan);
            }
        }, 800);
    }

    private void realisasiDirect(String arrayRealisasi) {
        bottomSheet.showWithSheetView(LayoutInflater.from(DetailReportSumaActivity.this).inflate(R.layout.layout_realisasi_list, bottomSheet, false));
        realKunjunganBTN = findViewById(R.id.real_kunjungan_btn);
        realPenagihanBTN = findViewById(R.id.real_penagihan_btn);
        realPengirimanBTN = findViewById(R.id.real_pengiriman_btn);
        markRealKunjungan = findViewById(R.id.mark_real_kunjungan);
        markRealPenagihan = findViewById(R.id.mark_real_penagihan);
        markRealPengiriman = findViewById(R.id.mark_real_pengiriman);

        realKunjunganBTN.setVisibility(View.GONE);
        realPenagihanBTN.setVisibility(View.GONE);
        realPengirimanBTN.setVisibility(View.GONE);
        markRealKunjungan.setVisibility(View.GONE);
        markRealPenagihan.setVisibility(View.GONE);
        markRealPengiriman.setVisibility(View.GONE);
        realKunjunganBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
        realPenagihanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
        realPengirimanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));

        String[] list = arrayRealisasi.split(",");
        String tipe, id;
        for (int i = 0; i < list.length; i++) {
            tipe = list[i].split("-")[0];
            id = list[i].split("-")[1];
            if(tipe.equals("2")){
                String finalId = id;
                realKunjunganBTN.setVisibility(View.VISIBLE);
                realKunjunganBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realKunjunganBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option_choice));
                        realPenagihanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
                        realPengirimanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
                        markRealKunjungan.setVisibility(View.VISIBLE);
                        markRealPenagihan.setVisibility(View.GONE);
                        markRealPengiriman.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                                        intent.putExtra("report_id", finalId);
                                        startActivity(intent);
                                    }
                                }, 200);
                            }
                        }, 200);
                    }
                });
            } else if(tipe.equals("3")){
                String finalId = id;
                realPenagihanBTN.setVisibility(View.VISIBLE);
                realPenagihanBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realKunjunganBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
                        realPenagihanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option_choice));
                        realPengirimanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
                        markRealKunjungan.setVisibility(View.GONE);
                        markRealPenagihan.setVisibility(View.VISIBLE);
                        markRealPengiriman.setVisibility(View.GONE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                                        intent.putExtra("report_id", finalId);
                                        startActivity(intent);
                                    }
                                }, 200);
                            }
                        }, 200);
                    }
                });
            } else if(tipe.equals("4")){
                String finalId = id;
                realPengirimanBTN.setVisibility(View.VISIBLE);
                realPengirimanBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realKunjunganBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
                        realPenagihanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option));
                        realPengirimanBTN.setBackground(ContextCompat.getDrawable(DetailReportSumaActivity.this, R.drawable.shape_option_choice));
                        markRealKunjungan.setVisibility(View.GONE);
                        markRealPenagihan.setVisibility(View.GONE);
                        markRealPengiriman.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(DetailReportSumaActivity.this, DetailReportSumaActivity.class);
                                        intent.putExtra("report_id", finalId);
                                        startActivity(intent);
                                    }
                                }, 200);
                            }
                        }, 200);
                    }
                });
            }
        }
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
                                    loadingDataPartSj.setVisibility(View.GONE);
                                    noDataPartSj.setVisibility(View.GONE);
                                    noSjRV.setVisibility(View.VISIBLE);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataNoSuratJalans = gson.fromJson(data_surat_jalan, DataNoSuratJalan[].class);
                                    adapterNoSuratJalan = new AdapterNoSuratJalanRealisasi(dataNoSuratJalans, DetailReportSumaActivity.this);
                                    noSjRV.setAdapter(adapterNoSuratJalan);
                                } else {
                                    loadingDataPartSj.setVisibility(View.GONE);
                                    noDataPartSj.setVisibility(View.VISIBLE);
                                    noSjRV.setVisibility(View.GONE);
                                }
                            } else {
                                loadingDataPartSj.setVisibility(View.GONE);
                                noDataPartSj.setVisibility(View.VISIBLE);
                                noSjRV.setVisibility(View.GONE);
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

            productInputDetailPart.setVisibility(View.VISIBLE);
            idProduct = id_product;
            productName = nama_product;
            productHargaSatuan = harga_satuan;
            qtyProductPicker.setMin(1);
            qtyProductPicker.setValue(1);
            qtyProduct = String.valueOf(qtyProductPicker.getValue());
            subTotal = String.valueOf(qtyProductPicker.getValue() * Integer.parseInt(harga_satuan));

            productChoiceTV.setText(nama_product);
            productHargaSatuanTV.setText(decimalFormat.format(Integer.parseInt(harga_satuan)));
            subTotalTV.setText(decimalFormat.format(Integer.parseInt(String.valueOf(qtyProductPicker.getValue() * Integer.parseInt(harga_satuan)))));

            InputMethodManager imm = (InputMethodManager) DetailReportSumaActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = DetailReportSumaActivity.this.getCurrentFocus();
            if (view == null) {
                view = new View(DetailReportSumaActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            keteranganKunjunganRealisasiED.clearFocus();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
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
                        Log.d(MotionEffect.TAG, "Response: " + response.toString());
                        try {
                            Log.d("Success.Response", response.toString());
                            String status = response.getString("status");

                            if (status.equals("200")) {
                                JSONArray mainData = response.getJSONArray("data");
                                if(mainData.length() > 0){
                                    loadingDataPiutang.setVisibility(View.GONE);
                                    invRV.setVisibility(View.VISIBLE);
                                    noDataPiutang.setVisibility(View.GONE);

                                    invRV.setLayoutManager(new LinearLayoutManager(DetailReportSumaActivity.this));
                                    invRV.setHasFixedSize(true);
                                    invRV.setNestedScrollingEnabled(false);
                                    invRV.setItemAnimator(new DefaultItemAnimator());

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

                                        totalTagihan = grandTotal;

                                        String jsonString = jsonArray.toString();
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.create();
                                        dataInvoicePiutangs = gson.fromJson(jsonString, DataInvoicePiutang[].class);
                                        adapterInvoicePiutang = new AdapterInvoicePiutangRealisasi(dataInvoicePiutangs, DetailReportSumaActivity.this);
                                        invRV.setAdapter(adapterInvoicePiutang);

                                        Locale localeID = new Locale("id", "ID");
                                        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
                                        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
                                        decimalFormat.setMaximumFractionDigits(0);

                                        totalInvTV.setText(decimalFormat.format(grandTotal));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    loadingDataPiutang.setVisibility(View.GONE);
                                    invRV.setVisibility(View.GONE);
                                    noDataPiutang.setVisibility(View.VISIBLE);
                                }

                            } else {
                                loadingDataPiutang.setVisibility(View.GONE);
                                invRV.setVisibility(View.GONE);
                                noDataPiutang.setVisibility(View.VISIBLE);
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
                        Log.e(MotionEffect.TAG, "Volley error: " + error.getMessage());
                        connectionFailed();
                    }
                }) {
        };

        requestQueue.add(jsonObjectRequest);

    }

    private void hitungTotalPesanan() {
        Locale localeID = new Locale("id", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
        decimalFormat.setMaximumFractionDigits(0);

        String[] array = new String[dataProductRealisasi.size()];
        array = dataProductRealisasi.toArray(array);

        int jumlah = 0;

        for (int i = 0; i < array.length; i++) {
            String[] arrayData = array[i].split("/");
            jumlah += Integer.parseInt(arrayData[4]);
        }
        totalPesanan = jumlah;
        inputTotalPesananTV.setText(decimalFormat.format(jumlah));

    }

    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()) {
            bottomSheet.dismissSheet();
        } else {
            if(updateForm.isExpanded() || rescheduleForm.isExpanded() || updateRealisasiKunjunganForm.isExpanded()){
                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.WARNING_TYPE)
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
                                updateForm.collapse();
                                rescheduleForm.collapse();
                                updateRealisasiKunjunganForm.collapse();
                                onBackPressed();
                            }
                        })
                        .show();
            } else {
                super.onBackPressed();
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

    protected void onResume() {
        super.onResume();
        if (!isFirstRun) {
            getData();
        }
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

                if(tipeLaporan.equals("1")){
                    countImageTV.setText(String.valueOf(lampiranImage.size()));
                    if(lampiranImage.size()>=3){
                        fotoLampiranRealisasiBTN.setVisibility(View.GONE);
                    } else {
                        fotoLampiranRealisasiBTN.setVisibility(View.VISIBLE);
                    }
                    viewLampiranRealisasiBTN.setVisibility(View.VISIBLE);
                    labelLampiranTV.setText("+ Lampiran Foto/SP");
                    viewLampiranRealisasiBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                            intent.putExtra("data", String.valueOf(lampiranImage));
                            startActivity(intent);
                        }
                    });
                } else {
                    countImageUpdateTV.setText(String.valueOf(lampiranImage.size()));
                    if(lampiranImage.size()>=3){
                        fotoLamBTN.setVisibility(View.GONE);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,61
                        );
                        layoutParams.setMargins(0, 0, 0, 0);
                        viewLampiranUpdateBTN.setLayoutParams(layoutParams);
                    } else {
                        fotoLamBTN.setVisibility(View.VISIBLE);
                    }
                    viewLampiranUpdateBTN.setVisibility(View.VISIBLE);
                    labelLampTV.setText("Ubah Lampiran Foto/SP");
                    viewLampiranUpdateBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageSliderActivity.class);
                            intent.putExtra("data", String.valueOf(lampiranImage));
                            startActivity(intent);
                        }
                    });
                }
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
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

    public boolean isDeveloperModeEnabled(){
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 17) {
            return android.provider.Settings.Secure.getInt(DetailReportSumaActivity.this.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        }
        return false;
    }

    private String getTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}