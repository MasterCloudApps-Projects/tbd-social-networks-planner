package com.mastercloudapps.thesocialnetworkplanner.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginCallbackResponse {
    @JsonProperty("accountId")
    private String accountId;
}
