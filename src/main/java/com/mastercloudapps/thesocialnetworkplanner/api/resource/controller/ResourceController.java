package com.mastercloudapps.thesocialnetworkplanner.api.resource.controller;

import com.mastercloudapps.thesocialnetworkplanner.api.resource.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping()
    public ResponseEntity<String> upload(@RequestParam(value = "image",
            required = false) MultipartFile multipartFile) {
        String resource = this.resourceService.createImage(multipartFile);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam String urlFile) {
        this.resourceService.deleteImage(urlFile);
        return ResponseEntity.ok(urlFile);
    }
}
