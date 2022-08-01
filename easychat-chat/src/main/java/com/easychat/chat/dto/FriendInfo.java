package com.easychat.chat.dto;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-21 14:09
 */
@Data
public class FriendInfo {

    private String userId;
    private String sessionId;
    private String sessionTime;
    private String friendUserId;
    private String friendRemark;
    private String friendNickName;
    private String friendAvatar;
    private String createTime;

}
