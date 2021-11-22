package com.mastercloudapps.thesocialnetworkplanner.api.instagram.service;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.client.InstagramRestClient;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramNotAuthorizeException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramImageIdResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.repository.InstagramRepository;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.service.ResourceService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class InstagramServiceTest {

    @Mock
    private InstagramRestClient instagramRestClient;

    @Mock
    private ResourceService resourceService;

    @Mock
    private InstagramRepository instagramRepository;

    private InstagramService instagramService;

    private MockMultipartFile multipartFile;

    @Before
    public void before() {
        this.instagramService = new InstagramService(instagramRestClient, resourceService, instagramRepository);
        this.multipartFile = new MockMultipartFile("image", "image.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Image for instagram".getBytes());
    }

    @Test
    public void login_shouldReturnInstagramDeviceLoginResponse() throws InstagramException {
        when(this.instagramRestClient.login())
                .thenReturn(InstagramDeviceLoginResponse.builder()
                        .userCode("XXXX")
                        .verificationUri("verificationUri")
                        .build());
        String response = this.instagramService.login();

        verify(this.instagramRestClient, times(1)).login();
        Assertions.assertThat(response).isEqualTo("Enter this code XXXX on verificationUri");
    }

    @Test(expected = InstagramException.class)
    public void login_shouldThrowInstagramException_whenInstagramDeviceLoginResponseIsNull() throws InstagramException {
        when(this.instagramRestClient.login())
                .thenReturn(null);
        this.instagramService.login();

        verify(this.instagramRestClient, times(1)).login();
    }

    @Test(expected = InstagramException.class)
    public void login_shouldThrowInstagramException() throws InstagramException {
        when(this.instagramRestClient.login()).thenThrow(new InstagramException());
        this.instagramService.login();

        verify(this.instagramRestClient, times(1)).login();
    }

    @Test
    public void getAccount_shouldReturnAccountId() throws InstagramException {
        when(this.instagramRestClient.getAccount()).thenReturn("accountId");
        String accountId = this.instagramService.getAccount();

        Assertions.assertThat(accountId).isEqualTo("accountId");
        verify(this.instagramRestClient, times(1)).getAccount();
    }

    @Test(expected = InstagramNotAuthorizeException.class)
    public void getAccount_shouldThrowInstagramNotAuthorizeException_whenAccountIdIsNull() throws InstagramException {
        when(this.instagramRestClient.getAccount()).thenReturn(null);
        String accountId = this.instagramService.getAccount();

        Assertions.assertThat(accountId).isNull();
        verify(this.instagramRestClient, times(1)).getAccount();
    }

    @Test
    public void post_shouldReturnPostId() throws InstagramException {
        when(this.instagramRestClient.post(any(), any()))
                .thenReturn("1111");
        String postId = this.instagramService.post(this.multipartFile,
                "caption");

        Assertions.assertThat(postId).isEqualTo("1111");
        verify(this.instagramRestClient, times(1)).post(any(), anyString());
    }

    @Test
    public void post_shouldReturnPostIdNull() throws InstagramException {
        when(this.instagramRestClient.post(any(), any()))
                .thenReturn(null);
        String postId = this.instagramService.post(this.multipartFile,
                "caption");

        Assertions.assertThat(postId).isNull();
        verify(this.instagramRestClient, times(1)).post(any(), anyString());
    }

    @Test(expected = InstagramException.class)
    public void post_shouldThrowInstagramException() throws InstagramException {
        when(this.instagramRestClient.post(any(), any()))
                .thenThrow(new InstagramException());
        this.instagramService.post(this.multipartFile,
                "caption");
        verify(this.instagramRestClient, times(1)).post(any(), anyString());
    }

    @Test(expected = InstagramException.class)
    public void getPostInfo_shouldThrowInstagramException() throws InstagramException {
        when(this.instagramRestClient.getPostInfo(anyString()))
                .thenThrow(new InstagramException());
        this.instagramService.getPostInfo("postId");
        verify(this.instagramRestClient, times(1)).getPostInfo(anyString());
    }

    @Test
    public void getPostInfo_shouldReturnInstagramPostInfoResponse() throws InstagramException {
        when(this.instagramRestClient.getPostInfo(anyString()))
                .thenReturn(InstagramPostInfoResponse.builder()
                        .id("id")
                        .caption("caption")
                        .build());
        InstagramPostInfoResponse instagramPostInfoResponse = this.instagramService.getPostInfo("postId");

        verify(this.instagramRestClient, times(1)).getPostInfo(anyString());
        Assertions.assertThat(instagramPostInfoResponse.getCaption()).isEqualTo("caption");
        Assertions.assertThat(instagramPostInfoResponse.getId()).isEqualTo("id");
    }

    @Test
    public void getPostInfo_shouldReturnInstagramPostInfoResponseNull() throws InstagramException {
        when(this.instagramRestClient.getPostInfo(anyString()))
                .thenReturn(null);
        InstagramPostInfoResponse instagramPostInfoResponse = this.instagramService.getPostInfo("postId");

        verify(this.instagramRestClient, times(1)).getPostInfo(anyString());
        Assertions.assertThat(instagramPostInfoResponse).isNull();
    }

    @Test(expected = InstagramException.class)
    public void getAllMedia_shouldThrowInstagramException() throws InstagramException {
        when(this.instagramRestClient.getAllMedia())
                .thenThrow(new InstagramException());
        this.instagramService.getAllMedia();
        verify(this.instagramRestClient, times(1)).getAllMedia();
    }

    @Test
    public void getAllMedia_shouldReturnInstagramPostInfoResponse() throws InstagramException {
        when(this.instagramRestClient.getAllMedia())
                .thenReturn(InstagramMediaResponse.builder().postIds(List.of(
                        InstagramImageIdResponse.builder()
                                .id("postId")
                                .build())).build());
        InstagramMediaResponse instagramMediaResponse = this.instagramService.getAllMedia();

        verify(this.instagramRestClient, times(1)).getAllMedia();
        Assertions.assertThat(instagramMediaResponse.getPostIds().get(0).getId()).isEqualTo("postId");
    }

    @Test
    public void getAllMedia_shouldReturnInstagramPostInfoResponseNull() throws InstagramException {
        when(this.instagramRestClient.getAllMedia())
                .thenReturn(null);
        InstagramMediaResponse instagramMediaResponse = this.instagramService.getAllMedia();

        verify(this.instagramRestClient, times(1)).getAllMedia();
        Assertions.assertThat(instagramMediaResponse).isNull();
    }
}