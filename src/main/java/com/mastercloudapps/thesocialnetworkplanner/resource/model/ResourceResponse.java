package com.mastercloudapps.thesocialnetworkplanner.resource.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ResourceResponse {

    private Long id;
    private String url;
    private Date creationDate;
    private Date deletedDate;
    private Long tweetId;
}
