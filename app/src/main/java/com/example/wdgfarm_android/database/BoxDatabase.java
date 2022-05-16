package com.example.wdgfarm_android.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.wdgfarm_android.model.Box;

@Database(entities = {Box.class}, version = 1)
public abstract class BoxDatabase extends RoomDatabase {
    private static BoxDatabase instance;

    public abstract BoxDao boxDao();

    public static synchronized BoxDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BoxDatabase.class, "box_database")
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
        private BoxDao boxDao;

        private PopulateDbAsyncTask(BoxDatabase db){
            boxDao = db.boxDao();
        }

        @Override
        protected Void doInBackground(Void... voids){

            return null;
        }
    }
}
