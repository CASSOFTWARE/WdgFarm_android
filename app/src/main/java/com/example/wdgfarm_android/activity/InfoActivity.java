package com.example.wdgfarm_android.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.databinding.ActivityInfoBinding;
import com.example.wdgfarm_android.model.Box;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.utils.SqliteUtil;
import com.example.wdgfarm_android.viewmodel.InfoViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    SqliteUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        dbUtil = new SqliteUtil(this);

        Intent intent = getIntent();
        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);
        InfoViewModel infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        String info = intent.getExtras().getString("info");

        infoViewModel.info.setValue(info);

        infoViewModel.info.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.infoTitle.setText(infoViewModel.info.getValue()+"");
            }
        });

        binding.infoBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.infoAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InfoAddActivity.class);
                intent.putExtra("info", info);
                //type 0 = 추가, 1 = 수정
                intent.putExtra("type", 0);

                startActivity(intent);
            }
        });

        binding.exportFileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    sendMail(info);
                }
            }
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private String getDataInCsv(String info) {
        String columnString = null;
        String dataString = "";

        switch (info) {
            case "상품 정보":
                List<Product> productList = dbUtil.getProductList();
                columnString = String.format("\"%s\",\"%s\",\"%s\"\n",
                        "상품코드",
                        "상품명",
                        "단가");

                for (int i = 0; i < productList.size(); i++) {

                    Product product = productList.get(i);

                    dataString += String.format("\"%s\",\"%s\",\"%s\"\n",
                            product.getCode(),
                            product.getName(),
                            product.getPrice());
                }
                break;
            case "업체 정보":
                List<Company> companyList = dbUtil.getCompanyList();
                columnString = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        "거래처코드",
                        "거래처명",
                        "대표자명",
                        "전화",
                        "모바일",
                        "검색창내용");

                for (int i = 0; i < companyList.size(); i++) {

                    Company company = companyList.get(i);

                    dataString += String.format("\"%s\",\"%s\"\n",
                            company.getCode(),
                            company.getName());
                }
                break;
            case "박스 정보":
                List<Box> boxList = dbUtil.getBoxList();
                columnString = String.format("\"%s\",\"%s\"\n",
                        "박스명",
                        "박스중량");

                for (int i = 0; i < boxList.size(); i++) {

                    Box box = boxList.get(i);

                    dataString += String.format("\"%s\",\"%s\"\n",
                            box.getName(),
                            box.getWeight());
                }
                break;

            default:
                break;
        }
        return columnString + dataString;
    }

    private void sendMail(String info) {
        String dataString = getDataInCsv(info);
        String fileName = null;
        switch (info){
            case "상품 정보":
                fileName = "ProductData.csv";
                break;
            case "업체 정보":
                fileName = "CompanyData.csv";
                break;
            case "박스 정보":
                fileName = "BoxData.csv";
                break;
        }
        File file = null;
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (root.canWrite()) {
            File dir = new File(root.getAbsolutePath() + "/PersonData");
            dir.mkdirs();
            file = new File(dir, fileName);
            FileOutputStream out = null;
            BufferedWriter bufferedWriter = null;

            String locale = this.getResources().getConfiguration().locale.getCountry();
            String encoding;
            if (locale == "KR") {
                encoding = "MS949";
            } else {
                encoding = "UTF-8";
            }

            try {
                out = new FileOutputStream(file);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, encoding));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.write(dataString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.wdgfarm_android.provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_SUBJECT, "WdgFarm Data");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("text/html");
        startActivity(intent);
    }
}