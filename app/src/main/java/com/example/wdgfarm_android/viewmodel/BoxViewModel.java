package com.example.wdgfarm_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.database.BoxRepository;
import com.example.wdgfarm_android.model.Box;

import java.util.List;

public class BoxViewModel extends AndroidViewModel {
    private BoxRepository repository;
    private LiveData<List<Box>> allBoxs;


    public BoxViewModel(@NonNull Application application){
        super(application);

        repository = new BoxRepository(application);
        allBoxs = repository.getAllBoxs();
    }


    public void insert(Box box){
        repository.insert(box);
    }

    public void update(Box box){
        repository.update(box);
    }

    public void delete(Box box){
        repository.delete(box);
    }

    public void deleteAllBoxs(){
        repository.deleteAllBoxs();
    }

    public LiveData<List<Box>> getAllBoxs(){
        return allBoxs;
    }
}
