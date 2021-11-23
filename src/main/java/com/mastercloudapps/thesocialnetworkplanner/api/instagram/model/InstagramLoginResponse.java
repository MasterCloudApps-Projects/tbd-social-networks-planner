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
public class InstagramLoginResponse {
    @JsonProperty("visit_this_url")
    private String visitUrl;

    @JsonProperty("enter_this_code")
    private String enterCode;
}
