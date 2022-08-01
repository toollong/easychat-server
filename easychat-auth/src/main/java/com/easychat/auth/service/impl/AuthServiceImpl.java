package com.easychat.auth.service.impl;

import com.easychat.auth.dto.UserJwt;
import com.easychat.auth.feign.UserFeignService;
import com.easychat.auth.service.AuthService;
import com.easychat.auth.vo.LoginRequest;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.CommonCode;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.common.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: long
 * @Date: 2022-05-02 10:05
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserFeignService userFeignService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public UserJwt loginAuth(LoginRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUsername())
                || StringUtils.isBlank(request.getPassword())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        ResponseResult<UserJwt> result = userFeignService
                .getUserInfo(request.getUsername(), request.getPassword());
        UserJwt userJwt = result.getData();
        if (result.isSuccess() && userJwt != null) {
            String key = "user_id:" + userJwt.getId();
            String jwt = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(jwt)) {
                redisTemplate.delete(key);
            }
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", userJwt.getId());
            claims.put("username", userJwt.getUsername());
            claims.put("nickName", userJwt.getNickName());
            claims.put("avatar", userJwt.getAvatar());
            long expireTime = 24 * 60 * 60 * 1000;
            if (request.getLoginFree()) {
                expireTime = expireTime * 7;
            }
            jwt = JwtUtil.createJwt(claims, expireTime);
            redisTemplate.opsForValue().set(key, jwt, expireTime, TimeUnit.MILLISECONDS);
            userJwt.setJwt(jwt);
        }
        return userJwt;
    }

    @Override
    public UserJwt getUserJwt(String userId) throws CustomException {
        UserJwt userJwt = null;
        if (StringUtils.isNotBlank(userId)) {
            String key = "user_id:" + userId;
            String jwt = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(jwt)) {
                boolean result = JwtUtil.verifyJwt(jwt);
                if (result) {
                    Map<String, Object> claims = JwtUtil.parseJwt(jwt);
                    userJwt = new UserJwt();
                    userJwt.setId((String) claims.get("userId"));
                    userJwt.setUsername((String) claims.get("username"));
                    userJwt.setNickName((String) claims.get("nickName"));
                    userJwt.setAvatar((String) claims.get("avatar"));
                }
            }
        }
        return userJwt;
    }

    @Override
    public boolean deleteJwt(String userId) throws CustomException {
        String key = "user_id:" + userId;
        Boolean result = redisTemplate.delete(key);
        return result != null && result;
    }
}
