package com.andersen.marketplace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.andersen.marketplace.properties.S3BucketProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    private final AmazonS3 amazonS3;
    private final S3BucketProperties s3BucketProperties;

    public PictureServiceImpl(AmazonS3 amazonS3, S3BucketProperties s3BucketProperties) {
        this.amazonS3 = amazonS3;
        this.s3BucketProperties = s3BucketProperties;
    }

    @Override
    public String uploadAndGetKey(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }
        String newFileKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(s3BucketProperties.getBucketName(), newFileKey, inputStream, new ObjectMetadata());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return newFileKey;
    }

    @Override
    public void deleteFileFromS3(String key) {
        if (key != null) {
            String bucketName = s3BucketProperties.getBucketName();
            amazonS3.deleteObject(bucketName, key);
        }
    }

    @Override
    public void deleteFilesFromS3(List<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(this::deleteFileFromS3);
        }
    }
}