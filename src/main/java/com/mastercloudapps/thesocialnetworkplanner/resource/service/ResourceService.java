package com.mastercloudapps.thesocialnetworkplanner.resource.service;

import com.mastercloudapps.thesocialnetworkplanner.resource.model.ResourceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    ResourceResponse createImage(MultipartFile multiPartFile);

    void deleteImage(String image);
}
