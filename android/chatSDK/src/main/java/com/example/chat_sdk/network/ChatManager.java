package com.example.chat_sdk.network;

import android.util.Log;

import com.example.chat_sdk.models.Chat;
import com.example.chat_sdk.requests.ChatMessagesRequest;
import com.example.chat_sdk.requests.ChatRequest;
import com.example.chat_sdk.models.Message;
import com.example.chat_sdk.requests.UnreadCountRequest;
import com.example.chat_sdk.requests.UnreadCountResponse;
import com.example.chat_sdk.requests.UserChatsRequest;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.RSocketUtils;


import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChatManager {
    private final RSocketClientManager rSocketClientManager;
    private Disposable messageStreamDisposable;


    public ChatManager() {
        this.rSocketClientManager = RSocketClientManager.getInstance();
    }

    public CompletableFuture<List<Chat>> getUserChats(String userId, int page, int size) {
        try {
            UserChatsRequest request = new UserChatsRequest(userId, page, size);
            Payload payload = RSocketUtils.buildPayload("chats.getAll", request);

            return getActiveRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Chat.class))
                    .collectList()
                    .toFuture();

        } catch (Exception e) {
            CompletableFuture<List<Chat>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<List<Message>> getChatMessages(String chatId, int page, int size) {
        try {
            ChatMessagesRequest request = new ChatMessagesRequest(chatId, page, size);
            Payload payload = RSocketUtils.buildPayload("chats.getMessages", request);

            return getActiveRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Message.class))
                    .collectList()
                    .toFuture();

        } catch (Exception e) {
            CompletableFuture<List<Message>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Chat> getOrCreateChat(String user1Id, String user2Id) {
        try {
            ChatRequest request = new ChatRequest(user1Id, user2Id);
            Log.d("ChatManager", "Sending ChatRequest: " + request.toString());
            Payload payload = RSocketUtils.buildPayload("chats.getOrCreate", request);

            return getActiveRSocket()
                    .requestResponse(payload)
                    .map(p -> {
                        Log.d("ChatManager", "Received chat payload");
                        return RSocketUtils.parsePayload(p, Chat.class);
                    })
                    .toFuture();

        } catch (Exception e) {
            Log.e("ChatManager", "Error creating/opening chat", e);
            CompletableFuture<Chat> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public Flux<Message> streamMessages(String chatId) {
        try {
            Payload payload = RSocketUtils.buildPayload("chats.streamMessages", chatId);
            return getActiveRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Message.class));
        } catch (Exception e) {
            return Flux.error(e);
        }
    }

    public CompletableFuture<Integer> getUnreadCount(String chatId, String userId) {
        try {
            UnreadCountRequest request = new UnreadCountRequest(chatId, userId);
            Payload payload = RSocketUtils.buildPayload("chats.getUnreadCount", request);

            return getActiveRSocket()
                    .requestResponse(payload)
                    .map(p -> {
                        UnreadCountResponse response = RSocketUtils.parsePayload(p, UnreadCountResponse.class);
                        return response.getCount();
                    })
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<Integer> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Boolean> hasChats(String userId) {
        try {
            Payload payload = RSocketUtils.buildPayload("chats.hasChats", userId);

            return getActiveRSocket()
                    .requestResponse(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Boolean.class))
                    .toFuture();

        } catch (Exception e) {
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }



    public void disposeMessageStream() {
        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
            messageStreamDisposable.dispose();
        }
    }

    private RSocket getActiveRSocket() {
        RSocket rSocket = rSocketClientManager.getRSocket();
        if (rSocket == null || rSocket.isDisposed()) {
            throw new IllegalStateException("RSocket connection is not established");
        }
        return rSocket;
    }
}
