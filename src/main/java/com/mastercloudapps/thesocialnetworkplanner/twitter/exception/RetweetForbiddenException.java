package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

public class RetweetForbiddenException extends TwitterClientException {
    public RetweetForbiddenException(boolean isRetweet) {
        super(isRetweet ? "This tweet has been already retweeted" : "This tweet is not retweet by you.");
    }

}
