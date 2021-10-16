package com.mastercloudapps.thesocialnetworkplanner.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.data.Status;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
public class TwitterClientTest {

    TwitterClient twitterClient;

    private static final String TWEET_ID_STRING = "01234";

    @Mock
    private Twitter twitter;

    @Before
    public void beforeEach() {
        this.twitterClient = new TwitterClient(this.twitter);
    }

    @Test
    public void getTweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.getTweet(TWEET_ID_STRING);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void getTweet_shouldReturnNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.getTweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void getTweet_shouldThrowTwitterException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.getTweet(TWEET_ID_STRING);
    }

    @Test(expected = TwitterClientException.class)
    public void getTweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 400));
        this.twitterClient.getTweet(TWEET_ID_STRING);
    }

    @Test
    public void postTweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.updateStatus(anyString())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.postTweet(TWEET_ID_STRING);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void postTweet_shouldReturnNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.updateStatus(anyString())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.postTweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = UnauthorizedTwitterClientException.class)
    public void postTweet_shouldThrowUnauthorizedTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.updateStatus(anyString())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.postTweet(TWEET_ID_STRING);
    }

    @Test(expected = TwitterClientException.class)
    public void postTweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.updateStatus(anyString())).thenThrow(new TwitterException("message", null, 400));
        this.twitterClient.postTweet("This is a tweet.");
    }

    @Test
    public void deleteTweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.destroyStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.deleteTweet(TWEET_ID_STRING);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void deleteTweet_shouldReturnNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.destroyStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.deleteTweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void deleteTweet_shouldThrowTwitterException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.destroyStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.deleteTweet(TWEET_ID_STRING);
    }

    @Test(expected = TwitterClientException.class)
    public void deleteTweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.destroyStatus(anyLong())).thenThrow(new TwitterException("message", null, 400));
        this.twitterClient.deleteTweet(TWEET_ID_STRING);
    }

    @Test
    public void retweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.retweetStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.retweet(TWEET_ID_STRING);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void retweet_shouldReturnTweetInformationNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.retweetStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.retweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void retweet_shouldThrowTweetNotFoundException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.retweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 404) );
        this.twitterClient.retweet(TWEET_ID_STRING);
    }

    @Test(expected = RetweetForbiddenException.class)
    public void retweet_shouldThrowRetweetForbiddenException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.retweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 403) );
        this.twitterClient.retweet(TWEET_ID_STRING);
    }

    @Test(expected = TwitterClientException.class)
    public void retweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.retweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 401) );
        this.twitterClient.retweet(TWEET_ID_STRING);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformation_whenTweetIsAlreadyRetweeted() throws TwitterClientException, TwitterException {
        Status status = new Status();
        status.setIsRetweeted(true);
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitter.unRetweetStatus(anyLong())).thenReturn(status);
        twitter4j.Status response = this.twitterClient.undoRetweet(TWEET_ID_STRING);
        Assertions.assertThat(response).isNotNull();
    }

    @Test(expected = RetweetForbiddenException.class)
    public void undoRetweet_shouldThrowRetweetForbiddenException_whenTweetIsNotRetweeted() throws TwitterClientException, TwitterException {
        Status status = new Status();
        status.setIsRetweeted(false);
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(status);
        this.twitterClient.undoRetweet(TWEET_ID_STRING);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformationNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(new Status());
        twitter4j.Status response = this.twitterClient.undoRetweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void undoRetweet_shouldThrowTweetNotFoundException_whenCalledShowStatus() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 404) );
        this.twitterClient.undoRetweet(TWEET_ID_STRING);
    }


    @Test(expected = TwitterClientException.class)
    public void undoRetweet_shouldThrowTwitterClientException_whenCalledShowStatus() throws TwitterClientException, TwitterException {
        Mockito.when(twitter.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 401) );
        this.twitterClient.undoRetweet(TWEET_ID_STRING);
    }

    @Test(expected = TweetNotFoundException.class)
    public void undoRetweet_shouldThrowTweetNotFoundException_whenCalledUnRetweet() throws TwitterClientException, TwitterException {
        Status status = new Status();
        status.setIsRetweeted(true);
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitter.unRetweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 404) );
        this.twitterClient.undoRetweet(TWEET_ID_STRING);
    }

    @Test(expected = TwitterClientException.class)
    public void undoRetweet_shouldThrowTwitterClientException_whenCalledUnRetweet() throws TwitterClientException, TwitterException {
        Status status = new Status();
        status.setIsRetweeted(true);
        Mockito.when(twitter.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitter.unRetweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 401) );
        this.twitterClient.undoRetweet(TWEET_ID_STRING);
    }
}