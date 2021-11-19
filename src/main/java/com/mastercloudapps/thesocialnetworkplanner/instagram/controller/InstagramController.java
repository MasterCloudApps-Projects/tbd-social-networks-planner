package com.mastercloudapps.thesocialnetworkplanner.instagram.controller;

import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramNotAuthorizeException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.service.InstagramService;
import com.mastercloudapps.thesocialnetworkplanner.resource.model.ResourceResponse;
import com.mastercloudapps.thesocialnetworkplanner.resource.service.ResourceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

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
        return ResponseEntity.ok(this.instagramService.login());
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authenticate() throws InstagramException {
        String accountId = this.instagramService.authenticate();
        return ResponseEntity.ok("Using Instagram Business Account: " + accountId);
    }

    @PostMapping("/post")
    public ResponseEntity<String> post(@RequestParam("caption") String caption, @RequestParam(value = "image")
            MultipartFile multipartFile) throws InstagramException {
        ResourceResponse resource = this.resourceService.createImage(multipartFile);
        return ResponseEntity.ok(this.instagramService.post(resource, caption));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity getPostInfo(@PathVariable("id") @NotNull String id) throws InstagramException {
        return ResponseEntity.ok(this.instagramService.getPostInfo(id));
    }

    @GetMapping("/posts")
    public ResponseEntity getAllMedia() throws InstagramException {
        return ResponseEntity.ok(this.instagramService.getAllMedia());
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

    @ExceptionHandler({InstagramNotAuthorizeException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleInstagramNotAuthorizeException(InstagramNotAuthorizeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
