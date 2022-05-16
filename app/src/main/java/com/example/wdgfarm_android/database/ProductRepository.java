package com.example.wdgfarm_android.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.model.Product;

import java.util.List;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;

    public ProductRepository(Application application){
        ProductDatabase database = ProductDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
    }

    public void insert(Product product){
        new InsertProductAsyncTask(productDao).execute(product);
    }

    public void update(Product product){
        new UpdateProductAsyncTask(productDao).execute(product);
    }

    public void delete(Product product){
        new DeleteProductAsyncTask(productDao).execute(product);
    }

    public void deleteAllProducts(){
        new DeleteAllProductsAsyncTask(productDao).execute();
    }

    public LiveData<List<Product>> getAllProducts(){
        return allProducts;
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao){
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products){
            productDao.insert(products[0]);
            return null;
        }
    }
    private static class UpdateProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private UpdateProductAsyncTask(ProductDao productDao){
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products){
            productDao.update(products[0]);
            return null;
        }
    }
    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private DeleteProductAsyncTask(ProductDao productDao){
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products){
            productDao.delete(products[0]);
            return null;
        }
    }

    private static class DeleteAllProductsAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private DeleteAllProductsAsyncTask(ProductDao productDao){
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... voids){
            productDao.deleteAllProducts();
            return null;
        }
    }
}
