package com.example.ivanovnv.myfirstapplication.comments;

public class CommentForPost {

    private String text;
    private int album_id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public CommentForPost(int album_id, String text) {
        this.text = text;
        this.album_id = album_id;
    }
}
