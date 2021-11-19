package com.mastercloudapps.thesocialnetworkplanner.instagram.service;

import com.mastercloudapps.thesocialnetworkplanner.instagram.client.InstagramRestClient;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramNotAuthorizeException;
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

    public String login() throws InstagramException {
        InstagramDeviceLoginResponse instagramDeviceLoginResponse = this.instagramRestClient.login();
        if (instagramDeviceLoginResponse != null) {
            return "Enter this code " + instagramDeviceLoginResponse.getUserCode() + " on "
                    + instagramDeviceLoginResponse.getVerificationUri();
        } else {
            throw new InstagramException("Error getting authentication code.");
        }
    }

    public String authenticate() throws InstagramException {
        String accountId = this.instagramRestClient.authenticate();
        if (accountId == null) {
            throw new InstagramNotAuthorizeException();
        }
        return accountId;
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
