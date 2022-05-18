package com.example.wdgfarm_android.utils;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.FragmentDatetimePickerBinding;
import com.example.wdgfarm_android.fragment.WorkFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class DatetimePickerFragment extends DialogFragment {

    public static final String ARG_DATE = "com.example.wdgfarm_android.ARG_DATE";
    public static final String EXTRA_DATE = "com.example.wdgfarm_android.EXTRA_DATE";


    public DatetimePickerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentDatetimePickerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_datetime_picker, container, false);
        View view = binding.getRoot();


        binding.datetimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Date date = new GregorianCalendar(binding.datePicker.getYear(), binding.datePicker.getMonth(), binding.datePicker.getDayOfMonth()).getTime();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ARG_DATE, date);
//                getParentFragmentManager().setFragmentResult("requestKey", bundle);
//                getChildFragmentManager().popBackStack();
                //getParentFragmentManager().popBackStack();

            }
        });
        return view;
    }



}