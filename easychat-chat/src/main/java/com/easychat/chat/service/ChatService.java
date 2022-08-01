package com.easychat.chat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easychat.chat.dto.ChatInfo;
import com.easychat.chat.entity.ChatHistory;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-20 17:20
 */
public interface ChatService {

    /**
     * 查询聊天列表（包含最新聊天记录）
     *
     * @param userId 用户 id
     * @return 聊天列表
     * @throws CustomException
     */
    ResponseResult<List<ChatInfo>> queryChatList(String userId) throws CustomException;

    /**
     * 查询聊天信息（包含最新聊天记录）
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @return 聊天信息
     * @throws CustomException
     */
    ResponseResult<ChatInfo> queryChatInfo(String userId, String friendUserId) throws CustomException;

    /**
     * 添加会话到好友信息
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @param sessionTime 会话时间
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> addFriendSession(
            String userId, String friendUserId, String sessionTime) throws CustomException;

    /**
     * 在好友信息中删除会话信息
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> removeFriendSession(String userId, String friendUserId) throws CustomException;

    /**
     * 修改好友备注
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @param remark 好友备注
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updateFriendRemark(
            String userId, String friendUserId, String remark) throws CustomException;

    /**
     * 分页查询聊天记录
     *
     * @param userId 用户 id
     * @param sessionId 会话 id
     * @param page 页码（默认为 1）
     * @param size 每页记录数（默认为 15）
     * @return 聊天记录
     * @throws CustomException
     */
    ResponseResult<Page<ChatHistory>> queryChatHistoryByPage(
            String userId, String sessionId, int page, int size) throws CustomException;

    /**
     * 新增一条聊天记录
     *
     * @param chatHistory 聊天记录
     * @param files 文件列表（发送非文字消息时使用）
     * @return 新增的聊天记录列表
     * @throws CustomException
     */
    ResponseResult<List<ChatHistory>> createChatHistory(
            ChatHistory chatHistory, MultipartFile[] files) throws CustomException;

    /**
     * 用户读取会话中接收到的所有未读消息
     *
     * @param sessionId 会话 id
     * @param userId 用户 id
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> readChatHistory(String sessionId, String userId) throws CustomException;
}
