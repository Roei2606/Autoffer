package com.example.chat_sdk.models;

import java.util.List;
import java.util.Map;

public class Message {
    private String id;
    private String chatId;
    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;
    private List<String> readBy;

    // 🔥 קובץ
    private List<Byte> fileBytes;
    private String fileName;
    private String fileType;

    // ✅ סוג ההודעה
    private String type; // "TEXT", "FILE", "BOQ_REQUEST", ...

    // ✅ מידע נוסף
    private Map<String, String> metadata;

    public Message() {}

    public Message(String chatId, String senderId, String receiverId, String content, String timestamp) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // 🧾 Getters & Setters

    public String getId() {
        return id;
    }

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public Message setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public Message setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public Message setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Message setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }

    // 📎 שדות קובץ

    public List<Byte> getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(List<Byte> fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getType() {
        return type;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Message setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }
}
