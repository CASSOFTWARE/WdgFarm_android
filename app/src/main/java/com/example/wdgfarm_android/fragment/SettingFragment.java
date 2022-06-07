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
import com.example.wdgfarm_android.viewmodel.ScaleViewModel;


public class SettingFragment extends Fragment {

    SharedPreferencesManager sharedPreferencesManager;

    private static Button mSettingSaveButton;
    private static EditText mAScaleIpValue, mBScaleIpValue;
    private static EditText mAScalePortValue, mBScalePortValue;
    private static EditText mAScaleNameValue, mBScaleNameValue;
    private static EditText mAuthKeyValue;

    public static ScaleViewModel scaleViewModel;

    public SettingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        View view = binding.getRoot();

        scaleViewModel = new ViewModelProvider(getActivity()).get(ScaleViewModel.class);


        mSettingSaveButton = view.findViewById(R.id.setting_save_btn);
        mAScaleNameValue = view.findViewById(R.id.a_scale_name_value);
        mAScaleIpValue = view.findViewById(R.id.a_scale_ip_value);
        mAScalePortValue = view.findViewById(R.id.a_scale_port_value);

        mBScaleNameValue = view.findViewById(R.id.b_scale_name_value);
        mBScaleIpValue = view.findViewById(R.id.b_scale_ip_value);
        mBScalePortValue = view.findViewById(R.id.b_scale_port_value);

        mAuthKeyValue = view.findViewById(R.id.auth_key_value);

        mAScaleNameValue.setText((SharedPreferencesManager.getString(getContext(), "A_SCALE_NAME")));
        mAScaleIpValue.setText(SharedPreferencesManager.getString(getContext(), "A_SCALE_IP"));
        mAScalePortValue.setText(SharedPreferencesManager.getString(getContext(), "A_SCALE_PORT"));

        mBScaleNameValue.setText((SharedPreferencesManager.getString(getContext(), "B_SCALE_NAME")));
        mBScaleIpValue.setText(SharedPreferencesManager.getString(getContext(), "B_SCALE_IP"));
        mBScalePortValue.setText(SharedPreferencesManager.getString(getContext(), "B_SCALE_PORT"));

        mAuthKeyValue.setText(SharedPreferencesManager.getString(getContext(), "AUTH_KEY"));


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
        SharedPreferencesManager.setString(getContext(), PreferencesKey.A_SCALE_NAME.name(),mAScaleNameValue.getText().toString());
        SharedPreferencesManager.setString(getContext(), PreferencesKey.A_SCALE_IP.name(),mAScaleIpValue.getText().toString());
        SharedPreferencesManager.setString(getContext(),PreferencesKey.A_SCALE_PORT.name(),mAScalePortValue.getText().toString());

        SharedPreferencesManager.setString(getContext(), PreferencesKey.B_SCALE_NAME.name(),mBScaleNameValue.getText().toString());
        SharedPreferencesManager.setString(getContext(),PreferencesKey.B_SCALE_IP.name(),mBScaleIpValue.getText().toString());
        SharedPreferencesManager.setString(getContext(),PreferencesKey.B_SCALE_PORT.name(),mBScalePortValue.getText().toString());

        SharedPreferencesManager.setString(getContext(),PreferencesKey.AUTH_KEY.name(),mAuthKeyValue.getText().toString());

        scaleViewModel.scaleAName.setValue(mAScaleNameValue.getText().toString());
        scaleViewModel.scaleBName.setValue(mBScaleNameValue.getText().toString());
        Toast.makeText(getContext(), R.string.save, Toast.LENGTH_SHORT).show();
    }
}
