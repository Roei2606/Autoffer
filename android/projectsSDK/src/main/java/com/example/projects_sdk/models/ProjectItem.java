package com.example.projects_sdk.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectItem {

    private int itemNumber;
    private AlumProfile profile;
    private Glass glass;
    private double height;
    private double width;
    private int quantity;
    @JsonProperty("location")
    private String location;


    public ProjectItem() {
    }

    // Copy constructor
    public ProjectItem(ProjectItem other) {
        this.itemNumber = other.itemNumber;
        this.profile = other.profile;
        this.glass = other.glass;
        this.height = other.height;
        this.width = other.width;
        this.quantity = other.quantity;
        this.location = other.location;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public AlumProfile getProfile() {
        return profile;
    }

    public Glass getGlass() {
        return glass;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setProfile(AlumProfile profile) {
        this.profile = profile;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // equals() method for comparing items (לבדוק שינויים)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProjectItem)) return false;
        ProjectItem other = (ProjectItem) obj;
        return this.itemNumber == other.itemNumber &&
                this.height == other.height &&
                this.width == other.width &&
                this.quantity == other.quantity &&
                this.location.equals(other.location);
    }

    public int getDimensions() {
        return (int) (height * width);
    }
}

