package com.example.chat_sdk.requests;

import java.util.List;

public class FileMessageRequest {
    private String chatId;
    private String senderId;
    private String receiverId;
    private List<Byte> fileBytes;
    private String fileName;
    private String fileType;
    private String timestamp;

    public FileMessageRequest() {}

    public FileMessageRequest(String chatId, String senderId, String receiverId,
                              List<Byte> fileBytes, String fileName,
                              String fileType, String timestamp) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.fileBytes = fileBytes;
        this.fileName = fileName;
        this.fileType = fileType;
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public FileMessageRequest setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public FileMessageRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public FileMessageRequest setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public List<Byte> getFileBytes() {
        return fileBytes;
    }

    public FileMessageRequest setFileBytes(List<Byte> fileBytes) {
        this.fileBytes = fileBytes;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileMessageRequest setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public FileMessageRequest setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public FileMessageRequest setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
