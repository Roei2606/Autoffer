package com.example.chat_sdk.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnreadCountResponse {

    @JsonProperty("chatId")
    private String chatId;

    @JsonProperty("count")
    private int count;

    // ✅ חובה כדי ש-Jackson יצליח לפרסר
    public UnreadCountResponse() {}

    public UnreadCountResponse(String chatId, int count) {
        this.chatId = chatId;
        this.count = count;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
