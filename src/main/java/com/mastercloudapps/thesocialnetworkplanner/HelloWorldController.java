package com.mastercloudapps.thesocialnetworkplanner;

import org.ff4j.FF4j;
import org.ff4j.spring.autowire.FF4JFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mastercloudapps.thesocialnetworkplanner.FeatureFlagsInitializer.FEATURE_FACEBOOK;

@RestController
@RequestMapping(value = "/")
public class HelloWorldController {

    private final FF4j ff4j;

    public HelloWorldController(FF4j ff4j) {
        this.ff4j = ff4j;
    }

    @GetMapping
    public String index() {
        if (ff4j.check(FEATURE_FACEBOOK)) {
            return "Feature facebook enabled!";
        } else {
            return "The Social Network Planner starts now!";
        }
    }

}