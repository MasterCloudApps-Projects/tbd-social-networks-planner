package com.mastercloudapps.thesocialnetworkplanner.api.twitter.model;

import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.Resource;
import com.mastercloudapps.thesocialnetworkplanner.api.schedule.Schedulable;
import com.mastercloudapps.thesocialnetworkplanner.api.schedule.Visitor;
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
public class Tweet implements Schedulable {

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

    @Override
    public boolean shouldPost() {
        return this.scheduledDate.before(new Date());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.postOnTwitter(this);
    }
}

