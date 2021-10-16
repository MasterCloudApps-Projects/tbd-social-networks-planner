package com.mastercloudapps.thesocialnetworkplanner.twitter.client.data;

import org.jetbrains.annotations.NotNull;
import twitter4j.*;

import java.util.Date;

public class Status implements twitter4j.Status {
    private boolean isRetweeted;
    private boolean isFavorited;
    private long inReplyToStatusId;

    @Override
    public Date getCreatedAt() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String getText() {
        return "This is a new tweet.";
    }

    @Override
    public int getDisplayTextRangeStart() {
        return 0;
    }

    @Override
    public int getDisplayTextRangeEnd() {
        return 0;
    }

    @Override
    public String getSource() {
        return null;
    }

    @Override
    public boolean isTruncated() {
        return false;
    }

    @Override
    public long getInReplyToStatusId() {
        return this.inReplyToStatusId;
    }

    @Override
    public long getInReplyToUserId() {
        return 0;
    }

    @Override
    public String getInReplyToScreenName() {
        return null;
    }

    @Override
    public GeoLocation getGeoLocation() {
        return null;
    }

    @Override
    public Place getPlace() {
        return null;
    }

    @Override
    public boolean isFavorited() {
        return this.isFavorited;
    }

    @Override
    public boolean isRetweeted() {
        return this.isRetweeted;
    }

    @Override
    public int getFavoriteCount() {
        return 0;
    }

    @Override
    public User getUser() {
        return new User();
    }

    @Override
    public boolean isRetweet() {
        return false;
    }

    @Override
    public twitter4j.Status getRetweetedStatus() {
        return null;
    }

    @Override
    public long[] getContributors() {
        return new long[0];
    }

    @Override
    public int getRetweetCount() {
        return 0;
    }

    @Override
    public boolean isRetweetedByMe() {
        return false;
    }

    @Override
    public long getCurrentUserRetweetId() {
        return 0;
    }

    @Override
    public boolean isPossiblySensitive() {
        return false;
    }

    @Override
    public String getLang() {
        return null;
    }

    @Override
    public Scopes getScopes() {
        return null;
    }

    @Override
    public String[] getWithheldInCountries() {
        return new String[0];
    }

    @Override
    public long getQuotedStatusId() {
        return 0;
    }

    @Override
    public twitter4j.Status getQuotedStatus() {
        return null;
    }

    @Override
    public URLEntity getQuotedStatusPermalink() {
        return null;
    }

    @Override
    public int compareTo(@NotNull twitter4j.Status o) {
        return 0;
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return new UserMentionEntity[0];
    }

    @Override
    public URLEntity[] getURLEntities() {
        return new URLEntity[0];
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return new HashtagEntity[0];
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        return new MediaEntity[0];
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return new SymbolEntity[0];
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return null;
    }

    @Override
    public int getAccessLevel() {
        return 0;
    }

    public void setIsRetweeted(boolean isRetweeted) {
        this.isRetweeted = isRetweeted;
    }

    public void setIsFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public void setInReplyToStatusId(long id) {
        this.inReplyToStatusId = id;
    }
}
