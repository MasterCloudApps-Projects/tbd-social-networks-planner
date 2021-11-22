package com.mastercloudapps.thesocialnetworkplanner.api.instagram.service;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.client.InstagramRestClient;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramNotAuthorizeException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.Instagram;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.repository.InstagramRepository;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.Resource;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.service.ResourceService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Service
@Log4j2
public class InstagramService {

    private final InstagramRestClient instagramRestClient;
    private final ResourceService resourceService;
    private final InstagramRepository instagramRepository;

    public InstagramService(InstagramRestClient instagramRestClient, ResourceService resourceService, InstagramRepository instagramRepository) {
        this.instagramRestClient = instagramRestClient;
        this.resourceService = resourceService;
        this.instagramRepository = instagramRepository;
    }

    public String login() throws InstagramException {
        InstagramDeviceLoginResponse instagramDeviceLoginResponse = this.instagramRestClient.login();
        if (instagramDeviceLoginResponse != null) {
            return "Enter this code " + instagramDeviceLoginResponse.getUserCode() + " on "
                    + instagramDeviceLoginResponse.getVerificationUri();
        } else {
            throw new InstagramException("Error getting authentication code.");
        }
    }

    public String getAccount() throws InstagramException {
        String accountId = this.instagramRestClient.getAccount();
        if (accountId == null) {
            throw new InstagramNotAuthorizeException();
        }
        return accountId;
    }

    public String post(MultipartFile multipartFile, String caption) throws InstagramException {
        String url = this.resourceService.createImage(multipartFile);
        String postId = this.instagramRestClient.post(url, caption);
        if (StringUtils.isNotBlank(postId)) {
            Instagram instagramPost = Instagram.builder().instagramId(Long.parseLong(postId)).creationDate(new Date()).text(caption).username("prueba").build();
            this.instagramRepository.save(instagramPost);
            Resource resource = Resource.builder().url(url).creationDate(new Date()).instagram(instagramPost).build();
            this.resourceService.saveResource(resource);
        }
        return postId;
    }

    public InstagramPostInfoResponse getPostInfo(String id) throws InstagramException {
        return this.instagramRestClient.getPostInfo(id);
    }

    public InstagramMediaResponse getAllMedia() throws InstagramException {
        return this.instagramRestClient.getAllMedia();
    }
}
