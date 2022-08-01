package com.easychat.chat.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-03 15:14
 */
@Data
public class EditFriendRequest {

    private String userId;
    private String friendUserId;
    private String sessionId;
    private String sessionTime;
    private String remark;

}
