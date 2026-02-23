package com.example.ads_sdk.requests;

import com.example.core_models_sdk.models.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdsRequest {

    @JsonProperty("audience")
    private String audience;

    public AdsRequest() {}

    public AdsRequest(UserType profileType) {
        this.audience = profileType.name(); // נהפוך את ה־Enum לשם שלו במחרוזת
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(UserType profileType) {
        this.audience = profileType.name();
    }
}
