package com.mastercloudapps.thesocialnetworkplanner;

import org.ff4j.FF4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FeatureFlagsInitializer {
    public static String FEATURE_TWITTER = "twitter";
    public static String FEATURE_FACEBOOK = "facebook";
    private final FF4j ff4j;

    public FeatureFlagsInitializer(FF4j ff4j) {
        this.ff4j = ff4j;
    }

    @PostConstruct
    public void initializeFlags() {
        if (!ff4j.exist(FEATURE_TWITTER)) {
            ff4j.createFeature(FEATURE_TWITTER, false);
        }

        if (!ff4j.exist(FEATURE_FACEBOOK)) {
            ff4j.createFeature(FEATURE_FACEBOOK, false);
        }
    }
}
