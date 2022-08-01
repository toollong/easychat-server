package com.easychat.chat.feign;

import com.easychat.chat.dto.FriendInfo;
import com.easychat.chat.dto.VerifyInfo;
import com.easychat.chat.vo.*;
import com.easychat.common.protocol.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: long
 * @Date: 2022-05-02 10:53
 */
@FeignClient("easychat-user-service")
public interface UserFeignService {

    @PostMapping("/user/user/changeStatus")
    ResponseResult<Object> changeStatus(@RequestBody ChangeStatusRequest request);

    @GetMapping("/user/friends/friend")
    ResponseResult<FriendInfo> getFriendInfo(@RequestParam("id") String userId,
                                             @RequestParam("friendId") String friendUserId);

    @PostMapping("/user/friends/add")
    ResponseResult<Object> addFriend(@RequestBody AddFriendRequest request);

    @PostMapping("/user/friends/edit")
    ResponseResult<Object> editFriend(@RequestBody EditFriendRequest request);

    @PostMapping("/user/friends/remove")
    ResponseResult<Object> removeFriend(@RequestBody RemoveFriendRequest request);

    @GetMapping("/user/friendVerify/info")
    ResponseResult<VerifyInfo> getFriendVerify(@RequestParam("senderId") String senderId,
                                               @RequestParam("receiverId") String receiverId);

    @PostMapping("/user/friendVerify/add")
    ResponseResult<Object> addFriendVerify(@RequestBody AddFriendVerifyRequest request);

    @PostMapping("/user/friendVerify/edit")
    ResponseResult<String> editFriendVerify(@RequestBody EditFriendVerifyRequest request);

    @PostMapping("/user/friendVerify/read")
    ResponseResult<Object> readFriendVerify(@RequestBody String userId);
}
