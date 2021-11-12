package com.example.simpleandroidapp;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private ImageButton imageButtonRefresh;
    private RecyclerView recyclerViewPhotos;
    private PhotosAdapter photosAdapter;
    private String updateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.mainLayout);
        imageButtonRefresh = findViewById(R.id.imageButtonRefresh);
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);

        updateMessage = getString(R.string.data_loaded);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this));
        photosAdapter = new PhotosAdapter(this, getLayoutInflater());
        recyclerViewPhotos.setAdapter(photosAdapter);

        updatePhotos();

        imageButtonRefresh.setOnClickListener(v -> {
            updateMessage = getString(R.string.data_updated);
            updatePhotos();
        });
    }

    private void updatePhotos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPhotoApi jsonPhotoApi = retrofit.create(JsonPhotoApi.class);

        Call<List<Photo>> call = jsonPhotoApi.getPhotos();

        Callback<List<Photo>> callbackPhotosLoaded = new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photosAdapter.setPhotos(response.body());
                    Snackbar.make(layout, updateMessage, 2000).show();
                } else
                    Snackbar.make(layout, getString(R.string.something_went_wrong_getting_photo_data_code, response.code()), 5000).show();
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Snackbar.make(layout, getString(R.string.something_went_wrong_getting_photo_data, t.getMessage()), 5000).show();
            }
        };

        call.enqueue(callbackPhotosLoaded);
    }
}