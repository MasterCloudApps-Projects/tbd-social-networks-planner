package com.mastercloudapps.thesocialnetworkplanner.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TweetRepliesResponse {

    @JsonProperty("tweet_id")
    private String tweetId;

    @JsonProperty("user_replies")
    private List<TweetResponse> replies;

}
