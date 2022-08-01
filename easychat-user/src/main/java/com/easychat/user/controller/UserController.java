package com.easychat.user.controller;

import com.easychat.common.protocol.ResponseResult;
import com.easychat.common.util.CookieUtil;
import com.easychat.user.dto.UserInfo;
import com.easychat.user.dto.ValidationInfo;
import com.easychat.user.service.EmailService;
import com.easychat.user.service.UserService;
import com.easychat.user.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @Author: long
 * @Date: 2022-04-18 10:03
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;

    @PostMapping("/verifyCode/send")
    public ResponseResult<Object> sendCode(@RequestBody SendCodeRequest request,
                                           HttpServletResponse response) {
        ResponseResult<Object> responseResult = emailService.sendCodeToEmail(request);
        if (responseResult.isSuccess()) {
            CookieUtil.addCookie(response, "toollong.icu", "/",
                    "verify", UUID.randomUUID().toString(), 60, false);
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @PostMapping("/verifyCode/validate")
    public ResponseResult<Object> validateCode(@RequestBody ValidateCodeRequest request) {
        return emailService.validateEmailAndCode(request);
    }

    @PostMapping("/username/validate")
    public ResponseResult<ValidationInfo> validateUsername(@RequestBody ValidateUsernameRequest request) {
        return userService.validateUserUsername(request);
    }

    @PostMapping("/password/validate")
    public ResponseResult<Object> validatePassword(@RequestBody ValidatePasswordRequest request) {
        return userService.validateUserPassword(request);
    }

    @PostMapping("/register")
    public ResponseResult<Object> register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/search")
    public ResponseResult<List<UserInfo>> searchUsers(@RequestParam("username") String username) {
        return userService.queryUserList(username);
    }

    @GetMapping("/user")
    public ResponseResult<UserInfo> getUserInfo(@RequestParam("id") String userId) {
        return userService.queryUserInfo(userId);
    }

    @GetMapping("/user/auth")
    public ResponseResult<UserInfo> getUserInfo(@RequestParam("username") String username,
                                                @RequestParam("password") String password) {
        return userService.queryUserInfo(username, password);
    }

    @PostMapping("/user/edit")
    public ResponseResult<UserInfo> editUserInfo(@RequestBody EditUserInfoRequest request) {
        return userService.updateUserInfo(request);
    }

    @PostMapping("/user/changeAvatar")
    public ResponseResult<String> changeAvatar(@RequestParam("file") MultipartFile image,
                                               @RequestParam("id") String userId) {
        return userService.updateAvatar(userId, image);
    }

    @PostMapping("/user/changeStatus")
    public ResponseResult<Object> changeStatus(@RequestBody ChangeStatusRequest request) {
        return userService.updateStatus(request);
    }

    @PostMapping("/user/changePassword")
    public ResponseResult<Object> changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.updatePassword(request);
    }

    @PostMapping("/user/addTag")
    public ResponseResult<Object> addTag(@RequestBody UpdateTagsRequest request) {
        return userService.updateTagsAdd(request);
    }

    @PostMapping("/user/removeTag")
    public ResponseResult<Object> removeTag(@RequestBody UpdateTagsRequest request) {
        return userService.updateTagsRemove(request);
    }
}
