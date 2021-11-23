package com.mastercloudapps.thesocialnetworkplanner.api.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.RetweetForbiddenException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.service.TwitterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(value = "/api/v1/twitter/action")
public class TwitterActionsController {
    private final TwitterService twitterService;

    public TwitterActionsController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @ApiOperation(value = "Retweet.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @PostMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> retweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.retweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Undo retweet.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @DeleteMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> undoRetweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.undoRetweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Like a tweet.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @PostMapping("/like/{id}")
    public ResponseEntity<TweetResponse> like(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.like(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Unlike a tweet.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @DeleteMapping("/like/{id}")
    public ResponseEntity<TweetResponse> undoLike(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.undoLike(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Reply to tweet. (Thread)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweets's details.", response = TweetRepliesResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "Not found.")
    })
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
