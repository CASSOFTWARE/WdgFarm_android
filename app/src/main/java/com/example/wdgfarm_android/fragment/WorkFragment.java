package com.example.wdgfarm_android.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.activity.InfoAddActivity;
import com.example.wdgfarm_android.activity.SelectActivity;
import com.example.wdgfarm_android.api.ApiListener;
import com.example.wdgfarm_android.api.PurchaseApi;
import com.example.wdgfarm_android.databinding.FragmentWorkBinding;
import com.example.wdgfarm_android.model.Weighing;
import com.example.wdgfarm_android.utils.DatetimePickerFragment;
import com.example.wdgfarm_android.viewmodel.ApiViewModel;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;
import com.example.wdgfarm_android.viewmodel.WeighingWorkViewModel;

import org.json.JSONException;

import java.text.Format;
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
    private static final String DIALOG_DATE = "DataTimePicker Dialog";

    private WeighingViewModel weighingViewModel;
    private SimpleDateFormat format;
    public static Weighing weighing;
    public static WeighingWorkViewModel weighingWorkViewModel;
    public static ApiViewModel apiViewModel;
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

        format = new SimpleDateFormat("yyyy/MM/dd a hh:mm");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false);
        View view = binding.getRoot();
        apiViewModel = new ViewModelProvider(getActivity()).get(ApiViewModel.class);
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
                FragmentManager manager = getFragmentManager();
                DatetimePickerFragment dialog = DatetimePickerFragment.newInstance();
                dialog.setTargetFragment(WorkFragment.this, REQUEST_DATE);

                dialog.show(manager, "DataTimePicker Dialog");
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
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

                    new PurchaseApi(apiViewModel.zone.getValue(), apiViewModel.sessionID.getValue(), format.format(weighing.getDate()), weighing.getCompanyName(), weighing.getProductName(), weighing.getProductPrice(), new ApiListener() {
                        @Override
                        public void success(String response) throws JSONException {
                            Log.d("TAG", "구매 입력 성공");

                            weighingViewModel.insert(weighing);
                            Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void fail() {
                            Log.e("TAG", "구매 입력 실패");
                            Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();

                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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
                    if(data.hasExtra(InfoAddActivity.EXTRA_ID)) {
                        weighing.setCompanyID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                        weighing.setCompanyCode(data.getStringExtra(InfoAddActivity.EXTRA_CODE));
                        weighing.setCompanyName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    }else{
                        weighing.setCompanyName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    }
                    binding.workCompanyBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;

                //상품
                case 200:
                    weighing.setProductID(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    weighing.setProductCode(data.getStringExtra(InfoAddActivity.EXTRA_CODE));
                    weighing.setProductName(data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    weighing.setProductPrice(data.getIntExtra(InfoAddActivity.EXTRA_VALUE, 1000));

                    binding.workProductBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                    break;

                default:
                    break;
            }
            if(requestCode == REQUEST_DATE) {
                Date date = (Date) data.getSerializableExtra(DatetimePickerFragment.EXTRA_DATE);
                weighing.setDate(date);
                binding.workDateBtn.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            weighingWorkViewModel.weighing.setValue(weighing);

        }

    }
}
