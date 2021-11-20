package com.mastercloudapps.thesocialnetworkplanner.api.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mastercloudapps.thesocialnetworkplanner.common.ToJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLoginRequest extends ToJsonString {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("scope")
    private String scope;
}
