package com.example.chat_sdk.models;

import java.util.List;

public class Chat {
    private String id;
    private List<String> participants;
    private String lastMessage;
    private String lastMessageTimestamp;

    public Chat() {}

    public Chat(List<String> participants, String lastMessage, String lastMessageTimestamp) {
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    // getters and setters


    public String getId() {
        return id;
    }

    public Chat setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public Chat setParticipants(List<String> participants) {
        this.participants = participants;
        return this;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Chat setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public String getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public Chat setLastMessageTimestamp(String lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
        return this;
    }
}
