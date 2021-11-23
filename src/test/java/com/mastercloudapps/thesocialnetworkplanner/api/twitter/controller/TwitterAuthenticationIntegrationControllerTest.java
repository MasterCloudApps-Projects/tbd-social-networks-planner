package com.mastercloudapps.thesocialnetworkplanner.api.twitter.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.service.TwitterAuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TwitterAuthenticationIntegrationControllerTest {
    private static final String BASE_URL = "/api/v1/twitter/auth";
    private static final String AUTHORIZE = "/callback";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TwitterAuthenticationService twitterAuthenticationService;

    @Test
    public void getRequestToken_shouldReturnGrantAccessUrl() throws Exception {
        Mockito.when(this.twitterAuthenticationService.getRequestTokenUrl()).thenReturn("redirect-url-to-twitter-grant-access");

        mockMvc.perform(get(BASE_URL))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.visit_this_url").value("redirect-url-to-twitter-grant-access"));
    }

    @Test
    public void getRequestToken_shouldThrow500INTERNAL_SERVER_ERROR() throws Exception {
        Mockito.when(this.twitterAuthenticationService.getRequestTokenUrl()).thenThrow(new TwitterClientException());

        mockMvc.perform(get(BASE_URL))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    public void authorize_shouldAuthorizeUser() throws Exception {
        Mockito.doNothing().when(this.twitterAuthenticationService).getAccessToken(any());

        mockMvc.perform(get(BASE_URL + AUTHORIZE)
                .param("oauth_verifier","oauthVerifierToken"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void authorize_shouldThrow500INTERNAL_SERVER_ERROR() throws Exception {
        Mockito.doThrow(new TwitterClientException()).when(this.twitterAuthenticationService).getAccessToken(any());

        mockMvc.perform(get(BASE_URL + AUTHORIZE)
                .param("oauth_verifier","oauthVerifierToken"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

}