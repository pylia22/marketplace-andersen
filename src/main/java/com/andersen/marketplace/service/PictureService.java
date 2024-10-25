package com.andersen.marketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureService {

    String uploadAndGetKey(MultipartFile file);

    void deleteFileFromS3(String key);

    void deleteFilesFromS3(List<String> keys);

    String getPictureUrl(String key);
}
