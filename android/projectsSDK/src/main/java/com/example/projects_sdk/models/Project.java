package com.example.projects_sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Project implements Serializable {

    private String id;
    private String clientId;
    private String projectAddress;
    private List<ProjectItem> items;
    private List<String> factoryIds;

    @JsonProperty("quoteStatuses")
    private Map<String, QuoteStatus> quoteStatuses;

    @JsonProperty("quotes") // factoryId -> Quote
    private Map<String, Quote> quotes;

    private String createdAt; // תאריך בפורמט String
    private List<Byte> boqPdf;

    public Project() {
    }

    public Project(String id, String clientId, String projectAddress, List<ProjectItem> items,
                   List<String> factoryIds, Map<String, QuoteStatus> quoteStatuses,
                   Map<String, Quote> quotes, String createdAt, List<Byte> boqPdf) {
        this.id = id;
        this.clientId = clientId;
        this.projectAddress = projectAddress;
        this.items = items;
        this.factoryIds = factoryIds;
        this.quoteStatuses = quoteStatuses;
        this.quotes = quotes;
        this.createdAt = createdAt;
        this.boqPdf = boqPdf;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public List<ProjectItem> getItems() {
        return items;
    }

    public List<String> getFactoryIds() {
        return factoryIds;
    }

    public Map<String, Quote> getQuotes() {
        return quotes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<Byte> getBoqPdf() {
        return boqPdf;
    }

    public Map<String, QuoteStatus> getQuoteStatuses() {
        return quoteStatuses;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public void setItems(List<ProjectItem> items) {
        this.items = items;
    }

    public void setFactoryIds(List<String> factoryIds) {
        this.factoryIds = factoryIds;
    }

    public void setQuotes(Map<String, Quote> quotes) {
        this.quotes = quotes;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setBoqPdf(List<Byte> boqPdf) {
        this.boqPdf = boqPdf;
    }

    public void setQuoteStatuses(Map<String, QuoteStatus> quoteStatuses) {
        this.quoteStatuses = quoteStatuses;
    }
}
