package com.example.gallery.repository.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gallery.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInDb(Photo photo);

    @Update
    void updateInDb(Photo photo);

    @Delete
    void deleteInDb(Photo photo);

    @Query("DELETE FROM photos_table")
    void deleteAllInDb();

    @Query("SELECT * FROM photos_table")
    LiveData<List<Photo>> getAllPhotosFromDb();

    @Query("SELECT * FROM photos_table")
    List<Photo> getAllPhotosDb();
}
