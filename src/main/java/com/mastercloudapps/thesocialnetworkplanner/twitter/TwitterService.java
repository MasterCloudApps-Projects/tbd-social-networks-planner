package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.client.TwitterClient;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Tweet;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRepliesResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetRequest;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import com.mastercloudapps.thesocialnetworkplanner.twitter.repository.TweetRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.Status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
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
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse postTweet(TweetRequest tweetRequest, MultipartFile imageFile) throws TwitterClientException, IOException {
        Status status = this.twitterClient.postTweet(tweetRequest.getText(), createFile(imageFile));

        if (status != null) {
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse deleteTweet(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.deleteTweet(tweetId);

        if (status != null) {
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse retweet(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.retweet(tweetId);

        if (status != null) {
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse undoRetweet(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.undoRetweet(tweetId);

        if (status != null) {
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse like(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.like(tweetId);

        if (status != null) {
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetResponse undoLike(Long tweetId) throws TwitterClientException {
        Status status = this.twitterClient.undoLike(tweetId);

        if (status != null) {
            return new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText());
        } else {
            throw new TwitterBadRequestException();
        }
    }

    public TweetRepliesResponse replyTweet(Long tweetId, TweetRequest tweetRequest) throws TwitterClientException {
        List<Status> statuses = this.twitterClient.replyTweet(tweetId, tweetRequest.getText());
        List<TweetResponse> replies = new ArrayList<>();
        if (statuses != null) {
            for (Status status : statuses) {
                replies.add(new TweetResponse(status.getId(), status.getUser().getScreenName(), status.getText()));
            }
        } else {
            throw new TwitterBadRequestException();
        }
        return new TweetRepliesResponse(tweetId, replies);
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
