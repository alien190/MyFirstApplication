package com.example.ivanovnv.myfirstapplication.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.ivanovnv.myfirstapplication.model.Album;

@Database(entities = {Album.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
