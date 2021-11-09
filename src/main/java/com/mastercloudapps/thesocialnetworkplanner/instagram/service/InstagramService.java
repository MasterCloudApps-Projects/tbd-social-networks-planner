package com.mastercloudapps.thesocialnetworkplanner.instagram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercloudapps.thesocialnetworkplanner.instagram.config.InstagramSession;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.*;
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

@Service
@Log4j2
public class InstagramService {

    @Value("${instagram.login}")
    private String deviceLoginUrl;

    @Value("${instagram.getAccessToken}")
    private String getAccessTokenUrl;

    @Value("${instagram.getPages}")
    private String getPages;

    @Value("${instagram.getIGBussinessAccount}")
    private String getInstagramAccount;

    @Value("${instagram.scope}")
    private String scope;

    @Value("${instagram.redirect_uri}")
    private String redirectUri;

    @Value("${instagram.accessToken}")
    private String loginAccessToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final InstagramSession instagramSession;


    public InstagramService(RestTemplate restTemplate, ObjectMapper objectMapper, InstagramSession instagramSession) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.instagramSession = instagramSession;
    }

    public DeviceLoginResponse login() throws InstagramException {
        DeviceLoginRequest deviceLoginRequest = DeviceLoginRequest.builder()
                .accessToken(loginAccessToken)
                .redirectUri(redirectUri)
                .scope(scope)
                .build();
        DeviceLoginResponse deviceLoginResponse;

        try {
            ResponseEntity<DeviceLoginResponse> response = this.restTemplate.exchange(deviceLoginUrl, HttpMethod.POST, getEntity(deviceLoginRequest.toJsonString()), DeviceLoginResponse.class);
            deviceLoginResponse = response.getBody();

            if (deviceLoginResponse != null) {
                this.instagramSession.setAuthCode(deviceLoginResponse.getCode());
                return deviceLoginResponse;
            } else {
                throw new InstagramException("Error logging into Facebook API");
            }
        } catch (HttpClientErrorException ex) {
            log.error("Exception on [deviceLogin]: " + ex.getMessage());
            if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new InstagramBadRequestException(ex.getMessage());
            } else {
                throw new InstagramException(ex.getMessage());
            }
        }
    }

    public String authenticate() throws InstagramException {
        PagesResponse pagesResponse = this.getPages();
        List<String> igBusinessAccounts = new ArrayList<>();
        if (pagesResponse != null) {
            for (Page page : pagesResponse.getPages()) {
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

    private void getAccessToken() throws InstagramException {
        AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                .code(instagramSession.getAuthCode())
                .accessToken(loginAccessToken)
                .build();

        AccessTokenResponse accessTokenResponse;
        try {
            ResponseEntity<String> responseAccessToken = this.restTemplate.exchange(getAccessTokenUrl, HttpMethod.POST,
                    getEntity(accessTokenRequest.toJsonString()), String.class);
            accessTokenResponse = objectMapper.readValue(responseAccessToken.getBody(), AccessTokenResponse.class);
            if (accessTokenResponse != null) {
                this.instagramSession.setAccessToken(accessTokenResponse.getAccessToken());
            }
        } catch (Exception ex) {
            log.error("Exception getting instagram [accessToken]: " + ex.getMessage());
            throw new InstagramException("Error getting access token from Facebook API");
        }
    }

    private PagesResponse getPages() throws InstagramException {
        this.getAccessToken();
        PagesResponse pageResponse;
        try {
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(getPages).queryParam("access_token",
                    instagramSession.getAccessToken()).build();
            ResponseEntity<String> pagesResponse = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    getEntity(null), String.class);
            pageResponse = objectMapper.readValue(pagesResponse.getBody(), PagesResponse.class);
        } catch (Exception ex) {
            log.error("Error getting [facebook-pages]: " + ex.getMessage());
            throw new InstagramException("Error getting pages from Facebook API.");
        }
        return pageResponse;
    }

    private String getInstagramBusinessAccount(String pageId) throws InstagramException {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("pageId", pageId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(getInstagramAccount)
                .queryParam("access_token", this.instagramSession.getAccessToken())
                .build();
        try {
            ResponseEntity<String> instagramBusinessAccount = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET, getEntity(null), String.class, uriParams);
            if (instagramBusinessAccount.getBody() != null) {
                InstagramBusinessAccountResponse instagramBusinessAccountResponse = objectMapper.readValue(instagramBusinessAccount.getBody(), InstagramBusinessAccountResponse.class);
                if (instagramBusinessAccountResponse.getInstagramBusinessAccount() != null) {
                    return instagramBusinessAccountResponse.getInstagramBusinessAccount().getId();
                }
            }
        } catch (Exception ex) {
            log.error("Error getting [instagram-account]: " + ex.getMessage());
            throw new InstagramException("Error getting instagram-account");
        }
        return null;
    }

    private HttpEntity<String> getEntity(String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }

}
