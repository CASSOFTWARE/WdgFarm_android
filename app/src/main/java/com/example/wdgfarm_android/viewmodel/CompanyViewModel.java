package com.example.wdgfarm_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.database.CompanyRepository;
import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;

import java.util.List;

public class CompanyViewModel extends AndroidViewModel {
    private CompanyRepository repository;
    private LiveData<List<Company>> allCompanys;


    public CompanyViewModel(@NonNull Application application){
        super(application);

        repository = new CompanyRepository(application);
        allCompanys = repository.getAllCompanys();
    }


    public void insert(Company company){
        repository.insert(company);
    }

    public void update(Company company){
        repository.update(company);
    }

    public void delete(Company company){
        repository.delete(company);
    }

    public void deleteAllCompanys(){
        repository.deleteAllCompanys();
    }

    public LiveData<List<Company>> getAllCompanys(){
        return allCompanys;
    }

    public LiveData<List<Company>> getFiltterCompanys(String arg){
        return repository.getFiltterCompanys(arg);
    }
}
