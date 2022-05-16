package com.example.wdgfarm_android.activity;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.ActivityInfoAddBinding;


public class InfoAddActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.wdgfarm_android.EXTRA_ID";
    public static final String EXTRA_CODE = "com.example.wdgfarm_android.EXTRA_CODE";
    public static final String EXTRA_NAME = "com.example.wdgfarm_android.EXTRA_NAME";
    public static final String EXTRA_VALUE = "com.example.wdgfarm_android.EXTRA_VALUE";

    public static final int DELETE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add);


        Intent intent = getIntent();
        ActivityInfoAddBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info_add);

        String info = intent.getExtras().getString("info");

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

        if(intent.hasExtra(EXTRA_ID)){
            binding.infoAddCodeValue.setText(String.valueOf(intent.getIntExtra(EXTRA_CODE, 0)));
            binding.infoAddNameValue.setText(intent.getStringExtra(EXTRA_NAME));
            binding.infoAddValue.setText(String.valueOf(intent.getIntExtra(EXTRA_VALUE, 1000)));
        }
        else{
            binding.infoDeleteBtn.setVisibility(View.GONE);
        }


        binding.infoSaveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (info){
                    case "상품 정보":
                        if(_checkBlank(binding, info)){
                            Intent data = new Intent();
                            data.putExtra("info", info);
                            data.putExtra(EXTRA_CODE, parseInt(binding.infoAddCodeValue.getText().toString()));
                            data.putExtra(EXTRA_NAME, binding.infoAddNameValue.getText().toString());
                            data.putExtra(EXTRA_VALUE, parseInt(binding.infoAddValue.getText().toString()));

                            int id = getIntent().getIntExtra(EXTRA_ID, -1);
                            if(id != -1){
                                data.putExtra(EXTRA_ID, id);
                            }
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;

                    case "업체 정보":
                        if(_checkBlank(binding, info)){
                            Intent data = new Intent();
                            data.putExtra("info", info);
                            data.putExtra(EXTRA_CODE, parseInt(binding.infoAddCodeValue.getText().toString()));
                            data.putExtra(EXTRA_NAME, binding.infoAddNameValue.getText().toString());

                            int id = getIntent().getIntExtra(EXTRA_ID, -1);
                            if(id != -1){
                                data.putExtra(EXTRA_ID, id);
                            }
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;

                    case "박스 정보":
                        if(_checkBlank(binding, info)){
                            Intent data = new Intent();
                            data.putExtra("info", info);
                            data.putExtra(EXTRA_NAME, binding.infoAddNameValue.getText().toString());
                            data.putExtra(EXTRA_VALUE, parseFloat(binding.infoAddValue.getText().toString()));

                            int id = getIntent().getIntExtra(EXTRA_ID, -1);
                            if(id != -1){
                                data.putExtra(EXTRA_ID, id);
                            }
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;
                }
            }
        });

        binding.infoDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("info", info);
                data.putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, 1));
                setResult(DELETE_REQUEST, data);
            }
        });
    }

    //빈칸 체크
    private boolean _checkBlank(ActivityInfoAddBinding binding, String info){
        switch (info){
            case "상품 정보":
                if(binding.infoAddCodeValue.getText().toString().trim().isEmpty() || binding.infoAddNameValue.getText().toString().trim().isEmpty() || binding.infoAddValue.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, R.string.toast_check_blank, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "업체 정보":
                if(binding.infoAddCodeValue.getText().toString().trim().isEmpty() || binding.infoAddNameValue.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, R.string.toast_check_blank, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "박스 정보":
                if(binding.infoAddNameValue.getText().toString().trim().isEmpty() || binding.infoAddValue.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, R.string.toast_check_blank, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        return true;
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