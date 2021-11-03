package com.mastercloudapps.thesocialnetworkplanner.twitter.service;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.ScheduleTweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Tweet;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.repository.TweetRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Log4j2
@Service
public class TwitterService {

    private final TwitterClient twitterClient;
    private final TweetRepository tweetRepository;

    public TwitterService(TwitterClient twitterClient, TweetRepository tweetRepository) {
        this.twitterClient = twitterClient;
        this.tweetRepository = tweetRepository;
    }

    public List<TweetResponse> getAllTweets() {
        return tweetRepository.findAll().stream().map(Tweet::toTweetResponse).collect(Collectors.toList());
    }

    public TweetResponse getTweet(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.getTweet(tweetId);

        if (status != null) {
            return TweetResponse.builder().id(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse postTweet(TweetRequest tweetRequest, MultipartFile imageFile) throws TwitterClientException, IOException {
        Date creationDate = new Date();
        Status status = this.twitterClient.postTweet(tweetRequest.getText(), createFile(imageFile));
        if (status != null) {
            Tweet tweet = this.tweetRepository.save(Tweet.builder()
                    .twitterId(status.getId())
                    .text(tweetRequest.getText())
                    .username(status.getUser().getScreenName())
                    .creationDate(creationDate)
                    .updateDate(status.getCreatedAt()).build());
            return TweetResponse.builder().id(tweet.getId()).twitterId(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse deleteTweet(Long tweetId) throws TwitterClientException {
        Optional<Tweet> optionalTweet = this.tweetRepository.findByTwitterId(tweetId);
        Status status = this.twitterClient.deleteTweet(tweetId);

        if (status != null) {
            if (!optionalTweet.isEmpty()) {
                Tweet tweet = optionalTweet.get();
                tweet.delete();
                this.tweetRepository.save(tweet);
            }
            return TweetResponse.builder().id(optionalTweet.orElse(null).getId()).twitterId(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse retweet(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.retweet(tweetId);

        if (status != null) {
            return TweetResponse.builder().id(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse undoRetweet(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.undoRetweet(tweetId);

        if (status != null) {
            return TweetResponse.builder().id(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse like(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.like(tweetId);

        if (status != null) {
            return TweetResponse.builder().id(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse undoLike(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.undoLike(tweetId);

        if (status != null) {
            return TweetResponse.builder().id(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build();
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetRepliesResponse replyTweet(Long tweetId, TweetRequest tweetRequest) throws TwitterClientException {
        List<Status> statuses = this.twitterClient.replyTweet(tweetId, tweetRequest.getText());
        List<TweetResponse> replies = new ArrayList<>();
        if (statuses != null) {
            for (Status status : statuses) {
                replies.add(TweetResponse.builder().id(status.getId()).username(status.getUser().getScreenName()).text(status.getText()).build());
            }
        } else {
            throw new TwitterBadRequestException();
        }
        return new TweetRepliesResponse(tweetId, replies);
    }

    public List<TweetResponse> getUnpublishedTweets() {
        return this.tweetRepository.findByTwitterIdIsNull().stream().map(Tweet::toTweetResponse).collect(Collectors.toList());
    }

    public TweetResponse scheduleTweet(ScheduleTweetRequest tweetResquest) {
        Tweet tweet = this.tweetRepository.save(Tweet.builder().text(tweetResquest.getText()).username(this.twitterClient.getUsername()).scheduledDate(tweetResquest.getPublishDateStore()).build());
        return TweetResponse.builder().id(tweet.getId()).text(tweet.getText()).username(tweet.getUsername()).build();
    }

    public void postScheduledTweets() throws TwitterClientException {
        log.info("Posting scheduled tweets.");
        List<TweetResponse> unpublishedTweets = this.getUnpublishedTweets();
        for (TweetResponse tweetToPublish : unpublishedTweets) {
            Date now = new Date();
            if (tweetToPublish.getScheduledDate().before(now)) {
                if (tweetToPublish.getUsername().equals(this.twitterClient.getUsername())) {
                    Status status = this.twitterClient.postTweet(tweetToPublish.getText(), null);
                    if (status != null) {
                        Tweet tweet = Tweet.builder()
                                .id(tweetToPublish.getId())
                                .twitterId(status.getId())
                                .text(tweetToPublish.getText())
                                .username(tweetToPublish.getUsername())
                                .creationDate(now)
                                .scheduledDate(tweetToPublish.getScheduledDate())
                                .updateDate(status.getCreatedAt()).build();
                        this.tweetRepository.save(tweet);
                        log.info("Published tweet with id: " + tweet.getId());
                    }
                } else {
                    log.info("Tweet with id: " + tweetToPublish.getId() + " can´t be published because the logged user is different.");
                }
            }
        }
    }

    private File createFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            File file = new File("src/main/resources/image_to_upload.jpeg");
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(multipartFile.getBytes());
            }
            return file;
        }
        return null;
    }

}
