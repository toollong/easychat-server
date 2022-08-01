package com.easychat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.CommonCode;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.dto.FriendInfo;
import com.easychat.user.dto.VerifyInfo;
import com.easychat.user.entity.Friend;
import com.easychat.user.entity.FriendVerify;
import com.easychat.user.entity.User;
import com.easychat.user.mapper.FriendMapper;
import com.easychat.user.mapper.FriendVerifyMapper;
import com.easychat.user.mapper.UserMapper;
import com.easychat.user.service.FriendService;
import com.easychat.user.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-05-03 10:44
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    FriendMapper friendMapper;
    @Autowired
    FriendVerifyMapper friendVerifyMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public ResponseResult<List<FriendInfo>> queryUserFriends(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        List<FriendInfo> friendList = friendMapper.selectFriendInfoList(userId);
        return ResponseResult.success(friendList);
    }

    @Override
    public ResponseResult<FriendInfo> queryFriendInfo(String userId, String friendUserId)
            throws CustomException {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUserId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        FriendInfo friendInfo = friendMapper.selectFriendInfo(userId, friendUserId);
        if (friendInfo != null) {
            return ResponseResult.success(friendInfo);
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> createFriend(AddFriendRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getSessionId())
                || StringUtils.isBlank(request.getSessionTime())
                || StringUtils.isBlank(request.getFriendUserId())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        Long count = userMapper
                .selectCount(new LambdaQueryWrapper<User>()
                        .eq(User::getId, request.getFriendUserId()));
        if (count > 0) {
            Long result = friendMapper
                    .selectCount(new LambdaQueryWrapper<Friend>()
                            .eq(Friend::getUserId, request.getUserId())
                            .eq(Friend::getFriendUserId, request.getFriendUserId()));
            if (result < 1) {
                Friend friend = new Friend();
                friend.setUserId(request.getUserId());
                friend.setSessionId(request.getSessionId());
                friend.setSessionTime(request.getSessionTime());
                friend.setFriendUserId(request.getFriendUserId());
                friend.setFriendRemark(request.getFriendRemark());
                friend.setCreateTime(request.getCreateTime());
                friendMapper.insert(friend);
            }
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> updateFriend(EditFriendRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getFriendUserId())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        Friend friend = friendMapper
                .selectOne(new LambdaQueryWrapper<Friend>()
                        .eq(Friend::getUserId, request.getUserId())
                        .eq(Friend::getFriendUserId, request.getFriendUserId()));
        if (friend != null) {
            if (StringUtils.isNotBlank(request.getSessionId())) {
                friend.setSessionId(request.getSessionId());
                friend.setSessionTime(request.getSessionTime());
            }
            if (request.getRemark() != null) {
                friend.setFriendRemark(request.getRemark());
            }
            int result = friendMapper.updateById(friend);
            if (result > 0) {
                return ResponseResult.success();
            }
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> deleteFriend(RemoveFriendRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getFriendUserId())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        friendMapper.delete(new LambdaQueryWrapper<Friend>()
                .eq(Friend::getUserId, request.getUserId())
                .eq(Friend::getFriendUserId, request.getFriendUserId()));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<List<VerifyInfo>> queryFriendVerifyList(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        List<VerifyInfo> verifyInfoList = friendVerifyMapper.selectVerifyInfoList(userId);
        return ResponseResult.success(verifyInfoList);
    }

    @Override
    public ResponseResult<VerifyInfo> queryFriendVerify(String senderId, String receiverId)
            throws CustomException {
        if (StringUtils.isBlank(senderId) || StringUtils.isBlank(receiverId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        VerifyInfo verifyInfo = friendVerifyMapper.selectVerifyInfo(senderId, receiverId);
        if (verifyInfo != null) {
            return ResponseResult.success(verifyInfo);
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> createFriendVerify(AddFriendVerifyRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getSenderId())
                || StringUtils.isBlank(request.getReceiverId())
                || request.getStatus() == null) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        FriendVerify friendVerify = friendVerifyMapper
                .selectOne(new LambdaQueryWrapper<FriendVerify>()
                        .eq(FriendVerify::getSenderId, request.getSenderId())
                        .eq(FriendVerify::getReceiverId, request.getReceiverId()));
        if (friendVerify == null) {
            friendVerify = new FriendVerify();
            friendVerify.setSenderId(request.getSenderId());
            friendVerify.setReceiverId(request.getReceiverId());
            friendVerify.setApplyReason(request.getApplyReason());
            friendVerify.setRemark(request.getRemark());
            friendVerify.setStatus(request.getStatus());
            friendVerify.setHasRead(request.getHasRead());
            friendVerify.setCreateTime(request.getCreateTime());
            friendVerifyMapper.insert(friendVerify);
            return ResponseResult.success();
        }
        friendVerify.setApplyReason(request.getApplyReason());
        friendVerify.setRemark(request.getRemark());
        friendVerify.setStatus(request.getStatus());
        friendVerify.setHasRead(request.getHasRead());
        friendVerify.setCreateTime(request.getCreateTime());
        friendVerifyMapper.updateById(friendVerify);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> updateFriendVerify(EditFriendVerifyRequest request)
            throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getSenderId())
                || StringUtils.isBlank(request.getReceiverId())
                || request.getStatus() == null) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        FriendVerify friendVerify = friendVerifyMapper
                .selectOne(new LambdaQueryWrapper<FriendVerify>()
                        .eq(FriendVerify::getSenderId, request.getSenderId())
                        .eq(FriendVerify::getReceiverId, request.getReceiverId()));
        if (friendVerify != null) {
            friendVerify.setStatus(request.getStatus());
            int result = friendVerifyMapper.updateById(friendVerify);
            if (result > 0) {
                return ResponseResult.success(friendVerify.getRemark());
            }
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> updateFriendVerify(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        friendVerifyMapper.updateFriendVerify(userId);
        return ResponseResult.success();
    }
}
