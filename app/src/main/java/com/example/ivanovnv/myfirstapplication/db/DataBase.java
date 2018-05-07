package com.example.ivanovnv.myfirstapplication.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.ivanovnv.myfirstapplication.model.Album;
import com.example.ivanovnv.myfirstapplication.model.Song;

@Database(entities = {Album.class, Song.class, AlbumSong.class}, version = 4)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
