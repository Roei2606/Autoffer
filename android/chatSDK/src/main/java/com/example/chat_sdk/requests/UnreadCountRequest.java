package com.example.chat_sdk.requests;

public class UnreadCountRequest {
    private String chatId;
    private String userId;

    // 👇 נדרש עבור Jackson
    public UnreadCountRequest() {}

    public UnreadCountRequest(String chatId, String userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public UnreadCountRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public UnreadCountRequest setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }
}
