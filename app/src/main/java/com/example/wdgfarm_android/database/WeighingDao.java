package com.example.wdgfarm_android.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wdgfarm_android.model.Weighing;

import java.util.Date;
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

    @Query("SELECT * FROM weighing_table WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    LiveData<List<Weighing>> getFitterDateWeighings(Long from, Long to);

    @Query("SELECT * FROM weighing_table ORDER BY date DESC")
    LiveData<List<Weighing>> getAllWeighings();

    @Query("SELECT * FROM weighing_table WHERE date BETWEEN :from AND :to AND companyName LIKE :arg ORDER BY date DESC")
    LiveData<List<Weighing>> getFitterCompanyWeighings(Long from, Long to, String arg);

    @Query("SELECT * FROM weighing_table WHERE date BETWEEN :from AND :to AND productName LIKE :arg ORDER BY date DESC")
    LiveData<List<Weighing>> getFitterProductWeighings(Long from, Long to, String arg);

    @Query("SELECT * FROM weighing_table WHERE date BETWEEN :from AND :to AND erpDate LIKE \"전송 실패\" ORDER BY date DESC")
    LiveData<List<Weighing>> getFitterNotSendWeighings(Long from, Long to);

    @Query("UPDATE weighing_table SET companyID = :companyId, companyCode = :companyCode, companyName = :companyName WHERE id = :id")
    void updateNotSendWeighings(int companyId, String companyCode, String companyName, int id);

    @Query("DELETE FROM weighing_table WHERE id = :id")
    void deleteWeighing(int id);
}
