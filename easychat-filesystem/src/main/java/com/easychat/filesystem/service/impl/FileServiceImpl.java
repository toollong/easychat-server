package com.easychat.filesystem.service.impl;

import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.CommonCode;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.filesystem.service.FileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Author: long
 * @Date: 2022-04-30 10:18
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucketName}")
    private String bucketName;

    @Override
    public ResponseResult<Object> uploadFile(MultipartFile file, String filePath) throws CustomException {
        if (file.isEmpty() || StringUtils.isBlank(filePath)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        try (InputStream inputStream = file.getInputStream()) {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpoint)
                            .credentials(accessKey, secretKey)
                            .build();
            boolean isExist = minioClient
                    .bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePath)
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.fail();
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<Object> checkFile(String filePath) throws CustomException {
        if (StringUtils.isBlank(filePath)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpoint)
                            .credentials(accessKey, secretKey)
                            .build();
            try {
                minioClient.statObject(
                        StatObjectArgs.builder().bucket(bucketName).object(filePath).build());
            } catch (Exception e) {
                return ResponseResult.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success();
    }
}
