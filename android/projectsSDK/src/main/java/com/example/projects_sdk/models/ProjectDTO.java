package com.example.projects_sdk.models;

import java.util.List;
import java.util.Map;

public class ProjectDTO {
    private String projectId;
    private String clientId;
    private String projectAddress;
    private List<ItemModelDTO> items;
    private List<String> factoryIds;
    private Map<String, String> quoteStatuses; // factoryId -> PENDING / ACCEPTED ...
    private Map<String, Quote> quotes; // ✅ factoryId -> Quote
    private List<Byte> boqPdf;
    private String createdAt;

    public ProjectDTO() {}

    public String getProjectId() {
        return projectId;
    }

    public ProjectDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public ProjectDTO setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public ProjectDTO setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
        return this;
    }

    public List<ItemModelDTO> getItems() {
        return items;
    }

    public ProjectDTO setItems(List<ItemModelDTO> items) {
        this.items = items;
        return this;
    }

    public List<String> getFactoryIds() {
        return factoryIds;
    }

    public ProjectDTO setFactoryIds(List<String> factoryIds) {
        this.factoryIds = factoryIds;
        return this;
    }

    public Map<String, String> getQuoteStatuses() {
        return quoteStatuses;
    }

    public ProjectDTO setQuoteStatuses(Map<String, String> quoteStatuses) {
        this.quoteStatuses = quoteStatuses;
        return this;
    }

    public Map<String, Quote> getQuotes() {
        return quotes;
    }

    public ProjectDTO setQuotes(Map<String, Quote> quotes) {
        this.quotes = quotes;
        return this;
    }

    public List<Byte> getBoqPdf() {
        return boqPdf;
    }

    public ProjectDTO setBoqPdf(List<Byte> boqPdf) {
        this.boqPdf = boqPdf;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ProjectDTO setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
