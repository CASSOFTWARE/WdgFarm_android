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
        new DeleteWeighingAsyncTask(weighingDao).execute(weighing);
    }
    

    public void deleteAllWeighings(){
        new DeleteAllWeighingsAsyncTask(weighingDao).execute();
    }

    public LiveData<List<Weighing>> getAllWeighings(){
        return allWeighings;
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
    private static class DeleteWeighingAsyncTask extends AsyncTask<Weighing, Void, Void> {
        private WeighingDao weighingDao;

        private DeleteWeighingAsyncTask(WeighingDao weighingDao){
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
}
