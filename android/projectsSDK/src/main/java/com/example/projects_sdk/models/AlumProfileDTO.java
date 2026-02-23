package com.example.projects_sdk.models;

public class AlumProfileDTO {
    private String profileNumber;
    private String usageType;
    private double pricePerSquareMeter;


    public AlumProfileDTO() {}


    public String getProfileNumber() {
        return profileNumber;
    }

    public AlumProfileDTO setProfileNumber(String profileNumber) {
        this.profileNumber = profileNumber;
        return this;
    }

    public String getUsageType() {
        return usageType;
    }

    public AlumProfileDTO setUsageType(String usageType) {
        this.usageType = usageType;
        return this;
    }

    public double getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public AlumProfileDTO setPricePerSquareMeter(double pricePerSquareMeter) {
        this.pricePerSquareMeter = pricePerSquareMeter;
        return this;
    }
}
