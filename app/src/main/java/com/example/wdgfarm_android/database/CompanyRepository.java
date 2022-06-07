package com.example.wdgfarm_android.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.model.Company;
import com.example.wdgfarm_android.model.Product;

import java.util.List;

public class CompanyRepository {
    private static CompanyDao companyDao;
    private static LiveData<List<Company>> allCompanys;

    public CompanyRepository(Application application){
        CompanyDatabase database = CompanyDatabase.getInstance(application);
        companyDao = database.companyDao();
        allCompanys = companyDao.getAllCompanys();
    }

    public void insert(Company company){
        new InsertCompanyAsyncTask(companyDao).execute(company);
    }

    public void update(Company company){
        new UpdateCompanyAsyncTask(companyDao).execute(company);
    }

    public void delete(Company company){
        new DeleteCompanyAsyncTask(companyDao).execute(company);
    }

    public void deleteAllCompanys(){
        new DeleteAllCompanysAsyncTask(companyDao).execute();
    }

    public LiveData<List<Company>> getAllCompanys(){
        return allCompanys;
    }

    public LiveData<List<Company>> getFiltterCompanys(String arg) {
        return companyDao.getFiltterCompanys(arg);
    }

    private static class InsertCompanyAsyncTask extends AsyncTask<Company, Void, Void> {
        private CompanyDao companyDao;

        private InsertCompanyAsyncTask(CompanyDao companyDao){
            this.companyDao = companyDao;
        }

        @Override
        protected Void doInBackground(Company... companys){
            companyDao.insert(companys[0]);
            return null;
        }
    }
    private static class UpdateCompanyAsyncTask extends AsyncTask<Company, Void, Void> {
        private CompanyDao companyDao;

        private UpdateCompanyAsyncTask(CompanyDao companyDao){
            this.companyDao = companyDao;
        }

        @Override
        protected Void doInBackground(Company... companys){
            companyDao.update(companys[0]);
            return null;
        }
    }
    private static class DeleteCompanyAsyncTask extends AsyncTask<Company, Void, Void> {
        private CompanyDao companyDao;

        private DeleteCompanyAsyncTask(CompanyDao companyDao){
            this.companyDao = companyDao;
        }

        @Override
        protected Void doInBackground(Company... companys){
            companyDao.delete(companys[0]);
            return null;
        }
    }

    private static class DeleteAllCompanysAsyncTask extends AsyncTask<Company, Void, Void> {
        private CompanyDao companyDao;

        private DeleteAllCompanysAsyncTask(CompanyDao companyDao){
            this.companyDao = companyDao;
        }

        @Override
        protected Void doInBackground(Company... voids){
            companyDao.deleteAllCompanys();
            return null;
        }
    }
}
