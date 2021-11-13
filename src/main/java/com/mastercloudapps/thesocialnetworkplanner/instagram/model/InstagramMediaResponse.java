package com.mastercloudapps.thesocialnetworkplanner.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstagramMediaResponse {
    @JsonProperty("data")
    private List<InstagramImageIdResponse> postIds;
}
