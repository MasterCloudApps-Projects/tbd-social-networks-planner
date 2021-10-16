package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.client.data.Status;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {

    @Mock
    private TwitterClient twitterClient;

    private TwitterService twitterService;

    private static final String TWEET_ID_STRING = "tweetId";

    @Before
    public void beforeEach() {
        this.twitterService = new TwitterService(this.twitterClient);
    }

    @Test
    public void getTweet_shouldReturnTweetInformation() throws TwitterClientException {
        Mockito.when(this.twitterClient.getTweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.getTweet(TWEET_ID_STRING);

        Assertions.assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        Assertions.assertEquals(tweetResponse.getText(), "This is a new tweet.");
        Assertions.assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void getTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        Mockito.when(this.twitterClient.getTweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.getTweet(TWEET_ID_STRING);

        Assertions.assertNull(tweetResponse);
    }

    @Test
    public void postTweet_shouldReturnTweetInformation() throws TwitterClientException {
        Mockito.when(this.twitterClient.postTweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetRequest());

        Assertions.assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        Assertions.assertEquals(tweetResponse.getText(), "This is a new tweet.");
        Assertions.assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void postTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        Mockito.when(this.twitterClient.postTweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetRequest());

        Assertions.assertNull(tweetResponse);
    }

    @Test
    public void deleteTweet_shouldReturnTweetInformation() throws TwitterClientException {
        Mockito.when(this.twitterClient.deleteTweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.deleteTweet(TWEET_ID_STRING);

        Assertions.assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        Assertions.assertEquals(tweetResponse.getText(), "This is a new tweet.");
        Assertions.assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void deleteTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        Mockito.when(this.twitterClient.deleteTweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.deleteTweet(TWEET_ID_STRING);

        Assertions.assertNull(tweetResponse);
    }

    @Test
    public void retweet_shouldReturnTweetInformation() throws TwitterClientException {
        Mockito.when(this.twitterClient.retweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.retweet(TWEET_ID_STRING);

        Assertions.assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        Assertions.assertEquals(tweetResponse.getText(), "This is a new tweet.");
        Assertions.assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void retweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        Mockito.when(this.twitterClient.retweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.retweet(TWEET_ID_STRING);

        Assertions.assertNull(tweetResponse);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformation() throws TwitterClientException {
        Mockito.when(this.twitterClient.undoRetweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.undoRetweet(TWEET_ID_STRING);

        Assertions.assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        Assertions.assertEquals(tweetResponse.getText(), "This is a new tweet.");
        Assertions.assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void undoRetweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        Mockito.when(this.twitterClient.undoRetweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.undoRetweet(TWEET_ID_STRING);

        Assertions.assertNull(tweetResponse);
    }

    private TweetRequest tweetRequest() {
        return TweetRequest.builder().text("This is a new tweet.").build();
    }

}