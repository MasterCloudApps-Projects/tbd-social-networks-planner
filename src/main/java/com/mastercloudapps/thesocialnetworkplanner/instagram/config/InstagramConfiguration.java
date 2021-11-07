package com.mastercloudapps.thesocialnetworkplanner.instagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstagramConfiguration {

    @Bean
    public InstagramSession instagramSession(){
        return InstagramSession.getInstance();
    }
}
