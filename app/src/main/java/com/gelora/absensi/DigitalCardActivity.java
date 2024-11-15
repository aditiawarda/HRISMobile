package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelora.absensi.kalert.KAlertDialog;
import com.google.zxing.WriterException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.aviran.cookiebar2.CookieBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DigitalCardActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    TextView namaKaryawan, nikKaryawan;
    String namaString, nikString, dataDiri, uriImage;
    View rootview;
    ImageView qrKaryawan;
    LinearLayout headerGAP, headerErlass, footerGAP, footerErlass, backBTN, captureBTN, digitalCard;
    private int i = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_card);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        namaKaryawan = findViewById(R.id.nama_karyawan);
        nikKaryawan = findViewById(R.id.nik_karyawan);
        qrKaryawan = findViewById(R.id.qr_karyawan);
        captureBTN = findViewById(R.id.capture_btn);
        digitalCard = findViewById(R.id.main_content);
        backBTN = findViewById(R.id.back_btn);
        headerGAP = findViewById(R.id.gap_header);
        headerErlass = findViewById(R.id.erlass_header);
        footerGAP = findViewById(R.id.gap_footer);
        footerErlass = findViewById(R.id.erlass_footer);

        namaString = getIntent().getExtras().getString("nama");
        nikString = getIntent().getExtras().getString("nik");

        namaKaryawan.setText(namaString.toUpperCase());
        nikKaryawan.setText("NIK. "+nikString);

        dataDiri = namaString+" - "+nikString;

        if(sharedPrefManager.getSpIdCor().equals("1")){
            headerGAP.setVisibility(View.VISIBLE);
            footerGAP.setVisibility(View.VISIBLE);
            headerErlass.setVisibility(View.GONE);
            footerErlass.setVisibility(View.GONE);
        } else if(sharedPrefManager.getSpIdCor().equals("3")){
            headerGAP.setVisibility(View.GONE);
            footerGAP.setVisibility(View.GONE);
            headerErlass.setVisibility(View.VISIBLE);
            footerErlass.setVisibility(View.VISIBLE);
        }

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        captureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DigitalCardActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Unduh ID Card Digital?")
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
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    Dexter.withActivity(DigitalCardActivity.this)
                                            .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                                            .withListener(new MultiplePermissionsListener() {
                                                @Override
                                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                    if (report.areAllPermissionsGranted()) {
                                                        File file = saveBitMap(DigitalCardActivity.this, digitalCard);    //which view you want to pass that view as parameter
                                                        if (file != null) {
                                                            final KAlertDialog pDialog = new KAlertDialog(DigitalCardActivity.this, KAlertDialog.PROGRESS_TYPE)
                                                                    .setTitleText("Loading");
                                                            pDialog.show();
                                                            pDialog.setCancelable(false);
                                                            new CountDownTimer(2000, 1000) {
                                                                public void onTick(long millisUntilFinished) {
                                                                    i++;
                                                                    switch (i) {
                                                                        case 0:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien));
                                                                            break;
                                                                        case 1:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien2));
                                                                            break;
                                                                        case 2:
                                                                        case 6:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien3));
                                                                            break;
                                                                        case 3:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien4));
                                                                            break;
                                                                        case 4:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien5));
                                                                            break;
                                                                        case 5:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien6));
                                                                            break;
                                                                    }
                                                                }
                                                                public void onFinish() {
                                                                    i = -1;
                                                                    pDialog.setTitleText("Unduh Berhasil")
                                                                            .setContentText("ID Card berhasil diunduh dan disimpan")
                                                                            .setCancelText("TUTUP")
                                                                            .setConfirmText("LIHAT")
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
                                                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriImage));
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                                                }
                                                            }.start();

                                                            Log.i("TAG", "Drawing saved to the gallery!");
                                                        }
                                                        else {
                                                            new KAlertDialog(DigitalCardActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Terjadi kesalahan!")
                                                                    .setConfirmText("TUTUP")
                                                                    .show();

                                                            Log.i("TAG", "Oops! Image could not be saved.");
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                                    token.continuePermissionRequest();
                                                }
                                            }).check();
                                } else {
                                    Dexter.withActivity(DigitalCardActivity.this)
                                            .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            .withListener(new MultiplePermissionsListener() {
                                                @Override
                                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                    if (report.areAllPermissionsGranted()) {
                                                        File file = saveBitMap(DigitalCardActivity.this, digitalCard);    //which view you want to pass that view as parameter
                                                        if (file != null) {
                                                            final KAlertDialog pDialog = new KAlertDialog(DigitalCardActivity.this, KAlertDialog.PROGRESS_TYPE)
                                                                    .setTitleText("Loading");
                                                            pDialog.show();
                                                            pDialog.setCancelable(false);
                                                            new CountDownTimer(2000, 1000) {
                                                                public void onTick(long millisUntilFinished) {
                                                                    i++;
                                                                    switch (i) {
                                                                        case 0:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien));
                                                                            break;
                                                                        case 1:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien2));
                                                                            break;
                                                                        case 2:
                                                                        case 6:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien3));
                                                                            break;
                                                                        case 3:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien4));
                                                                            break;
                                                                        case 4:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien5));
                                                                            break;
                                                                        case 5:
                                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                                    (DigitalCardActivity.this, R.color.colorGradien6));
                                                                            break;
                                                                    }
                                                                }
                                                                public void onFinish() {
                                                                    i = -1;
                                                                    pDialog.setTitleText("Unduh Berhasil")
                                                                            .setContentText("ID Card berhasil diunduh dan disimpan")
                                                                            .setCancelText("TUTUP")
                                                                            .setConfirmText("LIHAT")
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
                                                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriImage));
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                                                }
                                                            }.start();

                                                            Log.i("TAG", "Drawing saved to the gallery!");
                                                        }
                                                        else {
                                                            new KAlertDialog(DigitalCardActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Terjadi kesalahan!")
                                                                    .setConfirmText("TUTUP")
                                                                    .show();

                                                            Log.i("TAG", "Oops! Image could not be saved.");
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                                    token.continuePermissionRequest();
                                                }
                                            }).check();
                                }
                            }
                        })
                        .show();

            }
        });

        generateQRCode();

    }

    private void generateQRCode(){
        if (TextUtils.isEmpty(dataDiri)){
            CookieBar.build(DigitalCardActivity.this)
                    .setTitle("Perhatian")
                    .setMessage("QR Code gagal di generate!")
                    .setTitleColor(R.color.colorPrimaryDark)
                    .setMessageColor(R.color.colorPrimaryDark)
                    .setBackgroundColor(R.color.warningBottom)
                    .setIcon(R.drawable.warning_connection_mini)
                    .setCookiePosition(CookieBar.BOTTOM)
                    .show();

            return;
        }
        try {
            QRGEncoder qrgEncoder = new QRGEncoder(dataDiri,null, QRGContents.Type.TEXT,200);
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qrKaryawan.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private File saveBitMap(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"HRIS Mobile");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated) {
                Log.i("ATG", "Can't create directory to save the image");
                new KAlertDialog(DigitalCardActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Terjadi kesalahan!")
                        .setConfirmText("TUTUP")
                        .show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withActivity(DigitalCardActivity.this)
                            .withPermissions(Manifest.permission.READ_MEDIA_IMAGES)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        File file = saveBitMap(DigitalCardActivity.this, digitalCard);    //which view you want to pass that view as parameter
                                        if (file != null) {
                                            final KAlertDialog pDialog = new KAlertDialog(DigitalCardActivity.this, KAlertDialog.PROGRESS_TYPE)
                                                    .setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(2000, 1000) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    pDialog.setTitleText("Unduh Berhasil")
                                                            .setContentText("ID Card berhasil diunduh dan disimpan")
                                                            .setCancelText("TUTUP")
                                                            .setConfirmText("LIHAT")
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
                                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriImage));
                                                                    startActivity(intent);
                                                                }
                                                            })
                                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                                }
                                            }.start();

                                            Log.i("TAG", "Drawing saved to the gallery!");
                                        }
                                        else {
                                            Log.i("TAG", "Oops! Image could not be saved.");
                                        }
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                } else {
                    Dexter.withActivity(DigitalCardActivity.this)
                            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        File file = saveBitMap(DigitalCardActivity.this, digitalCard);    //which view you want to pass that view as parameter
                                        if (file != null) {
                                            final KAlertDialog pDialog = new KAlertDialog(DigitalCardActivity.this, KAlertDialog.PROGRESS_TYPE)
                                                    .setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(2000, 1000) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (DigitalCardActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    pDialog.setTitleText("Unduh Berhasil")
                                                            .setContentText("ID Card berhasil diunduh dan disimpan")
                                                            .setCancelText("TUTUP")
                                                            .setConfirmText("LIHAT")
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
                                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriImage));
                                                                    startActivity(intent);
                                                                }
                                                            })
                                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                                }
                                            }.start();

                                            Log.i("TAG", "Drawing saved to the gallery!");
                                        }
                                        else {
                                            Log.i("TAG", "Oops! Image could not be saved.");
                                        }
                                    }
                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }
            }
            return null;
        }

        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream); //relative
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context,pictureFile.getAbsolutePath());
        return pictureFile;
    }


    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    uriImage = String.valueOf(uri);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}