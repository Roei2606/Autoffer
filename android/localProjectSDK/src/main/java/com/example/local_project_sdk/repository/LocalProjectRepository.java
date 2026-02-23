package com.example.local_project_sdk.repository;

import android.content.Context;

import com.example.local_project_sdk.db.LocalItemDao;
import com.example.local_project_sdk.db.LocalProjectDao;
import com.example.local_project_sdk.db.LocalProjectDatabase;
import com.example.local_project_sdk.models.LocalProjectEntity;
import com.example.local_project_sdk.models.LocalItemEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalProjectRepository {

    private final LocalProjectDao projectDao;
    private final LocalItemDao itemDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LocalProjectRepository(Context context) {
        LocalProjectDatabase db = LocalProjectDatabase.getInstance(context);
        this.projectDao = db.projectDao();
        this.itemDao = db.itemDao();
    }

    public void insertProject(LocalProjectEntity project) {
        executor.execute(() -> projectDao.insert(project));
    }

    public void insertItems(List<LocalItemEntity> items) {
        executor.execute(() -> itemDao.insertAll(items));
    }

    public void insertProjectWithItems(LocalProjectEntity project, List<LocalItemEntity> items) {
        executor.execute(() -> {
            projectDao.insert(project);
            itemDao.insertAll(items);
        });
    }

    public List<LocalProjectEntity> getAllProjects() {
        return projectDao.getAllProjects();
    }

    public LocalProjectEntity getProjectById(String projectId) {
        return projectDao.getProjectById(projectId);
    }

    public List<LocalItemEntity> getItemsForProject(String projectId) {
        return itemDao.getItemsForProject(projectId);
    }

    public void deleteProjectWithItems(String projectId) {
        executor.execute(() -> {
            LocalProjectEntity project = projectDao.getProjectById(projectId);
            if (project != null) {
                itemDao.deleteItemsByProjectId(projectId);
                projectDao.delete(project);
            }
        });
    }

    public void updateProject(LocalProjectEntity project) {
        executor.execute(() -> projectDao.update(project));
    }

    public void updateItem(LocalItemEntity item) {
        executor.execute(() -> itemDao.update(item));
    }
}

