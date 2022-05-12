package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wdgfarm_android.R;

public class InfoViewModel extends ViewModel {
    public MutableLiveData<String> info = new MutableLiveData<>();

    public InfoViewModel(){
        info.setValue("");
    }
}
