package com.easychat.auth.controller;

import com.easychat.auth.dto.UserJwt;
import com.easychat.auth.service.AuthService;
import com.easychat.auth.vo.LoginRequest;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.common.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前端访问首页时，判断浏览器是否有 cookie uid（用户 id），
 * 如果没有，则跳转到登录页，登录成功后把用户信息存储到 redis，并将用户 id 放到 cookie uid 中，然后跳转到首页；
 *
 * @Author: long
 * @Date: 2022-05-01 16:33
 */
@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletResponse response) {
        UserJwt userJwt = authService.loginAuth(loginRequest);
        if (userJwt != null) {
            int maxAge = -1;
            if (loginRequest.getLoginFree()) {
                maxAge = 7 * 24 * 60 * 60;
            }
            CookieUtil.addCookie(response, "toollong.icu", "/",
                    "uid", userJwt.getId(), maxAge, false);
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @GetMapping("/userJwt")
    public ResponseResult<UserJwt> getUserJwt(HttpServletRequest request) {
        String userId = CookieUtil.getCookieValue(request, "uid");
        UserJwt userJwt = authService.getUserJwt(userId);
        if (userJwt != null) {
            return ResponseResult.success(userJwt);
        }
        return ResponseResult.fail();
    }

    @PostMapping("/logout")
    public ResponseResult<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        String userId = CookieUtil.getCookieValue(request, "uid");
        if (StringUtils.isNotBlank(userId)) {
            boolean result = authService.deleteJwt(userId);
            if (result) {
                CookieUtil.addCookie(response, "toollong.icu", "/",
                        "uid", userId, 0, false);
                return ResponseResult.success();
            }
        }
        return ResponseResult.fail();
    }
}
