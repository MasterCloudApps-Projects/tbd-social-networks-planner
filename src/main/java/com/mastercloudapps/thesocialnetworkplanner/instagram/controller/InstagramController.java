package com.mastercloudapps.thesocialnetworkplanner.instagram.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.*;
import com.mastercloudapps.thesocialnetworkplanner.instagram.service.InstagramService;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.TwitterClientException;
import com.mastercloudapps.thesocialnetworkplanner.twitter.exception.UnauthorizedTwitterClientException;
import lombok.extern.log4j.Log4j2;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mastercloudapps.thesocialnetworkplanner.ff4jconfig.FeatureFlagsInitializer.FEATURE_INSTAGRAM_SERVICE;

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

    private final InstagramService instagramService;

    public InstagramController(RestTemplate restTemplate, ObjectMapper objectMapper, FF4j ff4j, InstagramService instagramService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.ff4j = ff4j;
        this.instagramService = instagramService;
    }

    @GetMapping("/login")
    public ResponseEntity login() throws InstagramException {
        DeviceLoginResponse deviceLoginResponse;
        if (!ff4j.check(FEATURE_INSTAGRAM_SERVICE)) {
            DeviceLoginRequest deviceLoginRequest = DeviceLoginRequest.builder().accessToken(accessToken).redirectUri(redirectUri).scope(scope).build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> deviceLoginEntity = new HttpEntity(deviceLoginRequest.toJsonString(), httpHeaders);
            ResponseEntity<DeviceLoginResponse> response = this.restTemplate.exchange(deviceLoginUrl, HttpMethod.POST, deviceLoginEntity, DeviceLoginResponse.class);
            deviceLoginResponse = response.getBody();
            if (deviceLoginResponse != null) {
                code = deviceLoginResponse.getCode();
            }
        } else {
            deviceLoginResponse = this.instagramService.login();
        }
        return ResponseEntity.ok("Enter this code " + deviceLoginResponse.getUserCode() + " on " + deviceLoginResponse.getVerificationUri());
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authenticate() throws JsonProcessingException, InstagramException {
        String accountId;
        if (!ff4j.check(FEATURE_INSTAGRAM_SERVICE)) {
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
            accountId = igBusinessAccounts.stream().findFirst().get();
        } else {
            accountId = this.instagramService.authenticate();
        }

        return ResponseEntity.ok(accountId != null
                ? "Using Instagram Business Account: " + this.instagramService.authenticate()
                : "No Instagram Business Account to work with.");
    }

    @ExceptionHandler(InstagramException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInstagramException(InstagramException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler({InstagramBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInstagramBadRequestException(InstagramBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
