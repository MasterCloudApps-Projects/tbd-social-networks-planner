package com.mastercloudapps.thesocialnetworkplanner.api.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mastercloudapps.thesocialnetworkplanner.common.ToJsonString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenRequest extends ToJsonString {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("code")
    private String code;
}
