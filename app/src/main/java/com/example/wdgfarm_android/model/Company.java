package com.example.wdgfarm_android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Company_table")
public class Company {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String code;
    private String name;



    private String boss;
    private String tel;

    public Company (String code, String name, String boss, String tel){
        this.code = code;
        this.name = name;
        this.boss = boss;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


}
