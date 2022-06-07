package com.example.wdgfarm_android.activity;

import static com.example.wdgfarm_android.fragment.WorkFragment.binding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.ActivityDetailBinding;
import com.example.wdgfarm_android.viewmodel.WeighingViewModel;

public class DetailActivity extends AppCompatActivity {
    public static final int INFO_COMPANY = 100;

    public static final String EXTRA_ID = "com.example.wdgfarm_android.EXTRA_WEIGHING";

    public static final String EXTRA_COMPANY_ID = "com.example.wdgfarm_android.EXTRA_COMPANY_ID";
    public static final String EXTRA_COMPANY_CODE = "com.example.wdgfarm_android.EXTRA_COMPANY_CODE";
    public static final String EXTRA_COMPANY_NAME = "com.example.wdgfarm_android.EXTRA_COMPANY_NAME";

    public static final String EXTRA_PRODUCT_ID = "com.example.wdgfarm_android.EXTRA_PRODUCT_ID";
    public static final String EXTRA_PRODUCT_CODE = "com.example.wdgfarm_android.EXTRA_PRODUCT_CODE";
    public static final String EXTRA_PRODUCT_NAME = "com.example.wdgfarm_android.EXTRA_PRODUCT_NAME";
    public static final String EXTRA_PRODUCT_PRPICE = "com.example.wdgfarm_android.EXTRA_PRODUCT_PRPICE";

    public static final String EXTRA_DATE = "com.example.wdgfarm_android.EXTRA_DATE";
    public static final String EXTRA_TOTAL_WEIGHT = "com.example.wdgfarm_android.EXTRA_TOTAL_WEIGHT";

    public static final String EXTRA_BOX_ID = "com.example.wdgfarm_android.EXTRA_BOX_ID";
    public static final String EXTRA_BOX_NAME = "com.example.wdgfarm_android.EXTRA_BOX_NAME";
    public static final String EXTRA_BOX_WEIGHT = "com.example.wdgfarm_android.EXTRA_BOX_WEIGHT";

    public static final String EXTRA_BOX_ACCOUNT = "com.example.wdgfarm_android.EXTRA_BOX_ADCCOUNT";
    public static final String EXTRA_PALETTE_WEIGHT = "com.example.wdgfarm_android.EXTRA_PALETTE_WEIGHT";
    public static final String EXTRA_DEDUCTIBLE_WEIGHT = "com.example.wdgfarm_android.EXTRA_DEDUCTIBLE_WEIGHT";
    public static final String EXTRA_REAL_WEIGHT = "com.example.wdgfarm_android.EXTRA_REAL_WEIGHT";
    public static final String EXTRA_ERP_DATE = "com.example.wdgfarm_android.EXTRA_ERP_DATE";

    private WeighingViewModel weighingViewModel;

    int weighingId, companyId;
    String companyCode, companyName;
    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        weighingViewModel = new ViewModelProvider(this).get(WeighingViewModel.class);

        Intent intent = getIntent();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        binding.detailBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (!intent.getExtras().getString(EXTRA_ERP_DATE).contains("전송 실패")) {
            binding.detailCompanyButton.setEnabled(false);
        } else {
            binding.detailUpdateBtn.setVisibility(View.VISIBLE);
            binding.detailDeleteBtn.setVisibility(View.VISIBLE);
        }

        binding.detailCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                intent.putExtra(SelectActivity.EXTRA_DETAIL, true);
                intent.putExtra(SelectActivity.EXTRA_INFO, INFO_COMPANY);
                startActivityForResult(intent, INFO_COMPANY);
            }
        });

        weighingId = intent.getExtras().getInt(EXTRA_ID, 0);
        companyId = intent.getIntExtra(InfoAddActivity.EXTRA_ID, 0);
        companyCode = intent.getStringExtra(InfoAddActivity.EXTRA_CODE);
        companyName = intent.getStringExtra(InfoAddActivity.EXTRA_NAME);

        //수정하기
        binding.detailUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent data = new Intent();
//                data.putExtra(EXTRA_ID, weighingId);
//                data.putExtra(InfoAddActivity.EXTRA_ID, companyId);
//                data.putExtra(InfoAddActivity.EXTRA_CODE, companyCode);
//                data.putExtra(InfoAddActivity.EXTRA_NAME, companyName);
//                data.putExtra(EXTRA_ERP_DATE, intent.getExtras().getString(EXTRA_ERP_DATE));
//                setResult(Activity.RESULT_OK, data);
                companyName = binding.detailCompanyButton.getText().toString();
                weighingViewModel.updateNotSendWeighings(companyId, companyCode, companyName, weighingId);
                finish();
            }
        });

        //삭제하기
        binding.detailDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(DetailActivity.this);
                dlg.setTitle("미전송 데이터 삭제");
                dlg.setMessage("미전송 데이터를 삭제하시겠습니까?");
                dlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        weighingViewModel.deleteWeighing(weighingId);
                        finish();
                    }
                });
                dlg.show();

            }
        });

        binding.detailCompanyButton.setText(intent.getExtras().getString(EXTRA_COMPANY_NAME));
        binding.detailProductText.setText(intent.getExtras().getString(EXTRA_PRODUCT_NAME));
        binding.detailPriceText.setText(String.valueOf(intent.getExtras().getInt(EXTRA_PRODUCT_PRPICE, 1000)));
        binding.detailDateText.setText(intent.getExtras().getString(EXTRA_DATE));

        binding.totalWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_TOTAL_WEIGHT, 0)));

        binding.boxWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_BOX_WEIGHT, 0)));
        binding.boxAccountValue.setText(String.valueOf(intent.getExtras().getInt(EXTRA_BOX_ACCOUNT, 0)));
        binding.boxSize.setText(intent.getExtras().getString(EXTRA_BOX_NAME));

        binding.paletteWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_PALETTE_WEIGHT, 0)));
        binding.deductibleWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_DEDUCTIBLE_WEIGHT, 0)));

        binding.realWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_REAL_WEIGHT, 0)));
        binding.erpTimeValue.setText(intent.getExtras().getString(EXTRA_ERP_DATE));


//        weighing.setId(intent.getExtras().getInt(EXTRA_ID, 0));

//        weighing.setCompanyID(intent.getExtras().getInt(EXTRA_COMPANY_ID, 0));
//        weighing.setCompanyCode(intent.getExtras().getString(EXTRA_COMPANY_CODE));
//        weighing.setCompanyName(intent.getExtras().getString(EXTRA_COMPANY_NAME));
//
//        weighing.setProductID(intent.getExtras().getInt(EXTRA_PRODUCT_ID, 0));
//        weighing.setProductCode(intent.getExtras().getString(EXTRA_PRODUCT_CODE));
//        weighing.setProductName(intent.getExtras().getString(EXTRA_PRODUCT_NAME));
//        weighing.setProductPrice(intent.getExtras().getInt(EXTRA_PRODUCT_PRPICE, 0));
//
//        weighing.setDate(intent.getExtras().getString(EXTRA_DATE));
//        weighing.setTotalWeight(intent.getExtras().getInt(EXTRA_TOTAL_WEIGHT, 0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.getIntExtra(SelectActivity.EXTRA_INFO, 0) == 100) {
                companyId = data.getIntExtra(InfoAddActivity.EXTRA_ID, 0);
                companyCode = data.getStringExtra(InfoAddActivity.EXTRA_CODE);
                companyName = data.getStringExtra(InfoAddActivity.EXTRA_NAME);

                binding.detailCompanyButton.setText(companyName);
            }
        }
    }
}