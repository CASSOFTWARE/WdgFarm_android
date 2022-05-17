package com.example.wdgfarm_android.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wdgfarm_android.model.Weighing;

import java.util.List;


@Dao
public interface WeighingDao {

    @Insert
    void insert(Weighing weighing);

    @Update
    void update(Weighing weighing);

    @Delete
    void delete(Weighing weighing);

    @Query("DELETE FROM weighing_table")
    void deleteAllWeighings();

//    @Query("SELECT * FROM weighing_table WHERE id = :id")
//    Weighing selectWhere(int id);

    @Query("SELECT * FROM weighing_table")
    LiveData<List<Weighing>> getAllWeighings();
}
