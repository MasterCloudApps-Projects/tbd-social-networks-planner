package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

public class TwitterClientException extends Exception {
    private final String message;

    public TwitterClientException(String message) {
        this.message = message;
    }

    public TwitterClientException() {
        this.message = "Error occurred in Twitter Service";
    }

    public String getMessage() {
        return this.message;
    }
}
