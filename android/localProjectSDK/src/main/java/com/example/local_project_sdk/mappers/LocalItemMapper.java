package com.example.local_project_sdk.mappers;


import com.example.local_project_sdk.models.LocalItemEntity;
import com.example.projects_sdk.models.AlumProfile;
import com.example.projects_sdk.models.Glass;
import com.example.projects_sdk.models.ProjectItem;

public class LocalItemMapper {

    // 🔁 ממיר מ-ProjectItem (לוגיקה עסקית) ל-LocalItemEntity (Room)
    public static LocalItemEntity toEntity(ProjectItem item, String projectId) {
        LocalItemEntity entity = new LocalItemEntity();
        entity.setProjectId(projectId);
        entity.setItemNumber(item.getItemNumber());
        entity.setProfileNumber(item.getProfile().getProfileNumber());
        entity.setProfileType(item.getProfile().getUsageType());
        entity.setGlassType(item.getGlass().getType());
        entity.setHeight(item.getHeight());
        entity.setWidth(item.getWidth());
        entity.setQuantity(item.getQuantity());
        entity.setLocation(item.getLocation());
        return entity;
    }

    // 🔁 ממיר מ-LocalItemEntity (Room) ל-ProjectItem (לשליחה לשרת)
    public static ProjectItem toProjectItem(LocalItemEntity entity, AlumProfile profile, Glass glass) {
        ProjectItem item = new ProjectItem();
        item.setItemNumber(entity.getItemNumber());
        item.setProfile(profile);
        item.setGlass(glass);
        item.setHeight(entity.getHeight());
        item.setWidth(entity.getWidth());
        item.setQuantity(entity.getQuantity());
        item.setLocation(entity.getLocation());
        return item;
    }
}
