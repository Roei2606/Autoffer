package com.example.ads_sdk.models;

import com.example.core_models_sdk.models.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Ad {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("audience")
    private UserType profileType;


    public Ad() {
        // Empty constructor for Jackson
    }

    public Ad(String id, String title, String description, String imageUrl, UserType profileType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.profileType = profileType;
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserType getProfileType() {
        return profileType;
    }

    public void setProfileType(UserType profileType) {
        this.profileType = profileType;
    }
}
