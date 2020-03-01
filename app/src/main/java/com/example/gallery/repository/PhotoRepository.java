package com.example.gallery.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.example.gallery.model.NetworkState;
import com.example.gallery.model.Photo;
import com.example.gallery.repository.db.PhotoDatabase;
import com.example.gallery.repository.network.PhotoNetwork;
import com.example.gallery.repository.network.paging.NetPhotoDataSourceFactory;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PhotoRepository {
    private static final String TAG = "NewPhotoRepository";
    private static PhotoRepository instance;
    final private PhotoNetwork photoNetwork;
    final private PhotoDatabase database;
    final private MediatorLiveData liveDataMerger;

    @SuppressLint("CheckResult")
    private PhotoRepository(Context context, LiveData<String> observable) {
        photoNetwork = new PhotoNetwork(boundaryCallback,observable);
        database = PhotoDatabase.getInstance(context);
        liveDataMerger = new MediatorLiveData<>();
        liveDataMerger.addSource(photoNetwork.getPhotosPaged(),value->{
            liveDataMerger.setValue(value);
            Log.d(TAG, "NewPhotoRepository: "+value.toString());
        });

        NetPhotoDataSourceFactory.getPhotos()
                .observeOn(Schedulers.io())
                .subscribe(photo -> {
                    database.photoDao().insertInDb(photo);
                });
    }

    private PagedList.BoundaryCallback<Photo> boundaryCallback = new PagedList.BoundaryCallback<Photo>() {
        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            liveDataMerger.addSource(database.getPhotoPaged(),value ->{
                liveDataMerger.setValue(value);
                liveDataMerger.removeSource(database.getPhotoPaged());
            });
        }
    };

    public static PhotoRepository getInstance(Context context, LiveData<String> observable) {
        if (instance == null) {
            instance = new PhotoRepository(context, observable);
        }
        return instance;
    }
    public LiveData<PagedList<Photo>> getPhotosNew() {
        return liveDataMerger;
    }
    public LiveData<NetworkState> getNetworkState() {
        return photoNetwork.getNetworkState();
    }

}
