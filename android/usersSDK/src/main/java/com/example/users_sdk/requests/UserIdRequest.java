package com.example.users_sdk.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserIdRequest {

    @JsonProperty("userId")
    private String userId;

    public UserIdRequest() {
        // default constructor for Jackson
    }

    public UserIdRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
