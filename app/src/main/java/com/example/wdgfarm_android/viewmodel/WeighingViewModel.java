package com.example.wdgfarm_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.database.WeighingRepository;
import com.example.wdgfarm_android.model.Weighing;

import java.util.Date;
import java.util.List;

public class WeighingViewModel extends AndroidViewModel {
    private WeighingRepository repository;
    private LiveData<List<Weighing>> allWeighings;


    public WeighingViewModel(@NonNull Application application){
        super(application);

        repository = new WeighingRepository(application);
        allWeighings = repository.getAllWeighings();
    }

    public void insert(Weighing weighing){
        repository.insert(weighing);
    }

    public void update(Weighing weighing){
        repository.update(weighing);
    }

    public void delete(Weighing weighing){
        repository.delete(weighing);
    }

    public void deleteAllWeighings(){
        repository.deleteAllWeighings();
    }

    public LiveData<List<Weighing>> getFitterDateWeighings(Long from, Long to){ return repository.getFitterDateWeighings(from, to);}

    public LiveData<List<Weighing>> getAllWeighings(){
        return allWeighings;
    }

    public LiveData<List<Weighing>> getFitterCompanyWeighings(Long from, Long to, String arg){ return repository.getFitterCompanyWeighings(from, to, arg);}

    public LiveData<List<Weighing>> getFitterProductWeighings(Long from, Long to, String arg){ return repository.getFitterProductWeighings(from, to, arg);}

}
