package com.mastercloudapps.thesocialnetworkplanner.instagram.exception;

public class InstagramNotAuthorizeException extends InstagramException {
    public InstagramNotAuthorizeException() {
        super("User not authenticated.");
    }
}
