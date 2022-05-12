package com.example.wdgfarm_android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.FragmentSettingBinding;
import com.example.wdgfarm_android.databinding.FragmentWorkBinding;

public class WorkFragment extends Fragment {



    public WorkFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_work, container, false);
        FragmentWorkBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false);
        View view = binding.getRoot();



        return view;
    }
}
