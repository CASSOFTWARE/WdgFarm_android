package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wdgfarm_android.utils.TcpThread;

public class ScaleViewModel extends ViewModel {
    public MutableLiveData<String> weight = new MutableLiveData<>();
    public MutableLiveData<Boolean> scaleState = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    public MutableLiveData<String> scaleType = new MutableLiveData<>();

    public ScaleViewModel(){

    }

}
