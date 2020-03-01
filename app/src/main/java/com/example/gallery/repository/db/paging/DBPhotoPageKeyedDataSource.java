package com.example.gallery.repository.db.paging;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.gallery.model.Photo;
import com.example.gallery.model.Photos;
import com.example.gallery.repository.db.PhotoDao;

import java.util.List;

public class DBPhotoPageKeyedDataSource extends PageKeyedDataSource {

    private final PhotoDao photoDao;

    public DBPhotoPageKeyedDataSource(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback callback) {
        List<Photo> photoList = photoDao.getAllPhotosDb();
        if (photoList.size() != 0){
            callback.onResult(photoList,"0","1");
        }
        Photos photos = new Photos();
        photos.setPhoto(photoList);
    }

    @Override
    public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams params, @NonNull LoadCallback callback) {

    }
}
