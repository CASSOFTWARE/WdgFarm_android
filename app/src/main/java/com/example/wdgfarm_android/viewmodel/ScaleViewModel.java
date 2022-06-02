package com.example.wdgfarm_android.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wdgfarm_android.utils.PreferencesKey;
import com.example.wdgfarm_android.utils.SharedPreferencesManager;
import com.example.wdgfarm_android.utils.TcpThread;

public class ScaleViewModel extends AndroidViewModel {
    public MutableLiveData<String> weight = new MutableLiveData<>();
    public MutableLiveData<Boolean> scaleState = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    public MutableLiveData<String> scaleType = new MutableLiveData<>();

    public MutableLiveData<String> scaleAName = new MutableLiveData<>();
    public MutableLiveData<String> scaleBName = new MutableLiveData<>();

    public ScaleViewModel(Application application){
        super(application);
        //scaleType.setValue(SharedPreferencesManager.getString(getApplication(), PreferencesKey.CONNECTED_SCALE.name()));
        isConnected.setValue(false);
    }
}
