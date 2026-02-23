package com.example.projects_sdk.network;

import com.example.projects_sdk.models.AlumProfile;
import com.example.projects_sdk.models.Glass;
import com.example.projects_sdk.models.ProjectDTO;
import com.example.projects_sdk.models.QuoteStatus;
import com.example.projects_sdk.requests.CreateProjectRequest;
import com.example.projects_sdk.requests.GetQuotePdfRequest;
import com.example.projects_sdk.requests.SendBOQRequest;
import com.example.projects_sdk.requests.SizeRequest;
import com.example.projects_sdk.requests.UpdateFactoryStatusRequest;
import com.example.projects_sdk.requests.UserProjectRequest;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.RSocketUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Mono;

public class ProjectManager {

    private final RSocketClientManager rSocketClientManager;

    public ProjectManager() {
        this.rSocketClientManager = RSocketClientManager.getInstance();
    }

    private RSocket getRSocket() {
        return rSocketClientManager.getOrConnect();
    }

    public CompletableFuture<List<ProjectDTO>> getUserProjects(UserProjectRequest request) {
        try {
            Payload payload = RSocketUtils.buildPayload("projects.getAllForUser", request);
            return getRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, ProjectDTO.class))
                    .collectList()
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<List<ProjectDTO>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> sendBOQToFactories(SendBOQRequest request) {
        try {
            Payload payload = RSocketUtils.buildPayload("projects.sendToFactories", request);
            return getRSocket()
                    .requestResponse(payload)
                    .then()
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> respondToBoqRequest(UpdateFactoryStatusRequest request) {
        try {
            Payload payload = RSocketUtils.buildPayload("projects.respondToBoqRequest", request);
            return getRSocket()
                    .requestResponse(payload)
                    .then()
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<Void> deleteProjectById(String projectId) {
        try {
            Payload payload = RSocketUtils.buildPayload("projects.delete", projectId);
            return getRSocket()
                    .requestResponse(payload)
                    .then()
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<List<AlumProfile>> getMatchingProfiles(int width, int height) {
        try {
            SizeRequest request = new SizeRequest(width, height);
            Payload payload = RSocketUtils.buildPayload("profiles.matchBySize", request);
            return getRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, AlumProfile.class))
                    .collectList()
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<List<AlumProfile>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<List<Glass>> getGlassesForProfile(String profileNumber) {
        try {
            Payload payload = RSocketUtils.buildPayload("glasses.getByProfile", profileNumber);
            return getRSocket()
                    .requestStream(payload)
                    .map(p -> RSocketUtils.parsePayload(p, Glass.class))
                    .collectList()
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<List<Glass>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public CompletableFuture<ProjectDTO> createProject(CreateProjectRequest request) {
        try {
            Payload payload = RSocketUtils.buildPayload("project.create", request);
            return getRSocket()
                    .requestResponse(payload)
                    .map(p -> RSocketUtils.parsePayload(p, ProjectDTO.class))
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<ProjectDTO> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public Mono<Void> updateFactoryStatus(String projectId, String factoryId, QuoteStatus newStatus) {
        UpdateFactoryStatusRequest request = new UpdateFactoryStatusRequest(projectId, factoryId, newStatus);
        try {
            Payload payload = RSocketUtils.buildPayload("projects.updateFactoryStatus", request);
            return rSocketClientManager.getRSocket()
                    .requestResponse(payload)
                    .then();
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public CompletableFuture<List<Byte>> getQuotePdf(String projectId, String factoryId) {
        try {
            GetQuotePdfRequest request = new GetQuotePdfRequest(projectId, factoryId);
            Payload payload = RSocketUtils.buildPayload("projects.getQuotePdfForFactory", request);

            return getRSocket()
                    .requestResponse(payload)
                    .map(p -> {
                        try {
                            return RSocketUtils.parsePayloadAsByteList(p);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse PDF payload", e);
                        }
                    })
                    .toFuture();
        } catch (Exception e) {
            CompletableFuture<List<Byte>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
}
