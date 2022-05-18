package com.example.wdgfarm_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.database.ProductRepository;
import com.example.wdgfarm_android.model.Product;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repository;
    private LiveData<List<Product>> allProducts;


    public ProductViewModel(@NonNull Application application){
        super(application);

        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
    }

    public void insert(Product product){
        repository.insert(product);
    }

    public void update(Product product){
        repository.update(product);
    }

    public void delete(Product product){
        repository.delete(product);
    }

    public void deleteAllProducts(){
        repository.deleteAllProducts();
    }

    public LiveData<List<Product>> getAllProducts(){
        return allProducts;
    }

    public LiveData<List<Product>> getFiltterProducts(String arg){
        return repository.getFiltterProducts(arg);
    }
}
