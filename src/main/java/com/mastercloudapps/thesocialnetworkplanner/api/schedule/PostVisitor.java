package com.mastercloudapps.thesocialnetworkplanner.api.schedule;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.Instagram;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.service.InstagramService;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.Tweet;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.service.TwitterService;
import lombok.extern.log4j.Log4j2;
import org.ff4j.FF4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class PostVisitor implements Visitor {

    private final InstagramService instagramService;
    private final TwitterService twitterService;

    public PostVisitor(InstagramService instagramService, TwitterService twitterService) {
        this.instagramService = instagramService;
        this.twitterService = twitterService;
    }

    @Scheduled(fixedDelay = 3600000, initialDelay = 120000)
    public void postUnpublishedPosts() {
        List<Schedulable> twitterPosts = this.twitterService.getUnpublishedTweetsNew();
        List<Schedulable> instagramPosts = this.instagramService.getUnpublishedPosts();
        List<Schedulable> allPosts = new ArrayList();
        allPosts.addAll(twitterPosts);
        allPosts.addAll(instagramPosts);
        for (Schedulable post : allPosts) {
            if (post.shouldPost()) {
                post.accept(this);
            }
        }
    }

    @Override
    public void postOnTwitter(Tweet tweet) {
        try {
            this.twitterService.postScheduledTweet(tweet);
        } catch (TwitterClientException | IllegalStateException e) {
            log.error("Error posting tweet: " + tweet.getId(), e);
        }
    }

    @Override
    public void postOnInstagram(Instagram instagram) {
        try {
            this.instagramService.postScheduledInstagram(instagram);
        } catch (InstagramException e) {
            log.error("Error posting instagram: " + instagram.getId(), e);
        }

    }
}
