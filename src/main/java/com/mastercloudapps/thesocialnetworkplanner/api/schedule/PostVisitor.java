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

import java.util.List;

import static com.mastercloudapps.thesocialnetworkplanner.config.ff4jconfig.FeatureFlagsInitializer.FEATURE_NEW_SCHEDULER;

@Log4j2
@Service
public class PostVisitor implements Visitor {

    private final InstagramService instagramService;
    private final TwitterService twitterService;
    private final FF4j ff4j;

    public PostVisitor(InstagramService instagramService, TwitterService twitterService, FF4j ff4j) {
        this.instagramService = instagramService;
        this.twitterService = twitterService;
        this.ff4j = ff4j;
    }

    @Scheduled(fixedDelay = 3600000)
    public void postUnpublishedPosts() {
        if (ff4j.check(FEATURE_NEW_SCHEDULER)) {
            List<Schedulable> twitterPosts = twitterService.getUnpublishedTweetsNew();
            for (Schedulable post : twitterPosts) {
                if (post.shouldPost()) {
                    post.accept(this);
                }
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
