package com.mastercloudapps.thesocialnetworkplanner.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstagramPostInfoResponse {
    @JsonProperty("username")
    public String username;
    @JsonProperty("caption")
    public String caption;
    @JsonProperty("id")
    public String id;
    @JsonProperty("media_type")
    public String mediaType;
    @JsonProperty("media_url")
    public String mediaUrl;
    @JsonProperty("owner")
    public Owner owner;
    @JsonProperty("timestamp")
    public Date timestamp;
    @JsonProperty("like_count")
    public int likeCount;
}
