package com.mastercloudapps.thesocialnetworkplanner.twitter.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@Configuration
@Log4j2
public class TwitterConfiguration {
    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;
    @Value("${twitter.consumerSecret}")
    private String consumerSecret;
    @Value("${twitter.consumerKey}")
    private String consumerKey;
    @Value("${twitter.accessToken}")
    private String accessToken;
    @Value("${twitter.screenName}")
    private String screenName;

    ConfigurationBuilder cb = new ConfigurationBuilder();

    @Bean
    public Twitter Twitter() throws TwitterException {
        initializeTwitter4jVariables();
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        log.info(twitterFactory.getInstance().getOAuthAccessToken().getToken());
        log.info(twitterFactory.getInstance().getOAuthAccessToken().getTokenSecret());
        return twitterFactory.getInstance();
    }

    private void initializeTwitter4jVariables() {
        log.info("consumerKey: {}", consumerKey);
        log.info("consumerSecret: {}", consumerSecret);
        log.info("accessToken: {}", accessToken);
        log.info("screenName: {}", screenName);
        log.info("accessTokenSecret: {}", accessTokenSecret);

        if (null != consumerKey && null != consumerSecret && null != accessToken && null != screenName && null != accessTokenSecret) {
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(accessTokenSecret);
        }
    }
}

