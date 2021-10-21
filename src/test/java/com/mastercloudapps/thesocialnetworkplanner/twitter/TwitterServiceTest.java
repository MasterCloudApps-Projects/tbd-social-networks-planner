package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.client.data.Status;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {

    @Mock
    private TwitterClient twitterClient;

    private TwitterService twitterService;

    private static final String TWEET_ID_STRING = "tweetId";
    private static final String TWEET_TEXT = "text";

    @Before
    public void beforeEach() {
        this.twitterService = new TwitterService(this.twitterClient);
    }

    @Test
    public void getTweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.getTweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.getTweet(TWEET_ID_STRING);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void getTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.getTweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.getTweet(TWEET_ID_STRING);

        assertNull(tweetResponse);
    }

    @Test
    public void postTweet_shouldReturnTweetInformation() throws TwitterClientException, IOException {
        when(this.twitterClient.postTweet(anyString(), any())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetRequest(), null);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void postTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException, IOException {
        when(this.twitterClient.postTweet(anyString(), any())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetRequest(), null);

        assertNull(tweetResponse);
    }

    @Test
    public void deleteTweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.deleteTweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.deleteTweet(TWEET_ID_STRING);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void deleteTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.deleteTweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.deleteTweet(TWEET_ID_STRING);

        assertNull(tweetResponse);
    }

    @Test
    public void retweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.retweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.retweet(TWEET_ID_STRING);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void retweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.retweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.retweet(TWEET_ID_STRING);

        assertNull(tweetResponse);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.undoRetweet(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.undoRetweet(TWEET_ID_STRING);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void undoRetweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.undoRetweet(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.undoRetweet(TWEET_ID_STRING);

        assertNull(tweetResponse);
    }

    @Test
    public void like_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.like(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.like(TWEET_ID_STRING);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void like_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.like(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.like(TWEET_ID_STRING);

        assertNull(tweetResponse);
    }

    @Test
    public void undoLike_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.undoLike(anyString())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.undoLike(TWEET_ID_STRING);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), "0");
    }

    @Test(expected = TwitterBadRequestException.class)
    public void undoLike_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.undoLike(anyString())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.undoLike(TWEET_ID_STRING);

        assertNull(tweetResponse);
    }

    @Test
    public void replyTweet_shouldReturnListOfTweets() throws TwitterClientException {
        when(this.twitterClient.replyTweet(anyString(), anyString())).thenReturn(List.of(new Status()));
        TweetRepliesResponse tweetRepliesResponse = this.twitterService.replyTweet(TWEET_ID_STRING, new TweetRequest(TWEET_TEXT));
        assertThat(tweetRepliesResponse.getReplies().size()).isEqualTo(1);
    }

    @Test
    public void replyTweet_shouldReturnEmptyList() throws TwitterClientException {
        when(this.twitterClient.replyTweet(anyString(), anyString())).thenReturn(List.of());
        TweetRepliesResponse tweetRepliesResponse = this.twitterService.replyTweet(TWEET_ID_STRING, new TweetRequest(TWEET_TEXT));
        assertThat(tweetRepliesResponse.getReplies().size()).isEqualTo(0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void replyTweet_shouldThrowBadRequest() throws TwitterClientException {
        when(this.twitterClient.replyTweet(anyString(), anyString())).thenReturn(null);
        this.twitterService.replyTweet(TWEET_ID_STRING, new TweetRequest(TWEET_TEXT));
    }


    private TweetRequest tweetRequest() {
        return TweetRequest.builder().text("This is a new tweet.").build();
    }

}