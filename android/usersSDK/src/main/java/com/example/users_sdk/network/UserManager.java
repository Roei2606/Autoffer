package com.example.users_sdk.network;

import android.util.Log;


import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.RSocketUtils;
import com.example.users_sdk.requests.LoginRequest;
import com.example.users_sdk.requests.RegisterUserRequest;
import com.example.users_sdk.requests.ResetPasswordRequest;
import com.example.users_sdk.requests.UserIdRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.rsocket.Payload;
import io.rsocket.RSocket;

public class UserManager {

    private final RSocketClientManager rSocketClientManager;
    private static final String TAG = "UserManager";

    public UserManager() {
        this.rSocketClientManager = RSocketClientManager.getInstance();
    }

    private RSocket getActiveRSocket() {
        RSocket rSocket = rSocketClientManager.getOrConnect();
        if (rSocket == null || rSocket.isDisposed()) {
            throw new IllegalStateException("RSocket connection is not established");
        }
        return rSocket;
    }

    public CompletableFuture<User> loginUser(String email, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();

        try {
            LoginRequest request = new LoginRequest(email, password);
            Payload payload = RSocketUtils.buildPayload("users.login", request);

            getActiveRSocket().requestResponse(payload)
                    .map(response -> RSocketUtils.parsePayload(response, User.class))
                    .subscribe(
                            future::complete,
                            error -> {
                                Log.e(TAG, "Login Error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            });

        } catch (Exception e) {
            Log.e(TAG, "RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    public CompletableFuture<User> registerUser(RegisterUserRequest request) {
        CompletableFuture<User> future = new CompletableFuture<>();

        try {
            Payload payload = RSocketUtils.buildPayload("users.register", request);

            getActiveRSocket().requestResponse(payload)
                    .map(response -> RSocketUtils.parsePayload(response, User.class))
                    .subscribe(
                            future::complete,
                            error -> {
                                Log.e(TAG, "Register Error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            });

        } catch (Exception e) {
            Log.e(TAG, "Register RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    public CompletableFuture<List<User>> getAllUsers() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        try {
            Payload payload = RSocketUtils.buildPayload("users.getAll", "");

            getActiveRSocket().requestStream(payload)
                    .collectList()
                    .map(responses -> {
                        List<User> users = new ArrayList<>();
                        for (Payload response : responses) {
                            users.add(RSocketUtils.parseUserPayloadWithTypeDetection(response));
                        }
                        return users;
                    })
                    .subscribe(
                            future::complete,
                            error -> {
                                Log.e(TAG, "GetAllUsers Error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            });

        } catch (Exception e) {
            Log.e(TAG, "RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    public CompletableFuture<User> getUserById(String userId) {
        try {
            UserIdRequest request = new UserIdRequest(userId);
            Payload payload = RSocketUtils.buildPayload("users.getById", request);
            return getActiveRSocket()
                    .requestResponse(payload)
                    .map(RSocketUtils::parseUserPayloadWithTypeDetection)
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<User> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> resetPassword(ResetPasswordRequest request) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            Payload payload = RSocketUtils.buildPayload("users.resetPassword", request);

            getActiveRSocket().requestResponse(payload)
                    .subscribe(
                            response -> future.complete(null),
                            error -> {
                                Log.e(TAG, "Reset password error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            });

        } catch (Exception e) {
            Log.e(TAG, "Reset password RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    public CompletableFuture<List<User>> getUsersByType(UserType userType) {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        try {
            Payload payload = RSocketUtils.buildPayload("users.getByType", userType.name());

            getActiveRSocket().requestStream(payload)
                    .collectList()
                    .map(responses -> {
                        List<User> users = new ArrayList<>();
                        for (Payload response : responses) {
                            users.add(RSocketUtils.parseUserPayloadWithTypeDetection(response));
                        }
                        return users;
                    })
                    .subscribe(
                            future::complete,
                            error -> {
                                Log.e(TAG, "getUsersByType Error: " + error.getMessage(), error);
                                future.completeExceptionally(error);
                            });

        } catch (Exception e) {
            Log.e(TAG, "getUsersByType RSocket Request Failed: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }
}
