package com.easychat.auth.service;

import com.easychat.auth.dto.UserJwt;
import com.easychat.auth.vo.LoginRequest;
import com.easychat.common.exception.CustomException;

/**
 * @Author: long
 * @Date: 2022-05-02 10:05
 */
public interface AuthService {

    /**
     * 用户登录认证
     *
     * @param request 登录请求
     * @return 用户认证信息
     * @throws CustomException
     */
    UserJwt loginAuth(LoginRequest request) throws CustomException;

    /**
     * 从 redis 中获取用户认证信息
     *
     * @param userId 用户 id
     * @return 用户认证信息
     * @throws CustomException
     */
    UserJwt getUserJwt(String userId) throws CustomException;

    /**
     * 从 redis 中删除用户的 jwt
     *
     * @param userId 用户 id
     * @return 结果
     * @throws CustomException
     */
    boolean deleteJwt(String userId) throws CustomException;
}
