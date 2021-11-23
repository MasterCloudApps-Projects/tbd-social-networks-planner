package com.mastercloudapps.thesocialnetworkplanner.api.schedule;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.Instagram;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.Tweet;

public interface Visitor {

    void postOnTwitter(Tweet tweet);
    void postOnInstagram(Instagram instagram);
}
