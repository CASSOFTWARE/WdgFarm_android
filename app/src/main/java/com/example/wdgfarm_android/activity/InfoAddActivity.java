package com.example.wdgfarm_android.activity;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.ActivityInfoAddBinding;
import com.example.wdgfarm_android.model.Box;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.utils.SqliteUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class InfoAddActivity extends AppCompatActivity {

    SqliteUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add);

        dbUtil = new SqliteUtil(this);

        Intent intent = getIntent();
        ActivityInfoAddBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info_add);

        String info = intent.getExtras().getString("info");
        int type = intent.getExtras().getInt("type");
        binding.infoBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        switch (info){
            case "상품 정보":
                binding.infoAddTitle.setText(info);
                binding.infoAddCodeText.setText(R.string.product_code);
                binding.infoAddNameText.setText(R.string.product_name);
                binding.infoAddValueText.setText(R.string.product_price);
                break;

            case "업체 정보":
                binding.infoAddTitle.setText(info);
                binding.infoAddCodeText.setText(R.string.company_code);
                binding.infoAddNameText.setText(R.string.company_name);
                binding.infoAddValueText.setVisibility(View.GONE);
                binding.infoAddValue.setVisibility(View.GONE);
                break;

            case "박스 정보":
                binding.infoAddTitle.setText(info);
                binding.infoAddCodeText.setVisibility(View.GONE);
                binding.infoAddCodeValue.setVisibility(View.GONE);
                binding.infoAddNameText.setText(R.string.box_name);
                binding.infoAddValueText.setText(R.string.box_weight);
                break;

            default:
                break;
        }

        if(type == 0){
            binding.infoDeleteBtn.setVisibility(View.GONE);
        }


        binding.infoSaveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (info){
                    case "상품 정보":
                        if(_checkBlank(binding, info)){
                            Product product = new Product();
                            product.setCode(parseInt(binding.infoAddCodeValue.getText().toString()));
                            product.setName(binding.infoAddNameValue.getText().toString());
                            product.setPrice(parseInt(binding.infoAddValue.getText().toString()));

                            dbUtil.insertProduct(product);
                            Toast.makeText(getApplicationContext(), R.string.toast_save, Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        break;

                    case "업체 정보":
                        if(_checkBlank(binding, info)){
                            Company company = new Company();
                            company.setCode(parseInt(binding.infoAddCodeValue.getText().toString()));
                            company.setName(binding.infoAddNameValue.getText().toString());

                            dbUtil.insertCompany(company);
                            Toast.makeText(getApplicationContext(), R.string.toast_save, Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        break;

                    case "박스 정보":
                        if(_checkBlank(binding, info)){
                            Box box = new Box();
                            box.setName(binding.infoAddNameValue.getText().toString());
                            box.setWeight(parseFloat(binding.infoAddValue.getText().toString()));

                            dbUtil.insertBox(box);
                            Toast.makeText(getApplicationContext(), R.string.toast_save, Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        break;
                }
            }
        });
    }

    //빈칸 체크
    private boolean _checkBlank(ActivityInfoAddBinding binding, String info){
        switch (info){
            case "상품 정보":
                if(binding.infoAddCodeValue.getText().toString().isEmpty() || binding.infoAddNameValue.getText().toString().isEmpty() || binding.infoAddValue.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.toast_check_blank, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "업체 정보":
                if(binding.infoAddCodeValue.getText().toString().isEmpty() || binding.infoAddNameValue.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.toast_check_blank, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "박스 정보":
                if(binding.infoAddNameValue.getText().toString().isEmpty() || binding.infoAddValue.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.toast_check_blank, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        return true;
    }



}