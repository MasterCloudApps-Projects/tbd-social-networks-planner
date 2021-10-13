package com.mastercloudapps.thesocialnetworkplanner.twitter.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

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

    @Bean
    public Twitter Twitter() {
        initializeTwitter4jVariables();
        return new TwitterFactory().getInstance();
    }

    private void initializeTwitter4jVariables() {
        log.info("consumerKey: {}", consumerKey);
        log.info("consumerSecret: {}", consumerSecret);
        log.info("accessToken: {}", accessToken);
        log.info("screenName: {}", screenName);
        log.info("accessTokenSecret: {}", accessTokenSecret);

        String propertiesName = "twitter4j.properties";
        Properties properties = new Properties();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesName);
            properties.load(inputStream);

            if (null != consumerKey && null != consumerSecret && null != accessToken && null != screenName && null != accessTokenSecret) {
                properties.setProperty("oauth.consumerKey", consumerKey);
                properties.setProperty("oauth.consumerSecret", consumerSecret);
                properties.setProperty("oauth.accessToken", accessToken);
                properties.setProperty("oauth.screenName", screenName);
                properties.setProperty("oauth.accessTokenSecret", accessTokenSecret);
                outputStream = new FileOutputStream("src/main/resources/twitter4j.properties");
                properties.store(outputStream, "twitter4j.properties");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}

