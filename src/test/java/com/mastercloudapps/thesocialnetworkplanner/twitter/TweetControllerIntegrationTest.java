package com.mastercloudapps.thesocialnetworkplanner.twitter;

import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.model.TweetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
public class TweetControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TwitterService twitterService;
    private static final Long TWEET_ID = 1L;
    private static final String BASE_URL = "/twitter/tweets";

    @Test
    public void getAllTweets_shouldReturnTweetInformation() throws Exception {
        when(this.twitterService.getAllTweets()).thenReturn(
                Arrays.asList(
                        TweetResponse.builder().id(TWEET_ID).username("andrea_juanma").text("This is a new tweet.").build(),
                        TweetResponse.builder().id(2L).username("jguijars").text("This is an old tweet.").build()));

        mockMvc.perform(get(BASE_URL + "/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].username").value("andrea_juanma"))
                .andExpect(jsonPath("$[0].id").value(TWEET_ID))
                .andExpect(jsonPath("$[0].text").value("This is a new tweet."))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getTweet_shouldReturnTweetInformation() throws Exception {
        when(this.twitterService.getTweet(any())).thenReturn(TweetResponse.builder()
                .id(TWEET_ID)
                .username("andrea_juanma")
                .text("This is a new tweet.")
                .build());

        mockMvc.perform(get(BASE_URL + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value("andrea_juanma"))
                .andExpect(jsonPath("$.id").value(TWEET_ID))
                .andExpect(jsonPath("$.text").value("This is a new tweet."));
    }

    @Test
    public void getTweet_shouldThrowBadRequestException() throws Exception {
        when(this.twitterService.getTweet(any())).thenThrow(TwitterBadRequestException.class);

        mockMvc.perform(get(BASE_URL + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void postTweet_shouldReturnTweetInformation() throws Exception {
        String jsonRequest = "{ \"text\" : \"This is a new tweet\" }";
        MockMultipartFile multipartFile = new MockMultipartFile ("file", "image.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Image for tweet".getBytes());
        when(this.twitterService.postTweet(any(), any())).thenReturn(TweetResponse.builder()
                .id(TWEET_ID)
                .username("andrea_juanma")
                .text("This is a new tweet.")
                .build());

        mockMvc.perform(multipart(BASE_URL).file(multipartFile).contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value("andrea_juanma"))
                .andExpect(jsonPath("$.id").value(TWEET_ID))
                .andExpect(jsonPath("$.text").value("This is a new tweet."));
    }

    @Test
    public void postTweet_shouldThrowTwitterBadRequestException() throws Exception {
        String jsonRequest = "{ \"text\" : \"This is a new tweet\" }";
        MockMultipartFile multipartFile = new MockMultipartFile ("file", "image.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Image for tweet".getBytes());
        when(this.twitterService.postTweet(any(),any())).thenThrow(TwitterBadRequestException.class);

        mockMvc.perform(multipart(BASE_URL).file(multipartFile).contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void deleteTweet_shouldReturnTweetInformation() throws Exception {
        when(this.twitterService.deleteTweet(any())).thenReturn(TweetResponse.builder()
                .id(TWEET_ID)
                .username("andrea_juanma")
                .text("This is a new tweet.")
                .build());

        mockMvc.perform(delete(BASE_URL + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value("andrea_juanma"))
                .andExpect(jsonPath("$.id").value(TWEET_ID))
                .andExpect(jsonPath("$.text").value("This is a new tweet."));
    }

    @Test
    public void deleteTweet_shouldThrowBadRequestException() throws Exception {
        when(this.twitterService.deleteTweet(any())).thenThrow(TwitterBadRequestException.class);

        mockMvc.perform(delete(BASE_URL + "/" + TWEET_ID))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}