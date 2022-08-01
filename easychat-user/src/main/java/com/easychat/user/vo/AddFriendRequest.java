package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-20 12:33
 */
@Data
public class AddFriendRequest {

    private String userId;
    private String sessionId;
    private String sessionTime;
    private String friendUserId;
    private String friendRemark;
    private String createTime;

}
