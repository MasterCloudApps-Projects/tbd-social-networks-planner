package com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception;

public class TweetNotFoundException extends TwitterClientException {
    public TweetNotFoundException(Long id) {
        super("Tweet " + id + "has not been found in twitter.com");
    }
}
