package com.mastercloudapps.thesocialnetworkplanner.api.resource.service;

import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    String createImage(MultipartFile multiPartFile);

    void deleteImage(String image);

    Resource saveResource(Resource resource);
}
