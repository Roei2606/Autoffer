//package com.example.local_project_sdk.models;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "items")
//public class LocalItemEntity {
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    @ColumnInfo(name = "project_id")
//    private String projectId;
//
//    @ColumnInfo(name = "item_number")
//    private int itemNumber;
//
//    @ColumnInfo(name = "profile_number")
//    private String profileNumber;
//
//    @ColumnInfo(name = "profile_type")
//    private String profileType; // שמרנו כ-String של enum
//
//    @ColumnInfo(name = "glass_type")
//    private String glassType;
//
//    @ColumnInfo(name = "height")
//    private double height;
//
//    @ColumnInfo(name = "width")
//    private double width;
//
//    @ColumnInfo(name = "quantity")
//    private int quantity;
//
//    @ColumnInfo(name = "is_expensive")
//    private boolean isExpensive;
//
//    @ColumnInfo(name = "location")
//    private String location;
//
//    // Constructors
//    public LocalItemEntity() {}
//
//    // Getters & Setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getProjectId() {
//        return projectId;
//    }
//
//    public void setProjectId(String projectId) {
//        this.projectId = projectId;
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
//    public String getProfileType() {
//        return profileType;
//    }
//
//    public void setProfileType(String profileType) {
//        this.profileType = profileType;
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
//
//    public boolean isExpensive() {
//        return isExpensive;
//    }
//
//    public void setIsExpensive(boolean isExpensive) {
//        this.isExpensive = isExpensive;
//    }
//}
package com.example.local_project_sdk.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.example.projects_sdk.models.AlumProfileDTO;
import com.example.projects_sdk.models.GlassDTO;

@Entity(tableName = "items")
public class LocalItemEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "project_id")
    private String projectId;

    @ColumnInfo(name = "item_number")
    private int itemNumber;

    @ColumnInfo(name = "profile_number")
    private String profileNumber;

    @ColumnInfo(name = "profile_type")
    private String profileType; // Enum as String (e.g. "DOOR_OPEN")

    @ColumnInfo(name = "glass_type")
    private String glassType;

    @ColumnInfo(name = "height")
    private double height;

    @ColumnInfo(name = "width")
    private double width;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "is_expensive")
    private boolean isExpensive;

    @ColumnInfo(name = "location")
    private String location;

    // 🧠 New: Full profile & glass objects for sending to server (not saved in Room)
    @Ignore
    private AlumProfileDTO profile;

    @Ignore
    private GlassDTO glass;

    // Constructors
    public LocalItemEntity() {}

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getProfileNumber() {
        return profileNumber;
    }

    public void setProfileNumber(String profileNumber) {
        this.profileNumber = profileNumber;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getGlassType() {
        return glassType;
    }

    public void setGlassType(String glassType) {
        this.glassType = glassType;
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

    public boolean isExpensive() {
        return isExpensive;
    }

    public void setIsExpensive(boolean isExpensive) {
        this.isExpensive = isExpensive;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
