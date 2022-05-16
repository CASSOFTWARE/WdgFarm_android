package com.example.wdgfarm_android.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wdgfarm_android.model.Box;

import java.util.List;

public class BoxRepository {
    private BoxDao boxDao;
    private LiveData<List<Box>> allBoxs;

    public BoxRepository(Application application){
        BoxDatabase database = BoxDatabase.getInstance(application);
        boxDao = database.boxDao();
        allBoxs = boxDao.getAllBoxs();
    }

    public void insert(Box box){
        new InsertBoxAsyncTask(boxDao).execute(box);
    }

    public void update(Box box){
        new UpdateBoxAsyncTask(boxDao).execute(box);
    }

    public void delete(Box box){
        new DeleteBoxAsyncTask(boxDao).execute(box);
    }

    public void deleteAllBoxs(){
        new DeleteAllBoxsAsyncTask(boxDao).execute();
    }

    public LiveData<List<Box>> getAllBoxs(){
        return allBoxs;
    }

    private static class InsertBoxAsyncTask extends AsyncTask<Box, Void, Void> {
        private BoxDao boxDao;

        private InsertBoxAsyncTask(BoxDao boxDao){
            this.boxDao = boxDao;
        }

        @Override
        protected Void doInBackground(Box... boxs){
            boxDao.insert(boxs[0]);
            return null;
        }
    }
    private static class UpdateBoxAsyncTask extends AsyncTask<Box, Void, Void> {
        private BoxDao boxDao;

        private UpdateBoxAsyncTask(BoxDao boxDao){
            this.boxDao = boxDao;
        }

        @Override
        protected Void doInBackground(Box... boxs){
            boxDao.update(boxs[0]);
            return null;
        }
    }
    private static class DeleteBoxAsyncTask extends AsyncTask<Box, Void, Void> {
        private BoxDao boxDao;

        private DeleteBoxAsyncTask(BoxDao boxDao){
            this.boxDao = boxDao;
        }

        @Override
        protected Void doInBackground(Box... boxs){
            boxDao.delete(boxs[0]);
            return null;
        }
    }

    private static class DeleteAllBoxsAsyncTask extends AsyncTask<Box, Void, Void> {
        private BoxDao boxDao;

        private DeleteAllBoxsAsyncTask(BoxDao boxDao){
            this.boxDao = boxDao;
        }

        @Override
        protected Void doInBackground(Box... voids){
            boxDao.deleteAllBoxs();
            return null;
        }
    }
}
