package com.example.projects_sdk.requests;

public class GetQuotePdfRequest {
    private final String projectId;
    private final String factoryId;

    public GetQuotePdfRequest(String projectId, String factoryId) {
        this.projectId = projectId;
        this.factoryId = factoryId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getFactoryId() {
        return factoryId;
    }
}