package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wdgfarm_android.model.Weighing;

public class WeighingWorkViewModel extends ViewModel {
    public MutableLiveData<Weighing> weighing = new MutableLiveData<>();

    public WeighingWorkViewModel(){
        weighing.setValue(weighing.getValue());
    }
}
