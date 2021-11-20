package com.mastercloudapps.thesocialnetworkplanner.api.twitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetRequest {
    @JsonProperty("text")
    @Size(max = 280)
    @NotEmpty
    private String text;
}
