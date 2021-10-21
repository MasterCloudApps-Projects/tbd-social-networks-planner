package com.mastercloudapps.thesocialnetworkplanner.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.twitter.configuration.TwitterSession;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Component
@Log4j2
public class TwitterAuthenticationClient {

    private RequestToken requestToken;
    private final TwitterSession twitterSession;
    private final RestTemplate restTemplate;

    public TwitterAuthenticationClient(TwitterSession twitterSession, RestTemplate restTemplate) {
        this.twitterSession = twitterSession;
        this.restTemplate = restTemplate;
    }

    public String requestToken() throws TwitterClientException {
        twitterSession.resetTwitter();
        twitterSession.setOAuthConsumer();
        try {
            requestToken = twitterSession.getOAuthRequestToken();
        } catch (TwitterException ex) {
            throw new TwitterClientException();
        }
        log.info("Got request token.");
        return requestToken.getAuthenticationURL();
    }

    public void getAccessToken(String oauthVerifier) throws TwitterClientException {
        AccessToken accessToken;
        try {
            accessToken = twitterSession.getOAuthAccessToken(requestToken, oauthVerifier);
            twitterSession.setOAuthAccessToken(accessToken);
        } catch (TwitterException ex) {
            throw new TwitterClientException();
        }
    }

}
