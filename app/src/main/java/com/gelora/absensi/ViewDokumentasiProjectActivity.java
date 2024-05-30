package com.gelora.absensi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterDokumentasiProject;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataDokumentasiProject;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ViewDokumentasiProjectActivity extends AppCompatActivity {

    LinearLayout loadingData, actionBar, backBTN, addBTN, noDataDokumentasi;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    RequestQueue requestQueue;
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    List<DataDokumentasiProject> dataDokumentasiProjects = new ArrayList<>();
    private AdapterDokumentasiProject adapterDokumentasiProject;
    SliderView sliderView;
    String projectId = "";
    KAlertDialog pDialog;
    private int i = -1;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dokumentasi_project);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        backBTN = findViewById(R.id.back_btn);
        addBTN = findViewById(R.id.add_btn);
        actionBar = findViewById(R.id.action_bar);
        noDataDokumentasi = findViewById(R.id.no_data_dokumentasi);
        loadingData = findViewById(R.id.loading_part);

        projectId = getIntent().getExtras().getString("id_project");

        LocalBroadcastManager.getInstance(this).registerReceiver(removeImage, new IntentFilter("remove_dokumentasi"));

        sliderView = findViewById(R.id.imageSlider);
        adapterDokumentasiProject = new AdapterDokumentasiProject(dataDokumentasiProjects);
        sliderView.setSliderAdapter(adapterDokumentasiProject);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.SCROLL_AXIS_NONE);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(false);

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

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
            }
        });

        dataDokumentasiProjects.clear();
        getDokumentasi("first");

    }

    private void tambahDokumentasi() {
        String UPLOAD_URL = "https://hrisgelora.co.id/api/add_project_documentation";
        String path1 = FilePathimage.getPath(ViewDokumentasiProjectActivity.this, uri);
        if (path1 == null) {
            Toast.makeText(getBaseContext(), "Please move your .jpg file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path1, "file") //Adding file
                        .addParameter("id_project", projectId)
                        .addParameter("current_time", String.valueOf(System.currentTimeMillis()))
                        .setMaxRetries(1)
                        .startUpload();
            } catch (Exception exc) {
                Log.e("PaRSE JSON", "Oke");
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final KAlertDialog pDialog = new KAlertDialog(ViewDokumentasiProjectActivity.this, KAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien2));
                                break;
                            case 2:
                            case 6:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien3));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien4));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien5));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien6));
                                break;
                        }
                    }
                    public void onFinish() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                i = -1;
                                pDialog.setTitleText("Berhasil diunggah")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                dataDokumentasiProjects.clear();
                                getDokumentasi("last");
                            }
                        }, 3000);
                    }
                }.start();

            }
        }, 1);

    }

    public BroadcastReceiver removeImage = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getStringExtra("url");

            try {
                new KAlertDialog(ViewDokumentasiProjectActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk menghapus foto dokumentasi?")
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
                                pDialog = new KAlertDialog(ViewDokumentasiProjectActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ViewDokumentasiProjectActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        removeImage(url);
                                    }
                                }.start();
                            }
                        })
                        .show();
            }
            catch (WindowManager.BadTokenException e){
                Log.e("Error", e.toString());
            }

        }
    };

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getDokumentasi(String position) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/project_documentation";
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
                                JSONArray dataimage = data.getJSONArray("data");
                                loadingData.setVisibility(View.GONE);
                                noDataDokumentasi.setVisibility(View.GONE);
                                sliderView.setVisibility(View.VISIBLE);

                                for (int i = 0; i < dataimage.length(); i++) {
                                    JSONObject image = dataimage.getJSONObject(i);
                                    String dataImageDokumentasi = image.getString("image_url");
                                    String createdAt = image.getString("created_at");
                                    DataDokumentasiProject dataImage = new DataDokumentasiProject();
                                    dataImage.setImageUrl(dataImageDokumentasi);
                                    dataImage.setCreatedAt(createdAt);
                                    dataDokumentasiProjects.add(dataImage);
                                    adapterDokumentasiProject.renewItems(dataDokumentasiProjects);

                                    if (i == dataimage.length() - 1) {
                                        if(position.equals("last")){
                                            int lastIndex = adapterDokumentasiProject.getCount() - 1;
                                            sliderView.setCurrentPagePosition(lastIndex);
                                        }
                                    }

                                }

                            } else {
                                loadingData.setVisibility(View.GONE);
                                noDataDokumentasi.setVisibility(View.VISIBLE);
                                sliderView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_project", projectId);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void removeImage(String imageUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/remove_project_documentation";
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
                                pDialog.setTitleText("Berhasil")
                                        .setContentText("Foto berhasil dihapus")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                dataDokumentasiProjects.clear();
                                                getDokumentasi("last");
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                            } else {
                                pDialog.setTitleText("Gagal Dihapus")
                                        .setContentText("Terjadi kesalahan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("url", imageUrl);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(ViewDokumentasiProjectActivity.this)
                    .withPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES)
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
            Dexter.withActivity(ViewDokumentasiProjectActivity.this)
                    .withPermissions(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        String code = "project";
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, code);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(ViewDokumentasiProjectActivity.this);
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
        Intent intent = new Intent(ViewDokumentasiProjectActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(ViewDokumentasiProjectActivity.this, ImagePickerActivity.class);
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
        String file_directori = getRealPathFromURIPath(uri, ViewDokumentasiProjectActivity.this);
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
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, ViewDokumentasiProjectActivity.this);
                        String a = "File Directory : " + file_directori + " URI: " + String.valueOf(uri);
                        Log.e("PaRSE JSON", a);

                        tambahDokumentasi();

                    } else if(extension.equals(".png")||extension.equals(".PNG")){
                        String pngImagePath = FilePathimage.getPath(this, uri);
                        new ConvertImageTask().execute(pngImagePath);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(ViewDokumentasiProjectActivity.this, KAlertDialog.ERROR_TYPE)
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

                tambahDokumentasi();

            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new KAlertDialog(ViewDokumentasiProjectActivity.this, KAlertDialog.ERROR_TYPE)
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}