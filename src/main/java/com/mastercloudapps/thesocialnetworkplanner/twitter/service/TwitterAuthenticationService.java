package com.mastercloudapps.thesocialnetworkplanner.twitter.service;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterAuthenticationClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import org.springframework.stereotype.Service;

@Service
public class TwitterAuthenticationService {
    private final TwitterAuthenticationClient twitterAuthenticationClient;

    public TwitterAuthenticationService(TwitterAuthenticationClient twitterAuthenticationClient) {
        this.twitterAuthenticationClient = twitterAuthenticationClient;
    }

    public String getRequestTokenUrl() throws TwitterClientException {
        return this.twitterAuthenticationClient.requestToken();
    }

    public void getAccessToken(String oauthVerifier) throws TwitterClientException {
        this.twitterAuthenticationClient.getAccessToken(oauthVerifier);
    }
}
