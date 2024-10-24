package com.andersen.marketplace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.andersen.marketplace.properties.S3BucketProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.andersen.marketplace.utils.TestConstants.TEST_LOGO_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PictureServiceImplTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private S3BucketProperties s3BucketProperties;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private PictureServiceImpl pictureService;

    @Test
    void shouldReturnImageKeyWhenFileNotNullAndEmpty() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn(TEST_LOGO_KEY);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(TEST_LOGO_KEY.getBytes()));
        when(s3BucketProperties.getBucketName()).thenReturn("test");

        String actualImageKey = pictureService.uploadAndGetKey(file);

        verify(amazonS3).putObject(anyString(), anyString(), any(), any());
        assertEquals(TEST_LOGO_KEY, actualImageKey.split("_")[1]);
    }

    @Test
    void shouldThrowWhenFileNull() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> pictureService.uploadAndGetKey(null));
    }

    @Test
    void shouldVerifyFileDeleteWhenKeyNotNull() {
        when(s3BucketProperties.getBucketName()).thenReturn("test");

        pictureService.deleteFileFromS3(TEST_LOGO_KEY);

        verify(amazonS3).deleteObject("test", TEST_LOGO_KEY);
    }

    @Test
    void shouldVerifyNotInteractionsFileDeleteWhenKeyNotNull() {
        pictureService.deleteFileFromS3(null);
        verifyNoInteractions(amazonS3);
    }

    @Test
    void shouldVerifyDeleteFilesWhenKeysNotNull() {
        List<String> testLogoKey = List.of(TEST_LOGO_KEY, TEST_LOGO_KEY);

        when(s3BucketProperties.getBucketName()).thenReturn("test");

        pictureService.deleteFilesFromS3(testLogoKey);

        verify(amazonS3, times(2)).deleteObject("test", TEST_LOGO_KEY);
    }

}