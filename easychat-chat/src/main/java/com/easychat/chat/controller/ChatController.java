package com.easychat.chat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easychat.chat.dto.ChatInfo;
import com.easychat.chat.entity.ChatHistory;
import com.easychat.chat.service.ChatService;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.common.protocol.ResultCode;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-19 13:18
 */
@RestController
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping("/chats")
    public ResponseResult<List<ChatInfo>> getChatList(@RequestParam("id") String userId) {
        return chatService.queryChatList(userId);
    }

    @GetMapping("/chats/chatHistory")
    public ResponseResult<Page<ChatHistory>> getChatHistories(@RequestParam("id") String userId,
                                                              @RequestParam("session") String sessionId,
                                                              @RequestParam("page") Integer page,
                                                              @RequestParam("size") Integer size) {
        page = page == null ? 0 : page;
        size = size == null ? 0 : size;
        return chatService.queryChatHistoryByPage(userId, sessionId, page, size);
    }

    @PostMapping("/chats/savePictureMsg")
    public ResponseResult<List<ChatHistory>> savePictureMsg(@RequestParam("file") MultipartFile[] files,
                                                            @RequestParam("message") String message) {
        if (StringUtils.isNotBlank(message)) {
            ChatHistory chatHistory = new Gson().fromJson(message, ChatHistory.class);
            try {
                return chatService.createChatHistory(chatHistory, files);
            } catch (CustomException e) {
                ResultCode resultCode = e.getResultCode();
                if (resultCode.code() == 20002) {
                    return ResponseResult.success(null);
                }
            }
        }
        return ResponseResult.fail();
    }

    @PostMapping("/chats/saveFileMsg")
    public ResponseResult<ChatHistory> saveFileMsg(@RequestParam("file") MultipartFile[] files,
                                                   @RequestParam("message") String message) {
        if (StringUtils.isNotBlank(message)) {
            ChatHistory chatHistory = new Gson().fromJson(message, ChatHistory.class);
            try {
                ResponseResult<List<ChatHistory>> result = chatService.createChatHistory(chatHistory, files);
                if (result.isSuccess()) {
                    return ResponseResult.success(result.getData().get(0));
                }
            } catch (CustomException e) {
                ResultCode resultCode = e.getResultCode();
                if (resultCode.code() == 20002) {
                    return ResponseResult.success(null);
                }
            }
        }
        return ResponseResult.fail();
    }
}
