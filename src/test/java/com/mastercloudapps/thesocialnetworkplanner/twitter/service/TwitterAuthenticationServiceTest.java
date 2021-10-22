package com.mastercloudapps.thesocialnetworkplanner.twitter.service;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterAuthenticationClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TwitterAuthenticationServiceTest {
    @Mock
    private TwitterAuthenticationClient twitterAuthenticationClient;

    private TwitterAuthenticationService twitterAuthenticationService;

    @Before
    public void beforeEach() {
        this.twitterAuthenticationService = new TwitterAuthenticationService(twitterAuthenticationClient);
    }

    @Test
    public void getRequestTokenUrl_shouldReturnUrl() throws TwitterClientException {
        Mockito.when(this.twitterAuthenticationClient.requestToken()).thenReturn("URL");
        String url = this.twitterAuthenticationService.getRequestTokenUrl();

        Assertions.assertEquals(url, "URL");
        verify(this.twitterAuthenticationClient,times(1)).requestToken();
    }

    @Test
    public void getRequestTokenUrl_shouldReturnNull() throws TwitterClientException {
        Mockito.when(this.twitterAuthenticationClient.requestToken()).thenReturn(null);
        String url = this.twitterAuthenticationService.getRequestTokenUrl();
        Assertions.assertNull(url);
        verify(this.twitterAuthenticationClient,times(1)).requestToken();
    }

    @Test
    public void getAccessToken_shouldAuthenticateUser() throws TwitterClientException {
        Mockito.doNothing().when(this.twitterAuthenticationClient).getAccessToken(anyString());
        this.twitterAuthenticationService.getAccessToken("oauthVerifier");

        verify(this.twitterAuthenticationClient,times(1)).getAccessToken(any());
    }
}