package com.mastercloudapps.thesocialnetworkplanner.api.instagram.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.config.InstagramSession;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.*;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.ResourceResponse;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class InstagramRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private InstagramSession instagramSession;

    private InstagramRestClient instagramRestClient;

    @Before
    public void before() {
        this.instagramRestClient = new InstagramRestClient(restTemplate, objectMapper, instagramSession);

        ReflectionTestUtils.setField(instagramRestClient, "deviceLoginUrl", "https://graph.facebook.com/v2.6/device/login");
        ReflectionTestUtils.setField(instagramRestClient, "accessTokenUrl", "https://graph.facebook.com/v2.6/device/login_status");
        ReflectionTestUtils.setField(instagramRestClient, "pagesUrl", "https://graph.facebook.com/v12.0/me/accounts");
        ReflectionTestUtils.setField(instagramRestClient, "instagramAccountUrl", "https://graph.facebook.com/v12.0/{pageId}?fields=instagram_business_account");
        ReflectionTestUtils.setField(instagramRestClient, "scope", "instagram_basic,instagram_content_publish,pages_read_engagement");
        ReflectionTestUtils.setField(instagramRestClient, "redirectUri", "http://ais-tbd-social-networks.herokuapp.com/");
        ReflectionTestUtils.setField(instagramRestClient, "loginAccessToken", "loginAccessToken");
        ReflectionTestUtils.setField(instagramRestClient, "imageContainerUrl", "https://graph.facebook.com/{accountId}/media");
        ReflectionTestUtils.setField(instagramRestClient, "publishImage", "https://graph.facebook.com/{accountId}/media_publish");
        ReflectionTestUtils.setField(instagramRestClient, "mediaInfoUrl", "https://graph.facebook.com/v12.0/{mediaId}");
        ReflectionTestUtils.setField(instagramRestClient, "allMedia", "https://graph.facebook.com/v12.0/{accountId}/media");
    }

    @Test
    public void login_shouldReturnDeviceLoginResponse() throws InstagramException {
        InstagramDeviceLoginResponse instagramDeviceLoginResponse = InstagramDeviceLoginResponse.builder()
                .code("code")
                .userCode("userCode")
                .verificationUri("verificationUri")
                .build();
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramDeviceLoginResponse.class)))
                .thenReturn(ResponseEntity.ok(instagramDeviceLoginResponse));

        InstagramDeviceLoginResponse response = this.instagramRestClient.login();

        verify(this.instagramSession, times(1)).setAuthCode(any());

        Assertions.assertThat(response.getCode()).isEqualTo("code");
        Assertions.assertThat(response.getUserCode()).isEqualTo("userCode");
        Assertions.assertThat(response.getVerificationUri()).isEqualTo("verificationUri");
    }

    @Test
    public void login_shouldReturnNullDeviceLoginResponse() throws InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramDeviceLoginResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        InstagramDeviceLoginResponse response = this.instagramRestClient.login();

        verify(this.instagramSession, times(0)).setAuthCode(any());
        Assertions.assertThat(response).isNull();
    }

    @Test(expected = InstagramBadRequestException.class)
    public void login_shouldThrowInstagramBadRequestException() throws InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramDeviceLoginResponse.class)))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));

        this.instagramRestClient.login();

        verify(this.instagramSession, times(0)).setAuthCode(any());
    }

    @Test(expected = InstagramException.class)
    public void login_shouldThrowInstagramException() throws InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramDeviceLoginResponse.class)))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.instagramRestClient.login();

        verify(this.instagramSession, times(0)).setAuthCode(any());
    }

    @Test
    public void getAccount_shouldReturnAccountId() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("body"));
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class), anyMap())).thenReturn(ResponseEntity.ok("body"));
        when(this.objectMapper.readValue(anyString(), eq(InstagramAccessTokenResponse.class))).thenReturn(accessTokenResponse());
        when(this.objectMapper.readValue(anyString(), eq(InstagramPagesResponse.class))).thenReturn(pagesResponse());
        when(this.objectMapper.readValue(anyString(), eq(InstagramBusinessAccountResponse.class)))
                .thenReturn(instagramBusinessAccountResponse(InstagramBusinessAccount.builder().id("instagram_business_account_id").build()))
                .thenReturn(instagramBusinessAccountResponse(null));
        when(this.instagramSession.getAccessToken()).thenReturn("access_token");
        when(this.instagramSession.getAccountId()).thenReturn("account_id");

        String accountId = this.instagramRestClient.getAccount();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(1)).setAccountId(any());

        Assertions.assertThat(accountId).isEqualTo("account_id");
    }

    @Test
    public void getAccount_shouldReturnNullAccountId() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("body"));
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class), anyMap())).thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));
        when(this.objectMapper.readValue(anyString(), eq(InstagramAccessTokenResponse.class))).thenReturn(accessTokenResponse());
        when(this.objectMapper.readValue(anyString(), eq(InstagramPagesResponse.class))).thenReturn(pagesResponse());

        String accountId = this.instagramRestClient.getAccount();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(1)).setAccountId(any());

        Assertions.assertThat(accountId).isNull();
    }

    @Test(expected = InstagramBadRequestException.class)
    public void getAccount_shouldThrowInstagramBadRequestException_whenCallGetPages() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("body"))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));
        when(this.objectMapper.readValue(anyString(), eq(InstagramAccessTokenResponse.class))).thenReturn(accessTokenResponse());

        this.instagramRestClient.getAccount();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(0)).setAccountId(any());
    }

    @Test(expected = InstagramException.class)
    public void getAccount_shouldThrowInstagramException_whenCallGetPages() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("body"))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));
        when(this.objectMapper.readValue(anyString(), eq(InstagramAccessTokenResponse.class))).thenReturn(accessTokenResponse());

        this.instagramRestClient.getAccount();

        verify(this.instagramSession, times(1)).setAccessToken(any());
        verify(this.instagramSession, times(0)).setAccountId(any());
    }

    @Test(expected = InstagramBadRequestException.class)
    public void getAccount_shouldThrowException_whenCallGetAccessToken() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));
        when(this.objectMapper.readValue(anyString(), eq(InstagramAccessTokenResponse.class))).thenReturn(accessTokenResponse());

        this.instagramRestClient.getAccount();

        verify(this.instagramSession, times(0)).setAccessToken(any());
        verify(this.instagramSession, times(0)).setAccountId(any());
    }

    @Test(expected = InstagramException.class)
    public void getAccount_shouldThrowInstagramException_whenCallGetAccessToken() throws JsonProcessingException, InstagramException {
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));
        when(this.objectMapper.readValue(anyString(), eq(InstagramAccessTokenResponse.class))).thenReturn(accessTokenResponse());

        this.instagramRestClient.getAccount();

        verify(this.instagramSession, times(0)).setAccessToken(any());
        verify(this.instagramSession, times(0)).setAccountId(any());
    }

    @Test(expected = InstagramBadRequestException.class)
    public void post_shouldThrowInstagramBadRequestException_whenAccountIdIsNull() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn(null);

        this.instagramRestClient.post(resourceResponse(), "caption");
    }

    @Test(expected = InstagramBadRequestException.class)
    public void post_shouldThrowInstagramBadRequestException_whenHttpClientException() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));

        this.instagramRestClient.post(resourceResponse(), "caption");
    }

    @Test(expected = InstagramException.class)
    public void post_shouldThrowInstagramException_whenHttpClientException() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.instagramRestClient.post(resourceResponse(), "caption");
    }

    @Test(expected = InstagramException.class)
    public void post_shouldThrowInstagramException_whenContainerIdIsNull() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(null));

        this.instagramRestClient.post(resourceResponse(), "caption");
    }

    @Test
    public void post_shouldReturnIgPostId() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(InstagramImageIdResponse.builder().id("containerId").build()))
                .thenReturn(ResponseEntity.ok(InstagramImageIdResponse.builder().id("imageId").build()));

        String imageId = this.instagramRestClient.post(resourceResponse(), "caption");

        Mockito.verify(this.restTemplate, times(2))
                .exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap());

        Assertions.assertThat(imageId).isEqualTo("imageId");
    }

    @Test(expected = InstagramBadRequestException.class)
    public void post_shouldThrowInstagramBadRequestException_whenHttpClientErrorOnPublishingImage() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(InstagramImageIdResponse.builder().id("containerId").build()))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));

        this.instagramRestClient.post(resourceResponse(), "caption");

        Mockito.verify(this.restTemplate, times(2))
                .exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap());
    }

    @Test(expected = InstagramException.class)
    public void post_shouldThrowInstagramException_whenHttpClientErrorOnPublishingImage() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(InstagramImageIdResponse.builder().id("containerId").build()))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.instagramRestClient.post(resourceResponse(), "caption");

        Mockito.verify(this.restTemplate, times(2))
                .exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap());
    }

    @Test
    public void post_shouldReturnNullImageId() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(InstagramImageIdResponse.builder().id("containerId").build()))
                .thenReturn(ResponseEntity.ok(null));

        String imageId = this.instagramRestClient.post(resourceResponse(), "caption");

        Mockito.verify(this.restTemplate, times(2))
                .exchange(anyString(), any(), any(), eq(InstagramImageIdResponse.class), anyMap());
        Assertions.assertThat(imageId).isNull();
    }

    @Test(expected = InstagramBadRequestException.class)
    public void getPostInfo_shouldThrowInstagramBadRequestException_whenHttpClientException400BADREQUEST() throws InstagramException {
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramPostInfoResponse.class), anyMap()))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));

        this.instagramRestClient.getPostInfo("id");
    }

    @Test(expected = InstagramException.class)
    public void getPostInfo_shouldThrowInstagramException_whenHttpClientExceptionOtherException() throws InstagramException {
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramPostInfoResponse.class), anyMap()))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.instagramRestClient.getPostInfo("id");
    }

    @Test
    public void getPostInfo_shouldReturnNull() throws InstagramException {
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramPostInfoResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(null));

        InstagramPostInfoResponse response = this.instagramRestClient.getPostInfo("id");
        Assertions.assertThat(response).isNull();
    }

    @Test
    public void getPostInfo_shouldReturnInformation() throws InstagramException {
        InstagramPostInfoResponse instagramPostInfoResponse = InstagramPostInfoResponse.builder()
                .caption("caption")
                .likeCount(0)
                .id("id")
                .mediaType("IMAGE").build();
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramPostInfoResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(instagramPostInfoResponse));

        InstagramPostInfoResponse response = this.instagramRestClient.getPostInfo("id");

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo("id");
        Assertions.assertThat(response.getLikeCount()).isEqualTo(0);
        Assertions.assertThat(response.getMediaType()).isEqualTo("IMAGE");
        Assertions.assertThat(response.getCaption()).isEqualTo("caption");
    }

    @Test(expected = InstagramBadRequestException.class)
    public void getAllMedia_shouldThrowInstagramBadRequestException_whenAccountIdIsNull() throws InstagramException {
        this.instagramRestClient.getAllMedia();
    }

    @Test(expected = InstagramBadRequestException.class)
    public void getAllMedia_shouldThrowInstagramBadRequestException_whenHttpClientException400BADREQUEST() throws InstagramException {
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramMediaResponse.class), anyMap()))
                .thenThrow(httpClientErrorException(HttpStatus.BAD_REQUEST));

        this.instagramRestClient.getAllMedia();
    }

    @Test(expected = InstagramException.class)
    public void getAllMedia_shouldThrowInstagramException_whenHttpClientExceptionOtherException() throws InstagramException {
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramMediaResponse.class), anyMap()))
                .thenThrow(httpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.instagramRestClient.getAllMedia();
    }

    @Test
    public void getAllMedia_shouldReturnNull() throws InstagramException {
        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramMediaResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(null));

        InstagramMediaResponse response = this.instagramRestClient.getAllMedia();
        Assertions.assertThat(response).isNull();
    }

    @Test
    public void getAllMedia_shouldReturnListOfMediaIds() throws InstagramException {
        InstagramMediaResponse instagramMediaResponse = instagramMediaResponse();

        when(this.instagramSession.getAccountId()).thenReturn("accountId");
        when(this.instagramSession.getAccessToken()).thenReturn("accessToken");
        when(this.restTemplate.exchange(anyString(), any(), any(), eq(InstagramMediaResponse.class), anyMap()))
                .thenReturn(ResponseEntity.ok(instagramMediaResponse));

        InstagramMediaResponse response = this.instagramRestClient.getAllMedia();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getPostIds().size()).isEqualTo(2);
        Assertions.assertThat(response.getPostIds().get(0).getId()).isEqualTo("postId");
        Assertions.assertThat(response.getPostIds().get(1).getId()).isEqualTo("postId2");
    }

    private InstagramMediaResponse instagramMediaResponse() {
        return InstagramMediaResponse.builder()
                .postIds(List.of(
                        InstagramImageIdResponse.builder()
                                .id("postId").build(),
                        InstagramImageIdResponse.builder()
                                .id("postId2").build())).build();
    }

    private InstagramPagesResponse pagesResponse() {
        return InstagramPagesResponse.builder()
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

    private InstagramAccessTokenResponse accessTokenResponse() {
        return InstagramAccessTokenResponse.builder()
                .accessToken("accessToken")
                .build();
    }

    private ResourceResponse resourceResponse() {
        return ResourceResponse.builder().url("imageUrl").build();
    }

}