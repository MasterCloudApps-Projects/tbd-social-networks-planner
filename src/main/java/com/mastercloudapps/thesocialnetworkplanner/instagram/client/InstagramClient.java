package com.mastercloudapps.thesocialnetworkplanner.instagram.client;

import com.mastercloudapps.thesocialnetworkplanner.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.instagram.model.InstagramPostInfoResponse;
import com.mastercloudapps.thesocialnetworkplanner.resource.model.ResourceResponse;

public interface InstagramClient {
    InstagramDeviceLoginResponse login() throws InstagramException;

    String getAccount() throws InstagramException;

    String post(ResourceResponse resource, String caption) throws InstagramException;

    InstagramPostInfoResponse getPostInfo(String id) throws InstagramException;

    InstagramMediaResponse getAllMedia() throws InstagramException;
}
