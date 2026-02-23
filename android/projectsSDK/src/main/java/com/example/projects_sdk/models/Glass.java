package com.example.projects_sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Glass {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("supportedProfiles")
    private List<String> supportedProfiles;

    @JsonProperty("pricePerSquareMeter")
    private double pricePerSquareMeter;

    @JsonProperty("imageData")
    private List<Byte> imageData;

    public Glass() {}

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<Byte> getImageData() {
        return imageData;
    }

    public Glass setImageData(List<Byte> imageData) {
        this.imageData = imageData;
        return this;
    }

    public List<String> getSupportedProfiles() {
        return supportedProfiles;
    }

    public double getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }
}
