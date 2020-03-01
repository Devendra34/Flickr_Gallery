package com.example.gallery.repository.network.paging;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.gallery.model.Photo;
import com.example.gallery.model.NetworkState;

import io.reactivex.subjects.ReplaySubject;

public class NetPhotoDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<NetPhotoPagedKeyedDataSource> networkStatus;
    private NetPhotoPagedKeyedDataSource netPhotoPagedKeyedDataSource;

    public NetPhotoDataSourceFactory() {
        this.networkStatus = new MutableLiveData<>();
        netPhotoPagedKeyedDataSource = new NetPhotoPagedKeyedDataSource();
    }
    public NetPhotoDataSourceFactory(String query) {
        this.networkStatus = new MutableLiveData<>();
        netPhotoPagedKeyedDataSource = new NetPhotoPagedKeyedDataSource(query);
    }

    public static ReplaySubject<Photo> getPhotos() {
        return NetPhotoPagedKeyedDataSource.getPhotoObservable();
    }

    public static LiveData<NetworkState> getNetworkState() {
        return NetPhotoPagedKeyedDataSource.getNetworkState();
    }
    public LiveData<NetPhotoPagedKeyedDataSource> getNetworkStatus() {
        return networkStatus;
    }

    @NonNull
    @Override
    public DataSource.Factory map(@NonNull Function function) {
        return super.map(function);
    }

    @NonNull
    @Override
    public DataSource create() {
        networkStatus.postValue(netPhotoPagedKeyedDataSource);
        return netPhotoPagedKeyedDataSource;
    }
}
