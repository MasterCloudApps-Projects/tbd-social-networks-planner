package com.mastercloudapps.thesocialnetworkplanner.instagram.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercloudapps.thesocialnetworkplanner.instagram.config.InstagramSession;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.*;
import com.mastercloudapps.thesocialnetworkplanner.resource.model.ResourceResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class InstagramRestClient implements InstagramClient {

    @Value("${instagram.login}")
    private String deviceLoginUrl;

    @Value("${instagram.getAccessToken}")
    private String accessTokenUrl;

    @Value("${instagram.getPages}")
    private String pagesUrl;

    @Value("${instagram.getIGBussinessAccount}")
    private String instagramAccountUrl;

    @Value("${instagram.scope}")
    private String scope;

    @Value("${instagram.redirect_uri}")
    private String redirectUri;

    @Value("${instagram.accessToken}")
    private String loginAccessToken;

    @Value("${instagram.container}")
    private String imageContainerUrl;

    @Value("${instagram.publishImage}")
    private String publishImage;

    @Value("${instagram.getMediaInfo}")
    private String mediaInfoUrl;

    @Value("${instagram.getAllMedia}")
    private String allMedia;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final InstagramSession instagramSession;

    public InstagramRestClient(RestTemplate restTemplate, ObjectMapper objectMapper, InstagramSession instagramSession) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.instagramSession = instagramSession;
    }

    @Override
    public InstagramDeviceLoginResponse login() throws InstagramException {
        DeviceLoginRequest deviceLoginRequest = DeviceLoginRequest.builder()
                .accessToken(loginAccessToken)
                .redirectUri(redirectUri)
                .scope(scope)
                .build();
        InstagramDeviceLoginResponse instagramDeviceLoginResponse;

        try {
            log.info("Request to Facebook Business API: " + deviceLoginUrl);
            ResponseEntity<InstagramDeviceLoginResponse> response = this.restTemplate.exchange(deviceLoginUrl, HttpMethod.POST, getEntity(deviceLoginRequest.toJsonString()), InstagramDeviceLoginResponse.class);
            instagramDeviceLoginResponse = response.getBody();

            if (instagramDeviceLoginResponse != null) {
                this.instagramSession.setAuthCode(instagramDeviceLoginResponse.getCode());
                return instagramDeviceLoginResponse;
            } else {
                throw new InstagramException("Error logging into Facebook API");
            }
        } catch (HttpClientErrorException ex) {
            log.error("Exception on [deviceLogin]: " + ex.getMessage());
            if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new InstagramBadRequestException("Error login: " + ex.getMessage());
            } else {
                throw new InstagramException(ex.getMessage());
            }
        }
    }

    @Override
    public String authenticate() throws InstagramException {
        this.getAccessToken();
        InstagramPagesResponse instagramPagesResponse = this.getPages();
        List<String> igBusinessAccounts = new ArrayList<>();
        if (instagramPagesResponse != null) {
            for (Page page : instagramPagesResponse.getPages()) {
                try {
                    String igAccount = this.getInstagramBusinessAccount(page.getId());
                    if (igAccount != null) {
                        igBusinessAccounts.add(igAccount);
                    }
                } catch (Exception ex) {
                    log.warn("Exception getting instagram account. " + ex.getMessage());
                }
            }
            this.instagramSession.setAccountId(igBusinessAccounts.stream().findFirst().orElse(null));
        }
        return this.instagramSession.getAccountId();
    }

    @Override
    public String post(ResourceResponse resource, String caption) throws InstagramException {
        String containerId = this.createContainer(resource.getUrl(), caption);
        if (containerId == null) {
            throw new InstagramException("Cannot publish. Image container does not exist.");
        }
        return this.publishImage(containerId);
    }

    @Override
    public InstagramPostInfoResponse getPostInfo(String id) throws InstagramException {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("mediaId", id);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(mediaInfoUrl)
                .queryParam("access_token", this.instagramSession.getAccessToken())
                .build();
        try {
            log.info("Request to Facebook Business API: " + mediaInfoUrl);
            ResponseEntity<InstagramPostInfoResponse> postInfoResponseResponse = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, getEntity(null), InstagramPostInfoResponse.class, uriParams);
            return postInfoResponseResponse.getBody() != null ? postInfoResponseResponse.getBody() : null;
        } catch (HttpClientErrorException ex) {
            log.error("Error getting [image-info]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException("Error getting info for postId: " + id + ", Error: " + ex.getMessage());
            }
            throw new InstagramException(ex.getMessage());
        }
    }

    @Override
    public InstagramMediaResponse getAllMedia() throws InstagramException {
        this.checkAccountId();
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("accountId", this.instagramSession.getAccountId());
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(allMedia)
                .queryParam("access_token", this.instagramSession.getAccessToken())
                .build();
        try {
            log.info("Request to Facebook Business API: " + allMedia);
            ResponseEntity<InstagramMediaResponse> instagramMediaResponse = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, getEntity(null), InstagramMediaResponse.class, uriParams);
            return instagramMediaResponse.getBody() != null ? instagramMediaResponse.getBody() : null;
        } catch (HttpClientErrorException ex) {
            log.error("Error getting [all-media]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException(ex.getMessage());
            }
            throw new InstagramException("Error getting [all-media]: " + ex.getMessage());
        }
    }

    private void getAccessToken() throws InstagramException {
        AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                .code(instagramSession.getAuthCode())
                .accessToken(loginAccessToken)
                .build();

        InstagramAccessTokenResponse instagramAccessTokenResponse;
        this.waitForFacebook();
        try {
            log.info("Request to Facebook Business API: " + accessTokenUrl);
            ResponseEntity<String> responseAccessToken = this.restTemplate.exchange(accessTokenUrl, HttpMethod.POST,
                    getEntity(accessTokenRequest.toJsonString()), String.class);
            instagramAccessTokenResponse = objectMapper.readValue(responseAccessToken.getBody(), InstagramAccessTokenResponse.class);
            if (instagramAccessTokenResponse != null) {
                this.instagramSession.setAccessToken(instagramAccessTokenResponse.getAccessToken());
            }
        } catch (HttpClientErrorException ex) {
            log.error("Exception getting instagram [accessToken]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException("Error getting accessToken: " + ex.getMessage());
            }
            throw new InstagramException("Error getting access token from Facebook API");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private InstagramPagesResponse getPages() throws InstagramException {
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(pagesUrl).queryParam("access_token",
                instagramSession.getAccessToken()).build();
        InstagramPagesResponse pageResponse = null;
        this.waitForFacebook();
        try {
            log.info("Request to Facebook Business API: " + pagesUrl);
            ResponseEntity<String> pagesResponse = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    getEntity(null), String.class);
            pageResponse = objectMapper.readValue(pagesResponse.getBody(), InstagramPagesResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error getting [facebook-pages]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException("Error on [get-pages]:" + ex.getMessage());
            }
            throw new InstagramException("Error getting pages from Facebook API.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return pageResponse;
    }

    private String getInstagramBusinessAccount(String pageId) throws InstagramException {
        this.waitForFacebook();
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("pageId", pageId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(instagramAccountUrl)
                .queryParam("access_token", this.instagramSession.getAccessToken())
                .build();
        try {
            log.info("Request to Facebook Business API: " + instagramAccountUrl);
            ResponseEntity<String> instagramBusinessAccount = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET, getEntity(null), String.class, uriParams);
            if (instagramBusinessAccount.getBody() != null) {
                InstagramBusinessAccountResponse instagramBusinessAccountResponse = objectMapper.readValue(instagramBusinessAccount.getBody(), InstagramBusinessAccountResponse.class);
                if (instagramBusinessAccountResponse.getInstagramBusinessAccount() != null) {
                    return instagramBusinessAccountResponse.getInstagramBusinessAccount().getId();
                }
            }
        } catch (HttpClientErrorException ex) {
            log.error("Error getting [instagram-account]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException("Error getting [instagram-account]:" + ex.getMessage());
            }
            throw new InstagramException("Error getting instagram-account");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createContainer(String url, String caption) throws InstagramException {
        this.checkAccountId();
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("accountId", this.instagramSession.getAccountId());
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(imageContainerUrl)
                .queryParam("access_token", this.instagramSession.getAccessToken())
                .queryParam("caption", caption)
                .queryParam("image_url", url)
                .build();
        this.waitForFacebook();
        try {
            log.info("Request to Facebook Business API: " + imageContainerUrl);
            ResponseEntity<InstagramImageIdResponse> instagramBusinessAccount = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.POST, getEntity(null), InstagramImageIdResponse.class, uriParams);
            return instagramBusinessAccount.getBody() != null ? instagramBusinessAccount.getBody().getId() : null;
        } catch (HttpClientErrorException ex) {
            log.error("Error getting [image-container]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException("Error creating container for image: " + ex.getMessage());
            }
            throw new InstagramException(ex.getMessage());
        }
    }

    private String publishImage(String imageContainerId) throws InstagramException {
        this.checkAccountId();
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("accountId", this.instagramSession.getAccountId());
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(publishImage)
                .queryParam("access_token", this.instagramSession.getAccessToken())
                .queryParam("creation_id", imageContainerId)
                .build();
        this.waitForFacebook();
        try {
            log.info("Request to Facebook Business API: " + publishImage);
            ResponseEntity<InstagramImageIdResponse> instagramBusinessAccount = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.POST, getEntity(null), InstagramImageIdResponse.class, uriParams);
            return instagramBusinessAccount.getBody() != null ? instagramBusinessAccount.getBody().getId() : null;
        } catch (HttpClientErrorException ex) {
            log.error("Error getting [publish-image]: " + ex.getMessage());
            if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
                throw new InstagramBadRequestException(ex.getMessage());
            }
            throw new InstagramException("Error getting [publish-image]: " + ex.getMessage());
        }
    }

    private HttpEntity<String> getEntity(String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }

    private void waitForFacebook() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private void checkAccountId() throws InstagramBadRequestException {
        if (this.instagramSession.getAccountId() == null) {
            throw new InstagramBadRequestException("No Instagram Business account");
        }
    }
}
