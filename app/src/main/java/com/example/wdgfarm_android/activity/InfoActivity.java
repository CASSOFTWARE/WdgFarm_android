package com.example.wdgfarm_android.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import android.view.WindowManager;
import android.widget.Toast;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.adapter.BoxAdapter;
import com.example.wdgfarm_android.adapter.CompanyAdapter;
import com.example.wdgfarm_android.adapter.ProductAdapter;
import com.example.wdgfarm_android.databinding.ActivityInfoBinding;

import com.example.wdgfarm_android.model.Box;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.viewmodel.BoxViewModel;
import com.example.wdgfarm_android.viewmodel.CompanyViewModel;
import com.example.wdgfarm_android.viewmodel.InfoViewModel;
import com.example.wdgfarm_android.viewmodel.ProductViewModel;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    private ProductViewModel productViewModel;
    private CompanyViewModel companyViewModel;
    private BoxViewModel boxViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);

        Intent intent = getIntent();
        InfoViewModel infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        String info = intent.getExtras().getString("info");

        infoViewModel.info.setValue(info);

        infoViewModel.info.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.infoTitle.setText(infoViewModel.info.getValue() + "");
            }
        });

        binding.infoAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, InfoAddActivity.class);
                intent.putExtra("info", info);

                startActivityForResult(intent, ADD_REQUEST);
            }
        });

        binding.infoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.exportFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    exportData(info);
                }
            }
        });

        binding.loadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = "aaaa";
                infoViewModel.info.setValue(a);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        ProductAdapter productAdapter = new ProductAdapter();
        CompanyAdapter companyAdapter = new CompanyAdapter();
        BoxAdapter boxAdapter = new BoxAdapter();

        switch (info) {
            case "상품 정보":
                binding.recyclerView.setAdapter(productAdapter);
                break;

            case "업체 정보":
                binding.recyclerView.setAdapter(companyAdapter);
                break;

            case "박스 정보":
                binding.recyclerView.setAdapter(boxAdapter);
                break;

            default:
                break;
        }

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productAdapter.setProducts(products);
            }
        });

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(InfoActivity.this, InfoAddActivity.class);
                intent.putExtra(InfoAddActivity.EXTRA_ID, product.getId());
                intent.putExtra(InfoAddActivity.EXTRA_CODE, product.getCode());
                intent.putExtra(InfoAddActivity.EXTRA_NAME, product.getName());
                intent.putExtra(InfoAddActivity.EXTRA_VALUE, product.getPrice());
                intent.putExtra("info", info);

                startActivityForResult(intent, EDIT_REQUEST);
            }
        });

        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        companyViewModel.getAllCompanys().observe(this, new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> company) {
                companyAdapter.setCompanys(company);
            }
        });

        companyAdapter.setOnItemClickListener(new CompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Company company) {
                Intent intent = new Intent(InfoActivity.this, InfoAddActivity.class);
                intent.putExtra(InfoAddActivity.EXTRA_ID, company.getId());
                intent.putExtra(InfoAddActivity.EXTRA_CODE, company.getCode());
                intent.putExtra(InfoAddActivity.EXTRA_NAME, company.getName());
                intent.putExtra("info", info);

                startActivityForResult(intent, EDIT_REQUEST);
            }
        });

        boxViewModel = new ViewModelProvider(this).get(BoxViewModel.class);
        boxViewModel.getAllBoxs().observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> box) {
                boxAdapter.setBoxs(box);
            }
        });

        boxAdapter.setOnItemClickListener(new BoxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Box box) {
                Intent intent = new Intent(InfoActivity.this, InfoAddActivity.class);
                intent.putExtra(InfoAddActivity.EXTRA_ID, box.getId());
                intent.putExtra(InfoAddActivity.EXTRA_NAME, box.getName());
                intent.putExtra(InfoAddActivity.EXTRA_VALUE, box.getWeight());
                intent.putExtra("info", info);

                startActivityForResult(intent, EDIT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
            switch (data.getStringExtra("info")) {
                case "상품 정보":
                    int product_code = data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0);
                    String product_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    int product_price = data.getIntExtra(InfoAddActivity.EXTRA_VALUE, 1000);

                    Product product = new Product(product_code, product_name, product_price);
                    productViewModel.insert(product);

                    Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
                    break;

                case "업체 정보":
                    int company_code = data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0);
                    String company_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);


                    Company company = new Company(company_code, company_name);
                    companyViewModel.insert(company);

                    Toast.makeText(this, "Company saved", Toast.LENGTH_SHORT).show();
                    break;

                case "박스 정보":
                    String box_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    float box_weight = data.getFloatExtra(InfoAddActivity.EXTRA_VALUE, 0);


                    Box box = new Box(box_name, box_weight);
                    boxViewModel.insert(box);

                    Toast.makeText(this, "Box saved", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(InfoAddActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (data.getStringExtra("info")) {
                case "상품 정보":
                    int product_code = data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0);
                    String product_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    int product_price = data.getIntExtra(InfoAddActivity.EXTRA_VALUE, 1000);

                    Product product = new Product(product_code, product_name, product_price);
                    product.setId(id);

                    productViewModel.update(product);

                    Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
                    break;

                case "업체 정보":
                    int company_code = data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0);
                    String company_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);

                    Company company = new Company(company_code, company_name);
                    company.setId(id);

                    companyViewModel.update(company);

                    Toast.makeText(this, "Company updated", Toast.LENGTH_SHORT).show();
                    break;

                case "박스 정보":
                    String box_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    float box_weight = data.getFloatExtra(InfoAddActivity.EXTRA_VALUE, 0);

                    Box box = new Box(box_name, box_weight);
                    box.setId(id);

                    boxViewModel.update(box);

                    Toast.makeText(this, "Company updated", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (requestCode == EDIT_REQUEST && resultCode == InfoAddActivity.DELETE_REQUEST) {
            switch (data.getStringExtra("info")) {
                case "상품 정보":
                    Product product = new Product(data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0), data.getStringExtra(InfoAddActivity.EXTRA_NAME), data.getIntExtra(InfoAddActivity.EXTRA_VALUE, 1000));
                    product.setId(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    productViewModel.delete(product);
                    break;

                case "업체 정보":
                    Company company = new Company(data.getIntExtra(InfoAddActivity.EXTRA_CODE, 0), data.getStringExtra(InfoAddActivity.EXTRA_NAME));
                    company.setId(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    companyViewModel.delete(company);
                    break;

                case "박스 정보":
                    Box box = new Box(data.getStringExtra(InfoAddActivity.EXTRA_NAME), data.getFloatExtra(InfoAddActivity.EXTRA_VALUE, 0));
                    box.setId(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    boxViewModel.delete(box);
                    break;

                default:
                    break;
            }
            Toast.makeText(this, "Delete complete", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
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
                List<Product> productList = productViewModel.getAllProducts().getValue();
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
                List<Company> companyList = companyViewModel.getAllCompanys().getValue();
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
                List<Box> boxList = boxViewModel.getAllBoxs().getValue();
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

    private void exportData(String info) {
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