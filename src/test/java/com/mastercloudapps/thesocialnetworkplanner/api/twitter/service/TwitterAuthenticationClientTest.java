package com.mastercloudapps.thesocialnetworkplanner.api.twitter.service;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.client.TwitterAuthenticationClient;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.configuration.TwitterSession;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class TwitterAuthenticationClientTest {
    @Mock
    private TwitterSession twitterSession;

    private TwitterAuthenticationClient twitterAuthenticationClient;

    @Before
    public void before() {
        this.twitterAuthenticationClient = new TwitterAuthenticationClient(twitterSession);
    }

    @Test
    public void requestToken_shouldReturnToken() throws TwitterException, TwitterClientException {
        Mockito.doNothing().when(this.twitterSession).resetTwitter();
        Mockito.doNothing().when(this.twitterSession).setOAuthConsumer();
        Mockito.when(this.twitterSession.getOAuthRequestToken()).thenReturn(new RequestToken("",""));
        String url = this.twitterAuthenticationClient.requestToken();
        Assertions.assertThat(url).isNotNull();
    }

    @Test(expected = TwitterClientException.class)
    public void requestToken_shouldThrowTwitterClientException() throws TwitterException, TwitterClientException {
        Mockito.doNothing().when(this.twitterSession).resetTwitter();
        Mockito.doNothing().when(this.twitterSession).setOAuthConsumer();
        Mockito.when(this.twitterSession.getOAuthRequestToken()).thenThrow((new TwitterException("message", null, 401)));
        this.twitterAuthenticationClient.requestToken();
    }

    @Test
    public void getAccessToken_shouldAuthenticateUser() throws TwitterException, TwitterClientException {
        Mockito.when(twitterSession.getOAuthAccessToken(any(),any())).thenReturn(null);
        Mockito.doNothing().when(twitterSession).setOAuthAccessToken(any());
        this.twitterAuthenticationClient.getAccessToken("oauthVerifier");
    }

    @Test(expected = TwitterClientException.class)
    public void getAccessToken_shouldThrowTwitterClientException() throws TwitterException, TwitterClientException {
        Mockito.when(twitterSession.getOAuthAccessToken(any(),any())).thenThrow(new TwitterException("message", null, 401));
        this.twitterAuthenticationClient.getAccessToken("oauthVerifier");
    }
}