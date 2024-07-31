package com.gelora.absensi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gelora.absensi.support.ConnectivityUtils;

public class ConnectivityViewModel extends AndroidViewModel {
    private final ConnectivityUtils connectivityUtils;

    public ConnectivityViewModel(@NonNull Application application) {
        super(application);
        connectivityUtils = new ConnectivityUtils(application);
    }

    public LiveData<Boolean> getIsConnected() {
        return connectivityUtils.getIsConnected();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}