package com.example.ivanovnv.myfirstapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.ivanovnv.myfirstapplication.model.Album;
import com.example.ivanovnv.myfirstapplication.model.Song;

import java.util.List;

@Dao
public interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Query("SELECT * FROM album ORDER BY release ASC")
    List<Album> getAlbums();

    @Delete
    void deleteAlbum(Album album);

    @Query("DELETE FROM album where id = :albumId")
    void deleteAlbumById(int albumId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSongs(List<AlbumSong> albumSongs);

    @Query("DELETE FROM albumsong WHERE album_id = :albumId")
    void deleteAlbumSongs(int albumId);

    @Query("SELECT * FROM albumsong where album_id = :albumId")
    List<AlbumSong> getAlbumSongsByAlbumId(int albumId);

    @Query("SELECT * FROM song LEFT JOIN AlbumSong ON song_id = song.id WHERE album_id = :albumId")
    List<Song> getSongsByAlbumId(int albumId);

}

