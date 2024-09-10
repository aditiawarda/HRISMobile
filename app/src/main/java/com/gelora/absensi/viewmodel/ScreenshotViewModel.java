package com.gelora.absensi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScreenshotViewModel extends ViewModel {

    private MutableLiveData<Boolean> isDialogShowing = new MutableLiveData<>();

    public LiveData<Boolean> getIsDialogShowing() {
        return isDialogShowing;
    }

    public void setDialogShowing(boolean showing) {
        isDialogShowing.setValue(showing);
    }
}

