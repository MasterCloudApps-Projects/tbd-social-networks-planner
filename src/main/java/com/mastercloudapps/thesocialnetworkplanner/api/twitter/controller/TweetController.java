package com.mastercloudapps.thesocialnetworkplanner.api.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TweetNotFoundException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.UnauthorizedTwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.ScheduleTweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.service.TwitterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.ff4j.FF4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.mastercloudapps.thesocialnetworkplanner.config.ff4jconfig.FeatureFlagsInitializer.FEATURE_NEW_SCHEDULER;

@RestController
@Validated
@RequestMapping(value = "/api/v1/twitter")
public class TweetController {
    private final TwitterService twitterService;
    private final FF4j ff4j;

    public TweetController(TwitterService twitterService, FF4j ff4j) {
        this.twitterService = twitterService;
        this.ff4j = ff4j;
    }

    @ApiOperation(value = "Get all tweets of account.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweets's details.", responseContainer = "List", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @GetMapping("/tweets")
    public ResponseEntity<List<TweetResponse>> getAllTweets() {
        List<TweetResponse> tweetResponse = this.twitterService.getAllTweets();
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Get tweet details by id.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @GetMapping("/tweet/{id}")
    public ResponseEntity<TweetResponse> getTweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.getTweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Post new tweet.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. New tweet's details response.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @PostMapping("/tweet")
    public ResponseEntity<TweetResponse> postTweet(
            @RequestParam("text") @Size(max = 280) @NotNull String text, @RequestParam(value = "image",
            required = false) MultipartFile multipartFile) throws
            TwitterClientException, IOException {
        TweetResponse tweetResponse = this.twitterService.postTweet(text, multipartFile);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Delete tweet by id.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Deleted tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<TweetResponse> deleteTweet(@PathVariable Long id) throws TwitterClientException {
        TweetResponse tweetResponse = this.twitterService.deleteTweet(id);
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Get all programmed and unpublished tweets.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Unpublished tweets's details.", responseContainer = "List", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @GetMapping("/tweets/unpublished")
    public ResponseEntity<List<TweetResponse>> getUnpublishedTweets() {
        List<TweetResponse> tweetResponse = this.twitterService.getUnpublishedTweets();
        return ResponseEntity.ok(tweetResponse);
    }

    @ApiOperation(value = "Schedule new tweet to post it at concrete time.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Schedule tweet's details.", response = TweetResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 404, message = "Not found.")
    })
    @PostMapping("/tweet/schedule")
    public ResponseEntity<TweetResponse> scheduleTweet(@RequestBody @Valid ScheduleTweetRequest tweetRequest) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        tweetRequest.setPublishDateStore(formatter.parse(tweetRequest.getPublishDate()));
        TweetResponse tweetResponse = twitterService.scheduleTweet(tweetRequest);
        return ResponseEntity.ok(tweetResponse);
    }

    @Scheduled(fixedDelay = 3600000)
    public void postScheduledTweets() throws TwitterClientException {
        if (!ff4j.check(FEATURE_NEW_SCHEDULER)) {
            this.twitterService.postScheduledTweets();
        }
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
