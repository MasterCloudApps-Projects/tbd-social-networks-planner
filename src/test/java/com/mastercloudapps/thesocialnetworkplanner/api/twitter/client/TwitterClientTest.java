package com.mastercloudapps.thesocialnetworkplanner.api.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.client.data.QueryResult;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.client.data.Status;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.configuration.TwitterSession;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.UnauthorizedTwitterClientException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
public class TwitterClientTest {

    TwitterClient twitterClient;

    private static final Long TWEET_ID = 1234L;

    @Mock
    private TwitterSession twitterSession;

    private QueryResult queryResult;

    private Status status;

    @Before
    public void beforeEach() {
        this.twitterClient = new TwitterClient(this.twitterSession);
        this.status = new Status();
        this.queryResult = new QueryResult();
    }

    @Test
    public void getTweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.getTweet(TWEET_ID);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void getTweet_shouldReturnNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.getTweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void getTweet_shouldThrowTwitterException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.getTweet(TWEET_ID);
    }

    @Test(expected = TwitterClientException.class)
    public void getTweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 400));
        this.twitterClient.getTweet(TWEET_ID);
    }

    @Test
    public void postTweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus(anyString())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.postTweet("text", null);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void postTweet_shouldReturnNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus(anyString())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.postTweet(null, null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = UnauthorizedTwitterClientException.class)
    public void postTweet_shouldThrowUnauthorizedTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus(anyString())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.postTweet("text", null);
    }

    @Test(expected = TwitterClientException.class)
    public void postTweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus(anyString())).thenThrow(new TwitterException("message", null, 400));
        this.twitterClient.postTweet("This is a tweet.", null);
    }

    @Test
    public void deleteTweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.destroyStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.deleteTweet(TWEET_ID);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void deleteTweet_shouldReturnNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.destroyStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.deleteTweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void deleteTweet_shouldThrowTwitterException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.destroyStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.deleteTweet(TWEET_ID);
    }

    @Test(expected = TwitterClientException.class)
    public void deleteTweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.destroyStatus(anyLong())).thenThrow(new TwitterException("message", null, 400));
        this.twitterClient.deleteTweet(TWEET_ID);
    }

    @Test
    public void retweet_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.retweetStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.retweet(TWEET_ID);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void retweet_shouldReturnTweetInformationNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.retweetStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.retweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void retweet_shouldThrowTweetNotFoundException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.retweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.retweet(TWEET_ID);
    }

    @Test(expected = RetweetForbiddenException.class)
    public void retweet_shouldThrowRetweetForbiddenException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.retweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 403));
        this.twitterClient.retweet(TWEET_ID);
    }

    @Test(expected = TwitterClientException.class)
    public void retweet_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.retweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.retweet(TWEET_ID);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformation_whenTweetIsAlreadyRetweeted() throws TwitterClientException, TwitterException {
        this.status.setIsRetweeted(true);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitterSession.unRetweetStatus(anyLong())).thenReturn(status);
        twitter4j.Status response = this.twitterClient.undoRetweet(TWEET_ID);
        Assertions.assertThat(response).isNotNull();
    }

    @Test(expected = RetweetForbiddenException.class)
    public void undoRetweet_shouldThrowRetweetForbiddenException_whenTweetIsNotRetweeted() throws TwitterClientException, TwitterException {
        this.status.setIsRetweeted(false);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        this.twitterClient.undoRetweet(TWEET_ID);
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformationNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.undoRetweet(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void undoRetweet_shouldThrowTweetNotFoundException_whenCalledShowStatus() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.undoRetweet(TWEET_ID);
    }


    @Test(expected = TwitterClientException.class)
    public void undoRetweet_shouldThrowTwitterClientException_whenCalledShowStatus() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.undoRetweet(TWEET_ID);
    }

    @Test(expected = TweetNotFoundException.class)
    public void undoRetweet_shouldThrowTweetNotFoundException_whenCalledUnRetweet() throws TwitterClientException, TwitterException {
        this.status.setIsRetweeted(true);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitterSession.unRetweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.undoRetweet(TWEET_ID);
    }

    @Test(expected = TwitterClientException.class)
    public void undoRetweet_shouldThrowTwitterClientException_whenCalledUnRetweet() throws TwitterClientException, TwitterException {
        this.status.setIsRetweeted(true);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitterSession.unRetweetStatus(anyLong())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.undoRetweet(TWEET_ID);
    }

    @Test
    public void like_shouldReturnTweetInformation() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.createFavorite(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.like(TWEET_ID);
        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void like_shouldReturnTweetInformationNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.createFavorite(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.like(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void like_shouldThrowTweetNotFoundException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.createFavorite(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.like(TWEET_ID);
    }

    @Test(expected = RetweetForbiddenException.class)
    public void like_shouldThrowRetweetForbiddenException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.createFavorite(anyLong())).thenThrow(new TwitterException("message", null, 403));
        this.twitterClient.like(TWEET_ID);
    }

    @Test(expected = TwitterClientException.class)
    public void like_shouldThrowTwitterClientException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.createFavorite(anyLong())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.like(TWEET_ID);
    }

    @Test
    public void undoLike_shouldReturnTweetInformation_whenTweetIsAlreadyLiked() throws TwitterClientException, TwitterException {
        this.status.setIsFavorited(true);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitterSession.destroyFavorite(anyLong())).thenReturn(status);
        twitter4j.Status response = this.twitterClient.undoLike(TWEET_ID);
        Assertions.assertThat(response).isNotNull();
    }

    @Test(expected = RetweetForbiddenException.class)
    public void undoLike_shouldThrowRetweetForbiddenException_whenTweetIsNotLiked() throws TwitterClientException, TwitterException {
        this.status.setIsFavorited(false);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        this.twitterClient.undoLike(TWEET_ID);
    }

    @Test
    public void undoLike_shouldReturnTweetInformationNull() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(this.status);
        twitter4j.Status response = this.twitterClient.undoLike(null);
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = TweetNotFoundException.class)
    public void undoLike_shouldThrowTweetNotFoundException_whenCalledShowStatus() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.undoRetweet(TWEET_ID);
    }


    @Test(expected = TwitterClientException.class)
    public void undoLike_shouldThrowTwitterClientException_whenCalledShowStatus() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.showStatus(anyLong())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.undoLike(TWEET_ID);
    }

    @Test(expected = TweetNotFoundException.class)
    public void undoLike_shouldThrowTweetNotFoundException_whenCalledDestroyLike() throws TwitterClientException, TwitterException {
        this.status.setIsFavorited(true);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitterSession.destroyFavorite(anyLong())).thenThrow(new TwitterException("message", null, 404));
        this.twitterClient.undoLike(TWEET_ID);
    }

    @Test(expected = TwitterClientException.class)
    public void undoLike_shouldThrowTwitterClientException_whenCalledDestroyLike() throws TwitterClientException, TwitterException {
        this.status.setIsFavorited(true);
        Mockito.when(twitterSession.showStatus(anyLong())).thenReturn(status);
        Mockito.when(twitterSession.destroyFavorite(anyLong())).thenThrow(new TwitterException("message", null, 401));
        this.twitterClient.undoLike(TWEET_ID);
    }

    @Test
    public void reply_shouldReturnListOfReplies_whenPostTweet() throws TwitterClientException, TwitterException {
        this.status.setInReplyToStatusId(1234);
        this.queryResult.setReplies(List.of(status));
        Mockito.when(twitterSession.updateStatus((StatusUpdate) any())).thenReturn(status);
        Mockito.when(twitterSession.search(any())).thenReturn(queryResult);

        List<twitter4j.Status> response = this.twitterClient.replyTweet(TWEET_ID, "text");
        Assertions.assertThat(response.size()).isEqualTo(1);
    }

    @Test
    public void reply_shouldReturnEmptyListOfReplies_whenPostTweet() throws TwitterClientException, TwitterException {
        this.status.setInReplyToStatusId(1234);
        this.queryResult.setReplies(List.of());
        Mockito.when(twitterSession.updateStatus((StatusUpdate) any())).thenReturn(status);
        Mockito.when(twitterSession.search(any())).thenReturn(queryResult);

        List<twitter4j.Status> response = this.twitterClient.replyTweet(TWEET_ID, "text");
        Assertions.assertThat(response.size()).isEqualTo(0);
    }

    @Test(expected = TwitterClientException.class)
    public void reply_shouldThrowTwitterClientException_whenSearchThrowsException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus((StatusUpdate) any())).thenReturn(status);
        Mockito.when(twitterSession.search(any())).thenThrow(new RuntimeException());

        this.twitterClient.replyTweet(TWEET_ID, "text");
    }

    @Test(expected = TweetNotFoundException.class)
    public void reply_shouldThrowTweetNotFoundException_whenUpdateStatusThrowsException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus((StatusUpdate) any())).thenThrow(new TwitterException("message", null, 404));

        this.twitterClient.replyTweet(TWEET_ID, "text");
    }

    @Test(expected = TwitterClientException.class)
    public void reply_shouldThrowTwitterClientException_whenUpdateStatusThrowsException() throws TwitterClientException, TwitterException {
        Mockito.when(twitterSession.updateStatus((StatusUpdate) any())).thenThrow(new TwitterException("message", null, 401));

        this.twitterClient.replyTweet(TWEET_ID, "text");
    }

}