package com.mastercloudapps.thesocialnetworkplanner.twitter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long twitterId;
    private String text;
    private String resourceUrl;
    private String username;
    private Date scheduledDate;
    private Date creationDate;
    private Date updateDate;
    private Date deletedDate;

    public TweetResponse toTweetResponse() {
        return TweetResponse.builder().id(this.id).twitterId(this.twitterId).username(this.username).text(this.text).scheduledDate(scheduledDate).build();
    }

    public void delete() {
        this.deletedDate = new Date();
    }
}

