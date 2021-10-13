package com.mastercloudapps.thesocialnetworkplanner.twitter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@Configuration
public class TwitterConfiguration {

    @Bean
    public Twitter Twitter() {
        return new TwitterFactory().getInstance();
    }
}

