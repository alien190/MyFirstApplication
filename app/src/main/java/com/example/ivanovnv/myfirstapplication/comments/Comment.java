package com.example.ivanovnv.myfirstapplication.comments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment {

        @SerializedName("id")
        private int mId;
        @SerializedName("album_id")
        private int mAlbumId;
        @SerializedName("text")
        private String mText;
        @SerializedName("author")
        private String mAuthor;
        @SerializedName("timestamp")
        private String mTimestamp;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public int getAlbumId() {
            return mAlbumId;
        }

        public void setAlbumId(int albumId) {
            mAlbumId = albumId;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }

        public String getAuthor() {
            return mAuthor;
        }

        public void setAuthor(String author) {
            mAuthor = author;
        }

        public String getTimestamp() {
            return mTimestamp;
        }

        public void setTimestamp(String timestamp) {
            mTimestamp = timestamp;
        }

    public Comment(String mAuthor, String mText, String mTimestamp) {
        this.mText = mText;
        this.mAuthor = mAuthor;
        this.mTimestamp = mTimestamp;
    }
}
