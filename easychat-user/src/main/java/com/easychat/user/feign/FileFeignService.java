package com.easychat.user.feign;

import com.easychat.common.protocol.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: long
 * @Date: 2022-07-21 15:12
 */
@FeignClient("easychat-filesystem-service")
public interface FileFeignService {

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseResult<Object> uploadFileToMinio(@RequestPart("file") MultipartFile file,
                                             @RequestParam("path") String filePath);
}
