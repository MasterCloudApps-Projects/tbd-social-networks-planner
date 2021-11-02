package com.mastercloudapps.thesocialnetworkplanner.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.ScheduleTweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.service.TwitterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.TwitterException;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/twitter/tweets")
public class TweetController {
    private final TwitterService twitterService;

    public TweetController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @GetMapping()
    public ResponseEntity<List<TweetResponse>> getAllTweets() {
        List<TweetResponse> tweetResponse = this.twitterService.getAllTweets();
        return ResponseEntity.ok(tweetResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponse> getTweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.getTweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @PostMapping()
    public ResponseEntity<TweetResponse> postTweet(@Valid @RequestBody TweetRequest tweetRequest, @RequestParam(value = "image",
            required = false) MultipartFile multipartFile) throws
            TwitterClientException, IOException {
        TweetResponse tweetResponse = this.twitterService.postTweet(tweetRequest, multipartFile);
        return ResponseEntity.ok(tweetResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TweetResponse> deleteTweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.deleteTweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @GetMapping("/unpublished")
    public ResponseEntity<List<TweetResponse>> getUnpublishedTweets() {
        List<TweetResponse> tweetResponse = this.twitterService.getUnpublishedTweets();
        return ResponseEntity.ok(tweetResponse);
    }

    @PostMapping("/schedule")
    public ResponseEntity<TweetResponse> scheduleTweet(@RequestBody @Valid ScheduleTweetRequest tweetRequest) throws ParseException, TwitterException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        tweetRequest.setPublishDateStore(formatter.parse(tweetRequest.getPublishDate()));
        TweetResponse tweetResponse = twitterService.scheduleTweet(tweetRequest);
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

    @ExceptionHandler({TwitterBadRequestException.class, ParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUnauthorizedTwitterBadRequestException(TwitterBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
