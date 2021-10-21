package com.mastercloudapps.thesocialnetworkplanner.twitter.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class TwitterConfiguration {

    @Bean
    public TwitterSession tweetSession() {
        return TwitterSession.getInstance();
    }

}

