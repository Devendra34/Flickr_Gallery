package com.example.gallery.repository.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gallery.model.Photo;
import com.example.gallery.repository.db.paging.DBPhotoDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Photo.class}, version = 1)
public abstract class PhotoDatabase extends RoomDatabase {

    private static PhotoDatabase instance;
    public abstract PhotoDao photoDao();
    private LiveData<PagedList<Photo>> photoPaged;

    public static synchronized PhotoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PhotoDatabase.class,"photo_database")
                    .fallbackToDestructiveMigration()
                    .build();
            instance.init();
        }
        return instance;
    }
    private void init() {
        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(20)
                .build();


        Executor executor = Executors.newFixedThreadPool(3);
        DBPhotoDataSourceFactory dataSourceFactory = new DBPhotoDataSourceFactory(photoDao());
        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory,config);
        photoPaged = livePagedListBuilder.setFetchExecutor(executor).build();

    }
    public LiveData<PagedList<Photo>> getPhotoPaged() {
        return photoPaged;
    }
}
