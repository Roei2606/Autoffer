//package com.example.chatsdk.network;
//
//import com.example.chatsdk.models.Message;
//import com.example.chatsdk.utils.RSocketUtils;
//import io.rsocket.Payload;
//import io.rsocket.RSocket;
//
//import java.util.concurrent.CompletableFuture;
//
//public class MessageManager {
//
//    private final RSocket rSocket;
//
//    public MessageManager() {
//        this.rSocket = RSocketClientManager.getInstance().getRSocket();
//    }
//
//    public CompletableFuture<Message> sendMessage(Message message) {
//        try {
//            Payload payload = RSocketUtils.buildPayload("messages.send", message);
//
//            return rSocket.requestResponse(payload)
//                    .map(response -> RSocketUtils.parsePayload(response, Message.class))
//                    .toFuture();
//
//        } catch (Exception e) {
//            CompletableFuture<Message> future = new CompletableFuture<>();
//            future.completeExceptionally(e);
//            return future;
//        }
//    }
//}
package com.example.chat_sdk.network;

import android.util.Log;

import com.example.chat_sdk.models.Message;
import com.example.chat_sdk.requests.FileMessageRequest;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.RSocketUtils;


import io.rsocket.Payload;
import io.rsocket.RSocket;

import java.util.concurrent.CompletableFuture;

public class MessageManager {

    private final RSocket rSocket;

    public MessageManager() {
        this.rSocket = RSocketClientManager.getInstance().getRSocket();
    }

    public CompletableFuture<Message> sendMessage(Message message) {
        if (rSocket == null || rSocket.isDisposed()) {
            Log.e("MessageManager", "RSocket is not connected");
            CompletableFuture<Message> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new IllegalStateException("RSocket is not connected"));
            return failedFuture;
        }

        try {
            Payload payload = RSocketUtils.buildPayload("messages.send", message);

            return rSocket.requestResponse(payload)
                    .map(response -> {
                        Log.d("MessageManager", "Message sent successfully");
                        return RSocketUtils.parsePayload(response, Message.class);
                    })
                    .toFuture();

        } catch (Exception e) {
            Log.e("MessageManager", "Error sending message", e);
            CompletableFuture<Message> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> sendFileMessage(FileMessageRequest request) {
        if (rSocket == null || rSocket.isDisposed()) {
            Log.e("MessageManager", "RSocket is not connected");
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new IllegalStateException("RSocket is not connected"));
            return failedFuture;
        }

        try {
            Payload payload = RSocketUtils.buildPayload("chat.send.file", request);

            return rSocket.requestResponse(payload)
                    .then()
                    .toFuture();

        } catch (Exception e) {
            Log.e("MessageManager", "Error sending file message", e);
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

}
