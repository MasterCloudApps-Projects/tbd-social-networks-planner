package com.mastercloudapps.thesocialnetworkplanner.api.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestTokenResponse {
    @JsonProperty("visit_this_url")
    private String url;
}
