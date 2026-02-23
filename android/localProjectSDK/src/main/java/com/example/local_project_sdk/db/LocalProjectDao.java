package com.example.local_project_sdk.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;


import com.example.local_project_sdk.models.LocalProjectEntity;

import java.util.List;

@Dao
public interface LocalProjectDao {

    @Insert
    void insert(LocalProjectEntity project);

    @Update
    void update(LocalProjectEntity project);

    @Delete
    void delete(LocalProjectEntity project);

    @Query("SELECT * FROM projects")
    List<LocalProjectEntity> getAllProjects();

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    LocalProjectEntity getProjectById(String projectId);
}

