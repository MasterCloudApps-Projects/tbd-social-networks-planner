package com.mastercloudapps.thesocialnetworkplanner.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.Objects;

@Service
@Log4j2
public class TwitterClient {
    private final Twitter twitter;

    public TwitterClient(Twitter twitter) {
        this.twitter = twitter;
    }

    public Status getTweet(String tweetId) throws TwitterClientException {
        Status status;
        if (tweetId != null) {
            try {
                status = twitter.showStatus(Long.parseLong(tweetId));
                log.info("Showing @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
                return status;
            } catch (TwitterException te) {
                log.info("Twitter [get-tweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException();
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
            return null;
        }
    }

    public Status postTweet(String text) throws TwitterClientException {
        Status status;
        if (text != null) {
            try {
                status = twitter.updateStatus(text);
                log.info("Showing recently posted @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
                return status;
            } catch (TwitterException te) {
                if (Objects.equals(te.getStatusCode(), 401)) {
                    log.info("Twitter [post-tweet] throw exception: " + te.getMessage());
                    throw new UnauthorizedTwitterClientException();
                }
                log.info("Twitter [post-tweet] throw exception: " + te.getMessage());
                throw new TwitterClientException();
            }
        } else {
            log.info("Message for status is empty.");
            return null;
        }
    }

    public Status deleteTweet(String tweetId) throws TwitterClientException {
        Status status;
        if (tweetId != null) {
            try {
                status = twitter.destroyStatus(Long.parseLong(tweetId));
                log.info("Showing @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
            } catch (TwitterException te) {
                log.info("Twitter [delete-tweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException();
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
            return null;
        }

        return status;
    }

}
