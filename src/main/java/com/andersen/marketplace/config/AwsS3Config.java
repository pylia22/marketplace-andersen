package com.andersen.marketplace.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.andersen.marketplace.properties.S3BucketProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Amazon S3 client.
 */
@Configuration
public class AwsS3Config {

    /**
     * Creates and configures an Amazon S3 client.
     *
     * @param s3BucketProperties the S3 bucket properties
     * @return the configured Amazon S3 client
     */
    @Bean
    public AmazonS3 getAmazonS3Client(S3BucketProperties s3BucketProperties) {
        AWSCredentials credentials = new BasicAWSCredentials(s3BucketProperties.getAccessKey(), s3BucketProperties.getSecretKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(s3BucketProperties.getRegion())
                .build();
    }
}
