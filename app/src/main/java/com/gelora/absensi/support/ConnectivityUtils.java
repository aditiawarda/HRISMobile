package com.gelora.absensi.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class ConnectivityUtils {
    private final MutableLiveData<Boolean> _isConnected = new MutableLiveData<>();
    private final LiveData<Boolean> isConnected = _isConnected;

    private ConnectivityManager connectivityManager;
    private Context applicationContext;

    public ConnectivityUtils(Context context) {
        applicationContext = context.getApplicationContext();
        connectivityManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Register network callbacks or broadcast receiver
        registerNetworkCallbacks();

        // Check initial connection status
        updateConnectionStatus();
    }

    private void registerNetworkCallbacks() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    _isConnected.postValue(true);
                }

                @Override
                public void onLost(Network network) {
                    _isConnected.postValue(false);
                }
            });
        } else {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateConnectionStatus();
                }
            };

            applicationContext.registerReceiver(receiver, filter);
        }
    }

    private void updateConnectionStatus() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        _isConnected.postValue(isConnected);
    }

    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }

//    public void unregisterReceiver() {
//        if (applicationContext != null) {
//            applicationContext.unregisterReceiver(connectivityReceiver);
//        }
//    }
}
