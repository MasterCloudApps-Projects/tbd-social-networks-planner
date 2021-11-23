package com.mastercloudapps.thesocialnetworkplanner.api.instagram.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramBadRequestException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramNotAuthorizeException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.*;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.service.InstagramService;
import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.TweetResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Validated
@Log4j2
@RequestMapping(value = "/api/v1/instagram")
public class InstagramController {

    private final InstagramService instagramService;

    public InstagramController(InstagramService instagramService) {
        this.instagramService = instagramService;
    }

    @ApiOperation(value = "Get login code and URL from Facebook API.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Code and URL to give permissions to the app.", response = InstagramLoginResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/login")
    public ResponseEntity<InstagramLoginResponse> login() throws InstagramException {
        return ResponseEntity.ok(this.instagramService.login());
    }

    @ApiOperation(value = "Facebook API callback.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Authenticated Instagram Business account id", response = LoginCallbackResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/login/callback/")
    public ResponseEntity<LoginCallbackResponse> callback() throws InstagramException {
        String accountId = this.instagramService.getAccount();
        return ResponseEntity.ok(LoginCallbackResponse.builder().accountId(accountId).build());
    }

    @ApiOperation(value = "Publish new post on Instagram.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Post's details", response = PostResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/post")
    public ResponseEntity<PostResponse> post(@RequestParam("caption") String caption, @RequestParam(value = "image")
            MultipartFile multipartFile) throws InstagramException {
        return ResponseEntity.ok(PostResponse.builder().postId(this.instagramService.post(multipartFile, caption)).build());
    }

    @ApiOperation(value = "Get post's details from Instagram.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Post's details", response = InstagramPostInfoResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/post/{id}")
    public ResponseEntity<InstagramPostInfoResponse> getPostInfo(@PathVariable("id") @NotNull String id) throws InstagramException {
        return ResponseEntity.ok(this.instagramService.getPostInfo(id));
    }

    @ApiOperation(value = "Get all posts's ids from Instagram Business Account.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Posts's ids", response = InstagramMediaResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("/posts")
    public ResponseEntity<InstagramMediaResponse> getAllMedia() throws InstagramException {
        return ResponseEntity.ok(this.instagramService.getAllMedia());
    }

    @ApiOperation(value = "Schedule new post to publish on Instagram.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful. Post's details", response = InstagramResponse.class),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("/schedule")
    public ResponseEntity<InstagramResponse> scheduleInstagram(@RequestParam("caption") String caption, @RequestParam(value = "image")
            MultipartFile multipartFile, @RequestParam("publishDate") String publishDate) throws ParseException, InstagramException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date publishDateStore = formatter.parse(publishDate);
        InstagramResponse instagramResponse = this.instagramService.scheduleInstagram(caption, multipartFile, publishDateStore);
        return ResponseEntity.ok(instagramResponse);
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
