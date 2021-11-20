package com.mastercloudapps.thesocialnetworkplanner.api.twitter.model;

public enum Action {
    RETWEET("retweeted"),
    LIKE("liked");

    private final String errorActionMessage;

    Action(String errorActionMessage) {
        this.errorActionMessage = errorActionMessage;
    }

    public String getErrorActionMessage(){
        return this.errorActionMessage;
    }
}
