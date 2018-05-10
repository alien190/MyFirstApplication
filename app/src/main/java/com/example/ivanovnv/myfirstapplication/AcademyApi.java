package com.example.ivanovnv.myfirstapplication;

import com.example.ivanovnv.myfirstapplication.comments.Comment;
import com.example.ivanovnv.myfirstapplication.model.Album;
import com.example.ivanovnv.myfirstapplication.model.Song;
import com.example.ivanovnv.myfirstapplication.model.User;
import com.example.ivanovnv.myfirstapplication.model.UserForRegistration;

import java.util.List;

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
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Call<List<Song>> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);

    @GET("albums/{id}/comments")
    Single<List<Comment>> getAlbumComments(int id);
}
