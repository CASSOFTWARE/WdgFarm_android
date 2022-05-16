package com.example.wdgfarm_android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Box_table")
public class Box {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private float weight;

    public Box(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}