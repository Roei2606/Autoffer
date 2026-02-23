package com.example.local_project_sdk.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.local_project_sdk.models.LocalItemEntity;

import java.util.List;

@Dao
public interface LocalItemDao {

    @Insert
    void insert(LocalItemEntity item);

    @Insert
    void insertAll(List<LocalItemEntity> items);

    @Update
    void update(LocalItemEntity item);

    @Delete
    void delete(LocalItemEntity item);

    @Query("DELETE FROM items WHERE project_id = :projectId")
    void deleteItemsByProjectId(String projectId);

    @Query("SELECT * FROM items WHERE project_id = :projectId")
    List<LocalItemEntity> getItemsForProject(String projectId);
}

