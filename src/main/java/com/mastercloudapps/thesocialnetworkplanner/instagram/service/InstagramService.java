package com.mastercloudapps.thesocialnetworkplanner.instagram.service;

import com.mastercloudapps.thesocialnetworkplanner.instagram.client.InstagramRestClient;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.resource.model.ResourceResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class InstagramService {

    private final InstagramRestClient instagramRestClient;

    public InstagramService(InstagramRestClient instagramRestClient) {
        this.instagramRestClient = instagramRestClient;
    }

    public InstagramDeviceLoginResponse login() throws InstagramException {
        return this.instagramRestClient.login();
    }

    public String authenticate() throws InstagramException {
        return this.instagramRestClient.authenticate();
    }

    public String post(ResourceResponse resource, String caption) throws InstagramException {
        return this.instagramRestClient.post(resource, caption);
    }

    public InstagramPostInfoResponse getPostInfo(String id) throws InstagramException {
        return this.instagramRestClient.getPostInfo(id);
    }

    public InstagramMediaResponse getAllMedia() throws InstagramException {
        return this.instagramRestClient.getAllMedia();
    }
}
