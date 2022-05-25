package com.example.wdgfarm_android.utils;

import android.content.ContentValues;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;
import com.example.wdgfarm_android.viewmodel.CompanyViewModel;
import com.example.wdgfarm_android.viewmodel.ProductViewModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class ExcelHelper {

    public static void importExcelCompany(CompanyViewModel companyViewModel, Sheet sheet) {
        companyViewModel.deleteAllCompanys();
        Iterator<Row> rit = sheet.rowIterator();
        Row row = rit.next();
        row = rit.next();
        Company company;

        for ( ; rit.hasNext(); ) {
            row = rit.next();
            if(rit.hasNext()) {
                row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                company = new Company(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue(), row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue(), row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue(), row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                companyViewModel.insert(company);
            }
        }
    }

    public static void importExcelProduct(ProductViewModel productViewModel, Sheet sheet) {
        productViewModel.deleteAllProducts();
        Iterator<Row> rit = sheet.rowIterator();
        Row row = rit.next();
        row = rit.next();
        Product product;
        for ( ; rit.hasNext(); ) {
            row = rit.next();
            if(rit.hasNext()) {
                row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                //TODO : Product 모델하고 Excel 파일 모델하고 데이터 일치화
                //TODO : Excel 파일에는 가격이 없음

                product = new Product(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue(), row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue(), row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                productViewModel.insert(product);
            }
        }
    }
}
