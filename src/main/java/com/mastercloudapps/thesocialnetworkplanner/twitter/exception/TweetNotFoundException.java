package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

public class TweetNotFoundException extends TwitterClientException {
    public TweetNotFoundException(String id) {
        super("Tweet " + id + "has not been found in twitter.com");
    }
}
