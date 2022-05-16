package com.example.wdgfarm_android.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.wdgfarm_android.model.Company;

@Database(entities = {Company.class}, version = 1)
public abstract class CompanyDatabase extends RoomDatabase {
    private static CompanyDatabase instance;

    public abstract CompanyDao companyDao();

    public static synchronized CompanyDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CompanyDatabase.class, "company_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CompanyDao companyDao;

        private PopulateDbAsyncTask(CompanyDatabase db){
            companyDao = db.companyDao();
        }

        @Override
        protected Void doInBackground(Void... voids){

            return null;
        }
    }
}
