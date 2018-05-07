package com.example.ivanovnv.myfirstapplication.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class Album implements Serializable{

        @SerializedName("id")
        @PrimaryKey
        @ColumnInfo(name = "id")
        private int mId;

        @SerializedName("name")
        @ColumnInfo(name = "name")
        private String mName;

        @SerializedName("release_date")
        @ColumnInfo(name = "release")

        private String mReleaseDate;

        @SerializedName("songs")
        @Ignore
        private List<Song> mSongs;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getReleaseDate() {
            return mReleaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            mReleaseDate = releaseDate;
        }

        public List<Song> getSongs() {
            return mSongs;
        }

        public void setSongs(List<Song> songs) {
            mSongs = songs;
        }

    }
