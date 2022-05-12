package com.example.wdgfarm_android.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wdgfarm_android.model.Box;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SqliteUtil extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "WdgFarmDB";

    //상품 테이블
    private static final String TABLE_PRODUCT = "tableProduct";
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_CODE = "productCode";
    private static final String PRODUCT_NAME = "productName";
    private static final String PRODUCT_PRICE = "productPrice";

    //업체 테이블
    private static final String TABLE_COMPANY = "tableCompany";
    private static final String COMPANY_ID = "companyId";
    private static final String COMPANY_CODE = "companyCode";
    private static final String COMPANY_NAME = "companyName";

    //박스 테이블
    private static final String TABLE_BOX = "tableBox";
    private static final String BOX_ID = "boxId";
    private static final String BOX_NAME = "boxName";
    private static final String BOX_WEIGHT = "boxWeight";


    public SqliteUtil(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createProductTable(db);
        createCompanyTable(db);
        createBoxTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOX);
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        while(true){
            try{
                return super.getWritableDatabase();
            }catch(SQLiteDatabaseLockedException e){
                System.err.println(e);
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                System.err.println(e);
            }
        }
    }

    private void createProductTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_PRODUCT + "(" +
                PRODUCT_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                PRODUCT_CODE + ", " +
                PRODUCT_NAME + ", " +
                PRODUCT_PRICE +
                ")"
        );
    }

    private void createCompanyTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_COMPANY + "(" +
                COMPANY_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                COMPANY_CODE + ", " +
                COMPANY_NAME +
                ")"
        );
    }

    private void createBoxTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_BOX + "(" +
                BOX_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                BOX_NAME + ", " +
                BOX_WEIGHT +
                ")"
        );
    }


    public final long insertProduct(Product product){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PRODUCT_CODE, product.getCode());
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_PRICE, product.getPrice());

        return db.insert(TABLE_PRODUCT, null, values);
    }

    public final long insertCompany(Company company){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COMPANY_CODE, company.getCode());
        values.put(COMPANY_NAME, company.getName());

        return db.insert(TABLE_COMPANY, null, values);
    }

    public final long insertBox(Box box){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BOX_NAME, box.getName());
        values.put(BOX_WEIGHT, box.getWeight());

        return db.insert(TABLE_BOX, null, values);
    }

    @SuppressLint("Range")
    public List<Product> getProductList(){
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Product item = new Product();
            item.setCode(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            item.setName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
            item.setPrice(cursor.getInt(cursor.getColumnIndex(PRODUCT_PRICE)));
            productList.add(item);
        }
        cursor.close();
        db.close();
        return productList;
    }

    @SuppressLint("Range")
    public List<Company> getCompanyList(){
        List<Company> companyList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPANY, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Company item = new Company();
            item.setCode(cursor.getInt(cursor.getColumnIndex(COMPANY_CODE)));
            item.setName(cursor.getString(cursor.getColumnIndex(COMPANY_NAME)));
            companyList.add(item);
        }
        cursor.close();
        db.close();
        return companyList;
    }

    @SuppressLint("Range")
    public List<Box> getBoxList(){
        List<Box> boxList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOX, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Box item = new Box();
            item.setName(cursor.getString(cursor.getColumnIndex(BOX_NAME)));
            item.setWeight(cursor.getFloat(cursor.getColumnIndex(BOX_WEIGHT)));
            boxList.add(item);
        }
        cursor.close();
        db.close();
        return boxList;
    }
}
