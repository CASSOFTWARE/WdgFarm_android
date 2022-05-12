package com.example.wdgfarm_android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.FragmentSettingBinding;
import com.example.wdgfarm_android.utils.PreferencesKey;
import com.example.wdgfarm_android.utils.SharedPreferencesManager;


public class SettingFragment extends Fragment {

    SharedPreferencesManager sharedPreferencesManager;

    private Button mSettingSaveButton;
    private EditText mAScaleIpValue, mBScaleIpValue;
    private EditText mAScalePortValue, mBScalePortValue;

    public SettingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        View view = binding.getRoot();

        mSettingSaveButton = view.findViewById(R.id.setting_save_btn);
        mAScaleIpValue = view.findViewById(R.id.a_scale_ip_value);
        mAScalePortValue = view.findViewById(R.id.a_scale_port_value);
        mBScaleIpValue = view.findViewById(R.id.b_scale_ip_value);
        mBScalePortValue = view.findViewById(R.id.b_scale_port_value);

        mAScaleIpValue.setText(SharedPreferencesManager.getString(getContext(), "A_SCALE_IP"));
        mAScalePortValue.setText(SharedPreferencesManager.getString(getContext(), "A_SCALE_PORT"));
        mBScaleIpValue.setText(SharedPreferencesManager.getString(getContext(), "B_SCALE_IP"));
        mBScalePortValue.setText(SharedPreferencesManager.getString(getContext(), "B_SCALE_PORT"));


        mSettingSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                settingScale();
            }
        });

        return view;
    }

    //Preference에 저장
    private void settingScale(){
        SharedPreferencesManager.setString(getContext(), PreferencesKey.A_SCALE_IP.name(),mAScaleIpValue.getText().toString());
        SharedPreferencesManager.setString(getContext(),PreferencesKey.A_SCALE_PORT.name(),mAScalePortValue.getText().toString());
        SharedPreferencesManager.setString(getContext(),PreferencesKey.B_SCALE_IP.name(),mBScaleIpValue.getText().toString());
        SharedPreferencesManager.setString(getContext(),PreferencesKey.B_SCALE_PORT.name(),mBScalePortValue.getText().toString());
        Toast.makeText(getContext(), R.string.save, Toast.LENGTH_SHORT).show();
    }
}
