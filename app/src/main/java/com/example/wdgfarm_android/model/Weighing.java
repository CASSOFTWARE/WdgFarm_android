package com.example.wdgfarm_android.model;

import java.util.Date;

public class Weighing {

    private Box box;
    private Company company;
    private Product product;
    private Date date;
    private float totalWeight;
    private int boxAccount;
    private float paletteWeight;
    private float deductibleWeight;



    private float realWeight;

    public Weighing(){

    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

