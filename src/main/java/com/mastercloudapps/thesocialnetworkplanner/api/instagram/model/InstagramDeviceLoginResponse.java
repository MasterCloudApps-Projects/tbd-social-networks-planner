package com.mastercloudapps.thesocialnetworkplanner.api.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstagramDeviceLoginResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("verification_uri")
    private String verificationUri;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("interval")
    private String interval;

}
