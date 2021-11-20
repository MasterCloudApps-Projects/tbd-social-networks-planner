package com.mastercloudapps.thesocialnetworkplanner.api.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class TweetResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("twitterId")
    private Long twitterId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("text")
    private String text;

    @JsonProperty("scheduledDate")
    private Date scheduledDate;
}
