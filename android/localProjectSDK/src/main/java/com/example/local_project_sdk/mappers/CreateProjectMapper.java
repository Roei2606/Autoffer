//package com.example.local_project_sdk.mappers;
//
//import com.example.local_project_sdk.models.LocalItemEntity;
//import com.example.local_project_sdk.models.LocalProjectEntity;
//import com.example.projects_sdk.models.ProjectItemDto;
//import com.example.projects_sdk.requests.CreateProjectRequest;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CreateProjectMapper {
//
//    public static CreateProjectRequest toCreateRequest(
//            LocalProjectEntity localProject,
//            List<LocalItemEntity> localItems
//    ) {
//        List<ProjectItemDto> items = localItems.stream().map(entity -> new ProjectItemDto(
//                entity.getItemNumber(),
//                entity.getProfileNumber(),
//                entity.getGlassType(),
//                entity.getHeight(),
//                entity.getWidth(),
//                entity.getQuantity(),
//                entity.getLocation()
//        )).collect(Collectors.toList());
//
//        return new CreateProjectRequest(
//                localProject.getClientId(),
//                localProject.getProjectAddress(),
//                items,
//                List.of() // factoryIds תישלח בעתיד
//        );
//    }
//}
package com.example.local_project_sdk.mappers;

import com.example.local_project_sdk.models.LocalItemEntity;
import com.example.local_project_sdk.models.LocalProjectEntity;
import com.example.projects_sdk.models.ItemModelDTO;
import com.example.projects_sdk.requests.CreateProjectRequest;

import java.util.List;
import java.util.stream.Collectors;

public class CreateProjectMapper {

    public static CreateProjectRequest toCreateRequest(
            LocalProjectEntity localProject,
            List<LocalItemEntity> localItems
    ) {
        List<ItemModelDTO> items = localItems.stream()
                .map(entity -> new ItemModelDTO(
                        entity.getItemNumber(),
                        entity.getProfile(), // ✅ DTO מלא
                        entity.getGlass(),   // ✅ DTO מלא
                        entity.getHeight(),
                        entity.getWidth(),
                        entity.getQuantity(),
                        entity.getLocation()
                ))
                .collect(Collectors.toList());

        return new CreateProjectRequest(
                localProject.getClientId(),
                localProject.getProjectAddress(),
                items,
                List.of() // factoryIds תישלח בעתיד
        );
    }
}
