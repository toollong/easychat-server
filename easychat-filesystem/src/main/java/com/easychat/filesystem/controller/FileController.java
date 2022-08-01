package com.easychat.filesystem.controller;

import com.easychat.common.protocol.ResponseResult;
import com.easychat.filesystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: long
 * @Date: 2022-04-30 10:07
 */
@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseResult<Object> uploadFileToMinio(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("path") String filePath) {
        return fileService.uploadFile(file, filePath);
    }

    @GetMapping("/isExist")
    public ResponseResult<Object> checkFileIsExist(@RequestParam("path") String filePath) {
        return fileService.checkFile(filePath);
    }
}
