package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApiViewModel extends ViewModel {
    public MutableLiveData<String> zone = new MutableLiveData<>();
    public MutableLiveData<String> sessionID = new MutableLiveData<>();

    public ApiViewModel(){
//        zone.setValue("CD");
//        sessionID.setValue("");
    }

}
