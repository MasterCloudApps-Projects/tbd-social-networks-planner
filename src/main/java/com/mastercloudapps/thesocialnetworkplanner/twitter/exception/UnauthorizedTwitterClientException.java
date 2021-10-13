package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

public class UnauthorizedTwitterClientException extends TwitterClientException {
    public UnauthorizedTwitterClientException() {
        super("You are not authorized to do this request");
    }
}
