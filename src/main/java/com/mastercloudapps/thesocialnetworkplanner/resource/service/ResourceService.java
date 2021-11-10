package com.mastercloudapps.thesocialnetworkplanner.resource.service;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    String createImage(MultipartFile multiPartFile);

    void deleteImage(String image);
}
