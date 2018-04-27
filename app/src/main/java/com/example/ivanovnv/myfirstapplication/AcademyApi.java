package com.example.ivanovnv.myfirstapplication;

import com.example.ivanovnv.myfirstapplication.model.Album;
import com.example.ivanovnv.myfirstapplication.model.Albums;
import com.example.ivanovnv.myfirstapplication.model.Song;
import com.example.ivanovnv.myfirstapplication.model.Songs;
import com.example.ivanovnv.myfirstapplication.model.User;
import com.example.ivanovnv.myfirstapplication.model.UserForRegistration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcademyApi {

    @POST("registration")
    Call<Void> registration(@Body UserForRegistration userForRegistration);

    @GET("user")
    Call<User> getUser();

    @GET("albums")
    Call<Albums> getAlbums();

    @GET("albums/{id}")
    Call<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Call<Songs> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);
}
