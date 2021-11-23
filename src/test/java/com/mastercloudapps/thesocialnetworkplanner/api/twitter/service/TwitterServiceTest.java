package com.mastercloudapps.thesocialnetworkplanner.api.twitter.service;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.client.data.Status;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.ScheduleTweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.Tweet;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.repository.TweetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {

    private TwitterService twitterService;

    @Mock
    private TwitterClient twitterClient;
    @Mock
    private TweetRepository tweetRepository;

    private static final Long TWEET_ID = 1L;
    private static final String TWEET_TEXT = "text";

    @Before
    public void beforeEach() {
        this.twitterService = new TwitterService(this.twitterClient, tweetRepository);
    }

    @Test
    public void getTweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.getTweet(anyLong())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.getTweet(TWEET_ID);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), 0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void getTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.getTweet(anyLong())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.getTweet(TWEET_ID);

        assertNull(tweetResponse);
    }

    @Test
    public void postTweet_shouldReturnTweetInformation() throws TwitterClientException, IOException {
        when(this.twitterClient.postTweet(anyString(), any())).thenReturn(new Status());
        when(this.tweetRepository.save(any())).thenReturn(Tweet.builder().id(1L).build());
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetText(), null);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), 1);
        assertEquals(tweetResponse.getTwitterId(), 0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void postTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException, IOException {
        when(this.twitterClient.postTweet(anyString(), any())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetText(), null);

        assertNull(tweetResponse);
    }

    @Test
    public void deleteTweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.tweetRepository.findByTwitterId(anyLong())).thenReturn(Optional.of(Tweet.builder().id(TWEET_ID).build()));
        when(this.twitterClient.deleteTweet(anyLong())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.deleteTweet(TWEET_ID);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), TWEET_ID);
        verify(this.tweetRepository, times(1)).save(any());
    }

    @Test(expected = TwitterBadRequestException.class)
    public void deleteTweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.deleteTweet(anyLong())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.deleteTweet(TWEET_ID);

        assertNull(tweetResponse);
    }

    @Test
    public void retweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.retweet(anyLong())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.retweet(TWEET_ID);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), 0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void retweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.retweet(anyLong())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.retweet(TWEET_ID);

        assertNull(tweetResponse);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.undoRetweet(anyLong())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.undoRetweet(TWEET_ID);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), 0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void undoRetweet_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.undoRetweet(anyLong())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.undoRetweet(TWEET_ID);

        assertNull(tweetResponse);
    }

    @Test
    public void like_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.like(anyLong())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.like(TWEET_ID);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), 0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void like_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.like(anyLong())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.like(TWEET_ID);

        assertNull(tweetResponse);
    }

    @Test
    public void undoLike_shouldReturnTweetInformation() throws TwitterClientException {
        when(this.twitterClient.undoLike(anyLong())).thenReturn(new Status());
        TweetResponse tweetResponse = this.twitterService.undoLike(TWEET_ID);

        assertEquals(tweetResponse.getUsername(), "andrea_juanma");
        assertEquals(tweetResponse.getText(), "This is a new tweet.");
        assertEquals(tweetResponse.getId(), 0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void undoLike_shouldThrowTwitterBadRequestException() throws TwitterClientException {
        when(this.twitterClient.undoLike(anyLong())).thenReturn(null);
        TweetResponse tweetResponse = this.twitterService.undoLike(TWEET_ID);

        assertNull(tweetResponse);
    }

    @Test
    public void replyTweet_shouldReturnListOfTweets() throws TwitterClientException {
        when(this.twitterClient.replyTweet(anyLong(), anyString())).thenReturn(List.of(new Status()));
        TweetRepliesResponse tweetRepliesResponse = this.twitterService.replyTweet(TWEET_ID, new TweetRequest(TWEET_TEXT));
        assertThat(tweetRepliesResponse.getReplies().size()).isEqualTo(1);
    }

    @Test
    public void replyTweet_shouldReturnEmptyList() throws TwitterClientException {
        when(this.twitterClient.replyTweet(anyLong(), anyString())).thenReturn(List.of());
        TweetRepliesResponse tweetRepliesResponse = this.twitterService.replyTweet(TWEET_ID, new TweetRequest(TWEET_TEXT));
        assertThat(tweetRepliesResponse.getReplies().size()).isEqualTo(0);
    }

    @Test(expected = TwitterBadRequestException.class)
    public void replyTweet_shouldThrowBadRequest() throws TwitterClientException {
        when(this.twitterClient.replyTweet(anyLong(), anyString())).thenReturn(null);
        this.twitterService.replyTweet(TWEET_ID, new TweetRequest(TWEET_TEXT));
    }

    @Test
    public void getUnpublishedTweets_shouldReturnEmptyList() {
        when(this.tweetRepository.findByTwitterIdIsNull()).thenReturn(new ArrayList<>());
        List<TweetResponse> response = this.twitterService.getUnpublishedTweets();
        assertThat(response).isEmpty();
    }

    @Test
    public void getUnpublishedTweets_shouldReturnTweetInformation() {
        Tweet tweet = tweet();
        when(this.tweetRepository.findByTwitterIdIsNull()).thenReturn(List.of(tweet));
        List<TweetResponse> tweets = this.twitterService.getUnpublishedTweets();
        assertThat(tweets.size()).isEqualTo(1);
        assertThat(tweets.get(0).getId()).isEqualTo(tweet.getId());
        assertThat(tweets.get(0).getUsername()).isEqualTo(tweet.getUsername());
        assertThat(tweets.get(0).getText()).isEqualTo(tweet.getText());
    }

    @Test
    public void scheduleTweet_shouldReturnTweetInformation() {
        Tweet tweet = tweet();
        when(this.tweetRepository.save(any())).thenReturn(tweet);
        TweetResponse tweetResponse = this.twitterService.scheduleTweet(scheduleTweetRequest());
        assertThat(tweetResponse.getId()).isEqualTo(tweet.getId());
        assertThat(tweetResponse.getUsername()).isEqualTo(tweet.getUsername());
        assertThat(tweetResponse.getText()).isEqualTo(tweet.getText());
    }

    @Test
    public void postScheduledTweet_shouldPostTweet() throws TwitterClientException {
        Tweet tweet = tweet();
        when(this.twitterClient.getUsername()).thenReturn("andrea_juanma");
        when(this.twitterClient.postTweet(anyString(), any())).thenReturn(new Status());
        this.twitterService.postScheduledTweet(tweet);
        verify(this.tweetRepository, times(1)).save(any());
    }

    @Test
    public void postScheduledTweet_shouldNotPost_whenUsernameIsDifferent() throws TwitterClientException {
        Tweet tweet = tweet();
        when(this.twitterClient.getUsername()).thenReturn("other");
        this.twitterService.postScheduledTweet(tweet);
        verify(this.tweetRepository, times(0)).save(any());
    }

    @Test
    public void postScheduledTweet_shouldNotPost_whenStatusIsNull() throws TwitterClientException {
        Tweet tweet = tweet();
        when(this.twitterClient.getUsername()).thenReturn("other");
        this.twitterService.postScheduledTweet(tweet);
        verify(this.twitterClient, times(0)).postTweet(anyString(), any());
        verify(this.tweetRepository, times(0)).save(any());
    }

    private String tweetText() {
        return "This is a new tweet.";
    }

    private ScheduleTweetRequest scheduleTweetRequest() {
        return new ScheduleTweetRequest();
    }

    private Tweet tweet() {
        return Tweet.builder().id(TWEET_ID).text("This is a new tweet.").username("andrea_juanma").build();
    }

}