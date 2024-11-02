package com.andersen.marketplace.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.andersen.marketplace.properties.S3BucketProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing pictures in Amazon S3.
 */
@Service
public class PictureServiceImpl implements PictureService {

    private static final Logger logger = LoggerFactory.getLogger(PictureServiceImpl.class);

    private final AmazonS3 amazonS3;
    private final S3BucketProperties s3BucketProperties;

    /**
     * Constructs a new PictureServiceImpl.
     *
     * @param amazonS3 the Amazon S3 client
     * @param s3BucketProperties the S3 bucket properties
     */
    public PictureServiceImpl(AmazonS3 amazonS3, S3BucketProperties s3BucketProperties) {
        this.amazonS3 = amazonS3;
        this.s3BucketProperties = s3BucketProperties;
    }

    /**
     * Uploads a file to S3 and returns the file key.
     *
     * @param file the file to upload
     * @return the file key
     * @throws IllegalArgumentException if the file is null or empty
     * @throws RuntimeException if the file upload fails
     */
    @Override
    public String uploadAndGetKey(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            logger.error("File is null or empty");
            throw new IllegalArgumentException("File is null or empty");
        }
        String newFileKey = UUID.randomUUID() + "_" + file.getOriginalFilename();
        logger.info("Uploading file with key {}", newFileKey);

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(s3BucketProperties.getBucketName(), newFileKey, inputStream, new ObjectMetadata());
            logger.info("File uploaded successfully with key {}", newFileKey);
        } catch (IOException e) {
            logger.error("Failed to upload file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return newFileKey;
    }

    /**
     * Deletes a file from S3 by its key.
     *
     * @param key the file key
     */
    @Override
    public void deleteFileFromS3(String key) {
        if (key != null) {
            String bucketName = s3BucketProperties.getBucketName();
            logger.info("Deleting file with key {} from bucket {}", key, bucketName);
            amazonS3.deleteObject(bucketName, key);
        }
    }

    /**
     * Deletes multiple files from S3 by their keys.
     *
     * @param keys the list of file keys
     */
    @Override
    public void deleteFilesFromS3(List<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(this::deleteFileFromS3);
        }
    }

    /**
     * Retrieves the URL of a picture by its key.
     *
     * @param key the file key
     * @return the URL of the picture
     * @throws IllegalArgumentException if the key is null
     */
    @Override
    public String getPictureUrl(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        if (checkIfPictureUploaded(key)) {
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                    s3BucketProperties.getBucketName(), key)
                    .withMethod(HttpMethod.GET);
            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } else {
            logger.warn("Picture key {} doesn't exist in bucket {}", key, s3BucketProperties.getBucketName());
            return null;
        }
    }

    /**
     * Checks if a picture is uploaded in S3 by its key.
     *
     * @param key the file key
     * @return true if the picture exists, false otherwise
     */
    private boolean checkIfPictureUploaded(String key) {
        return amazonS3.doesObjectExist(s3BucketProperties.getBucketName(), key);
    }
}
