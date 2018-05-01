package com.example.ivanovnv.myfirstapplication;

import com.example.ivanovnv.myfirstapplication.model.Album;
import com.example.ivanovnv.myfirstapplication.model.Albums;
import com.example.ivanovnv.myfirstapplication.model.Song;
import com.example.ivanovnv.myfirstapplication.model.Songs;
import com.example.ivanovnv.myfirstapplication.model.User;
import com.example.ivanovnv.myfirstapplication.model.UserForRegistration;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcademyApi {

    @POST("registration")
    Completable registration(@Body UserForRegistration userForRegistration);

    @GET("user")
    Single<User> getUser();

    @GET("albums")
    Call<Albums> getAlbums();

    @GET("albums/{id}")
    Call<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Call<Songs> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);
}
