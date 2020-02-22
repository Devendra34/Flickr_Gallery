package com.example.gallery.ui.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gallery.model.Data;
import com.example.gallery.model.Example;
import com.example.gallery.model.Photo;
import com.example.gallery.model.Photos;
import com.example.gallery.repository.FlickrApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "debug";
    private MutableLiveData<String> query;
    private MutableLiveData<Photos> photoLiveData;
    private Photos photos;


    public SearchViewModel() {
        query = new MutableLiveData<>();
        photoLiveData = new MutableLiveData<>();
        if(Data.photos == null) {
            setPhotos();
        } else {
            photos = Data.photos;
        }
    }

    LiveData<Photos> getPhotoLiveData() {
        return photoLiveData;
    }

    void makeQuery(String query) {
        this.query.setValue(query);
        List<Photo> photoList = new ArrayList<>();

        if (photos == null || query == null || "".equals(query)) {
            photoList.clear();
            Log.d(TAG, "makeQuery: null");
        } else {
            for (Photo photo : photos.getPhoto()) {

                if("".equals(photo.getTitle()) || photo.getTitle() == null) {
                    continue;
                }
                if(photo.getTitle().toLowerCase().startsWith(query.toLowerCase())) {
                    photoList.add(photo);
                    Log.d(TAG, "makeQuery: added: "+photo.getTitle());
                } else {
                    Log.d(TAG, "makeQuery: different: "+query+" and: "+photo.getTitle());
                }
            }
        }
        Log.d(TAG, "makeQuery: \nQuery = "+query+"\n"+photoList.size());
        Photos photosNew = new Photos();
        photosNew.setPhoto(photoList);
        photoLiveData.setValue(photosNew);
    }

    private void setPhotos(){
        Call<Example> exampleCall = FlickrApi.getService().getPostList();
        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Unsuccessful code "+response.code());
                    return;
                }
                Example example = response.body();
                photos = example.getPhotos();

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed "+t.toString());
            }
        });
    }
}