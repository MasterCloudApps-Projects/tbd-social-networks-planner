package com.mastercloudapps.thesocialnetworkplanner.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.service.TwitterService;
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
    public ResponseEntity<TweetResponse> retweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.retweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @DeleteMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> undoRetweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.undoRetweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<TweetResponse> like(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.like(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<TweetResponse> undoLike(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.undoLike(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @PostMapping("/reply/{id}")
    public ResponseEntity<TweetRepliesResponse> replyTweet(@PathVariable Long id, @RequestBody TweetRequest tweetRequest) throws TwitterClientException {
        TweetRepliesResponse tweetResponse = this.twitterService.replyTweet(id, tweetRequest);
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
