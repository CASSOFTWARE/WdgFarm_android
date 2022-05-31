package com.example.wdgfarm_android.utils;

import com.example.wdgfarm_android.viewmodel.WeighingWorkViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class CurrentTime extends Thread{
    String date;
    Date currentDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd a hh:mm");
    WeighingWorkViewModel weighingWorkViewModel;
    public void CurrentTime(WeighingWorkViewModel weighingWorkViewModel){
        this.weighingWorkViewModel = weighingWorkViewModel;
    }

    public void run(){
        while (!isInterrupted()){
            long now = System.currentTimeMillis();
            currentDate = new Date(now);
            date = format.format(currentDate);
            weighingWorkViewModel.date.postValue(date);
        }
    }
}
