package com.mastercloudapps.thesocialnetworkplanner.twitter.configuration;

import org.springframework.beans.factory.annotation.Value;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.File;

public class TwitterSession {
    @Value("${twitter.consumerSecret}")
    private String consumerSecret;
    @Value("${twitter.consumerKey}")
    private String consumerKey;
    private static TwitterSession twitterSession;

    private static Twitter twitter;

    public static TwitterSession getInstance() {
        if (twitterSession == null) {
            twitterSession = new TwitterSession(new TwitterFactory().getInstance());
        }
        return twitterSession;
    }

    private TwitterSession() {}

    private TwitterSession(Twitter twitter4j) {
        twitter = twitter4j;
    }

    public void resetTwitter() {
        twitter = new TwitterFactory().getInstance();
    }

    public Status showStatus(long id) throws TwitterException {
        return twitter.showStatus(id);
    }

    public UploadedMedia uploadMedia(File file) throws TwitterException {
        return twitter.uploadMedia(file);
    }

    public Status updateStatus(StatusUpdate statusUpdate) throws TwitterException {
        return twitter.updateStatus(statusUpdate);
    }

    public Status updateStatus(String message) throws TwitterException {
        return twitter.updateStatus(message);
    }

    public Status destroyStatus(long id) throws TwitterException {
        return twitter.destroyStatus(id);
    }

    public Status retweetStatus(long id) throws TwitterException {
        return twitter.retweetStatus(id);
    }

    public Status unRetweetStatus(long id) throws TwitterException {
        return twitter.unRetweetStatus(id);
    }

    public Status createFavorite(long id) throws TwitterException {
        return twitter.createFavorite(id);
    }

    public Status destroyFavorite(long id) throws TwitterException {
        return twitter.destroyFavorite(id);
    }

    public QueryResult search(Query query) throws TwitterException {
        return twitter.search(query);
    }

    public void setOAuthConsumer() {
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    public RequestToken getOAuthRequestToken() throws TwitterException {
        return twitter.getOAuthRequestToken();
    }

    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return twitter.getOAuthAccessToken(requestToken, oauthVerifier);
    }

    public void setOAuthAccessToken(AccessToken accessToken) {
        twitter.setOAuthAccessToken(accessToken);
    }
}
