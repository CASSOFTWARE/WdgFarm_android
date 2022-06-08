package com.example.wdgfarm_android.activity;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Toast;

import com.example.wdgfarm_android.BuildConfig;
import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.adapter.BoxAdapter;
import com.example.wdgfarm_android.adapter.CompanyAdapter;
import com.example.wdgfarm_android.adapter.ProductAdapter;
import com.example.wdgfarm_android.api.ApiListener;
import com.example.wdgfarm_android.api.CompanyApi;
import com.example.wdgfarm_android.api.ProductApi;
import com.example.wdgfarm_android.databinding.ActivityInfoBinding;

import com.example.wdgfarm_android.model.Box;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.utils.ExcelHelper;
import com.example.wdgfarm_android.viewmodel.BoxViewModel;
import com.example.wdgfarm_android.viewmodel.CompanyViewModel;
import com.example.wdgfarm_android.viewmodel.InfoViewModel;
import com.example.wdgfarm_android.viewmodel.ProductViewModel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    private static ProductViewModel productViewModel;
    private static CompanyViewModel companyViewModel;
    private static BoxViewModel boxViewModel;

    private static String info, zone, session;
    ActivityResultLauncher<Intent> filePicker;

    private static String extensionXLXS = "XLXS";

    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ProductAdapter productAdapter = new ProductAdapter();
        CompanyAdapter companyAdapter = new CompanyAdapter();
        BoxAdapter boxAdapter = new BoxAdapter();

        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);

        Intent intent = getIntent();

        InfoViewModel infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        info = intent.getExtras().getString("info");
        zone = intent.getExtras().getString("zone");
        session = intent.getExtras().getString("session");

        infoViewModel.info.setValue(info);

        infoViewModel.info.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.infoTitle.setText(infoViewModel.info.getValue() + "");
                if (s.contains("박스 정보")) {
                    binding.loadFileBtn.setEnabled(false);
                }
            }
        });

        //파일 가져오기
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intentExcel = result.getData();
                        Uri uri = intentExcel.getData();

                        readExcelFile(InfoActivity.this, uri);

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
                if (!checkPermission()) {
                    exportData(info);
                } else {
                    if (checkPermission()){
                        requestPermissionAndContinue();
                    } else{
                        exportData(info);
                    }
                }
            }
        });

        binding.loadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);


        switch (info) {
            case "상품 정보":
                binding.recyclerView.setAdapter(productAdapter);
                binding.titleProductCode.setVisibility(View.VISIBLE);
                binding.titleProductName.setVisibility(View.VISIBLE);
                binding.titleProductPrice.setVisibility(View.VISIBLE);
                break;

            case "거래처 정보":
                binding.recyclerView.setAdapter(companyAdapter);
                binding.titleInfoCode.setVisibility(View.VISIBLE);
                binding.titleInfoValue.setVisibility(View.VISIBLE);
                break;

            case "박스 정보":
                binding.recyclerView.setAdapter(boxAdapter);
                binding.titleInfoCode.setVisibility(View.VISIBLE);
                binding.titleInfoValue.setVisibility(View.VISIBLE);
                binding.titleInfoCode.setText(R.string.box_name);
                binding.titleInfoValue.setText(R.string.box_weight);
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
                intent.putExtra(InfoAddActivity.EXTRA_VALUE, company.getBoss());
                intent.putExtra(InfoAddActivity.EXTRA_TEL, company.getTel());
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
                    String product_code = data.getStringExtra(InfoAddActivity.EXTRA_CODE);
                    String product_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    String product_price = data.getStringExtra(InfoAddActivity.EXTRA_VALUE);

                    Product product = new Product(product_code, product_name, product_price);
                    new ProductApi(zone, session, product_code, product_name, Integer.parseInt(product_price), new ApiListener() {
                        @Override
                        public void success(String response) throws JSONException {
                            productViewModel.insert(product);
                            Toast.makeText(getApplicationContext(), "상품 등록 성공", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void fail() {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(InfoActivity.this);
                            dlg.setTitle("상품 등록 실패");
                            dlg.setMessage("상품 등록 실패했습니다.");
                            dlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dlg.show();
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    productViewModel.insert(product);

                    //Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show();
                    break;

                case "거래처 정보":
                    String company_code = data.getStringExtra(InfoAddActivity.EXTRA_CODE);
                    String company_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    String company_boss = data.getStringExtra(InfoAddActivity.EXTRA_VALUE);
                    String company_tel = data.getStringExtra(InfoAddActivity.EXTRA_TEL);

                    Company company = new Company(company_code, company_name, company_boss, company_tel);
                    new CompanyApi(zone, session, company_code, company_name, company_boss, company_tel, new ApiListener() {
                        @Override
                        public void success(String response) throws JSONException {
                            companyViewModel.insert(company);
                            Toast.makeText(getApplicationContext(), "거래처 등록 성공", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void fail() {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(InfoActivity.this);
                            dlg.setTitle("거래처 등록 실패");
                            dlg.setMessage("거래처 등록 실패했습니다.");
                            dlg.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dlg.show();
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    //Toast.makeText(this, "Company saved", Toast.LENGTH_SHORT).show();
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
                    String product_code = data.getStringExtra(InfoAddActivity.EXTRA_CODE);
                    String product_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    String product_price = data.getStringExtra(InfoAddActivity.EXTRA_VALUE);

                    Product product = new Product(product_code, product_name, product_price);
                    product.setId(id);

                    productViewModel.update(product);

                    Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
                    break;

                case "거래처 정보":
                    String company_code = data.getStringExtra(InfoAddActivity.EXTRA_CODE);
                    String company_name = data.getStringExtra(InfoAddActivity.EXTRA_NAME);
                    String company_boss = data.getStringExtra(InfoAddActivity.EXTRA_VALUE);
                    String company_tel = data.getStringExtra(InfoAddActivity.EXTRA_TEL);

                    Company company = new Company(company_code, company_name, company_boss, company_tel);
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
                    Product product = new Product(data.getStringExtra(InfoAddActivity.EXTRA_CODE), data.getStringExtra(InfoAddActivity.EXTRA_NAME), data.getStringExtra(InfoAddActivity.EXTRA_VALUE));
                    product.setId(data.getIntExtra(InfoAddActivity.EXTRA_ID, 0));
                    productViewModel.delete(product);
                    break;

                case "거래처 정보":
                    Company company = new Company(data.getStringExtra(InfoAddActivity.EXTRA_CODE), data.getStringExtra(InfoAddActivity.EXTRA_NAME), data.getStringExtra(InfoAddActivity.EXTRA_VALUE), data.getStringExtra(InfoAddActivity.EXTRA_TEL));
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
            //Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
    }
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, MANAGE_EXTERNAL_STORAGE)) {

                androidx.appcompat.app.AlertDialog.Builder alertBuilder = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext());
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("권한이 필요합니다.");
                alertBuilder.setMessage("기기의 사진, 미디어, 파일에 액세스하도록 하용하시겠습니까?");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(InfoActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                androidx.appcompat.app.AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(InfoActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            exportData(info);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    exportData(info);
                } else {
//                    finish();
                }

            } else {
//                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
            case "거래처 정보":
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
        switch (info) {
            case "상품 정보":
                fileName = "ProductData.csv";
                break;
            case "거래처 정보":
                fileName = "CompanyData.csv";
                break;
            case "박스 정보":
                fileName = "BoxData.csv";
                break;
        }
        File file = null;
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (root.canWrite()) {
            File dir = new File(root.getAbsolutePath() + "/WRMS");
            if(!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(root, fileName);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_SUBJECT, "WdgFarm Data");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("text/html");
        //startActivity(intent);
        startActivity(intent.createChooser(intent, "Share"));
    }

    public void openFilePicker() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            fileIntent.setType("application/*");

            filePicker.launch(fileIntent);
        } catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
    }

    public void readExcelFile(Context context, Uri uri) {
        try {
            InputStream inStream;
            Workbook wb = null;

            try {
                inStream = context.getContentResolver().openInputStream(uri);

                wb = new XSSFWorkbook(inStream);

                inStream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

            Sheet sheet = wb.getSheetAt(0);
            switch (info) {
                case "상품 정보":
                    ExcelHelper.importExcelProduct(productViewModel, sheet);
                    break;
                case "거래처 정보":
                    ExcelHelper.importExcelCompany(companyViewModel, sheet);
                    break;
                case "박스 정보":
                    break;
            }

        } catch (Exception ex) {
            Log.d("TAG", ex.toString());
        }
    }
}