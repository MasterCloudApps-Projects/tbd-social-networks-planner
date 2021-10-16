package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import lombok.Data;
import org.springframework.stereotype.Service;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class TwitterService {

    private final TwitterClient twitterClient;

    public TwitterService(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    public TweetResponse getTweet(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.getTweet(tweetId);

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse postTweet(TweetRequest tweetRequest) throws TwitterClientException {
        Status status = this.twitterClient.postTweet(tweetRequest.getText());

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse deleteTweet(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.deleteTweet(tweetId);

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse retweet(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.retweet(tweetId);

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse undoRetweet(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.undoRetweet(tweetId);

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse like(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.like(tweetId);

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse undoLike(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.undoLike(tweetId);

        if (status != null) {
            return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetRepliesResponse replyTweet(String tweetId, TweetRequest tweetRequest) throws TwitterClientException {
        List<Status> statuses = this.twitterClient.replyTweet(tweetId, tweetRequest.getText());
        List<TweetResponse> replies = new ArrayList<>();
        if (statuses != null) {
            for (Status status : statuses) {
                replies.add(new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText()));
            }
        } else {
            throw new TwitterBadRequestException();
        }
        return new TweetRepliesResponse(tweetId, replies);
    }

}
