package com.gelora.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.StatusBarColorManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.shasin.notificationbanner.Banner;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import static android.service.controls.ControlsProviderService.TAG;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class SplashScreen extends AppCompatActivity {

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    View rootview;
    LinearLayout refreshPart, refreshBTN, updateBTN, closeBTN, updateLayout, updateDialog;
    TextView loadingOff, refreshLabel, descTV;
    String closeBottomSheet, statusUpdateLayout = "0";
    SwipeRefreshLayout refreshLayout;
    ProgressBar loadingProgressBar;

    private StatusBarColorManager mStatusBarColorManager;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        rootview = findViewById(android.R.id.content);
        updateLayout = findViewById(R.id.update_layout);
        updateDialog = findViewById(R.id.update_dialog);
        updateBTN = findViewById(R.id.update_btn);
        closeBTN = findViewById(R.id.close_btn);
        descTV = findViewById(R.id.desc_tv);
        refreshBTN = findViewById(R.id.refresh_ss_btn);
        refreshPart = findViewById(R.id.refresh_part);
        refreshLabel = findViewById(R.id.refresh_label);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        loadingOff = findViewById(R.id.loading_off);
        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLabel.setText("LOADING...");
                        loadingOff.setVisibility(View.GONE);
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        refreshBTN.setOnClickListener(null);
                        refreshBTN.setBackground(ContextCompat.getDrawable(SplashScreen.this, R.drawable.shape_refresh_ss_off));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                        }
                        versionCheck();
                    }
                }, 50);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                }
                versionCheck();
            }
        }, 50);

        refreshBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                refreshLabel.setText("LOADING...");
                loadingOff.setVisibility(View.GONE);
                loadingProgressBar.setVisibility(View.VISIBLE);
                refreshBTN.setOnClickListener(null);
                refreshBTN.setBackground(ContextCompat.getDrawable(SplashScreen.this, R.drawable.shape_refresh_ss_off));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                        }
                        versionCheck();
                    }
                }, 1200);
            }
        });

    }

    private void gpsEnableAction() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(SplashScreen.this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    permissionLoc();
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
                                        SplashScreen.this,
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

    @SuppressLint("InlinedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.i(TAG, "onActivityResult: GPS Enabled by user");
                        permissionLoc();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.i(TAG, "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void permissionLoc() {
        if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    permissionLoc();
                }
            }, 50);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 350);
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
        getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    Log.e("TAG", "GPS is on" + String.valueOf(location));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 50);
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

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (location != null) {
            Log.e("TAG", "GPS is on" + String.valueOf(location));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 50);

        } else {
            gpsEnableAction();
        }

    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                            Log.e("TAG", "GPS is on" + String.valueOf(location));
                        }  else {
                            gpsEnableAction();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error trying to get last GPS location");
                        e.printStackTrace();
                        gpsEnableAction();
                    }
                });

    }

    private void versionCheck() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/version_app";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            refreshLayout.setRefreshing(false);
                            refreshBTN.setBackground(ContextCompat.getDrawable(SplashScreen.this, R.drawable.shape_refresh_ss));

                            refreshBTN.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(View v) {
                                    refreshLabel.setText("LOADING...");
                                    loadingOff.setVisibility(View.GONE);
                                    loadingProgressBar.setVisibility(View.VISIBLE);
                                    refreshBTN.setOnClickListener(null);
                                    refreshBTN.setBackground(ContextCompat.getDrawable(SplashScreen.this, R.drawable.shape_refresh_ss_off));

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                                            }
                                            versionCheck();
                                        }
                                    }, 2000);
                                }
                            });

                            String status = response.getString("status");
                            String version = response.getString("version");
                            String popup = response.getString("pop_up");
                            String close_btn = response.getString("close_btn");

                            if (status.equals("Success")){
                                String currentVersion = "1.7.6";
                                if (!currentVersion.equals(version) && popup.equals("1")){

                                    refreshPart.animate()
                                        .translationY(refreshPart.getHeight())
                                        .setDuration(300)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            refreshPart.setVisibility(View.GONE);
                                            }
                                        });

                                    statusUpdateLayout = "1";

                                    Animation animation = new TranslateAnimation(0, 0,500, 0);
                                    animation.setDuration(600);
                                    animation.setFillAfter(true);
                                    updateDialog.startAnimation(animation);
                                    updateDialog.setVisibility(View.VISIBLE);
                                    updateLayout.setVisibility(View.VISIBLE);

                                    descTV.setText("HRIS Mobile Gelora v "+version+" telah tersedia di Google Play Store");

                                    updateLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    });

                                    updateBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            statusUpdateLayout = "0";
                                            updateDialog.animate()
                                                    .translationY(updateDialog.getHeight())
                                                    .setDuration(300)
                                                    .setListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            super.onAnimationEnd(animation);
                                                            updateDialog.setVisibility(View.GONE);
                                                        }
                                                    });
                                            updateLayout.animate()
                                                    .setListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            super.onAnimationEnd(animation);
                                                            updateLayout.setVisibility(View.GONE);
                                                        }
                                                    });
                                            Intent webIntent = new Intent(Intent.ACTION_VIEW);
                                            webIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.gelora.absensi"));
                                            try {
                                                startActivity(webIntent);
                                                refreshPart.setVisibility(View.GONE);
                                                finish();
                                            } catch (SecurityException e) {
                                                e.printStackTrace();
                                                new KAlertDialog(SplashScreen.this, KAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Perhatian")
                                                        .setContentText("Tidak dapat terhubung ke Play Store, anda dapat membuka Play Store secara langsung dan cari HRIS Mobile Gelora di kolom pencarian")
                                                        .setConfirmText("TUTUP")
                                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                            @Override
                                                            public void onClick(KAlertDialog sDialog) {
                                                                sDialog.dismiss();
                                                                permissionLoc();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }
                                    });

                                    closeBottomSheet = close_btn;
                                    if (close_btn.equals("1")){
                                        closeBTN.setVisibility(View.VISIBLE);
                                    } else {
                                        closeBTN.setVisibility(View.GONE);
                                    }

                                    closeBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            statusUpdateLayout = "0";
                                            updateDialog.animate()
                                                    .translationY(updateDialog.getHeight())
                                                    .setDuration(300)
                                                    .setListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            super.onAnimationEnd(animation);
                                                            updateDialog.setVisibility(View.GONE);
                                                            updateLayout.setVisibility(View.GONE);
                                                        }
                                                    });
                                            updateLayout.animate()
                                                    .setListener(new AnimatorListenerAdapter() {
                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            super.onAnimationEnd(animation);
                                                            updateLayout.setVisibility(View.GONE);
                                                        }
                                                    });
                                            refreshLayout.setRefreshing(false);
                                            permissionLoc();
                                        }
                                    });

                                } else {
                                    refreshLayout.setRefreshing(false);
                                    refreshPart.animate()
                                            .translationY(refreshPart.getHeight())
                                            .setDuration(300)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    refreshPart.setVisibility(View.GONE);
                                                }
                                            });
                                    permissionLoc();
                                }
                            } else {
                                refreshPart.setVisibility(View.VISIBLE);
                                refreshLabel.setText("REFRESH");

                                Banner.make(rootview, SplashScreen.this, Banner.ERROR, "Not found!", Banner.BOTTOM, 3000).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                refreshLayout.setRefreshing(false);
                loadingOff.setVisibility(View.VISIBLE);
                loadingProgressBar.setVisibility(View.GONE);

                refreshBTN.setBackground(ContextCompat.getDrawable(SplashScreen.this, R.drawable.shape_refresh_ss));
                refreshBTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        refreshLabel.setText("LOADING...");
                        loadingOff.setVisibility(View.GONE);
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        refreshBTN.setOnClickListener(null);
                        refreshBTN.setBackground(ContextCompat.getDrawable(SplashScreen.this, R.drawable.shape_refresh_ss_off));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                                }
                                versionCheck();
                            }
                        }, 2000);
                    }
                });

                Animation animation = new TranslateAnimation(0, 0,500, 0);
                animation.setDuration(600);
                animation.setFillAfter(true);
                refreshPart.startAnimation(animation);
                refreshPart.setVisibility(View.VISIBLE);
                refreshLabel.setText("REFRESH");

                CookieBar.build(SplashScreen.this)
                        .setCustomView(R.layout.layout_custom_cookie)
                        .setEnableAutoDismiss(true)
                        .setSwipeToDismiss(false)
                        .setCookiePosition(Gravity.TOP)
                        .show();

            }
        });

        requestQueue.add(request);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                }
                versionCheck();
            }
        }, 400);
    }

    @Override
    public void onBackPressed() {
        if (statusUpdateLayout.equals("1")){
            if (closeBottomSheet.equals("1")){
                statusUpdateLayout = "0";
                updateDialog.animate()
                        .translationY(updateDialog.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                updateDialog.setVisibility(View.GONE);
                            }
                        });
                updateLayout.animate()
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                updateLayout.setVisibility(View.GONE);
                            }
                        });
                permissionLoc();
            } else {
                statusUpdateLayout = "0";
                updateDialog.animate()
                        .translationY(updateDialog.getHeight())
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                updateDialog.setVisibility(View.GONE);
                            }
                        });
                updateLayout.animate()
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                updateLayout.setVisibility(View.GONE);
                            }
                        });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeApp();
                    }
                }, 300);

            }
        } else {
            super.onBackPressed();
        }
    }

    private void closeApp(){
        super.onBackPressed();
    }

}