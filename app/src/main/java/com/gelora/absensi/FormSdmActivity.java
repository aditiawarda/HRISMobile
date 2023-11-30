package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterKaryawanBaruSDM;
import com.gelora.absensi.adapter.AdapterKaryawanBaruSDM2;
import com.gelora.absensi.adapter.AdapterKaryawanBaruSDM3;
import com.gelora.absensi.adapter.AdapterKaryawanLamaSDM;
import com.gelora.absensi.adapter.AdapterKaryawanLamaSDM2;
import com.gelora.absensi.adapter.AdapterKaryawanLamaSDM3;
import com.gelora.absensi.adapter.AdapterKomponenGaji;
import com.gelora.absensi.adapter.AdapterKomponenGaji3;
import com.gelora.absensi.adapter.AdapterUnitBagian;
import com.gelora.absensi.adapter.AdapterUnitBagian2;
import com.gelora.absensi.adapter.AdapterUnitBagianLama;
import com.gelora.absensi.adapter.AdapterUnitBisnis;
import com.gelora.absensi.adapter.AdapterUnitBisnis2;
import com.gelora.absensi.adapter.AdapterUnitBisnis2Lama;
import com.gelora.absensi.adapter.AdapterUnitBisnis3;
import com.gelora.absensi.adapter.AdapterUnitBisnis3Lama;
import com.gelora.absensi.adapter.AdapterUnitBisnis4;
import com.gelora.absensi.adapter.AdapterUnitBisnis4Lama;
import com.gelora.absensi.adapter.AdapterUnitDepartemen;
import com.gelora.absensi.adapter.AdapterUnitDepartemen2;
import com.gelora.absensi.adapter.AdapterUnitDepartemenLama;
import com.gelora.absensi.adapter.AdapterUnitJabatan;
import com.gelora.absensi.adapter.AdapterUnitJabatan2;
import com.gelora.absensi.adapter.AdapterUnitJabatanBaruDetail;
import com.gelora.absensi.adapter.AdapterUnitJabatanLama;
import com.gelora.absensi.adapter.AdapterUnitJabatanLamaDetail;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.KaryawanSDM;
import com.gelora.absensi.model.KomponenGaji;
import com.gelora.absensi.model.KomponenGaji3;
import com.gelora.absensi.model.UnitBagian;
import com.gelora.absensi.model.UnitBisnis;
import com.gelora.absensi.model.UnitDepartemen;
import com.gelora.absensi.model.UnitJabatan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormSdmActivity extends AppCompatActivity {

    LinearLayout viewBTN, backBTN, pilihKeteranganPart, actionBar, submitBTN, attantionNoForm, loadingFormPart, formPart, successPart, warningNoForm;
    LinearLayout f1Part, f2Part, f3Part, f4Part;
    LinearLayout ket1BTN, ket2BTN, ket3BTN, ket4BTN, ket5BTN, ket6BTN, ket7BTN;
    LinearLayout markKet1, markKet2, markKet3, markKet4, markKet5, markKet6, markKet7;

    //Form 1
    LinearLayout f1KomponenGajiPart, f1BagianDisableMode, f1DepartemenDisableMode, f1UnitBisnisDisableMode, f1UnitBisnisPart, f1DepartemenPart, f1BagianPart, f1JabatanPart, f1TglDibutuhkanPart, f1TglPemenuhanPart;
    TextView f1KomponenGajiPilihTV, f1BagianDisableModeTV, f1DepartemenDisableModeTV, f1UnitBisnisDisableModeTV, f1UnitBisnisTV, f1DepartemenTV, f1BagianTV, f1JabatanTV, f1TglDibutuhkanTV, f1TglPemenuhanTV;
    EditText f1DeskripsiJabatanTV, f1SyaratTV, f1CatatanTV;
    String f1IdUnitBisnis = "", f1IdDepartemen = "", f1IdBagian = "", f1IdJabatan = "", f1TglDibutuhkan = "", f1TglPemenuhan = "";
    private RecyclerView f1UnitBisnisRV;
    private UnitBisnis[] unitBisnis;
    private AdapterUnitBisnis f1AdapterUnitBisnis;
    private RecyclerView f1DepartemenRV;
    private UnitDepartemen[] unitDepartemen;
    private AdapterUnitDepartemen adapterUnitDepartemen;
    private RecyclerView f1BagianRV;
    private UnitBagian[] unitBagian;
    private AdapterUnitBagian adapterUnitBagian;
    private RecyclerView f1JabatanRV;
    private UnitJabatan[] unitJabatans;
    private AdapterUnitJabatan adapterUnitJabatan;
    private RecyclerView f1KomponenGajiRV;
    private KomponenGaji[] komponenGajis;
    private AdapterKomponenGaji adapterKomponenGaji;

    //Form 2 3 4
    LinearLayout f2OptionSubPart, f2DurasiKontrakPart, f2KomponenGajiDisableMode, f2NamaKaryawanDisableMode, f2UnitBisnisDisableMode, f2NamaKaryawanPart, f2UnitBisnisPart, f2StartAttantionKaryawanBaruPart, f2NoDataKaryawanBaruPart, f2loadingDataKaryawanBaruPart;
    LinearLayout f2StartDatePart, f2EndDatePart, f2KomponenGajiDisableModeLama, f2UnitBisnisDisableModeLama, f2NamaKaryawanLamaPart, f2UnitBisnisLamaPart, f2StartAttantionKaryawanLamaPart, f2NoDataKaryawanLamaPart, f2loadingDataKaryawanLamaPart;
    ImageView f2loadingGif, f2loadingLamaGif;
    TextView f2LabelPoint, f2KomponenGajiDisableModeTV, f2NamaKaryawanDisableModeTV, f2UnitBisnisDisableModeLamaTV, f2UnitBisnisDisableModeTV, f2NamaKaryawanTV, f2UnitBisnisTV, f2DepartemenTV, f2BagianTV, f2JabatanTV;
    TextView f2DurasiKontrak, f2StartDatePilih, f2EndDatePilih, f2KomponenGajiDisableModeLamaTV, f2NamaKaryawanLamaTV, f2UnitBisnisLamaTV, f2DepartemenLamaTV, f2BagianLamaTV, f2JabatanLamaTV;
    EditText f2keywordKaryawanBaru, f2KomponenGajiTV;
    EditText f2keywordKaryawanLama, f2KomponenGajiLamaTV, f2CatatanTV;
    String f2NikBaru = "", f2IdUnitBisnis = "", f2DepartemenBaru = "", f2BagianBaru = "", f2JabatanBaru = "";
    String f2SubKet = "", f2DateChoiceMulai = "", f2DateChoiceAkhir = "", f2NikLama = "", f2IdUnitBisnisLama = "", f2DepartemenLama = "", f2BagianLama = "", f2JabatanLama= "", f2PemenuhanSyarat = "1";
    private RecyclerView f2KaryawanBaruRV, f2KaryawanLamaRV, f2UnitBisnisRV, f2UnitBisnisLamaRV;
    private KaryawanSDM[] f2KaryawanSDMS;
    private AdapterKaryawanBaruSDM f2AdapterKaryawanBaruSDM;
    private AdapterKaryawanLamaSDM f2AdapterKaryawanLamaSDM;
    private AdapterUnitBisnis2 f2AdapterUnitBisnis;
    private AdapterUnitBisnis2Lama f2AdapterUnitBisnisLama;
    RadioGroup f2OptionSubGroup;
    RadioButton f2OptionSub1, f2OptionSub2;

    //Form 5 6
    LinearLayout f3OptionSubPart, f4UnitBisnisDisableMode, f3JabatanBaruDetailDisableMode, f3JabatanLamaDetailDisableMode, f3KomponenGajiPart, f3NamaKaryawanDisableMode, f3TglDibutuhkanPart, f3TglPemenuhanPart, f3TglPengangkatanJabatanBaruPart, f3NamaKaryawanPart, f3UnitBisnisPart, f3DepartemenPart, f3BagianPart, f3JabatanPart, f3DepartemenLamaPart, f3BagianLamaPart, f3JabatanLamaPart, f3StartAttantionKaryawanBaruPart, f3NoDataKaryawanBaruPart, f3loadingDataKaryawanBaruPart;
    LinearLayout f3KomponenGajiDisableModeLama, f3JabatanDisableModeLama, f3BagianDisableModeLama, f3DepartemenDisableModeLama, f3UnitBisnisDisableModeLama, f3JabatanBaruDetailPart, f3TglPengangkatanJabatanLamaPart, f3JabatanLamaDetailPart, f3NamaKaryawanLamaPart, f3UnitBisnisLamaPart, f3StartAttantionKaryawanLamaPart, f3NoDataKaryawanLamaPart, f3loadingDataKaryawanLamaPart;
    ImageView f3loadingGif, f3loadingLamaGif;
    EditText f3AlasanPengangkatanTV;
    TextView f3LabelPoint, f3JabatanBaruDetailDisableModeTV, f3JabatanLamaDetailDisableModeTV, f3KomponenGajiPilihTV, f3NamaKaryawanDisableModeTV, f3KomponenGajiDisableModeLamaTV, f3JabatanDisableModeLamaTV, f3TglDibutuhkanTV, f3TglPemenuhanTV, f3TglPengangkatanJabatanBaruTV, f3JabatanBaruDetailTV, f3TglPengangkatanJabatanLamaTV, f3JabatanLamaDetailTV, f3NamaKaryawanTV, f3UnitBisnisTV, f3DepartemenTV, f3BagianTV, f3JabatanTV;
    TextView f4UnitBisnisDisableModeTV, f3BagianDisableModeLamaTV, f3DepartemenDisableModeLamaTV, f3UnitBisnisDisableModeLamaTV, f3NamaKaryawanLamaTV, f3UnitBisnisLamaTV, f3DepartemenLamaTV, f3BagianLamaTV, f3JabatanLamaTV;
    EditText f3keywordKaryawanBaru, f3KomponenGajiTV;
    EditText f3keywordKaryawanLama, f3KomponenGajiLamaTV, f3CatatanTV;
    String f3JabatanBaruDetail = "", f3NikBaru = "", f3IdUnitBisnis = "", f3DepartemenBaru = "", f3BagianBaru = "", f3JabatanBaru = "";
    String f3SubKet = "", f3TglDibutuhkan = "", f3TglPemenuhan = "", f3TglPengangkatanJabatanBaru = "", f3TglPengangkatanJabatanLama = "", f3JabatanLamaDetail = "", f3NikLama = "", f3IdUnitBisnisLama = "", f3DepartemenLama = "", f3BagianLama = "", f3JabatanLama= "", f3PemenuhanSyarat = "";
    private RecyclerView f3KaryawanBaruRV, f3KaryawanLamaRV, f3UnitBisnisRV, f3UnitBisnisLamaRV;
    private KaryawanSDM[] f3KaryawanSDMS;
    private AdapterKaryawanBaruSDM2 f3AdapterKaryawanBaruSDM;
    private AdapterKaryawanLamaSDM2 f3AdapterKaryawanLamaSDM;
    private AdapterUnitBisnis3 f3AdapterUnitBisnis;
    private AdapterUnitBisnis3Lama f3AdapterUnitBisnisLama;
    private RecyclerView f3DepartemenRV;
    private UnitDepartemen[] f3UnitDepartemen;
    private AdapterUnitDepartemen2 f3AdapterUnitDepartemen;
    private RecyclerView f3BagianRV;
    private UnitBagian[] f3UnitBagian;
    private AdapterUnitBagian2 f3AdapterUnitBagian;
    private RecyclerView f3JabatanRV;
    private UnitJabatan[] f3UnitJabatans;
    private AdapterUnitJabatan2 f3AdapterUnitJabatan;
    private RecyclerView f3DepartemenLamaRV;
    private UnitDepartemen[] f3UnitDepartemenLama;
    private AdapterUnitDepartemenLama f3AdapterUnitDepartemenLama;
    private RecyclerView f3BagianLamaRV;
    private UnitBagian[] f3UnitBagianLama;
    private AdapterUnitBagianLama f3AdapterUnitBagianLama;
    private RecyclerView f3JabatanLamaRV;
    private UnitJabatan[] f3UnitJabatanLamas;
    private AdapterUnitJabatanLama f3AdapterUnitJabatanLama;
    private RecyclerView f3JabatanLamaDetailRV;
    private UnitJabatan[] f3UnitJabatanLamaDetails;
    private AdapterUnitJabatanLamaDetail f3AdapterUnitJabatanLamaDetail;
    private RecyclerView f3JabatanBaruDetailRV;
    private UnitJabatan[] f3UnitJabatanBaruDetails;
    private AdapterUnitJabatanBaruDetail f3AdapterUnitJabatanBaruDetail;
    private RecyclerView f3KomponenGajiRV;
    private KomponenGaji3[] komponenGajis3;
    private AdapterKomponenGaji3 adapterKomponenGaji3;
    RadioGroup f3OptionSubGroup;
    RadioButton f3OptionSub1, f3OptionSub2;

    //Form 7
    LinearLayout f4KomponenGajiDisableMode, f4NamaKaryawanPart, f4UnitBisnisPart, f4StartAttantionKaryawanBaruPart, f4NoDataKaryawanBaruPart, f4loadingDataKaryawanBaruPart;
    LinearLayout f4KomponenGajiDisableModeLama, f4UnitBisnisDisableModeLama, f4NamaKaryawanLamaPart, f4UnitBisnisLamaPart, f4StartAttantionKaryawanLamaPart, f4NoDataKaryawanLamaPart, f4loadingDataKaryawanLamaPart;
    ImageView f4loadingGif, f4loadingLamaGif;
    TextView f4KomponenGajiDisableModeTV, f4NamaKaryawanTV, f4UnitBisnisTV, f4DepartemenTV, f4BagianTV, f4JabatanTV;
    TextView f4KomponenGajiDisableModeLamaTV, f4UnitBisnisDisableModeLamaTV, f4NamaKaryawanLamaTV, f4UnitBisnisLamaTV, f4DepartemenLamaTV, f4BagianLamaTV, f4JabatanLamaTV;
    EditText f4keywordKaryawanBaru, f4KomponenGajiTV;
    EditText f4keywordKaryawanLama, f4KomponenGajiLamaTV, f4CatatanTV, f4LainlainTV;
    String f4NikBaru = "", f4IdUnitBisnis = "", f4DepartemenBaru = "", f4BagianBaru = "", f4JabatanBaru = "";
    String f4NikLama = "", f4IdUnitBisnisLama = "", f4DepartemenLama = "", f4BagianLama = "", f4JabatanLama= "", f4Persetujuan = "";
    private RecyclerView f4KaryawanBaruRV, f4KaryawanLamaRV, f4UnitBisnisRV, f4UnitBisnisLamaRV;
    private KaryawanSDM[] f4KaryawanSDMS;
    private AdapterKaryawanBaruSDM3 f4AdapterKaryawanBaruSDM;
    private AdapterKaryawanLamaSDM3 f4AdapterKaryawanLamaSDM;
    private AdapterUnitBisnis4 f4AdapterUnitBisnis;
    private AdapterUnitBisnis4Lama f4AdapterUnitBisnisLama;
    RadioGroup f4VerifPersetujuanGroup;
    RadioButton f4OptionYa, f4OptionTidak;

    ImageView loadingForm;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    SwipeRefreshLayout refreshLayout;
    TextView keteranganTV;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    KAlertDialog pDialog;
    ImageView successGif;
    private int i = -1;
    String kodeKeterangan = "0", perngajuanTerkirim = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sdm);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        pilihKeteranganPart = findViewById(R.id.pilih_keterangan_part);
        keteranganTV = findViewById(R.id.keterangan_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        f1Part = findViewById(R.id.form_1);
        f2Part = findViewById(R.id.form_2);
        f3Part = findViewById(R.id.form_3);
        f4Part = findViewById(R.id.form_4);
        submitBTN = findViewById(R.id.submit_btn);
        warningNoForm = findViewById(R.id.warning_no_form);
        attantionNoForm = findViewById(R.id.attantion_no_form);
        loadingFormPart = findViewById(R.id.loading_form_part);
        loadingForm = findViewById(R.id.loading_form);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        successGif = findViewById(R.id.success_gif);
        viewBTN = findViewById(R.id.view_btn);

        //Form 1
        f1UnitBisnisPart = findViewById(R.id.f1_unit_bisnis_part);
        f1UnitBisnisDisableMode = findViewById(R.id.f1_unit_bisnis_disable_mode);
        f1DepartemenDisableMode = findViewById(R.id.f1_departemen_disable_mode);
        f1BagianDisableMode = findViewById(R.id.f1_bagian_disable_mode);
        f1UnitBisnisTV = findViewById(R.id.f1_unit_bisnis_tv);
        f1UnitBisnisDisableModeTV = findViewById(R.id.f1_unit_bisnis_disable_mode_tv);
        f1DepartemenDisableModeTV = findViewById(R.id.f1_departemen_disable_mode_tv);
        f1BagianDisableModeTV = findViewById(R.id.f1_bagian_disable_mode_tv);
        f1DepartemenPart = findViewById(R.id.f1_departemen_part);
        f1DepartemenTV = findViewById(R.id.f1_departemen_tv);
        f1BagianPart = findViewById(R.id.f1_bagian_part);
        f1BagianTV = findViewById(R.id.f1_bagian_tv);
        f1JabatanPart = findViewById(R.id.f1_jabatan_part);
        f1JabatanTV = findViewById(R.id.f1_jabatan_tv);
        f1KomponenGajiPart = findViewById(R.id.f1_komponen_gaji_part);
        f1KomponenGajiPilihTV = findViewById(R.id.f1_komponen_gaji_pilih_tv);
        f1DeskripsiJabatanTV = findViewById(R.id.f1_deskripsi_jabatan_tv);
        f1SyaratTV = findViewById(R.id.f1_syarat_tv);
        f1TglDibutuhkanPart = findViewById(R.id.f1_tgl_dibutuhkan_part);
        f1TglDibutuhkanTV = findViewById(R.id.f1_tgl_dibutuhkan_tv);
        f1TglPemenuhanPart = findViewById(R.id.f1_tgl_pemenuhan_part);
        f1TglPemenuhanTV = findViewById(R.id.f1_tgl_pemenuhan_tv);
        f1CatatanTV = findViewById(R.id.f1_catatan_tv);

        f1DeskripsiJabatanTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        //Form 2 3 4
        f2OptionSubPart = findViewById(R.id.f2_option_sub_part);
        f2NamaKaryawanPart = findViewById(R.id.f2_nama_karyawan_part);
        f2NamaKaryawanDisableMode = findViewById(R.id.f2_nama_karyawan_disable_mode);
        f2NamaKaryawanDisableModeTV = findViewById(R.id.f2_nama_karyawan_disable_mode_tv);
        f2NamaKaryawanTV = findViewById(R.id.f2_nama_karyawan_tv);
        f2UnitBisnisPart = findViewById(R.id.f2_unit_bisnis_part);
        f2UnitBisnisDisableMode = findViewById(R.id.f2_unit_bisnis_disable_mode);
        f2UnitBisnisDisableModeLama = findViewById(R.id.f2_unit_bisnis_disable_mode_lama);
        f2UnitBisnisDisableModeTV = findViewById(R.id.f2_unit_bisnis_disable_mode_tv);
        f2UnitBisnisDisableModeLamaTV = findViewById(R.id.f2_unit_bisnis_disable_mode_tv_lama);
        f2UnitBisnisTV = findViewById(R.id.f2_unit_bisnis_tv);
        f2DepartemenTV = findViewById(R.id.f2_departemen_tv);
        f2BagianTV = findViewById(R.id.f2_bagian_tv);
        f2JabatanTV = findViewById(R.id.f2_jabatan_tv);
        f2KomponenGajiTV = findViewById(R.id.f2_komponen_gaji_tv);
        f2NamaKaryawanLamaPart = findViewById(R.id.f2_nama_karyawan_lama_part);
        f2NamaKaryawanLamaTV = findViewById(R.id.f2_nama_karyawan_lama_tv);
        f2UnitBisnisLamaPart = findViewById(R.id.f2_unit_bisnis_lama_part);
        f2UnitBisnisLamaTV = findViewById(R.id.f2_unit_bisnis_lama_tv);
        f2DepartemenLamaTV = findViewById(R.id.f2_departemen_lama_tv);
        f2BagianLamaTV = findViewById(R.id.f2_bagian_lama_tv);
        f2JabatanLamaTV = findViewById(R.id.f2_jabatan_lama_tv);
        f2KomponenGajiLamaTV = findViewById(R.id.f2_komponen_gaji_lama_tv);
        f2KomponenGajiDisableMode = findViewById(R.id.f2_komponen_gaji_disable_mode);
        f2KomponenGajiDisableModeLama = findViewById(R.id.f2_komponen_gaji_disable_mode_lama);
        f2KomponenGajiDisableModeTV = findViewById(R.id.f2_komponen_gaji_disable_mode_tv);
        f2KomponenGajiDisableModeLamaTV = findViewById(R.id.f2_komponen_gaji_disable_mode_tv_lama);
        f2LabelPoint = findViewById(R.id.f2_label_point);
        f2DurasiKontrakPart = findViewById(R.id.f2_durasi_kontrak_part);
        f2StartDatePart = findViewById(R.id.f2_start_date_part);
        f2EndDatePart = findViewById(R.id.f2_end_date_part);
        f2CatatanTV = findViewById(R.id.f2_catatan_tv);
        f2StartDatePilih = findViewById(R.id.f2_start_date_pilih);
        f2EndDatePilih = findViewById(R.id.f2_end_date_pilih);
        f2DurasiKontrak = findViewById(R.id.f2_durasi_kontrak);
        f2OptionSubGroup = findViewById(R.id.f2_option_sub);
        f2OptionSub1 = findViewById(R.id.f2_option_sub_1);
        f2OptionSub2 = findViewById(R.id.f2_option_sub_2);

        f2KomponenGajiTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        f2KomponenGajiLamaTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        //Form 5 6
        f3OptionSubPart = findViewById(R.id.f3_option_sub_part);
        f3NamaKaryawanPart = findViewById(R.id.f3_nama_karyawan_part);
        f3NamaKaryawanTV = findViewById(R.id.f3_nama_karyawan_tv);
        f3NamaKaryawanDisableMode = findViewById(R.id.f3_nama_karyawan_disable_mode);
        f3NamaKaryawanDisableModeTV = findViewById(R.id.f3_nama_karyawan_disable_mode_tv);
        f3UnitBisnisPart = findViewById(R.id.f3_unit_bisnis_part);
        f3UnitBisnisDisableModeLama = findViewById(R.id.f3_unit_bisnis_disable_mode_lama);
        f3UnitBisnisDisableModeLamaTV = findViewById(R.id.f3_unit_bisnis_disable_mode_tv_lama);
        f3UnitBisnisTV = findViewById(R.id.f3_unit_bisnis_tv);
        f3DepartemenPart = findViewById(R.id.f3_departemen_part);
        f3DepartemenDisableModeLama = findViewById(R.id.f3_departemen_disable_mode_lama);
        f3DepartemenDisableModeLamaTV = findViewById(R.id.f3_departemen_disable_mode_tv_lama);
        f3DepartemenTV = findViewById(R.id.f3_departemen_tv);
        f3BagianPart = findViewById(R.id.f3_bagian_part);
        f3BagianDisableModeLama = findViewById(R.id.f3_bagian_disable_mode_lama);
        f3BagianDisableModeLamaTV = findViewById(R.id.f3_bagian_disable_mode_tv_lama);
        f3BagianTV = findViewById(R.id.f3_bagian_tv);
        f3JabatanPart = findViewById(R.id.f3_jabatan_part);
        f3JabatanDisableModeLama = findViewById(R.id.f3_jabatan_disable_mode_lama);
        f3JabatanDisableModeLamaTV = findViewById(R.id.f3_jabatan_disable_mode_tv_lama);
        f3JabatanTV = findViewById(R.id.f3_jabatan_tv);
        f3KomponenGajiTV = findViewById(R.id.f3_komponen_gaji_tv);
        f3KomponenGajiPart = findViewById(R.id.f3_komponen_gaji_part);
        f3KomponenGajiPilihTV = findViewById(R.id.f3_komponen_gaji_pilih_tv);
        f3KomponenGajiDisableModeLama = findViewById(R.id.f3_komponen_gaji_disable_mode_lama);
        f3KomponenGajiDisableModeLamaTV = findViewById(R.id.f3_komponen_gaji_disable_mode_tv_lama);
        f3NamaKaryawanLamaPart = findViewById(R.id.f3_nama_karyawan_lama_part);
        f3NamaKaryawanLamaTV = findViewById(R.id.f3_nama_karyawan_lama_tv);
        f3UnitBisnisLamaPart = findViewById(R.id.f3_unit_bisnis_lama_part);
        f3UnitBisnisLamaTV = findViewById(R.id.f3_unit_bisnis_lama_tv);
        f3DepartemenLamaPart = findViewById(R.id.f3_departemen_lama_part);
        f3DepartemenLamaTV = findViewById(R.id.f3_departemen_lama_tv);
        f3BagianLamaPart = findViewById(R.id.f3_bagian_lama_part);
        f3BagianLamaTV = findViewById(R.id.f3_bagian_lama_tv);
        f3JabatanLamaPart = findViewById(R.id.f3_jabatan_lama_part);
        f3JabatanLamaTV = findViewById(R.id.f3_jabatan_lama_tv);
        f3KomponenGajiLamaTV = findViewById(R.id.f3_komponen_gaji_lama_tv);
        f3JabatanLamaDetailPart = findViewById(R.id.f3_jabatan_lama_d_part);
        f3JabatanLamaDetailDisableMode = findViewById(R.id.f3_jabatan_lama_d_disable_mode);
        f3JabatanBaruDetailDisableMode = findViewById(R.id.f3_jabatan_baru_d_disable_mode);
        f3JabatanLamaDetailDisableModeTV = findViewById(R.id.f3_jabatan_lama_d_disable_mode_tv);
        f3JabatanBaruDetailDisableModeTV = findViewById(R.id.f3_jabatan_baru_d_disable_mode_tv);
        f3JabatanLamaDetailTV = findViewById(R.id.f3_jabatan_lama_d_tv);
        f3TglPengangkatanJabatanLamaPart = findViewById(R.id.f3_tgl_pengangkatan_jabatan_lama_part);
        f3TglPengangkatanJabatanLamaTV = findViewById(R.id.f3_tgl_pengangkatan_jabatan_lama_tv);
        f3JabatanBaruDetailPart = findViewById(R.id.f3_jabatan_baru_d_part);
        f3JabatanBaruDetailTV = findViewById(R.id.f3_jabatan_baru_d_tv);
        f3TglPengangkatanJabatanBaruPart = findViewById(R.id.f3_tgl_pengangkatan_jabatan_baru_part);
        f3TglPengangkatanJabatanBaruTV = findViewById(R.id.f3_tgl_pengangkatan_jabatan_baru_tv);
        f3AlasanPengangkatanTV = findViewById(R.id.f3_alasan_pengangkatan_tv);
        f3TglDibutuhkanPart = findViewById(R.id.f3_tgl_dibutuhkan_part);
        f3TglDibutuhkanTV = findViewById(R.id.f3_tgl_dibutuhkan_tv);
        f3TglPemenuhanPart = findViewById(R.id.f3_tgl_pemenuhan_part);
        f3TglPemenuhanTV = findViewById(R.id.f3_tgl_pemenuhan_tv);
        f3CatatanTV = findViewById(R.id.f3_catatan_tv);
        f3LabelPoint = findViewById(R.id.f3_label_point);
        f3OptionSubGroup = findViewById(R.id.f3_option_sub);
        f3OptionSub1 = findViewById(R.id.f3_option_sub_1);
        f3OptionSub2 = findViewById(R.id.f3_option_sub_2);

        f3KomponenGajiTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        f3KomponenGajiLamaTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        //Form 7
        f4NamaKaryawanPart = findViewById(R.id.f4_nama_karyawan_part);
        f4NamaKaryawanTV = findViewById(R.id.f4_nama_karyawan_tv);
        f4UnitBisnisPart = findViewById(R.id.f4_unit_bisnis_part);
        f4UnitBisnisTV = findViewById(R.id.f4_unit_bisnis_tv);
        f4DepartemenTV = findViewById(R.id.f4_departemen_tv);
        f4BagianTV = findViewById(R.id.f4_bagian_tv);
        f4JabatanTV = findViewById(R.id.f4_jabatan_tv);
        f4KomponenGajiTV = findViewById(R.id.f4_komponen_gaji_tv);
        f4NamaKaryawanLamaPart = findViewById(R.id.f4_nama_karyawan_lama_part);
        f4NamaKaryawanLamaTV = findViewById(R.id.f4_nama_karyawan_lama_tv);
        f4UnitBisnisLamaPart = findViewById(R.id.f4_unit_bisnis_lama_part);
        f4UnitBisnisLamaTV = findViewById(R.id.f4_unit_bisnis_lama_tv);
        f4DepartemenLamaTV = findViewById(R.id.f4_departemen_lama_tv);
        f4BagianLamaTV = findViewById(R.id.f4_bagian_lama_tv);
        f4JabatanLamaTV = findViewById(R.id.f4_jabatan_lama_tv);
        f4KomponenGajiLamaTV = findViewById(R.id.f4_komponen_gaji_lama_tv);
        f4LainlainTV = findViewById(R.id.f4_lainlain_tv);
        f4VerifPersetujuanGroup = findViewById(R.id.f4_option);
        f4OptionYa = findViewById(R.id.f4_option_ya);
        f4OptionTidak = findViewById(R.id.f4_option_tidak);
        f4CatatanTV = findViewById(R.id.f4_catatan_tv);
        f4UnitBisnisDisableModeLama = findViewById(R.id.f4_unit_bisnis_disable_mode_lama);
        f4UnitBisnisDisableModeLamaTV = findViewById(R.id.f4_unit_bisnis_disable_mode_tv_lama);
        f4KomponenGajiDisableModeLama = findViewById(R.id.f4_komponen_gaji_disable_mode_lama);
        f4KomponenGajiDisableModeLamaTV = findViewById(R.id.f4_komponen_gaji_disable_mode_tv_lama);
        f4UnitBisnisDisableMode = findViewById(R.id.f4_unit_bisnis_disable_mode);
        f4UnitBisnisDisableModeTV = findViewById(R.id.f4_unit_bisnis_disable_mode_tv);
        f4KomponenGajiDisableMode = findViewById(R.id.f4_komponen_gaji_disable_mode);
        f4KomponenGajiDisableModeTV = findViewById(R.id.f4_komponen_gaji_disable_mode_tv);

        f4KomponenGajiTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        f4KomponenGajiLamaTV.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(loadingForm);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {

                keteranganTV.setText("Pilih keterangan...");
                kodeKeterangan = "0";
                attantionNoForm.setVisibility(View.VISIBLE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);
                f3Part.setVisibility(View.GONE);
                f4Part.setVisibility(View.GONE);

                warningNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
                f1KomponenGajiPilihTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2 3 4
                f2OptionSubPart.setVisibility(View.GONE);
                f2SubKet = "";
                f2OptionSubGroup.clearCheck();
                f2NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2KomponenGajiDisableModeTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                f2UnitBisnisDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2KomponenGajiDisableModeLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                f2UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2LabelPoint.setText("Pengunduran Diri, Penugasan Kembali, Pensiun, PHK");
                f2LabelPoint.setVisibility(View.VISIBLE);
                f2CatatanTV.setText("");
                f2StartDatePilih.setText("");
                f2EndDatePilih.setText("");
                f2DateChoiceMulai = "";
                f2DateChoiceAkhir = "";

                //Form 5 6
                f3OptionSubPart.setVisibility(View.GONE);
                f3NamaKaryawanTV.setText("");
                f3NamaKaryawanDisableModeTV.setText("");
                f3NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f3DepartemenBaru = "";
                f3DepartemenTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f3BagianBaru = "";
                f3BagianTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f3JabatanBaru = "";
                f3JabatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f3KomponenGajiTV.setText("");
                f3UnitBisnisTV.setText("");
                f3IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f3NamaKaryawanLamaTV.setText("");
                f3NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f3DepartemenLama = "";
                f3DepartemenLamaTV.setText("");
                f3DepartemenDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
                f3BagianLama = "";
                f3BagianLamaTV.setText("");
                f3BagianDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
                f3JabatanLama = "";
                f3JabatanLamaTV.setText("");
                f3JabatanDisableModeLamaTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
                f3KomponenGajiLamaTV.setText("");
                f3KomponenGajiDisableModeLamaTV.setText("");
                f3UnitBisnisLamaTV.setText("");
                f3IdUnitBisnisLama = "";
                f3UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f3JabatanLamaDetail = "";
                f3JabatanLamaDetailTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                f3JabatanBaruDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
                f3TglPengangkatanJabatanLama = "";
                f3TglPengangkatanJabatanLamaTV.setText("");
                f3JabatanBaruDetail = "";
                f3JabatanBaruDetailTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
                f3TglPengangkatanJabatanBaru = "";
                f3TglPengangkatanJabatanBaruTV.setText("");
                f3AlasanPengangkatanTV.setText("");
                f3TglDibutuhkan = "";
                f3TglDibutuhkanTV.setText("");
                f3TglPemenuhan = "";
                f3TglPemenuhanTV.setText("");
                f3CatatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
                f3KomponenGajiPilihTV.setText("");
                f3LabelPoint.setText("Untuk Promosi/Mutasi/Penyesuaian Gaji");

                //Form 7
                f4NamaKaryawanTV.setText("");
                f4NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f4DepartemenBaru = "";
                f4DepartemenTV.setText("");
                f4BagianBaru = "";
                f4BagianTV.setText("");
                f4JabatanBaru = "";
                f4JabatanTV.setText("");
                f4KomponenGajiTV.setText("");
                f4UnitBisnisTV.setText("");
                f4IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f4NamaKaryawanLamaTV.setText("");
                f4NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f4DepartemenLama = "";
                f4DepartemenLamaTV.setText("");
                f4BagianLama = "";
                f4BagianLamaTV.setText("");
                f4JabatanLama = "";
                f4JabatanLamaTV.setText("");
                f4KomponenGajiLamaTV.setText("");
                f4UnitBisnisLamaTV.setText("");
                f4IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f4LainlainTV.setText("");
                f4Persetujuan = "";
                f4VerifPersetujuanGroup.clearCheck();
                f4CatatanTV.setText("");
                f4UnitBisnisDisableModeLamaTV.setText("");
                f4KomponenGajiDisableModeLamaTV.setText("");
                f4UnitBisnisDisableModeTV.setText("");
                f4KomponenGajiDisableModeTV.setText("");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        warningNoForm.setVisibility(View.GONE);
                        loadingFormPart.setVisibility(View.GONE);
                        attantionNoForm.setVisibility(View.VISIBLE);
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

        pilihKeteranganPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keteranganChoice();
            }
        });

        //Form 1
        LocalBroadcastManager.getInstance(this).registerReceiver(f1UnitBisnisBoard, new IntentFilter("f1_unit_bisnis_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1DepartemenBoard, new IntentFilter("f1_departemen_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1BagianBoard, new IntentFilter("f1_bagian_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1JabatanBoard, new IntentFilter("f1_jabatan_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f1KomponenGajiBoard, new IntentFilter("f1_komponen_gaji_board"));
        f1UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1UnitBisnisWay();
            }
        });
        f1DepartemenPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1DepartemenWay();
            }
        });
        f1BagianPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f1IdDepartemen.equals("")){
                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih departemen terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    f1BagianWay();
                }
            }
        });
        f1JabatanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1JabatanWay();
            }
        });
        f1KomponenGajiPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1KomponenGajiWay();
            }
        });
        f1TglDibutuhkanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDibutuhkan();
            }
        });
        f1TglPemenuhanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePemenuhan();
            }
        });
        //End Form 1

        //Form 2 3 4
        LocalBroadcastManager.getInstance(this).registerReceiver(f2KaryawanBaruBroad, new IntentFilter("f2_karyawan_baru_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f2KaryawanLamaBroad, new IntentFilter("f2_karyawan_lama_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f2UnitBisnisBoard, new IntentFilter("f2_unit_bisnis_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f2UnitBisnisLamaBoard, new IntentFilter("f2_unit_bisnis_lama_board"));
        f2OptionSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                if (f2OptionSub1.isChecked()) {
                    f2SubKet = "1";
                } else if (f2OptionSub2.isChecked()) {
                    f2SubKet = "2";
                }
            }
        });
        f2NamaKaryawanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2KaryawanBaruFunc();
            }
        });
        f2UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2UnitBisnisWay();
            }
        });
        f2NamaKaryawanLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2KaryawanLamaFunc();
            }
        });
        f2UnitBisnisLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2UnitBisnisLamaWay();
            }
        });
        f2StartDatePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateMulai();
            }
        });
        f2EndDatePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateAkhir();
            }
        });
        //End Form 2 3 4

        //Form 5 6
        LocalBroadcastManager.getInstance(this).registerReceiver(f3KaryawanBaruBroad, new IntentFilter("f3_karyawan_baru_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3UnitBisnisBoard, new IntentFilter("f3_unit_bisnis_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3DepartemenBoard, new IntentFilter("f3_departemen_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3BagianBoard, new IntentFilter("f3_bagian_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3JabatanBoard, new IntentFilter("f3_jabatan_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3KaryawanLamaBroad, new IntentFilter("f3_karyawan_lama_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3UnitBisnisLamaBoard, new IntentFilter("f3_unit_bisnis_lama_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3DepartemenLamaBoard, new IntentFilter("f3_departemen_lama_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3BagianLamaBoard, new IntentFilter("f3_bagian_lama_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3JabatanLamaBoard, new IntentFilter("f3_jabatan_lama_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3JabatanLamaDetailBoard, new IntentFilter("f3_jabatan_lama_detail_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3JabatanBaruDetailBoard, new IntentFilter("f3_jabatan_baru_detail_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f3KomponenGajiBoard, new IntentFilter("f3_komponen_gaji_board"));
        f3OptionSubGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                if (f3OptionSub1.isChecked()) {
                    f3SubKet = "1";
                } else if (f3OptionSub2.isChecked()) {
                    f3SubKet = "2";
                }
            }
        });
        f3NamaKaryawanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3KaryawanBaruFunc();
            }
        });
        f3UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3UnitBisnisWay();
            }
        });
        f3DepartemenPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3DepartemenWay();
            }
        });
        f3BagianPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f3DepartemenBaru.equals("")){
                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih departemen terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    f3BagianWay();
                }
            }
        });
        f3JabatanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3JabatanWay();
            }
        });
        f3NamaKaryawanLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3KaryawanLamaFunc();
            }
        });
        f3UnitBisnisLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3UnitBisnisLamaWay();
            }
        });
        f3DepartemenLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3DepartemenLamaWay();
            }
        });
        f3BagianLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f3DepartemenLama.equals("")){
                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap pilih departemen terlebih dahulu!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    f3BagianLamaWay();
                }
            }
        });
        f3JabatanLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3JabatanLamaWay();
            }
        });
        f3KomponenGajiPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3KomponenGajiWay();
            }
        });
        f3JabatanLamaDetailPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3JabatanLamaDetailWay();
            }
        });
        f3TglPengangkatanJabatanLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar cal = Calendar.getInstance();
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                        f3TglPengangkatanJabatanLama = String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);

                        String input_date = f3TglPengangkatanJabatanLama;
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEE");
                        @SuppressLint("SimpleDateFormat") DateFormat getweek = new SimpleDateFormat("W");
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

                        String dayDate = input_date.substring(8, 10);
                        String yearDate = input_date.substring(0, 4);
                        ;
                        String bulanValue = input_date.substring(5, 7);
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

                        f3TglPengangkatanJabatanLamaTV.setText(hariName + ", " + String.valueOf(Integer.parseInt(dayDate)) + " " + bulanName + " " + yearDate);

                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                    dpd.show();
                }
                else {
                    int y = Integer.parseInt(getDateY());
                    int m = Integer.parseInt(getDateM());
                    int d = Integer.parseInt(getDateD());
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                        f3TglPengangkatanJabatanLama = String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);

                        String input_date = f3TglPengangkatanJabatanLama;
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEE");
                        @SuppressLint("SimpleDateFormat") DateFormat getweek = new SimpleDateFormat("W");
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

                        String dayDate = input_date.substring(8, 10);
                        String yearDate = input_date.substring(0, 4);
                        ;
                        String bulanValue = input_date.substring(5, 7);
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

                        f3TglPengangkatanJabatanLamaTV.setText(hariName + ", " + String.valueOf(Integer.parseInt(dayDate)) + " " + bulanName + " " + yearDate);

                    }, y, m, d);
                    dpd.show();
                }
            }
        });
        f3JabatanBaruDetailPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f3JabatanBaruDetailWay();
            }
        });
        f3TglPengangkatanJabatanBaruPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar cal = Calendar.getInstance();
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                        f3TglPengangkatanJabatanBaru = String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);

                        String input_date = f3TglPengangkatanJabatanBaru;
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEE");
                        @SuppressLint("SimpleDateFormat") DateFormat getweek = new SimpleDateFormat("W");
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

                        String dayDate = input_date.substring(8, 10);
                        String yearDate = input_date.substring(0, 4);
                        ;
                        String bulanValue = input_date.substring(5, 7);
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

                        f3TglPengangkatanJabatanBaruTV.setText(hariName + ", " + String.valueOf(Integer.parseInt(dayDate)) + " " + bulanName + " " + yearDate);

                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                    dpd.show();
                }
                else {
                    int y = Integer.parseInt(getDateY());
                    int m = Integer.parseInt(getDateM());
                    int d = Integer.parseInt(getDateD());
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                        f3TglPengangkatanJabatanBaru = String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);

                        String input_date = f3TglPengangkatanJabatanBaru;
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        @SuppressLint("SimpleDateFormat") DateFormat format2 = new SimpleDateFormat("EEE");
                        @SuppressLint("SimpleDateFormat") DateFormat getweek = new SimpleDateFormat("W");
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

                        String dayDate = input_date.substring(8, 10);
                        String yearDate = input_date.substring(0, 4);
                        ;
                        String bulanValue = input_date.substring(5, 7);
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

                        f3TglPengangkatanJabatanBaruTV.setText(hariName + ", " + String.valueOf(Integer.parseInt(dayDate)) + " " + bulanName + " " + yearDate);

                    }, y, m, d);
                    dpd.show();
                }
            }
        });
        f3TglDibutuhkanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDibutuhkan2();
            }
        });
        f3TglPemenuhanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePemenuhan2();
            }
        });
        //End Form 5 6

        //Form 7
        LocalBroadcastManager.getInstance(this).registerReceiver(f4KaryawanBaruBroad, new IntentFilter("f4_karyawan_baru_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f4KaryawanLamaBroad, new IntentFilter("f4_karyawan_lama_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f4UnitBisnisBoard, new IntentFilter("f4_unit_bisnis_board"));
        LocalBroadcastManager.getInstance(this).registerReceiver(f4UnitBisnisLamaBoard, new IntentFilter("f4_unit_bisnis_lama_board"));
        f4NamaKaryawanPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f4KaryawanBaruFunc();
            }
        });
        f4UnitBisnisPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f4UnitBisnisWay();
            }
        });
        f4NamaKaryawanLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f4KaryawanLamaFunc();
            }
        });
        f4UnitBisnisLamaPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f4UnitBisnisLamaWay();
            }
        });
        f4VerifPersetujuanGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                if (f4OptionYa.isChecked()) {
                    f4Persetujuan = "1";
                } else if (f4OptionTidak.isChecked()) {
                    f4Persetujuan = "2";
                }
            }
        });
        //End Form 7

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kodeKeterangan.equals("1")) { //Form 1
                    if (f1IdUnitBisnis.equals("") || f1IdDepartemen.equals("") || f1IdBagian.equals("") || f1IdJabatan.equals("") || f1KomponenGajiPilihTV.getText().toString().equals("") || f1DeskripsiJabatanTV.getText().toString().equals("") || f1SyaratTV.getText().toString().equals("") || f1TglDibutuhkan.equals("") || f1TglPemenuhan.equals("")) {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
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
                    } else {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Kirim data sekarang?")
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
                                        pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }

                                            public void onFinish() {
                                                i = -1;
                                                checkSignature();
                                            }
                                        }.start();

                                    }
                                })
                                .show();
                    }
                }
                else if(kodeKeterangan.equals("2")||kodeKeterangan.equals("3")||kodeKeterangan.equals("4")){ //Form 2 3 4
                    if(kodeKeterangan.equals("3")) {
                        if (f2NikBaru.equals("") || f2IdUnitBisnis.equals("") || f2KomponenGajiDisableModeTV.getText().toString().equals("") || f2NikLama.equals("") || f2IdUnitBisnisLama.equals("") || f2KomponenGajiDisableModeLamaTV.getText().toString().equals("") || f2DateChoiceMulai.equals("") || f2DateChoiceAkhir.equals("")) {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Pastikan kolom nama, unit bisnis, komponen gaji, pemenuhan syarat, durasi kontrak terisi!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim data sekarang?")
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
                                            pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }

                                                public void onFinish() {
                                                    i = -1;
                                                    checkSignature();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    } else if(kodeKeterangan.equals("4")){
                        if (f2SubKet.equals("") || f2NikBaru.equals("") || f2IdUnitBisnis.equals("") || f2KomponenGajiDisableModeTV.getText().toString().equals("") || f2NikLama.equals("") || f2IdUnitBisnisLama.equals("") || f2KomponenGajiDisableModeLamaTV.getText().toString().equals("")) {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Pastikan sub keterangan, kolom nama, unit bisnis, komponen gaji dan pemenuhan syarat terisi!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim data sekarang?")
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
                                            pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }

                                                public void onFinish() {
                                                    i = -1;
                                                    checkSignature();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    } else if(kodeKeterangan.equals("2")){
                        if (f2NikBaru.equals("") || f2IdUnitBisnis.equals("") || f2KomponenGajiDisableModeTV.getText().toString().equals("") || f2NikLama.equals("") || f2IdUnitBisnisLama.equals("") || f2KomponenGajiDisableModeLamaTV.getText().toString().equals("")) {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Pastikan kolom nama, unit bisnis, komponen gaji dan pemenuhan syarat terisi!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim data sekarang?")
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
                                            pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }

                                                public void onFinish() {
                                                    i = -1;
                                                    checkSignature();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    }
                }
                else if(kodeKeterangan.equals("5")||kodeKeterangan.equals("6")) {
                    if(kodeKeterangan.equals("5")){
                        if (f3SubKet.equals("") || f3NikBaru.equals("") || f3DepartemenBaru.equals("") || f3BagianBaru.equals("") || f3JabatanBaru.equals("") || f3IdUnitBisnis.equals("") || f3KomponenGajiPilihTV.getText().toString().equals("") || f3NikLama.equals("") || f3IdUnitBisnisLama.equals("") || f3DepartemenLama.equals("") || f3BagianLama.equals("") || f3JabatanLama.equals("") || f3KomponenGajiDisableModeLamaTV.getText().toString().equals("") || f3JabatanLamaDetail.equals("") || f3TglPengangkatanJabatanLama.equals("") || f3JabatanBaruDetail.equals("") || f3TglPengangkatanJabatanBaru.equals("") || f3AlasanPengangkatanTV.getText().toString().equals("") || f3TglDibutuhkan.equals("") || f3TglPemenuhan.equals("")) {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Pastikan sub keterangan, kolom nama, unit bisnis dan komponen gaji terisi!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim data sekarang?")
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
                                            pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }

                                                public void onFinish() {
                                                    i = -1;
                                                    checkSignature();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    } else {
                        if (f3NikBaru.equals("") || f3DepartemenBaru.equals("") || f3BagianBaru.equals("") || f3JabatanBaru.equals("") || f3IdUnitBisnis.equals("") || f3KomponenGajiPilihTV.getText().toString().equals("") || f3NikLama.equals("") || f3IdUnitBisnisLama.equals("") || f3DepartemenLama.equals("") || f3BagianLama.equals("") || f3JabatanLama.equals("") || f3KomponenGajiDisableModeLamaTV.getText().toString().equals("") || f3JabatanLamaDetail.equals("") || f3TglPengangkatanJabatanLama.equals("") || f3JabatanBaruDetail.equals("") || f3TglPengangkatanJabatanBaru.equals("") || f3AlasanPengangkatanTV.getText().toString().equals("") || f3TglDibutuhkan.equals("") || f3TglPemenuhan.equals("")) {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Pastikan kolom nama, unit bisnis dan komponen gaji terisi!")
                                    .setConfirmText("    OK    ")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Kirim data sekarang?")
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
                                            pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormSdmActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }

                                                public void onFinish() {
                                                    i = -1;
                                                    checkSignature();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    }
                }
                else if(kodeKeterangan.equals("7")){
                    if (f4NikBaru.equals("") || f4IdUnitBisnis.equals("") || f4KomponenGajiDisableModeTV.getText().toString().equals("") || f4NikLama.equals("") || f4IdUnitBisnisLama.equals("") || f4KomponenGajiDisableModeLamaTV.getText().toString().equals("") || f4LainlainTV.getText().toString().equals("") || f4Persetujuan.equals("")) {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Pastikan kolom nama, unit bisnis, komponen gaji, kolom lain-lain dan persetujuan terisi!")
                                .setConfirmText("    OK    ")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Kirim data sekarang?")
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
                                        pDialog = new KAlertDialog(FormSdmActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormSdmActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }

                                            public void onFinish() {
                                                i = -1;
                                                checkSignature();
                                            }
                                        }.start();

                                    }
                                })
                                .show();
                    }
                }
            }
        });

        loadingFormPart.setVisibility(View.VISIBLE);
        attantionNoForm.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingFormPart.setVisibility(View.GONE);
                attantionNoForm.setVisibility(View.VISIBLE);
            }
        }, 2000);

        //Form 1
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");

        //Form 2 3 4 7
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");

        //Form 5 6
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");

    }

    private void keteranganChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_keterangan_form_sdm, bottomSheet, false));
        ket1BTN = findViewById(R.id.ket_1_btn);
        ket2BTN = findViewById(R.id.ket_2_btn);
        ket3BTN = findViewById(R.id.ket_3_btn);
        ket4BTN = findViewById(R.id.ket_4_btn);
        ket5BTN = findViewById(R.id.ket_5_btn);
        ket6BTN = findViewById(R.id.ket_6_btn);
        ket7BTN = findViewById(R.id.ket_7_btn);
        markKet1 = findViewById(R.id.mark_ket_1);
        markKet2 = findViewById(R.id.mark_ket_2);
        markKet3 = findViewById(R.id.mark_ket_3);
        markKet4 = findViewById(R.id.mark_ket_4);
        markKet5 = findViewById(R.id.mark_ket_5);
        markKet6 = findViewById(R.id.mark_ket_6);
        markKet7 = findViewById(R.id.mark_ket_7);

        if (kodeKeterangan.equals("1")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.VISIBLE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("2")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.VISIBLE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("3")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.VISIBLE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("4")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.VISIBLE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("5")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.VISIBLE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("6")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.VISIBLE);
            markKet7.setVisibility(View.GONE);
        } else if (kodeKeterangan.equals("7")){
            ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
            ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
            markKet1.setVisibility(View.GONE);
            markKet2.setVisibility(View.GONE);
            markKet3.setVisibility(View.GONE);
            markKet4.setVisibility(View.GONE);
            markKet5.setVisibility(View.GONE);
            markKet6.setVisibility(View.GONE);
            markKet7.setVisibility(View.VISIBLE);
        }

        ket1BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.VISIBLE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "1";
                keteranganTV.setText("Penerimaan");

                warningNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);
                f3Part.setVisibility(View.GONE);
                f4Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
                f1KomponenGajiPilihTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2 3 4
                f2OptionSubPart.setVisibility(View.GONE);
                f2SubKet = "";
                f2OptionSubGroup.clearCheck();
                f2NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2KomponenGajiDisableModeTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                f2UnitBisnisDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2KomponenGajiDisableModeLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                f2UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2LabelPoint.setText("Pengunduran Diri, Penugasan Kembali, Pensiun, PHK");
                f2LabelPoint.setVisibility(View.VISIBLE);
                f2CatatanTV.setText("");
                f2StartDatePilih.setText("");
                f2EndDatePilih.setText("");
                f2DateChoiceMulai = "";
                f2DateChoiceAkhir = "";

                //Form 5 6
                f3OptionSubPart.setVisibility(View.GONE);
                f3NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f3NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f3DepartemenBaru = "";
                f3DepartemenTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f3BagianBaru = "";
                f3BagianTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f3JabatanBaru = "";
                f3JabatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f3KomponenGajiTV.setText("");
                f3UnitBisnisTV.setText("");
                f3IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f3NamaKaryawanLamaTV.setText("");
                f3NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f3DepartemenLama = "";
                f3DepartemenLamaTV.setText("");
                f3DepartemenDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
                f3BagianLama = "";
                f3BagianLamaTV.setText("");
                f3BagianDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
                f3JabatanLama = "";
                f3JabatanLamaTV.setText("");
                f3JabatanDisableModeLamaTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
                f3KomponenGajiLamaTV.setText("");
                f3KomponenGajiDisableModeLamaTV.setText("");
                f3UnitBisnisLamaTV.setText("");
                f3IdUnitBisnisLama = "";
                f3UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f3JabatanLamaDetail = "";
                f3JabatanLamaDetailTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                f3JabatanBaruDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
                f3TglPengangkatanJabatanLama = "";
                f3TglPengangkatanJabatanLamaTV.setText("");
                f3JabatanBaruDetail = "";
                f3JabatanBaruDetailTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
                f3TglPengangkatanJabatanBaru = "";
                f3TglPengangkatanJabatanBaruTV.setText("");
                f3AlasanPengangkatanTV.setText("");
                f3TglDibutuhkan = "";
                f3TglDibutuhkanTV.setText("");
                f3TglPemenuhan = "";
                f3TglPemenuhanTV.setText("");
                f3CatatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
                f3KomponenGajiPilihTV.setText("");
                f3LabelPoint.setText("Untuk Promosi/Mutasi/Penyesuaian Gaji");

                //Form 7
                f4NamaKaryawanTV.setText("");
                f4NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f4DepartemenBaru = "";
                f4DepartemenTV.setText("");
                f4BagianBaru = "";
                f4BagianTV.setText("");
                f4JabatanBaru = "";
                f4JabatanTV.setText("");
                f4KomponenGajiTV.setText("");
                f4UnitBisnisTV.setText("");
                f4IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f4NamaKaryawanLamaTV.setText("");
                f4NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f4DepartemenLama = "";
                f4DepartemenLamaTV.setText("");
                f4BagianLama = "";
                f4BagianLamaTV.setText("");
                f4JabatanLama = "";
                f4JabatanLamaTV.setText("");
                f4KomponenGajiLamaTV.setText("");
                f4UnitBisnisLamaTV.setText("");
                f4IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f4LainlainTV.setText("");
                f4Persetujuan = "";
                f4VerifPersetujuanGroup.clearCheck();
                f4CatatanTV.setText("");
                f4UnitBisnisDisableModeLamaTV.setText("");
                f4KomponenGajiDisableModeLamaTV.setText("");
                f4UnitBisnisDisableModeTV.setText("");
                f4KomponenGajiDisableModeTV.setText("");

                if(sharedPrefManager.getSpIdJabatan().equals("1") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25")){
                    f1UnitBisnisPart.setVisibility(View.GONE);
                    f1UnitBisnisDisableMode.setVisibility(View.VISIBLE);
                    f1DepartemenPart.setVisibility(View.GONE);
                    f1DepartemenDisableMode.setVisibility(View.VISIBLE);
                    f1BagianPart.setVisibility(View.GONE);
                    f1BagianDisableMode.setVisibility(View.VISIBLE);

                    f1UnitBisnisDisableModeTV.setText("PT. Gelora Aksara Pratama");
                    f1IdUnitBisnis = "1";
                    f1IdDepartemen = sharedPrefManager.getSpIdHeadDept();
                    f1IdBagian = sharedPrefManager.getSpIdDept();

                    getBagianDepartemen();
                } else if(sharedPrefManager.getSpIdJabatan().equals("10") || sharedPrefManager.getSpIdJabatan().equals("3")){
                    f1UnitBisnisPart.setVisibility(View.GONE);
                    f1UnitBisnisDisableMode.setVisibility(View.VISIBLE);
                    f1DepartemenPart.setVisibility(View.GONE);
                    f1DepartemenDisableMode.setVisibility(View.VISIBLE);
                    f1BagianPart.setVisibility(View.VISIBLE);
                    f1BagianDisableMode.setVisibility(View.GONE);

                    f1UnitBisnisDisableModeTV.setText("PT. Gelora Aksara Pratama");
                    f1IdUnitBisnis = "1";
                    f1IdDepartemen = sharedPrefManager.getSpIdHeadDept();

                    getBagianDepartemen();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                warningNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.VISIBLE);
                                f2Part.setVisibility(View.GONE);
                                f3Part.setVisibility(View.GONE);
                                f4Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket2BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.VISIBLE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "2";
                keteranganTV.setText("Pengangkatan");

                warningNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);
                f3Part.setVisibility(View.GONE);
                f4Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
                f1KomponenGajiPilihTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2 3 4
                f2OptionSubPart.setVisibility(View.GONE);
                f2SubKet = "";
                f2OptionSubGroup.clearCheck();
                f2NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2KomponenGajiDisableModeTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                f2UnitBisnisDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2KomponenGajiDisableModeLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                f2UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2LabelPoint.setText("Pengangkatan");
                f2LabelPoint.setVisibility(View.GONE);
                f2CatatanTV.setText("");
                f2StartDatePilih.setText("");
                f2EndDatePilih.setText("");
                f2DateChoiceMulai = "";
                f2DateChoiceAkhir = "";

                //Form 5 6
                f3NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f3NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f3DepartemenBaru = "";
                f3DepartemenTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f3BagianBaru = "";
                f3BagianTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f3JabatanBaru = "";
                f3JabatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f3KomponenGajiTV.setText("");
                f3UnitBisnisTV.setText("");
                f3IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f3NamaKaryawanLamaTV.setText("");
                f3NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f3DepartemenLama = "";
                f3DepartemenLamaTV.setText("");
                f3DepartemenDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
                f3BagianLama = "";
                f3BagianLamaTV.setText("");
                f3BagianDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
                f3JabatanLama = "";
                f3JabatanLamaTV.setText("");
                f3JabatanDisableModeLamaTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
                f3KomponenGajiLamaTV.setText("");
                f3KomponenGajiDisableModeLamaTV.setText("");
                f3UnitBisnisLamaTV.setText("");
                f3IdUnitBisnisLama = "";
                f3UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f3JabatanLamaDetail = "";
                f3JabatanLamaDetailTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                f3JabatanBaruDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
                f3TglPengangkatanJabatanLama = "";
                f3TglPengangkatanJabatanLamaTV.setText("");
                f3JabatanBaruDetail = "";
                f3JabatanBaruDetailTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
                f3TglPengangkatanJabatanBaru = "";
                f3TglPengangkatanJabatanBaruTV.setText("");
                f3AlasanPengangkatanTV.setText("");
                f3TglDibutuhkan = "";
                f3TglDibutuhkanTV.setText("");
                f3TglPemenuhan = "";
                f3TglPemenuhanTV.setText("");
                f3CatatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
                f3KomponenGajiPilihTV.setText("");
                f3LabelPoint.setText("Untuk Promosi/Mutasi/Penyesuaian Gaji");

                //Form 7
                f4NamaKaryawanTV.setText("");
                f4NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f4DepartemenBaru = "";
                f4DepartemenTV.setText("");
                f4BagianBaru = "";
                f4BagianTV.setText("");
                f4JabatanBaru = "";
                f4JabatanTV.setText("");
                f4KomponenGajiTV.setText("");
                f4UnitBisnisTV.setText("");
                f4IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f4NamaKaryawanLamaTV.setText("");
                f4NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f4DepartemenLama = "";
                f4DepartemenLamaTV.setText("");
                f4BagianLama = "";
                f4BagianLamaTV.setText("");
                f4JabatanLama = "";
                f4JabatanLamaTV.setText("");
                f4KomponenGajiLamaTV.setText("");
                f4UnitBisnisLamaTV.setText("");
                f4IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f4LainlainTV.setText("");
                f4Persetujuan = "";
                f4VerifPersetujuanGroup.clearCheck();
                f4CatatanTV.setText("");
                f4UnitBisnisDisableModeLamaTV.setText("");
                f4KomponenGajiDisableModeLamaTV.setText("");
                f4UnitBisnisDisableModeTV.setText("");
                f4KomponenGajiDisableModeTV.setText("");

                f2NamaKaryawanPart.setVisibility(View.GONE);
                f2NamaKaryawanDisableMode.setVisibility(View.VISIBLE);
                f2UnitBisnisPart.setVisibility(View.GONE);
                f2UnitBisnisDisableMode.setVisibility(View.VISIBLE);
                f2UnitBisnisLamaPart.setVisibility(View.GONE);
                f2UnitBisnisDisableModeLama.setVisibility(View.VISIBLE);
                f2KomponenGajiTV.setVisibility(View.GONE);
                f2KomponenGajiDisableMode.setVisibility(View.VISIBLE);
                f2KomponenGajiLamaTV.setVisibility(View.GONE);
                f2KomponenGajiDisableModeLama.setVisibility(View.VISIBLE);

                f2DurasiKontrakPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                warningNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.VISIBLE);
                                f3Part.setVisibility(View.GONE);
                                f4Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket3BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.VISIBLE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "3";
                keteranganTV.setText("Penugasan Kembali");

                warningNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);
                f3Part.setVisibility(View.GONE);
                f4Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
                f1KomponenGajiPilihTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2 3 4
                f2OptionSubPart.setVisibility(View.GONE);
                f2SubKet = "";
                f2OptionSubGroup.clearCheck();
                f2NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2KomponenGajiDisableModeTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                f2UnitBisnisDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2KomponenGajiDisableModeLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                f2UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2LabelPoint.setText("Penugasan Kembali");
                f2LabelPoint.setVisibility(View.VISIBLE);
                f2CatatanTV.setText("");
                f2StartDatePilih.setText("");
                f2EndDatePilih.setText("");
                f2DateChoiceMulai = "";
                f2DateChoiceAkhir = "";

                //Form 5 6
                f3OptionSubPart.setVisibility(View.GONE);
                f3NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f3NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f3DepartemenBaru = "";
                f3DepartemenTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f3BagianBaru = "";
                f3BagianTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f3JabatanBaru = "";
                f3JabatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f3KomponenGajiTV.setText("");
                f3UnitBisnisTV.setText("");
                f3IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f3NamaKaryawanLamaTV.setText("");
                f3NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f3DepartemenLama = "";
                f3DepartemenLamaTV.setText("");
                f3DepartemenDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
                f3BagianLama = "";
                f3BagianLamaTV.setText("");
                f3BagianDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
                f3JabatanLama = "";
                f3JabatanLamaTV.setText("");
                f3JabatanDisableModeLamaTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
                f3KomponenGajiLamaTV.setText("");
                f3KomponenGajiDisableModeLamaTV.setText("");
                f3UnitBisnisLamaTV.setText("");
                f3IdUnitBisnisLama = "";
                f3UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f3JabatanLamaDetail = "";
                f3JabatanLamaDetailTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                f3JabatanBaruDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
                f3TglPengangkatanJabatanLama = "";
                f3TglPengangkatanJabatanLamaTV.setText("");
                f3JabatanBaruDetail = "";
                f3JabatanBaruDetailTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
                f3TglPengangkatanJabatanBaru = "";
                f3TglPengangkatanJabatanBaruTV.setText("");
                f3AlasanPengangkatanTV.setText("");
                f3TglDibutuhkan = "";
                f3TglDibutuhkanTV.setText("");
                f3TglPemenuhan = "";
                f3TglPemenuhanTV.setText("");
                f3CatatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
                f3KomponenGajiPilihTV.setText("");
                f3LabelPoint.setText("Untuk Promosi/Mutasi/Penyesuaian Gaji");

                //Form 7
                f4NamaKaryawanTV.setText("");
                f4NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f4DepartemenBaru = "";
                f4DepartemenTV.setText("");
                f4BagianBaru = "";
                f4BagianTV.setText("");
                f4JabatanBaru = "";
                f4JabatanTV.setText("");
                f4KomponenGajiTV.setText("");
                f4UnitBisnisTV.setText("");
                f4IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f4NamaKaryawanLamaTV.setText("");
                f4NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f4DepartemenLama = "";
                f4DepartemenLamaTV.setText("");
                f4BagianLama = "";
                f4BagianLamaTV.setText("");
                f4JabatanLama = "";
                f4JabatanLamaTV.setText("");
                f4KomponenGajiLamaTV.setText("");
                f4UnitBisnisLamaTV.setText("");
                f4IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f4LainlainTV.setText("");
                f4Persetujuan = "";
                f4VerifPersetujuanGroup.clearCheck();
                f4CatatanTV.setText("");
                f4UnitBisnisDisableModeLamaTV.setText("");
                f4KomponenGajiDisableModeLamaTV.setText("");
                f4UnitBisnisDisableModeTV.setText("");
                f4KomponenGajiDisableModeTV.setText("");

                f2NamaKaryawanPart.setVisibility(View.GONE);
                f2NamaKaryawanDisableMode.setVisibility(View.VISIBLE);
                f2UnitBisnisPart.setVisibility(View.GONE);
                f2UnitBisnisDisableMode.setVisibility(View.VISIBLE);
                f2UnitBisnisLamaPart.setVisibility(View.GONE);
                f2UnitBisnisDisableModeLama.setVisibility(View.VISIBLE);
                f2KomponenGajiTV.setVisibility(View.GONE);
                f2KomponenGajiDisableMode.setVisibility(View.VISIBLE);
                f2KomponenGajiLamaTV.setVisibility(View.GONE);
                f2KomponenGajiDisableModeLama.setVisibility(View.VISIBLE);

                f2DurasiKontrakPart.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                warningNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.VISIBLE);
                                f3Part.setVisibility(View.GONE);
                                f4Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket4BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.VISIBLE);
                markKet5.setVisibility(View.GONE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "4";
                keteranganTV.setText("Pensiun/PHK");

                warningNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);
                f3Part.setVisibility(View.GONE);
                f4Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
                f1KomponenGajiPilihTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2 3 4
                f2OptionSubPart.setVisibility(View.VISIBLE);
                f2NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2KomponenGajiDisableModeTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                f2UnitBisnisDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2KomponenGajiDisableModeLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                f2UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2LabelPoint.setText("Pensiun/PHK");
                f2LabelPoint.setVisibility(View.GONE);
                f2CatatanTV.setText("");
                f2StartDatePilih.setText("");
                f2EndDatePilih.setText("");
                f2DateChoiceMulai = "";
                f2DateChoiceAkhir = "";

                //Form 5 6
                f3OptionSubPart.setVisibility(View.GONE);
                f3NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f3NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f3DepartemenBaru = "";
                f3DepartemenTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f3BagianBaru = "";
                f3BagianTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f3JabatanBaru = "";
                f3JabatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f3KomponenGajiTV.setText("");
                f3UnitBisnisTV.setText("");
                f3IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f3NamaKaryawanLamaTV.setText("");
                f3NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f3DepartemenLama = "";
                f3DepartemenLamaTV.setText("");
                f3DepartemenDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
                f3BagianLama = "";
                f3BagianLamaTV.setText("");
                f3BagianDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
                f3JabatanLama = "";
                f3JabatanLamaTV.setText("");
                f3JabatanDisableModeLamaTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
                f3KomponenGajiLamaTV.setText("");
                f3KomponenGajiDisableModeLamaTV.setText("");
                f3UnitBisnisLamaTV.setText("");
                f3IdUnitBisnisLama = "";
                f3UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f3JabatanLamaDetail = "";
                f3JabatanLamaDetailTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                f3JabatanBaruDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
                f3TglPengangkatanJabatanLama = "";
                f3TglPengangkatanJabatanLamaTV.setText("");
                f3JabatanBaruDetail = "";
                f3JabatanBaruDetailTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
                f3TglPengangkatanJabatanBaru = "";
                f3TglPengangkatanJabatanBaruTV.setText("");
                f3AlasanPengangkatanTV.setText("");
                f3TglDibutuhkan = "";
                f3TglDibutuhkanTV.setText("");
                f3TglPemenuhan = "";
                f3TglPemenuhanTV.setText("");
                f3CatatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
                f3KomponenGajiPilihTV.setText("");
                f3LabelPoint.setText("Untuk Promosi/Mutasi/Penyesuaian Gaji");

                //Form 7
                f4NamaKaryawanTV.setText("");
                f4NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f4DepartemenBaru = "";
                f4DepartemenTV.setText("");
                f4BagianBaru = "";
                f4BagianTV.setText("");
                f4JabatanBaru = "";
                f4JabatanTV.setText("");
                f4KomponenGajiTV.setText("");
                f4UnitBisnisTV.setText("");
                f4IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f4NamaKaryawanLamaTV.setText("");
                f4NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f4DepartemenLama = "";
                f4DepartemenLamaTV.setText("");
                f4BagianLama = "";
                f4BagianLamaTV.setText("");
                f4JabatanLama = "";
                f4JabatanLamaTV.setText("");
                f4KomponenGajiLamaTV.setText("");
                f4UnitBisnisLamaTV.setText("");
                f4IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f4LainlainTV.setText("");
                f4Persetujuan = "";
                f4VerifPersetujuanGroup.clearCheck();
                f4CatatanTV.setText("");
                f4UnitBisnisDisableModeLamaTV.setText("");
                f4KomponenGajiDisableModeLamaTV.setText("");
                f4UnitBisnisDisableModeTV.setText("");
                f4KomponenGajiDisableModeTV.setText("");

                f2NamaKaryawanPart.setVisibility(View.GONE);
                f2NamaKaryawanDisableMode.setVisibility(View.VISIBLE);
                f2UnitBisnisPart.setVisibility(View.GONE);
                f2UnitBisnisDisableMode.setVisibility(View.VISIBLE);
                f2UnitBisnisLamaPart.setVisibility(View.GONE);
                f2UnitBisnisDisableModeLama.setVisibility(View.VISIBLE);
                f2KomponenGajiTV.setVisibility(View.GONE);
                f2KomponenGajiDisableMode.setVisibility(View.VISIBLE);
                f2KomponenGajiLamaTV.setVisibility(View.GONE);
                f2KomponenGajiDisableModeLama.setVisibility(View.VISIBLE);

                f2DurasiKontrakPart.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                warningNoForm.setVisibility(View.GONE);
                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.VISIBLE);
                                f3Part.setVisibility(View.GONE);
                                f4Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        ket5BTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markKet1.setVisibility(View.GONE);
                markKet2.setVisibility(View.GONE);
                markKet3.setVisibility(View.GONE);
                markKet4.setVisibility(View.GONE);
                markKet5.setVisibility(View.VISIBLE);
                markKet6.setVisibility(View.GONE);
                markKet7.setVisibility(View.GONE);
                ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
                ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
                kodeKeterangan = "5";
                keteranganTV.setText("Promosi/Mutasi");

                warningNoForm.setVisibility(View.GONE);
                loadingFormPart.setVisibility(View.VISIBLE);
                attantionNoForm.setVisibility(View.GONE);
                submitBTN.setVisibility(View.GONE);
                f1Part.setVisibility(View.GONE);
                f2Part.setVisibility(View.GONE);
                f3Part.setVisibility(View.GONE);
                f4Part.setVisibility(View.GONE);

                //Form 1
                f1UnitBisnisTV.setText("");
                f1IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f1DepartemenTV.setText("");
                f1IdDepartemen = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f1BagianTV.setText("");
                f1IdBagian = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f1JabatanTV.setText("");
                f1IdJabatan = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
                f1KomponenGajiPilihTV.setText("");
                f1DeskripsiJabatanTV.setText("");
                f1SyaratTV.setText("");
                f1TglDibutuhkan = "";
                f1TglDibutuhkanTV.setText("");
                f1TglPemenuhan = "";
                f1TglPemenuhanTV.setText("");
                f1CatatanTV.setText("");

                //Form 2 3 4
                f2OptionSubPart.setVisibility(View.GONE);
                f2SubKet = "";
                f2OptionSubGroup.clearCheck();
                f2NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f2NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f2DepartemenBaru = "";
                f2DepartemenTV.setText("");
                f2BagianBaru = "";
                f2BagianTV.setText("");
                f2JabatanBaru = "";
                f2JabatanTV.setText("");
                f2KomponenGajiTV.setText("");
                f2KomponenGajiDisableModeTV.setText("");
                f2UnitBisnisTV.setText("");
                f2IdUnitBisnis = "";
                f2UnitBisnisDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f2NamaKaryawanLamaTV.setText("");
                f2NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f2DepartemenLama = "";
                f2DepartemenLamaTV.setText("");
                f2BagianLama = "";
                f2BagianLamaTV.setText("");
                f2JabatanLama = "";
                f2JabatanLamaTV.setText("");
                f2KomponenGajiLamaTV.setText("");
                f2KomponenGajiDisableModeLamaTV.setText("");
                f2UnitBisnisLamaTV.setText("");
                f2IdUnitBisnisLama = "";
                f2UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f2LabelPoint.setText("Pengunduran Diri, Penugasan Kembali, Pensiun, PHK");
                f2LabelPoint.setVisibility(View.VISIBLE);
                f2CatatanTV.setText("");
                f2StartDatePilih.setText("");
                f2EndDatePilih.setText("");
                f2DateChoiceMulai = "";
                f2DateChoiceAkhir = "";

                //Form 5 6
                f3OptionSubPart.setVisibility(View.VISIBLE);
                f3NamaKaryawanTV.setText("");
                f2NamaKaryawanDisableModeTV.setText("");
                f3NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f3DepartemenBaru = "";
                f3DepartemenTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
                f3BagianBaru = "";
                f3BagianTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
                f3JabatanBaru = "";
                f3JabatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
                f3KomponenGajiTV.setText("");
                f3UnitBisnisTV.setText("");
                f3IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f3NamaKaryawanLamaTV.setText("");
                f3NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f3DepartemenLama = "";
                f3DepartemenLamaTV.setText("");
                f3DepartemenDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
                f3BagianLama = "";
                f3BagianLamaTV.setText("");
                f3BagianDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
                f3JabatanLama = "";
                f3JabatanLamaTV.setText("");
                f3JabatanDisableModeLamaTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
                f3KomponenGajiLamaTV.setText("");
                f3KomponenGajiDisableModeLamaTV.setText("");
                f3UnitBisnisLamaTV.setText("");
                f3IdUnitBisnisLama = "";
                f3UnitBisnisDisableModeLamaTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f3JabatanLamaDetail = "";
                f3JabatanLamaDetailTV.setText("");
                f3JabatanLamaDetailDisableModeTV.setText("");
                f3JabatanBaruDetailDisableModeTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
                f3TglPengangkatanJabatanLama = "";
                f3TglPengangkatanJabatanLamaTV.setText("");
                f3JabatanBaruDetail = "";
                f3JabatanBaruDetailTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
                f3TglPengangkatanJabatanBaru = "";
                f3TglPengangkatanJabatanBaruTV.setText("");
                f3AlasanPengangkatanTV.setText("");
                f3TglDibutuhkan = "";
                f3TglDibutuhkanTV.setText("");
                f3TglPemenuhan = "";
                f3TglPemenuhanTV.setText("");
                f3CatatanTV.setText("");
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
                f3KomponenGajiPilihTV.setText("");
                f3LabelPoint.setText("Untuk Promosi/Mutasi");

                //Form 7
                f4NamaKaryawanTV.setText("");
                f4NikBaru = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
                f4DepartemenBaru = "";
                f4DepartemenTV.setText("");
                f4BagianBaru = "";
                f4BagianTV.setText("");
                f4JabatanBaru = "";
                f4JabatanTV.setText("");
                f4KomponenGajiTV.setText("");
                f4UnitBisnisTV.setText("");
                f4IdUnitBisnis = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
                f4NamaKaryawanLamaTV.setText("");
                f4NikLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
                f4DepartemenLama = "";
                f4DepartemenLamaTV.setText("");
                f4BagianLama = "";
                f4BagianLamaTV.setText("");
                f4JabatanLama = "";
                f4JabatanLamaTV.setText("");
                f4KomponenGajiLamaTV.setText("");
                f4UnitBisnisLamaTV.setText("");
                f4IdUnitBisnisLama = "";
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
                f4LainlainTV.setText("");
                f4Persetujuan = "";
                f4VerifPersetujuanGroup.clearCheck();
                f4CatatanTV.setText("");
                f4UnitBisnisDisableModeLamaTV.setText("");
                f4KomponenGajiDisableModeLamaTV.setText("");
                f4UnitBisnisDisableModeTV.setText("");
                f4KomponenGajiDisableModeTV.setText("");

                f3UnitBisnisLamaPart.setVisibility(View.GONE);
                f3UnitBisnisDisableModeLama.setVisibility(View.VISIBLE);
                f3DepartemenLamaPart.setVisibility(View.GONE);
                f3DepartemenDisableModeLama.setVisibility(View.VISIBLE);
                f3BagianLamaPart.setVisibility(View.GONE);
                f3BagianDisableModeLama.setVisibility(View.VISIBLE);
                f3JabatanLamaPart.setVisibility(View.GONE);
                f3JabatanDisableModeLama.setVisibility(View.VISIBLE);
                f3KomponenGajiLamaTV.setVisibility(View.GONE);
                f3KomponenGajiDisableModeLama.setVisibility(View.VISIBLE);
                f3NamaKaryawanPart.setVisibility(View.GONE);
                f3NamaKaryawanDisableMode.setVisibility(View.VISIBLE);
                f3KomponenGajiTV.setVisibility(View.GONE);
                f3KomponenGajiPart.setVisibility(View.VISIBLE);
                f3JabatanLamaDetailPart.setVisibility(View.GONE);
                f3JabatanLamaDetailDisableMode.setVisibility(View.VISIBLE);
                f3JabatanBaruDetailPart.setVisibility(View.GONE);
                f3JabatanBaruDetailDisableMode.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();

                        loadingFormPart.setVisibility(View.VISIBLE);
                        attantionNoForm.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                warningNoForm.setVisibility(View.GONE);

                                loadingFormPart.setVisibility(View.GONE);
                                attantionNoForm.setVisibility(View.GONE);

                                submitBTN.setVisibility(View.VISIBLE);
                                f1Part.setVisibility(View.GONE);
                                f2Part.setVisibility(View.GONE);
                                f3Part.setVisibility(View.VISIBLE);
                                f4Part.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                }, 300);
            }
        });

        //ket6BTN.setOnClickListener(new View.OnClickListener() {
        //    @SuppressLint("SetTextI18n")
        //    @Override
        //    public void onClick(View v) {
        //        markKet1.setVisibility(View.GONE);
        //        markKet2.setVisibility(View.GONE);
        //        markKet3.setVisibility(View.GONE);
        //        markKet4.setVisibility(View.GONE);
        //        markKet5.setVisibility(View.GONE);
        //        markKet6.setVisibility(View.VISIBLE);
        //        markKet7.setVisibility(View.GONE);
        //        ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
        //        ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        kodeKeterangan = "6";
        //        keteranganTV.setText("Penyesuaian Gaji");
        //
        //        warningNoForm.setVisibility(View.GONE);
        //        loadingFormPart.setVisibility(View.VISIBLE);
        //        attantionNoForm.setVisibility(View.GONE);
        //        submitBTN.setVisibility(View.GONE);
        //        f1Part.setVisibility(View.GONE);
        //        f2Part.setVisibility(View.GONE);
        //        f3Part.setVisibility(View.GONE);
        //        f4Part.setVisibility(View.GONE);
        //
        //        //Form 1
        //        f1UnitBisnisTV.setText("");
        //        f1IdUnitBisnis = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f1DepartemenTV.setText("");
        //        f1IdDepartemen = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
        //        f1BagianTV.setText("");
        //        f1IdBagian = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
        //        f1JabatanTV.setText("");
        //        f1IdJabatan = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
        //        f1KomponenGajiPilihTV.setText("");
        //        f1DeskripsiJabatanTV.setText("");
        //        f1SyaratTV.setText("");
        //        f1TglDibutuhkan = "";
        //        f1TglDibutuhkanTV.setText("");
        //        f1TglPemenuhan = "";
        //        f1TglPemenuhanTV.setText("");
        //        f1CatatanTV.setText("");
        //
        //        //Form 2 3 4
        //        f2NamaKaryawanTV.setText("");
        //        f2NamaKaryawanDisableModeTV.setText("");
        //        f2NikBaru = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        //        f2DepartemenBaru = "";
        //        f2DepartemenTV.setText("");
        //        f2BagianBaru = "";
        //        f2BagianTV.setText("");
        //        f2JabatanBaru = "";
        //        f2JabatanTV.setText("");
        //        f2KomponenGajiTV.setText("");
        //        f2KomponenGajiDisableModeTV.setText("");
        //        f2UnitBisnisTV.setText("");
        //        f2IdUnitBisnis = "";
        //        f2UnitBisnisDisableModeTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f2NamaKaryawanLamaTV.setText("");
        //        f2NikLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        //        f2DepartemenLama = "";
        //        f2DepartemenLamaTV.setText("");
        //        f2BagianLama = "";
        //        f2BagianLamaTV.setText("");
        //        f2JabatanLama = "";
        //        f2JabatanLamaTV.setText("");
        //        f2KomponenGajiLamaTV.setText("");
        //        f2KomponenGajiDisableModeLamaTV.setText("");
        //        f2UnitBisnisLamaTV.setText("");
        //        f2IdUnitBisnisLama = "";
        //        f2UnitBisnisDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
        //        f2LabelPoint.setText("Pengunduran Diri, Penugasan Kembali, Pensiun, PHK");
        //        f2LabelPoint.setVisibility(View.VISIBLE);
        //        f2CatatanTV.setText("");
        //        f2StartDatePilih.setText("");
        //        f2EndDatePilih.setText("");
        //        f2DateChoiceMulai = "";
        //        f2DateChoiceAkhir = "";
        //
        //        //Form 5 6
        //        f3NamaKaryawanTV.setText("");
        //        f2NamaKaryawanDisableModeTV.setText("");
        //        f3NikBaru = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        //        f3DepartemenBaru = "";
        //        f3DepartemenTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
        //        f3BagianBaru = "";
        //        f3BagianTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
        //        f3JabatanBaru = "";
        //        f3JabatanTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
        //        f3KomponenGajiTV.setText("");
        //        f3UnitBisnisTV.setText("");
        //        f3IdUnitBisnis = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f3NamaKaryawanLamaTV.setText("");
        //        f3NikLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        //        f3DepartemenLama = "";
        //        f3DepartemenLamaTV.setText("");
        //        f3DepartemenDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
        //        f3BagianLama = "";
        //        f3BagianLamaTV.setText("");
        //        f3BagianDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
        //        f3JabatanLama = "";
        //        f3JabatanLamaTV.setText("");
        //        f3JabatanDisableModeLamaTV.setText("");
        //        f3JabatanLamaDetailDisableModeTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
        //        f3KomponenGajiLamaTV.setText("");
        //        f3KomponenGajiDisableModeLamaTV.setText("");
        //        f3UnitBisnisLamaTV.setText("");
        //        f3IdUnitBisnisLama = "";
        //        f3UnitBisnisDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
        //        f3JabatanLamaDetail = "";
        //        f3JabatanLamaDetailTV.setText("");
        //        f3JabatanLamaDetailDisableModeTV.setText("");
        //        f3JabatanBaruDetailDisableModeTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
        //        f3TglPengangkatanJabatanLama = "";
        //        f3TglPengangkatanJabatanLamaTV.setText("");
        //        f3JabatanBaruDetail = "";
        //        f3JabatanBaruDetailTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
        //        f3TglPengangkatanJabatanBaru = "";
        //        f3TglPengangkatanJabatanBaruTV.setText("");
        //        f3AlasanPengangkatanTV.setText("");
        //        f3TglDibutuhkan = "";
        //        f3TglDibutuhkanTV.setText("");
        //        f3TglPemenuhan = "";
        //        f3TglPemenuhanTV.setText("");
        //        f3CatatanTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
        //        f3KomponenGajiPilihTV.setText("");
        //        f3LabelPoint.setText("Untuk Penyesuaian Gaji");
        //
        //        //Form 7
        //        f4NamaKaryawanTV.setText("");
        //        f4NikBaru = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        //        f4DepartemenBaru = "";
        //        f4DepartemenTV.setText("");
        //        f4BagianBaru = "";
        //        f4BagianTV.setText("");
        //        f4JabatanBaru = "";
        //        f4JabatanTV.setText("");
        //        f4KomponenGajiTV.setText("");
        //        f4UnitBisnisTV.setText("");
        //        f4IdUnitBisnis = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f4NamaKaryawanLamaTV.setText("");
        //        f4NikLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        //        f4DepartemenLama = "";
        //        f4DepartemenLamaTV.setText("");
        //        f4BagianLama = "";
        //        f4BagianLamaTV.setText("");
        //        f4JabatanLama = "";
        //        f4JabatanLamaTV.setText("");
        //        f4KomponenGajiLamaTV.setText("");
        //        f4UnitBisnisLamaTV.setText("");
        //        f4IdUnitBisnisLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
        //        f4LainlainTV.setText("");
        //        f4Persetujuan = "";
        //        f4VerifPersetujuanGroup.clearCheck();
        //        f4CatatanTV.setText("");
        //        f4UnitBisnisDisableModeLamaTV.setText("");
        //        f4KomponenGajiDisableModeLamaTV.setText("");
        //        f4UnitBisnisDisableModeTV.setText("");
        //        f4KomponenGajiDisableModeTV.setText("");
        //
        //        f3UnitBisnisLamaPart.setVisibility(View.GONE);
        //        f3UnitBisnisDisableModeLama.setVisibility(View.VISIBLE);
        //        f3DepartemenLamaPart.setVisibility(View.GONE);
        //        f3DepartemenDisableModeLama.setVisibility(View.VISIBLE);
        //        f3BagianLamaPart.setVisibility(View.GONE);
        //        f3BagianDisableModeLama.setVisibility(View.VISIBLE);
        //        f3JabatanLamaPart.setVisibility(View.GONE);
        //        f3JabatanDisableModeLama.setVisibility(View.VISIBLE);
        //        f3KomponenGajiLamaTV.setVisibility(View.GONE);
        //        f3KomponenGajiDisableModeLama.setVisibility(View.VISIBLE);
        //        f3NamaKaryawanPart.setVisibility(View.GONE);
        //        f3NamaKaryawanDisableMode.setVisibility(View.VISIBLE);
        //        f3KomponenGajiTV.setVisibility(View.GONE);
        //        f3KomponenGajiPart.setVisibility(View.VISIBLE);
        //        f3JabatanLamaDetailPart.setVisibility(View.GONE);
        //        f3JabatanLamaDetailDisableMode.setVisibility(View.VISIBLE);
        //        f3JabatanBaruDetailPart.setVisibility(View.GONE);
        //        f3JabatanBaruDetailDisableMode.setVisibility(View.VISIBLE);
        //
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                bottomSheet.dismissSheet();
        //
        //                loadingFormPart.setVisibility(View.VISIBLE);
        //                attantionNoForm.setVisibility(View.GONE);
        //                new Handler().postDelayed(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        warningNoForm.setVisibility(View.GONE);
        //
        //                        loadingFormPart.setVisibility(View.GONE);
        //                        attantionNoForm.setVisibility(View.GONE);
        //
        //                        submitBTN.setVisibility(View.VISIBLE);
        //                        f1Part.setVisibility(View.GONE);
        //                        f2Part.setVisibility(View.GONE);
        //                        f3Part.setVisibility(View.VISIBLE);
        //                        f4Part.setVisibility(View.GONE);
        //                    }
        //                }, 1500);
        //            }
        //        }, 300);
        //    }
        //});
        //
        //ket7BTN.setOnClickListener(new View.OnClickListener() {
        //    @SuppressLint("SetTextI18n")
        //    @Override
        //    public void onClick(View v) {
        //        markKet1.setVisibility(View.GONE);
        //        markKet2.setVisibility(View.GONE);
        //        markKet3.setVisibility(View.GONE);
        //        markKet4.setVisibility(View.GONE);
        //        markKet5.setVisibility(View.GONE);
        //        markKet6.setVisibility(View.GONE);
        //        markKet7.setVisibility(View.VISIBLE);
        //        ket1BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket2BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket3BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket4BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket5BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket6BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option));
        //        ket7BTN.setBackground(ContextCompat.getDrawable(FormSdmActivity.this, R.drawable.shape_option_choice));
        //        kodeKeterangan = "7";
        //        keteranganTV.setText("Lain-lain");
        //
        //        warningNoForm.setVisibility(View.GONE);
        //        loadingFormPart.setVisibility(View.VISIBLE);
        //        attantionNoForm.setVisibility(View.GONE);
        //        submitBTN.setVisibility(View.GONE);
        //        f1Part.setVisibility(View.GONE);
        //        f2Part.setVisibility(View.GONE);
        //        f3Part.setVisibility(View.GONE);
        //        f4Part.setVisibility(View.GONE);
        //
        //        //Form 1
        //        f1UnitBisnisTV.setText("");
        //        f1IdUnitBisnis = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f1DepartemenTV.setText("");
        //        f1IdDepartemen = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
        //        f1BagianTV.setText("");
        //        f1IdBagian = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
        //        f1JabatanTV.setText("");
        //        f1IdJabatan = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI, "");
        //        f1KomponenGajiPilihTV.setText("");
        //        f1DeskripsiJabatanTV.setText("");
        //        f1SyaratTV.setText("");
        //        f1TglDibutuhkan = "";
        //        f1TglDibutuhkanTV.setText("");
        //        f1TglPemenuhan = "";
        //        f1TglPemenuhanTV.setText("");
        //        f1CatatanTV.setText("");
        //
        //        //Form 2 3 4
        //        f2NamaKaryawanTV.setText("");
        //        f2NamaKaryawanDisableModeTV.setText("");
        //        f2NikBaru = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        //        f2DepartemenBaru = "";
        //        f2DepartemenTV.setText("");
        //        f2BagianBaru = "";
        //        f2BagianTV.setText("");
        //        f2JabatanBaru = "";
        //        f2JabatanTV.setText("");
        //        f2KomponenGajiTV.setText("");
        //        f2KomponenGajiDisableModeTV.setText("");
        //        f2UnitBisnisTV.setText("");
        //        f2IdUnitBisnis = "";
        //        f2UnitBisnisDisableModeTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f2NamaKaryawanLamaTV.setText("");
        //        f2NikLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        //        f2DepartemenLama = "";
        //        f2DepartemenLamaTV.setText("");
        //        f2BagianLama = "";
        //        f2BagianLamaTV.setText("");
        //        f2JabatanLama = "";
        //        f2JabatanLamaTV.setText("");
        //        f2KomponenGajiLamaTV.setText("");
        //        f2KomponenGajiDisableModeLamaTV.setText("");
        //        f2UnitBisnisLamaTV.setText("");
        //        f2IdUnitBisnisLama = "";
        //        f2UnitBisnisDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
        //        f2LabelPoint.setText("Pengunduran Diri, Penugasan Kembali, Pensiun, PHK");
        //        f2LabelPoint.setVisibility(View.VISIBLE);
        //        f2CatatanTV.setText("");
        //        f2StartDatePilih.setText("");
        //        f2EndDatePilih.setText("");
        //        f2DateChoiceMulai = "";
        //        f2DateChoiceAkhir = "";
        //
        //        //Form 5 6
        //        f3NamaKaryawanTV.setText("");
        //        f2NamaKaryawanDisableModeTV.setText("");
        //        f3NikBaru = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        //        f3DepartemenBaru = "";
        //        f3DepartemenTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN, "");
        //        f3BagianBaru = "";
        //        f3BagianTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, "");
        //        f3JabatanBaru = "";
        //        f3JabatanTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN, "");
        //        f3KomponenGajiTV.setText("");
        //        f3UnitBisnisTV.setText("");
        //        f3IdUnitBisnis = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f3NamaKaryawanLamaTV.setText("");
        //        f3NikLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        //        f3DepartemenLama = "";
        //        f3DepartemenLamaTV.setText("");
        //        f3DepartemenDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, "");
        //        f3BagianLama = "";
        //        f3BagianLamaTV.setText("");
        //        f3BagianDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN_LAMA, "");
        //        f3JabatanLama = "";
        //        f3JabatanLamaTV.setText("");
        //        f3JabatanDisableModeLamaTV.setText("");
        //        f3JabatanLamaDetailDisableModeTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA, "");
        //        f3KomponenGajiLamaTV.setText("");
        //        f3KomponenGajiDisableModeLamaTV.setText("");
        //        f3UnitBisnisLamaTV.setText("");
        //        f3IdUnitBisnisLama = "";
        //        f3UnitBisnisDisableModeLamaTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
        //        f3JabatanLamaDetail = "";
        //        f3JabatanLamaDetailTV.setText("");
        //        f3JabatanLamaDetailDisableModeTV.setText("");
        //        f3JabatanBaruDetailDisableModeTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_LAMA_DETAIL, "");
        //        f3TglPengangkatanJabatanLama = "";
        //        f3TglPengangkatanJabatanLamaTV.setText("");
        //        f3JabatanBaruDetail = "";
        //        f3JabatanBaruDetailTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_JABATAN_BARU_DETAIL, "");
        //        f3TglPengangkatanJabatanBaru = "";
        //        f3TglPengangkatanJabatanBaruTV.setText("");
        //        f3AlasanPengangkatanTV.setText("");
        //        f3TglDibutuhkan = "";
        //        f3TglDibutuhkanTV.setText("");
        //        f3TglPemenuhan = "";
        //        f3TglPemenuhanTV.setText("");
        //        f3CatatanTV.setText("");
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KOMPONEN_GAJI_3, "");
        //        f3KomponenGajiPilihTV.setText("");
        //        f3LabelPoint.setText("Untuk Promosi/Mutasi/Penyesuaian Gaji");
        //
        //        //Form 7
        //        f4NamaKaryawanTV.setText("");
        //        f4NikBaru = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, "");
        //        f4DepartemenBaru = "";
        //        f4DepartemenTV.setText("");
        //        f4BagianBaru = "";
        //        f4BagianTV.setText("");
        //        f4JabatanBaru = "";
        //        f4JabatanTV.setText("");
        //        f4KomponenGajiTV.setText("");
        //        f4UnitBisnisTV.setText("");
        //        f4IdUnitBisnis = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS, "");
        //        f4NamaKaryawanLamaTV.setText("");
        //        f4NikLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_LAMA, "");
        //        f4DepartemenLama = "";
        //        f4DepartemenLamaTV.setText("");
        //        f4BagianLama = "";
        //        f4BagianLamaTV.setText("");
        //        f4JabatanLama = "";
        //        f4JabatanLamaTV.setText("");
        //        f4KomponenGajiLamaTV.setText("");
        //        f4UnitBisnisLamaTV.setText("");
        //        f4IdUnitBisnisLama = "";
        //        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BISNIS_LAMA, "");
        //        f4LainlainTV.setText("");
        //        f4Persetujuan = "";
        //        f4VerifPersetujuanGroup.clearCheck();
        //        f4CatatanTV.setText("");
        //        f4UnitBisnisDisableModeLamaTV.setText("");
        //        f4KomponenGajiDisableModeLamaTV.setText("");
        //        f4UnitBisnisDisableModeTV.setText("");
        //        f4KomponenGajiDisableModeTV.setText("");
        //
        //        f4UnitBisnisLamaPart.setVisibility(View.GONE);
        //        f4UnitBisnisDisableModeLama.setVisibility(View.VISIBLE);
        //        f4KomponenGajiLamaTV.setVisibility(View.GONE);
        //        f4KomponenGajiDisableModeLama.setVisibility(View.VISIBLE);
        //        f4UnitBisnisPart.setVisibility(View.GONE);
        //        f4UnitBisnisDisableMode.setVisibility(View.VISIBLE);
        //        f4KomponenGajiTV.setVisibility(View.GONE);
        //        f4KomponenGajiDisableMode.setVisibility(View.VISIBLE);
        //
        //        new Handler().postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                bottomSheet.dismissSheet();
        //
        //                loadingFormPart.setVisibility(View.VISIBLE);
        //                attantionNoForm.setVisibility(View.GONE);
        //                new Handler().postDelayed(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        warningNoForm.setVisibility(View.GONE);
        //
        //                        loadingFormPart.setVisibility(View.GONE);
        //                        attantionNoForm.setVisibility(View.GONE);
        //
        //                        submitBTN.setVisibility(View.VISIBLE);
        //                        f1Part.setVisibility(View.GONE);
        //                        f2Part.setVisibility(View.GONE);
        //                        f3Part.setVisibility(View.GONE);
        //                        f4Part.setVisibility(View.VISIBLE);
        //                    }
        //                }, 1500);
        //            }
        //        }, 300);
        //    }
        //});

    }

    //Form 1
    private void f1UnitBisnisWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f1UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f1UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f1UnitBisnisRV.setHasFixedSize(true);
                f1UnitBisnisRV.setNestedScrollingEnabled(false);
                f1UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f1GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f1UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f1UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f1UnitBisnisRV.setHasFixedSize(true);
                f1UnitBisnisRV.setNestedScrollingEnabled(false);
                f1UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f1GetUnitBisnis();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetUnitBisnis() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f1AdapterUnitBisnis = new AdapterUnitBisnis(unitBisnis,FormSdmActivity.this);
                            f1UnitBisnisRV.setAdapter(f1AdapterUnitBisnis);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f1UnitBisnisBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit = intent.getStringExtra("id_unit");
            String nama_unit = intent.getStringExtra("nama_unit");
            f1IdUnitBisnis = id_unit;
            f1UnitBisnisTV.setText(nama_unit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f1DepartemenWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f1DepartemenRV = findViewById(R.id.unit_departemen_rv);

                f1DepartemenRV.setLayoutManager(new LinearLayoutManager(this));
                f1DepartemenRV.setHasFixedSize(true);
                f1DepartemenRV.setNestedScrollingEnabled(false);
                f1DepartemenRV.setItemAnimator(new DefaultItemAnimator());

                f1GetDepartemen();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f1DepartemenRV = findViewById(R.id.unit_departemen_rv);

                f1DepartemenRV.setLayoutManager(new LinearLayoutManager(this));
                f1DepartemenRV.setHasFixedSize(true);
                f1DepartemenRV.setNestedScrollingEnabled(false);
                f1DepartemenRV.setItemAnimator(new DefaultItemAnimator());

                f1GetDepartemen();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetDepartemen() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_departemen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String departemen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitDepartemen = gson.fromJson(departemen, UnitDepartemen[].class);
                            adapterUnitDepartemen = new AdapterUnitDepartemen(unitDepartemen,FormSdmActivity.this);
                            f1DepartemenRV.setAdapter(adapterUnitDepartemen);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f1DepartemenBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_departemen = intent.getStringExtra("id_departemen");
            String nama_departemen = intent.getStringExtra("nama_departemen");
            f1IdDepartemen = id_departemen;
            f1DepartemenTV.setText(nama_departemen);
            f1IdBagian = "";
            f1BagianTV.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f1BagianWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f1BagianRV = findViewById(R.id.unit_bagian_rv);

                f1BagianRV.setLayoutManager(new LinearLayoutManager(this));
                f1BagianRV.setHasFixedSize(true);
                f1BagianRV.setNestedScrollingEnabled(false);
                f1BagianRV.setItemAnimator(new DefaultItemAnimator());

                f1GetBagian();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f1BagianRV = findViewById(R.id.unit_bagian_rv);

                f1BagianRV.setLayoutManager(new LinearLayoutManager(this));
                f1BagianRV.setHasFixedSize(true);
                f1BagianRV.setNestedScrollingEnabled(false);
                f1BagianRV.setItemAnimator(new DefaultItemAnimator());

                f1GetBagian();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String bagian = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBagian = gson.fromJson(bagian, UnitBagian[].class);
                            adapterUnitBagian = new AdapterUnitBagian(unitBagian,FormSdmActivity.this);
                            f1BagianRV.setAdapter(adapterUnitBagian);

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
                params.put("id_departemen", f1IdDepartemen);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f1BagianBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_bagian = intent.getStringExtra("id_bagian");
            String nama_bagian = intent.getStringExtra("nama_bagian");
            f1IdBagian = id_bagian;
            f1BagianTV.setText(nama_bagian);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f1JabatanWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f1JabatanRV = findViewById(R.id.unit_jabatan_rv);
                f1JabatanRV.setLayoutManager(new LinearLayoutManager(this));
                f1JabatanRV.setHasFixedSize(true);
                f1JabatanRV.setNestedScrollingEnabled(false);
                f1JabatanRV.setItemAnimator(new DefaultItemAnimator());
                f1GetJabatan();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f1JabatanRV = findViewById(R.id.unit_jabatan_rv);
                f1JabatanRV.setLayoutManager(new LinearLayoutManager(this));
                f1JabatanRV.setHasFixedSize(true);
                f1JabatanRV.setNestedScrollingEnabled(false);
                f1JabatanRV.setItemAnimator(new DefaultItemAnimator());
                f1GetJabatan();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetJabatan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_jabatan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String jabatan = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitJabatans = gson.fromJson(jabatan, UnitJabatan[].class);
                            adapterUnitJabatan = new AdapterUnitJabatan(unitJabatans,FormSdmActivity.this);
                            f1JabatanRV.setAdapter(adapterUnitJabatan);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f1JabatanBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_jabatan = intent.getStringExtra("id_jabatan");
            String nama_jabatan = intent.getStringExtra("nama_jabatan");
            f1IdJabatan = id_jabatan;
            f1JabatanTV.setText(nama_jabatan);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };


    private void f1KomponenGajiWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_komponen_gaji, bottomSheet, false));
                f1KomponenGajiRV = findViewById(R.id.komponen_gaji_rv);
                f1KomponenGajiRV.setLayoutManager(new LinearLayoutManager(this));
                f1KomponenGajiRV.setHasFixedSize(true);
                f1KomponenGajiRV.setNestedScrollingEnabled(false);
                f1KomponenGajiRV.setItemAnimator(new DefaultItemAnimator());
                f1GetKomponenGaji();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_komponen_gaji, bottomSheet, false));
                f1KomponenGajiRV = findViewById(R.id.komponen_gaji_rv);
                f1KomponenGajiRV.setLayoutManager(new LinearLayoutManager(this));
                f1KomponenGajiRV.setHasFixedSize(true);
                f1KomponenGajiRV.setNestedScrollingEnabled(false);
                f1KomponenGajiRV.setItemAnimator(new DefaultItemAnimator());
                f1GetKomponenGaji();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f1GetKomponenGaji() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_komponen_gaji";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String komponen_gaji = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            komponenGajis = gson.fromJson(komponen_gaji, KomponenGaji[].class);
                            adapterKomponenGaji = new AdapterKomponenGaji(komponenGajis,FormSdmActivity.this);
                            f1KomponenGajiRV.setAdapter(adapterKomponenGaji);
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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f1KomponenGajiBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nama_komponen_gaji = intent.getStringExtra("nama_komponen_gaji");
            f1KomponenGajiPilihTV.setText(nama_komponen_gaji);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    private void f3KomponenGajiWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_komponen_gaji, bottomSheet, false));
                f3KomponenGajiRV = findViewById(R.id.komponen_gaji_rv);
                f3KomponenGajiRV.setLayoutManager(new LinearLayoutManager(this));
                f3KomponenGajiRV.setHasFixedSize(true);
                f3KomponenGajiRV.setNestedScrollingEnabled(false);
                f3KomponenGajiRV.setItemAnimator(new DefaultItemAnimator());
                f3GetKomponenGaji();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_komponen_gaji, bottomSheet, false));
                f3KomponenGajiRV = findViewById(R.id.komponen_gaji_rv);
                f3KomponenGajiRV.setLayoutManager(new LinearLayoutManager(this));
                f3KomponenGajiRV.setHasFixedSize(true);
                f3KomponenGajiRV.setNestedScrollingEnabled(false);
                f3KomponenGajiRV.setItemAnimator(new DefaultItemAnimator());
                f3GetKomponenGaji();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetKomponenGaji() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_komponen_gaji";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String komponen_gaji = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            komponenGajis3 = gson.fromJson(komponen_gaji, KomponenGaji3[].class);
                            adapterKomponenGaji3 = new AdapterKomponenGaji3(komponenGajis3,FormSdmActivity.this);
                            f3KomponenGajiRV.setAdapter(adapterKomponenGaji3);
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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3KomponenGajiBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nama_komponen_gaji = intent.getStringExtra("nama_komponen_gaji");
            f3KomponenGajiPilihTV.setText(nama_komponen_gaji);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void dateDibutuhkan(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglDibutuhkan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglDibutuhkan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglDibutuhkanTV.setText("Pilih Kembali !");
                    f1TglDibutuhkan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal dibutuhkan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglPemenuhan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglDibutuhkan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglDibutuhkanTV.setText("Pilih Kembali !");
                            f1TglDibutuhkan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal dibutuhkan tidak bisa lebih besar dari tanggal pemenuhan. Harap pilih kembali!")
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
                    else {
                        String input_date = f1TglDibutuhkan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglDibutuhkan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglDibutuhkan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglDibutuhkanTV.setText("Pilih Kembali !");
                    f1TglDibutuhkan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal dibutuhkan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglPemenuhan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglDibutuhkan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglDibutuhkanTV.setText("Pilih Kembali !");
                            f1TglDibutuhkan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal dibutuhkan tidak bisa lebih besar dari tanggal pemenuhan. Harap pilih kembali!")
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
                    else {
                        String input_date = f1TglDibutuhkan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f1TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, y,m-1,d);
            dpd.show();
        }


    }
    @SuppressLint("SimpleDateFormat")
    private void datePemenuhan(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglPemenuhan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglPemenuhan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglPemenuhanTV.setText("Pilih Kembali !");
                    f1TglPemenuhan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglDibutuhkan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglPemenuhan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglPemenuhanTV.setText("Pilih Kembali !");
                            f1TglPemenuhan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal dibutuhkan. Harap pilih kembali!")
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
                    else {
                        String input_date = f1TglPemenuhan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        }
        else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f1TglPemenuhan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f1TglPemenuhan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f1TglPemenuhanTV.setText("Pilih Kembali !");
                    f1TglPemenuhan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f1TglDibutuhkan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f1TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f1TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f1TglPemenuhan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f1TglPemenuhanTV.setText("Pilih Kembali !");
                            f1TglPemenuhan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal dibutuhkan. Harap pilih kembali!")
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
                    else {
                        String input_date = f1TglPemenuhan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f1TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, y,m-1,d);
            dpd.show();
        }
    }
    private void f1SendData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/input_formulir_sdm";
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

                            if(status.equals("Success")){
                                String id = data.getString("id_data");
                                perngajuanTerkirim = "1";
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);

                                viewBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                                        intent.putExtra("id_data",id);
                                        startActivity(intent);
                                    }
                                });

                            }  else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
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
                        // error
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("pembuat", sharedPrefManager.getSpNik());

                params.put("keterangan", kodeKeterangan);
                params.put("catatan", f1CatatanTV.getText().toString());

                params.put("unit_bisnis", f1IdUnitBisnis);
                params.put("departemen", f1IdDepartemen);
                params.put("bagian", f1IdBagian);
                params.put("jabatan", f1IdJabatan);
                params.put("komponen_gaji", f1KomponenGajiPilihTV.getText().toString());

                params.put("jabatan_penerimaan", f1JabatanTV.getText().toString());
                params.put("deskripsi_penerimaan", f1DeskripsiJabatanTV.getText().toString());
                params.put("syarat", f1SyaratTV.getText().toString());
                params.put("tgl_dibutuhkan_penerimaan", f1TglDibutuhkan);
                params.put("tgl_pemenuhan_penerimaan", f1TglPemenuhan);

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }

    @SuppressLint("SimpleDateFormat")
    private void dateDibutuhkan2(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f3TglDibutuhkan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f3TglDibutuhkan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f3TglDibutuhkanTV.setText("Pilih Kembali !");
                    f3TglDibutuhkan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal dibutuhkan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f3TglPemenuhan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f3TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f3TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f3TglDibutuhkan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f3TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f3TglDibutuhkanTV.setText("Pilih Kembali !");
                            f3TglDibutuhkan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal dibutuhkan tidak bisa lebih besar dari tanggal pemenuhan. Harap pilih kembali!")
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
                    else {
                        String input_date = f3TglDibutuhkan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f3TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f3TglDibutuhkan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f3TglDibutuhkan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f3TglDibutuhkanTV.setText("Pilih Kembali !");
                    f3TglDibutuhkan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal dibutuhkan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f3TglPemenuhan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f3TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f3TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f3TglDibutuhkan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f3TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f3TglDibutuhkanTV.setText("Pilih Kembali !");
                            f3TglDibutuhkan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal dibutuhkan tidak bisa lebih besar dari tanggal pemenuhan. Harap pilih kembali!")
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
                    else {
                        String input_date = f3TglDibutuhkan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f3TglDibutuhkanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, y,m-1,d);
            dpd.show();
        }


    }
    @SuppressLint("SimpleDateFormat")
    private void datePemenuhan2(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar cal = Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f3TglPemenuhan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f3TglPemenuhan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f3TglPemenuhanTV.setText("Pilih Kembali !");
                    f3TglPemenuhan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f3TglDibutuhkan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f3TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f3TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f3TglPemenuhan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f3TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f3TglPemenuhanTV.setText("Pilih Kembali !");
                            f3TglPemenuhan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal dibutuhkan. Harap pilih kembali!")
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
                    else {
                        String input_date = f3TglPemenuhan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f3TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dpd.show();
        }
        else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f3TglPemenuhan = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date date2 = null;
                try {
                    date = sdf.parse(String.valueOf(f3TglPemenuhan));
                    date2 = sdf.parse(getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long pilih = date.getTime();
                long sekarang = date2.getTime();

                if (pilih<sekarang){
                    f3TglPemenuhanTV.setText("Pilih Kembali !");
                    f3TglPemenuhan = "";

                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal sekarang. Harap pilih kembali!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (!f3TglDibutuhkan.equals("")) {
                        SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_2 = null;
                        Date date2_2 = null;
                        try {
                            date_2 = sdf.parse(String.valueOf(f3TglDibutuhkan));
                            date2_2 = sdf.parse(String.valueOf(f3TglPemenuhan));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long dibutuhkan = date_2.getTime();
                        long pemenuhan = date2_2.getTime();

                        if (dibutuhkan<=pemenuhan){
                            String input_date = f3TglPemenuhan;
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                            f3TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        }
                        else {
                            f3TglPemenuhanTV.setText("Pilih Kembali !");
                            f3TglPemenuhan = "";

                            new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Tanggal pemenuhan tidak bisa lebih kecil dari tanggal dibutuhkan. Harap pilih kembali!")
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
                    else {
                        String input_date = f3TglPemenuhan;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
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

                        f3TglPemenuhanTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                    }
                }

            }, y,m-1,d);
            dpd.show();
        }
    }
    //Form 1 End

    //Form 2 3 4
    private void f2KaryawanBaruFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f2keywordKaryawanBaru = findViewById(R.id.keyword_user_ed);
        f2keywordKaryawanBaru.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        f2KaryawanBaruRV = findViewById(R.id.karyawan_rv);
        f2StartAttantionKaryawanBaruPart = findViewById(R.id.attantion_data_part);
        f2NoDataKaryawanBaruPart = findViewById(R.id.no_data_part);
        f2loadingDataKaryawanBaruPart = findViewById(R.id.loading_data_part);
        f2loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f2loadingGif);

        f2KaryawanBaruRV.setLayoutManager(new LinearLayoutManager(this));
        f2KaryawanBaruRV.setHasFixedSize(true);
        f2KaryawanBaruRV.setNestedScrollingEnabled(false);
        f2KaryawanBaruRV.setItemAnimator(new DefaultItemAnimator());

        f2keywordKaryawanBaru.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f2keywordKaryawanBaru.getText().toString();

                f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                f2loadingDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                f2NoDataKaryawanBaruPart.setVisibility(View.GONE);
                f2KaryawanBaruRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f2GetDataKaryawanBaru(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f2keywordKaryawanBaru.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f2keywordKaryawanBaru.getText().toString();
                    f2GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f2GetDataKaryawanBaru(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {
                                f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f2NoDataKaryawanBaruPart.setVisibility(View.GONE);
                                f2KaryawanBaruRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f2KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f2AdapterKaryawanBaruSDM = new AdapterKaryawanBaruSDM(f2KaryawanSDMS, FormSdmActivity.this);
                                f2KaryawanBaruRV.setAdapter(f2AdapterKaryawanBaruSDM);
                            } else {
                                f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f2NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                                f2KaryawanBaruRV.setVisibility(View.GONE);
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

                        f2StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                        f2loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                        f2NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                        f2KaryawanBaruRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f2KaryawanBaruBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_baru = intent.getStringExtra("nik_karyawan_baru");
            String nama_karyawan_baru = intent.getStringExtra("nama_karyawan_baru");
            String departemen_karyawan_baru = intent.getStringExtra("departemen_karyawan_baru");
            String id_departemen_karyawan_baru = intent.getStringExtra("id_departemen_karyawan_baru");
            String bagian_karyawan_baru = intent.getStringExtra("bagian_karyawan_baru");
            String id_bagian_karyawan_baru = intent.getStringExtra("id_bagian_karyawan_baru");
            String jabatan_karyawan_baru = intent.getStringExtra("jabatan_karyawan_baru");
            String id_jabatan_karyawan_baru = intent.getStringExtra("id_jabatan_karyawan_baru");

            f2UnitBisnisDisableModeTV.setText("PT. Gelora Aksara Pratama");
            f2IdUnitBisnis = "1";

            f2NikBaru = nik_karyawan_baru;
            f2NamaKaryawanTV.setText(nama_karyawan_baru);
            f2DepartemenBaru = id_departemen_karyawan_baru;
            f2DepartemenTV.setText(departemen_karyawan_baru);
            f2BagianBaru = id_bagian_karyawan_baru;
            f2BagianTV.setText(bagian_karyawan_baru);
            f2JabatanBaru = id_jabatan_karyawan_baru;
            f2JabatanTV.setText(jabatan_karyawan_baru);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f2UnitBisnisWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisRV.setHasFixedSize(true);
                f2UnitBisnisRV.setNestedScrollingEnabled(false);
                f2UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisRV.setHasFixedSize(true);
                f2UnitBisnisRV.setNestedScrollingEnabled(false);
                f2UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnis();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f2GetUnitBisnis() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f2AdapterUnitBisnis = new AdapterUnitBisnis2(unitBisnis,FormSdmActivity.this);
                            f2UnitBisnisRV.setAdapter(f2AdapterUnitBisnis);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f2UnitBisnisBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit = intent.getStringExtra("id_unit");
            String nama_unit = intent.getStringExtra("nama_unit");
            f2IdUnitBisnis = id_unit;
            f2UnitBisnisTV.setText(nama_unit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f2KaryawanLamaFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f2keywordKaryawanLama = findViewById(R.id.keyword_user_ed);
        f2keywordKaryawanLama.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        f2KaryawanLamaRV = findViewById(R.id.karyawan_rv);
        f2StartAttantionKaryawanLamaPart = findViewById(R.id.attantion_data_part);
        f2NoDataKaryawanLamaPart = findViewById(R.id.no_data_part);
        f2loadingDataKaryawanLamaPart = findViewById(R.id.loading_data_part);
        f2loadingLamaGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f2loadingLamaGif);

        f2KaryawanLamaRV.setLayoutManager(new LinearLayoutManager(this));
        f2KaryawanLamaRV.setHasFixedSize(true);
        f2KaryawanLamaRV.setNestedScrollingEnabled(false);
        f2KaryawanLamaRV.setItemAnimator(new DefaultItemAnimator());

        f2keywordKaryawanLama.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f2keywordKaryawanLama.getText().toString();

                f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                f2loadingDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                f2NoDataKaryawanLamaPart.setVisibility(View.GONE);
                f2KaryawanLamaRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f2GetDataKaryawanLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f2keywordKaryawanLama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f2keywordKaryawanLama.getText().toString();
                    f2GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f2GetDataKaryawanLama(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f2NoDataKaryawanLamaPart.setVisibility(View.GONE);
                                f2KaryawanLamaRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f2KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f2AdapterKaryawanLamaSDM = new AdapterKaryawanLamaSDM(f2KaryawanSDMS, FormSdmActivity.this);
                                f2KaryawanLamaRV.setAdapter(f2AdapterKaryawanLamaSDM);
                            } else {
                                f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f2loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f2NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                                f2KaryawanLamaRV.setVisibility(View.GONE);
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

                        f2StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                        f2loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                        f2NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                        f2KaryawanLamaRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f2KaryawanLamaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_lama = intent.getStringExtra("nik_karyawan_lama");
            String nama_karyawan_lama = intent.getStringExtra("nama_karyawan_lama");
            String departemen_karyawan_lama = intent.getStringExtra("departemen_karyawan_lama");
            String id_departemen_karyawan_lama = intent.getStringExtra("id_departemen_karyawan_lama");
            String bagian_karyawan_lama = intent.getStringExtra("bagian_karyawan_lama");
            String id_bagian_karyawan_lama = intent.getStringExtra("id_bagian_karyawan_lama");
            String jabatan_karyawan_lama = intent.getStringExtra("jabatan_karyawan_lama");
            String id_jabatan_karyawan_lama = intent.getStringExtra("id_jabatan_karyawan_lama");
            String status_karyawan_lama = intent.getStringExtra("status_karyawan_lama");

            //Lama

            f2UnitBisnisDisableModeLamaTV.setText("PT. Gelora Aksara Pratama");
            f2IdUnitBisnisLama = "1";

            f2NikLama = nik_karyawan_lama;
            f2NamaKaryawanLamaTV.setText(nama_karyawan_lama);
            f2DepartemenLama = id_departemen_karyawan_lama;
            f2DepartemenLamaTV.setText(departemen_karyawan_lama);
            f2BagianLama = id_bagian_karyawan_lama;
            f2BagianLamaTV.setText(bagian_karyawan_lama);
            f2JabatanLama = id_jabatan_karyawan_lama;
            f2JabatanLamaTV.setText(jabatan_karyawan_lama);
            f2KomponenGajiDisableModeLamaTV.setText(status_karyawan_lama);

            //Baru

            f2NikBaru = nik_karyawan_lama;
            f2NamaKaryawanDisableModeTV.setText(nama_karyawan_lama);

            f2UnitBisnisDisableModeTV.setText("PT. Gelora Aksara Pratama");
            f2IdUnitBisnis = "1";

            f2NamaKaryawanTV.setText(nama_karyawan_lama);
            f2DepartemenBaru = id_departemen_karyawan_lama;
            f2DepartemenTV.setText(departemen_karyawan_lama);
            f2BagianBaru = id_bagian_karyawan_lama;
            f2BagianTV.setText(bagian_karyawan_lama);
            f2JabatanBaru = id_jabatan_karyawan_lama;
            f2JabatanTV.setText(jabatan_karyawan_lama);

            if(kodeKeterangan.equals("2")){
                if(status_karyawan_lama.equals("Harian")){
                    f2KomponenGajiDisableModeTV.setText("Kontrak");
                } else if(status_karyawan_lama.equals("Kontrak")){
                    f2KomponenGajiDisableModeTV.setText("Tetap");
                } else if(status_karyawan_lama.equals("Percobaan")){
                    f2KomponenGajiDisableModeTV.setText("Tetap");
                } else if(status_karyawan_lama.equals("OJT")){
                    f2KomponenGajiDisableModeTV.setText("Percobaan");
                } else {
                    f2KomponenGajiDisableModeTV.setText(status_karyawan_lama);
                }
            } else if(kodeKeterangan.equals("3")||kodeKeterangan.equals("4")){
                f2KomponenGajiDisableModeTV.setText(status_karyawan_lama);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f2UnitBisnisLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisLamaRV.setHasFixedSize(true);
                f2UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f2UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnisLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f2UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f2UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f2UnitBisnisLamaRV.setHasFixedSize(true);
                f2UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f2UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f2GetUnitBisnisLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f2GetUnitBisnisLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f2AdapterUnitBisnisLama = new AdapterUnitBisnis2Lama(unitBisnis,FormSdmActivity.this);
                            f2UnitBisnisLamaRV.setAdapter(f2AdapterUnitBisnisLama);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f2UnitBisnisLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit_lama = intent.getStringExtra("id_unit_lama");
            String nama_unit_lama = intent.getStringExtra("nama_unit_lama");
            f2IdUnitBisnisLama = id_unit_lama;
            f2UnitBisnisLamaTV.setText(nama_unit_lama);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f2SendData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/input_formulir_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String id = data.getString("id_data");
                                perngajuanTerkirim = "1";
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);

                                viewBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                                        intent.putExtra("id_data",id);
                                        startActivity(intent);
                                    }
                                });

                            }  else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
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
                        // error
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("pembuat", sharedPrefManager.getSpNik());
                params.put("keterangan", kodeKeterangan);

                //BARU
                params.put("nik_baru", f2NikBaru);
                params.put("unit_bisnis_baru", f2IdUnitBisnis);
                params.put("departemen_baru", f2DepartemenBaru);
                params.put("bagian_baru", f2BagianBaru);
                params.put("jabatan_baru", f2JabatanBaru);
                params.put("komponen_gaji_baru", f2KomponenGajiDisableModeTV.getText().toString());

                //LAMA
                params.put("nik_lama", f2NikLama);
                params.put("unit_bisnis_lama", f2IdUnitBisnisLama);
                params.put("departemen_lama", f2DepartemenLama);
                params.put("bagian_lama", f2BagianLama);
                params.put("jabatan_lama", f2JabatanLama);
                params.put("komponen_gaji_lama", f2KomponenGajiDisableModeLamaTV.getText().toString());

                params.put("memenuhi_syarat", f2PemenuhanSyarat);

                if(kodeKeterangan.equals("3")){
                    params.put("catatan", "Durasi Kontrak : "+f2DurasiKontrak.getText().toString()+" ("+f2StartDatePilih.getText().toString()+" s.d. "+f2EndDatePilih.getText().toString()+") "+f2CatatanTV.getText().toString());
                } else if(kodeKeterangan.equals("4")){
                    if(f2SubKet.equals("1")){
                        params.put("catatan", "PENSIUN"+" - "+f2CatatanTV.getText().toString());
                    } else if(f2SubKet.equals("2")){
                        params.put("catatan", "PEMUTUSAN HUBUNGAN KERJA (PHK)"+" - "+f2CatatanTV.getText().toString());
                    }
                } else {
                    params.put("catatan", f2CatatanTV.getText().toString());
                }

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }
    //Form 2 3 4 End

    //Form 5 6
    private void f3KaryawanBaruFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f3keywordKaryawanBaru = findViewById(R.id.keyword_user_ed);
        f3keywordKaryawanBaru.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        f3KaryawanBaruRV = findViewById(R.id.karyawan_rv);
        f3StartAttantionKaryawanBaruPart = findViewById(R.id.attantion_data_part);
        f3NoDataKaryawanBaruPart = findViewById(R.id.no_data_part);
        f3loadingDataKaryawanBaruPart = findViewById(R.id.loading_data_part);
        f3loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f3loadingGif);

        f3KaryawanBaruRV.setLayoutManager(new LinearLayoutManager(this));
        f3KaryawanBaruRV.setHasFixedSize(true);
        f3KaryawanBaruRV.setNestedScrollingEnabled(false);
        f3KaryawanBaruRV.setItemAnimator(new DefaultItemAnimator());

        f3keywordKaryawanBaru.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f3keywordKaryawanBaru.getText().toString();

                f3StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                f3loadingDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                f3NoDataKaryawanBaruPart.setVisibility(View.GONE);
                f3KaryawanBaruRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f3GetDataKaryawanBaru(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f3keywordKaryawanBaru.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f3keywordKaryawanBaru.getText().toString();
                    f3GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f3GetDataKaryawanBaru(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                f3StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f3loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f3NoDataKaryawanBaruPart.setVisibility(View.GONE);
                                f3KaryawanBaruRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f3KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f3AdapterKaryawanBaruSDM = new AdapterKaryawanBaruSDM2(f3KaryawanSDMS, FormSdmActivity.this);
                                f3KaryawanBaruRV.setAdapter(f3AdapterKaryawanBaruSDM);
                            } else {
                                f3StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f3loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f3NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                                f3KaryawanBaruRV.setVisibility(View.GONE);
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

                        f3StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                        f3loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                        f3NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                        f3KaryawanBaruRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3KaryawanBaruBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_baru = intent.getStringExtra("nik_karyawan_baru");
            String nama_karyawan_baru = intent.getStringExtra("nama_karyawan_baru");
            String departemen_karyawan_baru = intent.getStringExtra("departemen_karyawan_baru");
            String id_departemen_karyawan_baru = intent.getStringExtra("id_departemen_karyawan_baru");
            String bagian_karyawan_baru = intent.getStringExtra("bagian_karyawan_baru");
            String id_bagian_karyawan_baru = intent.getStringExtra("id_bagian_karyawan_baru");
            String jabatan_karyawan_baru = intent.getStringExtra("jabatan_karyawan_baru");
            String id_jabatan_karyawan_baru = intent.getStringExtra("id_jabatan_karyawan_baru");

            f3NikBaru = nik_karyawan_baru;
            f3NamaKaryawanTV.setText(nama_karyawan_baru);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f3UnitBisnisWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f3UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f3UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f3UnitBisnisRV.setHasFixedSize(true);
                f3UnitBisnisRV.setNestedScrollingEnabled(false);
                f3UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f3GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f3UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f3UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f3UnitBisnisRV.setHasFixedSize(true);
                f3UnitBisnisRV.setNestedScrollingEnabled(false);
                f3UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f3GetUnitBisnis();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetUnitBisnis() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f3AdapterUnitBisnis = new AdapterUnitBisnis3(unitBisnis,FormSdmActivity.this);
                            f3UnitBisnisRV.setAdapter(f3AdapterUnitBisnis);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3UnitBisnisBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit = intent.getStringExtra("id_unit");
            String nama_unit = intent.getStringExtra("nama_unit");
            f3IdUnitBisnis = id_unit;
            f3UnitBisnisTV.setText(nama_unit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3DepartemenWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f3DepartemenRV = findViewById(R.id.unit_departemen_rv);

                f3DepartemenRV.setLayoutManager(new LinearLayoutManager(this));
                f3DepartemenRV.setHasFixedSize(true);
                f3DepartemenRV.setNestedScrollingEnabled(false);
                f3DepartemenRV.setItemAnimator(new DefaultItemAnimator());

                f3GetDepartemen();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f3DepartemenRV = findViewById(R.id.unit_departemen_rv);

                f3DepartemenRV.setLayoutManager(new LinearLayoutManager(this));
                f3DepartemenRV.setHasFixedSize(true);
                f3DepartemenRV.setNestedScrollingEnabled(false);
                f3DepartemenRV.setItemAnimator(new DefaultItemAnimator());

                f3GetDepartemen();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetDepartemen() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_departemen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String departemen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitDepartemen = gson.fromJson(departemen, UnitDepartemen[].class);
                            f3AdapterUnitDepartemen = new AdapterUnitDepartemen2(f3UnitDepartemen,FormSdmActivity.this);
                            f3DepartemenRV.setAdapter(f3AdapterUnitDepartemen);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3DepartemenBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_departemen = intent.getStringExtra("id_departemen");
            String nama_departemen = intent.getStringExtra("nama_departemen");
            f3DepartemenBaru = id_departemen;
            f3DepartemenTV.setText(nama_departemen);
            f3BagianBaru = "";
            f3BagianTV.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3BagianWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f3BagianRV = findViewById(R.id.unit_bagian_rv);

                f3BagianRV.setLayoutManager(new LinearLayoutManager(this));
                f3BagianRV.setHasFixedSize(true);
                f3BagianRV.setNestedScrollingEnabled(false);
                f3BagianRV.setItemAnimator(new DefaultItemAnimator());

                f3GetBagian();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f3BagianRV = findViewById(R.id.unit_bagian_rv);

                f3BagianRV.setLayoutManager(new LinearLayoutManager(this));
                f3BagianRV.setHasFixedSize(true);
                f3BagianRV.setNestedScrollingEnabled(false);
                f3BagianRV.setItemAnimator(new DefaultItemAnimator());

                f3GetBagian();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetBagian() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String bagian = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitBagian = gson.fromJson(bagian, UnitBagian[].class);
                            f3AdapterUnitBagian = new AdapterUnitBagian2(f3UnitBagian,FormSdmActivity.this);
                            f3BagianRV.setAdapter(f3AdapterUnitBagian);

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
                params.put("id_departemen", f3DepartemenBaru);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3BagianBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_bagian = intent.getStringExtra("id_bagian");
            String nama_bagian = intent.getStringExtra("nama_bagian");
            f3BagianBaru = id_bagian;
            f3BagianTV.setText(nama_bagian);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3JabatanWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanRV.setHasFixedSize(true);
                f3JabatanRV.setNestedScrollingEnabled(false);
                f3JabatanRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatan();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanRV.setHasFixedSize(true);
                f3JabatanRV.setNestedScrollingEnabled(false);
                f3JabatanRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatan();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetJabatan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_jabatan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String jabatan = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitJabatans = gson.fromJson(jabatan, UnitJabatan[].class);
                            f3AdapterUnitJabatan = new AdapterUnitJabatan2(f3UnitJabatans,FormSdmActivity.this);
                            f3JabatanRV.setAdapter(f3AdapterUnitJabatan);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3JabatanBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_jabatan = intent.getStringExtra("id_jabatan");
            String nama_jabatan = intent.getStringExtra("nama_jabatan");
            f3JabatanBaru = id_jabatan;
            f3JabatanTV.setText(nama_jabatan);

            f3JabatanBaruDetailDisableModeTV.setText(nama_jabatan);
            f3JabatanBaruDetail = id_jabatan;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3KaryawanLamaFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f3keywordKaryawanLama = findViewById(R.id.keyword_user_ed);
        f3keywordKaryawanLama.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        f3KaryawanLamaRV = findViewById(R.id.karyawan_rv);
        f3StartAttantionKaryawanLamaPart = findViewById(R.id.attantion_data_part);
        f3NoDataKaryawanLamaPart = findViewById(R.id.no_data_part);
        f3loadingDataKaryawanLamaPart = findViewById(R.id.loading_data_part);
        f3loadingLamaGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f3loadingLamaGif);

        f3KaryawanLamaRV.setLayoutManager(new LinearLayoutManager(this));
        f3KaryawanLamaRV.setHasFixedSize(true);
        f3KaryawanLamaRV.setNestedScrollingEnabled(false);
        f3KaryawanLamaRV.setItemAnimator(new DefaultItemAnimator());

        f3keywordKaryawanLama.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f3keywordKaryawanLama.getText().toString();

                f3StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                f3loadingDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                f3NoDataKaryawanLamaPart.setVisibility(View.GONE);
                f3KaryawanLamaRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f3GetDataKaryawanLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f3keywordKaryawanLama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f3keywordKaryawanLama.getText().toString();
                    f3GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f3GetDataKaryawanLama(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                f3StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f3loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f3NoDataKaryawanLamaPart.setVisibility(View.GONE);
                                f3KaryawanLamaRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f3KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f3AdapterKaryawanLamaSDM = new AdapterKaryawanLamaSDM2(f3KaryawanSDMS, FormSdmActivity.this);
                                f3KaryawanLamaRV.setAdapter(f3AdapterKaryawanLamaSDM);
                            } else {
                                f3StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f3loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f3NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                                f3KaryawanLamaRV.setVisibility(View.GONE);
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

                        f3StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                        f3loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                        f3NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                        f3KaryawanLamaRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3KaryawanLamaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_lama = intent.getStringExtra("nik_karyawan_lama");
            String nama_karyawan_lama = intent.getStringExtra("nama_karyawan_lama");
            String departemen_karyawan_lama = intent.getStringExtra("departemen_karyawan_lama");
            String id_departemen_karyawan_lama = intent.getStringExtra("id_departemen_karyawan_lama");
            String bagian_karyawan_lama = intent.getStringExtra("bagian_karyawan_lama");
            String id_bagian_karyawan_lama = intent.getStringExtra("id_bagian_karyawan_lama");
            String jabatan_karyawan_lama = intent.getStringExtra("jabatan_karyawan_lama");
            String id_jabatan_karyawan_lama = intent.getStringExtra("id_jabatan_karyawan_lama");
            String status_karyawan_lama = intent.getStringExtra("status_karyawan_lama");

            f3NikLama = nik_karyawan_lama;
            f3NamaKaryawanLamaTV.setText(nama_karyawan_lama);

            f3UnitBisnisDisableModeLamaTV.setText("PT. Gelora Aksara Pratama");
            f3IdUnitBisnisLama = "1";

            f3NikLama = nik_karyawan_lama;
            f3DepartemenLama = id_departemen_karyawan_lama;
            f3DepartemenDisableModeLamaTV.setText(departemen_karyawan_lama);
            f3BagianLama = id_bagian_karyawan_lama;
            f3BagianDisableModeLamaTV.setText(bagian_karyawan_lama);
            f3JabatanLama = id_jabatan_karyawan_lama;
            f3JabatanDisableModeLamaTV.setText(jabatan_karyawan_lama);
            f3KomponenGajiDisableModeLamaTV.setText(status_karyawan_lama);

            f3NikBaru = nik_karyawan_lama;
            f3NamaKaryawanDisableModeTV.setText(nama_karyawan_lama);

            f3JabatanLamaDetailDisableModeTV.setText(jabatan_karyawan_lama);
            f3JabatanLamaDetail = id_jabatan_karyawan_lama;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f3UnitBisnisLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f3UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f3UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3UnitBisnisLamaRV.setHasFixedSize(true);
                f3UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f3UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f3GetUnitBisnisLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f3UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f3UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3UnitBisnisLamaRV.setHasFixedSize(true);
                f3UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f3UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f3GetUnitBisnisLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetUnitBisnisLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f3AdapterUnitBisnisLama = new AdapterUnitBisnis3Lama(unitBisnis,FormSdmActivity.this);
                            f3UnitBisnisLamaRV.setAdapter(f3AdapterUnitBisnisLama);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3UnitBisnisLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit_lama = intent.getStringExtra("id_unit_lama");
            String nama_unit_lama = intent.getStringExtra("nama_unit_lama");
            f3IdUnitBisnisLama = id_unit_lama;
            f3UnitBisnisLamaTV.setText(nama_unit_lama);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3DepartemenLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f3DepartemenLamaRV = findViewById(R.id.unit_departemen_rv);

                f3DepartemenLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3DepartemenLamaRV.setHasFixedSize(true);
                f3DepartemenLamaRV.setNestedScrollingEnabled(false);
                f3DepartemenLamaRV.setItemAnimator(new DefaultItemAnimator());

                f3GetDepartemenLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_departemen, bottomSheet, false));
                f3DepartemenLamaRV = findViewById(R.id.unit_departemen_rv);

                f3DepartemenLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3DepartemenLamaRV.setHasFixedSize(true);
                f3DepartemenLamaRV.setNestedScrollingEnabled(false);
                f3DepartemenLamaRV.setItemAnimator(new DefaultItemAnimator());

                f3GetDepartemenLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetDepartemenLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_departemen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String departemen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitDepartemenLama = gson.fromJson(departemen, UnitDepartemen[].class);
                            f3AdapterUnitDepartemenLama = new AdapterUnitDepartemenLama(f3UnitDepartemenLama,FormSdmActivity.this);
                            f3DepartemenLamaRV.setAdapter(f3AdapterUnitDepartemenLama);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3DepartemenLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_departemen = intent.getStringExtra("id_departemen");
            String nama_departemen = intent.getStringExtra("nama_departemen");
            f3DepartemenLama = id_departemen;
            f3DepartemenLamaTV.setText(nama_departemen);
            f3BagianLama = "";
            f3BagianLamaTV.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3BagianLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f3BagianLamaRV = findViewById(R.id.unit_bagian_rv);

                f3BagianLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3BagianLamaRV.setHasFixedSize(true);
                f3BagianLamaRV.setNestedScrollingEnabled(false);
                f3BagianLamaRV.setItemAnimator(new DefaultItemAnimator());

                f3GetBagianLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bagian, bottomSheet, false));
                f3BagianLamaRV = findViewById(R.id.unit_bagian_rv);

                f3BagianLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3BagianLamaRV.setHasFixedSize(true);
                f3BagianLamaRV.setNestedScrollingEnabled(false);
                f3BagianLamaRV.setItemAnimator(new DefaultItemAnimator());

                f3GetBagianLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetBagianLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String bagian = data.getString("data");

                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitBagianLama = gson.fromJson(bagian, UnitBagian[].class);
                            f3AdapterUnitBagianLama = new AdapterUnitBagianLama(f3UnitBagianLama,FormSdmActivity.this);
                            f3BagianLamaRV.setAdapter(f3AdapterUnitBagianLama);

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
                params.put("id_departemen", f3DepartemenLama);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3BagianLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_bagian = intent.getStringExtra("id_bagian");
            String nama_bagian = intent.getStringExtra("nama_bagian");
            f3BagianLama = id_bagian;
            f3BagianLamaTV.setText(nama_bagian);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3JabatanLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanLamaRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanLamaRV.setHasFixedSize(true);
                f3JabatanLamaRV.setNestedScrollingEnabled(false);
                f3JabatanLamaRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatanLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanLamaRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanLamaRV.setHasFixedSize(true);
                f3JabatanLamaRV.setNestedScrollingEnabled(false);
                f3JabatanLamaRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatanLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetJabatanLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_jabatan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String jabatan = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitJabatanLamas = gson.fromJson(jabatan, UnitJabatan[].class);
                            f3AdapterUnitJabatanLama = new AdapterUnitJabatanLama(f3UnitJabatanLamas,FormSdmActivity.this);
                            f3JabatanLamaRV.setAdapter(f3AdapterUnitJabatanLama);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3JabatanLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_jabatan = intent.getStringExtra("id_jabatan");
            String nama_jabatan = intent.getStringExtra("nama_jabatan");
            f3JabatanLama = id_jabatan;
            f3JabatanLamaTV.setText(nama_jabatan);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3JabatanLamaDetailWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanLamaDetailRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanLamaDetailRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanLamaDetailRV.setHasFixedSize(true);
                f3JabatanLamaDetailRV.setNestedScrollingEnabled(false);
                f3JabatanLamaDetailRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatanLamaDetail();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanLamaDetailRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanLamaDetailRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanLamaDetailRV.setHasFixedSize(true);
                f3JabatanLamaDetailRV.setNestedScrollingEnabled(false);
                f3JabatanLamaDetailRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatanLamaDetail();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetJabatanLamaDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_jabatan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String jabatan = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitJabatanLamaDetails = gson.fromJson(jabatan, UnitJabatan[].class);
                            f3AdapterUnitJabatanLamaDetail = new AdapterUnitJabatanLamaDetail(f3UnitJabatanLamaDetails,FormSdmActivity.this);
                            f3JabatanLamaDetailRV.setAdapter(f3AdapterUnitJabatanLamaDetail);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3JabatanLamaDetailBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_jabatan = intent.getStringExtra("id_jabatan");
            String nama_jabatan = intent.getStringExtra("nama_jabatan");
            f3JabatanLamaDetail = id_jabatan;
            f3JabatanLamaDetailTV.setText(nama_jabatan);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3JabatanBaruDetailWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanBaruDetailRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanBaruDetailRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanBaruDetailRV.setHasFixedSize(true);
                f3JabatanBaruDetailRV.setNestedScrollingEnabled(false);
                f3JabatanBaruDetailRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatanBaruDetail();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_jabatan, bottomSheet, false));
                f3JabatanBaruDetailRV = findViewById(R.id.unit_jabatan_rv);
                f3JabatanBaruDetailRV.setLayoutManager(new LinearLayoutManager(this));
                f3JabatanBaruDetailRV.setHasFixedSize(true);
                f3JabatanBaruDetailRV.setNestedScrollingEnabled(false);
                f3JabatanBaruDetailRV.setItemAnimator(new DefaultItemAnimator());
                f3GetJabatanBaruDetail();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f3GetJabatanBaruDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_jabatan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String jabatan = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            f3UnitJabatanBaruDetails = gson.fromJson(jabatan, UnitJabatan[].class);
                            f3AdapterUnitJabatanBaruDetail = new AdapterUnitJabatanBaruDetail(f3UnitJabatanBaruDetails,FormSdmActivity.this);
                            f3JabatanBaruDetailRV.setAdapter(f3AdapterUnitJabatanBaruDetail);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f3JabatanBaruDetailBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_jabatan = intent.getStringExtra("id_jabatan");
            String nama_jabatan = intent.getStringExtra("nama_jabatan");
            f3JabatanBaruDetail = id_jabatan;
            f3JabatanBaruDetailTV.setText(nama_jabatan);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f3SendData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/input_formulir_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String id = data.getString("id_data");
                                perngajuanTerkirim = "1";
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);

                                viewBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                                        intent.putExtra("id_data",id);
                                        startActivity(intent);
                                    }
                                });

                            }  else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
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
                        // error
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("pembuat", sharedPrefManager.getSpNik());
                params.put("keterangan", kodeKeterangan);

                //BARU
                params.put("nik_baru", f3NikBaru);
                params.put("unit_bisnis_baru", f3IdUnitBisnis);
                params.put("departemen_baru", f3DepartemenBaru);
                params.put("bagian_baru", f3BagianBaru);
                params.put("jabatan_baru", f3JabatanBaru);
                params.put("komponen_gaji_baru", f3KomponenGajiPilihTV.getText().toString());

                //LAMA
                params.put("nik_lama", f3NikLama);
                params.put("unit_bisnis_lama", f3IdUnitBisnisLama);
                params.put("departemen_lama", f3DepartemenLama);
                params.put("bagian_lama", f3BagianLama);
                params.put("jabatan_lama", f3JabatanLama);
                params.put("komponen_gaji_lama", f3KomponenGajiDisableModeLamaTV.getText().toString());

                //DETAIL
                params.put("jabatan_lama_detail", f3JabatanLamaDetail);
                params.put("tgl_pengangkatan_jabatan_lama", f3TglPengangkatanJabatanLama);
                params.put("jabatan_baru_detail", f3JabatanBaruDetail);
                params.put("tgl_pengangkatan_jabatan_baru", f3TglPengangkatanJabatanLama);
                params.put("alasan_pengangkatan", f3AlasanPengangkatanTV.getText().toString());
                params.put("tgl_dibutuhkan", f3TglDibutuhkan);
                params.put("tgl_pemenuhan", f3TglPemenuhan);

                if(kodeKeterangan.equals("5")){
                    if(f3SubKet.equals("1")){
                        params.put("catatan", "PROMOSI"+" - "+f3CatatanTV.getText().toString());
                    } else if(f3SubKet.equals("2")){
                        params.put("catatan", "MUTASI"+" - "+f3CatatanTV.getText().toString());
                    }
                } else {
                    params.put("catatan", f3CatatanTV.getText().toString());
                }

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }
    //Form 5 6

    //Form 7
    private void f4KaryawanBaruFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f4keywordKaryawanBaru = findViewById(R.id.keyword_user_ed);
        f4keywordKaryawanBaru.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        f4KaryawanBaruRV = findViewById(R.id.karyawan_rv);
        f4StartAttantionKaryawanBaruPart = findViewById(R.id.attantion_data_part);
        f4NoDataKaryawanBaruPart = findViewById(R.id.no_data_part);
        f4loadingDataKaryawanBaruPart = findViewById(R.id.loading_data_part);
        f4loadingGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f4loadingGif);

        f4KaryawanBaruRV.setLayoutManager(new LinearLayoutManager(this));
        f4KaryawanBaruRV.setHasFixedSize(true);
        f4KaryawanBaruRV.setNestedScrollingEnabled(false);
        f4KaryawanBaruRV.setItemAnimator(new DefaultItemAnimator());

        f4keywordKaryawanBaru.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f4keywordKaryawanBaru.getText().toString();

                f4StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                f4loadingDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                f4NoDataKaryawanBaruPart.setVisibility(View.GONE);
                f4KaryawanBaruRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f4GetDataKaryawanBaru(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f4keywordKaryawanBaru.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f4keywordKaryawanBaru.getText().toString();
                    f4GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f4GetDataKaryawanBaru(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                f4StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f4loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f4NoDataKaryawanBaruPart.setVisibility(View.GONE);
                                f4KaryawanBaruRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f4KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f4AdapterKaryawanBaruSDM = new AdapterKaryawanBaruSDM3(f4KaryawanSDMS, FormSdmActivity.this);
                                f4KaryawanBaruRV.setAdapter(f4AdapterKaryawanBaruSDM);
                            } else {
                                f4StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                                f4loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                                f4NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                                f4KaryawanBaruRV.setVisibility(View.GONE);
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

                        f4StartAttantionKaryawanBaruPart.setVisibility(View.GONE);
                        f4loadingDataKaryawanBaruPart.setVisibility(View.GONE);
                        f4NoDataKaryawanBaruPart.setVisibility(View.VISIBLE);
                        f4KaryawanBaruRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f4KaryawanBaruBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_baru = intent.getStringExtra("nik_karyawan_baru");
            String nama_karyawan_baru = intent.getStringExtra("nama_karyawan_baru");
            String departemen_karyawan_baru = intent.getStringExtra("departemen_karyawan_baru");
            String id_departemen_karyawan_baru = intent.getStringExtra("id_departemen_karyawan_baru");
            String bagian_karyawan_baru = intent.getStringExtra("bagian_karyawan_baru");
            String id_bagian_karyawan_baru = intent.getStringExtra("id_bagian_karyawan_baru");
            String jabatan_karyawan_baru = intent.getStringExtra("jabatan_karyawan_baru");
            String id_jabatan_karyawan_baru = intent.getStringExtra("id_jabatan_karyawan_baru");
            String status_karyawan_baru = intent.getStringExtra("status_karyawan_baru");

            f4NikBaru = nik_karyawan_baru;
            f4NamaKaryawanTV.setText(nama_karyawan_baru);
            f4DepartemenBaru = id_departemen_karyawan_baru;
            f4DepartemenTV.setText(departemen_karyawan_baru);
            f4BagianBaru = id_bagian_karyawan_baru;
            f4BagianTV.setText(bagian_karyawan_baru);
            f4JabatanBaru = id_jabatan_karyawan_baru;
            f4JabatanTV.setText(jabatan_karyawan_baru);

            f4UnitBisnisDisableModeTV.setText("PT. Gelora Aksara Pratama");
            f4IdUnitBisnis = "1";
            f4KomponenGajiDisableModeTV.setText(status_karyawan_baru);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f4UnitBisnisWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f4UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f4UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f4UnitBisnisRV.setHasFixedSize(true);
                f4UnitBisnisRV.setNestedScrollingEnabled(false);
                f4UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f4GetUnitBisnis();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f4UnitBisnisRV = findViewById(R.id.unit_bisnis_rv);

                f4UnitBisnisRV.setLayoutManager(new LinearLayoutManager(this));
                f4UnitBisnisRV.setHasFixedSize(true);
                f4UnitBisnisRV.setNestedScrollingEnabled(false);
                f4UnitBisnisRV.setItemAnimator(new DefaultItemAnimator());

                f4GetUnitBisnis();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f4GetUnitBisnis() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f4AdapterUnitBisnis = new AdapterUnitBisnis4(unitBisnis,FormSdmActivity.this);
                            f4UnitBisnisRV.setAdapter(f4AdapterUnitBisnis);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f4UnitBisnisBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit = intent.getStringExtra("id_unit");
            String nama_unit = intent.getStringExtra("nama_unit");
            f4IdUnitBisnis = id_unit;
            f4UnitBisnisTV.setText(nama_unit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f4KaryawanLamaFunc(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormSdmActivity.this).inflate(R.layout.layout_karyawan_sdm, bottomSheet, false));
        f4keywordKaryawanLama = findViewById(R.id.keyword_user_ed);
        f4keywordKaryawanLama.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        f4KaryawanLamaRV = findViewById(R.id.karyawan_rv);
        f4StartAttantionKaryawanLamaPart = findViewById(R.id.attantion_data_part);
        f4NoDataKaryawanLamaPart = findViewById(R.id.no_data_part);
        f4loadingDataKaryawanLamaPart = findViewById(R.id.loading_data_part);
        f4loadingLamaGif = findViewById(R.id.loading_data);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading_sgn_digital)
                .into(f4loadingLamaGif);

        f4KaryawanLamaRV.setLayoutManager(new LinearLayoutManager(this));
        f4KaryawanLamaRV.setHasFixedSize(true);
        f4KaryawanLamaRV.setNestedScrollingEnabled(false);
        f4KaryawanLamaRV.setItemAnimator(new DefaultItemAnimator());

        f4keywordKaryawanLama.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String keyWordSearch = f4keywordKaryawanLama.getText().toString();

                f4StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                f4loadingDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                f4NoDataKaryawanLamaPart.setVisibility(View.GONE);
                f4KaryawanLamaRV.setVisibility(View.GONE);

                if(!keyWordSearch.equals("")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            f4GetDataKaryawanLama(keyWordSearch);
                        }
                    }, 500);
                }
            }

        });

        f4keywordKaryawanLama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWordSearch = f4keywordKaryawanLama.getText().toString();
                    f4GetDataKaryawanBaru(keyWordSearch);

                    InputMethodManager imm = (InputMethodManager) FormSdmActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    View view = FormSdmActivity.this.getCurrentFocus();
                    if (view == null) {
                        view = new View(FormSdmActivity.this);
                    }
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }
    private void f4GetDataKaryawanLama(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_karyawan_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                f4StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f4loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f4NoDataKaryawanLamaPart.setVisibility(View.GONE);
                                f4KaryawanLamaRV.setVisibility(View.VISIBLE);

                                String data_list = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                f4KaryawanSDMS = gson.fromJson(data_list, KaryawanSDM[].class);
                                f4AdapterKaryawanLamaSDM = new AdapterKaryawanLamaSDM3(f4KaryawanSDMS, FormSdmActivity.this);
                                f4KaryawanLamaRV.setAdapter(f4AdapterKaryawanLamaSDM);
                            } else {
                                f4StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                                f4loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                                f4NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                                f4KaryawanLamaRV.setVisibility(View.GONE);
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

                        f4StartAttantionKaryawanLamaPart.setVisibility(View.GONE);
                        f4loadingDataKaryawanLamaPart.setVisibility(View.GONE);
                        f4NoDataKaryawanLamaPart.setVisibility(View.VISIBLE);
                        f4KaryawanLamaRV.setVisibility(View.GONE);

                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("keyword_karyawan", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f4KaryawanLamaBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_karyawan_lama = intent.getStringExtra("nik_karyawan_lama");
            String nama_karyawan_lama = intent.getStringExtra("nama_karyawan_lama");
            String departemen_karyawan_lama = intent.getStringExtra("departemen_karyawan_lama");
            String id_departemen_karyawan_lama = intent.getStringExtra("id_departemen_karyawan_lama");
            String bagian_karyawan_lama = intent.getStringExtra("bagian_karyawan_lama");
            String id_bagian_karyawan_lama = intent.getStringExtra("id_bagian_karyawan_lama");
            String jabatan_karyawan_lama = intent.getStringExtra("jabatan_karyawan_lama");
            String id_jabatan_karyawan_lama = intent.getStringExtra("id_jabatan_karyawan_lama");
            String status_karyawan_lama = intent.getStringExtra("status_karyawan_lama");

            f4NikLama = nik_karyawan_lama;
            f4NamaKaryawanLamaTV.setText(nama_karyawan_lama);
            f4DepartemenLama = id_departemen_karyawan_lama;
            f4DepartemenLamaTV.setText(departemen_karyawan_lama);
            f4BagianLama = id_bagian_karyawan_lama;
            f4BagianLamaTV.setText(bagian_karyawan_lama);
            f4JabatanLama = id_jabatan_karyawan_lama;
            f4JabatanLamaTV.setText(jabatan_karyawan_lama);

            f4UnitBisnisDisableModeLamaTV.setText("PT. Gelora Aksara Pratama");
            f4IdUnitBisnisLama = "1";
            f4KomponenGajiDisableModeLamaTV.setText(status_karyawan_lama);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }, 300);
        }
    };
    private void f4UnitBisnisLamaWay(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f4UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f4UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f4UnitBisnisLamaRV.setHasFixedSize(true);
                f4UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f4UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f4GetUnitBisnisLama();
            } else {
                bottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_unit_bisnis, bottomSheet, false));
                f4UnitBisnisLamaRV = findViewById(R.id.unit_bisnis_rv);

                f4UnitBisnisLamaRV.setLayoutManager(new LinearLayoutManager(this));
                f4UnitBisnisLamaRV.setHasFixedSize(true);
                f4UnitBisnisLamaRV.setNestedScrollingEnabled(false);
                f4UnitBisnisLamaRV.setItemAnimator(new DefaultItemAnimator());

                f4GetUnitBisnisLama();
            }
        } catch (InflateException e){
            e.printStackTrace();
        }
    }
    private void f4GetUnitBisnisLama() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_unit_bisnis";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);

                            JSONObject data = new JSONObject(response);
                            String unit = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            unitBisnis = gson.fromJson(unit, UnitBisnis[].class);
                            f4AdapterUnitBisnisLama = new AdapterUnitBisnis4Lama(unitBisnis,FormSdmActivity.this);
                            f4UnitBisnisLamaRV.setAdapter(f4AdapterUnitBisnisLama);

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
                params.put("request", "request");
                return params;
            }
        };

        requestQueue.add(postRequest);

    }
    public BroadcastReceiver f4UnitBisnisLamaBoard = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_unit_lama = intent.getStringExtra("id_unit_lama");
            String nama_unit_lama = intent.getStringExtra("nama_unit_lama");
            f4IdUnitBisnisLama = id_unit_lama;
            f4UnitBisnisLamaTV.setText(nama_unit_lama);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);
        }
    };
    private void f4SendData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/input_formulir_sdm";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response);
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String id = data.getString("id_data");
                                perngajuanTerkirim = "1";
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);

                                viewBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(FormSdmActivity.this, DetailFormSdmActivity.class);
                                        intent.putExtra("id_data",id);
                                        startActivity(intent);
                                    }
                                });

                            }  else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
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
                        // error
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("pembuat", sharedPrefManager.getSpNik());
                params.put("keterangan", kodeKeterangan);

                //BARU
                params.put("nik_baru", f4NikBaru);
                params.put("unit_bisnis_baru", f4IdUnitBisnis);
                params.put("departemen_baru", f4DepartemenBaru);
                params.put("bagian_baru", f4BagianBaru);
                params.put("jabatan_baru", f4JabatanBaru);
                params.put("komponen_gaji_baru", f4KomponenGajiDisableModeTV.getText().toString());

                //LAMA
                params.put("nik_lama", f4NikLama);
                params.put("unit_bisnis_lama", f4IdUnitBisnisLama);
                params.put("departemen_lama", f4DepartemenLama);
                params.put("bagian_lama", f4BagianLama);
                params.put("jabatan_lama", f4JabatanLama);
                params.put("komponen_gaji_lama", f4KomponenGajiDisableModeLamaTV.getText().toString());

                params.put("lain-lain", f4LainlainTV.getText().toString());
                params.put("persetujuan", f4Persetujuan);
                params.put("catatan", f4CatatanTV.getText().toString());

                return params;
            }
        };

        requestQueue.add(postRequest);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

    }
    //Form 7

    private void checkSignature(){
        final String url = "https://geloraaksara.co.id/absen-online/api/cek_ttd_digital";
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

                            if (status.equals("Available")){
                                String signature = data.getString("data");
                                String url = "https://geloraaksara.co.id/absen-online/upload/digital_signature/"+signature;
                                if(kodeKeterangan.equals("1")){
                                    f1SendData();
                                } else  if(kodeKeterangan.equals("2")||kodeKeterangan.equals("3")||kodeKeterangan.equals("4")){
                                    f2SendData();
                                } else if(kodeKeterangan.equals("5")||kodeKeterangan.equals("6")){
                                    f3SendData();
                                } else if(kodeKeterangan.equals("7")){
                                    f4SendData();
                                }
                            } else {
                                pDialog.setTitleText("Perhatian")
                                        .setContentText("Anda belum mengisi tanda tangan digital. Harap isi terlebih dahulu")
                                        .setCancelText(" BATAL ")
                                        .setConfirmText("LANJUT")
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
                                                Intent intent = new Intent(FormSdmActivity.this, DigitalSignatureActivity.class);
                                                intent.putExtra("kode", "form");
                                                startActivity(intent);
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.WARNING_TYPE);
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
                params.put("NIK", sharedPrefManager.getSpNik());

                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormSdmActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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

    private void getBagianDepartemen() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_bagian_departemen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String departemen = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                f1DepartemenDisableModeTV.setText(departemen);
                                if(sharedPrefManager.getSpIdJabatan().equals("1") || sharedPrefManager.getSpIdJabatan().equals("11") || sharedPrefManager.getSpIdJabatan().equals("25")){
                                    f1BagianDisableModeTV.setText(bagian);
                                }
                            } else {
                                new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
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
                if(!f1IdBagian.equals("")){
                    params.put("id_bagian", f1IdBagian);
                } else {
                    params.put("id_bagian", sharedPrefManager.getSpIdDept());
                }
                params.put("id_departemen", f1IdDepartemen);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SimpleDateFormat")
    private void dateMulai(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f2DateChoiceMulai = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!f2DateChoiceAkhir.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(f2DateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(f2DateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = f2DateChoiceMulai;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

                        f2StartDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!f2DateChoiceMulai.equals("") && !f2DateChoiceAkhir.equals("")){
                            countDuration();
                        }

                    } else {
                        f2StartDatePilih.setText("Pilih Kembali !");
                        f2DateChoiceMulai = "";
                        f2DurasiKontrak.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal mulai tidak bisa lebih besar dari tanggal akhir. Harap ulangi!")
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

                else {
                    String input_date = f2DateChoiceMulai;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

                    f2StartDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f2DateChoiceMulai = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!f2DateChoiceAkhir.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(f2DateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(f2DateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = f2DateChoiceMulai;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

                        f2StartDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!f2DateChoiceMulai.equals("") && !f2DateChoiceAkhir.equals("")){
                            countDuration();
                        }

                    } else {
                        f2StartDatePilih.setText("Pilih Kembali !");
                        f2DateChoiceMulai = "";
                        f2DurasiKontrak.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal mulai tidak bisa lebih besar dari tanggal akhir. Harap ulangi!")
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

                else {
                    String input_date = f2DateChoiceMulai;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

                    f2StartDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, y,m-1,d);
            dpd.show();
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void dateAkhir(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f2DateChoiceAkhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!f2DateChoiceMulai.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(f2DateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(f2DateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = f2DateChoiceAkhir;
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

                        f2EndDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!f2DateChoiceMulai.equals("") && !f2DateChoiceAkhir.equals("")){
                            countDuration();
                        }

                    } else {
                        f2EndDatePilih.setText("Pilih Kembali !");
                        f2DateChoiceAkhir = "";
                        f2DurasiKontrak.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal akhir tidak bisa lebih kecil dari tanggal mulai. Harap ulangi!")
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

                else {
                    String input_date = f2DateChoiceAkhir;
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

                    f2EndDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, cal.get(android.icu.util.Calendar.YEAR), cal.get(android.icu.util.Calendar.MONTH), cal.get(android.icu.util.Calendar.DATE));
            dpd.show();
        } else {
            int y = Integer.parseInt(getDateY());
            int m = Integer.parseInt(getDateM());
            int d = Integer.parseInt(getDateD());
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            DatePickerDialog dpd = new DatePickerDialog(FormSdmActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

                f2DateChoiceAkhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

                if (!f2DateChoiceMulai.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Date date2 = null;
                    try {
                        date = sdf.parse(String.valueOf(f2DateChoiceMulai));
                        date2 = sdf.parse(String.valueOf(f2DateChoiceAkhir));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mulai = date.getTime();
                    long akhir = date2.getTime();

                    if (mulai<=akhir){
                        String input_date = f2DateChoiceAkhir;
                        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1= null;
                        try {
                            dt1 = format1.parse(input_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

                        f2EndDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                        if(!f2DateChoiceMulai.equals("") && !f2DateChoiceAkhir.equals("")){
                            countDuration();
                        }

                    } else {
                        f2EndDatePilih.setText("Pilih Kembali !");
                        f2DateChoiceAkhir = "";
                        f2DurasiKontrak.setText("Tentukan Tanggal...");

                        new KAlertDialog(FormSdmActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Tanggal akhir tidak bisa lebih kecil dari tanggal mulai. Harap ulangi!")
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

                else {
                    String input_date = f2DateChoiceAkhir;
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                    Date dt1= null;
                    try {
                        dt1 = format1.parse(input_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

                    f2EndDatePilih.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                }

            }, y,m-1,d);
            dpd.show();
        }

    }

    @SuppressLint("SetTextI18n")
    public void countDuration(){
        String startDateString = f2DateChoiceMulai;
        String endDateString = f2DateChoiceAkhir;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(endDateString);
            date2 = format.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = waktu1 - waktu2;

        long diffDays = (selisih_waktu / (24 * 60 * 60 * 1000)) + 1;

        long years = (diffDays / 365);
        long months = (diffDays - (years * 365)) / 30;
        long days = (diffDays - ((years * 365) + (months * 30)));

        // Print the resulting duration
        if (years == 0){
            if(months == 0){
                if(days == 0){
                    f2DurasiKontrak.setText("-");
                } else {
                    f2DurasiKontrak.setText(days +" Hari");
                }
            } else {
                if(days == 0){
                    f2DurasiKontrak.setText(months + " Bulan");
                } else {
                    f2DurasiKontrak.setText(months + " Bulan " + days + " Hari");
                }
            }
        } else {
            if(months == 0){
                if(days == 0){
                    f2DurasiKontrak.setText(years + " Tahun");
                } else {
                    f2DurasiKontrak.setText(years + " Tahun " + days + " Hari");
                }
            } else {
                if(days == 0){
                    f2DurasiKontrak.setText(years + " Tahun " + months + " Bulan");
                } else {
                    f2DurasiKontrak.setText(years + " Tahun " + months + " Bulan " + days + " Hari");
                }
            }
        }

    }

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        if (!kodeKeterangan.equals("0")){
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                if (perngajuanTerkirim.equals("1")){
                    super.onBackPressed();
                } else {
                    new KAlertDialog(FormSdmActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    kodeKeterangan = "0";
                                    onBackPressed();
                                }
                            })
                            .show();
                }
            }
        } else {
            if(bottomSheet.isSheetShowing()){
                bottomSheet.dismissSheet();
            } else {
                super.onBackPressed();
            }
        }
    }

}