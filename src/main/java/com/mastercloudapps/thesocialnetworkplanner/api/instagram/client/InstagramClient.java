package com.mastercloudapps.thesocialnetworkplanner.api.instagram.client;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.ResourceResponse;

public interface InstagramClient {
    InstagramDeviceLoginResponse login() throws InstagramException;

    String getAccount() throws InstagramException;

    String post(ResourceResponse resource, String caption) throws InstagramException;

    InstagramPostInfoResponse getPostInfo(String id) throws InstagramException;

    InstagramMediaResponse getAllMedia() throws InstagramException;
}
