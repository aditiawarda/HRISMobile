package com.gelora.absensi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gelora.absensi.support.CustomDecoratedBarcodeView;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

public class OfficeLeftScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private CustomDecoratedBarcodeView barcodeView;
    private StatusBarColorManager mStatusBarColorManager;
    LinearLayout btnFlashOff, btnFlashOn, backBTN;
    private boolean isFlashOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_left_scanner);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        barcodeView = findViewById(R.id.barcode_scanner);
        btnFlashOff = findViewById(R.id.flash_off_btn);
        btnFlashOn = findViewById(R.id.flash_on_btn);
        backBTN = findViewById(R.id.back_btn);

        backBTN.setOnClickListener(view -> onBackPressed());
        btnFlashOn.setOnClickListener(view -> toggleFlash());
        btnFlashOff.setOnClickListener(view -> toggleFlash());

        if (ContextCompat.checkSelfPermission(OfficeLeftScannerActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OfficeLeftScannerActivity.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            barcodeView.decodeContinuous(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    if (result.getText() != null) {
                        barcodeView.pause();
                        Toast.makeText(OfficeLeftScannerActivity.this, "Scanned: " + result.getText(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void possibleResultPoints(List<ResultPoint> resultPoints) {
                    // Handle possible result points if needed
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
        barcodeView.hideStatusText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    private void toggleFlash() {
        if (isFlashOn) {
            barcodeView.setTorchOff();
            isFlashOn = false;
            btnFlashOn.setVisibility(View.VISIBLE);
            btnFlashOff.setVisibility(View.GONE);
        } else {
            barcodeView.setTorchOn();
            isFlashOn = true;
            btnFlashOff.setVisibility(View.VISIBLE);
            btnFlashOn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView.resume();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_LONG).show();
            }
        }
    }
}
