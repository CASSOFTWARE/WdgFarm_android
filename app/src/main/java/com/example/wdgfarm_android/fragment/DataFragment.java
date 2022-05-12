package com.example.wdgfarm_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.activity.InfoActivity;
import com.example.wdgfarm_android.activity.MainActivity;
import com.example.wdgfarm_android.databinding.FragmentDataBinding;

public class DataFragment extends Fragment {

    public DataFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data, container, false);
        View view = binding.getRoot();

        binding.productInfoBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        startInfoActivity(getResources().getString(R.string.product_info));
                    }
                }
        );

        binding.companyInfoBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        startInfoActivity(getResources().getString(R.string.company_info));
                    }
                }
        );

        binding.boxInfoBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        startInfoActivity(getResources().getString(R.string.box_info));
                    }
                }
        );

        return view;
    }

    public void startInfoActivity(String info) {
        Intent intent = new Intent(getActivity().getApplication(), InfoActivity.class);
        intent.putExtra("info", info);
        startActivity(intent);
    }
}
