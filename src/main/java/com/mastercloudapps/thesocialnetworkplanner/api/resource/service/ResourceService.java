package com.mastercloudapps.thesocialnetworkplanner.api.resource.service;

import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.ResourceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    ResourceResponse createImage(MultipartFile multiPartFile);

    void deleteImage(String image);
}
