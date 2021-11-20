package com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception;

public class InstagramNotAuthorizeException extends InstagramException {
    public InstagramNotAuthorizeException() {
        super("User not authenticated.");
    }
}
