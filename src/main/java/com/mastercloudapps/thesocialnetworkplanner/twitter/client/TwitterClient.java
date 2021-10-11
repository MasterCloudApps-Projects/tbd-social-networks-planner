package com.mastercloudapps.thesocialnetworkplanner.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Objects;

@Component
@Log4j2
public class TwitterClient {
    private final Twitter twitter;

    public TwitterClient() {
        this.twitter = new TwitterFactory().getInstance();
    }

    public Status getTweet(String tweetId) throws TwitterClientException {
        Status status = null;
        try {
            if (tweetId != null) {
                status = twitter.showStatus(Long.parseLong(tweetId));
            } else {
                log.info("Tweet id is empty.");
            }
            log.info("Showing @" + status.getUser().getScreenName() + "'s tweet.");
            log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
        } catch (TwitterException te) {
            log.info("Twitter [get-tweet] throw exception: " + te.getMessage());
            if (Objects.equals(te.getStatusCode(), 404)) {
                throw new TweetNotFoundException();
            }
            throw new TwitterClientException();
        }

        return status;
    }

    public Status postTweet(String text) throws TwitterClientException {
        Status status = null;
        try {
            if (text != null) {
                status = twitter.updateStatus(text);
            } else {
                log.info("Message for status is empty.");
            }
            log.info("Showing recently posted @" + status.getUser().getScreenName() + "'s tweet.");
            log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
        } catch (TwitterException te) {
            if (Objects.equals(te.getStatusCode(), 401)) {
                log.info("Twitter [post-tweet] throw exception: " + te.getMessage());
                throw new UnauthorizedTwitterClientException();
            }
            log.info("Twitter [post-tweet] throw exception: " + te.getMessage());
            throw new TwitterClientException();
        }
        return status;
    }
}
