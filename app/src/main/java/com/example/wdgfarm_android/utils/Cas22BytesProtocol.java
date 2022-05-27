package com.example.wdgfarm_android.utils;

import android.util.Log;

import com.example.wdgfarm_android.viewmodel.ScaleViewModel;

public class Cas22BytesProtocol {
    public void recvPacket(String data, ScaleViewModel scaleViewModel) {
        if(data.startsWith("ST") || data.startsWith("US")){
            if(data.length() == 22) {
                Log.d("TAG","데이터 : " + data);

                if (data.startsWith("ST")) {
                    scaleViewModel.scaleState.postValue(true);
                } else if (data.startsWith("US")) {
                    scaleViewModel.scaleState.postValue(false);
                }
                String weight = data.substring(9);
                weight = weight.replaceAll("\\s+","");
                weight = weight.replace("kg","");

                scaleViewModel.weight.postValue(weight);
            }
        }
    }
}
