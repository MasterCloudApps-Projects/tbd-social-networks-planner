package com.mastercloudapps.thesocialnetworkplanner.api.instagram.client;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.exception.InstagramException;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramDeviceLoginResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramMediaResponse;
import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.InstagramPostInfoResponse;

public interface InstagramClient {
    InstagramDeviceLoginResponse login() throws InstagramException;

    String getAccount() throws InstagramException;

    String post(String resource, String caption) throws InstagramException;

    InstagramPostInfoResponse getPostInfo(String id) throws InstagramException;

    InstagramMediaResponse getAllMedia() throws InstagramException;
}
