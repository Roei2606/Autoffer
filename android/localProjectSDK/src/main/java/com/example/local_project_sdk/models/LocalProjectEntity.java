package com.example.local_project_sdk.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * מודל מקומי (Room) של פרויקט – נועד לאחסון זמני בזיכרון המקומי בלבד.
 */
@Entity(tableName = "projects")
public class LocalProjectEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id; // UUID או מזהה זמני

    @ColumnInfo(name = "client_id")
    private String clientId;

    @ColumnInfo(name = "project_address")
    private String projectAddress;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    public LocalProjectEntity() {
    }

    public LocalProjectEntity(@NonNull String id, String clientId, String projectAddress, String createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.projectAddress = projectAddress;
        this.createdAt = createdAt;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
