package com.mastercloudapps.thesocialnetworkplanner.resource.service;

import java.io.File;
import java.util.UUID;

import javax.annotation.PostConstruct;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class S3ResourceService implements ResourceService {

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @Value("${amazon.s3.region}")
    private String region;

    private AmazonS3 s3;

    @PostConstruct
    public void initS3Client() {
        this.s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .build();
        createBucketIfNotExist();
    }

    private void createBucketIfNotExist() {
        if (!this.s3.doesBucketExistV2(this.bucketName)) {
            this.s3.createBucket(this.bucketName);
        }
    }

    private String buildUrl(String filename) {
        return "https://" + this.bucketName + ".s3." + this.region + ".amazonaws.com/" + filename;
    }

    @Override
    public String createImage(MultipartFile multiPartFile) {
        String fileName = "image_" + UUID.randomUUID() + "_" + multiPartFile.getOriginalFilename();
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        try {
            multiPartFile.transferTo(file);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image on S3", e);
        }
        PutObjectRequest por = new PutObjectRequest(this.bucketName, fileName, file);
        por.setCannedAcl(CannedAccessControlList.PublicRead);
        this.s3.putObject(por);
        file.delete();
        return this.buildUrl(fileName);
    }

    @Override
    public void deleteImage(String image) {
        String[] tokens = image.split("/");
        String fileName = tokens[tokens.length - 1];
        this.s3.deleteObject(this.bucketName, fileName);
    }
}
