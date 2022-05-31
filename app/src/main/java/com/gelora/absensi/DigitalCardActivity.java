package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.shasin.notificationbanner.Banner;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DigitalCardActivity extends AppCompatActivity {

    TextView namaKaryawan, nikKaryawan;
    String namaString, nikString, dataDiri;
    View rootview;
    ImageView qrKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_card);

        rootview = findViewById(android.R.id.content);
        namaKaryawan = findViewById(R.id.nama_karyawan);
        nikKaryawan = findViewById(R.id.nik_karyawan);
        qrKaryawan = findViewById(R.id.qr_karyawan);

        namaString = getIntent().getExtras().getString("nama");
        nikString = getIntent().getExtras().getString("nik");

        namaKaryawan.setText(namaString.toUpperCase());
        nikKaryawan.setText("NIK. "+nikString);

        dataDiri = namaString+" - "+nikString;

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

}