package com.example.rsocket_sdk.network;

import android.util.Log;

import java.net.URI;
import java.time.Duration;
import java.util.List;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;

public class RSocketClientManager {
    private static final String TAG = "RSocketClientManager";
    private static RSocketClientManager instance;
    private RSocket rSocket;

    private RSocketClientManager() {}

    public static synchronized RSocketClientManager getInstance() {
        if (instance == null) instance = new RSocketClientManager();
        return instance;
    }

    public synchronized boolean connect() {
        if (isConnected()) return true;

        List<String> candidates = ServerConfig.getCandidateUrls();
        Log.i(TAG, "Candidate endpoints: " + candidates);

        for (String url : candidates) {
            try {
                URI uri = URI.create(url);
                Log.i(TAG, "Trying " + uri);

                ClientTransport transport = WebsocketClientTransport.create(uri);
                rSocket = RSocketConnector.create()
                        .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.getString())
                        .dataMimeType("application/json")
                        .keepAlive(Duration.ofSeconds(20), Duration.ofSeconds(90))
                        .connect(transport)
                        .block();

                if (rSocket != null && !rSocket.isDisposed()) {
                    Log.i(TAG, "✅ Connected to " + uri);
                    return true;
                }
            } catch (Exception e) {
                Log.w(TAG, "❌ Failed: " + url + " → " + e.getMessage());
            }
        }
        Log.e(TAG, "No endpoint reachable");
        return false;
    }

    public synchronized RSocket getOrConnect() {
        if (!isConnected() && !connect()) {
            throw new IllegalStateException("Unable to establish RSocket connection");
        }
        return rSocket;
    }

    public boolean isConnected() { return rSocket != null && !rSocket.isDisposed(); }

    public void disconnect() {
        if (rSocket != null && !rSocket.isDisposed()) {
            rSocket.dispose();
            rSocket = null;
            Log.i(TAG, "🔌 RSocket disconnected.");
        }
    }

    public RSocket getRSocket() { return rSocket; }
}