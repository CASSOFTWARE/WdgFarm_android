package com.example.wdgfarm_android.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.activity.InfoAddActivity;
import com.example.wdgfarm_android.activity.SelectActivity;
import com.example.wdgfarm_android.databinding.FragmentWorkBinding;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.utils.DatetimePickerFragment;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;
import com.example.wdgfarm_android.viewmodel.WeighingWorkViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkFragment extends Fragment {
    public static final int INFO_COMPANY = 100;
    public static final int INFO_PRODUCT = 200;

    private static final int REQUEST_DATE = 1;
    private static final String INIT_COMPANY = "업체를 선택하세요.";
    private static final String INIT_PRODUCT = "상품을 선택하세요.";
    private static final String INIT_DATE = "입고일";

    private WeighingViewModel weighingViewModel;
    public static Weighing weighing;
    public static WeighingWorkViewModel weighingWorkViewModel;
    public static FragmentWorkBinding binding;
    public WorkFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        weighing = new Weighing();
        weighing.setCompanyName(INIT_COMPANY);
        weighing.setProductName(INIT_PRODUCT);
        weighing.setDate(Calendar.getInstance().getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd a hh:mm");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false);
        View view = binding.getRoot();
        weighingViewModel = new ViewModelProvider(getActivity()).get(WeighingViewModel.class);
        weighingWorkViewModel = new ViewModelProvider(getActivity()).get(WeighingWorkViewModel.class);
        binding.workCompanyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), SelectActivity.class);
                intent.putExtra(SelectActivity.EXTRA_INFO, INFO_COMPANY);
                startActivityForResult(intent, INFO_COMPANY);
            }
        });

        binding.workProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), SelectActivity.class);
                intent.putExtra(SelectActivity.EXTRA_INFO, INFO_PRODUCT);
                startActivityForResult(intent, INFO_PRODUCT);
            }
        });

        weighingWorkViewModel.weighing.setValue(weighing);

        weighingWorkViewModel.weighing.observe(getActivity(), new Observer<Weighing>() {
            @Override
            public void onChanged(Weighing weighing) {
                binding.workCompanyBtn.setText(weighing.getCompanyName());
                binding.workProductBtn.setText(weighing.getProductName());
                binding.workPriceEdit.setText(String.valueOf(weighing.getProductPrice()));
                binding.workDateBtn.setText(format.format(weighing.getDate()));
            }
        });

        binding.workDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatetimePickerFragment dialog = new DatetimePickerFragment();

                dialog.show(getChildFragmentManager(), "DataTimePicker Dialog");
            }
        });


        CheckBox checkBox = (CheckBox) view.findViewById(R.id.work_datetime_checkbox) ;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBox.isChecked()){
                    binding.workDateBtn.setEnabled(false);
                }
                else{
                    binding.workDateBtn.setEnabled(true);
                }
            }
        });

        binding.workSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(System.currentTimeMillis());

                if(checkBox.isChecked()){
                    weighing.setDate(date);
                    weighingWorkViewModel.weighing.setValue(weighing);
                    binding.workDateBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else{
                    try {
                        weighing.setDate(format.parse(binding.workDateBtn.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!binding.workProductBtn.getText().toString().contains(INIT_PRODUCT) && !binding.workSaveBtn.getText().toString().contains(INIT_COMPANY) && !binding.workDateBtn.getText().toString().contains(INIT_DATE)) {
                    weighing.setTotalWeight(Float.parseFloat(binding.totalWeightValue.getText().toString().replace(" kg","")));
                    weighing.setBoxWeight(Float.parseFloat(binding.boxWeightValue.getText().toString().replace(" kg","")));
                    weighing.setBoxAccount(Integer.parseInt(binding.boxAccountValue.getText().toString()));
                    weighing.setPaletteWeight(Float.parseFloat(binding.paletteWeightValue.getText().toString().replace(" kg","")));
                    weighing.setDeductibleWeight(Float.parseFloat(binding.deductibleWeightValue.getText().toString().replace(" kg","")));
                    weighing.setRealWeight(Float.parseFloat(binding.realWeightValue.getText().toString().replace(" kg","")));

                    weighingWorkViewModel.weighing.setValue(weighing);

                    weighingViewModel.insert(weighing);

                    Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "빈칸빈칸", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            int info = data.getIntExtra(SelectActivity.EXTRA_INFO, 0);
            switch (info){
                //업체
                case 100:
                    weighing.setCompanyID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    weighing.setCompanyCode(data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0));
                    weighing.setCompanyName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));

                    binding.workCompanyBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;

                //상품
                case 200:
                    weighing.setProductID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    weighing.setProductCode(data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0));
                    weighing.setProductName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    weighing.setProductPrice(data.getIntExtra(InfoAddActivity.EXTRA_VALUE, 1000));

                    binding.workProductBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;
            }
            weighingWorkViewModel.weighing.setValue(weighing);

        }

    }
}
