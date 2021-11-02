package com.mastercloudapps.thesocialnetworkplanner.twitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTweetRequest extends TweetRequest{

    @NotBlank
    private String publishDate;

    private Date publishDateStore;
}
