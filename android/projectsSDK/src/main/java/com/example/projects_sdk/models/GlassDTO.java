package com.example.projects_sdk.models;

public class GlassDTO {
    private String type;
    private double pricePerSquareMeter;


    public GlassDTO() {}

    public String getType() {
        return type;
    }

    public GlassDTO setType(String type) {
        this.type = type;
        return this;
    }

    public double getPricePerSquareMeter() {
        return pricePerSquareMeter;
    }

    public GlassDTO setPricePerSquareMeter(double pricePerSquareMeter) {
        this.pricePerSquareMeter = pricePerSquareMeter;
        return this;
    }
}
