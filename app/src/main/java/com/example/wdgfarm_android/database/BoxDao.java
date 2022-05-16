package com.example.wdgfarm_android.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wdgfarm_android.model.Box;

import java.util.List;


@Dao
public interface BoxDao {

    @Insert
    void insert(Box box);

    @Update
    void update(Box box);

    @Delete
    void delete(Box box);

    @Query("DELETE FROM box_table")
    void deleteAllBoxs();

    @Query("SELECT * FROM box_table")
    LiveData<List<Box>> getAllBoxs();
}
