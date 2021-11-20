package com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception;

public class TwitterBadRequestException extends TwitterClientException {
    public TwitterBadRequestException() {
        super("Bad request.");
    }
}
