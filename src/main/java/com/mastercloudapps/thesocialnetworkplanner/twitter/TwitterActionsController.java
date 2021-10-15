package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(value = "/twitter/actions")
public class TwitterActionsController {
    private final TwitterService twitterService;

    public TwitterActionsController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @PostMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> retweet(@PathVariable String id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.retweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @DeleteMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> undoRetweet(@PathVariable String id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.undoRetweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ExceptionHandler(TwitterBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUnauthorizedTwitterBadRequestException(TwitterBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RetweetForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleRetweetForbiddenException(RetweetForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
