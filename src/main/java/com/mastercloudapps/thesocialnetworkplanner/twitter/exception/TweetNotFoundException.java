package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

public class TweetNotFoundException extends TwitterClientException {
    public TweetNotFoundException() {
        super("Tweet has not been found in twitter.com");
    }
}
