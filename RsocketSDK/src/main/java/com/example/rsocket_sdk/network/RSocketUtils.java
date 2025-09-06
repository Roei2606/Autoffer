package com.example.rsocket_sdk.network;

import com.example.core_models_sdk.models.FactoryUser;
import com.example.core_models_sdk.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.rsocket.Payload;
import io.rsocket.metadata.CompositeMetadataCodec;
import io.rsocket.metadata.RoutingMetadata;
import io.rsocket.metadata.TaggingMetadataCodec;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.util.DefaultPayload;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RSocketUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // ✅ תומך ב־LocalDateTime
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ✅ מונע תאריכים כ־long

    /**
     * בונה Payload תקני לפי Composite Metadata
     */
    public static Payload buildPayload(String route, Object data) {
        try {
            ByteBuf dataBuf;

            if (data instanceof String) {
                dataBuf = Unpooled.wrappedBuffer(((String) data).getBytes(StandardCharsets.UTF_8));
            } else if (data instanceof Enum<?>) {
                dataBuf = Unpooled.wrappedBuffer(((Enum<?>) data).name().getBytes(StandardCharsets.UTF_8));
            } else {
                String json = objectMapper.writeValueAsString(data);
                dataBuf = Unpooled.wrappedBuffer(json.getBytes(StandardCharsets.UTF_8));
            }

            RoutingMetadata routingMetadata = TaggingMetadataCodec.createRoutingMetadata(
                    ByteBufAllocator.DEFAULT,
                    Collections.singletonList(route)
            );

            CompositeByteBuf compositeMetadata = ByteBufAllocator.DEFAULT.compositeBuffer();

            CompositeMetadataCodec.encodeAndAddMetadata(
                    compositeMetadata,
                    ByteBufAllocator.DEFAULT,
                    WellKnownMimeType.MESSAGE_RSOCKET_ROUTING,
                    routingMetadata.getContent()
            );

            CompositeMetadataCodec.encodeAndAddMetadata(
                    compositeMetadata,
                    ByteBufAllocator.DEFAULT,
                    WellKnownMimeType.APPLICATION_JSON,
                    Unpooled.EMPTY_BUFFER
            );

            return DefaultPayload.create(dataBuf, compositeMetadata);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build RSocket payload for route: " + route, e);
        }
    }

    public static List<Byte> parsePayloadAsByteList(Payload payload) throws Exception {
        byte[] bytes = payload.getData().array();
        List<Byte> byteList = new ArrayList<>();
        for (byte b : bytes) byteList.add(b);
        return byteList;
    }


    /**
     * ממיר Payload חזרה לאובייקט
     */
    public static <T> T parsePayload(Payload payload, Class<T> type) {
        try {
            return objectMapper.readValue(payload.getDataUtf8(), type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse payload to " + type.getSimpleName(), e);
        } finally {
            payload.release();
        }
    }

    /**
     * פענוח חכם של יוזר לפי profileType
     */
    public static User parseUserPayloadWithTypeDetection(Payload payload) {
        try {
            String json = payload.getDataUtf8();
            JsonNode root = objectMapper.readTree(json);

            String type = root.has("profileType") ? root.get("profileType").asText() : "";

            if ("FACTORY".equalsIgnoreCase(type)) {
                return objectMapper.treeToValue(root, FactoryUser.class);
            } else {
                return objectMapper.treeToValue(root, User.class);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to dynamically parse User payload", e);
        } finally {
            payload.release();
        }
    }


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}