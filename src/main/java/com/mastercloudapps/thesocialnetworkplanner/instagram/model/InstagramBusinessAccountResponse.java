package com.mastercloudapps.thesocialnetworkplanner.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstagramBusinessAccountResponse {
    @JsonProperty("instagram_business_account")
    private InstagramBusinessAccount instagramBusinessAccount;
    @JsonProperty("id")
    private String pageId;
}
