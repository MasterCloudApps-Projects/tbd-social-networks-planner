package com.mastercloudapps.thesocialnetworkplanner.twitter.model;

import com.mastercloudapps.thesocialnetworkplanner.resource.model.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tweet")
    private List<Resource> resource;
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

