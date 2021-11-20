package com.mastercloudapps.thesocialnetworkplanner.api.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class TweetRepliesResponse {

    @JsonProperty("tweet_id")
    private Long tweetId;

    @JsonProperty("user_replies")
    private List<TweetResponse> replies;

}
