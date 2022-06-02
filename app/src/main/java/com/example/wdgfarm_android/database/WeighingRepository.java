package com.example.wdgfarm_android.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.model.Weighing;

import java.util.List;

public class WeighingRepository {
    private WeighingDao weighingDao;
    private LiveData<List<Weighing>> allWeighings;

    public WeighingRepository(Application application){
        WeighingDatabase database = WeighingDatabase.getInstance(application);
        weighingDao = database.weighingDao();
        allWeighings = weighingDao.getAllWeighings();
    }

    public void insert(Weighing weighing){
        new InsertWeighingAsyncTask(weighingDao).execute(weighing);
    }

    public void update(Weighing weighing){
        new UpdateWeighingAsyncTask(weighingDao).execute(weighing);
    }

    public void delete(Weighing weighing){
        new DeleteAsyncTask(weighingDao).execute(weighing);
    }

    public void deleteAllWeighings(){
        new DeleteAllWeighingsAsyncTask(weighingDao).execute();
    }

    public LiveData<List<Weighing>> getFitterDateWeighings(Long from, Long to){
        return weighingDao.getFitterDateWeighings(from, to);
    }

    public LiveData<List<Weighing>> getAllWeighings(){
        return allWeighings;
    }

    public LiveData<List<Weighing>> getFitterCompanyWeighings(Long from, Long to, String arg){
        return weighingDao.getFitterCompanyWeighings(from, to, arg);
    }

    public LiveData<List<Weighing>> getFitterProductWeighings(Long from, Long to, String arg){
        return weighingDao.getFitterProductWeighings(from, to, arg);
    }

    public LiveData<List<Weighing>> getFitterNotSendWeighings(Long from, Long to){
        return weighingDao.getFitterNotSendWeighings(from, to);
    }

    public void updateNotSendWeighings(int companyId, String companyCode, String companyName, int id){
        new UpdateNotSendWeighingsAsyncTask(weighingDao, companyId, companyCode, companyName, id).execute();
    }

    public void deleteWeighing(int id){
        new DeleteWeighingAsyncTask(weighingDao, id).execute();
    }

    private static class InsertWeighingAsyncTask extends AsyncTask<Weighing, Void, Void> {
        private WeighingDao weighingDao;

        private InsertWeighingAsyncTask(WeighingDao weighingDao){
            this.weighingDao = weighingDao;
        }

        @Override
        protected Void doInBackground(Weighing... weighings){
            weighingDao.insert(weighings[0]);
            return null;
        }
    }
    private static class UpdateWeighingAsyncTask extends AsyncTask<Weighing, Void, Void> {
        private WeighingDao weighingDao;

        private UpdateWeighingAsyncTask(WeighingDao weighingDao){
            this.weighingDao = weighingDao;
        }

        @Override
        protected Void doInBackground(Weighing... weighings){
            weighingDao.update(weighings[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<Weighing, Void, Void> {
        private WeighingDao weighingDao;

        private DeleteAsyncTask(WeighingDao weighingDao){
            this.weighingDao = weighingDao;
        }

        @Override
        protected Void doInBackground(Weighing... weighings){
            weighingDao.delete(weighings[0]);
            return null;
        }
    }

    private static class DeleteAllWeighingsAsyncTask extends AsyncTask<Weighing, Void, Void> {
        private WeighingDao weighingDao;

        private DeleteAllWeighingsAsyncTask(WeighingDao weighingDao){
            this.weighingDao = weighingDao;
        }

        @Override
        protected Void doInBackground(Weighing... voids){
            weighingDao.deleteAllWeighings();
            return null;
        }
    }

    private static class UpdateNotSendWeighingsAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeighingDao weighingDao;
        private int companyId;
        private String companyCode;
        private String companyName;
        private int id;

        private UpdateNotSendWeighingsAsyncTask(WeighingDao weighingDao ,int companyId, String companyCode, String companyName, int id){
            this.weighingDao = weighingDao;
            this.companyId = companyId;
            this.companyCode = companyCode;
            this.companyName = companyName;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids){
            weighingDao.updateNotSendWeighings(companyId, companyCode, companyName, id);
            return null;
        }
    }

    private static class DeleteWeighingAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeighingDao weighingDao;
        private int id;

        private DeleteWeighingAsyncTask(WeighingDao weighingDao, int id){
            this.weighingDao = weighingDao;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids){
            weighingDao.deleteWeighing(id);
            return null;
        }
    }
}
