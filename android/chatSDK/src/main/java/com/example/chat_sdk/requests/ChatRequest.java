package com.example.chat_sdk.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {

    @JsonProperty("currentUserId")
    private String currentUserId;

    @JsonProperty("otherUserId")
    private String otherUserId;

    public ChatRequest() {
    }

    public ChatRequest(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    @Override
    public String toString() {
        return "ChatRequest{currentUserId='" + currentUserId + "', otherUserId='" + otherUserId + "'}";
    }
}
