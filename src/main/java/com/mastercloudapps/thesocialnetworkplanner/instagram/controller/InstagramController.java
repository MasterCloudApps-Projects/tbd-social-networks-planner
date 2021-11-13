package com.mastercloudapps.thesocialnetworkplanner.instagram.controller;

import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.service.InstagramService;
import com.mastercloudapps.thesocialnetworkplanner.resource.model.ResourceResponse;
import com.mastercloudapps.thesocialnetworkplanner.resource.service.ResourceService;
import lombok.extern.log4j.Log4j2;
import org.ff4j.FF4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import static com.mastercloudapps.thesocialnetworkplanner.ff4jconfig.FeatureFlagsInitializer.FEATURE_GET_POST_INFO;

@RestController
@Validated
@Log4j2
@RequestMapping(value = "/instagram")
public class InstagramController {

    private final InstagramService instagramService;
    private final ResourceService resourceService;
    private final FF4j ff4j;

    public InstagramController(InstagramService instagramService, ResourceService resourceService, FF4j ff4j) {
        this.instagramService = instagramService;
        this.resourceService = resourceService;
        this.ff4j = ff4j;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() throws InstagramException {
        InstagramDeviceLoginResponse instagramDeviceLoginResponse = this.instagramService.login();
        return ResponseEntity.ok("Enter this code " + instagramDeviceLoginResponse.getUserCode() + " on " + instagramDeviceLoginResponse.getVerificationUri());
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authenticate() throws InstagramException {
        String accountId = this.instagramService.authenticate();
        return ResponseEntity.ok(accountId != null
                ? "Using Instagram Business Account: " + this.instagramService.authenticate()
                : "No Instagram Business Account to work with.");
    }

    @PostMapping("/image")
    public ResponseEntity<String> post(@RequestParam("caption") String caption,
                                       @RequestParam(value = "image") MultipartFile multipartFile) throws InstagramException {
        ResourceResponse resource = this.resourceService.createImage(multipartFile);
        return ResponseEntity.ok(this.instagramService.post(resource, caption));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity getPostInfo(@PathVariable("id") @NotNull String id) throws InstagramException {
        if (ff4j.check(FEATURE_GET_POST_INFO)) {
            return ResponseEntity.ok(this.instagramService.getPostInfo(id));
        }
        return ResponseEntity.ok("TBD");
    }

    @GetMapping("/posts")
    public ResponseEntity getAllMedia() throws InstagramException {
        if (ff4j.check(FEATURE_GET_POST_INFO)) {
            return ResponseEntity.ok(this.instagramService.getAllMedia());
        }
        return ResponseEntity.ok("TBD");
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
