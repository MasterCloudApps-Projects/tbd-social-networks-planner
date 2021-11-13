package com.mastercloudapps.thesocialnetworkplanner.instagram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Owner {
    @JsonProperty("id")
    private String id;
}
