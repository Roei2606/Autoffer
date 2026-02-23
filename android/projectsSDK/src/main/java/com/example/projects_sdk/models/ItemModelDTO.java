//package com.example.projects_sdk.models;
//
//public class ProjectItemDto {
//
//    private int itemNumber;
//    private String profileNumber;
//    private String glassType;
//    private double height;
//    private double width;
//    private int quantity;
//    private String location;
//
//    public ProjectItemDto() {
//        // Default constructor for Jackson
//    }
//
//    public ProjectItemDto(int itemNumber, String profileNumber, String glassType,
//                          double height, double width, int quantity, String location) {
//        this.itemNumber = itemNumber;
//        this.profileNumber = profileNumber;
//        this.glassType = glassType;
//        this.height = height;
//        this.width = width;
//        this.quantity = quantity;
//        this.location = location;
//    }
//
//    public int getItemNumber() {
//        return itemNumber;
//    }
//
//    public void setItemNumber(int itemNumber) {
//        this.itemNumber = itemNumber;
//    }
//
//    public String getProfileNumber() {
//        return profileNumber;
//    }
//
//    public void setProfileNumber(String profileNumber) {
//        this.profileNumber = profileNumber;
//    }
//
//    public String getGlassType() {
//        return glassType;
//    }
//
//    public void setGlassType(String glassType) {
//        this.glassType = glassType;
//    }
//
//    public double getHeight() {
//        return height;
//    }
//
//    public void setHeight(double height) {
//        this.height = height;
//    }
//
//    public double getWidth() {
//        return width;
//    }
//
//    public void setWidth(double width) {
//        this.width = width;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//}
package com.example.projects_sdk.models;

public class ItemModelDTO {

    private int itemNumber;
    private AlumProfileDTO profile;
    private GlassDTO glass;
    private double height;
    private double width;
    private int quantity;
    private String location;

    public ItemModelDTO() {
        // Default constructor for Jackson
    }

    public ItemModelDTO(int itemNumber, AlumProfileDTO profile, GlassDTO glass,
                        double height, double width, int quantity, String location) {
        this.itemNumber = itemNumber;
        this.profile = profile;
        this.glass = glass;
        this.height = height;
        this.width = width;
        this.quantity = quantity;
        this.location = location;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public AlumProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(AlumProfileDTO profile) {
        this.profile = profile;
    }

    public GlassDTO getGlass() {
        return glass;
    }

    public void setGlass(GlassDTO glass) {
        this.glass = glass;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
