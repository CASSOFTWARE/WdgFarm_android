package com.example.wdgfarm_android.utils;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    public static final String EXTRA_DATE = "com.example.wdgfarm_android.EXTRA_DATE";

    private DatePicker datePicker;
    private TimePicker timePicker;

    public DatetimePickerFragment() {
    }

    public static DatetimePickerFragment newInstance() {
        Bundle args = new Bundle();
        DatetimePickerFragment fragment = new DatetimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_datetime_picker, null);

        datePicker = v.findViewById(R.id.date_picker);
        timePicker = v.findViewById(R.id.time_picker);

        return new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = datePicker.getYear();
                                int month = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                int hour = timePicker.getHour();
                                int min = timePicker.getMinute();
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, min);
                                Date date = calendar.getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

/*    @Override
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
    }*/

}