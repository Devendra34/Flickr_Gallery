package com.example.gallery.ui.gallery_home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gallery.model.Data;
import com.example.gallery.model.Example;
import com.example.gallery.model.Photos;
import com.example.gallery.repository.FlickrApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<Photos> mPhotos;
    private static final String TAG = "debug";

    public GalleryViewModel() {
        mPhotos = new MutableLiveData<>();
        setmPhotos();
    }

    LiveData<Photos> getPhotos() {
        return mPhotos;
    }

    private void setmPhotos(){
        Call<Example> exampleCall = FlickrApi.getService().getPostList();
        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Unsuccessful code "+response.code());
                    return;
                }
                Example example = response.body();
                mPhotos.setValue(example.getPhotos());
                Data.photos = example.getPhotos();
                Log.d(TAG, "onResponse: "+example.getPhotos().getPhoto().size());
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed "+t.toString());
            }
        });
    }
}