package com.mastercloudapps.thesocialnetworkplanner.instagram.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercloudapps.thesocialnetworkplanner.instagram.config.InstagramSession;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class InstagramServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private InstagramSession instagramSession;

    private InstagramService instagramService;

    @Before
    public void before() {
        this.instagramService = new InstagramService(this.restTemplate, this.objectMapper, this.instagramSession);

        ReflectionTestUtils.setField(instagramService, "deviceLoginUrl", "https://graph.facebook.com/v2.6/device/login");
        ReflectionTestUtils.setField(instagramService, "accessTokenUrl", "https://graph.facebook.com/v2.6/device/login_status");
        ReflectionTestUtils.setField(instagramService, "pagesUrl", "https://graph.facebook.com/v12.0/me/accounts");
        ReflectionTestUtils.setField(instagramService, "instagramAccountUrl", "https://graph.facebook.com/v12.0/{pageId}?fields=instagram_business_account");
        ReflectionTestUtils.setField(instagramService, "scope", "instagram_basic,instagram_content_publish,pages_read_engagement");
        ReflectionTestUtils.setField(instagramService, "redirectUri", "http://ais-tbd-social-networks.herokuapp.com/");
        ReflectionTestUtils.setField(instagramService, "loginAccessToken", "loginAccessToken");
    }

    @Test
    public void login_shouldReturnDeviceLoginResponse() throws InstagramException {
        DeviceLoginResponse deviceLoginResponse = DeviceLoginResponse.builder()
                .code("code")
                .userCode("userCode")
                .verificationUri("verificationUri")
                .build();
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(DeviceLoginResponse.class)))
                .thenReturn(ResponseEntity.ok(deviceLoginResponse));

        DeviceLoginResponse response = this.instagramService.login();

        verify(this.instagramSession, times(1)).setAuthCode(any());

        Assertions.assertThat(response.getCode()).isEqualTo("code");
        Assertions.assertThat(response.getUserCode()).isEqualTo("userCode");
        Assertions.assertThat(response.getVerificationUri()).isEqualTo("verificationUri");
    }

    @Test(expected = InstagramException.class)
    public void login_shouldReturnNullDeviceLoginResponse() throws InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(DeviceLoginResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        this.instagramService.login();

        verify(this.instagramSession, times(0)).setAuthCode(any());
    }

    @Test(expected = InstagramBadRequestException.class)
    public void login_shouldThrowInstagramBadRequestException() throws InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(DeviceLoginResponse.class)))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));

        this.instagramService.login();

        verify(this.instagramSession, times(0)).setAuthCode(any());
    }

    @Test(expected = InstagramException.class)
    public void login_shouldThrowInstagramException() throws InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(DeviceLoginResponse.class)))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.instagramService.login();

        verify(this.instagramSession, times(0)).setAuthCode(any());
    }

    @Test
    public void authenticate_shouldReturnAccountId() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("body"));
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class), anyMap())).thenReturn(ResponseEntity.ok("body"));
        when(this.objectMapper.readValue(anyString(), eq(AccessTokenResponse.class))).thenReturn(accessTokenResponse());
        when(this.objectMapper.readValue(anyString(), eq(PagesResponse.class))).thenReturn(pagesResponse());
        when(this.objectMapper.readValue(anyString(), eq(InstagramBusinessAccountResponse.class)))
                .thenReturn(instagramBusinessAccountResponse(InstagramBusinessAccount.builder().id("instagram_business_account_id").build()))
                .thenReturn(instagramBusinessAccountResponse(null));
        when(this.instagramSession.getAccessToken()).thenReturn("access_token");
        when(this.instagramSession.getAccountId()).thenReturn("account_id");

        String accountId = this.instagramService.authenticate();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(1)).setAccountId(any());

        Assertions.assertThat(accountId).isEqualTo("account_id");
    }

    @Test
    public void authenticate_shouldReturnNullAccountId() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("body"));
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class), anyMap())).thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));
        when(this.objectMapper.readValue(anyString(), eq(AccessTokenResponse.class))).thenReturn(accessTokenResponse());
        when(this.objectMapper.readValue(anyString(), eq(PagesResponse.class))).thenReturn(pagesResponse());

        String accountId = this.instagramService.authenticate();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(1)).setAccountId(any());

        Assertions.assertThat(accountId).isNull();
    }

    @Test(expected = InstagramException.class)
    public void authenticate_shouldThrowException_whenCallGetPages() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("body"))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));
        when(this.objectMapper.readValue(anyString(), eq(AccessTokenResponse.class))).thenReturn(accessTokenResponse());

       this.instagramService.authenticate();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(0)).setAccountId(any());
    }

    @Test(expected = InstagramException.class)
    public void authenticate_shouldThrowException_whenCallGetAccessToken() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));
        when(this.objectMapper.readValue(anyString(), eq(AccessTokenResponse.class))).thenReturn(accessTokenResponse());

        this.instagramService.authenticate();

        verify(this.instagramSession, times(0)).setAccessToken(any());
        verify(this.instagramSession, times(0)).setAccountId(any());
    }

    private PagesResponse pagesResponse() {
        return PagesResponse.builder()
                .pages(List.of(
                        Page.builder()
                                .id("page_id")
                                .name("page_name")
                                .build(),
                        Page.builder()
                        .id("page_id")
                        .name("page_name")
                        .build()))
                .build();
    }

    private InstagramBusinessAccountResponse instagramBusinessAccountResponse(InstagramBusinessAccount instagramBusinessAccount) {
        return InstagramBusinessAccountResponse.builder()
                .instagramBusinessAccount(instagramBusinessAccount)
                .build();
    }

    private HttpClientErrorException httpClientErrorException(HttpStatus status) {
        return new HttpClientErrorException(status, "text", null, null, null);
    }

    private AccessTokenResponse accessTokenResponse(){
        return AccessTokenResponse.builder()
                .accessToken("accessToken")
                .build();
    }


}