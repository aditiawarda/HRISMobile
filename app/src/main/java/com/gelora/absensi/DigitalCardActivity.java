package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gelora.absensi.kalert.KAlertDialog;
import com.google.zxing.WriterException;
import com.shasin.notificationbanner.Banner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DigitalCardActivity extends AppCompatActivity {

    TextView namaKaryawan, nikKaryawan;
    String namaString, nikString, dataDiri, uriImage;
    View rootview;
    ImageView qrKaryawan;
    LinearLayout captureBTN, digitalCard;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_card);

        rootview = findViewById(android.R.id.content);
        namaKaryawan = findViewById(R.id.nama_karyawan);
        nikKaryawan = findViewById(R.id.nik_karyawan);
        qrKaryawan = findViewById(R.id.qr_karyawan);
        captureBTN = findViewById(R.id.capture_btn);
        digitalCard = findViewById(R.id.main_content);

        namaString = getIntent().getExtras().getString("nama");
        nikString = getIntent().getExtras().getString("nik");

        namaKaryawan.setText(namaString.toUpperCase());
        nikKaryawan.setText("NIK. "+nikString);

        dataDiri = namaString+" - "+nikString;

        captureBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DigitalCardActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Unduh ID Card Digital?")
                        .setCancelText("NO")
                        .setConfirmText("YES")
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
                                } else {
                                    Log.i("TAG", "Oops! Image could not be saved.");
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
            Banner.make(rootview,DigitalCardActivity.this,Banner.ERROR,"QR Code gagal di generate",Banner.BOTTOM,2000).show();
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
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Absensi App");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        //Bitmap resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
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
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }


    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
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