package com.example.wdgfarm_android.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.wdgfarm_android.model.Weighing;

@Database(entities = {Weighing.class}, version = 1)
public abstract class WeighingDatabase extends RoomDatabase {
    private static WeighingDatabase instance;

    public abstract WeighingDao weighingDao();

    public static synchronized WeighingDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WeighingDatabase.class, "weighing_database")
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
        private WeighingDao weighingDao;

        private PopulateDbAsyncTask(WeighingDatabase db){
            weighingDao = db.weighingDao();
        }

        @Override
        protected Void doInBackground(Void... voids){

            return null;
        }
    }
}
