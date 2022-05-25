package com.example.wdgfarm_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.ActivityDetailBinding;
import com.example.wdgfarm_android.model.Weighing;

public class DetailActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();

        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        binding.detailBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.detailCompanyText.setText(intent.getExtras().getString(EXTRA_COMPANY_NAME));
        binding.detailProductText.setText(intent.getExtras().getString(EXTRA_PRODUCT_NAME));
        binding.detailPriceText.setText(String.valueOf(intent.getExtras().getInt(EXTRA_PRODUCT_PRPICE, 1000)));
        binding.detailDateText.setText(intent.getExtras().getString(EXTRA_DATE));

        binding.totalWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_TOTAL_WEIGHT, 0)));

        binding.boxWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_BOX_WEIGHT, 0)));
        binding.boxAccountValue.setText(String.valueOf(intent.getExtras().getInt(EXTRA_BOX_ACCOUNT, 0)));
        binding.boxSize.setText(intent.getExtras().getString(EXTRA_BOX_NAME));

        binding.paletteWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_PALETTE_WEIGHT, 0)));
        binding.deductibleWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_DEDUCTIBLE_WEIGHT, 0)));

        binding.realWeightValue.setText(String.valueOf(intent.getExtras().getFloat(EXTRA_REAL_WEIGHT, 0))+" kg");
        binding.erpTimeValue.setText(intent.getExtras().getString(EXTRA_ERP_DATE));

    }
}