package com.example.gallery.repository.db.paging;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.example.gallery.repository.db.PhotoDao;

public class DBPhotoDataSourceFactory extends DataSource .Factory{
    private DBPhotoPageKeyedDataSource dbPhotoPageKeyedDataSource;

    public DBPhotoDataSourceFactory(PhotoDao photoDao) {
        dbPhotoPageKeyedDataSource = new DBPhotoPageKeyedDataSource(photoDao);
    }

    @NonNull
    @Override
    public DataSource create() {
        return dbPhotoPageKeyedDataSource;
    }
}
