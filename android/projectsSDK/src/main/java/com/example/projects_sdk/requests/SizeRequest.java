package com.example.projects_sdk.requests;

public class SizeRequest {
    private int width;
    private int height;

    public SizeRequest(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
