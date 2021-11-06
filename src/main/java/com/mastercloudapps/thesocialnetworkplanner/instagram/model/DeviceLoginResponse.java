package com.mastercloudapps.thesocialnetworkplanner.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.UriAssert;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLoginResponse {

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
