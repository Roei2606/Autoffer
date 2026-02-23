package com.example.chat_sdk.requests;

public class ChatMessagesRequest {
    private String chatId;
    private int page;
    private int size;

    public ChatMessagesRequest() {}

    public ChatMessagesRequest(String chatId, int page, int size) {
        this.chatId = chatId;
        this.page = page;
        this.size = size;
    }

    public String getChatId() {
        return chatId;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
