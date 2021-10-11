package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import lombok.Data;
import org.springframework.stereotype.Service;
import twitter4j.Status;

@Data
@Service
public class TwitterService {

    private final TwitterClient twitterClient;

    public TwitterService(TwitterClient twitterClient){
        this.twitterClient = twitterClient;
    }

    public TweetResponse getTweet(String tweetId) throws TwitterClientException {
        Status status = this.twitterClient.getTweet(tweetId);
        return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
    }

    public TweetResponse postTweet(TweetRequest tweetRequest) throws TwitterClientException {
        Status status = this.twitterClient.postTweet(tweetRequest.getText());
        return new TweetResponse(String.valueOf(status.getId()), status.getUser().getScreenName(), status.getText());
    }

}
