package com.easychat.user.controller;

import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.dto.FriendInfo;
import com.easychat.user.dto.VerifyInfo;
import com.easychat.user.service.FriendService;
import com.easychat.user.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-05-03 10:43
 */
@RestController
public class FriendController {

    @Autowired
    FriendService friendService;

    @GetMapping("/friends")
    public ResponseResult<List<FriendInfo>> getUserFriends(@RequestParam("id") String userId) {
        return friendService.queryUserFriends(userId);
    }

    @GetMapping("/friends/friend")
    public ResponseResult<FriendInfo> getFriendInfo(@RequestParam("id") String userId,
                                                    @RequestParam("friendId") String friendUserId) {
        return friendService.queryFriendInfo(userId, friendUserId);
    }

    @PostMapping("/friends/add")
    public ResponseResult<Object> addFriend(@RequestBody AddFriendRequest request) {
        return friendService.createFriend(request);
    }

    @PostMapping("/friends/edit")
    public ResponseResult<Object> editFriend(@RequestBody EditFriendRequest request) {
        return friendService.updateFriend(request);
    }

    @PostMapping("/friends/remove")
    public ResponseResult<Object> removeFriend(@RequestBody RemoveFriendRequest request) {
        return friendService.deleteFriend(request);
    }

    @GetMapping("/friendVerify")
    public ResponseResult<List<VerifyInfo>> getFriendVerifyList(@RequestParam("id") String userId) {
        return friendService.queryFriendVerifyList(userId);
    }

    @GetMapping("/friendVerify/info")
    public ResponseResult<VerifyInfo> getFriendVerify(@RequestParam("senderId") String senderId,
                                                      @RequestParam("receiverId") String receiverId) {
        return friendService.queryFriendVerify(senderId, receiverId);
    }

    @PostMapping("/friendVerify/add")
    public ResponseResult<Object> addFriendVerify(@RequestBody AddFriendVerifyRequest request) {
        return friendService.createFriendVerify(request);
    }

    @PostMapping("/friendVerify/edit")
    public ResponseResult<String> editFriendVerify(@RequestBody EditFriendVerifyRequest request) {
        return friendService.updateFriendVerify(request);
    }

    @PostMapping("/friendVerify/read")
    public ResponseResult<Object> readFriendVerify(@RequestBody String userId) {
        return friendService.updateFriendVerify(userId);
    }
}
