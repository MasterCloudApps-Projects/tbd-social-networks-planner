package com.mastercloudapps.thesocialnetworkplanner.twitter.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

@Component
@Log4j2
public class TwitterAuthentication {
    @Value("${oauth.accessTokenSecret}")
    private String accessTokenSecret;
    @Value("${oauth.consumerSecret}")
    private String consumerSecret;
    @Value("${oauth.consumerKey}")
    private String consumerKey;
    @Value("${oauth.accessToken}")
    private String accessToken;
    @Value("${oauth.screenName}")
    private String screenName;

    @PostConstruct
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
