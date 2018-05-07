package com.example.ivanovnv.myfirstapplication.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Song {

        @SerializedName("id")
        @ColumnInfo(name = "id")
        @PrimaryKey
        private int mId;

        @SerializedName("name")
        @ColumnInfo(name = "name")
        private String mName;

        @SerializedName("duration")
        @ColumnInfo(name = "duration")
        private String mDuration;

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

        public String getDuration() {
            return mDuration;
        }

        public void setDuration(String duration) {
            mDuration = duration;
        }
}
