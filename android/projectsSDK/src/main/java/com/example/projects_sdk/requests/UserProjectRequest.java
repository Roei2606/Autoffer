// package: com.example.projects_sdk.requests

package com.example.projects_sdk.requests;

import com.example.core_models_sdk.models.UserType;

public class UserProjectRequest {
    private String userId;
    private UserType profileType;

    public UserProjectRequest() {
    }

    public UserProjectRequest(String userId, UserType profileType) {
        this.userId = userId;
        this.profileType = profileType;
    }

    public String getUserId() {
        return userId;
    }

    public UserType getProfileType() {
        return profileType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProfileType(UserType profileType) {
        this.profileType = profileType;
    }
}
