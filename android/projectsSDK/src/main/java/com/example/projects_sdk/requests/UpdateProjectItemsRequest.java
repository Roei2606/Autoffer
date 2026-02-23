package com.example.projects_sdk.requests;


import com.example.projects_sdk.models.ProjectItem;

import java.util.List;

public class UpdateProjectItemsRequest {
    private String projectId;
    private List<ProjectItem> items;

    public UpdateProjectItemsRequest(String projectId, List<ProjectItem> items) {
        this.projectId = projectId;
        this.items = items;
    }

    public String getProjectId() {
        return projectId;
    }

    public List<ProjectItem> getItems() {
        return items;
    }
}
