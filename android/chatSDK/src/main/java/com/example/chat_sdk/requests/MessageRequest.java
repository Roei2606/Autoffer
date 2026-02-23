package com.example.chat_sdk.requests;

import java.time.LocalDateTime;

public class MessageRequest {
    private String chatId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;

    public MessageRequest(String chatId, String senderId, String content) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

}