package com.mastercloudapps.thesocialnetworkplanner.instagram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.*;
import lombok.extern.log4j.Log4j2;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercloudapps.thesocialnetworkplanner.FeatureFlagsInitializer.FEATURE_FACEBOOK_LOGIN;

@RestController
@Validated
@Log4j2
@RequestMapping(value = "/instagram")
public class InstagramController {

    @Value("${instagram.login}")
    private String deviceLoginUrl;

    @Value("${instagram.getAccessToken}")
    private String getAccessTokenUrl;

    @Value("${instagram.getPages}")
    private String getPages;

    @Value("${instagram.getIGBussinessAccount}")
    private String getInstagramAccount;

    @Value("${instagram.accessToken}")
    private String accessToken;

    @Value("${instagram.scope}")
    private String scope;

    @Value("${instagram.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FF4j ff4j;
    private String code;

    public InstagramController(RestTemplate restTemplate, ObjectMapper objectMapper, FF4j ff4j) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.ff4j = ff4j;
    }

    @GetMapping("/auth")
    public ResponseEntity authenticate() {
        if (ff4j.check(FEATURE_FACEBOOK_LOGIN)) {
            DeviceLoginRequest deviceLoginRequest = DeviceLoginRequest.builder().accessToken(accessToken).redirectUri(redirectUri).scope(scope).build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> deviceLoginEntity = new HttpEntity(deviceLoginRequest.toJsonString(), httpHeaders);
            ResponseEntity<DeviceLoginResponse> response = this.restTemplate.exchange(deviceLoginUrl, HttpMethod.POST, deviceLoginEntity, DeviceLoginResponse.class);
            DeviceLoginResponse deviceLoginResponse = response.getBody();
            if (deviceLoginResponse != null) {
                code = deviceLoginResponse.getCode();
            }
            return ResponseEntity.ok("Enter this code " + deviceLoginResponse.getUserCode() + " on " + deviceLoginResponse.getVerificationUri());
        } else {
            return ResponseEntity.ok("TBD");
        }
    }

    @GetMapping("/accounts")
    public List<String> getInstagramAccounts() throws JsonProcessingException {
        if(ff4j.check(FEATURE_FACEBOOK_LOGIN)) {
            AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                    .code(code)
                    .accessToken(accessToken)
                    .build();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> accessTokenEntity = new HttpEntity(accessTokenRequest.toJsonString(), httpHeaders);
            ResponseEntity<String> responseAccessToken = this.restTemplate.exchange(getAccessTokenUrl, HttpMethod.POST, accessTokenEntity, String.class);
            AccessTokenResponse accessTokenResponse = objectMapper.readValue(responseAccessToken.getBody(), AccessTokenResponse.class);

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(getPages).queryParam("access_token", accessTokenResponse.getAccessToken()).build();
            ResponseEntity<String> pagesResponse = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET, accessTokenEntity, String.class);
            PagesResponse pageResponse = objectMapper.readValue(pagesResponse.getBody(), PagesResponse.class);

            List<String> igBusinessAccounts = new ArrayList<>();
            for (Page page : pageResponse.getPages()) {
                Map<String, String> uriParams = new HashMap<>();
                uriParams.put("pageId", page.getId());
                builder = UriComponentsBuilder.fromHttpUrl(getInstagramAccount)
                        .queryParam("access_token", accessTokenResponse.getAccessToken())
                        .build();

                try {
                    ResponseEntity<String> instagramBusinessAccount = this.restTemplate.exchange(builder.toUriString(), HttpMethod.GET, accessTokenEntity, String.class, uriParams);
                    if (instagramBusinessAccount.getBody() != null) {
                        InstagramBusinessAccountResponse instagramBusinessAccountResponse = objectMapper.readValue(instagramBusinessAccount.getBody(), InstagramBusinessAccountResponse.class);
                        if (instagramBusinessAccountResponse.getInstagramBusinessAccount() != null) {
                            igBusinessAccounts.add(instagramBusinessAccountResponse.getInstagramBusinessAccount().getId());
                        }
                    }
                } catch (HttpClientErrorException ex) {
                    log.error("No ig account associated to: " + page.getId());
                }
            }

            return igBusinessAccounts;
        } else {
            return List.of("TBD");
        }
    }
}
