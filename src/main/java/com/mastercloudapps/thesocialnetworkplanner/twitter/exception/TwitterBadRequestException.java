package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

public class TwitterBadRequestException extends TwitterClientException {
    public TwitterBadRequestException() {
        super("Bad request.");
    }
}
