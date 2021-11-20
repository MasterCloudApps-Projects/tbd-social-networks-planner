package com.mastercloudapps.thesocialnetworkplanner.api.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.RequestTokenResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.service.TwitterAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

@RestController
@Validated
@RequestMapping(value = "/api/v1/twitter/auth")
public class TwitterAuthenticationController {

    private final TwitterAuthenticationService twitterAuthenticationService;

    public TwitterAuthenticationController(TwitterAuthenticationService twitterAuthenticationService) {
        this.twitterAuthenticationService = twitterAuthenticationService;
    }

    @GetMapping()
    public ResponseEntity<RequestTokenResponse> getAuthUrl() throws TwitterClientException {
        String url = this.twitterAuthenticationService.getRequestTokenUrl();
        return ResponseEntity.ok(new RequestTokenResponse(url));
    }

    @GetMapping("/authorize")
    public ResponseEntity authorize(@RequestParam(name = "oauth_verifier") String oauthVerifier) throws TwitterException, TwitterClientException {
        this.twitterAuthenticationService.getAccessToken(oauthVerifier);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(TwitterClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleTwitterClientException(TwitterClientException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
