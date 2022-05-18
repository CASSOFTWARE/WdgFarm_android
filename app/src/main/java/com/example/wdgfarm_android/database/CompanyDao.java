package com.example.wdgfarm_android.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;

import java.util.List;


@Dao
public interface CompanyDao {

    @Insert
    void insert(Company company);

    @Update
    void update(Company company);

    @Delete
    void delete(Company company);

    @Query("DELETE FROM company_table")
    void deleteAllCompanys();

    @Query("SELECT * FROM company_table")
    LiveData<List<Company>> getAllCompanys();

    @Query("SELECT * FROM company_table WHERE name LIKE :arg")
    LiveData<List<Company>> getFiltterCompanys(String arg);
}
