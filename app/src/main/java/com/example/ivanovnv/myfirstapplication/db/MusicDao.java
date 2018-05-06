package com.example.ivanovnv.myfirstapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.example.ivanovnv.myfirstapplication.model.Album;

import java.util.Date;
import java.util.List;

@Dao
public interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Query("SELECT * FROM album")
    List<Album> getAlbums();

    @Delete
    void deleteAlbum(Album album);

    @Query("DELETE FROM album where id = :albumId")
    void deleteAlbumById(int albumId);

    @Query("SELECT * FROM album where id IN (:albums)")
    @TypeConverters({AlbumConverter.class})
    List<Album> getAlbumsByIds(List<Album> albums);
}

