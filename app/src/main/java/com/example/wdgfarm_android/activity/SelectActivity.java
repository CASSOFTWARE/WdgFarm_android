package com.example.wdgfarm_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

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
    private ProductSelectAdapter productSelectAdapter;
    private CompanySelectAdapter companySelectAdapter;

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

        companySelectAdapter = new CompanySelectAdapter();
        productSelectAdapter = new ProductSelectAdapter();

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


        EditText editText = (EditText)findViewById(R.id.select_edit) ;

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchDatabase(editable.toString(), info);
            }
        });
    }

    private void searchDatabase(String query, int info){
        String searchQuery = "%" + query + "%";
        switch (info){
            case 100:
                companyViewModel.getFiltterCompanys(searchQuery).observe(this, new Observer<List<Company>>() {
                    @Override
                    public void onChanged(List<Company> companies) {
                        companySelectAdapter.setCompanys(companies);
                    }
                });
                break;

            case 200:
                productViewModel.getFiltterProducts(searchQuery).observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        productSelectAdapter.setProducts(products);
                    }
                });
                break;
        }

    }

    //화면 터치시 키보드 감추기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    //키보드 감추기
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}