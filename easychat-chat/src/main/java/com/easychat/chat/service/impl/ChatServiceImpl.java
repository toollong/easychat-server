package com.easychat.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easychat.chat.dto.ChatInfo;
import com.easychat.chat.dto.FriendInfo;
import com.easychat.chat.entity.ChatHistory;
import com.easychat.chat.feign.FileFeignService;
import com.easychat.chat.feign.UserFeignService;
import com.easychat.chat.mapper.ChatHistoryMapper;
import com.easychat.chat.service.ChatService;
import com.easychat.chat.vo.EditFriendRequest;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.CommonCode;
import com.easychat.common.protocol.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-20 17:20
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    ChatHistoryMapper chatHistoryMapper;
    @Autowired
    UserFeignService userFeignService;
    @Autowired
    FileFeignService fileFeignService;

    @Override
    public ResponseResult<List<ChatInfo>> queryChatList(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        List<ChatInfo> chatInfoList = chatHistoryMapper.selectChatInfoList(userId);
        return ResponseResult.success(chatInfoList);
    }

    @Override
    public ResponseResult<ChatInfo> queryChatInfo(String userId, String friendUserId)
            throws CustomException {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUserId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        ChatInfo chatInfo = chatHistoryMapper.selectChatInfo(userId, friendUserId);
        if (chatInfo != null) {
            return ResponseResult.success(chatInfo);
        }
        return ResponseResult.fail();
    }

    @Override
    public ResponseResult<Object> addFriendSession(String userId, String friendUserId, String sessionTime)
            throws CustomException {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUserId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        ResponseResult<FriendInfo> myResult = userFeignService.getFriendInfo(userId, friendUserId);
        if (myResult.isSuccess()) {
            FriendInfo friendInfo = myResult.getData();
            String sessionId = friendInfo.getSessionId();
            if ("0".equals(sessionId)) {
                sessionId = IdWorker.getIdStr();
                ResponseResult<FriendInfo> friendResult =
                        userFeignService.getFriendInfo(friendUserId, userId);
                if (friendResult.isSuccess()
                        && !"0".equals(friendResult.getData().getSessionId())) {
                    sessionId = friendResult.getData().getSessionId();
                }
                EditFriendRequest request = new EditFriendRequest();
                request.setUserId(userId);
                request.setFriendUserId(friendUserId);
                request.setSessionId(sessionId);
                request.setSessionTime(sessionTime);
                return userFeignService.editFriend(request);
            }
            return ResponseResult.success("exist");
        }
        return ResponseResult.fail();
    }

    @Override
    public ResponseResult<Object> removeFriendSession(String userId, String friendUserId)
            throws CustomException {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUserId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        EditFriendRequest request = new EditFriendRequest();
        request.setUserId(userId);
        request.setFriendUserId(friendUserId);
        request.setSessionId("0");
        request.setSessionTime(null);
        return userFeignService.editFriend(request);
    }

    @Override
    public ResponseResult<Object> updateFriendRemark(String userId, String friendUserId, String remark)
            throws CustomException {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUserId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        EditFriendRequest request = new EditFriendRequest();
        request.setUserId(userId);
        request.setFriendUserId(friendUserId);
        request.setRemark(remark);
        return userFeignService.editFriend(request);
    }

    @Override
    public ResponseResult<Page<ChatHistory>> queryChatHistoryByPage(
            String userId, String sessionId, int page, int size) throws CustomException {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        page = Math.max(page, 1);
        size = size > 0 ? size : 15;
        Page<ChatHistory> chatHistoryPage = chatHistoryMapper
                .selectPage(new Page<>(page, size), new LambdaQueryWrapper<ChatHistory>()
                        .eq(ChatHistory::getSessionId, sessionId)
                        .orderByDesc(ChatHistory::getCreateTime));
        return ResponseResult.success(chatHistoryPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<List<ChatHistory>> createChatHistory(ChatHistory chatHistory, MultipartFile[] files)
            throws CustomException {
        if (chatHistory == null
                || StringUtils.isBlank(chatHistory.getSenderId())
                || StringUtils.isBlank(chatHistory.getReceiverId())
                || StringUtils.isBlank(chatHistory.getSessionId())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        ResponseResult<FriendInfo> result = userFeignService
                .getFriendInfo(chatHistory.getReceiverId(), chatHistory.getSenderId());
        if (!result.isSuccess()) {
            throw new CustomException(CommonCode.USER_NOT_FRIEND);
        }
        if (chatHistory.getType() == 0) {
            int insert = chatHistoryMapper.insert(chatHistory);
            if (insert > 0) {
                List<ChatHistory> list = new ArrayList<>();
                list.add(chatHistory);
                return ResponseResult.success(list);
            }
        }
        if (chatHistory.getType() == 1) {
            if (files.length < 1 || files.length > 5) {
                throw new CustomException(CommonCode.INVALID_PARAM);
            }
            List<ChatHistory> list = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                if (!"image/png".equals(files[i].getContentType())
                        && !"image/jpeg".equals(files[i].getContentType())) {
                    throw new CustomException(CommonCode.UPLOAD_IMAGE_FORMAT_ERROR);
                }
                if (files[i].getSize() / 1024 / 1024 > 2) {
                    throw new CustomException(CommonCode.UPLOAD_IMAGE_SIZE_ERROR);
                }
                String filePath = "/pictures/" + chatHistory.getSessionId() + "/" + IdWorker.getIdStr();
                String filename = files[i].getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    filePath = filePath + filename.substring(filename.lastIndexOf("."));
                }
                ResponseResult<Object> uploadResult = fileFeignService.uploadFileToMinio(files[i], filePath);
                if (uploadResult.isSuccess()) {
                    chatHistory.setId(null);
                    chatHistory.setContent(filePath);
                    chatHistory.setCreateTime(LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    if (i > 0) {
                        chatHistory.setShowTime(0);
                    }
                    int insert = chatHistoryMapper.insert(chatHistory);
                    if (insert > 0) {
                        list.add(chatHistory);
                    }
                }
            }
            return ResponseResult.success(list);
        }
        if (chatHistory.getType() == 2) {
            if (files.length != 1) {
                throw new CustomException(CommonCode.INVALID_PARAM);
            }
            if (files[0].getSize() / 1024 / 1024 > 100) {
                throw new CustomException(CommonCode.UPLOAD_FILE_SIZE_ERROR);
            }
            String folderPath = "/files/" + chatHistory.getSessionId() + "/";
            String filename = files[0].getOriginalFilename();
            assert filename != null;
            String prefix = filename.substring(0, filename.lastIndexOf("."));
            String suffix = filename.substring(filename.lastIndexOf("."));
            String filePath = folderPath + filename;
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                ResponseResult<Object> isExist = fileFeignService.checkFileIsExist(filePath);
                if (!isExist.isSuccess()) {
                    break;
                }
                filePath = folderPath + prefix + "(" + i + ")" + suffix;
            }
            ResponseResult<Object> uploadResult = fileFeignService.uploadFileToMinio(files[0], filePath);
            if (uploadResult.isSuccess()) {
                chatHistory.setContent(filePath);
                int insert = chatHistoryMapper.insert(chatHistory);
                if (insert > 0) {
                    List<ChatHistory> list = new ArrayList<>();
                    list.add(chatHistory);
                    return ResponseResult.success(list);
                }
            }
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> readChatHistory(String sessionId, String userId)
            throws CustomException {
        if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(userId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        chatHistoryMapper.updateChatHistory(sessionId, userId);
        return ResponseResult.success();
    }
}
