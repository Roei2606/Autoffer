package com.example.projects_sdk.models;

import com.google.gson.annotations.SerializedName;

public class MeasurementCamera {
    @SerializedName("width_mm")
    private double width;

    @SerializedName("height_mm")
    private double height;

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}