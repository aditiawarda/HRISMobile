package com.gelora.absensi.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ButtonViewModel extends ViewModel {

    private final MutableLiveData<Boolean> selectedLeftButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> selectedRightButton = new MutableLiveData<>(false);

    public LiveData<Boolean> getSelectedLeftButton() {
        return selectedLeftButton;
    }

    public LiveData<Boolean> getSelectedRightButton() {
        return selectedRightButton;
    }

    public void selectLeftButton() {
        selectedLeftButton.setValue(true);
        selectedRightButton.setValue(false);
    }

    public void selectRightButton() {
        selectedLeftButton.setValue(false);
        selectedRightButton.setValue(true);
    }
}

