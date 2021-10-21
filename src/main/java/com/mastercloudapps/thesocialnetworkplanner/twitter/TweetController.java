package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Validated
@RequestMapping(value = "/twitter/tweet")
public class TweetController {
    private final TwitterService twitterService;

    public TweetController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponse> getTweet(@PathVariable String id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.getTweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @PostMapping()
    public ResponseEntity<TweetResponse> postTweet(@RequestBody TweetRequest tweetRequest, @RequestParam(value = "image",
            required = false) MultipartFile multipartFile) throws
            TwitterClientException, IOException {
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetRequest, multipartFile);
        return ResponseEntity.ok(tweetResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TweetResponse> deleteTweet(@PathVariable String id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.deleteTweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ExceptionHandler(TweetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleTweetNotFoundException(TweetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TwitterClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleTwitterClientException(TwitterClientException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedTwitterClientException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleUnauthorizedTwitterClientException(UnauthorizedTwitterClientException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(TwitterBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUnauthorizedTwitterBadRequestException(TwitterBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
