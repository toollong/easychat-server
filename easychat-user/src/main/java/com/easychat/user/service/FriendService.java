package com.easychat.user.service;

import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.dto.FriendInfo;
import com.easychat.user.dto.VerifyInfo;
import com.easychat.user.vo.*;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-05-03 10:44
 */
public interface FriendService {

    /**
     * 查询用户好友列表
     *
     * @param userId 用户 id
     * @return 好友信息列表
     * @throws CustomException
     */
    ResponseResult<List<FriendInfo>> queryUserFriends(String userId) throws CustomException;

    /**
     * 查询用户好友信息
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @return 好友信息
     * @throws CustomException
     */
    ResponseResult<FriendInfo> queryFriendInfo(String userId, String friendUserId) throws CustomException;

    /**
     * 创建好友信息
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> createFriend(AddFriendRequest request) throws CustomException;

    /**
     * 更新好友信息
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updateFriend(EditFriendRequest request) throws CustomException;

    /**
     * 删除好友信息
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> deleteFriend(RemoveFriendRequest request) throws CustomException;

    /**
     * 查询好友验证信息列表
     *
     * @param userId 用户 id
     * @return 好友验证信息列表
     * @throws CustomException
     */
    ResponseResult<List<VerifyInfo>> queryFriendVerifyList(String userId) throws CustomException;

    /**
     * 查询好友验证信息
     *
     * @param senderId 发送者 id
     * @param receiverId 接收者 id
     * @return 好友验证信息
     * @throws CustomException
     */
    ResponseResult<VerifyInfo> queryFriendVerify(String senderId, String receiverId) throws CustomException;

    /**
     * 创建好友验证信息
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> createFriendVerify(AddFriendVerifyRequest request) throws CustomException;

    /**
     * 更新好友验证信息
     *
     * @param request 请求信息
     * @return 备注
     * @throws CustomException
     */
    ResponseResult<String> updateFriendVerify(EditFriendVerifyRequest request) throws CustomException;

    /**
     * 将用户接收到的所有未读好友验证更新为已读
     *
     * @param userId 用户 id
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updateFriendVerify(String userId) throws CustomException;
}
