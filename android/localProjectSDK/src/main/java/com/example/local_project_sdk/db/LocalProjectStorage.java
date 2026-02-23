package com.example.local_project_sdk.db;

import com.example.local_project_sdk.models.LocalItemEntity;
import com.example.local_project_sdk.models.LocalProjectEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * מחלקת ניהול זמני לזיכרון – שומרת את הפרויקט הנוכחי ופריטים שנוספו
 * לא שומר בדאטאבייס! רק בזיכרון עד שהמשתמש שומר את הפרויקט.
 */
public class LocalProjectStorage {

    private static LocalProjectStorage instance;

    private LocalProjectEntity currentProject;
    private final List<LocalItemEntity> items = new ArrayList<>();

    private LocalProjectStorage() {}

    public static synchronized LocalProjectStorage getInstance() {
        if (instance == null) {
            instance = new LocalProjectStorage();
        }
        return instance;
    }

    public void setCurrentProject(LocalProjectEntity project) {
        this.currentProject = project;
        this.items.clear(); // איפוס פריטים בפרויקט חדש
    }

    public LocalProjectEntity getCurrentProject() {
        return currentProject;
    }

    public void addItemToCurrentProject(LocalItemEntity item) {
        items.add(item);
    }

    public void removeItem(LocalItemEntity item) {
        items.remove(item);
    }

    public List<LocalItemEntity> getCurrentItemEntities() {
        return new ArrayList<>(items);
    }

    public boolean hasProject() {
        return currentProject != null;
    }

    public void clear() {
        currentProject = null;
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public String getCurrentProjectAddress() {
        return currentProject != null ? currentProject.getProjectAddress() : null;
    }

    public void clearCurrentProject() {
        currentProject = null;
        items.clear();
    }
}
