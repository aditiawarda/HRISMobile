package com.gelora.absensi;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FullMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    RequestQueue requestQueue;
    private StatusBarColorManager mStatusBarColorManager;
    private LatLng userPoint;
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    LinearLayout backBTN, pusatkanBTN;
    DayNightSwitch dayNightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }

        requestQueue = Volley.newRequestQueue(getBaseContext());
        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);

        backBTN = findViewById(R.id.back_btn);
        pusatkanBTN = findViewById(R.id.pusatkan_btn);
        dayNightSwitch = findViewById(R.id.day_night_switch);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (is_night) {
                    mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
                } else {
                    mMap.setMapStyle(null);
                }
            }
        });

        pusatkanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = 17.8f; //This goes up to 21
                if(userPoint!=null){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                    mMap.getUiSettings().setCompassEnabled(false);
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0,0,0,100);
        permissionLoc();
    }

    private void permissionLoc(){
        if (ContextCompat.checkSelfPermission(FullMapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(FullMapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FullMapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            mMap.clear();
            userPosition();
            getLocation();

            // Maps style
            int hoursNow = Integer.parseInt(getTimeH());
            if (hoursNow >= 18) {
                mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
                 dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
            } else if (hoursNow >= 0 && hoursNow <= 5){
                mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
                 dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
            } else {
                mMap.setMapStyle(null);
                 dayNightSwitch.setIsNight(false,  mMap.setMapStyle(null));
            }

        }
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

                        userPoint = new LatLng(location.getLatitude(), location.getLongitude());

                        float zoomLevel = 17.8f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                        mMap.getUiSettings().setCompassEnabled(false);

                        int hoursNow = Integer.parseInt(getTimeH());
                        if (hoursNow >= 18) {
                            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
                             dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
                        } else if (hoursNow >= 0 && hoursNow <= 5){
                            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
                             dayNightSwitch.setIsNight(true,  mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json))));
                        } else {
                            mMap.setMapStyle(null);
                             dayNightSwitch.setIsNight(false,  mMap.setMapStyle(null));
                        }

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

    private void gpsEnableAction(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(FullMapsActivity.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        FullMapsActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

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

                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject location = data.getJSONObject(i);
                                String latitude = location.getString("latitude");
                                String longitude = location.getString("longitude");
                                String radius = location.getString("radius");
                                String locationName = location.getString("nama");

                                LatLng pointAbsen = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                CircleOptions circleOptions = new CircleOptions();
                                circleOptions.center(pointAbsen);
                                circleOptions.radius(Integer.parseInt(radius));
                                circleOptions.fillColor(Color.parseColor("#80FB3527"));
                                circleOptions.strokeWidth(3);
                                circleOptions.strokeColor(Color.parseColor("#E6FB3527"));
                                mMap.addMarker(new MarkerOptions().position(pointAbsen).title(locationName));
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

    private void connectionFailed(){
        CookieBar.build(FullMapsActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

}