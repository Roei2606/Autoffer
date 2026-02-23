package com.example.chat_sdk.requests;

public class UserChatsRequest {
    private String userId;
    private int page;
    private int size;

    public UserChatsRequest() {} // נדרש ל-Jackson

    public UserChatsRequest(String userId, int page, int size) {
        this.userId = userId;
        this.page = page;
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
