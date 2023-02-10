package com.gelora.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.isradeleon.notify.Notify;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterShiftAbsen;
import com.gelora.absensi.adapter.AdapterStatusAbsen;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.ShiftAbsen;
import com.gelora.absensi.model.StatusAbsen;
import com.gelora.absensi.support.Preferences;
import com.gelora.absensi.support.StatusBarColorManager;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;
import com.shasin.notificationbanner.Banner;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.provider.Settings.Secure;

import static android.service.controls.ControlsProviderService.TAG;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //private StatusBarColorManager mStatusBarColorManager;
    private GoogleMap mMap;
    private LatLng userPoint;
    double userLat, userLong;
    SwipeRefreshLayout refreshLayout;
    ImageView reminderImage, weatherIconPart, onlineGif, warningGif, notificationWarning;
    TextView markTitleShift, markTitleStatus, notePoint, descStatusPart, layoffDesc, descStart, notifMasukTV, reminderDecs, reminderCelebrateTV, izinDesc, currentDatePart, mainWeatherPart, tempWeatherPart, feelLikeTempPart, currentAddress, celebrateName, dateCheckinTV, dateCheckoutTV, eventCalender, monthTV, yearTV, ucapanTV, detailAbsenTV, timeCheckinTV, checkinPointTV, timeCheckoutTV, checkoutPointTV, actionTV, indicatorAbsen, hTime, mTime, sTime, absenPoint, dateTV, userTV, statusAbsenChoiceTV, shiftAbsenChoiceTV;
    LinearLayout markerNotification, pantauBTN, reminderCongrat, markerWarningAbsensi, openSessionBTN, skeletonLayout, closeBTNPart, dataCuacaPart, cuacaBTN, celebratePart, prevBTN, nextBTN, warningPart, closeBTN, connectionSuccess, connectionFailed, loadingLayout, userBTNPart, izinPart, layoffPart, attantionPart, recordAbsenPart, inputAbsenPart, actionBTN, pointPart, statusAbsenBTN, shiftBTN, statusAbsenChoice, changeStatusAbsen, shiftAbsenChoice, changeShiftAbsen, statusAbsenChoiceBTN, shiftAbsenChoiceBTN;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    String connectionStatus = "success", statusDialog = "0", statusLooping = "on", shiftIdAbsen = "", warningPerangkat = "nonaktif", sesiBaru = "nonaktif", actionSession = "", checkinTimeZone = "", devModCheck = "", fakeTimeCheck = "", timeDetection = "undefined", deviceID, zoomAction = "0", idIzin = "", statusLibur = "nonaktif", dialogAktif = "0", intervalTime, dateCheckin, statusTglLibur = "0", shiftType, shortName, pesanCheckout, statusPulangCepat, radiusZone = "undefined", idCheckin = "", idStatusAbsen, idShiftAbsen = "", namaStatusAbsen = "undefined", descStatusAbsen, namaShiftAbsen = "undefined", datangShiftAbsen = "00:00:00", pulangShiftAbsen = "00:00:00", batasPulang = "00:00:00", currentDay, statusAction = "undefined", lateTime, lateStatus, overTime, checkoutStatus;
    View rootview;
    DayNightSwitch dayNightSwitch;
    LocationManager locationManager;
    CompactCalendarView compactCalendarView;
    KAlertDialog pDialog;
    ResultReceiver resultReceiver;
    SwitchButton switchZoom;
    ProgressBar loadingCuaca;
    Vibrator vibrate;
    RippleBackground rippleIndicator;
    ProgressBar loadingProgressBar;

    private RecyclerView statusAbsenRV;
    private StatusAbsen[] statusAbsens;
    private AdapterStatusAbsen adapterStatusAbsen;

    private RecyclerView shifAbsenRV;
    private ShiftAbsen[] shiftAbsens;
    private AdapterShiftAbsen adapterShiftAbsen;

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private int i = -1;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    RequestQueue requestQueue;
    String appVersion = "1.6.8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }

        deviceID = String.valueOf(Secure.getString(MapsActivity.this.getContentResolver(), Secure.ANDROID_ID)).toUpperCase();

        if (getTimeZone().equals("GMT+07:00")||getTimeZone().equals("UTC+07")){
            checkinTimeZone = "WIB";
        } else if (getTimeZone().equals("GMT+08:00")||getTimeZone().equals("UTC+08")){
            checkinTimeZone = "WITA";
        } else if (getTimeZone().equals("GMT+09:00")||getTimeZone().equals("UTC+09")){
            checkinTimeZone = "WIT";
        } else {
            checkinTimeZone = getTimeZone();
        }

        resultReceiver = new AddressResultReceiver(new Handler());
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);

        rootview = findViewById(android.R.id.content);
        onlineGif = findViewById(R.id.img_online);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        indicatorAbsen = findViewById(R.id.indicator_absen_tv);
        hTime = findViewById(R.id.h_time);
        mTime = findViewById(R.id.m_time);
        sTime = findViewById(R.id.s_time);
        absenPoint = findViewById(R.id.abesen_point_tv);
        pointPart = findViewById(R.id.point_part);
        statusAbsenBTN = findViewById(R.id.status_absen_btn);
        shiftBTN = findViewById(R.id.shift_btn);
        dateTV = findViewById(R.id.date_tv);
        userTV = findViewById(R.id.user_tv);
        actionBTN = findViewById(R.id.action_btn);
        actionTV = findViewById(R.id.action_tv);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        statusAbsenChoice = findViewById(R.id.status_absen_choice);
        shiftAbsenChoice = findViewById(R.id.shift_absen_choice);
        statusAbsenChoiceTV = findViewById(R.id.status_absen_choice_tv);
        shiftAbsenChoiceTV = findViewById(R.id.shift_absen_choice_tv);
        changeStatusAbsen = findViewById(R.id.change_status_absen);
        changeShiftAbsen = findViewById(R.id.change_shift_absen);
        statusAbsenChoiceBTN = findViewById(R.id.status_absen_choice_btn);
        shiftAbsenChoiceBTN = findViewById(R.id.shift_absen_choice_btn);
        inputAbsenPart = findViewById(R.id.input_absen_part);
        recordAbsenPart = findViewById(R.id.record_absen_part);
        timeCheckinTV = findViewById(R.id.time_checkin_tv);
        timeCheckoutTV = findViewById(R.id.time_checkout_tv);
        checkinPointTV = findViewById(R.id.checkin_point_tv);
        checkoutPointTV = findViewById(R.id.checkout_point_tv);
        detailAbsenTV = findViewById(R.id.detail_absen_tv);
        attantionPart = findViewById(R.id.attantion_part);
        layoffPart = findViewById(R.id.layoff_part);
        izinPart = findViewById(R.id.izin_part);
        userBTNPart = findViewById(R.id.user_btn_part);
        loadingLayout = findViewById(R.id.layout_loding);
        warningGif = findViewById(R.id.warning_gif);
        connectionSuccess = findViewById(R.id.connection_success);
        connectionFailed = findViewById(R.id.connection_failed);
        dayNightSwitch = findViewById(R.id.day_night_switch);
        ucapanTV = findViewById(R.id.ucapan_tv);
        warningPart = findViewById(R.id.warning_part);
        dateCheckinTV = findViewById(R.id.date_checkin_tv);
        dateCheckoutTV = findViewById(R.id.date_checkout_tv);
        celebratePart = findViewById(R.id.celebrate_part);
        celebrateName = findViewById(R.id.celebrate_name);
        cuacaBTN = findViewById(R.id.info_cuaca_btn);
        skeletonLayout = findViewById(R.id.skeleton_layout);
        izinDesc = findViewById(R.id.izin_desc);
        openSessionBTN = findViewById(R.id.open_session_btn);
        switchZoom = findViewById(R.id.switch_zoom);
        markerWarningAbsensi = findViewById(R.id.marker_warning);
        notificationWarning = findViewById(R.id.warning_gif_absen);
        reminderCongrat = findViewById(R.id.reminder_congrat);
        reminderDecs = findViewById(R.id.reminder_desc);
        reminderCelebrateTV = findViewById(R.id.reminder_celebrate);
        pantauBTN = findViewById(R.id.pantau_btn);
        markerNotification = findViewById(R.id.marker_notification);
        notifMasukTV = findViewById(R.id.jumlah_notif_masuk);
        reminderImage = findViewById(R.id.reminder_image);
        descStart = findViewById(R.id.desc_start);
        layoffDesc = findViewById(R.id.layoff_desc);
        descStatusPart = findViewById(R.id.desc_status_part);
        notePoint = findViewById(R.id.note_point);
        markTitleStatus = findViewById(R.id.mark_title_status);
        markTitleShift = findViewById(R.id.mark_title_shift);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        requestQueue = Volley.newRequestQueue(getBaseContext());
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        rippleIndicator = (RippleBackground)findViewById(R.id.ripple_indicator);

        loadingProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#A6441F"),android.graphics.PorterDuff.Mode.MULTIPLY);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(MapsActivity.this, R.font.roboto);
            userTV.setTypeface(typeface);
            dateTV.setTypeface(typeface);
            reminderDecs.setTypeface(typeface);
            reminderDecs.setTypeface(typeface);
            celebrateName.setTypeface(typeface);
            dateCheckinTV.setTypeface(typeface);
            dateCheckoutTV.setTypeface(typeface);
            timeCheckinTV.setTypeface(typeface);
            timeCheckoutTV.setTypeface(typeface);
            checkinPointTV.setTypeface(typeface);
            checkoutPointTV.setTypeface(typeface);
            descStart.setTypeface(typeface);
            layoffDesc.setTypeface(typeface);
            descStatusPart.setTypeface(typeface);
            notePoint.setTypeface(typeface);
            markTitleStatus.setTypeface(typeface);
            markTitleShift.setTypeface(typeface);
        }

        rippleIndicator.startRippleAnimation();

        Glide.with(getApplicationContext())
                .load(R.drawable.icon_none)
                .into(onlineGif);

        Glide.with(getApplicationContext())
                .load(R.drawable.ic_warning_notification_gif_main)
                .into(notificationWarning);

        LocalBroadcastManager.getInstance(this).registerReceiver(statusAbsenBroad, new IntentFilter("status_absen_broad"));
        LocalBroadcastManager.getInstance(this).registerReceiver(shiftAbsenBroad, new IntentFilter("shift_absen_broad"));

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        refreshData();
                    }
                }, 1000);
            }
        });

        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                Log.d(TAG, "onSwitch() called with: is_night = [" + is_night + "]");
                if (is_night) {
                    mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
                } else {
                    mMap.setMapStyle(null);
                }
            }
        });

        switchZoom.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    zoomAction = "1";
                } else {
                    zoomAction = "0";
                }
            }
        });

        openSessionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionSession.equals("close_nextday")){
                    new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Apakah anda yakin untuk membuka sesi absensi?")
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

                                    final KAlertDialog pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE)
                                            .setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1300, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            i++;
                                            switch (i) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (MapsActivity.this, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (MapsActivity.this, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (MapsActivity.this, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (MapsActivity.this, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (MapsActivity.this, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (MapsActivity.this, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            i = -1;
                                            pDialog.setTitleText("Sesi Absensi Dibuka")
                                                    .setContentText("Anda bisa melakukan absensi hari ini.")
                                                    .setConfirmText("    OK    ")
                                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);

                                            bukaSesi();

                                        }
                                    }.start();

                                }
                            })
                            .show();
                } else {
                    new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Perhatian")
                            .setContentText("Apakah anda yakin untuk membuka sesi absensi dan menghapus status yang tertera hari ini?")
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
                                    editIzin();
                                }
                            })
                            .show();
                }

            }
        });

        loadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        cuacaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoCuaca();
            }
        });

        pantauBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusLooping = "off";
                Intent intent = new Intent(MapsActivity.this, MonitoringAbsensiBagianActivity.class);
                startActivity(intent);
            }
        });

        userBTNPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusLooping = "off";
                Intent intent = new Intent(MapsActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalender();
            }
        });

        statusAbsenBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAbsen();
            }
        });

        changeStatusAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAbsen();
            }
        });

        statusAbsenChoiceBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAbsen();
            }
        });

        shiftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftAbsen();
            }
        });

        changeShiftAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftAbsen();
            }
        });

        userTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        checkLogin();
        getCurrentDay();
        timeLive();
        dateLive();
        checkIzin();
        checkWarning();
        checkNotification();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0,0,0,12);
        permissionLoc();
    }

    @SuppressLint("MissingPermission")
    private void userPosition() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);

        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    Log.e("TAG", "GPS is on" + String.valueOf(location));
                    if(mMap != null){
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        startLocationUpdates();
                    }
                }  else {
                    gpsEnableAction();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error trying to get last GPS location");
                e.printStackTrace();
                gpsEnableAction();
            }
        });

    }

    public boolean isDeveloperModeEnabled(){
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 17) {
            return android.provider.Settings.Secure.getInt(MapsActivity.this.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
        }
        return false;
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (location != null) {
            Log.e("TAG", "GPS is on" + String.valueOf(location));
            userLat = location.getLatitude();
            userLong = location.getLongitude();

            // userLat = -6.3280459;
            // userLong = 106.8768529;

            checkTime();
            getCurrentDay();
            dateLive();

        } else {
            gpsEnableAction();
        }

        if (location != null) {
            userPoint = new LatLng(userLat, userLong);
            if (zoomAction.equals("0")){
                // User position camera
                float zoomLevel = 18.0f; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                mMap.getUiSettings().setCompassEnabled(false);
            }
            getAction();
        }

    }

    // Trigger new location updates at interval
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // Last location
        // getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
        //    @Override
        //    public void onSuccess(Location location) {
        //        if (location != null) {
        //            Log.e("TAG", "GPS is on" + String.valueOf(location));
        //            mMap.setMyLocationEnabled(true);
        //            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //            float zoomLevel = 18.0f; //This goes up to 21
        //            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        //            mMap.getUiSettings().setCompassEnabled(false);
        //            onLocationChanged(location);
        //        }  else {
        //            gpsEnableAction();
        //        }
        //    }
        //}).addOnFailureListener(new OnFailureListener() {
        //    @Override
        //    public void onFailure(@NonNull Exception e) {
        //        Log.d("MapsActivity", "Error trying to get last GPS location");
        //        e.printStackTrace();
        //        gpsEnableAction();
        //    }
        //});

        // Realtime location
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        if(statusLooping.equals("on") && connectionStatus=="success"){
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    }
                },
                Looper.myLooper());
    }

    private void getAction() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/aksi_absen";
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

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            String point = data.getString("gedung");
                            String status = data.getString("status");

                            if (status.equals("didalam radius")){
                                insideRadius();
                                indicatorAbsen.setText("DI DALAM JANGKAUAN");
                                indicatorAbsen.setTextColor(Color.parseColor("#309A35"));

                                absenPoint.setText(point);
                                absenPoint.setTextColor(Color.parseColor("#B15735"));
                                pointPart.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_point_2));
                                pointPart.setOnClickListener(null);
                                radiusZone = "inside";

                            } else {
                                outsideRadius();
                                indicatorAbsen.setText("DI LUAR JANGKAUAN");
                                indicatorAbsen.setTextColor(Color.parseColor("#B83633"));

                                absenPoint.setText("REFRESH");
                                absenPoint.setTextColor(Color.WHITE);
                                pointPart.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_second_btn));
                                pointPart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loadingLayout.setVisibility(View.VISIBLE);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshData();
                                            }
                                        }, 1500);
                                    }
                                });
                                radiusZone = "outside";
                            }

                            if(!idShiftAbsen.equals("")||!idCheckin.equals("")){
                                actionButton();
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
                params.put("latitude_u", String.valueOf(userLat));
                params.put("longitude_u", String.valueOf(userLong));
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getLocation() {
        String url = "https://geloraaksara.co.id/absen-online/api/lokasi";
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject location = data.getJSONObject(i);
                                String latitude = location.getString("latitude");
                                String longitude = location.getString("longitude");
                                String radius = location.getString("radius");

                                LatLng pointAbsen = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                CircleOptions circleOptions = new CircleOptions();
                                circleOptions.center(pointAbsen);
                                circleOptions.radius(Integer.parseInt(radius));
                                circleOptions.fillColor(Color.parseColor("#80FB3527"));
                                circleOptions.strokeWidth(3);
                                circleOptions.strokeColor(Color.parseColor("#E6FB3527"));
                                mMap.addCircle(circleOptions);
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        );

        requestQueue.add(getRequest);

    }

    private String getTimeCheck() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("07:00:00");
    }

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("07:00:00");
    }

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("01");
    }

    private String getTimeM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("mm");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("00");
    }

    private String getTimeS() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("ss");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("00");
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("2022-06-03");
    }

    private String getDateTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("2022-06-03");
    }

    private String getDayMonth() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("2022-06-03");
    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("26");
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

    private String getBulanTahun() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTimeZone() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("z");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void timeLive() {
        hTime.setText(getTimeH());
        mTime.setText(getTimeM());
        sTime.setText(getTimeS());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeLive();
            }
        }, 1000);
    }

    private void insideRadius() {
        Glide.with(getApplicationContext())
                .load(R.drawable.online_gif)
                .into(onlineGif);
    }

    private void outsideRadius() {
        Glide.with(getApplicationContext())
                .load(R.drawable.offline_gif)
                .into(onlineGif);
    }

    private void statusAbsen(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_status_absen, bottomSheet, false));
        statusAbsenRV = findViewById(R.id.status_absen_rv);

        if(statusAbsenRV != null){
            statusAbsenRV.setLayoutManager(new LinearLayoutManager(this));
            statusAbsenRV.setHasFixedSize(true);
            statusAbsenRV.setNestedScrollingEnabled(false);
            statusAbsenRV.setItemAnimator(new DefaultItemAnimator());
        }

        getStatusAbsenBagian();

    }

    private void shiftAbsen(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_shift_absen, bottomSheet, false));
        shifAbsenRV = findViewById(R.id.shift_absen_rv);

        if(shifAbsenRV != null){
            shifAbsenRV.setLayoutManager(new LinearLayoutManager(this));
            shifAbsenRV.setHasFixedSize(true);
            shifAbsenRV.setNestedScrollingEnabled(false);
            shifAbsenRV.setItemAnimator(new DefaultItemAnimator());
        }

        getShiftAbsenBagian();

    }

    @Override
    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            if (warningPerangkat.equals("aktif")){
                logoutFunction();
            } else {
                statusLooping = "off";
                super.onBackPressed();
            }
        }
    }

    private void logoutFunction(){
        Preferences.setLoggedInStatus(MapsActivity.this,false);
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
        sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        Preferences.clearLoggedInUser(MapsActivity.this);
        Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        finishAffinity();
    }

    @SuppressLint("SetTextI18n")
    private void checkLogin() {
        if(!sharedPrefManager.getSpSudahLogin()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if(sharedPrefManager.getSpIdJabatan().equals("8")||sharedPrefManager.getSpNik().equals("80085")){
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                finish();
            } else {
                shortName = sharedPrefManager.getSpNama();
                if(shortName.contains(" ")){
                    shortName = shortName.substring(0, shortName.indexOf(" "));
                    System.out.println(shortName);
                }
                userTV.setText("Halo, "+shortName);
            }
        }
    }

    private void getStatusAbsenBagian() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/status_absen_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status_absen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            statusAbsens = gson.fromJson(status_absen, StatusAbsen[].class);
                            adapterStatusAbsen = new AdapterStatusAbsen(statusAbsens,MapsActivity.this);
                            statusAbsenRV.setAdapter(adapterStatusAbsen);

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
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getShiftAbsenBagian() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/shift_absen_bagian";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String shift_absen = data.getString("data");

                            GsonBuilder builder =new GsonBuilder();
                            Gson gson = builder.create();
                            shiftAbsens = gson.fromJson(shift_absen, ShiftAbsen[].class);
                            adapterShiftAbsen = new AdapterShiftAbsen(shiftAbsens,MapsActivity.this);
                            shifAbsenRV.setAdapter(adapterShiftAbsen);

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
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("hari", currentDay);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public BroadcastReceiver statusAbsenBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            idStatusAbsen = intent.getStringExtra("id_status_absen");
            namaStatusAbsen = intent.getStringExtra("nama_status_absen");
            descStatusAbsen = intent.getStringExtra("desc_status_absen");
            attantionPart.setVisibility(View.GONE);
            statusAbsenBTN.setVisibility(View.GONE);
            shiftBTN.setVisibility(View.VISIBLE);

            changeStatusAbsen.setVisibility(View.VISIBLE);
            statusAbsenChoice.setVisibility(View.VISIBLE);
            statusAbsenChoiceTV.setText(namaStatusAbsen+" ("+descStatusAbsen+")");
            sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");

            if(namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                shiftAbsenChoiceBTN.setOnClickListener(null);

                datangShiftAbsen = "08:00:00";

                if (currentDay.equals("Sabtu")){
                    idShiftAbsen = "27";
                    namaShiftAbsen = "WFH SB1";
                    pulangShiftAbsen = "13.00:00";
                } else {
                    idShiftAbsen = "25";
                    namaShiftAbsen = "WFH";
                    pulangShiftAbsen = "16.00:00";
                }

                shiftBTN.setVisibility(View.GONE);
                shiftAbsenChoice.setVisibility(View.VISIBLE);
                changeShiftAbsen.setVisibility(View.GONE);
                shiftAbsenChoiceTV.setText(datangShiftAbsen+" - "+pulangShiftAbsen);
                shiftAbsenChoiceTV.setTextColor(Color.parseColor("#B15735"));
                shiftAbsenChoiceBTN.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_point));
                statusAction = "checkin";
                actionButton();
            } else if (namaStatusAbsen.equals("DL")) {
                shiftAbsenChoiceBTN.setOnClickListener(null);

                shiftBTN.setVisibility(View.GONE);
                shiftAbsenChoice.setVisibility(View.GONE);
                changeShiftAbsen.setVisibility(View.GONE);
                statusAction = "absen";
                actionButton();
            } else {
                shiftAbsenChoiceBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shiftAbsen();
                    }
                });
                actionBTN.setOnClickListener(null);
                actionBTN.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_disable_btn));

                shiftAbsenChoiceTV.setTextColor(Color.parseColor("#B15735"));
                shiftAbsenChoiceBTN.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_feel));
                changeShiftAbsen.setVisibility(View.VISIBLE);
                shiftAbsenChoice.setVisibility(View.GONE);
                idShiftAbsen = "";
                namaShiftAbsen = "";
                datangShiftAbsen = "";
                pulangShiftAbsen = "";
                actionTV.setText("CHECK IN");
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    public BroadcastReceiver shiftAbsenBroad = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            idShiftAbsen = intent.getStringExtra("id_shift_absen");
            namaShiftAbsen = intent.getStringExtra("nama_shift_absen");
            datangShiftAbsen = intent.getStringExtra("datang_shift_absen");
            pulangShiftAbsen = intent.getStringExtra("pulang_shift_absen");
            shiftBTN.setVisibility(View.GONE);

            shiftAbsenChoice.setVisibility(View.VISIBLE);
            shiftAbsenChoiceTV.setText(namaShiftAbsen+" ("+datangShiftAbsen+" - "+pulangShiftAbsen+")");

            statusAction = "checkin";
            actionButton();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheet.dismissSheet();
                }
            }, 300);

        }
    };

    @SuppressLint("SetTextI18n")
    private void dateLive(){
        switch (getDateM()) {
            case "01":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Januari " + getDateY());
                break;
            case "02":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Februari " + getDateY());
                break;
            case "03":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Maret " + getDateY());
                break;
            case "04":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " April " + getDateY());
                break;
            case "05":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Mei " + getDateY());
                break;
            case "06":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Juni " + getDateY());
                break;
            case "07":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Juli " + getDateY());
                break;
            case "08":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Agustus " + getDateY());
                break;
            case "09":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " September " + getDateY());
                break;
            case "10":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Oktober " + getDateY());
                break;
            case "11":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " November " + getDateY());
                break;
            case "12":
                dateTV.setText(currentDay + ", " + String.valueOf(Integer.parseInt(getDateD())) + " Desember " + getDateY());
                break;
            default:
                dateTV.setText("Not found!");
                break;
        }

    }

    private void getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay = "Minggu";
                break;
            case Calendar.MONDAY:
                currentDay = "Senin";
                break;
            case Calendar.TUESDAY:
                currentDay = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                currentDay = "Rabu";
                break;
            case Calendar.THURSDAY:
                currentDay = "Kamis";
                break;
            case Calendar.FRIDAY:
                currentDay = "Jumat";
                break;
            case Calendar.SATURDAY:
                currentDay = "Sabtu";
                break;
        }
    }

    private void lateTime() {
        String timeAbsen = getTime();
        String timeMasuk = datangShiftAbsen;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(timeAbsen);
            date2 = format.parse(timeMasuk);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = waktu1 - waktu2;
        NumberFormat f = new DecimalFormat("00");
        long hour = (selisih_waktu / 3600000) % 24;
        long min = (selisih_waktu / 60000) % 60;
        long sec = (selisih_waktu / 1000) % 60;

        if (statusLibur.equals("aktif")){
            lateTime = "00:00:00";
        } else {
            if (waktu1<waktu2+60000){
                lateTime = "00:00:00";
            } else {
                lateTime = String.valueOf((f.format(hour)) + ":" + (f.format(min)) + ":" + f.format(sec));
            }
        }

    }

    private void lateTimeNotif() {
        String timeAbsen = getTime();
        String timeMasuk = datangShiftAbsen;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(timeAbsen);
            date2 = format.parse(timeMasuk);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = waktu1 - waktu2;
        NumberFormat f = new DecimalFormat("00");
        long hour = (selisih_waktu / 3600000) % 24;
        long min = (selisih_waktu / 60000) % 60;
        long sec = (selisih_waktu / 1000) % 60;

        if (statusLibur.equals("aktif")){
            Notify.build(getApplicationContext())
                    .setTitle("HRIS Mobile Gelora")
                    .setContent("Anda lembur di hari libur. Selamat bekerja dan utamakan keselamatan")
                    .setSmallIcon(R.drawable.ic_skylight_notification)
                    .setColor(R.color.colorPrimary)
                    .largeCircularIcon()
                    .enableVibration(true)
                    .show();

            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrate.vibrate(500);
            }

        } else {
            if (waktu1>waktu2+60000){
                String lateDesc = "";

                if(!String.valueOf((f.format(hour))).equals("00")){ // 01:01:01
                    if (!String.valueOf((f.format(min))).equals("00")){ // 01:01:01
                        if(!String.valueOf((f.format(sec))).equals("00")){
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(hour))))) + " jam " + String.valueOf(Integer.parseInt(String.valueOf((f.format(min))))) + " menit " + String.valueOf(Integer.parseInt(String.valueOf((f.format(sec))))) + " detik";
                        } else {
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(hour))))) + " jam " + String.valueOf(Integer.parseInt(String.valueOf((f.format(min))))) + " menit";
                        }
                    } else { // 01:00:01
                        if(!String.valueOf((f.format(sec))).equals("00")){
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(hour))))) + " jam " + String.valueOf(Integer.parseInt(String.valueOf((f.format(sec))))) + " detik";
                        } else {
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(hour))))) + " jam";
                        }
                    }
                } else { // 00:01:01
                    if (!String.valueOf((f.format(min))).equals("00")){ // 00:01:01
                        if(!String.valueOf((f.format(sec))).equals("00")){
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(min))))) + " menit " + String.valueOf(Integer.parseInt(String.valueOf((f.format(sec))))) + " detik";
                        } else {
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(min))))) + " menit";
                        }
                    } else { // 00:00:01
                        if(!String.valueOf((f.format(sec))).equals("00")){
                            lateDesc = String.valueOf(Integer.parseInt(String.valueOf((f.format(sec))))) + " detik";
                        }
                    }
                }

                Notify.build(getApplicationContext())
                        .setTitle("HRIS Mobile Gelora")
                        .setContent("Anda terlambat "+lateDesc+", segera gunakan prosedur fingerscan/form keterangan tidak absen")
                        .setSmallIcon(R.drawable.ic_skylight_notification)
                        .setColor(R.color.colorPrimary)
                        .largeCircularIcon()
                        .enableVibration(true)
                        .show();

                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrate.vibrate(500);
                }

            }
        }

    }

    private void actionCheckin() {
        lateTime();

        String id_sift = idShiftAbsen;
        String NIK = sharedPrefManager.getSpNik();
        String tanggal = getDate();
        String timestamp_masuk = getDate()+" "+getTime();
        String jam_masuk = getTime();
        String waktu_terlambat = lateTime;
        String latitude = String.valueOf(userLat);
        String longitude = String.valueOf(userLong);
        String status_absen = idStatusAbsen;

        String zonaWaktu;
        if (getTimeZone().equals("GMT+07:00")||getTimeZone().equals("UTC+07")){
            zonaWaktu = "WIB";
        } else if (getTimeZone().equals("GMT+08:00")||getTimeZone().equals("UTC+08")){
            zonaWaktu = "WITA";
        } else if (getTimeZone().equals("GMT+09:00")||getTimeZone().equals("UTC+09")){
            zonaWaktu = "WIT";
        } else {
            zonaWaktu = getTimeZone();
        }

        String checkin_point;
        if (!idStatusAbsen.equals("2")) {
            checkin_point = "";
        } else {
            checkin_point = absenPoint.getText().toString();
        }

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/checkin";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String message = data.getString("message");

                            if (status.equals("Success")){
                                lateTimeNotif();
                                dialogAktif = "1";
                                idCheckin = data.getString("id_checkin");
                                checkAbsen();
                            } else if (status.equals("Available")){
                                pDialog.dismiss();
                                new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Hari ini anda sudah melakukan Check In!")
                                        .setConfirmText("    OK    ")
                                        .showCancelButton(true)
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                sesiBaru = "nonaktif";
                                                checkAbsen();
                                            }
                                        })
                                        .show();
                            } else {
                                pDialog.dismiss();
                                new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Check In Gagal")
                                        .setContentText("Terjadi kesalahan")
                                        .setConfirmText("    OK    ")
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

                params.put("id_shift", id_sift);
                params.put("NIK", NIK);
                params.put("tanggal", tanggal);
                params.put("jam_masuk", jam_masuk);
                params.put("timestamp_masuk", timestamp_masuk);
                params.put("timezone_masuk", zonaWaktu);
                params.put("waktu_terlambat", waktu_terlambat);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("status_absen", status_absen);
                params.put("checkin_point", checkin_point);
                params.put("app_version", appVersion);

                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        // postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
        //        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);

    }

    private void checkIzin(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_izin";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("izin");

                            if(status.equals("aktif")){
                                String tipe_izin = data.getString("tipe_izin");
                                String id_izin = data.getString("id_izin");
                                idIzin = id_izin;
                                izinDesc.setText(tipe_izin.toUpperCase());
                                izinPart.setVisibility(View.VISIBLE);
                                layoffPart.setVisibility(View.GONE);
                                inputAbsenPart.setVisibility(View.GONE);
                                recordAbsenPart.setVisibility(View.GONE);
                                attantionPart.setVisibility(View.GONE);
                                skeletonLayout.setVisibility(View.GONE);
                                statusAction = "history";
                                actionSession = "close_izin";
                                openSessionBTN.setVisibility(View.VISIBLE);
                                actionButton();
                            } else {
                                izinPart.setVisibility(View.GONE);
                                openSessionBTN.setVisibility(View.GONE);
                                checkLayoff();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("tanggal_sekarang", getDate());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void editIzin() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/edit_izin";
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
                                final KAlertDialog pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE)
                                        .setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (MapsActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (MapsActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (MapsActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (MapsActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (MapsActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (MapsActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        pDialog.setTitleText("Sesi Absensi Dibuka")
                                                .setContentText("Anda bisa melakukan absensi hari ini.")
                                                .setConfirmText("    OK    ")
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        checkIzin();
                                    }
                                }.start();

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
                params.put("id_izin", idIzin);
                params.put("tanggal", getDate());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void checkLayoff(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_layoff";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Terdaftar")){
                                layoffPart.setVisibility(View.VISIBLE);
                                inputAbsenPart.setVisibility(View.GONE);
                                recordAbsenPart.setVisibility(View.GONE);
                                attantionPart.setVisibility(View.GONE);
                                skeletonLayout.setVisibility(View.GONE);
                                statusAction = "history";
                                actionButton();
                            } else {
                                layoffPart.setVisibility(View.GONE);
                                checkAbsen();
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("tanggal", getDate());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void actionLayoff(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/layoff";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                refreshData();

                                pDialog.setTitleText("Anda Diliburkan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);

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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("status_absen", "5");
                params.put("tanggal", getDate());
                params.put("latitude", String.valueOf(userLat));
                params.put("longitude", String.valueOf(userLong));
                params.put("status", "1");

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void checkAbsen(){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_absen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String libur = data.getString("libur");

                            if (!libur.equals("Tidak Libur")){
                                statusTglLibur = "1";
                            } else {
                                statusTglLibur = "0";
                            }

                            if(status.equals("Success")){ //Sudah checkin atau sudah checkout dan interval masih ada
                                JSONObject data_checkin = data.getJSONObject("data");
                                String date_checkin = data_checkin.getString("tanggal");
                                String time_checkin = data_checkin.getString("jam_masuk");
                                String timezone_masuk = data_checkin.getString("timezone_masuk");
                                String checkin_point = data_checkin.getString("checkin_point");
                                String id_status = data_checkin.getString("status_absen");
                                String id_shift = data_checkin.getString("id_shift");
                                idCheckin = data_checkin.getString("id");
                                checkinTimeZone = timezone_masuk;
                                shiftIdAbsen = id_shift;

                                String input_date = date_checkin;
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

                                dateCheckinTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                                timeCheckinTV.setText(time_checkin+" "+timezone_masuk);
                                idStatusAbsen = id_status;
                                dateCheckin = date_checkin;

                                if (checkin_point.equals("")){
                                    checkinPointTV.setText(sharedPrefManager.getSpNama());
                                } else {
                                    checkinPointTV.setText(checkin_point);
                                }

                                checkLibur(dateCheckin);
                                detailAbsen(id_status, id_shift);
                                String id_checkout = data.getString("id_checkout");
                                checkoutRecord(id_checkout);

                            } else { //Belum checkin atau interval sudah habis
                                attantionPart.setVisibility(View.VISIBLE);
                                statusAbsenBTN.setVisibility(View.VISIBLE);
                                changeStatusAbsen.setVisibility(View.GONE);
                                statusAbsenChoice.setVisibility(View.GONE);
                                shiftBTN.setVisibility(View.GONE);
                                changeShiftAbsen.setVisibility(View.GONE);
                                shiftAbsenChoice.setVisibility(View.GONE);
                                actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                                actionTV.setText("CHECK IN");
                                inputAbsenPart.setVisibility(View.VISIBLE);
                                recordAbsenPart.setVisibility(View.GONE);
                                skeletonLayout.setVisibility(View.GONE);
                                checkLibur(getDate());
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("tanggal", getDate());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    @SuppressLint("SetTextI18n")
    private void actionButton(){
        if (radiusZone.equals("inside")){
            if(statusAction.equals("checkin")){
                if(idShiftAbsen.equals("")){
                    actionTV.setText("CHECK IN");
                    actionBTN.setOnClickListener(null);
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                } else {
                    actionTV.setText("CHECK IN");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Check In sekarang?")
                                        .setContentText(namaStatusAbsen+" ("+datangShiftAbsen.substring(0,5)+" - "+pulangShiftAbsen.substring(0,5)+")")
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

                                                pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1000, 500) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;

                                                        if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        } else {

                                                            if (timeDetection.equals("matching")){
                                                                actionCheckin();
                                                            } else {
                                                                if (fakeTimeCheck.equals("1")){
                                                                    pDialog.dismiss();
                                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Perhatian")
                                                                            .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                            .setCancelText(" TUTUP ")
                                                                            .setConfirmText("SETTING")
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
                                                                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                }
                                                                            })
                                                                            .show();

                                                                    // Vibrate for 500 milliseconds
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                    } else {
                                                                        //deprecated in API 26
                                                                        vibrate.vibrate(500);
                                                                    }

                                                                } else {
                                                                    actionCheckin();
                                                                }

                                                            }

                                                        }

                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Check In sekarang?")
                                        .setContentText(namaStatusAbsen+" - "+namaShiftAbsen+" ("+datangShiftAbsen.substring(0,5)+" - "+pulangShiftAbsen.substring(0,5)+")")
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

                                                pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1000, 500) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;

                                                        if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        } else {
                                                            if(radiusZone.equals("inside")){
                                                                if (timeDetection.equals("matching")){
                                                                    actionCheckin();
                                                                } else {
                                                                    if (fakeTimeCheck.equals("1")){
                                                                        pDialog.dismiss();
                                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                                .setTitleText("Perhatian")
                                                                                .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                                .setCancelText(" TUTUP ")
                                                                                .setConfirmText("SETTING")
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
                                                                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                    }
                                                                                })
                                                                                .show();

                                                                        // Vibrate for 500 milliseconds
                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                        } else {
                                                                            //deprecated in API 26
                                                                            vibrate.vibrate(500);
                                                                        }

                                                                    } else {
                                                                        actionCheckin();
                                                                    }

                                                                }
                                                            } else {
                                                                pDialog.dismiss();
                                                                new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Perhatian")
                                                                        .setContentText("Posisi anda di luar jangkauan!")
                                                                        .setConfirmText("TUTUP")
                                                                        .showCancelButton(true)
                                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                            @Override
                                                                            public void onClick(KAlertDialog sDialog) {
                                                                                sDialog.dismiss();
                                                                            }
                                                                        })
                                                                        .show();

                                                                // Vibrate for 500 milliseconds
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                    vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                } else {
                                                                    //deprecated in API 26
                                                                    vibrate.vibrate(500);
                                                                }
                                                            }
                                                        }

                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }

                        }
                    });
                }
            } else if (statusAction.equals("checkout")){
                checkPulang();
                if (statusPulangCepat.equals("aktif")){
                    pesanCheckout = "Apakah anda yakin check out lebih cepat?";
                } else {
                    if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                        if (!datangShiftAbsen.equals("") && !pulangShiftAbsen.equals("")){
                            pesanCheckout
                                    = namaStatusAbsen+" ("+
                                    datangShiftAbsen.substring(0,5)+" - "+
                                    pulangShiftAbsen.substring(0,5)+")";
                        } else {
                            pesanCheckout
                                    = namaStatusAbsen+" - "+
                                    namaShiftAbsen+" ("+
                                    datangShiftAbsen+" - "+
                                    pulangShiftAbsen+")";
                        }
                    } else {
                        if (!datangShiftAbsen.equals("") && !pulangShiftAbsen.equals("")){
                            pesanCheckout
                                    = namaStatusAbsen+" - "+
                                    namaShiftAbsen+" ("+
                                    datangShiftAbsen.substring(0,5)+" - "+
                                    pulangShiftAbsen.substring(0,5)+")";
                        } else {
                            pesanCheckout
                                    = namaStatusAbsen+" - "+
                                    namaShiftAbsen+" ("+
                                    datangShiftAbsen+" - "+
                                    pulangShiftAbsen+")";
                        }
                    }
                }
                actionTV.setText("CHECK OUT");
                actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                actionBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Check Out sekarang?")
                                    .setContentText(pesanCheckout)
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

                                            pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;

                                                    if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                        pDialog.dismiss();
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                .setCancelText(" TUTUP ")
                                                                .setConfirmText("SETTING")
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
                                                                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                    }
                                                                })
                                                                .show();

                                                        // Vibrate for 500 milliseconds
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                        } else {
                                                            //deprecated in API 26
                                                            vibrate.vibrate(500);
                                                        }

                                                    } else {

                                                        if (timeDetection.equals("matching")){
                                                            actionCheckout();
                                                        } else {
                                                            if(fakeTimeCheck.equals("1")){
                                                                pDialog.dismiss();
                                                                new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Perhatian")
                                                                        .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                        .setCancelText(" TUTUP ")
                                                                        .setConfirmText("SETTING")
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
                                                                                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                            }
                                                                        })
                                                                        .show();

                                                                // Vibrate for 500 milliseconds
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                    vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                } else {
                                                                    //deprecated in API 26
                                                                    vibrate.vibrate(500);
                                                                }

                                                            } else {
                                                                actionCheckout();
                                                            }

                                                        }

                                                    }

                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        } else {
                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Check Out sekarang?")
                                    .setContentText(pesanCheckout)
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

                                            pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;

                                                    if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                        pDialog.dismiss();
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                .setCancelText(" TUTUP ")
                                                                .setConfirmText("SETTING")
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
                                                                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                    }
                                                                })
                                                                .show();

                                                        // Vibrate for 500 milliseconds
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                            vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                        } else {
                                                            //deprecated in API 26
                                                            vibrate.vibrate(500);
                                                        }

                                                    } else {
                                                        if(radiusZone.equals("inside")){

                                                            if (timeDetection.equals("matching")){
                                                                actionCheckout();
                                                            } else {
                                                                if(fakeTimeCheck.equals("1")){
                                                                    pDialog.dismiss();
                                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Perhatian")
                                                                            .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                            .setCancelText(" TUTUP ")
                                                                            .setConfirmText("SETTING")
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
                                                                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                }
                                                                            })
                                                                            .show();

                                                                    // Vibrate for 500 milliseconds
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                    } else {
                                                                        //deprecated in API 26
                                                                        vibrate.vibrate(500);
                                                                    }

                                                                } else {
                                                                    actionCheckout();
                                                                }

                                                            }

                                                        } else {
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Posisi anda di luar jangkauan!")
                                                                    .setConfirmText("TUTUP")
                                                                    .showCancelButton(true)
                                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                        @Override
                                                                        public void onClick(KAlertDialog sDialog) {
                                                                            sDialog.dismiss();
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        }

                                                    }

                                                }
                                            }.start();


                                        }
                                    })
                                    .show();
                        }

                    }
                });
            } else if (statusAction.equals("absen")) {
                actionTV.setText("ABSEN");
                actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                actionBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText("Perhatian")
                                .setContentText("Apakah anda yakin hari ini akan Libur/Absen?")
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

                                        pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        new CountDownTimer(1300, 800) {
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                switch (i) {
                                                    case 0:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (MapsActivity.this, R.color.colorGradien));
                                                        break;
                                                    case 1:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (MapsActivity.this, R.color.colorGradien2));
                                                        break;
                                                    case 2:
                                                    case 6:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (MapsActivity.this, R.color.colorGradien3));
                                                        break;
                                                    case 3:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (MapsActivity.this, R.color.colorGradien4));
                                                        break;
                                                    case 4:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (MapsActivity.this, R.color.colorGradien5));
                                                        break;
                                                    case 5:
                                                        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                (MapsActivity.this, R.color.colorGradien6));
                                                        break;
                                                }
                                            }
                                            public void onFinish() {
                                                i = -1;
                                                if (timeDetection.equals("matching")){
                                                    actionLayoff();
                                                } else {
                                                    if(fakeTimeCheck.equals("1")){
                                                        pDialog.dismiss();
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                .setCancelText(" TUTUP ")
                                                                .setConfirmText("SETTING")
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
                                                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        actionLayoff();
                                                    }
                                                }

                                            }
                                        }.start();

                                    }
                                })
                                .show();
                    }
                });
            } else if (statusAction.equals("history")) {
                actionTV.setText("RIWAYAT ABSENSI");
                actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_second_btn));
                actionBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusLooping = "off";
                        Intent intent = new Intent(MapsActivity.this, HistoryActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        else {
            if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL") || namaStatusAbsen.equals("DL")) {
                if (statusAction.equals("absen")){
                    actionTV.setText("ABSEN");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Apakah anda yakin hari ini akan Libur/Absen?")
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

                                            pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    if (timeDetection.equals("matching")){
                                                        actionLayoff();
                                                    } else {
                                                        if(fakeTimeCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                        }
                                                                    })
                                                                    .show();
                                                        } else {
                                                            actionLayoff();
                                                        }
                                                    }
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    });
                } else if (statusAction.equals("checkout")){
                    checkPulang();
                    if (statusPulangCepat.equals("aktif")){
                        pesanCheckout = "Apakah anda yakin check out lebih cepat?";
                    } else {
                        if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                            if (!datangShiftAbsen.equals("") && !pulangShiftAbsen.equals("")){
                                pesanCheckout
                                        = namaStatusAbsen+" ("+
                                        datangShiftAbsen.substring(0,5)+" - "+
                                        pulangShiftAbsen.substring(0,5)+")";
                            } else {
                                pesanCheckout
                                        = namaStatusAbsen+" - "+
                                        namaShiftAbsen+" ("+
                                        datangShiftAbsen+" - "+
                                        pulangShiftAbsen+")";
                            }
                        } else {
                            if (!datangShiftAbsen.equals("") && !pulangShiftAbsen.equals("")){
                                pesanCheckout
                                        = namaStatusAbsen+" ("+
                                        datangShiftAbsen.substring(0,5)+" - "+
                                        pulangShiftAbsen.substring(0,5)+")";
                            } else {
                                pesanCheckout
                                        = namaStatusAbsen+" - "+
                                        namaShiftAbsen+" ("+
                                        datangShiftAbsen+" - "+
                                        pulangShiftAbsen+")";
                            }
                        }
                    }
                    actionTV.setText("CHECK OUT");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Check Out sekarang?")
                                        .setContentText(pesanCheckout)
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

                                                pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;

                                                        if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        } else {

                                                            if (timeDetection.equals("matching")){
                                                                actionCheckout();
                                                            } else {
                                                                if(fakeTimeCheck.equals("1")){
                                                                    pDialog.dismiss();
                                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Perhatian")
                                                                            .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                            .setCancelText(" TUTUP ")
                                                                            .setConfirmText("SETTING")
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
                                                                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                }
                                                                            })
                                                                            .show();

                                                                    // Vibrate for 500 milliseconds
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                    } else {
                                                                        //deprecated in API 26
                                                                        vibrate.vibrate(500);
                                                                    }

                                                                } else {
                                                                    actionCheckout();
                                                                }

                                                            }

                                                        }

                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Check Out sekarang?")
                                        .setContentText(pesanCheckout)
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

                                                pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;

                                                        if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        } else {

                                                            if (timeDetection.equals("matching")){
                                                                actionCheckout();
                                                            } else {
                                                                if(fakeTimeCheck.equals("1")){
                                                                    pDialog.dismiss();
                                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Perhatian")
                                                                            .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                            .setCancelText(" TUTUP ")
                                                                            .setConfirmText("SETTING")
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
                                                                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                }
                                                                            })
                                                                            .show();

                                                                    // Vibrate for 500 milliseconds
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                    } else {
                                                                        //deprecated in API 26
                                                                        vibrate.vibrate(500);
                                                                    }

                                                                } else {
                                                                    actionCheckout();
                                                                }

                                                            }

                                                        }

                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                } else if (statusAction.equals("checkin")) {
                    actionTV.setText("CHECK IN");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")) {
                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Check In sekarang?")
                                        .setContentText(namaStatusAbsen+" ("+datangShiftAbsen.substring(0,5)+" - "+pulangShiftAbsen.substring(0,5)+")")
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

                                                pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE)
                                                        .setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1000, 500) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;

                                                        if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        } else {

                                                            if (timeDetection.equals("matching")){
                                                                actionCheckin();
                                                            } else {
                                                                if (fakeTimeCheck.equals("1")){
                                                                    pDialog.dismiss();
                                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Perhatian")
                                                                            .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                            .setCancelText(" TUTUP ")
                                                                            .setConfirmText("SETTING")
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
                                                                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                }
                                                                            })
                                                                            .show();

                                                                    // Vibrate for 500 milliseconds
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                    } else {
                                                                        //deprecated in API 26
                                                                        vibrate.vibrate(500);
                                                                    }

                                                                } else {
                                                                    actionCheckin();
                                                                }

                                                            }

                                                        }

                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            } else {
                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Check In sekarang?")
                                        .setContentText(namaStatusAbsen+" - "+namaShiftAbsen+" ("+datangShiftAbsen.substring(0,5)+" - "+pulangShiftAbsen.substring(0,5)+")")
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

                                                pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE)
                                                        .setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1000, 500) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (MapsActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;

                                                        if (isDeveloperModeEnabled() && devModCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Mode Pengembang/Developer pada perangkat anda terdeteksi aktif, harap non-aktifkan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                                                                        }
                                                                    })
                                                                    .show();

                                                            // Vibrate for 500 milliseconds
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                            } else {
                                                                //deprecated in API 26
                                                                vibrate.vibrate(500);
                                                            }

                                                        } else {

                                                            if (timeDetection.equals("matching")){
                                                                actionCheckin();
                                                            } else {
                                                                if (fakeTimeCheck.equals("1")){
                                                                    pDialog.dismiss();
                                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                            .setTitleText("Perhatian")
                                                                            .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                            .setCancelText(" TUTUP ")
                                                                            .setConfirmText("SETTING")
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
                                                                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                                }
                                                                            })
                                                                            .show();

                                                                    // Vibrate for 500 milliseconds
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                                    } else {
                                                                        //deprecated in API 26
                                                                        vibrate.vibrate(500);
                                                                    }

                                                                } else {
                                                                    actionCheckin();
                                                                }

                                                            }

                                                        }

                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }

                        }
                    });
                }  else if (statusAction.equals("history")) {
                    actionTV.setText("RIWAYAT ABSENSI");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_second_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            statusLooping = "off";
                            Intent intent = new Intent(MapsActivity.this, HistoryActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            } else {
                if (statusAction.equals("absen")){
                    actionTV.setText("ABSEN");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Perhatian")
                                    .setContentText("Apakah anda yakin hari ini akan Libur/Absen?")
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

                                            pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                            pDialog.show();
                                            pDialog.setCancelable(false);
                                            new CountDownTimer(1300, 800) {
                                                public void onTick(long millisUntilFinished) {
                                                    i++;
                                                    switch (i) {
                                                        case 0:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien));
                                                            break;
                                                        case 1:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien2));
                                                            break;
                                                        case 2:
                                                        case 6:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien3));
                                                            break;
                                                        case 3:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien4));
                                                            break;
                                                        case 4:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien5));
                                                            break;
                                                        case 5:
                                                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                    (MapsActivity.this, R.color.colorGradien6));
                                                            break;
                                                    }
                                                }
                                                public void onFinish() {
                                                    i = -1;
                                                    if (timeDetection.equals("matching")){
                                                        actionLayoff();
                                                    } else {
                                                        if(fakeTimeCheck.equals("1")){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian")
                                                                    .setContentText("Pengaturan waktu pada perangkat anda terdeteksi berbeda dengan sistem, harap sesuaikan terlebih dahulu!")
                                                                    .setCancelText(" TUTUP ")
                                                                    .setConfirmText("SETTING")
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
                                                                            startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                                                        }
                                                                    })
                                                                    .show();
                                                        } else {
                                                            actionLayoff();
                                                        }
                                                    }
                                                }
                                            }.start();

                                        }
                                    })
                                    .show();
                        }
                    });
                }  else if (statusAction.equals("history")) {
                    actionTV.setText("RIWAYAT ABSENSI");
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_second_btn));
                    actionBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            statusLooping = "off";
                            Intent intent = new Intent(MapsActivity.this, HistoryActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (statusAction.equals("checkin")) {
                    actionTV.setText("CHECK IN");
                    actionBTN.setOnClickListener(null);
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                } else {
                    actionTV.setText("CHECK OUT");
                    actionBTN.setOnClickListener(null);
                    actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                }

            }

        }

    }

    private void refreshData(){
        loadingLayout.setVisibility(View.GONE);
        refreshMaps();
        checkLogin();
        getCurrentDay();
        timeLive();
        dateLive();
        checkIzin();
        checkWarning();
        checkNotification();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");
        idShiftAbsen = "";
        connectionStatus = "success";
    }

    private void refreshData2(){
        loadingLayout.setVisibility(View.GONE);
        userPosition();
        checkLogin();
        getCurrentDay();
        timeLive();
        dateLive();
        checkIzin();
        checkWarning();
        checkNotification();
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");
        idShiftAbsen = "";
        connectionStatus = "success";
    }

    private void refreshMaps(){
        if(mMap != null){
            mMap.clear();
            userPosition();
            //startLocationUpdates();
            getLocation();

            if (userPoint!=null){
                // User position camera
                float zoomLevel = 18.0f; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                mMap.getUiSettings().setCompassEnabled(false);
            }

            // Maps style
            int hoursNow = Integer.parseInt(getTimeH());
            if (hoursNow >= 18) {
                dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
            } else if (hoursNow >= 0 && hoursNow <= 5){
                dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
            } else {
                dayNightSwitch.setIsNight(false,  mMap.setMapStyle(null));
            }
        }
    }

    private void detailAbsen(String id_status, String id_shift){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/detail_absen";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String nama_status = data.getString("nama_status");
                                String nama_shift = data.getString("nama_shift");
                                String datang = data.getString("datang");
                                String pulang = data.getString("pulang");
                                namaStatusAbsen = nama_status;
                                namaShiftAbsen = nama_shift;
                                datangShiftAbsen = datang;
                                pulangShiftAbsen = pulang;
                                batasPulang = pulang;
                                actionButton();

                                if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")){
                                    detailAbsenTV.setText(nama_status+" ("+datang.substring(0,5)+" - "+pulang.substring(0,5)+")");
                                } else {
                                    detailAbsenTV.setText(nama_status+" - "+nama_shift+" ("+datang.substring(0,5)+" - "+pulang.substring(0,5)+")");
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("id_status_absen", id_status);
                params.put("id_shift", id_shift);

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void actionCheckout() {
        overTime();

        String id_masuk = idCheckin;
        String tanggal = getDate();
        String timestamp_pulang = getDate()+" "+getTime();
        String jam_pulang = getTime();
        String kelebihan_jam = overTime;
        String status = checkoutStatus;
        String latitude = String.valueOf(userLat);
        String longitude = String.valueOf(userLong);

        String zonaWaktu;
        if (getTimeZone().equals("GMT+07:00")||getTimeZone().equals("UTC+07")){
            zonaWaktu = "WIB";
        } else if (getTimeZone().equals("GMT+08:00")||getTimeZone().equals("UTC+08")){
            zonaWaktu = "WITA";
        } else if (getTimeZone().equals("GMT+09:00")||getTimeZone().equals("UTC+09")){
            zonaWaktu = "WIT";
        } else {
            zonaWaktu = getTimeZone();
        }

        String checkout_point;
        if (!idStatusAbsen.equals("2")) {
            checkout_point = "";
        } else {
            checkout_point = absenPoint.getText().toString();
        }

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/checkout";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if(status.equals("Success")){
                                String message = data.getString("message");
                                String id_checkout = data.getString("id_checkout");
                                checkoutRecord(id_checkout);

                                pDialog.setTitleText("Check Out Berhasil")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            } else {
                                pDialog.setTitleText("Check Out Gagal")
                                        .setContentText("Terjadi kesalahan")
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("id_masuk", id_masuk);
                params.put("tanggal", tanggal);
                params.put("timestamp_pulang", timestamp_pulang);
                params.put("jam_pulang", jam_pulang);
                params.put("timezone_pulang", zonaWaktu);
                params.put("kelebihan_jam", kelebihan_jam);
                params.put("status", status);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("checkout_point", checkout_point);
                params.put("app_version", appVersion);

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);

    }

    private void overTime() {
        String timePulang;
        if (shiftType.equals("Normal")||shiftType.equals("Tanggung")){
            timePulang = dateCheckin+" "+pulangShiftAbsen;
        } else {
            String dayCheckout = dateCheckin.substring(8,10);
            String dateCheckout = dateCheckin.substring(0,8);
            int tgl_besok = Integer.parseInt(dayCheckout)+1;
            timePulang = dateCheckout+String.valueOf(tgl_besok)+" "+pulangShiftAbsen;
        }
        String timeCheckout = getDate()+" "+getTime();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(timeCheckout);
            date2 = format.parse(timePulang);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = (waktu1 - waktu2);
        NumberFormat f = new DecimalFormat("00");
        long hour = (selisih_waktu / 3600000) % 24;
        long min = (selisih_waktu / 60000) % 60;
        long sec = (selisih_waktu / 1000) % 60;

        if (waktu1<waktu2){
            if (statusLibur.equals("aktif")){
                if (shiftIdAbsen.equals("91")){
                    //Pulang Cepat
                    checkoutStatus = "2";
                    overTime = "00:00:00";
                } else {
                    //Pulang
                    checkoutStatus = "1";
                    overTime = "00:00:00";
                }
            } else {
                //Pulang Cepat
                checkoutStatus = "2";
                overTime = "00:00:00";
            }
        } else if (waktu1>waktu2+3600000) {
            //Pulang Lembur
            checkoutStatus = "3";
            overTime = String.valueOf((f.format(hour)) + ":" + (f.format(min)) + ":" + f.format(sec));
        } else {
            //Pulang
            checkoutStatus = "1";
            overTime = "00:00:00";
        }

    }

    private void checkoutRecord(String id_checkout){
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/checkout_record";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            connectionFailed.setVisibility(View.GONE);
                            connectionSuccess.setVisibility(View.VISIBLE);

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String jam_pulang_shift = data.getString("jam_pulang_shift");

                            if(status.equals("Success")){ // Belum Checkout
                                String tgl_checkin = data.getString("tgl_checkin");
                                String tipe_shift = data.getString("tipe_shift");
                                String interval = data.getString("interval");
                                String timestamp_checkin = data.getString("timestamp_checkin");
                                JSONObject data_checkin = data.getJSONObject("data");
                                String id_shift = data_checkin.getString("id_shift");
                                String time_checkout = data_checkin.getString("jam_pulang");
                                String timezone_pulang = data_checkin.getString("timezone_pulang");
                                String date_checkout = data_checkin.getString("tanggal");
                                String checkout_point = data_checkin.getString("checkout_point");
                                String status_pulang = data_checkin.getString("status");
                                shiftType = tipe_shift;
                                intervalTime = interval;

                                if (status_pulang.equals("0")){
                                    if(!tgl_checkin.equals(getDate())){
                                        if(tipe_shift.equals("Normal")){
                                            attantionPart.setVisibility(View.VISIBLE);
                                            statusAbsenBTN.setVisibility(View.VISIBLE);
                                            changeStatusAbsen.setVisibility(View.GONE);
                                            statusAbsenChoice.setVisibility(View.GONE);
                                            shiftBTN.setVisibility(View.GONE);
                                            changeShiftAbsen.setVisibility(View.GONE);
                                            shiftAbsenChoice.setVisibility(View.GONE);
                                            actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                                            actionTV.setText("CHECK IN");
                                            inputAbsenPart.setVisibility(View.VISIBLE);
                                            recordAbsenPart.setVisibility(View.GONE);
                                            skeletonLayout.setVisibility(View.GONE);
                                            warningPart.setVisibility(View.VISIBLE);

                                            Glide.with(getApplicationContext())
                                                    .load(R.drawable.warning_circle_gif)
                                                    .into(warningGif);

                                            statusAction = "checkin";
                                            idShiftAbsen = "";

                                            if(statusDialog.equals("0")){
                                                statusDialog = "1";
                                                KAlertDialog dialodWarning;
                                                dialodWarning = new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN/FORM KETERANGAN TIDAK ABSEN UNTUK MENGOREKSI JAM PULANG. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                        .setConfirmText("    OK    ");
                                                dialodWarning.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                    @Override
                                                    public void onClick(KAlertDialog sDialog) {
                                                        sDialog.dismiss();
                                                        statusDialog = "0";
                                                    }
                                                });
                                                dialodWarning.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        dialog.dismiss();
                                                        statusDialog = "0";
                                                    }
                                                });
                                                dialodWarning.show();

                                                Notify.build(getApplicationContext())
                                                        .setTitle("HRIS Mobile Gelora")
                                                        .setContent("Sebelumnya anda tidak melakukan checkout, segera gunakan prosedur fingerscan/form keterangan tidak absen untuk mengoreksi jam pulang. Jika tidak dilakukan koreksi, maka jam kerja akan terhitung 0")
                                                        .setSmallIcon(R.drawable.ic_skylight_notification)
                                                        .setColor(R.color.colorPrimary)
                                                        .largeCircularIcon()
                                                        .enableVibration(true)
                                                        .show();

                                                // Vibrate for 500 milliseconds
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                } else {
                                                    //deprecated in API 26
                                                    vibrate.vibrate(500);
                                                }

                                            }

                                            warningPart.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN/FORM KETERANGAN TIDAK ABSEN UNTUK MENGOREKSI JAM PULANG. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                            .setConfirmText("    OK    ")
                                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                @Override
                                                                public void onClick(KAlertDialog sDialog) {
                                                                    sDialog.dismiss();
                                                                }
                                                            })
                                                            .show();
                                                }
                                            });

                                            actionButton();

                                        } else if (tipe_shift.equals("Tanggung")){

                                            String pulang = tgl_checkin + " " + jam_pulang_shift;
                                            String batas = getDate() + " " + getTime();

                                            @SuppressLint("SimpleDateFormat")
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            Date date1 = null;
                                            Date date2 = null;
                                            try {
                                                date1 = format.parse(pulang);
                                                date2 = format.parse(batas);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            long waktu = date1.getTime() + Long.parseLong(interval);
                                            long waktu2 = date2.getTime(); //current

                                            if (waktu < waktu2) {
                                                attantionPart.setVisibility(View.VISIBLE);
                                                statusAbsenBTN.setVisibility(View.VISIBLE);
                                                changeStatusAbsen.setVisibility(View.GONE);
                                                statusAbsenChoice.setVisibility(View.GONE);
                                                shiftBTN.setVisibility(View.GONE);
                                                changeShiftAbsen.setVisibility(View.GONE);
                                                shiftAbsenChoice.setVisibility(View.GONE);
                                                actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                                                actionTV.setText("CHECK IN");
                                                inputAbsenPart.setVisibility(View.VISIBLE);
                                                recordAbsenPart.setVisibility(View.GONE);
                                                skeletonLayout.setVisibility(View.GONE);
                                                warningPart.setVisibility(View.VISIBLE);

                                                Glide.with(getApplicationContext())
                                                        .load(R.drawable.warning_circle_gif)
                                                        .into(warningGif);

                                                statusAction = "checkin";
                                                idShiftAbsen = "";

                                                if(statusDialog.equals("0")){
                                                    statusDialog = "1";
                                                    KAlertDialog dialodWarning;
                                                    dialodWarning = new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN/FORM KETERANGAN TIDAK ABSEN UNTUK MENGOREKSI JAM PULANG. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                            .setConfirmText("    OK    ");
                                                    dialodWarning.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                            statusDialog = "0";
                                                        }
                                                    });
                                                    dialodWarning.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            dialog.dismiss();
                                                            statusDialog = "0";
                                                        }
                                                    });
                                                    dialodWarning.show();

                                                    Notify.build(getApplicationContext())
                                                            .setTitle("HRIS Mobile Gelora")
                                                            .setContent("Sebelumnya anda tidak melakukan checkout, segera gunakan prosedur fingerscan/form keterangan tidak absen untuk mengoreksi jam pulang. Jika tidak dilakukan koreksi, maka jam kerja akan terhitung 0")
                                                            .setSmallIcon(R.drawable.ic_skylight_notification)
                                                            .setColor(R.color.colorPrimary)
                                                            .largeCircularIcon()
                                                            .enableVibration(true)
                                                            .show();

                                                    // Vibrate for 500 milliseconds
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                    } else {
                                                        //deprecated in API 26
                                                        vibrate.vibrate(500);
                                                    }

                                                }

                                                warningPart.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN/FORM KETERANGAN TIDAK ABSEN UNTUK MENGOREKSI JAM PULANG. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                });

                                                actionButton();

                                            } else {
                                                warningPart.setVisibility(View.GONE);
                                                inputAbsenPart.setVisibility(View.GONE);
                                                recordAbsenPart.setVisibility(View.VISIBLE);
                                                attantionPart.setVisibility(View.GONE);
                                                skeletonLayout.setVisibility(View.GONE);
                                                statusAction = "checkout";
                                                actionButton();
                                            }

                                        } else {
                                            String masuk  = timestamp_checkin;
                                            String batas = getDate()+" "+getTime();

                                            @SuppressLint("SimpleDateFormat")
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            Date date1 = null;
                                            Date date2 = null;
                                            try {
                                                date1 = format.parse(masuk);
                                                date2 = format.parse(batas);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            long waktu = date1.getTime() + Long.parseLong(interval);
                                            long waktu2 = date2.getTime();

                                            if (waktu<waktu2){
                                                attantionPart.setVisibility(View.VISIBLE);
                                                statusAbsenBTN.setVisibility(View.VISIBLE);
                                                changeStatusAbsen.setVisibility(View.GONE);
                                                statusAbsenChoice.setVisibility(View.GONE);
                                                shiftBTN.setVisibility(View.GONE);
                                                changeShiftAbsen.setVisibility(View.GONE);
                                                shiftAbsenChoice.setVisibility(View.GONE);
                                                actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
                                                actionTV.setText("CHECK IN");
                                                inputAbsenPart.setVisibility(View.VISIBLE);
                                                recordAbsenPart.setVisibility(View.GONE);
                                                skeletonLayout.setVisibility(View.GONE);

                                                warningPart.setVisibility(View.VISIBLE);
                                                Glide.with(getApplicationContext())
                                                        .load(R.drawable.warning_circle_gif)
                                                        .into(warningGif);

                                                statusAction = "checkin";
                                                idShiftAbsen = "";

                                                if(statusDialog.equals("0")){
                                                    statusDialog = "1";
                                                    KAlertDialog dialodWarning;
                                                    dialodWarning = new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN/FORM KETERANGAN TIDAK ABSEN UNTUK MENGOREKSI JAM PULANG. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                            .setConfirmText("    OK    ");
                                                    dialodWarning.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                            statusDialog = "0";
                                                        }
                                                    });
                                                    dialodWarning.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            dialog.dismiss();
                                                            statusDialog = "0";
                                                        }
                                                    });
                                                    dialodWarning.show();

                                                    Notify.build(getApplicationContext())
                                                            .setTitle("HRIS Mobile Gelora")
                                                            .setContent("Sebelumnya anda tidak melakukan checkout, segera gunakan prosedur fingerscan untuk mengoreksi jam pulang, dan serahkan ke bagian HRD. Jika tidak dilakukan koreksi, maka jam kerja akan terhitung 0")
                                                            .setSmallIcon(R.drawable.ic_skylight_notification)
                                                            .setColor(R.color.colorPrimary)
                                                            .largeCircularIcon()
                                                            .enableVibration(true)
                                                            .show();

                                                    // Vibrate for 500 milliseconds
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                                    } else {
                                                        //deprecated in API 26
                                                        vibrate.vibrate(500);
                                                    }

                                                }

                                                warningPart.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN/FORM KETERANGAN TIDAK ABSEN UNTUK MENGOREKSI JAM PULANG. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                                .setConfirmText("    OK    ")
                                                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                                    @Override
                                                                    public void onClick(KAlertDialog sDialog) {
                                                                        sDialog.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                });

                                                actionButton();

                                            } else {
                                                warningPart.setVisibility(View.GONE);
                                                inputAbsenPart.setVisibility(View.GONE);
                                                recordAbsenPart.setVisibility(View.VISIBLE);
                                                attantionPart.setVisibility(View.GONE);
                                                skeletonLayout.setVisibility(View.GONE);
                                                statusAction = "checkout";
                                                actionButton();
                                            }
                                        }
                                    } else {
                                        warningPart.setVisibility(View.GONE);
                                        dateCheckoutTV.setText("---- - -- - --");
                                        timeCheckoutTV.setText("-- : -- : -- ---");
                                        statusAction = "checkout";
                                        ucapanTV.setText("\"Selamat dan SUMAngat Bekerja!\"");
                                        inputAbsenPart.setVisibility(View.GONE);
                                        recordAbsenPart.setVisibility(View.VISIBLE);
                                        attantionPart.setVisibility(View.GONE);
                                        skeletonLayout.setVisibility(View.GONE);
                                        actionButton();
                                    }

                                }

                                else { // Sudah Checkout dan masih ada interval
                                    if (tipe_shift.equals("Next day")){
                                        actionSession = "close_nextday";
                                        openSessionBTN.setVisibility(View.VISIBLE);
                                        if (sesiBaru.equals("aktif")){
                                            bukaSesi();
                                        } else {
                                            warningPart.setVisibility(View.GONE);
                                            inputAbsenPart.setVisibility(View.GONE);
                                            recordAbsenPart.setVisibility(View.VISIBLE);
                                            attantionPart.setVisibility(View.GONE);
                                            skeletonLayout.setVisibility(View.GONE);

                                            String input_date = date_checkout;
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

                                            dateCheckoutTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                                            timeCheckoutTV.setText(time_checkout+" "+timezone_pulang);
                                            ucapanTV.setText("\"Terima kasih telah masuk kerja hari ini\"");
                                            statusAction = "history";
                                            actionButton();
                                        }

                                    } else {
                                        openSessionBTN.setVisibility(View.GONE);
                                        warningPart.setVisibility(View.GONE);
                                        inputAbsenPart.setVisibility(View.GONE);
                                        recordAbsenPart.setVisibility(View.VISIBLE);
                                        attantionPart.setVisibility(View.GONE);
                                        skeletonLayout.setVisibility(View.GONE);

                                        String input_date = date_checkout;
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

                                        dateCheckoutTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                                        timeCheckoutTV.setText(time_checkout+" "+timezone_pulang);
                                        ucapanTV.setText("\"Terima kasih telah masuk kerja hari ini\"");
                                        statusAction = "history";
                                        actionButton();

                                    }

                                }

                                if (checkout_point.equals("")||checkout_point.equals("null")){
                                    if (namaStatusAbsen.equals("WFH") || namaStatusAbsen.equals("Pjd") || namaStatusAbsen.equals("KLL")){
                                        if (!time_checkout.equals("00:00:00")){
                                            checkoutPointTV.setText(sharedPrefManager.getSpNama());
                                        } else {
                                            checkoutPointTV.setText("-");
                                        }
                                    } else {
                                        checkoutPointTV.setText("-");
                                    }
                                } else {
                                    checkoutPointTV.setText(checkout_point);
                                }

                                if (dialogAktif.equals("1")){
                                    pDialog.setTitleText("Check In Berhasil")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                    dialogAktif = "0";
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_checkout", id_checkout);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharedPrefManager.getSpSudahLogin()){
            finish();
        } else {
            statusLooping = "on";
            refreshData2();
        }
    }

    private void checkPulang(){
        sesiBaru = "nonaktif";
        String batasAbsenPulang;
        if (shiftType.equals("Normal")||shiftType.equals("Tanggung")){
            batasAbsenPulang = dateCheckin+" "+batasPulang;
        } else {
            String dayCheckout = dateCheckin.substring(8,10);
            String dateCheckout = dateCheckin.substring(0,8);
            int tgl_besok = Integer.parseInt(dayCheckout)+1;
            String besok = "";
            if (tgl_besok<10){
                besok = "0"+String.valueOf(tgl_besok);
            } else {
                besok = String.valueOf(tgl_besok);
            }
            batasAbsenPulang = dateCheckout+besok+" "+batasPulang;
        }
        String pulangAbsen = getDate()+" "+getTime();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(batasAbsenPulang);
            date2 = format.parse(pulangAbsen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime(); //batas
        long waktu2 = date2.getTime(); //current

        if (waktu1>waktu2){
            if (statusLibur.equals("aktif")){
                statusPulangCepat = "nonaktif";
            } else {
                //Pulang Cepat
                statusPulangCepat = "aktif";
            }
        } else {
            //Normal/Lembur
            statusPulangCepat = "nonaktif";
        }

    }

    private void permissionLoc(){
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            mMap.clear();
            userPosition();
            //startLocationUpdates();
            getLocation();

            // Maps style
            int hoursNow = Integer.parseInt(getTimeH());
            if (hoursNow >= 18) {
                dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
            } else if (hoursNow >= 0 && hoursNow <= 5){
                dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
            } else {
                dayNightSwitch.setIsNight(false,  mMap.setMapStyle(null));
            }

        }
    }

    private void gpsEnableAction(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(MapsActivity.this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //permissionLoc();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        MapsActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.d("TAG", "onActivityResult: GPS Enabled by user");
                        //mMap.clear();
                        permissionLoc();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d("TAG", "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void connectionFailed(){
        connectionFailed.setVisibility(View.VISIBLE);
        connectionSuccess.setVisibility(View.GONE);
        skeletonLayout.setVisibility(View.GONE);
        //Banner.make(rootview, MapsActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        connectionStatus = "failed";

        Glide.with(getApplicationContext())
                .load(R.drawable.icon_none)
                .into(onlineGif);

        indicatorAbsen.setText("TERPUTUS");
        indicatorAbsen.setTextColor(Color.parseColor("#575757"));

        absenPoint.setText("REFRESH");
        absenPoint.setTextColor(Color.WHITE);
        pointPart.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_second_btn));
        pointPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                }, 1500);
            }
        });

        if (!statusAction.equals("history")){
            actionBTN.setOnClickListener(null);
            actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //refreshData();
    }

    private void openCalender(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_calender, bottomSheet, false));
        monthTV = findViewById(R.id.month_calender);
        yearTV = findViewById(R.id.year_calender);
        closeBTN = findViewById(R.id.close_btn);
        prevBTN = findViewById(R.id.prevBTN);
        nextBTN = findViewById(R.id.nextBTN);
        eventCalender = findViewById(R.id.event_calender);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(MapsActivity.this, R.font.roboto);
            eventCalender.setTypeface(typeface);
        }

        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        String month = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(4,7);

        int maxLengthYear = Integer.parseInt(String.valueOf(String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).length()));
        int minLengthYear = maxLengthYear - 4;

        String year = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(minLengthYear,maxLengthYear);
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
            @SuppressLint("InlinedApi")
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

            @SuppressLint("InlinedApi")
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                String month = String.valueOf(firstDayOfNewMonth).substring(4,7);
                int maxLengthYear = Integer.parseInt(String.valueOf(String.valueOf(firstDayOfNewMonth).length()));
                int minLengthYear = maxLengthYear - 4;

                String year = String.valueOf(firstDayOfNewMonth).substring(minLengthYear,maxLengthYear);
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

    private void getEventCalender() {
        //RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
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

                                @SuppressLint("SimpleDateFormat")
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

        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void checkLibur(String date) {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/checking_libur";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Success.Response", response.toString());
                        try {
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String tanggal = data.getString("tanggal");
                            String libur = data.getString("data");

                            if (status.equals("Success")){
                                statusLibur = "aktif";
                                if(libur.equals("Libur Minggu")){
                                    celebratePart.setVisibility(View.GONE);
                                } else {
                                    if (tanggal.equals(getDate())){
                                        celebratePart.setVisibility(View.VISIBLE);
                                        celebrateName.setText(libur);
                                    } else {
                                        celebratePart.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                statusLibur = "nonaktif";
                                celebratePart.setVisibility(View.GONE);
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
                params.put("tanggal", date);
                return params;
            }
        };

        requestQueue.add(postRequest);

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @SuppressLint("SetTextI18n")
    private void infoCuaca(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_info_cuaca, bottomSheet, false));
        currentAddress = findViewById(R.id.current_address);
        dataCuacaPart = findViewById(R.id.data_cuaca_part);
        weatherIconPart = findViewById(R.id.weather_icon_part);
        mainWeatherPart = findViewById(R.id.main_weather_part);
        tempWeatherPart = findViewById(R.id.weather_temp_part);
        feelLikeTempPart = findViewById(R.id.feels_like_temp_part);
        currentDatePart = findViewById(R.id.current_date_part);
        closeBTNPart = findViewById(R.id.close_btn_part);
        loadingCuaca = findViewById(R.id.loading_cuaca);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(MapsActivity.this, R.font.roboto);
            currentDatePart.setTypeface(typeface);
            feelLikeTempPart.setTypeface(typeface);
            currentAddress.setTypeface(typeface);
            mainWeatherPart.setTypeface(typeface);
            tempWeatherPart.setTypeface(typeface);
        }

        switch (getDateM()) {
            case "01":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Januari " + getDateY());
                break;
            case "02":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Februari " + getDateY());
                break;
            case "03":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Maret " + getDateY());
                break;
            case "04":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " April " + getDateY());
                break;
            case "05":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Mei " + getDateY());
                break;
            case "06":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juni " + getDateY());
                break;
            case "07":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Juli " + getDateY());
                break;
            case "08":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Agustus " + getDateY());
                break;
            case "09":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " September " + getDateY());
                break;
            case "10":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Oktober " + getDateY());
                break;
            case "11":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " November " + getDateY());
                break;
            case "12":
                currentDatePart.setText(currentDay+", "+String.valueOf(Integer.parseInt(getDateD()))+ " Desember " + getDateY());
                break;
            default:
                currentDatePart.setText("Not found!");
                break;
        }

        closeBTNPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });

        getApiKeyWeather();

    }

    private void getApiKeyWeather() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/get_api_key_weather";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String api_key = response.getString("api_key");
                            getCurrentLocation(api_key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    private void getCurrentLocation(String weather_key) {
        //progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();

                            getCurrentWeather(weather_key, String.valueOf(lati), String.valueOf(longi));
                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchaddressfromlocation(location);

                        }
                    }
                }, Looper.getMainLooper());

    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                if(resultData.getString(Constants.LOCAITY)!=null){
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT));
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE));
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.LOCAITY));
                            }
                        }
                    }
                } else {
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.DISTRICT));
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText(resultData.getString(Constants.STATE));
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                currentAddress.setText(resultData.getString(Constants.POST_CODE));
                            } else {
                                currentAddress.setText("Lokasi tidak ditemukan");
                            }
                        }
                    }
                }
            } else {
                currentAddress.setText(resultData.getString(Constants.NO_ADDRESS));
            }
        }

    }

    private void fetchaddressfromlocation(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEVIER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private void getCurrentWeather(String api_key, String lat, String lon) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+api_key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        dataCuacaPart.setVisibility(View.VISIBLE);
                        JSONArray data = null;
                        try {
                            data = response.getJSONArray("weather");
                            JSONObject suhu = response.getJSONObject("main");
                            String name = response.getString("name");
                            String temp = suhu.getString("temp");
                            String feels_like = suhu.getString("feels_like");
                            float f = Float.parseFloat(temp);
                            float f2 = Float.parseFloat(feels_like);
                            tempWeatherPart.setText(String.valueOf(Math.round(convertFromKelvinToCelsius(f)))+"°");
                            feelLikeTempPart.setText("Terasa seperti "+String.valueOf(Math.round(convertFromKelvinToCelsius(f2)))+"°");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject weather = data.getJSONObject(i);
                                String main = weather.getString("main");
                                String description = weather.getString("description");
                                String icon = weather.getString("icon");
                                String desc_idn = "";

                                //Thunderstorm
                                if (description.equals("thunderstorm with light rain")){
                                    desc_idn = "Badai petir disertai hujan ringan";
                                } else if (description.equals("thunderstorm with rain")){
                                    desc_idn = "Badai petir dengan hujan";
                                } else if (description.equals("thunderstorm with heavy rain")){
                                    desc_idn = "Badai petir dengan hujan lebat";
                                } else if (description.equals("thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("light thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("heavy thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("ragged thunderstorm")){
                                    desc_idn = "Badai petir";
                                } else if (description.equals("thunderstorm with light drizzle")){
                                    desc_idn = "Badai petir dengan gerimis";
                                } else if (description.equals("thunderstorm with drizzle")){
                                    desc_idn = "Badai petir dengan gerimis";
                                } else if (description.equals("thunderstorm with heavy drizzle")){
                                    desc_idn = "Badai petir dengan hujan";
                                }

                                //Drizzle
                                else if (description.equals("light intensity drizzle")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("drizzle")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("heavy intensity drizzle")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("light intensity drizzle rain")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("drizzle rain")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("heavy intensity drizzle rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("shower rain and drizzle")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("heavy shower rain and drizzle")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("shower drizzle")){
                                    desc_idn = "Gerimis";
                                }

                                //Rain
                                else if (description.equals("light rain")){
                                    desc_idn = "Gerimis";
                                } else if (description.equals("moderate rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("heavy intensity rain")){
                                    desc_idn = "Hujan deras";
                                } else if (description.equals("very heavy rain")){
                                    desc_idn = "Hujan deras";
                                } else if (description.equals("extreme rain")){
                                    desc_idn = "Hujan ekstrim";
                                } else if (description.equals("freezing rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("light intensity shower rain")){
                                    desc_idn = "Hujan ringan";
                                } else if (description.equals("shower rain")){
                                    desc_idn = "Hujan";
                                } else if (description.equals("heavy intensity shower rain")){
                                    desc_idn = "Hujan deras";
                                } else if (description.equals("ragged shower rain")){
                                    desc_idn = "Hujan deras";
                                }

                                //Snow
                                else if (description.equals("light snow")){
                                    desc_idn = "Salju ringan";
                                } else if (description.equals("snow")){
                                    desc_idn = "Salju";
                                } else if (description.equals("heavy snow")){
                                    desc_idn = "Salju tebal";
                                } else if (description.equals("sleet")){
                                    desc_idn = "Hujan es";
                                } else if (description.equals("light shower sleet")){
                                    desc_idn = "Hujan es ringan";
                                } else if (description.equals("shower sleet")){
                                    desc_idn = "Hujan es";
                                } else if (description.equals("light rain and snow")){
                                    desc_idn = "Hujan ringan dan salju";
                                } else if (description.equals("rain and snow")){
                                    desc_idn = "Hujan salju";
                                } else if (description.equals("light shower snow")){
                                    desc_idn = "Hujan salju ringan";
                                } else if (description.equals("shower snow")){
                                    desc_idn = "Hujan salju";
                                } else if (description.equals("heavy shower snow")){
                                    desc_idn = "Hujan salju lebat";
                                }

                                //Atmosphere
                                else if (description.equals("mist")){
                                    desc_idn = "Berkabut";
                                } else if (description.equals("smoke")){
                                    desc_idn = "Kabut asap";
                                } else if (description.equals("haze")){
                                    desc_idn = "Berkabut";
                                } else if (description.equals("sand/ dust whirls")){
                                    desc_idn = "Badai pasir/debu";
                                } else if (description.equals("fog")){
                                    desc_idn = "Berkabut";
                                } else if (description.equals("sand")){
                                    desc_idn = "Berpasir";
                                } else if (description.equals("dust")){
                                    desc_idn = "Berdebu";
                                } else if (description.equals("volcanic ash")){
                                    desc_idn = "Abu vulkanik";
                                } else if (description.equals("squalls")){
                                    desc_idn = "Badai";
                                } else if (description.equals("tornado")) {
                                    desc_idn = "Angin topan";
                                }

                                //Clear
                                else if (description.equals("clear sky")){
                                    desc_idn = "Langit cerah";
                                }

                                //Clouds
                                else if (description.equals("few clouds")){
                                    desc_idn = "Sebagian berawan";
                                } else if (description.equals("scattered clouds")){
                                    desc_idn = "Berawan";
                                } else if (description.equals("broken clouds")){
                                    desc_idn = "Berawan";
                                } else if (description.equals("overcast clouds")){
                                    desc_idn = "Awan mendung";
                                }

                                mainWeatherPart.setText(desc_idn);
                                loadingCuaca.setVisibility(View.GONE);
                                weatherIconPart.setVisibility(View.VISIBLE);
                                String url = "https://geloraaksara.co.id/absen-online/upload/weather_icon/"+icon+".png";

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(weatherIconPart);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dataCuacaPart.setVisibility(View.GONE);
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    private void checkWarning() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/warning_absen";
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
                                String alpa = data.getString("alpa");
                                String terlambat = data.getString("terlambat");
                                String tidak_checkout = data.getString("tidak_checkout");
                                String cuaca_button = data.getString("cuaca_button");
                                String fake_time = data.getString("faketime_check");
                                String devmod_check = data.getString("devmod_check");
                                String join_reminder = data.getString("join_reminder");
                                String monitoring = data.getString("monitoring");
                                String cegat_device = data.getString("cegat_device");
                                String fitur_pengumuman = data.getString("fitur_pengumuman");
                                String pengumuman_date = data.getString("pengumuman_date");
                                String pengumuman_title = data.getString("pengumuman_title");
                                String pengumuman_desc = data.getString("pengumuman_desc");
                                String cek_email = data.getString("cek_email");
                                String cek_nohp = data.getString("cek_nohp");

                                String id_cab = data.getString("id_cab");
                                String id_dept = data.getString("id_dept");
                                String id_bagian = data.getString("id_bagian");
                                String id_jabatan = data.getString("id_jabatan");

                                if(!sharedPrefManager.getSpIdCab().equals(id_cab)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, id_cab);
                                }
                                if(!sharedPrefManager.getSpIdHeadDept().equals(id_dept)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, id_dept);
                                }
                                if(!sharedPrefManager.getSpIdDept().equals(id_bagian)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, id_bagian);
                                }
                                if(!sharedPrefManager.getSpIdJabatan().equals(id_jabatan)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, id_jabatan);
                                }

                                if(fitur_pengumuman.equals("0")){
                                    if (sharedPrefManager.getSpNik().length()==10){
                                        String tgl_masuk = data.getString("tgl_masuk");
                                        String tglBulanMasuk = tgl_masuk.substring(5,10);
                                        String tahunMasuk = tgl_masuk.substring(0,4);

                                        if(tglBulanMasuk.equals(getDayMonth())) {
                                            int masaKerja = Integer.parseInt(getDateY()) - Integer.parseInt(tahunMasuk);
                                            reminderDecs.setText("Hari ini tepat "+String.valueOf(masaKerja)+" tahun anda bekerja di PT. Gelora Aksara Pratama");
                                            reminderCelebrateTV.setText("Selamat Merayakan "+String.valueOf(masaKerja)+" Tahun Masa Kerja.");
                                            if (join_reminder.equals("1")){
                                                reminderCongrat.setVisibility(View.VISIBLE);
                                                Picasso.get().load(R.drawable.suma_good).networkPolicy(NetworkPolicy.NO_CACHE)
                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                        .into(reminderImage);
                                            } else {
                                                reminderCongrat.setVisibility(View.GONE);
                                            }
                                        } else {
                                            reminderCongrat.setVisibility(View.GONE);
                                        }
                                    }
                                } else if(fitur_pengumuman.equals("1")) {

                                    if(pengumuman_date.equals(getDate())){
                                        reminderCongrat.setVisibility(View.VISIBLE);
                                        Picasso.get().load(R.drawable.pengumuman_ic).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(reminderImage);
                                        reminderDecs.setText(pengumuman_title);
                                        reminderCelebrateTV.setText(pengumuman_desc);
                                    } else {
                                        if (sharedPrefManager.getSpNik().length()==10){
                                            String tgl_masuk = data.getString("tgl_masuk");
                                            String tglBulanMasuk = tgl_masuk.substring(5,10);
                                            String tahunMasuk = tgl_masuk.substring(0,4);

                                            if(tglBulanMasuk.equals(getDayMonth())) {
                                                int masaKerja = Integer.parseInt(getDateY()) - Integer.parseInt(tahunMasuk);
                                                reminderDecs.setText("Hari ini tepat "+String.valueOf(masaKerja)+" tahun anda bekerja di PT. Gelora Aksara Pratama");
                                                reminderCelebrateTV.setText("Selamat Merayakan "+String.valueOf(masaKerja)+" Tahun Masa Kerja.");
                                                if (join_reminder.equals("1")){
                                                    reminderCongrat.setVisibility(View.VISIBLE);
                                                    Picasso.get().load(R.drawable.suma_good).networkPolicy(NetworkPolicy.NO_CACHE)
                                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                            .into(reminderImage);
                                                } else {
                                                    reminderCongrat.setVisibility(View.GONE);
                                                }
                                            } else {
                                                reminderCongrat.setVisibility(View.GONE);
                                            }
                                        }
                                    }

                                }

                                fakeTimeCheck = fake_time;
                                devModCheck = devmod_check;

                                if (cuaca_button.equals("1")){
                                    cuacaBTN.setVisibility(View.VISIBLE);
                                } else {
                                    cuacaBTN.setVisibility(View.GONE);
                                }

                                if (monitoring.equals("1")){
                                    if (sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("11")){
                                        pantauBTN.setVisibility(View.VISIBLE);
                                    } else {
                                        pantauBTN.setVisibility(View.GONE);
                                    }
                                } else {
                                    pantauBTN.setVisibility(View.GONE);
                                }

                                if((cek_email.equals("")||cek_email==null||cek_email.equals("null")) && (cek_nohp.equals("")||cek_nohp==null||cek_nohp.equals("null"))){
                                    markerWarningAbsensi.setVisibility(View.VISIBLE);
                                } else {
                                    int alpaNumb = Integer.parseInt(alpa);
                                    int lateNumb = Integer.parseInt(terlambat);
                                    int noCheckoutNumb = Integer.parseInt(tidak_checkout);
                                    if (alpaNumb > 0 || lateNumb > 0 || noCheckoutNumb > 0){
                                        markerWarningAbsensi.setVisibility(View.VISIBLE);
                                    } else {
                                        markerWarningAbsensi.setVisibility(View.GONE);
                                    }
                                }

                                if(cegat_device.equals("1")){
                                    deviceIdFunction();
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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("bulan", getBulanTahun());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private static float convertFromKelvinToCelsius(float value) {
        return value - 273.15f;
    }

    private void deviceIdFunction() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_device_id";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");
                            String message = data.getString("message");

                            if (message.equals("Device ID tidak sesuai")){
                                warningPerangkat = "aktif";

                                if(pDialog==null){
                                    pDialog = new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("PERANGKAT ANDA TELAH DIGANTI, HARAP GUNAKAN PERANGKAT YANG TERAKHIR DIDAFTARKAN!")
                                            .setConfirmText("KELUAR");
                                    pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            logoutFunction();
                                        }
                                    });
                                    pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            dialog.dismiss();
                                            logoutFunction();
                                        }
                                    });
                                    pDialog.show();

                                    // Vibrate for 500 milliseconds
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        vibrate.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        //deprecated in API 26
                                        vibrate.vibrate(500);
                                    }

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
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("device_id_user", deviceID);
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(retryPolicy);

        requestQueue.add(postRequest);

    }

    private void checkNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/check_notification";
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
                                String count = data.getString("count");
                                String message_yet = data.getString("message_yet");
                                String count_finger = data.getString("count_finger");

                                if (count.equals("0") && message_yet.equals("0") && count_finger.equals("0")){
                                    markerNotification.setVisibility(View.GONE);
                                } else if (!count.equals("0") && message_yet.equals("0") && count_finger.equals("0")) {
                                    String visibility = data.getString("visibility");
                                    if (visibility.equals("0")){
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility.equals("1")){
                                        notifMasukTV.setText(count);
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
                                } else if (count.equals("0") && !message_yet.equals("0") && count_finger.equals("0")) {
                                    String visibility2 = data.getString("visibility2");
                                    if (visibility2.equals("0")){
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility2.equals("1")){
                                        notifMasukTV.setText(message_yet);
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
                                } else if (count.equals("0") && message_yet.equals("0") && !count_finger.equals("0")) {
                                    String visibility3 = data.getString("visibility3");
                                    if (visibility3.equals("0")){
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility3.equals("1")){
                                        notifMasukTV.setText(count_finger);
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
                                } else if (!count.equals("0") && !message_yet.equals("0") && count_finger.equals("0")) {
                                    String visibility = data.getString("visibility");
                                    String visibility2 = data.getString("visibility2");
                                    if (visibility.equals("0") && visibility2.equals("0")) {
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility.equals("1") && visibility2.equals("0")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt("0")));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("0") && visibility2.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt("0") + Integer.parseInt(message_yet)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("1") && visibility2.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt(message_yet)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
                                } else if (!count.equals("0") && message_yet.equals("0") && !count_finger.equals("0")) {
                                    String visibility = data.getString("visibility");
                                    String visibility3 = data.getString("visibility3");
                                    if (visibility.equals("0") && visibility3.equals("0")) {
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility.equals("1") && visibility3.equals("0")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt("0")));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("0") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt("0") + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("1") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
                                } else if (count.equals("0") && !message_yet.equals("0") && !count_finger.equals("0")) {
                                    String visibility2 = data.getString("visibility2");
                                    String visibility3 = data.getString("visibility3");
                                    if (visibility2.equals("0") && visibility3.equals("0")) {
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility2.equals("1") && visibility3.equals("0")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(message_yet) + Integer.parseInt("0")));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility2.equals("0") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt("0") + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility2.equals("1") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(message_yet) + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
                                } else if (!count.equals("0") && !message_yet.equals("0") && !count_finger.equals("0")) {
                                    String visibility = data.getString("visibility");
                                    String visibility2 = data.getString("visibility2");
                                    String visibility3 = data.getString("visibility3");
                                    if (visibility.equals("0") && visibility2.equals("0") && visibility3.equals("0")) {
                                        markerNotification.setVisibility(View.GONE);
                                    } else if (visibility.equals("1") && visibility2.equals("0") && visibility3.equals("0")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt("0") + Integer.parseInt("0")));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("0") && visibility2.equals("1") && visibility3.equals("0")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt("0") + Integer.parseInt(message_yet) + Integer.parseInt("0")));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("0") && visibility2.equals("0") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt("0") + Integer.parseInt("0") + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("1") && visibility2.equals("1") && visibility3.equals("0")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt(message_yet) + Integer.parseInt("0")));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("0") && visibility2.equals("1") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt("0") + Integer.parseInt(message_yet) + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("1") && visibility2.equals("0") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt("0") + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    } else if (visibility.equals("1") && visibility2.equals("1") && visibility3.equals("1")){
                                        notifMasukTV.setText(String.valueOf(Integer.parseInt(count) + Integer.parseInt(message_yet) + Integer.parseInt(count_finger)));
                                        markerNotification.setVisibility(View.VISIBLE);
                                    }
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
                params.put("id_departemen", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void checkTime() {
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/server_time";
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
                                String dateTimeServer = data.getString("time");
                                String tolerance = data.getString("tolerance");

                                int timeTolerance = Integer.parseInt(tolerance);

                                String timeServer = dateTimeServer;
                                String timeDevice = getDateTime();

                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date1 = format.parse(timeServer);
                                    date2 = format.parse(timeDevice);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long waktu1 = date1.getTime();
                                long waktu2 = date2.getTime();
                                long selisih_waktu = waktu1 - waktu2;

                                if(selisih_waktu<0){
                                    selisih_waktu = selisih_waktu * -1;
                                } else {
                                    selisih_waktu = selisih_waktu;
                                }

                                if (selisih_waktu<=timeTolerance){
                                    String zonaWaktu;
                                    if (getTimeZone().equals("GMT+07:00")||getTimeZone().equals("UTC+07")){
                                        zonaWaktu = "WIB";
                                    } else if (getTimeZone().equals("GMT+08:00")||getTimeZone().equals("UTC+08")){
                                        zonaWaktu = "WITA";
                                    } else if (getTimeZone().equals("GMT+09:00")||getTimeZone().equals("UTC+09")){
                                        zonaWaktu = "WIT";
                                    } else {
                                        zonaWaktu = getTimeZone();
                                    }

                                    if(checkinTimeZone.equals(zonaWaktu)){
                                        timeDetection = "matching";
                                    } else {
                                        timeDetection = "different";
                                    }

                                } else {
                                    timeDetection = "different";
                                }

                            } else {
                                timeDetection = "different";
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
                        timeDetection = "different";
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("time_zone", getTimeZone());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void bukaSesi() {
        openSessionBTN.setVisibility(View.GONE);
        attantionPart.setVisibility(View.VISIBLE);
        statusAbsenBTN.setVisibility(View.VISIBLE);
        changeStatusAbsen.setVisibility(View.GONE);
        statusAbsenChoice.setVisibility(View.GONE);
        shiftBTN.setVisibility(View.GONE);
        changeShiftAbsen.setVisibility(View.GONE);
        shiftAbsenChoice.setVisibility(View.GONE);
        actionBTN.setBackground(ContextCompat.getDrawable(MapsActivity.this, R.drawable.shape_disable_btn));
        actionTV.setText("CHECK IN");
        inputAbsenPart.setVisibility(View.VISIBLE);
        recordAbsenPart.setVisibility(View.GONE);
        skeletonLayout.setVisibility(View.GONE);
        statusAction = "checkin";
        idShiftAbsen = "";
        sesiBaru = "aktif";
        actionButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
        statusLooping = "off";
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
        statusLooping = "off";
    }

}