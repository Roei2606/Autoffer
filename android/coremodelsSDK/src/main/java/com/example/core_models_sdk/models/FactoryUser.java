package com.example.core_models_sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class FactoryUser extends User {

    @JsonProperty("businessId")
    private String businessId;

    @JsonProperty("factor")
    private Double factor;

    @JsonProperty("factoryName")
    private String factoryName;

    public FactoryUser() {
        super();
    }

    public FactoryUser(String id, String firstName, String lastName, String email,
                       String password, String phoneNumber, String address, UserType profileType,
                       LocalDateTime registeredAt, List<String> chats, byte[] photoBytes,
                       String factoryName, String businessId, Double factor) {

        super(id, firstName, lastName, email, password, phoneNumber, address,
                profileType, registeredAt, chats, photoBytes);

        this.factoryName = factoryName;
        this.businessId = businessId;
        this.factor = factor;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    @Override
    public String toString() {
        return "🏭 FactoryUser{" +
                "factoryName='" + factoryName + '\'' +
                ", businessId='" + businessId + '\'' +
                ", factor=" + factor +
                "} " + super.toString();
    }
}
