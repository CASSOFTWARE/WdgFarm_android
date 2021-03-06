package com.example.wdgfarm_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.wdgfarm_android.adapter.BoxSelectAdapter;
import com.example.wdgfarm_android.adapter.CompanyAdapter;
import com.example.wdgfarm_android.adapter.CompanySelectAdapter;
import com.example.wdgfarm_android.adapter.ProductAdapter;
import com.example.wdgfarm_android.adapter.ProductSelectAdapter;
import com.example.wdgfarm_android.databinding.ActivitySelectBinding;
import com.example.wdgfarm_android.model.Box;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.viewmodel.BoxViewModel;
import com.example.wdgfarm_android.viewmodel.CompanyViewModel;
import com.example.wdgfarm_android.viewmodel.ProductViewModel;

import java.util.List;

public class SelectActivity extends AppCompatActivity {
    public static final String EXTRA_INFO = "com.example.wdgfarm_android.EXTRA_INFO";
    public static final String EXTRA_DETAIL = "com.example.wdgfarm_android.EXTRA_DETAIL";

    private static ProductViewModel productViewModel;
    private static CompanyViewModel companyViewModel;
    private static BoxViewModel boxViewModel;
    private static ProductSelectAdapter productSelectAdapter;
    private static CompanySelectAdapter companySelectAdapter;
    private static BoxSelectAdapter boxSelectAdapter;

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
        boxSelectAdapter = new BoxSelectAdapter();

        binding.selectBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editText = new EditText(getApplicationContext());
                editText.setSingleLine(true);
                AlertDialog.Builder dlg = new AlertDialog.Builder(SelectActivity.this);
                dlg.setTitle("????????? ?????????");
                dlg.setMessage("???????????? ?????? ???????????? ???????????????.");
                dlg.setView(editText);
                dlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent data = new Intent();
                        String newCompanyName = editText.getText().toString();
                        data.putExtra(InfoAddActivity.EXTRA_NAME, newCompanyName);
                        data.putExtra(EXTRA_INFO, info);

                        setResult(RESULT_OK, data);

                        finish();
                    }
                });
                dlg.show();

            }
        });

        if(intent.getExtras().getBoolean(EXTRA_DETAIL, false))  binding.selectBtn.setVisibility(View.GONE);

        switch (info) {
            case 100:
                binding.selectTitle.setText(R.string.company_select);
                binding.recyclerView.setAdapter(companySelectAdapter);
                break;

            case 200:
                binding.selectBtn.setVisibility(View.GONE);
                binding.selectTitle.setText(R.string.product_select);
                binding.recyclerView.setAdapter(productSelectAdapter);
                break;

            case 300:
                binding.selectBtn.setVisibility(View.GONE);
                binding.selectTitle.setText(R.string.box_select);
                binding.recyclerView.setAdapter(boxSelectAdapter);
                break;

            default:
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

        boxViewModel = new ViewModelProvider(this).get(BoxViewModel.class);
        boxViewModel.getAllBoxs().observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                boxSelectAdapter.setBoxs(boxes);
            }
        });

        boxSelectAdapter.setOnItemClickListener(new BoxSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Box box) {
                Intent data = new Intent();
                data.putExtra(InfoAddActivity.EXTRA_ID, box.getId());
                data.putExtra(InfoAddActivity.EXTRA_NAME, box.getName());
                data.putExtra(InfoAddActivity.EXTRA_VALUE, box.getWeight());
                data.putExtra(EXTRA_INFO, info);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        EditText editText = (EditText) findViewById(R.id.select_edit);

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

    private void searchDatabase(String query, int info) {
        String searchQuery = "%" + query + "%";
        switch (info) {
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

    //?????? ????????? ????????? ?????????
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    //????????? ?????????
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