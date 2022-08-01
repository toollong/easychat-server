package com.easychat.chat.service.impl;

import com.easychat.chat.service.UserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: long
 * @Date: 2022-05-03 11:06
 */
@Service
public class UserOnlineServiceImpl implements UserOnlineService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void saveUserAndClient(String userId, String clientId) {
        String key = "user:" + userId;
        redisTemplate.opsForValue().set(key, clientId, 60 * 60 * 24, TimeUnit.SECONDS);
    }

    @Override
    public String getClientIdByUserId(String userId) {
        String key = "user:" + userId;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void removeUserAndClient(String userId) {
        String key = "user:" + userId;
        redisTemplate.delete(key);
    }

    @Override
    public void addOnlineUser(String userId) {
        String key = "online:userSet";
        redisTemplate.opsForSet().remove(key, userId);
        redisTemplate.opsForSet().add(key, userId);
    }

    @Override
    public Set<String> getOnlineUsers() {
        return redisTemplate.opsForSet().members("online:userSet");
    }

    @Override
    public void removeOnlineUser(String userId) {
        redisTemplate.opsForSet().remove("online:userSet", userId);
    }

    @Override
    public Integer getOnlineCount() {
        Set<String> members = redisTemplate.opsForSet().members("online:userSet");
        return members == null ? 0 : members.size();
    }

    @Override
    public boolean checkUserIsOnline(String userId) {
        Boolean result = redisTemplate.opsForSet().isMember("online:userSet", userId);
        return result != null && result;
    }
}
