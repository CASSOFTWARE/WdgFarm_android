package com.example.wdgfarm_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.adapter.CompanyAdapter;
import com.example.wdgfarm_android.adapter.CompanySelectAdapter;
import com.example.wdgfarm_android.adapter.ProductAdapter;
import com.example.wdgfarm_android.adapter.ProductSelectAdapter;
import com.example.wdgfarm_android.databinding.ActivitySelectBinding;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.viewmodel.CompanyViewModel;
import com.example.wdgfarm_android.viewmodel.ProductViewModel;

import java.util.List;

public class SelectActivity extends AppCompatActivity {
    public static final String EXTRA_INFO = "com.example.wdgfarm_android.EXTRA_INFO";

    private ProductViewModel productViewModel;
    private CompanyViewModel companyViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        ActivitySelectBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_select);

        int info = intent.getExtras().getInt(EXTRA_INFO);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        CompanySelectAdapter companySelectAdapter = new CompanySelectAdapter();
        ProductSelectAdapter productSelectAdapter = new ProductSelectAdapter();

        binding.selectBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        switch (info){
            case 100:
                binding.selectTitle.setText(R.string.company_select);
                binding.recyclerView.setAdapter(companySelectAdapter);
                break;

            case 200:
                binding.selectTitle.setText(R.string.product_select);
                binding.recyclerView.setAdapter(productSelectAdapter);
                break;
        }


        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        companyViewModel.getAllCompanys().observe(this, new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> company) {
                companySelectAdapter.setCompanys(company);
            }
        });

        companySelectAdapter.setOnItemClickListener(new CompanySelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Company company) {
                Intent data = new Intent();
                data.putExtra(InfoAddActivity.EXTRA_ID, company.getId());
                data.putExtra(InfoAddActivity.EXTRA_CODE, company.getCode());
                data.putExtra(InfoAddActivity.EXTRA_NAME, company.getName());
                data.putExtra(EXTRA_INFO, info);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productSelectAdapter.setProducts(products);
            }
        });

        productSelectAdapter.setOnItemClickListener(new ProductSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent data = new Intent();
                data.putExtra(InfoAddActivity.EXTRA_ID, product.getId());
                data.putExtra(InfoAddActivity.EXTRA_CODE, product.getCode());
                data.putExtra(InfoAddActivity.EXTRA_NAME, product.getName());
                data.putExtra(InfoAddActivity.EXTRA_VALUE, product.getPrice());
                data.putExtra(EXTRA_INFO, info);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
}