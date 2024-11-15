package com.gelora.absensi;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class FetchAddressIntentServices extends IntentService {
    ResultReceiver resultReceiver;

    public FetchAddressIntentServices() {
        super("FetchAddressIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            String errormessgae = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEVIER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if (location == null) {
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1);
            } catch (Exception ioException) {
                Log.e("", "Error in getting address for the location");
            }

            if (addresses == null || addresses.size() == 0) {
                errormessgae = "Lokasi tidak ditemukan";
                Bundle bundle = new Bundle();
                bundle.putString(Constants.NO_ADDRESS, errormessgae);
                resultReceiver.send(Constants.FAILURE_RESULT, bundle);
            } else {
                Address address = addresses.get(0);
                String str_postcode = address.getPostalCode();
                String str_Country = address.getCountryName();
                String str_state = address.getAdminArea();
                String str_district = address.getSubAdminArea();
                String str_locality = address.getLocality();
                String str_address = address.getFeatureName();
                devliverResultToRecevier(str_address, str_locality, str_district, str_state, str_Country, str_postcode);
            }
        }

    }

    private void devliverResultToRecevier(String address, String locality, String district, String state, String country, String postcode) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ADDRESS, address);
        bundle.putString(Constants.LOCAITY, locality);
        bundle.putString(Constants.DISTRICT, district);
        bundle.putString(Constants.STATE, state);
        bundle.putString(Constants.COUNTRY, country);
        bundle.putString(Constants.POST_CODE, postcode);
        resultReceiver.send(Constants.SUCCESS_RESULT, bundle);
    }

}

/* List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            } catch (Exception exception) {
                errormessgae = exception.getMessage();
            }
            ArrayList<String> addressFragments = null;
            if (addresses == null || addresses.isEmpty()) {
                devliverResultToRecevier(Constants.FAILURE_RESULT, errormessgae);
            } else {
                Address address = addresses.get(0);

                addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                    addressFragments.add(address.getAddressLine(i));
            }
            devliverResultToRecevier(Constants.SUCCESS_RESULT, TextUtils.join(
                    Objects.requireNonNull(System.getProperty("line.separator")), addressFragments));*/
