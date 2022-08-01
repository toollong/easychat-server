package com.easychat.user.service;

import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.vo.SendCodeRequest;
import com.easychat.user.vo.ValidateCodeRequest;

/**
 * @Author: long
 * @Date: 2022-06-30 18:08
 */
public interface EmailService {

    /**
     * 发送验证码到指定邮箱
     *
     * @param request 验证码发送请求
     * @return 响应结果
     */
    ResponseResult<Object> sendCodeToEmail(SendCodeRequest request);

    /**
     * 验证邮箱和验证码是否正确
     *
     * @param request 验证码验证请求
     * @return 响应结果
     */
    ResponseResult<Object> validateEmailAndCode(ValidateCodeRequest request);
}
