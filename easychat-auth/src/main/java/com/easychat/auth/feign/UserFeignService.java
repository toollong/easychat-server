package com.easychat.auth.feign;

import com.easychat.auth.dto.UserJwt;
import com.easychat.common.protocol.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: long
 * @Date: 2022-05-02 10:53
 */
@FeignClient("easychat-user-service")
public interface UserFeignService {

    @GetMapping("/user/user/auth")
    ResponseResult<UserJwt> getUserInfo(@RequestParam("username") String username,
                                        @RequestParam("password") String password);
}
