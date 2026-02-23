package com.example.projects_sdk.requests;

import com.example.projects_sdk.models.QuoteStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateFactoryStatusRequest {

    @JsonProperty("projectId")
    private String projectId;

    @JsonProperty("factoryId")
    private String factoryId;

    @JsonProperty("newStatus")
    private QuoteStatus newStatus;

    public UpdateFactoryStatusRequest() {
    }

    public UpdateFactoryStatusRequest(String projectId, String factoryId, QuoteStatus newStatus) {
        this.projectId = projectId;
        this.factoryId = factoryId;
        this.newStatus = newStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public QuoteStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(QuoteStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public String toString() {
        return "UpdateFactoryStatusRequest{" +
                "projectId='" + projectId + '\'' +
                ", factoryId='" + factoryId + '\'' +
                ", newStatus=" + newStatus +
                '}';
    }
}
