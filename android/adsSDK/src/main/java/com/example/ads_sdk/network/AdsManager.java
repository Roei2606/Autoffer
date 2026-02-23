package com.example.ads_sdk.network;

import com.example.ads_sdk.models.Ad;
import com.example.ads_sdk.requests.AdsRequest;
import com.example.core_models_sdk.models.UserType;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.RSocketUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Flux;

public class AdsManager {

    private final RSocket rSocket;

    public AdsManager() {
        this.rSocket = RSocketClientManager.getInstance().getRSocket();
    }

    // 🔥 שינוי חשוב: עכשיו מקבל ProfileType ולא String!
    public CompletableFuture<List<Ad>> getAdsForAudience(UserType profileType) {
        CompletableFuture<List<Ad>> future = new CompletableFuture<>();

        try {
            RSocket rSocket = getActiveRSocket();

            // 🔥 שולחים AdsRequest חדש עם ProfileType
            AdsRequest request = new AdsRequest(profileType);

            Payload payload = RSocketUtils.buildPayload(AdsRoutes.GET_ADS_FOR_AUDIENCE, request);

            Flux<Payload> responseStream = rSocket.requestStream(payload);

            responseStream.collectList()
                    .map(responses -> responses.stream()
                            .map(response -> RSocketUtils.parsePayload(response, Ad.class))
                            .collect(Collectors.toList())
                    )
                    .subscribe(future::complete, future::completeExceptionally);

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    private RSocket getActiveRSocket() {
        if (rSocket == null || rSocket.isDisposed()) {
            throw new IllegalStateException("RSocket connection is not established");
        }
        return rSocket;
    }
}
