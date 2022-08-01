package com.easychat.chat.service;

import java.util.Set;

/**
 * @Author: long
 * @Date: 2022-05-03 11:06
 */
public interface UserOnlineService {

    /**
     * 将登录的用户与对应的 socket-client 保存到 redis (userId:clientId)
     *
     * @param userId 用户 id
     * @param clientId 客户端 id
     */
    void saveUserAndClient(String userId, String clientId);

    /**
     * 根据用户 id 获取 socket-client id
     *
     * @param userId 用户 id
     * @return 客户端 id
     */
    String getClientIdByUserId(String userId);

    /**
     * 从 redis 中删除用户及对应的 socket-client
     *
     * @param userId 用户 id
     */
    void removeUserAndClient(String userId);

    /**
     * 将在线用户添加到 redis 的 set集合中
     *
     * @param userId 用户 id
     */
    void addOnlineUser(String userId);

    /**
     * 获取在线的用户集合
     *
     * @return 用户 Set 集合
     */
    Set<String> getOnlineUsers();

    /**
     * 用户下线时从在线集合中删除其信息
     *
     * @param userId 用户 id
     */
    void removeOnlineUser(String userId);

    /**
     * 获取在线的用户数量
     *
     * @return 在线用户数
     */
    Integer getOnlineCount();

    /**
     * 检查用户是否在线
     *
     * @param userId 用户 id
     * @return 结果
     */
    boolean checkUserIsOnline(String userId);
}
