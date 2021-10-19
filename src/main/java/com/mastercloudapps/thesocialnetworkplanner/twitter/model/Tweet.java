package com.mastercloudapps.thesocialnetworkplanner.twitter.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long twitterId;
    private String text;
    private String username;
    private Date creationDate;
    private Date updateDate;
}
