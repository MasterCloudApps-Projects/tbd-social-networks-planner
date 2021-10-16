package com.mastercloudapps.thesocialnetworkplanner.twitter.exception;

import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Action;

public class RetweetForbiddenException extends TwitterClientException {
    public RetweetForbiddenException(boolean isDoneAction, Action action) {
        super(isDoneAction ? "This tweet has been already " + action.getErrorActionMessage()
                : "This tweet has not been " + action.getErrorActionMessage() + " by you.");
    }

}
