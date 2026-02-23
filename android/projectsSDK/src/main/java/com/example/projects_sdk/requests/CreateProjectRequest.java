package com.example.projects_sdk.requests;


import com.example.projects_sdk.models.ItemModelDTO;

import java.util.List;

public class CreateProjectRequest {

    private String clientId;
    private String projectAddress;
    private List<ItemModelDTO> items;
    private List<String> factoryIds;


    public CreateProjectRequest() {}

    public CreateProjectRequest(String clientId, String projectAddress,
                                List<ItemModelDTO> items, List<String> factoryIds) {
        this.clientId = clientId;
        this.projectAddress = projectAddress;
        this.items = items;
        this.factoryIds = factoryIds;
    }

    // Getters & Setters...

    public String getClientId() {
        return clientId;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public List<ItemModelDTO> getItems() {
        return items;
    }

    public List<String> getFactoryIds() {
        return factoryIds;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public void setItems(List<ItemModelDTO> items) {
        this.items = items;
    }

    public void setFactoryIds(List<String> factoryIds) {
        this.factoryIds = factoryIds;
    }
}
