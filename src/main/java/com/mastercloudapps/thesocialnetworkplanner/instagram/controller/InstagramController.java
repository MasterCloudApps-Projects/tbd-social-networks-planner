package com.mastercloudapps.thesocialnetworkplanner.instagram.controller;

import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.DeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.service.InstagramService;
import com.mastercloudapps.thesocialnetworkplanner.resource.service.ResourceService;
import lombok.extern.log4j.Log4j2;
import org.ff4j.FF4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@Log4j2
@RequestMapping(value = "/instagram")
public class InstagramController {

    private final InstagramService instagramService;
    private final ResourceService resourceService;

    public InstagramController(InstagramService instagramService, ResourceService resourceService) {
        this.instagramService = instagramService;
        this.resourceService = resourceService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() throws InstagramException {
        DeviceLoginResponse deviceLoginResponse = this.instagramService.login();
        return ResponseEntity.ok("Enter this code " + deviceLoginResponse.getUserCode() + " on " + deviceLoginResponse.getVerificationUri());
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
        String resourceUrl = this.resourceService.createImage(multipartFile);
        return ResponseEntity.ok(this.instagramService.post(resourceUrl, caption));
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
