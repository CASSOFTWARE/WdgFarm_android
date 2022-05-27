package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScaleViewModel extends ViewModel {
    public MutableLiveData<String> weight = new MutableLiveData<>();
    public MutableLiveData<Boolean> scaleState = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    public MutableLiveData<String> scaleType = new MutableLiveData<>();

    public ScaleViewModel(){
//        zone.setValue("CD");
//        sessionID.setValue("");
    }

}
