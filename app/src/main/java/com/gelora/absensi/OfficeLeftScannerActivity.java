package com.gelora.absensi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.CustomDecoratedBarcodeView;
import com.gelora.absensi.support.EncryptionUtils;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfficeLeftScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private CustomDecoratedBarcodeView barcodeView;
    private StatusBarColorManager mStatusBarColorManager;
    LinearLayout btnFlashOff, btnFlashOn, backBTN, okBTN;
    RelativeLayout successPart, scannerPart;
    TextView descSuccess;
    private boolean isFlashOn = false;
    RequestQueue requestQueue;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    KAlertDialog pDialog;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_left_scanner);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        barcodeView = findViewById(R.id.barcode_scanner);
        btnFlashOff = findViewById(R.id.flash_off_btn);
        btnFlashOn = findViewById(R.id.flash_on_btn);
        backBTN = findViewById(R.id.back_btn);
        successPart = findViewById(R.id.success_part);
        scannerPart = findViewById(R.id.scanner_part);
        descSuccess = findViewById(R.id.desc_success);
        okBTN = findViewById(R.id.ok_btn);

        backBTN.setOnClickListener(view -> onBackPressed());
        btnFlashOn.setOnClickListener(view -> toggleFlash());
        btnFlashOff.setOnClickListener(view -> toggleFlash());
        okBTN.setOnClickListener(view -> {
            scannerPart.setVisibility(View.VISIBLE);
            successPart.setVisibility(View.GONE);
            barcodeView.resume();
            barcodeView.hideStatusText();
        });

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
                        pDialog = new KAlertDialog(OfficeLeftScannerActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                        pDialog.show();
                        pDialog.setCancelable(false);
                        new CountDownTimer(1300, 800) {
                            public void onTick(long millisUntilFinished) {
                                i++;
                                switch (i) {
                                    case 0:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (OfficeLeftScannerActivity.this, R.color.colorGradien));
                                        break;
                                    case 1:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (OfficeLeftScannerActivity.this, R.color.colorGradien2));
                                        break;
                                    case 2:
                                    case 6:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (OfficeLeftScannerActivity.this, R.color.colorGradien3));
                                        break;
                                    case 3:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (OfficeLeftScannerActivity.this, R.color.colorGradien4));
                                        break;
                                    case 4:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (OfficeLeftScannerActivity.this, R.color.colorGradien5));
                                        break;
                                    case 5:
                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                (OfficeLeftScannerActivity.this, R.color.colorGradien6));
                                        break;
                                }
                            }
                            public void onFinish() {
                                i = -1;
                                decryptCode(result.getText());
                            }
                        }.start();
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
        if (pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
        }
        scannerPart.setVisibility(View.VISIBLE);
        successPart.setVisibility(View.GONE);
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

    private void decryptCode(String result){
        String[] parts = result.split(",");
        try {
            String encryptedData = parts[0];
            String inValidMessage = parts[1];
            if(encryptedData.equals("iOS")){
                verificationBack(inValidMessage);
            } else {
                String decryptedData = EncryptionUtils.decrypt(encryptedData);
                try {
                    JSONObject jsonObject = new JSONObject(decryptedData);
                    String key = jsonObject.getString("key");
                    String approvalId = jsonObject.getString("id");
                    String nikPemohon = jsonObject.getString("nik");
                    if (parts.length == 2 && key.equals("hris_ijin_keluar")) {
                        verificationBack(approvalId);
                    } else {
                        pDialog.dismiss();
                        pDialog = new KAlertDialog(OfficeLeftScannerActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("QR Code tidak valid")
                                .setConfirmText("COBA LAGI");
                        pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                                barcodeView.resume();
                            }
                        });
                        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                                barcodeView.resume();
                            }
                        });
                        pDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            pDialog.dismiss();
            pDialog = new KAlertDialog(OfficeLeftScannerActivity.this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Perhatian")
                    .setContentText("QR Code tidak valid")
                    .setConfirmText("COBA LAGI");
            pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog sDialog) {
                    sDialog.dismiss();
                    barcodeView.resume();
                }
            });
            pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog.dismiss();
                    barcodeView.resume();
                }
            });
            pDialog.show();
        }
    }

    public void verificationBack(String id){
        final String url = "https://hrisgelora.co.id/api/verifikasi_kembali_satpam_izin_keluar_kantor";
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
                                String nik = data.getString("nik");
                                String nama = data.getString("nama");
                                pDialog.dismiss();
                                scannerPart.setVisibility(View.GONE);
                                successPart.setVisibility(View.VISIBLE);
                                descSuccess.setText("Verifikasi kembali dari permohonan izin keluar kantor atas nama "+nama+" dengan NIK "+nik+" berhasil diverifikasi.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.setTitleText("Gagal")
                                    .setContentText("Terjadi kesalahan")
                                    .setConfirmText("    OK    ")
                                    .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", id);
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("jam_kembali", getTime());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(OfficeLeftScannerActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
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
