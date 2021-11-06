package com.mastercloudapps.thesocialnetworkplanner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class HelloWorldController {


    @GetMapping
    public String index() {
        return "The Social Network Planner starts now!";
    }

}