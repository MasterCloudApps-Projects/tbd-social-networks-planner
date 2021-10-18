package com.mastercloudapps.thesocialnetworkplanner.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Action;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
                    UploadedMedia media = twitter.uploadMedia(image);
                    mediaIds[0] = media.getMediaId();

                    StatusUpdate statusUpdate = new StatusUpdate(text);
                    statusUpdate.setMediaIds(mediaIds);
                    status = twitter.updateStatus(statusUpdate);
                    File file = new File("src/main/resources/image_to_upload.jpeg");
                    if (file.exists()) {
                        file.delete();
                    }
                } else {
                    status = twitter.updateStatus(text);
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

    public Status retweet(String tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitter.retweetStatus(Long.parseLong(tweetId));

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

    public Status undoRetweet(String tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitter.showStatus(Long.parseLong(tweetId));
                if (status.isRetweeted()) {
                    status = twitter.unRetweetStatus(Long.parseLong(tweetId));
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

    public Status like(String tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitter.createFavorite(Long.parseLong(tweetId));

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

    public Status undoLike(String tweetId) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                status = twitter.showStatus(Long.parseLong(tweetId));
                if (status.isFavorited()) {
                    status = twitter.destroyFavorite(Long.parseLong(tweetId));
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

    public List<Status> replyTweet(String tweetId, String text) throws TwitterClientException {
        Status status = null;
        if (tweetId != null) {
            try {
                StatusUpdate statusUpdate = new StatusUpdate(text);
                statusUpdate.setInReplyToStatusId(Long.parseLong(tweetId));
                status = twitter.updateStatus(statusUpdate);
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

    private List<Status> showReplies(String username, String tweetId) throws TwitterClientException {
        List<Status> replies = new ArrayList<>();

        try {
            Query query = new Query("to:" + username + " since_id:" + tweetId);
            QueryResult results;

            do {
                results = twitter.search(query);
                log.info("Results: " + results.getTweets().size());
                List<Status> tweets = results.getTweets();

                for (Status tweet : tweets)
                    if (tweet.getInReplyToStatusId() == Long.parseLong(tweetId))
                        replies.add(tweet);
            } while ((query = results.nextQuery()) != null);

        } catch (Exception e) {
            throw new TwitterClientException();
        }
        return replies;
    }
}
