package com.mastercloudapps.thesocialnetworkplanner.resource.model;

import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Tweet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private Date creationDate;
    private Date deletedDate;

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;
}
