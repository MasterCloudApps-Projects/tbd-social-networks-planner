package com.mastercloudapps.thesocialnetworkplanner.api.instagram.service;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.client.InstagramRestClient;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramNotAuthorizeException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.Instagram;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramImageIdResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.repository.InstagramRepository;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.Resource;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.service.ResourceService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        InstagramLoginResponse response = this.instagramService.login();

        verify(this.instagramRestClient, times(1)).login();
        Assertions.assertThat(response.getEnterCode()).isEqualTo("XXXX");
        Assertions.assertThat(response.getVisitUrl()).isEqualTo("verificationUri");
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
        when(this.instagramRestClient.getAccount()).thenReturn("prueba");
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

    @Test
    public void scheduleInstagram_shouldReturnInstagramInformation() throws InstagramException {
        Instagram instagram = instagram();
        Resource resource = resource();
        when(this.instagramRestClient.getAccount()).thenReturn("andrea_juanma");
        when(this.instagramRepository.save(any())).thenReturn(instagram);
        when(this.resourceService.saveResource(any())).thenReturn(resource);
        when(this.resourceService.createImage(any())).thenReturn("photo_url");
        InstagramResponse instagramResponse = this.instagramService.scheduleInstagram("This is a new post.", this.multipartFile, new Date());
        assertThat(instagramResponse.getId()).isEqualTo(instagram.getId());
        assertThat(instagramResponse.getUsername()).isEqualTo(instagram.getUsername());
        assertThat(instagramResponse.getCaption()).isEqualTo(instagram.getText());
    }


    private Instagram instagram() {
        return Instagram.builder().id(1111L).text("This is a new post.").username("andrea_juanma").resource(Arrays.asList(resource())).build();
    }

    private Resource resource() {
        return Resource.builder().id(1111L).url("photo_url").build();
    }
}