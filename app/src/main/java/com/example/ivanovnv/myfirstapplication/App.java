package com.example.ivanovnv.myfirstapplication;

import android.app.Application;
import android.arch.persistence.room.Room;


import com.example.ivanovnv.myfirstapplication.db.DataBase;


public class App extends Application {
    private DataBase mDataBase;

    @Override
    public void onCreate() {
        super.onCreate();

        mDataBase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "music_database_new_01")
                .allowMainThreadQueries()
                .build();
    }

    public DataBase getDataBase() {
        return mDataBase;
    }
}
