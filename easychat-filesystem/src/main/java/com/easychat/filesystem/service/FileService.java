package com.easychat.filesystem.service;

import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: long
 * @Date: 2022-04-30 10:17
 */
public interface FileService {

    /**
     * 上传文件到 MinIO
     *
     * @param file 文件
     * @param filePath 文件路径
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> uploadFile(MultipartFile file, String filePath) throws CustomException;

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> checkFile(String filePath) throws CustomException;
}
