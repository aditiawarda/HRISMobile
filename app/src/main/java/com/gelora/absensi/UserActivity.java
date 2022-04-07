package com.gelora.absensi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.gelora.absensi.support.Preferences;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shasin.notificationbanner.Banner;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.service.controls.ControlsProviderService.TAG;

public class UserActivity extends AppCompatActivity {

    LinearLayout closeBSBTN, viewAvatarBTN, updateAvatarBTN, emptyAvatarBTN, availableAvatarBTN, emptyAvatarPart, availableAvatarPart, actionBar, covidBTN, companyBTN, connectBTN, closeBTN, reminderBTN, privacyPolicyBTN, contactServiceBTN, aboutAppBTN, reloadBTN, backBTN, logoutBTN, historyBTN;
    TextView descAvailable, descEmtpy, statusUserTV, prevBTN, nextBTN, eventCalender, yearTV, monthTV, nameUserTV, nikTV, departemenTV, bagianTV, jabatanTV;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    BottomSheetLayout bottomSheet;
    SwipeRefreshLayout refreshLayout;
    NestedScrollView scrollView;
    ImageView avatarUser, imageUserBS;
    View rootview;
    String avatarStatus = "0", avatarPath = "";

    AlarmManager alarmManager;
    CompactCalendarView compactCalendarView;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private int i = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        scrollView = findViewById(R.id.scrollView);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        logoutBTN = findViewById(R.id.logout_btn);
        nameUserTV = findViewById(R.id.name_of_user_tv);
        nikTV = findViewById(R.id.nik_user_tv);
        departemenTV = findViewById(R.id.departemen_tv);
        bagianTV = findViewById(R.id.bagian_tv);
        jabatanTV = findViewById(R.id.jabatan_tv);
        historyBTN = findViewById(R.id.history_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        aboutAppBTN = findViewById(R.id.about_app_btn);
        privacyPolicyBTN = findViewById(R.id.privacy_policy_btn);
        contactServiceBTN = findViewById(R.id.contact_service_btn);
        reminderBTN = findViewById(R.id.reminder_btn);
        rootview = findViewById(android.R.id.content);
        companyBTN = findViewById(R.id.about_company_btn);
        covidBTN = findViewById(R.id.covid_btn);
        statusUserTV = findViewById(R.id.status_user_tv);
        avatarUser = findViewById(R.id.avatar_user);
        emptyAvatarPart = findViewById(R.id.empty_avatar_part);
        availableAvatarPart = findViewById(R.id.available_avatar_part);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataUser();
                    }
                }, 1000);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(scrollView.getScrollY()>=15){
                    actionBar.setBackground(ContextCompat.getDrawable(UserActivity.this, R.drawable.shape_action_bar));
                } else {
                    actionBar.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        reminderBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderBar();
            }
        });

        avatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarFuction();
            }
        });

        companyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, CompanyActivity.class);
                startActivity(intent);
            }
        });


        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        covidBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, CovidActivity.class);
                startActivity(intent);
            }
        });

        aboutAppBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutApp();
            }
        });

        privacyPolicyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyPolicy();
            }
        });

        contactServiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactService();
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(UserActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Apakah anda yakin untuk logout?")
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
                                logoutFunction();
                            }
                        })
                        .show();

            }
        });

        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        getDataUser();

    }

    private void getDataUser(){
        String nama = sharedPrefManager.getSpNama();
        String nik = sharedPrefManager.getSpNik();
        String status = sharedPrefManager.getSpStatusUser();
        getDataKaryawan();
        nameUserTV.setText(nama.toUpperCase());
        nikTV.setText(nik);

        if(status.equals("1")){
            statusUserTV.setText("Aktif");
        } else {
            statusUserTV.setText("Non-Aktif");
        }
    }

    private void logoutFunction(){
        Preferences.setLoggedInStatus(this,false);
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_USER, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_AKTIF, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_HALAMAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        Preferences.clearLoggedInUser(UserActivity.this);
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDataKaryawan() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_karyawan";
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
                            if (status.equals("Success")){
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                String avatar = data.getString("avatar");
                                String info_covid = data.getString("info_covid");
                                departemenTV.setText(department);
                                bagianTV.setText(bagian);
                                jabatanTV.setText(jabatan);

                                if(!avatar.equals("null")){
                                    avatarStatus = "1";
                                    avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                    Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(avatarUser);
                                }

                                if(info_covid.equals("1")){
                                    covidBTN.setVisibility(View.VISIBLE);
                                } else {
                                    covidBTN.setVisibility(View.GONE);
                                }

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
        Banner.make(rootview, UserActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 4000).show();
    }

    private void aboutApp(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_about_app, bottomSheet, false));
        closeBTN = findViewById(R.id.close_btn);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
    }

    private void privacyPolicy(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_privacy_policy, bottomSheet, false));
        closeBTN = findViewById(R.id.close_btn);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
    }

    private void contactService(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_contact_service, bottomSheet, false));
        closeBTN = findViewById(R.id.close_btn);
        connectBTN = findViewById(R.id.connect_btn);
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });
        connectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+6281281898552&text="));
                startActivity(webIntent);
            }
        });
    }

    private void reminderBar(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_calender, bottomSheet, false));
        monthTV = findViewById(R.id.month_calender);
        yearTV = findViewById(R.id.year_calender);
        closeBTN = findViewById(R.id.close_btn);
        eventCalender = findViewById(R.id.event_calender);
        prevBTN = findViewById(R.id.prevBTN);
        nextBTN = findViewById(R.id.nextBTN);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        String month = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(4,7);
        String year = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(30,34);

        String bulanName;
        switch (month) {
            case "Jan":
                bulanName = "Januari";
                break;
            case "Feb":
                bulanName = "Februari";
                break;
            case "Mar":
                bulanName = "Maret";
                break;
            case "Apr":
                bulanName = "April";
                break;
            case "May":
                bulanName = "Mei";
                break;
            case "Jun":
                bulanName = "Juni";
                break;
            case "Jul":
                bulanName = "Juli";
                break;
            case "Aug":
                bulanName = "Agustus";
                break;
            case "Sep":
                bulanName = "September";
                break;
            case "Oct":
                bulanName = "Oktober";
                break;
            case "Nov":
                bulanName = "November";
                break;
            case "Dec":
                bulanName = "Desember";
                break;
            default:
                bulanName = "Not found!";
                break;
        }

        monthTV.setText(bulanName);
        yearTV.setText(year);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        getEventCalender();

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                if(events.size()<=0){
                    eventCalender.setText("");
                } else {
                    eventCalender.setText(String.valueOf(events.get(0).getData()));
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                String month = String.valueOf(firstDayOfNewMonth).substring(4,7);
                String year = String.valueOf(firstDayOfNewMonth).substring(30,34);

                String bulanName;
                switch (month) {
                    case "Jan":
                        bulanName = "Januari";
                        break;
                    case "Feb":
                        bulanName = "Februari";
                        break;
                    case "Mar":
                        bulanName = "Maret";
                        break;
                    case "Apr":
                        bulanName = "April";
                        break;
                    case "May":
                        bulanName = "Mei";
                        break;
                    case "Jun":
                        bulanName = "Juni";
                        break;
                    case "Jul":
                        bulanName = "Juli";
                        break;
                    case "Aug":
                        bulanName = "Agustus";
                        break;
                    case "Sep":
                        bulanName = "September";
                        break;
                    case "Oct":
                        bulanName = "Oktober";
                        break;
                    case "Nov":
                        bulanName = "November";
                        break;
                    case "Dec":
                        bulanName = "Desember";
                        break;
                    default:
                        bulanName = "Not found!";
                        break;
                }

                monthTV.setText(bulanName);
                yearTV.setText(year);
                eventCalender.setText("");
            }
        });

        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    private void getEventCalender() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/holiday";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {

                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject event = data.getJSONObject(i);
                                String nama = event.getString("nama");
                                String tanggal = event.getString("tanggal");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = sdf.parse(tanggal);
                                long millis = date.getTime();
                                Event ev1 = new Event(Color.RED, millis, nama);
                                compactCalendarView.addEvent(ev1);
                            }


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(UserActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(UserActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
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
        String file_directori = getRealPathFromURIPath(uri, UserActivity.this);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    String file_directori = getRealPathFromURIPath(uri, UserActivity.this);
                    String a= "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                    Log.e("PaRSE JSON", a);
                    uploadMultipart();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadMultipart() {
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/update_profilepic";
        String path1 = FilePathimage.getPath(this, uri);

        if (path1 == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path1, "file") //Adding file
                        .addParameter("NIK", sharedPrefManager.getSpNik()) //Adding text parameter to the request
                        .setMaxRetries(1)
                        .startUpload();
            } catch (Exception exc) {
                //Toast.makeText(this, "oke", Toast.LENGTH_SHORT).show();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final KAlertDialog pDialog = new KAlertDialog(UserActivity.this, KAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i) {
                            case 0:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this,R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this,R.color.material_deep_teal_50));
                                break;
                            case 2:
                            case 6:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this,R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this,R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this,R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                        (UserActivity.this,R.color.warning_stroke_color));
                                break;
                        }
                    }
                    public void onFinish() {
                        i = -1;
                        pDialog.setTitleText("Gambar Berhasil Diupload")
                                .setConfirmText("OK")
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        bottomSheet.dismissSheet();
                        getDataKaryawan();
                    }
                }.start();

            }
        }, 1);
    }

    @SuppressLint("SetTextI18n")
    private void avatarFuction(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_change_avatar, bottomSheet, false));
        emptyAvatarPart = findViewById(R.id.empty_avatar_part);
        availableAvatarPart = findViewById(R.id.available_avatar_part);
        descAvailable = findViewById(R.id.desc_available);
        descEmtpy = findViewById(R.id.desc_empty);
        emptyAvatarBTN = findViewById(R.id.empty_avatar_btn);
        availableAvatarBTN = findViewById(R.id.available_avatar_btn);
        updateAvatarBTN = findViewById(R.id.update_avatar_btn);
        viewAvatarBTN = findViewById(R.id.view_avatar_btn);
        imageUserBS = findViewById(R.id.image_user_bs);
        closeBSBTN = findViewById(R.id.close_bs_btn);

        descAvailable.setText("Halo "+sharedPrefManager.getSpNama()+", Anda bisa atur foto profil sesuai keinginan anda.");
        descEmtpy.setText("Halo "+sharedPrefManager.getSpNama()+", Anda bisa tambahkan foto profil sesuai keinginan anda.");

        if (avatarStatus.equals("1")){
            emptyAvatarPart.setVisibility(View.GONE);
            emptyAvatarBTN.setVisibility(View.GONE);
            availableAvatarPart.setVisibility(View.VISIBLE);
            availableAvatarBTN.setVisibility(View.VISIBLE);
            Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageUserBS);
        } else {
            emptyAvatarPart.setVisibility(View.VISIBLE);
            emptyAvatarBTN.setVisibility(View.VISIBLE);
            availableAvatarPart.setVisibility(View.GONE);
            availableAvatarBTN.setVisibility(View.GONE);
        }

        closeBSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

        emptyAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
                bottomSheet.dismissSheet();
            }
        });

        updateAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dexterCall();
                bottomSheet.dismissSheet();
            }
        });

        viewAvatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View imageLayoutView = inflater.inflate(R.layout.show_avatar, null);
                ImageView image = (ImageView) imageLayoutView.findViewById(R.id.dialog_imageview);
                Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(image);

                builder.setView(imageLayoutView)
                        .setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.create();
                builder.show();
            }
        });

    }

    private void dexterCall(){
        Dexter.withActivity(UserActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
