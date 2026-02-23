package com.example.projects_sdk.requests;


import java.util.List;

public class SendBOQRequest {
    private String projectId;
    private List<String> factoryIds;

    public SendBOQRequest(){}

    public SendBOQRequest(String projectId, List<String> factoryIds) {
        this.projectId = projectId;
        this.factoryIds = factoryIds;
    }

    public String getProjectId() {
        return projectId;
    }

    public List<String> getFactoryIds() {
        return factoryIds;
    }
}

