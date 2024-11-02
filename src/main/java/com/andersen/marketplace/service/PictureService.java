package com.andersen.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing pictures.
 */
public interface PictureService {

    /**
     * Uploads a file to S3 and returns the file key.
     *
     * @param file the file to upload
     * @return the file key
     * @throws IllegalArgumentException if the file is null or empty
     */
    String uploadAndGetKey(MultipartFile file);

    /**
     * Deletes a file from S3 by its key.
     *
     * @param key the file key
     */
    void deleteFileFromS3(String key);

    /**
     * Deletes multiple files from S3 by their keys.
     *
     * @param keys the list of file keys
     */
    void deleteFilesFromS3(List<String> keys);

    /**
     * Retrieves the URL of a picture by its key.
     *
     * @param key the file key
     * @return the URL of the picture
     * @throws IllegalArgumentException if the key is null
     */
    String getPictureUrl(String key);
}
