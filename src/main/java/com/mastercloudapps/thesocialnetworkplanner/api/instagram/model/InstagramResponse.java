package com.mastercloudapps.thesocialnetworkplanner.api.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class InstagramResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("instagramId")
    private Long instagramId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("url")
    private String url;

    @JsonProperty("scheduledDate")
    private Date scheduledDate;
}