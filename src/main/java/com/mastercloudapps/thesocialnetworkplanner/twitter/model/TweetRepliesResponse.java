package com.mastercloudapps.thesocialnetworkplanner.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TweetRepliesResponse {

    @JsonProperty("tweetId")
    private String tweetId;

    @JsonProperty("replies")
    private List<TweetResponse> replies;

}
