package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wdgfarm_android.model.Weighing;

public class WeighingWorkViewModel extends ViewModel {
    public MutableLiveData<Weighing> weighing = new MutableLiveData<>();
    public Weighing weighingdata = new Weighing();

    public WeighingWorkViewModel(){

        weighingdata.setTotalWeight(0);
        weighingdata.setBoxWeight(0);
        weighingdata.setBoxAccount(0);
        weighingdata.setPaletteWeight(0);
        weighingdata.setDeductibleWeight(0);
        weighingdata.setRealWeight(0);

        weighing.setValue(weighing.getValue());
    }
}
