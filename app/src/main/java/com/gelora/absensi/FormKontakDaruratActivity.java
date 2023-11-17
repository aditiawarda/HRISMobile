package com.gelora.absensi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gelora.absensi.kalert.KAlertDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormKontakDaruratActivity extends AppCompatActivity {

    LinearLayout kontakBTN, actionBar, backSuccessBTN, submitBTN, backBTN, hubunganBTN, hubunganLainnyaPart, formPart, successPart;
    LinearLayout sodaraLakiBTN, sodaraPerempuanBTN, markSodaraLaki, markSodaraPerempuan, ayahBTN, ibuBTN, suamiBTN, istriBTN, anakBTN, lainnyaBTN, markAyah, markIbu, markSuami, markIstri, markAnak, markLainnya;
    SwipeRefreshLayout refreshLayout;
    TextView hubunganPilihTV, titlePageTV, kontakPilihTV;
    EditText namaED, hubunganLainnyaED;
    BottomSheetLayout bottomSheet;
    String hubunganPilih = "", tipeForm = "", idKontak = "", noHandphone = "";
    KAlertDialog pDialog;
    SharedPrefManager sharedPrefManager;
    ImageView successGif;
    private int i = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kontak_darurat);

        sharedPrefManager = new SharedPrefManager(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        backSuccessBTN = findViewById(R.id.back_success_btn);
        titlePageTV = findViewById(R.id.title_page_tv);
        namaED = findViewById(R.id.nama_ed);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        hubunganPilihTV = findViewById(R.id.hubungan_pilih_tv);
        hubunganBTN = findViewById(R.id.hubungan_btn);
        hubunganLainnyaPart = findViewById(R.id.hubungan_lainnya_part);
        hubunganLainnyaED = findViewById(R.id.hubungan_lainnya_ed);
        kontakBTN = findViewById(R.id.kontak_btn);
        kontakPilihTV = findViewById(R.id.kontak_pilih_tv);
        formPart = findViewById(R.id.form_part);
        successPart = findViewById(R.id.success_submit);
        submitBTN = findViewById(R.id.submit_btn);
        actionBar = findViewById(R.id.action_bar);

        successGif = findViewById(R.id.success_gif);

        Glide.with(getApplicationContext())
                .load(R.drawable.success_ic)
                .into(successGif);

        namaED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hubunganLainnyaED.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        tipeForm = getIntent().getExtras().getString("tipe");

        if(tipeForm.equals("edit")){
            idKontak = getIntent().getExtras().getString("id_kontak");
            titlePageTV.setText("EDIT KONTAK DARURAT");
            getData();
        } else {
            titlePageTV.setText("FORM KONTAK DARURAT");
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
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

                if(tipeForm.equals("edit")){
                    getData();
                } else {
                    namaED.setText("");
                    kontakPilihTV.setText("");
                    hubunganPilihTV.setText("");
                    hubunganLainnyaED.setText("");
                    noHandphone = "";
                    hubunganPilih = "";
                    hubunganLainnyaPart.setVisibility(View.GONE);
                }

                namaED.clearFocus();
                hubunganLainnyaED.clearFocus();

                new Handler().postDelayed(new Runnable() {
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

        backSuccessBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        kontakBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                namaED.clearFocus();
                hubunganLainnyaED.clearFocus();

                Dexter.withActivity(FormKontakDaruratActivity.this)
                        .withPermissions(Manifest.permission.READ_CONTACTS)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                                    startActivityForResult(contactPickerIntent, 1);
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
        });

        hubunganBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                namaED.clearFocus();
                hubunganLainnyaED.clearFocus();
                hubunganChoice();
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                namaED.clearFocus();
                hubunganLainnyaED.clearFocus();
                if(namaED.getText().toString().equals("") || noHandphone.equals("") || hubunganPilih.equals("")){
                    new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.ERROR_TYPE)
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
                } else {
                    if (hubunganPilih.equals("Lainnya")) {
                        if(hubunganLainnyaED.getText().toString().equals("")){
                            new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.ERROR_TYPE)
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
                        } else {
                            new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Simpan data kontak?")
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
                                            pDialog = new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormKontakDaruratActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormKontakDaruratActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormKontakDaruratActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormKontakDaruratActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormKontakDaruratActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (FormKontakDaruratActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    sendData();
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    } else {
                        new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Simpan data kontak?")
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
                                        pDialog = new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormKontakDaruratActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormKontakDaruratActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormKontakDaruratActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormKontakDaruratActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormKontakDaruratActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (FormKontakDaruratActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }
                                            public void onFinish() {
                                                i = -1;
                                                sendData();
                                            }
                                        }.start();

                                    }
                                })
                                .show();
                    }

                }
            }
        });

    }

    @SuppressLint({"Range", "LongLogTag", "Recycle", "SetTextI18n"})
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Cursor cursor = null;
                    try {
                        String phoneNo = null;
                        String name = null;

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        phoneNo = cursor.getString(phoneIndex);
                        name = cursor.getString(nameIndex);

                        String noPhone = phoneNo.replace("+", "").replace("-", "").replace(" ","");

                        if (noPhone.substring(0,2).equals("62")){
                            kontakPilihTV.setText("0"+noPhone.substring(2,noPhone.length()));
                            noHandphone = "0"+noPhone.substring(2,noPhone.length());
                        } else {
                            kontakPilihTV.setText(noPhone);
                            noHandphone = noPhone;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.e("Failed", "Not able to pick contact");
        }
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(FormKontakDaruratActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message_2));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void hubunganChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(FormKontakDaruratActivity.this).inflate(R.layout.layout_hubungan, bottomSheet, false));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
            ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
            lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
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
                ayahBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                ibuBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                suamiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                istriBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                anakBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraLakiBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                sodaraPerempuanBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option));
                lainnyaBTN.setBackground(ContextCompat.getDrawable(FormKontakDaruratActivity.this, R.drawable.shape_option_choice));
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

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/upload_data_kontak";
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
                                pDialog.dismiss();
                                successPart.setVisibility(View.VISIBLE);
                                formPart.setVisibility(View.GONE);
                            } else {
                                successPart.setVisibility(View.GONE);
                                formPart.setVisibility(View.VISIBLE);
                                pDialog.setTitleText("Gagal Tersimpan")
                                        .setContentText("Terjadi kesalahan saat mengirim data")
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
                        successPart.setVisibility(View.GONE);
                        formPart.setVisibility(View.VISIBLE);
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tipe", tipeForm);

                if(tipeForm.equals("edit")){
                    params.put("id_kontak", idKontak);
                }

                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("nama_kontak", namaED.getText().toString());
                params.put("notelp", noHandphone);

                if(hubunganPilih.equals("Lainnya")){
                    params.put("hubungan", hubunganPilih);
                    params.put("hubungan_lainnya", hubunganLainnyaED.getText().toString());
                } else {
                    params.put("hubungan", hubunganPilih);
                }

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

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_detail_kontak_edit";
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
                                String id = dataArray.getString("id");
                                String NIK = dataArray.getString("NIK");
                                String notelp = dataArray.getString("notelp");
                                String nama_kontak = dataArray.getString("nama_kontak");
                                String hubungan = dataArray.getString("hubungan");
                                String hubungan_lainnya = dataArray.getString("hubungan_lainnya");

                                namaED.setText(nama_kontak);
                                kontakPilihTV.setText(notelp);
                                noHandphone = notelp;

                                hubunganPilih = hubungan;
                                hubunganPilihTV.setText(hubungan);
                                if(hubungan.equals("Lainnya")){
                                    hubunganLainnyaPart.setVisibility(View.VISIBLE);
                                    hubunganLainnyaED.setText(hubungan_lainnya);
                                } else {
                                    hubunganLainnyaPart.setVisibility(View.GONE);
                                }

                            } else {
                                new KAlertDialog(FormKontakDaruratActivity.this, KAlertDialog.ERROR_TYPE)
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
                params.put("id_kontak", idKontak);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormKontakDaruratActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    public void onBackPressed() {
        if(bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

}