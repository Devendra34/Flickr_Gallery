package com.example.gallery.repository.network.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.gallery.model.Example;
import com.example.gallery.model.Photo;
import com.example.gallery.repository.network.api.FlickrApi;
import com.example.gallery.model.NetworkState;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.subjects.ReplaySubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetPhotoPagedKeyedDataSource extends PageKeyedDataSource<String, Photo> {

    private static final String TAG = "NetPhtoPageKeyDataSrc";
    private static final MutableLiveData networkState = new MutableLiveData();
    private static final ReplaySubject<Photo> photoObservable = ReplaySubject.create();
    private String query;

    public NetPhotoPagedKeyedDataSource() {

    }

    public NetPhotoPagedKeyedDataSource(String query) {
        this.query = query;
    }

    public static MutableLiveData getNetworkState() {
        return networkState;
    }

    public static ReplaySubject<Photo> getPhotoObservable() {
        return photoObservable;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, Photo> callback) {

        if (query == null) {
            query = "cat";
        }
        Call<Example> exampleCall = FlickrApi.getService().getPostList(2,query);
        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Unsuccessful code "+response.code());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    return;
                }

                if (response.body().getPhotos() == null){
                    return;
                }
                Log.d(TAG, "onResponse: Initial Loaded items = "+response.body().getPhotos().getPerpage());
                Example example = response.body();
                callback.onResult(example.getPhotos().getPhoto(),Integer.toString(2),Integer.toString(3));
                networkState.postValue(NetworkState.LOADED);
                for (Photo photo : example.getPhotos().getPhoto()) {
                    photoObservable.onNext(photo);
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onResponse: Failed "+t.toString());
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED,t.toString()));
                callback.onResult(new ArrayList<>(),Integer.toString(2),Integer.toString(3));
            }
        });
    }
    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Photo> callback) {
        final AtomicInteger page = new AtomicInteger();
        try {
            page.set(Integer.parseInt(params.key));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (page.get() > 3) {
            return;
        }
        if (query == null) {
            query = "cat";
        }
        networkState.postValue(NetworkState.LOADING);
        Log.d(TAG, "loadAfter: called\n");
        Call<Example> exampleCall = FlickrApi.getService().getPostList(page.get(),query);
        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.isSuccessful()) {
                    Example example = response.body();
                    callback.onResult(example.getPhotos().getPhoto(),Integer.toString(page.get()+1));
                    networkState.postValue(NetworkState.LOADED);
                    Log.d(TAG, "onResponse: Load After: "+example.getPhotos().getPerpage());
                    for (Photo photo : example.getPhotos().getPhoto()) {
                        photoObservable.onNext(photo);
                    }

                } else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED,response.message()));
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onFailure: FAiled from net to load more");
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED,t.toString()));
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Photo> callback) {

    }

}
