package com.mastercloudapps.thesocialnetworkplanner.twitter.client;

import com.mastercloudapps.thesocialnetworkplanner.twitter.configuration.TwitterSession;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Component
@Log4j2
public class TwitterAuthenticationClient {

    private RequestToken requestToken;
    private final TwitterSession twitterSession;

    public TwitterAuthenticationClient(TwitterSession twitterSession) {
        this.twitterSession = twitterSession;
    }

    public String requestToken() throws TwitterClientException {
        twitterSession.resetTwitter();
        twitterSession.setOAuthConsumer();
        try {
            requestToken = twitterSession.getOAuthRequestToken();
            log.info("Got request token.");
        } catch (TwitterException ex) {
            throw new TwitterClientException();
        }
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
