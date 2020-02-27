package com.example.gallery.ui.gallery_home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.gallery.model.Photo;
import com.example.gallery.model.Photos;
import com.example.gallery.model.NetworkState;
import com.example.gallery.repository.PhotoRepository;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<Photos> mPhotos;
    private static final String TAG = "debug";
    private PhotoRepository photoRepository;

    public GalleryViewModel() {
        mPhotos = new MutableLiveData<>();
    }

    public void setPhotoRepository(Context context) {
        photoRepository = PhotoRepository.getInstance(context);
    }

    public LiveData<PagedList<Photo>> getPhotoesNew() {
        return photoRepository.getPhotosNew();
    }

    public LiveData<NetworkState> getNetworkState() {
        return photoRepository.getNetworkState();
    }
}