package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.takisoft.datetimepicker.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormInfoKeluargaActivity extends AppCompatActivity {

    LinearLayout actionBar, lainnyaBTN, markLainnya, islamBTN, kristenBTN, hinduBTN, buddhaBTN, katolikBTN, konghuchuBTN, markIslam, markKristen, markHindu, markBuddha, markKatolik, markKonghuchu, backSuccessBTN, formPart, successPart, tanggalLAhirBTN, submitBTN, backBTN, agamaBTN, genderBTN, statusPernikahanBTN, maleBTN, femaleBTN, markMale, markFemale, belumMenikahBTN, sudahMenikahBTN, ceraiHidupBTN, ceraiMatiBTN, markBelumMenikah, markSudahMenikah, markCeraiHidup, markCeraiMati;
    TextView titlePageTV, agamaPilihTV, namaTV, genderPilihTV, tanggalLahirPilihTV, statusPernikahanPilihTV;
    LinearLayout hubunganBTN, hubunganLainnyaPart, bekerjaBTN, tidakBekerjaBTN, markBekerja, markTidakBekerja;
    LinearLayout sodaraLakiBTN, sodaraPerempuanBTN, markSodaraLaki, markSodaraPerempuan, ayahBTN, ibuBTN, suamiBTN, istriBTN, anakBTN, markAyah, markIbu, markSuami, markIstri, markAnak;
    LinearLayout statusBekerjaBTN, tanggalLahirBTN;
    TextView statusBekerjaPilihTV, hubunganPilihTV;
    EditText namaED, tempatLahirED, hubunganLainnyaED;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    String tipeForm = "", idData = "";
    String statusBekerjaChoice = "", genderChoice = "", tanggalLAhir = "", hubunganPilih = "", statusPernikahanChoice = "", agamaChoice = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_keluarga);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        successPart = findViewById(R.id.success_submit);
        submitBTN = findViewById(R.id.submit_btn);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        namaED = findViewById(R.id.nama_ed);
        tempatLahirED = findViewById(R.id.tempat_lahir_ed);
        tanggalLahirBTN = findViewById(R.id.tanggal_lahir_btn);
        tanggalLahirPilihTV = findViewById(R.id.tanggal_lahir_pilih_tv);
        genderBTN = findViewById(R.id.gender_btn);
        genderPilihTV = findViewById(R.id.gender_pilih_tv);
        hubunganBTN = findViewById(R.id.hubungan_btn);
        hubunganPilihTV = findViewById(R.id.hubungan_pilih_tv);
        hubunganLainnyaPart = findViewById(R.id.hubungan_lainnya_part);
        hubunganLainnyaED = findViewById(R.id.hubungan_lainnya_ed);
        statusPernikahanBTN = findViewById(R.id.status_pernikahan_btn);
        statusPernikahanPilihTV = findViewById(R.id.status_pernikahan_pilih_tv);
        agamaBTN = findViewById(R.id.agama_btn);
        agamaPilihTV = findViewById(R.id.agama_pilih_tv);
        statusBekerjaBTN = findViewById(R.id.status_bekerja_btn);
        statusBekerjaPilihTV = findViewById(R.id.status_bekerja_pilih_tv);
        titlePageTV = findViewById(R.id.title_page_tv);

        namaED.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hubunganLainnyaED.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        tipeForm = getIntent().getExtras().getString("tipe");

        if(tipeForm.equals("edit")){
            idData = getIntent().getExtras().getString("id_data");
            titlePageTV.setText("EDIT INFO KELUARGA");
        } else {
            titlePageTV.setText("FORM INFO KELUARGA");
        }

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                if(tipeForm.equals("edit")){
//                    getData();
//                } else {
                    namaED.setText("");
                    tempatLahirED.setText("");
                    tanggalLahirPilihTV.setText("");
                    tanggalLAhir = "";
                    genderPilihTV.setText("");
                    genderChoice = "";
                    hubunganPilihTV.setText("");
                    hubunganPilih = "";
                    hubunganLainnyaPart.setVisibility(View.GONE);
                    hubunganLainnyaED.setText("");
                    statusPernikahanPilihTV.setText("");
                    statusPernikahanChoice = "";
                    agamaPilihTV.setText("");
                    agamaChoice = "";
                    statusBekerjaPilihTV.setText("");
                    statusBekerjaChoice = "";
//                }

                namaED.clearFocus();
                tempatLahirED.clearFocus();
                hubunganLainnyaED.clearFocus();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        //getData();
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

        tanggalLahirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                bornDate();
            }
        });

        genderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderChoice();
            }
        });

        hubunganBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                hubunganChoice();
            }
        });

        agamaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agamaChoice();
            }
        });

        statusPernikahanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusPernikahanChoice();
            }
        });

        statusBekerjaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBekerjaChoice();
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private void bornDate(){
        int y = Integer.parseInt(getDateY());
        int m = Integer.parseInt(getDateM());
        int d = Integer.parseInt(getDateD());
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormInfoKeluargaActivity.this, (view1, year, month, dayOfMonth) -> {

            tanggalLAhir = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            String dayDate = tanggalLAhir.substring(8,10);
            String yearDate = tanggalLAhir.substring(0,4);;
            String bulanValue = tanggalLAhir.substring(5,7);
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

            tanggalLahirPilihTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

        }, y,m,d);
        dpd.show();

    }

    private void genderChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_gender, bottomSheet, false));
        maleBTN = findViewById(R.id.male_btn);
        femaleBTN = findViewById(R.id.female_btn);
        markMale = findViewById(R.id.mark_male);
        markFemale = findViewById(R.id.mark_female);

        if (genderChoice.equals("male")){
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markMale.setVisibility(View.VISIBLE);
            markFemale.setVisibility(View.GONE);
        } else if (genderChoice.equals("female")){
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markMale.setVisibility(View.GONE);
            markFemale.setVisibility(View.VISIBLE);
        } else {
            maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markMale.setVisibility(View.GONE);
            markFemale.setVisibility(View.GONE);
        }

        maleBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markMale.setVisibility(View.VISIBLE);
                markFemale.setVisibility(View.GONE);
                maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                genderChoice = "male";
                genderPilihTV.setText("Laki-Laki");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        femaleBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markMale.setVisibility(View.GONE);
                markFemale.setVisibility(View.VISIBLE);
                maleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                femaleBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                genderChoice = "female";
                genderPilihTV.setText("Perempuan");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void hubunganChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_hubungan, bottomSheet, false));
        ayahBTN = findViewById(R.id.ayah_btn);
        ibuBTN = findViewById(R.id.ibu_btn);
        suamiBTN = findViewById(R.id.suami_btn);
        istriBTN = findViewById(R.id.istri_btn);
        anakBTN = findViewById(R.id.anak_btn);
        sodaraLakiBTN = findViewById(R.id.sodara_laki_btn);
        sodaraPerempuanBTN = findViewById(R.id.sodara_perempuan_btn);
        lainnyaBTN = findViewById(R.id.lainnya_btn);
        markAyah = findViewById(R.id.mark_ayah);
        markIbu = findViewById(R.id.mark_ibu);
        markSuami = findViewById(R.id.mark_suami);
        markIstri = findViewById(R.id.mark_istri);
        markAnak = findViewById(R.id.mark_anak);
        markSodaraLaki = findViewById(R.id.mark_sodara_laki);
        markSodaraPerempuan = findViewById(R.id.mark_sodara_perempuan);
        markLainnya = findViewById(R.id.mark_lainnya);

        if (hubunganPilih.equals("Ayah")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.VISIBLE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Ibu")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.VISIBLE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Suami")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.VISIBLE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Istri")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.VISIBLE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Anak")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.VISIBLE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Saudara Laki-Laki")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.VISIBLE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Saudara Perempuan")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.VISIBLE);
            markLainnya.setVisibility(View.GONE);

            hubunganLainnyaPart.setVisibility(View.GONE);
        } else if (hubunganPilih.equals("Lainnya")){
            ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markAyah.setVisibility(View.GONE);
            markIbu.setVisibility(View.GONE);
            markSuami.setVisibility(View.GONE);
            markIstri.setVisibility(View.GONE);
            markAnak.setVisibility(View.GONE);
            markSodaraLaki.setVisibility(View.GONE);
            markSodaraPerempuan.setVisibility(View.GONE);
            markLainnya.setVisibility(View.VISIBLE);

            hubunganLainnyaPart.setVisibility(View.VISIBLE);
        }

        ayahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.VISIBLE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Ayah";
                hubunganPilihTV.setText("Ayah");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        ibuBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.VISIBLE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Ibu";
                hubunganPilihTV.setText("Ibu");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        suamiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.VISIBLE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Suami";
                hubunganPilihTV.setText("Suami");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        istriBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.VISIBLE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Istri";
                hubunganPilihTV.setText("Istri");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        anakBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.VISIBLE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Anak";
                hubunganPilihTV.setText("Anak");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sodaraLakiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.VISIBLE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Saudara Laki-Laki";
                hubunganPilihTV.setText("Saudara Laki-Laki");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sodaraPerempuanBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.VISIBLE);
                markLainnya.setVisibility(View.GONE);

                hubunganLainnyaPart.setVisibility(View.GONE);

                hubunganPilih = "Saudara Perempuan";
                hubunganPilihTV.setText("Saudara Perempuan");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        lainnyaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                ayahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                markAyah.setVisibility(View.GONE);
                markIbu.setVisibility(View.GONE);
                markSuami.setVisibility(View.GONE);
                markIstri.setVisibility(View.GONE);
                markAnak.setVisibility(View.GONE);
                markSodaraLaki.setVisibility(View.GONE);
                markSodaraPerempuan.setVisibility(View.GONE);
                markLainnya.setVisibility(View.VISIBLE);

                hubunganLainnyaPart.setVisibility(View.VISIBLE);

                hubunganPilih = "Lainnya";
                hubunganPilihTV.setText("Lainnya");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void statusPernikahanChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_status_pernikahan, bottomSheet, false));
        belumMenikahBTN = findViewById(R.id.belum_menikah_btn);
        sudahMenikahBTN = findViewById(R.id.sudah_menikah_btn);
        ceraiHidupBTN = findViewById(R.id.cerai_hidup_btn);
        ceraiMatiBTN = findViewById(R.id.cerai_mati_btn);
        markBelumMenikah = findViewById(R.id.mark_belum_menikah);
        markSudahMenikah = findViewById(R.id.mark_sudah_menikah);
        markCeraiHidup = findViewById(R.id.mark_cerai_hidup);
        markCeraiMati = findViewById(R.id.mark_cerai_mati);

        if (statusPernikahanChoice.equals("1")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.VISIBLE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("2")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.VISIBLE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("3")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.VISIBLE);
            markCeraiMati.setVisibility(View.GONE);
        } else if (statusPernikahanChoice.equals("4")){
            belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markBelumMenikah.setVisibility(View.GONE);
            markSudahMenikah.setVisibility(View.GONE);
            markCeraiHidup.setVisibility(View.GONE);
            markCeraiMati.setVisibility(View.VISIBLE);
        }

        belumMenikahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markBelumMenikah.setVisibility(View.VISIBLE);
                markSudahMenikah.setVisibility(View.GONE);
                markCeraiHidup.setVisibility(View.GONE);
                markCeraiMati.setVisibility(View.GONE);

                statusPernikahanChoice = "1";
                statusPernikahanPilihTV.setText("Belum Menikah");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        sudahMenikahBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markBelumMenikah.setVisibility(View.GONE);
                markSudahMenikah.setVisibility(View.VISIBLE);
                markCeraiHidup.setVisibility(View.GONE);
                markCeraiMati.setVisibility(View.GONE);

                statusPernikahanChoice = "2";
                statusPernikahanPilihTV.setText("Menikah");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        ceraiHidupBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markBelumMenikah.setVisibility(View.GONE);
                markSudahMenikah.setVisibility(View.GONE);
                markCeraiHidup.setVisibility(View.VISIBLE);
                markCeraiMati.setVisibility(View.GONE);

                statusPernikahanChoice = "3";
                statusPernikahanPilihTV.setText("Cerai Hidup");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        ceraiMatiBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                belumMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                sudahMenikahBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiHidupBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                ceraiMatiBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                markBelumMenikah.setVisibility(View.GONE);
                markSudahMenikah.setVisibility(View.GONE);
                markCeraiHidup.setVisibility(View.GONE);
                markCeraiMati.setVisibility(View.VISIBLE);

                statusPernikahanChoice = "4";
                statusPernikahanPilihTV.setText("Cerai Mati");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void agamaChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_agama, bottomSheet, false));
        islamBTN = findViewById(R.id.islam_btn);
        kristenBTN = findViewById(R.id.kristen_btn);
        hinduBTN = findViewById(R.id.hindu_btn);
        buddhaBTN = findViewById(R.id.buddha_btn);
        katolikBTN = findViewById(R.id.katolik_btn);
        konghuchuBTN = findViewById(R.id.konghuchu_btn);
        lainnyaBTN = findViewById(R.id.lainnya_btn);
        markIslam = findViewById(R.id.mark_islam);
        markKristen = findViewById(R.id.mark_kristen);
        markHindu = findViewById(R.id.mark_hindu);
        markBuddha = findViewById(R.id.mark_buddha);
        markKatolik = findViewById(R.id.mark_katolik);
        markKonghuchu = findViewById(R.id.mark_konghuchu);
        markLainnya = findViewById(R.id.mark_lainnya);

        if (agamaChoice.equals("Islam")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.VISIBLE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Kristen")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.VISIBLE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Hindu")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.VISIBLE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Buddha")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.VISIBLE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Katolik")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.VISIBLE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Kong Hu Chu")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.VISIBLE);
            markLainnya.setVisibility(View.GONE);
        } else if (agamaChoice.equals("Lainnya")){
            islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markIslam.setVisibility(View.GONE);
            markKristen.setVisibility(View.GONE);
            markHindu.setVisibility(View.GONE);
            markBuddha.setVisibility(View.GONE);
            markKatolik.setVisibility(View.GONE);
            markKonghuchu.setVisibility(View.GONE);
            markLainnya.setVisibility(View.VISIBLE);
        }

        islamBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.VISIBLE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Islam";
                agamaPilihTV.setText("Islam");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        kristenBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.VISIBLE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Kristen";
                agamaPilihTV.setText("Kristen");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        hinduBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.VISIBLE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Hindu";
                agamaPilihTV.setText("Hindu");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        buddhaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.VISIBLE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Buddha";
                agamaPilihTV.setText("Buddha");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        katolikBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.VISIBLE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Katolik";
                agamaPilihTV.setText("Katolik");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        konghuchuBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.VISIBLE);
                markLainnya.setVisibility(View.GONE);

                agamaChoice = "Kong Hu Chu";
                agamaPilihTV.setText("Kong Hu Chu");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        lainnyaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                islamBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                kristenBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                hinduBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                buddhaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                katolikBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                konghuchuBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                markIslam.setVisibility(View.GONE);
                markKristen.setVisibility(View.GONE);
                markHindu.setVisibility(View.GONE);
                markBuddha.setVisibility(View.GONE);
                markKatolik.setVisibility(View.GONE);
                markKonghuchu.setVisibility(View.GONE);
                markLainnya.setVisibility(View.VISIBLE);

                agamaChoice = "Lainnya";
                agamaPilihTV.setText("Lainnya");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

    }

    private void statusBekerjaChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormInfoKeluargaActivity.this).inflate(R.layout.layout_status_pekerjaan, bottomSheet, false));
        bekerjaBTN = findViewById(R.id.bekerja_btn);
        tidakBekerjaBTN = findViewById(R.id.tidak_bekerja_btn);
        markBekerja = findViewById(R.id.mark_bekerja);
        markTidakBekerja = findViewById(R.id.mark_tidak_bekerja);

        if (statusBekerjaChoice.equals("Bekerja")){
            bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBekerja.setVisibility(View.VISIBLE);
            markTidakBekerja.setVisibility(View.GONE);
        } else if (statusBekerjaChoice.equals("Belum/Tidak Bekerja")){
            bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
            markBekerja.setVisibility(View.GONE);
            markTidakBekerja.setVisibility(View.VISIBLE);
        } else {
            bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
            markBekerja.setVisibility(View.GONE);
            markTidakBekerja.setVisibility(View.GONE);
        }

        bekerjaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markBekerja.setVisibility(View.VISIBLE);
                markTidakBekerja.setVisibility(View.GONE);
                bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                statusBekerjaChoice = "Bekerja";
                statusBekerjaPilihTV.setText("Bekerja");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

        tidakBekerjaBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                markBekerja.setVisibility(View.GONE);
                markTidakBekerja.setVisibility(View.VISIBLE);
                bekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option));
                tidakBekerjaBTN.setBackground(ContextCompat.getDrawable(FormInfoKeluargaActivity.this, R.drawable.shape_option_choice));
                statusBekerjaChoice = "Belum/Tidak Bekerja";
                statusBekerjaPilihTV.setText("Belum/Tidak Bekerja");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheet.dismissSheet();
                    }
                }, 300);
            }
        });

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

    public void onBackPressed() {
        if(bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}