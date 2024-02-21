package com.gelora.absensi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailReportSumaActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout viewLampiranBTN, tglRencanaPart, backBTN, actionBar, mapsPart;
    SharedPrefManager sharedPrefManager;
    RequestQueue requestQueue;
    TextView tglRencanaTV, nikSalesTV, namaSalesTV, detailLocationTV, reportKategoriTV, namaPelangganTV, alamatPelangganTV, picPelangganTV, teleponPelangganTV, keteranganTV;
    String idReport = "";
    SwipeRefreshLayout refreshLayout;
    SharedPrefAbsen sharedPrefAbsen;
    private StatusBarColorManager mStatusBarColorManager;
    private LatLng userPoint;
    private GoogleMap mMap;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    ResultReceiver resultReceiver;
    Context mContext;
    Activity mActivity;
    String locationNow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report_suma);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        resultReceiver = new DetailReportSumaActivity.AddressResultReceiver(new Handler());
        mContext = getBaseContext();
        mActivity = DetailReportSumaActivity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
        idReport = getIntent().getExtras().getString("report_id");

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        reportKategoriTV = findViewById(R.id.report_kategori_tv);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);
        namaPelangganTV = findViewById(R.id.nama_pelanggan_tv);
        alamatPelangganTV = findViewById(R.id.alamat_pelanggan_tv);
        picPelangganTV = findViewById(R.id.pic_pelanggan_tv);
        teleponPelangganTV = findViewById(R.id.telepon_pelanggan_tv);
        keteranganTV = findViewById(R.id.keterangan_tv);
        detailLocationTV = findViewById(R.id.detail_location_tv);
        namaSalesTV = findViewById(R.id.nama_sales_tv);
        nikSalesTV = findViewById(R.id.nik_sales_tv);
        mapsPart = findViewById(R.id.maps_part);
        tglRencanaPart = findViewById(R.id.tgl_rencana_part);
        tglRencanaTV = findViewById(R.id.tgl_rencana_tv);
        viewLampiranBTN = findViewById(R.id.view_lampiran_btn);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        permissionLoc();
                    }
                }, 1000);
            }
        });

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

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0,0,0,0);
        permissionLoc();
    }

    private void permissionLoc(){
        if (ContextCompat.checkSelfPermission(DetailReportSumaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(DetailReportSumaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailReportSumaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            mMap.clear();
            getData();
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
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(DetailReportSumaActivity.this).checkLocationSettings(builder.build());
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
                                        DetailReportSumaActivity.this,
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

    private String getTimeH() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://reporting.sumasistem.co.id/api/report_detail_test";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "MissingPermission"})
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                JSONObject dataArray = data.getJSONObject("data");
                                String latitude = dataArray.getString("latitude");
                                String longitude = dataArray.getString("longitude");
                                String idSales = dataArray.getString("idSales");
                                getSales(idSales);
                                String tipeLaporan = dataArray.getString("tipeLaporan");

                                if(tipeLaporan.equals("1")){
                                    viewLampiranBTN.setVisibility(View.GONE);
                                    reportKategoriTV.setText("RENCANA KUNJUNGAN");
                                    tglRencanaPart.setVisibility(View.VISIBLE);
                                    String tgl_rencana = dataArray.getString("tgl_rencana");

                                    String input_date = tgl_rencana;
                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dt1 = null;
                                    try {
                                        dt1 = format1.parse(input_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    @SuppressLint("SimpleDateFormat")
                                    DateFormat format2 = new SimpleDateFormat("EEE");
                                    String finalDay = format2.format(dt1);
                                    String hariName = "";

                                    if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                                        hariName = "Senin";
                                    } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                                        hariName = "Selasa";
                                    } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                                        hariName = "Rabu";
                                    } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                                        hariName = "Kamis";
                                    } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                                        hariName = "Jumat";
                                    } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                                        hariName = "Sabtu";
                                    } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                                        hariName = "Minggu";
                                    }

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
                                            bulanName = "Not found!";
                                            break;
                                    }

                                    tglRencanaTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

                                } else if(tipeLaporan.equals("2")){
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("KUNJUNGAN");
                                    tglRencanaPart.setVisibility(View.GONE);

                                    String file = dataArray.getString("file");
                                } else if(tipeLaporan.equals("3")){
                                    viewLampiranBTN.setVisibility(View.VISIBLE);
                                    reportKategoriTV.setText("AKTIVITAS PENAGIHAN");
                                    tglRencanaPart.setVisibility(View.GONE);

                                    String file = dataArray.getString("file");
                                    viewLampiranBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailReportSumaActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", file);
                                            intent.putExtra("kode", "detail");
                                            intent.putExtra("jenis_detail", "suma");
                                            startActivity(intent);
                                        }
                                    });
                                }

                                String namaPelanggan = dataArray.getString("namaPelanggan");
                                String alamat_customer = dataArray.getString("alamat_customer");
                                String pic = dataArray.getString("pic");
                                String no_telp = dataArray.getString("no_telp");
                                String keterangan = dataArray.getString("keterangan");
                                namaPelangganTV.setText(namaPelanggan);
                                alamatPelangganTV.setText(alamat_customer);
                                picPelangganTV.setText(pic);
                                teleponPelangganTV.setText(no_telp);
                                keteranganTV.setText(keterangan);

                                if(mMap != null && (tipeLaporan.equals("2") || tipeLaporan.equals("3"))){
                                    mapsPart.setVisibility(View.VISIBLE);
                                    mMap.setMyLocationEnabled(false);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                    userPoint = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    float zoomLevel = 17.8f;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, zoomLevel));
                                    mMap.getUiSettings().setCompassEnabled(false);
                                    mMap.addMarker(new MarkerOptions().position(userPoint).title(sharedPrefManager.getSpNama()).snippet(latitude+","+longitude).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_picker_ic)));
                                    Location location = new Location("dummyProvider");
                                    location.setLatitude(Double.parseDouble(latitude));
                                    location.setLongitude(Double.parseDouble(longitude));
                                    new ReverseGeocodingTask().execute(location);
                                } else {
                                    mapsPart.setVisibility(View.GONE);
                                }

                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan saat mengakses data")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
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
                params.put("id_report", idReport);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void getSales(String nik) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_sales";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "MissingPermission"})
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String nama = data.getString("nama");
                                String NIK = data.getString("NIK");
                                namaSalesTV.setText(nama);
                                nikSalesTV.setText(NIK);
                            } else {
                                new KAlertDialog(DetailReportSumaActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan saat mengakses data")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
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
                params.put("nik", nik);
                return params;
            }
        };

        requestQueue.add(postRequest);

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
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.LOCAITY)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.LOCAITY));
                                locationNow = resultData.getString(Constants.LOCAITY);
                            }
                        }
                    }
                } else {
                    if(resultData.getString(Constants.DISTRICT)!=null){
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.DISTRICT)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.DISTRICT));
                                locationNow = resultData.getString(Constants.DISTRICT);
                            }
                        }
                    } else {
                        if(resultData.getString(Constants.STATE)!=null){
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.STATE)+", "+resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText(resultData.getString(Constants.STATE));
                                locationNow = resultData.getString(Constants.STATE);
                            }
                        } else {
                            if(resultData.getString(Constants.POST_CODE)!=null){
                                detailLocationTV.setText(resultData.getString(Constants.POST_CODE));
                                locationNow = resultData.getString(Constants.POST_CODE);
                            } else {
                                detailLocationTV.setText("Lokasi tidak ditemukan");
                                locationNow = "Lokasi tidak ditemukan";
                            }
                        }
                    }
                }
            } else {
                detailLocationTV.setText(resultData.getString(Constants.NO_ADDRESS));
                locationNow = resultData.getString(Constants.NO_ADDRESS);
            }
        }

    }

    private void fetchaddressfromlocation(Location location) {
        try {
            Context context = mContext;
            Intent intent = new Intent(context, FetchAddressIntentServices.class);
            intent.putExtra(Constants.RECEVIER, resultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
            context.startService(intent);
        }
        catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void connectionFailed(){
        CookieBar.build(DetailReportSumaActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @SuppressLint("StaticFieldLeak")
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... params) {
            Location location = params[0];
            String addressText = "";

            Geocoder geocoder = new Geocoder(DetailReportSumaActivity.this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressBuilder.append(address.getAddressLine(i)).append(", ");
                    }
                    addressText = addressBuilder.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching address: " + e.getMessage());
            }

            return addressText;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (!address.isEmpty()) {
                Log.d(TAG, "Alamat: " + address);
                detailLocationTV.setText(address.substring(0,address.length()-2));
            } else {
                Log.e(TAG, "Alamat tidak ditemukan");
                detailLocationTV.setText("Alamat tidak ditemukan");
            }
        }
    }

}