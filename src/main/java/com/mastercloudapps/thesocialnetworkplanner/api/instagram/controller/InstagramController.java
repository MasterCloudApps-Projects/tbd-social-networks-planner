package com.mastercloudapps.thesocialnetworkplanner.api.instagram.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramNotAuthorizeException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.LoginCallbackResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.PostResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.service.InstagramService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@Validated
@Log4j2
@RequestMapping(value = "/api/v1/instagram")
public class InstagramController {

    private final InstagramService instagramService;

    public InstagramController(InstagramService instagramService) {
        this.instagramService = instagramService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() throws InstagramException {
        return ResponseEntity.ok(this.instagramService.login());
    }

    @GetMapping("/login/callback/")
    public ResponseEntity<LoginCallbackResponse> callback() throws InstagramException {
        String accountId = this.instagramService.getAccount();
        return ResponseEntity.ok(LoginCallbackResponse.builder().accountId(accountId).build());
    }

    @PostMapping("/post")
    public ResponseEntity<PostResponse> post(@RequestParam("caption") String caption, @RequestParam(value = "image")
            MultipartFile multipartFile) throws InstagramException {
        return ResponseEntity.ok(PostResponse.builder().postId(this.instagramService.post(multipartFile, caption)).build());
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<InstagramPostInfoResponse> getPostInfo(@PathVariable("id") @NotNull String id) throws InstagramException {
        return ResponseEntity.ok(this.instagramService.getPostInfo(id));
    }

    @GetMapping("/posts")
    public ResponseEntity<InstagramMediaResponse> getAllMedia() throws InstagramException {
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
