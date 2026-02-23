package com.example.projects_sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


//@JsonIgnoreProperties(value = "imageData", allowGetters = true)
public class AlumProfile {

    @JsonProperty("id")
    private String id;

    @JsonProperty("profileNumber")
    private String profileNumber;

    @JsonProperty("usageType")
    private String usageType; // Enum name: WINDOW_SLIDE, DOOR_OPEN, etc.

    @JsonProperty("minHeight")
    private int minHeight;

    @JsonProperty("maxHeight")
    private int maxHeight;

    @JsonProperty("minWidth")
    private int minWidth;

    @JsonProperty("maxWidth")
    private int maxWidth;

    @JsonProperty("isExpensive")
    private boolean isExpensive;

    @JsonProperty("recommendedGlassType")
    private String recommendedGlassType;

    @JsonProperty("pricePerSquareMeter")
    private double pricePerSquareMeter;

    @JsonProperty("imageData")
    private byte[] imageData;

    public AlumProfile() {}

    public String getId() {
        return id;
    }

    public String getProfileNumber() {
        return profileNumber;
    }

    public String getUsageType() {
        return usageType;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public boolean isExpensive() {
        return isExpensive;
    }

    public String getRecommendedGlassType() {
        return recommendedGlassType;
    }

    public double getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
