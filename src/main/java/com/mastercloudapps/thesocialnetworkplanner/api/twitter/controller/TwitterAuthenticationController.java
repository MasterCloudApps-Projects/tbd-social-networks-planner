package com.mastercloudapps.thesocialnetworkplanner.api.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.RequestTokenResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.service.TwitterAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import twitter4j.TwitterException;

@RestController
@Validated
@RequestMapping(value = "/api/v1/twitter/auth")
public class TwitterAuthenticationController {

    private final TwitterAuthenticationService twitterAuthenticationService;

    public TwitterAuthenticationController(TwitterAuthenticationService twitterAuthenticationService) {
        this.twitterAuthenticationService = twitterAuthenticationService;
    }

    @ApiOperation(value = "Get Twitter authentication URL.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Get authentication URL from Twitter API.", response = RequestTokenResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error. Error occurred in Twitter Service")
    })
    @GetMapping()
    public ResponseEntity<RequestTokenResponse> getAuthUrl() throws TwitterClientException {
        String url = this.twitterAuthenticationService.getRequestTokenUrl();
        return ResponseEntity.ok(new RequestTokenResponse(url));
    }

    @ApiOperation(value = "Authentication URL callback.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Get user authenticated."),
            @ApiResponse(code = 500, message = "Internal Server Error. Error occurred in Twitter Service.")
    })
    @GetMapping("/callback")
    public ResponseEntity authorize(@RequestParam(name = "oauth_verifier") String oauthVerifier) throws TwitterClientException {
        this.twitterAuthenticationService.getAccessToken(oauthVerifier);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(TwitterClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleTwitterClientException(TwitterClientException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
