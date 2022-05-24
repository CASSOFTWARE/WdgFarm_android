package com.example.wdgfarm_android.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.wdgfarm_android.utils.RoomTypeConverter;

import java.util.Date;

@Entity(tableName = "Weighing_table")
public class Weighing {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private int companyID;
    private String companyCode;
    private String companyName;

    private int productID;
    private String productCode;
    private String productName;
    private int productPrice;

    @TypeConverters({RoomTypeConverter.class})
    private Date date;

    private float totalWeight;

    private int boxID;
    private String boxName;
    private float boxWeight;

    private int boxAccount;
    private float paletteWeight;
    private float deductibleWeight;
    private float realWeight;

    public Weighing() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getBoxID() {
        return boxID;
    }

    public void setBoxID(int boxID) {
        this.boxID = boxID;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public float getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(float boxWeight) {
        this.boxWeight = boxWeight;
    }

    public int getBoxAccount() {
        return boxAccount;
    }

    public void setBoxAccount(int boxAccount) {
        this.boxAccount = boxAccount;
    }

    public float getPaletteWeight() {
        return paletteWeight;
    }

    public void setPaletteWeight(float paletteWeight) {
        this.paletteWeight = paletteWeight;
    }

    public float getDeductibleWeight() {
        return deductibleWeight;
    }

    public void setDeductibleWeight(float deductibleWeight) {
        this.deductibleWeight = deductibleWeight;
    }

    public float getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(float realWeight) {
        this.realWeight = realWeight;
    }
}
