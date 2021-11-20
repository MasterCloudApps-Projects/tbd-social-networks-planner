package com.mastercloudapps.thesocialnetworkplanner.api.instagram.config;

public class InstagramSession {

    private String accessToken;
    private String accountId;
    private String authCode;

    private static InstagramSession instagramSession;

    private InstagramSession() {
    }

    public static InstagramSession getInstance() {
        if (instagramSession != null) {
            return instagramSession;
        }
        return new InstagramSession();
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }


}
