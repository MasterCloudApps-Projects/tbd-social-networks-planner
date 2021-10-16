package com.mastercloudapps.thesocialnetworkplanner.twitter.client.data;

import twitter4j.Query;
import twitter4j.RateLimitStatus;
import twitter4j.Status;

import java.util.List;

public class QueryResult implements twitter4j.QueryResult {
    List<Status> replies;

    @Override
    public long getSinceId() {
        return 0;
    }

    @Override
    public long getMaxId() {
        return 0;
    }

    @Override
    public String getRefreshURL() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double getCompletedIn() {
        return 0;
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public List<Status> getTweets() {
        return this.replies;
    }

    @Override
    public Query nextQuery() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return null;
    }

    @Override
    public int getAccessLevel() {
        return 0;
    }

    public void setReplies(List<Status> statuses) {
        this.replies = statuses;
    }

}

