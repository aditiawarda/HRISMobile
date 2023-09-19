package com.gelora.absensi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormExitClearanceActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN, resignDateBTN, submitBTN;

    LinearLayout st1UploadBTN, st1UploadView;
    TextView st1FileTV, st1UploadIc, st1UploadIcChange;

    LinearLayout st2UploadBTN, st2UploadView;
    TextView st2FileTV, st2UploadIc, st2UploadIcChange;

    LinearLayout st3UploadBTN, st3UploadView;
    TextView st3FileTV, st3UploadIc, st3UploadIcChange;

    LinearLayout st4UploadBTN, st4UploadView;
    TextView st4FileTV, st4UploadIc, st4UploadIcChange;

    LinearLayout st5UploadBTN, st5UploadView;
    TextView st5FileTV, st5UploadIc, st5UploadIcChange;

    LinearLayout st6UploadBTN, st6UploadView;
    TextView st6FileTV, st6UploadIc, st6UploadIcChange;

    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    TextView namaTV, nikTV, detailTV, tanggalMasukTV, tanggalResignTV;
    String tanggalResign = "", permohonanTerkirim = "";

    // In your activity or fragment
    private static final int PICK_PDF_ST_1 = 1;
    private static final int PICK_PDF_ST_2 = 2;
    private static final int PICK_PDF_ST_3 = 3;
    private static final int PICK_PDF_ST_4 = 4;
    private static final int PICK_PDF_ST_5 = 5;
    private static final int PICK_PDF_ST_6 = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_exit_clearance);

        sharedPrefManager = new SharedPrefManager(this);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        namaTV = findViewById(R.id.nama_karyawan_tv);
        nikTV = findViewById(R.id.nik_karyawan_tv);
        detailTV = findViewById(R.id.detail_karyawan_tv);
        tanggalMasukTV = findViewById(R.id.tanggal_masuk_tv);
        resignDateBTN = findViewById(R.id.resign_date_btn);
        tanggalResignTV = findViewById(R.id.resign_date_pilih);
        submitBTN = findViewById(R.id.submit_btn);

        st1UploadBTN = findViewById(R.id.st_1_upload_btn);
        st1FileTV = findViewById(R.id.st_1_file_tv);
        st1UploadView = findViewById(R.id.st_1_upload_view);
        st1UploadIc = findViewById(R.id.st_1_upload_ic);
        st1UploadIcChange = findViewById(R.id.st_1_upload_ic_change);

        st2UploadBTN = findViewById(R.id.st_2_upload_btn);
        st2FileTV = findViewById(R.id.st_2_file_tv);
        st2UploadView = findViewById(R.id.st_2_upload_view);
        st2UploadIc = findViewById(R.id.st_2_upload_ic);
        st2UploadIcChange = findViewById(R.id.st_2_upload_ic_change);

        st3UploadBTN = findViewById(R.id.st_3_upload_btn);
        st3FileTV = findViewById(R.id.st_3_file_tv);
        st3UploadView = findViewById(R.id.st_3_upload_view);
        st3UploadIc = findViewById(R.id.st_3_upload_ic);
        st3UploadIcChange = findViewById(R.id.st_3_upload_ic_change);

        st4UploadBTN = findViewById(R.id.st_4_upload_btn);
        st4FileTV = findViewById(R.id.st_4_file_tv);
        st4UploadView = findViewById(R.id.st_4_upload_view);
        st4UploadIc = findViewById(R.id.st_4_upload_ic);
        st4UploadIcChange = findViewById(R.id.st_4_upload_ic_change);

        st5UploadBTN = findViewById(R.id.st_5_upload_btn);
        st5FileTV = findViewById(R.id.st_5_file_tv);
        st5UploadView = findViewById(R.id.st_5_upload_view);
        st5UploadIc = findViewById(R.id.st_5_upload_ic);
        st5UploadIcChange = findViewById(R.id.st_5_upload_ic_change);

        st6UploadBTN = findViewById(R.id.st_6_upload_btn);
        st6FileTV = findViewById(R.id.st_6_file_tv);
        st6UploadView = findViewById(R.id.st_6_upload_view);
        st6UploadIc = findViewById(R.id.st_6_upload_ic);
        st6UploadIcChange = findViewById(R.id.st_6_upload_ic_change);

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);

                        tanggalResign = "";
                        tanggalResignTV.setText("");

                        st1FileTV.setText("Unggah PDF...");
                        st1UploadView.setVisibility(View.GONE);
                        st1UploadIc.setVisibility(View.VISIBLE);
                        st1UploadIcChange.setVisibility(View.GONE);

                        st2FileTV.setText("Unggah PDF...");
                        st2UploadView.setVisibility(View.GONE);
                        st2UploadIc.setVisibility(View.VISIBLE);
                        st2UploadIcChange.setVisibility(View.GONE);

                        st3FileTV.setText("Unggah PDF...");
                        st3UploadView.setVisibility(View.GONE);
                        st3UploadIc.setVisibility(View.VISIBLE);
                        st3UploadIcChange.setVisibility(View.GONE);

                        st4FileTV.setText("Unggah PDF...");
                        st4UploadView.setVisibility(View.GONE);
                        st4UploadIc.setVisibility(View.VISIBLE);
                        st4UploadIcChange.setVisibility(View.GONE);

                        st5FileTV.setText("Unggah PDF...");
                        st5UploadView.setVisibility(View.GONE);
                        st5UploadIc.setVisibility(View.VISIBLE);
                        st5UploadIcChange.setVisibility(View.GONE);

                        st6FileTV.setText("Unggah PDF...");
                        st6UploadView.setVisibility(View.GONE);
                        st6UploadIc.setVisibility(View.VISIBLE);
                        st6UploadIcChange.setVisibility(View.GONE);

                        getDataKaryawan();
                    }
                }, 800);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resignDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resignDate();
            }
        });

        st1UploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormExitClearanceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormExitClearanceActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "PDF - 1"), PICK_PDF_ST_1);
                }

            }
        });

        st2UploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormExitClearanceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormExitClearanceActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "PDF - 2"), PICK_PDF_ST_2);
                }

            }
        });

        st3UploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormExitClearanceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormExitClearanceActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "PDF - 3"), PICK_PDF_ST_3);
                }

            }
        });

        st4UploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormExitClearanceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormExitClearanceActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "PDF - 4"), PICK_PDF_ST_4);
                }

            }
        });

        st5UploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormExitClearanceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormExitClearanceActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "PDF - 5"), PICK_PDF_ST_5);
                }

            }
        });

        st6UploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormExitClearanceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FormExitClearanceActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "PDF - 6"), PICK_PDF_ST_6);
                }

            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tanggalResign.equals("")){
                    new KAlertDialog(FormExitClearanceActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Harap isi tanggal keluar!")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(FormExitClearanceActivity.this, "Submit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getDataKaryawan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PDF_ST_1) {
                Uri selectedPdfUri = data.getData();
                String pdfFilePath = selectedPdfUri.getPath();
                processDocument(selectedPdfUri, "1");
            } else if (requestCode == PICK_PDF_ST_2) {
                Uri selectedPdfUri = data.getData();
                String pdfFilePath = selectedPdfUri.getPath();
                processDocument(selectedPdfUri, "2");
            } else if (requestCode == PICK_PDF_ST_3) {
                Uri selectedPdfUri = data.getData();
                String pdfFilePath = selectedPdfUri.getPath();
                processDocument(selectedPdfUri, "3");
            } else if (requestCode == PICK_PDF_ST_4) {
                Uri selectedPdfUri = data.getData();
                String pdfFilePath = selectedPdfUri.getPath();
                processDocument(selectedPdfUri, "4");
            } else if (requestCode == PICK_PDF_ST_5) {
                Uri selectedPdfUri = data.getData();
                String pdfFilePath = selectedPdfUri.getPath();
                processDocument(selectedPdfUri, "5");
            } else if (requestCode == PICK_PDF_ST_6) {
                Uri selectedPdfUri = data.getData();
                String pdfFilePath = selectedPdfUri.getPath();
                processDocument(selectedPdfUri, "6");
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void resignDate(){
        int y = Integer.parseInt(getDateY());
        int m = Integer.parseInt(getDateM());
        int d = Integer.parseInt(getDateD());

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        DatePickerDialog dpd = new DatePickerDialog(FormExitClearanceActivity.this, R.style.datePickerStyle, (view1, year, month, dayOfMonth) -> {

            tanggalResign = String.format("%d", year)+"-"+String.format("%02d", month + 1)+"-"+String.format("%02d", dayOfMonth);

            String dayDate = tanggalResign.substring(8,10);
            String yearDate = tanggalResign.substring(0,4);
            String bulanValue = tanggalResign.substring(5,7);
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

            tanggalResignTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

        }, y,m-1,d);
        dpd.show();

    }

    private void processDocument(Uri contentUri, String code) {
        ContentResolver contentResolver = getContentResolver();

        // Use a cursor to get the file's display name and MIME type
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        String displayName = null;
        String mimeType = null;
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int mimeTypeIndex = cursor.getColumnIndex("mime_type"); // Use "mime_type" for MIME type
            if (nameIndex != -1) {
                displayName = cursor.getString(nameIndex);
            }
            if (mimeTypeIndex != -1) {
                mimeType = cursor.getString(mimeTypeIndex);
            }
            cursor.close();
        }

        // Create a temporary file to store the content
        File tempFile;
        File path;
        try {
            path = this.getExternalCacheDir();
            if (!path.exists()) path.mkdirs();
            tempFile = File.createTempFile("temp_", ".pdf", path.getAbsoluteFile());
            InputStream inputStream = contentResolver.openInputStream(contentUri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if(code.equals("1")){
            st1FileTV.setText(displayName);
            if (tempFile.exists()) {
                st1UploadView.setVisibility(View.VISIBLE);
                st1UploadIc.setVisibility(View.GONE);
                st1UploadIcChange.setVisibility(View.VISIBLE);
                String finalMimeType = mimeType;
                st1UploadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri fileUri = Uri.parse(String.valueOf(tempFile));
                        Intent intent = new Intent(FormExitClearanceActivity.this, PdfViewerActivity.class);
                        intent.putExtra("kode_st", "1");
                        intent.putExtra("uri", String.valueOf(tempFile));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        } else if(code.equals("2")){
            st2FileTV.setText(displayName);
            if (tempFile.exists()) {
                st2UploadIc.setVisibility(View.GONE);
                st2UploadIcChange.setVisibility(View.VISIBLE);
                st2UploadView.setVisibility(View.VISIBLE);
                String finalMimeType = mimeType;
                st2UploadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri fileUri = Uri.parse(String.valueOf(tempFile));
                        Intent intent = new Intent(FormExitClearanceActivity.this, PdfViewerActivity.class);
                        intent.putExtra("kode_st", "2");
                        intent.putExtra("uri", String.valueOf(tempFile));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        } else if(code.equals("3")){
            st3FileTV.setText(displayName);
            if (tempFile.exists()) {
                st3UploadIc.setVisibility(View.GONE);
                st3UploadIcChange.setVisibility(View.VISIBLE);
                st3UploadView.setVisibility(View.VISIBLE);
                String finalMimeType = mimeType;
                st3UploadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri fileUri = Uri.parse(String.valueOf(tempFile));
                        Intent intent = new Intent(FormExitClearanceActivity.this, PdfViewerActivity.class);
                        intent.putExtra("kode_st", "3");
                        intent.putExtra("uri", String.valueOf(tempFile));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        } else if(code.equals("4")){
            st4FileTV.setText(displayName);
            if (tempFile.exists()) {
                st4UploadIc.setVisibility(View.GONE);
                st4UploadIcChange.setVisibility(View.VISIBLE);
                st4UploadView.setVisibility(View.VISIBLE);
                String finalMimeType = mimeType;
                st4UploadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri fileUri = Uri.parse(String.valueOf(tempFile));
                        Intent intent = new Intent(FormExitClearanceActivity.this, PdfViewerActivity.class);
                        intent.putExtra("kode_st", "4");
                        intent.putExtra("uri", String.valueOf(tempFile));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        } else if(code.equals("5")){
            st5FileTV.setText(displayName);
            if (tempFile.exists()) {
                st5UploadIc.setVisibility(View.GONE);
                st5UploadIcChange.setVisibility(View.VISIBLE);
                st5UploadView.setVisibility(View.VISIBLE);
                String finalMimeType = mimeType;
                st5UploadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri fileUri = Uri.parse(String.valueOf(tempFile));
                        Intent intent = new Intent(FormExitClearanceActivity.this, PdfViewerActivity.class);
                        intent.putExtra("kode_st", "5");
                        intent.putExtra("uri", String.valueOf(tempFile));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        } else if(code.equals("6")){
            st6FileTV.setText(displayName);
            if (tempFile.exists()) {
                st6UploadIc.setVisibility(View.GONE);
                st6UploadIcChange.setVisibility(View.VISIBLE);
                st6UploadView.setVisibility(View.VISIBLE);
                String finalMimeType = mimeType;
                st6UploadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri fileUri = Uri.parse(String.valueOf(tempFile));
                        Intent intent = new Intent(FormExitClearanceActivity.this, PdfViewerActivity.class);
                        intent.putExtra("kode_st", "6");
                        intent.putExtra("uri", String.valueOf(tempFile));
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
            }
        }

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

    private void getDataKaryawan(){
        namaTV.setText(sharedPrefManager.getSpNama().toUpperCase());
        nikTV.setText(sharedPrefManager.getSpNik());
        getDataKaryawanDetail();
    }

    private void getDataKaryawanDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_karyawan_personal";
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
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                String tanggal_masuk = data.getString("tanggal_masuk");
                                detailTV.setText(jabatan+" | "+bagian+" | "+department);

                                String input_date = tanggal_masuk;
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
                                        bulanName = "Not found";
                                        break;
                                }

                                tanggalMasukTV.setText(Integer.parseInt(dayDate) +" "+bulanName+" "+yearDate);

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
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(FormExitClearanceActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(FormExitClearanceActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message_3));
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

    @Override
    public void onBackPressed() {
        if (!tanggalResign.equals("")){
            if (permohonanTerkirim.equals("1")){
                super.onBackPressed();
            } else {
                new KAlertDialog(FormExitClearanceActivity.this, KAlertDialog.WARNING_TYPE)
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
                                permohonanTerkirim = "1";
                                onBackPressed();
                            }
                        })
                        .show();
            }
        } else {
            super.onBackPressed();
        }
    }

}