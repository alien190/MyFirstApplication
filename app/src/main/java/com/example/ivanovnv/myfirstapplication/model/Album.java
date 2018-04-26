package com.example.ivanovnv.myfirstapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album {

    /**
     * data : {"id":0,"name":"string","songs":[{"id":0,"name":"string","duration":"string"}],"release_date":"2018-04-26T14:50:58.754Z"}
     */

    @SerializedName("data")
    private DataBean mData;

    public DataBean getData() {
        return mData;
    }

    public void setData(DataBean data) {
        mData = data;
    }

    public static class DataBean {
        /**
         * id : 0
         * name : string
         * songs : [{"id":0,"name":"string","duration":"string"}]
         * release_date : 2018-04-26T14:50:58.754Z
         */

        @SerializedName("id")
        private int mId;
        @SerializedName("name")
        private String mName;
        @SerializedName("release_date")
        private String mReleaseDate;
        @SerializedName("songs")
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
}
