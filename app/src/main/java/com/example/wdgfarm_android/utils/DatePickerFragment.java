package com.example.wdgfarm_android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wdgfarm_android.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String ARG_DATE = "com.example.wdgfarm_android.ARG_DATE";
    private static final String ARG_FROMDATE = "fromdate";

    public static final String EXTRA_DATE = "com.example.wdgfarm_android.EXTRA_DATE";

    private DatePicker datePicker;

    public DatePickerFragment() {
    }

    public static DatePickerFragment newInstanceFrom(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DatePickerFragment newInstanceTo(Date date, Date fromdate) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putSerializable(ARG_FROMDATE, fromdate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Date fromdate = (Date) getArguments().getSerializable(ARG_FROMDATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_date_picker, null);

        datePicker = v.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, null);

        //fromdate 날짜부터 지정 가능
        if(fromdate != null){
            datePicker.setMinDate(fromdate.getTime());
        }

        return new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = datePicker.getYear();
                                int month = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();

                                Date date = new GregorianCalendar(year, month, day).getTime();
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