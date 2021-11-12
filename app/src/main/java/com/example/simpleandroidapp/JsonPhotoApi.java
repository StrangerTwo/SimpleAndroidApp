package com.example.simpleandroidapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPhotoApi {

    @GET("photos")
    Call<List<Photo>> getPhotos();
}
