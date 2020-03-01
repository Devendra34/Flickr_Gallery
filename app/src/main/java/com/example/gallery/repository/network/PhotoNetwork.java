package com.example.gallery.repository.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.gallery.model.Photo;
import com.example.gallery.model.NetworkState;
import com.example.gallery.repository.network.paging.NetPhotoDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoNetwork {
    private static final String TAG = "PhotoNetwork";
    private LiveData<PagedList<Photo>> photosPaged;
    final private LiveData<NetworkState> networkState;
    private PagedList.Config config;
    private PagedList.BoundaryCallback<Photo> boundaryCallback;

    private Executor executor;

    public PhotoNetwork(PagedList.BoundaryCallback<Photo> boundaryCallback, LiveData<String> liveData) {
        config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10).setPageSize(20).build();
        this.boundaryCallback = boundaryCallback;
        networkState = NetPhotoDataSourceFactory.getNetworkState();
        executor = Executors.newFixedThreadPool(3);

        photosPaged = Transformations.switchMap(liveData,input -> {
            LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(new NetPhotoDataSourceFactory(input), config);
            return livePagedListBuilder.setFetchExecutor(executor).setBoundaryCallback(boundaryCallback).build();
        });
    }


    public LiveData<PagedList<Photo>> getPhotosPaged() {
        return photosPaged;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

}
