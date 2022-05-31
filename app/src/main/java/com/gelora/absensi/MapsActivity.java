package com.gelora.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.shasin.notificationbanner.Banner;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

import static android.service.controls.ControlsProviderService.TAG;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //private StatusBarColorManager mStatusBarColorManager;
    private GoogleMap mMap;
    private LatLng userPoint;
    double userLat, userLong;
    SwipeRefreshLayout refreshLayout;
    ImageView weatherIconPart, onlineGif, loadingGif, warningGif;
    TextView izinDesc, currentDatePart, mainWeatherPart, tempWeatherPart, feelLikeTempPart, currentAddress, celebrateName, dateCheckinTV, dateCheckoutTV, eventCalender, monthTV, yearTV, ucapanTV, detailAbsenTV, timeCheckinTV, checkinPointTV, timeCheckoutTV, checkoutPointTV, actionTV, indicatorAbsen, hTime, mTime, sTime, absenPoint, statusAbsenTV, dateTV, userTV, statusAbsenChoiceTV, shiftAbsenChoiceTV;
    LinearLayout openSessionBTN, skeletonLayout, closeBTNPart, dataCuacaPart, cuacaBTN, celebratePart, prevBTN, nextBTN, warningPart, closeBTN, connectionSuccess, connectionFailed, loadingLayout, userBTNPart, reloadBTN, izinPart, layoffPart, attantionPart, recordAbsenPart, inputAbsenPart, actionBTN, pointPart, statusAbsenBTN, shiftBTN, statusAbsenChoice, changeStatusAbsen, shiftAbsenChoice, changeShiftAbsen, statusAbsenChoiceBTN, shiftAbsenChoiceBTN;
    BottomSheetLayout bottomSheet;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    String idIzin = "", statusLibur = "nonaktif", dialogAktif = "0", intervalTime, dateCheckin, statusTglLibur = "0", shiftType, shortName, pesanCheckout, statusPulangCepat, radiusZone = "undefined", idCheckin = "", idStatusAbsen, idShiftAbsen = "", namaStatusAbsen = "undefined", descStatusAbsen, namaShiftAbsen = "undefined", datangShiftAbsen = "00:00:00", pulangShiftAbsen = "00:00:00", batasPulang = "00:00:00", currentDay, statusAction = "undefined", lateTime, lateStatus, overTime, checkoutStatus;
    View rootview;
    DayNightSwitch dayNightSwitch;
    LocationManager locationManager;
    CompactCalendarView compactCalendarView;
    KAlertDialog pDialog;
    ResultReceiver resultReceiver;

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

        //ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        //ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        resultReceiver = new MapsActivity.AddressResultReceiver(new Handler());
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        //mStatusBarColorManager = new StatusBarColorManager(this);
        //mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        //MapsActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);

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
        statusAbsenTV = findViewById(R.id.status_absen_tv);
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
        reloadBTN = findViewById(R.id.reload_btn);
        userBTNPart = findViewById(R.id.user_btn_part);
        loadingLayout = findViewById(R.id.layout_loding);
        loadingGif = findViewById(R.id.loading);
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
        requestQueue = Volley.newRequestQueue(getBaseContext());

        Glide.with(getApplicationContext())
                .load(R.drawable.icon_none)
                .into(onlineGif);

        Glide.with(getApplicationContext())
                .load(R.drawable.load_progress)
                .into(loadingGif);

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

        dayNightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayNightSwitch.isNight()) {
                    dayNightSwitch.setIsNight(false, mMap.setMapStyle(null));
                    //MapsActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
                } else {
                    dayNightSwitch.setIsNight(true, mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
                    //MapsActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
                }
            }
        });

        openSessionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Apakah anda yakin untuk membuka sesi absensi dan menghapus status yang tertera hari ini?")
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
                                editIzin();
                            }
                        })
                        .show();
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

        userBTNPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    startLocationUpdates();
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
        } else {
            gpsEnableAction();
        }

        userPoint = new LatLng(userLat, userLong);

        // User position camera
        float zoomLevel = 18.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
        mMap.getUiSettings().setCompassEnabled(false);

        getAction();

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
                        onLocationChanged(locationResult.getLastLocation());
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
                        // connectionFailed();
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

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
        //return ("01:00:00");
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
        //return ("2022-06-01");
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

        statusAbsenRV.setLayoutManager(new LinearLayoutManager(this));
        statusAbsenRV.setHasFixedSize(true);
        statusAbsenRV.setItemAnimator(new DefaultItemAnimator());

        getStatusAbsenBagian();

    }

    private void shiftAbsen(){
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_shift_absen, bottomSheet, false));
        shifAbsenRV = findViewById(R.id.shift_absen_rv);

        shifAbsenRV.setLayoutManager(new LinearLayoutManager(this));
        shifAbsenRV.setHasFixedSize(true);
        shifAbsenRV.setItemAnimator(new DefaultItemAnimator());

        getShiftAbsenBagian();

    }

    @Override
    public void onBackPressed() {
        if (bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    private void checkLogin() {
        if(!sharedPrefManager.getSpSudahLogin()){
            Intent intent = new Intent(this, LoginActivity.class);
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
                dateTV.setText(getDateD() + " Januari " + getDateY());
                break;
            case "02":
                dateTV.setText(getDateD() + " Februari " + getDateY());
                break;
            case "03":
                dateTV.setText(getDateD() + " Maret " + getDateY());
                break;
            case "04":
                dateTV.setText(getDateD() + " April " + getDateY());
                break;
            case "05":
                dateTV.setText(getDateD() + " Mei " + getDateY());
                break;
            case "06":
                dateTV.setText(getDateD() + " Juni " + getDateY());
                break;
            case "07":
                dateTV.setText(getDateD() + " Juli " + getDateY());
                break;
            case "08":
                dateTV.setText(getDateD() + " Agustus " + getDateY());
                break;
            case "09":
                dateTV.setText(getDateD() + " September " + getDateY());
                break;
            case "10":
                dateTV.setText(getDateD() + " Oktober " + getDateY());
                break;
            case "11":
                dateTV.setText(getDateD() + " November " + getDateY());
                break;
            case "12":
                dateTV.setText(getDateD() + " Desember " + getDateY());
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

            Notify.build(getApplicationContext())
                    .setTitle("Absensi App")
                    .setContent("Anda lembur di hari libur. Selamat bekerja dan utamakan keselamatan")
                    .setSmallIcon(R.drawable.ic_skylight_notification)
                    .setColor(R.color.colorPrimary)
                    .largeCircularIcon()
                    .enableVibration(true)
                    .show();

        } else {
            if (waktu1<waktu2+60000){
                // lateStatus = "1";
                lateTime = "00:00:00";
            } else {
                // lateStatus = "2";
                lateTime = String.valueOf((f.format(hour)) + ":" + (f.format(min)) + ":" + f.format(sec));
                String lateDesc = "";

                if(!String.valueOf((f.format(hour))).equals("00")){ // 01:01:01
                    if (!String.valueOf((f.format(min))).equals("00")){ // 01:01:01
                        lateDesc = String.valueOf((f.format(hour)) + " jam " + (f.format(min)) + " menit " + f.format(sec) + " detik");
                    } else { // 01:00:01
                        lateDesc = String.valueOf((f.format(hour)) + " jam " + f.format(sec) + " detik");
                    }
                } else { // 00:01:01
                    if (!String.valueOf((f.format(min))).equals("00")){ // 00:01:01
                        lateDesc = String.valueOf((f.format(min)) + " menit " + f.format(sec) + " detik");
                    } else { // 00:00:01
                        lateDesc = String.valueOf(f.format(sec) + " detik");
                    }
                }

                Notify.build(getApplicationContext())
                        .setTitle("Absensi App")
                        .setContent("Anda terlambat "+lateDesc+", segera gunakan prosedur fingerscan dan serahkan ke bagian HRD")
                        .setSmallIcon(R.drawable.ic_skylight_notification)
                        .setColor(R.color.colorPrimary)
                        .largeCircularIcon()
                        .enableVibration(true)
                        .show();
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
        // String status_terlambat = lateStatus;
        String waktu_terlambat = lateTime;
        String latitude = String.valueOf(userLat);
        String longitude = String.valueOf(userLong);
        String status_absen = idStatusAbsen;

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
                            idCheckin = data.getString("id_checkin");
                            checkAbsen();

                            dialogAktif = "1";

                            // pDialog.setTitleText("Check In Berhasil!")
                            //       .setConfirmText("OK")
                            //       .changeAlertType(KAlertDialog.SUCCESS_TYPE);

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
                // params.put("status_terlambat", status_terlambat);
                params.put("waktu_terlambat", waktu_terlambat);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("status_absen", status_absen);
                params.put("checkin_point", checkin_point);

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
                                                .setConfirmText("OK")
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

                                pDialog.setTitleText("Anda Diliburkan!")
                                        .setConfirmText("OK")
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

                            if(status.equals("Success")){
                                JSONObject data_checkin = data.getJSONObject("data");
                                String date_checkin = data_checkin.getString("tanggal");
                                String time_checkin = data_checkin.getString("jam_masuk");
                                String checkin_point = data_checkin.getString("checkin_point");
                                String id_status = data_checkin.getString("status_absen");
                                String id_shift = data_checkin.getString("id_shift");
                                idCheckin = data_checkin.getString("id");
                                dateCheckinTV.setText(date_checkin);
                                timeCheckinTV.setText(time_checkin+" "+getTimeZone());
                                idStatusAbsen = id_status;
                                dateCheckin = date_checkin;

                                if (checkin_point.equals("")){
                                    checkinPointTV.setText(sharedPrefManager.getSpNama());
                                } else {
                                    checkinPointTV.setText(checkin_point);
                                }

                                //inputAbsenPart.setVisibility(View.GONE);
                                //recordAbsenPart.setVisibility(View.VISIBLE);
                                //attantionPart.setVisibility(View.GONE);

                                checkLibur(dateCheckin);
                                detailAbsen(id_status, id_shift);
                                String id_checkout = data.getString("id_checkout");
                                checkoutRecord(id_checkout);
                            } else {
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

                                                        if (isDeveloperModeEnabled()){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Perhatian!")
                                                                    .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                        } else {
                                                            actionCheckin();
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

                                                        if (isDeveloperModeEnabled()){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Perhatian!")
                                                                    .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                        } else {
                                                            if(radiusZone.equals("inside")){
                                                                actionCheckin();
                                                            } else {
                                                                pDialog.dismiss();
                                                                new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Perhatian!")
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

                                                    if (isDeveloperModeEnabled()){
                                                        pDialog.dismiss();
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Perhatian!")
                                                                .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                    } else {
                                                        actionCheckout();
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

                                                    if (isDeveloperModeEnabled()){
                                                        pDialog.dismiss();
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Perhatian!")
                                                                .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                    } else {
                                                        if(radiusZone.equals("inside")){
                                                            actionCheckout();
                                                        } else {
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Perhatian!")
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
                                                actionLayoff();
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
                                                    actionLayoff();
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

                                                        if (isDeveloperModeEnabled()){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Perhatian!")
                                                                    .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                        } else {
                                                            actionCheckout();
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

                                                        if (isDeveloperModeEnabled()){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Perhatian!")
                                                                    .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                        } else {
                                                            actionCheckout();
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

                                                        if (isDeveloperModeEnabled()){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Perhatian!")
                                                                    .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                        } else {
                                                            actionCheckin();
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

                                                        if (isDeveloperModeEnabled()){
                                                            pDialog.dismiss();
                                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Perhatian!")
                                                                    .setContentText("Mode Pengembang/Developer pada Smartphone Anda aktif, harap non-aktifkan terlebih dahulu!")
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
                                                        } else {
                                                            actionCheckin();
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
                                                    actionLayoff();
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
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, "");
        idShiftAbsen = "";
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
                mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
            } else if (hoursNow >= 0 && hoursNow <= 5){
                mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
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
                            String message = data.getString("message");
                            String id_checkout = data.getString("id_checkout");
                            checkoutRecord(id_checkout);

                            pDialog.setTitleText("Check Out Berhasil!")
                                    .setConfirmText("OK")
                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);

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
                params.put("kelebihan_jam", kelebihan_jam);
                params.put("status", status);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("checkout_point", checkout_point);

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
                //Pulang
                checkoutStatus = "1";
                overTime = "00:00:00";
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

                            if(status.equals("Success")){
                                String tgl_checkin = data.getString("tgl_checkin");
                                String tipe_shift = data.getString("tipe_shift");
                                String interval = data.getString("interval");
                                String timestamp_checkin = data.getString("timestamp_checkin");
                                JSONObject data_checkin = data.getJSONObject("data");
                                String time_checkout = data_checkin.getString("jam_pulang");
                                String date_checkout = data_checkin.getString("tanggal");
                                String checkout_point = data_checkin.getString("checkout_point");
                                shiftType = tipe_shift;
                                intervalTime = interval;

                                if (time_checkout.equals("00:00:00")){
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

                                            new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Perhatian!")
                                                    .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN UNTUK MENGOREKSI JAM PULANG, DAN SERAHKAN KE BAGIAN HRD. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                        @Override
                                                        public void onClick(KAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .show();

                                            Notify.build(getApplicationContext())
                                                    .setTitle("Absensi App")
                                                    .setContent("Sebelumnya anda tidak melakukan checkout, segera gunakan prosedur fingerscan untuk mengoreksi jam pulang, dan serahkan ke bagian HRD. Jika tidak dilakukan koreksi, maka jam kerja akan terhitung 0")
                                                    .setSmallIcon(R.drawable.ic_skylight_notification)
                                                    .setColor(R.color.colorPrimary)
                                                    .largeCircularIcon()
                                                    .enableVibration(true)
                                                    .show();

                                            warningPart.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Perhatian!")
                                                            .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN UNTUK MENGOREKSI JAM PULANG, DAN SERAHKAN KE BAGIAN HRD. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                            .setConfirmText("OK")
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
                                            String pulang  = tgl_checkin+" "+time_checkout;
                                            String batas = getDate()+" "+getTime();

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
                                            long waktu2 = date2.getTime();

                                            if (waktu>waktu2){
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

                                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN UNTUK MENGOREKSI JAM PULANG, DAN SERAHKAN KE BAGIAN HRD. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();

                                                Notify.build(getApplicationContext())
                                                        .setTitle("Absensi App")
                                                        .setContent("Sebelumnya anda tidak melakukan checkout, segera gunakan prosedur fingerscan untuk mengoreksi jam pulang, dan serahkan ke bagian HRD. Jika tidak dilakukan koreksi, maka jam kerja akan terhitung 0")
                                                        .setSmallIcon(R.drawable.ic_skylight_notification)
                                                        .setColor(R.color.colorPrimary)
                                                        .largeCircularIcon()
                                                        .enableVibration(true)
                                                        .show();

                                                warningPart.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN UNTUK MENGOREKSI JAM PULANG, DAN SERAHKAN KE BAGIAN HRD. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                                .setConfirmText("OK")
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

                                                new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN UNTUK MENGOREKSI JAM PULANG, DAN SERAHKAN KE BAGIAN HRD. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .show();

                                                Notify.build(getApplicationContext())
                                                        .setTitle("Absensi App")
                                                        .setContent("Sebelumnya anda tidak melakukan checkout, segera gunakan prosedur fingerscan untuk mengoreksi jam pulang, dan serahkan ke bagian HRD. Jika tidak dilakukan koreksi, maka jam kerja akan terhitung 0")
                                                        .setSmallIcon(R.drawable.ic_skylight_notification)
                                                        .setColor(R.color.colorPrimary)
                                                        .largeCircularIcon()
                                                        .enableVibration(true)
                                                        .show();

                                                warningPart.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        new KAlertDialog(MapsActivity.this, KAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Perhatian")
                                                                .setContentText("SEBELUMNYA ANDA TIDAK MELAKUKAN CHECKOUT, SEGERA GUNAKAN PROSEDUR FINGERSCAN UNTUK MENGOREKSI JAM PULANG, DAN SERAHKAN KE BAGIAN HRD. JIKA TIDAK DILAKUKAN KOREKSI, MAKA JAM KERJA AKAN TERHITUNG 0")
                                                                .setConfirmText("OK")
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

                                        if (dialogAktif.equals("1")){
                                            pDialog.setTitleText("Check In Berhasil!")
                                                    .setConfirmText("OK")
                                                    .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                            dialogAktif = "0";
                                        }

                                        warningPart.setVisibility(View.GONE);
                                        dateCheckoutTV.setText("---- - -- - --");
                                        timeCheckoutTV.setText("-- : -- : --");
                                        statusAction = "checkout";
                                        ucapanTV.setText("Selamat dan SUMAngat Bekerja!");
                                        inputAbsenPart.setVisibility(View.GONE);
                                        recordAbsenPart.setVisibility(View.VISIBLE);
                                        attantionPart.setVisibility(View.GONE);
                                        skeletonLayout.setVisibility(View.GONE);
                                        actionButton();
                                    }

                                } else {
                                    warningPart.setVisibility(View.GONE);
                                    inputAbsenPart.setVisibility(View.GONE);
                                    recordAbsenPart.setVisibility(View.VISIBLE);
                                    attantionPart.setVisibility(View.GONE);
                                    skeletonLayout.setVisibility(View.GONE);
                                    dateCheckoutTV.setText(date_checkout);
                                    timeCheckoutTV.setText(time_checkout+" "+getTimeZone());
                                    ucapanTV.setText("Terima kasih telah masuk kerja hari ini.");

                                    //if (tgl_checkin.equals(getDate())){
                                    statusAction = "history";
                                    //} else {
                                    //      statusAction = "history";
                                    //      new Handler().postDelayed(new Runnable() {
                                    //        @Override
                                    //         public void run() {
                                    //             statusAction = "checkin";
                                    //             idShiftAbsen = "";
                                    //           checkIzin();
                                    //         }
                                    //     }, 6000);
                                    // }

                                    actionButton();

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
        }
    }

    private void checkPulang(){
        String batasAbsenPulang;
        if (shiftType.equals("Normal")||shiftType.equals("Tanggung")){
            batasAbsenPulang = dateCheckin+" "+batasPulang;
        } else {
            String dayCheckout = dateCheckin.substring(8,10);
            String dateCheckout = dateCheckin.substring(0,8);
            int tgl_besok = Integer.parseInt(dayCheckout)+1;
            batasAbsenPulang = dateCheckout+String.valueOf(tgl_besok)+" "+batasPulang;
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
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();

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
                //MapsActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
            } else if (hoursNow >= 0 && hoursNow <= 5){
                dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
                //MapsActivity.this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
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
        Banner.make(rootview, MapsActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 4000).show();

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

        switch (getDateM()) {
            case "01":
                currentDatePart.setText(currentDay+", "+getDateD() + " Januari " + getDateY());
                break;
            case "02":
                currentDatePart.setText(currentDay+", "+getDateD() + " Februari " + getDateY());
                break;
            case "03":
                currentDatePart.setText(currentDay+", "+getDateD() + " Maret " + getDateY());
                break;
            case "04":
                currentDatePart.setText(currentDay+", "+getDateD() + " April " + getDateY());
                break;
            case "05":
                currentDatePart.setText(currentDay+", "+getDateD() + " Mei " + getDateY());
                break;
            case "06":
                currentDatePart.setText(currentDay+", "+getDateD() + " Juni " + getDateY());
                break;
            case "07":
                currentDatePart.setText(currentDay+", "+getDateD() + " Juli " + getDateY());
                break;
            case "08":
                currentDatePart.setText(currentDay+", "+getDateD() + " Agustus " + getDateY());
                break;
            case "09":
                currentDatePart.setText(currentDay+", "+getDateD() + " September " + getDateY());
                break;
            case "10":
                currentDatePart.setText(currentDay+", "+getDateD() + " Oktober " + getDateY());
                break;
            case "11":
                currentDatePart.setText(currentDay+", "+getDateD() + " November " + getDateY());
                break;
            case "12":
                currentDatePart.setText(currentDay+", "+getDateD() + " Desember " + getDateY());
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
                            //Toast.makeText(UserActivity.this, String.format("Latitude : %s\n Longitude: %s", lati, longi), Toast.LENGTH_SHORT).show();

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
                currentAddress.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
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
                            //Toast.makeText(UserActivity.this, name, Toast.LENGTH_SHORT).show();
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

    private static float convertFromKelvinToCelsius(float value) {
        return value - 273.15f;
    }

    @Override
    public void onPause() {
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }

}