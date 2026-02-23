package com.example.projects_sdk.models;

import java.util.List;
import java.util.Objects;

public class Quote {

    private String id;
    private String factoryId;
    private String projectId;
    private List<ItemModelDTO> pricedItems;
    private double factor;
    private double finalPrice;
    private List<Byte> quotePdf;
    private String status; // RECEIVED, CONFIRMED, DECLINED
    private String createdAt;

    public Quote() {
    }

    public Quote(String factoryId, String projectId, List<ItemModelDTO> pricedItems,
                 double factor, double finalPrice, List<Byte> quotePdf,
                 String status, String createdAt) {
        this.factoryId = factoryId;
        this.projectId = projectId;
        this.pricedItems = pricedItems;
        this.factor = factor;
        this.finalPrice = finalPrice;
        this.quotePdf = quotePdf;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<ItemModelDTO> getPricedItems() {
        return pricedItems;
    }

    public void setPricedItems(List<ItemModelDTO> pricedItems) {
        this.pricedItems = pricedItems;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public List<Byte> getQuotePdf() {
        return quotePdf;
    }

    public void setQuotePdf(List<Byte> quotePdf) {
        this.quotePdf = quotePdf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "factoryId='" + factoryId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", factor=" + factor +
                ", finalPrice=" + finalPrice +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quote)) return false;
        Quote quote = (Quote) o;
        return Objects.equals(factoryId, quote.factoryId) &&
                Objects.equals(projectId, quote.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factoryId, projectId);
    }
}
