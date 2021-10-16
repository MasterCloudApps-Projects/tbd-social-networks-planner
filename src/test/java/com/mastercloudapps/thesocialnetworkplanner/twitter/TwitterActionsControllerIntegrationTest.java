package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class TwitterActionsControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TwitterService twitterService;

    private static final String TWEET_ID = "tweetId";
    private static final String BASE_URL = "/twitter/actions";
    private static final String RETWEET = "/retweet";

    @Test
    public void retweet_shouldReturnTweetInformation() throws Exception {
        when(this.twitterService.retweet(any())).thenReturn(TweetResponse.builder()
                .id("id")
                .username("andrea_juanma")
                .text("This is a new tweet.")
                .build());

        mockMvc.perform(post(BASE_URL + RETWEET + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value("andrea_juanma"))
                .andExpect(jsonPath("$.id").value("id"))
                .andExpect(jsonPath("$.text").value("This is a new tweet."));
    }

    @Test
    public void retweet_shouldThrowBadRequestException() throws Exception {
        when(this.twitterService.retweet(any())).thenThrow(TwitterBadRequestException.class);

        mockMvc.perform(post(BASE_URL + RETWEET + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void undoRetweet_shouldReturnTweetInformation() throws Exception {
        when(this.twitterService.undoRetweet(any())).thenReturn(TweetResponse.builder()
                .id("id")
                .username("andrea_juanma")
                .text("This is a new tweet.")
                .build());

        mockMvc.perform(delete(BASE_URL + RETWEET + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value("andrea_juanma"))
                .andExpect(jsonPath("$.id").value("id"))
                .andExpect(jsonPath("$.text").value("This is a new tweet."));
    }

    @Test
    public void undoRetweet_shouldThrowBadRequestException() throws Exception {
        when(this.twitterService.undoRetweet(any())).thenThrow(TwitterBadRequestException.class);

        mockMvc.perform(delete(BASE_URL + RETWEET + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}