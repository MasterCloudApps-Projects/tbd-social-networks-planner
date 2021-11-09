package com.mastercloudapps.thesocialnetworkplanner.instagram.controller;

import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.DeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.service.InstagramService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
public class InstagramControllerIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private InstagramService instagramService;

    private static final String BASE_URL = "/instagram";
    private static final String LOGIN = "/login";
    private static final String AUTHENTICATE = "/auth";

    @Test
    public void login_shouldReturn200OK() throws Exception {
        when(this.instagramService.login()).thenReturn(DeviceLoginResponse.builder()
                .userCode("XXXXX")
                .code("XXXXX")
                .verificationUri("https://www.facebook.com/device")
                .build());

        mockMvc.perform(get(BASE_URL + LOGIN))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("Enter this code XXXXX on https://www.facebook.com/device"));
    }

    @Test
    public void login_shouldReturn500INTERNAL_SERVER_ERROR() throws Exception {
        when(this.instagramService.login()).thenThrow(new InstagramException());

        mockMvc.perform(get(BASE_URL + LOGIN))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    public void authenticate_shouldReturn200OK() throws Exception {
        when(this.instagramService.authenticate()).thenReturn("instagram_business_account_id");

        mockMvc.perform(get(BASE_URL + AUTHENTICATE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("Using Instagram Business Account: instagram_business_account_id"));
    }

    @Test
    public void authenticate_shouldReturn200OK_noAccount() throws Exception {
        when(this.instagramService.authenticate()).thenReturn(null);

        mockMvc.perform(get(BASE_URL + AUTHENTICATE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("No Instagram Business Account to work with."));
    }

    @Test
    public void authenticate_shouldReturn500INTERNAL_SERVER_ERROR() throws Exception {
        when(this.instagramService.authenticate()).thenThrow(new InstagramException());

        mockMvc.perform(get(BASE_URL + AUTHENTICATE))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    public void authenticate_shouldReturn400BAD_REQUEST() throws Exception {
        when(this.instagramService.authenticate()).thenThrow(new InstagramBadRequestException("Error message"));

        mockMvc.perform(get(BASE_URL + AUTHENTICATE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$").value("Error message"));
    }
}