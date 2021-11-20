package com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception;

public class InstagramException extends Exception {
    @Override
    public String getMessage() {
        return message;
    }

    private String message;
    public InstagramException(){}

    public InstagramException(String message) {
        this.message = message;
    }
}

