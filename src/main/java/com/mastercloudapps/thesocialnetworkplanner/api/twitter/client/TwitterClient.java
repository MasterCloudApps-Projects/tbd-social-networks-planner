package com.mastercloudapps.thesocialnetworkplanner.api.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.configuration.TwitterSession;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.UnauthorizedTwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.Action;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class TwitterClient {
    private final TwitterSession twitterSession;

    public TwitterClient(TwitterSession twitterSession) {
        this.twitterSession = twitterSession;
    }

    public Status getTweet(Long tweetId) throws TwitterClientException {
        Status status;
        if (tweetId != null) {
            try {
                status = twitterSession.showStatus(tweetId);
                log.info("Showing @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
                return status;
            } catch (TwitterException te) {
                log.info("Twitter [get-tweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
            return null;
        }
    }

    public Status postTweet(String text, File image) throws TwitterClientException {
        Status status;
        if (text != null) {
            try {
                if (image != null) {
                    long[] mediaIds = new long[1];
                    UploadedMedia media = twitterSession.uploadMedia(image);
                    mediaIds[0] = media.getMediaId();

                    StatusUpdate statusUpdate = new StatusUpdate(text);
                    statusUpdate.setMediaIds(mediaIds);
                    status = twitterSession.updateStatus(statusUpdate);
                    File file = new File("src/main/resources/image_to_upload.jpeg");
                    if (file.exists()) {
                        file.delete();
                    }
                } else {
                    status = twitterSession.updateStatus(text);
                }
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

    public Status deleteTweet(Long tweetId) throws TwitterClientException {
        Status status;
        if (tweetId != null) {
            try {
                status = twitterSession.destroyStatus(tweetId);
                log.info("Showing @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
            } catch (TwitterException te) {
                log.info("Twitter [delete-tweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
            return null;
        }

        return status;
    }

    public Status retweet(Long tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitterSession.retweetStatus(tweetId);

                log.info("Showing retweeted @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
            } catch (TwitterException te) {
                log.info("Twitter [retweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                } else if (Objects.equals(te.getStatusCode(), 403)) {
                    throw new RetweetForbiddenException(true, Action.RETWEET);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
        }
        return status;
    }

    public Status undoRetweet(Long tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitterSession.showStatus(tweetId);
                if (status.isRetweeted()) {
                    status = twitterSession.unRetweetStatus(tweetId);
                    log.info("Showing retweeted @" + status.getUser().getScreenName() + "'s tweet.");
                    log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
                } else {
                    log.info("This tweet is not retweet by @" + status.getUser().getScreenName());
                    throw new RetweetForbiddenException(false, Action.RETWEET);
                }
            } catch (TwitterException te) {
                log.info("Twitter [undo-retweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
        }
        return status;
    }

    public Status like(Long tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitterSession.createFavorite(tweetId);

                log.info("Showing liked @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
            } catch (TwitterException te) {
                log.info("Twitter [create-favorite] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                } else if (Objects.equals(te.getStatusCode(), 403)) {
                    throw new RetweetForbiddenException(true, Action.RETWEET);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
        }
        return status;
    }

    public Status undoLike(Long tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitterSession.showStatus(tweetId);
                if (status.isFavorited()) {
                    status = twitterSession.destroyFavorite(tweetId);
                    log.info("Showing liked @" + status.getUser().getScreenName() + "'s tweet.");
                    log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
                } else {
                    log.info("This tweet is not favorited of @" + status.getUser().getScreenName());
                    throw new RetweetForbiddenException(false, Action.LIKE);
                }
            } catch (TwitterException te) {
                log.info("Twitter [undo-like] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
        }
        return status;
    }

    public List<Status> replyTweet(Long tweetId, String text) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                StatusUpdate statusUpdate = new StatusUpdate(text);
                statusUpdate.setInReplyToStatusId(tweetId);
                status = twitterSession.updateStatus(statusUpdate);
                log.info("Showing reply to tweet:" + tweetId + " @" + status.getUser().getScreenName() + "'s tweet.");
                log.info("@" + status.getUser().getScreenName() + " - " + status.getText());
            } catch (TwitterException te) {
                log.info("Twitter [reply-to-tweet] throw exception: " + te.getMessage());
                if (Objects.equals(te.getStatusCode(), 404)) {
                    throw new TweetNotFoundException(tweetId);
                }
                throw new TwitterClientException();
            }
        } else {
            log.info("Tweet id is empty.");
        }
        return status != null ? showReplies(status.getUser().getScreenName(), tweetId) : null;
    }

    private List<Status> showReplies(String username, Long tweetId) throws TwitterClientException {
        List<Status> replies = new ArrayList<>();

        try {
            Query query = new Query("to:" + username + " since_id:" + tweetId);
            QueryResult results;

            do {
                results = twitterSession.search(query);
                log.info("Results: " + results.getTweets().size());
                List<Status> tweets = results.getTweets();

                for (Status tweet : tweets)
                    if (tweet.getInReplyToStatusId() == tweetId)
                        replies.add(tweet);
            } while ((query = results.nextQuery()) != null);

        } catch (Exception e) {
            throw new TwitterClientException();
        }
        return replies;
    }

    public String getUsername() {
        String username = null;
        try {
            username = this.twitterSession.getUsername();
        } catch (TwitterException e) {
            log.error("Error retrieving username");
        }
        return username;
    }

}
