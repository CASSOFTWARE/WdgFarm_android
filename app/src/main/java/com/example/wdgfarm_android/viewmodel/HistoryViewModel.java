package com.example.wdgfarm_android.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

public class HistoryViewModel extends ViewModel {
    public MutableLiveData<Date> dateFrom = new MutableLiveData<>();
    public MutableLiveData<Date> dateTo = new MutableLiveData<>();

    private Calendar calendarFrom = Calendar.getInstance();
    private Calendar calendarTo = Calendar.getInstance();

    public HistoryViewModel(){
        calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
        calendarFrom.set(Calendar.MINUTE, 0);
        calendarFrom.set(Calendar.SECOND, 0);
        calendarTo.set(Calendar.HOUR_OF_DAY, 23);
        calendarTo.set(Calendar.MINUTE, 59);
        calendarTo.set(Calendar.SECOND, 59);
        dateFrom.setValue(calendarFrom.getTime());
        dateTo.setValue(calendarTo.getTime());
    }
}
